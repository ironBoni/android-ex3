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

-- Dumping structure for table pomelodb.chats
CREATE TABLE IF NOT EXISTS `chats` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table pomelodb.chats: ~23 rows (approximately)
DELETE FROM `chats`;
INSERT INTO `chats` (`Id`) VALUES
	(1),
	(2),
	(4),
	(5),
	(6),
	(7),
	(8),
	(9),
	(10),
	(11),
	(12),
	(13),
	(14),
	(15),
	(16),
	(17),
	(18),
	(19),
	(20),
	(21),
	(22),
	(23),
	(24);

-- Dumping structure for table pomelodb.chatuser
CREATE TABLE IF NOT EXISTS `chatuser` (
  `ChatsId` int(11) NOT NULL,
  `UsersUsername` varchar(255) NOT NULL,
  PRIMARY KEY (`ChatsId`,`UsersUsername`),
  KEY `IX_ChatUser_UsersUsername` (`UsersUsername`),
  CONSTRAINT `FK_ChatUser_Chats_ChatsId` FOREIGN KEY (`ChatsId`) REFERENCES `chats` (`Id`) ON DELETE CASCADE,
  CONSTRAINT `FK_ChatUser_Users_UsersUsername` FOREIGN KEY (`UsersUsername`) REFERENCES `users` (`Username`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table pomelodb.chatuser: ~43 rows (approximately)
DELETE FROM `chatuser`;
INSERT INTO `chatuser` (`ChatsId`, `UsersUsername`) VALUES
	(1, 'noam'),
	(1, 'ron'),
	(2, 'dvir'),
	(2, 'ron'),
	(4, 'dan'),
	(4, 'ron'),
	(5, 'idan'),
	(5, 'ron'),
	(6, 'hadar'),
	(6, 'ron'),
	(7, 'dvir'),
	(7, 'noam'),
	(8, 'dan'),
	(8, 'noam'),
	(9, 'idan'),
	(9, 'noam'),
	(10, 'hadar'),
	(10, 'noam'),
	(11, 'dan'),
	(12, 'idan'),
	(13, 'dan'),
	(13, 'hadar'),
	(14, 'hadar'),
	(14, 'idan'),
	(15, 'dvir'),
	(15, 'hadar'),
	(16, 'noam'),
	(16, 'ran'),
	(17, 'noam'),
	(17, 'ran'),
	(18, 'noam'),
	(18, 'yuval'),
	(19, 'noam'),
	(19, 'yuval'),
	(20, 'noam'),
	(20, 'oren'),
	(21, 'noam'),
	(21, 'oren'),
	(22, 'noam'),
	(22, 'yaniv'),
	(23, 'noam'),
	(23, 'ran'),
	(24, 'noam');

-- Dumping structure for table pomelodb.contacts
CREATE TABLE IF NOT EXISTS `contacts` (
  `Id` varchar(255) NOT NULL,
  `Name` longtext NOT NULL,
  `Server` longtext NOT NULL,
  `Last` longtext DEFAULT NULL,
  `Lastdate` datetime(6) DEFAULT NULL,
  `ProfileImage` longtext DEFAULT NULL,
  `Username` longtext DEFAULT NULL,
  `ContactId` longtext NOT NULL,
  `OfUser` longtext DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table pomelodb.contacts: ~30 rows (approximately)
DELETE FROM `contacts`;
INSERT INTO `contacts` (`Id`, `Name`, `Server`, `Last`, `Lastdate`, `ProfileImage`, `Username`, `ContactId`, `OfUser`) VALUES
	('1', 'Ron Solomon', 'http://localhost:5186', 'Nice to meet you!', '2022-05-30 17:15:36.000000', 'https://cdn.dribbble.com/users/2364329/screenshots/10481283/media/f013d5235bfcf1753d56cad154f11a67.jpg', 'noam', 'ron', 'noam'),
	('10', 'Idan Ben Ari', 'http://localhost:5186', 'I\'m glad you\'re coming :)', '2022-05-30 17:23:01.000000', 'https://fiverr-res.cloudinary.com/images/t_main1,q_auto,f_auto,q_auto,f_auto/attachments/delivery/asset/54164cf6ae1512d8c0a2b1d8306c5a68-1649285147/wouterbult/draw-nice-style-cartoon-caricature-as-a-profile-picture.jpg', 'dvir', 'idan', 'dan'),
	('11', 'Noam Cohen', 'http://localhost:5186', 'Nice to meet you! :)', '2022-05-30 18:06:40.000000', 'https://scontent.fsdv3-1.fna.fbcdn.net/v/t1.6435-9/46498020_2215436798469037_9121585456583016448_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=pgAp58x8fUcAX_q7Geu&_nc_ht=scontent.fsdv3-1.fna&oh=00_AT-sSFofwqgtb84VAAwlKpm5m_LtaBWKsq04MI4ZqVYu_A&oe=62C19375', 'ron', 'noam', 'ron'),
	('12', 'Dvir Pollak', 'http://localhost:5186', 'I\'m glad to hear that :)', '2022-05-30 18:07:25.000000', 'https://media-exp1.licdn.com/dms/image/C4E03AQF6ZOFmppSpxg/profile-displayphoto-shrink_200_200/0/1646299616015?e=1660176000&v=beta&t=wnbQBn76v397qnw3fWuHpQD2ocgcI6pAAL06XXbsw_I', 'ron', 'dvir', 'ron'),
	('13', 'Dan Cohen', 'http://localhost:5186', 'I appreciate that :)', '2022-05-30 18:08:04.000000', 'https://avatoon.net/wp-content/uploads/2020/04/Gary-Avatar.png', 'ron', 'dan', 'ron'),
	('14', 'Idan Ben Ari', 'http://localhost:5186', 'I\'m glad to hear that :)', '2022-05-30 18:08:55.000000', 'https://fiverr-res.cloudinary.com/images/t_main1,q_auto,f_auto,q_auto,f_auto/attachments/delivery/asset/54164cf6ae1512d8c0a2b1d8306c5a68-1649285147/wouterbult/draw-nice-style-cartoon-caricature-as-a-profile-picture.jpg', 'ron', 'idan', 'ron'),
	('15', 'Hadar Pinto', 'http://localhost:5186', 'I appreciate that :)', '2022-05-30 18:09:44.000000', 'https://media-exp1.licdn.com/dms/image/C4D03AQG7Oph-nHMJdQ/profile-displayphoto-shrink_200_200/0/1646846030443?e=1660176000&v=beta&t=TO5w-Fpy7_ve4_ixX4EWvqZDC1W_A0aqiszvCKT86jo', 'ron', 'hadar', 'ron'),
	('16', 'Ron Solomon', 'http://localhost:5186', 'I\'m glad to hear that :)', '2022-05-30 18:11:03.000000', 'https://cdn.dribbble.com/users/2364329/screenshots/10481283/media/f013d5235bfcf1753d56cad154f11a67.jpg', 'idan', 'ron', 'idan'),
	('17', 'Noam Cohen', 'http://localhost:5186', 'I\'m happy to hear that! :)', '2022-05-30 18:11:33.000000', 'https://scontent.fsdv3-1.fna.fbcdn.net/v/t1.6435-9/46498020_2215436798469037_9121585456583016448_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=pgAp58x8fUcAX_q7Geu&_nc_ht=scontent.fsdv3-1.fna&oh=00_AT-sSFofwqgtb84VAAwlKpm5m_LtaBWKsq04MI4ZqVYu_A&oe=62C19375', 'idan', 'noam', 'idan'),
	('18', 'Dan Cohen', 'http://localhost:5186', 'I\'m glad you\'re coming! :)', '2022-05-30 18:12:27.000000', 'https://avatoon.net/wp-content/uploads/2020/04/Gary-Avatar.png', 'idan', 'dan', 'idan'),
	('19', 'Hadar Pinto', 'http://localhost:5186', 'Have a nice day bro!', '2022-05-30 18:13:34.000000', 'https://media-exp1.licdn.com/dms/image/C4D03AQG7Oph-nHMJdQ/profile-displayphoto-shrink_200_200/0/1646846030443?e=1660176000&v=beta&t=TO5w-Fpy7_ve4_ixX4EWvqZDC1W_A0aqiszvCKT86jo', 'idan', 'hadar', 'idan'),
	('2', 'Hadar Pinto', 'http://localhost:5186', 'I\'m glad you agreed!', '2022-05-30 17:23:24.000000', 'https://media-exp1.licdn.com/dms/image/C4D03AQG7Oph-nHMJdQ/profile-displayphoto-shrink_200_200/0/1646846030443?e=1660176000&v=beta&t=TO5w-Fpy7_ve4_ixX4EWvqZDC1W_A0aqiszvCKT86jo', 'noam', 'hadar', 'noam'),
	('20', 'Ron Solomon', 'http://localhost:5186', 'I\'m glad to hear that :)', '2022-05-30 18:15:18.000000', 'https://cdn.dribbble.com/users/2364329/screenshots/10481283/media/f013d5235bfcf1753d56cad154f11a67.jpg', 'dvir', 'ron', 'dvir'),
	('21', 'Noam Cohen', 'http://localhost:5186', 'I Love it Noam! It\'s a very nice song! :)', '2022-05-30 18:16:01.000000', 'https://scontent.fsdv3-1.fna.fbcdn.net/v/t1.6435-9/46498020_2215436798469037_9121585456583016448_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=pgAp58x8fUcAX_q7Geu&_nc_ht=scontent.fsdv3-1.fna&oh=00_AT-sSFofwqgtb84VAAwlKpm5m_LtaBWKsq04MI4ZqVYu_A&oe=62C19375', 'dvir', 'noam', 'dvir'),
	('22', 'Dan Cohen', 'http://localhost:5186', 'Always smile! :)', '2022-05-30 18:16:39.000000', 'https://avatoon.net/wp-content/uploads/2020/04/Gary-Avatar.png', 'dvir', 'dan', 'dvir'),
	('23', 'Hadar Pinto', 'http://localhost:5186', 'Always smile! :)', '2022-05-30 18:17:20.000000', 'https://media-exp1.licdn.com/dms/image/C4D03AQG7Oph-nHMJdQ/profile-displayphoto-shrink_200_200/0/1646846030443?e=1660176000&v=beta&t=TO5w-Fpy7_ve4_ixX4EWvqZDC1W_A0aqiszvCKT86jo', 'dvir', 'hadar', 'dvir'),
	('24', 'Ron Solomon', 'http://localhost:5186', 'Fine Ron, nice idea', '2022-05-30 18:18:20.000000', 'https://cdn.dribbble.com/users/2364329/screenshots/10481283/media/f013d5235bfcf1753d56cad154f11a67.jpg', 'hadar', 'ron', 'hadar'),
	('25', 'Noam Cohen', 'http://localhost:5186', 'I think so too :)', '2022-05-30 18:18:53.000000', 'https://scontent.fsdv3-1.fna.fbcdn.net/v/t1.6435-9/46498020_2215436798469037_9121585456583016448_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=pgAp58x8fUcAX_q7Geu&_nc_ht=scontent.fsdv3-1.fna&oh=00_AT-sSFofwqgtb84VAAwlKpm5m_LtaBWKsq04MI4ZqVYu_A&oe=62C19375', 'hadar', 'noam', 'hadar'),
	('26', 'Dan Cohen', 'http://localhost:5186', 'I\'m glad you agreed!', '2022-05-30 18:19:52.000000', 'https://avatoon.net/wp-content/uploads/2020/04/Gary-Avatar.png', 'hadar', 'dan', 'hadar'),
	('27', 'Idan Ben Ari', 'http://localhost:5186', 'Have a nice day bro!', '2022-05-30 18:20:34.000000', 'https://fiverr-res.cloudinary.com/images/t_main1,q_auto,f_auto,q_auto,f_auto/attachments/delivery/asset/54164cf6ae1512d8c0a2b1d8306c5a68-1649285147/wouterbult/draw-nice-style-cartoon-caricature-as-a-profile-picture.jpg', 'hadar', 'idan', 'hadar'),
	('28', 'Dvir Pollak', 'http://localhost:5186', 'Always smile! :)', '2022-05-30 18:21:07.000000', 'https://media-exp1.licdn.com/dms/image/C4E03AQF6ZOFmppSpxg/profile-displayphoto-shrink_200_200/0/1646299616015?e=1660176000&v=beta&t=wnbQBn76v397qnw3fWuHpQD2ocgcI6pAAL06XXbsw_I', 'hadar', 'dvir', 'hadar'),
	('3', 'Divr Pollak', 'http://localhost:5186', 'I Love it Noam! It\'s a very nice song! :)', '2022-05-30 17:16:35.000000', 'https://media-exp1.licdn.com/dms/image/C4E03AQF6ZOFmppSpxg/profile-displayphoto-shrink_200_200/0/1646299616015?e=1660176000&v=beta&t=wnbQBn76v397qnw3fWuHpQD2ocgcI6pAAL06XXbsw_I', 'noam', 'dvir', 'noam'),
	('4', 'Dan Cohen', 'http://localhost:5186', 'I\'m glad to hear so! :)', '2022-05-30 17:16:35.000000', 'https://avatoon.net/wp-content/uploads/2020/04/Gary-Avatar.png', 'noam', 'dan', 'noam'),
	('5', 'Idan Ben Ari', 'http://localhost:5186', 'I\'m happy to hear that! :)', '2022-05-30 17:16:35.000000', 'https://fiverr-res.cloudinary.com/images/t_main1,q_auto,f_auto,q_auto,f_auto/attachments/delivery/asset/54164cf6ae1512d8c0a2b1d8306c5a68-1649285147/wouterbult/draw-nice-style-cartoon-caricature-as-a-profile-picture.jpg', 'noam', 'idan', 'noam'),
	('5acc8415-f9a9-4fea-afd6-d39d11a2d9c4', 'Ran Levi', 'localhost:5186', NULL, NULL, 'https://i.etsystatic.com/28761236/c/2000/1589/0/76/il/d929f2/3162035768/il_340x270.3162035768_ci09.jpg', 'noam', 'ran', 'noam'),
	('6', 'Hadar Pinto', 'http://localhost:5186', 'I think so too :)', '2022-05-30 17:18:59.000000', 'https://media-exp1.licdn.com/dms/image/C4D03AQG7Oph-nHMJdQ/profile-displayphoto-shrink_200_200/0/1646846030443?e=1660176000&v=beta&t=TO5w-Fpy7_ve4_ixX4EWvqZDC1W_A0aqiszvCKT86jo', 'dan', 'hadar', 'dan'),
	('7', 'Ron Solomon', 'http://localhost:5186', 'I appreciate that :)', '2022-05-30 17:18:59.000000', 'https://cdn.dribbble.com/users/2364329/screenshots/10481283/media/f013d5235bfcf1753d56cad154f11a67.jpg', 'dan', 'ron', 'dan'),
	('8', 'Noam Cohen', 'http://localhost:5186', 'I\'m glad to hear so! :)', '2022-05-30 17:18:59.000000', 'https://scontent.fsdv3-1.fna.fbcdn.net/v/t1.6435-9/46498020_2215436798469037_9121585456583016448_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=pgAp58x8fUcAX_q7Geu&_nc_ht=scontent.fsdv3-1.fna&oh=00_AT-sSFofwqgtb84VAAwlKpm5m_LtaBWKsq04MI4ZqVYu_A&oe=62C19375', 'dan', 'noam', 'dan'),
	('9', 'Dvir Pollak', 'http://localhost:5186', 'Always smile! :)', '2022-05-30 17:22:07.000000', 'https://media-exp1.licdn.com/dms/image/C4E03AQF6ZOFmppSpxg/profile-displayphoto-shrink_200_200/0/1646299616015?e=1660176000&v=beta&t=wnbQBn76v397qnw3fWuHpQD2ocgcI6pAAL06XXbsw_I', 'dan', 'dvir', 'dan'),
	('c27b0253-38bd-43dc-8e0a-42cacaaee167', 'Noam Cohen', 'http://localhost:5186/', NULL, NULL, 'https://scontent.fsdv3-1.fna.fbcdn.net/v/t1.6435-9/46498020_2215436798469037_9121585456583016448_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=pgAp58x8fUcAX_q7Geu&_nc_ht=scontent.fsdv3-1.fna&oh=00_AT-sSFofwqgtb84VAAwlKpm5m_LtaBWKsq04MI4ZqVYu_A&oe=62C19375', 'ran', 'noam', 'ran');

-- Dumping structure for table pomelodb.messages
CREATE TABLE IF NOT EXISTS `messages` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Type` varchar(10) NOT NULL,
  `Text` longtext NOT NULL,
  `Username` longtext NOT NULL,
  `WrittenIn` datetime(6) NOT NULL,
  `FileName` longtext DEFAULT NULL,
  `Sent` tinyint(1) NOT NULL,
  `ChatId` int(11) NOT NULL DEFAULT 0,
  `Username1` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_Messages_Username1` (`Username1`),
  KEY `IX_Messages_ChatId` (`ChatId`),
  CONSTRAINT `FK_Messages_Chats_ChatId` FOREIGN KEY (`ChatId`) REFERENCES `chats` (`Id`) ON DELETE CASCADE,
  CONSTRAINT `FK_Messages_Users_Username1` FOREIGN KEY (`Username1`) REFERENCES `users` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table pomelodb.messages: ~44 rows (approximately)
DELETE FROM `messages`;
INSERT INTO `messages` (`Id`, `Type`, `Text`, `Username`, `WrittenIn`, `FileName`, `Sent`, `ChatId`, `Username1`) VALUES
	(1, 'text', 'yesh!! Thanks :)', 'noam', '2022-06-01 02:05:11.325633', '', 1, 24, NULL),
	(2, 'text', 'hahahahha', 'noam', '2022-06-01 02:05:15.252798', '', 1, 24, NULL),
	(3, 'text', 'blo', 'noam', '2022-06-01 02:03:59.620933', '', 1, 16, NULL),
	(4, 'text', 'dsadsadsa', 'noam', '2022-06-01 01:33:12.007215', '', 1, 1, NULL),
	(5, 'text', 'I\'m good. Thanks Ron!', 'dvir', '2022-05-30 16:27:58.000000', '', 0, 2, NULL),
	(6, 'text', 'I\'m glad to hear that :)', 'ron', '2022-05-30 16:28:42.000000', '', 1, 2, NULL),
	(7, 'text', 'Dan, let\'s go to the mall', 'ron', '2022-05-30 16:31:47.000000', '', 1, 4, NULL),
	(8, 'text', 'Ok bro. Nice idea Ron', 'dan', '2022-05-30 16:32:26.000000', '', 0, 4, NULL),
	(9, 'text', 'I appreciate that :)', 'ron', '2022-05-30 16:33:08.000000', NULL, 1, 4, NULL),
	(10, 'text', 'Idan, how was your semester', 'ron', '2022-05-30 16:34:50.000000', ' ', 1, 5, NULL),
	(11, 'text', 'It was GOOD. Thanks ron.', 'idan', '2022-05-30 16:36:22.000000', NULL, 0, 5, NULL),
	(12, 'text', 'I\'m glad to hear that :)', 'ron', '2022-05-30 16:42:12.000000', NULL, 1, 5, NULL),
	(13, 'text', 'Hadar, let\'s go to eat pizza', 'ron', '2022-05-30 16:43:44.000000', NULL, 1, 6, NULL),
	(14, 'text', 'Fine Ron, nice idea', 'hadar', '2022-05-30 16:44:23.000000', NULL, 0, 6, NULL),
	(15, 'text', 'I appreciate that', 'ron', '2022-05-30 16:45:00.000000', NULL, 1, 6, NULL),
	(16, 'text', 'Hey Dvir, how are you?', 'noam', '2022-05-30 16:47:37.000000', NULL, 1, 7, NULL),
	(17, 'text', 'I\'m great. Thanks Noam!', 'dvir', '2022-05-30 16:48:23.000000', NULL, 0, 7, NULL),
	(18, 'text', 'I Love it Noam! It\'s a nice song! :)', 'dvir', '2022-05-30 16:49:12.000000', NULL, 0, 7, NULL),
	(19, 'text', 'Hey Dan, how are you?', 'noam', '2022-05-30 16:53:12.000000', NULL, 1, 8, NULL),
	(20, 'text', 'I\'m great. Thanks!', 'dan', '2022-05-30 16:53:50.000000', NULL, 0, 8, NULL),
	(21, 'text', 'I\'m glad to hear so', 'noam', '2022-05-30 16:54:47.000000', NULL, 1, 8, NULL),
	(22, 'text', 'Hey, how are you?', 'noam', '2022-05-30 16:56:02.000000', NULL, 1, 9, NULL),
	(23, 'text', 'I\'m great. Thanks!', 'dan', '2022-05-30 16:56:02.000000', NULL, 0, 9, NULL),
	(24, 'text', 'I\'m happy to hear that! :)', 'noam', '2022-09-30 16:56:02.000000', NULL, 1, 9, NULL),
	(25, 'text', 'Hadar, let\'s go to eat pizza', 'noam', '2022-05-30 16:56:02.000000', NULL, 1, 10, NULL),
	(26, 'text', 'Fine, nice idea Noam', 'hadar', '2022-05-30 16:56:02.000000', NULL, 0, 10, NULL),
	(27, 'text', 'I think so too :)', 'noam', '2022-05-30 16:56:02.000000', NULL, 1, 10, NULL),
	(28, 'text', 'Hey Dvir, how are you?', 'dan', '2022-05-30 16:56:02.000000', NULL, 1, 11, NULL),
	(29, 'text', 'I\'m great Dan. Thanks!', 'dvir', '2022-05-30 16:56:02.000000', NULL, 0, 11, NULL),
	(30, 'text', 'Always smile! :)', 'dan', '2022-05-30 16:56:02.000000', NULL, 1, 11, NULL),
	(31, 'text', 'Idan lets\' go to the mall', 'dan', '2022-05-30 16:56:02.000000', NULL, 1, 12, NULL),
	(32, 'text', 'Ok bro. Nice idea Dan', 'idan', '2022-05-30 16:56:02.000000', NULL, 0, 12, NULL),
	(33, 'text', 'I\'m glad you\'re coming', 'dan', '2022-05-30 16:56:02.000000', NULL, 1, 12, NULL),
	(34, 'text', 'Hadar, let\'s go to eat pizza', 'dan', '2022-05-30 16:56:02.000000', NULL, 1, 13, NULL),
	(35, 'text', 'Find Dan. Nice idea!', 'hadar', '2022-05-30 16:56:02.000000', NULL, 0, 13, NULL),
	(36, 'text', 'I\'m glad you agreed!', 'dan', '2022-05-30 16:56:02.000000', NULL, 1, 13, NULL),
	(37, 'text', 'Hey, how are you?', 'idan', '2022-05-30 16:56:02.000000', NULL, 1, 14, NULL),
	(38, 'text', 'I\'m great. Thanks :)', 'hadar', '2022-05-30 16:56:02.000000', NULL, 0, 14, NULL),
	(39, 'text', 'Have a nice day bro! :)', 'idan', '2022-05-30 16:56:02.000000', NULL, 1, 14, NULL),
	(40, 'text', 'Hey, how are you?', 'hadar', '2022-05-30 16:56:02.000000', NULL, 1, 15, NULL),
	(41, 'text', 'I\'m great. thanks!', 'dvir', '2022-05-30 16:56:02.000000', NULL, 0, 15, NULL),
	(42, 'text', 'Have a nice day Dvir! :)', 'hadar', '2022-05-30 16:56:02.000000', NULL, 1, 15, NULL),
	(43, 'text', 'aaaa', 'noam', '2022-05-31 01:46:33.529218', NULL, 1, 10, NULL),
	(44, 'text', 'dddd', 'noam', '2022-05-31 23:25:33.108102', NULL, 1, 10, NULL);

-- Dumping structure for table pomelodb.users
CREATE TABLE IF NOT EXISTS `users` (
  `Username` varchar(255) NOT NULL,
  `Nickname` longtext NOT NULL,
  `Password` longtext NOT NULL,
  `ProfileImage` longtext DEFAULT NULL,
  `Server` longtext NOT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table pomelodb.users: ~11 rows (approximately)
DELETE FROM `users`;
INSERT INTO `users` (`Username`, `Nickname`, `Password`, `ProfileImage`, `Server`) VALUES
	('dan', 'Dan Cohen', 'Np1234', 'https://avatoon.net/wp-content/uploads/2020/04/Gary-Avatar.png', 'http://localhost:5186'),
	('dvir', 'Dvir Pollak', 'Np1234', 'https://media-exp1.licdn.com/dms/image/C4E03AQF6ZOFmppSpxg/profile-displayphoto-shrink_200_200/0/1646299616015?e=1660176000&v=beta&t=wnbQBn76v397qnw3fWuHpQD2ocgcI6pAAL06XXbsw_I', 'http://localhost:5186'),
	('hadar', 'Hadar Pinto', 'Np1234', 'https://media-exp1.licdn.com/dms/image/C4D03AQG7Oph-nHMJdQ/profile-displayphoto-shrink_200_200/0/1646846030443?e=1660176000&v=beta&t=TO5w-Fpy7_ve4_ixX4EWvqZDC1W_A0aqiszvCKT86jo', 'http://localhost:5186'),
	('idan', 'Idan Ben Ari', 'Np1234', 'https://fiverr-res.cloudinary.com/images/t_main1,q_auto,f_auto,q_auto,f_auto/attachments/delivery/asset/54164cf6ae1512d8c0a2b1d8306c5a68-1649285147/wouterbult/draw-nice-style-cartoon-caricature-as-a-profile-picture.jpg', 'http://localhost:5186'),
	('noam', 'Noam Cohen', 'Np1234', 'https://scontent.fsdv3-1.fna.fbcdn.net/v/t1.6435-9/46498020_2215436798469037_9121585456583016448_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=pgAp58x8fUcAX_q7Geu&_nc_ht=scontent.fsdv3-1.fna&oh=00_AT-sSFofwqgtb84VAAwlKpm5m_LtaBWKsq04MI4ZqVYu_A&oe=62C19375', 'http://localhost:5186/'),
	('oren', 'Oren Orbach', 'Np1234', 'https://i.etsystatic.com/28761236/c/2000/1589/0/76/il/d929f2/3162035768/il_340x270.3162035768_ci09.jpg', 'http://localhost:5186'),
	('ran', 'Ran Levi', 'Np1234', 'https://i.etsystatic.com/28761236/c/2000/1589/0/76/il/d929f2/3162035768/il_340x270.3162035768_ci09.jpg', 'http://localhost:5186'),
	('ron', 'Ron Solomon', 'Np1234', 'https://cdn.dribbble.com/users/2364329/screenshots/10481283/media/f013d5235bfcf1753d56cad154f11a67.jpg', 'http://localhost:5186'),
	('shlomo', 'Shlomo Levin', 'Np1234', 'https://fiverr-res.cloudinary.com/images/t_main1,q_auto,f_auto,q_auto,f_auto/gigs/50183164/original/f80714a807d09df88dc708d83941384ac5d9e6dd/draw-nice-style-cartoon-caricature-as-a-profile-picture.png', 'http://localhost:5186'),
	('yaniv', 'Yaniv Hoffman', 'Np1234', 'https://cdn.pixabay.com/photo/2016/12/07/21/01/cartoon-1890438_960_720.jpg', 'http://localhost:5186'),
	('yuval', 'Yuval Baruchi', 'Np1234', 'https://cdn.dribbble.com/users/2364329/screenshots/10481283/media/f013d5235bfcf1753d56cad154f11a67.jpg', 'http://localhost:5186');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
