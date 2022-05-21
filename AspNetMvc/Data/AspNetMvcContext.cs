using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using AspNetMvc.Models;

namespace AspNetMvc.Data
{
    public class AspNetMvcContext : DbContext
    {
        public AspNetMvcContext (DbContextOptions<AspNetMvcContext> options)
            : base(options)
        {
        }

       // public DbSet<AspNetMvc.Models.Rating> Rating { get; set; }
    }
}
