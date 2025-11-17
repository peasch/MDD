-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: mdd
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `article_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `author_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `FK_comment_article` (`article_id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK5yx0uphgjc6ik6hb82kkw501y` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_comment_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (2,'wouaw celui là aussi c\'est suer',2,25,'2025-10-06 18:50:21.000000',25),(6,'Super article !',6,25,'2025-10-06 18:50:21.000000',25),(7,'Super article ! Depuis PostMan!',6,25,'2025-10-06 18:50:21.000000',25),(8,'Super utile : OnPush + immutabilité = UI réactive ?',2,6,'2025-10-06 18:50:21.000000',6),(9,'Merci pour les exemples de ChangeDetection, ça clarifie !',2,2,'2025-10-06 18:50:21.000000',2),(10,'J’adore les collectors personnalisés, merci pour les tips.',11,25,'2025-10-06 18:50:21.000000',25),(11,'Attention aux parallel streams sur petites collections ?',11,1,'2025-10-06 18:50:21.000000',1),(12,'Les générateurs changent la donne pour les gros fichiers !',10,2,'2025-10-06 18:50:21.000000',2),(13,'Top les explications sur yield et la mémoire.',10,25,'2025-10-06 18:50:21.000000',25),(14,'Les CBV sont sous-estimées, merci pour les patterns.',8,7,'2025-10-06 18:50:21.000000',7),(15,'La séparation settings dev/prod = indispensable !',8,6,'2025-10-06 18:50:21.000000',6),(16,'Golden hour FTW ? Merci pour la check-list !',11,1,'2025-10-06 18:50:21.000000',1),(17,'Un rappel utile sur la balance des blancs ?',11,7,'2025-10-06 18:50:21.000000',7),(18,'test ajout commentaire depuis appli ?',4,NULL,'2025-10-28 08:52:21.000000',25),(19,'commentaire gérard',4,NULL,'2025-10-28 08:52:21.000000',25),(20,'si j\'en ajoute un là ?',2,NULL,'2025-10-28 08:52:21.000000',25),(21,'et là ?',2,NULL,'2025-10-28 08:54:09.744000',25),(22,'ajout de commentaire pendant la session de mentorat',2,NULL,'2025-10-31 12:42:18.223000',25),(23,'test ',16,NULL,'2025-11-04 17:36:46.473000',25),(24,'on rajoute un commentaire mardi',4,NULL,'2025-11-04 17:37:38.131000',25),(25,' re teste du vendredi 14',14,NULL,'2025-11-14 09:51:40.988000',6),(26,'allez on en ajoute un pour la comparaison de la date',11,NULL,'2025-11-14 10:03:49.324000',6),(27,'ajout commentaire mentorat du 13 novembre',2,NULL,'2025-11-14 12:42:19.130000',6),(28,'test du 14-11',16,NULL,'2025-11-14 17:37:22.226000',6),(29,'ohhhh super',17,NULL,'2025-11-14 17:37:33.595000',6),(30,'voici mon article , un superbe lorem ipsum sur Django, parce que j\'ai rien de mieux à dire !\n',18,NULL,'2025-11-16 14:58:15.492000',6),(31,'merci à tous pour vos commentaires enrichissants !!\n',16,NULL,'2025-11-16 15:56:12.933000',6);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-17 17:36:01
