using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models.DataServices.Interfaces {
    public interface IUserService : IDataService<User, string>, IContactService {
        string GetFullServerUrl(string url);
        Chat GetChatByParticipants(User user1, User user2);
        Chat GetChatByParticipants(string user1, string user2);
        List<Message> GetAllMessages(string username, string other);
    }
}
