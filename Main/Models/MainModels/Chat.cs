using AspWebApi;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models {
    public class Chat {
        [Key]
        //[ForeignKey("ChatsId")]
        public virtual int Id { get; set; }

        [Required(ErrorMessage = "Please enter the participants.")]
        public virtual List<User> Users { get; set; }

        [Required(ErrorMessage = "Please enter the messages.")]
        public virtual List<Message> Messages { get; set; }
        public Chat()
        {   
        }

        public Chat(int id, List<User> participants, List<Message> messages)
        {
            Id = id;
            Users = participants;
            Messages = messages;
        }

        public Chat(List<User> participants, List<Message> messages)
        {
            Id = GetNewId();
            Users = participants;
            Messages = messages;
        }

        public Chat(List<User> participants)
            : this(participants, new List<Message>())
        {
        }

        public static int GetNewId()
        {
            using(var db = new ItemsContext())
            {
                var chats = db.Chats.ToList();
                return chats.Max(x => x.Id) + 1;
            }
        }
    }
}
