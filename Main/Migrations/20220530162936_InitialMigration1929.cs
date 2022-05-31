using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace AspWebApi.Migrations
{
    public partial class InitialMigration1929 : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "OfUser",
                table: "Contacts",
                type: "longtext",
                nullable: true)
                .Annotation("MySql:CharSet", "utf8mb4");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "OfUser",
                table: "Contacts");
        }
    }
}
