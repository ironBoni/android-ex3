using Models;
using Models.Models;

namespace AspWebApi.DataServices {
    public class DatabaseLoader {
        private static void LoadParticipantsToChats(ItemsContext db)
        {
            foreach (var chat in db.Chats.ToList())
            {
                if (chat.Participants != null && chat.Participants.Count >= 2) continue;
                chat.Participants = new List<User>();
                foreach (var u in db.Users.ToList())
                {
                    if (chat.Messages.Any(m => m.SenderUsername == u.Username)
                        && !chat.Participants.Contains(u))
                    {
                        chat.Participants.Remove(u);
                        db.SaveChanges();

                        chat.Participants.Add(u);
                        db.SaveChanges();
                    }
                }
            }
        }

        private static void LoadMessagesToChat(ItemsContext db)
        {
            foreach (var chat in db.Chats.ToList())
            {
                if (chat.Messages != null) continue;
                chat.Messages = new List<Message>();
                foreach (var m in db.Messages.ToList().OrderBy(m => m.Id))
                {
                    if (chat.Participants.Any(u => u.Username == m.SenderUsername))
                    {
                        var user = db.Users.Where(u => u.Username == m.SenderUsername).FirstOrDefault();
                        if (!chat.Participants.Contains(user))
                        {
                            chat.Participants.Remove(user);
                            db.SaveChanges();
                            chat.Participants.Add(user);
                            db.SaveChanges();
                        }
                    }
                }
            }
        }
        private static void LoadContactsToUsers(ItemsContext db)
        {
            foreach (var user in db.Users.ToList())
            {
                if (user.Contacts != null) continue;
                user.Contacts = new List<Contact>();
                foreach (var c in db.Contacts.ToList())
                {
                    if (c.OfUser == user.Username)
                    {
                        user.Contacts.Remove(c);
                        db.SaveChanges();
                        user.Contacts.Add(new Contact(c.Name, c.Server, c.Last, c.Lastdate, c.ProfileImage, c.ContactId, c.OfUser));
                    }
                }
            }
            db.SaveChanges();
        }
        public static void Load()
        {
            using (var db = new ItemsContext())
            {
                LoadContactsToUsers(db);
                LoadParticipantsToChats(db);
                LoadMessagesToChat(db);
                db.SaveChanges();
            }
        }
    }
}

