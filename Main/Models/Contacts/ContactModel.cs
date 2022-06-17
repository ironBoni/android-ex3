namespace AspWebApi.Models.Contacts {
    public class ContactModel {
        public string Id { get; set; }
        public string ContactId { get; set; }

        public string Name { get; set; }

        public string Server { get; set; }

        public string Last { get; set; }

        public string Lastdate { get; set; }
        public string OfUser { get; set; }
        public string LastdateStr { get; set; }

        public string ProfileImage { get; set; }

        public ContactModel()
        {
        }

        public ContactModel(string id, string contactId, string name, string server, string last, string lastdate, string profileImage, string ofUser)
        {
            Id = id;
            ContactId = contactId;
            Name = name;
            Server = server;
            Last = last;
            Lastdate = lastdate;
            ProfileImage = profileImage;
            OfUser = ofUser;
            DateTime? dateTime;
            if (lastdate == null || lastdate == "")
            {
                dateTime = null;
                LastdateStr = null;
                return;
            }
            else
            {
                dateTime = Convert.ToDateTime(lastdate);
                LastdateStr = dateTime.Value.ToString("dd/MM/yyyy HH:mm");
            }
        }
    }
}
