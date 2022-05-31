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
        //private static List<Chat> chats = new List<Chat>() { };
        public ChatService()
        {
        }
         /*   new Chat(1, new List<string>{ "ron", "noam" }, new List<Message>
            {
                new Message(1, "text", "my name is Noam.", "noam", new DateTime(2021, 4, 6, 9, 56, 0), true),
                new Message(2, "text", "my name is Ron.", "ron", new DateTime(2021, 4, 6, 10, 5, 0), true),
                new Message(3, "text", "Nice to meet you!", "ron", new DateTime(2021, 4, 8, 10, 30, 0), true)
            }),

            new Chat(2, new List<string>{ "ron", "dvir" }, new List<Message>
            {
                new Message(1, "text", "Hey Dvir, how are you?", "ron", new DateTime(2021, 4, 6, 10, 10, 0), true),
                new Message(2, "text", "I'm good. Thanks Ron!", "dvir", new DateTime(2021, 4, 6, 10, 20, 0), true),
                new Message(3, "text", "I'm glad to hear that :)", "ron", new DateTime(2021, 4, 8, 12, 30, 0), true)
            }),

            new Chat(4, new List<string>{ "ron", "dan" }, new List<Message>
            {
                new Message(1, "text", "Dan, let's go to the mall", "ron", new DateTime(2021, 4, 6, 10, 12, 0), true),
                new Message(2, "text", "Ok bro. Nice idea Ron", "dan", new DateTime(2021, 4, 6, 10, 20, 0), true),
                new Message(3, "text", "I appreciate that :)", "ron", new DateTime(2021, 4, 8, 12, 30, 0), true)
            }),
            new Chat(5, new List<string>{ "ron", "idan" }, new List<Message>
            {
                new Message(1, "text", "Idan, how was your semester?", "ron", new DateTime(2021, 4, 5, 10, 21, 0), true),
                new Message(2, "text", "It was good. Thanks Ron.", "idan", new DateTime(2021, 4, 5, 10, 22, 0), true),
                new Message(3, "text", "I'm glad to hear that :)", "ron", new DateTime(2021, 4, 8, 12, 30, 0), true)
            }),
            new Chat(6, new List<string>{ "ron", "hadar" }, new List<Message>
            {
                new Message(1, "text", "Hadar, let's go to eat pizza", "ron", new DateTime(2021, 4, 6, 10, 40, 0), true),
                new Message(2, "text", "Fine Ron, nice idea", "hadar", new DateTime(2021, 4, 6, 10, 52, 0), true),
                new Message(3, "text", "I appreciate that :)", "ron", new DateTime(2021, 4, 8, 12, 30, 0), true)
            }),
            new Chat(7, new List<string>{ "noam", "dvir" }, new List<Message>
            {
                new Message(1, "text","Hey Dvir, how are you?", "noam", new DateTime(2021, 4, 6, 12, 5, 0), true),
                new Message(2, "text", "I'm great. Thanks Noam!", "dvir", new DateTime(2021, 4, 6, 12, 7, 0), true),
                new Message(3, "text", "I Love it Noam! It's a very nice song! :)", "dvir", new DateTime(2021, 4, 8, 12, 30, 0), true)
            }),
            new Chat(8, new List<string>{ "noam", "dan" }, new List<Message>
            {
                new Message(1, "text","Hey Dan, how are you?", "noam", new DateTime(2021, 4, 6, 12, 5, 0), true),
                new Message(2, "text", "I'm great. Thanks!", "dan", new DateTime(2021, 4, 6, 12, 7, 0), true),
                new Message(3, "text", "I'm glad to hear so! :)", "noam", new DateTime(2021, 4, 8, 12, 30, 0), true)
            }),
            new Chat(9, new List<string>{ "noam", "idan" }, new List<Message>
            {
                new Message(1, "text","Hey, how are you?", "noam", new DateTime(2021, 4, 6, 12, 5, 0), true),
                new Message(2, "text", "I'm great. Thanks!", "dan", new DateTime(2021, 4, 6, 12, 7, 0), true),
                new Message(3, "text", "I'm happy to hear that! :)", "noam", new DateTime(2021, 4, 8, 12, 30, 0), true)
            }),
            new Chat(10, new List<string>{ "noam", "hadar" }, new List<Message>
            {
                new Message(1, "text","Hadar, let's go eat pizza", "noam", new DateTime(2021, 4, 6, 12, 5, 0), true),
                new Message(2, "text", "Fine Noam, Nice idea", "hadar", new DateTime(2021, 4, 6, 12, 7, 0), true),
                new Message(3, "text", "I think so too :)", "noam", new DateTime(2021, 4, 8, 12, 30, 0), true)
            }),
            new Chat(11, new List<string>{ "dan", "dvir" }, new List<Message>
            {
                new Message(1, "text","Hey Dvir, how are you?", "dan", new DateTime(2021, 4, 6, 12, 5, 0), true),
                new Message(2, "text", "I'm great Dan. Thanks!", "dvir", new DateTime(2021, 4, 6, 12, 7, 0), true),
                new Message(3, "text", "Always smile! :)", "dan", new DateTime(2021, 4, 8, 12, 30, 0), true)
            }),
            new Chat(12, new List<string>{ "dan", "idan" }, new List<Message>
            {
                new Message(1, "text","Idan, let's go the mall", "dan", new DateTime(2021, 4, 6, 12, 5, 0), true),
                new Message(2, "text", "Ok bro. Nice idea Dan", "idan", new DateTime(2021, 4, 6, 12, 7, 0), true),
                new Message(3, "text", "I'm glad you're coming! :)", "dan", new DateTime(2021, 4, 8, 12, 30, 0), true)
            }),
            new Chat(13, new List<string>{ "dan", "hadar" }, new List<Message>
            {
                new Message(1, "text","Hadar, let's go to eat pizza", "dan", new DateTime(2021, 4, 6, 12, 5, 0), true),
                new Message(2, "text", "Fine Dan. Nice idea!", "hadar", new DateTime(2021, 4, 6, 12, 7, 0), true),
                new Message(3, "text", "I'm glad you agreed!", "dan", new DateTime(2021, 4, 8, 12, 30, 0), true)
            }),
            new Chat(14, new List<string>{ "idan", "hadar" }, new List<Message>
            {
                new Message(1, "text","Hey, how are you?", "idan", new DateTime(2021, 4, 6, 12, 5, 0), true),
                new Message(2, "text", "I'm great. Thanks!", "hadar", new DateTime(2021, 4, 6, 12, 7, 0), true),
                new Message(2, "text", "Have a nice day bro!", "idan", new DateTime(2021, 4, 8, 12, 30, 0), true)
            }),
            new Chat(15, new List<string>{ "hadar", "dvir" }, new List<Message>
            {
                new Message(1, "text","Hey, how are you?", "hadar", new DateTime(2021, 4, 6, 12, 5, 0), true),
                new Message(2, "text", "I'm great. Thanks!", "dvir", new DateTime(2021, 4, 6, 12, 7, 0), true),
                new Message(2, "text", "Always smile! :)", "hadar", new DateTime(2021, 4, 8, 12, 30, 0), true)
            })
        };*/

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
                    db.SaveChanges();
                    return true;
                }
            } catch(Exception ex)
            {
                Debug.WriteLine(ex.ToString());
                return false;
            }
        }
    }
}
