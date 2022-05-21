namespace AspWebApi.Models.Login {
    [Serializable]
    public class LoginRequest {
        public string Username { get; set; }
        public string Password { get; set; }
    }
}
