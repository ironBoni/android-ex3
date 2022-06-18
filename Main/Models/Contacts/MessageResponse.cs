using Models.DataServices;

namespace AspWebApi.Models.Contacts {
    [Serializable]
    public class MessageResponse {
        public int Id { get; set; }
        public string Content { get; set; }
        public string Created { get; set; }
        public string CreatedDateStr { get; set; }
       
        public bool Sent { get; set; }

        public string Type { get; set; }
        public string Receiver { get; set; }
        public string SenderUsername { get; set; }

        public int ChatId { get; set; }

        public string FileName { get; set; }
        public MessageResponse(int id, string content, DateTime? createdDate, bool sent, string senderUsername, int chatId, string receiver)
        {
            Id = id;
            Content = content;
            if (createdDate != null)
            {
                CreatedDateStr = UserService.getDateString(createdDate.Value);
                Created = DateTime.Now.ToString("yyyy-MM-ddTHH:mm:ss.fffffff");
            }
            else
            {
                CreatedDateStr = string.Empty;
                Created = null;
            }

            Sent = sent;
            Type = "text";
            SenderUsername = senderUsername;
            FileName = "";
            ChatId = chatId;
            Receiver = receiver;
        }

        public MessageResponse()
        {
        }
    }
}
