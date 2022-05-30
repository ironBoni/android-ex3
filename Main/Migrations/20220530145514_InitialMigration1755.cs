using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace AspWebApi.Migrations
{
    public partial class InitialMigration1755 : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "ContactId",
                table: "Contacts",
                type: "longtext",
                nullable: false)
                .Annotation("MySql:CharSet", "utf8mb4");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "ContactId",
                table: "Contacts");
        }
    }
}
