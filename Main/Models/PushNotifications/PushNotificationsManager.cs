namespace AspWebApi.Models.PushNotifications {
    public class PushNotificationsManager {
        public static Dictionary<string, string> IdToTokens = new Dictionary<string, string>();

        public static void addUser(string userId, string token)
        {
            IdToTokens[userId] = token;
        }
    }
}
