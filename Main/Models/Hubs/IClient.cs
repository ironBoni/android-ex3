using AspWebApi.Models.Contacts;
using AspWebApi.Models.Transfer;

namespace AspWebApi.Models.Hubs {
    public interface IClient {
        Task ReceiveMessage(MessageResponse message);
    }
}
