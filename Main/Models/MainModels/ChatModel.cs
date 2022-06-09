using Models;
using System.ComponentModel.DataAnnotations;

namespace AspWebApi.Models.MainModels {
    public class ChatModel {
        private static int id = 16;
        public int Id { get; set; }

        [Required(ErrorMessage = "Please enter the participants.")]
        public List<string> Participants { get; set; }

        [Required(ErrorMessage = "Please enter the messages.")]
        public List<MessageModel> Messages { get; set; }

        public ChatModel(int id, List<string> participants, List<MessageModel> messages)
        {
            Id = id;
            Participants = participants;
            Messages = messages;
        }

        public ChatModel(List<string> participants, List<MessageModel> messages)
        {
            Id = id;
            id++;
            Participants = participants;
            Messages = messages;
        }

        public ChatModel(List<string> participants)
            : this(participants, new List<MessageModel>())
        {
        }
    }
}
