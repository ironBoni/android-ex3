using AspWebApi;
using AspWebApi.Models;
using AspWebApi.Models.Hubs;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using Models.DataServices;
using Microsoft.EntityFrameworkCore;
using Models.DataServices.Interfaces;
using System.Text;

var builder = WebApplication.CreateBuilder(args);
// Add services to the container.

builder.Services.AddControllers();
builder.Services.AddSignalR();
builder.Services.AddScoped<IChatService, ChatService>();
builder.Services.AddScoped<IUserService, UserService>();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme).AddJwtBearer(options =>
{
    options.RequireHttpsMetadata = false;
    options.SaveToken = true;
    options.TokenValidationParameters = new TokenValidationParameters()
    {
        ValidateIssuer = true,
        ValidateAudience = true,
        ValidAudience = builder.Configuration["Jwt:Audience"],
        ValidIssuer = builder.Configuration["Jwt:Issuer"],
        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(builder.Configuration["Jwt:Key"]))
    };
});

//builder.Services.AddDbContext<ItemsContext>(
 //    options => options.UseSqlServer("name=server=localhost;port=3306;database=pomelodb;user=root;password=Np1239:DefaultConnection"));

builder.Services.AddCors(p => p.AddPolicy("cors", builder =>
{
    /*     builder.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader();*/
    /* builder.AllowAnyHeader()
                 .AllowAnyMethod()
                 .WithOrigins("http://localhost:3000")
                 .AllowCredentials();*/
    builder.AllowAnyHeader().AllowAnyMethod().SetIsOriginAllowed(a => true).AllowCredentials();
}));

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseCors("cors");
app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();
app.MapHub<ChatHub>("/hub");
//CurrentUsers.SetContacts();
app.Run();
