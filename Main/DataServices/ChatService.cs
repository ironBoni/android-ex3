using AspWebApi;
using Microsoft.EntityFrameworkCore;
using Models.DataServices.Interfaces;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models.DataServices {
    public class ChatService : IChatService {
        public ChatService()
        {
        }

        public bool Create(Chat entity)
        {
            try
            {
                using (var db = new ItemsContext())
                {
                    db.Chats.Add(entity);
                    db.SaveChanges();
                    return true;
                }
            } catch(Exception ex)
            {
                Debug.WriteLine(ex.Message);
                return false;
            }
        }

        public bool Delete(int Id)
        {
            try
            {
                using (var db = new ItemsContext())
                {
                    var toRemove = db.Chats.Include(x => x.Messages).Where(chat => chat.Id == Id).FirstOrDefault();
                    if (toRemove == null)
                        return false;
                    db.Chats.Remove(toRemove);
                    db.SaveChanges();
                    return true;
                }
            } catch(Exception ex)
            {
                Debug.WriteLine(ex.ToString());
                return false;
            }
        }

        public List<Chat> GetAll()
        {
            using (var db = new ItemsContext())
            {
                return db.Chats.Include(x => x.Messages).Include(x => x.Users).ToList();
            }
        }

        public Chat GetById(int Id)
        {
            using (var db = new ItemsContext())
            {
                return db.Chats.Include(x => x.Messages).Include(x => x.Users).Where(chat => chat.Id == Id).FirstOrDefault();
            }
        }

        public bool Update(Chat entity)
        {
            try
            {
                using (var db = new ItemsContext())
                {
                    var chat = db.Chats.Include(x => x.Messages).Include(x => x.Users).Where(chat => chat.Id == entity.Id).FirstOrDefault();
                    if (chat == null)
                        return false;
                    chat.Id = entity.Id;
                    db.Chats.Update(entity);
                    return true;
                }
            } catch(Exception ex)
            {
                Debug.WriteLine(ex.ToString());
                return false;
            }
        }

        public int GetNewMsgIdInChat(int id)
        {
            using (var db = new ItemsContext())
            {
                var chat = db.Chats.Include(x => x.Messages).Where(chat => chat.Id == id).FirstOrDefault();
                if (chat == null)
                    return 0;

                var chatMessages = chat.Messages;
                if (chatMessages == null || chatMessages.Count == 0)
                    return 1;
                var maxMessageId = chatMessages.Max(message => message.Id);
                return maxMessageId + 1;
            }
        }

        public void UpdateLastMessage(int chatId, Message message)
        {
            using (var db = new ItemsContext())
            {
                var chat = db.Chats.Include(x => x.Users).ToList().Find(x => x.Id == chatId);
                if (chat == null) return;
                var usersTalking = chat.Users;
                if (usersTalking == null) return;
                foreach (var user in usersTalking)
                {
                    var currentUser = db.Users.Include(x => x.Contacts).ToList().Find(x => x.Username == user.Username);
                    var otherUser = usersTalking.Find(x => x.Username != user.Username);
                    var contact = currentUser.Contacts.Find(x => x.ContactId == otherUser.Username);
                    contact.Last = message.Text;
                    contact.Lastdate = DateTime.Now;
                }
                db.SaveChanges();
            }
        }

        public bool AddMessage(int chatId, Message message)
        {
            try
            {
                using (var db = new ItemsContext())
                {
                    var chat = db.Chats.Include(x => x.Messages).Where(chat => chat.Id == chatId).FirstOrDefault();
                    if (chat == null) return false;
                    var chatMessages = chat.Messages;
                    chatMessages.Add(message);
                    UpdateLastMessage(chatId, message);
                    db.SaveChanges();
                    return true;
                }
            } catch(Exception ex)
            {
                Debug.WriteLine(ex.ToString());
                return true;
            }
        }

        public bool AddMessage(int chatId, int messageId, string content, string from, bool sent)
        {
            return AddMessage(chatId, new Message(messageId, content, from, sent));
        }
    }
}
