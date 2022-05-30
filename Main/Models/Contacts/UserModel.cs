using Models;

namespace AspWebApi.Models.Contacts {
    public class UserModel {
        public string Username { get; set; }

        public string Nickname { get; set; }

        public string Password { get; set; }

        public string ProfileImage { get; set; }

        public string Server { get; set; }

        public List<Chat> Chats { get; set; }
        public List<ContactModel> Contacts { get; set; }
        public UserModel()
        {
        }

        public UserModel(string username, string nickname, string password, string profileImage, string server)
        {
            Username = username;
            Nickname = nickname;
            Password = password;
            ProfileImage = profileImage;
            Server = server;
        }
    }
}
