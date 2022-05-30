-- --------------------------------------------------------
-- מארח:                         127.0.0.1
-- Server version:               10.6.7-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL גירסא:               12.0.0.6468
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for pomelodb
CREATE DATABASE IF NOT EXISTS `pomelodb` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `pomelodb`;

-- Dumping structure for table pomelodb.messages
CREATE TABLE IF NOT EXISTS `messages` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Type` varchar(10) NOT NULL,
  `Text` longtext NOT NULL,
  `SenderUsername` longtext NOT NULL,
  `WrittenIn` datetime(6) NOT NULL,
  `FileName` longtext DEFAULT NULL,
  `Sent` tinyint(1) NOT NULL,
  `ChatId` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_Messages_ChatId` (`ChatId`),
  CONSTRAINT `FK_Messages_Chats_ChatId` FOREIGN KEY (`ChatId`) REFERENCES `chats` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table pomelodb.messages: ~42 rows (approximately)
DELETE FROM `messages`;
INSERT INTO `messages` (`Id`, `Type`, `Text`, `SenderUsername`, `WrittenIn`, `FileName`, `Sent`, `ChatId`) VALUES
	(1, 'text', 'my name is Noam.', 'noam', '2021-05-30 16:21:47.000000', '', 1, 1),
	(2, 'text', 'my name is Ron.', 'ron', '2022-05-30 16:23:51.000000', '', 0, 1),
	(3, 'text', 'Nice to meet you!', 'ron', '2022-05-30 16:24:53.000000', '', 0, 1),
	(4, 'text', 'Hey Dvir, how are you?', 'ron', '2022-05-30 16:26:51.000000', ' ', 1, 2),
	(5, 'text', 'I\'m good. Thanks Ron!', 'dvir', '2022-05-30 16:27:58.000000', '', 0, 2),
	(6, 'text', 'I\'m glad to hear that :)', 'ron', '2022-05-30 16:28:42.000000', '', 1, 2),
	(7, 'text', 'Dan, let\'s go to the mall', 'ron', '2022-05-30 16:31:47.000000', '', 1, 4),
	(8, 'text', 'Ok bro. Nice idea Ron', 'dan', '2022-05-30 16:32:26.000000', '', 0, 4),
	(9, 'text', 'I appreciate that :)', 'ron', '2022-05-30 16:33:08.000000', NULL, 1, 4),
	(10, 'text', 'Idan, how was your semester', 'ron', '2022-05-30 16:34:50.000000', ' ', 1, 5),
	(11, 'text', 'It was GOOD. Thanks ron.', 'idan', '2022-05-30 16:36:22.000000', NULL, 0, 5),
	(12, 'text', 'I\'m glad to hear that :)', 'ron', '2022-05-30 16:42:12.000000', NULL, 1, 5),
	(13, 'text', 'Hadar, let\'s go to eat pizza', 'ron', '2022-05-30 16:43:44.000000', NULL, 1, 6),
	(14, 'text', 'Fine Ron, nice idea', 'hadar', '2022-05-30 16:44:23.000000', NULL, 0, 6),
	(15, 'text', 'I appreciate that', 'ron', '2022-05-30 16:45:00.000000', NULL, 1, 6),
	(16, 'text', 'Hey Dvir, how are you?', 'noam', '2022-05-30 16:47:37.000000', NULL, 1, 7),
	(17, 'text', 'I\'m great. Thanks Noam!', 'dvir', '2022-05-30 16:48:23.000000', NULL, 0, 7),
	(18, 'text', 'I Love it Noam! It\'s a nice song! :)', 'dvir', '2022-05-30 16:49:12.000000', NULL, 0, 7),
	(19, 'text', 'Hey Dan, how are you?', 'noam', '2022-05-30 16:53:12.000000', NULL, 1, 8),
	(20, 'text', 'I\'m great. Thanks!', 'dan', '2022-05-30 16:53:50.000000', NULL, 0, 8),
	(21, 'text', 'I\'m glad to hear so', 'noam', '2022-05-30 16:54:47.000000', NULL, 1, 8),
	(22, 'text', 'Hey, how are you?', 'noam', '2022-05-30 16:56:02.000000', NULL, 1, 9),
	(23, 'text', 'I\'m great. Thanks!', 'dan', '2022-05-30 16:56:02.000000', NULL, 0, 9),
	(24, 'text', 'I\'m happy to hear that! :)', 'noam', '2022-09-30 16:56:02.000000', NULL, 1, 9),
	(25, 'text', 'Hadar, let\'s go to eat pizza', 'noam', '2022-05-30 16:56:02.000000', NULL, 1, 10),
	(26, 'text', 'Fine, nice idea Noam', 'hadar', '2022-05-30 16:56:02.000000', NULL, 0, 10),
	(27, 'text', 'I think so too :)', 'noam', '2022-05-30 16:56:02.000000', NULL, 1, 10),
	(28, 'text', 'Hey Dvir, how are you?', 'dan', '2022-05-30 16:56:02.000000', NULL, 1, 11),
	(29, 'text', 'I\'m great Dan. Thanks!', 'dvir', '2022-05-30 16:56:02.000000', NULL, 0, 11),
	(30, 'text', 'Always smile! :)', 'dan', '2022-05-30 16:56:02.000000', NULL, 1, 11),
	(31, 'text', 'Idan lets\' go to the mall', 'dan', '2022-05-30 16:56:02.000000', NULL, 1, 12),
	(32, 'text', 'Ok bro. Nice idea Dan', 'idan', '2022-05-30 16:56:02.000000', NULL, 0, 12),
	(33, 'text', 'I\'m glad you\'re coming', 'dan', '2022-05-30 16:56:02.000000', NULL, 1, 12),
	(34, 'text', 'Hadar, let\'s go to eat pizza', 'dan', '2022-05-30 16:56:02.000000', NULL, 1, 13),
	(35, 'text', 'Find Dan. Nice idea!', 'hadar', '2022-05-30 16:56:02.000000', NULL, 0, 13),
	(36, 'text', 'I\'m glad you agreed!', 'dan', '2022-05-30 16:56:02.000000', NULL, 1, 13),
	(37, 'text', 'Hey, how are you?', 'idan', '2022-05-30 16:56:02.000000', NULL, 1, 14),
	(38, 'text', 'I\'m great. Thanks :)', 'hadar', '2022-05-30 16:56:02.000000', NULL, 0, 14),
	(39, 'text', 'Have a nice day bro! :)', 'idan', '2022-05-30 16:56:02.000000', NULL, 1, 14),
	(40, 'text', 'Hey, how are you?', 'hadar', '2022-05-30 16:56:02.000000', NULL, 1, 15),
	(41, 'text', 'I\'m great. thanks!', 'dvir', '2022-05-30 16:56:02.000000', NULL, 0, 15),
	(42, 'text', 'Have a nice day Dvir! :)', 'hadar', '2022-05-30 16:56:02.000000', NULL, 1, 15);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
