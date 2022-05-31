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

            modelBuilder.Entity<User>().HasKey(e => e.Username);
            modelBuilder.Entity<User>().HasMany<Chat>(e => e.Chats)
                .WithMany(c => c.Users);
            modelBuilder.Entity<User>().HasMany<Contact>(e => e.Contacts)
                .WithOne(c => c.MappedUser).HasForeignKey(e => e.Username);

            modelBuilder.Entity<Contact>().HasKey(e => e.Id);
            modelBuilder.Entity<Contact>().HasOne<User>(e => e.MappedUser)
                .WithMany(u => u.Contacts).HasForeignKey(e => e.Username);
             
                
            modelBuilder.Entity<Chat>().HasKey(e => e.Id);
            modelBuilder.Entity<Chat>().HasMany<User>(e => e.Users)
                .WithMany(u => u.Chats);
            modelBuilder.Entity<Chat>().HasMany<Message>(e => e.Messages)
                .WithOne(e => e.MappedChat).HasForeignKey(e => e.ChatId);

            modelBuilder.Entity<Message>().HasKey(e => e.Id);
            modelBuilder.Entity<Message>().HasOne<User>(e => e.User);
        }

        public ItemsContext()
        {
        }
        public ItemsContext(DbContextOptions<ItemsContext> options)
            :base(options)
        {
        }

        public DbSet<User> Users { get; set; }
        public DbSet<Contact> Contacts { get; set; }
        public DbSet<Chat> Chats { get; set; }
        public DbSet<Message> Messages { get; set; }
    }
}
