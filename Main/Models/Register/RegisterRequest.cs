namespace AspWebApi.Models.Register {
    [Serializable]
    public class RegisterRequest {
        public string Id { get; set; }
        public string Name { get; set; }

        public string Password { get; set; }

        public string ProfileImage { get; set; }

        public string Server { get; set; }

    }
}
