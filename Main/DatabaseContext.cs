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
            MySqlCommand cmd = new MySqlCommand(@"SELECT MAX(@Column) FROM @Tbl");
            cmd.Parameters.AddWithValue("@Column", column);
            cmd.Parameters.AddWithValue("@Tbl", table);
            cmd.Connection = conn;
            var reader = cmd.ExecuteReader();
            return reader.GetInt32(column);
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
            MySqlCommand insert = new MySqlCommand("INSERT INTO @Table (Id) VALUES (@Value);");
            foreach (var user in c.Users)
            {
                var maxCurrentId = GetMaxFromTable("chatuser", "ChatsId", conn);
                var newId = maxCurrentId + 1;
                insert = new MySqlCommand("INSERT INTO @Table (@Column1, @Column2) VALUES (@Value1, @Value2);");
                insert.Parameters.AddWithValue("@Table", "chatuser");
                insert.Parameters.AddWithValue("@Column1", "ChatsId");
                insert.Parameters.AddWithValue("@Column2", "UsersUsername");
                insert.Parameters.AddWithValue("@Value1", newId);
                insert.Parameters.AddWithValue("@Value2", user.Username);
                insert.Connection = conn;
                insert.ExecuteNonQuery();
            }
        }

        public List<User> GetParticipants(int chatId)
        {
            List<string> participants = new List<string>();
            List<User> users = new List<User>();
            using(var conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                MySqlCommand cmd = new MySqlCommand(@"SELECT UsersUsername FROM chatuser WHERE ChatsId=@Id");
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
        public List<Contact> Contacts { get; }
        public List<Chat> Chats { get; }
        public List<Message> Messages { get; }

    }
}
