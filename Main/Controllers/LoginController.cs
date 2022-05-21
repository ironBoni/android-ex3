using AspWebApi.Models;
using AspWebApi.Models.Login;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using Models;
using Models.DataServices;
using Models.DataServices.Interfaces;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace AspWebApi.Controllers {
    [Route("api/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase {
        private readonly IUserService serivce;
        private readonly IConfiguration configuration;

        public LoginController(IConfiguration configuration)
        {
            serivce = new UserService();
            this.configuration = configuration;
        }
        
        // GET api/<LoginController>/5
        [HttpGet("{id}")]
        public TokenResponse Get(string id)
        {
            if (!CurrentUsers.IdToContactsDict.ContainsKey(id)) return null;
            return new TokenResponse(CurrentUsers.IdToTokenDict[id]);
        }

        private TokenResponse CreateToken(User user)
        {
            if(CurrentUsers.IdToTokenDict.ContainsKey(user.Username))
                return new TokenResponse(CurrentUsers.IdToTokenDict[user.Username]);   
            //create claims details based on the user information
            var claims = new[] {
                        new Claim(JwtRegisteredClaimNames.Sub, configuration["Jwt:Subject"]),
                        new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
                        new Claim(JwtRegisteredClaimNames.Iat, DateTime.UtcNow.ToString()),
                        new Claim("UserId", user.Username.ToString())
                    };

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(configuration["Jwt:Key"]));
            var signIn = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
            var token = new JwtSecurityToken(
                configuration["Jwt:Issuer"],
                configuration["Jwt:Audience"],
                claims,
                expires: DateTime.UtcNow.AddDays(1),
                signingCredentials: signIn);
            var tokenKey = new JwtSecurityTokenHandler().WriteToken(token);
            CurrentUsers.IdToTokenDict[user.Username] = tokenKey;
            return new TokenResponse(tokenKey);
        }

        // POST api/<LoginController>
        [HttpPost]
        public IActionResult Post([FromBody] LoginRequest req)
        {
            var user = serivce.GetById(req.Username);
            if (user == null) return Ok(new LoginResponse("User doesn't exist.", false));
            var isCorrect = user.Password == req.Password;
            if (!isCorrect) return Ok(new LoginResponse("Username and password does not match.", false, user.Password, CreateToken(user)));

            return Ok(new LoginResponse("Good Login", true, CreateToken(user)));
        }
    }
}
