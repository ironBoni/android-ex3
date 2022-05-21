namespace AspWebApi.Models {
    
    public class ContactRequest {
        public string Id { get; set; }
        public string Name { get; set; }
        public string Server { get; set; }

        public ContactRequest()
        {

        }

        public ContactRequest(string id, string name, string server)
        {
            this.Id = id;
            this.Name = name;
            this.Server = server;
        }
    }
}
