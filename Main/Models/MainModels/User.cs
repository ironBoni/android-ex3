﻿using Models.Models;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models {
    public class User {
        [Key]
        //[ForeignKey("UsersUsername")]
        public virtual string Username { get; set; }

        [Required(ErrorMessage ="Please enter nickname.")]
        public string Nickname { get; set; }
    
        [Required(ErrorMessage ="Please enter password")]
        public string Password { get; set; }

        public string ProfileImage { get; set; }

        [Required(ErrorMessage ="Please enter server")]
        public string Server { get; set; }

        public virtual List<Chat> Chats { get; set; }
        public virtual List<Contact> Contacts { get; set; }
        public User()
        {
        }

        public User(string username, string nickname, string password, string profileImage, string server)
        {
            Username = username;
            Nickname = nickname;
            Password = password;
            ProfileImage = profileImage;
            Server = server;
            Contacts = new List<Contact>();
        }

        public User(string username, string nickname, string password, string profileImage)
            :this(username, nickname, password, profileImage, "http://localhost:5186/")    
        {
        }
    }
}
