using Models.DataServices;
using Models.DataServices.Interfaces;
using Models.Models;

namespace AspWebApi.Models {
    public class CurrentUsers {
        // user to token
        public static Dictionary<string, string> IdToTokenDict = new Dictionary<string, string>();
        public static Dictionary<string, List<Contact>> IdToContactsDict = new Dictionary<string, List<Contact>>();
        private IChatService chatService;
        private IUserService userService;
        
        public CurrentUsers(IUserService userServ, IChatService chatServ)
        {
            userService = userServ;
            chatService = chatServ;
        }
        /*public static List<Contact> SetContactsForUser(string username)
        {
            var users = (new UserService()).GetAll();
            var currentUser = users.Find(user => user.Username == username);
            currentUser.Contacts = new List<Contact>();
            var contactsByMessages = new List<Contact>();
            var chats = chatService.GetAll();
            var chatsWithHim = chats.Where(chat => chat.Participants.Select(x => x.Username).Any(u => u == username));

            var friends = chatsWithHim.Select(chat => chat.Participants.Find(participant => participant.Username != username));

            foreach (var fUser in friends)
            {
                var chat = chatService.GetChatByParticipants(currentUser, fUser);
                var lastTime = chat.Messages.Max(message => message.WrittenIn);
                var lastMsg = chat.Messages.Find(message => message.WrittenIn == lastTime);
                var contact = new Contact(fUser.Nickname, fUser.Server, lastMsg.Text, lastTime, fUser.ProfileImage, fUser.Username);
                contactsByMessages.Add(contact);
            }

            foreach (var contact in contactsByMessages)
            {
                if (!currentUser.Contacts.Any(c => c.Id == contact.ContactId))
                    currentUser.Contacts.Add(contact);
            }

            if (IdToContactsDict.ContainsKey(currentUser.Username))
            {
                var oldContacts = IdToContactsDict[currentUser.Username];
                if (oldContacts != null)
                {
                    var currentIds = currentUser.Contacts.Select(c => c.ContactId).ToList();
                    var newToAdd = oldContacts.Where(c => !currentIds.Contains(c.ContactId));
                    currentUser.Contacts.AddRange(newToAdd);
                }
            }
            IdToContactsDict[username] = currentUser.Contacts;
            return currentUser.Contacts;
        }*/

        /*public static void SetContacts()
        {
            var chatsService = new ChatService();
            var users = (new UserService()).GetAll();
            foreach (var username in users.Select(x => x.Username))
                SetContactsForUser(username);
        }*/

        public static string GetIdByToken(string token)
        {
            if (!IdToTokenDict.ContainsValue(token)) return String.Empty;
            var item = IdToTokenDict.FirstOrDefault(item => item.Value == token);
            return item.Key;
        }
    }
}
