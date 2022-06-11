using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models.DataServices.Interfaces {
    public interface IChatService : IDataService<Chat, int> {
        int GetNewMsgIdInChat(int id);
        bool AddMessage(int chatId, Message message);
        bool AddMessage(int id, int messageId, string content, string from, bool sent);
    }
}
