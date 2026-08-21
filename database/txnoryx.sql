mysqldump: [Warning] Using a password on the command line interface can be insecure.
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: txnoryx
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `agent_actions`
--

DROP TABLE IF EXISTS `agent_actions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agent_actions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action` enum('APPROVE','RETRY_PAYMENT','VERIFY_PAYMENT','ESCALATE','BLOCK') DEFAULT NULL,
  `confidence` double NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agent_actions`
--

LOCK TABLES `agent_actions` WRITE;
/*!40000 ALTER TABLE `agent_actions` DISABLE KEYS */;
INSERT INTO `agent_actions` VALUES (1,'RETRY_PAYMENT',0.92,'2026-08-26 13:45:46.903582','Payment recovered automatically','SUCCESS','txn-009'),(2,'APPROVE',0.97,'2026-08-26 13:45:47.196729','Transaction approved','APPROVED','txn-026'),(3,'RETRY_PAYMENT',0.92,'2026-08-30 15:45:07.035453','Payment recovered automatically','SUCCESS','txn-009'),(4,'RETRY_PAYMENT',0.92,'2026-08-30 16:37:40.493533','Payment recovered automatically','SUCCESS','txn-009');
/*!40000 ALTER TABLE `agent_actions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_analysis`
--

DROP TABLE IF EXISTS `ai_analysis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_analysis` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `confidence` double NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `explanation` varchar(255) DEFAULT NULL,
  `recommendation` varchar(255) DEFAULT NULL,
  `risk_level` varchar(255) DEFAULT NULL,
  `risk_score` int NOT NULL,
  `root_cause` varchar(255) DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_analysis`
--

LOCK TABLES `ai_analysis` WRITE;
/*!40000 ALTER TABLE `ai_analysis` DISABLE KEYS */;
INSERT INTO `ai_analysis` VALUES (1,0.8,'2026-08-26 12:34:56.663853','The transaction appears to be affected by suspicious transaction flag;  rather than clear fraudulent behavior.','RETRY_PAYMENT','LOW',30,'Suspicious transaction flag; ','txn-013'),(2,0.8,'2026-08-26 12:46:49.653275','The transaction appears to be affected by suspicious transaction flag;  rather than clear fraudulent behavior.','RETRY_PAYMENT','LOW',30,'Suspicious transaction flag; ','txn-013'),(3,0.8,'2026-08-26 13:15:13.278084','The transaction appears to be affected by suspicious transaction flag;  rather than clear fraudulent behavior.','RETRY_PAYMENT','LOW',30,'Suspicious transaction flag; ','txn-013'),(4,0.8,'2026-08-26 13:37:01.510110','The transaction appears to be affected by payment timeout; gateway failure;  rather than clear fraudulent behavior.','RETRY_PAYMENT','MEDIUM',35,'Payment timeout; Gateway failure; ','txn-009'),(5,0.8,'2026-08-30 15:44:53.849424','The transaction appears to be affected by suspicious transaction flag;  rather than clear fraudulent behavior.','RETRY_PAYMENT','LOW',30,'Suspicious transaction flag; ','txn-013'),(6,0.8,'2026-08-30 16:37:40.193311','The transaction appears to be affected by suspicious transaction flag;  rather than clear fraudulent behavior.','RETRY_PAYMENT','LOW',30,'Suspicious transaction flag; ','txn-013'),(7,0.8,'2026-08-30 16:37:41.380895','The transaction appears to be affected by suspicious transaction flag;  rather than clear fraudulent behavior.','RETRY_PAYMENT','LOW',30,'Suspicious transaction flag; ','txn-013');
/*!40000 ALTER TABLE `ai_analysis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fraud_analysis`
--

DROP TABLE IF EXISTS `fraud_analysis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fraud_analysis` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `recommendation` varchar(255) DEFAULT NULL,
  `risk_level` enum('LOW','MEDIUM','HIGH','CRITICAL') DEFAULT NULL,
  `risk_score` int NOT NULL,
  `suspicious` bit(1) NOT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fraud_analysis`
--

LOCK TABLES `fraud_analysis` WRITE;
/*!40000 ALTER TABLE `fraud_analysis` DISABLE KEYS */;
INSERT INTO `fraud_analysis` VALUES (1,'2026-08-30 16:55:16.319683','BLOCK_TRANSACTION','CRITICAL',85,_binary '','txn-013');
/*!40000 ALTER TABLE `fraud_analysis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recovery_actions`
--

DROP TABLE IF EXISTS `recovery_actions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recovery_actions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `attempts` int NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `probability` double NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `strategy` enum('RETRY','ALTERNATIVE_ROUTE','VERIFY','ESCALATE','BLOCK','NO_ACTION') DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recovery_actions`
--

LOCK TABLES `recovery_actions` WRITE;
/*!40000 ALTER TABLE `recovery_actions` DISABLE KEYS */;
INSERT INTO `recovery_actions` VALUES (1,0,'2026-08-26 12:52:27.924989',NULL,0.95,'ESCALATED','ESCALATE','txn-013'),(2,1,'2026-08-26 13:15:13.117864',NULL,0.87,'RECOVERED','RETRY','txn-028'),(3,0,'2026-08-26 13:37:01.674303',NULL,0.99,'NO_ACTION','NO_ACTION','txn-007'),(4,0,'2026-08-30 15:44:54.254652','No recovery required',0.99,'NO_ACTION','NO_ACTION','txn-003'),(5,2,'2026-08-30 16:37:40.780813','Payment recovered through retry',0.87,'RECOVERED','RETRY','txn-009');
/*!40000 ALTER TABLE `recovery_actions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `risk_factors`
--

DROP TABLE IF EXISTS `risk_factors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `risk_factors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `explanation` varchar(255) DEFAULT NULL,
  `factor` varchar(255) DEFAULT NULL,
  `score` int NOT NULL,
  `fraud_analysis_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6n7iuxl9gyd4ugktbr0xg0e9w` (`fraud_analysis_id`),
  CONSTRAINT `FK6n7iuxl9gyd4ugktbr0xg0e9w` FOREIGN KEY (`fraud_analysis_id`) REFERENCES `fraud_analysis` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `risk_factors`
--

LOCK TABLES `risk_factors` WRITE;
/*!40000 ALTER TABLE `risk_factors` DISABLE KEYS */;
INSERT INTO `risk_factors` VALUES (1,'Transaction has been flagged as suspicious','SUSPICIOUS_STATUS',35,1),(2,'Unusually high transaction frequency detected','HIGH_VELOCITY',30,1),(3,'Multiple recent failures detected for this user','MULTIPLE_FAILURES',20,1);
/*!40000 ALTER TABLE `risk_factors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_events`
--

DROP TABLE IF EXISTS `transaction_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_events` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_events`
--

LOCK TABLES `transaction_events` WRITE;
/*!40000 ALTER TABLE `transaction_events` DISABLE KEYS */;
INSERT INTO `transaction_events` VALUES (1,'{\"eventType\":\"TRANSACTION_CREATED\"}','TRANSACTION_CREATED','2026-08-22 11:26:49.000000','txn-001'),(2,'{\"eventType\":\"TRANSACTION_CREATED\"}','TRANSACTION_CREATED','2026-08-22 11:26:49.000000','txn-002'),(3,'{\"eventType\":\"TRANSACTION_CREATED\"}','TRANSACTION_CREATED','2026-08-22 11:26:49.000000','txn-003'),(4,'{\"eventType\":\"TRANSACTION_CREATED\"}','TRANSACTION_CREATED','2026-08-22 11:26:49.000000','txn-004'),(5,'{\"eventType\":\"TRANSACTION_CREATED\"}','TRANSACTION_CREATED','2026-08-22 11:26:49.000000','txn-005'),(6,'{\"eventType\":\"TRANSACTION_FAILED\",\"failureReason\":\"Invalid card number\"}','TRANSACTION_FAILED','2026-08-22 11:26:49.000000','txn-006'),(7,'{\"eventType\":\"TRANSACTION_FAILED\",\"failureReason\":\"Insufficient funds\"}','TRANSACTION_FAILED','2026-08-22 11:26:49.000000','txn-007'),(8,'{\"eventType\":\"TRANSACTION_FAILED\",\"failureReason\":\"UPI PIN expired\"}','TRANSACTION_FAILED','2026-08-22 11:26:49.000000','txn-008'),(9,'{\"eventType\":\"TRANSACTION_TIMED_OUT\",\"reason\":\"Payment gateway timeout\"}','TRANSACTION_TIMED_OUT','2026-08-22 11:26:49.000000','txn-009'),(10,'{\"eventType\":\"TRANSACTION_TIMED_OUT\",\"reason\":\"Bank response timeout\"}','TRANSACTION_TIMED_OUT','2026-08-22 11:26:49.000000','txn-010'),(11,'{\"eventType\":\"TRANSACTION_DECLINED\",\"reason\":\"Transaction declined by bank\"}','TRANSACTION_DECLINED','2026-08-22 11:26:49.000000','txn-011'),(12,'{\"eventType\":\"TRANSACTION_DECLINED\",\"reason\":\"Card declined\"}','TRANSACTION_DECLINED','2026-08-22 11:26:49.000000','txn-012'),(13,'{\"eventType\":\"HIGH_RISK_FLAGGED\",\"reason\":\"Large value transaction\"}','HIGH_RISK_FLAGGED','2026-08-22 11:26:49.000000','txn-013'),(14,'{\"eventType\":\"HIGH_RISK_FLAGGED\",\"reason\":\"High-risk merchant\"}','HIGH_RISK_FLAGGED','2026-08-22 11:26:49.000000','txn-014'),(15,'{\"eventType\":\"HIGH_RISK_FLAGGED\",\"reason\":\"Unusual pattern detected\"}','HIGH_RISK_FLAGGED','2026-08-22 11:26:49.000000','txn-015'),(16,'{\"eventType\":\"TRANSACTION_RECOVERED\",\"reason\":\"Recovered after retry\"}','TRANSACTION_RECOVERED','2026-08-22 11:26:49.000000','txn-016'),(17,'{\"eventType\":\"TRANSACTION_RECOVERED\",\"reason\":\"Recovered after timeout\"}','TRANSACTION_RECOVERED','2026-08-22 11:26:49.000000','txn-017'),(18,'{\"eventType\":\"TRANSACTION_CREATED\"}','TRANSACTION_CREATED','2026-08-22 11:26:54.000000','txn-018'),(19,'{\"eventType\":\"TRANSACTION_FAILED\",\"failureReason\":\"Card blocked\"}','TRANSACTION_FAILED','2026-08-22 11:26:54.000000','txn-019'),(20,'{\"eventType\":\"TRANSACTION_TIMED_OUT\",\"reason\":\"Gateway timeout\"}','TRANSACTION_TIMED_OUT','2026-08-22 11:26:54.000000','txn-020'),(21,'{\"eventType\":\"TRANSACTION_CREATED\"}','TRANSACTION_CREATED','2026-08-22 11:26:54.000000','txn-021'),(22,'{\"eventType\":\"TRANSACTION_DECLINED\",\"reason\":\"Insufficient UPI balance\"}','TRANSACTION_DECLINED','2026-08-22 11:26:54.000000','txn-022'),(23,'{\"eventType\":\"HIGH_RISK_FLAGGED\",\"reason\":\"Suspicious activity\"}','HIGH_RISK_FLAGGED','2026-08-22 11:26:54.000000','txn-023'),(24,'{\"eventType\":\"TRANSACTION_RECOVERED\",\"reason\":\"Recovered after review\"}','TRANSACTION_RECOVERED','2026-08-22 11:26:54.000000','txn-024'),(25,'{\"eventType\":\"TRANSACTION_CREATED\"}','TRANSACTION_CREATED','2026-08-22 11:26:54.000000','txn-025'),(26,'{\"eventType\":\"TRANSACTION_FAILED\",\"reason\":\"Risk flagged\"}','TRANSACTION_FAILED','2026-08-22 11:26:54.000000','txn-026'),(27,'{\"eventType\":\"TRANSACTION_SUCCESS\"}','TRANSACTION_SUCCESS','2026-08-22 11:26:54.000000','txn-027'),(28,'{\"eventType\":\"TRANSACTION_TIMED_OUT\",\"reason\":\"Timeout\"}','TRANSACTION_TIMED_OUT','2026-08-22 11:26:54.000000','txn-028'),(29,'{\"eventType\":\"TRANSACTION_DECLINED\",\"reason\":\"Declined\"}','TRANSACTION_DECLINED','2026-08-22 11:26:54.000000','txn-029'),(30,'{\"eventType\":\"TRANSACTION_SUCCESS\"}','TRANSACTION_SUCCESS','2026-08-22 11:26:54.000000','txn-030'),(31,'{\"eventType\":\"HIGH_RISK_FLAGGED\",\"reason\":\"High risk\"}','HIGH_RISK_FLAGGED','2026-08-22 11:26:54.000000','txn-031'),(32,'{\"eventType\":\"TRANSACTION_RECOVERED\",\"reason\":\"Recovered\"}','TRANSACTION_RECOVERED','2026-08-22 11:26:54.000000','txn-032'),(33,'{\"eventType\":\"TRANSACTION_FAILED\",\"reason\":\"Invalid card\"}','TRANSACTION_FAILED','2026-08-22 11:26:54.000000','txn-033'),(34,'{\"eventType\":\"TRANSACTION_SUCCESS\"}','TRANSACTION_SUCCESS','2026-08-22 11:26:54.000000','txn-034'),(35,'{\"eventType\":\"TRANSACTION_DECLINED\",\"reason\":\"Declined\"}','TRANSACTION_DECLINED','2026-08-22 11:26:54.000000','txn-035'),(36,'{\"eventType\":\"TRANSACTION_SUCCESS\"}','TRANSACTION_SUCCESS','2026-08-22 11:26:58.000000','txn-036'),(37,'{\"eventType\":\"TRANSACTION_FAILED\",\"reason\":\"Insufficient funds\"}','TRANSACTION_FAILED','2026-08-22 11:26:58.000000','txn-037'),(38,'{\"eventType\":\"TRANSACTION_TIMED_OUT\",\"reason\":\"Gateway timeout\"}','TRANSACTION_TIMED_OUT','2026-08-22 11:26:58.000000','txn-038'),(39,'{\"eventType\":\"TRANSACTION_DECLINED\",\"reason\":\"Declined\"}','TRANSACTION_DECLINED','2026-08-22 11:26:58.000000','txn-039'),(40,'{\"eventType\":\"HIGH_RISK_FLAGGED\",\"reason\":\"Suspicious\"}','HIGH_RISK_FLAGGED','2026-08-22 11:26:58.000000','txn-040'),(41,'{\"eventType\":\"TRANSACTION_RECOVERED\",\"reason\":\"Recovered\"}','TRANSACTION_RECOVERED','2026-08-22 11:26:58.000000','txn-041'),(42,'{\"eventType\":\"TRANSACTION_SUCCESS\"}','TRANSACTION_SUCCESS','2026-08-22 11:26:58.000000','txn-042'),(43,'{\"eventType\":\"TRANSACTION_FAILED\",\"reason\":\"Failed\"}','TRANSACTION_FAILED','2026-08-22 11:26:58.000000','txn-043'),(44,'{\"eventType\":\"TRANSACTION_TIMED_OUT\",\"reason\":\"Timeout\"}','TRANSACTION_TIMED_OUT','2026-08-22 11:26:58.000000','txn-044'),(45,'{\"eventType\":\"TRANSACTION_DECLINED\",\"reason\":\"Declined\"}','TRANSACTION_DECLINED','2026-08-22 11:26:58.000000','txn-045'),(46,'{\"eventType\":\"TRANSACTION_SUCCESS\"}','TRANSACTION_SUCCESS','2026-08-22 11:26:58.000000','txn-046'),(47,'{\"eventType\":\"TRANSACTION_FAILED\",\"reason\":\"UPI failed\"}','TRANSACTION_FAILED','2026-08-22 11:26:58.000000','txn-047'),(48,'{\"eventType\":\"TRANSACTION_SUCCESS\"}','TRANSACTION_SUCCESS','2026-08-22 11:26:58.000000','txn-048'),(49,'{\"eventType\":\"HIGH_RISK_FLAGGED\",\"reason\":\"Suspicious\"}','HIGH_RISK_FLAGGED','2026-08-22 11:26:58.000000','txn-049'),(50,'{\"eventType\":\"TRANSACTION_RECOVERED\",\"reason\":\"Recovered\"}','TRANSACTION_RECOVERED','2026-08-22 11:26:58.000000','txn-050'),(51,'Simulated transaction created for scenario GATEWAY_TIMEOUT','TRANSACTION_CREATED','2026-08-22 07:04:12.980061','TXN-SIM-29892337'),(52,'Simulated failure: GATEWAY_TIMEOUT','TRANSACTION_FAILED','2026-08-22 07:04:12.984505','TXN-SIM-29892337'),(53,'Simulated transaction created for scenario GATEWAY_TIMEOUT','TRANSACTION_CREATED','2026-08-22 07:17:31.520035','TXN-SIM-B63D1A39'),(54,'Simulated failure: GATEWAY_TIMEOUT','TRANSACTION_FAILED','2026-08-22 07:17:31.524420','TXN-SIM-B63D1A39'),(55,'Simulated transaction created for scenario GATEWAY_TIMEOUT','TRANSACTION_CREATED','2026-08-22 08:45:02.428406','TXN-SIM-CDA70D84'),(56,'Simulated failure: GATEWAY_TIMEOUT','TRANSACTION_FAILED','2026-08-22 08:45:02.443580','TXN-SIM-CDA70D84'),(57,'Transaction created','TRANSACTION_CREATED','2026-08-26 13:39:37.421818','txn-audit-01'),(58,'Transaction created','TRANSACTION_CREATED','2026-08-28 16:55:34.020477','txn-geo-001'),(59,'Transaction created','TRANSACTION_CREATED','2026-08-28 16:55:51.682478','txn-old-base'),(60,'Transaction created','TRANSACTION_CREATED','2026-08-28 16:55:51.763322','txn-old-base2'),(61,'Transaction created','TRANSACTION_CREATED','2026-08-28 16:55:51.851165','txn-old-base3'),(62,'Transaction created','TRANSACTION_CREATED','2026-08-28 16:55:52.022156','txn-old-anomaly'),(63,'Transaction created','TRANSACTION_CREATED','2026-08-28 16:55:59.861132','txn-fail-1'),(64,'Transaction created','TRANSACTION_CREATED','2026-08-28 16:55:59.944179','txn-fail-2'),(65,'Transaction created','TRANSACTION_CREATED','2026-08-28 16:56:00.031108','txn-fail-3'),(66,'Transaction created','TRANSACTION_CREATED','2026-08-28 16:56:00.204336','txn-high-fail'),(67,'Transaction created','TRANSACTION_CREATED','2026-08-28 16:59:57.561082','txn-live-verify-01'),(68,'Transaction created','TRANSACTION_CREATED','2026-08-28 16:59:57.830765','txn-live-verify-02'),(69,'Transaction created','TRANSACTION_CREATED','2026-08-30 17:19:21.695490','TXN-1001'),(70,'Transaction created','TRANSACTION_CREATED','2026-08-30 17:19:21.827583','TXN-1002'),(71,'Transaction created','TRANSACTION_CREATED','2026-08-30 17:19:21.934434','TXN-1003'),(72,'Transaction created','TRANSACTION_CREATED','2026-08-30 17:19:22.051367','TXN-1004'),(73,'Transaction created','TRANSACTION_CREATED','2026-08-30 17:19:22.147233','TXN-1005');
/*!40000 ALTER TABLE `transaction_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(15,2) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `device_id` varchar(255) DEFAULT NULL,
  `failure_reason` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `merchant` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `transaction_id` varchar(255) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6plyfbm3wy6ds7hongoml5xbk` (`transaction_id`),
  KEY `fk_transactions_users` (`user_id`),
  CONSTRAINT `fk_transactions_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,150.00,'2026-08-22 11:26:30.000000','USD','dev-001',NULL,'NY','Amazon','UPI','SUCCESS','txn-001',1),(2,299.50,'2026-08-22 11:26:30.000000','USD','dev-002',NULL,'CA','Flipkart','CARD','SUCCESS','txn-002',2),(3,75.00,'2026-08-22 11:26:30.000000','USD','dev-003',NULL,'TX','Myntra','NET BANKING','SUCCESS','txn-003',3),(4,500.00,'2026-08-22 11:26:30.000000','USD','dev-004',NULL,'NY','Snapdeal','WALLET','SUCCESS','txn-004',1),(5,350.00,'2026-08-22 11:26:30.000000','USD','dev-005',NULL,'CA','Paytm','UPI','SUCCESS','txn-005',2),(6,100.00,'2026-08-22 11:26:34.000000','USD','dev-001','Invalid card number','NY','Uber','CARD','FAILED','txn-006',1),(7,200.00,'2026-08-22 11:26:34.000000','USD','dev-002','Insufficient funds','CA','Swiggy','NET BANKING','FAILED','txn-007',2),(8,75.00,'2026-08-22 11:26:34.000000','USD','dev-003','UPI PIN expired','TX','MakeMyTrip','UPI','FAILED','txn-008',3),(9,300.00,'2026-08-22 11:26:34.000000','USD','dev-004','Payment gateway timeout','NY','AirAsia','CARD','TIMEOUT','txn-009',1),(10,150.00,'2026-08-22 11:26:34.000000','USD','dev-005','Bank response timeout','CA','Netflix','WALLET','TIMEOUT','txn-010',2),(11,50.00,'2026-08-22 11:26:34.000000','USD','dev-001','Transaction declined by bank','TX','Paytm','UPI','DECLINED','txn-011',3),(12,250.00,'2026-08-22 11:26:34.000000','USD','dev-002','Card declined','NY','Amazon','CARD','DECLINED','txn-012',1),(13,5000.00,'2026-08-22 11:26:34.000000','USD','dev-003','Large value transaction','CA','Amazon','CARD','SUSPICIOUS','txn-013',2),(14,2000.00,'2026-08-22 11:26:34.000000','USD','dev-004','High-risk merchant','TX','Flipkart','NET BANKING','SUSPICIOUS','txn-014',3),(15,1000.00,'2026-08-22 11:26:34.000000','USD','dev-005','Unusual pattern detected','NY','Myntra','UPI','SUSPICIOUS','txn-015',1),(16,300.00,'2026-08-22 11:26:34.000000','USD','dev-001','Recovered after retry','CA','Snapdeal','WALLET','RECOVERED','txn-016',2),(17,120.00,'2026-08-22 11:26:34.000000','USD','dev-002','Recovered after timeout','TX','Uber','CARD','RECOVERED','txn-017',3),(18,85.50,'2026-08-22 11:26:38.000000','USD','dev-001',NULL,'NY','MakeMyTrip','UPI','SUCCESS','txn-018',1),(19,450.00,'2026-08-22 11:26:38.000000','USD','dev-002','Card blocked','CA','Paytm','CARD','FAILED','txn-019',2),(20,220.00,'2026-08-22 11:26:38.000000','USD','dev-003','Gateway timeout','TX','Amazon','NET BANKING','TIMEOUT','txn-020',3),(21,600.00,'2026-08-22 11:26:38.000000','USD','dev-004',NULL,'NY','Flipkart','WALLET','SUCCESS','txn-021',1),(22,180.00,'2026-08-22 11:26:38.000000','USD','dev-005','Insufficient UPI balance','CA','Swiggy','UPI','DECLINED','txn-022',2),(23,350.00,'2026-08-22 11:26:38.000000','USD','dev-001','Suspicious activity','TX','Myntra','CARD','SUSPICIOUS','txn-023',3),(24,550.00,'2026-08-22 11:26:38.000000','USD','dev-002','Recovered after review','NY','Amazon','NET BANKING','RECOVERED','txn-024',1),(25,95.00,'2026-08-22 11:26:38.000000','USD','dev-003',NULL,'CA','Uber','UPI','SUCCESS','txn-025',2),(26,700.00,'2026-08-22 11:26:38.000000','USD','dev-004','Risk flagged','TX','Flipkart','CARD','FAILED','txn-026',3),(27,300.00,'2026-08-22 11:26:38.000000','USD','dev-005',NULL,'NY','Paytm','WALLET','SUCCESS','txn-027',1),(28,250.00,'2026-08-22 11:26:38.000000','USD','dev-001','Timeout','CA','MakeMyTrip','UPI','TIMEOUT','txn-028',2),(29,400.00,'2026-08-22 11:26:38.000000','USD','dev-002','Declined','TX','Amazon','CARD','DECLINED','txn-029',3),(30,150.00,'2026-08-22 11:26:38.000000','USD','dev-003',NULL,'NY','Swiggy','NET BANKING','SUCCESS','txn-030',1),(31,500.00,'2026-08-22 11:26:43.000000','USD','dev-004','High risk','CA','Flipkart','UPI','SUSPICIOUS','txn-031',2),(32,350.00,'2026-08-22 11:26:43.000000','USD','dev-005','Recovered','TX','Myntra','WALLET','RECOVERED','txn-032',3),(33,200.00,'2026-08-22 11:26:43.000000','USD','dev-001','Invalid card','NY','Amazon','CARD','FAILED','txn-033',1),(34,600.00,'2026-08-22 11:26:43.000000','USD','dev-002',NULL,'CA','Paytm','NET BANKING','SUCCESS','txn-034',2),(35,180.00,'2026-08-22 11:26:43.000000','USD','dev-003','Declined','TX','Swiggy','UPI','DECLINED','txn-035',3),(36,800.00,'2026-08-22 11:26:43.000000','USD','dev-004',NULL,'NY','Flipkart','CARD','SUCCESS','txn-036',1),(37,300.00,'2026-08-22 11:26:43.000000','USD','dev-005','Insufficient funds','CA','Amazon','WALLET','FAILED','txn-037',2),(38,220.00,'2026-08-22 11:26:43.000000','USD','dev-001','Gateway timeout','TX','Myntra','UPI','TIMEOUT','txn-038',3),(39,450.00,'2026-08-22 11:26:43.000000','USD','dev-002','Declined','NY','Paytm','NET BANKING','DECLINED','txn-039',1),(40,550.00,'2026-08-22 11:26:43.000000','USD','dev-003','Suspicious','CA','Swiggy','CARD','SUSPICIOUS','txn-040',2),(41,120.00,'2026-08-22 11:26:43.000000','USD','dev-004','Recovered','TX','Flipkart','UPI','RECOVERED','txn-041',3),(42,900.00,'2026-08-22 11:26:43.000000','USD','dev-005',NULL,'NY','Amazon','CARD','SUCCESS','txn-042',1),(43,275.00,'2026-08-22 11:26:43.000000','USD','dev-001','Failed','CA','Myntra','NET BANKING','FAILED','txn-043',2),(44,350.00,'2026-08-22 11:26:43.000000','USD','dev-002','Timeout','TX','Amazon','UPI','TIMEOUT','txn-044',3),(45,650.00,'2026-08-22 11:26:43.000000','USD','dev-003','Declined','NY','Swiggy','CARD','DECLINED','txn-045',1),(46,400.00,'2026-08-22 11:26:43.000000','USD','dev-004',NULL,'CA','Flipkart','WALLET','SUCCESS','txn-046',2),(47,250.00,'2026-08-22 11:26:43.000000','USD','dev-005','UPI failed','TX','Myntra','UPI','FAILED','txn-047',3),(48,700.00,'2026-08-22 11:26:43.000000','USD','dev-001',NULL,'NY','Amazon','NET BANKING','SUCCESS','txn-048',1),(49,300.00,'2026-08-22 11:26:43.000000','USD','dev-002','Suspicious','CA','Swiggy','CARD','SUSPICIOUS','txn-049',2),(50,500.00,'2026-08-22 11:26:43.000000','USD','dev-003','Recovered','TX','Amazon','UPI','RECOVERED','txn-050',3),(51,1499.00,'2026-08-22 07:04:12.933798','INR','SIM-DEVICE','GATEWAY_TIMEOUT','Simulation','Simulated Merchant','UPI','FAILED','TXN-SIM-29892337',1),(52,1499.00,'2026-08-22 07:17:31.512021','INR','SIM-DEVICE','GATEWAY_TIMEOUT','Simulation','Simulated Merchant','UPI','FAILED','TXN-SIM-B63D1A39',1),(53,1499.00,'2026-08-22 08:45:02.375204','INR','SIM-DEVICE','GATEWAY_TIMEOUT','Simulation','Simulated Merchant','UPI','FAILED','TXN-SIM-CDA70D84',1),(54,100.00,'2026-08-26 13:39:37.367486','USD','dev-001',NULL,'NY','Test','UPI','PENDING','txn-audit-01',1),(55,1200.00,'2026-08-28 16:55:33.915508','INR','dev-NEW-999',NULL,'London','Mumbai Store','UPI','PENDING','txn-geo-001',1),(56,500.00,'2026-08-28 16:55:51.671249','INR','dev-OLD-1',NULL,'Delhi','Delhi Store','UPI','PENDING','txn-old-base',4),(57,600.00,'2026-08-28 16:55:51.754869','INR','dev-OLD-1',NULL,'Delhi','Delhi Store','UPI','PENDING','txn-old-base2',4),(58,700.00,'2026-08-28 16:55:51.840199','INR','dev-OLD-1',NULL,'Delhi','Delhi Store','UPI','PENDING','txn-old-base3',4),(59,800.00,'2026-08-28 16:55:52.011031','INR','dev-NEW-XYZ',NULL,'London','London Store','UPI','PENDING','txn-old-anomaly',4),(60,100.00,'2026-08-28 16:55:59.850648','INR','dev-OLD-1',NULL,'Delhi','Fail Store','CARD','FAILED','txn-fail-1',4),(61,100.00,'2026-08-28 16:55:59.936162','INR','dev-OLD-1',NULL,'Delhi','Fail Store','CARD','FAILED','txn-fail-2',4),(62,100.00,'2026-08-28 16:56:00.024060','INR','dev-OLD-1',NULL,'Delhi','Fail Store','CARD','FAILED','txn-fail-3',4),(63,75000.00,'2026-08-28 16:56:00.195356','INR','dev-OLD-1',NULL,'Delhi','Luxury Store','CARD','PENDING','txn-high-fail',4),(64,999.00,'2026-08-28 16:59:57.485430','INR','dev-OLD-1',NULL,'Delhi','Verify Store','UPI','FAILED','txn-live-verify-01',4),(65,100.00,'2026-08-28 16:59:57.823886','INR',NULL,NULL,NULL,'Verify2','UPI','PENDING','txn-live-verify-02',4),(66,500.00,'2026-08-30 17:19:21.679509','INR','dev-demo-1',NULL,'Mumbai','Demo Store','UPI','SUCCESS','TXN-1001',1),(67,75000.00,'2026-08-30 17:19:21.818421','INR','dev-demo-2','Gateway Timeout','Delhi','Demo Store','CARD','TIMEOUT','TXN-1002',1),(68,92000.00,'2026-08-30 17:19:21.924302','INR','dev-demo-3','Insufficient funds','Bangalore','Demo Store','CARD','DECLINED','TXN-1003',1),(69,150000.00,'2026-08-30 17:19:22.041580','INR','dev-NEW-1004','High velocity','London','Demo Store','UPI','SUSPICIOUS','TXN-1004',1),(70,35000.00,'2026-08-30 17:19:22.138508','INR','dev-demo-5','Gateway Timeout','Chennai','Demo Store','WALLET','TIMEOUT','TXN-1005',2);
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2026-08-22 11:26:26.000000','john@example.com','John Doe',NULL,NULL),(2,'2026-08-22 11:26:26.000000','jane@example.com','Jane Smith',NULL,NULL),(3,'2026-08-22 11:26:26.000000','bob@example.com','Bob Wilson',NULL,NULL),(4,'2026-07-29 22:25:51.000000','olduser@test.com','Old User','9999999999','USER');
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

-- Dump completed on 2026-08-30 22:49:24
