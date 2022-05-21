namespace AspWebApi.Models.Contacts {
    [Serializable]
    public class MessageResponse {
        public int Id { get; set; }
        public string Content { get; set; }
        public DateTime? Created { get; set; }
        public bool Sent { get; set; }

        public string Type { get; set; }
        public string SenderUsername { get; set; }
        
        public string FileName { get; set; }
        public MessageResponse(int id, string content, DateTime? created, bool sent, string senderUsername)
        {
            Id = id;
            Content = content;
            Created = created;
            Sent = sent;
            Type = "text";
            SenderUsername = senderUsername;
            FileName = "";
        }

        public MessageResponse()
        {
        }
    }
}
