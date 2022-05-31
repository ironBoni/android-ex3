using AspWebApi;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models.Models {
    public class Contact {
        private const int begin = 100000, end = 1000000;
        [Required(ErrorMessage ="Please enter Id")]
        [Key]
        public string Id { get; set; }
        [Required(ErrorMessage = "Please enter contact Id")]
        public string ContactId { get; set; }

        [Required(ErrorMessage ="Please enter name")]
        public string Name { get; set; }

        [Required(ErrorMessage ="Please enter server")]
        public string Server { get; set; }

        public string Last { get; set; }

        public DateTime? Lastdate { get; set; }
        [ForeignKey("Username")]
        public string Username { get; set; }
        public string ProfileImage { get; set; }
        public string OfUser { get; set; }
        public User MappedUser { get; set; }
        public Contact()
        {
        }

        public static string GetNewId()
        {
            return Guid.NewGuid().ToString();
        }
        public Contact(string name, string server, string last, DateTime? lastDate, string contactId, string ofUser)
        {
            
            Id = GetNewId();
            Name = name;
            Server = server;
            Last = last;
            Lastdate = lastDate;
            ContactId = contactId;
            OfUser = ofUser;
            Username = ofUser;
        }

        public Contact(string name, string server, string last, DateTime? lastDate, string profileImage, string contactId, string ofUser)
        : this(name, server, last, lastDate, contactId, ofUser){
            
            ProfileImage = profileImage;
        }
    }
}
