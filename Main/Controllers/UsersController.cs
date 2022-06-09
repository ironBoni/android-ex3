using AspWebApi.Models.Contacts;
using AspWebApi.Models.Users;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Models;
using Models.DataServices.Interfaces;
using Models.Models;
using System.Diagnostics;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace AspWebApi.Controllers {
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase {
        private readonly IUserService userService;

        public UsersController(IUserService userService)
        {
            this.userService = userService;
        }
        // GET: api/<UsersController>
        [HttpGet]
        [Route("/api/users")]
        public List<UserModel> Get()
        {
            return userService.GetAll().Select(x => new UserModel(x.Username, x.Nickname,
                x.Password, x.ProfileImage, x.Server)).ToList();
        }
        // POST api/<UsersController>
        [HttpPost]
        public void Post([FromBody] string value)
        {
        }

        [HttpGet]
        [Route("/api/users/{id}")]
        public UserModel Get(string id)
        {
            using (var db = new ItemsContext())
            {
                var user = db.Users.ToList().Find(user => user.Username == id);
                return new UserModel(user.Username, user.Nickname, user.Password, user.ProfileImage, user.Server);
            }
        }

        // PUT api/<UsersController>/5
        [HttpPut]
        public void Put([FromBody] ChangeServerRequest request)
        {
            Current.Username = User.Claims.SingleOrDefault(i => i.Type.EndsWith("UserId"))?.Value;
            using (var db = new ItemsContext())
            {
                var user = db.Users.ToList().Find(user => user.Username == Current.Username);
                user.Server = request.Server;
                try
                {
                    db.SaveChanges();
                    Response.StatusCode = 204;
                }
                catch (Exception ex)
                {
                    Debug.WriteLine(ex.Message);
                    Response.StatusCode = 404;
                }
            }
        }

        // DELETE api/<UsersController>/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
