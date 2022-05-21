using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models {
    public class Message {
        [Key]
        public int Id { get; set; }

        [Required]
        [MaxLength(10)]
        public string Type { get; set; }

        [Required(ErrorMessage = "Please enter the message text")]
        public string Text { get; set; }

        [Required(ErrorMessage = "Please enter the sender username")]
        public string SenderUsername { get; set; }

        [Required(ErrorMessage = "Please enter when the message was written in")]
        public DateTime WrittenIn { get; set; }

        public string FileName { get; set; }
        public bool Sent { get; set; }
        public Message(int id, string type, string text, string senderUsername, DateTime writtenIn, string fileName)
        {
            Id = id;
            Type = type;
            Text = text;
            SenderUsername = senderUsername;
            WrittenIn = writtenIn;
            FileName = fileName;
            Sent = false;
        }

        public Message(int id, string type, string text, string senderUsername, DateTime writtenIn, string fileName, bool sent)
        {
            Id = id;
            Type = type;
            Text = text;
            SenderUsername = senderUsername;
            WrittenIn = writtenIn;
            FileName = fileName;
            Sent = sent;
        }

        public Message(int id, string text, string senderUsername)
            : this(id, "text", text, senderUsername, DateTime.Now)
        {
        }

        public Message(int id, string text, string senderUsername, bool sent)
            : this(id, "text", text, senderUsername, DateTime.Now)
        {
            Sent = sent;
        }

        public Message(int id, string type, string text, string senderUsername, DateTime writtenIn)
            : this(id, type, text, senderUsername, writtenIn, "") { }


        public Message(int id, string type, string text, string senderUsername, DateTime writtenIn, bool sent)
            : this(id, type, text, senderUsername, writtenIn, "") { Sent = sent; }
    }
}
