-- MySQL dump 10.13  Distrib 5.1.41, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: demo
-- ------------------------------------------------------
-- Server version	5.1.41-3ubuntu12.10-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointment` (
  `appointment_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `calendar_id` int(10) unsigned NOT NULL,
  `organizer_id` int(11) NOT NULL,
  `type` varchar(25) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `start_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_date` date NOT NULL,
  `end_time` time NOT NULL,
  `status` varchar(25) DEFAULT NULL,
  `notes` text,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`appointment_id`),
  KEY `appointment__calendar` (`calendar_id`),
  CONSTRAINT `appointment__calendar` FOREIGN KEY (`calendar_id`) REFERENCES `calendar` (`calendar_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointment_change`
--

DROP TABLE IF EXISTS `appointment_change`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointment_change` (
  `appointment_change_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `appointment_id` int(10) unsigned NOT NULL,
  `type` varchar(25) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `change_by` int(10) NOT NULL,
  `change_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`appointment_change_id`),
  KEY `appointment_change__appointment` (`appointment_id`),
  KEY `appointment_change__change_by` (`change_by`),
  CONSTRAINT `appointment_change__appointment` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`appointment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `appointment_change__change_by` FOREIGN KEY (`change_by`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment_change`
--

LOCK TABLES `appointment_change` WRITE;
/*!40000 ALTER TABLE `appointment_change` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointment_change` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attendee`
--

DROP TABLE IF EXISTS `attendee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attendee` (
  `attendee_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `appointment_id` int(10) unsigned NOT NULL,
  `user_id` int(10) NOT NULL,
  `role` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  `notes` varchar(100) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`attendee_id`),
  KEY `attendee__appointment` (`appointment_id`),
  KEY `attendee__user_id` (`user_id`),
  CONSTRAINT `attendee__appointment` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`appointment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `attendee__user_id` FOREIGN KEY (`user_id`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendee`
--

LOCK TABLES `attendee` WRITE;
/*!40000 ALTER TABLE `attendee` DISABLE KEYS */;
/*!40000 ALTER TABLE `attendee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `audit_entity`
--

DROP TABLE IF EXISTS `audit_entity`;
/*!50001 DROP VIEW IF EXISTS `audit_entity`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `audit_entity` (
  `audit_entity_id` int(11),
  `audit_event_id` int(11),
  `entity_id` int(11),
  `entity` varchar(100),
  `entity_type` varchar(100),
  `audit_type` varchar(10),
  `modified` timestamp
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `audit_entity_history`
--

DROP TABLE IF EXISTS `audit_entity_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_entity_history` (
  `audit_entity_id` int(10) NOT NULL AUTO_INCREMENT,
  `audit_event_id` int(10) NOT NULL,
  `entity_id` int(10) NOT NULL,
  `entity` varchar(100) NOT NULL,
  `entity_type` varchar(100) NOT NULL,
  `audit_type` varchar(10) NOT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`audit_entity_id`),
  KEY `audit_entity_history__audit_event_id` (`audit_event_id`),
  CONSTRAINT `audit_entity_history__audit_event_id` FOREIGN KEY (`audit_event_id`) REFERENCES `audit_event_history` (`audit_event_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_entity_history`
--

LOCK TABLES `audit_entity_history` WRITE;
/*!40000 ALTER TABLE `audit_entity_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_entity_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_entity_work`
--

DROP TABLE IF EXISTS `audit_entity_work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_entity_work` (
  `audit_entity_id` int(10) NOT NULL AUTO_INCREMENT,
  `audit_event_id` int(10) NOT NULL,
  `entity_id` int(10) NOT NULL,
  `entity` varchar(100) NOT NULL,
  `entity_type` varchar(100) DEFAULT NULL,
  `audit_type` varchar(10) NOT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`audit_entity_id`),
  KEY `audit_entity_work__audit_event_id` (`audit_event_id`),
  CONSTRAINT `audit_entity_work__audit_event_id` FOREIGN KEY (`audit_event_id`) REFERENCES `audit_event_work` (`audit_event_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=636 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_entity_work`
--

LOCK TABLES `audit_entity_work` WRITE;
/*!40000 ALTER TABLE `audit_entity_work` DISABLE KEYS */;
INSERT INTO `audit_entity_work` VALUES (626,9018,1179,'EnrollmentStatus','EnrollmentStatus','INSERT','2010-07-09 04:10:00'),(627,9018,8001,'Patient','Patient','INSERT','2010-07-09 04:10:00'),(628,9024,1179,'Visit','Visit','INSERT','2010-07-09 04:10:27'),(629,9045,1179,'Criteria Validation','Instrument','INSERT','2010-07-09 04:23:21'),(630,9056,1179,'Criteria Validation','Instrument','UPDATE','2010-07-09 04:28:31'),(631,9058,1179,'Criteria Validation','Instrument','UPDATE','2010-07-09 04:28:35'),(632,9066,1179,'Criteria Validation','Instrument','DELETE','2010-07-09 04:30:27'),(633,9070,1179,'Visit','Visit','DELETE','2010-07-09 04:30:31'),(634,9080,1179,'EnrollmentStatus','EnrollmentStatus','DELETE','2010-07-09 04:30:50'),(635,9080,8001,'Patient','Patient','DELETE','2010-07-09 04:30:50');
/*!40000 ALTER TABLE `audit_entity_work` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `audit_event`
--

DROP TABLE IF EXISTS `audit_event`;
/*!50001 DROP VIEW IF EXISTS `audit_event`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `audit_event` (
  `audit_event_id` int(11),
  `audit_user` varchar(50),
  `audit_host` varchar(25),
  `audit_timestamp` timestamp,
  `action` varchar(255),
  `action_event` varchar(50),
  `action_id_param` varchar(50),
  `event_note` varchar(255),
  `exception` varchar(255),
  `exception_message` varchar(255),
  `modified` timestamp
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `audit_event_history`
--

DROP TABLE IF EXISTS `audit_event_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_event_history` (
  `audit_event_id` int(10) NOT NULL AUTO_INCREMENT,
  `audit_user` varchar(50) NOT NULL,
  `audit_host` varchar(25) NOT NULL,
  `audit_timestamp` timestamp NULL DEFAULT NULL,
  `action` varchar(255) NOT NULL,
  `action_event` varchar(50) NOT NULL,
  `action_id_param` varchar(50) DEFAULT NULL,
  `event_note` varchar(255) DEFAULT NULL,
  `exception` varchar(255) DEFAULT NULL,
  `exception_message` varchar(255) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`audit_event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_event_history`
--

LOCK TABLES `audit_event_history` WRITE;
/*!40000 ALTER TABLE `audit_event_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_event_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_event_work`
--

DROP TABLE IF EXISTS `audit_event_work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_event_work` (
  `audit_event_id` int(10) NOT NULL AUTO_INCREMENT,
  `audit_user` varchar(50) NOT NULL,
  `audit_host` varchar(25) NOT NULL,
  `audit_timestamp` timestamp NULL DEFAULT NULL,
  `action` varchar(255) NOT NULL,
  `action_event` varchar(50) NOT NULL,
  `action_id_param` varchar(50) DEFAULT NULL,
  `event_note` varchar(255) DEFAULT NULL,
  `exception` varchar(255) DEFAULT NULL,
  `exception_message` varchar(255) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`audit_event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_event_work`
--

LOCK TABLES `audit_event_work` WRITE;
/*!40000 ALTER TABLE `audit_event_work` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_event_work` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `audit_property`
--

DROP TABLE IF EXISTS `audit_property`;
/*!50001 DROP VIEW IF EXISTS `audit_property`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `audit_property` (
  `audit_property_id` int(11),
  `audit_entity_id` int(11),
  `property` varchar(100),
  `index_key` varchar(100),
  `subproperty` varchar(255),
  `old_value` varchar(255),
  `new_value` varchar(255),
  `audit_timestamp` timestamp,
  `modified` timestamp
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `audit_property_history`
--

DROP TABLE IF EXISTS `audit_property_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_property_history` (
  `audit_property_id` int(10) NOT NULL AUTO_INCREMENT,
  `audit_entity_id` int(10) NOT NULL,
  `property` varchar(100) NOT NULL,
  `index_key` varchar(100) DEFAULT NULL,
  `subproperty` varchar(255) DEFAULT NULL,
  `old_value` varchar(255) NOT NULL,
  `new_value` varchar(255) NOT NULL,
  `audit_timestamp` timestamp NULL DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`audit_property_id`),
  KEY `audit_property_history__audit_entity_id` (`audit_entity_id`),
  CONSTRAINT `audit_property_history__audit_entity_id` FOREIGN KEY (`audit_entity_id`) REFERENCES `audit_entity_history` (`audit_entity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_property_history`
--

LOCK TABLES `audit_property_history` WRITE;
/*!40000 ALTER TABLE `audit_property_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_property_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_property_work`
--

DROP TABLE IF EXISTS `audit_property_work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_property_work` (
  `audit_property_id` int(10) NOT NULL AUTO_INCREMENT,
  `audit_entity_id` int(10) NOT NULL,
  `property` varchar(100) NOT NULL,
  `index_key` varchar(100) DEFAULT NULL,
  `subproperty` varchar(255) DEFAULT NULL,
  `old_value` varchar(255) NOT NULL,
  `new_value` varchar(255) NOT NULL,
  `audit_timestamp` timestamp NULL DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`audit_property_id`),
  KEY `audit_property_work__audit_entity_id` (`audit_entity_id`),
  CONSTRAINT `audit_property_work__audit_entity_id` FOREIGN KEY (`audit_entity_id`) REFERENCES `audit_entity_work` (`audit_entity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_property_work`
--

LOCK TABLES `audit_property_work` WRITE;
/*!40000 ALTER TABLE `audit_property_work` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_property_work` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `audit_text`
--

DROP TABLE IF EXISTS `audit_text`;
/*!50001 DROP VIEW IF EXISTS `audit_text`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `audit_text` (
  `audit_property_id` int(11),
  `old_text` text,
  `new_text` text
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `audit_text_history`
--

DROP TABLE IF EXISTS `audit_text_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_text_history` (
  `audit_property_id` int(10) NOT NULL,
  `old_text` text,
  `new_text` text,
  PRIMARY KEY (`audit_property_id`),
  KEY `audit_text_history__audit_property_id` (`audit_property_id`),
  CONSTRAINT `audit_text_history__audit_property_id` FOREIGN KEY (`audit_property_id`) REFERENCES `audit_property_history` (`audit_property_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_text_history`
--

LOCK TABLES `audit_text_history` WRITE;
/*!40000 ALTER TABLE `audit_text_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_text_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_text_work`
--

DROP TABLE IF EXISTS `audit_text_work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_text_work` (
  `audit_property_id` int(10) NOT NULL,
  `old_text` text,
  `new_text` text,
  PRIMARY KEY (`audit_property_id`),
  KEY `audit_text_work__audit_property_id` (`audit_property_id`),
  CONSTRAINT `audit_text_work__audit_property_id` FOREIGN KEY (`audit_property_id`) REFERENCES `audit_property_work` (`audit_property_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_text_work`
--

LOCK TABLES `audit_text_work` WRITE;
/*!40000 ALTER TABLE `audit_text_work` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_text_work` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authgroup`
--

DROP TABLE IF EXISTS `authgroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authgroup` (
  `GID` int(10) NOT NULL AUTO_INCREMENT,
  `GroupName` varchar(50) NOT NULL,
  `EffectiveDate` date NOT NULL,
  `ExpirationDate` date DEFAULT NULL,
  `Notes` varchar(255) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`GID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authgroup`
--

LOCK TABLES `authgroup` WRITE;
/*!40000 ALTER TABLE `authgroup` DISABLE KEYS */;
INSERT INTO `authgroup` VALUES (1,'Admins','2009-01-01',NULL,'Admins (can perform all actions)','2009-01-01 08:00:00'),(2,'Coordinators','2009-01-01',NULL,'Coordinators (can perform all non-admin actions in any project)','2009-11-25 01:14:28');
/*!40000 ALTER TABLE `authgroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authpermission`
--

DROP TABLE IF EXISTS `authpermission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authpermission` (
  `PermID` int(10) NOT NULL AUTO_INCREMENT,
  `RoleID` int(10) NOT NULL,
  `PermitDeny` varchar(10) NOT NULL,
  `Scope` varchar(50) NOT NULL,
  `Module` varchar(50) NOT NULL,
  `Section` varchar(50) NOT NULL,
  `Target` varchar(50) NOT NULL,
  `Mode` varchar(25) NOT NULL,
  `Notes` varchar(100) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`PermID`),
  KEY `authpermission_RoleID` (`RoleID`),
  CONSTRAINT `authpermission_RoleID` FOREIGN KEY (`RoleID`) REFERENCES `authrole` (`RoleID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authpermission`
--

LOCK TABLES `authpermission` WRITE;
/*!40000 ALTER TABLE `authpermission` DISABLE KEYS */;
INSERT INTO `authpermission` VALUES (3,2,'PERMIT','crms','*','*','*','*',NULL,'2009-01-25 05:22:17'),(6,3,'PERMIT','crms','*','*','*','*',NULL,'2009-01-25 05:22:17'),(9,4,'PERMIT','crms','*','*','*','*',NULL,'2009-01-25 05:22:17'),(17,8,'PERMIT','crms','*','*','*','view',NULL,'2009-02-04 06:14:53'),(33,13,'PERMIT','crms','*','*','*','view','read only access to all data','2009-01-25 05:22:17'),(39,-1,'DENY','core','admin','*','*','*','Restricts access to admin module to all users by default','2009-01-25 05:22:17'),(50,3,'PERMIT','core','admin','*','*','*',NULL,'2009-02-06 19:09:33'),(57,17,'PERMIT','crms','*','*','*','view',NULL,'2009-01-25 05:22:17'),(58,13,'PERMIT','crms','specimens','*','*','*','full permission to specimens section','2009-01-25 05:22:17'),(59,16,'PERMIT','crms','*','*','*','view',NULL,'2009-01-25 05:22:17'),(60,18,'PERMIT','crms','assessment','*','*','*',NULL,'2009-01-25 05:22:17'),(67,20,'PERMIT','crms','*','*','*','*',NULL,'2009-01-25 05:22:17'),(68,4,'PERMIT','core','reporting','*','*','*',NULL,'2009-01-31 16:41:50'),(69,4,'PERMIT','core','home','prefs','*','*',NULL,'2009-08-21 21:25:14'),(70,3,'PERMIT','core','home','prefs','*','*',NULL,'2009-08-21 21:25:34');
/*!40000 ALTER TABLE `authpermission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authrole`
--

DROP TABLE IF EXISTS `authrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authrole` (
  `RoleID` int(10) NOT NULL AUTO_INCREMENT,
  `RoleName` varchar(25) NOT NULL,
  `Notes` varchar(255) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`RoleID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authrole`
--

LOCK TABLES `authrole` WRITE;
/*!40000 ALTER TABLE `authrole` DISABLE KEYS */;
INSERT INTO `authrole` VALUES (-1,'DEFAULT_PERMISSIONS','This role groups together default permissions that apply to all roles','2009-01-25 05:22:52'),(2,'DATA MANAGER','Data Managers: staff who need full access to data and functionality for the purposes of data entry, cleanup, and auditing','2009-01-25 05:22:52'),(3,'SYSTEM ADMIN','System Admin: staff who need full access to administrative functionality and read only access to data.','2009-01-25 05:22:52'),(4,'COORDINATOR','Project Coordinators: staff with responsibility for recruitment, enrollment, scheduling, assessment, and project administration','2009-03-10 18:42:16'),(8,'ASSOCIATE','Allows read only access to patients and patient data.','2009-01-25 05:22:52'),(13,'GENETIC STAFF','Staff with access to genetic information','2009-01-25 05:22:52'),(16,'REFERRER','Allows read only access to patients and data without access to protected health information.','2009-01-25 05:22:52'),(17,'AFFILIATE','Allows read only access to data associated with the patients the user already can access.','2009-01-25 05:22:52'),(18,'DATA ENTRY','This role allows full permissions to assessment module for patients that the user has access to through another role.  PHI access is not granted via this role.','2009-01-25 05:22:52'),(20,'TESTER','A role to use for testing permissions','2009-01-25 05:22:52');
/*!40000 ALTER TABLE `authrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authuser`
--

DROP TABLE IF EXISTS `authuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authuser` (
  `UID` int(10) NOT NULL AUTO_INCREMENT,
  `UserName` varchar(50) NOT NULL,
  `Login` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(25) DEFAULT NULL,
  `AccessAgreementDate` date DEFAULT NULL,
  `ShortUserName` varchar(50) DEFAULT NULL,
  `ShortUserNameRev` varchar(50) DEFAULT NULL,
  `EffectiveDate` date NOT NULL,
  `ExpirationDate` date DEFAULT NULL,
  `Notes` varchar(255) DEFAULT NULL,
  `authenticationType` varchar(10) DEFAULT 'LOCAL',
  `password` varchar(100) DEFAULT NULL,
  `passwordExpiration` timestamp NULL DEFAULT NULL,
  `passwordResetToken` varchar(100) DEFAULT NULL,
  `passwordResetExpiration` timestamp NULL DEFAULT NULL,
  `failedLoginCount` smallint(6) DEFAULT NULL,
  `lastFailedLogin` timestamp NULL DEFAULT NULL,
  `accountLocked` timestamp NULL DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`UID`),
  UNIQUE KEY `Unique_UserName` (`UserName`),
  UNIQUE KEY `Unique_Login` (`Login`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authuser`
--

LOCK TABLES `authuser` WRITE;
/*!40000 ALTER TABLE `authuser` DISABLE KEYS */;
INSERT INTO `authuser` VALUES (2,'Joe Hesse','jhesse',NULL,NULL,'2009-01-01','J.  Hesse',' Hesse, J','2009-01-01',NULL,NULL,'UCSF AD',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2009-08-21 21:26:20'),(5,'Charlie Toohey','ctoohey',NULL,NULL,'2009-01-01','C.  Toohey',' Toohey, C','2009-01-01',NULL,NULL,'UCSF AD',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2009-08-21 21:26:26'),(10,'Test','test',NULL,NULL,'2009-07-23','Test','Test','2009-07-23',NULL,NULL,'LOCAL','11f5f0473b87bc5ee0468e4b9f8d55439d0454a8cc491608963e69f3fb4e411c',NULL,NULL,NULL,NULL,NULL,NULL,'2009-10-05 05:37:13'),(12,'Albert Lee','alee7',NULL,NULL,'2009-08-21','A.  Lee',' Lee, A','2009-08-21',NULL,NULL,'UCSF AD',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2009-08-21 21:24:41'),(29,'Admin','admin',NULL,NULL,'2009-11-24','Admin','Admin','2009-11-24',NULL,NULL,'LOCAL','d7c2b0ff8ba702e9b739962c8e8b10a5e22f0223a8e59421791ab288f054083f',NULL,NULL,NULL,NULL,NULL,NULL,'2009-11-25 02:05:23');
/*!40000 ALTER TABLE `authuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authusergroup`
--

DROP TABLE IF EXISTS `authusergroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authusergroup` (
  `UGID` int(10) NOT NULL AUTO_INCREMENT,
  `UID` int(10) NOT NULL,
  `GID` int(10) NOT NULL,
  `Notes` varchar(255) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`UGID`),
  KEY `authusergroup_UID` (`UID`),
  KEY `authusergroup_GID` (`GID`),
  CONSTRAINT `authusergroup_GID` FOREIGN KEY (`GID`) REFERENCES `authgroup` (`GID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `authusergroup_UID` FOREIGN KEY (`UID`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authusergroup`
--

LOCK TABLES `authusergroup` WRITE;
/*!40000 ALTER TABLE `authusergroup` DISABLE KEYS */;
INSERT INTO `authusergroup` VALUES (5,5,1,NULL,'2009-02-06 19:10:46'),(11,12,1,NULL,'2009-08-21 21:24:51'),(39,2,1,NULL,'2009-10-20 00:18:42'),(46,2,2,NULL,'2009-11-25 01:15:29'),(47,12,2,NULL,'2009-11-25 01:15:39'),(48,5,2,NULL,'2009-11-25 01:15:49'),(49,10,2,NULL,'2009-11-25 01:16:00'),(50,29,1,NULL,'2009-11-25 02:06:08'),(51,29,2,NULL,'2009-11-25 02:06:16');
/*!40000 ALTER TABLE `authusergroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authuserrole`
--

DROP TABLE IF EXISTS `authuserrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authuserrole` (
  `URID` int(10) NOT NULL AUTO_INCREMENT,
  `RoleID` int(10) NOT NULL,
  `UID` int(10) DEFAULT NULL,
  `GID` int(10) DEFAULT NULL,
  `Notes` varchar(255) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`URID`),
  KEY `authuserrole_GID` (`GID`),
  KEY `authuserrole_RoleID` (`RoleID`),
  KEY `authuserrole_UID` (`UID`),
  CONSTRAINT `authuserrole_GID` FOREIGN KEY (`GID`) REFERENCES `authgroup` (`GID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `authuserrole_RoleID` FOREIGN KEY (`RoleID`) REFERENCES `authrole` (`RoleID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `authuserrole_UID` FOREIGN KEY (`UID`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authuserrole`
--

LOCK TABLES `authuserrole` WRITE;
/*!40000 ALTER TABLE `authuserrole` DISABLE KEYS */;
INSERT INTO `authuserrole` VALUES (1,3,NULL,1,'','2009-01-01 08:00:00'),(3,4,NULL,2,'','2009-01-01 08:00:00');
/*!40000 ALTER TABLE `authuserrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `calendar`
--

DROP TABLE IF EXISTS `calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calendar` (
  `calendar_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(25) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `notes` text,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`calendar_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calendar`
--

LOCK TABLES `calendar` WRITE;
/*!40000 ALTER TABLE `calendar` DISABLE KEYS */;
/*!40000 ALTER TABLE `calendar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `caregiver`
--

DROP TABLE IF EXISTS `caregiver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `caregiver` (
  `CareID` int(10) NOT NULL AUTO_INCREMENT,
  `PIDN` int(10) NOT NULL,
  `Lname` varchar(25) NOT NULL,
  `FName` varchar(25) NOT NULL,
  `Gender` tinyint(3) DEFAULT NULL,
  `PTRelation` varchar(25) DEFAULT NULL,
  `LivesWithPT` smallint(5) DEFAULT NULL,
  `PrimaryLanguage` varchar(25) DEFAULT NULL,
  `TransNeeded` smallint(5) DEFAULT NULL,
  `TransLanguage` varchar(25) DEFAULT NULL,
  `IsPrimContact` smallint(5) DEFAULT NULL,
  `IsContact` smallint(5) DEFAULT NULL,
  `IsContactNotes` varchar(100) DEFAULT NULL,
  `IsCaregiver` smallint(5) DEFAULT NULL,
  `IsInformant` smallint(5) DEFAULT NULL,
  `IsNextOfKin` smallint(5) DEFAULT NULL,
  `IsResearchSurrogate` smallint(5) DEFAULT NULL,
  `IsPowerOfAttorney` smallint(5) DEFAULT NULL,
  `IsOtherRole` smallint(5) DEFAULT NULL,
  `OtherRoleDesc` varchar(50) DEFAULT NULL,
  `Note` varchar(255) DEFAULT NULL,
  `ActiveFlag` smallint(5) DEFAULT '1',
  `DOB` datetime DEFAULT NULL,
  `Educ` tinyint(3) DEFAULT NULL,
  `Race` varchar(25) DEFAULT NULL,
  `MaritalStatus` varchar(25) DEFAULT NULL,
  `Occupation` varchar(25) DEFAULT NULL,
  `Age` int(10) DEFAULT NULL,
  `FullName` varchar(100) DEFAULT NULL,
  `FullNameRev` varchar(100) DEFAULT NULL,
  `ContactDesc` varchar(255) DEFAULT NULL,
  `RolesDesc` varchar(255) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`CareID`),
  KEY `caregiver__PIDN` (`PIDN`),
  CONSTRAINT `caregiver__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caregiver`
--

LOCK TABLES `caregiver` WRITE;
/*!40000 ALTER TABLE `caregiver` DISABLE KEYS */;
/*!40000 ALTER TABLE `caregiver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contactinfo`
--

DROP TABLE IF EXISTS `contactinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contactinfo` (
  `CInfoID` int(10) NOT NULL AUTO_INCREMENT,
  `PIDN` int(10) NOT NULL,
  `CareID` int(10) DEFAULT NULL,
  `ContactPT` smallint(5) DEFAULT NULL,
  `IsPTResidence` smallint(5) DEFAULT NULL,
  `OptOutMAC` smallint(5) DEFAULT '0',
  `OptOutAffiliates` smallint(5) DEFAULT '0',
  `ActiveFlag` smallint(5) DEFAULT '1',
  `Address` varchar(100) DEFAULT NULL,
  `Address2` varchar(100) DEFAULT NULL,
  `City` varchar(50) DEFAULT NULL,
  `State` char(10) DEFAULT NULL,
  `Zip` varchar(10) DEFAULT NULL,
  `Country` varchar(50) DEFAULT NULL,
  `Phone1` varchar(25) DEFAULT NULL,
  `PhoneType1` varchar(10) DEFAULT NULL,
  `Phone2` varchar(25) DEFAULT NULL,
  `PhoneType2` varchar(10) DEFAULT NULL,
  `Phone3` varchar(25) DEFAULT NULL,
  `PhoneType3` varchar(10) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Notes` varchar(250) DEFAULT NULL,
  `ContactNameRev` varchar(100) DEFAULT NULL,
  `ContactDesc` varchar(100) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`CInfoID`),
  KEY `contactinfo__PIDN` (`PIDN`),
  KEY `contactinfo__CareID` (`CareID`),
  CONSTRAINT `contactinfo__CareID` FOREIGN KEY (`CareID`) REFERENCES `caregiver` (`CareID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `contactinfo__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contactinfo`
--

LOCK TABLES `contactinfo` WRITE;
/*!40000 ALTER TABLE `contactinfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `contactinfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contactlog`
--

DROP TABLE IF EXISTS `contactlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contactlog` (
  `LogID` int(10) NOT NULL AUTO_INCREMENT,
  `PIDN` int(10) NOT NULL,
  `ProjName` varchar(75) DEFAULT NULL,
  `LogDate` date DEFAULT NULL,
  `LogTime` time DEFAULT NULL,
  `Method` varchar(25) NOT NULL DEFAULT 'Phone',
  `StaffInit` smallint(5) NOT NULL DEFAULT '1',
  `Staff` varchar(50) DEFAULT NULL,
  `Contact` varchar(50) DEFAULT NULL,
  `Note` text,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`LogID`),
  KEY `contactlog__PIDN` (`PIDN`),
  KEY `contactlog__ProjName` (`ProjName`),
  CONSTRAINT `contactlog__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `contactlog__ProjName` FOREIGN KEY (`ProjName`) REFERENCES `projectunit` (`ProjUnitDesc`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contactlog`
--

LOCK TABLES `contactlog` WRITE;
/*!40000 ALTER TABLE `contactlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `contactlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crmsauthrole`
--

DROP TABLE IF EXISTS `crmsauthrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crmsauthrole` (
  `RoleID` int(10) NOT NULL,
  `PatientAccess` smallint(5) NOT NULL DEFAULT '1',
  `PhiAccess` smallint(5) NOT NULL DEFAULT '1',
  `GhiAccess` smallint(5) NOT NULL DEFAULT '0',
  PRIMARY KEY (`RoleID`),
  KEY `crmsauthrole__RoleID` (`RoleID`),
  CONSTRAINT `crmsauthrole__RoleID` FOREIGN KEY (`RoleID`) REFERENCES `authrole` (`RoleID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crmsauthrole`
--

LOCK TABLES `crmsauthrole` WRITE;
/*!40000 ALTER TABLE `crmsauthrole` DISABLE KEYS */;
INSERT INTO `crmsauthrole` VALUES (-1,0,0,0),(2,1,1,0),(3,1,1,0),(4,1,1,0),(8,1,1,0),(13,1,1,1),(16,1,0,0),(17,0,1,0),(18,0,0,0),(20,1,1,0);
/*!40000 ALTER TABLE `crmsauthrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crmsauthuser`
--

DROP TABLE IF EXISTS `crmsauthuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crmsauthuser` (
  `UID` int(10) NOT NULL,
  PRIMARY KEY (`UID`),
  KEY `crmsauthuser__UID` (`UID`),
  CONSTRAINT `crmsauthuser__UID` FOREIGN KEY (`UID`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crmsauthuser`
--

LOCK TABLES `crmsauthuser` WRITE;
/*!40000 ALTER TABLE `crmsauthuser` DISABLE KEYS */;
INSERT INTO `crmsauthuser` VALUES (2),(5),(10),(12),(29);
/*!40000 ALTER TABLE `crmsauthuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crmsauthuserrole`
--

DROP TABLE IF EXISTS `crmsauthuserrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crmsauthuserrole` (
  `URID` int(10) NOT NULL,
  `Project` varchar(25) DEFAULT NULL,
  `Unit` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`URID`),
  KEY `crmsauthuserrole__URID` (`URID`),
  CONSTRAINT `crmsauthuserrole__URID` FOREIGN KEY (`URID`) REFERENCES `authuserrole` (`URID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crmsauthuserrole`
--

LOCK TABLES `crmsauthuserrole` WRITE;
/*!40000 ALTER TABLE `crmsauthuserrole` DISABLE KEYS */;
INSERT INTO `crmsauthuserrole` VALUES (1,'*','*'),(3,'*','*');
/*!40000 ALTER TABLE `crmsauthuserrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `doctor` (
  `DocID` int(10) NOT NULL AUTO_INCREMENT,
  `LName` varchar(25) NOT NULL,
  `MInitial` char(1) DEFAULT NULL,
  `FName` varchar(25) NOT NULL,
  `Address` varchar(100) DEFAULT NULL,
  `City` varchar(50) DEFAULT NULL,
  `State` char(2) DEFAULT NULL,
  `Zip` varchar(10) DEFAULT NULL,
  `Phone1` varchar(25) DEFAULT NULL,
  `PhoneType1` varchar(10) DEFAULT NULL,
  `Phone2` varchar(25) DEFAULT NULL,
  `PhoneType2` varchar(10) DEFAULT NULL,
  `Phone3` varchar(25) DEFAULT NULL,
  `PhoneType3` varchar(10) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `DocType` varchar(50) DEFAULT NULL,
  `FullNameRev` varchar(100) DEFAULT NULL,
  `FullName` varchar(100) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`DocID`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollmentstatus`
--

DROP TABLE IF EXISTS `enrollmentstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enrollmentstatus` (
  `EnrollStatID` int(10) NOT NULL AUTO_INCREMENT,
  `PIDN` int(10) NOT NULL,
  `ProjName` varchar(75) DEFAULT NULL,
  `SubjectStudyID` varchar(10) DEFAULT NULL,
  `ReferralSource` varchar(75) DEFAULT NULL,
  `LatestDesc` varchar(25) DEFAULT NULL,
  `LatestDate` datetime DEFAULT NULL,
  `LatestNote` varchar(100) DEFAULT NULL,
  `ReferredDesc` varchar(25) DEFAULT NULL,
  `ReferredDate` datetime DEFAULT NULL,
  `ReferredNote` varchar(100) DEFAULT NULL,
  `DeferredDesc` varchar(25) DEFAULT NULL,
  `DeferredDate` datetime DEFAULT NULL,
  `DeferredNote` varchar(100) DEFAULT NULL,
  `EligibleDesc` varchar(25) DEFAULT NULL,
  `EligibleDate` datetime DEFAULT NULL,
  `EligibleNote` varchar(100) DEFAULT NULL,
  `IneligibleDesc` varchar(25) DEFAULT NULL,
  `IneligibleDate` datetime DEFAULT NULL,
  `IneligibleNote` varchar(100) DEFAULT NULL,
  `DeclinedDesc` varchar(25) DEFAULT NULL,
  `DeclinedDate` datetime DEFAULT NULL,
  `DeclinedNote` varchar(100) DEFAULT NULL,
  `EnrolledDesc` varchar(25) DEFAULT NULL,
  `EnrolledDate` datetime DEFAULT NULL,
  `EnrolledNote` varchar(100) DEFAULT NULL,
  `ExcludedDesc` varchar(25) DEFAULT NULL,
  `ExcludedDate` datetime DEFAULT NULL,
  `ExcludedNote` varchar(100) DEFAULT NULL,
  `WithdrewDesc` varchar(25) DEFAULT NULL,
  `WithdrewDate` datetime DEFAULT NULL,
  `WithdrewNote` varchar(100) DEFAULT NULL,
  `InactiveDesc` varchar(25) DEFAULT NULL,
  `InactiveDate` datetime DEFAULT NULL,
  `InactiveNote` varchar(100) DEFAULT NULL,
  `DeceasedDesc` varchar(25) DEFAULT NULL,
  `DeceasedDate` datetime DEFAULT NULL,
  `DeceasedNote` varchar(100) DEFAULT NULL,
  `AutopsyDesc` varchar(25) DEFAULT NULL,
  `AutopsyDate` datetime DEFAULT NULL,
  `AutopsyNote` varchar(100) DEFAULT NULL,
  `ClosedDesc` varchar(25) DEFAULT NULL,
  `ClosedDate` datetime DEFAULT NULL,
  `ClosedNote` varchar(100) DEFAULT NULL,
  `EnrollmentNotes` varchar(500) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`EnrollStatID`),
  KEY `enrollmentstatus__PIDN` (`PIDN`),
  KEY `enrollmentstatus__ProjName` (`ProjName`),
  CONSTRAINT `enrollmentstatus__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `enrollmentstatus__ProjName` FOREIGN KEY (`ProjName`) REFERENCES `projectunit` (`ProjUnitDesc`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollmentstatus`
--

LOCK TABLES `enrollmentstatus` WRITE;
/*!40000 ALTER TABLE `enrollmentstatus` DISABLE KEYS */;
/*!40000 ALTER TABLE `enrollmentstatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernateproperty`
--

DROP TABLE IF EXISTS `hibernateproperty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernateproperty` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `instance` varchar(25) NOT NULL DEFAULT 'lava',
  `scope` varchar(25) NOT NULL,
  `entity` varchar(100) NOT NULL,
  `property` varchar(100) NOT NULL,
  `dbTable` varchar(50) NOT NULL,
  `dbColumn` varchar(50) NOT NULL,
  `dbType` varchar(50) DEFAULT NULL,
  `dbLength` smallint(5) DEFAULT NULL,
  `dbPrecision` smallint(5) DEFAULT NULL,
  `dbScale` smallint(5) DEFAULT NULL,
  `dbOrder` smallint(5) DEFAULT NULL,
  `hibernateProperty` varchar(50) DEFAULT NULL,
  `hibernateType` varchar(50) DEFAULT NULL,
  `hibernateClass` varchar(250) DEFAULT NULL,
  `hibernateNotNull` varchar(50) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2540 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernateproperty`
--

LOCK TABLES `hibernateproperty` WRITE;
/*!40000 ALTER TABLE `hibernateproperty` DISABLE KEYS */;
INSERT INTO `hibernateproperty` VALUES (1625,'lava','core','appointment','id','appointment','reservation_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-03-31 20:35:27'),(1626,'lava','core','appointment','calendar','appointment','calendar_id','int',NULL,10,0,2,'calendar','many-to-one','edu.ucsf.lava.core.resource.model.ResourceCalendar','Yes','2009-03-31 20:35:27'),(1627,'lava','core','appointment','organizer','appointment','organizer_id','int',NULL,10,0,3,'owner','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-03-31 20:35:27'),(1628,'lava','core','appointment','type','appointment','type','varchar',25,NULL,NULL,4,'type','string',NULL,'Yes','2009-03-31 20:35:27'),(1629,'lava','core','appointment','description','appointment','description','varchar',100,NULL,NULL,5,'description','string',NULL,'No','2009-03-31 20:35:27'),(1630,'lava','core','appointment','location','appointment','location','varchar',100,NULL,NULL,6,'location','string',NULL,'No','2009-03-31 20:35:27'),(1631,'lava','core','appointment','startDate','appointment','start_date','date',NULL,NULL,NULL,7,'startDate','date',NULL,'Yes','2009-03-31 20:35:27'),(1632,'lava','core','appointment','startTime','appointment','start_time','time',NULL,NULL,NULL,8,'startTime','time',NULL,'Yes','2009-03-31 20:35:27'),(1633,'lava','core','appointment','endDate','appointment','end_date','date',NULL,NULL,NULL,9,'endDate','date',NULL,'Yes','2009-06-02 22:27:56'),(1634,'lava','core','appointment','endTime','appointment','end_time','time',NULL,NULL,NULL,10,'endTime','time',NULL,NULL,'2009-06-02 22:28:28'),(1635,'lava','core','appointment','status','appointment','status','varchar',25,NULL,NULL,11,'status','string',NULL,'No','2009-05-11 19:45:09'),(1636,'lava','core','appointment','notes','appointment','notes','text',NULL,NULL,NULL,11,'notes','string',NULL,'No','2009-03-31 20:35:27'),(1637,'lava','core','appointment_change','id','appointment_change','appointment_change_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-05-11 20:29:54'),(1638,'lava','core','appointment_change','appointment','appointment_change','appointment_id','int',NULL,10,0,2,'appointment','many-to-one','edu.ucsf.lava.core.calendar.model.Appointment','Yes','2009-05-11 20:29:54'),(1639,'lava','core','appointment_change','type','appointment_change','type','varchar',25,NULL,NULL,3,'type','string',NULL,'Yes','2009-05-11 20:29:54'),(1640,'lava','core','appointment_change','changeBy','appointment_change','change_by','int',NULL,10,0,4,'changeBy','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-05-11 20:29:54'),(1641,'lava','core','appointment_change','changeTimestamp','appointment_change','change_timestamp','timestamp',NULL,NULL,NULL,5,'changeTimestamp','timestamp',NULL,'Yes','2009-05-11 20:29:54'),(1642,'lava','core','attendee','id','attendee','attendee_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-06-03 20:40:16'),(1643,'lava','core','attendee','appointment','attendee','appointment_id','int',NULL,10,0,2,'appointment','many-to-one','edu.ucsf.lava.core.calendar.model.Appointment','Yes','2009-06-03 20:40:16'),(1644,'lava','core','attendee','user','attendee','user_id','int',NULL,10,0,3,'user','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-06-03 20:40:16'),(1645,'lava','core','attendee','role','attendee','role','varchar',25,NULL,NULL,4,'role','string',NULL,'Yes','2009-06-03 20:40:16'),(1646,'lava','core','attendee','status','attendee','status','varchar',25,NULL,NULL,5,'status','string',NULL,'Yes','2009-06-03 20:40:16'),(1647,'lava','core','attendee','notes','attendee','notes','varchar',100,NULL,NULL,6,'notes','string',NULL,'No','2009-06-03 20:40:16'),(1648,'lava','core','auditEntity','id','Audit_Entity_Work','audit_entity_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1649,'lava','core','auditEntity','auditEvent','Audit_Entity_Work','audit_event_id','int',NULL,10,0,2,'auditEvent','many-to-one','edu.ucsf.memory.lava.model.AuditEvent','Yes','2009-01-25 05:25:56'),(1650,'lava','core','auditEntity','entityId','Audit_Entity_Work','entity_id','int',NULL,10,0,3,'entityId','long',NULL,'Yes','2009-01-25 05:25:56'),(1651,'lava','core','auditEntity','entity','Audit_Entity_Work','entity','varchar',100,NULL,NULL,4,'entity','string',NULL,'Yes','2009-01-25 05:25:56'),(1652,'lava','core','auditEntity','entityType','Audit_Entity_Work','entity_type','varchar',100,NULL,NULL,5,'entityType','string',NULL,'Yes','2009-01-25 05:25:56'),(1653,'lava','core','auditEntity','auditType','Audit_Entity_Work','audit_type','varchar',10,NULL,NULL,6,'auditType','string',NULL,'Yes','2009-01-25 05:25:56'),(1654,'lava','core','auditEntity','hversion','Audit_Entity_Work','hversion','int',NULL,10,0,7,'hversion','long',NULL,'Yes','2009-01-25 05:25:56'),(1655,'lava','core','auditEntityHistory','id','Audit_Entity','audit_entity_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1656,'lava','core','auditEntityHistory','auditEvent','Audit_Entity','audit_event_id','int',NULL,10,0,2,'auditEvent','many-to-one','edu.ucsf.memory.lava.model.AuditEvent','Yes','2009-01-25 05:25:56'),(1657,'lava','core','auditEntityHistory','entityId','Audit_Entity','entity_id','int',NULL,10,0,3,'entityId','long',NULL,'Yes','2009-01-25 05:25:56'),(1658,'lava','core','auditEntityHistory','entity','Audit_Entity','entity','varchar',100,NULL,NULL,4,'entity','string',NULL,'Yes','2009-01-25 05:25:56'),(1659,'lava','core','auditEntityHistory','entityType','Audit_Entity','entity_type','varchar',100,NULL,NULL,5,'entityType','string',NULL,'Yes','2009-01-25 05:25:56'),(1660,'lava','core','auditEntityHistory','auditType','Audit_Entity','audit_type','varchar',10,NULL,NULL,6,'auditType','string',NULL,'Yes','2009-01-25 05:25:56'),(1661,'lava','core','auditEntityHistory','hversion','Audit_Entity','hversion','int',NULL,10,0,7,'hversion','long',NULL,'Yes','2009-01-25 05:25:56'),(1662,'lava','core','auditEvent','id','Audit_Event_Work','audit_event_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1663,'lava','core','auditEvent','auditUser','Audit_Event_Work','audit_user','varchar',50,NULL,NULL,2,'auditUser','string',NULL,'Yes','2009-01-25 05:25:56'),(1664,'lava','core','auditEvent','auditHost','Audit_Event_Work','audit_host','varchar',25,NULL,NULL,3,'auditHost','string',NULL,'Yes','2009-01-25 05:25:56'),(1665,'lava','core','auditEvent','auditTimestamp','Audit_Event_Work','audit_timestamp','timestamp',NULL,23,3,4,'auditTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1666,'lava','core','auditEvent','action','Audit_Event_Work','action','varchar',255,NULL,NULL,5,'action','string',NULL,'Yes','2009-01-25 05:25:56'),(1667,'lava','core','auditEvent','actionEvent','Audit_Event_Work','action_event','varchar',50,NULL,NULL,6,'actionEvent','string',NULL,'Yes','2009-01-25 05:25:56'),(1668,'lava','core','auditEvent','actionIdParam','Audit_Event_Work','action_id_param','varchar',50,NULL,NULL,7,'actionIdParam','string',NULL,'No','2009-01-25 05:25:56'),(1669,'lava','core','auditEvent','eventNote','Audit_Event_Work','event_note','varchar',255,NULL,NULL,8,'eventNote','string',NULL,'No','2009-01-25 05:25:56'),(1670,'lava','core','auditEvent','exception','Audit_Event_Work','exception','varchar',255,NULL,NULL,9,'exception','string',NULL,'No','2009-01-25 05:25:56'),(1671,'lava','core','auditEvent','exceptionMessage','Audit_Event_Work','exception_message','varchar',255,NULL,NULL,10,'exceptionMessage','string',NULL,'No','2009-01-25 05:25:56'),(1672,'lava','core','auditEvent','hversion','Audit_Event_Work','hversion','int',NULL,10,0,11,'hversion','long',NULL,'Yes','2009-01-25 05:25:56'),(1673,'lava','core','auditEventHistory','id','Audit_Event','audit_event_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1674,'lava','core','auditEventHistory','auditUser','Audit_Event','audit_user','varchar',50,NULL,NULL,2,'auditUser','string',NULL,'Yes','2009-01-25 05:25:56'),(1675,'lava','core','auditEventHistory','auditHost','Audit_Event','audit_host','varchar',25,NULL,NULL,3,'auditHost','string',NULL,'Yes','2009-01-25 05:25:56'),(1676,'lava','core','auditEventHistory','auditTimestamp','Audit_Event','audit_timestamp','timestamp',NULL,23,3,4,'auditTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1677,'lava','core','auditEventHistory','action','Audit_Event','action','varchar',255,NULL,NULL,5,'action','string',NULL,'Yes','2009-01-25 05:25:56'),(1678,'lava','core','auditEventHistory','actionEvent','Audit_Event','action_event','varchar',50,NULL,NULL,6,'actionEvent','string',NULL,'Yes','2009-01-25 05:25:56'),(1679,'lava','core','auditEventHistory','actionIdParam','Audit_Event','action_id_param','varchar',50,NULL,NULL,7,'actionIdParam','string',NULL,'No','2009-01-25 05:25:56'),(1680,'lava','core','auditEventHistory','eventNote','Audit_Event','event_note','varchar',255,NULL,NULL,8,'eventNote','string',NULL,'No','2009-01-25 05:25:56'),(1681,'lava','core','auditEventHistory','exception','Audit_Event','exception','varchar',255,NULL,NULL,9,'exception','string',NULL,'No','2009-01-25 05:25:56'),(1682,'lava','core','auditEventHistory','exceptionMessage','Audit_Event','exception_message','varchar',255,NULL,NULL,10,'exceptionMessage','string',NULL,'No','2009-01-25 05:25:56'),(1683,'lava','core','auditEventHistory','hversion','Audit_Event','hversion','int',NULL,10,0,11,'hversion','long',NULL,'Yes','2009-01-25 05:25:56'),(1684,'lava','core','auditProperty','id','Audit_Property_Work','audit_property_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1685,'lava','core','auditProperty','auditEntity','Audit_Property_Work','audit_entity_id','int',NULL,10,0,2,'auditEntity','many-to-one','edu.ucsf.memory.lava.model.AuditEntity','Yes','2009-01-25 05:25:56'),(1686,'lava','core','auditProperty','property','Audit_Property_Work','property','varchar',100,NULL,NULL,3,'property','string',NULL,'Yes','2009-01-25 05:25:56'),(1687,'lava','core','auditProperty','indexKey','Audit_Property_Work','index_key','varchar',100,NULL,NULL,4,'indexKey','string',NULL,'No','2009-01-25 05:25:56'),(1688,'lava','core','auditProperty','subproperty','Audit_Property_Work','subproperty','varchar',255,NULL,NULL,5,'subproperty','string',NULL,'No','2009-01-25 05:25:56'),(1689,'lava','core','auditProperty','oldValue','Audit_Property_Work','old_value','varchar',255,NULL,NULL,6,'oldValue','string',NULL,'Yes','2009-01-25 05:25:56'),(1690,'lava','core','auditProperty','newValue','Audit_Property_Work','new_value','varchar',255,NULL,NULL,7,'newValue','string',NULL,'Yes','2009-01-25 05:25:56'),(1691,'lava','core','auditProperty','auditTimestamp','Audit_Property_Work','audit_timestamp','timestamp',NULL,23,3,8,'auditTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1692,'lava','core','auditProperty','oldText','Audit_Text_Work','old_text','text',16,NULL,NULL,9,'oldText','text',NULL,'No','2009-01-25 05:25:56'),(1693,'lava','core','auditProperty','newText','Audit_Text_Work','new_text','text',16,NULL,NULL,10,'newText','text',NULL,'No','2009-01-25 05:25:56'),(1694,'lava','core','auditProperty','hversion','Audit_Property_Work','hversion','int',NULL,10,0,11,'hversion','long',NULL,'No','2009-01-25 05:25:56'),(1695,'lava','core','auditPropertyHistory','id','Audit_Property','audit_property_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1696,'lava','core','auditPropertyHistory','auditEntity','Audit_Property','audit_entity_id','int',NULL,10,0,2,'auditEntity','many-to-one','edu.ucsf.memory.lava.model.AuditEntity','Yes','2009-01-25 05:25:56'),(1697,'lava','core','auditPropertyHistory','property','Audit_Property','property','varchar',100,NULL,NULL,3,'property','string',NULL,'Yes','2009-01-25 05:25:56'),(1698,'lava','core','auditPropertyHistory','indexKey','Audit_Property','index_key','varchar',100,NULL,NULL,4,'indexKey','string',NULL,'No','2009-01-25 05:25:56'),(1699,'lava','core','auditPropertyHistory','subproperty','Audit_Property','subproperty','varchar',255,NULL,NULL,5,'subproperty','string',NULL,'No','2009-01-25 05:25:56'),(1700,'lava','core','auditPropertyHistory','oldValue','Audit_Property','old_value','varchar',255,NULL,NULL,6,'oldValue','string',NULL,'Yes','2009-01-25 05:25:56'),(1701,'lava','core','auditPropertyHistory','newValue','Audit_Property','new_value','varchar',255,NULL,NULL,7,'newValue','string',NULL,'Yes','2009-01-25 05:25:56'),(1702,'lava','core','auditPropertyHistory','auditTimestamp','Audit_Property','audit_timestamp','timestamp',NULL,23,3,8,'auditTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1703,'lava','core','auditPropertyHistory','oldText','Audit_Text','old_text','text',16,NULL,NULL,9,'oldText','text',NULL,'No','2009-01-25 05:25:56'),(1704,'lava','core','auditPropertyHistory','newText','Audit_Text','new_text','text',16,NULL,NULL,10,'newText','text',NULL,'No','2009-01-25 05:25:56'),(1705,'lava','core','auditPropertyHistory','hversion','Audit_Property','hversion','int',NULL,10,0,11,'hversion','long',NULL,'No','2009-01-25 05:25:56'),(1706,'lava','core','authGroup','id','AuthGroup','GID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1707,'lava','core','authGroup','groupName','AuthGroup','GroupName','varchar',50,NULL,NULL,2,'groupName','string',NULL,'Yes','2009-01-25 05:25:56'),(1708,'lava','core','authGroup','effectiveDate','AuthGroup','EffectiveDate','date',NULL,16,0,3,'effectiveDate','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1709,'lava','core','authGroup','expirationDate','AuthGroup','ExpirationDate','date',NULL,16,0,4,'expirationDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1710,'lava','core','authGroup','notes','AuthGroup','Notes','varchar',255,NULL,NULL,5,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1711,'lava','core','authPermission','id','AuthPermission','PermID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1712,'lava','core','authPermission','role','AuthPermission','RoleID','varchar',25,NULL,NULL,2,'role','many-to-one','edu.ucsf.memory.lava.model.AuthRole','Yes','2009-01-25 05:25:56'),(1713,'lava','core','authPermission','module','AuthPermission','Module','varchar',25,NULL,NULL,4,'module','string',NULL,'Yes','2009-01-25 05:25:56'),(1714,'lava','core','authPermission','permitDeny','AuthPermission','PermitDeny','varchar',10,NULL,NULL,4,'permitDeny','string',NULL,'Yes','2009-01-25 05:25:56'),(1715,'lava','core','authPermission','section','AuthPermission','Section','varchar',25,NULL,NULL,5,'section','string',NULL,'Yes','2009-01-25 05:25:56'),(1716,'lava','core','authPermission','target','AuthPermission','Target','varchar',25,NULL,NULL,6,'target','string',NULL,'Yes','2009-01-25 05:25:56'),(1717,'lava','core','authPermission','mode','AuthPermission','Mode','varchar',25,NULL,NULL,7,'mode','string',NULL,'Yes','2009-01-25 05:25:56'),(1718,'lava','core','authPermission','notes','AuthPermission','Notes','varchar',100,NULL,NULL,10,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1719,'lava','core','authRole','id','AuthRole','RoleID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1720,'lava','core','authRole','roleName','AuthRole','RoleName','varchar',25,NULL,NULL,2,'roleName','string',NULL,'Yes','2009-01-25 05:25:56'),(1721,'lava','core','authRole','patientAccess','AuthRole','PatientAccess','smallint',NULL,5,0,3,'patientAccess','short',NULL,'Yes','2009-01-25 05:25:56'),(1722,'lava','core','authRole','phiAccess','AuthRole','PhiAccess','smallint',NULL,5,0,4,'phiAccess','short',NULL,'Yes','2009-01-25 05:25:56'),(1723,'lava','core','authRole','patientAccess','AuthRole','GhiAccess','smallint',NULL,5,0,5,'ghiAccess','short',NULL,'Yes','2009-01-25 05:25:56'),(1724,'lava','core','authRole','notes','AuthRole','Notes','varchar',255,NULL,NULL,8,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1725,'lava','core','authUser','id','AuthUser','UID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1726,'lava','core','authUser','userName','AuthUser','UserName','varchar',50,NULL,NULL,2,'userName','string',NULL,'Yes','2009-01-25 05:25:56'),(1727,'lava','core','authUser','login','AuthUser','Login','varchar',100,NULL,NULL,3,'login','string',NULL,'No','2009-01-25 05:25:56'),(1728,'lava','core','authUser','email','authuser','email','varchar',100,NULL,NULL,4,'email','string',NULL,'No','2009-05-12 18:53:20'),(1729,'lava','core','authUser','phone','authuser','phone','varchar',25,NULL,NULL,5,'phone','string',NULL,'No','2009-05-12 18:53:20'),(1730,'lava','core','authUser','accessAgreementDate','AuthUser','AccessAgreementDate','date',NULL,16,0,7,'accessAgreementDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1731,'lava','core','authUser','shortUserName','AuthUser','ShortUserName','varchar',53,NULL,NULL,8,'shortUserName','string',NULL,'No','2009-01-25 05:25:56'),(1732,'lava','core','authUser','shortUserNameRev','AuthUser','ShortUserNameRev','varchar',54,NULL,NULL,9,'shortUserNameRev','string',NULL,'No','2009-01-25 05:25:56'),(1733,'lava','core','authUser','effectiveDate','AuthUser','EffectiveDate','date',NULL,16,0,10,'effectiveDate','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1734,'lava','core','authUser','expirationDate','AuthUser','ExpirationDate','date',NULL,16,0,11,'expirationDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1735,'lava','core','authUser','authenticationType','authuser','authenticationType','varchar',10,NULL,NULL,12,'authenticationType','string',NULL,'No','2009-05-12 18:53:20'),(1736,'lava','core','authUser','notes','AuthUser','Notes','varchar',255,NULL,NULL,12,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1737,'lava','core','authUser','password','authuser','password','varchar',100,NULL,NULL,13,'password','string',NULL,'No','2009-05-12 18:53:20'),(1738,'lava','core','authUser','passwordExpiration','authuser','passwordExpiration','timestamp',NULL,NULL,NULL,14,'passwordExpiration','timestamp',NULL,'No','2009-05-12 18:53:20'),(1739,'lava','core','authUser','passwordResetToken','authuser','passwordResetToken','varchar',100,NULL,NULL,15,'passwordResetToken','string',NULL,'No','2009-05-12 18:53:20'),(1740,'lava','core','authUser','passwordResetExpiration','authuser','passwordResetExpiration','timestamp',NULL,NULL,NULL,16,'passwordResetExpiration','timestamp',NULL,'No','2009-05-12 18:53:20'),(1741,'lava','core','authUser','failedLoginCount','authuser','failedLoginCount','smallint',NULL,5,0,17,'failedLoginCount','short',NULL,'No','2009-05-12 18:53:20'),(1742,'lava','core','authUser','lastFailedLogin','authuser','lastFailedLogin','timestamp',NULL,NULL,NULL,18,'lastFailedLogin','timestamp',NULL,'No','2009-05-12 18:53:20'),(1743,'lava','core','authUser','accountLocked','authuser','accountLocked','timestamp',NULL,NULL,NULL,19,'accountLocked','timestamp',NULL,'No','2009-05-12 18:53:20'),(1744,'lava','core','authUserGroup','id','AuthUserGroup','UGID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1745,'lava','core','authUserGroup','user','AuthUserGroup','UID','int',NULL,10,0,2,'user','many-to-one','edu.ucsf.memory.lava.model.AuthUser','Yes','2009-01-25 05:25:56'),(1746,'lava','core','authUserGroup','group','AuthUserGroup','GID','int',NULL,10,0,3,'group','many-to-one','edu.ucsf.memory.lava.model.AuthGroup','Yes','2009-01-25 05:25:56'),(1747,'lava','core','authUserGroup','notes','AuthUserGroup','Notes','varchar',255,NULL,NULL,6,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1748,'lava','core','authUserRole','id','AuthUserRole','URID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1749,'lava','core','authUserRole','role','AuthUserRole','RoleID','varchar',25,NULL,NULL,3,'role','many-to-one','edu.ucsf.memory.lava.model.AuthRole','Yes','2009-01-25 05:25:56'),(1750,'lava','core','authUserRole','project','AuthUserRole','Project','varchar',25,NULL,NULL,4,'project','string',NULL,'No','2009-01-25 05:25:56'),(1751,'lava','core','authUserRole','unit','AuthUserRole','Unit','varchar',25,NULL,NULL,5,'unit','string',NULL,'No','2009-01-25 05:25:56'),(1752,'lava','core','authUserRole','user','AuthUserRole','UID','int',NULL,10,0,7,'user','many-to-one','edu.ucsf.memory.lava.model.AuthUser','No','2009-01-25 05:25:56'),(1753,'lava','core','authUserRole','group','AuthUserRole','GID','int',NULL,10,0,8,'group','many-to-one','edu.ucsf.memory.lava.model.AuthGroup','No','2009-01-25 05:25:56'),(1754,'lava','core','authUserRole','notes','AuthUserRole','Notes','varchar',255,NULL,NULL,12,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1755,'lava','core','calendar','calendar_id','calendar','calendar_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-03-31 19:09:35'),(1756,'lava','core','calendar','type','calendar','type','varchar',25,NULL,NULL,2,'type','string',NULL,'Yes','2009-06-02 22:27:08'),(1757,'lava','core','calendar','name','calendar','name','varchar',100,NULL,NULL,3,'name','string',NULL,'Yes','2009-03-31 19:09:35'),(1758,'lava','core','calendar','description','calendar','description','varchar',255,NULL,NULL,4,'description','string',NULL,'No','2009-03-31 19:09:35'),(1759,'lava','core','calendar','notes','calendar','notes','text',NULL,NULL,NULL,5,'notes','string',NULL,'No','2009-03-31 19:09:35'),(1760,'lava','core','HibernateProperty','id','HibernateProperty','id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1761,'lava','core','HibernateProperty','scope','HibernateProperty','scope','varchar',100,NULL,NULL,2,'scope','string',NULL,'Yes','2009-01-25 05:25:56'),(1762,'lava','core','HibernateProperty','entity','HibernateProperty','entity','varchar',100,NULL,NULL,3,'entity','string',NULL,'Yes','2009-01-25 05:25:56'),(1763,'lava','core','HibernateProperty','property','HibernateProperty','property','varchar',100,NULL,NULL,4,'property','string',NULL,'Yes','2009-01-25 05:25:56'),(1764,'lava','core','HibernateProperty','dbTable','HibernateProperty','dbTable','varchar',50,NULL,NULL,5,'dbTable','string',NULL,'Yes','2009-01-25 05:25:56'),(1765,'lava','core','HibernateProperty','dbColumn','HibernateProperty','dbColumn','varchar',50,NULL,NULL,6,'dbColumn','string',NULL,'Yes','2009-01-25 05:25:56'),(1766,'lava','core','HibernateProperty','dbType','HibernateProperty','dbType','varchar',50,NULL,NULL,7,'dbType','string',NULL,'No','2009-01-25 05:25:56'),(1767,'lava','core','HibernateProperty','dbLength','HibernateProperty','dbLength','smallint',NULL,5,0,8,'dbLength','short',NULL,'No','2009-01-25 05:25:56'),(1768,'lava','core','HibernateProperty','dbPrecision','HibernateProperty','dbPrecision','smallint',NULL,5,0,9,'dbPrecision','short',NULL,'No','2009-01-25 05:25:56'),(1769,'lava','core','HibernateProperty','dbScale','HibernateProperty','dbScale','smallint',NULL,5,0,10,'dbScale','short',NULL,'No','2009-01-25 05:25:56'),(1770,'lava','core','HibernateProperty','dbOrder','HibernateProperty','dbOrder','smallint',NULL,5,0,11,'dbOrder','short',NULL,'No','2009-01-25 05:25:56'),(1771,'lava','core','HibernateProperty','hibernateProperty','HibernateProperty','hibernateProperty','varchar',50,NULL,NULL,12,'hibernateProperty','string',NULL,'No','2009-01-25 05:25:56'),(1772,'lava','core','HibernateProperty','hibernateType','HibernateProperty','hibernateType','varchar',50,NULL,NULL,13,'hibernateType','string',NULL,'No','2009-01-25 05:25:56'),(1773,'lava','core','HibernateProperty','hibernateClass','HibernateProperty','hibernateClass','varchar',250,NULL,NULL,14,'hibernateClass','string',NULL,'No','2009-01-25 05:25:56'),(1774,'lava','core','HibernateProperty','hibernateNotNull','HibernateProperty','hibernateNotNull','varchar',50,NULL,NULL,15,'hibernateNotNull','string',NULL,'No','2009-01-25 05:25:56'),(1775,'lava','core','lavaSession','id','lava_session','lava_session_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1776,'lava','core','lavaSession','serverInstanceId','LavaSession','ServerInstanceID','int',NULL,10,0,2,'serverInstanceId','long',NULL,'Yes','2009-01-25 05:25:56'),(1777,'lava','core','lavaSession','createTimestamp','lava_session','create_timestamp','timestamp',NULL,23,3,3,'createTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1778,'lava','core','lavaSession','accessTimestamp','lava_session','access_timestamp','timestamp',NULL,23,3,4,'accessTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1779,'lava','core','lavaSession','expireTimestamp','lava_session','expire_timestamp','timestamp',0,23,3,5,'expireTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1780,'lava','core','lavaSession','currentStatus','lava_session','current_status','varchar',25,NULL,NULL,6,'currentStatus','string',NULL,'Yes','2009-01-25 05:25:56'),(1781,'lava','core','lavaSession','userId','lava_session','user_id','int',NULL,10,0,7,'userId','long',NULL,'No','2009-01-25 05:25:56'),(1782,'lava','core','lavaSession','username','lava_session','user_name','varchar',50,NULL,NULL,8,'username','string',NULL,'No','2009-01-25 05:25:56'),(1783,'lava','core','lavaSession','hostname','lava_session','host_name','varchar',50,NULL,NULL,9,'hostname','string',NULL,'No','2009-01-25 05:25:56'),(1784,'lava','core','lavaSession','httpSessionId','lava_session','http_session_id','int',64,NULL,NULL,10,'httpSessionId','string',NULL,'No','2009-01-25 05:25:56'),(1785,'lava','core','lavaSession','disconnectTime','lava_session','disconnect_time','time',NULL,23,3,11,'disconnectTime','time',NULL,'No','2009-01-25 05:25:56'),(1786,'lava','core','lavaSession','disconnectMessage','lava_session','disconnect_message','varchar',255,NULL,NULL,12,'disconnectMessage','string',NULL,'No','2009-01-25 05:25:56'),(1787,'lava','core','lavaSession','notes','lava_session','Notes','varchar',255,NULL,NULL,13,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1788,'lava','core','lavaSession','disconnectDate','lava_session','disconnect_date','date',0,NULL,NULL,14,'disconnectDate','date',NULL,'No','2009-04-22 18:00:00'),(1789,'lava','core','resourceCalendar','id','resource_calendar','calendar_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-06-03 17:28:00'),(1790,'lava','core','resourceCalendar','resourceType','resource_calendar','resource_type','varchar',25,NULL,NULL,2,'resourceType','string',NULL,'Yes','2009-06-03 17:28:00'),(1791,'lava','core','resourceCalendar','location','resource_calendar','location','varchar',100,NULL,NULL,3,'location','string',NULL,'No','2009-06-03 17:28:00'),(1792,'lava','core','resourceCalendar','contact','resource_calendar','contact_id','int',NULL,10,0,4,'contact','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-06-03 17:28:00'),(1793,'lava','core','ViewProperty','id','ViewProperty','id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1794,'lava','core','ViewProperty','messageCode','ViewProperty','messageCode','varchar',250,NULL,NULL,2,'messageCode','string',NULL,'No','2009-01-25 05:25:56'),(1795,'lava','core','ViewProperty','locale','ViewProperty','locale','varchar',10,NULL,NULL,3,'locale','string',NULL,'Yes','2009-01-25 05:25:56'),(1796,'lava','core','ViewProperty','scope','ViewProperty','scope','varchar',100,NULL,NULL,4,'scope','string',NULL,'Yes','2009-01-25 05:25:56'),(1797,'lava','core','ViewProperty','prefix','ViewProperty','prefix','varchar',50,NULL,NULL,5,'prefix','string',NULL,'No','2009-01-25 05:25:56'),(1798,'lava','core','ViewProperty','entity','ViewProperty','entity','varchar',100,NULL,NULL,6,'entity','string',NULL,'Yes','2009-01-25 05:25:56'),(1799,'lava','core','ViewProperty','property','ViewProperty','property','varchar',100,NULL,NULL,7,'property','string',NULL,'Yes','2009-01-25 05:25:56'),(1800,'lava','core','ViewProperty','section','ViewProperty','section','varchar',100,NULL,NULL,8,'section','string',NULL,'No','2009-01-25 05:25:56'),(1801,'lava','core','ViewProperty','context','ViewProperty','context','varchar',10,NULL,NULL,9,'context','string',NULL,'No','2009-01-25 05:25:56'),(1802,'lava','core','ViewProperty','style','ViewProperty','style','varchar',25,NULL,NULL,10,'style','string',NULL,'No','2009-01-25 05:25:56'),(1803,'lava','core','ViewProperty','list','ViewProperty','list','varchar',50,NULL,NULL,11,'list','string',NULL,'No','2009-01-25 05:25:56'),(1804,'lava','core','ViewProperty','attributes','ViewProperty','attributes','varchar',100,NULL,NULL,12,'attributes','string',NULL,'No','2009-01-25 05:25:56'),(1805,'lava','core','ViewProperty','required','ViewProperty','required','varchar',10,NULL,NULL,13,'required','string',NULL,'No','2009-01-25 05:25:56'),(1806,'lava','core','ViewProperty','label','ViewProperty','label','varchar',500,NULL,NULL,14,'label','string',NULL,'No','2009-01-25 05:25:56'),(1807,'lava','core','ViewProperty','quickHelp','ViewProperty','quickHelp','varchar',500,NULL,NULL,15,'quickHelp','string',NULL,'No','2009-01-25 05:25:56'),(1808,'lava','core','ViewProperty','propOrder','ViewProperty','propOrder','int',NULL,10,0,16,'propOrder','long',NULL,'No','2009-01-25 05:25:56'),(1809,'lava','crms','caregiver','id','Caregiver','CareID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1810,'lava','crms','caregiver','patient','Caregiver','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-25 05:25:56'),(1811,'lava','crms','caregiver','lastName','Caregiver','Lname','varchar',25,NULL,NULL,3,'lastName','string',NULL,'Yes','2009-01-25 05:25:56'),(1812,'lava','crms','caregiver','firstName','Caregiver','FName','varchar',25,NULL,NULL,4,'firstName','string',NULL,'Yes','2009-01-25 05:25:56'),(1813,'lava','crms','caregiver','gender','Caregiver','Gender','tinyint',NULL,3,0,5,'gender','byte',NULL,'No','2009-01-25 05:25:56'),(1814,'lava','crms','caregiver','relation','Caregiver','PTRelation','varchar',25,NULL,NULL,6,'relation','string',NULL,'No','2009-01-25 05:25:56'),(1815,'lava','crms','caregiver','livesWithPatient','Caregiver','LivesWithPT','smallint',NULL,5,0,7,'livesWithPatient','short',NULL,'No','2009-01-25 05:25:56'),(1816,'lava','crms','caregiver','primaryLanguage','Caregiver','PrimaryLanguage','varchar',25,NULL,NULL,8,'primaryLanguage','string',NULL,'No','2009-01-25 05:25:56'),(1817,'lava','crms','caregiver','transNeeded','Caregiver','TransNeeded','smallint',NULL,5,0,9,'transNeeded','short',NULL,'No','2009-01-25 05:25:56'),(1818,'lava','crms','caregiver','transLanguage','Caregiver','TransLanguage','varchar',25,NULL,NULL,10,'transLanguage','string',NULL,'No','2009-01-25 05:25:56'),(1819,'lava','crms','caregiver','isPrimaryContact','Caregiver','IsPrimContact','smallint',NULL,5,0,11,'isPrimaryContact','short',NULL,'No','2009-01-25 05:25:56'),(1820,'lava','crms','caregiver','isContact','Caregiver','IsContact','smallint',NULL,5,0,12,'isContact','short',NULL,'No','2009-01-25 05:25:56'),(1821,'lava','crms','caregiver','isContactNotes','Caregiver','IsContactNotes','varchar',100,NULL,NULL,13,'isContactNotes','string',NULL,'No','2009-01-25 05:25:56'),(1822,'lava','crms','caregiver','isCaregiver','Caregiver','IsCaregiver','smallint',NULL,5,0,14,'isCaregiver','short',NULL,'No','2009-01-25 05:25:56'),(1823,'lava','crms','caregiver','isInformant','Caregiver','IsInformant','smallint',NULL,5,0,15,'isInformant','short',NULL,'No','2009-01-25 05:25:56'),(1824,'lava','crms','caregiver','isResearchSurrogate','Caregiver','IsResearchSurrogate','smallint',NULL,5,0,16,'isResearchSurrogate','short',NULL,'No','2009-01-25 05:25:56'),(1825,'lava','crms','caregiver','isNextOfKin','Caregiver','IsNextOfKin','smallint',NULL,5,0,16,'isNextOfKin','short',NULL,'No','2009-01-25 05:25:56'),(1826,'lava','crms','caregiver','isPowerOfAttorney','Caregiver','IsPowerOfAttorney','smallint',NULL,5,0,17,'isPowerOfAttorney','short',NULL,'No','2009-01-25 05:25:56'),(1827,'lava','crms','caregiver','isOtherRole','Caregiver','IsOtherRole','smallint',NULL,5,0,18,'isOtherRole','short',NULL,'No','2009-01-25 05:25:56'),(1828,'lava','crms','caregiver','otherRoleDesc','Caregiver','OtherRoleDesc','varchar',50,NULL,NULL,19,'otherRoleDesc','string',NULL,'No','2009-01-25 05:25:56'),(1829,'lava','crms','caregiver','note','Caregiver','Note','varchar',255,NULL,NULL,20,'note','string',NULL,'No','2009-01-25 05:25:56'),(1830,'lava','crms','caregiver','active','Caregiver','ActiveFlag','smallint',NULL,5,0,21,'active','short',NULL,'No','2009-01-25 05:25:56'),(1831,'lava','crms','caregiver','birthDate','Caregiver','DOB','smalldatetime',NULL,16,0,22,'birthDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1832,'lava','crms','caregiver','education','Caregiver','Educ','tinyint',NULL,3,0,23,'education','byte',NULL,'No','2009-01-25 05:25:56'),(1833,'lava','crms','caregiver','race','Caregiver','Race','varchar',25,NULL,NULL,24,'race','string',NULL,'No','2009-01-25 05:25:56'),(1834,'lava','crms','caregiver','maritalStatus','Caregiver','MaritalStatus','varchar',25,NULL,NULL,25,'maritalStatus','string',NULL,'No','2009-01-25 05:25:56'),(1835,'lava','crms','caregiver','occupation','Caregiver','Occupation','varchar',25,NULL,NULL,26,'occupation','string',NULL,'No','2009-01-25 05:25:56'),(1836,'lava','crms','caregiver','age','Caregiver','Age','int',NULL,10,0,27,'age','long',NULL,'No','2009-01-25 05:25:56'),(1837,'lava','crms','caregiver','fullName','Caregiver','FullName','varchar',51,NULL,NULL,28,'fullName','string',NULL,'Yes','2009-01-25 05:25:56'),(1838,'lava','crms','caregiver','fullNameRev','Caregiver','FullNameRev','varchar',52,NULL,NULL,29,'fullNameRev','string',NULL,'Yes','2009-01-25 05:25:56'),(1839,'lava','crms','caregiver','contactDesc','Caregiver','ContactDesc','varchar',117,NULL,NULL,30,'contactDesc','string',NULL,'No','2009-01-25 05:25:56'),(1840,'lava','crms','caregiver','rolesDesc','Caregiver','RolesDesc','varchar',117,NULL,NULL,31,'rolesDesc','string',NULL,'No','2009-01-25 05:25:56'),(1841,'lava','crms','consent','consentId','PatientConsent','ConsentID','int',NULL,10,0,1,'consentId','long',NULL,'Yes','2009-01-25 05:25:56'),(1842,'lava','crms','consent','patient','PatientConsent','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-25 05:25:56'),(1843,'lava','crms','consent','caregiver','PatientConsent','CareID','int',NULL,10,0,3,'caregiver','many-to-one','edu.ucsf.memory.lava.model.Caregiver','No','2009-01-25 05:25:56'),(1844,'lava','crms','consent','projName','PatientConsent','ProjName','varchar',25,NULL,NULL,4,'projName','string',NULL,'Yes','2009-01-25 05:25:56'),(1845,'lava','crms','consent','consentType','PatientConsent','ConsentType','varchar',50,NULL,NULL,5,'consentType','string',NULL,'Yes','2009-01-25 05:25:56'),(1846,'lava','crms','consent','consentDate','PatientConsent','ConsentDate','smalldatetime',NULL,16,0,6,'consentDate','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1847,'lava','crms','consent','expirationDate','PatientConsent','ExpirationDate','smalldatetime',NULL,16,0,7,'expirationDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1848,'lava','crms','consent','withdrawlDate','PatientConsent','WithdrawlDate','smalldatetime',NULL,16,0,8,'withdrawlDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1849,'lava','crms','consent','note','PatientConsent','Note','varchar',100,NULL,NULL,9,'note','string',NULL,'No','2009-01-25 05:25:56'),(1850,'lava','crms','consent','capacityReviewBy','PatientConsent','CapacityReviewBy','varchar',25,NULL,NULL,10,'capacityReviewBy','string',NULL,'No','2009-01-25 05:25:56'),(1851,'lava','crms','consent','consentRevision','PatientConsent','ConsentRevision','varchar',10,NULL,NULL,11,'consentRevision','string',NULL,'No','2009-01-25 05:25:56'),(1852,'lava','crms','consent','consentDeclined','PatientConsent','ConsentDeclined','varchar',10,NULL,NULL,12,'consentDeclined','string',NULL,'No','2009-01-25 05:25:56'),(1853,'lava','crms','consent','research','PatientConsent','CTreasearch','varchar',10,NULL,NULL,13,'research','string',NULL,'No','2009-01-25 05:25:56'),(1854,'lava','crms','consent','neuro','PatientConsent','CTneuro','varchar',10,NULL,NULL,14,'neuro','string',NULL,'No','2009-01-25 05:25:56'),(1855,'lava','crms','consent','dna','PatientConsent','CTDNA','varchar',10,NULL,NULL,15,'dna','string',NULL,'No','2009-01-25 05:25:56'),(1856,'lava','crms','consent','genetic','PatientConsent','CTGenetic','varchar',10,NULL,NULL,16,'genetic','string',NULL,'No','2009-01-25 05:25:56'),(1857,'lava','crms','consent','geneticShare','PatientConsent','CTGeneticShare','varchar',10,NULL,NULL,17,'geneticShare','string',NULL,'No','2009-01-25 05:25:56'),(1858,'lava','crms','consent','lumbar','PatientConsent','CTlumbar','varchar',10,NULL,NULL,18,'lumbar','string',NULL,'No','2009-01-25 05:25:56'),(1859,'lava','crms','consent','video','PatientConsent','CTvideo','varchar',10,NULL,NULL,19,'video','string',NULL,'No','2009-01-25 05:25:56'),(1860,'lava','crms','consent','audio','PatientConsent','CTaudio','varchar',10,NULL,NULL,20,'audio','string',NULL,'No','2009-01-25 05:25:56'),(1861,'lava','crms','consent','mediaEdu','PatientConsent','CTmediaedu','varchar',10,NULL,NULL,21,'mediaEdu','string',NULL,'No','2009-01-25 05:25:56'),(1862,'lava','crms','consent','t1_5mri','PatientConsent','CT1point5T','varchar',10,NULL,NULL,22,'t1_5mri','string',NULL,'No','2009-01-25 05:25:56'),(1863,'lava','crms','consent','t4mri','PatientConsent','CT4t','varchar',10,NULL,NULL,23,'t4mri','string',NULL,'No','2009-01-25 05:25:56'),(1864,'lava','crms','consent','otherStudy','PatientConsent','CTotherstudy','varchar',10,NULL,NULL,24,'otherStudy','string',NULL,'No','2009-01-25 05:25:56'),(1865,'lava','crms','consent','followup','PatientConsent','CTfollowup','varchar',10,NULL,NULL,25,'followup','string',NULL,'No','2009-01-25 05:25:56'),(1866,'lava','crms','consent','music','PatientConsent','CTmusic','varchar',10,NULL,NULL,26,'music','string',NULL,'No','2009-01-25 05:25:56'),(1867,'lava','crms','consent','part','PatientConsent','CTpart','varchar',10,NULL,NULL,27,'part','string',NULL,'No','2009-01-25 05:25:56'),(1868,'lava','crms','consent','carepart','PatientConsent','CTcarepart','varchar',10,NULL,NULL,28,'carepart','string',NULL,'No','2009-01-25 05:25:56'),(1869,'lava','crms','contactInfo','id','ContactInfo','CInfoID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1870,'lava','crms','contactInfo','patient','ContactInfo','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-25 05:25:56'),(1871,'lava','crms','contactInfo','caregiverId','ContactInfo','CareID','int',NULL,10,0,3,'caregiverId','long',NULL,'No','2009-01-25 05:25:56'),(1872,'lava','crms','contactInfo','contactPatient','ContactInfo','ContactPT','smallint',NULL,5,0,4,'contactPatient','short',NULL,'No','2009-01-25 05:25:56'),(1873,'lava','crms','contactInfo','isPatientResidence','ContactInfo','IsPTResidence','smallint',NULL,5,0,5,'isPatientResidence','short',NULL,'No','2009-01-25 05:25:56'),(1874,'lava','crms','contactInfo','optOutMac','ContactInfo','OptOutMAC','smallint',NULL,5,0,6,'optOutMac','short',NULL,'No','2009-01-25 05:25:56'),(1875,'lava','crms','contactInfo','optOutAffiliates','ContactInfo','OptOutAffiliates','smallint',NULL,5,0,7,'optOutAffiliates','short',NULL,'No','2009-01-25 05:25:56'),(1876,'lava','crms','contactInfo','active','ContactInfo','ActiveFlag','smallint',NULL,5,0,8,'active','short',NULL,'No','2009-01-25 05:25:56'),(1877,'lava','crms','contactInfo','address','ContactInfo','Address','varchar',100,NULL,NULL,9,'address','string',NULL,'No','2009-01-25 05:25:56'),(1878,'lava','crms','contactInfo','address2','ContactInfo','Address2','varchar',100,NULL,NULL,10,'address2','string',NULL,'No','2009-01-25 05:25:56'),(1879,'lava','crms','contactInfo','city','ContactInfo','City','varchar',50,NULL,NULL,11,'city','string',NULL,'No','2009-01-25 05:25:56'),(1880,'lava','crms','contactInfo','state','ContactInfo','State','char',10,NULL,NULL,12,'state','character',NULL,'No','2009-01-25 05:25:56'),(1881,'lava','crms','contactInfo','zip','ContactInfo','Zip','varchar',10,NULL,NULL,13,'zip','string',NULL,'No','2009-01-25 05:25:56'),(1882,'lava','crms','contactInfo','country','ContactInfo','Country','varchar',50,NULL,NULL,14,'country','string',NULL,'No','2009-01-25 05:25:56'),(1883,'lava','crms','contactInfo','phone1','ContactInfo','Phone1','varchar',25,NULL,NULL,15,'phone1','string',NULL,'No','2009-01-25 05:25:56'),(1884,'lava','crms','contactInfo','phoneType1','ContactInfo','PhoneType1','varchar',10,NULL,NULL,16,'phoneType1','string',NULL,'No','2009-01-25 05:25:56'),(1885,'lava','crms','contactInfo','phone2','ContactInfo','Phone2','varchar',25,NULL,NULL,17,'phone2','string',NULL,'No','2009-01-25 05:25:56'),(1886,'lava','crms','contactInfo','phoneType2','ContactInfo','PhoneType2','varchar',10,NULL,NULL,18,'phoneType2','string',NULL,'No','2009-01-25 05:25:56'),(1887,'lava','crms','contactInfo','phone3','ContactInfo','Phone3','varchar',25,NULL,NULL,19,'phone3','string',NULL,'No','2009-01-25 05:25:56'),(1888,'lava','crms','contactInfo','phoneType3','ContactInfo','PhoneType3','varchar',10,NULL,NULL,20,'phoneType3','string',NULL,'No','2009-01-25 05:25:56'),(1889,'lava','crms','contactInfo','email','ContactInfo','Email','varchar',100,NULL,NULL,21,'email','string',NULL,'No','2009-01-25 05:25:56'),(1890,'lava','crms','contactInfo','notes','ContactInfo','Notes','varchar',250,NULL,NULL,22,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1891,'lava','crms','contactInfo','contactNameRev','ContactInfo','ContactNameRev','varchar',100,NULL,NULL,23,'contactNameRev','string',NULL,'No','2009-01-25 05:25:56'),(1892,'lava','crms','contactInfo','contactDesc','ContactInfo','ContactDesc','varchar',100,NULL,NULL,24,'contactDesc','string',NULL,'No','2009-01-25 05:25:56'),(1893,'lava','crms','ContactLog','id','ContactLog','LogID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1894,'lava','crms','ContactLog','patient','ContactLog','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-25 05:25:56'),(1895,'lava','crms','ContactLog','projName','ContactLog','ProjName','varchar',25,NULL,NULL,3,'projName','string',NULL,'No','2009-01-25 05:25:56'),(1896,'lava','crms','ContactLog','logDate','ContactLog','LogDate','smalldatetime',NULL,16,0,4,'logDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1897,'lava','crms','ContactLog','method','ContactLog','Method','varchar',25,NULL,NULL,5,'method','string',NULL,'Yes','2009-01-25 05:25:56'),(1898,'lava','crms','ContactLog','staffInit','ContactLog','StaffInit','smallint',NULL,5,0,6,'staffInit','short',NULL,'Yes','2009-01-25 05:25:56'),(1899,'lava','crms','ContactLog','staff','ContactLog','Staff','varchar',50,NULL,NULL,7,'staff','string',NULL,'No','2009-01-25 05:25:56'),(1900,'lava','crms','ContactLog','contact','ContactLog','Contact','varchar',50,NULL,NULL,8,'contact','string',NULL,'No','2009-01-25 05:25:56'),(1901,'lava','crms','ContactLog','note','ContactLog','Note','text',16,NULL,NULL,9,'note','string',NULL,'No','2009-01-25 05:25:56'),(1902,'lava','crms','Doctor','id','Doctor','DocID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1903,'lava','crms','Doctor','lastName','Doctor','LName','varchar',25,NULL,NULL,2,'lastName','string',NULL,'Yes','2009-01-25 05:25:56'),(1904,'lava','crms','Doctor','middleInitial','Doctor','MInitial','char',1,NULL,NULL,3,'middleInitial','character',NULL,'No','2009-01-25 05:25:56'),(1905,'lava','crms','Doctor','firstName','Doctor','FName','varchar',25,NULL,NULL,4,'firstName','string',NULL,'Yes','2009-01-25 05:25:56'),(1906,'lava','crms','Doctor','address','Doctor','Address','varchar',100,NULL,NULL,5,'address','string',NULL,'No','2009-01-25 05:25:56'),(1907,'lava','crms','Doctor','city','Doctor','City','varchar',50,NULL,NULL,6,'city','string',NULL,'No','2009-01-25 05:25:56'),(1908,'lava','crms','Doctor','state','Doctor','State','char',2,NULL,NULL,7,'state','character',NULL,'No','2009-01-25 05:25:56'),(1909,'lava','crms','Doctor','zip','Doctor','Zip','varchar',10,NULL,NULL,8,'zip','string',NULL,'No','2009-01-25 05:25:56'),(1910,'lava','crms','Doctor','phone1','Doctor','Phone1','varchar',25,NULL,NULL,9,'phone1','string',NULL,'No','2009-01-25 05:25:56'),(1911,'lava','crms','Doctor','phoneType1','Doctor','PhoneType1','varchar',10,NULL,NULL,10,'phoneType1','string',NULL,'No','2009-01-25 05:25:56'),(1912,'lava','crms','Doctor','phone2','Doctor','Phone2','varchar',25,NULL,NULL,11,'phone2','string',NULL,'No','2009-01-25 05:25:56'),(1913,'lava','crms','Doctor','phoneType2','Doctor','PhoneType2','varchar',10,NULL,NULL,12,'phoneType2','string',NULL,'No','2009-01-25 05:25:56'),(1914,'lava','crms','Doctor','phone3','Doctor','Phone3','varchar',25,NULL,NULL,13,'phone3','string',NULL,'No','2009-01-25 05:25:56'),(1915,'lava','crms','Doctor','phoneType3','Doctor','PhoneType3','varchar',10,NULL,NULL,14,'phoneType3','string',NULL,'No','2009-01-25 05:25:56'),(1916,'lava','crms','Doctor','email','Doctor','Email','varchar',100,NULL,NULL,15,'email','string',NULL,'No','2009-01-25 05:25:56'),(1917,'lava','crms','Doctor','docType','Doctor','DocType','varchar',50,NULL,NULL,16,'docType','string',NULL,'No','2009-01-25 05:25:56'),(1918,'lava','crms','Doctor','fullNameRev','Doctor','FullNameRev','varchar',55,NULL,NULL,17,'fullNameRev','string',NULL,'No','2009-01-25 05:25:56'),(1919,'lava','crms','Doctor','fullName','Doctor','FullName','varchar',54,NULL,NULL,18,'fullName','string',NULL,'No','2009-01-25 05:25:56'),(1920,'lava','crms','EnrollmentStatus','id','EnrollmentStatus','EnrollStatID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1921,'lava','crms','EnrollmentStatus','patient','EnrollmentStatus','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-25 05:25:56'),(1922,'lava','crms','EnrollmentStatus','projName','EnrollmentStatus','projName','varchar',75,NULL,NULL,3,'projName','string',NULL,'Yes','2009-01-25 05:25:56'),(1923,'lava','crms','EnrollmentStatus','subjectStudyId','EnrollmentStatus','SubjectStudyID','varchar',10,NULL,NULL,4,'studySubjectId','string',NULL,'No','2009-01-25 05:25:56'),(1924,'lava','crms','EnrollmentStatus','referralSource','EnrollmentStatus','ReferralSource','varchar',75,NULL,NULL,4,'referralSource','string',NULL,'No','2009-01-25 05:25:56'),(1925,'lava','crms','EnrollmentStatus','latestDesc','EnrollmentStatus','LatestDesc','varchar',25,NULL,NULL,5,'latestDesc','string',NULL,'No','2009-01-25 05:25:56'),(1926,'lava','crms','EnrollmentStatus','latestDate','EnrollmentStatus','LatestDate','smalldatetime',NULL,16,0,6,'latestDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1927,'lava','crms','EnrollmentStatus','latestNote','EnrollmentStatus','LatestNote','varchar',100,NULL,NULL,7,'latestNote','string',NULL,'No','2009-01-25 05:25:56'),(1928,'lava','crms','EnrollmentStatus','referredDesc','EnrollmentStatus','ReferredDesc','varchar',25,NULL,NULL,9,'referredDesc','string',NULL,'No','2009-01-25 05:25:56'),(1929,'lava','crms','EnrollmentStatus','referredDate','EnrollmentStatus','ReferredDate','smalldatetime',NULL,16,0,10,'referredDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1930,'lava','crms','EnrollmentStatus','referredNote','EnrollmentStatus','ReferredNote','varchar',100,NULL,NULL,11,'referredNote','string',NULL,'No','2009-01-25 05:25:56'),(1931,'lava','crms','EnrollmentStatus','deferredDesc','EnrollmentStatus','DeferredDesc','varchar',25,NULL,NULL,13,'deferredDesc','string',NULL,'No','2009-01-25 05:25:56'),(1932,'lava','crms','EnrollmentStatus','deferredDate','EnrollmentStatus','DeferredDate','smalldatetime',NULL,16,0,14,'deferredDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1933,'lava','crms','EnrollmentStatus','deferredNote','EnrollmentStatus','DeferredNote','varchar',100,NULL,NULL,15,'deferredNote','string',NULL,'No','2009-01-25 05:25:56'),(1934,'lava','crms','EnrollmentStatus','eligibleDesc','EnrollmentStatus','EligibleDesc','varchar',25,NULL,NULL,17,'eligibleDesc','string',NULL,'No','2009-01-25 05:25:56'),(1935,'lava','crms','EnrollmentStatus','eligibleDate','EnrollmentStatus','EligibleDate','smalldatetime',NULL,16,0,18,'eligibleDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1936,'lava','crms','EnrollmentStatus','eligibleNote','EnrollmentStatus','EligibleNote','varchar',100,NULL,NULL,19,'eligibleNote','string',NULL,'No','2009-01-25 05:25:56'),(1937,'lava','crms','EnrollmentStatus','ineligibleDesc','EnrollmentStatus','IneligibleDesc','varchar',25,NULL,NULL,21,'ineligibleDesc','string',NULL,'No','2009-01-25 05:25:56'),(1938,'lava','crms','EnrollmentStatus','ineligibleDate','EnrollmentStatus','IneligibleDate','smalldatetime',NULL,16,0,22,'ineligibleDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1939,'lava','crms','EnrollmentStatus','ineligibleNote','EnrollmentStatus','IneligibleNote','varchar',100,NULL,NULL,23,'ineligibleNote','string',NULL,'No','2009-01-25 05:25:56'),(1940,'lava','crms','EnrollmentStatus','declinedDesc','EnrollmentStatus','DeclinedDesc','varchar',25,NULL,NULL,25,'declinedDesc','string',NULL,'No','2009-01-25 05:25:56'),(1941,'lava','crms','EnrollmentStatus','declinedDate','EnrollmentStatus','DeclinedDate','smalldatetime',NULL,16,0,26,'declinedDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1942,'lava','crms','EnrollmentStatus','declinedNote','EnrollmentStatus','DeclinedNote','varchar',100,NULL,NULL,27,'declinedNote','string',NULL,'No','2009-01-25 05:25:56'),(1943,'lava','crms','EnrollmentStatus','enrolledDesc','EnrollmentStatus','EnrolledDesc','varchar',25,NULL,NULL,29,'enrolledDesc','string',NULL,'No','2009-01-25 05:25:56'),(1944,'lava','crms','EnrollmentStatus','enrolledDate','EnrollmentStatus','EnrolledDate','smalldatetime',NULL,16,0,30,'enrolledDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1945,'lava','crms','EnrollmentStatus','enrolledNote','EnrollmentStatus','EnrolledNote','varchar',100,NULL,NULL,31,'enrolledNote','string',NULL,'No','2009-01-25 05:25:56'),(1946,'lava','crms','EnrollmentStatus','excludedDesc','EnrollmentStatus','ExcludedDesc','varchar',25,NULL,NULL,33,'excludedDesc','string',NULL,'No','2009-01-25 05:25:56'),(1947,'lava','crms','EnrollmentStatus','excludedDate','EnrollmentStatus','ExcludedDate','smalldatetime',NULL,16,0,34,'excludedDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1948,'lava','crms','EnrollmentStatus','excludedNote','EnrollmentStatus','ExcludedNote','varchar',100,NULL,NULL,35,'excludedNote','string',NULL,'No','2009-01-25 05:25:56'),(1949,'lava','crms','EnrollmentStatus','withdrewDesc','EnrollmentStatus','WithdrewDesc','varchar',25,NULL,NULL,37,'withdrewDesc','string',NULL,'No','2009-01-25 05:25:56'),(1950,'lava','crms','EnrollmentStatus','withdrewDate','EnrollmentStatus','WithdrewDate','smalldatetime',NULL,16,0,38,'withdrewDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1951,'lava','crms','EnrollmentStatus','withdrewNote','EnrollmentStatus','WithdrewNote','varchar',100,NULL,NULL,39,'withdrewNote','string',NULL,'No','2009-01-25 05:25:56'),(1952,'lava','crms','EnrollmentStatus','inactiveDesc','EnrollmentStatus','InactiveDesc','varchar',25,NULL,NULL,41,'inactiveDesc','string',NULL,'No','2009-01-25 05:25:56'),(1953,'lava','crms','EnrollmentStatus','inactiveDate','EnrollmentStatus','InactiveDate','smalldatetime',NULL,16,0,42,'inactiveDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1954,'lava','crms','EnrollmentStatus','inactiveNote','EnrollmentStatus','InactiveNote','varchar',100,NULL,NULL,43,'inactiveNote','string',NULL,'No','2009-01-25 05:25:56'),(1955,'lava','crms','EnrollmentStatus','deceasedDesc','EnrollmentStatus','DeceasedDesc','varchar',25,NULL,NULL,49,'deceasedDesc','string',NULL,'No','2009-01-25 05:25:56'),(1956,'lava','crms','EnrollmentStatus','deceasedDate','EnrollmentStatus','DeceasedDate','smalldatetime',NULL,16,0,50,'deceasedDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1957,'lava','crms','EnrollmentStatus','deceasedNote','EnrollmentStatus','DeceasedNote','varchar',100,NULL,NULL,51,'deceasedNote','string',NULL,'No','2009-01-25 05:25:56'),(1958,'lava','crms','EnrollmentStatus','autopsyDesc','EnrollmentStatus','AutopsyDesc','varchar',25,NULL,NULL,53,'autopsyDesc','string',NULL,'No','2009-01-25 05:25:56'),(1959,'lava','crms','EnrollmentStatus','autopsyDate','EnrollmentStatus','AutopsyDate','smalldatetime',NULL,16,0,54,'autopsyDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1960,'lava','crms','EnrollmentStatus','autopsyNote','EnrollmentStatus','AutopsyNote','varchar',100,NULL,NULL,55,'autopsyNote','string',NULL,'No','2009-01-25 05:25:56'),(1961,'lava','crms','EnrollmentStatus','closedDesc','EnrollmentStatus','ClosedDesc','varchar',25,NULL,NULL,56,'closedDesc','string',NULL,'No','2009-01-25 05:25:56'),(1962,'lava','crms','EnrollmentStatus','closedDate','EnrollmentStatus','ClosedDate','smalldatetime',NULL,16,0,57,'closedDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1963,'lava','crms','EnrollmentStatus','closedNote','EnrollmentStatus','ClosedNote','varchar',100,NULL,NULL,58,'closedNote','string',NULL,'No','2009-01-25 05:25:56'),(1964,'lava','crms','EnrollmentStatus','enrollmentNotes','EnrollmentStatus','EnrollmentNotes','varchar',500,NULL,NULL,59,'enrollmentNotes','string',NULL,'No','2009-01-25 05:25:56'),(1965,'lava','crms','Instrument','id','InstrumentTracking','InstrID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1966,'lava','crms','Instrument','visit','InstrumentTracking','VID','int',NULL,10,0,2,'visit','many-to-one','edu.ucsf.memory.lava.model.Visit','Yes','2009-01-25 05:25:56'),(1967,'lava','crms','Instrument','projName','InstrumentTracking','ProjName','varchar',25,NULL,NULL,3,'projName','string',NULL,'Yes','2009-01-25 05:25:56'),(1968,'lava','crms','Instrument','patient','InstrumentTracking','PIDN','int',NULL,10,0,4,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-25 05:25:56'),(1969,'lava','crms','Instrument','instrType','InstrumentTracking','InstrType','varchar',25,NULL,NULL,5,'instrType','string',NULL,'Yes','2009-01-25 05:25:56'),(1970,'lava','crms','Instrument','instrVer','InstrumentTracking','InstrVer','varchar',25,NULL,NULL,6,'instrVer','string',NULL,'No','2009-01-25 05:25:56'),(1971,'lava','crms','Instrument','dcDate','InstrumentTracking','DCDate','smalldatetime',NULL,16,0,7,'dcDate','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1972,'lava','crms','Instrument','dcBy','InstrumentTracking','DCBy','varchar',25,NULL,NULL,8,'dcBy','string',NULL,'No','2009-01-25 05:25:56'),(1973,'lava','crms','Instrument','dcStatus','InstrumentTracking','DCStatus','varchar',25,NULL,NULL,9,'dcStatus','string',NULL,'Yes','2009-01-25 05:25:56'),(1974,'lava','crms','Instrument','dcNotes','InstrumentTracking','DCNotes','varchar',255,NULL,NULL,10,'dcNotes','string',NULL,'No','2009-01-25 05:25:56'),(1975,'lava','crms','Instrument','researchStatus','InstrumentTracking','ResearchStatus','varchar',50,NULL,NULL,11,'researchStatus','string',NULL,'No','2009-01-25 05:25:56'),(1976,'lava','crms','Instrument','qualityIssue','InstrumentTracking','QualityIssue','varchar',50,NULL,NULL,12,'qualityIssue','string',NULL,'No','2009-01-25 05:25:56'),(1977,'lava','crms','Instrument','qualityIssue2','InstrumentTracking','QualityIssue2','varchar',50,NULL,NULL,13,'qualityIssue2','string',NULL,'No','2009-01-25 05:25:56'),(1978,'lava','crms','Instrument','qualityIssue3','InstrumentTracking','QualityIssue3','varchar',50,NULL,NULL,14,'qualityIssue3','string',NULL,'No','2009-01-25 05:25:56'),(1979,'lava','crms','Instrument','qualityNotes','InstrumentTracking','QualityNotes','varchar',100,NULL,NULL,15,'qualityNotes','string',NULL,'No','2009-01-25 05:25:56'),(1980,'lava','crms','Instrument','deDate','InstrumentTracking','DEDate','smalldatetime',NULL,16,0,16,'deDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1981,'lava','crms','Instrument','deBy','InstrumentTracking','DEBy','varchar',25,NULL,NULL,17,'deBy','string',NULL,'No','2009-01-25 05:25:56'),(1982,'lava','crms','Instrument','deStatus','InstrumentTracking','DEStatus','varchar',25,NULL,NULL,18,'deStatus','string',NULL,'No','2009-01-25 05:25:56'),(1983,'lava','crms','Instrument','deNotes','InstrumentTracking','DENotes','varchar',255,NULL,NULL,19,'deNotes','string',NULL,'No','2009-01-25 05:25:56'),(1984,'lava','crms','Instrument','dvDate','InstrumentTracking','DVDate','smalldatetime',NULL,16,0,20,'dvDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1985,'lava','crms','Instrument','dvBy','InstrumentTracking','DVBy','varchar',25,NULL,NULL,21,'dvBy','string',NULL,'No','2009-01-25 05:25:56'),(1986,'lava','crms','Instrument','dvStatus','InstrumentTracking','DVStatus','varchar',25,NULL,NULL,22,'dvStatus','string',NULL,'No','2009-01-25 05:25:56'),(1987,'lava','crms','Instrument','dvNotes','InstrumentTracking','DVNotes','varchar',255,NULL,NULL,23,'dvNotes','string',NULL,'No','2009-01-25 05:25:56'),(1988,'lava','crms','Instrument','fieldStatus','InstrumentTracking','FieldStatus','smallint',NULL,5,0,25,'fieldStatus','short',NULL,'No','2009-01-25 05:25:56'),(1989,'lava','crms','Instrument','ageAtDC','InstrumentTracking','AgeAtDC','smallint',NULL,5,0,27,'ageAtDC','short',NULL,'No','2009-01-25 05:25:56'),(1990,'lava','crms','Instrument','instrumentAuditCreated','InstrumentTracking','Audit_Created','datetime',NULL,23,3,28,'instrumentAuditCreated','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1991,'lava','crms','Instrument','instrumentAuditEffDate','InstrumentTracking','Audit_EffDate','datetime',NULL,23,3,29,'instrumentAuditEffDate','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1992,'lava','crms','Instrument','instrumentAuditExpDate','InstrumentTracking','Audit_ExpDate','datetime',NULL,23,3,30,'instrumentAuditExpDate','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1993,'lava','crms','Instrument','instrumentAuditHostName','InstrumentTracking','Audit_HostName','varchar',50,NULL,NULL,31,'instrumentAuditHostName','string',NULL,'No','2009-01-25 05:25:56'),(1994,'lava','crms','Instrument','instrumentAuditUsername','InstrumentTracking','Audit_Username','varchar',50,NULL,NULL,32,'instrumentAuditUsername','string',NULL,'No','2009-01-25 05:25:56'),(1995,'lava','crms','Patient','id','Patient','PIDN','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1996,'lava','crms','Patient','lastName','Patient','LName','varchar',25,NULL,NULL,2,'lastName','string',NULL,'Yes','2009-01-25 05:25:56'),(1997,'lava','crms','Patient','middleInitial','Patient','MInitial','char',1,NULL,NULL,3,'middleInitial','character',NULL,'No','2009-01-25 05:25:56'),(1998,'lava','crms','Patient','firstName','Patient','FName','varchar',25,NULL,NULL,4,'firstName','string',NULL,'Yes','2009-01-25 05:25:56'),(1999,'lava','crms','Patient','suffix','Patient','Suffix','varchar',15,NULL,NULL,5,'suffix','string',NULL,'No','2009-01-25 05:25:56'),(2000,'lava','crms','Patient','degree','Patient','Degree','varchar',15,NULL,NULL,6,'degree','string',NULL,'No','2009-01-25 05:25:56'),(2001,'lava','crms','Patient','ucid','Patient','UCID','varchar',20,NULL,NULL,7,'ucid','string',NULL,'No','2009-01-25 05:25:56'),(2002,'lava','crms','Patient','ssn','Patient','SSN','nvarchar',30,NULL,NULL,8,'ssn','string',NULL,'No','2009-01-25 05:25:56'),(2003,'lava','crms','Patient','birthDate','Patient','DOB','smalldatetime',NULL,16,0,9,'birthDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(2004,'lava','crms','Patient','age','Patient','AGE','int',NULL,10,0,10,'age','long',NULL,'No','2009-01-25 05:25:56'),(2005,'lava','crms','Patient','gender','Patient','Gender','tinyint',NULL,3,0,11,'gender','byte',NULL,'No','2009-01-25 05:25:56'),(2006,'lava','crms','Patient','hand','Patient','Hand','varchar',25,NULL,NULL,12,'hand','string',NULL,'No','2009-01-25 05:25:56'),(2007,'lava','crms','Patient','deceased','Patient','Deceased','bit',NULL,1,0,13,'deceased','boolean',NULL,'Yes','2009-01-25 05:25:56'),(2008,'lava','crms','Patient','deathDate','Patient','DOD','smalldatetime',NULL,16,0,14,'deathDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(2009,'lava','crms','Patient','transNeeded','Patient','TransNeeded','bit',NULL,1,0,15,'transNeeded','boolean',NULL,'No','2009-01-25 05:25:56'),(2010,'lava','crms','Patient','primaryLanguage','Patient','PrimaryLanguage','varchar',25,NULL,NULL,16,'primaryLanguage','string',NULL,'No','2009-01-25 05:25:56'),(2011,'lava','crms','Patient','transLanguage','Patient','TransLanguage','varchar',25,NULL,NULL,16,'transLanguage','string',NULL,'No','2009-01-25 05:25:56'),(2012,'lava','crms','Patient','testingLanguage','Patient','testingLanguage','varchar',25,NULL,NULL,16,'testingLanguage','string',NULL,'No','2009-01-25 05:25:56'),(2013,'lava','crms','Patient','enterBy','Patient','EnterBy','varchar',25,NULL,NULL,17,'enterBy','string',NULL,'No','2009-01-25 05:25:56'),(2014,'lava','crms','Patient','dupNameFlag','Patient','DupNameFlag','bit',NULL,1,0,18,'dupNameFlag','boolean',NULL,'Yes','2009-01-25 05:25:56'),(2015,'lava','crms','Patient','fullNameRev','Patient','FullNameRev','varchar',100,NULL,NULL,19,'fullNameRev','string',NULL,'No','2009-01-25 05:25:56'),(2016,'lava','crms','Patient','fullName','Patient','FullName','varchar',65,NULL,NULL,20,'fullName','string',NULL,'No','2009-01-25 05:25:56'),(2017,'lava','crms','Patient','fullNameRevNoSuffix','Patient','FullNameRevNoSuffix','varchar',66,NULL,NULL,21,'fullNameRevNoSuffix','string',NULL,'No','2009-01-25 05:25:56'),(2018,'lava','crms','Patient','fullNameNoSuffix','Patient','FullNameNoSuffix','varchar',65,NULL,NULL,22,'fullNameNoSuffix','string',NULL,'No','2009-01-25 05:25:56'),(2019,'lava','crms','PatientDoctor','id','PatientDoctor','PIDNDocID','int',NULL,10,0,1,'id','integer',NULL,'Yes','2009-01-25 05:25:56'),(2020,'lava','crms','PatientDoctor','doctor','PatientDoctor','DocID','int',NULL,10,0,2,'doctor','many-to-one','edu.ucsf.memory.lava.model.Doctor','Yes','2009-01-25 05:25:56'),(2021,'lava','crms','PatientDoctor','patient','PatientDoctor','PIDN','int',NULL,10,0,3,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-25 05:25:56'),(2022,'lava','crms','PatientDoctor','docStat','PatientDoctor','DocStat','varchar',25,NULL,NULL,4,'docStat','string',NULL,'No','2009-01-25 05:25:56'),(2023,'lava','crms','PatientDoctor','docNote','PatientDoctor','DocNote','varchar',100,NULL,NULL,5,'docNote','string',NULL,'No','2009-01-25 05:25:56'),(2024,'lava','crms','task','id','Tasks','TaskID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(2025,'lava','crms','task','patient','Tasks','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-25 05:25:56'),(2026,'lava','crms','task','projName','Tasks','ProjName','varchar',75,NULL,NULL,3,'projName','string',NULL,'No','2009-01-25 05:25:56'),(2027,'lava','crms','task','openedDate','Tasks','OpenedDate','smalldatetime',NULL,16,0,4,'openedDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(2028,'lava','crms','task','openedBy','Tasks','OpenedBy','varchar',25,NULL,NULL,5,'openedBy','string',NULL,'No','2009-01-25 05:25:56'),(2029,'lava','crms','task','taskType','Tasks','TaskType','varchar',25,NULL,NULL,6,'taskType','string',NULL,'No','2009-01-25 05:25:56'),(2030,'lava','crms','task','taskDesc','Tasks','TaskDesc','varchar',255,NULL,NULL,7,'taskDesc','string',NULL,'No','2009-01-25 05:25:56'),(2031,'lava','crms','task','dueDate','Tasks','DueDate','smalldatetime',NULL,16,0,8,'dueDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(2032,'lava','crms','task','taskStatus','Tasks','TaskStatus','varchar',50,NULL,NULL,9,'taskStatus','string',NULL,'No','2009-01-25 05:25:56'),(2033,'lava','crms','task','assignedTo','Tasks','AssignedTo','varchar',25,NULL,NULL,10,'assignedTo','string',NULL,'No','2009-01-25 05:25:56'),(2034,'lava','crms','task','workingNotes','Tasks','WorkingNotes','varchar',255,NULL,NULL,11,'workingNotes','string',NULL,'No','2009-01-25 05:25:56'),(2035,'lava','crms','task','closedDate','Tasks','ClosedDate','smalldatetime',NULL,16,0,12,'closedDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(2036,'lava','crms','Visit','id','Visit','VID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(2037,'lava','crms','Visit','patient','Visit','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-25 05:25:56'),(2038,'lava','crms','Visit','projName','Visit','ProjName','varchar',25,NULL,NULL,3,'projName','string',NULL,'Yes','2009-01-25 05:25:56'),(2039,'lava','crms','Visit','visitLocation','Visit','VLocation','varchar',25,NULL,NULL,4,'visitLocation','string',NULL,'Yes','2009-01-25 05:25:56'),(2040,'lava','crms','Visit','visitType','Visit','VType','varchar',25,NULL,NULL,5,'visitType','string',NULL,'Yes','2009-01-25 05:25:56'),(2041,'lava','crms','Visit','visitWith','Visit','VWith','varchar',25,NULL,NULL,6,'visitWith','string',NULL,'No','2009-01-25 05:25:56'),(2042,'lava','crms','Visit','visitDate','Visit','VDate','smalldatetime',NULL,16,0,7,'visitDate','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(2043,'lava','crms','Visit','visitStatus','Visit','VStatus','varchar',25,NULL,NULL,8,'visitStatus','string',NULL,'Yes','2009-01-25 05:25:56'),(2044,'lava','crms','Visit','visitNote','Visit','VNotes','varchar',255,NULL,NULL,9,'visitNote','string',NULL,'No','2009-01-25 05:25:56'),(2045,'lava','crms','Visit','followUpMonth','Visit','FUMonth','char',3,NULL,NULL,10,'followUpMonth','character',NULL,'No','2009-01-25 05:25:56'),(2046,'lava','crms','Visit','followUpYear','Visit','FUYear','char',4,NULL,NULL,11,'followUpYear','character',NULL,'No','2009-01-25 05:25:56'),(2047,'lava','crms','Visit','followUpNote','Visit','FUNote','varchar',100,NULL,NULL,12,'followUpNote','string',NULL,'No','2009-01-25 05:25:56'),(2048,'lava','crms','Visit','waitList','Visit','WList','varchar',25,NULL,NULL,13,'waitList','string',NULL,'No','2009-01-25 05:25:56'),(2049,'lava','crms','Visit','waitListNote','Visit','WListNote','varchar',100,NULL,NULL,14,'waitListNote','string',NULL,'No','2009-01-25 05:25:56'),(2050,'lava','crms','Visit','waitListDate','Visit','WListDate','smalldatetime',NULL,16,0,15,'waitListDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(2051,'lava','crms','Visit','visitDescrip','Visit','VShortDesc','varchar',64,NULL,NULL,16,'visitDescrip','string',NULL,'No','2009-01-25 05:25:56'),(2052,'lava','crms','Visit','ageAtVisit','Visit','AgeAtVisit','smallint',NULL,5,0,17,'ageAtVisit','short',NULL,'No','2009-01-25 05:25:56');
/*!40000 ALTER TABLE `hibernateproperty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrument`
--

DROP TABLE IF EXISTS `instrument`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instrument` (
  `InstrID` int(10) NOT NULL AUTO_INCREMENT,
  `InstrName` varchar(25) NOT NULL,
  `TableName` varchar(25) NOT NULL,
  `FormName` varchar(50) DEFAULT NULL,
  `Category` varchar(25) DEFAULT NULL,
  `HasVersion` tinyint(1) NOT NULL DEFAULT '0',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`InstrID`),
  UNIQUE KEY `InstrName` (`InstrName`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrument`
--

LOCK TABLES `instrument` WRITE;
/*!40000 ALTER TABLE `instrument` DISABLE KEYS */;
/*!40000 ALTER TABLE `instrument` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrumentnotes`
--

DROP TABLE IF EXISTS `instrumentnotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instrumentnotes` (
  `InstrID` int(10) NOT NULL,
  `Section` varchar(50) NOT NULL,
  `Note` varchar(2000) DEFAULT NULL,
  KEY `instrumentnotes__instrID` (`InstrID`),
  CONSTRAINT `instrumentnotes__instrID` FOREIGN KEY (`InstrID`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrumentnotes`
--

LOCK TABLES `instrumentnotes` WRITE;
/*!40000 ALTER TABLE `instrumentnotes` DISABLE KEYS */;
/*!40000 ALTER TABLE `instrumentnotes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrumentsummary`
--

DROP TABLE IF EXISTS `instrumentsummary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instrumentsummary` (
  `InstrID` int(10) NOT NULL,
  `Summary` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`InstrID`),
  KEY `instrumentsummary__InstrID` (`InstrID`),
  CONSTRAINT `instrumentsummary__InstrID` FOREIGN KEY (`InstrID`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrumentsummary`
--

LOCK TABLES `instrumentsummary` WRITE;
/*!40000 ALTER TABLE `instrumentsummary` DISABLE KEYS */;
/*!40000 ALTER TABLE `instrumentsummary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrumenttracking`
--

DROP TABLE IF EXISTS `instrumenttracking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instrumenttracking` (
  `InstrID` int(10) NOT NULL AUTO_INCREMENT,
  `VID` int(10) NOT NULL,
  `ProjName` varchar(75) NOT NULL,
  `PIDN` int(10) NOT NULL,
  `InstrType` varchar(25) NOT NULL,
  `InstrVer` varchar(25) DEFAULT NULL,
  `DCDate` datetime NOT NULL,
  `DCBy` varchar(25) DEFAULT NULL,
  `DCStatus` varchar(25) NOT NULL,
  `DCNotes` varchar(255) DEFAULT NULL,
  `ResearchStatus` varchar(50) DEFAULT NULL,
  `QualityIssue` varchar(50) DEFAULT NULL,
  `QualityIssue2` varchar(50) DEFAULT NULL,
  `QualityIssue3` varchar(50) DEFAULT NULL,
  `QualityNotes` varchar(100) DEFAULT NULL,
  `DEDate` datetime DEFAULT NULL,
  `DEBy` varchar(25) DEFAULT NULL,
  `DEStatus` varchar(25) DEFAULT NULL,
  `DENotes` varchar(255) DEFAULT NULL,
  `DVDate` datetime DEFAULT NULL,
  `DVBy` varchar(25) DEFAULT NULL,
  `DVStatus` varchar(25) DEFAULT NULL,
  `DVNotes` varchar(3000) DEFAULT NULL,
  `latestflag` tinyint(1) NOT NULL DEFAULT '0',
  `FieldStatus` smallint(5) DEFAULT NULL,
  `AgeAtDC` smallint(5) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`InstrID`),
  KEY `AgeLookup` (`InstrID`,`AgeAtDC`),
  KEY `PIDN_InstrType_DCDate_DCStatus` (`PIDN`,`InstrType`,`DCDate`,`DCStatus`),
  KEY `instrumenttracking__InstrType` (`InstrType`),
  KEY `instrumenttracking__VID` (`VID`),
  KEY `instrumenttracking__ProjName` (`ProjName`),
  KEY `insttumenttracking__PIDN` (`PIDN`),
  CONSTRAINT `instrumenttracking__InstrType` FOREIGN KEY (`InstrType`) REFERENCES `instrument` (`InstrName`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `instrumenttracking__ProjName` FOREIGN KEY (`ProjName`) REFERENCES `projectunit` (`ProjUnitDesc`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `instrumenttracking__VID` FOREIGN KEY (`VID`) REFERENCES `visit` (`VID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `insttumenttracking__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrumenttracking`
--

LOCK TABLES `instrumenttracking` WRITE;
/*!40000 ALTER TABLE `instrumenttracking` DISABLE KEYS */;
/*!40000 ALTER TABLE `instrumenttracking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lava_session`
--

DROP TABLE IF EXISTS `lava_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lava_session` (
  `lava_session_id` int(10) NOT NULL AUTO_INCREMENT,
  `server_instance_id` int(10) NOT NULL,
  `http_session_id` varchar(64) DEFAULT NULL,
  `current_status` varchar(25) NOT NULL DEFAULT 'NEW',
  `user_id` int(10) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `host_name` varchar(50) DEFAULT NULL,
  `create_timestamp` timestamp NULL DEFAULT NULL,
  `access_timestamp` timestamp NULL DEFAULT NULL,
  `expire_timestamp` datetime DEFAULT NULL,
  `disconnect_date` date DEFAULT NULL,
  `disconnect_time` time DEFAULT NULL,
  `disconnect_message` varchar(500) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`lava_session_id`),
  KEY `lavasession__server_instance_id` (`server_instance_id`),
  KEY `lavasession__user_id` (`user_id`),
  CONSTRAINT `lavasession__server_instance_id` FOREIGN KEY (`server_instance_id`) REFERENCES `lavaserverinstance` (`ServerInstanceID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lavasession__user_id` FOREIGN KEY (`user_id`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lava_session`
--

LOCK TABLES `lava_session` WRITE;
/*!40000 ALTER TABLE `lava_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `lava_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lavaserverinstance`
--

DROP TABLE IF EXISTS `lavaserverinstance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lavaserverinstance` (
  `ServerInstanceID` int(10) NOT NULL AUTO_INCREMENT,
  `ServerDescription` varchar(255) DEFAULT NULL,
  `CreateTime` timestamp NULL DEFAULT NULL,
  `DisconnectTime` datetime DEFAULT NULL,
  `DisconnectWarningMinutes` int(10) DEFAULT NULL,
  `DisconnectMessage` varchar(500) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ServerInstanceID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lavaserverinstance`
--

LOCK TABLES `lavaserverinstance` WRITE;
/*!40000 ALTER TABLE `lavaserverinstance` DISABLE KEYS */;
/*!40000 ALTER TABLE `lavaserverinstance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `list`
--

DROP TABLE IF EXISTS `list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `list` (
  `ListID` int(10) NOT NULL AUTO_INCREMENT,
  `ListName` varchar(50) NOT NULL,
  `scope` varchar(25) NOT NULL,
  `NumericKey` tinyint(1) NOT NULL DEFAULT '0',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ListID`),
  UNIQUE KEY `ListName` (`ListName`)
) ENGINE=InnoDB AUTO_INCREMENT=519 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `list`
--

LOCK TABLES `list` WRITE;
/*!40000 ALTER TABLE `list` DISABLE KEYS */;
INSERT INTO `list` VALUES (468,'LavaSessionStatus','core',0,'2009-01-25 04:57:59'),(469,'NavigationListPageSize','core',1,'2009-01-25 04:57:59'),(470,'TextYesNo','core',0,'2009-01-25 04:57:59'),(471,'TextYesNoDK','core',0,'2009-01-25 04:57:59'),(472,'TextYesNoNA','core',0,'2009-01-25 04:57:59'),(473,'YESNO','core',1,'2009-01-25 04:57:59'),(474,'YESNODK','core',1,'2009-01-25 04:57:59'),(475,'YesNoDK_Zero','core',0,'2009-01-25 04:57:59'),(476,'YesNoUnknown','core',1,'2009-01-25 04:57:59'),(477,'YesNo_Zero','core',0,'2009-01-25 04:57:59'),(478,'CaregiverMaritalStatus','crms',0,'2009-01-25 04:57:59'),(479,'ConsentType','crms',0,'2009-01-25 04:57:59'),(480,'ContactMethods','crms',0,'2009-01-25 04:57:59'),(481,'ContactRelations','crms',0,'2009-01-25 04:57:59'),(482,'DataCollectionStatus','crms',0,'2009-01-25 04:57:59'),(483,'DataEntryStatus','crms',0,'2009-01-25 04:57:59'),(484,'DataValidationStatus','crms',0,'2009-01-25 04:57:59'),(485,'DoctorStatus','crms',0,'2009-01-25 04:57:59'),(486,'Education','crms',1,'2009-01-25 04:57:59'),(487,'Gender','crms',1,'2009-01-25 04:57:59'),(488,'Handedness','crms',0,'2009-01-25 04:57:59'),(489,'InstrumentQualityIssue','crms',0,'2009-01-25 04:57:59'),(490,'InstrumentResearchStatus','crms',0,'2009-01-25 04:57:59'),(491,'InstrumentVersions','crms',0,'2009-01-25 04:57:59'),(492,'MaritalStatus','crms',1,'2009-01-25 04:57:59'),(493,'Occupation','crms',1,'2009-01-25 04:57:59'),(494,'PatientLanguage','crms',0,'2009-01-25 04:57:59'),(495,'PhoneType','crms',0,'2009-01-25 04:57:59'),(496,'PrimaryCaregiver','crms',1,'2009-01-25 04:57:59'),(497,'ProbablePossibleNo','crms',1,'2009-01-25 04:57:59'),(498,'ProjectStatus','crms',0,'2009-01-25 04:57:59'),(499,'ProjectStatusType','crms',0,'2009-01-25 04:57:59'),(500,'RACE','crms',1,'2009-01-25 04:57:59'),(501,'ReferralSources','crms',0,'2009-01-25 04:57:59'),(502,'SkipErrorCodes','crms',0,'2009-01-25 04:57:59'),(503,'SpanishOrigin','crms',1,'2009-01-25 04:57:59'),(504,'StaffList','crms',0,'2009-01-25 04:57:59'),(505,'StandardErrorCodes','crms',1,'2009-01-25 04:57:59'),(506,'States','crms',0,'2009-01-25 04:57:59'),(507,'TaskStatus','crms',0,'2009-01-25 04:57:59'),(508,'TaskType','crms',0,'2009-01-25 04:57:59'),(509,'UsualSomeRare','crms',0,'2009-01-25 04:57:59'),(510,'UsualSomeRareDK','crms',0,'2009-01-25 04:57:59'),(511,'VisitLocations','crms',0,'2009-01-25 04:57:59'),(512,'VisitStatus','crms',0,'2009-01-25 04:57:59'),(513,'VisitType','crms',0,'2009-01-25 04:57:59'),(514,'YesNoScale_NoCorrect','crms',1,'2009-01-25 04:57:59'),(515,'YesNoScale_YesCorrect','crms',1,'2009-01-25 04:57:59'),(516,'YesNoZeroNA','core',1,'2009-05-12 00:16:47'),(517,'AbsentPresent','crms',1,'2011-03-17 19:35:59'),(518,'NormalAbnormal','crms',1,'2011-03-17 19:36:13');
/*!40000 ALTER TABLE `list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listvalues`
--

DROP TABLE IF EXISTS `listvalues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `listvalues` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `ListID` int(10) NOT NULL,
  `ValueKey` varchar(100) NOT NULL,
  `ValueDesc` varchar(100) DEFAULT NULL,
  `OrderID` int(10) NOT NULL DEFAULT '0',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `ListID` (`ListID`),
  KEY `ValueKey` (`ValueKey`),
  KEY `listvalues__listID` (`ListID`),
  CONSTRAINT `listvalues__listID` FOREIGN KEY (`ListID`) REFERENCES `list` (`ListID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24727 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listvalues`
--

LOCK TABLES `listvalues` WRITE;
/*!40000 ALTER TABLE `listvalues` DISABLE KEYS */;
INSERT INTO `listvalues` VALUES (24376,468,'NEW',NULL,1,'2009-01-25 04:57:59'),(24377,468,'ACTIVE',NULL,2,'2009-01-25 04:57:59'),(24378,468,'LOGOFF',NULL,3,'2009-01-25 04:57:59'),(24379,468,'EXPIRED',NULL,4,'2009-01-25 04:57:59'),(24380,468,'DISCONNECTED',NULL,5,'2009-01-25 04:57:59'),(24381,469,'10','10/page',0,'2009-01-25 04:57:59'),(24382,469,'100','100/page',0,'2009-01-25 04:57:59'),(24383,469,'15','15/page',0,'2009-01-25 04:57:59'),(24384,469,'25','25/page',0,'2009-01-25 04:57:59'),(24385,469,'250','250/page',0,'2009-01-25 04:57:59'),(24386,469,'5','5/page',0,'2009-01-25 04:57:59'),(24387,469,'50','50/page',0,'2009-01-25 04:57:59'),(24388,470,'Yes',NULL,1,'2009-01-25 04:57:59'),(24389,470,'No',NULL,2,'2009-01-25 04:57:59'),(24390,471,'Yes',NULL,1,'2009-01-25 04:57:59'),(24391,471,'No',NULL,2,'2009-01-25 04:57:59'),(24392,471,'Don\'t Know',NULL,3,'2009-01-25 04:57:59'),(24393,472,'Yes',NULL,1,'2009-01-25 04:57:59'),(24394,472,'No',NULL,2,'2009-01-25 04:57:59'),(24395,472,'N/A',NULL,3,'2009-01-25 04:57:59'),(24396,473,'1','Yes',0,'2009-01-25 04:57:59'),(24397,473,'2','No',0,'2009-01-25 04:57:59'),(24398,474,'1','Yes',0,'2009-01-25 04:57:59'),(24399,474,'2','No',0,'2009-01-25 04:57:59'),(24400,474,'9','Don\'t Know',0,'2009-01-25 04:57:59'),(24401,475,'0','No',0,'2009-01-25 04:57:59'),(24402,475,'1','Yes',0,'2009-01-25 04:57:59'),(24403,475,'9','Don\'t Know',0,'2009-01-25 04:57:59'),(24404,476,'0','No',0,'2009-01-25 04:57:59'),(24405,476,'1','Yes',0,'2009-01-25 04:57:59'),(24406,476,'9','Unknown',0,'2009-01-25 04:57:59'),(24407,477,'0','No',0,'2009-01-25 04:57:59'),(24408,477,'1','Yes',0,'2009-01-25 04:57:59'),(24409,478,'DIVORCED',NULL,0,'2009-01-25 04:57:59'),(24410,478,'MARRIED',NULL,0,'2009-01-25 04:57:59'),(24411,478,'SINGLE',NULL,0,'2009-01-25 04:57:59'),(24412,480,'Email',NULL,0,'2009-01-25 04:57:59'),(24413,480,'Fax',NULL,0,'2009-01-25 04:57:59'),(24414,480,'Letter',NULL,0,'2009-01-25 04:57:59'),(24415,480,'Phone',NULL,0,'2009-01-25 04:57:59'),(24416,481,'BROTHER',NULL,0,'2009-01-25 04:57:59'),(24417,481,'BROTHER-IN-LAW',NULL,0,'2009-01-25 04:57:59'),(24418,481,'CONSERVATOR',NULL,0,'2009-01-25 04:57:59'),(24419,481,'DAUGHTER',NULL,0,'2009-01-25 04:57:59'),(24420,481,'FATHER',NULL,0,'2009-01-25 04:57:59'),(24421,481,'FRIEND',NULL,0,'2009-01-25 04:57:59'),(24422,481,'HUSBAND',NULL,0,'2009-01-25 04:57:59'),(24423,481,'MOTHER',NULL,0,'2009-01-25 04:57:59'),(24424,481,'NEPHEW',NULL,0,'2009-01-25 04:57:59'),(24425,481,'NIECE',NULL,0,'2009-01-25 04:57:59'),(24426,481,'NURSE',NULL,0,'2009-01-25 04:57:59'),(24427,481,'OTHER',NULL,0,'2009-01-25 04:57:59'),(24428,481,'PAID CAREGIVER',NULL,0,'2009-01-25 04:57:59'),(24429,481,'PARTNER',NULL,0,'2009-01-25 04:57:59'),(24430,481,'SISTER',NULL,0,'2009-01-25 04:57:59'),(24431,481,'SISTER-IN-LAW',NULL,0,'2009-01-25 04:57:59'),(24432,481,'SOCIAL WORKER',NULL,0,'2009-01-25 04:57:59'),(24433,481,'SON',NULL,0,'2009-01-25 04:57:59'),(24434,481,'WIFE',NULL,0,'2009-01-25 04:57:59'),(24435,482,'Scheduled',NULL,1,'2009-01-25 04:57:59'),(24436,482,'Canceled',NULL,2,'2009-01-25 04:57:59'),(24437,482,'Complete',NULL,2,'2009-01-25 04:57:59'),(24438,482,'Canceled-Patient Factor',NULL,3,'2009-01-25 04:57:59'),(24439,482,'Canceled-Situational',NULL,4,'2009-01-25 04:57:59'),(24440,482,'Canceled-Alt Test Given',NULL,5,'2009-01-25 04:57:59'),(24441,482,'Incomplete',NULL,6,'2009-01-25 04:57:59'),(24442,482,'Incomplete-Scoring',NULL,7,'2009-01-25 04:57:59'),(24443,482,'Incomplete-Not Returned',NULL,8,'2009-01-25 04:57:59'),(24444,482,'Unknown',NULL,9,'2009-01-25 04:57:59'),(24445,483,'Complete',NULL,1,'2009-01-25 04:57:59'),(24446,483,'Incomplete',NULL,2,'2009-01-25 04:57:59'),(24447,483,'Entry Problem',NULL,3,'2009-01-25 04:57:59'),(24448,483,'Returned To Examiner',NULL,4,'2009-01-25 04:57:59'),(24449,484,'Validation Needed',NULL,1,'2009-01-25 04:57:59'),(24450,484,'In Progress',NULL,2,'2009-01-25 04:57:59'),(24451,484,'Complete-OK',NULL,3,'2009-01-25 04:57:59'),(24452,484,'Complete-Problems',NULL,4,'2009-01-25 04:57:59'),(24453,484,'Complete-Invalid Data',NULL,5,'2009-01-25 04:57:59'),(24454,485,'OTHER',NULL,0,'2009-01-25 04:57:59'),(24455,485,'PRIMARY',NULL,0,'2009-01-25 04:57:59'),(24456,485,'REFERRING',NULL,0,'2009-01-25 04:57:59'),(24457,485,'REFERRING/PRIMARY',NULL,0,'2009-01-25 04:57:59'),(24458,486,'0',NULL,0,'2009-01-25 04:57:59'),(24459,486,'1',NULL,0,'2009-01-25 04:57:59'),(24460,486,'10',NULL,0,'2009-01-25 04:57:59'),(24461,486,'11',NULL,0,'2009-01-25 04:57:59'),(24462,486,'12','Completed High School',0,'2009-01-25 04:57:59'),(24463,486,'13',NULL,0,'2009-01-25 04:57:59'),(24464,486,'14','2 Year College Degree',0,'2009-01-25 04:57:59'),(24465,486,'15',NULL,0,'2009-01-25 04:57:59'),(24466,486,'16','4 Year College Degree',0,'2009-01-25 04:57:59'),(24467,486,'17',NULL,0,'2009-01-25 04:57:59'),(24468,486,'18','Master\'s Degree',0,'2009-01-25 04:57:59'),(24469,486,'19',NULL,0,'2009-01-25 04:57:59'),(24470,486,'2',NULL,0,'2009-01-25 04:57:59'),(24471,486,'20','Ph.D, M.D. or other Professional Degree',0,'2009-01-25 04:57:59'),(24472,486,'21',NULL,0,'2009-01-25 04:57:59'),(24473,486,'22',NULL,0,'2009-01-25 04:57:59'),(24474,486,'23',NULL,0,'2009-01-25 04:57:59'),(24475,486,'24',NULL,0,'2009-01-25 04:57:59'),(24476,486,'25',NULL,0,'2009-01-25 04:57:59'),(24477,486,'26',NULL,0,'2009-01-25 04:57:59'),(24478,486,'27',NULL,0,'2009-01-25 04:57:59'),(24479,486,'28',NULL,0,'2009-01-25 04:57:59'),(24480,486,'29',NULL,0,'2009-01-25 04:57:59'),(24481,486,'3',NULL,0,'2009-01-25 04:57:59'),(24482,486,'30',NULL,0,'2009-01-25 04:57:59'),(24483,486,'31',NULL,0,'2009-01-25 04:57:59'),(24484,486,'32',NULL,0,'2009-01-25 04:57:59'),(24485,486,'33',NULL,0,'2009-01-25 04:57:59'),(24486,486,'34',NULL,0,'2009-01-25 04:57:59'),(24487,486,'35',NULL,0,'2009-01-25 04:57:59'),(24488,486,'4',NULL,0,'2009-01-25 04:57:59'),(24489,486,'5',NULL,0,'2009-01-25 04:57:59'),(24490,486,'6',NULL,0,'2009-01-25 04:57:59'),(24491,486,'7',NULL,0,'2009-01-25 04:57:59'),(24492,486,'8',NULL,0,'2009-01-25 04:57:59'),(24493,486,'9',NULL,0,'2009-01-25 04:57:59'),(24494,486,'99','Not Determined',0,'2009-01-25 04:57:59'),(24495,487,'1','MALE',0,'2009-01-25 04:57:59'),(24496,487,'2','FEMALE',0,'2009-01-25 04:57:59'),(24497,488,'AMBIDEXTROUS',NULL,0,'2009-01-25 04:57:59'),(24498,488,'LEFT',NULL,0,'2009-01-25 04:57:59'),(24499,488,'RIGHT',NULL,0,'2009-01-25 04:57:59'),(24500,489,'BEHAVIORAL DISTURBANCES',NULL,0,'2009-01-25 04:57:59'),(24501,489,'INCOMPLETE DATA ENTRY',NULL,0,'2009-01-25 04:57:59'),(24502,489,'MOTOR DIFFICULTIES',NULL,2,'2009-01-25 04:57:59'),(24503,489,'SPEECH DIFFICULTIES',NULL,3,'2009-01-25 04:57:59'),(24504,489,'HEARING IMPAIRMENT',NULL,4,'2009-01-25 04:57:59'),(24505,489,'VISUAL IMPAIRMENT',NULL,5,'2009-01-25 04:57:59'),(24506,489,'ESL',NULL,6,'2009-01-25 04:57:59'),(24507,489,'MINIMAL EDUCATION',NULL,7,'2009-01-25 04:57:59'),(24508,489,'LACK OF EFFORT',NULL,8,'2009-01-25 04:57:59'),(24509,489,'UNRELIABLE INFORMANT',NULL,9,'2009-01-25 04:57:59'),(24510,489,'OTHER (Describe in Notes)',NULL,10,'2009-01-25 04:57:59'),(24511,489,'INCOMPLETE',NULL,11,'2009-01-25 04:57:59'),(24512,489,'UNAVAILABLE INFORMANT',NULL,12,'2009-01-25 04:57:59'),(24513,489,'LANGUAGE COMPREHENSION',NULL,13,'2009-01-25 04:57:59'),(24514,489,'COMPREHENSION OF TEST RULES',NULL,14,'2009-01-25 04:57:59'),(24515,489,'SPEED OF RESPONSE',NULL,15,'2009-01-25 04:57:59'),(24516,489,'INFORMANT HAS < DAILY CONTACT',NULL,16,'2009-01-25 04:57:59'),(24517,490,'GOOD FOR RESEARCH',NULL,0,'2009-01-25 04:57:59'),(24518,490,'NOT FOR RESEARCH',NULL,0,'2009-01-25 04:57:59'),(24519,490,'INCOMPLETE DATA ENTRY',NULL,3,'2009-01-25 04:57:59'),(24520,492,'1','NEVER MARRIED',0,'2009-01-25 04:57:59'),(24521,492,'2','MARRIED',0,'2009-01-25 04:57:59'),(24522,492,'3','WIDOWED',0,'2009-01-25 04:57:59'),(24523,492,'4','DIVORCED',0,'2009-01-25 04:57:59'),(24524,492,'5','SEPARATED',0,'2009-01-25 04:57:59'),(24525,492,'9','NOT DETERMINED',0,'2009-01-25 04:57:59'),(24526,493,'1','Labor / Craftsman / Mechanic',1,'2009-01-25 04:57:59'),(24527,493,'2','Professional (teacher, lawyer,...)',2,'2009-01-25 04:57:59'),(24528,493,'3','Health Care (physician, nurse,. . .)',3,'2009-01-25 04:57:59'),(24529,493,'4','Business Owner / Executive',4,'2009-01-25 04:57:59'),(24530,493,'5','Homemaker',5,'2009-01-25 04:57:59'),(24531,493,'6','Office / Clerical',6,'2009-01-25 04:57:59'),(24532,493,'7','Retail / Sales',7,'2009-01-25 04:57:59'),(24533,493,'8','Computer / Technical',8,'2009-01-25 04:57:59'),(24534,493,'9','Artistic / Creative',9,'2009-01-25 04:57:59'),(24535,493,'10','Other',10,'2009-01-25 04:57:59'),(24536,493,'0','None / Did Not Work',11,'2009-01-25 04:57:59'),(24537,494,'Cantonese','',0,'2009-01-25 04:57:59'),(24538,494,'English','',0,'2009-01-25 04:57:59'),(24539,494,'Japanese','',0,'2009-01-25 04:57:59'),(24540,494,'Mandarin','',0,'2009-01-25 04:57:59'),(24541,494,'Russian','',0,'2009-01-25 04:57:59'),(24542,494,'Spanish','',0,'2009-01-25 04:57:59'),(24543,494,'Unknown','',0,'2009-01-25 04:57:59'),(24544,495,'HOME 1',NULL,1,'2009-01-25 04:57:59'),(24545,495,'HOME 2',NULL,2,'2009-01-25 04:57:59'),(24546,495,'WORK',NULL,3,'2009-01-25 04:57:59'),(24547,495,'OFFICE',NULL,4,'2009-01-25 04:57:59'),(24548,495,'CELL',NULL,5,'2009-01-25 04:57:59'),(24549,495,'FAX',NULL,6,'2009-01-25 04:57:59'),(24550,495,'PAGER',NULL,7,'2009-01-25 04:57:59'),(24551,495,'OTHER',NULL,8,'2009-01-25 04:57:59'),(24552,496,'1','SPOUSE',0,'2009-01-25 04:57:59'),(24553,496,'10','OTHER',0,'2009-01-25 04:57:59'),(24554,496,'11','NO ONE HELPS THE PATIENT',0,'2009-01-25 04:57:59'),(24555,496,'2','SON',0,'2009-01-25 04:57:59'),(24556,496,'3','SON-IN-LAW',0,'2009-01-25 04:57:59'),(24557,496,'4','DAUGHTER',0,'2009-01-25 04:57:59'),(24558,496,'5','DAUGHTER-IN-LAW',0,'2009-01-25 04:57:59'),(24559,496,'6','OTHER RELATIVE',0,'2009-01-25 04:57:59'),(24560,496,'7','FRIEND',0,'2009-01-25 04:57:59'),(24561,496,'8','NEIGHBOR',0,'2009-01-25 04:57:59'),(24562,496,'9','PAID CAREGIVER',0,'2009-01-25 04:57:59'),(24563,497,'0','No',0,'2009-01-25 04:57:59'),(24564,497,'1','Possible',1,'2009-01-25 04:57:59'),(24565,497,'2','Probable',2,'2009-01-25 04:57:59'),(24566,498,'GENERAL','REFERRED',1,'2009-01-25 04:57:59'),(24567,498,'GENERAL','DEFERRED',5,'2009-01-25 04:57:59'),(24568,498,'GENERAL','ELIGIBLE',10,'2009-01-25 04:57:59'),(24569,498,'GENERAL','INELIGIBLE',15,'2009-01-25 04:57:59'),(24570,498,'GENERAL','DECLINED',20,'2009-01-25 04:57:59'),(24571,498,'GENERAL','ENROLLED',25,'2009-01-25 04:57:59'),(24572,498,'GENERAL','EXCLUDED',30,'2009-01-25 04:57:59'),(24573,498,'GENERAL','WITHDREW',35,'2009-01-25 04:57:59'),(24574,498,'GENERAL','INACTIVE',40,'2009-01-25 04:57:59'),(24575,498,'GENERAL','DECEASED',45,'2009-01-25 04:57:59'),(24576,498,'GENERAL','AUTOPSY',50,'2009-01-25 04:57:59'),(24577,498,'GENERAL','CLOSED',55,'2009-01-25 04:57:59'),(24578,499,'GENERAL','Enrollment',0,'2009-01-25 04:57:59'),(24579,500,'1','WHITE',0,'2009-01-25 04:57:59'),(24580,500,'10','LAOTIAN',0,'2009-01-25 04:57:59'),(24581,500,'11','VIETNAMESE',0,'2009-01-25 04:57:59'),(24582,500,'12','OTHER ASIAN',0,'2009-01-25 04:57:59'),(24583,500,'13','NATIVE HAWAIIAN',0,'2009-01-25 04:57:59'),(24584,500,'14','GUAMANIAN',0,'2009-01-25 04:57:59'),(24585,500,'15','SAMOAN',0,'2009-01-25 04:57:59'),(24586,500,'16','OTHER PACIFIC ISLANDER',0,'2009-01-25 04:57:59'),(24587,500,'17','NATIVE AMERICAN',0,'2009-01-25 04:57:59'),(24588,500,'18','OTHER RACE',0,'2009-01-25 04:57:59'),(24589,500,'2','BLACK/AFRICAN AMERICAN',0,'2009-01-25 04:57:59'),(24590,500,'3','ASIAN INDIAN',0,'2009-01-25 04:57:59'),(24591,500,'4','CAMBODIAN',0,'2009-01-25 04:57:59'),(24592,500,'5','CHINESE',0,'2009-01-25 04:57:59'),(24593,500,'6','FILIPINO',0,'2009-01-25 04:57:59'),(24594,500,'7','JAPANESE',0,'2009-01-25 04:57:59'),(24595,500,'8','HMONG',0,'2009-01-25 04:57:59'),(24596,500,'9','KOREAN',0,'2009-01-25 04:57:59'),(24597,500,'99','REFUSED TO STATE/UNKNOWN',0,'2009-01-25 04:57:59'),(24598,502,'-1','Patient Factor',0,'2009-01-25 04:57:59'),(24599,502,'-2','Situational Factor',0,'2009-01-25 04:57:59'),(24600,502,'-3','Alternate Test Given',0,'2009-01-25 04:57:59'),(24601,502,'-4','Refused',0,'2009-01-25 04:57:59'),(24602,503,'1','NORTH AMERICAN',0,'2009-01-25 04:57:59'),(24603,503,'2','SOUTH AMERICAN',0,'2009-01-25 04:57:59'),(24604,503,'3','CENTRAL AMERICAN',0,'2009-01-25 04:57:59'),(24605,503,'4','PUERTO RICAN',0,'2009-01-25 04:57:59'),(24606,503,'5','CUBAN',0,'2009-01-25 04:57:59'),(24607,503,'6','HAITIAN',0,'2009-01-25 04:57:59'),(24608,503,'7','OTHER',0,'2009-01-25 04:57:59'),(24609,505,'-6','Logical Skip',0,'2009-01-25 04:57:59'),(24610,505,'-7','Incomplete',0,'2009-01-25 04:57:59'),(24611,505,'-8','Unused Variable',0,'2009-01-25 04:57:59'),(24612,505,'-9','Missing Data',0,'2009-01-25 04:57:59'),(24613,506,'AB','Alberta',0,'2009-01-25 04:57:59'),(24614,506,'AK','ALASKA',0,'2009-01-25 04:57:59'),(24615,506,'AL','ALABAMA',0,'2009-01-25 04:57:59'),(24616,506,'AR','ARKANSAS',0,'2009-01-25 04:57:59'),(24617,506,'AZ','ARIZONA',0,'2009-01-25 04:57:59'),(24618,506,'BC','British Columbia',0,'2009-01-25 04:57:59'),(24619,506,'CA','CALIFORNIA',0,'2009-01-25 04:57:59'),(24620,506,'CO','COLORADO',0,'2009-01-25 04:57:59'),(24621,506,'CT','CONNECTICUT',0,'2009-01-25 04:57:59'),(24622,506,'DC','DISTRICT OF COLUMBIA',0,'2009-01-25 04:57:59'),(24623,506,'DE','DELAWARE',0,'2009-01-25 04:57:59'),(24624,506,'FL','FLORIDA',0,'2009-01-25 04:57:59'),(24625,506,'GA','GEORGIA',0,'2009-01-25 04:57:59'),(24626,506,'HI','HAWAII',0,'2009-01-25 04:57:59'),(24627,506,'IA','IOWA',0,'2009-01-25 04:57:59'),(24628,506,'ID','IDAHO',0,'2009-01-25 04:57:59'),(24629,506,'IL','ILLINOIS',0,'2009-01-25 04:57:59'),(24630,506,'IN','INDIANA',0,'2009-01-25 04:57:59'),(24631,506,'KS','KANSAS',0,'2009-01-25 04:57:59'),(24632,506,'KY','KENTUCKY',0,'2009-01-25 04:57:59'),(24633,506,'LA','LOUISIANA',0,'2009-01-25 04:57:59'),(24634,506,'MA','MASSACHUSETTS',0,'2009-01-25 04:57:59'),(24635,506,'MB','Manitoba',0,'2009-01-25 04:57:59'),(24636,506,'MD','MARYLAND',0,'2009-01-25 04:57:59'),(24637,506,'ME','MAINE',0,'2009-01-25 04:57:59'),(24638,506,'MI','MICHIGAN',0,'2009-01-25 04:57:59'),(24639,506,'MN','MINNESOTA',0,'2009-01-25 04:57:59'),(24640,506,'MO','MISSOURI',0,'2009-01-25 04:57:59'),(24641,506,'MS','MISSISSIPPI',0,'2009-01-25 04:57:59'),(24642,506,'MT','MONTANA',0,'2009-01-25 04:57:59'),(24643,506,'NB','New Brunswick',0,'2009-01-25 04:57:59'),(24644,506,'NC','NORTH CAROLINA',0,'2009-01-25 04:57:59'),(24645,506,'ND','NORTH DAKOTA',0,'2009-01-25 04:57:59'),(24646,506,'NE','NEBRASKA',0,'2009-01-25 04:57:59'),(24647,506,'NF','Newfoundland',0,'2009-01-25 04:57:59'),(24648,506,'NH','NEW HAMPSHIRE',0,'2009-01-25 04:57:59'),(24649,506,'NJ','NEW JERSEY',0,'2009-01-25 04:57:59'),(24650,506,'NM','NEW MEXICO',0,'2009-01-25 04:57:59'),(24651,506,'NS','Nova Scotia',0,'2009-01-25 04:57:59'),(24652,506,'NT','Northwest Territories',0,'2009-01-25 04:57:59'),(24653,506,'NV','NEVADA',0,'2009-01-25 04:57:59'),(24654,506,'NY','NEW YORK',0,'2009-01-25 04:57:59'),(24655,506,'OH','OHIO',0,'2009-01-25 04:57:59'),(24656,506,'OK','OKLAHOMA',0,'2009-01-25 04:57:59'),(24657,506,'ON','Ontario',0,'2009-01-25 04:57:59'),(24658,506,'OR','OREGON',0,'2009-01-25 04:57:59'),(24659,506,'PA','PENNSYLVANIA',0,'2009-01-25 04:57:59'),(24660,506,'PQ','Quebec',0,'2009-01-25 04:57:59'),(24661,506,'RI','RHODE ISLAND',0,'2009-01-25 04:57:59'),(24662,506,'SC','SOUTH CAROLINA',0,'2009-01-25 04:57:59'),(24663,506,'SD','SOUTH DAKOTA',0,'2009-01-25 04:57:59'),(24664,506,'SK','Saskatchewan',0,'2009-01-25 04:57:59'),(24665,506,'TN','TENNESSEE',0,'2009-01-25 04:57:59'),(24666,506,'TX','TEXAS',0,'2009-01-25 04:57:59'),(24667,506,'UT','UTAH',0,'2009-01-25 04:57:59'),(24668,506,'VA','VIRGINIA',0,'2009-01-25 04:57:59'),(24669,506,'VT','VERMONT',0,'2009-01-25 04:57:59'),(24670,506,'WA','WASHINGTON',0,'2009-01-25 04:57:59'),(24671,506,'WI','WISCONSIN',0,'2009-01-25 04:57:59'),(24672,506,'WV','WEST VIRGINIA',0,'2009-01-25 04:57:59'),(24673,506,'WY','WYOMING',0,'2009-01-25 04:57:59'),(24674,507,'CLOSED',NULL,0,'2009-01-25 04:57:59'),(24675,507,'OPEN',NULL,0,'2009-01-25 04:57:59'),(24676,508,'CALL DOCTOR',NULL,0,'2009-01-25 04:57:59'),(24677,508,'CALL PATIENT/CAREGIVER',NULL,0,'2009-01-25 04:57:59'),(24678,508,'EMAIL',NULL,0,'2009-01-25 04:57:59'),(24679,508,'GENERIC TASK',NULL,0,'2009-01-25 04:57:59'),(24680,508,'LOCATE',NULL,0,'2009-01-25 04:57:59'),(24681,508,'MAKE DECISION',NULL,0,'2009-01-25 04:57:59'),(24682,508,'SCHEDULE VISIT',NULL,0,'2009-01-25 04:57:59'),(24683,508,'SEND BY FAX',NULL,0,'2009-01-25 04:57:59'),(24684,508,'SEND BY MAIL',NULL,0,'2009-01-25 04:57:59'),(24685,509,'Usually',NULL,1,'2009-01-25 04:57:59'),(24686,509,'Sometimes',NULL,2,'2009-01-25 04:57:59'),(24687,509,'Rarely',NULL,3,'2009-01-25 04:57:59'),(24688,510,'Usually',NULL,1,'2009-01-25 04:57:59'),(24689,510,'Sometimes',NULL,2,'2009-01-25 04:57:59'),(24690,510,'Rarely',NULL,3,'2009-01-25 04:57:59'),(24691,510,'Don\'t Know',NULL,4,'2009-01-25 04:57:59'),(24692,512,'CAME IN',NULL,0,'2009-01-25 04:57:59'),(24693,512,'COMPLETE',NULL,0,'2009-01-25 04:57:59'),(24694,512,'MAC CANCELED',NULL,0,'2009-01-25 04:57:59'),(24695,512,'NO SHOW',NULL,0,'2009-01-25 04:57:59'),(24696,512,'PATIENT CANCELED',NULL,0,'2009-01-25 04:57:59'),(24697,512,'SCHEDULED',NULL,0,'2009-01-25 04:57:59'),(24698,514,'0','Yes',1,'2009-01-25 04:57:59'),(24699,514,'1','No',2,'2009-01-25 04:57:59'),(24700,515,'1','Yes',1,'2009-01-25 04:57:59'),(24701,515,'0','No',2,'2009-01-25 04:57:59'),(24715,511,'GENERAL','UCSF',0,'2009-08-20 23:54:11'),(24719,513,'GENERAL','TEST VISIT',0,'2010-07-07 07:00:00'),(24720,516,'1','Yes',1,'2009-05-12 00:17:48'),(24721,516,'0','No',2,'2009-05-12 00:17:48'),(24722,516,'9','N/A',3,'2009-05-12 00:17:48'),(24723,517,'0','Absent',1,'2011-03-17 19:35:59'),(24724,517,'1','Present',2,'2011-03-17 19:35:59'),(24725,518,'0','Normal',1,'2011-03-17 19:36:13'),(24726,518,'1','Abnormal',2,'2011-03-17 19:36:13');
/*!40000 ALTER TABLE `listvalues` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient` (
  `PIDN` int(10) NOT NULL AUTO_INCREMENT,
  `LName` varchar(25) NOT NULL,
  `MInitial` char(1) DEFAULT NULL,
  `FName` varchar(25) NOT NULL,
  `Suffix` varchar(15) DEFAULT NULL,
  `Degree` varchar(15) DEFAULT NULL,
  `DOB` datetime DEFAULT NULL,
  `AGE` int(10) DEFAULT NULL,
  `Gender` tinyint(3) DEFAULT NULL,
  `Hand` varchar(25) DEFAULT NULL,
  `Deceased` tinyint(1) NOT NULL DEFAULT '0',
  `DOD` datetime DEFAULT NULL,
  `PrimaryLanguage` varchar(25) DEFAULT NULL,
  `TestingLanguage` varchar(25) DEFAULT NULL,
  `TransNeeded` tinyint(1) DEFAULT '0',
  `TransLanguage` varchar(25) DEFAULT NULL,
  `EnterBy` varchar(25) DEFAULT NULL,
  `DupNameFlag` tinyint(1) NOT NULL DEFAULT '0',
  `FullNameRev` varchar(100) DEFAULT NULL,
  `FullName` varchar(100) DEFAULT NULL,
  `FullNameRevNoSuffix` varchar(100) DEFAULT NULL,
  `FullNameNoSuffix` varchar(100) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`PIDN`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patientconsent`
--

DROP TABLE IF EXISTS `patientconsent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patientconsent` (
  `ConsentID` int(10) NOT NULL AUTO_INCREMENT,
  `PIDN` int(10) NOT NULL,
  `CareID` int(10) DEFAULT NULL,
  `ProjName` varchar(75) NOT NULL,
  `ConsentType` varchar(50) NOT NULL,
  `ConsentDate` timestamp NULL DEFAULT NULL,
  `ExpirationDate` datetime DEFAULT NULL,
  `WithdrawlDate` datetime DEFAULT NULL,
  `Note` varchar(100) DEFAULT NULL,
  `CapacityReviewBy` varchar(25) DEFAULT NULL,
  `ConsentRevision` varchar(10) DEFAULT NULL,
  `ConsentDeclined` varchar(10) DEFAULT NULL,
  `CTreasearch` varchar(10) DEFAULT NULL,
  `CTneuro` varchar(10) DEFAULT NULL,
  `CTDNA` varchar(10) DEFAULT NULL,
  `CTGenetic` varchar(10) DEFAULT NULL,
  `CTGeneticShare` varchar(10) DEFAULT NULL,
  `CTlumbar` varchar(10) DEFAULT NULL,
  `CTvideo` varchar(10) DEFAULT NULL,
  `CTaudio` varchar(10) DEFAULT NULL,
  `CTmediaedu` varchar(10) DEFAULT NULL,
  `CT1point5T` varchar(10) DEFAULT NULL,
  `CT4t` varchar(10) DEFAULT NULL,
  `CTotherstudy` varchar(10) DEFAULT NULL,
  `CTfollowup` varchar(10) DEFAULT NULL,
  `CTmusic` varchar(10) DEFAULT NULL,
  `CTpart` varchar(10) DEFAULT NULL,
  `CTcarepart` varchar(10) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ConsentID`),
  KEY `patientconsent__PIDN` (`PIDN`),
  KEY `patientconsent__ProjName` (`ProjName`),
  CONSTRAINT `patientconsent__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `patientconsent__ProjName` FOREIGN KEY (`ProjName`) REFERENCES `projectunit` (`ProjUnitDesc`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patientconsent`
--

LOCK TABLES `patientconsent` WRITE;
/*!40000 ALTER TABLE `patientconsent` DISABLE KEYS */;
/*!40000 ALTER TABLE `patientconsent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patientdoctors`
--

DROP TABLE IF EXISTS `patientdoctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patientdoctors` (
  `PIDNDocID` int(10) NOT NULL AUTO_INCREMENT,
  `DocID` int(10) NOT NULL,
  `PIDN` int(10) NOT NULL,
  `DocStat` varchar(25) DEFAULT NULL,
  `DocNote` varchar(100) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`PIDNDocID`),
  KEY `patientdoctors__PIDN` (`PIDN`),
  KEY `patientdoctors__DocID` (`DocID`),
  CONSTRAINT `patientdoctors__DocID` FOREIGN KEY (`DocID`) REFERENCES `doctor` (`DocID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `patientdoctors__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patientdoctors`
--

LOCK TABLES `patientdoctors` WRITE;
/*!40000 ALTER TABLE `patientdoctors` DISABLE KEYS */;
/*!40000 ALTER TABLE `patientdoctors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preference`
--

DROP TABLE IF EXISTS `preference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preference` (
  `preference_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) DEFAULT NULL,
  `context` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `visible` int(11) NOT NULL DEFAULT '1',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`preference_id`),
  KEY `preference__user_id` (`user_id`),
  CONSTRAINT `preference__user_id` FOREIGN KEY (`user_id`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preference`
--

LOCK TABLES `preference` WRITE;
/*!40000 ALTER TABLE `preference` DISABLE KEYS */;
INSERT INTO `preference` VALUES (1,NULL,'calendar','displayRange','Default View (e.g. Month, Week)','Month',0,'2010-01-26 14:10:06'),(2,NULL,'calendar','showDayLength','Sets day length in week or day views to display either full day or work day','Work Day',0,'2010-01-26 17:13:23'),(3,NULL,'CFR_confocal_calendar','receive_cancellation_alert','Set to YES if you wish to receive an email alert when a scheduled reservation for the confocal microscope has been cancelled','1',1,'2010-02-08 13:20:46');
/*!40000 ALTER TABLE `preference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectunit`
--

DROP TABLE IF EXISTS `projectunit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectunit` (
  `ProjUnitID` int(10) NOT NULL AUTO_INCREMENT,
  `Project` varchar(25) NOT NULL,
  `Unit` varchar(25) DEFAULT NULL,
  `Status` varchar(25) NOT NULL DEFAULT 'ACTIVE',
  `EffDate` timestamp NULL DEFAULT NULL,
  `ExpDate` datetime DEFAULT NULL,
  `ProjUnitDesc` varchar(75) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ProjUnitID`),
  KEY `projectunit_ProjUnitDesc` (`ProjUnitDesc`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectunit`
--

LOCK TABLES `projectunit` WRITE;
/*!40000 ALTER TABLE `projectunit` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectunit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource_calendar`
--

DROP TABLE IF EXISTS `resource_calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource_calendar` (
  `calendar_id` int(10) unsigned NOT NULL,
  `resource_type` varchar(25) NOT NULL,
  `location` varchar(100) DEFAULT NULL,
  `contact_id` int(10) NOT NULL,
  PRIMARY KEY (`calendar_id`),
  KEY `resource_calendar__calendar` (`calendar_id`),
  KEY `resource_calendar__user_id` (`contact_id`),
  CONSTRAINT `resource_calendar__calendar` FOREIGN KEY (`calendar_id`) REFERENCES `calendar` (`calendar_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `resource_calendar__user_id` FOREIGN KEY (`contact_id`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource_calendar`
--

LOCK TABLES `resource_calendar` WRITE;
/*!40000 ALTER TABLE `resource_calendar` DISABLE KEYS */;
/*!40000 ALTER TABLE `resource_calendar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks` (
  `TaskID` int(10) NOT NULL AUTO_INCREMENT,
  `PIDN` int(10) NOT NULL,
  `ProjName` varchar(75) DEFAULT NULL,
  `OpenedDate` datetime DEFAULT NULL,
  `OpenedBy` varchar(25) DEFAULT NULL,
  `TaskType` varchar(25) DEFAULT NULL,
  `TaskDesc` varchar(255) DEFAULT NULL,
  `DueDate` datetime DEFAULT NULL,
  `TaskStatus` varchar(50) DEFAULT NULL,
  `AssignedTo` varchar(25) DEFAULT NULL,
  `WorkingNotes` varchar(255) DEFAULT NULL,
  `ClosedDate` datetime DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`TaskID`),
  KEY `tasks__PIDN` (`PIDN`),
  KEY `tasks__ProjName` (`ProjName`),
  CONSTRAINT `tasks__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `tasks__ProjName` FOREIGN KEY (`ProjName`) REFERENCES `projectunit` (`ProjUnitDesc`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uploadedfiles`
--

DROP TABLE IF EXISTS `uploadedfiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uploadedfiles` (
  `InstrID` int(10) NOT NULL,
  `FileName` varchar(500) DEFAULT NULL,
  `FileContents` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`InstrID`),
  KEY `uploadedfiles__InstrID` (`InstrID`),
  CONSTRAINT `uploadedfiles__InstrID` FOREIGN KEY (`InstrID`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uploadedfiles`
--

LOCK TABLES `uploadedfiles` WRITE;
/*!40000 ALTER TABLE `uploadedfiles` DISABLE KEYS */;
/*!40000 ALTER TABLE `uploadedfiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `versionhistory`
--

DROP TABLE IF EXISTS `versionhistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `versionhistory` (
  `Module` varchar(25) NOT NULL,
  `Version` varchar(10) NOT NULL,
  `VersionDate` datetime NOT NULL,
  `Major` int(10) NOT NULL,
  `Minor` int(10) NOT NULL,
  `Fix` int(10) NOT NULL,
  `UpdateRequired` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Module`,`Version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `versionhistory`
--

LOCK TABLES `versionhistory` WRITE;
/*!40000 ALTER TABLE `versionhistory` DISABLE KEYS */;
INSERT INTO `versionhistory` VALUES ('lava-core-model','3.0.3','2009-07-02 15:51:49',3,0,3,1),('lava-crms-model','3.0.3','2009-07-02 16:01:25',3,0,3,1);
/*!40000 ALTER TABLE `versionhistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `viewproperty`
--

DROP TABLE IF EXISTS `viewproperty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `viewproperty` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `messageCode` varchar(255) DEFAULT NULL,
  `locale` varchar(10) NOT NULL DEFAULT 'en',
  `instance` varchar(25) NOT NULL DEFAULT 'lava',
  `scope` varchar(25) NOT NULL,
  `prefix` varchar(50) DEFAULT NULL,
  `entity` varchar(100) NOT NULL,
  `property` varchar(100) NOT NULL,
  `section` varchar(50) DEFAULT NULL,
  `context` varchar(10) DEFAULT NULL,
  `style` varchar(25) DEFAULT NULL,
  `required` varchar(3) DEFAULT NULL,
  `label` varchar(500) DEFAULT NULL,
  `maxLength` smallint(5) DEFAULT NULL,
  `size` smallint(5) DEFAULT NULL,
  `indentLevel` smallint(5) DEFAULT '0',
  `attributes` varchar(100) DEFAULT NULL,
  `list` varchar(50) DEFAULT NULL,
  `listAttributes` varchar(100) DEFAULT NULL,
  `propOrder` int(10) DEFAULT NULL,
  `quickHelp` varchar(500) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3924 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `viewproperty`
--

LOCK TABLES `viewproperty` WRITE;
/*!40000 ALTER TABLE `viewproperty` DISABLE KEYS */;
INSERT INTO `viewproperty` VALUES (2479,'*.appointment.id','en','lava','core',NULL,'appointment','id','details','c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2009-03-31 20:35:39'),(2480,'*.appointment.calendar.name','en','lava','core',NULL,'appointment','calendar.name','details','c','string','No','Calendar',NULL,NULL,0,NULL,NULL,NULL,2,NULL,'2009-03-31 20:35:39'),(2481,'*.appointment.organizerId','en','lava','core','','appointment','organizerId','details','c','range','No','Organizer',NULL,NULL,NULL,'','appointment.organizer','',3,'','2009-03-31 20:35:39'),(2482,'*.appointment.type','en','lava','core',NULL,'appointment','type','details','i','range','No','Type',NULL,NULL,0,NULL,'appointment.type',NULL,4,NULL,'2009-03-31 20:35:39'),(2483,'*.appointment.description','en','lava','core',NULL,'appointment','description','details','i','text','No','Description',100,NULL,0,NULL,NULL,NULL,5,NULL,'2009-03-31 20:35:39'),(2484,'*.appointment.location','en','lava','core',NULL,'appointment','location','details','i','string','No','Location',100,NULL,0,NULL,NULL,NULL,6,NULL,'2009-03-31 20:35:39'),(2485,'*.appointment.startDate','en','lava','core','','appointment','startDate','details','i','date','Yes','Start Date',NULL,10,0,NULL,NULL,NULL,7,NULL,'2009-04-16 15:31:52'),(2486,'*.appointment.startTime','en','lava','core','','appointment','startTime','details','i','time','Yes','Time',NULL,NULL,0,'',NULL,'',8,'','2009-04-02 04:49:05'),(2487,'*.appointment.endDate','en','lava','core','','appointment','endDate','details','i','date','Yes','End Date',NULL,10,0,NULL,NULL,NULL,9,NULL,'2009-04-27 20:47:43'),(2488,'*.appointment.endTime','en','lava','core','','appointment','endTime','details','i','time','Yes','Time',NULL,NULL,0,NULL,NULL,NULL,10,NULL,'2009-04-16 15:30:06'),(2489,'*.appointment.status','en','lava','core',NULL,'appointment','status','details','i','range','No','Status',NULL,NULL,0,NULL,'resourceReservation.status',NULL,10,NULL,'2009-05-11 19:43:15'),(2490,'*.appointment.notes','en','lava','core',NULL,'appointment','notes','notes','i','unlimitedtext','No','Notes',NULL,NULL,0,'rows=\"10\" cols=\"45\"',NULL,NULL,11,NULL,'2009-03-31 20:35:39'),(2491,'*.appointment.organizer.userName','en','lava','core',NULL,'appointment','organizer.userName','details','c','string','No','Organizer',NULL,NULL,0,NULL,NULL,NULL,14,'Organizer Name','2009-06-11 17:00:00'),(2492,'*.appointment_change.type','en','lava','core',NULL,'appointment_change','type','details','c','range','Yes','Change Type',25,NULL,0,NULL,'resourceReservationChange.type',NULL,3,NULL,'2009-05-11 20:29:52'),(2493,'*.appointment_change.description','en','lava','core',NULL,'appointment_change','description','details','c','string','No','Description',NULL,NULL,0,NULL,NULL,NULL,4,NULL,'2009-05-11 20:29:52'),(2494,'*.appointment_change.changeBy.userName','en','lava','core',NULL,'appointment_change','changeBy.userName','details','c','string','Yes','Change By',NULL,NULL,0,NULL,NULL,NULL,5,NULL,'2009-05-11 20:29:52'),(2495,'*.appointment_change.changeTimestamp','en','lava','core',NULL,'appointment_change','changeTimestamp','details','c','timestamp','Yes','Change Timestamp',NULL,NULL,0,NULL,NULL,NULL,6,NULL,'2009-05-11 20:29:52'),(2496,'*.attendee.userId','en','lava','core',NULL,'attendee','userId','details','i','range','Yes','Attendee',NULL,NULL,0,NULL,'attendee.attendee',NULL,3,'Attendee','2009-06-03 20:40:16'),(2497,'*.attendee.role','en','lava','core',NULL,'attendee','role','details','i','range','Yes','Role',25,NULL,0,NULL,'attendee.role',NULL,4,'Attendee Role','2009-06-03 20:40:16'),(2498,'*.attendee.status','en','lava','core',NULL,'attendee','status','details','i','range','Yes','Status',25,NULL,0,NULL,'attendee.status',NULL,5,'Attendee Status','2009-06-03 20:40:16'),(2499,'*.attendee.notes','en','lava','core',NULL,'attendee','notes','details','i','test','No','Notes',100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,6,'Notes','2009-06-03 20:40:16'),(2500,'*.auditEntityHistory.id','en','lava','core',NULL,'auditEntityHistory','id',NULL,'c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-25 05:28:51'),(2501,'*.auditEntityHistory.entityId','en','lava','core',NULL,'auditEntityHistory','entityId',NULL,'c','numeric','Yes','Entity ID',NULL,NULL,0,NULL,NULL,NULL,3,'ID of the Entity','2009-01-25 05:28:51'),(2502,'*.auditEntityHistory.entity','en','lava','core',NULL,'auditEntityHistory','entity',NULL,'c','string','Yes','Entity',100,NULL,0,NULL,NULL,NULL,4,'Base entity name, e.g. Patient, Instrument (this is the entity name where the autoincrementing id field is defined)','2009-01-25 05:28:51'),(2503,'*.auditEntityHistory.entityType','en','lava','core',NULL,'auditEntityHistory','entityType',NULL,'c','string','No','Entity Type',100,NULL,0,NULL,NULL,NULL,5,'Optional subtype of the entity (e.g. MacPatient, BedsideScreen','2009-01-25 05:28:51'),(2504,'*.auditEntityHistory.auditType','en','lava','core',NULL,'auditEntityHistory','auditType',NULL,'c','string','Yes','Audit Type',10,NULL,0,NULL,NULL,NULL,6,'The type of auditing for the entity (e.g. CREATE, READ, UPDATE, DELETE)','2009-01-25 05:28:51'),(2505,'*.auditEventHistory.id','en','lava','core',NULL,'auditEventHistory','id',NULL,'c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-25 05:28:51'),(2506,'*.auditEventHistory.auditUser','en','lava','core',NULL,'auditEventHistory','auditUser',NULL,'c','string','Yes','Audit User',50,NULL,0,NULL,NULL,NULL,2,'The user who initiated the event','2009-01-25 05:28:51'),(2507,'*.auditEventHistory.auditHost','en','lava','core',NULL,'auditEventHistory','auditHost',NULL,'c','string','Yes','Audit Host',25,NULL,0,NULL,NULL,NULL,3,'The host (machine) that the event was initiated from','2009-01-25 05:28:51'),(2508,'*.auditEventHistory.auditTime','en','lava','core',NULL,'auditEventHistory','auditTime',NULL,'c','datetime','Yes','Audit Time',NULL,NULL,0,NULL,NULL,NULL,4,'The time that the event was initiated','2009-01-25 05:28:51'),(2509,'*.auditEventHistory.action','en','lava','core',NULL,'auditEventHistory','action',NULL,'c','string','Yes','Action',255,NULL,0,NULL,NULL,NULL,5,'The action id od the event','2009-01-25 05:28:51'),(2510,'*.auditEventHistory.actionEvent','en','lava','core',NULL,'auditEventHistory','actionEvent',NULL,'c','string','Yes','Action Event',50,NULL,0,NULL,NULL,NULL,6,'The event type of the event (e.g. add, view, delete, edit, list)','2009-01-25 05:28:51'),(2511,'*.auditEventHistory.actionIdParam','en','lava','core',NULL,'auditEventHistory','actionIdParam',NULL,'c','string','No','ID Param',50,NULL,0,NULL,NULL,NULL,7,'If an ID parameter was supplied for the event','2009-01-25 05:28:51'),(2512,'*.auditEventHistory.eventNote','en','lava','core',NULL,'auditEventHistory','eventNote',NULL,'c','text','No','Note',255,NULL,0,NULL,NULL,NULL,8,'An optional note field for the event','2009-01-25 05:28:51'),(2513,'*.auditEventHistory.exception','en','lava','core',NULL,'auditEventHistory','exception',NULL,'c','text','No','Exception',255,NULL,0,NULL,NULL,NULL,9,'If the event resulted in a handled exception','2009-01-25 05:28:51'),(2514,'*.auditEventHistory.exceptionMessage','en','lava','core',NULL,'auditEventHistory','exceptionMessage',NULL,'c','text','No','Exception Message',255,NULL,0,NULL,NULL,NULL,10,'The message associated with the handled exception.','2009-01-25 05:28:51'),(2515,'*.auditPropertyHistory.id','en','lava','core',NULL,'auditPropertyHistory','id',NULL,'c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-25 05:28:51'),(2516,'*.auditPropertyHistory.property','en','lava','core',NULL,'auditPropertyHistory','property',NULL,'c','string','Yes','Property Name',100,NULL,0,NULL,NULL,NULL,3,'The name of the entity property','2009-01-25 05:28:51'),(2517,'*.auditPropertyHistory.indexKey','en','lava','core',NULL,'auditPropertyHistory','indexKey',NULL,'c','string','No','Index Key Value',100,NULL,0,NULL,NULL,NULL,4,'If the property is a collection, the index into the collection for this particular subproperty value','2009-01-25 05:28:51'),(2518,'*.auditPropertyHistory.subproperty','en','lava','core',NULL,'auditPropertyHistory','subproperty',NULL,'c','string','No','Subproperty Name',255,NULL,0,NULL,NULL,NULL,5,'The name of the subproperty when theproperty is a collection','2009-01-25 05:28:51'),(2519,'*.auditPropertyHistory.oldValue','en','lava','core',NULL,'auditPropertyHistory','oldValue',NULL,'c','string','Yes','Old Value',255,NULL,0,NULL,NULL,NULL,6,'The old value or {CREATED} when the record is for a new value','2009-01-25 05:28:51'),(2520,'*.auditPropertyHistory.newValue','en','lava','core',NULL,'auditPropertyHistory','newValue',NULL,'c','string','Yes','New Value',255,NULL,0,NULL,NULL,NULL,7,'The new value or {DELETED} when the record is for a property deletion','2009-01-25 05:28:51'),(2521,'*.auditPropertyHistory.auditTimestamp','en','lava','core',NULL,'auditPropertyHistory','auditTimestamp',NULL,'c','timestamp','Yes','Audit Time',NULL,NULL,0,NULL,NULL,NULL,8,'The time of the event (copied from the Audit_Event table for convenience','2009-01-25 05:28:51'),(2522,'*.auditPropertyHistory.oldText','en','lava','core',NULL,'auditPropertyHistory','oldText',NULL,'c','unlimitedtext','Yes','Old Text Value',16,NULL,0,'rows=\"10\" cols=\"80\"',NULL,NULL,9,'The old text value or {CREATED} if the record if for a property creation','2009-01-25 05:28:51'),(2523,'*.auditPropertyHistory.newText','en','lava','core',NULL,'auditPropertyHistory','newText',NULL,'c','unlimitedtext','Yes','New Text Value',16,NULL,0,'rows=\"10\" cols=\"80\"',NULL,NULL,10,'The new text value of {DELETED} if the record is for a property deletion','2009-01-25 05:28:51'),(2524,'*.authGroup.id','en','lava','core',NULL,'authGroup','id','details','c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the authorization group','2009-01-25 05:28:51'),(2525,'*.authGroup.disabled','en','lava','core',NULL,'authGroup','disabled','status','i','range','Yes','Disabled',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,2,'Disabled','2009-01-25 05:28:51'),(2526,'*.authGroup.groupName','en','lava','core',NULL,'authGroup','groupName','details','i','string','Yes','Group Name',50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-25 05:28:51'),(2527,'*.authGroup.groupNameWithStatus','en','lava','core',NULL,'authGroup','groupNameWithStatus',NULL,'c','string','Yes','Group Name',50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-25 05:28:51'),(2528,'*.authGroup.effectiveDate','en','lava','core',NULL,'authGroup','effectiveDate','status','i','date','Yes','Effective Date',NULL,NULL,0,NULL,NULL,NULL,3,'Effective Date of the authorization group','2009-01-25 05:28:51'),(2529,'*.authGroup.expirationDate','en','lava','core',NULL,'authGroup','expirationDate','status','i','date','Yes','Expiration Date',NULL,NULL,0,NULL,NULL,NULL,4,'Expiration Date of the authorization group','2009-01-25 05:28:51'),(2530,'*.authGroup.notes','en','lava','core',NULL,'authGroup','notes','note','i','text','Yes','Notes',255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,5,'Notes about the authorization group','2009-01-25 05:28:51'),(2531,'*.authPermission.id','en','lava','core',NULL,'authPermission','id','details','c','numeric','Yes','Permission ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the permission','2009-01-25 05:28:51'),(2532,'*.authPermission.role.id','en','lava','core',NULL,'authPermission','role.id',NULL,'c','numeric','Yes','Role ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the role','2009-01-25 05:28:51'),(2533,'*.authPermission.role.roleName','en','lava','core',NULL,'authPermission','role.roleName',NULL,'i','string','Yes','Role Name',25,NULL,0,NULL,NULL,NULL,2,'Name of the role','2009-01-25 05:28:51'),(2534,'*.authPermission.roleId','en','lava','core',NULL,'authPermission','roleId','details','i','range','Yes','Role',25,NULL,0,NULL,'auth.roles',NULL,2,'The role that the permission applies to','2009-01-25 05:28:51'),(2535,'*.authPermission.permitDeny','en','lava','core',NULL,'authPermission','permitDeny','details','i','range','Yes','Permit / Deny',10,NULL,0,NULL,'authPermission.permitDeny',NULL,3,'Whether to PERMIT or DENY the permission to the role','2009-01-25 05:28:51'),(2536,'*.authPermission.scope','en','lava','core','','authPermission','scope','details','i','string','Yes','Scope',25,0,0,'','','',4,'Scope','2009-01-01 08:00:00'),(2537,'*.authPermission.module','en','lava','core',NULL,'authPermission','module','details','i','suggest','Yes','Module',25,NULL,0,NULL,NULL,NULL,5,'the moule that the permission covers','2009-01-25 05:28:51'),(2538,'*.authPermission.section','en','lava','core',NULL,'authPermission','section','details','i','suggest','Yes','Section',25,NULL,0,NULL,NULL,NULL,6,'the section that the permission covers','2009-01-25 05:28:51'),(2539,'*.authPermission.target','en','lava','core',NULL,'authPermission','target','details','i','suggest','Yes','Target',25,NULL,0,NULL,NULL,NULL,7,'the target that the permission covers','2009-01-25 05:28:51'),(2540,'*.authPermission.mode','en','lava','core',NULL,'authPermission','mode','details','i','suggest','Yes','Mode',25,NULL,0,NULL,NULL,NULL,8,'the mode that the permission covers','2009-01-25 05:28:51'),(2541,'*.authPermission.notes','en','lava','core',NULL,'authPermission','notes','note','i','text','No','Notes',100,NULL,0,'rows=\"2\" cols=\"40\"',NULL,NULL,9,'Notes','2009-01-25 05:28:51'),(2542,'*.authRole.id','en','lava','core',NULL,'authRole','id',NULL,'c','numeric','Yes','Role ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the role','2009-01-25 05:28:51'),(2543,'*.authRole.roleName','en','lava','core',NULL,'authRole','roleName',NULL,'i','string','Yes','Role Name',25,NULL,0,NULL,NULL,NULL,2,'Name of the role','2009-01-25 05:28:51'),(2544,'*.authRole.patientAccess','en','lava','core',NULL,'authRole','patientAccess',NULL,'i','range','Yes','Patient Access',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,3,'This role confers patient access to the user','2009-01-25 05:28:51'),(2545,'*.authRole.phiAccess','en','lava','core',NULL,'authRole','phiAccess',NULL,'i','range','Yes','PHI Access',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,4,'This role confers access to Protected Health Identifiers/Informantion','2009-01-25 05:28:51'),(2546,'*.authRole.ghiAccess','en','lava','core',NULL,'authRole','ghiAccess',NULL,'i','range','Yes','Genetic Access',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,5,'This role confers access to genetic health information to the user','2009-01-25 05:28:51'),(2547,'*.authRole.notes','en','lava','core',NULL,'authRole','notes',NULL,'i','text','No','Notes',255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,8,'Notes','2009-01-25 05:28:51'),(2548,'*.authRole.notes','en','lava','core',NULL,'authRole','notes',NULL,'i','text','No','Notes',255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,8,'Notes','2009-01-25 05:28:51'),(2549,'*.authRole.summaryInfo','en','lava','core',NULL,'authRole','summaryInfo','details','c','string','No','Summary',255,NULL,0,NULL,NULL,NULL,13,'Summary information for the role','2009-01-01 08:00:00'),(2550,'*.authUser.id','en','lava','core',NULL,'authUser','id',NULL,'c','numeric','Yes','User ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the user','2009-01-25 05:28:51'),(2551,'*.authUser.role.id','en','lava','core',NULL,'authUser','role.id','details','c','numeric','Yes','Role ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID of the role','2009-01-25 05:28:51'),(2552,'*.authUser.userName','en','lava','core',NULL,'authUser','userName','details','i','string','Yes','User Name',50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2553,'*.authUser.userNameWithStatus','en','lava','core',NULL,'authUser','userNameWithStatus',NULL,'c','string','Yes','User Name',50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name] with status','2009-01-25 05:28:51'),(2554,'filter.authUser.userName','en','lava','core','filter','authUser','userName',NULL,'i','string','No','User Name',50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2555,'*.authUser.login','en','lava','core',NULL,'authUser','login','details','i','string','No','Network Login',100,NULL,0,NULL,NULL,NULL,3,'Network Login for the user','2009-01-25 05:28:51'),(2556,'filter.authUser.login','en','lava','core','filter','authUser','login',NULL,'i','string','No','Network Login',100,NULL,0,NULL,NULL,NULL,3,'Network Login for the user','2009-01-25 05:28:51'),(2557,'*.authUser.email','en','lava','core',NULL,'authUser','email','details','i','text','No','Email',100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,4,'Email','2009-05-12 21:45:03'),(2558,'*.authUser.phone','en','lava','core',NULL,'authUser','phone','details','i','string','No','Phone',25,NULL,0,NULL,NULL,NULL,5,'Phone','2009-05-12 21:45:03'),(2559,'*.authUser.accessAgreementDate','en','lava','core',NULL,'authUser','accessAgreementDate','status','i','date','No','Access Agreement Date',NULL,NULL,0,NULL,NULL,NULL,7,'Date the user signed an Access Agreement for using the system','2009-01-25 05:28:51'),(2560,'filter.authUser.accessAgreementDateEnd','en','lava','core','filter','authUser','accessAgreementDateEnd',NULL,'i','date','No','and',NULL,NULL,0,NULL,NULL,NULL,7,'Date the user signed an Access Agreement for using the system','2009-01-25 05:28:51'),(2561,'filter.authUser.accessAgreementDateStart','en','lava','core','filter','authUser','accessAgreementDateStart',NULL,'i','date','No','Agreement Date Between',NULL,NULL,0,NULL,NULL,NULL,7,'Date the user signed an Access Agreement for using the system','2009-01-25 05:28:51'),(2562,'*.authUser.effectiveDate','en','lava','core',NULL,'authUser','effectiveDate','status','i','date','Yes','Effective Date',NULL,NULL,0,NULL,NULL,NULL,8,'Effective Date','2009-01-25 05:28:51'),(2563,'filter.authUser.effectiveDateEnd','en','lava','core','filter','authUser','effectiveDateEnd',NULL,'i','date','No','and',NULL,NULL,0,NULL,NULL,NULL,8,'Effective Date','2009-01-25 05:28:51'),(2564,'filter.authUser.effectiveDateStart','en','lava','core','filter','authUser','effectiveDateStart',NULL,'i','date','No','Effective Date Between',NULL,NULL,0,NULL,NULL,NULL,8,'Effective Date','2009-01-25 05:28:51'),(2565,'*.authUser.expirationDate','en','lava','core',NULL,'authUser','expirationDate','status','i','date','No','Expiration Date',NULL,NULL,0,NULL,NULL,NULL,9,'Expiration Date','2009-01-25 05:28:51'),(2566,'filter.authUser.expirationDateEnd','en','lava','core','filter','authUser','expirationDateEnd',NULL,'i','date','No','and',NULL,NULL,0,NULL,NULL,NULL,9,'Expiration Date','2009-01-25 05:28:51'),(2567,'filter.authUser.expirationDateStart','en','lava','core','filter','authUser','expirationDateStart',NULL,'i','date','No','Expiration Date Between',NULL,NULL,0,NULL,NULL,NULL,9,'Expiration Date','2009-01-25 05:28:51'),(2568,'*.authUser.shortUserName','en','lava','core',NULL,'authUser','shortUserName','details','c','string','No','Short User Name',53,NULL,0,NULL,NULL,NULL,10,'Shortened User name','2009-01-25 05:28:51'),(2569,'*.authUser.shortUserNameRev','en','lava','core',NULL,'authUser','shortUserNameRev','details','c','string','No','Short User Name Reversed',54,NULL,0,NULL,NULL,NULL,11,'Shortened User Name Reversed','2009-01-25 05:28:51'),(2570,'*.authUser.notes','en','lava','core',NULL,'authUser','notes','note','i','text','No','Notes',255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,12,'Notes','2009-01-25 05:28:51'),(2571,'*.authUser.authenticationType','en','lava','core',NULL,'authUser','authenticationType','details','i','range','Yes','Auth Type',10,NULL,0,NULL,'authUser.authenticationType',NULL,12,'Authentication Type','2009-05-12 21:45:03'),(2572,'*.authUser.disabled','en','lava','core',NULL,'authUser','disabled','status','i','range','Yes','Disabled',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,13,'Disabled','2009-01-25 05:28:51'),(2573,'*.authUser.password','en','lava','core',NULL,'authUser','password','local','c','text','No','Password (hashed)',100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,13,'Password Hash','2009-05-12 21:45:03'),(2574,'*.authUser.passwordExpiration','en','lava','core',NULL,'authUser','passwordExpiration','local','c','timestamp','No','Password Exp.',NULL,NULL,0,NULL,NULL,NULL,14,'Password Expiration','2009-05-12 21:45:03'),(2575,'*.authUser.passwordResetToken','en','lava','core',NULL,'authUser','passwordResetToken','local','c','text','No','Reset Token',100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,15,'Password Reset Token','2009-05-12 21:45:03'),(2576,'*.authUser.passwordResetExpiration','en','lava','core',NULL,'authUser','passwordResetExpiration','local','c','timestamp','No','Reset Exp.',NULL,NULL,0,NULL,NULL,NULL,16,'Password Reset Token Expires','2009-05-12 21:45:03'),(2577,'*.authUser.failedLoginCount','en','lava','core',NULL,'authUser','failedLoginCount','local','c','numeric','No','Failed Logins',NULL,NULL,0,NULL,NULL,NULL,17,'Failed Login Attempts','2009-05-12 21:45:03'),(2578,'*.authUser.lastFailedLogin','en','lava','core',NULL,'authUser','lastFailedLogin','local','c','timestamp','No','Last Failed Login',NULL,NULL,0,NULL,NULL,NULL,18,'Last Failed Logon Attempt','2009-05-12 21:45:03'),(2579,'*.authUser.accountLocked','en','lava','core',NULL,'authUser','accountLocked','local','c','timestamp','No','Account Locked',NULL,NULL,0,NULL,NULL,NULL,19,'Account Locked Timestamp','2009-05-12 21:45:03'),(2580,'*.authUserGroup.group.id','en','lava','core',NULL,'authUserGroup','group.id',NULL,'c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the authorization group','2009-01-25 05:28:51'),(2581,'*.authUserGroup.id','en','lava','core',NULL,'authUserGroup','id',NULL,'c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the User Role Assocaition','2009-01-25 05:28:51'),(2582,'*.authUserGroup.user.id','en','lava','core',NULL,'authUserGroup','user.id','details','c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-25 05:28:51'),(2583,'*.authUserGroup.user.id','en','lava','core',NULL,'authUserGroup','user.id','details','c','numeric','Yes','User ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID of the User','2009-01-25 05:28:51'),(2584,'*.authUserGroup.group.groupNameWithStatus','en','lava','core',NULL,'authUserGroup','group.groupNameWithStatus',NULL,'i','string','Yes','Group Name',50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-25 05:28:51'),(2585,'*.authUserGroup.user.login','en','lava','core',NULL,'authUserGroup','user.login','details','i','string','Yes','User Name',50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2586,'*.authUserGroup.user.userNameWithStatus','en','lava','core',NULL,'authUserGroup','user.userNameWithStatus','details','i','string','Yes','User Name',50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2587,'*.authUserGroup.userId','en','lava','core',NULL,'authUserGroup','userId','details','i','range','Yes','User',NULL,NULL,0,NULL,'auth.users',NULL,2,'Unique ID of the User','2009-01-25 05:28:51'),(2588,'*.authUserGroup.groupId','en','lava','core',NULL,'authUserGroup','groupId','details','i','range','Yes','Group',NULL,NULL,0,NULL,'auth.groups',NULL,3,'Unique ID of the Group','2009-01-25 05:28:51'),(2589,'*.authUserGroup.notes','en','lava','core',NULL,'authUserGroup','notes','note','i','text','No','Notes',255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,6,'Notes','2009-01-25 05:28:51'),(2590,'*.authUserPasswordDto.oldPassword','en','lava','core',NULL,'authUserPasswordDto','oldPassword',NULL,'i','password','Yes','Current Password',NULL,NULL,0,NULL,NULL,NULL,1,'Current Password','2009-05-12 21:45:03'),(2591,'*.authUserPasswordDto.newPassword','en','lava','core',NULL,'authUserPasswordDto','newPassword',NULL,'i','password','Yes','New Password',NULL,NULL,0,NULL,NULL,NULL,2,'New Password','2009-05-12 21:45:03'),(2592,'*.authUserPasswordDto.newPasswordConfirm','en','lava','core',NULL,'authUserPasswordDto','newPasswordConfirm',NULL,'i','password','Yes','New Password Confirm',NULL,NULL,0,NULL,NULL,NULL,3,'New Password Confirm','2009-05-12 21:45:03'),(2593,'*.authUserRole.group.id','en','lava','core',NULL,'authUserRole','group.id',NULL,'c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the authorization group','2009-01-25 05:28:51'),(2594,'*.authUserRole.id','en','lava','core',NULL,'authUserRole','id','details','c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the User/Group Role Association','2009-01-25 05:28:51'),(2595,'*.authUserRole.role.id','en','lava','core',NULL,'authUserRole','role.id',NULL,'c','numeric','Yes','Role ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the role','2009-01-25 05:28:51'),(2596,'*.authUserRole.user.id','en','lava','core',NULL,'authUserRole','user.id',NULL,'c','numeric','Yes','User ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID of the User','2009-01-25 05:28:51'),(2597,'*.authUserRole.group.groupNameWithStatus','en','lava','core',NULL,'authUserRole','group.groupNameWithStatus',NULL,'i','string','Yes','Group Name',50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-25 05:28:51'),(2598,'*.authUserRole.role.roleName','en','lava','core',NULL,'authUserRole','role.roleName',NULL,'i','string','Yes','Role Name',25,NULL,0,NULL,NULL,NULL,2,'Name of the role','2009-01-25 05:28:51'),(2599,'*.authUserRole.user.userNameWithStatus','en','lava','core',NULL,'authUserRole','user.userNameWithStatus',NULL,'i','string','Yes','User Name',50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2600,'*.authUserRole.role.summaryInfo','en','lava','core',NULL,'authUserRole','role.summaryInfo',NULL,'i','range','Yes','Summary',255,NULL,0,NULL,NULL,NULL,3,'Summary info about the role','2009-01-25 05:28:51'),(2601,'*.authUserRole.roleId','en','lava','core',NULL,'authUserRole','roleId','details','i','range','Yes','Role',25,NULL,0,NULL,'auth.roles',NULL,3,'The role name','2009-01-25 05:28:51'),(2602,'*.crmsAuthUserRole.unit','en','lava','crms',NULL,'crmsAuthUserRole','unit','details','i','suggest','No','Unit/Site',25,NULL,0,NULL,'projectUnit.units',NULL,5,'The program that the role applies to (* for any)','2009-01-25 05:28:51'),(2603,'*.authUserRole.userId','en','lava','core',NULL,'authUserRole','userId','details','i','range','No','User',NULL,NULL,0,NULL,'auth.users',NULL,7,'The user to assign the role to','2009-01-25 05:28:51'),(2604,'*.authUserRole.groupId','en','lava','core',NULL,'authUserRole','groupId','details','i','range','No','Group',NULL,NULL,0,NULL,'auth.groups',NULL,8,'the group to assign the role to','2009-01-25 05:28:51'),(2605,'*.authUserRole.notes','en','lava','core',NULL,'authUserRole','notes','note','i','text','No','Notes',255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,12,'Notes','2009-01-25 05:28:51'),(2606,'*.authUserRole.summaryInfo','en','lava','core','','authUserRole','summaryInfo','details','c','string','No','Summary',255,NULL,0,NULL,NULL,'',13,'Summary information for the role assignment','2009-01-01 08:00:00'),(2607,'*.calendar.id','en','lava','core',NULL,'calendar','id','details','c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2009-03-31 17:56:31'),(2608,'*.calendar.type','en','lava','core','','calendar','type','details','c','range','Yes','Type',25,NULL,0,NULL,'calendar.type','',2,'Type','2009-06-10 07:00:00'),(2609,'*.calendar.name','en','lava','core',NULL,'calendar','name','details','i','string','Yes','Name',100,NULL,0,NULL,NULL,NULL,3,NULL,'2009-03-31 17:56:31'),(2610,'*.calendar.description','en','lava','core',NULL,'calendar','description','details','i','text','No','Description',255,NULL,0,'rows=\"4\" cols=\"45\"',NULL,NULL,4,NULL,'2009-03-31 17:56:31'),(2611,'*.calendar.notes','en','lava','core',NULL,'calendar','notes','notes','i','unlimitedtext','No','Notes',NULL,NULL,0,'rows=\"10\" cols=\"45\"',NULL,NULL,5,NULL,'2009-03-31 17:56:31'),(2612,'*.hibernateProperty.id','en','lava','core',NULL,'HibernateProperty','id','','','','Yes','',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2613,'*.hibernateProperty.scope','en','lava','core',NULL,'HibernateProperty','scope','','','','Yes','',NULL,NULL,0,'','',NULL,2,'','2009-01-25 05:28:51'),(2614,'*.hibernateProperty.entity','en','lava','core',NULL,'HibernateProperty','entity','','','','Yes','',NULL,NULL,0,'','',NULL,3,'','2009-01-25 05:28:51'),(2615,'*.hibernateProperty.property','en','lava','core',NULL,'HibernateProperty','property','','','','Yes','',NULL,NULL,0,'','',NULL,4,'','2009-01-25 05:28:51'),(2616,'*.hibernateProperty.dbTable','en','lava','core',NULL,'HibernateProperty','dbTable','','','','Yes','',NULL,NULL,0,'','',NULL,5,'','2009-01-25 05:28:51'),(2617,'*.hibernateProperty.dbColumn','en','lava','core',NULL,'HibernateProperty','dbColumn','','','','Yes','',NULL,NULL,0,'','',NULL,6,'','2009-01-25 05:28:51'),(2618,'*.hibernateProperty.dbType','en','lava','core',NULL,'HibernateProperty','dbType','','','','No','',NULL,NULL,0,'','',NULL,7,'','2009-01-25 05:28:51'),(2619,'*.hibernateProperty.dbLength','en','lava','core',NULL,'HibernateProperty','dbLength','','','','No','',NULL,NULL,0,'','',NULL,8,'','2009-01-25 05:28:51'),(2620,'*.hibernateProperty.dbPrecision','en','lava','core',NULL,'HibernateProperty','dbPrecision','','','','No','',NULL,NULL,0,'','',NULL,9,'','2009-01-25 05:28:51'),(2621,'*.hibernateProperty.dbScale','en','lava','core',NULL,'HibernateProperty','dbScale','','','','No','',NULL,NULL,0,'','',NULL,10,'','2009-01-25 05:28:51'),(2622,'*.hibernateProperty.dbOrder','en','lava','core',NULL,'HibernateProperty','dbOrder','','','','No','',NULL,NULL,0,'','',NULL,11,'','2009-01-25 05:28:51'),(2623,'*.hibernateProperty.hibernateProperty','en','lava','core',NULL,'HibernateProperty','hibernateProperty','','','','No','',NULL,NULL,0,'','',NULL,12,'','2009-01-25 05:28:51'),(2624,'*.hibernateProperty.hibernateType','en','lava','core',NULL,'HibernateProperty','hibernateType','','','','No','',NULL,NULL,0,'','',NULL,13,'','2009-01-25 05:28:51'),(2625,'*.hibernateProperty.hibernateClass','en','lava','core',NULL,'HibernateProperty','hibernateClass','','','','No','',NULL,NULL,0,'','',NULL,14,'','2009-01-25 05:28:51'),(2626,'*.hibernateProperty.hibernateNotNull','en','lava','core',NULL,'HibernateProperty','hibernateNotNull','','','','No','',NULL,NULL,0,'','',NULL,15,'','2009-01-25 05:28:51'),(2627,'*.lavaSession.id','en','lava','core',NULL,'LavaSession','id',NULL,'c','numeric','Yes','Session ID',NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2009-01-25 05:28:51'),(2628,'*.lavaSession.serverInstanceId','en','lava','core',NULL,'LavaSession','serverInstanceId',NULL,'c','numeric','Yes','Server Instance ID',NULL,NULL,0,NULL,NULL,NULL,2,NULL,'2009-01-25 05:28:51'),(2629,'*.lavaSession.createTimestamp','en','lava','core',NULL,'LavaSession','createTimestamp',NULL,'c','datetime','Yes','Created Time',NULL,NULL,0,NULL,NULL,NULL,3,NULL,'2009-01-25 05:28:51'),(2630,'*.lavaSession.accessTimestamp','en','lava','core',NULL,'LavaSession','accessTimestamp',NULL,'c','datetime','Yes','Accessed Time',NULL,NULL,0,NULL,NULL,NULL,4,NULL,'2009-01-25 05:28:51'),(2631,'*.lavaSession.expireTimestamp','en','lava','core',NULL,'LavaSession','expireTimestamp',NULL,'c','datetime','Yes','Expiration Time',NULL,NULL,0,NULL,NULL,NULL,5,NULL,'2009-01-25 05:28:51'),(2632,'*.lavaSession.currentStatus','en','lava','core',NULL,'LavaSession','currentStatus',NULL,'c','range','Yes','Current Status',NULL,NULL,0,NULL,'lavaSession.status',NULL,6,NULL,'2009-01-25 05:28:51'),(2633,'*.lavaSession.userId','en','lava','core',NULL,'LavaSession','userId',NULL,'c','numeric','No','User ID',NULL,NULL,0,NULL,NULL,NULL,7,NULL,'2009-01-25 05:28:51'),(2634,'*.lavaSession.username','en','lava','core',NULL,'LavaSession','username',NULL,'c','string','No','Username',NULL,NULL,0,NULL,NULL,NULL,8,NULL,'2009-01-25 05:28:51'),(2635,'*.lavaSession.hostname','en','lava','core',NULL,'LavaSession','hostname',NULL,'c','string','No','Hostname',NULL,NULL,0,NULL,NULL,NULL,9,NULL,'2009-01-25 05:28:51'),(2636,'*.lavaSession.httpSessionId','en','lava','core',NULL,'LavaSession','httpSessionId',NULL,'c','string','No','HTTP Session',NULL,40,0,NULL,NULL,NULL,10,NULL,'2009-01-25 05:28:51'),(2637,'*.lavaSession.disconnectTime','en','lava','core',NULL,'LavaSession','disconnectTime',NULL,'i','time','No','Time',NULL,NULL,0,NULL,NULL,NULL,11,NULL,'2009-01-25 05:28:51'),(2638,'*.lavaSession.disconnectMessage','en','lava','core',NULL,'LavaSession','disconnectMessage',NULL,'i','text','No','Disconnect Message',NULL,NULL,0,'rows=\"4\", cols=\"45\"',NULL,NULL,12,NULL,'2009-01-25 05:28:51'),(2639,'*.lavaSession.notes','en','lava','core',NULL,'LavaSession','notes',NULL,'i','text','No','Notes',NULL,NULL,0,'rows=\"4\" cols=\"45\"',NULL,NULL,13,NULL,'2009-01-25 05:28:51'),(2640,'*.lavaSession.disconnectDate','en','lava','core',NULL,'lavaSession','disconnectDate',NULL,'i','date','No','Disconnect Date',10,NULL,0,NULL,NULL,NULL,16,NULL,'2009-04-27 22:11:44'),(2641,'*.reservation.organizer.userName','en','lava','core',NULL,'reservation','organizer.userName','details','c','string','No','Reserved By',NULL,NULL,0,NULL,NULL,NULL,14,'Reserved By','2009-06-11 16:40:26'),(2642,'*.reservation.organizerId','en','lava','core',NULL,'reservation','organizerId','details','i','range','Yes','Reserved By',NULL,NULL,0,NULL,'appointment.organizer',NULL,15,'Reserved By','2009-06-11 16:37:29'),(2643,'*.resourceCalendar.resourceType','en','lava','core',NULL,'resourceCalendar','resource_type','resource','i','range','Yes','Resource Type',25,NULL,0,NULL,'resourceCalendar.resourceType',NULL,2,'Resource Type','2009-06-03 17:27:39'),(2644,'*.resourceCalendar.location','en','lava','core',NULL,'resourceCalendar','location','resource','i','text','No','Location',100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,3,'Location','2009-06-03 17:27:39'),(2645,'*.resourceCalendar.contactId','en','lava','core',NULL,'resourceCalendar','contactId','resource','i','range','Yes','Contact',NULL,NULL,0,NULL,'resourceCalendar.contact',NULL,4,'Contact','2009-06-03 17:27:39'),(2646,'*.resourceCalendar.contact.email','en','lava','core',NULL,'resourceCalendar','contact.email','resource','c','string','No','Contact Email',100,NULL,0,NULL,NULL,NULL,6,'Contact Email','2009-06-03 17:27:39'),(2647,'*.resourceCalendar.contact.phone','en','lava','core',NULL,'resourceCalendar','contact.phone','resource','c','string','No','Contact Phone',25,NULL,0,NULL,NULL,NULL,7,'Contact Phone','2009-06-03 17:27:39'),(2648,'*.userPreferences.email','en','lava','core',NULL,'userPreferences','email','details','i','text','No','Email',100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,1,'Email','2009-05-26 23:48:36'),(2649,'*.userPreferences.phone','en','lava','core',NULL,'userPreferences','phone','details','i','string','No','Phone',25,NULL,0,NULL,NULL,NULL,2,'Phone','2009-05-26 23:49:22'),(2650,'*.viewProperty.id','en','lava','core',NULL,'ViewProperty','id','','','','Yes','',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2651,'*.viewProperty.messageCode','en','lava','core',NULL,'ViewProperty','messageCode','','','','No','',NULL,NULL,0,'','',NULL,2,'','2009-01-25 05:28:51'),(2652,'*.viewProperty.locale','en','lava','core',NULL,'ViewProperty','locale','','','','Yes','',NULL,NULL,0,'','',NULL,3,'','2009-01-25 05:28:51'),(2653,'*.viewProperty.scope','en','lava','core',NULL,'ViewProperty','scope','','','','Yes','',NULL,NULL,0,'','',NULL,4,'','2009-01-25 05:28:51'),(2654,'*.viewProperty.prefix','en','lava','core',NULL,'ViewProperty','prefix','','','','No','',NULL,NULL,0,'','',NULL,5,'','2009-01-25 05:28:51'),(2655,'*.viewProperty.entity','en','lava','core',NULL,'ViewProperty','entity','','','','Yes','',NULL,NULL,0,'','',NULL,6,'','2009-01-25 05:28:51'),(2656,'*.viewProperty.property','en','lava','core',NULL,'ViewProperty','property','','','','Yes','',NULL,NULL,0,'','',NULL,7,'','2009-01-25 05:28:51'),(2657,'*.viewProperty.section','en','lava','core',NULL,'ViewProperty','section','','','','No','',NULL,NULL,0,'','',NULL,8,'','2009-01-25 05:28:51'),(2658,'*.viewProperty.context','en','lava','core',NULL,'ViewProperty','context','','','','No','',NULL,NULL,0,'','',NULL,9,'','2009-01-25 05:28:51'),(2659,'*.viewProperty.style','en','lava','core',NULL,'ViewProperty','style','','','','No','',NULL,NULL,0,'','',NULL,10,'','2009-01-25 05:28:51'),(2660,'*.viewProperty.list','en','lava','core',NULL,'ViewProperty','list','','','','No','',NULL,NULL,0,'','',NULL,11,'','2009-01-25 05:28:51'),(2661,'*.viewProperty.attributes','en','lava','core',NULL,'ViewProperty','attributes','','','','No','',NULL,NULL,0,'','',NULL,12,'','2009-01-25 05:28:51'),(2662,'*.viewProperty.required','en','lava','core',NULL,'ViewProperty','required','','','','No','',NULL,NULL,0,'','',NULL,13,'','2009-01-25 05:28:51'),(2663,'*.viewProperty.label','en','lava','core',NULL,'ViewProperty','label','','','','No','',NULL,NULL,0,'','',NULL,14,'','2009-01-25 05:28:51'),(2664,'*.viewProperty.quickHelp','en','lava','core',NULL,'ViewProperty','quickHelp','','','','No','',NULL,NULL,0,'','',NULL,15,'','2009-01-25 05:28:51'),(2665,'*.viewProperty.propOrder','en','lava','core',NULL,'ViewProperty','propOrder','','','','No','',NULL,NULL,0,'','',NULL,16,'','2009-01-25 05:28:51'),(2666,'*.addEnrollmentStatus.patient_fullName','en','lava','crms',NULL,'addEnrollmentStatus','patient_fullName','','c','string','No','Patient',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2667,'*.addEnrollmentStatus.enrollmentStatus_projName','en','lava','crms',NULL,'addEnrollmentStatus','enrollmentStatus_projName',NULL,'i','range','Yes','Project',NULL,NULL,0,NULL,'enrollmentStatus.patientUnassignedProjects',NULL,10,NULL,'2009-01-25 05:28:51'),(2668,'*.addEnrollmentStatus.projName','en','lava','crms',NULL,'addEnrollmentStatus','projName','','i','range','Yes','Project',NULL,NULL,0,'','enrollmentStatus.patientUnassignedProjects',NULL,10,'','2009-01-25 05:28:51'),(2669,'*.addEnrollmentStatus.status','en','lava','crms',NULL,'addEnrollmentStatus','status','','i','range','Yes','Status',NULL,NULL,0,'','enrollmentStatus.projectStatus',NULL,11,'','2009-01-25 05:28:51'),(2670,'*.addEnrollmentStatus.statusDate','en','lava','crms',NULL,'addEnrollmentStatus','statusDate','','i','date','Yes','Status Date',NULL,NULL,0,'','',NULL,12,'','2009-01-25 05:28:51'),(2671,'*.addPatient.ignoreMatches','en','lava','crms',NULL,'addPatient','ignoreMatches','','i','toggle','No','This is a new patient/subject. Ignore possible matches.',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2673,'*.addPatient.patient_firstName','en','lava','crms',NULL,'addPatient','patient_firstName','','i','string','Yes','First Name',NULL,NULL,0,'','',NULL,2,'','2009-01-25 05:28:51'),(2675,'*.addPatient.patient_middleInitial','en','lava','crms',NULL,'addPatient','patient_middleInitial','','i','string','No','Middle Initial',NULL,NULL,0,'','',NULL,3,'','2009-01-25 05:28:51'),(2677,'*.addPatient.patient_lastName','en','lava','crms',NULL,'addPatient','patient_lastName','','i','string','Yes','Last Name',NULL,NULL,0,'','',NULL,4,'','2009-01-25 05:28:51'),(2678,'*.addPatient.patient_suffix','en','lava','crms',NULL,'addPatient','patient_suffix','','i','string','No','Suffix',NULL,NULL,0,'','',NULL,5,'','2009-01-25 05:28:51'),(2681,'*.addPatient.patient_degree','en','lava','crms',NULL,'addPatient','patient_degree','','i','string','No','Degree',NULL,NULL,0,'','',NULL,6,'','2009-01-25 05:28:51'),(2683,'*.addPatient.patient_birthDate','en','lava','crms',NULL,'addPatient','patient_birthDate','','i','date','Yes','Date of Birth',NULL,NULL,0,'','',NULL,9,'','2009-01-25 05:28:51'),(2684,'*.addPatient.enrollmentStatus_projName','en','lava','crms',NULL,'addPatient','enrollmentStatus_projName','','i','range','Yes','Initial Project',NULL,NULL,0,'','addPatient.projectList',NULL,10,'','2009-01-25 05:28:51'),(2686,'*.addPatient.status','en','lava','crms',NULL,'addPatient','status','','i','range','Yes','Initial Status',NULL,NULL,0,'','enrollmentStatus.projectStatus',NULL,11,'','2009-01-25 05:28:51'),(2687,'*.addPatient.statusDate','en','lava','crms',NULL,'addPatient','statusDate','','i','date','Yes','Status Date',NULL,NULL,0,'','',NULL,12,'','2009-01-25 05:28:51'),(2688,'*.addPatient.deidentified','en','lava','crms',NULL,'addPatient','deidentified','','i','toggle','No','Use Deidentified ID',NULL,NULL,0,'','',NULL,20,'','2009-01-25 05:28:51'),(2689,'*.addPatient.subjectId','en','lava','crms',NULL,'addPatient','subjectId','','i','string','Yes','Subject ID',NULL,NULL,0,'','',NULL,21,'','2009-01-25 05:28:51'),(2690,'filter.caregiver.firstName','en','lava','crms','filter','caregiver','firstName','','i','string','No','Caregiver First Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2691,'filter.caregiver.lastName','en','lava','crms','filter','caregiver','lastName','','i','string','No','Caregiver Last Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2692,'*.caregiver.id','en','lava','crms',NULL,'Caregiver','id','','c','numeric','Yes','',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2693,'*.caregiver.lastName','en','lava','crms',NULL,'Caregiver','lastName','','i','string','Yes','Last Name',NULL,NULL,0,'','',NULL,3,'','2009-01-25 05:28:51'),(2694,'*.caregiver.firstName','en','lava','crms',NULL,'Caregiver','firstName','','i','string','Yes','First Name',NULL,NULL,0,'','',NULL,4,'','2009-01-25 05:28:51'),(2695,'*.caregiver.gender','en','lava','crms',NULL,'Caregiver','gender','','i','range','No','Gender',NULL,NULL,0,'','generic.gender',NULL,5,'','2009-01-25 05:28:51'),(2696,'*.caregiver.relation','en','lava','crms',NULL,'Caregiver','relation','','i','range','Yes','Relation to patient',NULL,NULL,0,'','caregiver.contactRelations',NULL,6,'','2009-01-25 05:28:51'),(2697,'*.caregiver.livesWithPatient','en','lava','crms',NULL,'Caregiver','livesWithPatient','','i','range','Yes','Lives with patient',NULL,NULL,0,'','generic.yesNoZero',NULL,7,'','2009-01-25 05:28:51'),(2698,'*.caregiver.active','en','lava','crms',NULL,'Caregiver','active','','i','range','Yes','Is current/active',NULL,NULL,0,'','generic.yesNoZero',NULL,8,'','2009-01-25 05:28:51'),(2699,'*.caregiver.primaryLanguage','en','lava','crms',NULL,'caregiver','primaryLanguage','Language Details','i','suggest','No','Primary Language',25,NULL,0,'','patient.patientLanguage',NULL,8,'','2009-01-25 05:28:51'),(2700,'*.caregiver.transNeeded','en','lava','crms',NULL,'caregiver','transNeeded','Language Details','i','range','No','Interpreter Needed',NULL,NULL,0,'','generic.yesNoZero',NULL,9,'','2009-01-25 05:28:51'),(2701,'*.caregiver.transLanguage','en','lava','crms',NULL,'caregiver','transLanguage','Language Details','i','suggest','No','Interpreter Type',25,NULL,0,'','patient.patientLanguage',NULL,10,'','2009-01-25 05:28:51'),(2702,'*.caregiver.isPrimaryContact','en','lava','crms',NULL,'Caregiver','isPrimaryContact','','i','range','No','Is the primary contact',NULL,NULL,0,'','generic.yesNoZero',NULL,11,'','2009-01-25 05:28:51'),(2703,'*.caregiver.isContact','en','lava','crms',NULL,'Caregiver','isContact','','i','range','No','Is other contact',NULL,NULL,0,'','generic.yesNoZero',NULL,12,'','2009-01-25 05:28:51'),(2704,'*.caregiver.isContactNotes','en','lava','crms',NULL,'Caregiver','isContactNotes','','i','string','No','Other contact description',100,NULL,0,'','',NULL,13,'','2009-01-25 05:28:51'),(2705,'*.caregiver.isCaregiver','en','lava','crms',NULL,'Caregiver','isCaregiver','','i','range','No','Is a caregiver',NULL,NULL,0,'','generic.yesNoZero',NULL,14,'','2009-01-25 05:28:51'),(2706,'*.caregiver.isInformant','en','lava','crms',NULL,'Caregiver','isInformant','','i','range','No','Is an informant',NULL,NULL,0,'','generic.yesNoZero',NULL,15,'','2009-01-25 05:28:51'),(2707,'*.caregiver.isNextOfKin','en','lava','crms',NULL,'Caregiver','isNextOfKin','','i','range','No','Is the next of kin',NULL,NULL,0,'','generic.yesNoZero',NULL,16,'','2009-01-25 05:28:51'),(2708,'*.caregiver.isResearchSurrogate','en','lava','crms',NULL,'Caregiver','isResearchSurrogate','','i','range','No','Is the research surrogate',NULL,NULL,0,'','generic.yesNoZero',NULL,16,'','2009-01-25 05:28:51'),(2709,'*.caregiver.isPowerOfAttorney','en','lava','crms',NULL,'Caregiver','isPowerOfAttorney','','i','range','No','Has healthcare power of attorney',NULL,NULL,0,'','generic.yesNoZero',NULL,17,'','2009-01-25 05:28:51'),(2710,'*.caregiver.isOtherRole','en','lava','crms',NULL,'Caregiver','isOtherRole','','i','range','No','Has other role',NULL,NULL,0,'','generic.yesNoZero',NULL,18,'','2009-01-25 05:28:51'),(2711,'*.caregiver.otherRoleDesc','en','lava','crms',NULL,'Caregiver','otherRoleDesc','','i','string','No','Other role description',50,NULL,0,'','',NULL,19,'','2009-01-25 05:28:51'),(2712,'*.caregiver.note','en','lava','crms',NULL,'Caregiver','note','','i','text','No','',255,NULL,0,'rows=\"4\" cols=\"45\"','',NULL,20,'','2009-01-25 05:28:51'),(2713,'*.caregiver.birthDate','en','lava','crms',NULL,'Caregiver','birthDate','','i','date','No','DOB',NULL,NULL,0,'','',NULL,21,'','2009-01-25 05:28:51'),(2714,'*.caregiver.patient_fullName','en','lava','crms',NULL,'Caregiver','patient_fullName','','c','string','No','Patient',NULL,NULL,0,'','',NULL,21,'','2009-01-25 05:28:51'),(2715,'*.caregiver.patient_fullNameNoSuffix','en','lava','crms',NULL,'Caregiver','patient_fullNameNoSuffix','','c','string','No','Patient',NULL,NULL,0,'','',NULL,21,'','2009-01-25 05:28:51'),(2716,'*.caregiver.education','en','lava','crms',NULL,'Caregiver','education','','i','range','No','Education',NULL,NULL,0,'','generic.education',NULL,22,'','2009-01-25 05:28:51'),(2717,'*.caregiver.race','en','lava','crms',NULL,'Caregiver','race','','i','range','No','Race',NULL,NULL,0,'','generic.race',NULL,23,'','2009-01-25 05:28:51'),(2718,'*.caregiver.maritalStatus','en','lava','crms',NULL,'Caregiver','maritalStatus','','i','range','No','Marital Status',NULL,NULL,0,'','patient.maritalStatus',NULL,24,'','2009-01-25 05:28:51'),(2719,'*.caregiver.occupation','en','lava','crms',NULL,'Caregiver','occupation','','i','string','No','Occupation',NULL,NULL,0,'','',NULL,25,'','2009-01-25 05:28:51'),(2720,'*.caregiver.age','en','lava','crms',NULL,'Caregiver','age','','c','numeric','No','Age',NULL,NULL,0,'','',NULL,26,'','2009-01-25 05:28:51'),(2721,'*.caregiver.fullName','en','lava','crms',NULL,'Caregiver','fullName','','c','string','No','Caregiver',NULL,NULL,0,'','',NULL,27,'','2009-01-25 05:28:51'),(2722,'*.caregiver.fullNameRev','en','lava','crms',NULL,'Caregiver','fullNameRev','','c','string','No','Caregiver',NULL,NULL,0,'','',NULL,28,'','2009-01-25 05:28:51'),(2723,'*.caregiver.contactDesc','en','lava','crms',NULL,'Caregiver','contactDesc','','i','string','','Contact Status',NULL,NULL,0,'','',NULL,29,'','2009-01-25 05:28:51'),(2724,'*.caregiver.rolesDesc','en','lava','crms',NULL,'Caregiver','rolesDesc','','i','string','','Roles Description',NULL,NULL,0,'','',NULL,30,'','2009-01-25 05:28:51'),(2725,'filter.caregiver.patient.firstName','en','lava','crms','filter','caregiver.patient','firstName','','i','string','no','Patient First Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2726,'filter.caregiver.patient.lastName','en','lava','crms','filter','caregiver.patient','lastName','','i','string','no','Patient Last Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2727,'filter.consent.consentDateEnd','en','lava','crms','filter','consent','consentDateEnd','','i','date','no','and',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2728,'filter.consent.consentDateStart','en','lava','crms','filter','consent','consentDateStart','','i','date','no','Consent Date between',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2729,'filter.consent.consentDeclined','en','lava','crms','filter','consent','consentDeclined','','i','range','no','Declined',NULL,NULL,0,'','generic.textYesNo',NULL,NULL,'','2009-01-25 05:28:51'),(2730,'filter.consent.consentType','en','lava','crms','filter','consent','consentType','','i','range','no','Consent Type',NULL,NULL,0,'','consent.consentTypes',NULL,NULL,'','2009-01-25 05:28:51'),(2731,'filter.consent.patient.firstName','en','lava','crms','filter','consent','patient.firstName','','i','string','no','First Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2732,'filter.consent.patient.lastName','en','lava','crms','filter','consent','patient.lastName','','i','string','no','Last Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2733,'filter.consent.projName','en','lava','crms','filter','Consent','projName','','i','string','No','Project',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2734,'*.consent.id','en','lava','crms',NULL,'consent','id','','c','string','Yes','Consent ID',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2735,'*.consent.caregiverId','en','lava','crms',NULL,'consent','caregiverId','','i','range','No','Surrogate Consent',NULL,NULL,0,'','patient.caregivers',NULL,3,'','2009-01-25 05:28:51'),(2736,'*.consent.projName','en','lava','crms',NULL,'consent','projName','','i','range','Yes','Project',NULL,NULL,0,'','enrollmentStatus.patientProjects',NULL,4,'','2009-01-25 05:28:51'),(2737,'*.consent.consentType','en','lava','crms',NULL,'consent','consentType','','i','range','Yes','Consent Type',NULL,40,0,'','consent.consentTypes',NULL,5,'','2009-01-25 05:28:51'),(2738,'*.consent.consentDate','en','lava','crms',NULL,'consent','consentDate','','i','date','Yes','Consent Date',NULL,NULL,0,'','',NULL,6,'','2009-01-25 05:28:51'),(2739,'*.consent.expirationDate','en','lava','crms',NULL,'consent','expirationDate','','i','date','No','Expiration Date',NULL,NULL,0,'','',NULL,7,'','2009-01-25 05:28:51'),(2740,'*.consent.withdrawlDate','en','lava','crms',NULL,'consent','withdrawlDate','','i','date','No','Withdrawal Date',NULL,NULL,0,'','',NULL,8,'','2009-01-25 05:28:51'),(2741,'*.consent.note','en','lava','crms',NULL,'consent','note','','i','text','No','Notes',NULL,NULL,0,'','',NULL,9,'','2009-01-25 05:28:51'),(2742,'*.consent.capacityReviewBy','en','lava','crms',NULL,'consent','capacityReviewBy','','i','suggest','No0','Capacity Review By',NULL,NULL,0,'','project.staffList',NULL,10,'','2009-01-25 05:28:51'),(2743,'*.consent.consentRevision','en','lava','crms',NULL,'consent','consentRevision','','i','string','No','Revision #',NULL,NULL,0,'','',NULL,11,'','2009-01-25 05:28:51'),(2744,'*.consent.consentDeclined','en','lava','crms',NULL,'consent','consentDeclined','','i','range','No','Consent Declined',NULL,NULL,0,'','generic.textYesNo',NULL,12,'','2009-01-25 05:28:51'),(2761,'*.contactInfo.addressBlock','en','lava','crms',NULL,'contactInfo','addressBlock','','c','text','No','Address',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2762,'*.contactInfo.contactDesc','en','lava','crms',NULL,'contactInfo','contactDesc','','c','string','No','Contact Status',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2763,'*.contactInfo.phoneEmailBlock','en','lava','crms',NULL,'contactInfo','phoneEmailBlock','','c','text','No','Phone/Email',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2764,'filter.contactInfo.city','en','lava','crms','filter','contactInfo','city','','i','string','no','City',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2765,'filter.contactInfo.contactNameRev','en','lava','crms','filter','contactInfo','contactNameRev','','i','string','no','Contact Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2766,'filter.contactInfo.patient.firstName','en','lava','crms','filter','contactInfo','patient.firstName','','i','string','no','Patient First Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2767,'filter.contactInfo.patient.lastName','en','lava','crms','filter','contactInfo','patient.lastName','','i','string','no','Patient Last Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2768,'filter.contactInfo.state','en','lava','crms','filter','contactInfo','state','','i','string','no','State',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2769,'*.contactInfo.id','en','lava','crms',NULL,'contactInfo','id','','c','string','Yes','Contact Info ID',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2770,'*.contactInfo.caregiverId','en','lava','crms',NULL,'contactInfo','caregiverId','Type of Contact Info','i','range','No','Caregiver',NULL,NULL,0,'','patient.caregivers',NULL,3,'','2009-01-25 05:28:51'),(2771,'*.contactInfo.contactNameRev','en','lava','crms',NULL,'contactInfo','contactNameRev','','i','string','No','Contact Name',NULL,NULL,0,'','',NULL,3,'','2009-01-25 05:28:51'),(2772,'*.contactInfo.contactPatient','en','lava','crms',NULL,'contactInfo','contactPatient','Type of Contact Info','i','range','no','Contact Patient Directly',NULL,NULL,0,'','generic.yesNoZero',NULL,4,'','2009-01-25 05:28:51'),(2773,'*.contactInfo.isPatientResidence','en','lava','crms',NULL,'contactInfo','isPatientResidence','Type of Contact Info','i','range','no','Is this the patient\'s residence',NULL,NULL,0,'','generic.yesNoZero',NULL,5,'','2009-01-25 05:28:51'),(2774,'*.contactInfo.optOutMac','en','lava','crms',NULL,'contactInfo','optOutMac','OptOutOfMailings','i','range','no','Don\'t send center mailings',NULL,NULL,0,'','generic.yesNoZero',NULL,6,'','2009-01-25 05:28:51'),(2775,'*.contactInfo.optOutMac','en','lava','crms',NULL,'contactInfo','optOutMac','OptOutOfMailings','i','range','no','Don\'t send center mailings',NULL,NULL,0,'','generic.yesNoZero',NULL,6,'','2009-01-25 05:28:51'),(2776,'*.contactInfo.optOutAffiliates','en','lava','crms',NULL,'contactInfo','optOutAffiliates','OptOutOfMailings','i','range','no','Don\'t send affiliated org. mailings',NULL,NULL,0,'','generic.yesNoZero',NULL,7,'','2009-01-25 05:28:51'),(2777,'*.contactInfo.active','en','lava','crms',NULL,'contactInfo','active','Type of Contact Info','i','range','no','Active',NULL,NULL,0,'','generic.yesNoZero',NULL,8,'','2009-01-25 05:28:51'),(2778,'*.contactInfo.isCaregiver','en','lava','crms',NULL,'contactInfo','isCaregiver','','i','toggle','no','Contact info is for a caregiver/contact',NULL,NULL,0,'','',NULL,8,'','2009-01-25 05:28:51'),(2779,'*.contactInfo.address','en','lava','crms',NULL,'contactInfo','address','Address','i','string','No','Address',NULL,NULL,0,'','',NULL,9,'','2009-01-25 05:28:51'),(2780,'*.contactInfo.address2','en','lava','crms',NULL,'contactInfo','address2','Address','i','string','No','Address Line 2',NULL,NULL,0,'','',NULL,10,'','2009-01-25 05:28:51'),(2781,'*.contactInfo.city','en','lava','crms',NULL,'contactInfo','city','Address','i','suggest','No','City',NULL,NULL,0,'','contactInfo.city',NULL,11,'','2009-01-25 05:28:51'),(2782,'*.contactInfo.state','en','lava','crms',NULL,'contactInfo','state','Address','i','range','No','State',NULL,NULL,0,'','generic.state',NULL,12,'','2009-01-25 05:28:51'),(2783,'*.contactInfo.zip','en','lava','crms',NULL,'contactInfo','zip','Address','i','string','No','Zip',NULL,NULL,0,'','',NULL,13,'','2009-01-25 05:28:51'),(2784,'*.contactInfo.country','en','lava','crms',NULL,'contactInfo','country','Address','i','string','No','Country',NULL,NULL,0,'','',NULL,14,'','2009-01-25 05:28:51'),(2785,'*.contactInfo.phone1','en','lava','crms',NULL,'contactInfo','phone1','Phone/Email','i','string','No','First Phone',NULL,NULL,0,'','',NULL,15,'','2009-01-25 05:28:51'),(2786,'*.contactInfo.phoneType1','en','lava','crms',NULL,'contactInfo','phoneType1','Phone/Email','i','suggest','No','First Phone Type',NULL,NULL,0,'','generic.phoneType',NULL,16,'','2009-01-25 05:28:51'),(2787,'*.contactInfo.phone2','en','lava','crms',NULL,'contactInfo','phone2','Phone/Email','i','string','No','Second Phone',NULL,NULL,0,'','',NULL,17,'','2009-01-25 05:28:51'),(2788,'*.contactInfo.phoneType2','en','lava','crms',NULL,'contactInfo','phoneType2','Phone/Email','i','suggest','No','Second Phone Type',NULL,NULL,0,'','generic.phoneType',NULL,18,'','2009-01-25 05:28:51'),(2789,'*.contactInfo.phone3','en','lava','crms',NULL,'contactInfo','phone3','Phone/Email','i','string','No','Third Phone',NULL,NULL,0,'','',NULL,19,'','2009-01-25 05:28:51'),(2790,'*.contactInfo.phoneType3','en','lava','crms',NULL,'contactInfo','phoneType3','Phone/Email','i','suggest','No','Third Phone Type',NULL,NULL,0,'','generic.phoneType',NULL,20,'','2009-01-25 05:28:51'),(2791,'*.contactInfo.email','en','lava','crms',NULL,'contactInfo','email','Phone/Email','i','string','No','Email',NULL,NULL,0,'','',NULL,21,'','2009-01-25 05:28:51'),(2792,'*.contactInfo.notes','en','lava','crms',NULL,'contactInfo','notes','','i','text','No','',255,NULL,0,'rows=\"4\" cols=\"45\"','',NULL,22,'','2009-01-25 05:28:51'),(2793,'filter.contactLog.contact','en','lava','crms','filter','contactLog','contact','','i','string','no','Contact Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2794,'filter.contactLog.logDateEnd','en','lava','crms','filter','contactLog','logDateEnd','','i','date','no','     and',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2795,'filter.contactLog.logDateStart','en','lava','crms','filter','contactLog','logDateStart','','i','date','no','Log Date between',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2796,'filter.contactLog.projName','en','lava','crms','filter','ContactLog','projName','','i','string','No','Project',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2797,'filter.contactLog.staff','en','lava','crms','filter','contactLog','staff','','i','suggest','no','Staff Member',NULL,NULL,0,'','project.staffList',NULL,NULL,'','2009-01-25 05:28:51'),(2798,'*.contactLog.id','en','lava','crms',NULL,'ContactLog','id','','c','numeric','Yes','Log ID',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2799,'*.contactLog.projName','en','lava','crms',NULL,'ContactLog','projName','','i','range','No','Project',NULL,NULL,0,'','context.projectList',NULL,3,'','2009-01-25 05:28:51'),(2800,'*.contactLog.logTime','en','lava','crms','','contactLog','logTime',NULL,'i','time','No','Time',NULL,NULL,0,NULL,NULL,NULL,4,'Time of the contact','2009-04-23 20:00:00'),(2801,'*.contactLog.logDate','en','lava','crms',NULL,'ContactLog','logDate','','i','date','No','Log Date',NULL,10,0,'','',NULL,4,'','2009-01-25 05:28:51'),(2802,'*.contactLog.method','en','lava','crms',NULL,'ContactLog','method','','i','suggest','Yes','Contact method',NULL,NULL,0,'','contactLog.contactMethod',NULL,5,'','2009-01-25 05:28:51'),(2803,'*.contactLog.staffInit','en','lava','crms',NULL,'ContactLog','staffInit','','i','scale','Yes','Staff Initiated',NULL,NULL,0,'','generic.yesNoZero',NULL,6,'','2009-01-25 05:28:51'),(2804,'*.contactLog.staff','en','lava','crms',NULL,'ContactLog','staff','','i','suggest','No','Staff Name',NULL,NULL,0,'','project.staffList',NULL,7,'','2009-01-25 05:28:51'),(2805,'*.contactLog.contact','en','lava','crms',NULL,'ContactLog','contact','','i','string','No','Contact',NULL,NULL,0,'','',NULL,8,'','2009-01-25 05:28:51'),(2806,'*.contactLog.note','en','lava','crms',NULL,'ContactLog','note','','i','unlimitedtext','No','Note',NULL,NULL,0,'rows=\"20\" cols=\"100\"','',NULL,9,'','2009-01-25 05:28:51'),(2807,'filter.contactLog.patient.firstName','en','lava','crms','filter','contactLog.patient','firstName','','i','string','No','Pat. First Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2808,'filter.contactLog.patient.lastName','en','lava','crms','filter','contactLog.patient','lastName','','i','string','No','Pat. Last Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2809,'*.crmsAuthRole.patientAccess','en','lava','crms',NULL,'crmsAuthRole','patientAccess','details','i','scale','yes','Patient Access',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,4,'Does the role grant access to patients','2009-01-01 08:00:00'),(2810,'*.crmsAuthRole.phiAccess','en','lava','crms',NULL,'crmsAuthRole','phiAccess','details','i','scale','yes','PHI Access',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,5,'Does the role grant access to protected health information fields (PHI)','2009-01-01 08:00:00'),(2811,'*.crmsAuthRole.ghiAccess','en','lava','crms',NULL,'crmsAuthRole','ghiAccess','details','i','scale','yes','Genetics Access ',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,6,'Does the role grant access to genetic health information','2009-01-01 08:00:00'),(2812,'*.doctor.addressBlock','en','lava','crms',NULL,'Doctor','addressBlock','','c','text','No','Address',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2813,'*.doctor.phoneEmailBlock','en','lava','crms',NULL,'Doctor','phoneEmailBlock','','c','text','No','Phone/Email',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2814,'*.doctor.id','en','lava','crms',NULL,'Doctor','id','','c','string','Yes','id',NULL,80,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2815,'*.doctor.lastName','en','lava','crms',NULL,'Doctor','lastName','','i','string','Yes','Last Name',NULL,NULL,0,'','',NULL,2,'','2009-01-25 05:28:51'),(2816,'*.doctor.middleInitial','en','lava','crms',NULL,'Doctor','middleInitial','','i','string','No','Middle Initial',NULL,NULL,0,'','',NULL,3,'','2009-01-25 05:28:51'),(2817,'*.doctor.firstName','en','lava','crms',NULL,'Doctor','firstName','','i','string','Yes','First Name',NULL,NULL,0,'','',NULL,4,'','2009-01-25 05:28:51'),(2818,'*.doctor.address','en','lava','crms',NULL,'Doctor','address','','','text','No','Address',NULL,NULL,0,'','',NULL,5,'','2009-01-25 05:28:51'),(2819,'*.doctor.city','en','lava','crms',NULL,'Doctor','city','','i','suggest','No','City',NULL,NULL,0,'','doctor.city',NULL,6,'','2009-01-25 05:28:51'),(2820,'*.doctor.state','en','lava','crms',NULL,'Doctor','state','','i','range','No','State',NULL,NULL,0,'','generic.state',NULL,7,'','2009-01-25 05:28:51'),(2821,'*.doctor.zip','en','lava','crms',NULL,'Doctor','zip','','i','string','No','Zip',NULL,NULL,0,'','',NULL,8,'','2009-01-25 05:28:51'),(2822,'*.doctor.phone1','en','lava','crms',NULL,'Doctor','phone1','','i','string','No','First Phone',NULL,NULL,0,'','',NULL,9,'','2009-01-25 05:28:51'),(2823,'*.doctor.phoneType1','en','lava','crms',NULL,'Doctor','phoneType1','','i','suggest','No','First Phone Type',NULL,NULL,0,'','generic.phoneType',NULL,10,'','2009-01-25 05:28:51'),(2824,'*.doctor.phone2','en','lava','crms',NULL,'Doctor','phone2','','i','string','No','Second Phone',NULL,NULL,0,'','',NULL,11,'','2009-01-25 05:28:51'),(2825,'*.doctor.phoneType2','en','lava','crms',NULL,'Doctor','phoneType2','','i','suggest','No','Second Phone Type',NULL,NULL,0,'','generic.phoneType',NULL,12,'','2009-01-25 05:28:51'),(2826,'*.doctor.phone3','en','lava','crms',NULL,'Doctor','phone3','','i','string','No','Third Phone',NULL,NULL,0,'','',NULL,13,'','2009-01-25 05:28:51'),(2827,'*.doctor.phoneType3','en','lava','crms',NULL,'Doctor','phoneType3','','i','suggest','No','Third Phone Type',NULL,NULL,0,'','generic.phoneType',NULL,14,'','2009-01-25 05:28:51'),(2828,'*.doctor.email','en','lava','crms',NULL,'Doctor','email','','i','string','No','Email',NULL,NULL,0,'','',NULL,15,'','2009-01-25 05:28:51'),(2829,'*.doctor.docType','en','lava','crms',NULL,'Doctor','docType','','i','string','No','Doctor Type',NULL,NULL,0,'','',NULL,16,'','2009-01-25 05:28:51'),(2830,'*.doctor.fullNameRev','en','lava','crms',NULL,'Doctor','fullNameRev','','c','string','No','Doctor',NULL,NULL,0,'','',NULL,17,'','2009-01-25 05:28:51'),(2831,'*.doctor.fullName','en','lava','crms',NULL,'Doctor','fullName','','c','string','No','Doctor',NULL,NULL,0,'','',NULL,18,'','2009-01-25 05:28:51'),(2832,'filter.enrollmentStatus.enrolledDateEnd','en','lava','crms','filter','enrollmentStatus','enrolledDateEnd','','i','date','no','and',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2833,'filter.enrollmentStatus.enrolledDateStart','en','lava','crms','filter','enrollmentStatus','enrolledDateStart','','i','date','no','Enrolled between',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2834,'filter.enrollmentStatus.latestDesc','en','lava','crms','filter','enrollmentStatus','latestDesc','','i','range','no','Latest Status',NULL,NULL,0,'','enrollmentStatus.projectStatus',NULL,NULL,'','2009-01-25 05:28:51'),(2835,'filter.enrollmentStatus.patient.firstName','en','lava','crms','filter','enrollmentStatus','patient.firstName','','i','string','no','First Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2836,'filter.enrollmentStatus.patient.lastName','en','lava','crms','filter','enrollmentStatus','patient.lastName','','i','string','no','Last Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2837,'filter.enrollmentStatus.projName','en','lava','crms','filter','EnrollmentStatus','projName','','i','string','No','Project',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2838,'*.enrollmentStatus.id','en','lava','crms',NULL,'EnrollmentStatus','id','','c','numeric','Yes','Status ID',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2839,'*.enrollmentStatus.projName','en','lava','crms',NULL,'EnrollmentStatus','projName','','c','range','Yes','Project',75,NULL,0,'','enrollmentStatus.Project',NULL,3,'','2009-01-25 05:28:51'),(2840,'*.enrollmentStatus.subjectStudyId','en','lava','crms',NULL,'EnrollmentStatus','subjectStudyId','','i','string','No','Subject Study ID',10,NULL,0,'','',NULL,4,'','2009-01-25 05:28:51'),(2841,'*.enrollmentStatus.latestDesc','en','lava','crms',NULL,'EnrollmentStatus','latestDesc','','c','string','No','Latest Status',25,NULL,0,'','',NULL,5,'','2009-01-25 05:28:51'),(2842,'*.enrollmentStatus.referralSource','en','lava','crms',NULL,'EnrollmentStatus','referralSource','','i','suggest','No','Referral Source',75,NULL,0,'','enrollmentStatus.referralSource',NULL,5,'','2009-01-25 05:28:51'),(2843,'*.enrollmentStatus.latestDate','en','lava','crms',NULL,'EnrollmentStatus','latestDate','','c','date','No','Latest Status Date',NULL,NULL,0,'','',NULL,6,'','2009-01-25 05:28:51'),(2844,'*.enrollmentStatus.latestNote','en','lava','crms',NULL,'EnrollmentStatus','latestNote','','c','text','No','Latest Status Note',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,7,'','2009-01-25 05:28:51'),(2845,'*.enrollmentStatus.referredDesc','en','lava','crms',NULL,'EnrollmentStatus','referredDesc','','c','string','No','',25,NULL,0,'','',NULL,9,'','2009-01-25 05:28:51'),(2846,'*.enrollmentStatus.referredDate','en','lava','crms',NULL,'EnrollmentStatus','referredDate','','i','date','No','',NULL,NULL,0,'','',NULL,10,'','2009-01-25 05:28:51'),(2847,'*.enrollmentStatus.referredNote','en','lava','crms',NULL,'EnrollmentStatus','referredNote','','i','text','No','',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,11,'','2009-01-25 05:28:51'),(2848,'*.enrollmentStatus.deferredDesc','en','lava','crms',NULL,'EnrollmentStatus','deferredDesc','','c','string','No','',25,NULL,0,'','',NULL,13,'','2009-01-25 05:28:51'),(2849,'*.enrollmentStatus.deferredDate','en','lava','crms',NULL,'EnrollmentStatus','deferredDate','','i','date','No','',NULL,NULL,0,'','',NULL,14,'','2009-01-25 05:28:51'),(2850,'*.enrollmentStatus.deferredNote','en','lava','crms',NULL,'EnrollmentStatus','deferredNote','','i','text','No','',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,15,'','2009-01-25 05:28:51'),(2851,'*.enrollmentStatus.eligibleDesc','en','lava','crms',NULL,'EnrollmentStatus','eligibleDesc','','c','string','No','',25,NULL,0,'','',NULL,17,'','2009-01-25 05:28:51'),(2852,'*.enrollmentStatus.eligibleDate','en','lava','crms',NULL,'EnrollmentStatus','eligibleDate','','i','date','No','',NULL,NULL,0,'','',NULL,18,'','2009-01-25 05:28:51'),(2853,'*.enrollmentStatus.eligibleNote','en','lava','crms',NULL,'EnrollmentStatus','eligibleNote','','i','text','No','',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,19,'','2009-01-25 05:28:51'),(2854,'*.enrollmentStatus.ineligibleDesc','en','lava','crms',NULL,'EnrollmentStatus','ineligibleDesc','','c','string','No','',25,NULL,0,'','',NULL,21,'','2009-01-25 05:28:51'),(2855,'*.enrollmentStatus.ineligibleDate','en','lava','crms',NULL,'EnrollmentStatus','ineligibleDate','','i','date','No','',NULL,NULL,0,'','',NULL,22,'','2009-01-25 05:28:51'),(2856,'*.enrollmentStatus.ineligibleNote','en','lava','crms',NULL,'EnrollmentStatus','ineligibleNote','','i','text','No','',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,23,'','2009-01-25 05:28:51'),(2857,'*.enrollmentStatus.declinedDesc','en','lava','crms',NULL,'EnrollmentStatus','declinedDesc','','c','string','No','',25,NULL,0,'','',NULL,25,'','2009-01-25 05:28:51'),(2858,'*.enrollmentStatus.declinedDate','en','lava','crms',NULL,'EnrollmentStatus','declinedDate','','i','date','No','',NULL,NULL,0,'','',NULL,26,'','2009-01-25 05:28:51'),(2859,'*.enrollmentStatus.declinedNote','en','lava','crms',NULL,'EnrollmentStatus','declinedNote','','i','text','No','',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,27,'','2009-01-25 05:28:51'),(2860,'*.enrollmentStatus.enrolledDesc','en','lava','crms',NULL,'EnrollmentStatus','enrolledDesc','','c','string','No','',25,NULL,0,'','',NULL,29,'','2009-01-25 05:28:51'),(2861,'*.enrollmentStatus.enrolledDate','en','lava','crms',NULL,'EnrollmentStatus','enrolledDate','','i','date','No','',NULL,NULL,0,'','',NULL,30,'','2009-01-25 05:28:51'),(2862,'*.enrollmentStatus.enrolledNote','en','lava','crms',NULL,'EnrollmentStatus','enrolledNote','','i','text','No','',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,31,'','2009-01-25 05:28:51'),(2863,'*.enrollmentStatus.excludedDesc','en','lava','crms',NULL,'EnrollmentStatus','excludedDesc','','c','string','No','',25,NULL,0,'','',NULL,33,'','2009-01-25 05:28:51'),(2864,'*.enrollmentStatus.excludedDate','en','lava','crms',NULL,'EnrollmentStatus','excludedDate','','i','date','No','',NULL,NULL,0,'','',NULL,34,'','2009-01-25 05:28:51'),(2865,'*.enrollmentStatus.excludedNote','en','lava','crms',NULL,'EnrollmentStatus','excludedNote','','i','text','No','',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,35,'','2009-01-25 05:28:51'),(2866,'*.enrollmentStatus.withdrewDesc','en','lava','crms',NULL,'EnrollmentStatus','withdrewDesc','','c','string','No','',25,NULL,0,'','',NULL,37,'','2009-01-25 05:28:51'),(2867,'*.enrollmentStatus.withdrewDate','en','lava','crms',NULL,'EnrollmentStatus','withdrewDate','','i','date','No','',NULL,NULL,0,'','',NULL,38,'','2009-01-25 05:28:51'),(2868,'*.enrollmentStatus.withdrewNote','en','lava','crms',NULL,'EnrollmentStatus','withdrewNote','','i','text','No','',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,39,'','2009-01-25 05:28:51'),(2869,'*.enrollmentStatus.inactiveDesc','en','lava','crms',NULL,'EnrollmentStatus','inactiveDesc','','c','string','No','',25,NULL,0,'','',NULL,41,'','2009-01-25 05:28:51'),(2870,'*.enrollmentStatus.inactiveDate','en','lava','crms',NULL,'EnrollmentStatus','inactiveDate','','i','date','No','',NULL,NULL,0,'','',NULL,42,'','2009-01-25 05:28:51'),(2871,'*.enrollmentStatus.inactiveNote','en','lava','crms',NULL,'EnrollmentStatus','inactiveNote','','i','text','No','',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,43,'','2009-01-25 05:28:51'),(2872,'*.enrollmentStatus.deceasedDesc','en','lava','crms',NULL,'EnrollmentStatus','deceasedDesc','','c','string','No','',25,NULL,0,'','',NULL,48,'','2009-01-25 05:28:51'),(2873,'*.enrollmentStatus.deceasedDate','en','lava','crms',NULL,'EnrollmentStatus','deceasedDate','','i','date','No','',NULL,NULL,0,'','',NULL,49,'','2009-01-25 05:28:51'),(2874,'*.enrollmentStatus.deceasedNote','en','lava','crms',NULL,'EnrollmentStatus','deceasedNote','','i','text','No','',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,50,'','2009-01-25 05:28:51'),(2875,'*.enrollmentStatus.autopsyDesc','en','lava','crms',NULL,'EnrollmentStatus','autopsyDesc','','c','string','No','',25,NULL,0,'','',NULL,52,'','2009-01-25 05:28:51'),(2876,'*.enrollmentStatus.autopsyDate','en','lava','crms',NULL,'EnrollmentStatus','autopsyDate','','i','date','No','',NULL,NULL,0,'','',NULL,53,'','2009-01-25 05:28:51'),(2877,'*.enrollmentStatus.autopsyNote','en','lava','crms',NULL,'EnrollmentStatus','autopsyNote','','i','text','No','',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,54,'','2009-01-25 05:28:51'),(2878,'*.enrollmentStatus.closedDesc','en','lava','crms',NULL,'EnrollmentStatus','closedDesc','','c','string','No','',25,NULL,0,'','',NULL,56,'','2009-01-25 05:28:51'),(2879,'*.enrollmentStatus.closedDate','en','lava','crms',NULL,'EnrollmentStatus','closedDate','','i','date','No','',NULL,NULL,0,'','',NULL,57,'','2009-01-25 05:28:51'),(2880,'*.enrollmentStatus.closedNote','en','lava','crms',NULL,'EnrollmentStatus','closedNote','','i','text','No','',100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,58,'','2009-01-25 05:28:51'),(2881,'*.enrollmentStatus.enrollmentNotes','en','lava','crms',NULL,'EnrollmentStatus','enrollmentNotes','','i','text','No','',500,NULL,0,'rows=\"14\" cols=\"40\"','',NULL,59,'','2009-01-25 05:28:51'),(2882,'*.instrument.collectionStatusBlock','en','lava','crms',NULL,'instrument','collectionStatusBlock','','c','string','no','Collection Status',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2883,'*.instrument.entryStatusBlock','en','lava','crms',NULL,'instrument','entryStatusBlock','','c','string','no','Data Entry Status',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2884,'*.instrument.patient_fullName','en','lava','crms',NULL,'instrument','patient_fullName','','c','string','No','Patient',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2885,'*.instrument.sectionNote','en','lava','crms',NULL,'Instrument','sectionNote','','i','text','No','Notes',2000,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2886,'*.instrument.summary','en','lava','crms',NULL,'instrument','summary','','c','string','no','Summary',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2887,'*.instrument.verifyStatusBlock','en','lava','crms',NULL,'instrument','verifyStatusBlock','','c','string','no','Verification Status',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2888,'*.instrument.visit_id','en','lava','crms',NULL,'instrument','visit_id','','c','range','Yes','Visit Description',NULL,40,0,'','visit.patientVisits',NULL,NULL,'','2009-01-25 05:28:51'),(2889,'*.instrument.visit_visitDescrip','en','lava','crms',NULL,'instrument','visit_visitDescrip','','c','string','No','Visit Description',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2890,'filter.instrument.customDateEnd','en','lava','crms','filter','instrument','customDateEnd','','i','date','No','      and',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2891,'filter.instrument.customDateStart','en','lava','crms','filter','instrument','customDateStart','','i','date','No','Collection Between',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2892,'filter.instrument.dcDateEnd','en','lava','crms','filter','instrument','dcDateEnd','','i','date','no','     and',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2893,'filter.instrument.dcDateStart','en','lava','crms','filter','instrument','dcDateStart','','i','date','no','Collected between',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2894,'filter.instrument.deDateEnd','en','lava','crms','filter','instrument','deDateEnd','','i','date','no','     and',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2895,'filter.instrument.deDateStart','en','lava','crms','filter','instrument','deDateStart','','i','date','no','Entered between',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2896,'filter.instrument.instrType','en','lava','crms','filter','instrument','instrType','','i','string','No','Instrument Type',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2897,'*.instrument.selected','en','lava','crms',NULL,'instrument','selected',NULL,'i','toggle',NULL,NULL,NULL,NULL,0,'onclick=\"selectItemClicked(this)\"',NULL,NULL,NULL,NULL,'2009-03-05 22:15:43'),(2898,'*.instrument.id','en','lava','crms',NULL,'Instrument','id','','c','numeric','Yes','Instrument ID',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2899,'*.instrument.projName','en','lava','crms',NULL,'Instrument','projName','','c','string','Yes','Site',NULL,NULL,0,'','',NULL,3,'','2009-01-25 05:28:51'),(2900,'*.instrument.instrType','en','lava','crms',NULL,'Instrument','instrType','','c','range','Yes','Instrument Type',NULL,NULL,0,'','instrumentMetadata.instrTypes',NULL,5,'','2009-01-25 05:28:51'),(2901,'*.instrument.instrVer','en','lava','crms',NULL,'Instrument','instrVer','','i','range','Yes','Instrument Version',NULL,NULL,0,'','instrument.versions',NULL,6,'','2009-01-25 05:28:51'),(2902,'*.instrument.dcDate','en','lava','crms',NULL,'Instrument','dcDate','Data Collection','i','date','Yes','Collection Date',NULL,0,0,'','',NULL,7,'','2009-01-25 05:28:51'),(2903,'*.instrument.dcBy','en','lava','crms',NULL,'Instrument','dcBy','Data Collection','i','suggest','Yes','Collection By',NULL,NULL,0,'','project.staffList',NULL,8,'','2009-01-25 05:28:51'),(2904,'filter.instrument.dcBy','en','lava','crms','filter','Instrument','dcBy','Data Collection','i','suggest','No','Collection By',NULL,NULL,0,'','project.staffList',NULL,8,'','2009-01-25 05:28:51'),(2905,'filter.instrument.deBy','en','lava','crms','filter','Instrument','deBy','Data Collection','i','suggest','No','Entry By',NULL,NULL,0,'','project.staffList',NULL,8,'','2009-01-25 05:28:51'),(2906,'*.instrument.dcStatus','en','lava','crms',NULL,'Instrument','dcStatus','Data Collection','i','range','Yes','Collection Status',NULL,NULL,0,'','instrument.dcStatus',NULL,9,'','2009-01-25 05:28:51'),(2907,'filter.instrument.dcStatus','en','lava','crms','filter','Instrument','dcStatus','Data Collection','i','suggest','No','Collection Status',NULL,NULL,0,'','instrument.dcStatus',NULL,9,'','2009-01-25 05:28:51'),(2908,'filter.instrument.deStatus','en','lava','crms','filter','Instrument','deStatus','Data Collection','i','suggest','No','Entry Status',NULL,NULL,0,'','instrument.deStatus',NULL,9,'','2009-01-25 05:28:51'),(2909,'*.instrument.dcNotes','en','lava','crms',NULL,'Instrument','dcNotes','Data Collection','i','text','No','Collection Notes',NULL,NULL,0,'rows=\"3\" cols=\"35\"','',NULL,10,'','2009-01-25 05:28:51'),(2910,'*.instrument.researchStatus','en','lava','crms',NULL,'Instrument','researchStatus','Research Status/Quality','i','range','No','Research Status',NULL,NULL,0,'','instrument.researchStatus',NULL,11,'','2009-01-25 05:28:51'),(2911,'*.instrument.qualityIssue','en','lava','crms',NULL,'Instrument','qualityIssue','','i','range','No','Quality Issues',NULL,NULL,0,'','instrument.qualityIssue',NULL,12,'','2009-01-25 05:28:51'),(2912,'*.instrument.qualityIssue2','en','lava','crms',NULL,'Instrument','qualityIssue2','','i','range','No','',NULL,NULL,0,'','instrument.qualityIssue2',NULL,13,'','2009-01-25 05:28:51'),(2913,'*.instrument.qualityIssue3','en','lava','crms',NULL,'Instrument','qualityIssue3','Research Status/Quality','i','range','No','',NULL,NULL,0,'','instrument.qualityIssue3',NULL,14,'','2009-01-25 05:28:51'),(2914,'*.instrument.qualityNotes','en','lava','crms',NULL,'Instrument','qualityNotes','Research Status/Quality','i','text','','Quality Notes',NULL,0,0,'rows=\"3\" cols=\"35\"','',NULL,15,'','2009-01-25 05:28:51'),(2915,'*.instrument.deDate','en','lava','crms',NULL,'Instrument','deDate','Data Entry','i','date','No','',NULL,15,0,'','',NULL,16,'','2009-01-25 05:28:51'),(2916,'*.instrument.deBy','en','lava','crms',NULL,'Instrument','deBy','Data Entry','i','suggest','Yes','Entry By',NULL,NULL,0,'','project.staffList',NULL,17,'','2009-01-25 05:28:51'),(2917,'*.instrument.deStatus','en','lava','crms',NULL,'Instrument','deStatus','Data Entry','i','range','Yes','Entry Status',NULL,NULL,0,'','instrument.deStatus',NULL,18,'','2009-01-25 05:28:51'),(2918,'*.instrument.deNotes','en','lava','crms',NULL,'Instrument','deNotes','Data Entry','i','text','No','Entry Notes',NULL,NULL,0,'rows=\"3\" cols=\"35\"','',NULL,19,'','2009-01-25 05:28:51'),(2919,'*.instrument.dvDate','en','lava','crms',NULL,'Instrument','dvDate','Verification','i','date','No','Verify Date',NULL,NULL,0,'disabled','',NULL,20,'','2009-01-25 05:28:51'),(2920,'*.instrument.dvBy','en','lava','crms',NULL,'Instrument','dvBy','Verification','i','suggest','No','Verify By',NULL,NULL,0,'disabled','project.staffList',NULL,21,'','2009-01-25 05:28:51'),(2921,'*.instrument.dvStatus','en','lava','crms',NULL,'Instrument','dvStatus','Verification','i','range','No','Verify Status',NULL,NULL,0,'disabled','instrument.dvStatus',NULL,22,'','2009-01-25 05:28:51'),(2922,'*.instrument.patient_fullNameNoSuffix','en','lava','crms',NULL,'instrument','patient_fullNameNoSuffix','','c','string','No','Case',NULL,NULL,0,'','',NULL,22,'','2009-01-25 05:28:51'),(2923,'*.instrument.dvNotes','en','lava','crms',NULL,'Instrument','dvNotes','Verification','i','text','No','Verify Notes',NULL,NULL,0,'rows=\"3\" cols=\"35\"','',NULL,23,'','2009-01-25 05:28:51'),(2924,'*.instrument.ageAtDC','en','lava','crms',NULL,'Instrument','ageAtDC','','c','numeric','No','Age At Collection',NULL,NULL,0,'','',NULL,27,'','2009-01-25 05:28:51'),(2925,'filter.instrument.patient.firstName','en','lava','crms','filter','instrument.patient','firstName','','i','string','No','First Name',NULL,NULL,0,'','',NULL,8,'','2009-01-25 05:28:51'),(2926,'filter.instrument.patient.lastName','en','lava','crms','filter','instrument.patient','lastName','','i','string','no','Last name',NULL,NULL,0,'','',NULL,9,'','2009-01-25 05:28:51'),(2927,'filter.instrument.visit.projName','en','lava','crms','filter','instrument.visit','projName','','i','string','No','Project',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2928,'filter.instrument.visit.visitType','en','lava','crms','filter','instrument.visit','visitType','','i','string','No','Visit Type',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2929,'*.medications.drugId','en','lava','crms',NULL,'medications','drugId',NULL,'r','string','No','Drug ID',NULL,NULL,0,'readonly',NULL,NULL,3,'The Multum generic drug id for the medication','2009-03-05 22:15:50'),(2930,'*.medications.generic','en','lava','crms',NULL,'medications','generic',NULL,'r','string','No','Generic',50,NULL,0,'readonly',NULL,NULL,5,'Generic Name of the medication','2009-03-05 22:15:50'),(2931,'*.medications.brand','en','lava','crms',NULL,'medications','brand',NULL,'r','string','No','Brand Name',50,NULL,0,'readonly',NULL,NULL,6,'Brand Name of the medication (optional)','2009-03-05 22:15:50'),(2932,'*.medications.notListed','en','lava','crms',NULL,'medications','notListed',NULL,'r','string','No','Not Listed',50,NULL,0,NULL,NULL,NULL,7,'Description of the medication if not listed','2009-03-05 22:15:50'),(2933,'*.medications.drugLookup','en','lava','crms',NULL,'medications','drugLookup',NULL,'r','range','No','Lookup (00000=clear,99999=Not Listed)',100,NULL,0,NULL,'medications.drugLookup',NULL,40,'Lookup medication by brand or generic name','2009-03-05 22:15:50'),(2934,'*.medications.drugLookupClone','en','lava','crms',NULL,'medications','drugLookupClone',NULL,'r','range','No','Lookup (00000=clear,99999=Not Listed)',100,NULL,0,NULL,NULL,NULL,40,'Lookup medication by brand or generic name','2009-03-05 22:15:50'),(2935,'filter.patient.id','en','lava','crms','filter','patient','id','','i','numeric','No','PIDN',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(2936,'*.patient.id','en','lava','crms',NULL,'Patient','id','','c','numeric','Yes','PIDN',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2937,'*.patient.educ','en','lava','crms',NULL,'Patient','educ','Personal History/Current Arrangements','i','range','No','Education',NULL,NULL,0,'','generic.education',NULL,2,'','2009-01-25 05:28:51'),(2938,'*.patient.lastName','en','lava','crms',NULL,'Patient','lastName','ID and Core Demographics','i','string','Yes','Last Name',NULL,NULL,0,'','',NULL,2,'','2009-01-25 05:28:51'),(2939,'*.patient.middleInitial','en','lava','crms',NULL,'Patient','middleInitial','ID and Core Demographics','i','string','No','Middle Initial',1,3,0,'','',NULL,3,'','2009-01-25 05:28:51'),(2940,'*.patient.vet','en','lava','crms',NULL,'Patient','vet','Personal History/Current Arrangements','i','range','No','Millitary Veteran',NULL,NULL,0,'','generic.yesNoDK',NULL,3,'','2009-01-25 05:28:51'),(2941,'*.patient.firstName','en','lava','crms',NULL,'Patient','firstName','ID and Core Demographics','i','string','Yes','First Name',NULL,NULL,0,'','',NULL,4,'','2009-01-25 05:28:51'),(2942,'*.patient.spanOr','en','lava','crms',NULL,'Patient','spanOr','Racial Demographics','i','range','No','Spanish Origin',NULL,NULL,0,'','generic.yesNoDK',NULL,4,'','2009-01-25 05:28:51'),(2943,'*.patient.ifSpanOr','en','lava','crms',NULL,'Patient','ifSpanOr','Racial Demographics','i','range','No','If of Spanish Origin',NULL,NULL,0,'','muds.spanishOrigin',NULL,5,'','2009-01-25 05:28:51'),(2944,'*.patient.suffix','en','lava','crms',NULL,'Patient','suffix','ID and Core Demographics','i','string','No','Suffix',NULL,NULL,0,'','',NULL,5,'','2009-01-25 05:28:51'),(2945,'*.patient.degree','en','lava','crms',NULL,'Patient','degree','ID and Core Demographics','i','string','No','Degree',NULL,NULL,0,'','',NULL,6,'','2009-01-25 05:28:51'),(2946,'*.patient.race','en','lava','crms',NULL,'Patient','race','Racial Demographics','i','range','No','Race',NULL,NULL,0,'','generic.race',NULL,6,'','2009-01-25 05:28:51'),(2947,'*.patient.multRac','en','lava','crms',NULL,'Patient','multRac','Racial Demographics','i','range','No','Mutiple Race',NULL,NULL,0,'','generic.yesNo',NULL,7,'','2009-01-25 05:28:51'),(2948,'*.patient.ucid','en','lava','crms',NULL,'Patient','ucid','ID and Core Demographics','i','string','No','UCID/MRN',NULL,NULL,0,'','',NULL,7,'','2009-01-25 05:28:51'),(2949,'*.patient.multRace1','en','lava','crms',NULL,'Patient','multRace1','Racial Demographics','i','range','No','Multiple Race 1',NULL,NULL,0,'','generic.race',NULL,8,'','2009-01-25 05:28:51'),(2950,'*.patient.ssn','en','lava','crms',NULL,'Patient','ssn','ID and Core Demographics','i','string','No','SSN',NULL,NULL,0,'','',NULL,8,'','2009-01-25 05:28:51'),(2951,'*.patient.birthDate','en','lava','crms',NULL,'Patient','birthDate','ID and Core Demographics','i','date','Yes','Date of Birth',NULL,NULL,0,'','',NULL,9,'','2009-01-25 05:28:51'),(2952,'*.patient.multRace2','en','lava','crms',NULL,'Patient','multRace2','Racial Demographics','i','range','No','Multiple Race 2',NULL,NULL,0,'','generic.race',NULL,9,'','2009-01-25 05:28:51'),(2953,'*.patient.age','en','lava','crms',NULL,'Patient','age','ID and Core Demographics','c','numeric','No','Age',NULL,NULL,0,'','',NULL,10,'','2009-01-25 05:28:51'),(2954,'*.patient.multRace3','en','lava','crms',NULL,'Patient','multRace3','Racial Demographics','i','range','No','Multiple Race 3',NULL,NULL,0,'','generic.race',NULL,10,'','2009-01-25 05:28:51'),(2955,'*.patient.desRace','en','lava','crms',NULL,'Patient','desRace','Racial Demographics','i','range','No','Race that Best Describes',NULL,NULL,0,'','generic.race',NULL,11,'','2009-01-25 05:28:51'),(2956,'*.patient.gender','en','lava','crms',NULL,'Patient','gender','ID and Core Demographics','i','range','Yes','Gender',NULL,NULL,0,'','generic.gender',NULL,11,'','2009-01-25 05:28:51'),(2957,'*.patient.hand','en','lava','crms',NULL,'Patient','hand','ID and Core Demographics','i','range','No','Handedness',NULL,NULL,0,'','patient.handedness',NULL,12,'','2009-01-25 05:28:51'),(2958,'*.patient.mrgStat','en','lava','crms',NULL,'Patient','mrgStat','Personal History/Current Arrangements','i','range','No','Marital Status',NULL,NULL,0,'','patient.maritalStatus',NULL,12,'','2009-01-25 05:28:51'),(2959,'*.patient.sexualOrient','en','lava','crms',NULL,'Patient','sexualOrient','Personal History/Current Arrangements','i','range','No','Sexual Orientation/Identity',NULL,NULL,0,'','patient.sexualOrientation',NULL,12,'','2009-01-25 05:28:51'),(2960,'*.patient.deceased','en','lava','crms',NULL,'Patient','deceased','ID and Core Demographics','i','scale','Yes','Is Deceased',NULL,NULL,0,'','generic.yesNoZero',NULL,13,'','2009-01-25 05:28:51'),(2961,'*.patient.meda','en','lava','crms',NULL,'Patient','meda','Insurance Details','i','range','No','MEDA',NULL,NULL,0,'','generic.yesNoDK',NULL,13,'','2009-01-25 05:28:51'),(2962,'*.patient.deathDate','en','lava','crms',NULL,'Patient','deathDate','ID and Core Demographics','i','date','No','Date of Death',NULL,NULL,0,'','',NULL,14,'','2009-01-25 05:28:51'),(2963,'*.patient.medb','en','lava','crms',NULL,'Patient','medb','Insurance Details','i','range','No','MEDB',NULL,NULL,0,'','generic.yesNoDK',NULL,14,'','2009-01-25 05:28:51'),(2964,'*.patient.medCal','en','lava','crms',NULL,'Patient','medCal','Insurance Details','i','range','No','MEDCal',NULL,NULL,0,'','generic.yesNoDK',NULL,15,'','2009-01-25 05:28:51'),(2965,'*.patient.transNeeded','en','lava','crms',NULL,'Patient','transNeeded','Language Details','i','scale','No','Interpreter Needed',NULL,NULL,0,'','generic.yesNoZero',NULL,15,'','2009-01-25 05:28:51'),(2966,'*.patient.primaryLanguage','en','lava','crms',NULL,'Patient','primaryLanguage','Language Details','i','suggest','No','Primary Language',25,NULL,0,'','patient.patientLanguage',NULL,16,'','2009-01-25 05:28:51'),(2967,'*.patient.testingLanguage','en','lava','crms',NULL,'Patient','testingLanguage','Language Details','i','suggest','No','Testing Language',25,NULL,0,'','patient.patientLanguage',NULL,16,'','2009-01-25 05:28:51'),(2968,'*.patient.transLanguage','en','lava','crms',NULL,'Patient','transLanguage','Language Details','i','suggest','No','Interpreter Type',25,NULL,0,'','patient.patientLanguage',NULL,16,'','2009-01-25 05:28:51'),(2969,'*.patient.vaInsur','en','lava','crms',NULL,'Patient','vaInsur','Insurance Details','i','range','No','VAinsur',NULL,NULL,0,'','generic.yesNoDK',NULL,16,'','2009-01-25 05:28:51'),(2970,'*.patient.enterBy','en','lava','crms',NULL,'Patient','enterBy','Record Management','i','suggest','No','Entered By',NULL,NULL,0,'','patient.staffList',NULL,17,'','2009-01-25 05:28:51'),(2971,'*.patient.insurLongTerm','en','lava','crms',NULL,'Patient','insurLongTerm','Insurance Details','i','range','No','InsurLongTerm',NULL,NULL,0,'','generic.yesNoDK',NULL,17,'','2009-01-25 05:28:51'),(2972,'*.patient.dupNameFlag','en','lava','crms',NULL,'Patient','dupNameFlag','Record Management','i','scale','Yes','Dup Name Warning',NULL,NULL,0,'','generic.yesNoZero',NULL,18,'','2009-01-25 05:28:51'),(2973,'*.patient.other','en','lava','crms',NULL,'Patient','other','Insurance Details','i','range','No','Other',NULL,NULL,0,'','generic.yesNoDK',NULL,18,'','2009-01-25 05:28:51'),(2974,'*.patient.fullNameRev','en','lava','crms',NULL,'Patient','fullNameRev','','c','string','No','Patient',NULL,NULL,0,'','',NULL,19,'','2009-01-25 05:28:51'),(2975,'*.patient.none','en','lava','crms',NULL,'Patient','none','Insurance Details','i','range','No','None',NULL,NULL,0,'','generic.yesNoDK',NULL,19,'','2009-01-25 05:28:51'),(2976,'*.patient.deidentified','en','lava','crms',NULL,'patient','deidentified','','i','toggle','No','Use Deidentified ID',NULL,NULL,0,'','',NULL,20,'','2009-01-25 05:28:51'),(2977,'*.patient.fullName','en','lava','crms',NULL,'Patient','fullName','','c','string','No','Patient',NULL,NULL,0,'','',NULL,20,'','2009-01-25 05:28:51'),(2978,'*.patient.reimburse','en','lava','crms',NULL,'Patient','reimburse','Insurance Details','i','range','No','Reimburse',NULL,NULL,0,'','muds.reimbursement',NULL,20,'','2009-01-25 05:28:51'),(2979,'*.patient.fullNameNoSuffix','en','lava','crms',NULL,'Patient','fullNameNoSuffix','','c','string','No','Patient',NULL,NULL,0,'','',NULL,21,'','2009-01-25 05:28:51'),(2980,'*.patient.fullNameRevNoSuffix','en','lava','crms',NULL,'Patient','fullNameRevNoSuffix','','c','string','No','Patient',NULL,NULL,0,'','',NULL,21,'','2009-01-25 05:28:51'),(2981,'*.patient.payment','en','lava','crms',NULL,'Patient','payment','Insurance Details','i','range','No','Payment',NULL,NULL,0,'','muds.payment',NULL,21,'','2009-01-25 05:28:51'),(2982,'*.patient.subjectId','en','lava','crms',NULL,'patient','subjectId','','i','string','Yes','Subject ID',NULL,NULL,0,'','',NULL,21,'','2009-01-25 05:28:51'),(2983,'*.patient.primCare','en','lava','crms',NULL,'Patient','primCare','Personal History/Current Arrangements','i','range','No','Primary Caregiver',NULL,NULL,0,'','muds.primaryCaregiver',NULL,22,'','2009-01-25 05:28:51'),(2984,'*.patient.reside','en','lava','crms',NULL,'Patient','reside','Personal History/Current Arrangements','i','range','No','Principle Residence',NULL,NULL,0,'','muds.principalResidence',NULL,23,'','2009-01-25 05:28:51'),(2985,'*.patient.legStat','en','lava','crms',NULL,'Patient','legStat','Personal History/Current Arrangements','i','range','No','Legal Status',NULL,NULL,0,'','patient.legalStatus',NULL,24,'','2009-01-25 05:28:51'),(2986,'*.patient.advDir','en','lava','crms',NULL,'Patient','advDir','Personal History/Current Arrangements','i','range','No','Advanced Directive',NULL,NULL,0,'','generic.yesNo',NULL,25,'','2009-01-25 05:28:51'),(2987,'*.patient.raceText','en','lava','crms',NULL,'Patient','raceText','','c','string','No','RaceText',NULL,NULL,0,'','',NULL,26,'','2009-01-25 05:28:51'),(2988,'*.patient.multRace1Text','en','lava','crms',NULL,'Patient','multRace1Text','','c','string','No','MultRace1Text',NULL,NULL,0,'','',NULL,27,'','2009-01-25 05:28:51'),(2989,'*.patient.multRace2Text','en','lava','crms',NULL,'Patient','multRace2Text','','c','string','No','MultRace2Text',NULL,NULL,0,'','',NULL,28,'','2009-01-25 05:28:51'),(2990,'*.patient.multRace3Text','en','lava','crms',NULL,'Patient','multRace3Text','','c','string','No','MultRace3Text',NULL,NULL,0,'','',NULL,29,'','2009-01-25 05:28:51'),(2991,'*.patient.desRaceText','en','lava','crms',NULL,'Patient','desRaceText','','c','string','No','DesRaceText',NULL,NULL,0,'','',NULL,30,'','2009-01-25 05:28:51'),(2992,'*.patient.ifSpanOrText','en','lava','crms',NULL,'Patient','ifSpanOrText','','c','string','No','ifSpanOrText',NULL,NULL,0,'','',NULL,31,'','2009-01-25 05:28:51'),(2993,'*.patient.nihEthnicCategory','en','lava','crms',NULL,'Patient','nihEthnicCategory','','c','string','No','NIHEthnicCategory',NULL,NULL,0,'','',NULL,32,'','2009-01-25 05:28:51'),(2994,'*.patient.nihRacialCategory','en','lava','crms',NULL,'Patient','nihRacialCategory','','c','string','No','NIHRacialCategory',NULL,NULL,0,'','',NULL,33,'','2009-01-25 05:28:51'),(2995,'*.patient.nihHispanicRacialCategory','en','lava','crms',NULL,'Patient','nihHispanicRacialCategory','','c','string','No','NIHHispanicRacialCategory',NULL,NULL,0,'','',NULL,34,'','2009-01-25 05:28:51'),(2996,'*.patient.altArccId','en','lava','crms',NULL,'Patient','altArccId','Alternate ARCC ID','i','range','No','Alternate ARCC ID',NULL,NULL,0,'','patient.alternateARCCID',NULL,35,'','2009-01-25 05:28:51'),(2997,'*.patient.altPatId','en','lava','crms',NULL,'Patient','altPatId','Alternate ARCC ID','i','string','No','Alternate PAT ID',NULL,NULL,0,'','',NULL,36,'','2009-01-25 05:28:51'),(2998,'*.patientContext.patientSearch','en','lava','crms',NULL,'PatientContext','patientSearch','','i','suggest','No','',NULL,NULL,0,'','context.patientResults',NULL,NULL,'','2009-01-25 05:28:51'),(2999,'*.patientContext.searchBy','en','lava','crms',NULL,'PatientContext','searchBy','','i','range','No','',NULL,NULL,0,'','context.searchBy',NULL,NULL,'','2009-01-25 05:28:51'),(3000,'*.patientDoctor.doctor_id','en','lava','crms',NULL,'patientDoctor','doctor_id','','i','range','','Doctor',NULL,NULL,0,'','doctor.allDoctors',NULL,NULL,'','2009-01-25 05:28:51'),(3001,'*.patientDoctor.id','en','lava','crms',NULL,'PatientDoctor','id','','c','string','Yes','id',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(3002,'*.patientDoctor.docStat','en','lava','crms',NULL,'PatientDoctor','docStat','','i','suggest','No','Doctor Status',NULL,NULL,0,'','doctor.doctorStatus',NULL,4,'','2009-01-25 05:28:51'),(3003,'*.patientDoctor.docNote','en','lava','crms',NULL,'PatientDoctor','docNote','','i','text','No','Doctor Note',NULL,NULL,0,'rows=\"3\" cols=\"50\"','',NULL,5,'','2009-01-25 05:28:51'),(3004,'*.project.projUnitDesc','en','lava','crms',NULL,'project','projUnitDesc','','c','string','No','Project',NULL,NULL,0,'','',NULL,NULL,'Project Name','2009-01-25 05:28:51'),(3005,'*.projectContext.projectName','en','lava','crms',NULL,'projectContext','projectName','','i','range','No','',NULL,NULL,0,'','context.projectList',NULL,NULL,'','2009-01-25 05:28:51'),(3006,'*.reportSetup.format','en','lava','crms',NULL,'reportSetup','format','','i','range','No','Format',NULL,10,0,'','reportSetup.format',NULL,NULL,'','2009-01-30 09:41:46'),(3007,'filter.reportSetup.customDateStart','en','lava','crms','filter','reportSetup','customDateStart','','i','date','No','Date is between',NULL,NULL,0,'','',NULL,NULL,'','2009-01-30 09:41:46'),(3008,'filter.reportSetup.customDateEnd','en','lava','crms','filter','reportSetup','customDateEnd','','i','date','No','      and',NULL,NULL,0,'','',NULL,NULL,'','2009-01-30 09:41:46'),(3009,'filter.reportSetup.projectList','en','lava','crms','filter','reportSetup','projectList','','i','multiple','No','Project(s)',NULL,20,0,'','context.projectList',NULL,NULL,'','2009-01-30 09:41:46'),(3010,'udsExtract.reportSetup.customDateStart','en','lava','crms','udsExtract','reportSetup','customDateStart',NULL,'i','datetime','No','Start Date/Time',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'2009-01-30 09:41:46'),(3011,'udsExtract.reportSetup.format','en','lava','crms','udsExtract','reportSetup','format',NULL,'i','range','No','Format',NULL,10,0,NULL,'reportSetup.formatCsv',NULL,NULL,NULL,'2009-01-30 09:41:46'),(3012,'filter.reportSetup.patientId','en','lava','crms','filter','reportSetup','patientId',NULL,'i','numeric','No','Patient ID',NULL,5,0,NULL,NULL,NULL,NULL,NULL,'2009-01-30 09:41:46'),(3013,'*.reportSetup.format','en','lava','crms',NULL,'reportSetup','format','','i','range','No','Format',NULL,10,0,'','reportSetup.format',NULL,NULL,'','2009-01-30 09:41:46'),(3014,'filter.reportSetup.customDateStart','en','lava','crms','filter','reportSetup','customDateStart','','i','date','No','Date is between',NULL,NULL,0,'','',NULL,NULL,'','2009-01-30 09:41:46'),(3015,'filter.reportSetup.customDateEnd','en','lava','crms','filter','reportSetup','customDateEnd','','i','date','No','      and',NULL,NULL,0,'','',NULL,NULL,'','2009-01-30 09:41:46'),(3016,'filter.reportSetup.projectList','en','lava','crms','filter','reportSetup','projectList','','i','multiple','No','Project(s)',NULL,20,0,'','context.projectList',NULL,NULL,'','2009-01-30 09:41:46'),(3017,'udsExtract.reportSetup.customDateStart','en','lava','crms','udsExtract','reportSetup','customDateStart',NULL,'i','date','No','Start Date',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'2009-01-30 09:41:46'),(3018,'udsExtract.reportSetup.format','en','lava','crms','udsExtract','reportSetup','format',NULL,'i','range','No','Format',NULL,10,0,NULL,'reportSetup.formatCsv',NULL,NULL,NULL,'2009-01-30 09:41:46'),(3019,'filter.reportSetup.patientId','en','lava','crms','filter','reportSetup','patientId',NULL,'i','numeric','No','Patient ID',NULL,5,0,NULL,NULL,NULL,NULL,NULL,'2009-01-30 09:41:46'),(3020,'*.task.id','en','lava','crms',NULL,'task','id','details','c','numeric','No','TaskID',NULL,NULL,0,NULL,NULL,NULL,1,'The unique ID of the task record','2009-01-25 05:28:51'),(3021,'*.task.patient_fullNameRev','en','lava','crms',NULL,'task','patient_fullNameRev','details','c','string','No','Patient',NULL,NULL,0,NULL,NULL,NULL,2,'the ID of the patient','2009-01-25 05:28:51'),(3022,'*.task.projName','en','lava','crms',NULL,'task','projName','details','i','suggest','No','Project',75,NULL,0,NULL,'enrollmentStatus.patientProjects',NULL,3,'The related project of the task (optional)','2009-01-25 05:28:51'),(3023,'filter.task.projName','en','lava','crms','filter','task','projName',NULL,'i','string','No','Project',75,NULL,0,NULL,NULL,NULL,3,'The related project of the task (optional)','2009-01-25 05:28:51'),(3024,'*.task.openedDate','en','lava','crms',NULL,'task','openedDate','details','i','date','No','Opened Date',NULL,NULL,0,NULL,NULL,NULL,4,'Date the task was opened','2009-01-25 05:28:51'),(3025,'filter.task.openedDateEnd','en','lava','crms','filter','task','openedDateEnd',NULL,'i','date','No','and',NULL,NULL,0,NULL,NULL,NULL,4,'Date the task was opened','2009-01-25 05:28:51'),(3026,'filter.task.openedDateStart','en','lava','crms','filter','task','openedDateStart',NULL,'i','date','No','Opened Date Between',NULL,NULL,0,NULL,NULL,NULL,4,'Date the task was opened','2009-01-25 05:28:51'),(3027,'*.task.openedBy','en','lava','crms',NULL,'task','openedBy','details','i','suggest','No','Opened By',25,NULL,0,NULL,'project.staffList',NULL,5,'Who opened the task','2009-01-25 05:28:51'),(3028,'filter.task.openedBy','en','lava','crms','filter','task','openedBy',NULL,'i','string','No','Opened By',25,NULL,0,NULL,NULL,NULL,5,'Who opened the task','2009-01-25 05:28:51'),(3029,'*.task.taskType','en','lava','crms',NULL,'task','taskType','details','i','suggest','No','Task Type',25,NULL,0,NULL,'task.taskType',NULL,6,'The type of task','2009-01-25 05:28:51'),(3030,'filter.task.taskType','en','lava','crms','filter','task','taskType',NULL,'i','suggest','No','Task Type',25,NULL,0,NULL,'task.taskType',NULL,6,'The type of task','2009-01-25 05:28:51'),(3031,'*.task.taskDesc','en','lava','crms',NULL,'task','taskDesc','details','i','text','No','Task Description',255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,7,'Description of the task','2009-01-25 05:28:51'),(3032,'filter.task.taskDesc','en','lava','crms','filter','task','taskDesc',NULL,'i','string','No','Task Description',255,NULL,0,NULL,NULL,NULL,7,'Description of the task','2009-01-25 05:28:51'),(3033,'*.task.dueDate','en','lava','crms',NULL,'task','dueDate','details','i','date','No','Due Date',NULL,NULL,0,NULL,NULL,NULL,8,'When the task is due','2009-01-25 05:28:51'),(3034,'filter.task.dueDateEnd','en','lava','crms','filter','task','dueDateEnd',NULL,'i','date','No','and',NULL,NULL,0,NULL,NULL,NULL,8,'When the task is due','2009-01-25 05:28:51'),(3035,'filter.task.dueDateStart','en','lava','crms','filter','task','dueDateStart',NULL,'i','date','No','Due Date Between',NULL,NULL,0,NULL,NULL,NULL,8,'When the task is due','2009-01-25 05:28:51'),(3036,'*.task.taskStatus','en','lava','crms',NULL,'task','taskStatus','details','i','suggest','No','Task Status',50,NULL,0,NULL,'task.taskStatus',NULL,9,'The task status','2009-01-25 05:28:51'),(3037,'filter.task.taskStatus','en','lava','crms','filter','task','taskStatus',NULL,'i','suggest','No','Task Status',50,NULL,0,NULL,'task.taskStatus',NULL,9,'The task status','2009-01-25 05:28:51'),(3038,'*.task.assignedTo','en','lava','crms',NULL,'task','assignedTo','details','i','suggest','No','Assigned To',25,NULL,0,NULL,'project.staffList',NULL,10,'Who the task is assigned to','2009-01-25 05:28:51'),(3039,'filter.task.assignedTo','en','lava','crms','filter','task','assignedTo',NULL,'i','string','No','Assigned To',25,NULL,0,NULL,NULL,NULL,10,'Who the task is assigned to','2009-01-25 05:28:51'),(3040,'*.task.workingNotes','en','lava','crms',NULL,'task','workingNotes','details','i','text','No','Working Notes',255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,11,'Notes about the task','2009-01-25 05:28:51'),(3041,'filter.task.workingNotes','en','lava','crms','filter','task','workingNotes',NULL,'i','string','No','Working Notes',255,NULL,0,NULL,NULL,NULL,11,'Notes about the task','2009-01-25 05:28:51'),(3042,'*.task.closedDate','en','lava','crms',NULL,'task','closedDate','details','i','date','No','Closed Date',NULL,NULL,0,NULL,NULL,NULL,12,'When the task was completed or closed','2009-01-25 05:28:51'),(3043,'filter.task.closedDateEnd','en','lava','crms','filter','task','closedDateEnd',NULL,'i','date','No','and',NULL,NULL,0,NULL,NULL,NULL,12,'When the task was completed or closed','2009-01-25 05:28:51'),(3044,'filter.task.closedDateStart','en','lava','crms','filter','task','closedDateStart',NULL,'i','date','No','Closed Date Between',NULL,NULL,0,NULL,NULL,NULL,12,'When the task was completed or closed','2009-01-25 05:28:51'),(3045,'filter.task.patient.firstName','en','lava','crms','filter','task.patient','firstName',NULL,'i','string','No','Patient',NULL,NULL,0,NULL,NULL,NULL,2,'the ID of the patient','2009-01-25 05:28:51'),(3046,'filter.task.patient.lastName','en','lava','crms','filter','task.patient','lastName',NULL,'i','string','No','Patient',NULL,NULL,0,NULL,NULL,NULL,2,'the ID of the patient','2009-01-25 05:28:51'),(3047,'filter.visit.customDateEnd','en','lava','crms','filter','Visit','customDateEnd','','i','date','No','      and',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(3048,'filter.visit.customDateStart','en','lava','crms','filter','Visit','customDateStart','','i','date','No','Date is between',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(3049,'filter.visit.patient.firstName','en','lava','crms','filter','Visit','patient.firstName','','i','string','No','First Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(3050,'filter.visit.patient.lastName','en','lava','crms','filter','Visit','patient.lastName','','i','string','No','Last Name',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(3051,'filter.visit.projName','en','lava','crms','filter','Visit','projName','','i','string','No','Project',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(3052,'filter.visit.visitLocation','en','lava','crms','filter','Visit','visitLocation','','i','suggest','No','Location',NULL,NULL,0,'','visit.locations',NULL,NULL,'','2009-01-25 05:28:51'),(3053,'filter.visit.visitType','en','lava','crms','filter','Visit','visitType','','i','string','No','Type',NULL,NULL,0,'','',NULL,NULL,'','2009-01-25 05:28:51'),(3054,'*.visit.id','en','lava','crms',NULL,'Visit','id','','c','range','Yes','Visit ID',NULL,40,0,'','visit.patientVisits',NULL,1,'','2009-01-25 05:28:51'),(3055,'*.visit.projName','en','lava','crms',NULL,'Visit','projName','','i','range','Yes','Project',NULL,NULL,0,'','enrollmentStatus.patientProjects',NULL,3,'','2009-01-25 05:28:51'),(3056,'*.visit.visitLocation','en','lava','crms',NULL,'Visit','visitLocation','','i','suggest','Yes','Location',NULL,NULL,0,'','visit.visitLocations',NULL,4,'','2009-01-25 05:28:51'),(3057,'*.visit.visitType','en','lava','crms',NULL,'Visit','visitType','','i','range','Yes','Visit Type',NULL,NULL,0,'','visit.visitTypes',NULL,5,'','2009-01-25 05:28:51'),(3058,'*.visit.visitWith','en','lava','crms',NULL,'Visit','visitWith','','i','suggest','Yes','Appt With',NULL,NULL,0,'','project.staffList',NULL,6,'','2009-01-25 05:28:51'),(3059,'filter.visit.visitWith','en','lava','crms','filter','Visit','visitWith','','i','suggest','No','Appt With',NULL,NULL,0,'','project.staffList',NULL,6,'','2009-01-25 05:28:51'),(3060,'*.visit.visitDate','en','lava','crms',NULL,'Visit','visitDate','','i','date','Yes','Visit Date',NULL,10,0,'','',NULL,7,'','2009-01-25 05:28:51'),(3061,'*.visit.visitStatus','en','lava','crms',NULL,'Visit','visitStatus','','i','range','Yes','Status',NULL,NULL,0,'','visit.status',NULL,8,'','2009-01-25 05:28:51'),(3062,'filter.visit.visitStatus','en','lava','crms','filter','Visit','visitStatus','','i','range','Yes','Status',NULL,NULL,0,'','visit.status',NULL,8,'','2009-01-25 05:28:51'),(3063,'*.visit.visitTime','en','lava','crms','','visit','visitTime','','i','time','No','Time',NULL,NULL,0,NULL,NULL,NULL,8,'Time of the visit','2009-04-29 21:00:00'),(3064,'*.visit.visitNote','en','lava','crms',NULL,'Visit','visitNote','','i','text','No','Visit Notes',NULL,NULL,0,'rows=\"5\" cols=\"35\"','',NULL,9,'','2009-01-25 05:28:51'),(3065,'*.visit.visitDescrip','en','lava','crms',NULL,'Visit','visitDescrip','','c','string','No','Description',NULL,NULL,0,'','',NULL,16,'','2009-01-25 05:28:51'),(3066,'*.visit.ageAtVisit','en','lava','crms',NULL,'Visit','ageAtVisit','','c','numeric','No','Age At Visit',NULL,NULL,0,'','',NULL,17,'','2009-01-25 05:28:51'),(3310,'*.crmsAuthUserRole.project','en','lava','crms',NULL,'crmsAuthUserRole','project','details','i','suggest','Yes','Project',NULL,NULL,0,NULL,'projectUnit.projects',NULL,10,NULL,'2009-01-01 08:00:00'),(3920,'*.consent.hipaa','en','lava','crms',NULL,'consent','hipaa',NULL,'i','range','No','HIPAA',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,NULL,'HIPAA','2011-03-17 19:46:26'),(3921,'*.consent.consentTypeBlock','en','lava','crms',NULL,'consent','consentTypeBlock',NULL,'c','string','No','Consent Type',NULL,NULL,0,NULL,NULL,NULL,NULL,'Consent Type','2011-03-17 19:47:44'),(3922,'*.addPatient.patient_hand','en','lava','crms',NULL,'addPatient','patient_hand',NULL,'i','range','No','Handedness',NULL,NULL,0,NULL,'patient.handedness',NULL,NULL,'Handedness','2011-03-17 19:52:07'),(3923,'*.addPatient.patient_gender','en','lava','crms',NULL,'addPatient','patient_gender',NULL,'i','range','Yes','Gender',NULL,NULL,0,NULL,'generic.gender',NULL,NULL,'Gender','2011-03-17 19:52:07');
/*!40000 ALTER TABLE `viewproperty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visit`
--

DROP TABLE IF EXISTS `visit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit` (
  `VID` int(10) NOT NULL AUTO_INCREMENT,
  `PIDN` int(10) NOT NULL,
  `ProjName` varchar(75) NOT NULL,
  `VLocation` varchar(25) NOT NULL,
  `VType` varchar(25) NOT NULL,
  `VWith` varchar(25) DEFAULT NULL,
  `VDate` date NOT NULL,
  `VTime` time DEFAULT NULL,
  `VStatus` varchar(25) NOT NULL,
  `VNotes` varchar(255) DEFAULT NULL,
  `FUMonth` char(3) DEFAULT NULL,
  `FUYear` char(4) DEFAULT NULL,
  `FUNote` varchar(100) DEFAULT NULL,
  `WList` varchar(25) DEFAULT NULL,
  `WListNote` varchar(100) DEFAULT NULL,
  `WListDate` datetime DEFAULT NULL,
  `VShortDesc` varchar(255) DEFAULT NULL,
  `AgeAtVisit` smallint(5) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`VID`),
  KEY `visit__PIDN` (`PIDN`),
  KEY `visit__ProjName` (`ProjName`),
  CONSTRAINT `visit__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `visit__ProjName` FOREIGN KEY (`ProjName`) REFERENCES `projectunit` (`ProjUnitDesc`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visit`
--

LOCK TABLES `visit` WRITE;
/*!40000 ALTER TABLE `visit` DISABLE KEYS */;
/*!40000 ALTER TABLE `visit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `vwrptprojectpatientstatus`
--

DROP TABLE IF EXISTS `vwrptprojectpatientstatus`;
/*!50001 DROP VIEW IF EXISTS `vwrptprojectpatientstatus`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `vwrptprojectpatientstatus` (
  `PIDN` int(11),
  `FullNameRev` varchar(100),
  `AGE` int(11),
  `Gender` tinyint(4),
  `ProjName` varchar(75),
  `StatusDate` datetime,
  `Status` varchar(25),
  `StatusNote` varchar(100),
  `StatusOrder` bigint(20),
  `ProjUnitDesc` varchar(75),
  `Project` varchar(25),
  `Unit` varchar(25),
  `UnitOrder` bigint(20)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `vwrptprojectvisitlist`
--

DROP TABLE IF EXISTS `vwrptprojectvisitlist`;
/*!50001 DROP VIEW IF EXISTS `vwrptprojectvisitlist`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `vwrptprojectvisitlist` (
  `PIDN` int(10),
  `FullNameRev` varchar(100),
  `TransLanguage` varchar(25),
  `Gender` tinyint(3),
  `AGE` int(10),
  `VLocation` varchar(25),
  `VType` varchar(25),
  `VWith` varchar(25),
  `VDate` date,
  `VStatus` varchar(25),
  `ProjName` varchar(75),
  `VNotes` varchar(255),
  `VDateNoTime` date
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'demo'
--
/*!50003 DROP PROCEDURE IF EXISTS `util_AddTableToHibernateProperty` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `util_AddTableToHibernateProperty`(TableNameIn varchar (50),EntityIn varchar (50),ScopeIn varchar(25))
BEGIN

INSERT INTO `hibernateproperty` (`scope`,`entity`,`property`,`dbTable`,`dbColumn`,`dbType`,`dbLength`,
`dbPrecision`,`dbScale`,`dbOrder`,`hibernateProperty`,`hibernateType`,`hibernateClass`,`hibernateNotNull`)
  SELECT ScopeIn, EntityIn, CONCAT(LOWER(LEFT(`COLUMN_NAME`,1)),RIGHT(`COLUMN_NAME`,LENGTH(`COLUMN_NAME`)-1)),
   TableNameIn, `COLUMN_NAME`, `DATA_TYPE`,
   CASE WHEN `CHARACTER_MAXIMUM_LENGTH` < 10000 THEN `CHARACTER_MAXIMUM_LENGTH` ELSE NULL END, `NUMERIC_PRECISION`, `NUMERIC_SCALE`,
    `ORDINAL_POSITION`, CONCAT(LOWER(LEFT(`COLUMN_NAME`,1)),RIGHT(`COLUMN_NAME`,LENGTH(`COLUMN_NAME`)-1)),
		CASE `DATA_TYPE` WHEN 'datetime' THEN 'timestamp'
			WHEN 'float' THEN 'float'
			WHEN 'image' THEN 'binary'
			WHEN 'int' THEN 'long'
			WHEN 'timestamp' THEN 'timestamp'
			WHEN 'text' THEN 'text'
			WHEN 'decimal' THEN 'float'
			WHEN 'numeric' THEN 'float'
			WHEN 'char' THEN 'character'
			WHEN 'nvarchar' THEN 'string'
			WHEN 'binary' THEN 'binary'
			WHEN 'tinyint' THEN 'byte'
			WHEN 'date' THEN 'date'
			WHEN 'time' THEN 'time'
			WHEN 'smalldatetime' THEN 'timestamp'
			WHEN 'varchar' THEN 'string'
			WHEN 'bit' THEN 'boolean'
			WHEN 'smallint' THEN 'short'
			ELSE 'UNMAPPED TYPE' END,
		NULL, CASE WHEN `IS_NULLABLE`='No' THEN 'Yes' ELSE 'No' END
	FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_NAME`=TableNameIn order by `ORDINAL_POSITION`;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `util_AddTableToMetaData` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `util_AddTableToMetaData`(TableNameIn varchar (50),EntityIn varchar (50),ScopeIn varchar(25))
BEGIN

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`entity`,`property`,`required`,`maxLength`,`propOrder`)
   SELECT CONCAT('*.',EntityIn, '.',LOWER(LEFT(`COLUMN_NAME`,1)),RIGHT(`COLUMN_NAME`,LENGTH(`COLUMN_NAME`)-1)),
   'en','lava',ScopeIn, EntityIn, CONCAT(LOWER(LEFT(`COLUMN_NAME`,1)),RIGHT(`COLUMN_NAME`,LENGTH(`COLUMN_NAME`)-1)),
        CASE WHEN `IS_NULLABLE`='No' THEN 'Yes' ELSE 'No' END,
        CASE WHEN `CHARACTER_MAXIMUM_LENGTH` < 10000 THEN `CHARACTER_MAXIMUM_LENGTH` ELSE NULL END, `ORDINAL_POSITION`
	FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_NAME`=TableNameIn order by `ORDINAL_POSITION`;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `util_CreateMetadataInsertStatements` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `util_CreateMetadataInsertStatements`(InstanceMask varchar(50), ScopeMask varchar(50), EntityMask varchar (50))
BEGIN

IF InstanceMask IS NULL THEN
  SET InstanceMask = 'lava';
END IF;

IF ScopeMask IS NULL THEN
  SET ScopeMask = '%';
END IF;


IF EntityMask IS NULL THEN
  SET EntityMask = '%';
END IF;





SELECT CONCAT('INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,',
            '`context`,`style`,`required`,`label`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,',
            '`propOrder`,`quickHelp`,`modified`) VALUES(',
        	  CASE WHEN `messageCode` IS NULL THEN 'NULL,' ELSE CONCAT('''',`messageCode`,''',') END,
	          CASE WHEN `locale` IS NULL THEN 'NULL,' ELSE CONCAT('''',`locale`,''',') END,
	          CASE WHEN `instance` IS NULL THEN 'NULL,' ELSE CONCAT('''',`instance`,''',') END,
        	  CASE WHEN `scope` IS NULL THEN 'NULL,' ELSE CONCAT('''',`scope`,''',') END,
        	  CASE WHEN `prefix` IS NULL THEN 'NULL,' ELSE CONCAT('''',`prefix`,''',') END,
        	  CASE WHEN `entity` IS NULL THEN 'NULL,' ELSE CONCAT('''',`entity`,''',') END,
        	  CASE WHEN `property` IS NULL THEN 'NULL,' ELSE CONCAT('''',`property`,''',') END,
            CASE WHEN `section` IS NULL THEN 'NULL,' ELSE CONCAT('''',`section`,''',') END,
            CASE WHEN `context` IS NULL THEN 'NULL,' ELSE CONCAT('''',`context`,''',') END,
            CASE WHEN `style` IS NULL THEN 'NULL,' ELSE CONCAT('''',`style`,''',') END,
            CASE WHEN `required` IS NULL THEN 'NULL,' ELSE CONCAT('''',`required`,''',') END,
            CASE WHEN `label` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(`label`,'''','\\'''),''',') END,
            CASE WHEN `maxLength` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`maxLength` as char),',') END,
            CASE WHEN `size` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`size` as char),',') END,
            CASE WHEN `indentLevel` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`indentLevel` as char),',') END,
            CASE WHEN `attributes` IS NULL THEN 'NULL,' ELSE CONCAT('''',`attributes`,''',') END,
            CASE WHEN `list` IS NULL THEN 'NULL,' ELSE CONCAT('''',`list`,''',') END,
            CASE WHEN `listAttributes` IS NULL THEN 'NULL,' ELSE CONCAT('''',`listAttributes`,''',') END,
            CASE WHEN `propOrder` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`propOrder` as char),',') END,
            CASE WHEN `quickHelp` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(`quickHelp`,'''','\\'''),''',') END,
           CASE WHEN `modified` IS NULL THEN 'NULL' ELSE CONCAT('''',CAST(`modified` as char),'''') END,
            ');')
            FROM `viewproperty` WHERE `entity` Like EntityMask and
                                    `instance` like InstanceMask and
                                    `scope` like ScopeMask
            ORDER BY `entity`, `propOrder`;




SELECT CONCAT('INSERT INTO hibernateproperty (`instance`,`scope`,`entity`,`property`,`dbTable`,`dbColumn`,`dbType`,',
              '`dbLength`,`dbPrecision`,`dbScale`,`dbOrder`,`hibernateProperty`,`hibernateType`,`hibernateClass`,',
              '`hibernateNotNull`,`modified`) VALUES(',
        	  CASE WHEN `instance` IS NULL THEN 'NULL,' ELSE CONCAT('''',`instance`,''',') END,
        	  CASE WHEN `scope` IS NULL THEN 'NULL,' ELSE CONCAT('''',`scope`,''',') END,
        	  CASE WHEN `entity` IS NULL THEN 'NULL,' ELSE CONCAT('''',`entity`,''',') END,
        	  CASE WHEN `property` IS NULL THEN 'NULL,' ELSE CONCAT('''',`property`,''',') END,
            CASE WHEN `dbTable` IS NULL THEN 'NULL,' ELSE CONCAT('''',`dbTable`,''',') END,
            CASE WHEN `dbColumn` IS NULL THEN 'NULL,' ELSE CONCAT('''',`dbColumn`,''',') END,
            CASE WHEN `dbType` IS NULL THEN 'NULL,' ELSE CONCAT('''',`dbType`,''',') END,
            CASE WHEN `dbLength` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`dbLength` as char),',') END,
            CASE WHEN `dbPrecision` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`dbPrecision` as char),',') END,
            CASE WHEN `dbScale` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`dbScale` as char),',') END,
            CASE WHEN `dbOrder` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`dbOrder` as char),',') END,
            CASE WHEN `hibernateProperty` IS NULL THEN 'NULL,' ELSE CONCAT('''',`hibernateProperty`,''',') END,
            CASE WHEN `hibernateType` IS NULL THEN 'NULL,' ELSE CONCAT('''',`hibernateType`,''',') END,
            CASE WHEN `hibernateClass` IS NULL THEN 'NULL,' ELSE CONCAT('''',`hibernateClass`,''',') END,
            CASE WHEN `hibernateNotNull` IS NULL THEN 'NULL,' ELSE CONCAT('''',`hibernateNotNull`,''',') END,
            CASE WHEN `modified` IS NULL THEN 'NULL' ELSE CONCAT('''',CAST(`modified` as char),'''') END,
            ');')
            FROM `hibernateproperty` WHERE `entity` Like EntityMask and
                                    `instance` like InstanceMask and
                                    `scope` like ScopeMask
            ORDER BY `entity`, `dbOrder`;




SELECT CONCAT('INSERT INTO `list` (`ListName`,`scope`,`NumericKey`,`modified`) VALUES(',
        	  CASE WHEN `ListName` IS NULL THEN 'NULL,' ELSE CONCAT('''',`ListName`,''',') END,
        	  CASE WHEN `scope` IS NULL THEN 'NULL,' ELSE CONCAT('''',`scope`,''',') END,
        	  CASE WHEN `NumericKey` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`NumericKey` as char),',') END,
              CASE WHEN `modified` IS NULL THEN 'NULL' ELSE CONCAT('''',CAST(`modified` as char),'''') END,
            ');')
            FROM `list` WHERE `scope` like ScopeMask
            ORDER BY `ListName`; 





SELECT CONCAT('INSERT INTO `listvalues` (`ListID`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`)',
			' SELECT `ListID`,',CASE WHEN lv.`ValueKey` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(lv.`ValueKey`,'''','\\'''),''',') END,
        	  CASE WHEN lv.`ValueDesc` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(lv.`ValueDesc`,'''','\\'''),''',') END,
				CASE WHEN lv.`OrderID` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(lv.`OrderID` as char),',') END,
              CASE WHEN lv.`modified` IS NULL THEN 'NULL' ELSE CONCAT('''',CAST(lv.`modified` as char),'''') END,
            ' FROM `list` where `ListName`=''',l.`ListName`,''';')
            FROM `listvalues` lv INNER JOIN `list` l on l.`ListId`=lv.`ListID` WHERE l.`scope` like ScopeMask
            ORDER BY l.`ListName`, lv.ORDERID, lv.ValueKey;


END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `util_FixMetadataPropertyNames` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `util_FixMetadataPropertyNames`(EntityIn varchar(50))
BEGIN






UPDATE hibernateproperty SET
property =
CONCAT(SUBSTRING_INDEX(property,'_',1),
UPPER(SUBSTRING(property,LOCATE('_',property)+1,1)),
SUBSTRING(property,LOCATE('_',property)+2)),
hibernateProperty =
CONCAT(SUBSTRING_INDEX(hibernateProperty,'_',1),
UPPER(SUBSTRING(hibernateProperty,LOCATE('_',hibernateProperty)+1,1)),
SUBSTRING(hibernateProperty,LOCATE('_',hibernateProperty)+2))
WHERE entity = EntityIn AND hibernateProperty like '%\_%';


UPDATE viewproperty SET
messageCode = CONCAT(SUBSTRING_INDEX(messageCode,'_',1),
UPPER(SUBSTRING(messageCode,LOCATE('_',messageCode)+1,1)),
SUBSTRING(messageCode,LOCATE('_',messageCode)+2)),
property = CONCAT(SUBSTRING_INDEX(property,'_',1),
UPPER(SUBSTRING(property,LOCATE('_',property)+1,1)),
SUBSTRING(property,LOCATE('_',property)+2))
WHERE entity = EntityIn and property like '%\_%';

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `util_GenerateCode` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `util_GenerateCode`(EntityIn varchar(50), ScopeIn VARCHAR(25))
BEGIN

CALL util_HibernateMapping(EntityIn,ScopeIn);
CALL util_GetJavaModelProperties(EntityIn,ScopeIn);
CALL util_GetResultFields(EntityIn,ScopeIn);
CALL util_GetCreateFieldTags(EntityIn,ScopeIn);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `util_GetCreateFieldTags` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `util_GetCreateFieldTags`(EntityIn varchar(50), ScopeIn VARCHAR(25))
BEGIN


SELECT CONCAT('<tags:createField property="',`property`,'" component="${component}"/>')
FROM `viewproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn Order By `propOrder`;


SELECT CONCAT('<tags:createField property="',`property`,'" component="${component}" entity="',
  LOWER(LEFT(`Entity`,1)),RIGHT(`Entity`,LENGTH(`Entity`)-1),'"/>')
FROM `viewproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn Order By `propOrder`;


SELECT CONCAT('<tags:createField property="',`property`,'" component="${component}" entity="${instrTypeEncoded}"/>')
FROM `viewproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn Order By `propOrder`;


SELECT CONCAT('<tags:createField property="',`property`,'" component="',
  LOWER(LEFT(`Entity`,1)),RIGHT(`Entity`,LENGTH(`Entity`)-1),'"/>')
FROM `viewproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn Order By `propOrder`;


SELECT CONCAT('<tags:listField property="',`property`,'" component="${component}" listIndex="${iterator.index}" entityType="',
  LOWER(LEFT(`Entity`,1)),RIGHT(`Entity`,LENGTH(`Entity`)-1),'"/>')
FROM `viewproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn Order By `propOrder`;


END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `util_GetJavaModelProperties` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `util_GetJavaModelProperties`(EntityIn varchar(50), ScopeIn VARCHAR(25))
BEGIN

SELECT CONCAT('protected ',CASE WHEN `HibernateType` IN('many-to-one','one-to-many','one-to-one') THEN `HibernateClass`
     WHEN `HibernateType` = 'Timestamp' THEN 'Date'
     ELSE CONCAT(UPPER(LEFT(`HibernateType`,1)),RIGHT(`HibernateType`,LENGTH(`HibernateType`)-1)) END,
	  ' ',`HibernateProperty`,';')
FROM `hibernateproperty` WHERE `entity`=EntityIn and `Scope`=ScopeIn ORDER BY `DBTable`,`DBOrder`;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `util_GetResultFields` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `util_GetResultFields`(EntityIn varchar(50), ScopeIn VARCHAR(25))
BEGIN

SELECT CONCAT('"',`property`,'",')
FROM `viewproperty`
WHERE `entity`=EntityIn AND context='r' AND `scope`=ScopeIn
ORDER BY `propOrder`;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `util_HibernateMapping` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `util_HibernateMapping`(EntityIn varchar(50), ScopeIn Varchar(25))
BEGIN

SELECT CONCAT('<?xml version="1.0"?>

<!DOCTYPE hibernate-mapping PUBLIC
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping>

<class name="edu.ucsf.memory.[scope].[module].model.', EntityIn,'" table="', EntityIn,'" select-before-update="true">

		<id name="id" type="long">
			<column name="[KEY COLUMN]" not-null="true"/>
			<generator class="identity"/>
		</id>

')
FROM `hibernateproperty` WHERE `Entity` = EntityIn and `Scope` = ScopeIn GROUP BY `Entity`,`Scope`;

SELECT CONCAT('\t\t<',

    CASE WHEN `HibernateType` IN('many-to-one','one-to-many','one-to-one') THEN `HibernateType` ELSE 'property' END,

    ' name="',`HibernateProperty`,
    '" column="',`DBColumn`,'"',

    CASE WHEN `HibernateType` IN('many-to-one','one-to-many','one-to-one')
		   THEN CONCAT(' class="', COALESCE(`HibernateClass`,''),'"') ELSE CONCAT(' type="',`HibernateType`,'"') END,


   CASE WHEN `DBType` IN ('numeric','float','decimal')
		  THEN CONCAT(' precision="',CAST(`DBprecision` as CHAR),
                  '" scale="',CAST(DBScale as CHAR),'"') ELSE ''END,


    CASE WHEN `DBType` IN ('char','varchar','nchar','nvarchar','binary','varbinary')
		  THEN CONCAT(' length="',CAST(`DBLength` as CHAR),'"') ELSE '' END,


    CASE WHEN `HibernateNotNull` = 'Yes' THEN ' not-null="true"' ELSE '' END,'/>')
FROM `hibernateproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn ORDER BY `DBTable`,`DBOrder`;

SELECT '

	<!-- associations -->



	<!-- filters -->
               <!--define or remove these standard filters-->
               <filter name="projectContext" condition=":projectContext)=[define]"/>
               <filter name="patient" condition=":patientId=[define]"/>


</class>


         <!-- queries  -->



</hibernate-mapping>'
FROM `hibernateproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn GROUP BY `Entity`,`Scope`;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `audit_entity`
--

/*!50001 DROP TABLE IF EXISTS `audit_entity`*/;
/*!50001 DROP VIEW IF EXISTS `audit_entity`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `audit_entity` AS select `audit_entity_work`.`audit_entity_id` AS `audit_entity_id`,`audit_entity_work`.`audit_event_id` AS `audit_event_id`,`audit_entity_work`.`entity_id` AS `entity_id`,`audit_entity_work`.`entity` AS `entity`,`audit_entity_work`.`entity_type` AS `entity_type`,`audit_entity_work`.`audit_type` AS `audit_type`,`audit_entity_work`.`modified` AS `modified` from `audit_entity_work` union all select `audit_entity_history`.`audit_entity_id` AS `audit_entity_id`,`audit_entity_history`.`audit_event_id` AS `audit_event_id`,`audit_entity_history`.`entity_id` AS `entity_id`,`audit_entity_history`.`entity` AS `entity`,`audit_entity_history`.`entity_type` AS `entity_type`,`audit_entity_history`.`audit_type` AS `audit_type`,`audit_entity_history`.`modified` AS `modified` from `audit_entity_history` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `audit_event`
--

/*!50001 DROP TABLE IF EXISTS `audit_event`*/;
/*!50001 DROP VIEW IF EXISTS `audit_event`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `audit_event` AS select `audit_event_work`.`audit_event_id` AS `audit_event_id`,`audit_event_work`.`audit_user` AS `audit_user`,`audit_event_work`.`audit_host` AS `audit_host`,`audit_event_work`.`audit_timestamp` AS `audit_timestamp`,`audit_event_work`.`action` AS `action`,`audit_event_work`.`action_event` AS `action_event`,`audit_event_work`.`action_id_param` AS `action_id_param`,`audit_event_work`.`event_note` AS `event_note`,`audit_event_work`.`exception` AS `exception`,`audit_event_work`.`exception_message` AS `exception_message`,`audit_event_work`.`modified` AS `modified` from `audit_event_work` union all select `audit_event_history`.`audit_event_id` AS `audit_event_id`,`audit_event_history`.`audit_user` AS `audit_user`,`audit_event_history`.`audit_host` AS `audit_host`,`audit_event_history`.`audit_timestamp` AS `audit_timestamp`,`audit_event_history`.`action` AS `action`,`audit_event_history`.`action_event` AS `action_event`,`audit_event_history`.`action_id_param` AS `action_id_param`,`audit_event_history`.`event_note` AS `event_note`,`audit_event_history`.`exception` AS `exception`,`audit_event_history`.`exception_message` AS `exception_message`,`audit_event_history`.`modified` AS `modified` from `audit_event_history` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `audit_property`
--

/*!50001 DROP TABLE IF EXISTS `audit_property`*/;
/*!50001 DROP VIEW IF EXISTS `audit_property`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `audit_property` AS select `audit_property_work`.`audit_property_id` AS `audit_property_id`,`audit_property_work`.`audit_entity_id` AS `audit_entity_id`,`audit_property_work`.`property` AS `property`,`audit_property_work`.`index_key` AS `index_key`,`audit_property_work`.`subproperty` AS `subproperty`,`audit_property_work`.`old_value` AS `old_value`,`audit_property_work`.`new_value` AS `new_value`,`audit_property_work`.`audit_timestamp` AS `audit_timestamp`,`audit_property_work`.`modified` AS `modified` from `audit_property_work` union all select `audit_property_history`.`audit_property_id` AS `audit_property_id`,`audit_property_history`.`audit_entity_id` AS `audit_entity_id`,`audit_property_history`.`property` AS `property`,`audit_property_history`.`index_key` AS `index_key`,`audit_property_history`.`subproperty` AS `subproperty`,`audit_property_history`.`old_value` AS `old_value`,`audit_property_history`.`new_value` AS `new_value`,`audit_property_history`.`audit_timestamp` AS `audit_timestamp`,`audit_property_history`.`modified` AS `modified` from `audit_property_history` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `audit_text`
--

/*!50001 DROP TABLE IF EXISTS `audit_text`*/;
/*!50001 DROP VIEW IF EXISTS `audit_text`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `audit_text` AS select `audit_text_history`.`audit_property_id` AS `audit_property_id`,`audit_text_history`.`old_text` AS `old_text`,`audit_text_history`.`new_text` AS `new_text` from `audit_text_history` union all select `audit_text_work`.`audit_property_id` AS `audit_property_id`,`audit_text_work`.`old_text` AS `old_text`,`audit_text_work`.`new_text` AS `new_text` from `audit_text_work` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vwrptprojectpatientstatus`
--

/*!50001 DROP TABLE IF EXISTS `vwrptprojectpatientstatus`*/;
/*!50001 DROP VIEW IF EXISTS `vwrptprojectpatientstatus`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vwrptprojectpatientstatus` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `StatusDate`,`lps`.`LatestDesc` AS `Status`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`Project` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,_utf8'OVERALL' AS `Unit`,1 AS `UnitOrder` from ((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) union select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `LatestDate`,`lps`.`LatestDesc` AS `LatestDesc`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`ProjUnitDesc` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,`pu`.`Unit` AS `Unit`,2 AS `UnitOrder` from ((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) where (`pu`.`Unit` is not null) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vwrptprojectvisitlist`
--

/*!50001 DROP TABLE IF EXISTS `vwrptprojectvisitlist`*/;
/*!50001 DROP VIEW IF EXISTS `vwrptprojectvisitlist`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vwrptprojectvisitlist` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`TransLanguage` AS `TransLanguage`,`p`.`Gender` AS `Gender`,`p`.`AGE` AS `AGE`,`v`.`VLocation` AS `VLocation`,`v`.`VType` AS `VType`,`v`.`VWith` AS `VWith`,`v`.`VDate` AS `VDate`,`v`.`VStatus` AS `VStatus`,`v`.`ProjName` AS `ProjName`,`v`.`VNotes` AS `VNotes`,cast(`v`.`VDate` as date) AS `VDateNoTime` from (`patient` `p` join `visit` `v` on((`p`.`PIDN` = `v`.`PIDN`))) where (not((`v`.`VStatus` like _latin1'%CANC%'))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-07-20 19:44:28
