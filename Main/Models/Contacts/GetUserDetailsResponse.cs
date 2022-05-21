namespace AspWebApi.Models.Contacts {
    public class GetUserDetailsResponse {
        public string Server { get; set; }
        public string Name { get; set; }
        public string ProfileImage { get; set; }
        public GetUserDetailsResponse()
        {
        }

        public GetUserDetailsResponse(string server, string name)
        {
            Server = server;
            Name = name;
            ProfileImage = "/images/default.jpg";
        }

        public GetUserDetailsResponse(string server, string name, string profileImage)
        {
            Server = server;
            Name = name;
            ProfileImage = profileImage;
        }
    }
}
