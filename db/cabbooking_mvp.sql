-- MySQL dump 10.13  Distrib 8.0.45, for Linux (x86_64)
--
-- Host: localhost    Database: cabbooking_mvp
-- ------------------------------------------------------
-- Server version	8.0.45-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cabs`
--

DROP TABLE IF EXISTS `cabs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cabs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `brand` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cab_type` enum('LUX','SEDAN','SUV','VAN') COLLATE utf8mb4_unicode_ci NOT NULL,
  `color` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `model` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `plate_number` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `driver_id` bigint NOT NULL,
  `status` enum('OFFLINE','ONLINE') COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cabs_plate` (`plate_number`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cabs`
--

LOCK TABLES `cabs` WRITE;
/*!40000 ALTER TABLE `cabs` DISABLE KEYS */;
INSERT INTO `cabs` VALUES (1,_binary '','Toyota','SUV','Red','Corolla','ABC-123',0,'ONLINE'),(2,_binary '','Toyota','SEDAN','White','Prius','XYZ-001',2,'OFFLINE'),(3,_binary '','Toyota','SEDAN','White','Prius','XYZ-999',2,'OFFLINE'),(4,_binary '','Toyota','SEDAN','Red','Prius','XYZ-1000',2,'OFFLINE'),(5,_binary '','Toyota','SEDAN','Red','Prius','XYZ-1001',2,'OFFLINE'),(6,_binary '','Toytola','SEDAN','White','Corella','FIN-755',6,'OFFLINE'),(7,_binary '','Toytola','SEDAN','White','Corella','FiN-774',6,'OFFLINE'),(8,_binary '','Toyota','SEDAN','Blue','Prius','XYZ-2026',6,'OFFLINE');
/*!40000 ALTER TABLE `cabs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `driver_cab_assignment`
--

DROP TABLE IF EXISTS `driver_cab_assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `driver_cab_assignment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `current` bit(1) NOT NULL,
  `end_time` datetime(6) DEFAULT NULL,
  `start_time` datetime(6) NOT NULL,
  `cab_id` bigint NOT NULL,
  `driver_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_assign_driver` (`driver_id`),
  KEY `idx_assign_cab` (`cab_id`),
  CONSTRAINT `fk_assign_cab` FOREIGN KEY (`cab_id`) REFERENCES `cabs` (`id`),
  CONSTRAINT `fk_assign_driver` FOREIGN KEY (`driver_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver_cab_assignment`
--

LOCK TABLES `driver_cab_assignment` WRITE;
/*!40000 ALTER TABLE `driver_cab_assignment` DISABLE KEYS */;
/*!40000 ALTER TABLE `driver_cab_assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ratings`
--

DROP TABLE IF EXISTS `ratings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ratings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `score` int NOT NULL,
  `driver_id` bigint NOT NULL,
  `rider_id` bigint NOT NULL,
  `trip_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rating_trip` (`trip_id`),
  KEY `idx_rating_driver` (`driver_id`),
  KEY `idx_rating_rider` (`rider_id`),
  CONSTRAINT `fk_rating_driver` FOREIGN KEY (`driver_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_rating_rider` FOREIGN KEY (`rider_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_rating_trip` FOREIGN KEY (`trip_id`) REFERENCES `trip_bookings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ratings`
--

LOCK TABLES `ratings` WRITE;
/*!40000 ALTER TABLE `ratings` DISABLE KEYS */;
INSERT INTO `ratings` VALUES (1,'Great ride!','2026-04-14 20:28:37.595287',5,6,2,37);
/*!40000 ALTER TABLE `ratings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trip_bookings`
--

DROP TABLE IF EXISTS `trip_bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trip_bookings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `dropoff_location` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pickup_location` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('ACCEPTED','CANCELLED','COMPLETED','IN_PROGRESS','PENDING') COLLATE utf8mb4_unicode_ci NOT NULL,
  `total_fare` decimal(12,2) DEFAULT NULL,
  `updated_at` datetime(6) NOT NULL,
  `cab_id` bigint DEFAULT NULL,
  `driver_id` bigint DEFAULT NULL,
  `rider_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_trip_rider` (`rider_id`),
  KEY `idx_trip_driver` (`driver_id`),
  KEY `idx_trip_status` (`status`),
  KEY `fk_trip_cab` (`cab_id`),
  CONSTRAINT `fk_trip_cab` FOREIGN KEY (`cab_id`) REFERENCES `cabs` (`id`),
  CONSTRAINT `fk_trip_driver` FOREIGN KEY (`driver_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_trip_rider` FOREIGN KEY (`rider_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trip_bookings`
--

LOCK TABLES `trip_bookings` WRITE;
/*!40000 ALTER TABLE `trip_bookings` DISABLE KEYS */;
INSERT INTO `trip_bookings` VALUES (1,'2026-03-05 07:52:54.351824','Centria Campus','Helsinki Central Station','COMPLETED',18.50,'2026-03-09 16:54:30.929651',1,1,1),(2,'2026-03-09 16:51:16.749009','Centria Campus','Helsinki Railway Station','COMPLETED',NULL,'2026-04-05 07:52:35.406576',4,6,2),(3,'2026-03-10 13:02:23.005562','Centria Campus','Helsinki Railway Station','COMPLETED',NULL,'2026-04-05 07:52:36.323711',4,6,2),(4,'2026-03-10 13:02:50.410114','Centria Campus','Helsinki Railway Station','IN_PROGRESS',NULL,'2026-03-31 16:14:07.339368',1,2,2),(5,'2026-03-10 13:05:01.535817','Centria Campus','Helsinki Railway Station','CANCELLED',NULL,'2026-04-05 08:31:09.584067',1,1,2),(6,'2026-03-10 13:11:37.829443','Centria Campus','Helsinki Railway Station','IN_PROGRESS',NULL,'2026-03-10 13:17:18.551314',1,1,2),(7,'2026-03-10 13:11:54.494655','Centria Campus','Helsinki Railway Station','COMPLETED',NULL,'2026-03-10 13:21:56.098637',1,1,2),(8,'2026-03-10 13:16:44.003369','Centria Campus','Helsinki Railway Station','CANCELLED',NULL,'2026-04-05 08:31:10.567321',1,1,2),(9,'2026-03-10 13:18:55.493539','Centria Campus','Helsinki Railway Station','COMPLETED',NULL,'2026-03-10 15:17:15.964717',1,1,2),(10,'2026-03-10 15:16:03.632729','Centria Campus','Helsinki Railway Station','COMPLETED',NULL,'2026-03-11 09:00:59.560851',1,1,2),(11,'2026-03-10 15:16:35.421487','Centria Campus','Helsinki Railway Station','CANCELLED',NULL,'2026-03-11 09:02:11.854064',1,1,2),(12,'2026-03-10 15:16:58.038884','Centria Campus','Helsinki Railway Station','COMPLETED',NULL,'2026-04-05 07:52:36.845536',6,6,2),(13,'2026-03-11 09:00:23.692573','Centria Campus','Helsinki Railway Station','COMPLETED',NULL,'2026-04-05 07:54:06.677474',6,6,2),(14,'2026-03-11 09:01:56.242927','Centria Campus','Helsinki Railway Station','COMPLETED',NULL,'2026-04-05 07:54:07.289432',6,6,2),(15,'2026-03-19 15:03:07.385881','Centria University','Helsinki Railway Station','COMPLETED',NULL,'2026-04-05 07:54:07.839208',6,6,2),(16,'2026-03-19 15:04:47.116846','Centria University','Kokkola Railway Station','COMPLETED',NULL,'2026-04-05 07:54:08.406380',6,6,2),(17,'2026-03-31 16:09:49.361540','Kokkola Railway Station','Centria University','COMPLETED',NULL,'2026-04-05 07:54:08.909407',6,6,2),(18,'2026-04-02 19:13:36.851226','Chydenia Shopping Center','City Center','COMPLETED',NULL,'2026-04-05 07:54:09.292781',6,6,2),(19,'2026-04-02 19:34:50.620741','Centria University','Kokkola Bus Station','COMPLETED',NULL,'2026-04-05 07:52:37.616517',6,6,6),(20,'2026-04-03 15:00:56.032422','Campus','Station','COMPLETED',NULL,'2026-04-05 07:54:10.373050',6,6,2),(21,'2026-04-03 15:04:09.412332','Campus','Station','COMPLETED',NULL,'2026-04-05 07:54:10.729693',6,6,2),(22,'2026-04-03 15:04:55.316919','Campus','Station','COMPLETED',NULL,'2026-04-03 15:13:29.125649',6,6,2),(23,'2026-04-03 15:19:46.584129','Campus','Mall','COMPLETED',NULL,'2026-04-05 07:54:11.263204',6,6,2),(24,'2026-04-03 15:20:09.499475','Campus','Mall','CANCELLED',NULL,'2026-04-03 15:21:40.005994',NULL,NULL,2),(25,'2026-04-05 07:45:51.750429','Centria University','City Center','CANCELLED',NULL,'2026-04-05 07:46:50.592446',NULL,NULL,7),(26,'2026-04-05 07:47:28.131385','City Center','Kokkola Bus Station','COMPLETED',NULL,'2026-04-05 07:50:11.686777',6,6,7),(27,'2026-04-05 08:03:14.500395','Centria University','City Center','COMPLETED',NULL,'2026-04-05 08:13:06.586273',7,6,7),(28,'2026-04-05 08:09:20.735087','Kokkola Bus Station','Chydenia Shopping Center','COMPLETED',NULL,'2026-04-05 08:14:17.457560',7,6,7),(29,'2026-04-05 08:12:31.009657','Kokkola Railway Station','Centria University','CANCELLED',NULL,'2026-04-05 08:19:22.291157',NULL,NULL,2),(30,'2026-04-05 08:19:30.295442','Centria University','City Center','COMPLETED',NULL,'2026-04-05 08:21:40.902560',7,6,2),(31,'2026-04-05 08:24:44.388108','Chydenia Shopping Center','Kokkola Bus Station','CANCELLED',NULL,'2026-04-05 08:25:01.707414',NULL,NULL,2),(32,'2026-04-05 08:33:24.992186','Chydenia Shopping Center','Kokkola Bus Station','COMPLETED',NULL,'2026-04-05 08:35:53.291251',7,6,2),(33,'2026-04-05 08:37:47.048193','Centria University','City Center','CANCELLED',NULL,'2026-04-05 08:38:45.548084',NULL,NULL,2),(34,'2026-04-05 09:39:25.687520','Kokkola Railway Station','Centria University','CANCELLED',NULL,'2026-04-06 15:35:20.682081',NULL,NULL,2),(35,'2026-04-06 15:36:05.516379','Chydenia Shopping Center','Kokkola Railway Station','COMPLETED',NULL,'2026-04-06 15:36:46.058540',7,6,2),(36,'2026-04-06 15:37:47.596040','Centria University','Kokkola Railway Station','CANCELLED',NULL,'2026-04-06 15:37:58.982629',NULL,NULL,2),(37,'2026-04-14 20:23:39.444059','Centria University','City Center','COMPLETED',NULL,'2026-04-14 20:28:10.555024',7,6,2),(38,'2026-04-15 21:32:07.887051','Centria University','City Center','PENDING',NULL,'2026-04-15 21:32:07.887051',NULL,NULL,2);
/*!40000 ALTER TABLE `trip_bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trip_rejections`
--

DROP TABLE IF EXISTS `trip_rejections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trip_rejections` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `driver_id` bigint NOT NULL,
  `trip_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_reject_trip_driver` (`trip_id`,`driver_id`),
  KEY `idx_reject_driver` (`driver_id`),
  KEY `idx_reject_trip` (`trip_id`),
  CONSTRAINT `fk_reject_driver` FOREIGN KEY (`driver_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_reject_trip` FOREIGN KEY (`trip_id`) REFERENCES `trip_bookings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trip_rejections`
--

LOCK TABLES `trip_rejections` WRITE;
/*!40000 ALTER TABLE `trip_rejections` DISABLE KEYS */;
INSERT INTO `trip_rejections` VALUES (1,'2026-04-15 21:34:15.600178',6,38);
/*!40000 ALTER TABLE `trip_rejections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('ACTIVE','BANNED','DELETED') COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_users_email` (`email`),
  UNIQUE KEY `uk_users_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'rider@test.com','Test Rider','demo_hash','0400000001','RIDER','ACTIVE'),(2,'alice@test.com','Alice','123456','0400000002','RIDER','ACTIVE'),(3,'163@test.com','Zheng','456789','0400000003','RIDER','ACTIVE'),(4,'248@test.com','heng','455789','0400000013','RIDER','ACTIVE'),(6,'driver@test.com','Test Driver','123456','0400000099','DRIVER','ACTIVE'),(7,'18520721982@163.com','Minghao','1234','18520721982','RIDER','ACTIVE'),(8,'admin@test.com','Admin','123456','0400000000','ADMIN','ACTIVE');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-19 10:04:40
