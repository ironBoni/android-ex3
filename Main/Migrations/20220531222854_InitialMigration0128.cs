using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace AspWebApi.Migrations
{
    public partial class InitialMigration0128 : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Messages_Chats_ChatId1",
                table: "Messages");

            migrationBuilder.RenameColumn(
                name: "ChatId1",
                table: "Messages",
                newName: "MappedChatId");

            migrationBuilder.RenameIndex(
                name: "IX_Messages_ChatId1",
                table: "Messages",
                newName: "IX_Messages_MappedChatId");

            migrationBuilder.AddForeignKey(
                name: "FK_Messages_Chats_MappedChatId",
                table: "Messages",
                column: "MappedChatId",
                principalTable: "Chats",
                principalColumn: "Id");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Messages_Chats_MappedChatId",
                table: "Messages");

            migrationBuilder.RenameColumn(
                name: "MappedChatId",
                table: "Messages",
                newName: "ChatId1");

            migrationBuilder.RenameIndex(
                name: "IX_Messages_MappedChatId",
                table: "Messages",
                newName: "IX_Messages_ChatId1");

            migrationBuilder.AddForeignKey(
                name: "FK_Messages_Chats_ChatId1",
                table: "Messages",
                column: "ChatId1",
                principalTable: "Chats",
                principalColumn: "Id");
        }
    }
}
