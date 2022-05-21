using System.ComponentModel.DataAnnotations;

namespace AspNetMvc.Models
{
    public class Rating
    {
        [Required]
        public int ID { get; set; }
        [Required]
        [Range(1, 5, ErrorMessage = "Rate must be between 1 to 5!")]
        public int Rate { get; set; }
        [Required]
        [MaxLength(80, ErrorMessage = "Exceeded max length! 80 chars requried")]
        public string Name { get; set; }
        [MaxLength(200, ErrorMessage = "Exceeded max length! 200 chars requried")]
        public string Feedback { get; set; }
        public DateTime DateTime { get; set; }
        public Rating(int id, int rate, string name, string feedback)
        {
            ID = id;
            Rate = rate;
            Name = name;   
            Feedback = feedback;
            DateTime = DateTime.Now;
        }

        public Rating()
        {
            DateTime = DateTime.Now;
        }
    }
}
