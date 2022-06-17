using AspWebApi.Models.Contacts;
using Microsoft.AspNetCore.SignalR;
using Models;
using Models.DataServices;
using Models.DataServices.Interfaces;
using System.Collections.Concurrent;

namespace AspWebApi.Models.Hubs {
    public class ChatHub : Hub {
        private readonly IUserService userService;
        private readonly IChatService chatService;
        public static ConcurrentDictionary<string, List<string>> userToConnection = new ConcurrentDictionary<string, List<string>>();
        private const string ReceiveMessage = "ReceiveMessage";
        private const string ReceiveContact = "ReceiveContact";
        public ChatHub(IUserService userServ, IChatService chatServ)
        {
            userService = userServ;
            chatService = chatServ;
        }

        // id = noam
        // idToAdd = yuval
        // notify yuval than noam is new contact
        public async Task AddContact(string id, string idToAdd, string nickname, string server)
        {
            if (!userToConnection.ContainsKey(idToAdd)) return;
            var connectionIds = userToConnection[idToAdd];
            // the user of the sender/ adder
            var user = userService.GetById(id);
            if (user == null) return;

            // the nickname and server of the adder
            var newNick = user.Nickname;
            var newServer = user.Server;

            foreach (var conId in connectionIds)
                await Clients.Client(conId).SendAsync(ReceiveContact, new ContactRequest(id, newNick, newServer));
        }
        public async Task SendMsg(string from, string content, string to)
        {
            //var userId = CurrentUsers.GetIdByToken(token);
            var chat = userService.GetChatByParticipants(from, to);
            if(chat == null) return;
            var id = chatService.GetNewMsgIdInChat(chat.Id);

            if (userToConnection.ContainsKey(to))
            {
                var connectionIds = userToConnection[to];
                foreach (var conId in connectionIds)
                    await Clients.Client(conId).SendAsync(ReceiveMessage, new MessageResponse(id, content, DateTime.Now, true, from, chat.Id));
            }
            else
                await Clients.AllExcept(Context.ConnectionId)
                .SendAsync(ReceiveMessage, new MessageResponse(id, content, DateTime.Now, true, from, chat.Id));
        }

        public async Task SetIdInServer(string username)
        {
            if (!userToConnection.ContainsKey(username))
                userToConnection[username] = new List<string>();

            userToConnection[username].Add(Context.ConnectionId);
            await Clients.Client(Context.ConnectionId).SendAsync("Ok");
        }
    }
}