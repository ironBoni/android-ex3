using Models.DataServices;
using Models.Models;

namespace AspWebApi.Models {
    public class CurrentUsers {
        // user to token
        public static Dictionary<string, string> IdToTokenDict = new Dictionary<string, string>();
        public static Dictionary<string, List<Contact>> IdToContactsDict = new Dictionary<string, List<Contact>>();
        public static List<Contact> SetContactsForUser(string username)
        {
            var chatsService = new ChatService();
            var users = (new UserService()).GetAll();
            var currentUser = users.Find(user => user.Username == username);
            currentUser.Contacts = new List<Contact>();
            var contactsByMessages = new List<Contact>();
            var chats = chatsService.GetAll();
            var chatsWithHim = chats.Where(chat => chat.Participants.Contains(username));

            var friends = chatsWithHim.Select(chat => chat.Participants.Find(participant => participant != username));

            foreach (var fUsername in friends)
            {
                var fUser = users.Find(user => user.Username == fUsername);
                var chat = chats.Find(chat => chat.Participants.Contains(username) &&
                                                       chat.Participants.Contains(fUsername));
                var lastTime = chat.Messages.Max(message => message.WrittenIn);
                var lastMsg = chat.Messages.Find(message => message.WrittenIn == lastTime);
                var contact = new Contact(fUsername, fUser.Nickname, fUser.Server, lastMsg.Text, lastTime, fUser.ProfileImage);
                contactsByMessages.Add(contact);
            }

            foreach (var contact in contactsByMessages)
            {
                if (!currentUser.Contacts.Any(c => c.Id == contact.Id))
                    currentUser.Contacts.Add(contact);
            }

            if (IdToContactsDict.ContainsKey(currentUser.Username))
            {
                var oldContacts = IdToContactsDict[currentUser.Username];
                if (oldContacts != null)
                {
                    var currentIds = currentUser.Contacts.Select(c => c.Id).ToList();
                    var newToAdd = oldContacts.Where(c => !currentIds.Contains(c.Id));
                    currentUser.Contacts.AddRange(newToAdd);
                }
            }
            IdToContactsDict[username] = currentUser.Contacts;
            return currentUser.Contacts;
        }

        public static void SetContacts()
        {
            var chatsService = new ChatService();
            var users = (new UserService()).GetAll();
            foreach (var username in users.Select(x => x.Username))
                SetContactsForUser(username);
        }

        public static string GetIdByToken(string token)
        {
            if (!IdToTokenDict.ContainsValue(token)) return String.Empty;
            var item = IdToTokenDict.FirstOrDefault(item => item.Value == token);
            return item.Key;
        }
    }
}
