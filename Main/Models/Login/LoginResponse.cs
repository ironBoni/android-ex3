namespace AspWebApi.Models.Login {
    public class LoginResponse {
        public string Response { get; set; }
        public bool IsCorrectInput { get; set; }
        public TokenResponse Token { get; set; }
        public string CorrectPass { get; set; }
        public LoginResponse(string response, bool isCorrectInput, string correctPass, TokenResponse tokenRes)
        {
            Response = response;
            IsCorrectInput = isCorrectInput;
            Token = tokenRes;
            CorrectPass = correctPass;
        }

        public LoginResponse(string response, bool isCorrectInput, TokenResponse tokenRes)
        {
            Response = response;
            IsCorrectInput = isCorrectInput;
            Token = tokenRes;
        }

        public LoginResponse(string response, bool isCorrectInput)
        {
            Response = response;
            IsCorrectInput = isCorrectInput;
            CorrectPass = string.Empty;
        }
    }
}
