using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models.Models {
    public class Contact {
        [Required(ErrorMessage ="Please enter contact Id")]
        public string Id { get; set; }

        [Required(ErrorMessage ="Please enter name")]
        public string Name { get; set; }

        [Required(ErrorMessage ="Please enter server")]
        public string Server { get; set; }

        [Required(ErrorMessage ="Please enter last")]
        public string Last { get; set; }

        [Required(ErrorMessage ="Please enter date")]
        public DateTime? Lastdate { get; set; }

        public string ProfileImage { get; set; }
        public Contact(string id, string name, string server, string last, DateTime? lastDate)
        {
            Id = id;
            Name = name;
            Server = server;
            Last = last;
            Lastdate = lastDate;
        }

        public Contact(string id, string name, string server, string last, DateTime? lastDate, string profileImage)
            : this(id, name, server, last, lastDate)
        {
            ProfileImage = profileImage;
        }
    }
}
