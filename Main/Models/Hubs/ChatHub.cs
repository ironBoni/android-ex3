using AspWebApi.Models.Contacts;
using Microsoft.AspNetCore.SignalR;
using Models;
using Models.DataServices;
using Models.DataServices.Interfaces;
using System.Collections.Concurrent;

namespace AspWebApi.Models.Hubs {
    public class ChatHub : Hub {
        private readonly IChatService chatService;
        private static ConcurrentDictionary<string, List<string>> userToConnection = new ConcurrentDictionary<string, List<string>>();
        private const string ReceiveMessage = "ReceiveMessage";
        public ChatHub()
        {
            chatService = new ChatService();
        }
        public async Task SendMsg(string from, string content, string to)
        {
            //var userId = CurrentUsers.GetIdByToken(token);
            var chat = chatService.GetChatByParticipants(from, to);
            var id = chatService.GetNewMsgIdInChat(chat.Id);

            if (userToConnection.ContainsKey(to)) {
                var connectionIds = userToConnection[to];
                foreach(var conId in connectionIds)
                    await Clients.Client(conId).SendAsync(ReceiveMessage, new MessageResponse(id, content, DateTime.Now, true, from));
            }
            else
                await Clients.AllExcept(Context.ConnectionId)
                .SendAsync(ReceiveMessage, new MessageResponse(id, content, DateTime.Now, true, from));
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
