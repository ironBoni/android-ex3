using AspWebApi.Models.Register;
using Microsoft.AspNetCore.Mvc;
using Models;
using Models.DataServices;
using Models.DataServices.Interfaces;
using Models.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace AspWebApi.Controllers {
    [Route("api/[controller]")]
    [ApiController]
    public class RegisterController : ControllerBase {
        private IUserService service;
        public RegisterController()
        {
            service = new UserService();
        }
        // GET: api/<RegisterController>
        [HttpGet]
        public IActionResult Get()
        {
            var users = service.GetAll();
            if(users == null) return NotFound();
            return Ok(new UsersList(users.Select(user => user.Username).ToList()));
        }

        // POST api/<RegisterController>
        [Route("/api/Register")]
        [HttpPost]
        public IActionResult Post([FromBody] RegisterRequest req)
        {
            bool isUserExist = service.GetById(req.Id) != null;
            var success = !isUserExist && service.Create(new User(req.Id, req.Name, req.Password, req.ProfileImage, req.Server));

            if (!success) return BadRequest("User already exist.");
            return Ok();
        }
    }
}
