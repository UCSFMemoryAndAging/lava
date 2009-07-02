-- MySQL dump 10.13  Distrib 5.1.34, for apple-darwin9.5.0 (i386)
--
-- Host: localhost    Database: lava
-- ------------------------------------------------------
-- Server version	5.1.34

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_entity_work`
--

LOCK TABLES `audit_entity_work` WRITE;
/*!40000 ALTER TABLE `audit_entity_work` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=1071 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=328 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authgroup`
--

LOCK TABLES `authgroup` WRITE;
/*!40000 ALTER TABLE `authgroup` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authpermission`
--

LOCK TABLES `authpermission` WRITE;
/*!40000 ALTER TABLE `authpermission` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authrole`
--

LOCK TABLES `authrole` WRITE;
/*!40000 ALTER TABLE `authrole` DISABLE KEYS */;
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
  `authenticationType` varchar(10) DEFAULT 'EXTERNAL',
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authuser`
--

LOCK TABLES `authuser` WRITE;
/*!40000 ALTER TABLE `authuser` DISABLE KEYS */;
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
  KEY `authusergroup_GID` (`UGID`),
  CONSTRAINT `authusergroup_UID` FOREIGN KEY (`UID`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `authusergroup_GID` FOREIGN KEY (`UGID`) REFERENCES `authgroup` (`GID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authusergroup`
--

LOCK TABLES `authusergroup` WRITE;
/*!40000 ALTER TABLE `authusergroup` DISABLE KEYS */;
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
  KEY `authuserrole_RoleID` (`RoleID`),
  KEY `authuserrole_UID` (`URID`),
  KEY `authuserrole_GID` (`GID`),
  CONSTRAINT `authuserrole_RoleID` FOREIGN KEY (`RoleID`) REFERENCES `authrole` (`RoleID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `authuserrole_UID` FOREIGN KEY (`URID`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `authuserrole_GID` FOREIGN KEY (`GID`) REFERENCES `authgroup` (`GID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authuserrole`
--

LOCK TABLES `authuserrole` WRITE;
/*!40000 ALTER TABLE `authuserrole` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=1809 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernateproperty`
--

LOCK TABLES `hibernateproperty` WRITE;
/*!40000 ALTER TABLE `hibernateproperty` DISABLE KEYS */;
INSERT INTO `hibernateproperty` VALUES (1625,'lava','core','appointment','id','appointment','reservation_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-03-31 20:35:27'),(1626,'lava','core','appointment','calendar','appointment','calendar_id','int',NULL,10,0,2,'calendar','many-to-one','edu.ucsf.lava.core.resource.model.ResourceCalendar','Yes','2009-03-31 20:35:27'),(1627,'lava','core','appointment','organizer','appointment','organizer_id','int',NULL,10,0,3,'owner','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-03-31 20:35:27'),(1628,'lava','core','appointment','type','appointment','type','varchar',25,NULL,NULL,4,'type','string',NULL,'Yes','2009-03-31 20:35:27'),(1629,'lava','core','appointment','description','appointment','description','varchar',100,NULL,NULL,5,'description','string',NULL,'No','2009-03-31 20:35:27'),(1630,'lava','core','appointment','location','appointment','location','varchar',100,NULL,NULL,6,'location','string',NULL,'No','2009-03-31 20:35:27'),(1631,'lava','core','appointment','startDate','appointment','start_date','date',NULL,NULL,NULL,7,'startDate','date',NULL,'Yes','2009-03-31 20:35:27'),(1632,'lava','core','appointment','startTime','appointment','start_time','time',NULL,NULL,NULL,8,'startTime','time',NULL,'Yes','2009-03-31 20:35:27'),(1633,'lava','core','appointment','endDate','appointment','end_date','date',NULL,NULL,NULL,9,'endDate','date',NULL,'Yes','2009-06-02 22:27:56'),(1634,'lava','core','appointment','endTime','appointment','end_time','time',NULL,NULL,NULL,10,'endTime','time',NULL,NULL,'2009-06-02 22:28:28'),(1635,'lava','core','appointment','status','appointment','status','varchar',25,NULL,NULL,11,'status','string',NULL,'No','2009-05-11 19:45:09'),(1636,'lava','core','appointment','notes','appointment','notes','text',NULL,NULL,NULL,11,'notes','string',NULL,'No','2009-03-31 20:35:27'),(1637,'lava','core','appointment_change','id','appointment_change','appointment_change_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-05-11 20:29:54'),(1638,'lava','core','appointment_change','appointment','appointment_change','appointment_id','int',NULL,10,0,2,'appointment','many-to-one','edu.ucsf.lava.core.calendar.model.Appointment','Yes','2009-05-11 20:29:54'),(1639,'lava','core','appointment_change','type','appointment_change','type','varchar',25,NULL,NULL,3,'type','string',NULL,'Yes','2009-05-11 20:29:54'),(1640,'lava','core','appointment_change','changeBy','appointment_change','change_by','int',NULL,10,0,4,'changeBy','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-05-11 20:29:54'),(1641,'lava','core','appointment_change','changeTimestamp','appointment_change','change_timestamp','timestamp',NULL,NULL,NULL,5,'changeTimestamp','timestamp',NULL,'Yes','2009-05-11 20:29:54'),(1642,'lava','core','attendee','id','attendee','attendee_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-06-03 20:40:16'),(1643,'lava','core','attendee','appointment','attendee','appointment_id','int',NULL,10,0,2,'appointment','many-to-one','edu.ucsf.lava.core.calendar.model.Appointment','Yes','2009-06-03 20:40:16'),(1644,'lava','core','attendee','user','attendee','user_id','int',NULL,10,0,3,'user','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-06-03 20:40:16'),(1645,'lava','core','attendee','role','attendee','role','varchar',25,NULL,NULL,4,'role','string',NULL,'Yes','2009-06-03 20:40:16'),(1646,'lava','core','attendee','status','attendee','status','varchar',25,NULL,NULL,5,'status','string',NULL,'Yes','2009-06-03 20:40:16'),(1647,'lava','core','attendee','notes','attendee','notes','varchar',100,NULL,NULL,6,'notes','string',NULL,'No','2009-06-03 20:40:16'),(1648,'lava','core','auditEntity','id','Audit_Entity_Work','audit_entity_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1649,'lava','core','auditEntity','auditEvent','Audit_Entity_Work','audit_event_id','int',NULL,10,0,2,'auditEvent','many-to-one','edu.ucsf.memory.lava.model.AuditEvent','Yes','2009-01-25 05:25:56'),(1650,'lava','core','auditEntity','entityId','Audit_Entity_Work','entity_id','int',NULL,10,0,3,'entityId','long',NULL,'Yes','2009-01-25 05:25:56'),(1651,'lava','core','auditEntity','entity','Audit_Entity_Work','entity','varchar',100,NULL,NULL,4,'entity','string',NULL,'Yes','2009-01-25 05:25:56'),(1652,'lava','core','auditEntity','entityType','Audit_Entity_Work','entity_type','varchar',100,NULL,NULL,5,'entityType','string',NULL,'Yes','2009-01-25 05:25:56'),(1653,'lava','core','auditEntity','auditType','Audit_Entity_Work','audit_type','varchar',10,NULL,NULL,6,'auditType','string',NULL,'Yes','2009-01-25 05:25:56'),(1654,'lava','core','auditEntity','hversion','Audit_Entity_Work','hversion','int',NULL,10,0,7,'hversion','long',NULL,'Yes','2009-01-25 05:25:56'),(1655,'lava','core','auditEntityHistory','id','Audit_Entity','audit_entity_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1656,'lava','core','auditEntityHistory','auditEvent','Audit_Entity','audit_event_id','int',NULL,10,0,2,'auditEvent','many-to-one','edu.ucsf.memory.lava.model.AuditEvent','Yes','2009-01-25 05:25:56'),(1657,'lava','core','auditEntityHistory','entityId','Audit_Entity','entity_id','int',NULL,10,0,3,'entityId','long',NULL,'Yes','2009-01-25 05:25:56'),(1658,'lava','core','auditEntityHistory','entity','Audit_Entity','entity','varchar',100,NULL,NULL,4,'entity','string',NULL,'Yes','2009-01-25 05:25:56'),(1659,'lava','core','auditEntityHistory','entityType','Audit_Entity','entity_type','varchar',100,NULL,NULL,5,'entityType','string',NULL,'Yes','2009-01-25 05:25:56'),(1660,'lava','core','auditEntityHistory','auditType','Audit_Entity','audit_type','varchar',10,NULL,NULL,6,'auditType','string',NULL,'Yes','2009-01-25 05:25:56'),(1661,'lava','core','auditEntityHistory','hversion','Audit_Entity','hversion','int',NULL,10,0,7,'hversion','long',NULL,'Yes','2009-01-25 05:25:56'),(1662,'lava','core','auditEvent','id','Audit_Event_Work','audit_event_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1663,'lava','core','auditEvent','auditUser','Audit_Event_Work','audit_user','varchar',50,NULL,NULL,2,'auditUser','string',NULL,'Yes','2009-01-25 05:25:56'),(1664,'lava','core','auditEvent','auditHost','Audit_Event_Work','audit_host','varchar',25,NULL,NULL,3,'auditHost','string',NULL,'Yes','2009-01-25 05:25:56'),(1665,'lava','core','auditEvent','auditTimestamp','Audit_Event_Work','audit_timestamp','timestamp',NULL,23,3,4,'auditTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1666,'lava','core','auditEvent','action','Audit_Event_Work','action','varchar',255,NULL,NULL,5,'action','string',NULL,'Yes','2009-01-25 05:25:56'),(1667,'lava','core','auditEvent','actionEvent','Audit_Event_Work','action_event','varchar',50,NULL,NULL,6,'actionEvent','string',NULL,'Yes','2009-01-25 05:25:56'),(1668,'lava','core','auditEvent','actionIdParam','Audit_Event_Work','action_id_param','varchar',50,NULL,NULL,7,'actionIdParam','string',NULL,'No','2009-01-25 05:25:56'),(1669,'lava','core','auditEvent','eventNote','Audit_Event_Work','event_note','varchar',255,NULL,NULL,8,'eventNote','string',NULL,'No','2009-01-25 05:25:56'),(1670,'lava','core','auditEvent','exception','Audit_Event_Work','exception','varchar',255,NULL,NULL,9,'exception','string',NULL,'No','2009-01-25 05:25:56'),(1671,'lava','core','auditEvent','exceptionMessage','Audit_Event_Work','exception_message','varchar',255,NULL,NULL,10,'exceptionMessage','string',NULL,'No','2009-01-25 05:25:56'),(1672,'lava','core','auditEvent','hversion','Audit_Event_Work','hversion','int',NULL,10,0,11,'hversion','long',NULL,'Yes','2009-01-25 05:25:56'),(1673,'lava','core','auditEventHistory','id','Audit_Event','audit_event_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1674,'lava','core','auditEventHistory','auditUser','Audit_Event','audit_user','varchar',50,NULL,NULL,2,'auditUser','string',NULL,'Yes','2009-01-25 05:25:56'),(1675,'lava','core','auditEventHistory','auditHost','Audit_Event','audit_host','varchar',25,NULL,NULL,3,'auditHost','string',NULL,'Yes','2009-01-25 05:25:56'),(1676,'lava','core','auditEventHistory','auditTimestamp','Audit_Event','audit_timestamp','timestamp',NULL,23,3,4,'auditTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1677,'lava','core','auditEventHistory','action','Audit_Event','action','varchar',255,NULL,NULL,5,'action','string',NULL,'Yes','2009-01-25 05:25:56'),(1678,'lava','core','auditEventHistory','actionEvent','Audit_Event','action_event','varchar',50,NULL,NULL,6,'actionEvent','string',NULL,'Yes','2009-01-25 05:25:56'),(1679,'lava','core','auditEventHistory','actionIdParam','Audit_Event','action_id_param','varchar',50,NULL,NULL,7,'actionIdParam','string',NULL,'No','2009-01-25 05:25:56'),(1680,'lava','core','auditEventHistory','eventNote','Audit_Event','event_note','varchar',255,NULL,NULL,8,'eventNote','string',NULL,'No','2009-01-25 05:25:56'),(1681,'lava','core','auditEventHistory','exception','Audit_Event','exception','varchar',255,NULL,NULL,9,'exception','string',NULL,'No','2009-01-25 05:25:56'),(1682,'lava','core','auditEventHistory','exceptionMessage','Audit_Event','exception_message','varchar',255,NULL,NULL,10,'exceptionMessage','string',NULL,'No','2009-01-25 05:25:56'),(1683,'lava','core','auditEventHistory','hversion','Audit_Event','hversion','int',NULL,10,0,11,'hversion','long',NULL,'Yes','2009-01-25 05:25:56'),(1684,'lava','core','auditProperty','id','Audit_Property_Work','audit_property_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1685,'lava','core','auditProperty','auditEntity','Audit_Property_Work','audit_entity_id','int',NULL,10,0,2,'auditEntity','many-to-one','edu.ucsf.memory.lava.model.AuditEntity','Yes','2009-01-25 05:25:56'),(1686,'lava','core','auditProperty','property','Audit_Property_Work','property','varchar',100,NULL,NULL,3,'property','string',NULL,'Yes','2009-01-25 05:25:56'),(1687,'lava','core','auditProperty','indexKey','Audit_Property_Work','index_key','varchar',100,NULL,NULL,4,'indexKey','string',NULL,'No','2009-01-25 05:25:56'),(1688,'lava','core','auditProperty','subproperty','Audit_Property_Work','subproperty','varchar',255,NULL,NULL,5,'subproperty','string',NULL,'No','2009-01-25 05:25:56'),(1689,'lava','core','auditProperty','oldValue','Audit_Property_Work','old_value','varchar',255,NULL,NULL,6,'oldValue','string',NULL,'Yes','2009-01-25 05:25:56'),(1690,'lava','core','auditProperty','newValue','Audit_Property_Work','new_value','varchar',255,NULL,NULL,7,'newValue','string',NULL,'Yes','2009-01-25 05:25:56'),(1691,'lava','core','auditProperty','auditTimestamp','Audit_Property_Work','audit_timestamp','timestamp',NULL,23,3,8,'auditTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1692,'lava','core','auditProperty','oldText','Audit_Text_Work','old_text','text',16,NULL,NULL,9,'oldText','text',NULL,'No','2009-01-25 05:25:56'),(1693,'lava','core','auditProperty','newText','Audit_Text_Work','new_text','text',16,NULL,NULL,10,'newText','text',NULL,'No','2009-01-25 05:25:56'),(1694,'lava','core','auditProperty','hversion','Audit_Property_Work','hversion','int',NULL,10,0,11,'hversion','long',NULL,'No','2009-01-25 05:25:56'),(1695,'lava','core','auditPropertyHistory','id','Audit_Property','audit_property_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1696,'lava','core','auditPropertyHistory','auditEntity','Audit_Property','audit_entity_id','int',NULL,10,0,2,'auditEntity','many-to-one','edu.ucsf.memory.lava.model.AuditEntity','Yes','2009-01-25 05:25:56'),(1697,'lava','core','auditPropertyHistory','property','Audit_Property','property','varchar',100,NULL,NULL,3,'property','string',NULL,'Yes','2009-01-25 05:25:56'),(1698,'lava','core','auditPropertyHistory','indexKey','Audit_Property','index_key','varchar',100,NULL,NULL,4,'indexKey','string',NULL,'No','2009-01-25 05:25:56'),(1699,'lava','core','auditPropertyHistory','subproperty','Audit_Property','subproperty','varchar',255,NULL,NULL,5,'subproperty','string',NULL,'No','2009-01-25 05:25:56'),(1700,'lava','core','auditPropertyHistory','oldValue','Audit_Property','old_value','varchar',255,NULL,NULL,6,'oldValue','string',NULL,'Yes','2009-01-25 05:25:56'),(1701,'lava','core','auditPropertyHistory','newValue','Audit_Property','new_value','varchar',255,NULL,NULL,7,'newValue','string',NULL,'Yes','2009-01-25 05:25:56'),(1702,'lava','core','auditPropertyHistory','auditTimestamp','Audit_Property','audit_timestamp','timestamp',NULL,23,3,8,'auditTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1703,'lava','core','auditPropertyHistory','oldText','Audit_Text','old_text','text',16,NULL,NULL,9,'oldText','text',NULL,'No','2009-01-25 05:25:56'),(1704,'lava','core','auditPropertyHistory','newText','Audit_Text','new_text','text',16,NULL,NULL,10,'newText','text',NULL,'No','2009-01-25 05:25:56'),(1705,'lava','core','auditPropertyHistory','hversion','Audit_Property','hversion','int',NULL,10,0,11,'hversion','long',NULL,'No','2009-01-25 05:25:56'),(1706,'lava','core','authGroup','id','AuthGroup','GID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1707,'lava','core','authGroup','groupName','AuthGroup','GroupName','varchar',50,NULL,NULL,2,'groupName','string',NULL,'Yes','2009-01-25 05:25:56'),(1708,'lava','core','authGroup','effectiveDate','AuthGroup','EffectiveDate','date',NULL,16,0,3,'effectiveDate','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1709,'lava','core','authGroup','expirationDate','AuthGroup','ExpirationDate','date',NULL,16,0,4,'expirationDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1710,'lava','core','authGroup','notes','AuthGroup','Notes','varchar',255,NULL,NULL,5,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1711,'lava','core','authPermission','id','AuthPermission','PermID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1712,'lava','core','authPermission','role','AuthPermission','RoleID','varchar',25,NULL,NULL,2,'role','many-to-one','edu.ucsf.memory.lava.model.AuthRole','Yes','2009-01-25 05:25:56'),(1713,'lava','core','authPermission','module','AuthPermission','Module','varchar',25,NULL,NULL,4,'module','string',NULL,'Yes','2009-01-25 05:25:56'),(1714,'lava','core','authPermission','permitDeny','AuthPermission','PermitDeny','varchar',10,NULL,NULL,4,'permitDeny','string',NULL,'Yes','2009-01-25 05:25:56'),(1715,'lava','core','authPermission','section','AuthPermission','Section','varchar',25,NULL,NULL,5,'section','string',NULL,'Yes','2009-01-25 05:25:56'),(1716,'lava','core','authPermission','target','AuthPermission','Target','varchar',25,NULL,NULL,6,'target','string',NULL,'Yes','2009-01-25 05:25:56'),(1717,'lava','core','authPermission','mode','AuthPermission','Mode','varchar',25,NULL,NULL,7,'mode','string',NULL,'Yes','2009-01-25 05:25:56'),(1718,'lava','core','authPermission','notes','AuthPermission','Notes','varchar',100,NULL,NULL,10,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1719,'lava','core','authRole','id','AuthRole','RoleID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1720,'lava','core','authRole','roleName','AuthRole','RoleName','varchar',25,NULL,NULL,2,'roleName','string',NULL,'Yes','2009-01-25 05:25:56'),(1721,'lava','core','authRole','patientAccess','AuthRole','PatientAccess','smallint',NULL,5,0,3,'patientAccess','short',NULL,'Yes','2009-01-25 05:25:56'),(1722,'lava','core','authRole','phiAccess','AuthRole','PhiAccess','smallint',NULL,5,0,4,'phiAccess','short',NULL,'Yes','2009-01-25 05:25:56'),(1723,'lava','core','authRole','patientAccess','AuthRole','GhiAccess','smallint',NULL,5,0,5,'ghiAccess','short',NULL,'Yes','2009-01-25 05:25:56'),(1724,'lava','core','authRole','notes','AuthRole','Notes','varchar',255,NULL,NULL,8,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1725,'lava','core','authUser','id','AuthUser','UID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1726,'lava','core','authUser','userName','AuthUser','UserName','varchar',50,NULL,NULL,2,'userName','string',NULL,'Yes','2009-01-25 05:25:56'),(1727,'lava','core','authUser','login','AuthUser','Login','varchar',100,NULL,NULL,3,'login','string',NULL,'No','2009-01-25 05:25:56'),(1728,'lava','core','authUser','email','authuser','email','varchar',100,NULL,NULL,4,'email','string',NULL,'No','2009-05-12 18:53:20'),(1729,'lava','core','authUser','phone','authuser','phone','varchar',25,NULL,NULL,5,'phone','string',NULL,'No','2009-05-12 18:53:20'),(1730,'lava','core','authUser','accessAgreementDate','AuthUser','AccessAgreementDate','date',NULL,16,0,7,'accessAgreementDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1731,'lava','core','authUser','shortUserName','AuthUser','ShortUserName','varchar',53,NULL,NULL,8,'shortUserName','string',NULL,'No','2009-01-25 05:25:56'),(1732,'lava','core','authUser','shortUserNameRev','AuthUser','ShortUserNameRev','varchar',54,NULL,NULL,9,'shortUserNameRev','string',NULL,'No','2009-01-25 05:25:56'),(1733,'lava','core','authUser','effectiveDate','AuthUser','EffectiveDate','date',NULL,16,0,10,'effectiveDate','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1734,'lava','core','authUser','expirationDate','AuthUser','ExpirationDate','date',NULL,16,0,11,'expirationDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1735,'lava','core','authUser','authenticationType','authuser','authenticationType','varchar',10,NULL,NULL,12,'authenticationType','string',NULL,'No','2009-05-12 18:53:20'),(1736,'lava','core','authUser','notes','AuthUser','Notes','varchar',255,NULL,NULL,12,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1737,'lava','core','authUser','password','authuser','password','varchar',100,NULL,NULL,13,'password','string',NULL,'No','2009-05-12 18:53:20'),(1738,'lava','core','authUser','passwordExpiration','authuser','passwordExpiration','timestamp',NULL,NULL,NULL,14,'passwordExpiration','timestamp',NULL,'No','2009-05-12 18:53:20'),(1739,'lava','core','authUser','passwordResetToken','authuser','passwordResetToken','varchar',100,NULL,NULL,15,'passwordResetToken','string',NULL,'No','2009-05-12 18:53:20'),(1740,'lava','core','authUser','passwordResetExpiration','authuser','passwordResetExpiration','timestamp',NULL,NULL,NULL,16,'passwordResetExpiration','timestamp',NULL,'No','2009-05-12 18:53:20'),(1741,'lava','core','authUser','failedLoginCount','authuser','failedLoginCount','smallint',NULL,5,0,17,'failedLoginCount','short',NULL,'No','2009-05-12 18:53:20'),(1742,'lava','core','authUser','lastFailedLogin','authuser','lastFailedLogin','timestamp',NULL,NULL,NULL,18,'lastFailedLogin','timestamp',NULL,'No','2009-05-12 18:53:20'),(1743,'lava','core','authUser','accountLocked','authuser','accountLocked','timestamp',NULL,NULL,NULL,19,'accountLocked','timestamp',NULL,'No','2009-05-12 18:53:20'),(1744,'lava','core','authUserGroup','id','AuthUserGroup','UGID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1745,'lava','core','authUserGroup','user','AuthUserGroup','UID','int',NULL,10,0,2,'user','many-to-one','edu.ucsf.memory.lava.model.AuthUser','Yes','2009-01-25 05:25:56'),(1746,'lava','core','authUserGroup','group','AuthUserGroup','GID','int',NULL,10,0,3,'group','many-to-one','edu.ucsf.memory.lava.model.AuthGroup','Yes','2009-01-25 05:25:56'),(1747,'lava','core','authUserGroup','notes','AuthUserGroup','Notes','varchar',255,NULL,NULL,6,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1748,'lava','core','authUserRole','id','AuthUserRole','URID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1749,'lava','core','authUserRole','role','AuthUserRole','RoleID','varchar',25,NULL,NULL,3,'role','many-to-one','edu.ucsf.memory.lava.model.AuthRole','Yes','2009-01-25 05:25:56'),(1750,'lava','core','authUserRole','project','AuthUserRole','Project','varchar',25,NULL,NULL,4,'project','string',NULL,'No','2009-01-25 05:25:56'),(1751,'lava','core','authUserRole','unit','AuthUserRole','Unit','varchar',25,NULL,NULL,5,'unit','string',NULL,'No','2009-01-25 05:25:56'),(1752,'lava','core','authUserRole','user','AuthUserRole','UID','int',NULL,10,0,7,'user','many-to-one','edu.ucsf.memory.lava.model.AuthUser','No','2009-01-25 05:25:56'),(1753,'lava','core','authUserRole','group','AuthUserRole','GID','int',NULL,10,0,8,'group','many-to-one','edu.ucsf.memory.lava.model.AuthGroup','No','2009-01-25 05:25:56'),(1754,'lava','core','authUserRole','notes','AuthUserRole','Notes','varchar',255,NULL,NULL,12,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1755,'lava','core','calendar','calendar_id','calendar','calendar_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-03-31 19:09:35'),(1756,'lava','core','calendar','type','calendar','type','varchar',25,NULL,NULL,2,'type','string',NULL,'Yes','2009-06-02 22:27:08'),(1757,'lava','core','calendar','name','calendar','name','varchar',100,NULL,NULL,3,'name','string',NULL,'Yes','2009-03-31 19:09:35'),(1758,'lava','core','calendar','description','calendar','description','varchar',255,NULL,NULL,4,'description','string',NULL,'No','2009-03-31 19:09:35'),(1759,'lava','core','calendar','notes','calendar','notes','text',NULL,NULL,NULL,5,'notes','string',NULL,'No','2009-03-31 19:09:35'),(1760,'lava','core','HibernateProperty','id','HibernateProperty','id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1761,'lava','core','HibernateProperty','scope','HibernateProperty','scope','varchar',100,NULL,NULL,2,'scope','string',NULL,'Yes','2009-01-25 05:25:56'),(1762,'lava','core','HibernateProperty','entity','HibernateProperty','entity','varchar',100,NULL,NULL,3,'entity','string',NULL,'Yes','2009-01-25 05:25:56'),(1763,'lava','core','HibernateProperty','property','HibernateProperty','property','varchar',100,NULL,NULL,4,'property','string',NULL,'Yes','2009-01-25 05:25:56'),(1764,'lava','core','HibernateProperty','dbTable','HibernateProperty','dbTable','varchar',50,NULL,NULL,5,'dbTable','string',NULL,'Yes','2009-01-25 05:25:56'),(1765,'lava','core','HibernateProperty','dbColumn','HibernateProperty','dbColumn','varchar',50,NULL,NULL,6,'dbColumn','string',NULL,'Yes','2009-01-25 05:25:56'),(1766,'lava','core','HibernateProperty','dbType','HibernateProperty','dbType','varchar',50,NULL,NULL,7,'dbType','string',NULL,'No','2009-01-25 05:25:56'),(1767,'lava','core','HibernateProperty','dbLength','HibernateProperty','dbLength','smallint',NULL,5,0,8,'dbLength','short',NULL,'No','2009-01-25 05:25:56'),(1768,'lava','core','HibernateProperty','dbPrecision','HibernateProperty','dbPrecision','smallint',NULL,5,0,9,'dbPrecision','short',NULL,'No','2009-01-25 05:25:56'),(1769,'lava','core','HibernateProperty','dbScale','HibernateProperty','dbScale','smallint',NULL,5,0,10,'dbScale','short',NULL,'No','2009-01-25 05:25:56'),(1770,'lava','core','HibernateProperty','dbOrder','HibernateProperty','dbOrder','smallint',NULL,5,0,11,'dbOrder','short',NULL,'No','2009-01-25 05:25:56'),(1771,'lava','core','HibernateProperty','hibernateProperty','HibernateProperty','hibernateProperty','varchar',50,NULL,NULL,12,'hibernateProperty','string',NULL,'No','2009-01-25 05:25:56'),(1772,'lava','core','HibernateProperty','hibernateType','HibernateProperty','hibernateType','varchar',50,NULL,NULL,13,'hibernateType','string',NULL,'No','2009-01-25 05:25:56'),(1773,'lava','core','HibernateProperty','hibernateClass','HibernateProperty','hibernateClass','varchar',250,NULL,NULL,14,'hibernateClass','string',NULL,'No','2009-01-25 05:25:56'),(1774,'lava','core','HibernateProperty','hibernateNotNull','HibernateProperty','hibernateNotNull','varchar',50,NULL,NULL,15,'hibernateNotNull','string',NULL,'No','2009-01-25 05:25:56'),(1775,'lava','core','lavaSession','id','lava_session','lava_session_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1776,'lava','core','lavaSession','serverInstanceId','LavaSession','ServerInstanceID','int',NULL,10,0,2,'serverInstanceId','long',NULL,'Yes','2009-01-25 05:25:56'),(1777,'lava','core','lavaSession','createTimestamp','lava_session','create_timestamp','timestamp',NULL,23,3,3,'createTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1778,'lava','core','lavaSession','accessTimestamp','lava_session','access_timestamp','timestamp',NULL,23,3,4,'accessTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1779,'lava','core','lavaSession','expireTimestamp','lava_session','expire_timestamp','timestamp',0,23,3,5,'expireTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1780,'lava','core','lavaSession','currentStatus','lava_session','current_status','varchar',25,NULL,NULL,6,'currentStatus','string',NULL,'Yes','2009-01-25 05:25:56'),(1781,'lava','core','lavaSession','userId','lava_session','user_id','int',NULL,10,0,7,'userId','long',NULL,'No','2009-01-25 05:25:56'),(1782,'lava','core','lavaSession','username','lava_session','user_name','varchar',50,NULL,NULL,8,'username','string',NULL,'No','2009-01-25 05:25:56'),(1783,'lava','core','lavaSession','hostname','lava_session','host_name','varchar',50,NULL,NULL,9,'hostname','string',NULL,'No','2009-01-25 05:25:56'),(1784,'lava','core','lavaSession','httpSessionId','lava_session','http_session_id','int',64,NULL,NULL,10,'httpSessionId','string',NULL,'No','2009-01-25 05:25:56'),(1785,'lava','core','lavaSession','disconnectTime','lava_session','disconnect_time','time',NULL,23,3,11,'disconnectTime','time',NULL,'No','2009-01-25 05:25:56'),(1786,'lava','core','lavaSession','disconnectMessage','lava_session','disconnect_message','varchar',255,NULL,NULL,12,'disconnectMessage','string',NULL,'No','2009-01-25 05:25:56'),(1787,'lava','core','lavaSession','notes','lava_session','Notes','varchar',255,NULL,NULL,13,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1788,'lava','core','lavaSession','disconnectDate','lava_session','disconnect_date','date',0,NULL,NULL,14,'disconnectDate','date',NULL,'No','2009-04-22 18:00:00'),(1789,'lava','core','resourceCalendar','id','resource_calendar','calendar_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-06-03 17:28:00'),(1790,'lava','core','resourceCalendar','resourceType','resource_calendar','resource_type','varchar',25,NULL,NULL,2,'resourceType','string',NULL,'Yes','2009-06-03 17:28:00'),(1791,'lava','core','resourceCalendar','location','resource_calendar','location','varchar',100,NULL,NULL,3,'location','string',NULL,'No','2009-06-03 17:28:00'),(1792,'lava','core','resourceCalendar','contact','resource_calendar','contact_id','int',NULL,10,0,4,'contact','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-06-03 17:28:00'),(1793,'lava','core','ViewProperty','id','ViewProperty','id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1794,'lava','core','ViewProperty','messageCode','ViewProperty','messageCode','varchar',250,NULL,NULL,2,'messageCode','string',NULL,'No','2009-01-25 05:25:56'),(1795,'lava','core','ViewProperty','locale','ViewProperty','locale','varchar',10,NULL,NULL,3,'locale','string',NULL,'Yes','2009-01-25 05:25:56'),(1796,'lava','core','ViewProperty','scope','ViewProperty','scope','varchar',100,NULL,NULL,4,'scope','string',NULL,'Yes','2009-01-25 05:25:56'),(1797,'lava','core','ViewProperty','prefix','ViewProperty','prefix','varchar',50,NULL,NULL,5,'prefix','string',NULL,'No','2009-01-25 05:25:56'),(1798,'lava','core','ViewProperty','entity','ViewProperty','entity','varchar',100,NULL,NULL,6,'entity','string',NULL,'Yes','2009-01-25 05:25:56'),(1799,'lava','core','ViewProperty','property','ViewProperty','property','varchar',100,NULL,NULL,7,'property','string',NULL,'Yes','2009-01-25 05:25:56'),(1800,'lava','core','ViewProperty','section','ViewProperty','section','varchar',100,NULL,NULL,8,'section','string',NULL,'No','2009-01-25 05:25:56'),(1801,'lava','core','ViewProperty','context','ViewProperty','context','varchar',10,NULL,NULL,9,'context','string',NULL,'No','2009-01-25 05:25:56'),(1802,'lava','core','ViewProperty','style','ViewProperty','style','varchar',25,NULL,NULL,10,'style','string',NULL,'No','2009-01-25 05:25:56'),(1803,'lava','core','ViewProperty','list','ViewProperty','list','varchar',50,NULL,NULL,11,'list','string',NULL,'No','2009-01-25 05:25:56'),(1804,'lava','core','ViewProperty','attributes','ViewProperty','attributes','varchar',100,NULL,NULL,12,'attributes','string',NULL,'No','2009-01-25 05:25:56'),(1805,'lava','core','ViewProperty','required','ViewProperty','required','varchar',10,NULL,NULL,13,'required','string',NULL,'No','2009-01-25 05:25:56'),(1806,'lava','core','ViewProperty','label','ViewProperty','label','varchar',500,NULL,NULL,14,'label','string',NULL,'No','2009-01-25 05:25:56'),(1807,'lava','core','ViewProperty','quickHelp','ViewProperty','quickHelp','varchar',500,NULL,NULL,15,'quickHelp','string',NULL,'No','2009-01-25 05:25:56'),(1808,'lava','core','ViewProperty','propOrder','ViewProperty','propOrder','int',NULL,10,0,16,'propOrder','long',NULL,'No','2009-01-25 05:25:56');
/*!40000 ALTER TABLE `hibernateproperty` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=243 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=478 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `list`
--

LOCK TABLES `list` WRITE;
/*!40000 ALTER TABLE `list` DISABLE KEYS */;
INSERT INTO `list` VALUES (468,'LavaSessionStatus','core',0,'2009-01-25 04:57:59'),(469,'NavigationListPageSize','core',1,'2009-01-25 04:57:59'),(470,'TextYesNo','core',0,'2009-01-25 04:57:59'),(471,'TextYesNoDK','core',0,'2009-01-25 04:57:59'),(472,'TextYesNoNA','core',0,'2009-01-25 04:57:59'),(473,'YESNO','core',1,'2009-01-25 04:57:59'),(474,'YESNODK','core',1,'2009-01-25 04:57:59'),(475,'YesNoDK_Zero','core',0,'2009-01-25 04:57:59'),(476,'YesNoUnknown','core',1,'2009-01-25 04:57:59'),(477,'YesNo_Zero','core',0,'2009-01-25 04:57:59');
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
) ENGINE=InnoDB AUTO_INCREMENT=24409 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listvalues`
--

LOCK TABLES `listvalues` WRITE;
/*!40000 ALTER TABLE `listvalues` DISABLE KEYS */;
INSERT INTO `listvalues` VALUES (24376,468,'NEW',NULL,1,'2009-01-25 04:57:59'),(24377,468,'ACTIVE',NULL,2,'2009-01-25 04:57:59'),(24378,468,'LOGOFF',NULL,3,'2009-01-25 04:57:59'),(24379,468,'EXPIRED',NULL,4,'2009-01-25 04:57:59'),(24380,468,'DISCONNECTED',NULL,5,'2009-01-25 04:57:59'),(24381,469,'10','10/page',0,'2009-01-25 04:57:59'),(24382,469,'100','100/page',0,'2009-01-25 04:57:59'),(24383,469,'15','15/page',0,'2009-01-25 04:57:59'),(24384,469,'25','25/page',0,'2009-01-25 04:57:59'),(24385,469,'250','250/page',0,'2009-01-25 04:57:59'),(24386,469,'5','5/page',0,'2009-01-25 04:57:59'),(24387,469,'50','50/page',0,'2009-01-25 04:57:59'),(24388,470,'Yes',NULL,1,'2009-01-25 04:57:59'),(24389,470,'No',NULL,2,'2009-01-25 04:57:59'),(24390,471,'Yes',NULL,1,'2009-01-25 04:57:59'),(24391,471,'No',NULL,2,'2009-01-25 04:57:59'),(24392,471,'Don\'t Know',NULL,3,'2009-01-25 04:57:59'),(24393,472,'Yes',NULL,1,'2009-01-25 04:57:59'),(24394,472,'No',NULL,2,'2009-01-25 04:57:59'),(24395,472,'N/A',NULL,3,'2009-01-25 04:57:59'),(24396,473,'1','Yes',0,'2009-01-25 04:57:59'),(24397,473,'2','No',0,'2009-01-25 04:57:59'),(24398,474,'1','Yes',0,'2009-01-25 04:57:59'),(24399,474,'2','No',0,'2009-01-25 04:57:59'),(24400,474,'9','Don\'t Know',0,'2009-01-25 04:57:59'),(24401,475,'0','No',0,'2009-01-25 04:57:59'),(24402,475,'1','Yes',0,'2009-01-25 04:57:59'),(24403,475,'9','Don\'t Know',0,'2009-01-25 04:57:59'),(24404,476,'0','No',0,'2009-01-25 04:57:59'),(24405,476,'1','Yes',0,'2009-01-25 04:57:59'),(24406,476,'9','Unknown',0,'2009-01-25 04:57:59'),(24407,477,'0','No',0,'2009-01-25 04:57:59'),(24408,477,'1','Yes',0,'2009-01-25 04:57:59');
/*!40000 ALTER TABLE `listvalues` ENABLE KEYS */;
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
INSERT INTO `versionhistory` VALUES ('lava-core-model','3.0.3','2009-07-02 15:51:49',3,0,3,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=2666 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `viewproperty`
--

LOCK TABLES `viewproperty` WRITE;
/*!40000 ALTER TABLE `viewproperty` DISABLE KEYS */;
INSERT INTO `viewproperty` VALUES (2479,'*.appointment.id','en','lava','core',NULL,'appointment','id','details','c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2009-03-31 20:35:39'),(2480,'*.appointment.calendar.name','en','lava','core',NULL,'appointment','calendar.name','details','c','string','No','Calendar',NULL,NULL,0,NULL,NULL,NULL,2,NULL,'2009-03-31 20:35:39'),(2481,'*.appointment.organizerId','en','lava','core','','appointment','organizerId','details','c','range','No','Organizer',NULL,NULL,NULL,'','appointment.organizer','',3,'','2009-03-31 20:35:39'),(2482,'*.appointment.type','en','lava','core',NULL,'appointment','type','details','i','range','No','Type',NULL,NULL,0,NULL,'appointment.type',NULL,4,NULL,'2009-03-31 20:35:39'),(2483,'*.appointment.description','en','lava','core',NULL,'appointment','description','details','i','text','No','Description',100,NULL,0,NULL,NULL,NULL,5,NULL,'2009-03-31 20:35:39'),(2484,'*.appointment.location','en','lava','core',NULL,'appointment','location','details','i','string','No','Location',100,NULL,0,NULL,NULL,NULL,6,NULL,'2009-03-31 20:35:39'),(2485,'*.appointment.startDate','en','lava','core','','appointment','startDate','details','i','date','Yes','Start Date',NULL,10,0,NULL,NULL,NULL,7,NULL,'2009-04-16 15:31:52'),(2486,'*.appointment.startTime','en','lava','core','','appointment','startTime','details','i','time','Yes','Time',NULL,NULL,0,'',NULL,'',8,'','2009-04-02 04:49:05'),(2487,'*.appointment.endDate','en','lava','core','','appointment','endDate','details','i','date','Yes','End Date',NULL,10,0,NULL,NULL,NULL,9,NULL,'2009-04-27 20:47:43'),(2488,'*.appointment.endTime','en','lava','core','','appointment','endTime','details','i','time','Yes','Time',NULL,NULL,0,NULL,NULL,NULL,10,NULL,'2009-04-16 15:30:06'),(2489,'*.appointment.status','en','lava','core',NULL,'appointment','status','details','i','range','No','Status',NULL,NULL,0,NULL,'resourceReservation.status',NULL,10,NULL,'2009-05-11 19:43:15'),(2490,'*.appointment.notes','en','lava','core',NULL,'appointment','notes','notes','i','unlimitedtext','No','Notes',NULL,NULL,0,'rows=\"10\" cols=\"45\"',NULL,NULL,11,NULL,'2009-03-31 20:35:39'),(2491,'*.appointment.organizer.userName','en','lava','core',NULL,'appointment','organizer.userName','details','c','string','No','Organizer',NULL,NULL,0,NULL,NULL,NULL,14,'Organizer Name','2009-06-11 17:00:00'),(2492,'*.appointment_change.type','en','lava','core',NULL,'appointment_change','type','details','c','range','Yes','Change Type',25,NULL,0,NULL,'resourceReservationChange.type',NULL,3,NULL,'2009-05-11 20:29:52'),(2493,'*.appointment_change.description','en','lava','core',NULL,'appointment_change','description','details','c','string','No','Description',NULL,NULL,0,NULL,NULL,NULL,4,NULL,'2009-05-11 20:29:52'),(2494,'*.appointment_change.changeBy.userName','en','lava','core',NULL,'appointment_change','changeBy.userName','details','c','string','Yes','Change By',NULL,NULL,0,NULL,NULL,NULL,5,NULL,'2009-05-11 20:29:52'),(2495,'*.appointment_change.changeTimestamp','en','lava','core',NULL,'appointment_change','changeTimestamp','details','c','timestamp','Yes','Change Timestamp',NULL,NULL,0,NULL,NULL,NULL,6,NULL,'2009-05-11 20:29:52'),(2496,'*.attendee.userId','en','lava','core',NULL,'attendee','userId','details','i','range','Yes','Attendee',NULL,NULL,0,NULL,'attendee.attendee',NULL,3,'Attendee','2009-06-03 20:40:16'),(2497,'*.attendee.role','en','lava','core',NULL,'attendee','role','details','i','range','Yes','Role',25,NULL,0,NULL,'attendee.role',NULL,4,'Attendee Role','2009-06-03 20:40:16'),(2498,'*.attendee.status','en','lava','core',NULL,'attendee','status','details','i','range','Yes','Status',25,NULL,0,NULL,'attendee.status',NULL,5,'Attendee Status','2009-06-03 20:40:16'),(2499,'*.attendee.notes','en','lava','core',NULL,'attendee','notes','details','i','test','No','Notes',100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,6,'Notes','2009-06-03 20:40:16'),(2500,'*.auditEntityHistory.id','en','lava','core',NULL,'auditEntityHistory','id',NULL,'c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-25 05:28:51'),(2501,'*.auditEntityHistory.entityId','en','lava','core',NULL,'auditEntityHistory','entityId',NULL,'c','numeric','Yes','Entity ID',NULL,NULL,0,NULL,NULL,NULL,3,'ID of the Entity','2009-01-25 05:28:51'),(2502,'*.auditEntityHistory.entity','en','lava','core',NULL,'auditEntityHistory','entity',NULL,'c','string','Yes','Entity',100,NULL,0,NULL,NULL,NULL,4,'Base entity name, e.g. Patient, Instrument (this is the entity name where the autoincrementing id field is defined)','2009-01-25 05:28:51'),(2503,'*.auditEntityHistory.entityType','en','lava','core',NULL,'auditEntityHistory','entityType',NULL,'c','string','No','Entity Type',100,NULL,0,NULL,NULL,NULL,5,'Optional subtype of the entity (e.g. MacPatient, BedsideScreen','2009-01-25 05:28:51'),(2504,'*.auditEntityHistory.auditType','en','lava','core',NULL,'auditEntityHistory','auditType',NULL,'c','string','Yes','Audit Type',10,NULL,0,NULL,NULL,NULL,6,'The type of auditing for the entity (e.g. CREATE, READ, UPDATE, DELETE)','2009-01-25 05:28:51'),(2505,'*.auditEventHistory.id','en','lava','core',NULL,'auditEventHistory','id',NULL,'c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-25 05:28:51'),(2506,'*.auditEventHistory.auditUser','en','lava','core',NULL,'auditEventHistory','auditUser',NULL,'c','string','Yes','Audit User',50,NULL,0,NULL,NULL,NULL,2,'The user who initiated the event','2009-01-25 05:28:51'),(2507,'*.auditEventHistory.auditHost','en','lava','core',NULL,'auditEventHistory','auditHost',NULL,'c','string','Yes','Audit Host',25,NULL,0,NULL,NULL,NULL,3,'The host (machine) that the event was initiated from','2009-01-25 05:28:51'),(2508,'*.auditEventHistory.auditTime','en','lava','core',NULL,'auditEventHistory','auditTime',NULL,'c','datetime','Yes','Audit Time',NULL,NULL,0,NULL,NULL,NULL,4,'The time that the event was initiated','2009-01-25 05:28:51'),(2509,'*.auditEventHistory.action','en','lava','core',NULL,'auditEventHistory','action',NULL,'c','string','Yes','Action',255,NULL,0,NULL,NULL,NULL,5,'The action id od the event','2009-01-25 05:28:51'),(2510,'*.auditEventHistory.actionEvent','en','lava','core',NULL,'auditEventHistory','actionEvent',NULL,'c','string','Yes','Action Event',50,NULL,0,NULL,NULL,NULL,6,'The event type of the event (e.g. add, view, delete, edit, list)','2009-01-25 05:28:51'),(2511,'*.auditEventHistory.actionIdParam','en','lava','core',NULL,'auditEventHistory','actionIdParam',NULL,'c','string','No','ID Param',50,NULL,0,NULL,NULL,NULL,7,'If an ID parameter was supplied for the event','2009-01-25 05:28:51'),(2512,'*.auditEventHistory.eventNote','en','lava','core',NULL,'auditEventHistory','eventNote',NULL,'c','text','No','Note',255,NULL,0,NULL,NULL,NULL,8,'An optional note field for the event','2009-01-25 05:28:51'),(2513,'*.auditEventHistory.exception','en','lava','core',NULL,'auditEventHistory','exception',NULL,'c','text','No','Exception',255,NULL,0,NULL,NULL,NULL,9,'If the event resulted in a handled exception','2009-01-25 05:28:51'),(2514,'*.auditEventHistory.exceptionMessage','en','lava','core',NULL,'auditEventHistory','exceptionMessage',NULL,'c','text','No','Exception Message',255,NULL,0,NULL,NULL,NULL,10,'The message associated with the handled exception.','2009-01-25 05:28:51'),(2515,'*.auditPropertyHistory.id','en','lava','core',NULL,'auditPropertyHistory','id',NULL,'c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-25 05:28:51'),(2516,'*.auditPropertyHistory.property','en','lava','core',NULL,'auditPropertyHistory','property',NULL,'c','string','Yes','Property Name',100,NULL,0,NULL,NULL,NULL,3,'The name of the entity property','2009-01-25 05:28:51'),(2517,'*.auditPropertyHistory.indexKey','en','lava','core',NULL,'auditPropertyHistory','indexKey',NULL,'c','string','No','Index Key Value',100,NULL,0,NULL,NULL,NULL,4,'If the property is a collection, the index into the collection for this particular subproperty value','2009-01-25 05:28:51'),(2518,'*.auditPropertyHistory.subproperty','en','lava','core',NULL,'auditPropertyHistory','subproperty',NULL,'c','string','No','Subproperty Name',255,NULL,0,NULL,NULL,NULL,5,'The name of the subproperty when theproperty is a collection','2009-01-25 05:28:51'),(2519,'*.auditPropertyHistory.oldValue','en','lava','core',NULL,'auditPropertyHistory','oldValue',NULL,'c','string','Yes','Old Value',255,NULL,0,NULL,NULL,NULL,6,'The old value or {CREATED} when the record is for a new value','2009-01-25 05:28:51'),(2520,'*.auditPropertyHistory.newValue','en','lava','core',NULL,'auditPropertyHistory','newValue',NULL,'c','string','Yes','New Value',255,NULL,0,NULL,NULL,NULL,7,'The new value or {DELETED} when the record is for a property deletion','2009-01-25 05:28:51'),(2521,'*.auditPropertyHistory.auditTimestamp','en','lava','core',NULL,'auditPropertyHistory','auditTimestamp',NULL,'c','timestamp','Yes','Audit Time',NULL,NULL,0,NULL,NULL,NULL,8,'The time of the event (copied from the Audit_Event table for convenience','2009-01-25 05:28:51'),(2522,'*.auditPropertyHistory.oldText','en','lava','core',NULL,'auditPropertyHistory','oldText',NULL,'c','unlimitedtext','Yes','Old Text Value',16,NULL,0,'rows=\"10\" cols=\"80\"',NULL,NULL,9,'The old text value or {CREATED} if the record if for a property creation','2009-01-25 05:28:51'),(2523,'*.auditPropertyHistory.newText','en','lava','core',NULL,'auditPropertyHistory','newText',NULL,'c','unlimitedtext','Yes','New Text Value',16,NULL,0,'rows=\"10\" cols=\"80\"',NULL,NULL,10,'The new text value of {DELETED} if the record is for a property deletion','2009-01-25 05:28:51'),(2524,'*.authGroup.id','en','lava','core',NULL,'authGroup','id','details','c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the authorization group','2009-01-25 05:28:51'),(2525,'*.authGroup.disabled','en','lava','core',NULL,'authGroup','disabled','status','i','range','Yes','Disabled',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,2,'Disabled','2009-01-25 05:28:51'),(2526,'*.authGroup.groupName','en','lava','core',NULL,'authGroup','groupName','details','i','string','Yes','Group Name',50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-25 05:28:51'),(2527,'*.authGroup.groupNameWithStatus','en','lava','core',NULL,'authGroup','groupNameWithStatus',NULL,'c','string','Yes','Group Name',50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-25 05:28:51'),(2528,'*.authGroup.effectiveDate','en','lava','core',NULL,'authGroup','effectiveDate','status','i','date','Yes','Effective Date',NULL,NULL,0,NULL,NULL,NULL,3,'Effective Date of the authorization group','2009-01-25 05:28:51'),(2529,'*.authGroup.expirationDate','en','lava','core',NULL,'authGroup','expirationDate','status','i','date','Yes','Expiration Date',NULL,NULL,0,NULL,NULL,NULL,4,'Expiration Date of the authorization group','2009-01-25 05:28:51'),(2530,'*.authGroup.notes','en','lava','core',NULL,'authGroup','notes','note','i','text','Yes','Notes',255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,5,'Notes about the authorization group','2009-01-25 05:28:51'),(2531,'*.authPermission.id','en','lava','core',NULL,'authPermission','id','details','c','numeric','Yes','Permission ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the permission','2009-01-25 05:28:51'),(2532,'*.authPermission.role.id','en','lava','core',NULL,'authPermission','role.id',NULL,'c','numeric','Yes','Role ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the role','2009-01-25 05:28:51'),(2533,'*.authPermission.role.roleName','en','lava','core',NULL,'authPermission','role.roleName',NULL,'i','string','Yes','Role Name',25,NULL,0,NULL,NULL,NULL,2,'Name of the role','2009-01-25 05:28:51'),(2534,'*.authPermission.roleId','en','lava','core',NULL,'authPermission','roleId','details','i','range','Yes','Role',25,NULL,0,NULL,'auth.roles',NULL,2,'The role that the permission applies to','2009-01-25 05:28:51'),(2535,'*.authPermission.permitDeny','en','lava','core',NULL,'authPermission','permitDeny','details','i','range','Yes','Permit / Deny',10,NULL,0,NULL,'authPermission.permitDeny',NULL,3,'Whether to PERMIT or DENY the permission to the role','2009-01-25 05:28:51'),(2536,'*.authPermission.scope','en','lava','core','','authPermission','scope','details','i','string','Yes','Scope',25,0,0,'','','',4,'Scope','2009-01-01 08:00:00'),(2537,'*.authPermission.module','en','lava','core',NULL,'authPermission','module','details','i','suggest','Yes','Module',25,NULL,0,NULL,NULL,NULL,5,'the moule that the permission covers','2009-01-25 05:28:51'),(2538,'*.authPermission.section','en','lava','core',NULL,'authPermission','section','details','i','suggest','Yes','Section',25,NULL,0,NULL,NULL,NULL,6,'the section that the permission covers','2009-01-25 05:28:51'),(2539,'*.authPermission.target','en','lava','core',NULL,'authPermission','target','details','i','suggest','Yes','Target',25,NULL,0,NULL,NULL,NULL,7,'the target that the permission covers','2009-01-25 05:28:51'),(2540,'*.authPermission.mode','en','lava','core',NULL,'authPermission','mode','details','i','suggest','Yes','Mode',25,NULL,0,NULL,NULL,NULL,8,'the mode that the permission covers','2009-01-25 05:28:51'),(2541,'*.authPermission.notes','en','lava','core',NULL,'authPermission','notes','note','i','text','No','Notes',100,NULL,0,'rows=\"2\" cols=\"40\"',NULL,NULL,9,'Notes','2009-01-25 05:28:51'),(2542,'*.authRole.id','en','lava','core',NULL,'authRole','id',NULL,'c','numeric','Yes','Role ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the role','2009-01-25 05:28:51'),(2543,'*.authRole.roleName','en','lava','core',NULL,'authRole','roleName',NULL,'i','string','Yes','Role Name',25,NULL,0,NULL,NULL,NULL,2,'Name of the role','2009-01-25 05:28:51'),(2544,'*.authRole.patientAccess','en','lava','core',NULL,'authRole','patientAccess',NULL,'i','range','Yes','Patient Access',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,3,'This role confers patient access to the user','2009-01-25 05:28:51'),(2545,'*.authRole.phiAccess','en','lava','core',NULL,'authRole','phiAccess',NULL,'i','range','Yes','PHI Access',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,4,'This role confers access to Protected Health Identifiers/Informantion','2009-01-25 05:28:51'),(2546,'*.authRole.ghiAccess','en','lava','core',NULL,'authRole','ghiAccess',NULL,'i','range','Yes','Genetic Access',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,5,'This role confers access to genetic health information to the user','2009-01-25 05:28:51'),(2547,'*.authRole.notes','en','lava','core',NULL,'authRole','notes',NULL,'i','text','No','Notes',255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,8,'Notes','2009-01-25 05:28:51'),(2548,'*.authRole.notes','en','lava','core',NULL,'authRole','notes',NULL,'i','text','No','Notes',255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,8,'Notes','2009-01-25 05:28:51'),(2549,'*.authRole.summaryInfo','en','lava','core',NULL,'authRole','summaryInfo','details','c','string','No','Summary',255,NULL,0,NULL,NULL,NULL,13,'Summary information for the role','2009-01-01 08:00:00'),(2550,'*.authUser.id','en','lava','core',NULL,'authUser','id',NULL,'c','numeric','Yes','User ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the user','2009-01-25 05:28:51'),(2551,'*.authUser.role.id','en','lava','core',NULL,'authUser','role.id','details','c','numeric','Yes','Role ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID of the role','2009-01-25 05:28:51'),(2552,'*.authUser.userName','en','lava','core',NULL,'authUser','userName','details','i','string','Yes','User Name',50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2553,'*.authUser.userNameWithStatus','en','lava','core',NULL,'authUser','userNameWithStatus',NULL,'c','string','Yes','User Name',50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name] with status','2009-01-25 05:28:51'),(2554,'filter.authUser.userName','en','lava','core','filter','authUser','userName',NULL,'i','string','No','User Name',50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2555,'*.authUser.login','en','lava','core',NULL,'authUser','login','details','i','string','No','Network Login',100,NULL,0,NULL,NULL,NULL,3,'Network Login for the user','2009-01-25 05:28:51'),(2556,'filter.authUser.login','en','lava','core','filter','authUser','login',NULL,'i','string','No','Network Login',100,NULL,0,NULL,NULL,NULL,3,'Network Login for the user','2009-01-25 05:28:51'),(2557,'*.authUser.email','en','lava','core',NULL,'authUser','email','details','i','text','No','Email',100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,4,'Email','2009-05-12 21:45:03'),(2558,'*.authUser.phone','en','lava','core',NULL,'authUser','phone','details','i','string','No','Phone',25,NULL,0,NULL,NULL,NULL,5,'Phone','2009-05-12 21:45:03'),(2559,'*.authUser.accessAgreementDate','en','lava','core',NULL,'authUser','accessAgreementDate','status','i','date','No','Access Agreement Date',NULL,NULL,0,NULL,NULL,NULL,7,'Date the user signed an Access Agreement for using the system','2009-01-25 05:28:51'),(2560,'filter.authUser.accessAgreementDateEnd','en','lava','core','filter','authUser','accessAgreementDateEnd',NULL,'i','date','No','and',NULL,NULL,0,NULL,NULL,NULL,7,'Date the user signed an Access Agreement for using the system','2009-01-25 05:28:51'),(2561,'filter.authUser.accessAgreementDateStart','en','lava','core','filter','authUser','accessAgreementDateStart',NULL,'i','date','No','Agreement Date Between',NULL,NULL,0,NULL,NULL,NULL,7,'Date the user signed an Access Agreement for using the system','2009-01-25 05:28:51'),(2562,'*.authUser.effectiveDate','en','lava','core',NULL,'authUser','effectiveDate','status','i','date','Yes','Effective Date',NULL,NULL,0,NULL,NULL,NULL,8,'Effective Date','2009-01-25 05:28:51'),(2563,'filter.authUser.effectiveDateEnd','en','lava','core','filter','authUser','effectiveDateEnd',NULL,'i','date','No','and',NULL,NULL,0,NULL,NULL,NULL,8,'Effective Date','2009-01-25 05:28:51'),(2564,'filter.authUser.effectiveDateStart','en','lava','core','filter','authUser','effectiveDateStart',NULL,'i','date','No','Effective Date Between',NULL,NULL,0,NULL,NULL,NULL,8,'Effective Date','2009-01-25 05:28:51'),(2565,'*.authUser.expirationDate','en','lava','core',NULL,'authUser','expirationDate','status','i','date','No','Expiration Date',NULL,NULL,0,NULL,NULL,NULL,9,'Expiration Date','2009-01-25 05:28:51'),(2566,'filter.authUser.expirationDateEnd','en','lava','core','filter','authUser','expirationDateEnd',NULL,'i','date','No','and',NULL,NULL,0,NULL,NULL,NULL,9,'Expiration Date','2009-01-25 05:28:51'),(2567,'filter.authUser.expirationDateStart','en','lava','core','filter','authUser','expirationDateStart',NULL,'i','date','No','Expiration Date Between',NULL,NULL,0,NULL,NULL,NULL,9,'Expiration Date','2009-01-25 05:28:51'),(2568,'*.authUser.shortUserName','en','lava','core',NULL,'authUser','shortUserName','details','c','string','No','Short User Name',53,NULL,0,NULL,NULL,NULL,10,'Shortened User name','2009-01-25 05:28:51'),(2569,'*.authUser.shortUserNameRev','en','lava','core',NULL,'authUser','shortUserNameRev','details','c','string','No','Short User Name Reversed',54,NULL,0,NULL,NULL,NULL,11,'Shortened User Name Reversed','2009-01-25 05:28:51'),(2570,'*.authUser.notes','en','lava','core',NULL,'authUser','notes','note','i','text','No','Notes',255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,12,'Notes','2009-01-25 05:28:51'),(2571,'*.authUser.authenticationType','en','lava','core',NULL,'authUser','authenticationType','details','i','range','Yes','Auth Type',10,NULL,0,NULL,'authUser.authenticationType',NULL,12,'Authentication Type','2009-05-12 21:45:03'),(2572,'*.authUser.disabled','en','lava','core',NULL,'authUser','disabled','status','i','range','Yes','Disabled',NULL,NULL,0,NULL,'generic.yesNoZero',NULL,13,'Disabled','2009-01-25 05:28:51'),(2573,'*.authUser.password','en','lava','core',NULL,'authUser','password','local','c','text','No','Password (hashed)',100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,13,'Password Hash','2009-05-12 21:45:03'),(2574,'*.authUser.passwordExpiration','en','lava','core',NULL,'authUser','passwordExpiration','local','c','timestamp','No','Password Exp.',NULL,NULL,0,NULL,NULL,NULL,14,'Password Expiration','2009-05-12 21:45:03'),(2575,'*.authUser.passwordResetToken','en','lava','core',NULL,'authUser','passwordResetToken','local','c','text','No','Reset Token',100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,15,'Password Reset Token','2009-05-12 21:45:03'),(2576,'*.authUser.passwordResetExpiration','en','lava','core',NULL,'authUser','passwordResetExpiration','local','c','timestamp','No','Reset Exp.',NULL,NULL,0,NULL,NULL,NULL,16,'Password Reset Token Expires','2009-05-12 21:45:03'),(2577,'*.authUser.failedLoginCount','en','lava','core',NULL,'authUser','failedLoginCount','local','c','numeric','No','Failed Logins',NULL,NULL,0,NULL,NULL,NULL,17,'Failed Login Attempts','2009-05-12 21:45:03'),(2578,'*.authUser.lastFailedLogin','en','lava','core',NULL,'authUser','lastFailedLogin','local','c','timestamp','No','Last Failed Login',NULL,NULL,0,NULL,NULL,NULL,18,'Last Failed Logon Attempt','2009-05-12 21:45:03'),(2579,'*.authUser.accountLocked','en','lava','core',NULL,'authUser','accountLocked','local','c','timestamp','No','Account Locked',NULL,NULL,0,NULL,NULL,NULL,19,'Account Locked Timestamp','2009-05-12 21:45:03'),(2580,'*.authUserGroup.group.id','en','lava','core',NULL,'authUserGroup','group.id',NULL,'c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the authorization group','2009-01-25 05:28:51'),(2581,'*.authUserGroup.id','en','lava','core',NULL,'authUserGroup','id',NULL,'c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the User Role Assocaition','2009-01-25 05:28:51'),(2582,'*.authUserGroup.user.id','en','lava','core',NULL,'authUserGroup','user.id','details','c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-25 05:28:51'),(2583,'*.authUserGroup.user.id','en','lava','core',NULL,'authUserGroup','user.id','details','c','numeric','Yes','User ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID of the User','2009-01-25 05:28:51'),(2584,'*.authUserGroup.group.groupNameWithStatus','en','lava','core',NULL,'authUserGroup','group.groupNameWithStatus',NULL,'i','string','Yes','Group Name',50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-25 05:28:51'),(2585,'*.authUserGroup.user.login','en','lava','core',NULL,'authUserGroup','user.login','details','i','string','Yes','User Name',50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2586,'*.authUserGroup.user.userNameWithStatus','en','lava','core',NULL,'authUserGroup','user.userNameWithStatus','details','i','string','Yes','User Name',50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2587,'*.authUserGroup.userId','en','lava','core',NULL,'authUserGroup','userId','details','i','range','Yes','User',NULL,NULL,0,NULL,'auth.users',NULL,2,'Unique ID of the User','2009-01-25 05:28:51'),(2588,'*.authUserGroup.groupId','en','lava','core',NULL,'authUserGroup','groupId','details','i','range','Yes','Group',NULL,NULL,0,NULL,'auth.groups',NULL,3,'Unique ID of the Group','2009-01-25 05:28:51'),(2589,'*.authUserGroup.notes','en','lava','core',NULL,'authUserGroup','notes','note','i','text','No','Notes',255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,6,'Notes','2009-01-25 05:28:51'),(2590,'*.authUserPasswordDto.oldPassword','en','lava','core',NULL,'authUserPasswordDto','oldPassword',NULL,'i','password','Yes','Current Password',NULL,NULL,0,NULL,NULL,NULL,1,'Current Password','2009-05-12 21:45:03'),(2591,'*.authUserPasswordDto.newPassword','en','lava','core',NULL,'authUserPasswordDto','newPassword',NULL,'i','password','Yes','New Password',NULL,NULL,0,NULL,NULL,NULL,2,'New Password','2009-05-12 21:45:03'),(2592,'*.authUserPasswordDto.newPasswordConfirm','en','lava','core',NULL,'authUserPasswordDto','newPasswordConfirm',NULL,'i','password','Yes','New Password Confirm',NULL,NULL,0,NULL,NULL,NULL,3,'New Password Confirm','2009-05-12 21:45:03'),(2593,'*.authUserRole.group.id','en','lava','core',NULL,'authUserRole','group.id',NULL,'c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the authorization group','2009-01-25 05:28:51'),(2594,'*.authUserRole.id','en','lava','core',NULL,'authUserRole','id','details','c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the User/Group Role Association','2009-01-25 05:28:51'),(2595,'*.authUserRole.role.id','en','lava','core',NULL,'authUserRole','role.id',NULL,'c','numeric','Yes','Role ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the role','2009-01-25 05:28:51'),(2596,'*.authUserRole.user.id','en','lava','core',NULL,'authUserRole','user.id',NULL,'c','numeric','Yes','User ID',NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID of the User','2009-01-25 05:28:51'),(2597,'*.authUserRole.group.groupNameWithStatus','en','lava','core',NULL,'authUserRole','group.groupNameWithStatus',NULL,'i','string','Yes','Group Name',50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-25 05:28:51'),(2598,'*.authUserRole.role.roleName','en','lava','core',NULL,'authUserRole','role.roleName',NULL,'i','string','Yes','Role Name',25,NULL,0,NULL,NULL,NULL,2,'Name of the role','2009-01-25 05:28:51'),(2599,'*.authUserRole.user.userNameWithStatus','en','lava','core',NULL,'authUserRole','user.userNameWithStatus',NULL,'i','string','Yes','User Name',50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2600,'*.authUserRole.role.summaryInfo','en','lava','core',NULL,'authUserRole','role.summaryInfo',NULL,'i','range','Yes','Summary',255,NULL,0,NULL,NULL,NULL,3,'Summary info about the role','2009-01-25 05:28:51'),(2601,'*.authUserRole.roleId','en','lava','core',NULL,'authUserRole','roleId','details','i','range','Yes','Role',25,NULL,0,NULL,'auth.roles',NULL,3,'The role name','2009-01-25 05:28:51'),(2602,'*.crmsAuthUserRole.unit','en','lava','core',NULL,'authUserRole','unit','details','i','suggest','No','Unit/Site',25,NULL,0,NULL,'projectUnit.units',NULL,5,'The program that the role applies to (* for any)','2009-01-25 05:28:51'),(2603,'*.authUserRole.userId','en','lava','core',NULL,'authUserRole','userId','details','i','range','No','User',NULL,NULL,0,NULL,'auth.users',NULL,7,'The user to assign the role to','2009-01-25 05:28:51'),(2604,'*.authUserRole.groupId','en','lava','core',NULL,'authUserRole','groupId','details','i','range','No','Group',NULL,NULL,0,NULL,'auth.groups',NULL,8,'the group to assign the role to','2009-01-25 05:28:51'),(2605,'*.authUserRole.notes','en','lava','core',NULL,'authUserRole','notes','note','i','text','No','Notes',255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,12,'Notes','2009-01-25 05:28:51'),(2606,'*.authUserRole.summaryInfo','en','lava','core','','authUserRole','summaryInfo','details','c','string','No','Summary',255,NULL,0,NULL,NULL,'',13,'Summary information for the role assignment','2009-01-01 08:00:00'),(2607,'*.calendar.id','en','lava','core',NULL,'calendar','id','details','c','numeric','Yes','ID',NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2009-03-31 17:56:31'),(2608,'*.calendar.type','en','lava','core','','calendar','type','details','c','range','Yes','Type',25,NULL,0,NULL,'calendar.type','',2,'Type','2009-06-10 07:00:00'),(2609,'*.calendar.name','en','lava','core',NULL,'calendar','name','details','i','string','Yes','Name',100,NULL,0,NULL,NULL,NULL,3,NULL,'2009-03-31 17:56:31'),(2610,'*.calendar.description','en','lava','core',NULL,'calendar','description','details','i','text','No','Description',255,NULL,0,'rows=\"4\" cols=\"45\"',NULL,NULL,4,NULL,'2009-03-31 17:56:31'),(2611,'*.calendar.notes','en','lava','core',NULL,'calendar','notes','notes','i','unlimitedtext','No','Notes',NULL,NULL,0,'rows=\"10\" cols=\"45\"',NULL,NULL,5,NULL,'2009-03-31 17:56:31'),(2612,'*.hibernateProperty.id','en','lava','core',NULL,'HibernateProperty','id','','','','Yes','',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2613,'*.hibernateProperty.scope','en','lava','core',NULL,'HibernateProperty','scope','','','','Yes','',NULL,NULL,0,'','',NULL,2,'','2009-01-25 05:28:51'),(2614,'*.hibernateProperty.entity','en','lava','core',NULL,'HibernateProperty','entity','','','','Yes','',NULL,NULL,0,'','',NULL,3,'','2009-01-25 05:28:51'),(2615,'*.hibernateProperty.property','en','lava','core',NULL,'HibernateProperty','property','','','','Yes','',NULL,NULL,0,'','',NULL,4,'','2009-01-25 05:28:51'),(2616,'*.hibernateProperty.dbTable','en','lava','core',NULL,'HibernateProperty','dbTable','','','','Yes','',NULL,NULL,0,'','',NULL,5,'','2009-01-25 05:28:51'),(2617,'*.hibernateProperty.dbColumn','en','lava','core',NULL,'HibernateProperty','dbColumn','','','','Yes','',NULL,NULL,0,'','',NULL,6,'','2009-01-25 05:28:51'),(2618,'*.hibernateProperty.dbType','en','lava','core',NULL,'HibernateProperty','dbType','','','','No','',NULL,NULL,0,'','',NULL,7,'','2009-01-25 05:28:51'),(2619,'*.hibernateProperty.dbLength','en','lava','core',NULL,'HibernateProperty','dbLength','','','','No','',NULL,NULL,0,'','',NULL,8,'','2009-01-25 05:28:51'),(2620,'*.hibernateProperty.dbPrecision','en','lava','core',NULL,'HibernateProperty','dbPrecision','','','','No','',NULL,NULL,0,'','',NULL,9,'','2009-01-25 05:28:51'),(2621,'*.hibernateProperty.dbScale','en','lava','core',NULL,'HibernateProperty','dbScale','','','','No','',NULL,NULL,0,'','',NULL,10,'','2009-01-25 05:28:51'),(2622,'*.hibernateProperty.dbOrder','en','lava','core',NULL,'HibernateProperty','dbOrder','','','','No','',NULL,NULL,0,'','',NULL,11,'','2009-01-25 05:28:51'),(2623,'*.hibernateProperty.hibernateProperty','en','lava','core',NULL,'HibernateProperty','hibernateProperty','','','','No','',NULL,NULL,0,'','',NULL,12,'','2009-01-25 05:28:51'),(2624,'*.hibernateProperty.hibernateType','en','lava','core',NULL,'HibernateProperty','hibernateType','','','','No','',NULL,NULL,0,'','',NULL,13,'','2009-01-25 05:28:51'),(2625,'*.hibernateProperty.hibernateClass','en','lava','core',NULL,'HibernateProperty','hibernateClass','','','','No','',NULL,NULL,0,'','',NULL,14,'','2009-01-25 05:28:51'),(2626,'*.hibernateProperty.hibernateNotNull','en','lava','core',NULL,'HibernateProperty','hibernateNotNull','','','','No','',NULL,NULL,0,'','',NULL,15,'','2009-01-25 05:28:51'),(2627,'*.lavaSession.id','en','lava','core',NULL,'LavaSession','id',NULL,'c','numeric','Yes','Session ID',NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2009-01-25 05:28:51'),(2628,'*.lavaSession.serverInstanceId','en','lava','core',NULL,'LavaSession','serverInstanceId',NULL,'c','numeric','Yes','Server Instance ID',NULL,NULL,0,NULL,NULL,NULL,2,NULL,'2009-01-25 05:28:51'),(2629,'*.lavaSession.createTimestamp','en','lava','core',NULL,'LavaSession','createTimestamp',NULL,'c','datetime','Yes','Created Time',NULL,NULL,0,NULL,NULL,NULL,3,NULL,'2009-01-25 05:28:51'),(2630,'*.lavaSession.accessTimestamp','en','lava','core',NULL,'LavaSession','accessTimestamp',NULL,'c','datetime','Yes','Accessed Time',NULL,NULL,0,NULL,NULL,NULL,4,NULL,'2009-01-25 05:28:51'),(2631,'*.lavaSession.expireTimestamp','en','lava','core',NULL,'LavaSession','expireTimestamp',NULL,'c','datetime','Yes','Expiration Time',NULL,NULL,0,NULL,NULL,NULL,5,NULL,'2009-01-25 05:28:51'),(2632,'*.lavaSession.currentStatus','en','lava','core',NULL,'LavaSession','currentStatus',NULL,'c','range','Yes','Current Status',NULL,NULL,0,NULL,'lavaSession.status',NULL,6,NULL,'2009-01-25 05:28:51'),(2633,'*.lavaSession.userId','en','lava','core',NULL,'LavaSession','userId',NULL,'c','numeric','No','User ID',NULL,NULL,0,NULL,NULL,NULL,7,NULL,'2009-01-25 05:28:51'),(2634,'*.lavaSession.username','en','lava','core',NULL,'LavaSession','username',NULL,'c','string','No','Username',NULL,NULL,0,NULL,NULL,NULL,8,NULL,'2009-01-25 05:28:51'),(2635,'*.lavaSession.hostname','en','lava','core',NULL,'LavaSession','hostname',NULL,'c','string','No','Hostname',NULL,NULL,0,NULL,NULL,NULL,9,NULL,'2009-01-25 05:28:51'),(2636,'*.lavaSession.httpSessionId','en','lava','core',NULL,'LavaSession','httpSessionId',NULL,'c','string','No','HTTP Session',NULL,40,0,NULL,NULL,NULL,10,NULL,'2009-01-25 05:28:51'),(2637,'*.lavaSession.disconnectTime','en','lava','core',NULL,'LavaSession','disconnectTime',NULL,'i','time','No','Time',NULL,NULL,0,NULL,NULL,NULL,11,NULL,'2009-01-25 05:28:51'),(2638,'*.lavaSession.disconnectMessage','en','lava','core',NULL,'LavaSession','disconnectMessage',NULL,'i','text','No','Disconnect Message',NULL,NULL,0,'rows=\"4\", cols=\"45\"',NULL,NULL,12,NULL,'2009-01-25 05:28:51'),(2639,'*.lavaSession.notes','en','lava','core',NULL,'LavaSession','notes',NULL,'i','text','No','Notes',NULL,NULL,0,'rows=\"4\" cols=\"45\"',NULL,NULL,13,NULL,'2009-01-25 05:28:51'),(2640,'*.lavaSession.disconnectDate','en','lava','core',NULL,'lavaSession','disconnectDate',NULL,'i','date','No','Disconnect Date',10,NULL,0,NULL,NULL,NULL,16,NULL,'2009-04-27 22:11:44'),(2641,'*.reservation.organizer.userName','en','lava','core',NULL,'reservation','organizer.userName','details','c','string','No','Reserved By',NULL,NULL,0,NULL,NULL,NULL,14,'Reserved By','2009-06-11 16:40:26'),(2642,'*.reservation.organizerId','en','lava','core',NULL,'reservation','organizerId','details','i','range','Yes','Reserved By',NULL,NULL,0,NULL,'appointment.organizer',NULL,15,'Reserved By','2009-06-11 16:37:29'),(2643,'*.resourceCalendar.resourceType','en','lava','core',NULL,'resourceCalendar','resource_type','resource','i','range','Yes','Resource Type',25,NULL,0,NULL,'resourceCalendar.resourceType',NULL,2,'Resource Type','2009-06-03 17:27:39'),(2644,'*.resourceCalendar.location','en','lava','core',NULL,'resourceCalendar','location','resource','i','text','No','Location',100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,3,'Location','2009-06-03 17:27:39'),(2645,'*.resourceCalendar.contactId','en','lava','core',NULL,'resourceCalendar','contactId','resource','i','range','Yes','Contact',NULL,NULL,0,NULL,'resourceCalendar.contact',NULL,4,'Contact','2009-06-03 17:27:39'),(2646,'*.resourceCalendar.contact.email','en','lava','core',NULL,'resourceCalendar','contact.email','resource','c','string','No','Contact Email',100,NULL,0,NULL,NULL,NULL,6,'Contact Email','2009-06-03 17:27:39'),(2647,'*.resourceCalendar.contact.phone','en','lava','core',NULL,'resourceCalendar','contact.phone','resource','c','string','No','Contact Phone',25,NULL,0,NULL,NULL,NULL,7,'Contact Phone','2009-06-03 17:27:39'),(2648,'*.userPreferences.email','en','lava','core',NULL,'userPreferences','email','details','i','text','No','Email',100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,1,'Email','2009-05-26 23:48:36'),(2649,'*.userPreferences.phone','en','lava','core',NULL,'userPreferences','phone','details','i','string','No','Phone',25,NULL,0,NULL,NULL,NULL,2,'Phone','2009-05-26 23:49:22'),(2650,'*.viewProperty.id','en','lava','core',NULL,'ViewProperty','id','','','','Yes','',NULL,NULL,0,'','',NULL,1,'','2009-01-25 05:28:51'),(2651,'*.viewProperty.messageCode','en','lava','core',NULL,'ViewProperty','messageCode','','','','No','',NULL,NULL,0,'','',NULL,2,'','2009-01-25 05:28:51'),(2652,'*.viewProperty.locale','en','lava','core',NULL,'ViewProperty','locale','','','','Yes','',NULL,NULL,0,'','',NULL,3,'','2009-01-25 05:28:51'),(2653,'*.viewProperty.scope','en','lava','core',NULL,'ViewProperty','scope','','','','Yes','',NULL,NULL,0,'','',NULL,4,'','2009-01-25 05:28:51'),(2654,'*.viewProperty.prefix','en','lava','core',NULL,'ViewProperty','prefix','','','','No','',NULL,NULL,0,'','',NULL,5,'','2009-01-25 05:28:51'),(2655,'*.viewProperty.entity','en','lava','core',NULL,'ViewProperty','entity','','','','Yes','',NULL,NULL,0,'','',NULL,6,'','2009-01-25 05:28:51'),(2656,'*.viewProperty.property','en','lava','core',NULL,'ViewProperty','property','','','','Yes','',NULL,NULL,0,'','',NULL,7,'','2009-01-25 05:28:51'),(2657,'*.viewProperty.section','en','lava','core',NULL,'ViewProperty','section','','','','No','',NULL,NULL,0,'','',NULL,8,'','2009-01-25 05:28:51'),(2658,'*.viewProperty.context','en','lava','core',NULL,'ViewProperty','context','','','','No','',NULL,NULL,0,'','',NULL,9,'','2009-01-25 05:28:51'),(2659,'*.viewProperty.style','en','lava','core',NULL,'ViewProperty','style','','','','No','',NULL,NULL,0,'','',NULL,10,'','2009-01-25 05:28:51'),(2660,'*.viewProperty.list','en','lava','core',NULL,'ViewProperty','list','','','','No','',NULL,NULL,0,'','',NULL,11,'','2009-01-25 05:28:51'),(2661,'*.viewProperty.attributes','en','lava','core',NULL,'ViewProperty','attributes','','','','No','',NULL,NULL,0,'','',NULL,12,'','2009-01-25 05:28:51'),(2662,'*.viewProperty.required','en','lava','core',NULL,'ViewProperty','required','','','','No','',NULL,NULL,0,'','',NULL,13,'','2009-01-25 05:28:51'),(2663,'*.viewProperty.label','en','lava','core',NULL,'ViewProperty','label','','','','No','',NULL,NULL,0,'','',NULL,14,'','2009-01-25 05:28:51'),(2664,'*.viewProperty.quickHelp','en','lava','core',NULL,'ViewProperty','quickHelp','','','','No','',NULL,NULL,0,'','',NULL,15,'','2009-01-25 05:28:51'),(2665,'*.viewProperty.propOrder','en','lava','core',NULL,'ViewProperty','propOrder','','','','No','',NULL,NULL,0,'','',NULL,16,'','2009-01-25 05:28:51');
/*!40000 ALTER TABLE `viewproperty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `audit_entity`
--

/*!50001 DROP TABLE `audit_entity`*/;
/*!50001 DROP VIEW IF EXISTS `audit_entity`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013  SQL SECURITY DEFINER */
/*!50001 VIEW `audit_entity` AS select `audit_entity_work`.`audit_entity_id` AS `audit_entity_id`,`audit_entity_work`.`audit_event_id` AS `audit_event_id`,`audit_entity_work`.`entity_id` AS `entity_id`,`audit_entity_work`.`entity` AS `entity`,`audit_entity_work`.`entity_type` AS `entity_type`,`audit_entity_work`.`audit_type` AS `audit_type`,`audit_entity_work`.`modified` AS `modified` from `audit_entity_work` union all select `audit_entity_history`.`audit_entity_id` AS `audit_entity_id`,`audit_entity_history`.`audit_event_id` AS `audit_event_id`,`audit_entity_history`.`entity_id` AS `entity_id`,`audit_entity_history`.`entity` AS `entity`,`audit_entity_history`.`entity_type` AS `entity_type`,`audit_entity_history`.`audit_type` AS `audit_type`,`audit_entity_history`.`modified` AS `modified` from `audit_entity_history` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `audit_event`
--

/*!50001 DROP TABLE `audit_event`*/;
/*!50001 DROP VIEW IF EXISTS `audit_event`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013  SQL SECURITY DEFINER */
/*!50001 VIEW `audit_event` AS select `audit_event_work`.`audit_event_id` AS `audit_event_id`,`audit_event_work`.`audit_user` AS `audit_user`,`audit_event_work`.`audit_host` AS `audit_host`,`audit_event_work`.`audit_timestamp` AS `audit_timestamp`,`audit_event_work`.`action` AS `action`,`audit_event_work`.`action_event` AS `action_event`,`audit_event_work`.`action_id_param` AS `action_id_param`,`audit_event_work`.`event_note` AS `event_note`,`audit_event_work`.`exception` AS `exception`,`audit_event_work`.`exception_message` AS `exception_message`,`audit_event_work`.`modified` AS `modified` from `audit_event_work` union all select `audit_event_history`.`audit_event_id` AS `audit_event_id`,`audit_event_history`.`audit_user` AS `audit_user`,`audit_event_history`.`audit_host` AS `audit_host`,`audit_event_history`.`audit_timestamp` AS `audit_timestamp`,`audit_event_history`.`action` AS `action`,`audit_event_history`.`action_event` AS `action_event`,`audit_event_history`.`action_id_param` AS `action_id_param`,`audit_event_history`.`event_note` AS `event_note`,`audit_event_history`.`exception` AS `exception`,`audit_event_history`.`exception_message` AS `exception_message`,`audit_event_history`.`modified` AS `modified` from `audit_event_history` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `audit_property`
--

/*!50001 DROP TABLE `audit_property`*/;
/*!50001 DROP VIEW IF EXISTS `audit_property`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013  SQL SECURITY DEFINER */
/*!50001 VIEW `audit_property` AS select `audit_property_work`.`audit_property_id` AS `audit_property_id`,`audit_property_work`.`audit_entity_id` AS `audit_entity_id`,`audit_property_work`.`property` AS `property`,`audit_property_work`.`index_key` AS `index_key`,`audit_property_work`.`subproperty` AS `subproperty`,`audit_property_work`.`old_value` AS `old_value`,`audit_property_work`.`new_value` AS `new_value`,`audit_property_work`.`audit_timestamp` AS `audit_timestamp`,`audit_property_work`.`modified` AS `modified` from `audit_property_work` union all select `audit_property_history`.`audit_property_id` AS `audit_property_id`,`audit_property_history`.`audit_entity_id` AS `audit_entity_id`,`audit_property_history`.`property` AS `property`,`audit_property_history`.`index_key` AS `index_key`,`audit_property_history`.`subproperty` AS `subproperty`,`audit_property_history`.`old_value` AS `old_value`,`audit_property_history`.`new_value` AS `new_value`,`audit_property_history`.`audit_timestamp` AS `audit_timestamp`,`audit_property_history`.`modified` AS `modified` from `audit_property_history` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `audit_text`
--

/*!50001 DROP TABLE `audit_text`*/;
/*!50001 DROP VIEW IF EXISTS `audit_text`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013  SQL SECURITY DEFINER */
/*!50001 VIEW `audit_text` AS select `audit_text_history`.`audit_property_id` AS `audit_property_id`,`audit_text_history`.`old_text` AS `old_text`,`audit_text_history`.`new_text` AS `new_text` from `audit_text_history` union all select `audit_text_work`.`audit_property_id` AS `audit_property_id`,`audit_text_work`.`old_text` AS `old_text`,`audit_text_work`.`new_text` AS `new_text` from `audit_text_work` */;
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

-- Dump completed on 2009-07-02 22:57:28
