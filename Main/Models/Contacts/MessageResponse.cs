using Models.DataServices;

namespace AspWebApi.Models.Contacts {
    [Serializable]
    public class MessageResponse {
        public int Id { get; set; }
        public string Content { get; set; }
        public DateTime? Created { get; set; }
        public string CreatedDateStr { get; set; }
        public bool Sent { get; set; }

        public string Type { get; set; }
        public string SenderUsername { get; set; }
        
        public string FileName { get; set; }
        public MessageResponse(int id, string content, DateTime? createdDate, bool sent, string senderUsername)
        {
            Id = id;
            Content = content;
            Created = createdDate;
            if (createdDate != null)
            {
                CreatedDateStr = UserService.getDateString(createdDate.Value);
                Created = createdDate.Value;
            } else
            {
                CreatedDateStr = string.Empty;
                Created = null;
            }
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
