using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models {
    public class Chat {
        private static int id = 16;
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
            Id = id;
            id++;
            Users = participants;
            Messages = messages;
        }

        public Chat(List<User> participants)
            : this(participants, new List<Message>())
        {
        }
    }
}
