using Microsoft.Data.SqlClient;
using Models;
using Models.Models;
using MySqlConnector;
using System.Data;

namespace AspWebApi {
    public class DatabaseContext {
        private const string connectionString = "server=localhost;port=3306;database=pomelodb;user=root;password=Np1239";

        private List<T> GetList<T>(IDataReader reader)
        {
            List<T> list = new List<T>();
            while(reader.Read())
            {
                var type = typeof(T);
                T obj = (T)Activator.CreateInstance(type);
                foreach(var prop in type.GetProperties())
                {
                    var propType = prop.PropertyType;
                    prop.SetValue(obj, Convert.ChangeType(reader[prop.Name].ToString(), propType));
                }
                list.Add(obj);
            }
            return list;
        }

        public int GetMaxFromTable(string table, string column)
        {
            using (var conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                MySqlCommand cmd = new MySqlCommand(@"SELECT MAX(" + column + ") FROM " + table + ";");
                cmd.Connection = conn;
                var reader = cmd.ExecuteReader();
                reader.Read();
                var maxValue = reader.GetInt32(0);
                return maxValue;
            }
        }

        public void AddChat(Chat c)
        {
            using (var conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                var newId = InsertToChatTable(c.Id);
                InsertToChatUserTable(c, newId);
            }
        }

        private int InsertToChatTable(int id)
        {
            using (var conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                var insert = new MySqlCommand("INSERT INTO chats (Id) VALUES (@id);");
                var maxCurrentId = GetMaxFromTable("chats", "Id");
                var newId = maxCurrentId + 1;
                insert.Parameters.AddWithValue("@id", newId);
                insert.Connection = conn;
                insert.ExecuteNonQuery();
                return newId;
            }
        }

        private void InsertToChatUserTable(Chat c, int newId)
        {
            using (var conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                foreach (var user in c.Users)
                {
                    var maxCurrentId = GetMaxFromTable("chatuser", "ChatsId");
                    var insert = new MySqlCommand("INSERT INTO chatuser (ChatsId, UsersUsername) VALUES (@Value1, @Value2);");

                    insert.Parameters.AddWithValue("@Value1", newId);
                    insert.Parameters.AddWithValue("@Value2", user.Username);
                    insert.Connection = conn;
                    insert.ExecuteNonQuery();
                }
            }
        }

        public List<Message> GetMessages(int chatId)
        {
            List<Message> users = new List<Message>();
            using (var conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                MySqlCommand cmd = new MySqlCommand(@"SELECT * FROM messages WHERE ChatId=@ChtId;");
                cmd.Parameters.AddWithValue("@ChtId", chatId);
                cmd.Connection = conn;
                var reader = cmd.ExecuteReader();
                var messages = GetList<Message>(reader);
                return messages;
            }
        }

        public List<Contact> GetContacts(string userId)
        {
            using (var conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                MySqlCommand cmd = new MySqlCommand(@"SELECT * FROM contacts WHERE OfUser=@userId;");
                cmd.Parameters.AddWithValue("@userId", userId);
                cmd.Connection = conn;
                var reader = cmd.ExecuteReader();
                var contacts = GetList<Contact>(reader);
                return contacts;
            }
        }
        public List<User> GetParticipants(int chatId)
        {
            List<string> participants = new List<string>();
            List<User> users = new List<User>();
            using(var conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                MySqlCommand cmd = new MySqlCommand(@"SELECT UsersUsername FROM chatuser WHERE ChatsId=@Id;");
                cmd.Connection = conn;
                cmd.Parameters.AddWithValue("@Id", chatId);
                var reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    var participant = reader.GetString(0);
                    participants.Add(participant);
                }
                
            }
            using(var db = new ItemsContext())
            {
                users = db.Users.Where(u => participants.Contains(u.Username)).ToList();
            }
            if (users == null)
                return new List<User>();
            return users;
        }

        public void AddContact(Contact c)
        {
            using (var conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                var newId = Guid.NewGuid().ToString();
                var insert = new MySqlCommand(@"INSERT INTO contacts (Id, Name, Server, Last, 
                Lastdate, ProfileImage, Username, ContactId, OfUser) VALUES(@id, @name, 
                @server, @last, @lastdate, @profileImage, @username, @contactId, @ofUser);");
                insert.Parameters.AddWithValue("@id", newId);
                insert.Parameters.AddWithValue("@name", c.Name);
                insert.Parameters.AddWithValue("@server", c.Server);
                insert.Parameters.AddWithValue("@last", c.Last);
                insert.Parameters.AddWithValue("@lastdate", c.Lastdate);
                insert.Parameters.AddWithValue("@profileImage", c.ProfileImage);
                insert.Parameters.AddWithValue("@username", c.OfUser);
                insert.Parameters.AddWithValue("@contactId", c.ContactId);
                insert.Parameters.AddWithValue("@ofUser", c.OfUser);
                insert.Connection = conn;
                insert.ExecuteNonQuery();
            }
        }

        public void AddMessage(Message message, int chatId)
        {
            using (var conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                var maxId = GetMaxFromTable("messages", "Id");
                var newId = maxId + 1;
                var insert = new MySqlCommand("INSERT INTO messages (Id, Type, Text, Username, WrittenIn, Sent, ChatId) VALUES (@id, @type, @text, @username, @writtenIn, @sent, @chatId);");
                insert.Parameters.AddWithValue("@id", newId);
                insert.Parameters.AddWithValue("@type", message.Type);
                insert.Parameters.AddWithValue("@text", message.Text);
                insert.Parameters.AddWithValue("@username", message.Username);
                insert.Parameters.AddWithValue("@writtenIn", message.WrittenIn);
                insert.Parameters.AddWithValue("@sent", message.Sent);
                insert.Parameters.AddWithValue("@chatId", chatId);
                insert.Connection = conn;
                insert.ExecuteNonQuery();
            }

        }
    }
}
