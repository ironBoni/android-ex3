using Models;

namespace AspWebApi.Models.Contacts {
    public class UserModel {
        public string Id { get; set; }

        public string Name { get; set; }

        public string Password { get; set; }

        public string ProfileImage { get; set; }

        public string Server { get; set; }

        public List<ContactModel> Contacts { get; set; }
        public UserModel()
        {
        }

        public UserModel(string username, string nickname, string password, string profileImage, string server)
        {
            Id = username;
            Name = nickname;
            Password = password;
            ProfileImage = profileImage;
            Server = server;
        }
    }
}
