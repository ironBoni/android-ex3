﻿// <auto-generated />
using System;
using AspWebApi;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;

#nullable disable

namespace AspWebApi.Migrations
{
    [DbContext(typeof(ItemsContext))]
    [Migration("20220530191259_InitialMigration2212")]
    partial class InitialMigration2212
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "6.0.5")
                .HasAnnotation("Relational:MaxIdentifierLength", 64);

            modelBuilder.Entity("ChatUser", b =>
                {
                    b.Property<int>("ChatsId")
                        .HasColumnType("int");

                    b.Property<string>("UsersUsername")
                        .HasColumnType("varchar(255)");

                    b.HasKey("ChatsId", "UsersUsername");

                    b.HasIndex("UsersUsername");

                    b.ToTable("ChatUser");
                });

            modelBuilder.Entity("Models.Chat", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.ToTable("Chats");
                });

            modelBuilder.Entity("Models.Message", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    b.Property<int?>("ChatId")
                        .HasColumnType("int");

                    b.Property<string>("FileName")
                        .HasColumnType("longtext");

                    b.Property<bool>("Sent")
                        .HasColumnType("tinyint(1)");

                    b.Property<string>("Text")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<string>("Type")
                        .IsRequired()
                        .HasMaxLength(10)
                        .HasColumnType("varchar(10)");

                    b.Property<string>("Username")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<DateTime>("WrittenIn")
                        .HasColumnType("datetime(6)");

                    b.HasKey("Id");

                    b.HasIndex("ChatId");

                    b.ToTable("Messages");
                });

            modelBuilder.Entity("Models.Models.Contact", b =>
                {
                    b.Property<string>("Id")
                        .HasColumnType("varchar(255)");

                    b.Property<string>("ContactId")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<string>("Last")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<DateTime?>("Lastdate")
                        .IsRequired()
                        .HasColumnType("datetime(6)");

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<string>("OfUser")
                        .HasColumnType("longtext");

                    b.Property<string>("ProfileImage")
                        .HasColumnType("longtext");

                    b.Property<string>("Server")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<string>("Username")
                        .HasColumnType("varchar(255)");

                    b.HasKey("Id");

                    b.HasIndex("Username");

                    b.ToTable("Contacts");
                });

            modelBuilder.Entity("Models.User", b =>
                {
                    b.Property<string>("Username")
                        .HasColumnType("varchar(255)");

                    b.Property<string>("Nickname")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<string>("Password")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<string>("ProfileImage")
                        .HasColumnType("longtext");

                    b.Property<string>("Server")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.HasKey("Username");

                    b.ToTable("Users");
                });

            modelBuilder.Entity("ChatUser", b =>
                {
                    b.HasOne("Models.Chat", null)
                        .WithMany()
                        .HasForeignKey("ChatsId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("Models.User", null)
                        .WithMany()
                        .HasForeignKey("UsersUsername")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("Models.Message", b =>
                {
                    b.HasOne("Models.Chat", null)
                        .WithMany("Messages")
                        .HasForeignKey("ChatId");
                });

            modelBuilder.Entity("Models.Models.Contact", b =>
                {
                    b.HasOne("Models.User", null)
                        .WithMany("Contacts")
                        .HasForeignKey("Username");
                });

            modelBuilder.Entity("Models.Chat", b =>
                {
                    b.Navigation("Messages");
                });

            modelBuilder.Entity("Models.User", b =>
                {
                    b.Navigation("Contacts");
                });
#pragma warning restore 612, 618
        }
    }
}
