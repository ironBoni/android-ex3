using Models.DataServices;
using Models.DataServices.Interfaces;
using Models.Models;

namespace AspWebApi.Models {
    public class CurrentUsers {
        // user to token
        public static Dictionary<string, string> IdToTokenDict = new Dictionary<string, string>();
        public static Dictionary<string, List<Contact>> IdToContactsDict = new Dictionary<string, List<Contact>>();
        private IChatService chatService;
        private IUserService userService;
        
        public CurrentUsers(IUserService userServ, IChatService chatServ)
        {
            userService = userServ;
            chatService = chatServ;
        }
        
        public static string GetIdByToken(string token)
        {
            if (!IdToTokenDict.ContainsValue(token)) return String.Empty;
            var item = IdToTokenDict.FirstOrDefault(item => item.Value == token);
            return item.Key;
        }
    }
}
