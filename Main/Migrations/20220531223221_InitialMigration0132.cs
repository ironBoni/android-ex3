using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace AspWebApi.Migrations
{
    public partial class InitialMigration0132 : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Messages_Chats_MappedChatId",
                table: "Messages");

            migrationBuilder.DropIndex(
                name: "IX_Messages_MappedChatId",
                table: "Messages");

            migrationBuilder.DropColumn(
                name: "MappedChatId",
                table: "Messages");

            migrationBuilder.AlterColumn<int>(
                name: "ChatId",
                table: "Messages",
                type: "int",
                nullable: false,
                defaultValue: 0,
                oldClrType: typeof(string),
                oldType: "longtext",
                oldNullable: true)
                .OldAnnotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateIndex(
                name: "IX_Messages_ChatId",
                table: "Messages",
                column: "ChatId");

            migrationBuilder.AddForeignKey(
                name: "FK_Messages_Chats_ChatId",
                table: "Messages",
                column: "ChatId",
                principalTable: "Chats",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Messages_Chats_ChatId",
                table: "Messages");

            migrationBuilder.DropIndex(
                name: "IX_Messages_ChatId",
                table: "Messages");

            migrationBuilder.AlterColumn<string>(
                name: "ChatId",
                table: "Messages",
                type: "longtext",
                nullable: true,
                oldClrType: typeof(int),
                oldType: "int")
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddColumn<int>(
                name: "MappedChatId",
                table: "Messages",
                type: "int",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_Messages_MappedChatId",
                table: "Messages",
                column: "MappedChatId");

            migrationBuilder.AddForeignKey(
                name: "FK_Messages_Chats_MappedChatId",
                table: "Messages",
                column: "MappedChatId",
                principalTable: "Chats",
                principalColumn: "Id");
        }
    }
}
