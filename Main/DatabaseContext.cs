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

        public int GetMaxFromTable(string table, string column, MySqlConnection conn)
        {
            MySqlCommand cmd = new MySqlCommand(@"SELECT MAX("+column+") FROM "+table+";");
            cmd.Connection = conn;
            var reader = cmd.ExecuteReader();
            reader.Read();
            var maxValue = reader.GetInt32(0);
            return maxValue;
        }

        public void AddChat(Chat c)
        {

            using (var conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                //InsertToChatTable();
                InsertToChatUserTable(c, conn);
                using(var db = new ItemsContext())
                {
                    db.Chats.Add(c);
                    db.SaveChanges();
                }
                //InsertToContactsTable(c);
            }
        }

        private void InsertToContactsTable(Chat c, MySqlConnection conn)
        {
            var maxId = GetMaxFromTable("contacts", "Id", conn);
            var newId = maxId + 1;
            MySqlCommand insert = new MySqlCommand("INSERT INTO @Tbl (@Column1, @Column2, @Column3, @Column4, @Column5, @Column6, @Column7, @Column8, @Column9) " +
                "VALUES (@Value1, @Value2, @Value3', @Value4, @Value5, @Value6, 'dan', 'noam', 'dan');");
            insert.Parameters.AddWithValue("@Table", "chats");
            insert.Parameters.AddWithValue("@Value", newId);
            insert.Connection = conn;
            insert.ExecuteNonQuery();
        }

        private void InsertToChatTable(MySqlConnection conn)
        {
            var maxCurrentId = GetMaxFromTable("chats", "Id", conn);
            var newId = maxCurrentId + 1;
            MySqlCommand insert = new MySqlCommand("INSERT INTO @Table (Id) VALUES (@Value);");
            insert.Parameters.AddWithValue("@Table", "chats");
            insert.Parameters.AddWithValue("@Value", newId);
            insert.Connection = conn;
            insert.ExecuteNonQuery();
        }

        private void InsertToChatUserTable(Chat c, MySqlConnection conn)
        {
            foreach (var user in c.Users)
            {
                var maxCurrentId = GetMaxFromTable("chatuser", "ChatsId", conn);
                var newId = maxCurrentId + 1;
                var insert = new MySqlCommand("INSERT INTO chatuser (ChatsId, UsersUsername) VALUES (@Value1, @Value2);");

                insert.Parameters.AddWithValue("@Value1", newId);
                insert.Parameters.AddWithValue("@Value2", user.Username);
                insert.Connection = conn;
                insert.ExecuteNonQuery();
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

        public void AddChatMessage(Message message, int chatId)
        {
            using (var conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                var maxId = GetMaxFromTable("messages", "Id", conn);
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
