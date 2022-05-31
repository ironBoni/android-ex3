﻿using AspWebApi.Models.Hubs;
using AspWebApi.Models.Transfer;
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
        public IActionResult Post([Bind("From,To,Content")] TransferRequest request)
        {
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
                if (success) { return Ok(); }
                else return BadRequest();
            }

            var messageId = service.GetNewMsgIdInChat(chat.Id);
            // the sent is false because it was not sent from my server
            var addSuccess = service.AddMessage(chat.Id, new Message(messageId, request.Content, request.From, false));
            if (!addSuccess) return BadRequest();
            return Ok();
        }
    }
}
