using Microsoft.AspNetCore.SignalR;

namespace SignalRDemo.Hubs
{
    public class MyHub: Hub
    {
        public async Task Changed(string value)
        {
            // activate the method changeReceved to all clients
            await Clients.All.SendAsync("ChangeReceived", value);
        }
    }
}
