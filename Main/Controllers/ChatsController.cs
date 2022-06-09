using AspWebApi.Models.MainModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Models;
using Models.DataServices.Interfaces;
using System.Globalization;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace AspWebApi.Controllers {
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class ChatsController : ControllerBase {
        private readonly IChatService chatService;

        public ChatsController(IChatService chatService)
        {
            this.chatService = chatService;
        }
        // GET: api/<ChatsController>
        [Route("/api/chats")]
        [HttpGet]
        public IActionResult Get()
        {
            return Ok(chatService.GetAll().Select(chat => new ChatModel(chat.Id, chat.Users.Select(x => x.Username).ToList(),
                chat.Messages.Select(m => new MessageModel(m.Id, "text", m.Text, m.Username, m.WrittenIn,"", m.Sent)).ToList())));
        }

        // GET api/<ChatsController>/5
        [HttpGet("{id}")]
        public string Get(int id)
        {
            return "value";
        }

        // POST api/<ChatsController>
        [HttpPost]
        public void Post([FromBody] string value)
        {
        }

        // PUT api/<ChatsController>/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE api/<ChatsController>/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
