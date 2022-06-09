namespace AspWebApi.Models.Contacts {
    public class ContactModel {
        public string Id { get; set; }
        public string ContactId { get; set; }

        public string Name { get; set; }

        public string Server { get; set; }

        public string Last { get; set; }

        public String Lastdate { get; set; }

        public string ProfileImage { get; set; }

        public ContactModel()
        {
        }

        public ContactModel(string id, string contactId, string name, string server, string last, string lastdate, string profileImage)
        {
            Id = id;
            ContactId = contactId;
            Name = name;
            Server = server;
            Last = last;
            Lastdate = lastdate;
            ProfileImage = profileImage;
        }
    }
}
