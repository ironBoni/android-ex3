﻿using AspWebApi;
using AspWebApi.Models;
using AspWebApi.Models.Contacts;
using Microsoft.EntityFrameworkCore;
using Models.DataServices.Interfaces;
using Models.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models.DataServices {
    // The key is string
    public class UserService : IUserService {
        private readonly IChatService chatsService;
        public UserService()
        {
            this.chatsService = new ChatService();
        }
        /*private static List<User> users = new List<User>() {
            new User("noam", "Noam Cohen", "Np1234", "/profile/noam.jpg"),
            new User("hadar", "Hadar Pinto", "Np1234", "/profile/hadar.jpg"),
            new User("dvir", "Dvir Pollak", "Np1234", "/profile/dvir.jpg"),
            new User("ron", "Ron Solomon", "Np1234", "/profile/ron.jpg"),
            new User("dan", "Dan Cohen", "Np1234", "/profile/dan.jpg"),
            new User("idan", "Idan Ben Ari", "Np1234", "/profile/idan.jpg"),
            new User("shlomo", "Shlomo Levin", "Np1234", "/profile/shlomo.png"),
            new User("yaniv", "Yaniv Hoffman", "Np1234", "/profile/yaniv.png"),
            new User("oren", "Oren Orbach", "Np1234", "/profile/oren.webp"),
            new User("yuval", "Yuval Baruchi", "Np1234", "/profile/yuval.png"),
            new User("ran", "Ran Levi", "Np1234", "/profile/ran.webp"),
        };*/


        public List<ContactModel> GetContacts(string username)
        {
            var contacts = new List<ContactModel>();
            using (var db = new ItemsContext())
            {
                var user = db.Users.Where(user => user.Username == username).FirstOrDefault();
                if (user == null) return new List<ContactModel>();

                contacts = db.Contacts.Where(contact => contact.OfUser == username)
                    .Select(c => new ContactModel(c.ContactId, c.Name, c.Server, c.Last, c.Lastdate, c.ProfileImage)).ToList();
                db.SaveChanges();
                return contacts;
            }
        }

        public Chat GetChatByParticipants(User user1, User user2)
        {
            using (var db = new ItemsContext())
            {
                foreach (var chat in db.Chats)
                {
                    var participants = chat.Participants.Select(x => x.Username).ToList();
                    if (participants.Contains(user1.Username) && participants.Contains(user2.Username))
                        return chat;
                }
                return null;
            }
        }

        public List<Message> GetAllMessages(User user1, User user2)
        {
            var chat = GetChatByParticipants(user1, user2);
            if (chat == null) return null;
            return chat.Messages;
        }


        public List<Message> GetAllMessages(string username, string other)
        {
            var chat = GetChatByParticipants(username, other);
            if (chat == null) return null;
            return chat.Messages;
        }

        public Chat GetChatByParticipants(string username1, string username2)
        {
            var user1 = GetById(username1);
            var user2 = GetById(username2);
            return GetChatByParticipants(user1, user2);
        }

        public bool AddContact(string friendToAdd, string name, string server, out string response)
        {
            using (var db = new ItemsContext())
            {
                var username = Current.Username;
                var currentUserObj = db.Users.Where(user => user.Username == Current.Username).FirstOrDefault();
                var contacts = GetContacts(currentUserObj.Username);
                var currentUser = new UserModel(currentUserObj.Username, currentUserObj.Nickname, currentUserObj.Password,
                    currentUserObj.ProfileImage, currentUserObj.Server);

                var currentContacts = GetContacts(username);

                if (currentContacts == null)
                {
                    response = "There are not contacts.";
                    return false;
                }

                // You cannot add yourself to the chat list.
                if (username == friendToAdd)
                {
                    response = "You cannot add yourself to the chat list";
                    return false;
                }

                // You cannot add someone that is already in your chats.
                if (currentContacts.Any(user => user.Id == friendToAdd))
                {
                    response = "You cannot add him, because he's already in your chat list.";
                    return false;
                }

                User friend;
                friend = db.Users.Where(user => user.Username == friendToAdd).FirstOrDefault();
                // then add it
                var newChat = new Chat(new List<User>() {
                currentUserObj, friend});

                if (friend == null)
                {
                    response = "The user doesn't exist in the system.";
                    return false;
                }

                friend.Server = server;
                

                var newContact = new Contact(friendToAdd, name, server, null, null, friend.ProfileImage);
                var newContactModel = new ContactModel(friendToAdd, name, server, null, null, friend.ProfileImage);
                if (!currentContacts.Contains(newContactModel))
                {
                    db.Contacts.Add(newContact);
                    CurrentUsers.IdToContactsDict[currentUser.Username] = db.Contacts.Where(contact => contact.OfUser == username).ToList();;
                }
                db.SaveChanges();

                response = "";
                return chatsService.Create(newChat);
            }
        }

        public bool Create(User entity)
        {
            using (var db = new ItemsContext())
            {
                db.Users.Add(entity);
                db.SaveChanges();
            }
            return true;
        }

        public bool Delete(string username)
        {
            using (var db = new ItemsContext())
            {
                var toRemove = db.Users.Where(user => user.Username == username).FirstOrDefault();
                if (toRemove == null)
                    return false;
                db.Users.Remove(toRemove);
                db.SaveChanges();
                return true;
            }
        }

        public List<User> GetAll()
        {
            using (var db = new ItemsContext())
            {
                return db.Users.ToList();
            }
        }

        public User GetById(string username)
        {
            using (var db = new ItemsContext())
            {
                return db.Users.Where(user => user.Username == username).FirstOrDefault();
            }
        }

        public bool Update(User entity)
        {
            using (var db = new ItemsContext())
            {
                var user = db.Users.Where(user => user.Username == entity.Username).FirstOrDefault();
                if (user == null)
                    return false;
                user.Username = entity.Username;
                user.Password = entity.Password;
                user.ProfileImage = entity.ProfileImage;
                db.SaveChanges();
                return true;
            }
        }

        public bool RemoveContact(string userToRemove, out string res)
        {
            using (var db = new ItemsContext())
            {
                var username = Current.Username;
                var currentUser = db.Users.Where(user => user.Username == Current.Username).FirstOrDefault();
                var currentContacts = GetContacts(username);
                res = "";
                if (currentContacts == null)
                {
                    res = "Current Contacts not set.";
                    return false;
                }

                if (userToRemove == Current.Username)
                {
                    res = "You cannot add yourself to the chat list.";
                    return false;
                }

                if (!currentContacts.Any(user => user.Id == userToRemove))
                {
                    res = "You cannot add someone that is already in your chats.";
                    return false;
                }

                var contactToRemove = db.Contacts.Where(c => c.ContactId == userToRemove).FirstOrDefault();
                var userObjectToRemove = db.Users.Where(c => c.Username == userToRemove).FirstOrDefault();
                if (contactToRemove == null)
                {
                    res = "You cannot remove it. It's not one of your contacts.";
                    return false;
                }

                var hisContacts = db.Contacts.Where(contact => contact.OfUser == username).ToList();
                hisContacts.Remove(contactToRemove);
                CurrentUsers.IdToContactsDict[currentUser.Username] = hisContacts;

                var chatToRemove = GetChatByParticipants(userObjectToRemove, currentUser);

                if (chatToRemove == null)
                {
                    res = "There is no chat with such participants.";
                    return false;
                }

                db.SaveChanges();
                return chatsService.Delete(chatToRemove.Id);
            }
        }

        public bool AcceptInvitation(string from, string server, string to, out string response)
        {
            using (var db = new ItemsContext())
            {
                var userToAdd = db.Users.Where(u => u.Username == from).FirstOrDefault();
                string name = "";
                if (userToAdd == null)
                {
                    response = "no such user";
                    return false;
                }
                //var username = Current.Username;
                var currentUser = db.Users.Where(user => user.Username == to).FirstOrDefault();
                if (currentUser == null)
                {
                    response = "such user doesn't exists";
                    return false;
                }
                var currentContacts = GetContacts(to);

                if (currentContacts == null)
                {
                    response = "There are not contacts.";
                    return false;
                }

                // You cannot add someone that is already in your chats.
                if (currentContacts.Any(user => user.Id == from))
                {
                    response = "User cannot be added, because he's already in your chat list.";
                    return false;
                }

                var requestor = db.Users.Where(user => user.Username == from).FirstOrDefault();

                var newChat = new Chat(new List<User>() {
                currentUser, requestor});

                // then add it
                if (requestor == null)
                {
                    response = "The user doesn't exist in the system.";
                    return false;
                }

                if (requestor.Nickname == null || requestor.Nickname == "")
                    name = requestor.Username;
                else
                    name = requestor.Nickname;
                requestor.Server = server;

                var newContact = new ContactModel(from, name, server, null, null, requestor.ProfileImage);
                if (!currentContacts.Contains(newContact))
                {
                    currentContacts.Add(newContact);
                    CurrentUsers.IdToContactsDict[currentUser.Username] = db.Contacts.Where(contact => contact.OfUser == currentUser.Username).ToList();
                }

                response = "";

                var isExistsChat = GetChatByParticipants(requestor, currentUser) != null;
                if (!isExistsChat)
                    return true;
                db.SaveChanges();
                return chatsService.Create(newChat);
            }
        }

        public string GetFullServerUrl(string url)
        {
            if (!url.EndsWith("/"))
                url = url + "/";
            if (!url.StartsWith("http://"))
                url = "http://" + url;
            return url;
        }
    }
}