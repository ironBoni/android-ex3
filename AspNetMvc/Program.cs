using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using AspNetMvc.Data;
var builder = WebApplication.CreateBuilder(args);


builder.Services.AddDbContext<AspNetMvcContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("AspNetMvcContext") ?? throw new InvalidOperationException("Connection string 'AspNetMvcContext' not found.")));

// Add services to the container.
builder.Services.AddControllersWithViews();
builder.Services.AddSignalR();
var app = builder.Build();


// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
}
app.UseStaticFiles();

app.UseRouting();
app.UseAuthorization();
app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Ratings}/{action=Index}/{id?}");

app.Run();
