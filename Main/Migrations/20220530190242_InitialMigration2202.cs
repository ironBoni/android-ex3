using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace AspWebApi.Migrations
{
    public partial class InitialMigration2202 : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_ChatUser_Users_ParticipantsUsername",
                table: "ChatUser");

            migrationBuilder.RenameColumn(
                name: "ParticipantsUsername",
                table: "ChatUser",
                newName: "UsersUsername");

            migrationBuilder.RenameIndex(
                name: "IX_ChatUser_ParticipantsUsername",
                table: "ChatUser",
                newName: "IX_ChatUser_UsersUsername");

            migrationBuilder.AddForeignKey(
                name: "FK_ChatUser_Users_UsersUsername",
                table: "ChatUser",
                column: "UsersUsername",
                principalTable: "Users",
                principalColumn: "Username",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_ChatUser_Users_UsersUsername",
                table: "ChatUser");

            migrationBuilder.RenameColumn(
                name: "UsersUsername",
                table: "ChatUser",
                newName: "ParticipantsUsername");

            migrationBuilder.RenameIndex(
                name: "IX_ChatUser_UsersUsername",
                table: "ChatUser",
                newName: "IX_ChatUser_ParticipantsUsername");

            migrationBuilder.AddForeignKey(
                name: "FK_ChatUser_Users_ParticipantsUsername",
                table: "ChatUser",
                column: "ParticipantsUsername",
                principalTable: "Users",
                principalColumn: "Username",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
