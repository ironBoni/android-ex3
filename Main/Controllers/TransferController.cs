using AspWebApi.Models.Hubs;
using AspWebApi.Models.PushNotifications;
using AspWebApi.Models.Transfer;
using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Models;
using Models.DataServices;
using Models.DataServices.Interfaces;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace AspWebApi.Controllers {
    [Route("api/[controller]")]
    [ApiController]
    public class TransferController : ControllerBase {
        private IChatService service;
        private IUserService userService;

        public TransferController(IUserService userServ, IChatService serv)
        {
            service = serv;
            this.userService = userServ;

        }

        // POST api/<TransferController>
        [HttpPost]
        [Route("/api/transfer")]
        public async Task<IActionResult> Post([Bind("From,To,Content")] TransferRequest request)
        {
            string responseFromFirebase;
            var fromUser = userService.GetById(request.From);
            var toUser = userService.GetById(request.To);
            Chat chat = userService.GetChatByParticipants(fromUser, toUser);
            if (chat == null)
            {
                chat = new Chat(new List<User>()
                {
                    fromUser,
                    toUser
                });
                var success = service.Create(chat);
                if (success)
                {
                    responseFromFirebase = await SendNotification(request);
                    return Ok();
                }
                else return BadRequest();
            }

            var messageId = service.GetNewMsgIdInChat(chat.Id);
            // the sent is false because it was not sent from my server

            var addSuccess = service.AddMessage(chat.Id, messageId, request.Content, request.From, false);
            if (!addSuccess) return BadRequest();
            responseFromFirebase = await SendNotification(request);
            return Ok();
        }

        private async Task<string> SendNotification(TransferRequest request)
        {
            FirebaseApp.Create(new
                AppOptions
            {
                Credential = GoogleCredential.FromFile("privateKey.json")
            });

            var registrationToken = PushNotificationsManager.IdToTokens[request.To];

            var message = new FirebaseAdmin.Messaging.Message()
            {
                Data = new Dictionary<string, string>()
                {
                    { "From", request.From },
                    { "Conent", request.Content }
                },
                Token = registrationToken,
                Notification = new Notification()
                {
                    Title = "New message: from " + request.From,
                    Body = request.Content
                }
            };

            // Send a message to the device corresponding to the provided
            // registration token.
            string response = await FirebaseMessaging.DefaultInstance.SendAsync(message);
            return response;
        }
    }
}
