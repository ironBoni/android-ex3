using Microsoft.EntityFrameworkCore;
using Models;
using Models.Models;

namespace AspWebApi {
    public class ItemsContext : DbContext {
        private const string connectionString = "server=localhost;port=3306;database=pomelodb;user=root;password=Np1239";

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseMySql(connectionString, MariaDbServerVersion.AutoDetect(connectionString));
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Configuring the Name property as the primary
            // key of the Items table
            modelBuilder.Entity<Contact>().HasKey(e => e.Id);
            modelBuilder.Entity<Chat>().HasKey(e => e.Id);
            modelBuilder.Entity<Message>().HasKey(e => e.Id);
            modelBuilder.Entity<User>().HasKey(e => e.Username);
        }

        public DbSet<User> Users { get; set; }
        public DbSet<Contact> Contacts { get; set; }
        public DbSet<Chat> Chats { get; set; }
        public DbSet<Message> Messages { get; set; }
    }
}
