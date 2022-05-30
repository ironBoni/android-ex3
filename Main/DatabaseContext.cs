using Microsoft.Data.SqlClient;
using Models;
using Models.Models;
using MySqlConnector;
using System.Data;
using System.Diagnostics;

namespace AspWebApi {
    public class DatabaseContext {
        private const string connectionString = "server=localhost;port=3306;database=pomelodb;user=root;password=Np1239";

        private List<T> GetList<T>(IDataReader reader)
        {
            try
            {
                List<T> list = new List<T>();
                while (reader.Read())
                {
                    var type = typeof(T);
                    T obj = (T)Activator.CreateInstance(type);
                    foreach (var prop in type.GetProperties())
                    {
                        var propType = prop.PropertyType;
                        prop.SetValue(obj, Convert.ChangeType(reader[prop.Name].ToString(), propType));
                    }
                    list.Add(obj);
                }
                return list;
            }
            catch(Exception ex)
            {
                Debug.Write(ex.ToString());
                return null;
            }
        }

        public int GetMaxFromTable(string table, string column)
        {
            try
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
            } catch(Exception ex)
            {
                Debug.Write(ex.ToString());
                var random = new Random();
                return random.Next(10000, 1000000);
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
            try
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
            } catch(Exception ex) {
                Debug.Write(ex.ToString());
                var random = new Random();
                return random.Next(10000, 1000000);
            }
        }

        private void InsertToChatUserTable(Chat c, int newId)
        {
            try
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
            } catch(Exception ex)
            {
                Debug.Write(ex.ToString());
            }
        }

        public List<Message> GetMessages(int chatId)
        {
            try
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
            } catch(Exception ex)
            {
                Debug.Write(ex.ToString());
                return new List<Message>();
            }
        }

        public List<Contact> GetContacts(string userId)
        {
            try
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
            catch(Exception ex)
            {
                Debug.Write(ex.ToString());
                return new List<Contact>();
            }
        }
        public List<User> GetParticipants(int chatId)
        {
            try
            {
                List<string> participants = new List<string>();
                List<User> users = new List<User>();
                using (var conn = new MySqlConnection(connectionString))
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
                using (var db = new ItemsContext())
                {
                    users = db.Users.Where(u => participants.Contains(u.Username)).ToList();
                }
                if (users == null)
                    return new List<User>();
                return users;
            } catch(Exception ex)
            {
                Debug.Write(ex.ToString());
                return new List<User>();
            }
        }

        public void AddContact(Contact c)
        {
            try
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
            } catch(Exception ex)
            {
                Debug.Write(ex.ToString());
            }
        }

        public void UpdateChat(Chat entity)
        {
            //TODO implement
        }

        public void RemoveChat(Chat toRemove)
        {
            //TODO implement
        }

        public void AddMessage(Message message, int chatId)
        {
            try
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
            catch(Exception ex)
            {
                Debug.Write(ex.ToString());
            }
        }
    }
}
