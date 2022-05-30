﻿using Microsoft.Data.SqlClient;
using Models;
using Models.Models;
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
            SqlCommand cmd = new SqlCommand(@"SELECT MAX(@Column) FROM @Tbl");
            cmd.Parameters.AddWithValue("@Column", column);
            cmd.Parameters.AddWithValue("@Tbl", table);
            var reader = cmd.ExecuteReader();
            return reader.GetInt32(column);
        }

        public void AddChat(Chat c)
        {

            using (var conn = new SqlConnection(connectionString))
            {
                conn.Open();
                InsertToChatTable();
                InsertToChatUserTable(c);

            }
        }

        private void InsertToChatTable()
        {
            var maxCurrentId = GetMaxFromTable("chats", "Id");
            var newId = maxCurrentId + 1;
            SqlCommand insert = new SqlCommand("INSERT INTO @Table (Id) VALUES (@Value);");
            insert.Parameters.AddWithValue("@Table", "chats");
            insert.Parameters.AddWithValue("@Value", newId);
            insert.ExecuteNonQuery();
        }

        private void InsertToChatUserTable(Chat c)
        {
            SqlCommand insert = new SqlCommand("INSERT INTO @Table (Id) VALUES (@Value);");
            foreach (var user in c.Users)
            {
                var maxCurrentId = GetMaxFromTable("chatuser", "ChatsId");
                var newId = maxCurrentId + 1;
                insert = new SqlCommand("INSERT INTO @Table (@Column1, @Column2) VALUES (@Value1, @Value2);");
                insert.Parameters.AddWithValue("@Table", "chatuser");
                insert.Parameters.AddWithValue("@Column1", "ChatsId");
                insert.Parameters.AddWithValue("@Column2", "UsersUsername");
                insert.Parameters.AddWithValue("@Value1", newId);
                insert.Parameters.AddWithValue("@Value2", user.Username);
                insert.ExecuteNonQuery();
            }
        }

        public List<User> GetParticipants(int chatId)
        {
            List<string> participants = new List<string>();
            List<User> users = new List<User>();
            using(var conn = new SqlConnection(connectionString))
            {
                conn.Open();
                SqlCommand cmd = new SqlCommand(@"SELECT UsersUsername FROM chatuser WHERE ChatsId=@Id");
                cmd.Parameters.AddWithValue("@Id", chatId);
                var reader = cmd.ExecuteReader();
                participants = GetList<string>(reader);
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
