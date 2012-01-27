-- MySQL dump 10.11
--
-- Host: localhost    Database: lava_crms
-- ------------------------------------------------------
-- Server version	5.0.56sp1-enterprise-gpl-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `appointment` (
  `appointment_id` int(10) unsigned NOT NULL auto_increment,
  `calendar_id` int(10) unsigned NOT NULL,
  `organizer_id` int(11) NOT NULL,
  `type` varchar(25) NOT NULL,
  `description` varchar(100) default NULL,
  `location` varchar(100) default NULL,
  `start_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_date` date NOT NULL,
  `end_time` time NOT NULL,
  `status` varchar(25) default NULL,
  `notes` text,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`appointment_id`),
  KEY `appointment__calendar` (`calendar_id`),
  CONSTRAINT `appointment__calendar` FOREIGN KEY (`calendar_id`) REFERENCES `calendar` (`calendar_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attendee`
--

DROP TABLE IF EXISTS `attendee`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `attendee` (
  `attendee_id` int(10) unsigned NOT NULL auto_increment,
  `appointment_id` int(10) unsigned NOT NULL,
  `user_id` int(10) NOT NULL,
  `role` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  `notes` varchar(100) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`attendee_id`),
  KEY `attendee__appointment` (`appointment_id`),
  KEY `attendee__user_id` (`user_id`),
  CONSTRAINT `attendee__appointment` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`appointment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `attendee__user_id` FOREIGN KEY (`user_id`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
/*!50001 CREATE TABLE `audit_entity` (
  `audit_entity_id` int(11),
  `audit_event_id` int(11),
  `entity_id` int(11),
  `entity` varchar(100),
  `entity_type` varchar(100),
  `audit_type` varchar(10),
  `modified` timestamp
) */;

--
-- Table structure for table `audit_entity_history`
--

DROP TABLE IF EXISTS `audit_entity_history`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `audit_entity_history` (
  `audit_entity_id` int(10) NOT NULL auto_increment,
  `audit_event_id` int(10) NOT NULL,
  `entity_id` int(10) NOT NULL,
  `entity` varchar(100) NOT NULL,
  `entity_type` varchar(100) NOT NULL,
  `audit_type` varchar(10) NOT NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`audit_entity_id`),
  KEY `audit_entity_history__audit_event_id` (`audit_event_id`),
  CONSTRAINT `audit_entity_history__audit_event_id` FOREIGN KEY (`audit_event_id`) REFERENCES `audit_event_history` (`audit_event_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `audit_entity_work` (
  `audit_entity_id` int(10) NOT NULL auto_increment,
  `audit_event_id` int(10) NOT NULL,
  `entity_id` int(10) NOT NULL,
  `entity` varchar(100) NOT NULL,
  `entity_type` varchar(100) default NULL,
  `audit_type` varchar(10) NOT NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`audit_entity_id`),
  KEY `audit_entity_work__audit_event_id` (`audit_event_id`),
  CONSTRAINT `audit_entity_work__audit_event_id` FOREIGN KEY (`audit_event_id`) REFERENCES `audit_event_work` (`audit_event_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
) */;

--
-- Table structure for table `audit_event_history`
--

DROP TABLE IF EXISTS `audit_event_history`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `audit_event_history` (
  `audit_event_id` int(10) NOT NULL auto_increment,
  `audit_user` varchar(50) NOT NULL,
  `audit_host` varchar(25) NOT NULL,
  `audit_timestamp` timestamp NULL default NULL,
  `action` varchar(255) NOT NULL,
  `action_event` varchar(50) NOT NULL,
  `action_id_param` varchar(50) default NULL,
  `event_note` varchar(255) default NULL,
  `exception` varchar(255) default NULL,
  `exception_message` varchar(255) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`audit_event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `audit_event_work` (
  `audit_event_id` int(10) NOT NULL auto_increment,
  `audit_user` varchar(50) NOT NULL,
  `audit_host` varchar(25) NOT NULL,
  `audit_timestamp` timestamp NULL default NULL,
  `action` varchar(255) NOT NULL,
  `action_event` varchar(50) NOT NULL,
  `action_id_param` varchar(50) default NULL,
  `event_note` varchar(255) default NULL,
  `exception` varchar(255) default NULL,
  `exception_message` varchar(255) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`audit_event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1071 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
) */;

--
-- Table structure for table `audit_property_history`
--

DROP TABLE IF EXISTS `audit_property_history`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `audit_property_history` (
  `audit_property_id` int(10) NOT NULL auto_increment,
  `audit_entity_id` int(10) NOT NULL,
  `property` varchar(100) NOT NULL,
  `index_key` varchar(100) default NULL,
  `subproperty` varchar(255) default NULL,
  `old_value` varchar(255) NOT NULL,
  `new_value` varchar(255) NOT NULL,
  `audit_timestamp` timestamp NULL default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`audit_property_id`),
  KEY `audit_property_history__audit_entity_id` (`audit_entity_id`),
  CONSTRAINT `audit_property_history__audit_entity_id` FOREIGN KEY (`audit_entity_id`) REFERENCES `audit_entity_history` (`audit_entity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `audit_property_work` (
  `audit_property_id` int(10) NOT NULL auto_increment,
  `audit_entity_id` int(10) NOT NULL,
  `property` varchar(100) NOT NULL,
  `index_key` varchar(100) default NULL,
  `subproperty` varchar(255) default NULL,
  `old_value` varchar(255) NOT NULL,
  `new_value` varchar(255) NOT NULL,
  `audit_timestamp` timestamp NULL default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`audit_property_id`),
  KEY `audit_property_work__audit_entity_id` (`audit_entity_id`),
  CONSTRAINT `audit_property_work__audit_entity_id` FOREIGN KEY (`audit_entity_id`) REFERENCES `audit_entity_work` (`audit_entity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=328 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
/*!50001 CREATE TABLE `audit_text` (
  `audit_property_id` int(11),
  `old_text` text,
  `new_text` text
) */;

--
-- Table structure for table `audit_text_history`
--

DROP TABLE IF EXISTS `audit_text_history`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `audit_text_history` (
  `audit_property_id` int(10) NOT NULL,
  `old_text` text,
  `new_text` text,
  PRIMARY KEY  (`audit_property_id`),
  KEY `audit_text_history__audit_property_id` (`audit_property_id`),
  CONSTRAINT `audit_text_history__audit_property_id` FOREIGN KEY (`audit_property_id`) REFERENCES `audit_property_history` (`audit_property_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `audit_text_work` (
  `audit_property_id` int(10) NOT NULL,
  `old_text` text,
  `new_text` text,
  PRIMARY KEY  (`audit_property_id`),
  KEY `audit_text_work__audit_property_id` (`audit_property_id`),
  CONSTRAINT `audit_text_work__audit_property_id` FOREIGN KEY (`audit_property_id`) REFERENCES `audit_property_work` (`audit_property_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `authgroup` (
  `GID` int(10) NOT NULL auto_increment,
  `GroupName` varchar(50) NOT NULL,
  `EffectiveDate` date NOT NULL,
  `ExpirationDate` date default NULL,
  `Notes` varchar(255) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`GID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `authgroup`
--

LOCK TABLES `authgroup` WRITE;
/*!40000 ALTER TABLE `authgroup` DISABLE KEYS */;
INSERT INTO `authgroup` (`GID`, `GroupName`, `EffectiveDate`, `ExpirationDate`, `Notes`, `modified`) VALUES (1,'Admins','2012-01-01',NULL,'Admins (can perform all actions)','2012-01-01 00:00:00'),(2,'Coordinators','2012-01-01',NULL,'Coordinators (can perform all non-admin actions in any project)','2012-01-01 00:00:00');
/*!40000 ALTER TABLE `authgroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authpermission`
--

DROP TABLE IF EXISTS `authpermission`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `authpermission` (
  `PermID` int(10) NOT NULL auto_increment,
  `RoleID` int(10) NOT NULL,
  `PermitDeny` varchar(10) NOT NULL,
  `Scope` varchar(50) NOT NULL,
  `Module` varchar(50) NOT NULL,
  `Section` varchar(50) NOT NULL,
  `Target` varchar(50) NOT NULL,
  `Mode` varchar(25) NOT NULL,
  `Notes` varchar(100) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`PermID`),
  KEY `authpermission_RoleID` (`RoleID`),
  CONSTRAINT `authpermission_RoleID` FOREIGN KEY (`RoleID`) REFERENCES `authrole` (`RoleID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `authpermission`
--

LOCK TABLES `authpermission` WRITE;
/*!40000 ALTER TABLE `authpermission` DISABLE KEYS */;
INSERT INTO `authpermission` (`PermID`, `RoleID`, `PermitDeny`, `Scope`, `Module`, `Section`, `Target`, `Mode`, `Notes`, `modified`) VALUES (6,1,'PERMIT','*','*','*','*','*','permits access to all modules','2012-01-01 00:00:00'),(9,2,'PERMIT','*','*','*','*','*','permits access to all modules','2012-01-01 00:00:00'),(39,-1,'DENY','core','admin','*','*','*','Restricts access to admin module to all users by default','2012-01-01 00:00:00'),(50,1,'PERMIT','core','admin','*','*','*','Allows admins to access the admin module.','2012-01-01 00:00:00');
/*!40000 ALTER TABLE `authpermission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authrole`
--

DROP TABLE IF EXISTS `authrole`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `authrole` (
  `RoleID` int(10) NOT NULL auto_increment,
  `RoleName` varchar(25) NOT NULL,
  `Notes` varchar(255) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`RoleID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `authrole`
--

LOCK TABLES `authrole` WRITE;
/*!40000 ALTER TABLE `authrole` DISABLE KEYS */;
INSERT INTO `authrole` (`RoleID`, `RoleName`, `Notes`, `modified`) VALUES (-1,'DEFAULT PERMISSIONS','This role groups together default permissions that apply to all roles','2012-01-01 00:00:00'),(1,'SYSTEM ADMIN','System Admin: staff who need full access to administrative functionality and read only access to data.','2012-01-01 00:00:00'),(2,'COORDINATOR','Project Coordinators: staff with responsibility for recruitment, enrollment, scheduling, assessment, and project administration','2012-01-01 00:00:00'),(3,'PATIENT ACCESS DENIED','Removes the projects/units matched by the role assignment from the list of projects that give the user patient access privileges.  This does remove access to project data where the user already has access to the patient.','2012-01-01 00:00:00'),(4,'PROJECT ACCESS DENIED','Access is not granted to patients (or project data) matched by this role assignment.  If user has access to patient from another role, they will have patient access, but not access to project/unit data matched by this role.','2012-01-01 00:00:00');
/*!40000 ALTER TABLE `authrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authuser`
--

DROP TABLE IF EXISTS `authuser`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `authuser` (
  `UID` int(10) NOT NULL auto_increment,
  `UserName` varchar(50) NOT NULL,
  `Login` varchar(100) default NULL,
  `email` varchar(100) default NULL,
  `phone` varchar(25) default NULL,
  `AccessAgreementDate` date default NULL,
  `ShortUserName` varchar(50) default NULL,
  `ShortUserNameRev` varchar(50) default NULL,
  `EffectiveDate` date NOT NULL,
  `ExpirationDate` date default NULL,
  `Notes` varchar(255) default NULL,
  `authenticationType` varchar(10) default 'EXTERNAL',
  `password` varchar(100) default NULL,
  `passwordExpiration` timestamp NULL default NULL,
  `passwordResetToken` varchar(100) default NULL,
  `passwordResetExpiration` timestamp NULL default NULL,
  `failedLoginCount` smallint(6) default NULL,
  `lastFailedLogin` timestamp NULL default NULL,
  `accountLocked` timestamp NULL default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`UID`),
  UNIQUE KEY `Unique_UserName` (`UserName`),
  UNIQUE KEY `Unique_Login` (`Login`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `authuser`
--

LOCK TABLES `authuser` WRITE;
/*!40000 ALTER TABLE `authuser` DISABLE KEYS */;
INSERT INTO `authuser` (`UID`, `UserName`, `Login`, `email`, `phone`, `AccessAgreementDate`, `ShortUserName`, `ShortUserNameRev`, `EffectiveDate`, `ExpirationDate`, `Notes`, `authenticationType`, `password`, `passwordExpiration`, `passwordResetToken`, `passwordResetExpiration`, `failedLoginCount`, `lastFailedLogin`, `accountLocked`, `modified`) VALUES (1,'Admin','admin','admin@admin.org','555-555-5555','2009-01-01','Admin','Admin','2009-01-01',NULL,'','XML CONFIG',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2012-01-01 00:00:00'),(2,'Demo','demo','demo@demo.org','555-555-5555','2009-01-01','Demo','Demo','2009-01-01',NULL,'Demonstration user.','XML CONFIG',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2012-01-01 00:00:00');
/*!40000 ALTER TABLE `authuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authusergroup`
--

DROP TABLE IF EXISTS `authusergroup`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `authusergroup` (
  `UGID` int(10) NOT NULL auto_increment,
  `UID` int(10) NOT NULL,
  `GID` int(10) NOT NULL,
  `Notes` varchar(255) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`UGID`),
  KEY `authusergroup_UID` (`UID`),
  KEY `authusergroup_GID` (`GID`),
  CONSTRAINT `authusergroup_UID` FOREIGN KEY (`UID`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `authusergroup_GID` FOREIGN KEY (`GID`) REFERENCES `authgroup` (`GID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `authusergroup`
--

LOCK TABLES `authusergroup` WRITE;
/*!40000 ALTER TABLE `authusergroup` DISABLE KEYS */;
INSERT INTO `authusergroup` (`UGID`, `UID`, `GID`, `Notes`, `modified`) VALUES (1,1,1,NULL,'2012-01-01 00:00:00'),(2,2,2,NULL,'2012-01-01 00:00:00');
/*!40000 ALTER TABLE `authusergroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authuserrole`
--

DROP TABLE IF EXISTS `authuserrole`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `authuserrole` (
  `URID` int(10) NOT NULL auto_increment,
  `RoleID` int(10) NOT NULL,
  `UID` int(10) default NULL,
  `GID` int(10) default NULL,
  `Notes` varchar(255) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`URID`),
  KEY `authuserrole_RoleID` (`RoleID`),
  KEY `authuserrole_UID` (`UID`),
  KEY `authuserrole_GID` (`GID`),
  CONSTRAINT `authuserrole_RoleID` FOREIGN KEY (`RoleID`) REFERENCES `authrole` (`RoleID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `authuserrole_UID` FOREIGN KEY (`UID`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `authuserrole_GID` FOREIGN KEY (`GID`) REFERENCES `authgroup` (`GID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `authuserrole`
--

LOCK TABLES `authuserrole` WRITE;
/*!40000 ALTER TABLE `authuserrole` DISABLE KEYS */;
INSERT INTO `authuserrole` (`URID`, `RoleID`, `UID`, `GID`, `Notes`, `modified`) VALUES (1,1,NULL,1,NULL,'2012-01-01 00:00:00'),(2,2,NULL,2,NULL,'2012-01-01 00:00:00');
/*!40000 ALTER TABLE `authuserrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `calendar`
--

DROP TABLE IF EXISTS `calendar`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `calendar` (
  `calendar_id` int(10) unsigned NOT NULL auto_increment,
  `type` varchar(25) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(255) default NULL,
  `work_days` varchar(100) default NULL,
  `work_begin_time` time default NULL,
  `work_end_time` time default NULL,
  `notes` text,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`calendar_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `caregiver` (
  `CareID` int(10) NOT NULL auto_increment,
  `PIDN` int(10) NOT NULL,
  `Lname` varchar(25) NOT NULL,
  `FName` varchar(25) NOT NULL,
  `Gender` tinyint(3) default NULL,
  `PTRelation` varchar(25) default NULL,
  `LivesWithPT` smallint(5) default NULL,
  `PrimaryLanguage` varchar(25) default NULL,
  `TransNeeded` smallint(5) default NULL,
  `TransLanguage` varchar(25) default NULL,
  `IsPrimContact` smallint(5) default NULL,
  `IsContact` smallint(5) default NULL,
  `IsContactNotes` varchar(100) default NULL,
  `IsCaregiver` smallint(5) default NULL,
  `IsInformant` smallint(5) default NULL,
  `IsNextOfKin` smallint(5) default NULL,
  `IsResearchSurrogate` smallint(5) default NULL,
  `IsPowerOfAttorney` smallint(5) default NULL,
  `IsOtherRole` smallint(5) default NULL,
  `OtherRoleDesc` varchar(50) default NULL,
  `Note` varchar(255) default NULL,
  `ActiveFlag` smallint(5) default '1',
  `DOB` datetime default NULL,
  `Educ` tinyint(3) default NULL,
  `Race` varchar(25) default NULL,
  `MaritalStatus` varchar(25) default NULL,
  `Occupation` varchar(25) default NULL,
  `Age` int(10) default NULL,
  `FullName` varchar(100) default NULL,
  `FullNameRev` varchar(100) default NULL,
  `ContactDesc` varchar(255) default NULL,
  `RolesDesc` varchar(255) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`CareID`),
  KEY `caregiver__PIDN` (`PIDN`),
  CONSTRAINT `caregiver__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `caregiver`
--

LOCK TABLES `caregiver` WRITE;
/*!40000 ALTER TABLE `caregiver` DISABLE KEYS */;
/*!40000 ALTER TABLE `caregiver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cbt_tracking`
--

DROP TABLE IF EXISTS `cbt_tracking`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `cbt_tracking` (
  `instr_id` int(11) NOT NULL,
  `filename` varchar(500) default NULL,
  `task` varchar(25) default NULL,
  `version` varchar(10) default NULL,
  `version_date` date default NULL,
  `language` varchar(10) default NULL,
  `form` varchar(10) default NULL,
  `adult_child` varchar(10) default NULL,
  PRIMARY KEY  (`instr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `cbt_tracking`
--

LOCK TABLES `cbt_tracking` WRITE;
/*!40000 ALTER TABLE `cbt_tracking` DISABLE KEYS */;
/*!40000 ALTER TABLE `cbt_tracking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contactinfo`
--

DROP TABLE IF EXISTS `contactinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `contactinfo` (
  `CInfoID` int(10) NOT NULL auto_increment,
  `PIDN` int(10) NOT NULL,
  `CareID` int(10) default NULL,
  `ContactPT` smallint(5) default NULL,
  `IsPTResidence` smallint(5) default NULL,
  `OptOutMAC` smallint(5) default '0',
  `OptOutAffiliates` smallint(5) default '0',
  `ActiveFlag` smallint(5) default '1',
  `Address` varchar(100) default NULL,
  `Address2` varchar(100) default NULL,
  `City` varchar(50) default NULL,
  `State` char(10) default NULL,
  `Zip` varchar(10) default NULL,
  `Country` varchar(50) default NULL,
  `Phone1` varchar(25) default NULL,
  `PhoneType1` varchar(10) default NULL,
  `Phone2` varchar(25) default NULL,
  `PhoneType2` varchar(10) default NULL,
  `Phone3` varchar(25) default NULL,
  `PhoneType3` varchar(10) default NULL,
  `Email` varchar(100) default NULL,
  `Notes` varchar(250) default NULL,
  `ContactNameRev` varchar(100) default NULL,
  `ContactDesc` varchar(100) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`CInfoID`),
  KEY `contactinfo__PIDN` (`PIDN`),
  KEY `contactinfo__CareID` (`CareID`),
  CONSTRAINT `contactinfo__CareID` FOREIGN KEY (`CareID`) REFERENCES `caregiver` (`CareID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `contactinfo__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `contactlog` (
  `LogID` int(10) NOT NULL auto_increment,
  `PIDN` int(10) NOT NULL,
  `ProjName` varchar(75) default NULL,
  `LogDate` date default NULL,
  `LogTime` time default NULL,
  `Method` varchar(25) NOT NULL default 'Phone',
  `StaffInit` smallint(5) NOT NULL default '1',
  `Staff` varchar(50) default NULL,
  `Contact` varchar(50) default NULL,
  `Note` text,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`LogID`),
  KEY `contactlog__PIDN` (`PIDN`),
  KEY `contactlog__ProjName` (`ProjName`),
  KEY `contactLog__authfilter` (`PIDN`,`ProjName`,`LogID`),
  CONSTRAINT `contactlog__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `contactlog__ProjName` FOREIGN KEY (`ProjName`) REFERENCES `projectunit` (`ProjUnitDesc`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `contactlog`
--

LOCK TABLES `contactlog` WRITE;
/*!40000 ALTER TABLE `contactlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `contactlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crms_file`
--

DROP TABLE IF EXISTS `crms_file`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `crms_file` (
  `id` int(11) NOT NULL,
  `pidn` int(11) default NULL COMMENT '	',
  `enroll_stat_id` int(11) default NULL COMMENT '	',
  `vid` int(11) default NULL COMMENT '		',
  `instr_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_crms_file__pidn` (`pidn`),
  KEY `fk_crms_file__enroll_stat_id` (`enroll_stat_id`),
  KEY `fk_crms_file__vid` (`vid`),
  KEY `fk_crms_file__instr_id` (`instr_id`),
  KEY `crms_file__id` (`id`),
  CONSTRAINT `crms_file__id` FOREIGN KEY (`id`) REFERENCES `lava_file` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_crms_file__enroll_stat_id` FOREIGN KEY (`enroll_stat_id`) REFERENCES `enrollmentstatus` (`EnrollStatID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_crms_file__instr_id` FOREIGN KEY (`instr_id`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_crms_file__pidn` FOREIGN KEY (`pidn`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_crms_file__vid` FOREIGN KEY (`vid`) REFERENCES `visit` (`VID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `crms_file`
--

LOCK TABLES `crms_file` WRITE;
/*!40000 ALTER TABLE `crms_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `crms_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crmsauthrole`
--

DROP TABLE IF EXISTS `crmsauthrole`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `crmsauthrole` (
  `RoleID` int(10) NOT NULL,
  `PatientAccess` smallint(5) NOT NULL default '1',
  `PhiAccess` smallint(5) NOT NULL default '1',
  `GhiAccess` smallint(5) NOT NULL default '0',
  PRIMARY KEY  (`RoleID`),
  KEY `crmsauthrole__RoleID` (`RoleID`),
  CONSTRAINT `crmsauthrole__RoleID` FOREIGN KEY (`RoleID`) REFERENCES `authrole` (`RoleID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `crmsauthrole`
--

LOCK TABLES `crmsauthrole` WRITE;
/*!40000 ALTER TABLE `crmsauthrole` DISABLE KEYS */;
INSERT INTO `crmsauthrole` (`RoleID`, `PatientAccess`, `PhiAccess`, `GhiAccess`) VALUES (-1,0,0,0),(1,1,1,0),(2,1,1,0),(3,0,0,0),(4,0,0,0);
/*!40000 ALTER TABLE `crmsauthrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crmsauthuser`
--

DROP TABLE IF EXISTS `crmsauthuser`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `crmsauthuser` (
  `UID` int(10) NOT NULL,
  PRIMARY KEY  (`UID`),
  KEY `crmsauthuser__UID` (`UID`),
  CONSTRAINT `crmsauthuser__UID` FOREIGN KEY (`UID`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `crmsauthuser`
--

LOCK TABLES `crmsauthuser` WRITE;
/*!40000 ALTER TABLE `crmsauthuser` DISABLE KEYS */;
INSERT INTO `crmsauthuser` (`UID`) VALUES (1),(2);
/*!40000 ALTER TABLE `crmsauthuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crmsauthuserrole`
--

DROP TABLE IF EXISTS `crmsauthuserrole`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `crmsauthuserrole` (
  `URID` int(10) NOT NULL,
  `Project` varchar(25) default NULL,
  `Unit` varchar(25) default NULL,
  PRIMARY KEY  (`URID`),
  KEY `crmsauthuserrole__URID` (`URID`),
  CONSTRAINT `crmsauthuserrole__URID` FOREIGN KEY (`URID`) REFERENCES `authuserrole` (`URID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `crmsauthuserrole`
--

LOCK TABLES `crmsauthuserrole` WRITE;
/*!40000 ALTER TABLE `crmsauthuserrole` DISABLE KEYS */;
INSERT INTO `crmsauthuserrole` (`URID`, `Project`, `Unit`) VALUES (1,'*','*'),(2,'*','*');
/*!40000 ALTER TABLE `crmsauthuserrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datadictionary`
--

DROP TABLE IF EXISTS `datadictionary`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `datadictionary` (
  `id` int(11) NOT NULL auto_increment,
  `instance` varchar(25) NOT NULL default 'lava',
  `scope` varchar(25) NOT NULL,
  `entity` varchar(100) NOT NULL,
  `prop_order` smallint(6) default NULL,
  `prop_name` varchar(100) default NULL,
  `prop_description` varchar(500) default NULL,
  `data_values` varchar(500) default NULL,
  `data_calculation` varchar(500) default NULL,
  `required` tinyint(4) default '1',
  `db_table` varchar(50) default NULL,
  `db_column` varchar(50) default NULL,
  `db_order` smallint(6) default NULL,
  `db_datatype` varchar(20) default NULL,
  `db_datalength` varchar(20) default NULL,
  `db_nullable` tinyint(4) default '1',
  `db_default` varchar(50) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `datadictionary`
--

LOCK TABLES `datadictionary` WRITE;
/*!40000 ALTER TABLE `datadictionary` DISABLE KEYS */;
/*!40000 ALTER TABLE `datadictionary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `doctor` (
  `DocID` int(10) NOT NULL auto_increment,
  `LName` varchar(25) NOT NULL,
  `MInitial` char(1) default NULL,
  `FName` varchar(25) NOT NULL,
  `Address` varchar(100) default NULL,
  `City` varchar(50) default NULL,
  `State` char(2) default NULL,
  `Zip` varchar(10) default NULL,
  `Phone1` varchar(25) default NULL,
  `PhoneType1` varchar(10) default NULL,
  `Phone2` varchar(25) default NULL,
  `PhoneType2` varchar(10) default NULL,
  `Phone3` varchar(25) default NULL,
  `PhoneType3` varchar(10) default NULL,
  `Email` varchar(100) default NULL,
  `DocType` varchar(50) default NULL,
  `FullNameRev` varchar(100) default NULL,
  `FullName` varchar(100) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`DocID`)
) ENGINE=InnoDB AUTO_INCREMENT=10905 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `enrollmentstatus` (
  `EnrollStatID` int(10) NOT NULL auto_increment,
  `PIDN` int(10) NOT NULL,
  `ProjName` varchar(75) default NULL,
  `SubjectStudyID` varchar(10) default NULL,
  `ReferralSource` varchar(75) default NULL,
  `LatestDesc` varchar(25) default NULL,
  `LatestDate` datetime default NULL,
  `LatestNote` varchar(100) default NULL,
  `ReferredDesc` varchar(25) default NULL,
  `ReferredDate` datetime default NULL,
  `ReferredNote` varchar(100) default NULL,
  `DeferredDesc` varchar(25) default NULL,
  `DeferredDate` datetime default NULL,
  `DeferredNote` varchar(100) default NULL,
  `EligibleDesc` varchar(25) default NULL,
  `EligibleDate` datetime default NULL,
  `EligibleNote` varchar(100) default NULL,
  `IneligibleDesc` varchar(25) default NULL,
  `IneligibleDate` datetime default NULL,
  `IneligibleNote` varchar(100) default NULL,
  `DeclinedDesc` varchar(25) default NULL,
  `DeclinedDate` datetime default NULL,
  `DeclinedNote` varchar(100) default NULL,
  `EnrolledDesc` varchar(25) default NULL,
  `EnrolledDate` datetime default NULL,
  `EnrolledNote` varchar(100) default NULL,
  `ExcludedDesc` varchar(25) default NULL,
  `ExcludedDate` datetime default NULL,
  `ExcludedNote` varchar(100) default NULL,
  `WithdrewDesc` varchar(25) default NULL,
  `WithdrewDate` datetime default NULL,
  `WithdrewNote` varchar(100) default NULL,
  `InactiveDesc` varchar(25) default NULL,
  `InactiveDate` datetime default NULL,
  `InactiveNote` varchar(100) default NULL,
  `DeceasedDesc` varchar(25) default NULL,
  `DeceasedDate` datetime default NULL,
  `DeceasedNote` varchar(100) default NULL,
  `AutopsyDesc` varchar(25) default NULL,
  `AutopsyDate` datetime default NULL,
  `AutopsyNote` varchar(100) default NULL,
  `ClosedDesc` varchar(25) default NULL,
  `ClosedDate` datetime default NULL,
  `ClosedNote` varchar(100) default NULL,
  `EnrollmentNotes` varchar(500) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`EnrollStatID`),
  KEY `enrollmentstatus__PIDN` (`PIDN`),
  KEY `enrollmentstatus__ProjName` (`ProjName`),
  KEY `enrollmentstatus__authfilter` (`PIDN`,`ProjName`,`EnrollStatID`),
  CONSTRAINT `enrollmentstatus__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `enrollmentstatus__ProjName` FOREIGN KEY (`ProjName`) REFERENCES `projectunit` (`ProjUnitDesc`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `hibernateproperty` (
  `id` int(10) NOT NULL auto_increment,
  `instance` varchar(25) NOT NULL default 'lava',
  `scope` varchar(25) NOT NULL,
  `entity` varchar(100) NOT NULL,
  `property` varchar(100) NOT NULL,
  `dbTable` varchar(50) NOT NULL,
  `dbColumn` varchar(50) NOT NULL,
  `dbType` varchar(50) default NULL,
  `dbLength` smallint(5) default NULL,
  `dbPrecision` smallint(5) default NULL,
  `dbScale` smallint(5) default NULL,
  `dbOrder` smallint(5) default NULL,
  `hibernateProperty` varchar(50) default NULL,
  `hibernateType` varchar(50) default NULL,
  `hibernateClass` varchar(250) default NULL,
  `hibernateNotNull` varchar(50) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2244 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `hibernateproperty`
--

LOCK TABLES `hibernateproperty` WRITE;
/*!40000 ALTER TABLE `hibernateproperty` DISABLE KEYS */;
INSERT INTO `hibernateproperty` (`id`, `instance`, `scope`, `entity`, `property`, `dbTable`, `dbColumn`, `dbType`, `dbLength`, `dbPrecision`, `dbScale`, `dbOrder`, `hibernateProperty`, `hibernateType`, `hibernateClass`, `hibernateNotNull`, `modified`) VALUES (1625,'lava','core','appointment','id','appointment','reservation_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-03-31 13:35:27'),(1626,'lava','core','appointment','calendar','appointment','calendar_id','int',NULL,10,0,2,'calendar','many-to-one','edu.ucsf.lava.core.resource.model.ResourceCalendar','Yes','2009-03-31 13:35:27'),(1627,'lava','core','appointment','organizer','appointment','organizer_id','int',NULL,10,0,3,'owner','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-03-31 13:35:27'),(1628,'lava','core','appointment','type','appointment','type','varchar',25,NULL,NULL,4,'type','string',NULL,'Yes','2009-03-31 13:35:27'),(1629,'lava','core','appointment','description','appointment','description','varchar',100,NULL,NULL,5,'description','string',NULL,'No','2009-03-31 13:35:27'),(1630,'lava','core','appointment','location','appointment','location','varchar',100,NULL,NULL,6,'location','string',NULL,'No','2009-03-31 13:35:27'),(1631,'lava','core','appointment','startDate','appointment','start_date','date',NULL,NULL,NULL,7,'startDate','date',NULL,'Yes','2009-03-31 13:35:27'),(1632,'lava','core','appointment','startTime','appointment','start_time','time',NULL,NULL,NULL,8,'startTime','time',NULL,'Yes','2009-03-31 13:35:27'),(1633,'lava','core','appointment','endDate','appointment','end_date','date',NULL,NULL,NULL,9,'endDate','date',NULL,'Yes','2009-06-02 15:27:56'),(1634,'lava','core','appointment','endTime','appointment','end_time','time',NULL,NULL,NULL,10,'endTime','time',NULL,NULL,'2009-06-02 15:28:28'),(1635,'lava','core','appointment','status','appointment','status','varchar',25,NULL,NULL,11,'status','string',NULL,'No','2009-05-11 12:45:09'),(1636,'lava','core','appointment','notes','appointment','notes','text',NULL,NULL,NULL,11,'notes','string',NULL,'No','2009-03-31 13:35:27'),(1637,'lava','core','attendee','id','attendee','attendee_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-06-03 13:40:16'),(1638,'lava','core','attendee','appointment','attendee','appointment_id','int',NULL,10,0,2,'appointment','many-to-one','edu.ucsf.lava.core.calendar.model.Appointment','Yes','2009-06-03 13:40:16'),(1639,'lava','core','attendee','user','attendee','user_id','int',NULL,10,0,3,'user','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-06-03 13:40:16'),(1640,'lava','core','attendee','role','attendee','role','varchar',25,NULL,NULL,4,'role','string',NULL,'Yes','2009-06-03 13:40:16'),(1641,'lava','core','attendee','status','attendee','status','varchar',25,NULL,NULL,5,'status','string',NULL,'Yes','2009-06-03 13:40:16'),(1642,'lava','core','attendee','notes','attendee','notes','varchar',100,NULL,NULL,6,'notes','string',NULL,'No','2009-06-03 13:40:16'),(1643,'lava','core','auditEntity','id','audit_entity_work','audit_entity_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(1644,'lava','core','auditEntity','auditEvent','audit_entity_work','audit_event_id','int',NULL,10,0,2,'auditEvent','many-to-one','edu.ucsf.memory.lava.model.AuditEvent','Yes','2009-01-24 21:25:56'),(1645,'lava','core','auditEntity','entityId','audit_entity_work','entity_id','int',NULL,10,0,3,'entityId','long',NULL,'Yes','2009-01-24 21:25:56'),(1646,'lava','core','auditEntity','entity','audit_entity_work','entity','varchar',100,NULL,NULL,4,'entity','string',NULL,'Yes','2009-01-24 21:25:56'),(1647,'lava','core','auditEntity','entityType','audit_entity_work','entity_type','varchar',100,NULL,NULL,5,'entityType','string',NULL,'Yes','2009-01-24 21:25:56'),(1648,'lava','core','auditEntity','auditType','audit_entity_work','audit_type','varchar',10,NULL,NULL,6,'auditType','string',NULL,'Yes','2009-01-24 21:25:56'),(1649,'lava','core','auditEntity','hversion','audit_entity_work','hversion','int',NULL,10,0,7,'hversion','long',NULL,'Yes','2009-01-24 21:25:56'),(1650,'lava','core','auditEntityHistory','id','audit_entity','audit_entity_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(1651,'lava','core','auditEntityHistory','auditEvent','audit_entity','audit_event_id','int',NULL,10,0,2,'auditEvent','many-to-one','edu.ucsf.memory.lava.model.AuditEvent','Yes','2009-01-24 21:25:56'),(1652,'lava','core','auditEntityHistory','entityId','audit_entity','entity_id','int',NULL,10,0,3,'entityId','long',NULL,'Yes','2009-01-24 21:25:56'),(1653,'lava','core','auditEntityHistory','entity','audit_entity','entity','varchar',100,NULL,NULL,4,'entity','string',NULL,'Yes','2009-01-24 21:25:56'),(1654,'lava','core','auditEntityHistory','entityType','audit_entity','entity_type','varchar',100,NULL,NULL,5,'entityType','string',NULL,'Yes','2009-01-24 21:25:56'),(1655,'lava','core','auditEntityHistory','auditType','audit_entity','audit_type','varchar',10,NULL,NULL,6,'auditType','string',NULL,'Yes','2009-01-24 21:25:56'),(1656,'lava','core','auditEntityHistory','hversion','audit_entity','hversion','int',NULL,10,0,7,'hversion','long',NULL,'Yes','2009-01-24 21:25:56'),(1657,'lava','core','auditEvent','id','audit_event_work','audit_event_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(1658,'lava','core','auditEvent','auditUser','audit_event_work','audit_user','varchar',50,NULL,NULL,2,'auditUser','string',NULL,'Yes','2009-01-24 21:25:56'),(1659,'lava','core','auditEvent','auditHost','audit_event_work','audit_host','varchar',25,NULL,NULL,3,'auditHost','string',NULL,'Yes','2009-01-24 21:25:56'),(1660,'lava','core','auditEvent','auditTimestamp','audit_event_work','audit_timestamp','timestamp',NULL,23,3,4,'auditTimestamp','timestamp',NULL,'Yes','2009-01-24 21:25:56'),(1661,'lava','core','auditEvent','action','audit_event_work','action','varchar',255,NULL,NULL,5,'action','string',NULL,'Yes','2009-01-24 21:25:56'),(1662,'lava','core','auditEvent','actionEvent','audit_event_work','action_event','varchar',50,NULL,NULL,6,'actionEvent','string',NULL,'Yes','2009-01-24 21:25:56'),(1663,'lava','core','auditEvent','actionIdParam','audit_event_work','action_id_param','varchar',50,NULL,NULL,7,'actionIdParam','string',NULL,'No','2009-01-24 21:25:56'),(1664,'lava','core','auditEvent','eventNote','audit_event_work','event_note','varchar',255,NULL,NULL,8,'eventNote','string',NULL,'No','2009-01-24 21:25:56'),(1665,'lava','core','auditEvent','exception','audit_event_work','exception','varchar',255,NULL,NULL,9,'exception','string',NULL,'No','2009-01-24 21:25:56'),(1666,'lava','core','auditEvent','exceptionMessage','audit_event_work','exception_message','varchar',255,NULL,NULL,10,'exceptionMessage','string',NULL,'No','2009-01-24 21:25:56'),(1667,'lava','core','auditEvent','hversion','audit_event_work','hversion','int',NULL,10,0,11,'hversion','long',NULL,'Yes','2009-01-24 21:25:56'),(1668,'lava','core','auditEventHistory','id','audit_event','audit_event_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(1669,'lava','core','auditEventHistory','auditUser','audit_event','audit_user','varchar',50,NULL,NULL,2,'auditUser','string',NULL,'Yes','2009-01-24 21:25:56'),(1670,'lava','core','auditEventHistory','auditHost','audit_event','audit_host','varchar',25,NULL,NULL,3,'auditHost','string',NULL,'Yes','2009-01-24 21:25:56'),(1671,'lava','core','auditEventHistory','auditTimestamp','audit_event','audit_timestamp','timestamp',NULL,23,3,4,'auditTimestamp','timestamp',NULL,'Yes','2009-01-24 21:25:56'),(1672,'lava','core','auditEventHistory','action','audit_event','action','varchar',255,NULL,NULL,5,'action','string',NULL,'Yes','2009-01-24 21:25:56'),(1673,'lava','core','auditEventHistory','actionEvent','audit_event','action_event','varchar',50,NULL,NULL,6,'actionEvent','string',NULL,'Yes','2009-01-24 21:25:56'),(1674,'lava','core','auditEventHistory','actionIdParam','audit_event','action_id_param','varchar',50,NULL,NULL,7,'actionIdParam','string',NULL,'No','2009-01-24 21:25:56'),(1675,'lava','core','auditEventHistory','eventNote','audit_event','event_note','varchar',255,NULL,NULL,8,'eventNote','string',NULL,'No','2009-01-24 21:25:56'),(1676,'lava','core','auditEventHistory','exception','audit_event','exception','varchar',255,NULL,NULL,9,'exception','string',NULL,'No','2009-01-24 21:25:56'),(1677,'lava','core','auditEventHistory','exceptionMessage','audit_event','exception_message','varchar',255,NULL,NULL,10,'exceptionMessage','string',NULL,'No','2009-01-24 21:25:56'),(1678,'lava','core','auditEventHistory','hversion','audit_event','hversion','int',NULL,10,0,11,'hversion','long',NULL,'Yes','2009-01-24 21:25:56'),(1679,'lava','core','auditProperty','id','audit_property_work','audit_property_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(1680,'lava','core','auditProperty','auditEntity','audit_property_work','audit_entity_id','int',NULL,10,0,2,'auditEntity','many-to-one','edu.ucsf.memory.lava.model.AuditEntity','Yes','2009-01-24 21:25:56'),(1681,'lava','core','auditProperty','property','audit_property_work','property','varchar',100,NULL,NULL,3,'property','string',NULL,'Yes','2009-01-24 21:25:56'),(1682,'lava','core','auditProperty','indexKey','audit_property_work','index_key','varchar',100,NULL,NULL,4,'indexKey','string',NULL,'No','2009-01-24 21:25:56'),(1683,'lava','core','auditProperty','subproperty','audit_property_work','subproperty','varchar',255,NULL,NULL,5,'subproperty','string',NULL,'No','2009-01-24 21:25:56'),(1684,'lava','core','auditProperty','oldValue','audit_property_work','old_value','varchar',255,NULL,NULL,6,'oldValue','string',NULL,'Yes','2009-01-24 21:25:56'),(1685,'lava','core','auditProperty','newValue','audit_property_work','new_value','varchar',255,NULL,NULL,7,'newValue','string',NULL,'Yes','2009-01-24 21:25:56'),(1686,'lava','core','auditProperty','auditTimestamp','audit_property_work','audit_timestamp','timestamp',NULL,23,3,8,'auditTimestamp','timestamp',NULL,'Yes','2009-01-24 21:25:56'),(1687,'lava','core','auditProperty','oldText','audit_text_work','old_text','text',16,NULL,NULL,9,'oldText','text',NULL,'No','2009-01-24 21:25:56'),(1688,'lava','core','auditProperty','newText','audit_text_work','new_text','text',16,NULL,NULL,10,'newText','text',NULL,'No','2009-01-24 21:25:56'),(1689,'lava','core','auditProperty','hversion','audit_property_work','hversion','int',NULL,10,0,11,'hversion','long',NULL,'No','2009-01-24 21:25:56'),(1690,'lava','core','auditPropertyHistory','id','audit_property','audit_property_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(1691,'lava','core','auditPropertyHistory','auditEntity','audit_property','audit_entity_id','int',NULL,10,0,2,'auditEntity','many-to-one','edu.ucsf.memory.lava.model.AuditEntity','Yes','2009-01-24 21:25:56'),(1692,'lava','core','auditPropertyHistory','property','audit_property','property','varchar',100,NULL,NULL,3,'property','string',NULL,'Yes','2009-01-24 21:25:56'),(1693,'lava','core','auditPropertyHistory','indexKey','audit_property','index_key','varchar',100,NULL,NULL,4,'indexKey','string',NULL,'No','2009-01-24 21:25:56'),(1694,'lava','core','auditPropertyHistory','subproperty','audit_property','subproperty','varchar',255,NULL,NULL,5,'subproperty','string',NULL,'No','2009-01-24 21:25:56'),(1695,'lava','core','auditPropertyHistory','oldValue','audit_property','old_value','varchar',255,NULL,NULL,6,'oldValue','string',NULL,'Yes','2009-01-24 21:25:56'),(1696,'lava','core','auditPropertyHistory','newValue','audit_property','new_value','varchar',255,NULL,NULL,7,'newValue','string',NULL,'Yes','2009-01-24 21:25:56'),(1697,'lava','core','auditPropertyHistory','auditTimestamp','audit_property','audit_timestamp','timestamp',NULL,23,3,8,'auditTimestamp','timestamp',NULL,'Yes','2009-01-24 21:25:56'),(1698,'lava','core','auditPropertyHistory','oldText','audit_text','old_text','text',16,NULL,NULL,9,'oldText','text',NULL,'No','2009-01-24 21:25:56'),(1699,'lava','core','auditPropertyHistory','newText','audit_text','new_text','text',16,NULL,NULL,10,'newText','text',NULL,'No','2009-01-24 21:25:56'),(1700,'lava','core','auditPropertyHistory','hversion','audit_property','hversion','int',NULL,10,0,11,'hversion','long',NULL,'No','2009-01-24 21:25:56'),(1701,'lava','core','authGroup','id','authgroup','GID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(1702,'lava','core','authGroup','groupName','authgroup','GroupName','varchar',50,NULL,NULL,2,'groupName','string',NULL,'Yes','2009-01-24 21:25:56'),(1703,'lava','core','authGroup','effectiveDate','authgroup','EffectiveDate','date',NULL,16,0,3,'effectiveDate','timestamp',NULL,'Yes','2009-01-24 21:25:56'),(1704,'lava','core','authGroup','expirationDate','authgroup','ExpirationDate','date',NULL,16,0,4,'expirationDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(1705,'lava','core','authGroup','notes','authgroup','Notes','varchar',255,NULL,NULL,5,'notes','string',NULL,'No','2009-01-24 21:25:56'),(1706,'lava','core','authPermission','id','authpermission','PermID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(1707,'lava','core','authPermission','role','authpermission','RoleID','varchar',25,NULL,NULL,2,'role','many-to-one','edu.ucsf.memory.lava.model.AuthRole','Yes','2009-01-24 21:25:56'),(1708,'lava','core','authPermission','module','authpermission','Module','varchar',25,NULL,NULL,4,'module','string',NULL,'Yes','2009-01-24 21:25:56'),(1709,'lava','core','authPermission','permitDeny','authpermission','PermitDeny','varchar',10,NULL,NULL,4,'permitDeny','string',NULL,'Yes','2009-01-24 21:25:56'),(1710,'lava','core','authPermission','section','authpermission','Section','varchar',25,NULL,NULL,5,'section','string',NULL,'Yes','2009-01-24 21:25:56'),(1711,'lava','core','authPermission','target','authpermission','Target','varchar',25,NULL,NULL,6,'target','string',NULL,'Yes','2009-01-24 21:25:56'),(1712,'lava','core','authPermission','mode','authpermission','Mode','varchar',25,NULL,NULL,7,'mode','string',NULL,'Yes','2009-01-24 21:25:56'),(1713,'lava','core','authPermission','notes','authpermission','Notes','varchar',100,NULL,NULL,10,'notes','string',NULL,'No','2009-01-24 21:25:56'),(1714,'lava','core','authRole','id','authrole','RoleID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(1715,'lava','core','authRole','roleName','authrole','RoleName','varchar',25,NULL,NULL,2,'roleName','string',NULL,'Yes','2009-01-24 21:25:56'),(1716,'lava','core','authRole','patientAccess','authrole','PatientAccess','smallint',NULL,5,0,3,'patientAccess','short',NULL,'Yes','2009-01-24 21:25:56'),(1717,'lava','core','authRole','phiAccess','authrole','PhiAccess','smallint',NULL,5,0,4,'phiAccess','short',NULL,'Yes','2009-01-24 21:25:56'),(1718,'lava','core','authRole','patientAccess','authrole','GhiAccess','smallint',NULL,5,0,5,'ghiAccess','short',NULL,'Yes','2009-01-24 21:25:56'),(1719,'lava','core','authRole','notes','authrole','Notes','varchar',255,NULL,NULL,8,'notes','string',NULL,'No','2009-01-24 21:25:56'),(1720,'lava','core','authUser','id','authuser','UID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(1721,'lava','core','authUser','userName','authuser','UserName','varchar',50,NULL,NULL,2,'userName','string',NULL,'Yes','2009-01-24 21:25:56'),(1722,'lava','core','authUser','login','authuser','Login','varchar',100,NULL,NULL,3,'login','string',NULL,'No','2009-01-24 21:25:56'),(1723,'lava','core','authUser','email','authuser','email','varchar',100,NULL,NULL,4,'email','string',NULL,'No','2009-05-12 11:53:20'),(1724,'lava','core','authUser','phone','authuser','phone','varchar',25,NULL,NULL,5,'phone','string',NULL,'No','2009-05-12 11:53:20'),(1725,'lava','core','authUser','accessAgreementDate','authuser','AccessAgreementDate','date',NULL,16,0,7,'accessAgreementDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(1726,'lava','core','authUser','shortUserName','authuser','ShortUserName','varchar',53,NULL,NULL,8,'shortUserName','string',NULL,'No','2009-01-24 21:25:56'),(1727,'lava','core','authUser','shortUserNameRev','authuser','ShortUserNameRev','varchar',54,NULL,NULL,9,'shortUserNameRev','string',NULL,'No','2009-01-24 21:25:56'),(1728,'lava','core','authUser','effectiveDate','authuser','EffectiveDate','date',NULL,16,0,10,'effectiveDate','timestamp',NULL,'Yes','2009-01-24 21:25:56'),(1729,'lava','core','authUser','expirationDate','authuser','ExpirationDate','date',NULL,16,0,11,'expirationDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(1730,'lava','core','authUser','authenticationType','authuser','authenticationType','varchar',10,NULL,NULL,12,'authenticationType','string',NULL,'No','2009-05-12 11:53:20'),(1731,'lava','core','authUser','notes','authuser','Notes','varchar',255,NULL,NULL,12,'notes','string',NULL,'No','2009-01-24 21:25:56'),(1732,'lava','core','authUser','password','authuser','password','varchar',100,NULL,NULL,13,'password','string',NULL,'No','2009-05-12 11:53:20'),(1733,'lava','core','authUser','passwordExpiration','authuser','passwordExpiration','timestamp',NULL,NULL,NULL,14,'passwordExpiration','timestamp',NULL,'No','2009-05-12 11:53:20'),(1734,'lava','core','authUser','passwordResetToken','authuser','passwordResetToken','varchar',100,NULL,NULL,15,'passwordResetToken','string',NULL,'No','2009-05-12 11:53:20'),(1735,'lava','core','authUser','passwordResetExpiration','authuser','passwordResetExpiration','timestamp',NULL,NULL,NULL,16,'passwordResetExpiration','timestamp',NULL,'No','2009-05-12 11:53:20'),(1736,'lava','core','authUser','failedLoginCount','authuser','failedLoginCount','smallint',NULL,5,0,17,'failedLoginCount','short',NULL,'No','2009-05-12 11:53:20'),(1737,'lava','core','authUser','lastFailedLogin','authuser','lastFailedLogin','timestamp',NULL,NULL,NULL,18,'lastFailedLogin','timestamp',NULL,'No','2009-05-12 11:53:20'),(1738,'lava','core','authUser','accountLocked','authuser','accountLocked','timestamp',NULL,NULL,NULL,19,'accountLocked','timestamp',NULL,'No','2009-05-12 11:53:20'),(1739,'lava','core','authUserGroup','id','authusergroup','UGID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(1740,'lava','core','authUserGroup','user','authusergroup','UID','int',NULL,10,0,2,'user','many-to-one','edu.ucsf.memory.lava.model.AuthUser','Yes','2009-01-24 21:25:56'),(1741,'lava','core','authUserGroup','group','authusergroup','GID','int',NULL,10,0,3,'group','many-to-one','edu.ucsf.memory.lava.model.AuthGroup','Yes','2009-01-24 21:25:56'),(1742,'lava','core','authUserGroup','notes','authusergroup','Notes','varchar',255,NULL,NULL,6,'notes','string',NULL,'No','2009-01-24 21:25:56'),(1743,'lava','core','authUserRole','id','authuserrole','URID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(1744,'lava','core','authUserRole','role','authuserrole','RoleID','varchar',25,NULL,NULL,3,'role','many-to-one','edu.ucsf.memory.lava.model.AuthRole','Yes','2009-01-24 21:25:56'),(1745,'lava','core','authUserRole','project','authuserrole','Project','varchar',25,NULL,NULL,4,'project','string',NULL,'No','2009-01-24 21:25:56'),(1746,'lava','core','authUserRole','unit','authuserrole','Unit','varchar',25,NULL,NULL,5,'unit','string',NULL,'No','2009-01-24 21:25:56'),(1747,'lava','core','authUserRole','user','authuserrole','UID','int',NULL,10,0,7,'user','many-to-one','edu.ucsf.memory.lava.model.AuthUser','No','2009-01-24 21:25:56'),(1748,'lava','core','authUserRole','group','authuserrole','GID','int',NULL,10,0,8,'group','many-to-one','edu.ucsf.memory.lava.model.AuthGroup','No','2009-01-24 21:25:56'),(1749,'lava','core','authUserRole','notes','authuserrole','Notes','varchar',255,NULL,NULL,12,'notes','string',NULL,'No','2009-01-24 21:25:56'),(1750,'lava','core','calendar','calendar_id','calendar','calendar_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-03-31 12:09:35'),(1751,'lava','core','calendar','type','calendar','type','varchar',25,NULL,NULL,2,'type','string',NULL,'Yes','2009-06-02 15:27:08'),(1752,'lava','core','calendar','name','calendar','name','varchar',100,NULL,NULL,3,'name','string',NULL,'Yes','2009-03-31 12:09:35'),(1753,'lava','core','calendar','description','calendar','description','varchar',255,NULL,NULL,4,'description','string',NULL,'No','2009-03-31 12:09:35'),(1754,'lava','core','calendar','notes','calendar','notes','text',NULL,NULL,NULL,5,'notes','string',NULL,'No','2009-03-31 12:09:35'),(1755,'lava','core','lavaFile','id','lava_file','id','int',NULL,10,0,1,'id','long',NULL,'Yes','2011-10-19 11:05:17'),(1756,'lava','core','lavaFile','name','lava_file','name','varchar',255,NULL,NULL,2,'name','string',NULL,'No','2011-10-19 11:05:17'),(1757,'lava','core','lavaFile','fileType','lava_file','file_type','varchar',255,NULL,NULL,3,'fileType','string',NULL,'No','2011-10-19 11:05:17'),(1758,'lava','core','lavaFile','contentType','lava_file','content_type','varchar',100,NULL,NULL,4,'contentType','string',NULL,'No','2011-10-19 11:05:17'),(1759,'lava','core','lavaFile','fileStatusDate','lava_file','file_status_date','date',NULL,NULL,NULL,5,'fileStatusDate','date',NULL,'No','2011-10-19 11:05:17'),(1760,'lava','core','lavaFile','fileStatus','lava_file','file_status','varchar',50,NULL,NULL,6,'fileStatus','string',NULL,'No','2011-10-19 11:05:17'),(1761,'lava','core','lavaFile','fileStatusBy','lava_file','file_status_by','varchar',50,NULL,NULL,7,'fileStatusBy','string',NULL,'No','2011-10-19 11:05:17'),(1762,'lava','core','lavaFile','repositoryId','lava_file','repository_id','varchar',100,NULL,NULL,8,'repositoryId','string',NULL,'No','2011-10-19 11:05:17'),(1763,'lava','core','lavaFile','fileId','lava_file','file_id','varchar',100,NULL,NULL,9,'fileId','string',NULL,'No','2011-10-19 11:05:17'),(1764,'lava','core','lavaFile','location','lava_file','location','varchar',1000,NULL,NULL,10,'location','string',NULL,'No','2011-10-19 11:05:17'),(1765,'lava','core','lavaFile','checksum','lava_file','checksum','varchar',100,NULL,NULL,11,'checksum','string',NULL,'No','2011-10-19 11:05:17'),(1766,'lava','core','resourceCalendar','id','resource_calendar','calendar_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-06-03 10:28:00'),(1767,'lava','core','resourceCalendar','resourceType','resource_calendar','resource_type','varchar',25,NULL,NULL,2,'resourceType','string',NULL,'Yes','2009-06-03 10:28:00'),(1768,'lava','core','resourceCalendar','location','resource_calendar','location','varchar',100,NULL,NULL,3,'location','string',NULL,'No','2009-06-03 10:28:00'),(1769,'lava','core','resourceCalendar','contact','resource_calendar','contact_id','int',NULL,10,0,4,'contact','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-06-03 10:28:00'),(2007,'lava','crms','caregiver','id','caregiver','CareID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(2008,'lava','crms','caregiver','patient','caregiver','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-24 21:25:56'),(2009,'lava','crms','caregiver','lastName','caregiver','Lname','varchar',25,NULL,NULL,3,'lastName','string',NULL,'Yes','2009-01-24 21:25:56'),(2010,'lava','crms','caregiver','firstName','caregiver','FName','varchar',25,NULL,NULL,4,'firstName','string',NULL,'Yes','2009-01-24 21:25:56'),(2011,'lava','crms','caregiver','gender','caregiver','Gender','tinyint',NULL,3,0,5,'gender','byte',NULL,'No','2009-01-24 21:25:56'),(2012,'lava','crms','caregiver','relation','caregiver','PTRelation','varchar',25,NULL,NULL,6,'relation','string',NULL,'No','2009-01-24 21:25:56'),(2013,'lava','crms','caregiver','livesWithPatient','caregiver','LivesWithPT','smallint',NULL,5,0,7,'livesWithPatient','short',NULL,'No','2009-01-24 21:25:56'),(2014,'lava','crms','caregiver','primaryLanguage','caregiver','PrimaryLanguage','varchar',25,NULL,NULL,8,'primaryLanguage','string',NULL,'No','2009-01-24 21:25:56'),(2015,'lava','crms','caregiver','transNeeded','caregiver','TransNeeded','smallint',NULL,5,0,9,'transNeeded','short',NULL,'No','2009-01-24 21:25:56'),(2016,'lava','crms','caregiver','transLanguage','caregiver','TransLanguage','varchar',25,NULL,NULL,10,'transLanguage','string',NULL,'No','2009-01-24 21:25:56'),(2017,'lava','crms','caregiver','isPrimaryContact','caregiver','IsPrimContact','smallint',NULL,5,0,11,'isPrimaryContact','short',NULL,'No','2009-01-24 21:25:56'),(2018,'lava','crms','caregiver','isContact','caregiver','IsContact','smallint',NULL,5,0,12,'isContact','short',NULL,'No','2009-01-24 21:25:56'),(2019,'lava','crms','caregiver','isContactNotes','caregiver','IsContactNotes','varchar',100,NULL,NULL,13,'isContactNotes','string',NULL,'No','2009-01-24 21:25:56'),(2020,'lava','crms','caregiver','isCaregiver','caregiver','IsCaregiver','smallint',NULL,5,0,14,'isCaregiver','short',NULL,'No','2009-01-24 21:25:56'),(2021,'lava','crms','caregiver','isInformant','caregiver','IsInformant','smallint',NULL,5,0,15,'isInformant','short',NULL,'No','2009-01-24 21:25:56'),(2022,'lava','crms','caregiver','isResearchSurrogate','caregiver','IsResearchSurrogate','smallint',NULL,5,0,16,'isResearchSurrogate','short',NULL,'No','2009-01-24 21:25:56'),(2023,'lava','crms','caregiver','isNextOfKin','caregiver','IsNextOfKin','smallint',NULL,5,0,16,'isNextOfKin','short',NULL,'No','2009-01-24 21:25:56'),(2024,'lava','crms','caregiver','isPowerOfAttorney','caregiver','IsPowerOfAttorney','smallint',NULL,5,0,17,'isPowerOfAttorney','short',NULL,'No','2009-01-24 21:25:56'),(2025,'lava','crms','caregiver','isOtherRole','caregiver','IsOtherRole','smallint',NULL,5,0,18,'isOtherRole','short',NULL,'No','2009-01-24 21:25:56'),(2026,'lava','crms','caregiver','otherRoleDesc','caregiver','OtherRoleDesc','varchar',50,NULL,NULL,19,'otherRoleDesc','string',NULL,'No','2009-01-24 21:25:56'),(2027,'lava','crms','caregiver','note','caregiver','Note','varchar',255,NULL,NULL,20,'note','string',NULL,'No','2009-01-24 21:25:56'),(2028,'lava','crms','caregiver','active','caregiver','ActiveFlag','smallint',NULL,5,0,21,'active','short',NULL,'No','2009-01-24 21:25:56'),(2029,'lava','crms','caregiver','birthDate','caregiver','DOB','smalldatetime',NULL,16,0,22,'birthDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2030,'lava','crms','caregiver','education','caregiver','Educ','tinyint',NULL,3,0,23,'education','byte',NULL,'No','2009-01-24 21:25:56'),(2031,'lava','crms','caregiver','race','caregiver','Race','varchar',25,NULL,NULL,24,'race','string',NULL,'No','2009-01-24 21:25:56'),(2032,'lava','crms','caregiver','maritalStatus','caregiver','MaritalStatus','varchar',25,NULL,NULL,25,'maritalStatus','string',NULL,'No','2009-01-24 21:25:56'),(2033,'lava','crms','caregiver','occupation','caregiver','Occupation','varchar',25,NULL,NULL,26,'occupation','string',NULL,'No','2009-01-24 21:25:56'),(2034,'lava','crms','caregiver','age','caregiver','Age','int',NULL,10,0,27,'age','long',NULL,'No','2009-01-24 21:25:56'),(2035,'lava','crms','caregiver','fullName','caregiver','FullName','varchar',51,NULL,NULL,28,'fullName','string',NULL,'Yes','2009-01-24 21:25:56'),(2036,'lava','crms','caregiver','fullNameRev','caregiver','FullNameRev','varchar',52,NULL,NULL,29,'fullNameRev','string',NULL,'Yes','2009-01-24 21:25:56'),(2037,'lava','crms','caregiver','contactDesc','caregiver','ContactDesc','varchar',117,NULL,NULL,30,'contactDesc','string',NULL,'No','2009-01-24 21:25:56'),(2038,'lava','crms','caregiver','rolesDesc','caregiver','RolesDesc','varchar',117,NULL,NULL,31,'rolesDesc','string',NULL,'No','2009-01-24 21:25:56'),(2039,'lava','crms','consent','consentId','patientconsent','ConsentID','int',NULL,10,0,1,'consentId','long',NULL,'Yes','2009-01-24 21:25:56'),(2040,'lava','crms','consent','patient','patientconsent','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-24 21:25:56'),(2041,'lava','crms','consent','caregiver','patientconsent','CareID','int',NULL,10,0,3,'caregiver','many-to-one','edu.ucsf.memory.lava.model.Caregiver','No','2009-01-24 21:25:56'),(2042,'lava','crms','consent','projName','patientconsent','ProjName','varchar',25,NULL,NULL,4,'projName','string',NULL,'Yes','2009-01-24 21:25:56'),(2043,'lava','crms','consent','hipaa','patientconsent','HIPAA','tinyint',NULL,NULL,NULL,5,'hipaa','byte',NULL,'No','2012-01-21 12:00:00'),(2044,'lava','crms','consent','consentType','patientconsent','ConsentType','varchar',50,NULL,NULL,5,'consentType','string',NULL,'Yes','2009-01-24 21:25:56'),(2045,'lava','crms','consent','consentDate','patientconsent','ConsentDate','smalldatetime',NULL,16,0,6,'consentDate','timestamp',NULL,'Yes','2009-01-24 21:25:56'),(2046,'lava','crms','consent','expirationDate','patientconsent','ExpirationDate','smalldatetime',NULL,16,0,7,'expirationDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2047,'lava','crms','consent','withdrawlDate','patientconsent','WithdrawlDate','smalldatetime',NULL,16,0,8,'withdrawlDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2048,'lava','crms','consent','note','patientconsent','Note','varchar',100,NULL,NULL,9,'note','string',NULL,'No','2009-01-24 21:25:56'),(2049,'lava','crms','consent','capacityReviewBy','patientconsent','CapacityReviewBy','varchar',25,NULL,NULL,10,'capacityReviewBy','string',NULL,'No','2009-01-24 21:25:56'),(2050,'lava','crms','consent','consentRevision','patientconsent','ConsentRevision','varchar',10,NULL,NULL,11,'consentRevision','string',NULL,'No','2009-01-24 21:25:56'),(2051,'lava','crms','consent','consentDeclined','patientconsent','ConsentDeclined','varchar',10,NULL,NULL,12,'consentDeclined','string',NULL,'No','2009-01-24 21:25:56'),(2052,'lava','crms','contactInfo','id','contactinfo','CInfoID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(2053,'lava','crms','contactInfo','patient','contactinfo','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-24 21:25:56'),(2054,'lava','crms','contactInfo','caregiverId','contactinfo','CareID','int',NULL,10,0,3,'caregiverId','long',NULL,'No','2009-01-24 21:25:56'),(2055,'lava','crms','contactInfo','contactPatient','contactinfo','ContactPT','smallint',NULL,5,0,4,'contactPatient','short',NULL,'No','2009-01-24 21:25:56'),(2056,'lava','crms','contactInfo','isPatientResidence','contactinfo','IsPTResidence','smallint',NULL,5,0,5,'isPatientResidence','short',NULL,'No','2009-01-24 21:25:56'),(2057,'lava','crms','contactInfo','optOutMac','contactinfo','OptOutMAC','smallint',NULL,5,0,6,'optOutMac','short',NULL,'No','2009-01-24 21:25:56'),(2058,'lava','crms','contactInfo','optOutAffiliates','contactinfo','OptOutAffiliates','smallint',NULL,5,0,7,'optOutAffiliates','short',NULL,'No','2009-01-24 21:25:56'),(2059,'lava','crms','contactInfo','active','contactinfo','ActiveFlag','smallint',NULL,5,0,8,'active','short',NULL,'No','2009-01-24 21:25:56'),(2060,'lava','crms','contactInfo','address','contactinfo','Address','varchar',100,NULL,NULL,9,'address','string',NULL,'No','2009-01-24 21:25:56'),(2061,'lava','crms','contactInfo','address2','contactinfo','Address2','varchar',100,NULL,NULL,10,'address2','string',NULL,'No','2009-01-24 21:25:56'),(2062,'lava','crms','contactInfo','city','contactinfo','City','varchar',50,NULL,NULL,11,'city','string',NULL,'No','2009-01-24 21:25:56'),(2063,'lava','crms','contactInfo','state','contactinfo','State','char',10,NULL,NULL,12,'state','character',NULL,'No','2009-01-24 21:25:56'),(2064,'lava','crms','contactInfo','zip','contactinfo','Zip','varchar',10,NULL,NULL,13,'zip','string',NULL,'No','2009-01-24 21:25:56'),(2065,'lava','crms','contactInfo','country','contactinfo','Country','varchar',50,NULL,NULL,14,'country','string',NULL,'No','2009-01-24 21:25:56'),(2066,'lava','crms','contactInfo','phone1','contactinfo','Phone1','varchar',25,NULL,NULL,15,'phone1','string',NULL,'No','2009-01-24 21:25:56'),(2067,'lava','crms','contactInfo','phoneType1','contactinfo','PhoneType1','varchar',10,NULL,NULL,16,'phoneType1','string',NULL,'No','2009-01-24 21:25:56'),(2068,'lava','crms','contactInfo','phone2','contactinfo','Phone2','varchar',25,NULL,NULL,17,'phone2','string',NULL,'No','2009-01-24 21:25:56'),(2069,'lava','crms','contactInfo','phoneType2','contactinfo','PhoneType2','varchar',10,NULL,NULL,18,'phoneType2','string',NULL,'No','2009-01-24 21:25:56'),(2070,'lava','crms','contactInfo','phone3','contactinfo','Phone3','varchar',25,NULL,NULL,19,'phone3','string',NULL,'No','2009-01-24 21:25:56'),(2071,'lava','crms','contactInfo','phoneType3','contactinfo','PhoneType3','varchar',10,NULL,NULL,20,'phoneType3','string',NULL,'No','2009-01-24 21:25:56'),(2072,'lava','crms','contactInfo','email','contactinfo','Email','varchar',100,NULL,NULL,21,'email','string',NULL,'No','2009-01-24 21:25:56'),(2073,'lava','crms','contactInfo','notes','contactinfo','Notes','varchar',250,NULL,NULL,22,'notes','string',NULL,'No','2009-01-24 21:25:56'),(2074,'lava','crms','contactInfo','contactNameRev','contactinfo','ContactNameRev','varchar',100,NULL,NULL,23,'contactNameRev','string',NULL,'No','2009-01-24 21:25:56'),(2075,'lava','crms','contactInfo','contactDesc','contactinfo','ContactDesc','varchar',100,NULL,NULL,24,'contactDesc','string',NULL,'No','2009-01-24 21:25:56'),(2076,'lava','crms','contactLog','id','contactlog','LogID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(2077,'lava','crms','contactLog','patient','contactlog','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-24 21:25:56'),(2078,'lava','crms','contactLog','projName','contactlog','ProjName','varchar',25,NULL,NULL,3,'projName','string',NULL,'No','2009-01-24 21:25:56'),(2079,'lava','crms','contactLog','logDate','contactlog','LogDate','smalldatetime',NULL,16,0,4,'logDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2080,'lava','crms','contactLog','method','contactlog','Method','varchar',25,NULL,NULL,5,'method','string',NULL,'Yes','2009-01-24 21:25:56'),(2081,'lava','crms','contactLog','staffInit','contactlog','StaffInit','smallint',NULL,5,0,6,'staffInit','short',NULL,'Yes','2009-01-24 21:25:56'),(2082,'lava','crms','contactLog','staff','contactlog','Staff','varchar',50,NULL,NULL,7,'staff','string',NULL,'No','2009-01-24 21:25:56'),(2083,'lava','crms','contactLog','contact','contactlog','Contact','varchar',50,NULL,NULL,8,'contact','string',NULL,'No','2009-01-24 21:25:56'),(2084,'lava','crms','contactLog','note','contactlog','Note','text',16,NULL,NULL,9,'note','string',NULL,'No','2009-01-24 21:25:56'),(2085,'lava','crms','crmsFile','id','crms_file','id','int',NULL,10,0,1,'id','long',NULL,'Yes','2011-10-20 14:49:06'),(2086,'lava','crms','crmsFile','pidn','crms_file','pidn','int',NULL,10,0,2,'pidn','long',NULL,'No','2011-10-20 14:49:06'),(2087,'lava','crms','crmsFile','patient','crms_file','pidn','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.lava.crms.people.model.Patient','No','2011-10-20 14:55:15'),(2088,'lava','crms','crmsFile','enrollmentStatus','crms_file','enroll_stat_id','int',NULL,10,0,3,'enrollmentStatus','many-to-one','edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus','No','2011-10-20 14:55:15'),(2089,'lava','crms','crmsFile','enrollStatId','crms_file','enroll_stat_id','int',NULL,10,0,3,'enrollStatId','long',NULL,'No','2011-10-20 14:49:06'),(2090,'lava','crms','crmsFile','vid','crms_file','vid','int',NULL,10,0,4,'vid','long',NULL,'No','2011-10-20 14:49:06'),(2091,'lava','crms','crmsFile','visit','crms_file','vid','int',NULL,10,0,4,'visit','many-to-one','edu.ucsf.lava.crms.scheduling.model.Visit','No','2011-10-20 14:55:15'),(2092,'lava','crms','crmsFile','instrument','crms_file','instr_id','int',NULL,10,0,5,'instrument','many-to-one','edu.ucsf.lava.crms.assessment.model.InstrumentTracking','No','2011-10-20 14:55:15'),(2093,'lava','crms','crmsFile','instrId','crms_file','instr_id','int',NULL,10,0,5,'instrId','long',NULL,'No','2011-10-20 14:49:06'),(2094,'lava','crms','doctor','id','doctor','DocID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(2095,'lava','crms','doctor','lastName','doctor','LName','varchar',25,NULL,NULL,2,'lastName','string',NULL,'Yes','2009-01-24 21:25:56'),(2096,'lava','crms','doctor','middleInitial','doctor','MInitial','char',1,NULL,NULL,3,'middleInitial','character',NULL,'No','2009-01-24 21:25:56'),(2097,'lava','crms','doctor','firstName','doctor','FName','varchar',25,NULL,NULL,4,'firstName','string',NULL,'Yes','2009-01-24 21:25:56'),(2098,'lava','crms','doctor','address','doctor','Address','varchar',100,NULL,NULL,5,'address','string',NULL,'No','2009-01-24 21:25:56'),(2099,'lava','crms','doctor','city','doctor','City','varchar',50,NULL,NULL,6,'city','string',NULL,'No','2009-01-24 21:25:56'),(2100,'lava','crms','doctor','state','doctor','State','char',2,NULL,NULL,7,'state','character',NULL,'No','2009-01-24 21:25:56'),(2101,'lava','crms','doctor','zip','doctor','Zip','varchar',10,NULL,NULL,8,'zip','string',NULL,'No','2009-01-24 21:25:56'),(2102,'lava','crms','doctor','phone1','doctor','Phone1','varchar',25,NULL,NULL,9,'phone1','string',NULL,'No','2009-01-24 21:25:56'),(2103,'lava','crms','doctor','phoneType1','doctor','PhoneType1','varchar',10,NULL,NULL,10,'phoneType1','string',NULL,'No','2009-01-24 21:25:56'),(2104,'lava','crms','doctor','phone2','doctor','Phone2','varchar',25,NULL,NULL,11,'phone2','string',NULL,'No','2009-01-24 21:25:56'),(2105,'lava','crms','doctor','phoneType2','doctor','PhoneType2','varchar',10,NULL,NULL,12,'phoneType2','string',NULL,'No','2009-01-24 21:25:56'),(2106,'lava','crms','doctor','phone3','doctor','Phone3','varchar',25,NULL,NULL,13,'phone3','string',NULL,'No','2009-01-24 21:25:56'),(2107,'lava','crms','doctor','phoneType3','doctor','PhoneType3','varchar',10,NULL,NULL,14,'phoneType3','string',NULL,'No','2009-01-24 21:25:56'),(2108,'lava','crms','doctor','email','doctor','Email','varchar',100,NULL,NULL,15,'email','string',NULL,'No','2009-01-24 21:25:56'),(2109,'lava','crms','doctor','docType','doctor','DocType','varchar',50,NULL,NULL,16,'docType','string',NULL,'No','2009-01-24 21:25:56'),(2110,'lava','crms','doctor','fullNameRev','doctor','FullNameRev','varchar',55,NULL,NULL,17,'fullNameRev','string',NULL,'No','2009-01-24 21:25:56'),(2111,'lava','crms','doctor','fullName','doctor','FullName','varchar',54,NULL,NULL,18,'fullName','string',NULL,'No','2009-01-24 21:25:56'),(2112,'lava','crms','enrollmentStatus','id','enrollmentstatus','EnrollStatID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(2113,'lava','crms','enrollmentStatus','patient','enrollmentstatus','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-24 21:25:56'),(2114,'lava','crms','enrollmentStatus','projName','enrollmentstatus','projName','varchar',75,NULL,NULL,3,'projName','string',NULL,'Yes','2009-01-24 21:25:56'),(2115,'lava','crms','enrollmentStatus','referralSource','enrollmentstatus','ReferralSource','varchar',75,NULL,NULL,4,'referralSource','string',NULL,'No','2009-01-24 21:25:56'),(2116,'lava','crms','enrollmentStatus','subjectStudyId','enrollmentstatus','SubjectStudyID','varchar',10,NULL,NULL,4,'studySubjectId','string',NULL,'No','2009-01-24 21:25:56'),(2117,'lava','crms','enrollmentStatus','latestDesc','enrollmentstatus','LatestDesc','varchar',25,NULL,NULL,5,'latestDesc','string',NULL,'No','2009-01-24 21:25:56'),(2118,'lava','crms','enrollmentStatus','latestDate','enrollmentstatus','LatestDate','smalldatetime',NULL,16,0,6,'latestDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2119,'lava','crms','enrollmentStatus','latestNote','enrollmentstatus','LatestNote','varchar',100,NULL,NULL,7,'latestNote','string',NULL,'No','2009-01-24 21:25:56'),(2120,'lava','crms','enrollmentStatus','referredDesc','enrollmentstatus','ReferredDesc','varchar',25,NULL,NULL,9,'referredDesc','string',NULL,'No','2009-01-24 21:25:56'),(2121,'lava','crms','enrollmentStatus','referredDate','enrollmentstatus','ReferredDate','smalldatetime',NULL,16,0,10,'referredDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2122,'lava','crms','enrollmentStatus','referredNote','enrollmentstatus','ReferredNote','varchar',100,NULL,NULL,11,'referredNote','string',NULL,'No','2009-01-24 21:25:56'),(2123,'lava','crms','enrollmentStatus','deferredDesc','enrollmentstatus','DeferredDesc','varchar',25,NULL,NULL,13,'deferredDesc','string',NULL,'No','2009-01-24 21:25:56'),(2124,'lava','crms','enrollmentStatus','deferredDate','enrollmentstatus','DeferredDate','smalldatetime',NULL,16,0,14,'deferredDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2125,'lava','crms','enrollmentStatus','deferredNote','enrollmentstatus','DeferredNote','varchar',100,NULL,NULL,15,'deferredNote','string',NULL,'No','2009-01-24 21:25:56'),(2126,'lava','crms','enrollmentStatus','eligibleDesc','enrollmentstatus','EligibleDesc','varchar',25,NULL,NULL,17,'eligibleDesc','string',NULL,'No','2009-01-24 21:25:56'),(2127,'lava','crms','enrollmentStatus','eligibleDate','enrollmentstatus','EligibleDate','smalldatetime',NULL,16,0,18,'eligibleDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2128,'lava','crms','enrollmentStatus','eligibleNote','enrollmentstatus','EligibleNote','varchar',100,NULL,NULL,19,'eligibleNote','string',NULL,'No','2009-01-24 21:25:56'),(2129,'lava','crms','enrollmentStatus','ineligibleDesc','enrollmentstatus','IneligibleDesc','varchar',25,NULL,NULL,21,'ineligibleDesc','string',NULL,'No','2009-01-24 21:25:56'),(2130,'lava','crms','enrollmentStatus','ineligibleDate','enrollmentstatus','IneligibleDate','smalldatetime',NULL,16,0,22,'ineligibleDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2131,'lava','crms','enrollmentStatus','ineligibleNote','enrollmentstatus','IneligibleNote','varchar',100,NULL,NULL,23,'ineligibleNote','string',NULL,'No','2009-01-24 21:25:56'),(2132,'lava','crms','enrollmentStatus','declinedDesc','enrollmentstatus','DeclinedDesc','varchar',25,NULL,NULL,25,'declinedDesc','string',NULL,'No','2009-01-24 21:25:56'),(2133,'lava','crms','enrollmentStatus','declinedDate','enrollmentstatus','DeclinedDate','smalldatetime',NULL,16,0,26,'declinedDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2134,'lava','crms','enrollmentStatus','declinedNote','enrollmentstatus','DeclinedNote','varchar',100,NULL,NULL,27,'declinedNote','string',NULL,'No','2009-01-24 21:25:56'),(2135,'lava','crms','enrollmentStatus','enrolledDesc','enrollmentstatus','EnrolledDesc','varchar',25,NULL,NULL,29,'enrolledDesc','string',NULL,'No','2009-01-24 21:25:56'),(2136,'lava','crms','enrollmentStatus','enrolledDate','enrollmentstatus','EnrolledDate','smalldatetime',NULL,16,0,30,'enrolledDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2137,'lava','crms','enrollmentStatus','enrolledNote','enrollmentstatus','EnrolledNote','varchar',100,NULL,NULL,31,'enrolledNote','string',NULL,'No','2009-01-24 21:25:56'),(2138,'lava','crms','enrollmentStatus','excludedDesc','enrollmentstatus','ExcludedDesc','varchar',25,NULL,NULL,33,'excludedDesc','string',NULL,'No','2009-01-24 21:25:56'),(2139,'lava','crms','enrollmentStatus','excludedDate','enrollmentstatus','ExcludedDate','smalldatetime',NULL,16,0,34,'excludedDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2140,'lava','crms','enrollmentStatus','excludedNote','enrollmentstatus','ExcludedNote','varchar',100,NULL,NULL,35,'excludedNote','string',NULL,'No','2009-01-24 21:25:56'),(2141,'lava','crms','enrollmentStatus','withdrewDesc','enrollmentstatus','WithdrewDesc','varchar',25,NULL,NULL,37,'withdrewDesc','string',NULL,'No','2009-01-24 21:25:56'),(2142,'lava','crms','enrollmentStatus','withdrewDate','enrollmentstatus','WithdrewDate','smalldatetime',NULL,16,0,38,'withdrewDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2143,'lava','crms','enrollmentStatus','withdrewNote','enrollmentstatus','WithdrewNote','varchar',100,NULL,NULL,39,'withdrewNote','string',NULL,'No','2009-01-24 21:25:56'),(2144,'lava','crms','enrollmentStatus','inactiveDesc','enrollmentstatus','InactiveDesc','varchar',25,NULL,NULL,41,'inactiveDesc','string',NULL,'No','2009-01-24 21:25:56'),(2145,'lava','crms','enrollmentStatus','inactiveDate','enrollmentstatus','InactiveDate','smalldatetime',NULL,16,0,42,'inactiveDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2146,'lava','crms','enrollmentStatus','inactiveNote','enrollmentstatus','InactiveNote','varchar',100,NULL,NULL,43,'inactiveNote','string',NULL,'No','2009-01-24 21:25:56'),(2147,'lava','crms','enrollmentStatus','deceasedDesc','enrollmentstatus','DeceasedDesc','varchar',25,NULL,NULL,49,'deceasedDesc','string',NULL,'No','2009-01-24 21:25:56'),(2148,'lava','crms','enrollmentStatus','deceasedDate','enrollmentstatus','DeceasedDate','smalldatetime',NULL,16,0,50,'deceasedDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2149,'lava','crms','enrollmentStatus','deceasedNote','enrollmentstatus','DeceasedNote','varchar',100,NULL,NULL,51,'deceasedNote','string',NULL,'No','2009-01-24 21:25:56'),(2150,'lava','crms','enrollmentStatus','autopsyDesc','enrollmentstatus','AutopsyDesc','varchar',25,NULL,NULL,53,'autopsyDesc','string',NULL,'No','2009-01-24 21:25:56'),(2151,'lava','crms','enrollmentStatus','autopsyDate','enrollmentstatus','AutopsyDate','smalldatetime',NULL,16,0,54,'autopsyDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2152,'lava','crms','enrollmentStatus','autopsyNote','enrollmentstatus','AutopsyNote','varchar',100,NULL,NULL,55,'autopsyNote','string',NULL,'No','2009-01-24 21:25:56'),(2153,'lava','crms','enrollmentStatus','closedDesc','enrollmentstatus','ClosedDesc','varchar',25,NULL,NULL,56,'closedDesc','string',NULL,'No','2009-01-24 21:25:56'),(2154,'lava','crms','enrollmentStatus','closedDate','enrollmentstatus','ClosedDate','smalldatetime',NULL,16,0,57,'closedDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2155,'lava','crms','enrollmentStatus','closedNote','enrollmentstatus','ClosedNote','varchar',100,NULL,NULL,58,'closedNote','string',NULL,'No','2009-01-24 21:25:56'),(2156,'lava','crms','enrollmentStatus','enrollmentNotes','enrollmentstatus','EnrollmentNotes','varchar',500,NULL,NULL,59,'enrollmentNotes','string',NULL,'No','2009-01-24 21:25:56'),(2157,'lava','crms','instrument','id','instrumenttracking','InstrID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(2158,'lava','crms','instrument','visit','instrumenttracking','VID','int',NULL,10,0,2,'visit','many-to-one','edu.ucsf.memory.lava.model.Visit','Yes','2009-01-24 21:25:56'),(2159,'lava','crms','instrument','projName','instrumenttracking','ProjName','varchar',25,NULL,NULL,3,'projName','string',NULL,'Yes','2009-01-24 21:25:56'),(2160,'lava','crms','instrument','patient','instrumenttracking','PIDN','int',NULL,10,0,4,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-24 21:25:56'),(2161,'lava','crms','instrument','instrType','instrumenttracking','InstrType','varchar',25,NULL,NULL,5,'instrType','string',NULL,'Yes','2009-01-24 21:25:56'),(2162,'lava','crms','instrument','instrVer','instrumenttracking','InstrVer','varchar',25,NULL,NULL,6,'instrVer','string',NULL,'No','2009-01-24 21:25:56'),(2163,'lava','crms','instrument','dcDate','instrumenttracking','DCDate','smalldatetime',NULL,16,0,7,'dcDate','timestamp',NULL,'Yes','2009-01-24 21:25:56'),(2164,'lava','crms','instrument','dcBy','instrumenttracking','DCBy','varchar',25,NULL,NULL,8,'dcBy','string',NULL,'No','2009-01-24 21:25:56'),(2165,'lava','crms','instrument','dcStatus','instrumenttracking','DCStatus','varchar',25,NULL,NULL,9,'dcStatus','string',NULL,'Yes','2009-01-24 21:25:56'),(2166,'lava','crms','instrument','dcNotes','instrumenttracking','DCNotes','varchar',255,NULL,NULL,10,'dcNotes','string',NULL,'No','2009-01-24 21:25:56'),(2167,'lava','crms','instrument','researchStatus','instrumenttracking','ResearchStatus','varchar',50,NULL,NULL,11,'researchStatus','string',NULL,'No','2009-01-24 21:25:56'),(2168,'lava','crms','instrument','qualityIssue','instrumenttracking','QualityIssue','varchar',50,NULL,NULL,12,'qualityIssue','string',NULL,'No','2009-01-24 21:25:56'),(2169,'lava','crms','instrument','qualityIssue2','instrumenttracking','QualityIssue2','varchar',50,NULL,NULL,13,'qualityIssue2','string',NULL,'No','2009-01-24 21:25:56'),(2170,'lava','crms','instrument','qualityIssue3','instrumenttracking','QualityIssue3','varchar',50,NULL,NULL,14,'qualityIssue3','string',NULL,'No','2009-01-24 21:25:56'),(2171,'lava','crms','instrument','qualityNotes','instrumenttracking','QualityNotes','varchar',100,NULL,NULL,15,'qualityNotes','string',NULL,'No','2009-01-24 21:25:56'),(2172,'lava','crms','instrument','deDate','instrumenttracking','DEDate','smalldatetime',NULL,16,0,16,'deDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2173,'lava','crms','instrument','deBy','instrumenttracking','DEBy','varchar',25,NULL,NULL,17,'deBy','string',NULL,'No','2009-01-24 21:25:56'),(2174,'lava','crms','instrument','deStatus','instrumenttracking','DEStatus','varchar',25,NULL,NULL,18,'deStatus','string',NULL,'No','2009-01-24 21:25:56'),(2175,'lava','crms','instrument','deNotes','instrumenttracking','DENotes','varchar',255,NULL,NULL,19,'deNotes','string',NULL,'No','2009-01-24 21:25:56'),(2176,'lava','crms','instrument','dvDate','instrumenttracking','DVDate','smalldatetime',NULL,16,0,20,'dvDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2177,'lava','crms','instrument','dvBy','instrumenttracking','DVBy','varchar',25,NULL,NULL,21,'dvBy','string',NULL,'No','2009-01-24 21:25:56'),(2178,'lava','crms','instrument','dvStatus','instrumenttracking','DVStatus','varchar',25,NULL,NULL,22,'dvStatus','string',NULL,'No','2009-01-24 21:25:56'),(2179,'lava','crms','instrument','dvNotes','instrumenttracking','DVNotes','varchar',255,NULL,NULL,23,'dvNotes','string',NULL,'No','2009-01-24 21:25:56'),(2180,'lava','crms','instrument','fieldStatus','instrumenttracking','FieldStatus','smallint',NULL,5,0,25,'fieldStatus','short',NULL,'No','2009-01-24 21:25:56'),(2181,'lava','crms','instrument','ageAtDC','instrumenttracking','AgeAtDC','smallint',NULL,5,0,27,'ageAtDC','short',NULL,'No','2009-01-24 21:25:56'),(2182,'lava','crms','instrument','instrumentAuditCreated','instrumenttracking','Audit_Created','datetime',NULL,23,3,28,'instrumentAuditCreated','timestamp',NULL,'Yes','2009-01-24 21:25:56'),(2183,'lava','crms','instrument','instrumentAuditEffDate','instrumenttracking','Audit_EffDate','datetime',NULL,23,3,29,'instrumentAuditEffDate','timestamp',NULL,'Yes','2009-01-24 21:25:56'),(2184,'lava','crms','instrument','instrumentAuditExpDate','instrumenttracking','Audit_ExpDate','datetime',NULL,23,3,30,'instrumentAuditExpDate','timestamp',NULL,'Yes','2009-01-24 21:25:56'),(2185,'lava','crms','instrument','instrumentAuditHostName','instrumenttracking','Audit_HostName','varchar',50,NULL,NULL,31,'instrumentAuditHostName','string',NULL,'No','2009-01-24 21:25:56'),(2186,'lava','crms','instrument','instrumentAuditUsername','instrumenttracking','Audit_Username','varchar',50,NULL,NULL,32,'instrumentAuditUsername','string',NULL,'No','2009-01-24 21:25:56'),(2187,'lava','crms','patient','id','patient','PIDN','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(2188,'lava','crms','patient','lastName','patient','LName','varchar',25,NULL,NULL,2,'lastName','string',NULL,'Yes','2009-01-24 21:25:56'),(2189,'lava','crms','patient','middleInitial','patient','MInitial','char',1,NULL,NULL,3,'middleInitial','character',NULL,'No','2009-01-24 21:25:56'),(2190,'lava','crms','patient','firstName','patient','FName','varchar',25,NULL,NULL,4,'firstName','string',NULL,'Yes','2009-01-24 21:25:56'),(2191,'lava','crms','patient','suffix','patient','Suffix','varchar',15,NULL,NULL,5,'suffix','string',NULL,'No','2009-01-24 21:25:56'),(2192,'lava','crms','patient','degree','patient','Degree','varchar',15,NULL,NULL,6,'degree','string',NULL,'No','2009-01-24 21:25:56'),(2193,'lava','crms','patient','ucid','patient','UCID','varchar',20,NULL,NULL,7,'ucid','string',NULL,'No','2009-01-24 21:25:56'),(2194,'lava','crms','patient','ssn','patient','SSN','nvarchar',30,NULL,NULL,8,'ssn','string',NULL,'No','2009-01-24 21:25:56'),(2195,'lava','crms','patient','birthDate','patient','DOB','smalldatetime',NULL,16,0,9,'birthDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2196,'lava','crms','patient','gender','patient','Gender','tinyint',NULL,3,0,11,'gender','byte',NULL,'No','2009-01-24 21:25:56'),(2197,'lava','crms','patient','hand','patient','Hand','varchar',25,NULL,NULL,12,'hand','string',NULL,'No','2009-01-24 21:25:56'),(2198,'lava','crms','patient','deceased','patient','Deceased','bit',NULL,1,0,13,'deceased','boolean',NULL,'Yes','2009-01-24 21:25:56'),(2199,'lava','crms','patient','deathDate','patient','DOD','smalldatetime',NULL,16,0,14,'deathDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2200,'lava','crms','patient','transNeeded','patient','TransNeeded','bit',NULL,1,0,15,'transNeeded','boolean',NULL,'No','2009-01-24 21:25:56'),(2201,'lava','crms','patient','primaryLanguage','patient','PrimaryLanguage','varchar',25,NULL,NULL,16,'primaryLanguage','string',NULL,'No','2009-01-24 21:25:56'),(2202,'lava','crms','patient','testingLanguage','patient','testingLanguage','varchar',25,NULL,NULL,16,'testingLanguage','string',NULL,'No','2009-01-24 21:25:56'),(2203,'lava','crms','patient','transLanguage','patient','TransLanguage','varchar',25,NULL,NULL,16,'transLanguage','string',NULL,'No','2009-01-24 21:25:56'),(2204,'lava','crms','patient','enterBy','patient','EnterBy','varchar',25,NULL,NULL,17,'enterBy','string',NULL,'No','2009-01-24 21:25:56'),(2205,'lava','crms','patient','dupNameFlag','patient','DupNameFlag','bit',NULL,1,0,18,'dupNameFlag','boolean',NULL,'Yes','2009-01-24 21:25:56'),(2206,'lava','crms','patient','fullNameRev','patient','FullNameRev','varchar',100,NULL,NULL,19,'fullNameRev','string',NULL,'No','2009-01-24 21:25:56'),(2207,'lava','crms','patient','fullName','patient','FullName','varchar',65,NULL,NULL,20,'fullName','string',NULL,'No','2009-01-24 21:25:56'),(2208,'lava','crms','patient','fullNameRevNoSuffix','patient','FullNameRevNoSuffix','varchar',66,NULL,NULL,21,'fullNameRevNoSuffix','string',NULL,'No','2009-01-24 21:25:56'),(2209,'lava','crms','patient','fullNameNoSuffix','patient','FullNameNoSuffix','varchar',65,NULL,NULL,22,'fullNameNoSuffix','string',NULL,'No','2009-01-24 21:25:56'),(2210,'lava','crms','patientDoctor','id','patientdoctor','PIDNDocID','int',NULL,10,0,1,'id','integer',NULL,'Yes','2009-01-24 21:25:56'),(2211,'lava','crms','patientDoctor','doctor','patientdoctor','DocID','int',NULL,10,0,2,'doctor','many-to-one','edu.ucsf.memory.lava.model.Doctor','Yes','2009-01-24 21:25:56'),(2212,'lava','crms','patientDoctor','patient','patientdoctor','PIDN','int',NULL,10,0,3,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-24 21:25:56'),(2213,'lava','crms','patientDoctor','docStat','patientdoctor','DocStat','varchar',25,NULL,NULL,4,'docStat','string',NULL,'No','2009-01-24 21:25:56'),(2214,'lava','crms','patientDoctor','docNote','patientdoctor','DocNote','varchar',100,NULL,NULL,5,'docNote','string',NULL,'No','2009-01-24 21:25:56'),(2215,'lava','crms','task','id','tasks','TaskID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(2216,'lava','crms','task','patient','tasks','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-24 21:25:56'),(2217,'lava','crms','task','projName','tasks','ProjName','varchar',75,NULL,NULL,3,'projName','string',NULL,'No','2009-01-24 21:25:56'),(2218,'lava','crms','task','openedDate','tasks','OpenedDate','smalldatetime',NULL,16,0,4,'openedDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2219,'lava','crms','task','openedBy','tasks','OpenedBy','varchar',25,NULL,NULL,5,'openedBy','string',NULL,'No','2009-01-24 21:25:56'),(2220,'lava','crms','task','taskType','tasks','TaskType','varchar',25,NULL,NULL,6,'taskType','string',NULL,'No','2009-01-24 21:25:56'),(2221,'lava','crms','task','taskDesc','tasks','TaskDesc','varchar',255,NULL,NULL,7,'taskDesc','string',NULL,'No','2009-01-24 21:25:56'),(2222,'lava','crms','task','dueDate','tasks','DueDate','smalldatetime',NULL,16,0,8,'dueDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2223,'lava','crms','task','taskStatus','tasks','TaskStatus','varchar',50,NULL,NULL,9,'taskStatus','string',NULL,'No','2009-01-24 21:25:56'),(2224,'lava','crms','task','assignedTo','tasks','AssignedTo','varchar',25,NULL,NULL,10,'assignedTo','string',NULL,'No','2009-01-24 21:25:56'),(2225,'lava','crms','task','workingNotes','tasks','WorkingNotes','varchar',255,NULL,NULL,11,'workingNotes','string',NULL,'No','2009-01-24 21:25:56'),(2226,'lava','crms','task','closedDate','tasks','ClosedDate','smalldatetime',NULL,16,0,12,'closedDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2227,'lava','crms','visit','id','visit','VID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-24 21:25:56'),(2228,'lava','crms','visit','patient','visit','PIDN','int',NULL,10,0,2,'patient','many-to-one','edu.ucsf.memory.lava.model.Patient','Yes','2009-01-24 21:25:56'),(2229,'lava','crms','visit','projName','visit','ProjName','varchar',25,NULL,NULL,3,'projName','string',NULL,'Yes','2009-01-24 21:25:56'),(2230,'lava','crms','visit','visitLocation','visit','VLocation','varchar',25,NULL,NULL,4,'visitLocation','string',NULL,'Yes','2009-01-24 21:25:56'),(2231,'lava','crms','visit','visitType','visit','VType','varchar',25,NULL,NULL,5,'visitType','string',NULL,'Yes','2009-01-24 21:25:56'),(2232,'lava','crms','visit','visitWith','visit','VWith','varchar',25,NULL,NULL,6,'visitWith','string',NULL,'No','2009-01-24 21:25:56'),(2233,'lava','crms','visit','visitDate','visit','VDate','smalldatetime',NULL,16,0,7,'visitDate','timestamp',NULL,'Yes','2009-01-24 21:25:56'),(2234,'lava','crms','visit','visitStatus','visit','VStatus','varchar',25,NULL,NULL,8,'visitStatus','string',NULL,'Yes','2009-01-24 21:25:56'),(2235,'lava','crms','visit','visitNote','visit','VNotes','varchar',255,NULL,NULL,9,'visitNote','string',NULL,'No','2009-01-24 21:25:56'),(2236,'lava','crms','visit','followUpMonth','visit','FUMonth','char',3,NULL,NULL,10,'followUpMonth','character',NULL,'No','2009-01-24 21:25:56'),(2237,'lava','crms','visit','followUpYear','visit','FUYear','char',4,NULL,NULL,11,'followUpYear','character',NULL,'No','2009-01-24 21:25:56'),(2238,'lava','crms','visit','followUpNote','visit','FUNote','varchar',100,NULL,NULL,12,'followUpNote','string',NULL,'No','2009-01-24 21:25:56'),(2239,'lava','crms','visit','waitList','visit','WList','varchar',25,NULL,NULL,13,'waitList','string',NULL,'No','2009-01-24 21:25:56'),(2240,'lava','crms','visit','waitListNote','visit','WListNote','varchar',100,NULL,NULL,14,'waitListNote','string',NULL,'No','2009-01-24 21:25:56'),(2241,'lava','crms','visit','waitListDate','visit','WListDate','smalldatetime',NULL,16,0,15,'waitListDate','timestamp',NULL,'No','2009-01-24 21:25:56'),(2242,'lava','crms','visit','visitDescrip','visit','VShortDesc','varchar',64,NULL,NULL,16,'visitDescrip','string',NULL,'No','2009-01-24 21:25:56'),(2243,'lava','crms','visit','ageAtVisit','visit','AgeAtVisit','smallint',NULL,5,0,17,'ageAtVisit','short',NULL,'No','2009-01-24 21:25:56');
/*!40000 ALTER TABLE `hibernateproperty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrument`
--

DROP TABLE IF EXISTS `instrument`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `instrument` (
  `InstrID` int(10) NOT NULL auto_increment,
  `InstrName` varchar(25) NOT NULL,
  `TableName` varchar(25) NOT NULL,
  `FormName` varchar(50) default NULL,
  `Category` varchar(25) default NULL,
  `HasVersion` tinyint(1) NOT NULL default '0',
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`InstrID`),
  UNIQUE KEY `InstrName` (`InstrName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `instrumentnotes` (
  `InstrID` int(10) NOT NULL,
  `Section` varchar(50) NOT NULL,
  `Note` varchar(2000) default NULL,
  KEY `instrumentnotes__instrID` (`InstrID`),
  CONSTRAINT `instrumentnotes__instrID` FOREIGN KEY (`InstrID`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `instrumentsummary` (
  `InstrID` int(10) NOT NULL,
  `Summary` varchar(500) default NULL,
  PRIMARY KEY  (`InstrID`),
  KEY `instrumentsummary__InstrID` (`InstrID`),
  CONSTRAINT `instrumentsummary__InstrID` FOREIGN KEY (`InstrID`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `instrumenttracking` (
  `InstrID` int(10) NOT NULL auto_increment,
  `VID` int(10) NOT NULL,
  `ProjName` varchar(75) NOT NULL,
  `PIDN` int(10) NOT NULL,
  `InstrType` varchar(25) NOT NULL,
  `InstrVer` varchar(25) default NULL,
  `DCDate` datetime NOT NULL,
  `DCBy` varchar(25) default NULL,
  `DCStatus` varchar(25) NOT NULL,
  `DCNotes` varchar(255) default NULL,
  `ResearchStatus` varchar(50) default NULL,
  `QualityIssue` varchar(50) default NULL,
  `QualityIssue2` varchar(50) default NULL,
  `QualityIssue3` varchar(50) default NULL,
  `QualityNotes` varchar(100) default NULL,
  `DEDate` datetime default NULL,
  `DEBy` varchar(25) default NULL,
  `DEStatus` varchar(25) default NULL,
  `DENotes` varchar(255) default NULL,
  `DVDate` datetime default NULL,
  `DVBy` varchar(25) default NULL,
  `DVStatus` varchar(25) default NULL,
  `DVNotes` varchar(255) default NULL,
  `latestflag` tinyint(1) NOT NULL default '0',
  `FieldStatus` smallint(5) default NULL,
  `AgeAtDC` smallint(5) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`InstrID`),
  KEY `AgeLookup` (`InstrID`,`AgeAtDC`),
  KEY `PIDN_InstrType_DCDate_DCStatus` (`PIDN`,`InstrType`,`DCDate`,`DCStatus`),
  KEY `instrumenttracking__InstrType` (`InstrType`),
  KEY `instrumenttracking__VID` (`VID`),
  KEY `instrumenttracking__ProjName` (`ProjName`),
  KEY `instrumenttracking__PIDN` (`PIDN`),
  KEY `instrumenttracking__authfilter` (`PIDN`,`ProjName`,`InstrID`),
  CONSTRAINT `instrumenttracking__InstrType` FOREIGN KEY (`InstrType`) REFERENCES `instrument` (`InstrName`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `instrumenttracking__ProjName` FOREIGN KEY (`ProjName`) REFERENCES `projectunit` (`ProjUnitDesc`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `instrumenttracking__VID` FOREIGN KEY (`VID`) REFERENCES `visit` (`VID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `insttumenttracking__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `instrumenttracking`
--

LOCK TABLES `instrumenttracking` WRITE;
/*!40000 ALTER TABLE `instrumenttracking` DISABLE KEYS */;
/*!40000 ALTER TABLE `instrumenttracking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lava_file`
--

DROP TABLE IF EXISTS `lava_file`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lava_file` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `file_type` varchar(255) default NULL,
  `content_type` varchar(100) default NULL,
  `file_status_date` date default NULL,
  `file_status` varchar(50) default NULL,
  `file_status_by` varchar(50) default NULL,
  `repository_id` varchar(100) default NULL,
  `file_id` varchar(100) default NULL,
  `location` varchar(1000) default NULL,
  `checksum` varchar(100) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`),
  KEY `content_type` (`id`,`content_type`),
  KEY `repository_info` (`id`,`repository_id`,`file_id`,`location`(767))
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lava_file`
--

LOCK TABLES `lava_file` WRITE;
/*!40000 ALTER TABLE `lava_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `lava_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `list`
--

DROP TABLE IF EXISTS `list`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `list` (
  `ListID` int(10) NOT NULL auto_increment,
  `ListName` varchar(50) NOT NULL,
  `instance` varchar(25) NOT NULL default 'lava',
  `scope` varchar(25) NOT NULL,
  `NumericKey` tinyint(1) NOT NULL default '0',
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ListID`),
  UNIQUE KEY `ListName` (`ListName`)
) ENGINE=InnoDB AUTO_INCREMENT=563 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `list`
--

LOCK TABLES `list` WRITE;
/*!40000 ALTER TABLE `list` DISABLE KEYS */;
INSERT INTO `list` (`ListID`, `ListName`, `instance`, `scope`, `NumericKey`, `modified`) VALUES (468,'LavaFileContentType','lava','core',0,'2011-11-04 17:41:43'),(469,'LavaFileStatus','lava','core',0,'2011-11-04 17:45:18'),(470,'LavaSessionStatus','lava','core',0,'2009-01-24 20:57:59'),(471,'NavigationListPageSize','lava','core',1,'2009-01-24 20:57:59'),(472,'TextYesNo','lava','core',0,'2009-01-24 20:57:59'),(473,'TextYesNoDK','lava','core',0,'2009-01-24 20:57:59'),(474,'TextYesNoNA','lava','core',0,'2009-01-24 20:57:59'),(475,'YESNO','lava','core',1,'2009-01-24 20:57:59'),(476,'YESNODK','lava','core',1,'2009-01-24 20:57:59'),(477,'YesNoDK_Zero','lava','core',0,'2009-01-24 20:57:59'),(478,'YesNoUnknown','lava','core',1,'2009-01-24 20:57:59'),(479,'YesNoZeroNA','lava','core',1,'2009-05-11 17:16:47'),(480,'YesNo_Zero','lava','core',0,'2009-01-24 20:57:59'),(522,'AbsentPresent','lava','crms',1,'2009-05-12 14:10:23'),(523,'CaregiverMaritalStatus','lava','crms',0,'2009-01-24 20:57:59'),(524,'ConsentType','lava','crms',0,'2009-01-24 20:57:59'),(525,'ContactMethods','lava','crms',0,'2009-01-24 20:57:59'),(526,'ContactRelations','lava','crms',0,'2009-01-24 20:57:59'),(527,'CrmsFileContentType','lava','crms',0,'2011-11-07 11:35:25'),(528,'DataCollectionStatus','lava','crms',0,'2009-01-24 20:57:59'),(529,'DataEntryStatus','lava','crms',0,'2009-01-24 20:57:59'),(530,'DataValidationStatus','lava','crms',0,'2009-01-24 20:57:59'),(531,'DoctorStatus','lava','crms',0,'2009-01-24 20:57:59'),(532,'Education','lava','crms',1,'2009-01-24 20:57:59'),(533,'Gender','lava','crms',1,'2009-01-24 20:57:59'),(534,'Handedness','lava','crms',0,'2009-01-24 20:57:59'),(535,'InstrumentQualityIssue','lava','crms',0,'2009-01-24 20:57:59'),(536,'InstrumentResearchStatus','lava','crms',0,'2009-01-24 20:57:59'),(537,'InstrumentVersions','lava','crms',0,'2009-01-24 20:57:59'),(538,'MaritalStatus','lava','crms',1,'2009-01-24 20:57:59'),(539,'NormalAbnormal','lava','crms',1,'2009-05-11 17:22:28'),(540,'Occupation','lava','crms',1,'2009-01-24 20:57:59'),(541,'PatientLanguage','lava','crms',0,'2009-01-24 20:57:59'),(542,'PhoneType','lava','crms',0,'2009-01-24 20:57:59'),(543,'PrimaryCaregiver','lava','crms',1,'2009-01-24 20:57:59'),(544,'ProbablePossibleNo','lava','crms',1,'2009-01-24 20:57:59'),(545,'ProjectStatus','lava','crms',0,'2009-01-24 20:57:59'),(546,'ProjectStatusType','lava','crms',0,'2009-01-24 20:57:59'),(547,'RACE','lava','crms',1,'2009-01-24 20:57:59'),(548,'ReferralSources','lava','crms',0,'2009-01-24 20:57:59'),(549,'SkipErrorCodes','lava','crms',0,'2009-01-24 20:57:59'),(550,'SpanishOrigin','lava','crms',1,'2009-01-24 20:57:59'),(551,'StaffList','lava','crms',0,'2009-01-24 20:57:59'),(552,'StandardErrorCodes','lava','crms',1,'2009-01-24 20:57:59'),(553,'States','lava','crms',0,'2009-01-24 20:57:59'),(554,'TaskStatus','lava','crms',0,'2009-01-24 20:57:59'),(555,'TaskType','lava','crms',0,'2009-01-24 20:57:59'),(556,'UsualSomeRare','lava','crms',0,'2009-01-24 20:57:59'),(557,'UsualSomeRareDK','lava','crms',0,'2009-01-24 20:57:59'),(558,'VisitLocations','lava','crms',0,'2009-01-24 20:57:59'),(559,'VisitStatus','lava','crms',0,'2009-01-24 20:57:59'),(560,'VisitType','lava','crms',0,'2009-01-24 20:57:59'),(561,'YesNoScale_NoCorrect','lava','crms',1,'2009-01-24 20:57:59'),(562,'YesNoScale_YesCorrect','lava','crms',1,'2009-01-24 20:57:59');
/*!40000 ALTER TABLE `list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listvalues`
--

DROP TABLE IF EXISTS `listvalues`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `listvalues` (
  `ID` int(10) NOT NULL auto_increment,
  `ListID` int(10) NOT NULL,
  `instance` varchar(25) NOT NULL default 'lava',
  `scope` varchar(25) NOT NULL,
  `ValueKey` varchar(100) NOT NULL,
  `ValueDesc` varchar(255) default NULL,
  `OrderID` int(10) NOT NULL default '0',
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ID`),
  KEY `ListID` (`ListID`),
  KEY `ValueKey` (`ValueKey`),
  KEY `listvalues__listID` (`ListID`),
  CONSTRAINT `listvalues__listID` FOREIGN KEY (`ListID`) REFERENCES `list` (`ListID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24712 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `listvalues`
--

LOCK TABLES `listvalues` WRITE;
/*!40000 ALTER TABLE `listvalues` DISABLE KEYS */;
INSERT INTO `listvalues` (`ID`, `ListID`, `instance`, `scope`, `ValueKey`, `ValueDesc`, `OrderID`, `modified`) VALUES (24376,468,'lava','core','GENERAL','Image',0,'2011-11-04 17:44:15'),(24377,468,'lava','core','GENERAL','Document',0,'2011-11-04 17:44:15'),(24378,469,'lava','core','GENERAL','Uploaded',0,'2011-11-04 17:46:17'),(24379,470,'lava','core','NEW',NULL,1,'2009-01-24 20:57:59'),(24380,470,'lava','core','ACTIVE',NULL,2,'2009-01-24 20:57:59'),(24381,470,'lava','core','LOGOFF',NULL,3,'2009-01-24 20:57:59'),(24382,470,'lava','core','EXPIRED',NULL,4,'2009-01-24 20:57:59'),(24383,470,'lava','core','DISCONNECTED',NULL,5,'2009-01-24 20:57:59'),(24384,471,'lava','core','10','10/page',0,'2009-01-24 20:57:59'),(24385,471,'lava','core','100','100/page',0,'2009-01-24 20:57:59'),(24386,471,'lava','core','15','15/page',0,'2009-01-24 20:57:59'),(24387,471,'lava','core','25','25/page',0,'2009-01-24 20:57:59'),(24388,471,'lava','core','250','250/page',0,'2009-01-24 20:57:59'),(24389,471,'lava','core','5','5/page',0,'2009-01-24 20:57:59'),(24390,471,'lava','core','50','50/page',0,'2009-01-24 20:57:59'),(24391,472,'lava','core','Yes',NULL,1,'2009-01-24 20:57:59'),(24392,472,'lava','core','No',NULL,2,'2009-01-24 20:57:59'),(24393,473,'lava','core','Yes',NULL,1,'2009-01-24 20:57:59'),(24394,473,'lava','core','No',NULL,2,'2009-01-24 20:57:59'),(24395,473,'lava','core','Don\'t Know',NULL,3,'2009-01-24 20:57:59'),(24396,474,'lava','core','Yes',NULL,1,'2009-01-24 20:57:59'),(24397,474,'lava','core','No',NULL,2,'2009-01-24 20:57:59'),(24398,474,'lava','core','N/A',NULL,3,'2009-01-24 20:57:59'),(24399,475,'lava','core','1','Yes',0,'2009-01-24 20:57:59'),(24400,475,'lava','core','2','No',0,'2009-01-24 20:57:59'),(24401,476,'lava','core','1','Yes',0,'2009-01-24 20:57:59'),(24402,476,'lava','core','2','No',0,'2009-01-24 20:57:59'),(24403,476,'lava','core','9','Don\'t Know',0,'2009-01-24 20:57:59'),(24404,477,'lava','core','0','No',0,'2009-01-24 20:57:59'),(24405,477,'lava','core','1','Yes',0,'2009-01-24 20:57:59'),(24406,477,'lava','core','9','Don\'t Know',0,'2009-01-24 20:57:59'),(24407,478,'lava','core','0','No',0,'2009-01-24 20:57:59'),(24408,478,'lava','core','1','Yes',0,'2009-01-24 20:57:59'),(24409,478,'lava','core','9','Unknown',0,'2009-01-24 20:57:59'),(24410,479,'lava','core','1','Yes',1,'2009-05-11 17:17:48'),(24411,479,'lava','core','0','No',2,'2009-05-11 17:17:48'),(24412,479,'lava','core','9','N/A',3,'2009-05-11 17:17:48'),(24413,480,'lava','core','0','No',0,'2009-01-24 20:57:59'),(24414,480,'lava','core','1','Yes',0,'2009-01-24 20:57:59'),(24415,522,'lava','crms','0','Absent',1,'2009-05-12 14:10:57'),(24416,522,'lava','crms','1','Present',2,'2009-05-12 14:10:57'),(24417,523,'lava','crms','DIVORCED',NULL,0,'2009-01-24 20:57:59'),(24418,523,'lava','crms','MARRIED',NULL,0,'2009-01-24 20:57:59'),(24419,523,'lava','crms','SINGLE',NULL,0,'2009-01-24 20:57:59'),(24420,525,'lava','crms','Email',NULL,0,'2009-01-24 20:57:59'),(24421,525,'lava','crms','Fax',NULL,0,'2009-01-24 20:57:59'),(24422,525,'lava','crms','Letter',NULL,0,'2009-01-24 20:57:59'),(24423,525,'lava','crms','Phone',NULL,0,'2009-01-24 20:57:59'),(24424,526,'lava','crms','BROTHER',NULL,0,'2009-01-24 20:57:59'),(24425,526,'lava','crms','BROTHER-IN-LAW',NULL,0,'2009-01-24 20:57:59'),(24426,526,'lava','crms','CONSERVATOR',NULL,0,'2009-01-24 20:57:59'),(24427,526,'lava','crms','DAUGHTER',NULL,0,'2009-01-24 20:57:59'),(24428,526,'lava','crms','FATHER',NULL,0,'2009-01-24 20:57:59'),(24429,526,'lava','crms','FRIEND',NULL,0,'2009-01-24 20:57:59'),(24430,526,'lava','crms','HUSBAND',NULL,0,'2009-01-24 20:57:59'),(24431,526,'lava','crms','MOTHER',NULL,0,'2009-01-24 20:57:59'),(24432,526,'lava','crms','NEPHEW',NULL,0,'2009-01-24 20:57:59'),(24433,526,'lava','crms','NIECE',NULL,0,'2009-01-24 20:57:59'),(24434,526,'lava','crms','NURSE',NULL,0,'2009-01-24 20:57:59'),(24435,526,'lava','crms','OTHER',NULL,0,'2009-01-24 20:57:59'),(24436,526,'lava','crms','PAID CAREGIVER',NULL,0,'2009-01-24 20:57:59'),(24437,526,'lava','crms','PARTNER',NULL,0,'2009-01-24 20:57:59'),(24438,526,'lava','crms','SISTER',NULL,0,'2009-01-24 20:57:59'),(24439,526,'lava','crms','SISTER-IN-LAW',NULL,0,'2009-01-24 20:57:59'),(24440,526,'lava','crms','SOCIAL WORKER',NULL,0,'2009-01-24 20:57:59'),(24441,526,'lava','crms','SON',NULL,0,'2009-01-24 20:57:59'),(24442,526,'lava','crms','WIFE',NULL,0,'2009-01-24 20:57:59'),(24443,528,'lava','crms','Scheduled',NULL,1,'2009-01-24 20:57:59'),(24444,528,'lava','crms','Canceled',NULL,2,'2009-01-24 20:57:59'),(24445,528,'lava','crms','Complete',NULL,2,'2009-01-24 20:57:59'),(24446,528,'lava','crms','Canceled-Patient Factor',NULL,3,'2009-01-24 20:57:59'),(24447,528,'lava','crms','Canceled-Situational',NULL,4,'2009-01-24 20:57:59'),(24448,528,'lava','crms','Canceled-Alt Test Given',NULL,5,'2009-01-24 20:57:59'),(24449,528,'lava','crms','Incomplete',NULL,6,'2009-01-24 20:57:59'),(24450,528,'lava','crms','Incomplete-Scoring',NULL,7,'2009-01-24 20:57:59'),(24451,528,'lava','crms','Incomplete-Not Returned',NULL,8,'2009-01-24 20:57:59'),(24452,528,'lava','crms','Unknown',NULL,9,'2009-01-24 20:57:59'),(24453,529,'lava','crms','Complete',NULL,1,'2009-01-24 20:57:59'),(24454,529,'lava','crms','Incomplete',NULL,2,'2009-01-24 20:57:59'),(24455,529,'lava','crms','Entry Problem',NULL,3,'2009-01-24 20:57:59'),(24456,529,'lava','crms','Returned To Examiner',NULL,4,'2009-01-24 20:57:59'),(24457,530,'lava','crms','Validation Needed',NULL,1,'2009-01-24 20:57:59'),(24458,530,'lava','crms','In Progress',NULL,2,'2009-01-24 20:57:59'),(24459,530,'lava','crms','Complete-OK',NULL,3,'2009-01-24 20:57:59'),(24460,530,'lava','crms','Complete-Problems',NULL,4,'2009-01-24 20:57:59'),(24461,530,'lava','crms','Complete-Invalid Data',NULL,5,'2009-01-24 20:57:59'),(24462,531,'lava','crms','OTHER',NULL,0,'2009-01-24 20:57:59'),(24463,531,'lava','crms','PRIMARY',NULL,0,'2009-01-24 20:57:59'),(24464,531,'lava','crms','REFERRING',NULL,0,'2009-01-24 20:57:59'),(24465,531,'lava','crms','REFERRING/PRIMARY',NULL,0,'2009-01-24 20:57:59'),(24466,532,'lava','crms','0',NULL,0,'2009-01-24 20:57:59'),(24467,532,'lava','crms','1',NULL,0,'2009-01-24 20:57:59'),(24468,532,'lava','crms','10',NULL,0,'2009-01-24 20:57:59'),(24469,532,'lava','crms','11',NULL,0,'2009-01-24 20:57:59'),(24470,532,'lava','crms','12','Completed High School',0,'2009-01-24 20:57:59'),(24471,532,'lava','crms','13',NULL,0,'2009-01-24 20:57:59'),(24472,532,'lava','crms','14','2 Year College Degree',0,'2009-01-24 20:57:59'),(24473,532,'lava','crms','15',NULL,0,'2009-01-24 20:57:59'),(24474,532,'lava','crms','16','4 Year College Degree',0,'2009-01-24 20:57:59'),(24475,532,'lava','crms','17',NULL,0,'2009-01-24 20:57:59'),(24476,532,'lava','crms','18','Master\'s Degree',0,'2009-01-24 20:57:59'),(24477,532,'lava','crms','19',NULL,0,'2009-01-24 20:57:59'),(24478,532,'lava','crms','2',NULL,0,'2009-01-24 20:57:59'),(24479,532,'lava','crms','20','Ph.D, M.D. or other Professional Degree',0,'2009-01-24 20:57:59'),(24480,532,'lava','crms','21',NULL,0,'2009-01-24 20:57:59'),(24481,532,'lava','crms','22',NULL,0,'2009-01-24 20:57:59'),(24482,532,'lava','crms','23',NULL,0,'2009-01-24 20:57:59'),(24483,532,'lava','crms','24',NULL,0,'2009-01-24 20:57:59'),(24484,532,'lava','crms','25',NULL,0,'2009-01-24 20:57:59'),(24485,532,'lava','crms','26',NULL,0,'2009-01-24 20:57:59'),(24486,532,'lava','crms','27',NULL,0,'2009-01-24 20:57:59'),(24487,532,'lava','crms','28',NULL,0,'2009-01-24 20:57:59'),(24488,532,'lava','crms','29',NULL,0,'2009-01-24 20:57:59'),(24489,532,'lava','crms','3',NULL,0,'2009-01-24 20:57:59'),(24490,532,'lava','crms','30',NULL,0,'2009-01-24 20:57:59'),(24491,532,'lava','crms','31',NULL,0,'2009-01-24 20:57:59'),(24492,532,'lava','crms','32',NULL,0,'2009-01-24 20:57:59'),(24493,532,'lava','crms','33',NULL,0,'2009-01-24 20:57:59'),(24494,532,'lava','crms','34',NULL,0,'2009-01-24 20:57:59'),(24495,532,'lava','crms','35',NULL,0,'2009-01-24 20:57:59'),(24496,532,'lava','crms','4',NULL,0,'2009-01-24 20:57:59'),(24497,532,'lava','crms','5',NULL,0,'2009-01-24 20:57:59'),(24498,532,'lava','crms','6',NULL,0,'2009-01-24 20:57:59'),(24499,532,'lava','crms','7',NULL,0,'2009-01-24 20:57:59'),(24500,532,'lava','crms','8',NULL,0,'2009-01-24 20:57:59'),(24501,532,'lava','crms','9',NULL,0,'2009-01-24 20:57:59'),(24502,532,'lava','crms','99','Not Determined',0,'2009-01-24 20:57:59'),(24503,533,'lava','crms','1','MALE',0,'2009-01-24 20:57:59'),(24504,533,'lava','crms','2','FEMALE',0,'2009-01-24 20:57:59'),(24505,534,'lava','crms','AMBIDEXTROUS',NULL,0,'2009-01-24 20:57:59'),(24506,534,'lava','crms','LEFT',NULL,0,'2009-01-24 20:57:59'),(24507,534,'lava','crms','RIGHT',NULL,0,'2009-01-24 20:57:59'),(24508,535,'lava','crms','BEHAVIORAL DISTURBANCES',NULL,0,'2009-01-24 20:57:59'),(24509,535,'lava','crms','INCOMPLETE DATA ENTRY',NULL,0,'2009-01-24 20:57:59'),(24510,535,'lava','crms','MOTOR DIFFICULTIES',NULL,2,'2009-01-24 20:57:59'),(24511,535,'lava','crms','SPEECH DIFFICULTIES',NULL,3,'2009-01-24 20:57:59'),(24512,535,'lava','crms','HEARING IMPAIRMENT',NULL,4,'2009-01-24 20:57:59'),(24513,535,'lava','crms','VISUAL IMPAIRMENT',NULL,5,'2009-01-24 20:57:59'),(24514,535,'lava','crms','ESL',NULL,6,'2009-01-24 20:57:59'),(24515,535,'lava','crms','MINIMAL EDUCATION',NULL,7,'2009-01-24 20:57:59'),(24516,535,'lava','crms','LACK OF EFFORT',NULL,8,'2009-01-24 20:57:59'),(24517,535,'lava','crms','UNRELIABLE INFORMANT',NULL,9,'2009-01-24 20:57:59'),(24518,535,'lava','crms','OTHER (Describe in Notes)',NULL,10,'2009-01-24 20:57:59'),(24519,535,'lava','crms','INCOMPLETE',NULL,11,'2009-01-24 20:57:59'),(24520,535,'lava','crms','UNAVAILABLE INFORMANT',NULL,12,'2009-01-24 20:57:59'),(24521,535,'lava','crms','LANGUAGE COMPREHENSION',NULL,13,'2009-01-24 20:57:59'),(24522,535,'lava','crms','COMPREHENSION OF TEST RULES',NULL,14,'2009-01-24 20:57:59'),(24523,535,'lava','crms','SPEED OF RESPONSE',NULL,15,'2009-01-24 20:57:59'),(24524,535,'lava','crms','INFORMANT HAS < DAILY CONTACT',NULL,16,'2009-01-24 20:57:59'),(24525,536,'lava','crms','GOOD FOR RESEARCH',NULL,0,'2009-01-24 20:57:59'),(24526,536,'lava','crms','NOT FOR RESEARCH',NULL,0,'2009-01-24 20:57:59'),(24527,536,'lava','crms','INCOMPLETE DATA ENTRY',NULL,3,'2009-01-24 20:57:59'),(24528,538,'lava','crms','1','NEVER MARRIED',0,'2009-01-24 20:57:59'),(24529,538,'lava','crms','2','MARRIED',0,'2009-01-24 20:57:59'),(24530,538,'lava','crms','3','WIDOWED',0,'2009-01-24 20:57:59'),(24531,538,'lava','crms','4','DIVORCED',0,'2009-01-24 20:57:59'),(24532,538,'lava','crms','5','SEPARATED',0,'2009-01-24 20:57:59'),(24533,538,'lava','crms','9','NOT DETERMINED',0,'2009-01-24 20:57:59'),(24534,539,'lava','crms','0','Normal',1,'2009-05-11 17:23:13'),(24535,539,'lava','crms','1','Abnormal',2,'2009-05-11 17:23:14'),(24536,540,'lava','crms','1','Labor / Craftsman / Mechanic',1,'2009-01-24 20:57:59'),(24537,540,'lava','crms','2','Professional (teacher, lawyer,...)',2,'2009-01-24 20:57:59'),(24538,540,'lava','crms','3','Health Care (physician, nurse,. . .)',3,'2009-01-24 20:57:59'),(24539,540,'lava','crms','4','Business Owner / Executive',4,'2009-01-24 20:57:59'),(24540,540,'lava','crms','5','Homemaker',5,'2009-01-24 20:57:59'),(24541,540,'lava','crms','6','Office / Clerical',6,'2009-01-24 20:57:59'),(24542,540,'lava','crms','7','Retail / Sales',7,'2009-01-24 20:57:59'),(24543,540,'lava','crms','8','Computer / Technical',8,'2009-01-24 20:57:59'),(24544,540,'lava','crms','9','Artistic / Creative',9,'2009-01-24 20:57:59'),(24545,540,'lava','crms','10','Other',10,'2009-01-24 20:57:59'),(24546,540,'lava','crms','0','None / Did Not Work',11,'2009-01-24 20:57:59'),(24547,541,'lava','crms','Cantonese','',0,'2009-01-24 20:57:59'),(24548,541,'lava','crms','English','',0,'2009-01-24 20:57:59'),(24549,541,'lava','crms','Japanese','',0,'2009-01-24 20:57:59'),(24550,541,'lava','crms','Mandarin','',0,'2009-01-24 20:57:59'),(24551,541,'lava','crms','Russian','',0,'2009-01-24 20:57:59'),(24552,541,'lava','crms','Spanish','',0,'2009-01-24 20:57:59'),(24553,541,'lava','crms','Unknown','',0,'2009-01-24 20:57:59'),(24554,542,'lava','crms','HOME 1',NULL,1,'2009-01-24 20:57:59'),(24555,542,'lava','crms','HOME 2',NULL,2,'2009-01-24 20:57:59'),(24556,542,'lava','crms','WORK',NULL,3,'2009-01-24 20:57:59'),(24557,542,'lava','crms','OFFICE',NULL,4,'2009-01-24 20:57:59'),(24558,542,'lava','crms','CELL',NULL,5,'2009-01-24 20:57:59'),(24559,542,'lava','crms','FAX',NULL,6,'2009-01-24 20:57:59'),(24560,542,'lava','crms','PAGER',NULL,7,'2009-01-24 20:57:59'),(24561,542,'lava','crms','OTHER',NULL,8,'2009-01-24 20:57:59'),(24562,543,'lava','crms','1','SPOUSE',0,'2009-01-24 20:57:59'),(24563,543,'lava','crms','10','OTHER',0,'2009-01-24 20:57:59'),(24564,543,'lava','crms','11','NO ONE HELPS THE PATIENT',0,'2009-01-24 20:57:59'),(24565,543,'lava','crms','2','SON',0,'2009-01-24 20:57:59'),(24566,543,'lava','crms','3','SON-IN-LAW',0,'2009-01-24 20:57:59'),(24567,543,'lava','crms','4','DAUGHTER',0,'2009-01-24 20:57:59'),(24568,543,'lava','crms','5','DAUGHTER-IN-LAW',0,'2009-01-24 20:57:59'),(24569,543,'lava','crms','6','OTHER RELATIVE',0,'2009-01-24 20:57:59'),(24570,543,'lava','crms','7','FRIEND',0,'2009-01-24 20:57:59'),(24571,543,'lava','crms','8','NEIGHBOR',0,'2009-01-24 20:57:59'),(24572,543,'lava','crms','9','PAID CAREGIVER',0,'2009-01-24 20:57:59'),(24573,544,'lava','crms','0','No',0,'2009-01-24 20:57:59'),(24574,544,'lava','crms','1','Possible',1,'2009-01-24 20:57:59'),(24575,544,'lava','crms','2','Probable',2,'2009-01-24 20:57:59'),(24576,545,'lava','crms','GENERAL','REFERRED',1,'2009-01-24 20:57:59'),(24577,545,'lava','crms','GENERAL','DEFERRED',5,'2009-01-24 20:57:59'),(24578,545,'lava','crms','GENERAL','ELIGIBLE',10,'2009-01-24 20:57:59'),(24579,545,'lava','crms','GENERAL','INELIGIBLE',15,'2009-01-24 20:57:59'),(24580,545,'lava','crms','GENERAL','DECLINED',20,'2009-01-24 20:57:59'),(24581,545,'lava','crms','GENERAL','ENROLLED',25,'2009-01-24 20:57:59'),(24582,545,'lava','crms','GENERAL','EXCLUDED',30,'2009-01-24 20:57:59'),(24583,545,'lava','crms','GENERAL','WITHDREW',35,'2009-01-24 20:57:59'),(24584,545,'lava','crms','GENERAL','INACTIVE',40,'2009-01-24 20:57:59'),(24585,545,'lava','crms','GENERAL','DECEASED',45,'2009-01-24 20:57:59'),(24586,545,'lava','crms','GENERAL','AUTOPSY',50,'2009-01-24 20:57:59'),(24587,545,'lava','crms','GENERAL','CLOSED',55,'2009-01-24 20:57:59'),(24588,546,'lava','crms','GENERAL','Enrollment',0,'2009-01-24 20:57:59'),(24589,547,'lava','crms','1','WHITE',0,'2009-01-24 20:57:59'),(24590,547,'lava','crms','10','LAOTIAN',0,'2009-01-24 20:57:59'),(24591,547,'lava','crms','11','VIETNAMESE',0,'2009-01-24 20:57:59'),(24592,547,'lava','crms','12','OTHER ASIAN',0,'2009-01-24 20:57:59'),(24593,547,'lava','crms','13','NATIVE HAWAIIAN',0,'2009-01-24 20:57:59'),(24594,547,'lava','crms','14','GUAMANIAN',0,'2009-01-24 20:57:59'),(24595,547,'lava','crms','15','SAMOAN',0,'2009-01-24 20:57:59'),(24596,547,'lava','crms','16','OTHER PACIFIC ISLANDER',0,'2009-01-24 20:57:59'),(24597,547,'lava','crms','17','NATIVE AMERICAN',0,'2009-01-24 20:57:59'),(24598,547,'lava','crms','18','OTHER RACE',0,'2009-01-24 20:57:59'),(24599,547,'lava','crms','2','BLACK/AFRICAN AMERICAN',0,'2009-01-24 20:57:59'),(24600,547,'lava','crms','3','ASIAN INDIAN',0,'2009-01-24 20:57:59'),(24601,547,'lava','crms','4','CAMBODIAN',0,'2009-01-24 20:57:59'),(24602,547,'lava','crms','5','CHINESE',0,'2009-01-24 20:57:59'),(24603,547,'lava','crms','6','FILIPINO',0,'2009-01-24 20:57:59'),(24604,547,'lava','crms','7','JAPANESE',0,'2009-01-24 20:57:59'),(24605,547,'lava','crms','8','HMONG',0,'2009-01-24 20:57:59'),(24606,547,'lava','crms','9','KOREAN',0,'2009-01-24 20:57:59'),(24607,547,'lava','crms','99','REFUSED TO STATE/UNKNOWN',0,'2009-01-24 20:57:59'),(24608,549,'lava','crms','-1','Patient Factor',0,'2009-01-24 20:57:59'),(24609,549,'lava','crms','-2','Situational Factor',0,'2009-01-24 20:57:59'),(24610,549,'lava','crms','-3','Alternate Test Given',0,'2009-01-24 20:57:59'),(24611,549,'lava','crms','-4','Refused',0,'2009-01-24 20:57:59'),(24612,550,'lava','crms','1','NORTH AMERICAN',0,'2009-01-24 20:57:59'),(24613,550,'lava','crms','2','SOUTH AMERICAN',0,'2009-01-24 20:57:59'),(24614,550,'lava','crms','3','CENTRAL AMERICAN',0,'2009-01-24 20:57:59'),(24615,550,'lava','crms','4','PUERTO RICAN',0,'2009-01-24 20:57:59'),(24616,550,'lava','crms','5','CUBAN',0,'2009-01-24 20:57:59'),(24617,550,'lava','crms','6','HAITIAN',0,'2009-01-24 20:57:59'),(24618,550,'lava','crms','7','OTHER',0,'2009-01-24 20:57:59'),(24619,552,'lava','crms','-6','Logical Skip',0,'2009-01-24 20:57:59'),(24620,552,'lava','crms','-7','Incomplete',0,'2009-01-24 20:57:59'),(24621,552,'lava','crms','-8','Unused Variable',0,'2009-01-24 20:57:59'),(24622,552,'lava','crms','-9','Missing Data',0,'2009-01-24 20:57:59'),(24623,553,'lava','crms','AB','Alberta',0,'2009-01-24 20:57:59'),(24624,553,'lava','crms','AK','ALASKA',0,'2009-01-24 20:57:59'),(24625,553,'lava','crms','AL','ALABAMA',0,'2009-01-24 20:57:59'),(24626,553,'lava','crms','AR','ARKANSAS',0,'2009-01-24 20:57:59'),(24627,553,'lava','crms','AZ','ARIZONA',0,'2009-01-24 20:57:59'),(24628,553,'lava','crms','BC','British Columbia',0,'2009-01-24 20:57:59'),(24629,553,'lava','crms','CA','CALIFORNIA',0,'2009-01-24 20:57:59'),(24630,553,'lava','crms','CO','COLORADO',0,'2009-01-24 20:57:59'),(24631,553,'lava','crms','CT','CONNECTICUT',0,'2009-01-24 20:57:59'),(24632,553,'lava','crms','DC','DISTRICT OF COLUMBIA',0,'2009-01-24 20:57:59'),(24633,553,'lava','crms','DE','DELAWARE',0,'2009-01-24 20:57:59'),(24634,553,'lava','crms','FL','FLORIDA',0,'2009-01-24 20:57:59'),(24635,553,'lava','crms','GA','GEORGIA',0,'2009-01-24 20:57:59'),(24636,553,'lava','crms','HI','HAWAII',0,'2009-01-24 20:57:59'),(24637,553,'lava','crms','IA','IOWA',0,'2009-01-24 20:57:59'),(24638,553,'lava','crms','ID','IDAHO',0,'2009-01-24 20:57:59'),(24639,553,'lava','crms','IL','ILLINOIS',0,'2009-01-24 20:57:59'),(24640,553,'lava','crms','IN','INDIANA',0,'2009-01-24 20:57:59'),(24641,553,'lava','crms','KS','KANSAS',0,'2009-01-24 20:57:59'),(24642,553,'lava','crms','KY','KENTUCKY',0,'2009-01-24 20:57:59'),(24643,553,'lava','crms','LA','LOUISIANA',0,'2009-01-24 20:57:59'),(24644,553,'lava','crms','MA','MASSACHUSETTS',0,'2009-01-24 20:57:59'),(24645,553,'lava','crms','MB','Manitoba',0,'2009-01-24 20:57:59'),(24646,553,'lava','crms','MD','MARYLAND',0,'2009-01-24 20:57:59'),(24647,553,'lava','crms','ME','MAINE',0,'2009-01-24 20:57:59'),(24648,553,'lava','crms','MI','MICHIGAN',0,'2009-01-24 20:57:59'),(24649,553,'lava','crms','MN','MINNESOTA',0,'2009-01-24 20:57:59'),(24650,553,'lava','crms','MO','MISSOURI',0,'2009-01-24 20:57:59'),(24651,553,'lava','crms','MS','MISSISSIPPI',0,'2009-01-24 20:57:59'),(24652,553,'lava','crms','MT','MONTANA',0,'2009-01-24 20:57:59'),(24653,553,'lava','crms','NB','New Brunswick',0,'2009-01-24 20:57:59'),(24654,553,'lava','crms','NC','NORTH CAROLINA',0,'2009-01-24 20:57:59'),(24655,553,'lava','crms','ND','NORTH DAKOTA',0,'2009-01-24 20:57:59'),(24656,553,'lava','crms','NE','NEBRASKA',0,'2009-01-24 20:57:59'),(24657,553,'lava','crms','NF','Newfoundland',0,'2009-01-24 20:57:59'),(24658,553,'lava','crms','NH','NEW HAMPSHIRE',0,'2009-01-24 20:57:59'),(24659,553,'lava','crms','NJ','NEW JERSEY',0,'2009-01-24 20:57:59'),(24660,553,'lava','crms','NM','NEW MEXICO',0,'2009-01-24 20:57:59'),(24661,553,'lava','crms','NS','Nova Scotia',0,'2009-01-24 20:57:59'),(24662,553,'lava','crms','NT','Northwest Territories',0,'2009-01-24 20:57:59'),(24663,553,'lava','crms','NV','NEVADA',0,'2009-01-24 20:57:59'),(24664,553,'lava','crms','NY','NEW YORK',0,'2009-01-24 20:57:59'),(24665,553,'lava','crms','OH','OHIO',0,'2009-01-24 20:57:59'),(24666,553,'lava','crms','OK','OKLAHOMA',0,'2009-01-24 20:57:59'),(24667,553,'lava','crms','ON','Ontario',0,'2009-01-24 20:57:59'),(24668,553,'lava','crms','OR','OREGON',0,'2009-01-24 20:57:59'),(24669,553,'lava','crms','PA','PENNSYLVANIA',0,'2009-01-24 20:57:59'),(24670,553,'lava','crms','PQ','Quebec',0,'2009-01-24 20:57:59'),(24671,553,'lava','crms','RI','RHODE ISLAND',0,'2009-01-24 20:57:59'),(24672,553,'lava','crms','SC','SOUTH CAROLINA',0,'2009-01-24 20:57:59'),(24673,553,'lava','crms','SD','SOUTH DAKOTA',0,'2009-01-24 20:57:59'),(24674,553,'lava','crms','SK','Saskatchewan',0,'2009-01-24 20:57:59'),(24675,553,'lava','crms','TN','TENNESSEE',0,'2009-01-24 20:57:59'),(24676,553,'lava','crms','TX','TEXAS',0,'2009-01-24 20:57:59'),(24677,553,'lava','crms','UT','UTAH',0,'2009-01-24 20:57:59'),(24678,553,'lava','crms','VA','VIRGINIA',0,'2009-01-24 20:57:59'),(24679,553,'lava','crms','VT','VERMONT',0,'2009-01-24 20:57:59'),(24680,553,'lava','crms','WA','WASHINGTON',0,'2009-01-24 20:57:59'),(24681,553,'lava','crms','WI','WISCONSIN',0,'2009-01-24 20:57:59'),(24682,553,'lava','crms','WV','WEST VIRGINIA',0,'2009-01-24 20:57:59'),(24683,553,'lava','crms','WY','WYOMING',0,'2009-01-24 20:57:59'),(24684,554,'lava','crms','CLOSED',NULL,0,'2009-01-24 20:57:59'),(24685,554,'lava','crms','OPEN',NULL,0,'2009-01-24 20:57:59'),(24686,555,'lava','crms','CALL DOCTOR',NULL,0,'2009-01-24 20:57:59'),(24687,555,'lava','crms','CALL PATIENT/CAREGIVER',NULL,0,'2009-01-24 20:57:59'),(24688,555,'lava','crms','EMAIL',NULL,0,'2009-01-24 20:57:59'),(24689,555,'lava','crms','GENERIC TASK',NULL,0,'2009-01-24 20:57:59'),(24690,555,'lava','crms','LOCATE',NULL,0,'2009-01-24 20:57:59'),(24691,555,'lava','crms','MAKE DECISION',NULL,0,'2009-01-24 20:57:59'),(24692,555,'lava','crms','SCHEDULE VISIT',NULL,0,'2009-01-24 20:57:59'),(24693,555,'lava','crms','SEND BY FAX',NULL,0,'2009-01-24 20:57:59'),(24694,555,'lava','crms','SEND BY MAIL',NULL,0,'2009-01-24 20:57:59'),(24695,556,'lava','crms','Usually',NULL,1,'2009-01-24 20:57:59'),(24696,556,'lava','crms','Sometimes',NULL,2,'2009-01-24 20:57:59'),(24697,556,'lava','crms','Rarely',NULL,3,'2009-01-24 20:57:59'),(24698,557,'lava','crms','Usually',NULL,1,'2009-01-24 20:57:59'),(24699,557,'lava','crms','Sometimes',NULL,2,'2009-01-24 20:57:59'),(24700,557,'lava','crms','Rarely',NULL,3,'2009-01-24 20:57:59'),(24701,557,'lava','crms','Don\'t Know',NULL,4,'2009-01-24 20:57:59'),(24702,559,'lava','crms','CAME IN',NULL,0,'2009-01-24 20:57:59'),(24703,559,'lava','crms','CLINIC CANCELLED',NULL,0,'2009-01-24 20:57:59'),(24704,559,'lava','crms','COMPLETE',NULL,0,'2009-01-24 20:57:59'),(24705,559,'lava','crms','NO SHOW',NULL,0,'2009-01-24 20:57:59'),(24706,559,'lava','crms','PATIENT CANCELED',NULL,0,'2009-01-24 20:57:59'),(24707,559,'lava','crms','SCHEDULED',NULL,0,'2009-01-24 20:57:59'),(24708,561,'lava','crms','0','Yes',1,'2009-01-24 20:57:59'),(24709,561,'lava','crms','1','No',2,'2009-01-24 20:57:59'),(24710,562,'lava','crms','1','Yes',1,'2009-01-24 20:57:59'),(24711,562,'lava','crms','0','No',2,'2009-01-24 20:57:59');
/*!40000 ALTER TABLE `listvalues` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `lq_view_demographics`
--

DROP TABLE IF EXISTS `lq_view_demographics`;
/*!50001 DROP VIEW IF EXISTS `lq_view_demographics`*/;
/*!50001 CREATE TABLE `lq_view_demographics` (
  `PIDN_demographics` int(10),
  `DOB` datetime,
  `AGE` double(17,0),
  `Gender` tinyint(3),
  `Hand` varchar(25),
  `Deceased` tinyint(1),
  `DOD` datetime,
  `PrimaryLanguage` varchar(25),
  `TestingLanguage` varchar(25),
  `TransNeeded` tinyint(1),
  `TransLanguage` varchar(25)
) */;

--
-- Temporary table structure for view `lq_view_enrollment`
--

DROP TABLE IF EXISTS `lq_view_enrollment`;
/*!50001 DROP VIEW IF EXISTS `lq_view_enrollment`*/;
/*!50001 CREATE TABLE `lq_view_enrollment` (
  `EnrollStatID` int(10),
  `PIDN_Enrollment` int(10),
  `ProjName` varchar(75),
  `SubjectStudyID` varchar(10),
  `ReferralSource` varchar(75),
  `LatestDesc` varchar(25),
  `LatestDate` datetime,
  `LatestNote` varchar(100),
  `ReferredDesc` varchar(25),
  `ReferredDate` datetime,
  `ReferredNote` varchar(100),
  `DeferredDesc` varchar(25),
  `DeferredDate` datetime,
  `DeferredNote` varchar(100),
  `EligibleDesc` varchar(25),
  `EligibleDate` datetime,
  `EligibleNote` varchar(100),
  `IneligibleDesc` varchar(25),
  `IneligibleDate` datetime,
  `IneligibleNote` varchar(100),
  `DeclinedDesc` varchar(25),
  `DeclinedDate` datetime,
  `DeclinedNote` varchar(100),
  `EnrolledDesc` varchar(25),
  `EnrolledDate` datetime,
  `EnrolledNote` varchar(100),
  `ExcludedDesc` varchar(25),
  `ExcludedDate` datetime,
  `ExcludedNote` varchar(100),
  `WithdrewDesc` varchar(25),
  `WithdrewDate` datetime,
  `WithdrewNote` varchar(100),
  `InactiveDesc` varchar(25),
  `InactiveDate` datetime,
  `InactiveNote` varchar(100),
  `DeceasedDesc` varchar(25),
  `DeceasedDate` datetime,
  `DeceasedNote` varchar(100),
  `AutopsyDesc` varchar(25),
  `AutopsyDate` datetime,
  `AutopsyNote` varchar(100),
  `ClosedDesc` varchar(25),
  `ClosedDate` datetime,
  `ClosedNote` varchar(100),
  `EnrollmentNotes` varchar(500),
  `modified` timestamp
) */;

--
-- Temporary table structure for view `lq_view_instruments`
--

DROP TABLE IF EXISTS `lq_view_instruments`;
/*!50001 DROP VIEW IF EXISTS `lq_view_instruments`*/;
/*!50001 CREATE TABLE `lq_view_instruments` (
  `InstrID` int(10),
  `VID` int(10),
  `ProjName` varchar(75),
  `PIDN_Instrument` int(10),
  `InstrType` varchar(25),
  `InstrVer` varchar(25),
  `DCDate` datetime,
  `DCBy` varchar(25),
  `DCStatus` varchar(25),
  `DCNotes` varchar(255),
  `ResearchStatus` varchar(50),
  `QualityIssue` varchar(50),
  `QualityIssue2` varchar(50),
  `QualityIssue3` varchar(50),
  `QualityNotes` varchar(100),
  `DEDate` datetime,
  `DEBy` varchar(25),
  `DEStatus` varchar(25),
  `DENotes` varchar(255),
  `DVDate` datetime,
  `DVBy` varchar(25),
  `DVStatus` varchar(25),
  `DVNotes` varchar(255),
  `latestflag` tinyint(1),
  `FieldStatus` smallint(5),
  `AgeAtDC` smallint(5),
  `modified` timestamp,
  `summary` varchar(500)
) */;

--
-- Temporary table structure for view `lq_view_visit`
--

DROP TABLE IF EXISTS `lq_view_visit`;
/*!50001 DROP VIEW IF EXISTS `lq_view_visit`*/;
/*!50001 CREATE TABLE `lq_view_visit` (
  `VID` int(10),
  `PIDN_Visit` int(10),
  `ProjName` varchar(75),
  `VLocation` varchar(25),
  `VType` varchar(25),
  `VWith` varchar(25),
  `VDate` date,
  `VTime` time,
  `VStatus` varchar(25),
  `VNotes` varchar(255),
  `FUMonth` char(3),
  `FUYear` char(4),
  `FUNote` varchar(100),
  `WList` varchar(25),
  `WListNote` varchar(100),
  `WListDate` datetime,
  `VShortDesc` varchar(255),
  `AgeAtVisit` smallint(5),
  `modified` timestamp
) */;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `patient` (
  `PIDN` int(10) NOT NULL auto_increment,
  `LName` varchar(25) NOT NULL,
  `MInitial` char(1) default NULL,
  `FName` varchar(25) NOT NULL,
  `Suffix` varchar(15) default NULL,
  `Degree` varchar(15) default NULL,
  `DOB` datetime default NULL,
  `Gender` tinyint(3) default NULL,
  `Hand` varchar(25) default NULL,
  `Deceased` tinyint(1) NOT NULL default '0',
  `DOD` datetime default NULL,
  `PrimaryLanguage` varchar(25) default NULL,
  `TestingLanguage` varchar(25) default NULL,
  `TransNeeded` tinyint(1) default '0',
  `TransLanguage` varchar(25) default NULL,
  `EnterBy` varchar(25) default NULL,
  `DupNameFlag` tinyint(1) NOT NULL default '0',
  `FullNameRev` varchar(100) default NULL,
  `FullName` varchar(100) default NULL,
  `FullNameRevNoSuffix` varchar(100) default NULL,
  `FullNameNoSuffix` varchar(100) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`PIDN`)
) ENGINE=InnoDB AUTO_INCREMENT=8009 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `patient_age`
--

DROP TABLE IF EXISTS `patient_age`;
/*!50001 DROP VIEW IF EXISTS `patient_age`*/;
/*!50001 CREATE TABLE `patient_age` (
  `PIDN` int(10),
  `AGE` double(17,0)
) */;

--
-- Table structure for table `patientconsent`
--

DROP TABLE IF EXISTS `patientconsent`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `patientconsent` (
  `ConsentID` int(10) NOT NULL auto_increment,
  `PIDN` int(10) NOT NULL,
  `CareID` int(10) default NULL,
  `ProjName` varchar(75) NOT NULL,
  `ConsentType` varchar(50) NOT NULL,
  `HIPAA` tinyint(4) default NULL,
  `ConsentDate` timestamp NULL default NULL,
  `ExpirationDate` datetime default NULL,
  `WithdrawlDate` datetime default NULL,
  `Note` varchar(100) default NULL,
  `CapacityReviewBy` varchar(25) default NULL,
  `ConsentRevision` varchar(10) default NULL,
  `ConsentDeclined` varchar(10) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ConsentID`),
  KEY `patientconsent__PIDN` (`PIDN`),
  KEY `patientconsent__ProjName` (`ProjName`),
  CONSTRAINT `patientconsent__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `patientconsent__ProjName` FOREIGN KEY (`ProjName`) REFERENCES `projectunit` (`ProjUnitDesc`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `patientdoctors` (
  `PIDNDocID` int(10) NOT NULL auto_increment,
  `DocID` int(10) NOT NULL,
  `PIDN` int(10) NOT NULL,
  `DocStat` varchar(25) default NULL,
  `DocNote` varchar(100) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`PIDNDocID`),
  KEY `patientdoctors__PIDN` (`PIDN`),
  KEY `patientdoctors__DocID` (`DocID`),
  CONSTRAINT `patientdoctors__DocID` FOREIGN KEY (`DocID`) REFERENCES `doctor` (`DocID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `patientdoctors__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `preference` (
  `preference_id` int(11) NOT NULL auto_increment,
  `user_id` int(10) default NULL,
  `context` varchar(255) default NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) default NULL,
  `value` varchar(255) default NULL,
  `visible` int(11) NOT NULL default '1',
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`preference_id`),
  KEY `preference__user_id` (`user_id`),
  CONSTRAINT `preference__user_id` FOREIGN KEY (`user_id`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `preference`
--

LOCK TABLES `preference` WRITE;
/*!40000 ALTER TABLE `preference` DISABLE KEYS */;
INSERT INTO `preference` (`preference_id`, `user_id`, `context`, `name`, `description`, `value`, `visible`, `modified`) VALUES (1,NULL,'calendar','displayRange','Default View (e.g. Month, Week)','Month',0,'2010-01-26 14:10:06'),(2,NULL,'calendar','showDayLength','Sets day length in week or day views to display either full day or work day','Work Day',0,'2010-01-26 17:13:23'),(4,NULL,'baseList','pageSize','The default page view size for lists.','25',1,'2012-01-20 16:37:29');
/*!40000 ALTER TABLE `preference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectunit`
--

DROP TABLE IF EXISTS `projectunit`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `projectunit` (
  `ProjUnitID` int(10) NOT NULL auto_increment,
  `Project` varchar(25) NOT NULL,
  `Unit` varchar(25) default NULL,
  `Status` varchar(25) NOT NULL default 'ACTIVE',
  `EffDate` timestamp NULL default NULL,
  `ExpDate` datetime default NULL,
  `ProjUnitDesc` varchar(75) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ProjUnitID`),
  KEY `projectunit_ProjUnitDesc` (`ProjUnitDesc`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `projectunit`
--

LOCK TABLES `projectunit` WRITE;
/*!40000 ALTER TABLE `projectunit` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectunit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_config_option`
--

DROP TABLE IF EXISTS `prot_config_option`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_config_option` (
  `option_id` int(11) NOT NULL auto_increment,
  `parent_id` int(11) NOT NULL,
  `ProjName` varchar(75) NOT NULL,
  `default_option` tinyint(1) default NULL,
  `label` varchar(25) NOT NULL,
  `notes` varchar(255) default NULL,
  `eff_date` date default NULL,
  `exp_date` date default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`option_id`),
  KEY `fk_prot_config_option__parent_id` (`parent_id`),
  CONSTRAINT `fk_prot_config_option__parent_id` FOREIGN KEY (`parent_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_config_option`
--

LOCK TABLES `prot_config_option` WRITE;
/*!40000 ALTER TABLE `prot_config_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_config_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_instr`
--

DROP TABLE IF EXISTS `prot_instr`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_instr` (
  `node_id` int(11) NOT NULL auto_increment,
  `InstrID` int(11) default NULL,
  `collect_anchor_date` date default NULL,
  `collect_win_start` date default NULL,
  `collect_win_end` date default NULL,
  PRIMARY KEY  (`node_id`),
  KEY `fk_prot_instr__node_id` (`node_id`),
  KEY `fk_prot_instr__InstrID` (`InstrID`),
  CONSTRAINT `fk_prot_instr__InstrID` FOREIGN KEY (`InstrID`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_instr__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_instr`
--

LOCK TABLES `prot_instr` WRITE;
/*!40000 ALTER TABLE `prot_instr` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_instr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_instr_config`
--

DROP TABLE IF EXISTS `prot_instr_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_instr_config` (
  `node_id` int(11) NOT NULL auto_increment,
  `optional` tinyint(1) default NULL,
  `category` varchar(25) default NULL,
  `collect_win_def` tinyint(1) default NULL,
  `collect_win_prot_visit_conf_id` int(11) default NULL,
  `collect_win_size` smallint(6) default NULL,
  `collect_win_offset` smallint(6) default NULL,
  `default_comp_status` varchar(25) default NULL,
  `default_comp_reason` varchar(25) default NULL,
  `default_comp_note` varchar(100) default NULL,
  PRIMARY KEY  (`node_id`),
  KEY `fk_prot_instr_config__node_id` (`node_id`),
  KEY `fk_prot_instr_config__collect_win_prot_visit_conf_id` (`collect_win_prot_visit_conf_id`),
  CONSTRAINT `fk_prot_instr_config__collect_win_prot_visit_conf_id` FOREIGN KEY (`collect_win_prot_visit_conf_id`) REFERENCES `prot_visit_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_instr_config__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_instr_config`
--

LOCK TABLES `prot_instr_config` WRITE;
/*!40000 ALTER TABLE `prot_instr_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_instr_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_instr_config_option`
--

DROP TABLE IF EXISTS `prot_instr_config_option`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_instr_config_option` (
  `option_id` int(11) NOT NULL auto_increment,
  `instr_type` varchar(25) default NULL,
  PRIMARY KEY  (`option_id`),
  KEY `fk_prot_instr_config_option__option_id` (`option_id`),
  CONSTRAINT `fk_prot_instr_config_option__option_id` FOREIGN KEY (`option_id`) REFERENCES `prot_config_option` (`option_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_instr_config_option`
--

LOCK TABLES `prot_instr_config_option` WRITE;
/*!40000 ALTER TABLE `prot_instr_config_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_instr_config_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_node`
--

DROP TABLE IF EXISTS `prot_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_node` (
  `node_id` int(11) NOT NULL auto_increment,
  `parent_id` int(11) default NULL,
  `config_node_id` int(11) NOT NULL,
  `list_order` int(11) default NULL,
  `PIDN` int(11) NOT NULL,
  `ProjName` varchar(75) NOT NULL,
  `strategy` smallint(6) NOT NULL,
  `curr_status` varchar(25) default NULL,
  `curr_reason` varchar(25) default NULL,
  `curr_note` varchar(100) default NULL,
  `comp_status` varchar(25) default NULL,
  `comp_reason` varchar(25) default NULL,
  `comp_note` varchar(100) default NULL,
  `comp_by` varchar(25) default NULL,
  `comp_date` date default NULL,
  `sched_win_status` varchar(25) default NULL,
  `sched_win_reason` varchar(25) default NULL,
  `sched_win_note` varchar(100) default NULL,
  `collect_win_status` varchar(25) default NULL,
  `collect_win_reason` varchar(25) default NULL,
  `collect_win_note` varchar(100) default NULL,
  `assign_desc` varchar(100) default NULL,
  `notes` varchar(255) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`node_id`),
  KEY `fk_prot_node__parent_id` (`parent_id`),
  KEY `fk_prot_node__config_node_id` (`config_node_id`),
  CONSTRAINT `fk_prot_node__config_node_id` FOREIGN KEY (`config_node_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_node__parent_id` FOREIGN KEY (`parent_id`) REFERENCES `prot_node` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_node`
--

LOCK TABLES `prot_node` WRITE;
/*!40000 ALTER TABLE `prot_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_node_config`
--

DROP TABLE IF EXISTS `prot_node_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_node_config` (
  `node_id` int(11) NOT NULL auto_increment,
  `parent_id` int(11) default NULL,
  `list_order` int(11) default NULL,
  `ProjName` varchar(75) NOT NULL,
  `label` varchar(25) NOT NULL,
  `summary` varchar(100) default NULL,
  `notes` varchar(255) default NULL,
  `eff_date` date default NULL,
  `exp_date` date default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`node_id`),
  KEY `fk_prot_node_config__parent_id` (`parent_id`),
  CONSTRAINT `fk_prot_node_config__parent_id` FOREIGN KEY (`parent_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_node_config`
--

LOCK TABLES `prot_node_config` WRITE;
/*!40000 ALTER TABLE `prot_node_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_node_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_protocol`
--

DROP TABLE IF EXISTS `prot_protocol`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_protocol` (
  `node_id` int(11) NOT NULL auto_increment,
  `assigned_date` date default NULL,
  `EnrollStatID` int(11) default NULL,
  PRIMARY KEY  (`node_id`),
  KEY `fk_prot_protocol__node_id` (`node_id`),
  KEY `fk_prot_protocol__EnrollStatID` (`EnrollStatID`),
  CONSTRAINT `fk_prot_protocol__EnrollStatID` FOREIGN KEY (`EnrollStatID`) REFERENCES `enrollmentstatus` (`EnrollStatID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_protocol__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_protocol`
--

LOCK TABLES `prot_protocol` WRITE;
/*!40000 ALTER TABLE `prot_protocol` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_protocol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_protocol_config`
--

DROP TABLE IF EXISTS `prot_protocol_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_protocol_config` (
  `node_id` int(11) NOT NULL auto_increment,
  `category` varchar(25) default NULL,
  `first_prot_tp_conf_id` int(11) default NULL,
  PRIMARY KEY  (`node_id`),
  KEY `fk_prot_protocol_config__node_id` (`node_id`),
  CONSTRAINT `fk_prot_protocol_config__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_protocol_config`
--

LOCK TABLES `prot_protocol_config` WRITE;
/*!40000 ALTER TABLE `prot_protocol_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_protocol_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_tp`
--

DROP TABLE IF EXISTS `prot_tp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_tp` (
  `node_id` int(11) NOT NULL auto_increment,
  `sched_anchor_date` date default NULL,
  `sched_win_start` date default NULL,
  `sched_win_end` date default NULL,
  `pri_prot_visit_id` int(11) default NULL,
  `collect_anchor_date` date default NULL,
  `collect_win_start` date default NULL,
  `collect_win_end` date default NULL,
  PRIMARY KEY  (`node_id`),
  KEY `fk_prot_tp__node_id` (`node_id`),
  CONSTRAINT `fk_prot_tp__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_tp`
--

LOCK TABLES `prot_tp` WRITE;
/*!40000 ALTER TABLE `prot_tp` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_tp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_tp_config`
--

DROP TABLE IF EXISTS `prot_tp_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_tp_config` (
  `node_id` int(11) NOT NULL auto_increment,
  `optional` tinyint(1) default NULL,
  `sched_win_rel_tp_id` int(11) default NULL,
  `sched_win_rel_amt` smallint(6) default NULL,
  `sched_win_rel_units` smallint(6) default NULL,
  `sched_win_rel_mode` smallint(6) default NULL,
  `sched_win_days_from_start` smallint(6) default NULL,
  `sched_win_size` smallint(6) default NULL,
  `sched_win_offset` smallint(6) default NULL,
  `duration` smallint(6) default NULL,
  `sched_auto` tinyint(1) default NULL,
  `pri_prot_visit_conf_id` int(11) default NULL,
  `collect_win_def` tinyint(1) default NULL,
  `collect_win_size` smallint(6) default NULL,
  `collect_win_offset` smallint(6) default NULL,
  `repeating` tinyint(1) default NULL,
  `rpt_interval` smallint(6) default NULL,
  `rpt_init_num` smallint(6) default NULL,
  `rpt_create_auto` tinyint(1) default NULL,
  PRIMARY KEY  (`node_id`),
  KEY `fk_prot_tp_config__node_id` (`node_id`),
  KEY `fk_prot_tp_config__sched_win_rel_tp_id` (`sched_win_rel_tp_id`),
  CONSTRAINT `fk_prot_tp_config__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_tp_config__sched_win_rel_tp_id` FOREIGN KEY (`sched_win_rel_tp_id`) REFERENCES `prot_tp_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_tp_config`
--

LOCK TABLES `prot_tp_config` WRITE;
/*!40000 ALTER TABLE `prot_tp_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_tp_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_visit`
--

DROP TABLE IF EXISTS `prot_visit`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_visit` (
  `node_id` int(11) NOT NULL auto_increment,
  `VID` int(11) default NULL,
  PRIMARY KEY  (`node_id`),
  KEY `fk_prot_visit__node_id` (`node_id`),
  KEY `fk_prot_visit__VID` (`VID`),
  CONSTRAINT `fk_prot_visit__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_visit__VID` FOREIGN KEY (`VID`) REFERENCES `visit` (`VID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_visit`
--

LOCK TABLES `prot_visit` WRITE;
/*!40000 ALTER TABLE `prot_visit` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_visit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_visit_config`
--

DROP TABLE IF EXISTS `prot_visit_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_visit_config` (
  `node_id` int(11) NOT NULL auto_increment,
  `optional` tinyint(1) default NULL,
  `category` varchar(25) default NULL,
  PRIMARY KEY  (`node_id`),
  KEY `fk_prot_visit_config__node_id` (`node_id`),
  CONSTRAINT `fk_prot_visit_config__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_visit_config`
--

LOCK TABLES `prot_visit_config` WRITE;
/*!40000 ALTER TABLE `prot_visit_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_visit_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prot_visit_config_option`
--

DROP TABLE IF EXISTS `prot_visit_config_option`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prot_visit_config_option` (
  `option_id` int(11) NOT NULL auto_increment,
  `ProjName` varchar(75) default NULL,
  `visit_type` varchar(25) default NULL,
  PRIMARY KEY  (`option_id`),
  KEY `fk_prot_visit_config_option__option_id` (`option_id`),
  CONSTRAINT `fk_prot_visit_config_option__option_id` FOREIGN KEY (`option_id`) REFERENCES `prot_config_option` (`option_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `prot_visit_config_option`
--

LOCK TABLES `prot_visit_config_option` WRITE;
/*!40000 ALTER TABLE `prot_visit_config_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `prot_visit_config_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_objects`
--

DROP TABLE IF EXISTS `query_objects`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `query_objects` (
  `query_object_id` int(11) unsigned NOT NULL auto_increment,
  `instance` varchar(50) NOT NULL default 'lava',
  `scope` varchar(50) NOT NULL,
  `module` varchar(50) NOT NULL,
  `section` varchar(50) NOT NULL,
  `target` varchar(50) NOT NULL,
  `short_desc` varchar(50) default NULL,
  `standard` tinyint(4) NOT NULL default '1',
  `primary_link` tinyint(4) NOT NULL default '1',
  `secondary_link` tinyint(4) NOT NULL default '1',
  `notes` text,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`query_object_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `query_objects`
--

LOCK TABLES `query_objects` WRITE;
/*!40000 ALTER TABLE `query_objects` DISABLE KEYS */;
INSERT INTO `query_objects` (`query_object_id`, `instance`, `scope`, `module`, `section`, `target`, `short_desc`, `standard`, `primary_link`, `secondary_link`, `notes`, `modified`) VALUES (1,'lava','crms','query','patient','demographics','Demographics',1,0,1,NULL,'2012-01-27 09:07:14'),(2,'lava','crms','query','enrollment','status','Enrollment Status',1,0,0,NULL,'2012-01-27 09:07:14'),(3,'lava','crms','query','scheduling','visits','Visits',1,0,0,NULL,'2012-01-27 09:07:14'),(4,'lava','crms','query','assessment','instruments','Instrument Tracking',1,0,0,NULL,'2012-01-27 09:07:14');
/*!40000 ALTER TABLE `query_objects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource_calendar`
--

DROP TABLE IF EXISTS `resource_calendar`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `resource_calendar` (
  `calendar_id` int(10) unsigned NOT NULL,
  `resource_type` varchar(25) NOT NULL,
  `location` varchar(100) default NULL,
  `contact_id` int(10) NOT NULL,
  `peak_usage_days` varchar(100) default NULL,
  `peak_usage_begin_time` time default NULL,
  `peak_usage_end_time` time default NULL,
  PRIMARY KEY  (`calendar_id`),
  KEY `resource_calendar__calendar` (`calendar_id`),
  KEY `resource_calendar__user_id` (`contact_id`),
  CONSTRAINT `resource_calendar__calendar` FOREIGN KEY (`calendar_id`) REFERENCES `calendar` (`calendar_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `resource_calendar__user_id` FOREIGN KEY (`contact_id`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tasks` (
  `TaskID` int(10) NOT NULL auto_increment,
  `PIDN` int(10) NOT NULL,
  `ProjName` varchar(75) default NULL,
  `OpenedDate` datetime default NULL,
  `OpenedBy` varchar(25) default NULL,
  `TaskType` varchar(25) default NULL,
  `TaskDesc` varchar(255) default NULL,
  `DueDate` datetime default NULL,
  `TaskStatus` varchar(50) default NULL,
  `AssignedTo` varchar(25) default NULL,
  `WorkingNotes` varchar(255) default NULL,
  `ClosedDate` datetime default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`TaskID`),
  KEY `tasks__PIDN` (`PIDN`),
  KEY `tasks__ProjName` (`ProjName`),
  CONSTRAINT `tasks__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `tasks__ProjName` FOREIGN KEY (`ProjName`) REFERENCES `projectunit` (`ProjUnitDesc`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `uploadedfiles` (
  `InstrID` int(10) NOT NULL,
  `FileName` varchar(500) default NULL,
  `FileContents` varchar(16) default NULL,
  PRIMARY KEY  (`InstrID`),
  KEY `uploadedfiles__InstrID` (`InstrID`),
  CONSTRAINT `uploadedfiles__InstrID` FOREIGN KEY (`InstrID`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `versionhistory` (
  `Module` varchar(25) NOT NULL,
  `Version` varchar(10) NOT NULL,
  `VersionDate` datetime NOT NULL,
  `Major` int(10) NOT NULL,
  `Minor` int(10) NOT NULL,
  `Fix` int(10) NOT NULL,
  `UpdateRequired` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`Module`,`Version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `versionhistory`
--

LOCK TABLES `versionhistory` WRITE;
/*!40000 ALTER TABLE `versionhistory` DISABLE KEYS */;
INSERT INTO `versionhistory` (`Module`, `Version`, `VersionDate`, `Major`, `Minor`, `Fix`, `UpdateRequired`) VALUES ('lava-core-model','3.1.0','2012-01-25 05:16:21',3,1,0,1);
/*!40000 ALTER TABLE `versionhistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `viewproperty`
--

DROP TABLE IF EXISTS `viewproperty`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `viewproperty` (
  `id` int(10) NOT NULL auto_increment,
  `messageCode` varchar(255) default NULL,
  `locale` varchar(10) NOT NULL default 'en',
  `instance` varchar(25) NOT NULL default 'lava',
  `scope` varchar(25) NOT NULL,
  `prefix` varchar(50) default NULL,
  `entity` varchar(100) NOT NULL,
  `property` varchar(100) NOT NULL,
  `section` varchar(50) default NULL,
  `context` varchar(10) default NULL,
  `style` varchar(25) default NULL,
  `required` varchar(3) default NULL,
  `label` varchar(500) default NULL,
  `label2` varchar(25) default NULL,
  `maxLength` smallint(5) default NULL,
  `size` smallint(5) default NULL,
  `indentLevel` smallint(5) default '0',
  `attributes` varchar(100) default NULL,
  `list` varchar(50) default NULL,
  `listAttributes` varchar(100) default NULL,
  `propOrder` int(10) default NULL,
  `quickHelp` varchar(500) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3714 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `viewproperty`
--

LOCK TABLES `viewproperty` WRITE;
/*!40000 ALTER TABLE `viewproperty` DISABLE KEYS */;
INSERT INTO `viewproperty` (`id`, `messageCode`, `locale`, `instance`, `scope`, `prefix`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `label2`, `maxLength`, `size`, `indentLevel`, `attributes`, `list`, `listAttributes`, `propOrder`, `quickHelp`, `modified`) VALUES (2479,'*.appointment.id','en','lava','core',NULL,'appointment','id','details','c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2009-03-31 13:35:39'),(2480,'*.appointment.calendar.name','en','lava','core',NULL,'appointment','calendar.name','details','c','string','No','Calendar',NULL,NULL,NULL,0,NULL,NULL,NULL,2,NULL,'2009-03-31 13:35:39'),(2481,'*.appointment.organizerId','en','lava','core','','appointment','organizerId','details','c','range','No','Organizer',NULL,NULL,NULL,NULL,'','appointment.organizer','',3,'','2009-03-31 13:35:39'),(2482,'*.appointment.type','en','lava','core',NULL,'appointment','type','details','i','range','No','Type',NULL,NULL,NULL,0,NULL,'appointment.type',NULL,4,NULL,'2009-03-31 13:35:39'),(2483,'*.appointment.description','en','lava','core',NULL,'appointment','description','details','i','text','No','Description',NULL,100,NULL,0,NULL,NULL,NULL,5,NULL,'2009-03-31 13:35:39'),(2484,'*.appointment.location','en','lava','core',NULL,'appointment','location','details','i','string','No','Location',NULL,100,NULL,0,NULL,NULL,NULL,6,NULL,'2009-03-31 13:35:39'),(2485,'*.appointment.startDate','en','lava','core','','appointment','startDate','details','i','date','Yes','Start Date',NULL,NULL,10,0,NULL,NULL,NULL,7,NULL,'2009-04-16 08:31:52'),(2486,'*.appointment.startTime','en','lava','core','','appointment','startTime','details','i','time','Yes','Time',NULL,NULL,NULL,0,'',NULL,'',8,'','2009-04-01 21:49:05'),(2487,'*.appointment.endDate','en','lava','core','','appointment','endDate','details','i','date','Yes','End Date',NULL,NULL,10,0,NULL,NULL,NULL,9,NULL,'2009-04-27 13:47:43'),(2488,'*.appointment.endTime','en','lava','core','','appointment','endTime','details','i','time','Yes','Time',NULL,NULL,NULL,0,NULL,NULL,NULL,10,NULL,'2009-04-16 08:30:06'),(2489,'*.appointment.status','en','lava','core',NULL,'appointment','status','details','i','range','No','Status',NULL,NULL,NULL,0,NULL,'resourceReservation.status',NULL,10,NULL,'2009-05-11 12:43:15'),(2490,'*.appointment.notes','en','lava','core',NULL,'appointment','notes','notes','i','unlimitedtext','No','Notes',NULL,NULL,NULL,0,'rows=\"10\" cols=\"45\"',NULL,NULL,11,NULL,'2009-03-31 13:35:39'),(2491,'*.appointment.organizer.userName','en','lava','core',NULL,'appointment','organizer.userName','details','c','string','No','Organizer',NULL,NULL,NULL,0,NULL,NULL,NULL,14,'Organizer Name','2009-06-11 10:00:00'),(2492,'*.attendee.userId','en','lava','core',NULL,'attendee','userId','details','i','range','Yes','Attendee',NULL,NULL,NULL,0,NULL,'attendee.attendee',NULL,3,'Attendee','2009-06-03 13:40:16'),(2493,'*.attendee.role','en','lava','core',NULL,'attendee','role','details','i','range','Yes','Role',NULL,25,NULL,0,NULL,'attendee.role',NULL,4,'Attendee Role','2009-06-03 13:40:16'),(2494,'*.attendee.status','en','lava','core',NULL,'attendee','status','details','i','range','Yes','Status',NULL,25,NULL,0,NULL,'attendee.status',NULL,5,'Attendee Status','2009-06-03 13:40:16'),(2495,'*.attendee.notes','en','lava','core',NULL,'attendee','notes','details','i','test','No','Notes',NULL,100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,6,'Notes','2009-06-03 13:40:16'),(2496,'*.auditEntityHistory.id','en','lava','core',NULL,'auditEntityHistory','id',NULL,'c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-24 21:28:51'),(2497,'*.auditEntityHistory.entityId','en','lava','core',NULL,'auditEntityHistory','entityId',NULL,'c','numeric','Yes','Entity ID',NULL,NULL,NULL,0,NULL,NULL,NULL,3,'ID of the Entity','2009-01-24 21:28:51'),(2498,'*.auditEntityHistory.entity','en','lava','core',NULL,'auditEntityHistory','entity',NULL,'c','string','Yes','Entity',NULL,100,NULL,0,NULL,NULL,NULL,4,'Base entity name, e.g. Patient, Instrument (this is the entity name where the autoincrementing id field is defined)','2009-01-24 21:28:51'),(2499,'*.auditEntityHistory.entityType','en','lava','core',NULL,'auditEntityHistory','entityType',NULL,'c','string','No','Entity Type',NULL,100,NULL,0,NULL,NULL,NULL,5,'Optional subtype of the entity (e.g. MacPatient, BedsideScreen','2009-01-24 21:28:51'),(2500,'*.auditEntityHistory.auditType','en','lava','core',NULL,'auditEntityHistory','auditType',NULL,'c','string','Yes','Audit Type',NULL,10,NULL,0,NULL,NULL,NULL,6,'The type of auditing for the entity (e.g. CREATE, READ, UPDATE, DELETE)','2009-01-24 21:28:51'),(2501,'*.auditEventHistory.id','en','lava','core',NULL,'auditEventHistory','id',NULL,'c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-24 21:28:51'),(2502,'*.auditEventHistory.auditUser','en','lava','core',NULL,'auditEventHistory','auditUser',NULL,'c','string','Yes','Audit User',NULL,50,NULL,0,NULL,NULL,NULL,2,'The user who initiated the event','2009-01-24 21:28:51'),(2503,'*.auditEventHistory.auditHost','en','lava','core',NULL,'auditEventHistory','auditHost',NULL,'c','string','Yes','Audit Host',NULL,25,NULL,0,NULL,NULL,NULL,3,'The host (machine) that the event was initiated from','2009-01-24 21:28:51'),(2504,'*.auditEventHistory.auditTime','en','lava','core',NULL,'auditEventHistory','auditTime',NULL,'c','datetime','Yes','Audit Time',NULL,NULL,NULL,0,NULL,NULL,NULL,4,'The time that the event was initiated','2009-01-24 21:28:51'),(2505,'*.auditEventHistory.action','en','lava','core',NULL,'auditEventHistory','action',NULL,'c','string','Yes','Action',NULL,255,NULL,0,NULL,NULL,NULL,5,'The action id od the event','2009-01-24 21:28:51'),(2506,'*.auditEventHistory.actionEvent','en','lava','core',NULL,'auditEventHistory','actionEvent',NULL,'c','string','Yes','Action Event',NULL,50,NULL,0,NULL,NULL,NULL,6,'The event type of the event (e.g. add, view, delete, edit, list)','2009-01-24 21:28:51'),(2507,'*.auditEventHistory.actionIdParam','en','lava','core',NULL,'auditEventHistory','actionIdParam',NULL,'c','string','No','ID Param',NULL,50,NULL,0,NULL,NULL,NULL,7,'If an ID parameter was supplied for the event','2009-01-24 21:28:51'),(2508,'*.auditEventHistory.eventNote','en','lava','core',NULL,'auditEventHistory','eventNote',NULL,'c','text','No','Note',NULL,255,NULL,0,NULL,NULL,NULL,8,'An optional note field for the event','2009-01-24 21:28:51'),(2509,'*.auditEventHistory.exception','en','lava','core',NULL,'auditEventHistory','exception',NULL,'c','text','No','Exception',NULL,255,NULL,0,NULL,NULL,NULL,9,'If the event resulted in a handled exception','2009-01-24 21:28:51'),(2510,'*.auditEventHistory.exceptionMessage','en','lava','core',NULL,'auditEventHistory','exceptionMessage',NULL,'c','text','No','Exception Message',NULL,255,NULL,0,NULL,NULL,NULL,10,'The message associated with the handled exception.','2009-01-24 21:28:51'),(2511,'*.auditPropertyHistory.id','en','lava','core',NULL,'auditPropertyHistory','id',NULL,'c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-24 21:28:51'),(2512,'*.auditPropertyHistory.property','en','lava','core',NULL,'auditPropertyHistory','property',NULL,'c','string','Yes','Property Name',NULL,100,NULL,0,NULL,NULL,NULL,3,'The name of the entity property','2009-01-24 21:28:51'),(2513,'*.auditPropertyHistory.indexKey','en','lava','core',NULL,'auditPropertyHistory','indexKey',NULL,'c','string','No','Index Key Value',NULL,100,NULL,0,NULL,NULL,NULL,4,'If the property is a collection, the index into the collection for this particular subproperty value','2009-01-24 21:28:51'),(2514,'*.auditPropertyHistory.subproperty','en','lava','core',NULL,'auditPropertyHistory','subproperty',NULL,'c','string','No','Subproperty Name',NULL,255,NULL,0,NULL,NULL,NULL,5,'The name of the subproperty when theproperty is a collection','2009-01-24 21:28:51'),(2515,'*.auditPropertyHistory.oldValue','en','lava','core',NULL,'auditPropertyHistory','oldValue',NULL,'c','string','Yes','Old Value',NULL,255,NULL,0,NULL,NULL,NULL,6,'The old value or {CREATED} when the record is for a new value','2009-01-24 21:28:51'),(2516,'*.auditPropertyHistory.newValue','en','lava','core',NULL,'auditPropertyHistory','newValue',NULL,'c','string','Yes','New Value',NULL,255,NULL,0,NULL,NULL,NULL,7,'The new value or {DELETED} when the record is for a property deletion','2009-01-24 21:28:51'),(2517,'*.auditPropertyHistory.auditTimestamp','en','lava','core',NULL,'auditPropertyHistory','auditTimestamp',NULL,'c','timestamp','Yes','Audit Time',NULL,NULL,NULL,0,NULL,NULL,NULL,8,'The time of the event (copied from the Audit_Event table for convenience','2009-01-24 21:28:51'),(2518,'*.auditPropertyHistory.oldText','en','lava','core',NULL,'auditPropertyHistory','oldText',NULL,'c','unlimitedtext','Yes','Old Text Value',NULL,16,NULL,0,'rows=\"10\" cols=\"80\"',NULL,NULL,9,'The old text value or {CREATED} if the record if for a property creation','2009-01-24 21:28:51'),(2519,'*.auditPropertyHistory.newText','en','lava','core',NULL,'auditPropertyHistory','newText',NULL,'c','unlimitedtext','Yes','New Text Value',NULL,16,NULL,0,'rows=\"10\" cols=\"80\"',NULL,NULL,10,'The new text value of {DELETED} if the record is for a property deletion','2009-01-24 21:28:51'),(2520,'*.authGroup.id','en','lava','core',NULL,'authGroup','id','details','c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the authorization group','2009-01-24 21:28:51'),(2521,'*.authGroup.disabled','en','lava','core',NULL,'authGroup','disabled','status','i','range','Yes','Disabled',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,2,'Disabled','2009-01-24 21:28:51'),(2522,'*.authGroup.groupName','en','lava','core',NULL,'authGroup','groupName','details','i','string','Yes','Group Name',NULL,50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-24 21:28:51'),(2523,'*.authGroup.groupNameWithStatus','en','lava','core',NULL,'authGroup','groupNameWithStatus',NULL,'c','string','Yes','Group Name',NULL,50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-24 21:28:51'),(2524,'*.authGroup.effectiveDate','en','lava','core',NULL,'authGroup','effectiveDate','status','i','date','Yes','Effective Date',NULL,NULL,NULL,0,NULL,NULL,NULL,3,'Effective Date of the authorization group','2009-01-24 21:28:51'),(2525,'*.authGroup.expirationDate','en','lava','core',NULL,'authGroup','expirationDate','status','i','date','Yes','Expiration Date',NULL,NULL,NULL,0,NULL,NULL,NULL,4,'Expiration Date of the authorization group','2009-01-24 21:28:51'),(2526,'*.authGroup.notes','en','lava','core',NULL,'authGroup','notes','note','i','text','Yes','Notes',NULL,255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,5,'Notes about the authorization group','2009-01-24 21:28:51'),(2527,'*.authPermission.id','en','lava','core',NULL,'authPermission','id','details','c','numeric','Yes','Permission ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the permission','2009-01-24 21:28:51'),(2528,'*.authPermission.role.id','en','lava','core',NULL,'authPermission','role.id',NULL,'c','numeric','Yes','Role ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the role','2009-01-24 21:28:51'),(2529,'*.authPermission.role.roleName','en','lava','core',NULL,'authPermission','role.roleName',NULL,'i','string','Yes','Role Name',NULL,25,NULL,0,NULL,NULL,NULL,2,'Name of the role','2009-01-24 21:28:51'),(2530,'*.authPermission.roleId','en','lava','core',NULL,'authPermission','roleId','details','i','range','Yes','Role',NULL,25,NULL,0,NULL,'auth.roles',NULL,2,'The role that the permission applies to','2009-01-24 21:28:51'),(2531,'*.authPermission.permitDeny','en','lava','core',NULL,'authPermission','permitDeny','details','i','range','Yes','Permit / Deny',NULL,10,NULL,0,NULL,'authPermission.permitDeny',NULL,3,'Whether to PERMIT or DENY the permission to the role','2009-01-24 21:28:51'),(2532,'*.authPermission.scope','en','lava','core','','authPermission','scope','details','i','string','Yes','Scope',NULL,25,0,0,'','','',4,'Scope','2009-01-01 00:00:00'),(2533,'*.authPermission.module','en','lava','core',NULL,'authPermission','module','details','i','suggest','Yes','Module',NULL,25,NULL,0,NULL,NULL,NULL,5,'the moule that the permission covers','2009-01-24 21:28:51'),(2534,'*.authPermission.section','en','lava','core',NULL,'authPermission','section','details','i','suggest','Yes','Section',NULL,25,NULL,0,NULL,NULL,NULL,6,'the section that the permission covers','2009-01-24 21:28:51'),(2535,'*.authPermission.target','en','lava','core',NULL,'authPermission','target','details','i','suggest','Yes','Target',NULL,25,NULL,0,NULL,NULL,NULL,7,'the target that the permission covers','2009-01-24 21:28:51'),(2536,'*.authPermission.mode','en','lava','core',NULL,'authPermission','mode','details','i','suggest','Yes','Mode',NULL,25,NULL,0,NULL,NULL,NULL,8,'the mode that the permission covers','2009-01-24 21:28:51'),(2537,'*.authPermission.notes','en','lava','core',NULL,'authPermission','notes','note','i','text','No','Notes',NULL,100,NULL,0,'rows=\"2\" cols=\"40\"',NULL,NULL,9,'Notes','2009-01-24 21:28:51'),(2538,'*.authRole.id','en','lava','core',NULL,'authRole','id',NULL,'c','numeric','Yes','Role ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the role','2009-01-24 21:28:51'),(2539,'*.authRole.roleName','en','lava','core',NULL,'authRole','roleName',NULL,'i','string','Yes','Role Name',NULL,25,NULL,0,NULL,NULL,NULL,2,'Name of the role','2009-01-24 21:28:51'),(2540,'*.authRole.patientAccess','en','lava','core',NULL,'authRole','patientAccess',NULL,'i','range','Yes','Patient Access',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,3,'This role confers patient access to the user','2009-01-24 21:28:51'),(2541,'*.authRole.phiAccess','en','lava','core',NULL,'authRole','phiAccess',NULL,'i','range','Yes','PHI Access',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,4,'This role confers access to Protected Health Identifiers/Informantion','2009-01-24 21:28:51'),(2542,'*.authRole.ghiAccess','en','lava','core',NULL,'authRole','ghiAccess',NULL,'i','range','Yes','Genetic Access',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,5,'This role confers access to genetic health information to the user','2009-01-24 21:28:51'),(2543,'*.authRole.notes','en','lava','core',NULL,'authRole','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,8,'Notes','2009-01-24 21:28:51'),(2544,'*.authRole.notes','en','lava','core',NULL,'authRole','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,8,'Notes','2009-01-24 21:28:51'),(2545,'*.authRole.summaryInfo','en','lava','core',NULL,'authRole','summaryInfo','details','c','string','No','Summary',NULL,255,NULL,0,NULL,NULL,NULL,13,'Summary information for the role','2009-01-01 00:00:00'),(2546,'*.authUser.id','en','lava','core',NULL,'authUser','id',NULL,'c','numeric','Yes','User ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the user','2009-01-24 21:28:51'),(2547,'*.authUser.role.id','en','lava','core',NULL,'authUser','role.id','details','c','numeric','Yes','Role ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID of the role','2009-01-24 21:28:51'),(2548,'*.authUser.userName','en','lava','core',NULL,'authUser','userName','details','i','string','Yes','User Name',NULL,50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-24 21:28:51'),(2549,'*.authUser.userNameWithStatus','en','lava','core',NULL,'authUser','userNameWithStatus',NULL,'c','string','Yes','User Name',NULL,50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name] with status','2009-01-24 21:28:51'),(2550,'filter.authUser.userName','en','lava','core','filter','authUser','userName',NULL,'i','string','No','User Name',NULL,50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-24 21:28:51'),(2551,'*.authUser.login','en','lava','core',NULL,'authUser','login','details','i','string','No','Network Login',NULL,100,NULL,0,NULL,NULL,NULL,3,'Network Login for the user','2009-01-24 21:28:51'),(2552,'filter.authUser.login','en','lava','core','filter','authUser','login',NULL,'i','string','No','Network Login',NULL,100,NULL,0,NULL,NULL,NULL,3,'Network Login for the user','2009-01-24 21:28:51'),(2553,'*.authUser.email','en','lava','core',NULL,'authUser','email','details','i','text','No','Email',NULL,100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,4,'Email','2009-05-12 14:45:03'),(2554,'*.authUser.phone','en','lava','core',NULL,'authUser','phone','details','i','string','No','Phone',NULL,25,NULL,0,NULL,NULL,NULL,5,'Phone','2009-05-12 14:45:03'),(2555,'*.authUser.accessAgreementDate','en','lava','core',NULL,'authUser','accessAgreementDate','status','i','date','No','Access Agreement Date',NULL,NULL,NULL,0,NULL,NULL,NULL,7,'Date the user signed an Access Agreement for using the system','2009-01-24 21:28:51'),(2556,'filter.authUser.accessAgreementDateEnd','en','lava','core','filter','authUser','accessAgreementDateEnd',NULL,'i','date','No','and',NULL,NULL,NULL,0,NULL,NULL,NULL,7,'Date the user signed an Access Agreement for using the system','2009-01-24 21:28:51'),(2557,'filter.authUser.accessAgreementDateStart','en','lava','core','filter','authUser','accessAgreementDateStart',NULL,'i','date','No','Agreement Date Between',NULL,NULL,NULL,0,NULL,NULL,NULL,7,'Date the user signed an Access Agreement for using the system','2009-01-24 21:28:51'),(2558,'*.authUser.effectiveDate','en','lava','core',NULL,'authUser','effectiveDate','status','i','date','Yes','Effective Date',NULL,NULL,NULL,0,NULL,NULL,NULL,8,'Effective Date','2009-01-24 21:28:51'),(2559,'filter.authUser.effectiveDateEnd','en','lava','core','filter','authUser','effectiveDateEnd',NULL,'i','date','No','and',NULL,NULL,NULL,0,NULL,NULL,NULL,8,'Effective Date','2009-01-24 21:28:51'),(2560,'filter.authUser.effectiveDateStart','en','lava','core','filter','authUser','effectiveDateStart',NULL,'i','date','No','Effective Date Between',NULL,NULL,NULL,0,NULL,NULL,NULL,8,'Effective Date','2009-01-24 21:28:51'),(2561,'*.authUser.expirationDate','en','lava','core',NULL,'authUser','expirationDate','status','i','date','No','Expiration Date',NULL,NULL,NULL,0,NULL,NULL,NULL,9,'Expiration Date','2009-01-24 21:28:51'),(2562,'filter.authUser.expirationDateEnd','en','lava','core','filter','authUser','expirationDateEnd',NULL,'i','date','No','and',NULL,NULL,NULL,0,NULL,NULL,NULL,9,'Expiration Date','2009-01-24 21:28:51'),(2563,'filter.authUser.expirationDateStart','en','lava','core','filter','authUser','expirationDateStart',NULL,'i','date','No','Expiration Date Between',NULL,NULL,NULL,0,NULL,NULL,NULL,9,'Expiration Date','2009-01-24 21:28:51'),(2564,'*.authUser.shortUserName','en','lava','core',NULL,'authUser','shortUserName','details','c','string','No','Short User Name',NULL,53,NULL,0,NULL,NULL,NULL,10,'Shortened User name','2009-01-24 21:28:51'),(2565,'*.authUser.shortUserNameRev','en','lava','core',NULL,'authUser','shortUserNameRev','details','c','string','No','Short User Name Reversed',NULL,54,NULL,0,NULL,NULL,NULL,11,'Shortened User Name Reversed','2009-01-24 21:28:51'),(2566,'*.authUser.notes','en','lava','core',NULL,'authUser','notes','note','i','text','No','Notes',NULL,255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,12,'Notes','2009-01-24 21:28:51'),(2567,'*.authUser.authenticationType','en','lava','core',NULL,'authUser','authenticationType','details','i','range','Yes','Auth Type',NULL,10,NULL,0,NULL,'authUser.authenticationType',NULL,12,'Authentication Type','2009-05-12 14:45:03'),(2568,'*.authUser.disabled','en','lava','core',NULL,'authUser','disabled','status','i','range','Yes','Disabled',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,13,'Disabled','2009-01-24 21:28:51'),(2569,'*.authUser.password','en','lava','core',NULL,'authUser','password','local','c','text','No','Password (hashed)',NULL,100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,13,'Password Hash','2009-05-12 14:45:03'),(2570,'*.authUser.passwordExpiration','en','lava','core',NULL,'authUser','passwordExpiration','local','c','timestamp','No','Password Exp.',NULL,NULL,NULL,0,NULL,NULL,NULL,14,'Password Expiration','2009-05-12 14:45:03'),(2571,'*.authUser.passwordResetToken','en','lava','core',NULL,'authUser','passwordResetToken','local','c','text','No','Reset Token',NULL,100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,15,'Password Reset Token','2009-05-12 14:45:03'),(2572,'*.authUser.passwordResetExpiration','en','lava','core',NULL,'authUser','passwordResetExpiration','local','c','timestamp','No','Reset Exp.',NULL,NULL,NULL,0,NULL,NULL,NULL,16,'Password Reset Token Expires','2009-05-12 14:45:03'),(2573,'*.authUser.failedLoginCount','en','lava','core',NULL,'authUser','failedLoginCount','local','c','numeric','No','Failed Logins',NULL,NULL,NULL,0,NULL,NULL,NULL,17,'Failed Login Attempts','2009-05-12 14:45:03'),(2574,'*.authUser.lastFailedLogin','en','lava','core',NULL,'authUser','lastFailedLogin','local','c','timestamp','No','Last Failed Login',NULL,NULL,NULL,0,NULL,NULL,NULL,18,'Last Failed Logon Attempt','2009-05-12 14:45:03'),(2575,'*.authUser.accountLocked','en','lava','core',NULL,'authUser','accountLocked','local','c','timestamp','No','Account Locked',NULL,NULL,NULL,0,NULL,NULL,NULL,19,'Account Locked Timestamp','2009-05-12 14:45:03'),(2576,'*.authUserGroup.group.id','en','lava','core',NULL,'authUserGroup','group.id',NULL,'c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the authorization group','2009-01-24 21:28:51'),(2577,'*.authUserGroup.id','en','lava','core',NULL,'authUserGroup','id',NULL,'c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the User Role Assocaition','2009-01-24 21:28:51'),(2578,'*.authUserGroup.user.id','en','lava','core',NULL,'authUserGroup','user.id','details','c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-24 21:28:51'),(2579,'*.authUserGroup.user.id','en','lava','core',NULL,'authUserGroup','user.id','details','c','numeric','Yes','User ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID of the User','2009-01-24 21:28:51'),(2580,'*.authUserGroup.group.groupNameWithStatus','en','lava','core',NULL,'authUserGroup','group.groupNameWithStatus',NULL,'i','string','Yes','Group Name',NULL,50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-24 21:28:51'),(2581,'*.authUserGroup.user.login','en','lava','core',NULL,'authUserGroup','user.login','details','i','string','Yes','User Name',NULL,50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-24 21:28:51'),(2582,'*.authUserGroup.user.userNameWithStatus','en','lava','core',NULL,'authUserGroup','user.userNameWithStatus','details','i','string','Yes','User Name',NULL,50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-24 21:28:51'),(2583,'*.authUserGroup.userId','en','lava','core',NULL,'authUserGroup','userId','details','i','range','Yes','User',NULL,NULL,NULL,0,NULL,'auth.users',NULL,2,'Unique ID of the User','2009-01-24 21:28:51'),(2584,'*.authUserGroup.groupId','en','lava','core',NULL,'authUserGroup','groupId','details','i','range','Yes','Group',NULL,NULL,NULL,0,NULL,'auth.groups',NULL,3,'Unique ID of the Group','2009-01-24 21:28:51'),(2585,'*.authUserGroup.notes','en','lava','core',NULL,'authUserGroup','notes','note','i','text','No','Notes',NULL,255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,6,'Notes','2009-01-24 21:28:51'),(2586,'*.authUserPasswordDto.oldPassword','en','lava','core',NULL,'authUserPasswordDto','oldPassword',NULL,'i','password','Yes','Current Password',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Current Password','2009-05-12 14:45:03'),(2587,'*.authUserPasswordDto.newPassword','en','lava','core',NULL,'authUserPasswordDto','newPassword',NULL,'i','password','Yes','New Password',NULL,NULL,NULL,0,NULL,NULL,NULL,2,'New Password','2009-05-12 14:45:03'),(2588,'*.authUserPasswordDto.newPasswordConfirm','en','lava','core',NULL,'authUserPasswordDto','newPasswordConfirm',NULL,'i','password','Yes','New Password Confirm',NULL,NULL,NULL,0,NULL,NULL,NULL,3,'New Password Confirm','2009-05-12 14:45:03'),(2589,'*.authUserRole.group.id','en','lava','core',NULL,'authUserRole','group.id',NULL,'c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the authorization group','2009-01-24 21:28:51'),(2590,'*.authUserRole.id','en','lava','core',NULL,'authUserRole','id','details','c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the User/Group Role Association','2009-01-24 21:28:51'),(2591,'*.authUserRole.role.id','en','lava','core',NULL,'authUserRole','role.id',NULL,'c','numeric','Yes','Role ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the role','2009-01-24 21:28:51'),(2592,'*.authUserRole.user.id','en','lava','core',NULL,'authUserRole','user.id',NULL,'c','numeric','Yes','User ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID of the User','2009-01-24 21:28:51'),(2593,'*.authUserRole.group.groupNameWithStatus','en','lava','core',NULL,'authUserRole','group.groupNameWithStatus',NULL,'i','string','Yes','Group Name',NULL,50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-24 21:28:51'),(2594,'*.authUserRole.role.roleName','en','lava','core',NULL,'authUserRole','role.roleName',NULL,'i','string','Yes','Role Name',NULL,25,NULL,0,NULL,NULL,NULL,2,'Name of the role','2009-01-24 21:28:51'),(2595,'*.authUserRole.user.userNameWithStatus','en','lava','core',NULL,'authUserRole','user.userNameWithStatus',NULL,'i','string','Yes','User Name',NULL,50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-24 21:28:51'),(2596,'*.authUserRole.role.summaryInfo','en','lava','core',NULL,'authUserRole','role.summaryInfo',NULL,'i','range','Yes','Summary',NULL,255,NULL,0,NULL,NULL,NULL,3,'Summary info about the role','2009-01-24 21:28:51'),(2597,'*.authUserRole.roleId','en','lava','core',NULL,'authUserRole','roleId','details','i','range','Yes','Role',NULL,25,NULL,0,NULL,'auth.roles',NULL,3,'The role name','2009-01-24 21:28:51'),(2598,'*.authUserRole.userId','en','lava','core',NULL,'authUserRole','userId','details','i','range','No','User',NULL,NULL,NULL,0,NULL,'auth.users',NULL,7,'The user to assign the role to','2009-01-24 21:28:51'),(2599,'*.authUserRole.groupId','en','lava','core',NULL,'authUserRole','groupId','details','i','range','No','Group',NULL,NULL,NULL,0,NULL,'auth.groups',NULL,8,'the group to assign the role to','2009-01-24 21:28:51'),(2600,'*.authUserRole.notes','en','lava','core',NULL,'authUserRole','notes','note','i','text','No','Notes',NULL,255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,12,'Notes','2009-01-24 21:28:51'),(2601,'*.authUserRole.summaryInfo','en','lava','core','','authUserRole','summaryInfo','details','c','string','No','Summary',NULL,255,NULL,0,NULL,NULL,'',13,'Summary information for the role assignment','2009-01-01 00:00:00'),(2602,'*.calendar.id','en','lava','core',NULL,'calendar','id','details','c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2009-03-31 10:56:31'),(2603,'*.calendar.type','en','lava','core','','calendar','type','details','c','range','Yes','Type',NULL,25,NULL,0,NULL,'calendar.type','',2,'Type','2009-06-10 00:00:00'),(2604,'*.calendar.name','en','lava','core',NULL,'calendar','name','details','i','string','Yes','Name',NULL,100,NULL,0,NULL,NULL,NULL,3,NULL,'2009-03-31 10:56:31'),(2605,'*.calendar.description','en','lava','core',NULL,'calendar','description','details','i','text','No','Description',NULL,255,NULL,0,'rows=\"4\" cols=\"45\"',NULL,NULL,4,NULL,'2009-03-31 10:56:31'),(2606,'*.calendar.notes','en','lava','core',NULL,'calendar','notes','notes','i','unlimitedtext','No','Notes',NULL,NULL,NULL,0,'rows=\"10\" cols=\"45\"',NULL,NULL,5,NULL,'2009-03-31 10:56:31'),(2607,'*.lavaFile.id','en','lava','core',NULL,'lavaFile','id','fileInfo','c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2011-10-20 15:03:20'),(2608,'*.lavaFile.statusBlock','en','lava','core',NULL,'lavaFile','statusBlock','summary','c','text','No','Status',NULL,NULL,NULL,0,'rows=\"2\" cols=\"80\"',NULL,NULL,1,NULL,'2011-11-04 20:35:36'),(2609,'*.lavaFile.name','en','lava','core',NULL,'lavaFile','name','fileInfo','i','string','No','File Name',NULL,255,40,0,NULL,NULL,NULL,2,NULL,'2011-10-20 15:03:20'),(2610,'*.lavaFile.fileType','en','lava','core',NULL,'lavaFile','fileType','fileInfo','c','suggest','No','File Type',NULL,255,40,0,NULL,'lavaFile.fileType',NULL,3,NULL,'2011-10-20 15:03:20'),(2611,'*.lavaFile.contentType','en','lava','core',NULL,'lavaFile','contentType','fileInfo','i','suggest','No','File Contents',NULL,100,40,0,NULL,'lavaFile.contentType',NULL,4,NULL,'2011-10-20 15:03:20'),(2612,'*.lavaFile.fileStatusDate','en','lava','core',NULL,'lavaFile','fileStatusDate','status','i','date','No','Status Date',NULL,NULL,NULL,0,NULL,NULL,NULL,5,NULL,'2011-10-20 15:03:20'),(2613,'*.lavaFile.fileStatus','en','lava','core',NULL,'lavaFile','fileStatus','status','i','range','No','Status',NULL,50,NULL,0,NULL,'lavaFile.status',NULL,6,NULL,'2011-10-20 15:03:20'),(2614,'*.lavaFile.fileStatusBy','en','lava','core',NULL,'lavaFile','fileStatusBy','status','i','range','No','Status By',NULL,50,NULL,0,NULL,'lavaFile.statusBy',NULL,7,NULL,'2011-10-20 15:03:20'),(2615,'*.lavaFile.repositoryId','en','lava','core',NULL,'lavaFile','repositoryId','repositoryInfo','c','string','No','Repository ID',NULL,100,40,0,NULL,NULL,NULL,8,NULL,'2011-10-20 15:03:20'),(2616,'*.lavaFile.fileId','en','lava','core',NULL,'lavaFile','fileId','repositoryInfo','c','string','No','File ID',NULL,100,40,0,NULL,NULL,NULL,9,NULL,'2011-10-20 15:03:20'),(2617,'*.lavaFile.location','en','lava','core',NULL,'lavaFile','location','repositoryInfo','c','text','No','Location',NULL,1000,40,0,'rows=\"3\" cols=\"80\"',NULL,NULL,10,NULL,'2011-10-20 15:03:20'),(2618,'*.lavaFile.checksum','en','lava','core',NULL,'lavaFile','checksum','fileInfo','c','string','No','Checksum',NULL,100,40,0,NULL,NULL,NULL,11,NULL,'2011-10-20 15:03:20'),(2619,'*.lavaSession.id','en','lava','core',NULL,'LavaSession','id',NULL,'c','numeric','Yes','Session ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2009-01-24 21:28:51'),(2620,'*.lavaSession.serverInstanceId','en','lava','core',NULL,'LavaSession','serverInstanceId',NULL,'c','numeric','Yes','Server Instance ID',NULL,NULL,NULL,0,NULL,NULL,NULL,2,NULL,'2009-01-24 21:28:51'),(2621,'*.lavaSession.createTimestamp','en','lava','core',NULL,'LavaSession','createTimestamp',NULL,'c','datetime','Yes','Created Time',NULL,NULL,NULL,0,NULL,NULL,NULL,3,NULL,'2009-01-24 21:28:51'),(2622,'*.lavaSession.accessTimestamp','en','lava','core',NULL,'LavaSession','accessTimestamp',NULL,'c','datetime','Yes','Accessed Time',NULL,NULL,NULL,0,NULL,NULL,NULL,4,NULL,'2009-01-24 21:28:51'),(2623,'*.lavaSession.expireTimestamp','en','lava','core',NULL,'LavaSession','expireTimestamp',NULL,'c','datetime','Yes','Expiration Time',NULL,NULL,NULL,0,NULL,NULL,NULL,5,NULL,'2009-01-24 21:28:51'),(2624,'*.lavaSession.currentStatus','en','lava','core',NULL,'LavaSession','currentStatus',NULL,'c','range','Yes','Current Status',NULL,NULL,NULL,0,NULL,'lavaSession.status',NULL,6,NULL,'2009-01-24 21:28:51'),(2625,'*.lavaSession.userId','en','lava','core',NULL,'LavaSession','userId',NULL,'c','numeric','No','User ID',NULL,NULL,NULL,0,NULL,NULL,NULL,7,NULL,'2009-01-24 21:28:51'),(2626,'*.lavaSession.username','en','lava','core',NULL,'LavaSession','username',NULL,'c','string','No','Username',NULL,NULL,NULL,0,NULL,NULL,NULL,8,NULL,'2009-01-24 21:28:51'),(2627,'*.lavaSession.hostname','en','lava','core',NULL,'LavaSession','hostname',NULL,'c','string','No','Hostname',NULL,NULL,NULL,0,NULL,NULL,NULL,9,NULL,'2009-01-24 21:28:51'),(2628,'*.lavaSession.httpSessionId','en','lava','core',NULL,'LavaSession','httpSessionId',NULL,'c','string','No','HTTP Session',NULL,NULL,40,0,NULL,NULL,NULL,10,NULL,'2009-01-24 21:28:51'),(2629,'*.lavaSession.disconnectTime','en','lava','core',NULL,'LavaSession','disconnectTime',NULL,'i','time','No','Time',NULL,NULL,NULL,0,NULL,NULL,NULL,11,NULL,'2009-01-24 21:28:51'),(2630,'*.lavaSession.disconnectMessage','en','lava','core',NULL,'LavaSession','disconnectMessage',NULL,'i','text','No','Disconnect Message',NULL,NULL,NULL,0,'rows=\"4\", cols=\"45\"',NULL,NULL,12,NULL,'2009-01-24 21:28:51'),(2631,'*.lavaSession.notes','en','lava','core',NULL,'LavaSession','notes',NULL,'i','text','No','Notes',NULL,NULL,NULL,0,'rows=\"4\" cols=\"45\"',NULL,NULL,13,NULL,'2009-01-24 21:28:51'),(2632,'*.lavaSession.disconnectDate','en','lava','core',NULL,'lavaSession','disconnectDate',NULL,'i','date','No','Disconnect Date',NULL,10,NULL,0,NULL,NULL,NULL,16,NULL,'2009-04-27 15:11:44'),(2633,'*.reservation.organizer.userName','en','lava','core',NULL,'reservation','organizer.userName','details','c','string','No','Reserved By',NULL,NULL,NULL,0,NULL,NULL,NULL,14,'Reserved By','2009-06-11 09:40:26'),(2634,'*.reservation.organizerId','en','lava','core',NULL,'reservation','organizerId','details','i','range','Yes','Reserved By',NULL,NULL,NULL,0,NULL,'appointment.organizer',NULL,15,'Reserved By','2009-06-11 09:37:29'),(2635,'*.resourceCalendar.resourceType','en','lava','core',NULL,'resourceCalendar','resource_type','resource','i','range','Yes','Resource Type',NULL,25,NULL,0,NULL,'resourceCalendar.resourceType',NULL,2,'Resource Type','2009-06-03 10:27:39'),(2636,'*.resourceCalendar.location','en','lava','core',NULL,'resourceCalendar','location','resource','i','text','No','Location',NULL,100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,3,'Location','2009-06-03 10:27:39'),(2637,'*.resourceCalendar.contactId','en','lava','core',NULL,'resourceCalendar','contactId','resource','i','range','Yes','Contact',NULL,NULL,NULL,0,NULL,'resourceCalendar.contact',NULL,4,'Contact','2009-06-03 10:27:39'),(2638,'*.resourceCalendar.contact.email','en','lava','core',NULL,'resourceCalendar','contact.email','resource','c','string','No','Contact Email',NULL,100,NULL,0,NULL,NULL,NULL,6,'Contact Email','2009-06-03 10:27:39'),(2639,'*.resourceCalendar.contact.phone','en','lava','core',NULL,'resourceCalendar','contact.phone','resource','c','string','No','Contact Phone',NULL,25,NULL,0,NULL,NULL,NULL,7,'Contact Phone','2009-06-03 10:27:39'),(2640,'*.userInfo.email','en','lava','core',NULL,'userInfo','email','details','i','text','No','Email',NULL,100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,1,'Email','2009-05-26 16:48:36'),(2641,'*.userInfo.phone','en','lava','core',NULL,'userInfo','phone','details','i','string','No','Phone',NULL,25,NULL,0,NULL,NULL,NULL,2,'Phone','2009-05-26 16:49:22'),(3178,'*.addEnrollmentStatus.patient_fullName','en','lava','crms',NULL,'addEnrollmentStatus','patient_fullName','','c','string','No','Patient',NULL,NULL,NULL,0,'','',NULL,1,'','2009-01-24 21:28:51'),(3179,'*.addEnrollmentStatus.enrollmentStatus_projName','en','lava','crms',NULL,'addEnrollmentStatus','enrollmentStatus_projName',NULL,'i','range','Yes','Project',NULL,NULL,NULL,0,NULL,'enrollmentStatus.patientUnassignedProjects',NULL,10,NULL,'2009-01-24 21:28:51'),(3180,'*.addEnrollmentStatus.projName','en','lava','crms',NULL,'addEnrollmentStatus','projName','','i','range','Yes','Project',NULL,NULL,NULL,0,'','enrollmentStatus.patientUnassignedProjects',NULL,10,'','2009-01-24 21:28:51'),(3181,'*.addEnrollmentStatus.status','en','lava','crms',NULL,'addEnrollmentStatus','status','','i','range','Yes','Status',NULL,NULL,NULL,0,'','enrollmentStatus.projectStatus',NULL,11,'','2009-01-24 21:28:51'),(3182,'*.addEnrollmentStatus.statusDate','en','lava','crms',NULL,'addEnrollmentStatus','statusDate','','i','date','Yes','Status Date',NULL,NULL,NULL,0,'','',NULL,12,'','2009-01-24 21:28:51'),(3183,'*.addPatient.patient_gender','en','lava','crms',NULL,'addPatient','patient_gender',NULL,'i','range','Yes','Gender',NULL,NULL,NULL,0,NULL,'generic.gender',NULL,NULL,NULL,'2010-09-02 12:19:55'),(3184,'*.addPatient.patient_hand','en','lava','crms',NULL,'addPatient','patient_hand',NULL,'i','range','No','Handedness',NULL,NULL,NULL,0,NULL,'patient.handedness',NULL,NULL,NULL,'2010-09-02 12:53:05'),(3185,'*.addPatient.ignoreMatches','en','lava','crms',NULL,'addPatient','ignoreMatches','','i','toggle','No','This is a new patient/subject. Ignore possible matches.',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3186,'*.addPatient.patient_firstName','en','lava','crms',NULL,'addPatient','patient_firstName','','i','string','Yes','First Name',NULL,NULL,NULL,0,'','',NULL,2,'','2009-01-24 21:28:51'),(3187,'*.addPatient.patient_middleInitial','en','lava','crms',NULL,'addPatient','patient_middleInitial','','i','string','No','Middle Initial',NULL,NULL,NULL,0,'','',NULL,3,'','2009-01-24 21:28:51'),(3188,'*.addPatient.patient_lastName','en','lava','crms',NULL,'addPatient','patient_lastName','','i','string','Yes','Last Name',NULL,NULL,NULL,0,'','',NULL,4,'','2009-01-24 21:28:51'),(3189,'*.addPatient.patient_suffix','en','lava','crms',NULL,'addPatient','patient_suffix','','i','string','No','Suffix',NULL,NULL,NULL,0,'','',NULL,5,'','2009-01-24 21:28:51'),(3190,'*.addPatient.patient_degree','en','lava','crms',NULL,'addPatient','patient_degree','','i','string','No','Degree',NULL,NULL,NULL,0,'','',NULL,6,'','2009-01-24 21:28:51'),(3191,'*.addPatient.patient_birthDate','en','lava','crms',NULL,'addPatient','patient_birthDate','','i','date','Yes','Date of Birth',NULL,NULL,NULL,0,'','',NULL,9,'','2009-01-24 21:28:51'),(3192,'*.addPatient.enrollmentStatus_projName','en','lava','crms',NULL,'addPatient','enrollmentStatus_projName','','i','range','Yes','Initial Project',NULL,NULL,NULL,0,'','addPatient.projectList',NULL,10,'','2009-01-24 21:28:51'),(3193,'*.addPatient.projName','en','lava','crms',NULL,'addPatient','projName',NULL,'i','range','Yes','Initial Project',NULL,NULL,NULL,0,NULL,'addPatient.projectList',NULL,10,NULL,'2009-01-24 21:28:51'),(3194,'*.addPatient.status','en','lava','crms',NULL,'addPatient','status','','i','range','Yes','Initial Status',NULL,NULL,NULL,0,'','enrollmentStatus.projectStatus',NULL,11,'','2009-01-24 21:28:51'),(3195,'*.addPatient.statusDate','en','lava','crms',NULL,'addPatient','statusDate','','i','date','Yes','Status Date',NULL,NULL,NULL,0,'','',NULL,12,'','2009-01-24 21:28:51'),(3196,'*.addPatient.deidentified','en','lava','crms',NULL,'addPatient','deidentified','','i','toggle','No','Use Deidentified ID',NULL,NULL,NULL,0,'','',NULL,20,'','2009-01-24 21:28:51'),(3197,'*.addPatient.subjectId','en','lava','crms',NULL,'addPatient','subjectId','','i','string','Yes','Subject ID',NULL,NULL,NULL,0,'','',NULL,21,'','2009-01-24 21:28:51'),(3198,'filter.caregiver.lastName','en','lava','crms','filter','caregiver','lastName','','i','string','No','Caregiver Last Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3199,'filter.caregiver.firstName','en','lava','crms','filter','caregiver','firstName','','i','string','No','Caregiver First Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3200,'*.caregiver.id','en','lava','crms',NULL,'caregiver','id','','c','numeric','Yes','',NULL,NULL,NULL,0,'','',NULL,1,'','2009-01-24 21:28:51'),(3201,'*.caregiver.lastName','en','lava','crms',NULL,'caregiver','lastName','','i','string','Yes','Last Name',NULL,NULL,NULL,0,'','',NULL,3,'','2009-01-24 21:28:51'),(3202,'*.caregiver.firstName','en','lava','crms',NULL,'caregiver','firstName','','i','string','Yes','First Name',NULL,NULL,NULL,0,'','',NULL,4,'','2009-01-24 21:28:51'),(3203,'*.caregiver.gender','en','lava','crms',NULL,'caregiver','gender','','i','range','No','Gender',NULL,NULL,NULL,0,'','generic.gender',NULL,5,'','2009-01-24 21:28:51'),(3204,'*.caregiver.relation','en','lava','crms',NULL,'caregiver','relation','','i','range','Yes','Relation to patient',NULL,NULL,NULL,0,'','caregiver.contactRelations',NULL,6,'','2009-01-24 21:28:51'),(3205,'*.caregiver.livesWithPatient','en','lava','crms',NULL,'caregiver','livesWithPatient','','i','range','Yes','Lives with patient',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,7,'','2009-01-24 21:28:51'),(3206,'*.caregiver.active','en','lava','crms',NULL,'caregiver','active','','i','range','Yes','Is current/active',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,8,'','2009-01-24 21:28:51'),(3207,'*.caregiver.primaryLanguage','en','lava','crms',NULL,'caregiver','primaryLanguage','Language Details','i','suggest','No','Primary Language',NULL,25,NULL,0,'','patient.patientLanguage',NULL,8,'','2009-01-24 21:28:51'),(3208,'*.caregiver.transNeeded','en','lava','crms',NULL,'caregiver','transNeeded','Language Details','i','range','No','Interpreter Needed',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,9,'','2009-01-24 21:28:51'),(3209,'*.caregiver.transLanguage','en','lava','crms',NULL,'caregiver','transLanguage','Language Details','i','suggest','No','Interpreter Type',NULL,25,NULL,0,'','patient.patientLanguage',NULL,10,'','2009-01-24 21:28:51'),(3210,'*.caregiver.isPrimaryContact','en','lava','crms',NULL,'caregiver','isPrimaryContact','','i','range','No','Is the primary contact',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,11,'','2009-01-24 21:28:51'),(3211,'*.caregiver.isContact','en','lava','crms',NULL,'caregiver','isContact','','i','range','No','Is other contact',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,12,'','2009-01-24 21:28:51'),(3212,'*.caregiver.isContactNotes','en','lava','crms',NULL,'caregiver','isContactNotes','','i','string','No','Other contact description',NULL,100,NULL,0,'','',NULL,13,'','2009-01-24 21:28:51'),(3213,'*.caregiver.isCaregiver','en','lava','crms',NULL,'caregiver','isCaregiver','','i','range','No','Is a caregiver',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,14,'','2009-01-24 21:28:51'),(3214,'*.caregiver.isInformant','en','lava','crms',NULL,'caregiver','isInformant','','i','range','No','Is an informant',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,15,'','2009-01-24 21:28:51'),(3215,'*.caregiver.isNextOfKin','en','lava','crms',NULL,'caregiver','isNextOfKin','','i','range','No','Is the next of kin',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,16,'','2009-01-24 21:28:51'),(3216,'*.caregiver.isResearchSurrogate','en','lava','crms',NULL,'caregiver','isResearchSurrogate','','i','range','No','Is the research surrogate',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,16,'','2009-01-24 21:28:51'),(3217,'*.caregiver.isPowerOfAttorney','en','lava','crms',NULL,'caregiver','isPowerOfAttorney','','i','range','No','Has healthcare power of attorney',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,17,'','2009-01-24 21:28:51'),(3218,'*.caregiver.isOtherRole','en','lava','crms',NULL,'caregiver','isOtherRole','','i','range','No','Has other role',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,18,'','2009-01-24 21:28:51'),(3219,'*.caregiver.otherRoleDesc','en','lava','crms',NULL,'caregiver','otherRoleDesc','','i','string','No','Other role description',NULL,50,NULL,0,'','',NULL,19,'','2009-01-24 21:28:51'),(3220,'*.caregiver.note','en','lava','crms',NULL,'caregiver','note','','i','text','No','',NULL,255,NULL,0,'rows=\"4\" cols=\"45\"','',NULL,20,'','2009-01-24 21:28:51'),(3221,'*.caregiver.birthDate','en','lava','crms',NULL,'caregiver','birthDate','','i','date','No','DOB',NULL,NULL,NULL,0,'','',NULL,21,'','2009-01-24 21:28:51'),(3222,'*.caregiver.patient_fullName','en','lava','crms',NULL,'caregiver','patient_fullName','','c','string','No','Patient',NULL,NULL,NULL,0,'','',NULL,21,'','2009-01-24 21:28:51'),(3223,'*.caregiver.patient_fullNameNoSuffix','en','lava','crms',NULL,'caregiver','patient_fullNameNoSuffix','','c','string','No','Patient',NULL,NULL,NULL,0,'','',NULL,21,'','2009-01-24 21:28:51'),(3224,'*.caregiver.education','en','lava','crms',NULL,'caregiver','education','','i','range','No','Education',NULL,NULL,NULL,0,'','generic.education',NULL,22,'','2009-01-24 21:28:51'),(3225,'*.caregiver.race','en','lava','crms',NULL,'caregiver','race','','i','range','No','Race',NULL,NULL,NULL,0,'','generic.race',NULL,23,'','2009-01-24 21:28:51'),(3226,'*.caregiver.maritalStatus','en','lava','crms',NULL,'caregiver','maritalStatus','','i','range','No','Marital Status',NULL,NULL,NULL,0,'','patient.maritalStatus',NULL,24,'','2009-01-24 21:28:51'),(3227,'*.caregiver.occupation','en','lava','crms',NULL,'caregiver','occupation','','i','string','No','Occupation',NULL,NULL,NULL,0,'','',NULL,25,'','2009-01-24 21:28:51'),(3228,'*.caregiver.age','en','lava','crms',NULL,'caregiver','age','','c','numeric','No','Age',NULL,NULL,NULL,0,'','',NULL,26,'','2009-01-24 21:28:51'),(3229,'*.caregiver.fullName','en','lava','crms',NULL,'caregiver','fullName','','c','string','No','Caregiver',NULL,NULL,NULL,0,'','',NULL,27,'','2009-01-24 21:28:51'),(3230,'*.caregiver.fullNameRev','en','lava','crms',NULL,'caregiver','fullNameRev','','c','string','No','Caregiver',NULL,NULL,NULL,0,'','',NULL,28,'','2009-01-24 21:28:51'),(3231,'*.caregiver.contactDesc','en','lava','crms',NULL,'caregiver','contactDesc','','i','string','','Contact Status',NULL,NULL,NULL,0,'','',NULL,29,'','2009-01-24 21:28:51'),(3232,'*.caregiver.rolesDesc','en','lava','crms',NULL,'caregiver','rolesDesc','','i','string','','Roles Description',NULL,NULL,NULL,0,'','',NULL,30,'','2009-01-24 21:28:51'),(3233,'filter.caregiver.patient.firstName','en','lava','crms','filter','caregiver.patient','firstName','','i','string','no','Patient First Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3234,'filter.caregiver.patient.lastName','en','lava','crms','filter','caregiver.patient','lastName','','i','string','no','Patient Last Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3235,'filter.consent.consentDateEnd','en','lava','crms','filter','consent','consentDateEnd','','i','date','no','and',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3236,'filter.consent.consentDateStart','en','lava','crms','filter','consent','consentDateStart','','i','date','no','Consent Date between',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3237,'filter.consent.consentDeclined','en','lava','crms','filter','consent','consentDeclined','','i','range','no','Declined',NULL,NULL,NULL,0,'','generic.textYesNo',NULL,NULL,'','2009-01-24 21:28:51'),(3238,'filter.consent.consentType','en','lava','crms','filter','consent','consentType','','i','range','no','Consent Type',NULL,NULL,NULL,0,'','consent.consentTypes',NULL,NULL,'','2009-01-24 21:28:51'),(3239,'filter.consent.patient.firstName','en','lava','crms','filter','consent','patient.firstName','','i','string','no','First Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3240,'filter.consent.patient.lastName','en','lava','crms','filter','consent','patient.lastName','','i','string','no','Last Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3241,'filter.consent.projName','en','lava','crms','filter','consent','projName','','i','string','No','Project',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3242,'*.consent.hipaa','en','lava','crms',NULL,'consent','hipaa',NULL,'i','range','No','HIPAA',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,NULL,'HIPAA','2011-03-17 12:46:26'),(3243,'*.consent.consentTypeBlock','en','lava','crms',NULL,'consent','consentTypeBlock',NULL,'c','string','No','Consent Type',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Consent Type','2011-03-17 12:47:44'),(3244,'*.consent.id','en','lava','crms',NULL,'consent','id','','c','string','Yes','Consent ID',NULL,NULL,NULL,0,'','',NULL,1,'','2009-01-24 21:28:51'),(3245,'*.consent.caregiverId','en','lava','crms',NULL,'consent','caregiverId','','i','range','No','Surrogate Consent',NULL,NULL,NULL,0,'','patient.caregivers',NULL,3,'','2009-01-24 21:28:51'),(3246,'*.consent.projName','en','lava','crms',NULL,'consent','projName','','i','range','Yes','Project',NULL,NULL,NULL,0,'','enrollmentStatus.patientProjects',NULL,4,'','2009-01-24 21:28:51'),(3247,'*.consent.consentType','en','lava','crms',NULL,'consent','consentType','','i','range','Yes','Consent Type',NULL,NULL,40,0,'','consent.consentTypes',NULL,5,'','2009-01-24 21:28:51'),(3248,'*.consent.consentDate','en','lava','crms',NULL,'consent','consentDate','','i','date','Yes','Consent Date',NULL,NULL,NULL,0,'','',NULL,6,'','2009-01-24 21:28:51'),(3249,'*.consent.expirationDate','en','lava','crms',NULL,'consent','expirationDate','','i','date','No','Expiration Date',NULL,NULL,NULL,0,'','',NULL,7,'','2009-01-24 21:28:51'),(3250,'*.consent.withdrawlDate','en','lava','crms',NULL,'consent','withdrawlDate','','i','date','No','Withdrawal Date',NULL,NULL,NULL,0,'','',NULL,8,'','2009-01-24 21:28:51'),(3251,'*.consent.note','en','lava','crms',NULL,'consent','note','','i','text','No','Notes',NULL,NULL,NULL,0,'','',NULL,9,'','2009-01-24 21:28:51'),(3252,'*.consent.capacityReviewBy','en','lava','crms',NULL,'consent','capacityReviewBy','','i','suggest','No0','Capacity Review By',NULL,NULL,NULL,0,'','project.staffList',NULL,10,'','2009-01-24 21:28:51'),(3253,'*.consent.consentRevision','en','lava','crms',NULL,'consent','consentRevision','','i','string','No','Revision #',NULL,NULL,NULL,0,'','',NULL,11,'','2009-01-24 21:28:51'),(3254,'*.consent.consentDeclined','en','lava','crms',NULL,'consent','consentDeclined','','i','range','No','Consent Declined',NULL,NULL,NULL,0,'','generic.textYesNo',NULL,12,'','2009-01-24 21:28:51'),(3255,'*.contactInfo.contactDesc','en','lava','crms',NULL,'contactInfo','contactDesc','','c','string','No','Contact Status',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3256,'*.contactInfo.phoneEmailBlock','en','lava','crms',NULL,'contactInfo','phoneEmailBlock','','c','text','No','Phone/Email',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3257,'filter.contactInfo.city','en','lava','crms','filter','contactInfo','city','','i','string','no','City',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3258,'filter.contactInfo.contactNameRev','en','lava','crms','filter','contactInfo','contactNameRev','','i','string','no','Contact Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3259,'filter.contactInfo.patient.firstName','en','lava','crms','filter','contactInfo','patient.firstName','','i','string','no','Patient First Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3260,'filter.contactInfo.patient.lastName','en','lava','crms','filter','contactInfo','patient.lastName','','i','string','no','Patient Last Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3261,'filter.contactInfo.state','en','lava','crms','filter','contactInfo','state','','i','string','no','State',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3262,'*.contactInfo.addressBlock','en','lava','crms',NULL,'contactInfo','addressBlock','','c','text','No','Address',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3263,'*.contactInfo.id','en','lava','crms',NULL,'contactInfo','id','','c','string','Yes','Contact Info ID',NULL,NULL,NULL,0,'','',NULL,1,'','2009-01-24 21:28:51'),(3264,'*.contactInfo.caregiverId','en','lava','crms',NULL,'contactInfo','caregiverId','Type of Contact Info','i','range','No','Caregiver',NULL,NULL,NULL,0,'','patient.caregivers',NULL,3,'','2009-01-24 21:28:51'),(3265,'*.contactInfo.contactNameRev','en','lava','crms',NULL,'contactInfo','contactNameRev','','i','string','No','Contact Name',NULL,NULL,NULL,0,'','',NULL,3,'','2009-01-24 21:28:51'),(3266,'*.contactInfo.contactPatient','en','lava','crms',NULL,'contactInfo','contactPatient','Type of Contact Info','i','range','no','Contact Patient Directly',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,4,'','2009-01-24 21:28:51'),(3267,'*.contactInfo.isPatientResidence','en','lava','crms',NULL,'contactInfo','isPatientResidence','Type of Contact Info','i','range','no','Is this the patient\'s residence',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,5,'','2009-01-24 21:28:51'),(3268,'*.contactInfo.optOutMac','en','lava','crms',NULL,'contactInfo','optOutMac','OptOutOfMailings','i','range','no','Don\'t send center mailings',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,6,'','2009-01-24 21:28:51'),(3269,'*.contactInfo.optOutMac','en','lava','crms',NULL,'contactInfo','optOutMac','OptOutOfMailings','i','range','no','Don\'t send center mailings',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,6,'','2009-01-24 21:28:51'),(3270,'*.contactInfo.optOutAffiliates','en','lava','crms',NULL,'contactInfo','optOutAffiliates','OptOutOfMailings','i','range','no','Don\'t send affiliated org. mailings',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,7,'','2009-01-24 21:28:51'),(3271,'*.contactInfo.active','en','lava','crms',NULL,'contactInfo','active','Type of Contact Info','i','range','no','Active',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,8,'','2009-01-24 21:28:51'),(3272,'*.contactInfo.isCaregiver','en','lava','crms',NULL,'contactInfo','isCaregiver','','i','toggle','no','Contact info is for a caregiver/contact',NULL,NULL,NULL,0,'','',NULL,8,'','2009-01-24 21:28:51'),(3273,'*.contactInfo.address','en','lava','crms',NULL,'contactInfo','address','Address','i','string','No','Address',NULL,NULL,NULL,0,'','',NULL,9,'','2009-01-24 21:28:51'),(3274,'*.contactInfo.address2','en','lava','crms',NULL,'contactInfo','address2','Address','i','string','No','Address Line 2',NULL,NULL,NULL,0,'','',NULL,10,'','2009-01-24 21:28:51'),(3275,'*.contactInfo.city','en','lava','crms',NULL,'contactInfo','city','Address','i','suggest','No','City',NULL,NULL,NULL,0,'','contactInfo.city',NULL,11,'','2009-01-24 21:28:51'),(3276,'*.contactInfo.state','en','lava','crms',NULL,'contactInfo','state','Address','i','range','No','State',NULL,NULL,NULL,0,'','generic.state',NULL,12,'','2009-01-24 21:28:51'),(3277,'*.contactInfo.zip','en','lava','crms',NULL,'contactInfo','zip','Address','i','string','No','Zip',NULL,NULL,NULL,0,'','',NULL,13,'','2009-01-24 21:28:51'),(3278,'*.contactInfo.country','en','lava','crms',NULL,'contactInfo','country','Address','i','string','No','Country',NULL,NULL,NULL,0,'','',NULL,14,'','2009-01-24 21:28:51'),(3279,'*.contactInfo.phone1','en','lava','crms',NULL,'contactInfo','phone1','Phone/Email','i','string','No','First Phone',NULL,NULL,NULL,0,'','',NULL,15,'','2009-01-24 21:28:51'),(3280,'*.contactInfo.phoneType1','en','lava','crms',NULL,'contactInfo','phoneType1','Phone/Email','i','suggest','No','First Phone Type',NULL,NULL,NULL,0,'','generic.phoneType',NULL,16,'','2009-01-24 21:28:51'),(3281,'*.contactInfo.phone2','en','lava','crms',NULL,'contactInfo','phone2','Phone/Email','i','string','No','Second Phone',NULL,NULL,NULL,0,'','',NULL,17,'','2009-01-24 21:28:51'),(3282,'*.contactInfo.phoneType2','en','lava','crms',NULL,'contactInfo','phoneType2','Phone/Email','i','suggest','No','Second Phone Type',NULL,NULL,NULL,0,'','generic.phoneType',NULL,18,'','2009-01-24 21:28:51'),(3283,'*.contactInfo.phone3','en','lava','crms',NULL,'contactInfo','phone3','Phone/Email','i','string','No','Third Phone',NULL,NULL,NULL,0,'','',NULL,19,'','2009-01-24 21:28:51'),(3284,'*.contactInfo.phoneType3','en','lava','crms',NULL,'contactInfo','phoneType3','Phone/Email','i','suggest','No','Third Phone Type',NULL,NULL,NULL,0,'','generic.phoneType',NULL,20,'','2009-01-24 21:28:51'),(3285,'*.contactInfo.email','en','lava','crms',NULL,'contactInfo','email','Phone/Email','i','string','No','Email',NULL,NULL,NULL,0,'','',NULL,21,'','2009-01-24 21:28:51'),(3286,'*.contactInfo.notes','en','lava','crms',NULL,'contactInfo','notes','','i','text','No','',NULL,255,NULL,0,'rows=\"4\" cols=\"45\"','',NULL,22,'','2009-01-24 21:28:51'),(3287,'filter.contactLog.contact','en','lava','crms','filter','contactLog','contact','','i','string','no','Contact Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3288,'filter.contactLog.logDateEnd','en','lava','crms','filter','contactLog','logDateEnd','','i','date','no','     and',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3289,'filter.contactLog.logDateStart','en','lava','crms','filter','contactLog','logDateStart','','i','date','no','Log Date between',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3290,'filter.contactLog.projName','en','lava','crms','filter','contactLog','projName','','i','string','No','Project',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3291,'filter.contactLog.staff','en','lava','crms','filter','contactLog','staff','','i','suggest','no','Staff Member',NULL,NULL,NULL,0,'','project.staffList',NULL,NULL,'','2009-01-24 21:28:51'),(3292,'*.contactLog.id','en','lava','crms',NULL,'contactLog','id','','c','numeric','Yes','Log ID',NULL,NULL,NULL,0,'','',NULL,1,'','2009-01-24 21:28:51'),(3293,'*.contactLog.projName','en','lava','crms',NULL,'contactLog','projName','','i','range','No','Project',NULL,NULL,NULL,0,'','context.projectList',NULL,3,'','2009-01-24 21:28:51'),(3294,'*.contactLog.logTime','en','lava','crms','','contactLog','logTime',NULL,'i','time','No','Time',NULL,NULL,NULL,0,NULL,NULL,NULL,4,'Time of the contact','2009-04-23 13:00:00'),(3295,'*.contactLog.logDate','en','lava','crms',NULL,'contactLog','logDate','','i','date','No','Log Date',NULL,NULL,10,0,'','',NULL,4,'','2009-01-24 21:28:51'),(3296,'*.contactLog.method','en','lava','crms',NULL,'contactLog','method','','i','suggest','Yes','Contact method',NULL,NULL,NULL,0,'','contactLog.contactMethod',NULL,5,'','2009-01-24 21:28:51'),(3297,'*.contactLog.staffInit','en','lava','crms',NULL,'contactLog','staffInit','','i','scale','Yes','Staff Initiated',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,6,'','2009-01-24 21:28:51'),(3298,'*.contactLog.staff','en','lava','crms',NULL,'contactLog','staff','','i','suggest','No','Staff Name',NULL,NULL,NULL,0,'','project.staffList',NULL,7,'','2009-01-24 21:28:51'),(3299,'*.contactLog.contact','en','lava','crms',NULL,'contactLog','contact','','i','string','No','Contact',NULL,NULL,NULL,0,'','',NULL,8,'','2009-01-24 21:28:51'),(3300,'*.contactLog.note','en','lava','crms',NULL,'contactLog','note','','i','unlimitedtext','No','Note',NULL,NULL,NULL,0,'rows=\"20\" cols=\"100\"','',NULL,9,'','2009-01-24 21:28:51'),(3301,'filter.contactLog.patient.firstName','en','lava','crms','filter','contactLog.patient','firstName','','i','string','No','Pat. First Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3302,'filter.contactLog.patient.lastName','en','lava','crms','filter','contactLog.patient','lastName','','i','string','No','Pat. Last Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3303,'*.crmsAuthRole.patientAccess','en','lava','crms',NULL,'crmsAuthRole','patientAccess','details','i','scale','yes','Patient Access',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,4,'Does the role grant access to patients','2009-01-01 00:00:00'),(3304,'*.crmsAuthRole.phiAccess','en','lava','crms',NULL,'crmsAuthRole','phiAccess','details','i','scale','yes','PHI Access',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,5,'Does the role grant access to protected health information fields (PHI)','2009-01-01 00:00:00'),(3305,'*.crmsAuthRole.ghiAccess','en','lava','crms',NULL,'crmsAuthRole','ghiAccess','details','i','scale','yes','Genetics Access ',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,6,'Does the role grant access to genetic health information','2009-01-01 00:00:00'),(3306,'*.crmsAuthUserRole.unit','en','lava','crms',NULL,'crmsAuthUserRole','unit','details','i','suggest','No','Unit/Site',NULL,25,NULL,0,NULL,'projectUnit.units',NULL,5,'The program that the role applies to (* for any)','2009-01-24 21:28:51'),(3307,'*.crmsAuthUserRole.project','en','lava','crms',NULL,'crmsAuthUserRole','project','details','i','suggest','Yes','Project',NULL,50,NULL,0,NULL,'projectUnit.projects',NULL,10,NULL,'2009-01-01 00:00:00'),(3308,'*.crmsFile.associationBlock','en','lava','crms',NULL,'crmsFile','associationBlock','Summary','c','text','No','Associations',NULL,NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2011-11-04 20:36:52'),(3309,'*.crmsFile.pidn','en','lava','crms',NULL,'crmsFile','pidn','LinkInfo','i','range','No','Patient',NULL,NULL,40,0,NULL,'crmsFile.patients',NULL,2,NULL,'2011-10-20 14:49:29'),(3310,'*.crmsFile.enrollStatId','en','lava','crms',NULL,'crmsFile','enrollStatId','LinkInfo','i','range','No','Project',NULL,NULL,40,0,NULL,'crmsFile.projects',NULL,3,NULL,'2011-10-20 14:49:29'),(3311,'*.crmsFile.visitId','en','lava','crms',NULL,'crmsFile','visitId','LinkInfo','i','range','No','Visit',NULL,NULL,40,0,NULL,'crmsFile.visits',NULL,4,NULL,'2011-10-20 14:49:29'),(3312,'*.crmsFile.instrId','en','lava','crms',NULL,'crmsFile','instrId','LinkInfo','i','range','No','Instrument',NULL,NULL,40,0,NULL,'crmsFile.instruments',NULL,5,NULL,'2011-10-20 14:49:29'),(3313,'*.crmsFile.patient_fullNameRevNoSuffix','en','lava','crms',NULL,'crmsFile','patient_fullNameRevNoSuffix','LinkInfo','c','string','No','Patient',NULL,NULL,NULL,0,NULL,NULL,NULL,6,NULL,'2011-11-03 14:03:03'),(3314,'*.crmsFile.contentType','en','lava','crms',NULL,'crmsFile','contentType','fileInfo','i','suggest','No','File Contents',NULL,100,40,0,NULL,'crmsFile.contentType',NULL,6,NULL,'2011-11-07 11:41:14'),(3315,'*.doctor.addressBlock','en','lava','crms',NULL,'doctor','addressBlock','','c','text','No','Address',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3316,'*.doctor.phoneEmailBlock','en','lava','crms',NULL,'doctor','phoneEmailBlock','','c','text','No','Phone/Email',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3317,'*.doctor.id','en','lava','crms',NULL,'doctor','id','','c','string','Yes','id',NULL,NULL,80,0,'','',NULL,1,'','2009-01-24 21:28:51'),(3318,'*.doctor.lastName','en','lava','crms',NULL,'doctor','lastName','','i','string','Yes','Last Name',NULL,NULL,NULL,0,'','',NULL,2,'','2009-01-24 21:28:51'),(3319,'*.doctor.middleInitial','en','lava','crms',NULL,'doctor','middleInitial','','i','string','No','Middle Initial',NULL,NULL,NULL,0,'','',NULL,3,'','2009-01-24 21:28:51'),(3320,'*.doctor.firstName','en','lava','crms',NULL,'doctor','firstName','','i','string','Yes','First Name',NULL,NULL,NULL,0,'','',NULL,4,'','2009-01-24 21:28:51'),(3321,'*.doctor.address','en','lava','crms',NULL,'doctor','address','','','text','No','Address',NULL,NULL,NULL,0,'','',NULL,5,'','2009-01-24 21:28:51'),(3322,'*.doctor.city','en','lava','crms',NULL,'doctor','city','','i','suggest','No','City',NULL,NULL,NULL,0,'','doctor.city',NULL,6,'','2009-01-24 21:28:51'),(3323,'*.doctor.state','en','lava','crms',NULL,'doctor','state','','i','range','No','State',NULL,NULL,NULL,0,'','generic.state',NULL,7,'','2009-01-24 21:28:51'),(3324,'*.doctor.zip','en','lava','crms',NULL,'doctor','zip','','i','string','No','Zip',NULL,NULL,NULL,0,'','',NULL,8,'','2009-01-24 21:28:51'),(3325,'*.doctor.phone1','en','lava','crms',NULL,'doctor','phone1','','i','string','No','First Phone',NULL,NULL,NULL,0,'','',NULL,9,'','2009-01-24 21:28:51'),(3326,'*.doctor.phoneType1','en','lava','crms',NULL,'doctor','phoneType1','','i','suggest','No','First Phone Type',NULL,NULL,NULL,0,'','generic.phoneType',NULL,10,'','2009-01-24 21:28:51'),(3327,'*.doctor.phone2','en','lava','crms',NULL,'doctor','phone2','','i','string','No','Second Phone',NULL,NULL,NULL,0,'','',NULL,11,'','2009-01-24 21:28:51'),(3328,'*.doctor.phoneType2','en','lava','crms',NULL,'doctor','phoneType2','','i','suggest','No','Second Phone Type',NULL,NULL,NULL,0,'','generic.phoneType',NULL,12,'','2009-01-24 21:28:51'),(3329,'*.doctor.phone3','en','lava','crms',NULL,'doctor','phone3','','i','string','No','Third Phone',NULL,NULL,NULL,0,'','',NULL,13,'','2009-01-24 21:28:51'),(3330,'*.doctor.phoneType3','en','lava','crms',NULL,'doctor','phoneType3','','i','suggest','No','Third Phone Type',NULL,NULL,NULL,0,'','generic.phoneType',NULL,14,'','2009-01-24 21:28:51'),(3331,'*.doctor.email','en','lava','crms',NULL,'doctor','email','','i','string','No','Email',NULL,NULL,NULL,0,'','',NULL,15,'','2009-01-24 21:28:51'),(3332,'*.doctor.docType','en','lava','crms',NULL,'doctor','docType','','i','string','No','Doctor Type',NULL,NULL,NULL,0,'','',NULL,16,'','2009-01-24 21:28:51'),(3333,'*.doctor.fullNameRev','en','lava','crms',NULL,'doctor','fullNameRev','','c','string','No','Doctor',NULL,NULL,NULL,0,'','',NULL,17,'','2009-01-24 21:28:51'),(3334,'*.doctor.fullName','en','lava','crms',NULL,'doctor','fullName','','c','string','No','Doctor',NULL,NULL,NULL,0,'','',NULL,18,'','2009-01-24 21:28:51'),(3335,'filter.enrollmentStatus.enrolledDateEnd','en','lava','crms','filter','enrollmentStatus','enrolledDateEnd','','i','date','no','and',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3336,'filter.enrollmentStatus.enrolledDateStart','en','lava','crms','filter','enrollmentStatus','enrolledDateStart','','i','date','no','Enrolled between',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3337,'filter.enrollmentStatus.latestDesc','en','lava','crms','filter','enrollmentStatus','latestDesc','','i','range','no','Latest Status',NULL,NULL,NULL,0,'','enrollmentStatus.projectStatus',NULL,NULL,'','2009-01-24 21:28:51'),(3338,'filter.enrollmentStatus.patient.firstName','en','lava','crms','filter','enrollmentStatus','patient.firstName','','i','string','no','First Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3339,'filter.enrollmentStatus.patient.lastName','en','lava','crms','filter','enrollmentStatus','patient.lastName','','i','string','no','Last Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3340,'filter.enrollmentStatus.projName','en','lava','crms','filter','enrollmentStatus','projName','','i','string','No','Project',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3341,'*.enrollmentStatus.id','en','lava','crms',NULL,'enrollmentStatus','id','','c','numeric','Yes','Status ID',NULL,NULL,NULL,0,'','',NULL,1,'','2009-01-24 21:28:51'),(3342,'*.enrollmentStatus.projName','en','lava','crms',NULL,'enrollmentStatus','projName','','c','range','Yes','Project',NULL,75,NULL,0,'','enrollmentStatus.Project',NULL,3,'','2009-01-24 21:28:51'),(3343,'*.enrollmentStatus.subjectStudyId','en','lava','crms',NULL,'enrollmentStatus','subjectStudyId','','i','string','No','Subject Study ID',NULL,10,NULL,0,'','',NULL,4,'','2009-01-24 21:28:51'),(3344,'*.enrollmentStatus.latestDesc','en','lava','crms',NULL,'enrollmentStatus','latestDesc','','c','string','No','Latest Status',NULL,25,NULL,0,'','',NULL,5,'','2009-01-24 21:28:51'),(3345,'*.enrollmentStatus.referralSource','en','lava','crms',NULL,'enrollmentStatus','referralSource','','i','suggest','No','Referral Source',NULL,75,NULL,0,'','enrollmentStatus.referralSource',NULL,5,'','2009-01-24 21:28:51'),(3346,'*.enrollmentStatus.latestDate','en','lava','crms',NULL,'enrollmentStatus','latestDate','','c','date','No','Latest Status Date',NULL,NULL,NULL,0,'','',NULL,6,'','2009-01-24 21:28:51'),(3347,'*.enrollmentStatus.latestNote','en','lava','crms',NULL,'enrollmentStatus','latestNote','','c','text','No','Latest Status Note',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,7,'','2009-01-24 21:28:51'),(3348,'*.enrollmentStatus.referredDesc','en','lava','crms',NULL,'enrollmentStatus','referredDesc','','c','string','No','',NULL,25,NULL,0,'','',NULL,9,'','2009-01-24 21:28:51'),(3349,'*.enrollmentStatus.referredDate','en','lava','crms',NULL,'enrollmentStatus','referredDate','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,10,'','2009-01-24 21:28:51'),(3350,'*.enrollmentStatus.referredNote','en','lava','crms',NULL,'enrollmentStatus','referredNote','','i','text','No','',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,11,'','2009-01-24 21:28:51'),(3351,'*.enrollmentStatus.deferredDesc','en','lava','crms',NULL,'enrollmentStatus','deferredDesc','','c','string','No','',NULL,25,NULL,0,'','',NULL,13,'','2009-01-24 21:28:51'),(3352,'*.enrollmentStatus.deferredDate','en','lava','crms',NULL,'enrollmentStatus','deferredDate','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,14,'','2009-01-24 21:28:51'),(3353,'*.enrollmentStatus.deferredNote','en','lava','crms',NULL,'enrollmentStatus','deferredNote','','i','text','No','',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,15,'','2009-01-24 21:28:51'),(3354,'*.enrollmentStatus.eligibleDesc','en','lava','crms',NULL,'enrollmentStatus','eligibleDesc','','c','string','No','',NULL,25,NULL,0,'','',NULL,17,'','2009-01-24 21:28:51'),(3355,'*.enrollmentStatus.eligibleDate','en','lava','crms',NULL,'enrollmentStatus','eligibleDate','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,18,'','2009-01-24 21:28:51'),(3356,'*.enrollmentStatus.eligibleNote','en','lava','crms',NULL,'enrollmentStatus','eligibleNote','','i','text','No','',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,19,'','2009-01-24 21:28:51'),(3357,'*.enrollmentStatus.ineligibleDesc','en','lava','crms',NULL,'enrollmentStatus','ineligibleDesc','','c','string','No','',NULL,25,NULL,0,'','',NULL,21,'','2009-01-24 21:28:51'),(3358,'*.enrollmentStatus.ineligibleDate','en','lava','crms',NULL,'enrollmentStatus','ineligibleDate','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,22,'','2009-01-24 21:28:51'),(3359,'*.enrollmentStatus.ineligibleNote','en','lava','crms',NULL,'enrollmentStatus','ineligibleNote','','i','text','No','',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,23,'','2009-01-24 21:28:51'),(3360,'*.enrollmentStatus.declinedDesc','en','lava','crms',NULL,'enrollmentStatus','declinedDesc','','c','string','No','',NULL,25,NULL,0,'','',NULL,25,'','2009-01-24 21:28:51'),(3361,'*.enrollmentStatus.declinedDate','en','lava','crms',NULL,'enrollmentStatus','declinedDate','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,26,'','2009-01-24 21:28:51'),(3362,'*.enrollmentStatus.declinedNote','en','lava','crms',NULL,'enrollmentStatus','declinedNote','','i','text','No','',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,27,'','2009-01-24 21:28:51'),(3363,'*.enrollmentStatus.enrolledDesc','en','lava','crms',NULL,'enrollmentStatus','enrolledDesc','','c','string','No','',NULL,25,NULL,0,'','',NULL,29,'','2009-01-24 21:28:51'),(3364,'*.enrollmentStatus.enrolledDate','en','lava','crms',NULL,'enrollmentStatus','enrolledDate','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,30,'','2009-01-24 21:28:51'),(3365,'*.enrollmentStatus.enrolledNote','en','lava','crms',NULL,'enrollmentStatus','enrolledNote','','i','text','No','',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,31,'','2009-01-24 21:28:51'),(3366,'*.enrollmentStatus.excludedDesc','en','lava','crms',NULL,'enrollmentStatus','excludedDesc','','c','string','No','',NULL,25,NULL,0,'','',NULL,33,'','2009-01-24 21:28:51'),(3367,'*.enrollmentStatus.excludedDate','en','lava','crms',NULL,'enrollmentStatus','excludedDate','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,34,'','2009-01-24 21:28:51'),(3368,'*.enrollmentStatus.excludedNote','en','lava','crms',NULL,'enrollmentStatus','excludedNote','','i','text','No','',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,35,'','2009-01-24 21:28:51'),(3369,'*.enrollmentStatus.withdrewDesc','en','lava','crms',NULL,'enrollmentStatus','withdrewDesc','','c','string','No','',NULL,25,NULL,0,'','',NULL,37,'','2009-01-24 21:28:51'),(3370,'*.enrollmentStatus.withdrewDate','en','lava','crms',NULL,'enrollmentStatus','withdrewDate','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,38,'','2009-01-24 21:28:51'),(3371,'*.enrollmentStatus.withdrewNote','en','lava','crms',NULL,'enrollmentStatus','withdrewNote','','i','text','No','',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,39,'','2009-01-24 21:28:51'),(3372,'*.enrollmentStatus.inactiveDesc','en','lava','crms',NULL,'enrollmentStatus','inactiveDesc','','c','string','No','',NULL,25,NULL,0,'','',NULL,41,'','2009-01-24 21:28:51'),(3373,'*.enrollmentStatus.inactiveDate','en','lava','crms',NULL,'enrollmentStatus','inactiveDate','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,42,'','2009-01-24 21:28:51'),(3374,'*.enrollmentStatus.inactiveNote','en','lava','crms',NULL,'enrollmentStatus','inactiveNote','','i','text','No','',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,43,'','2009-01-24 21:28:51'),(3375,'*.enrollmentStatus.deceasedDesc','en','lava','crms',NULL,'enrollmentStatus','deceasedDesc','','c','string','No','',NULL,25,NULL,0,'','',NULL,48,'','2009-01-24 21:28:51'),(3376,'*.enrollmentStatus.deceasedDate','en','lava','crms',NULL,'enrollmentStatus','deceasedDate','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,49,'','2009-01-24 21:28:51'),(3377,'*.enrollmentStatus.deceasedNote','en','lava','crms',NULL,'enrollmentStatus','deceasedNote','','i','text','No','',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,50,'','2009-01-24 21:28:51'),(3378,'*.enrollmentStatus.autopsyDesc','en','lava','crms',NULL,'enrollmentStatus','autopsyDesc','','c','string','No','',NULL,25,NULL,0,'','',NULL,52,'','2009-01-24 21:28:51'),(3379,'*.enrollmentStatus.autopsyDate','en','lava','crms',NULL,'enrollmentStatus','autopsyDate','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,53,'','2009-01-24 21:28:51'),(3380,'*.enrollmentStatus.autopsyNote','en','lava','crms',NULL,'enrollmentStatus','autopsyNote','','i','text','No','',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,54,'','2009-01-24 21:28:51'),(3381,'*.enrollmentStatus.closedDesc','en','lava','crms',NULL,'enrollmentStatus','closedDesc','','c','string','No','',NULL,25,NULL,0,'','',NULL,56,'','2009-01-24 21:28:51'),(3382,'*.enrollmentStatus.closedDate','en','lava','crms',NULL,'enrollmentStatus','closedDate','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,57,'','2009-01-24 21:28:51'),(3383,'*.enrollmentStatus.closedNote','en','lava','crms',NULL,'enrollmentStatus','closedNote','','i','text','No','',NULL,100,NULL,0,'rows=\"1\" cols=\"50\"','',NULL,58,'','2009-01-24 21:28:51'),(3384,'*.enrollmentStatus.enrollmentNotes','en','lava','crms',NULL,'enrollmentStatus','enrollmentNotes','','i','text','No','',NULL,500,NULL,0,'rows=\"14\" cols=\"40\"','',NULL,59,'','2009-01-24 21:28:51'),(3385,'*.findPatient.birthDateEnd','en','lava','crms',NULL,'findPatient','birthDateEnd','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,1,'','2011-10-05 14:40:00'),(3386,'*.findPatient.birthDateStart','en','lava','crms',NULL,'findPatient','birthDateStart','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,2,'','2011-10-05 14:40:00'),(3387,'*.findPatient.caregiverFirstName','en','lava','crms',NULL,'findPatient','caregiverFirstName','','i','string','No','',NULL,NULL,NULL,0,'','',NULL,3,'','2011-10-05 14:40:00'),(3388,'*.findPatient.caregiverFullNameRev','en','lava','crms',NULL,'findPatient','caregiverFullNameRev','','i','string','No','',NULL,NULL,NULL,0,'','',NULL,4,'','2011-10-05 14:40:00'),(3389,'*.findPatient.caregiverLastName','en','lava','crms',NULL,'findPatient','caregiverLastName','','i','string','No','',NULL,NULL,NULL,0,'','',NULL,6,'','2011-10-05 14:40:00'),(3390,'*.findPatient.contactInfoEmail','en','lava','crms',NULL,'findPatient','contactInfoEmail','','i','string','No','',NULL,NULL,NULL,0,'','',NULL,7,'','2011-10-05 14:40:00'),(3391,'*.findPatient.contactInfoPhone','en','lava','crms',NULL,'findPatient','contactInfoPhone','','i','string','No','',NULL,NULL,NULL,0,'','',NULL,8,'','2011-10-05 14:40:00'),(3392,'*.findPatient.deathDateEnd','en','lava','crms',NULL,'findPatient','deathDateEnd','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,9,'','2011-10-05 14:40:00'),(3393,'*.findPatient.deathDateStart','en','lava','crms',NULL,'findPatient','deathDateStart','','i','date','No','',NULL,NULL,NULL,0,'','',NULL,10,'','2011-10-05 14:40:00'),(3394,'*.findPatient.firstName','en','lava','crms',NULL,'findPatient','firstName','','i','string','No','',NULL,NULL,NULL,0,'','',NULL,11,'','2011-10-05 14:40:00'),(3395,'*.findPatient.lastName','en','lava','crms',NULL,'findPatient','lastName','','i','string','No','',NULL,NULL,NULL,0,'','',NULL,12,'','2011-10-05 14:40:00'),(3396,'*.findPatient.phone','en','lava','crms',NULL,'findPatient','phone','','i','string','No','',NULL,NULL,NULL,0,'','',NULL,13,'','2011-10-05 14:40:00'),(3397,'*.instrument.note','en','lava','crms',NULL,'instrument','note',NULL,'i','text','No','Notes',NULL,255,NULL,0,NULL,NULL,NULL,NULL,'Notes (max. 255 chars)','2011-05-19 17:34:20'),(3398,'*.instrument.collectionStatusBlock','en','lava','crms',NULL,'instrument','collectionStatusBlock','','c','string','no','Collection Status',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3399,'*.instrument.entryStatusBlock','en','lava','crms',NULL,'instrument','entryStatusBlock','','c','string','no','Data Entry Status',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3400,'*.instrument.patient_fullName','en','lava','crms',NULL,'instrument','patient_fullName','','c','string','No','Patient',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3401,'*.instrument.sectionNote','en','lava','crms',NULL,'instrument','sectionNote','','i','text','No','Notes',NULL,2000,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3402,'*.instrument.summary','en','lava','crms',NULL,'instrument','summary','','c','string','no','Summary',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3403,'*.instrument.verifyStatusBlock','en','lava','crms',NULL,'instrument','verifyStatusBlock','','c','string','no','Verification Status',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3404,'*.instrument.visit_id','en','lava','crms',NULL,'instrument','visit_id','','c','range','Yes','Visit Description',NULL,NULL,40,0,'','visit.patientVisits',NULL,NULL,'','2009-01-24 21:28:51'),(3405,'*.instrument.visit_visitDescrip','en','lava','crms',NULL,'instrument','visit_visitDescrip','','c','string','No','Visit Description',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3406,'filter.instrument.customDateEnd','en','lava','crms','filter','instrument','customDateEnd','','i','date','No','      and',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3407,'filter.instrument.customDateStart','en','lava','crms','filter','instrument','customDateStart','','i','date','No','Collection Between',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3408,'filter.instrument.dcDateEnd','en','lava','crms','filter','instrument','dcDateEnd','','i','date','no','     and',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3409,'filter.instrument.dcDateStart','en','lava','crms','filter','instrument','dcDateStart','','i','date','no','Collected between',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3410,'filter.instrument.deDateEnd','en','lava','crms','filter','instrument','deDateEnd','','i','date','no','     and',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3411,'filter.instrument.deDateStart','en','lava','crms','filter','instrument','deDateStart','','i','date','no','Entered between',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3412,'filter.instrument.instrType','en','lava','crms','filter','instrument','instrType','','i','string','No','Instrument Type',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3413,'*.instrument.selected','en','lava','crms',NULL,'instrument','selected',NULL,'i','toggle',NULL,NULL,NULL,NULL,NULL,0,'onclick=\"selectItemClicked(this)\"',NULL,NULL,NULL,NULL,'2009-03-05 14:15:43'),(3414,'*.instrument.id','en','lava','crms',NULL,'instrument','id','','c','numeric','Yes','Instrument ID',NULL,NULL,NULL,0,'','',NULL,1,'','2009-01-24 21:28:51'),(3415,'*.instrument.projName','en','lava','crms',NULL,'instrument','projName','','c','string','Yes','Project',NULL,NULL,NULL,0,'','',NULL,3,'','2009-01-24 21:28:51'),(3416,'*.instrument.instrType','en','lava','crms',NULL,'instrument','instrType','','c','range','Yes','Instrument Type',NULL,NULL,NULL,0,'','instrumentMetadata.instrTypes',NULL,5,'','2009-01-24 21:28:51'),(3417,'*.instrument.instrVer','en','lava','crms',NULL,'instrument','instrVer','','i','range','Yes','Instrument Version',NULL,NULL,NULL,0,'','instrument.versions',NULL,6,'','2009-01-24 21:28:51'),(3418,'*.instrument.dcDate','en','lava','crms',NULL,'instrument','dcDate','Data Collection','i','date','Yes','Collection Date',NULL,NULL,NULL,0,'','',NULL,7,'','2009-01-24 21:28:51'),(3419,'*.instrument.dcBy','en','lava','crms',NULL,'instrument','dcBy','Data Collection','i','suggest','Yes','Collection By',NULL,NULL,NULL,0,'','project.staffList',NULL,8,'','2009-01-24 21:28:51'),(3420,'filter.instrument.dcBy','en','lava','crms','filter','instrument','dcBy','Data Collection','i','suggest','No','Collection By',NULL,NULL,NULL,0,'','project.staffList',NULL,8,'','2009-01-24 21:28:51'),(3421,'filter.instrument.deBy','en','lava','crms','filter','instrument','deBy','Data Collection','i','suggest','No','Entry By',NULL,NULL,NULL,0,'','project.staffList',NULL,8,'','2009-01-24 21:28:51'),(3422,'*.instrument.dcStatus','en','lava','crms',NULL,'instrument','dcStatus','Data Collection','i','range','Yes','Collection Status',NULL,NULL,NULL,0,'','instrument.dcStatus',NULL,9,'','2009-01-24 21:28:51'),(3423,'filter.instrument.dcStatus','en','lava','crms','filter','instrument','dcStatus','Data Collection','i','suggest','No','Collection Status',NULL,NULL,NULL,0,'','instrument.dcStatus',NULL,9,'','2009-01-24 21:28:51'),(3424,'filter.instrument.deStatus','en','lava','crms','filter','instrument','deStatus','Data Collection','i','suggest','No','Entry Status',NULL,NULL,NULL,0,'','instrument.deStatus',NULL,9,'','2009-01-24 21:28:51'),(3425,'*.instrument.dcNotes','en','lava','crms',NULL,'instrument','dcNotes','Data Collection','i','text','No','Collection Notes',NULL,NULL,NULL,0,'rows=\"3\" cols=\"35\"','',NULL,10,'','2009-01-24 21:28:51'),(3426,'*.instrument.researchStatus','en','lava','crms',NULL,'instrument','researchStatus','Research Status/Quality','i','range','No','Research Status',NULL,NULL,NULL,0,'','instrument.researchStatus',NULL,11,'','2009-01-24 21:28:51'),(3427,'*.instrument.qualityIssue','en','lava','crms',NULL,'instrument','qualityIssue','','i','range','No','Quality Issues',NULL,NULL,NULL,0,'','instrument.qualityIssue',NULL,12,'','2009-01-24 21:28:51'),(3428,'*.instrument.qualityIssue2','en','lava','crms',NULL,'instrument','qualityIssue2','','i','range','No','',NULL,NULL,NULL,0,'','instrument.qualityIssue2',NULL,13,'','2009-01-24 21:28:51'),(3429,'*.instrument.qualityIssue3','en','lava','crms',NULL,'instrument','qualityIssue3','Research Status/Quality','i','range','No','',NULL,NULL,NULL,0,'','instrument.qualityIssue3',NULL,14,'','2009-01-24 21:28:51'),(3430,'*.instrument.qualityNotes','en','lava','crms',NULL,'instrument','qualityNotes','Research Status/Quality','i','text','','Quality Notes',NULL,NULL,NULL,0,'rows=\"3\" cols=\"35\"','',NULL,15,'','2009-01-24 21:28:51'),(3431,'*.instrument.deDate','en','lava','crms',NULL,'instrument','deDate','Data Entry','i','date','Yes','Entry Date',NULL,NULL,NULL,0,'','',NULL,16,'','2009-01-24 21:28:51'),(3432,'*.instrument.deBy','en','lava','crms',NULL,'instrument','deBy','Data Entry','i','range','Yes','Entry By',NULL,NULL,NULL,0,'','project.staffList',NULL,17,'','2009-01-24 21:28:51'),(3433,'*.instrument.deStatus','en','lava','crms',NULL,'instrument','deStatus','Data Entry','i','range','Yes','Entry Status',NULL,NULL,NULL,0,'','instrument.deStatus',NULL,18,'','2009-01-24 21:28:51'),(3434,'*.instrument.deNotes','en','lava','crms',NULL,'instrument','deNotes','Data Entry','i','text','No','Entry Notes',NULL,NULL,NULL,0,'rows=\"3\" cols=\"35\"','',NULL,19,'','2009-01-24 21:28:51'),(3435,'*.instrument.dvDate','en','lava','crms',NULL,'instrument','dvDate','Verification','i','date','No','Verify Date',NULL,NULL,NULL,0,'','',NULL,20,'','2009-01-24 21:28:51'),(3436,'*.instrument.dvBy','en','lava','crms',NULL,'instrument','dvBy','Verification','i','range','No','Verify By',NULL,NULL,NULL,0,'','project.staffList',NULL,21,'','2009-01-24 21:28:51'),(3437,'*.instrument.patient_fullNameNoSuffix','en','lava','crms',NULL,'instrument','patient_fullNameNoSuffix','','c','string','No','Case',NULL,NULL,NULL,0,'','',NULL,22,'','2009-01-24 21:28:51'),(3438,'*.instrument.dvStatus','en','lava','crms',NULL,'instrument','dvStatus','Verification','i','range','No','Verify Status',NULL,NULL,NULL,0,'','instrument.dvStatus',NULL,22,'','2009-01-24 21:28:51'),(3439,'*.instrument.patient_fullNameNoSuffix','en','lava','crms',NULL,'instrument','patient_fullNameNoSuffix','','c','string','No','Patient',NULL,NULL,NULL,0,'','',NULL,22,'','2009-01-24 21:28:51'),(3440,'*.instrument.dvNotes','en','lava','crms',NULL,'instrument','dvNotes','Verification','i','text','No','Verify Notes',NULL,NULL,NULL,0,'rows=\"3\" cols=\"35\"','',NULL,23,'','2009-01-24 21:28:51'),(3441,'*.instrument.ageAtDC','en','lava','crms',NULL,'instrument','ageAtDC','','c','numeric','No','Age At Collection',NULL,NULL,NULL,0,'','',NULL,27,'','2009-01-24 21:28:51'),(3442,'filter.instrument.patient.firstName','en','lava','crms','filter','instrument.patient','firstName','','i','string','No','First Name',NULL,NULL,NULL,0,'','',NULL,8,'','2009-01-24 21:28:51'),(3443,'filter.instrument.patient.lastName','en','lava','crms','filter','instrument.patient','lastName','','i','string','no','Last name',NULL,NULL,NULL,0,'','',NULL,9,'','2009-01-24 21:28:51'),(3444,'filter.instrument.visit.projName','en','lava','crms','filter','instrument.visit','projName','','i','string','No','Project',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3445,'filter.instrument.visit.visitType','en','lava','crms','filter','instrument.visit','visitType','','i','string','No','Visit Type',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3446,'*.medications.drugId','en','lava','crms',NULL,'medications','drugId',NULL,'r','string','No','Drug ID',NULL,NULL,NULL,0,'readonly',NULL,NULL,3,'The Multum generic drug id for the medication','2009-03-05 14:15:50'),(3447,'*.medications.generic','en','lava','crms',NULL,'medications','generic',NULL,'r','string','No','Generic',NULL,50,NULL,0,'readonly',NULL,NULL,5,'Generic Name of the medication','2009-03-05 14:15:50'),(3448,'*.medications.brand','en','lava','crms',NULL,'medications','brand',NULL,'r','string','No','Brand Name',NULL,50,NULL,0,'readonly',NULL,NULL,6,'Brand Name of the medication (optional)','2009-03-05 14:15:50'),(3449,'*.medications.notListed','en','lava','crms',NULL,'medications','notListed',NULL,'r','string','No','Not Listed',NULL,50,NULL,0,NULL,NULL,NULL,7,'Description of the medication if not listed','2009-03-05 14:15:50'),(3450,'*.medications.drugLookupClone','en','lava','crms',NULL,'medications','drugLookupClone',NULL,'r','range','No','Lookup (00000=clear,99999=Not Listed)',NULL,100,NULL,0,NULL,NULL,NULL,40,'Lookup medication by brand or generic name','2009-03-05 14:15:50'),(3451,'*.medications.drugLookup','en','lava','crms',NULL,'medications','drugLookup',NULL,'r','range','No','Lookup (00000=clear,99999=Not Listed)',NULL,100,NULL,0,NULL,'medications.drugLookup',NULL,40,'Lookup medication by brand or generic name','2009-03-05 14:15:50'),(3452,'filter.patient.id','en','lava','crms','filter','patient','id','','i','numeric','No','PIDN',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3453,'*.patient.id','en','lava','crms',NULL,'patient','id','','c','numeric','Yes','PIDN',NULL,NULL,NULL,0,'','',NULL,1,'','2009-01-24 21:28:51'),(3454,'*.patient.educ','en','lava','crms',NULL,'patient','educ','Personal History/Current Arrangements','i','range','No','Education',NULL,NULL,NULL,0,'','generic.education',NULL,2,'','2009-01-24 21:28:51'),(3455,'*.patient.lastName','en','lava','crms',NULL,'patient','lastName','ID and Core Demographics','i','string','Yes','Last Name',NULL,NULL,NULL,0,'','',NULL,2,'','2009-01-24 21:28:51'),(3456,'*.patient.middleInitial','en','lava','crms',NULL,'patient','middleInitial','ID and Core Demographics','i','string','No','Middle Initial',NULL,1,3,0,'','',NULL,3,'','2009-01-24 21:28:51'),(3457,'*.patient.vet','en','lava','crms',NULL,'patient','vet','Personal History/Current Arrangements','i','range','No','Millitary Veteran',NULL,NULL,NULL,0,'','generic.yesNoDK',NULL,3,'','2009-01-24 21:28:51'),(3458,'*.patient.firstName','en','lava','crms',NULL,'patient','firstName','ID and Core Demographics','i','string','Yes','First Name',NULL,NULL,NULL,0,'','',NULL,4,'','2009-01-24 21:28:51'),(3459,'*.patient.spanOr','en','lava','crms',NULL,'patient','spanOr','Racial Demographics','i','range','No','Spanish Origin',NULL,NULL,NULL,0,'','generic.yesNoDK',NULL,4,'','2009-01-24 21:28:51'),(3460,'*.patient.ifSpanOr','en','lava','crms',NULL,'patient','ifSpanOr','Racial Demographics','i','range','No','If of Spanish Origin',NULL,NULL,NULL,0,'','muds.spanishOrigin',NULL,5,'','2009-01-24 21:28:51'),(3461,'*.patient.suffix','en','lava','crms',NULL,'patient','suffix','ID and Core Demographics','i','string','No','Suffix',NULL,NULL,NULL,0,'','',NULL,5,'','2009-01-24 21:28:51'),(3462,'*.patient.degree','en','lava','crms',NULL,'patient','degree','ID and Core Demographics','i','string','No','Degree',NULL,NULL,NULL,0,'','',NULL,6,'','2009-01-24 21:28:51'),(3463,'*.patient.race','en','lava','crms',NULL,'patient','race','Racial Demographics','i','range','No','Race',NULL,NULL,NULL,0,'','generic.race',NULL,6,'','2009-01-24 21:28:51'),(3464,'*.patient.multRac','en','lava','crms',NULL,'patient','multRac','Racial Demographics','i','range','No','Mutiple Race',NULL,NULL,NULL,0,'','generic.yesNo',NULL,7,'','2009-01-24 21:28:51'),(3465,'*.patient.ucid','en','lava','crms',NULL,'patient','ucid','ID and Core Demographics','i','string','No','UCID/MRN',NULL,NULL,NULL,0,'','',NULL,7,'','2009-01-24 21:28:51'),(3466,'*.patient.multRace1','en','lava','crms',NULL,'patient','multRace1','Racial Demographics','i','range','No','Multiple Race 1',NULL,NULL,NULL,0,'','generic.race',NULL,8,'','2009-01-24 21:28:51'),(3467,'*.patient.ssn','en','lava','crms',NULL,'patient','ssn','ID and Core Demographics','i','string','No','SSN',NULL,NULL,NULL,0,'','',NULL,8,'','2009-01-24 21:28:51'),(3468,'*.patient.birthDate','en','lava','crms',NULL,'patient','birthDate','ID and Core Demographics','i','date','Yes','Date of Birth',NULL,NULL,NULL,0,'','',NULL,9,'','2009-01-24 21:28:51'),(3469,'*.patient.multRace2','en','lava','crms',NULL,'patient','multRace2','Racial Demographics','i','range','No','Multiple Race 2',NULL,NULL,NULL,0,'','generic.race',NULL,9,'','2009-01-24 21:28:51'),(3470,'*.patient.age','en','lava','crms',NULL,'patient','age','ID and Core Demographics','c','numeric','No','Age',NULL,NULL,NULL,0,'','',NULL,10,'','2009-01-24 21:28:51'),(3471,'*.patient.multRace3','en','lava','crms',NULL,'patient','multRace3','Racial Demographics','i','range','No','Multiple Race 3',NULL,NULL,NULL,0,'','generic.race',NULL,10,'','2009-01-24 21:28:51'),(3472,'*.patient.desRace','en','lava','crms',NULL,'patient','desRace','Racial Demographics','i','range','No','Race that Best Describes',NULL,NULL,NULL,0,'','generic.race',NULL,11,'','2009-01-24 21:28:51'),(3473,'*.patient.gender','en','lava','crms',NULL,'patient','gender','ID and Core Demographics','i','range','Yes','Gender',NULL,NULL,NULL,0,'','generic.gender',NULL,11,'','2009-01-24 21:28:51'),(3474,'*.patient.hand','en','lava','crms',NULL,'patient','hand','ID and Core Demographics','i','range','No','Handedness',NULL,NULL,NULL,0,'','patient.handedness',NULL,12,'','2009-01-24 21:28:51'),(3475,'*.patient.mrgStat','en','lava','crms',NULL,'patient','mrgStat','Personal History/Current Arrangements','i','range','No','Marital Status',NULL,NULL,NULL,0,'','patient.maritalStatus',NULL,12,'','2009-01-24 21:28:51'),(3476,'*.patient.sexualOrient','en','lava','crms',NULL,'patient','sexualOrient','Personal History/Current Arrangements','i','range','No','Sexual Orientation/Identity',NULL,NULL,NULL,0,'','patient.sexualOrientation',NULL,12,'','2009-01-24 21:28:51'),(3477,'*.patient.deceased','en','lava','crms',NULL,'patient','deceased','ID and Core Demographics','i','scale','Yes','Is Deceased',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,13,'','2009-01-24 21:28:51'),(3478,'*.patient.meda','en','lava','crms',NULL,'patient','meda','Insurance Details','i','range','No','MEDA',NULL,NULL,NULL,0,'','generic.yesNoDK',NULL,13,'','2009-01-24 21:28:51'),(3479,'*.patient.deathDate','en','lava','crms',NULL,'patient','deathDate','ID and Core Demographics','i','date','No','Date of Death',NULL,NULL,NULL,0,'','',NULL,14,'','2009-01-24 21:28:51'),(3480,'*.patient.medb','en','lava','crms',NULL,'patient','medb','Insurance Details','i','range','No','MEDB',NULL,NULL,NULL,0,'','generic.yesNoDK',NULL,14,'','2009-01-24 21:28:51'),(3481,'*.patient.medCal','en','lava','crms',NULL,'patient','medCal','Insurance Details','i','range','No','MEDCal',NULL,NULL,NULL,0,'','generic.yesNoDK',NULL,15,'','2009-01-24 21:28:51'),(3482,'*.patient.transNeeded','en','lava','crms',NULL,'patient','transNeeded','Language Details','i','scale','No','Interpreter Needed',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,15,'','2009-01-24 21:28:51'),(3483,'*.patient.primaryLanguage','en','lava','crms',NULL,'patient','primaryLanguage','Language Details','i','suggest','No','Primary Language',NULL,25,NULL,0,'','patient.patientLanguage',NULL,16,'','2009-01-24 21:28:51'),(3484,'*.patient.testingLanguage','en','lava','crms',NULL,'patient','testingLanguage','Language Details','i','suggest','No','Testing Language',NULL,25,NULL,0,'','patient.patientLanguage',NULL,16,'','2009-01-24 21:28:51'),(3485,'*.patient.transLanguage','en','lava','crms',NULL,'patient','transLanguage','Language Details','i','suggest','No','Interpreter Type',NULL,25,NULL,0,'','patient.patientLanguage',NULL,16,'','2009-01-24 21:28:51'),(3486,'*.patient.vaInsur','en','lava','crms',NULL,'patient','vaInsur','Insurance Details','i','range','No','VAinsur',NULL,NULL,NULL,0,'','generic.yesNoDK',NULL,16,'','2009-01-24 21:28:51'),(3487,'*.patient.enterBy','en','lava','crms',NULL,'patient','enterBy','Record Management','i','range','No','Entered By',NULL,NULL,NULL,0,'','patient.staffList',NULL,17,'','2009-01-24 21:28:51'),(3488,'*.patient.insurLongTerm','en','lava','crms',NULL,'patient','insurLongTerm','Insurance Details','i','range','No','InsurLongTerm',NULL,NULL,NULL,0,'','generic.yesNoDK',NULL,17,'','2009-01-24 21:28:51'),(3489,'*.patient.dupNameFlag','en','lava','crms',NULL,'patient','dupNameFlag','Record Management','i','scale','Yes','Dup Name Warning',NULL,NULL,NULL,0,'','generic.yesNoZero',NULL,18,'','2009-01-24 21:28:51'),(3490,'*.patient.other','en','lava','crms',NULL,'patient','other','Insurance Details','i','range','No','Other',NULL,NULL,NULL,0,'','generic.yesNoDK',NULL,18,'','2009-01-24 21:28:51'),(3491,'*.patient.fullNameRev','en','lava','crms',NULL,'patient','fullNameRev','','c','string','No','Patient',NULL,NULL,NULL,0,'','',NULL,19,'','2009-01-24 21:28:51'),(3492,'*.patient.none','en','lava','crms',NULL,'patient','none','Insurance Details','i','range','No','None',NULL,NULL,NULL,0,'','generic.yesNoDK',NULL,19,'','2009-01-24 21:28:51'),(3493,'*.patient.deidentified','en','lava','crms',NULL,'patient','deidentified','','i','toggle','No','Use Deidentified ID',NULL,NULL,NULL,0,'','',NULL,20,'','2009-01-24 21:28:51'),(3494,'*.patient.fullName','en','lava','crms',NULL,'patient','fullName','','c','string','No','Patient',NULL,NULL,NULL,0,'','',NULL,20,'','2009-01-24 21:28:51'),(3495,'*.patient.reimburse','en','lava','crms',NULL,'patient','reimburse','Insurance Details','i','range','No','Reimburse',NULL,NULL,NULL,0,'','muds.reimbursement',NULL,20,'','2009-01-24 21:28:51'),(3496,'*.patient.fullNameNoSuffix','en','lava','crms',NULL,'patient','fullNameNoSuffix','','c','string','No','Patient',NULL,NULL,NULL,0,'','',NULL,21,'','2009-01-24 21:28:51'),(3497,'*.patient.fullNameRevNoSuffix','en','lava','crms',NULL,'patient','fullNameRevNoSuffix','','c','string','No','Patient',NULL,NULL,NULL,0,'','',NULL,21,'','2009-01-24 21:28:51'),(3498,'*.patient.payment','en','lava','crms',NULL,'patient','payment','Insurance Details','i','range','No','Payment',NULL,NULL,NULL,0,'','muds.payment',NULL,21,'','2009-01-24 21:28:51'),(3499,'*.patient.subjectId','en','lava','crms',NULL,'patient','subjectId','','i','string','Yes','Subject ID',NULL,NULL,NULL,0,'','',NULL,21,'','2009-01-24 21:28:51'),(3500,'*.patient.primCare','en','lava','crms',NULL,'patient','primCare','Personal History/Current Arrangements','i','range','No','Primary Caregiver',NULL,NULL,NULL,0,'','muds.primaryCaregiver',NULL,22,'','2009-01-24 21:28:51'),(3501,'*.patient.reside','en','lava','crms',NULL,'patient','reside','Personal History/Current Arrangements','i','range','No','Principle Residence',NULL,NULL,NULL,0,'','muds.principalResidence',NULL,23,'','2009-01-24 21:28:51'),(3502,'*.patient.legStat','en','lava','crms',NULL,'patient','legStat','Personal History/Current Arrangements','i','range','No','Legal Status',NULL,NULL,NULL,0,'','patient.legalStatus',NULL,24,'','2009-01-24 21:28:51'),(3503,'*.patient.advDir','en','lava','crms',NULL,'patient','advDir','Personal History/Current Arrangements','i','range','No','Advanced Directive',NULL,NULL,NULL,0,'','generic.yesNo',NULL,25,'','2009-01-24 21:28:51'),(3504,'*.patient.raceText','en','lava','crms',NULL,'patient','raceText','','c','string','No','RaceText',NULL,NULL,NULL,0,'','',NULL,26,'','2009-01-24 21:28:51'),(3505,'*.patient.multRace1Text','en','lava','crms',NULL,'patient','multRace1Text','','c','string','No','MultRace1Text',NULL,NULL,NULL,0,'','',NULL,27,'','2009-01-24 21:28:51'),(3506,'*.patient.multRace2Text','en','lava','crms',NULL,'patient','multRace2Text','','c','string','No','MultRace2Text',NULL,NULL,NULL,0,'','',NULL,28,'','2009-01-24 21:28:51'),(3507,'*.patient.multRace3Text','en','lava','crms',NULL,'patient','multRace3Text','','c','string','No','MultRace3Text',NULL,NULL,NULL,0,'','',NULL,29,'','2009-01-24 21:28:51'),(3508,'*.patient.desRaceText','en','lava','crms',NULL,'patient','desRaceText','','c','string','No','DesRaceText',NULL,NULL,NULL,0,'','',NULL,30,'','2009-01-24 21:28:51'),(3509,'*.patient.ifSpanOrText','en','lava','crms',NULL,'patient','ifSpanOrText','','c','string','No','ifSpanOrText',NULL,NULL,NULL,0,'','',NULL,31,'','2009-01-24 21:28:51'),(3510,'*.patient.nihEthnicCategory','en','lava','crms',NULL,'patient','nihEthnicCategory','','c','string','No','NIHEthnicCategory',NULL,NULL,NULL,0,'','',NULL,32,'','2009-01-24 21:28:51'),(3511,'*.patient.nihRacialCategory','en','lava','crms',NULL,'patient','nihRacialCategory','','c','string','No','NIHRacialCategory',NULL,NULL,NULL,0,'','',NULL,33,'','2009-01-24 21:28:51'),(3512,'*.patient.nihHispanicRacialCategory','en','lava','crms',NULL,'patient','nihHispanicRacialCategory','','c','string','No','NIHHispanicRacialCategory',NULL,NULL,NULL,0,'','',NULL,34,'','2009-01-24 21:28:51'),(3513,'*.patient.altArccId','en','lava','crms',NULL,'patient','altArccId','Alternate ARCC ID','i','range','No','Alternate ARCC ID',NULL,NULL,NULL,0,'','patient.alternateARCCID',NULL,35,'','2009-01-24 21:28:51'),(3514,'*.patient.altPatId','en','lava','crms',NULL,'patient','altPatId','Alternate ARCC ID','i','string','No','Alternate PAT ID',NULL,NULL,NULL,0,'','',NULL,36,'','2009-01-24 21:28:51'),(3515,'*.patientContext.patientSearch','en','lava','crms',NULL,'patientContext','patientSearch','','i','suggest','No','',NULL,NULL,NULL,0,'','context.patientResults',NULL,NULL,'','2009-01-24 21:28:51'),(3516,'*.patientContext.searchBy','en','lava','crms',NULL,'patientContext','searchBy','','i','range','No','',NULL,NULL,NULL,0,'','context.searchBy',NULL,NULL,'','2009-01-24 21:28:51'),(3517,'*.patientDoctor.doctor_id','en','lava','crms',NULL,'patientDoctor','doctor_id','','i','range','','Doctor',NULL,NULL,NULL,0,'','doctor.allDoctors',NULL,NULL,'','2009-01-24 21:28:51'),(3518,'*.patientDoctor.id','en','lava','crms',NULL,'patientDoctor','id','','c','string','Yes','id',NULL,NULL,NULL,0,'','',NULL,1,'','2009-01-24 21:28:51'),(3519,'*.patientDoctor.docStat','en','lava','crms',NULL,'patientDoctor','docStat','','i','suggest','No','Doctor Status',NULL,NULL,NULL,0,'','doctor.doctorStatus',NULL,4,'','2009-01-24 21:28:51'),(3520,'*.patientDoctor.docNote','en','lava','crms',NULL,'patientDoctor','docNote','','i','text','No','Doctor Note',NULL,NULL,NULL,0,'rows=\"3\" cols=\"50\"','',NULL,5,'','2009-01-24 21:28:51'),(3521,'*.positionedProtocolConfig.anchorProtocol','en','lava','crms',NULL,'positionedProtocolConfig','anchorProtocol',NULL,'i','range',NULL,'Anchor Protocol',NULL,NULL,NULL,0,NULL,'protocol.seqAnchor',NULL,NULL,'Anchor Protocol for Protocol Position','2011-02-28 13:49:14'),(3522,'*.positionedProtocolConfig.anchorBeginEnd','en','lava','crms',NULL,'positionedProtocolConfig','anchorBeginEnd',NULL,'i','scale',NULL,'Anchor Begin or End',NULL,NULL,NULL,0,NULL,'protocol.seqAnchorBeginEnd',NULL,NULL,'Relative to Begin or End of Anchor Protocol','2011-02-28 13:49:14'),(3523,'*.positionedProtocolConfig.anchorDaysFrom','en','lava','crms',NULL,'positionedProtocolConfig','anchorDaysFrom',NULL,'i','range',NULL,'Days from Anchor',NULL,NULL,NULL,0,NULL,'protocol.seqAnchorDaysFrom',NULL,NULL,'Days from Anchor Protocol','2011-02-28 13:55:54'),(3524,'*.positionedProtocolConfig.id','en','lava','crms',NULL,'positionedProtocolConfig','id',NULL,'c','numeric',NULL,'ID',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Positioned Protocol ID','2011-03-01 17:14:54'),(3525,'*.positionedProtocolConfig.protocolId','en','lava','crms',NULL,'positionedProtocolConfig','protocolId',NULL,'i','range','Yes','Protocol',NULL,NULL,NULL,0,NULL,'protocol.allProtocols',NULL,NULL,'Unique ID of the Protocol','2011-03-02 15:20:03'),(3526,'*.project.projUnitDesc','en','lava','crms',NULL,'project','projUnitDesc','','c','string','No','Project',NULL,NULL,NULL,0,'','',NULL,NULL,'Project Name','2009-01-24 21:28:51'),(3527,'*.projectContext.projectName','en','lava','crms',NULL,'projectContext','projectName','','i','range','No','',NULL,NULL,NULL,0,'','context.projectList',NULL,NULL,'','2009-01-24 21:28:51'),(3528,'*.protocol.id','en','lava','crms',NULL,'protocol','id',NULL,'c','numeric',NULL,'ID',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Protocol ID','2011-03-12 17:20:13'),(3529,'*.protocol.staff','en','lava','crms',NULL,'protocol','staff',NULL,'i','suggest',NULL,'Staff',NULL,NULL,NULL,0,NULL,'project.staffList',NULL,NULL,'Staff Responsible','2011-03-12 17:21:23'),(3530,'*.protocol.projName','en','lava','crms',NULL,'protocol','projName',NULL,'c','string',NULL,'Project',NULL,NULL,NULL,0,NULL,'enrollmentStatus.patientProjects',NULL,NULL,'Project','2011-03-12 17:22:34'),(3531,'*.protocol.assignedDate','en','lava','crms',NULL,'protocol','assignedDate',NULL,'i','date',NULL,'Assignment Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Protocol Assignment Date','2011-03-12 17:24:44'),(3532,'*.protocol.currNote','en','lava','crms',NULL,'protocol','currNote',NULL,'i','string',NULL,'Current Status Note',NULL,100,NULL,0,NULL,NULL,NULL,NULL,'Current Protocol Status Note','2011-03-12 17:29:41'),(3533,'*.protocol.currReason','en','lava','crms',NULL,'protocol','currReason',NULL,'i','range',NULL,'Current Status Reason',NULL,NULL,NULL,0,NULL,'protocol.compReason',NULL,NULL,'Current Protocol Status Reason','2011-03-12 17:29:41'),(3534,'*.protocol.currStatus','en','lava','crms',NULL,'protocol','currStatus',NULL,'i','range',NULL,'Current Status',NULL,NULL,NULL,0,NULL,'protocol.compStatus',NULL,NULL,'Current Protocol Status','2011-03-12 17:29:41'),(3535,'*.protocol.protocolConfigId','en','lava','crms',NULL,'protocol','protocolConfigId',NULL,'i','range',NULL,'Protocol Config',NULL,NULL,NULL,0,NULL,'protocol.allProtocolConfigs',NULL,NULL,'Protocol Configuration','2011-03-13 11:21:45'),(3536,'*.protocol.assignDescrip','en','lava','crms',NULL,'protocol','assignDescrip',NULL,'c','string',NULL,'Assignment',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Assignment','2011-11-20 11:47:52'),(3537,'*.protocol.currStatusBlock','en','lava','crms',NULL,'protocol','currStatusBlock',NULL,'c','string',NULL,'Status',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Status','2011-11-21 06:25:45'),(3538,'*.protocol.notes','en','lava','crms',NULL,'protocol','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,NULL,'Notes','2011-11-13 22:08:19'),(3539,'*.protocolCompletionStatus.quickFilter','en','lava','crms',NULL,'protocolCompletionStatus','quickFilter',NULL,'c','string',NULL,'Quick Filter',NULL,NULL,NULL,0,NULL,'protocolCompletionStatus.quickFilter',NULL,NULL,'Quick Filter','2011-11-21 08:14:35'),(3540,'*.protocolConfig.configSummary','en','lava','crms',NULL,'protocolConfig','configSummary',NULL,'c','string',NULL,'Configuration',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Configuration','2012-01-20 17:23:44'),(3541,'*.protocolConfig.label','en','lava','crms',NULL,'protocolConfig','label',NULL,'i','string','No','Protocol',NULL,25,NULL,0,NULL,NULL,NULL,NULL,'Protocol','2010-11-19 16:58:33'),(3542,'*.protocolConfig.notes','en','lava','crms',NULL,'protocolConfig','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,NULL,'Notes','2010-11-19 17:01:05'),(3543,'*.protocolConfig.effDate','en','lava','crms',NULL,'protocolConfig','effDate',NULL,'i','date','No','Effective Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Effective Date','2010-11-19 17:02:26'),(3544,'*.protocolConfig.expDate','en','lava','crms',NULL,'protocolConfig','expDate',NULL,'i','date','No','Expiration Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Expiration Date','2010-11-19 17:03:16'),(3545,'*.protocolConfig.projName','en','lava','crms',NULL,'protocolConfig','projName',NULL,'i','range','Yes','Project',NULL,NULL,NULL,0,NULL,'context.projectList',NULL,NULL,'Protocol Project','2010-11-19 17:05:13'),(3546,'*.protocolConfig.id','en','lava','crms',NULL,'protocolConfig','id',NULL,'c','numeric',NULL,'Protocol ID',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Protocol ID','2010-11-23 17:54:09'),(3547,'*.protocolConfig.staff','en','lava','crms',NULL,'protocolConfig','staff',NULL,'i','suggest','Yes','Staff',NULL,NULL,NULL,0,NULL,'project.staffList',NULL,NULL,'Staff member responsible for this protocol component','2010-11-29 18:47:12'),(3548,'*.protocolConfig.category','en','lava','crms',NULL,'protocolConfig','category',NULL,'i','range','No','Category',NULL,NULL,NULL,0,NULL,'protocol.category',NULL,NULL,'Category','2011-02-22 15:57:51'),(3549,'*.protocolConfig.firstProtocolTimepointConfigId','en','lava','crms',NULL,'protocolConfig','firstProtocolTimepointConfigId',NULL,'i','range','No','First Timepoint',NULL,NULL,NULL,0,NULL,'protocol.allTimepointConfigs',NULL,NULL,'First Timepoint','2011-11-17 18:06:40'),(3550,'*.protocolInstrument.collectAnchorDate','en','lava','crms',NULL,'protocolInstrument','collectAnchorDate',NULL,'c','date',NULL,'Collect Anchor Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Collect Anchor Date','2011-11-18 13:39:59'),(3551,'*.protocolInstrument.collectWinEnd','en','lava','crms',NULL,'protocolInstrument','collectWinEnd',NULL,'c','date',NULL,'Collect Window End',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Instrument Collect Window End','2011-03-12 18:47:05'),(3552,'*.protocolInstrument.collectWinStart','en','lava','crms',NULL,'protocolInstrument','collectWinStart',NULL,'c','date',NULL,'Collect Window Start',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Instrument Collect Window Start','2011-03-12 18:47:05'),(3553,'*.protocolInstrument.collectWinNote','en','lava','crms',NULL,'protocolInstrument','collectWinNote',NULL,'i','string',NULL,'Collection Status Note',NULL,100,NULL,0,NULL,NULL,NULL,NULL,'Instrument Collect Window Status Note','2011-03-12 18:47:05'),(3554,'*.protocolInstrument.collectWinReason','en','lava','crms',NULL,'protocolInstrument','collectWinReason',NULL,'i','range',NULL,'Collection Status Reason',NULL,NULL,NULL,0,NULL,'protocol.collectReason',NULL,NULL,'Instrument Collect Window Status Reason','2011-03-12 18:47:05'),(3555,'*.protocolInstrument.collectWinStatus','en','lava','crms',NULL,'protocolInstrument','collectWinStatus',NULL,'i','range',NULL,'Collection Status',NULL,NULL,NULL,0,NULL,'protocol.collectStatus',NULL,NULL,'Instrument Collect Window Status','2011-03-12 18:47:05'),(3556,'*.protocolInstrument.compNote','en','lava','crms',NULL,'protocolInstrument','compNote',NULL,'i','string',NULL,NULL,NULL,100,NULL,0,NULL,NULL,NULL,NULL,'Instrument Completion Status Note','2011-03-12 18:47:05'),(3557,'*.protocolInstrument.compReason','en','lava','crms',NULL,'protocolInstrument','compReason',NULL,'i','range',NULL,'Completion Status Reason',NULL,NULL,NULL,0,NULL,'protocol.compReason',NULL,NULL,'Instrument Completion Status Reason','2011-03-12 18:47:05'),(3558,'*.protocolInstrument.compStatus','en','lava','crms',NULL,'protocolInstrument','compStatus',NULL,'i','range',NULL,'Completion Status',NULL,NULL,NULL,0,NULL,'protocol.compStatus',NULL,NULL,'Instrument Completion Status','2011-03-12 18:47:05'),(3559,'*.protocolInstrument.staff','en','lava','crms',NULL,'protocolInstrument','staff',NULL,'i','suggest',NULL,'Staff',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Staff Responsible','2011-03-12 18:47:05'),(3560,'*.protocolInstrument.id','en','lava','crms',NULL,'protocolInstrument','id',NULL,'c','numeric',NULL,'ID',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Patient Protocol Instrument ID','2011-03-12 18:47:05'),(3561,'*.protocolInstrument.instrId','en','lava','crms',NULL,'protocolInstrument','instrId',NULL,'i','range','No','Instrument',NULL,NULL,45,0,NULL,'protocol.matchingInstruments',NULL,NULL,'Instrument that matches Protocol definition','2011-06-03 20:27:26'),(3562,'*.protocolInstrument.notes','en','lava','crms',NULL,'protocolInstrument','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,NULL,'Notes','2011-11-13 22:18:49'),(3563,'*.protocolInstrumentConfig.defaultCompStatus','en','lava','crms',NULL,'protocolInstrumentConfig','defaultCompStatus',NULL,'i','range','No','Completion Status',NULL,NULL,NULL,0,NULL,'protocol.defaultCompletionStatus',NULL,NULL,'Default Completion Status','2011-11-18 12:37:04'),(3564,'*.protocolInstrumentConfig.defaultCompReason','en','lava','crms',NULL,'protocolInstrumentConfig','defaultCompReason',NULL,'i','range','No','Completion Reason',NULL,NULL,NULL,0,NULL,'protocol.defaultCompletionReason',NULL,NULL,'Default Completion Reason','2011-11-18 12:37:04'),(3565,'*.protocolInstrumentConfig.defaultCompNote','en','lava','crms',NULL,'protocolInstrumentConfig','defaultCompNote',NULL,'i','string','No','Completion Notes',NULL,100,NULL,0,NULL,NULL,NULL,NULL,'Default Completion Notes','2011-11-18 12:37:04'),(3566,'*.protocolInstrumentConfig.defaultOptionId','en','lava','crms',NULL,'protocolInstrumentConfig','defaultOptionId',NULL,'i','range',NULL,'Default Option',NULL,NULL,NULL,0,NULL,'protocol.instrumentConfigDefaultOption',NULL,NULL,'Default Option','2011-11-18 12:43:11'),(3567,'*.protocolInstrumentConfig.instrType','en','lava','crms',NULL,'protocolInstrumentConfig','instrType',NULL,'i','range','Yes','Instrument Type',NULL,NULL,NULL,0,NULL,'instrumentMetadata.instrTypes',NULL,NULL,'Instrument Type','2011-12-12 08:33:43'),(3568,'*.protocolInstrumentConfig.category','en','lava','crms',NULL,'protocolInstrumentConfig','category',NULL,'i','range','No','Category',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Category','2012-01-16 13:10:37'),(3569,'*.protocolInstrumentConfig.customCollectWinDefined','en','lava','crms',NULL,'protocolInstrumentConfig','customCollectWinDefined',NULL,'i','toggle',NULL,'Define a Custom Data Collection Window?',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Define a Custom Data Collection Window?','2012-01-18 16:21:11'),(3570,'*.protocolInstrumentConfig.label','en','lava','crms',NULL,'protocolInstrumentConfig','label',NULL,'i','string','No','Instrument',NULL,25,NULL,0,NULL,NULL,NULL,NULL,'Instrument','2010-11-19 17:16:18'),(3571,'*.protocolInstrumentConfig.notes','en','lava','crms',NULL,'protocolInstrumentConfig','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,NULL,'Notes','2010-11-19 17:17:18'),(3572,'*.protocolInstrumentConfig.effDate','en','lava','crms',NULL,'protocolInstrumentConfig','effDate',NULL,'i','date','No','Effective Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Instrument Effective Date','2010-11-19 17:18:27'),(3573,'*.protocolInstrumentConfig.expDate','en','lava','crms',NULL,'protocolInstrumentConfig','expDate',NULL,'i','date','No','Expiration Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Instrument Expiration Date','2010-11-19 17:19:59'),(3574,'*.protocolInstrumentConfig.id','en','lava','crms',NULL,'protocolInstrumentConfig','id',NULL,'c','numeric',NULL,'Instrument ID',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Instrument ID','2010-11-23 22:24:00'),(3575,'*.protocolInstrumentConfig.optional','en','lava','crms',NULL,'protocolInstrumentConfig','optional',NULL,'i','toggle',NULL,'Optional',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Is instrument optional?','2010-11-29 18:23:54'),(3576,'*.protocolInstrumentConfig.staff','en','lava','crms',NULL,'protocolInstrumentConfig','staff',NULL,'i','suggest','Yes','Staff',NULL,NULL,NULL,0,NULL,'project.staffList',NULL,NULL,'Staff member responsible for this protocol component','2010-11-29 18:51:13'),(3577,'*.protocolInstrumentConfig.customCollectWinProtocolVisitConfigId','en','lava','crms',NULL,'protocolInstrumentConfig','customCollectWinProtocolVisitConfigId',NULL,'i','range','No','Visit Anchor',NULL,NULL,NULL,0,NULL,'protocol.primaryProtocolVisitConfig',NULL,NULL,'Collect Window Visit Anchor','2010-11-21 09:49:27'),(3578,'*.protocolInstrumentConfig.customCollectWinSize','en','lava','crms',NULL,'protocolInstrumentConfig','customCollectWinSize',NULL,'i','range','No','Window Size (days)',NULL,NULL,NULL,0,NULL,'protocol.collectWinSize',NULL,NULL,'Collect Window Size (days)','2010-11-21 09:52:14'),(3579,'*.protocolInstrumentConfig.customCollectWinOffset','en','lava','crms',NULL,'protocolInstrumentConfig','customCollectWinOffset',NULL,'i','range','No','Window Offset (days)',NULL,NULL,NULL,0,NULL,'protocol.collectWinOffset',NULL,NULL,'Collect Window Offset (days)','2010-11-21 09:53:23'),(3580,'*.protocolInstrumentConfigOption.label','en','lava','crms',NULL,'protocolInstrumentConfigOption','label',NULL,'i','string','No','Instrument Option',NULL,25,NULL,0,NULL,NULL,NULL,NULL,'Instrument Option','2010-11-19 17:16:18'),(3581,'*.protocolInstrumentConfigOption.notes','en','lava','crms',NULL,'protocolInstrumentConfigOption','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,NULL,'Notes','2010-11-19 17:17:18'),(3582,'*.protocolInstrumentConfigOption.effDate','en','lava','crms',NULL,'protocolInstrumentConfigOption','effDate',NULL,'i','date','No','Effective Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Instrument Option Effective Date','2010-11-19 17:18:27'),(3583,'*.protocolInstrumentConfigOption.expDate','en','lava','crms',NULL,'protocolInstrumentConfigOption','expDate',NULL,'i','date','No','Expiration Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Instrument Option Expiration Date','2010-11-19 17:19:59'),(3584,'*.protocolInstrumentConfigOption.instrType','en','lava','crms',NULL,'protocolInstrumentConfigOption','instrType',NULL,'i','range','Yes','Instrument Type',NULL,NULL,NULL,0,NULL,'instrumentMetadata.instrTypes',NULL,NULL,'Instrument Type','2010-11-21 10:07:23'),(3585,'*.protocolInstrumentConfigOption.id','en','lava','crms',NULL,'protocolInstrumentConfigOption','id',NULL,'c','numeric',NULL,'Instrument Option ID',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Instrument Option ID','2010-11-23 22:27:07'),(3586,'*.protocolSequenceConfig.name','en','lava','crms',NULL,'protocolSequenceConfig','name',NULL,'i','string',NULL,'Name',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Protocol Sequence Name','2011-02-27 16:59:28'),(3587,'*.protocolSequenceConfig.category','en','lava','crms',NULL,'protocolSequenceConfig','category',NULL,'i','range',NULL,'Category',NULL,NULL,NULL,0,NULL,'protocol.seqCategories',NULL,NULL,'Protocol Sequence Category','2011-02-27 17:25:09'),(3588,'*.protocolSequenceConfig.descrip','en','lava','crms',NULL,'protocolSequenceConfig','notes',NULL,'i','string',NULL,'Notes',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Notes','2011-02-27 17:25:09'),(3589,'*.protocolSequenceConfig.id','en','lava','crms',NULL,'protocolSequenceConfig','id',NULL,'c','numeric',NULL,'Sequence ID',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Protocol Sequence ID','2011-03-01 17:14:54'),(3590,'*.protocolTimepointConfig.schedWinRelativeMode','en','lava','crms',NULL,'protocolTimepoint','schedWinRelativeMode',NULL,'i','scale','No','Mode',NULL,NULL,NULL,0,NULL,'protocol.schedWinRelativeMode',NULL,NULL,'Mode (Calendar Days or Working Days) for Time from Relative Timepoint','2011-11-17 16:44:35'),(3591,'*.protocolTimepoint.schedAnchorDate','en','lava','crms',NULL,'protocolTimepoint','schedAnchorDate',NULL,'c','date','No','Scheduling Anchor Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Scheduling Anchor Date','2011-11-17 18:02:56'),(3592,'*.protocolTimepoint.collectAnchorDate','en','lava','crms',NULL,'protocolTimepoint','collectAnchorDate',NULL,'c','date',NULL,'Collect Anchor Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Collect Anchor Date','2011-11-18 13:42:02'),(3593,'*.protocolTimepoint.primaryProtocolVisitId','en','lava','crms',NULL,'protocolTimepoint','primaryProtocolVisitId',NULL,'c','string',NULL,'Primary Visit',NULL,NULL,NULL,0,NULL,'protocol.primaryProtocolVisit',NULL,NULL,'Primary Visit for this Timepoint','2011-11-18 14:11:02'),(3594,'*.protocolTimepoint.collectWinEnd','en','lava','crms',NULL,'protocolTimepoint','collectWinEnd',NULL,'c','date',NULL,'Collect Window End',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Timepoint Collect Window End','2011-03-12 18:23:22'),(3595,'*.protocolTimepoint.collectWinStart','en','lava','crms',NULL,'protocolTimepoint','collectWinStart',NULL,'c','date',NULL,'Collect Window Start',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Timepoint Collect Window Start','2011-03-12 18:23:22'),(3596,'*.protocolTimepoint.collectWinNote','en','lava','crms',NULL,'protocolTimepoint','collectWinNote',NULL,'i','string',NULL,'Collect Window Status Note',NULL,100,NULL,0,NULL,NULL,NULL,NULL,'Timepoint Collect Window Status Note','2011-03-12 18:23:22'),(3597,'*.protocolTimepoint.collectWinReason','en','lava','crms',NULL,'protocolTimepoint','collectWinReason',NULL,'i','range',NULL,'Collect Window Status Reason',NULL,NULL,NULL,0,NULL,'protocol.collectReason',NULL,NULL,'Timepoint Collect Window Status Reason','2011-03-12 18:23:22'),(3598,'*.protocolTimepoint.collectWinStatus','en','lava','crms',NULL,'protocolTimepoint','collectWinStatus',NULL,'i','range',NULL,'Collect Window Status',NULL,NULL,NULL,0,NULL,'protocol.collectStatus',NULL,NULL,'Timepoint Collect Window Status','2011-03-12 18:23:22'),(3599,'*.protocolTimepoint.schedWinEnd','en','lava','crms',NULL,'protocolTimepoint','schedWinEnd',NULL,'c','date','','Scheduling Window End',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Timepoint Scheduling Window End','2011-03-12 18:23:22'),(3600,'*.protocolTimepoint.schedWinStart','en','lava','crms',NULL,'protocolTimepoint','schedWinStart',NULL,'c','date',NULL,'Scheduling Window Start',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Timepoint Scheduling Window Start','2011-03-12 18:23:22'),(3601,'*.protocolTimepoint.schedWinNote','en','lava','crms',NULL,'protocolTimepoint','schedWinNote',NULL,'i','string',NULL,'Scheduling Window Status Note',NULL,100,NULL,0,NULL,NULL,NULL,NULL,'Timepoint Scheduling Window Status Note','2011-03-12 18:23:22'),(3602,'*.protocolTimepoint.schedWinReason','en','lava','crms',NULL,'protocolTimepoint','schedWinReason',NULL,'i','range',NULL,'Scheduling Window Status Reason',NULL,NULL,NULL,0,NULL,'protocol.schedReason',NULL,NULL,'Timepoint Scheduling Window Status Reason','2011-03-12 18:23:22'),(3603,'*.protocolTimepoint.schedWinStatus','en','lava','crms',NULL,'protocolTimepoint','schedWinStatus',NULL,'i','range',NULL,'Scheduling Window Status',NULL,NULL,NULL,0,NULL,'protocol.schedStatus',NULL,NULL,'Timepoint Scheduling Window Status','2011-03-12 18:23:22'),(3604,'*.protocolTimepoint.staff','en','lava','crms',NULL,'protocolTimepoint','staff',NULL,'i','suggest',NULL,'Staff',NULL,NULL,NULL,0,NULL,'project.staffList',NULL,NULL,'Staff Responsible','2011-03-12 17:34:24'),(3605,'*.protocolTimepoint.id','en','lava','crms',NULL,'protocolTimepoint','id',NULL,'c','numeric',NULL,'ID',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Patient Protocol Timepoint ID','2011-03-12 18:42:16'),(3606,'*.protocolTimepoint.notes','en','lava','crms',NULL,'protocolTimepoint','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,NULL,'Notes','2011-11-13 22:16:43'),(3607,'*.protocolTimepointConfig.schedWinRelativeUnits','en','lava','crms',NULL,'protocolTimepointConfig','schedWinRelativeUnits',NULL,'i','range','No','Units',NULL,NULL,NULL,0,NULL,'protocol.schedWinRelativeUnits',NULL,NULL,'Units for Amount of Time from Relative Timepoint','2011-11-17 17:03:45'),(3608,'*.protocolTimepointConfig.duration','en','lava','crms',NULL,'protocolTimepointConfig','duration',NULL,'i','range','No','Timepoint Duration (days)',NULL,NULL,NULL,0,NULL,'protocol.tpDuration',NULL,NULL,'Duration of a Timepoint (only for Timepoints that span multiple days)','2012-01-13 12:16:18'),(3609,'*.protocolTimepointConfig.schedAutomatic','en','lava','crms',NULL,'protocolTimepointConfig','schedAutomatic',NULL,'i','toggle',NULL,'Automatically Schedule Next Timepoint',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Schedule the next timepoint when this timepoint is complete','2012-01-13 12:18:40'),(3610,'*.protocolTimepointConfig.repeatCreateAutomatic','en','lava','crms',NULL,'protocolTimepointConfig','repeatCreateAutomatic',NULL,'i','toggle',NULL,'Automatically Create Next Timepoint',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Create the next timepoint when this timepoint is complete','2012-01-17 15:25:48'),(3611,'*.protocolTimepointConfig.collectWindowDefined','en','lava','crms',NULL,'protocolTimepointConfig','collectWindowDefined',NULL,'i','toggle',NULL,'Define a Data Collection Window?',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Define a Data Collection Window?','2012-01-18 15:02:53'),(3612,'*.protocolTimepointConfig.repeating','en','lava','crms',NULL,'protocolTimepointConfig','repeating',NULL,'i','toggle',NULL,'Define Repeating Timepoint?',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Define Repeating Timepoint?','2012-01-18 15:05:14'),(3613,'*.protocolTimepointConfig.schedWinRelativeTimepointId','en','lava','crms',NULL,'protocolTimepointConfig','schedWinRelativeTimepointId',NULL,'i','range','Yes','Relative Timepoint',NULL,NULL,NULL,0,NULL,'protocol.schedWinRelativeTimepoint',NULL,NULL,'Scheduling Window Relative Timepoint','2010-11-21 08:21:32'),(3614,'*.protocolTimepointConfig.schedWinRelativeAmount','en','lava','crms',NULL,'protocolTimepointConfig','schedWinRelativeAmount','','i','range','Yes','Time From Relative Timepoint',NULL,NULL,NULL,0,NULL,'protocol.schedWinRelativeAmount',NULL,NULL,'Scheduling Window Time From Relative Timepoint','2010-11-21 08:21:32'),(3615,'*.protocolTimepointConfig.schedWinSize','en','lava','crms',NULL,'protocolTimepointConfig','schedWinSize',NULL,'i','range','Yes','Size (days)',NULL,NULL,NULL,0,NULL,'protocol.schedWinSize',NULL,NULL,'Scheduling Window Size (days)','2010-11-21 08:26:09'),(3616,'*.protocolTimepointConfig.schedWinOffset','en','lava','crms',NULL,'protocolTimepointConfig','schedWinOffset',NULL,'i','range','Yes','Offset (days)',NULL,NULL,NULL,0,NULL,'protocol.schedWinOffset',NULL,NULL,'Scheduling Window Offset (days)','2010-11-21 08:27:51'),(3617,'*.protocolTimepointConfig.primaryProtocolVisitConfigId','en','lava','crms',NULL,'protocolTimepointConfig','primaryProtocolVisitConfigId',NULL,'i','range','No','Primary Visit',NULL,0,0,0,'','protocol.primaryProtocolVisitConfig',NULL,NULL,'Primary Visit Config','2010-11-21 08:31:31'),(3618,'*.protocolTimepointConfig.collectWinSize','en','lava','crms',NULL,'protocolTimepointConfig','collectWinSize',NULL,'i','range','Yes','Size (days)',NULL,NULL,NULL,0,NULL,'protocol.collectWinSize',NULL,NULL,'Collect Window Size (days)','2010-11-21 08:38:54'),(3619,'*.protocolTimepointConfig.collectWinOffset','en','lava','crms',NULL,'protocolTimepointConfig','collectWinOffset',NULL,'i','range','Yes','Offset (days)',NULL,NULL,NULL,0,NULL,'protocol.collectWinOffset',NULL,NULL,'Collect Window Offset (days)','2010-11-21 08:46:12'),(3620,'*.protocolTimepointConfig.repeatInitialNum','en','lava','crms',NULL,'protocolTimepointConfig','repeatInitialNum',NULL,'i','range',NULL,'Initial Number',NULL,NULL,NULL,0,NULL,'repeatingTimepoint.initialNum',NULL,NULL,'Num Timepoints to Create Initially','2010-11-29 16:34:45'),(3621,'*.protocolTimepointConfig.repeatInterval','en','lava','crms',NULL,'protocolTimepointConfig','repeatInterval',NULL,'i','range',NULL,'Interval (days)',NULL,NULL,NULL,0,NULL,'repeatingTimepoint.interval',NULL,NULL,'Interval (days)','2010-11-29 16:36:05'),(3622,'*.protocolTimepointConfig.label','en','lava','crms',NULL,'protocolTimepointConfig','label',NULL,'i','string','No','Timepoint',NULL,25,NULL,0,NULL,NULL,NULL,NULL,'Timepoint','2010-11-19 17:16:18'),(3623,'*.protocolTimepointConfig.notes','en','lava','crms',NULL,'protocolTimepointConfig','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,NULL,'Notes','2010-11-19 17:17:18'),(3624,'*.protocolTimepointConfig.effDate','en','lava','crms',NULL,'protocolTimepointConfig','effDate',NULL,'i','date','No','Effective Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Timepoint Effective Date','2010-11-19 17:18:27'),(3625,'*.protocolTimepointConfig.expDate','en','lava','crms',NULL,'protocolTimepointConfig','expDate',NULL,'i','date','No','Expiration Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Timepoint Expiration Date','2010-11-19 17:19:59'),(3626,'*.protocolTimepointConfig.optional','en','lava','crms',NULL,'protocolTimepointConfig','optional',NULL,'i','toggle','No','Timepoint Optional',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Timepoint Optional','2010-11-21 09:09:03'),(3627,'*.protocolTimepointConfig.id','en','lava','crms',NULL,'protocolTimepointConfig','id',NULL,'c','numeric',NULL,'Timepoint ID',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Timepoint ID','2010-11-23 22:23:18'),(3628,'*.protocolVisit.staff','en','lava','crms',NULL,'protocolVisit','staff',NULL,'i','suggest',NULL,'Staff',NULL,NULL,NULL,0,NULL,'project.staffList',NULL,NULL,'Staff Responsible','2011-03-12 18:36:13'),(3629,'*.protocolVisit.compReason','en','lava','crms',NULL,'protocolVisit','compReason',NULL,'i','range',NULL,'Completion Status Reason',NULL,NULL,NULL,0,NULL,'protocol.compReason',NULL,NULL,'Visit Completion Status Reason','2011-03-12 18:39:09'),(3630,'*.protocolVisit.compStatus','en','lava','crms',NULL,'protocolVisit','compStatus',NULL,'i','range',NULL,'Completion Status',NULL,NULL,NULL,0,NULL,'protocol.compStatus',NULL,NULL,'Visit Completion Status','2011-03-12 18:39:09'),(3631,'*.protocolVisit.id','en','lava','crms',NULL,'protocolVisit','id',NULL,'c','numeric',NULL,'ID',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Patient Protocol Visit ID','2011-03-12 18:42:16'),(3632,'*.protocolVisit.compNote','en','lava','crms',NULL,'protocolVisit','compNote',NULL,'i','string',NULL,'Completion Status Note',NULL,100,NULL,0,NULL,NULL,NULL,NULL,'Visit Completion Status Note','2011-03-12 18:42:16'),(3633,'*.protocolVisit.visitId','en','lava','crms',NULL,'protocolVisit','visitId',NULL,'i','range','No','Visit',NULL,NULL,45,0,NULL,'protocol.matchingVisits',NULL,NULL,'Visit that matches Protocol definition','2011-03-24 14:39:41'),(3634,'*.protocolVisit.notes','en','lava','crms',NULL,'protocolVisit','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,NULL,'Notes','2011-11-13 22:17:50'),(3635,'*.protocolVisitConfig.defaultOptionId','en','lava','crms',NULL,'protocolVisitConfig','defaultOptionId',NULL,'i','range',NULL,'Default Option',NULL,NULL,NULL,0,NULL,'protocol.visitConfigDefaultOption',NULL,NULL,'Default Option','2011-11-18 12:44:19'),(3636,'*.protocolVisitConfig.category','en','lava','crms',NULL,'protocolVisitConfig','category',NULL,'i','range','No','Category',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Category','2012-01-16 13:08:48'),(3637,'*.protocolVisitConfig.label','en','lava','crms',NULL,'protocolVisitConfig','label',NULL,'i','string','No','Visit',NULL,25,NULL,0,NULL,NULL,NULL,NULL,'Visit','2010-11-19 17:16:18'),(3638,'*.protocolVisitConfig.notes','en','lava','crms',NULL,'protocolVisitConfig','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,NULL,'Notes','2010-11-19 17:17:18'),(3639,'*.protocolVisitConfig.effDate','en','lava','crms',NULL,'protocolVisitConfig','effDate',NULL,'i','date','No','Effective Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Visit Effective Date','2010-11-19 17:18:27'),(3640,'*.protocolVisitConfig.expDate','en','lava','crms',NULL,'protocolVisitConfig','expDate',NULL,'i','date','No','Expiration Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Visit Expiration Date','2010-11-19 17:19:59'),(3641,'*.protocolVisitConfig.optional','en','lava','crms',NULL,'protocolVisitConfig','optional',NULL,'i','toggle',NULL,'Visit Optional',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Visit Optional','2010-11-21 09:09:03'),(3642,'*.protocolVisitConfig.id','en','lava','crms',NULL,'protocolVisitConfig','id',NULL,'c','numeric',NULL,'Visit ID',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Visit ID','2010-11-23 22:23:18'),(3643,'*.protocolVisitConfig.staff','en','lava','crms',NULL,'protocolVisitConfig','staff',NULL,'i','suggest','Yes','Staff',NULL,NULL,NULL,0,NULL,'project.staffList','',NULL,'Staff member responsible for this protocol component','2010-11-29 18:49:16'),(3644,'*.protocolVisitConfigOption.visitTypeProjName','en','lava','crms',NULL,'protocolVisitOptionConfig','visitTypeProjName',NULL,'i','range','Yes','Project for Visit Type',NULL,NULL,NULL,0,NULL,'context.projectList',NULL,NULL,'Project for Visit Types','2011-11-18 13:30:05'),(3645,'*.protocolVisitConfigOption.visitTypeInList','en','lava','crms',NULL,'protocolVisitOptionConfig','visitTypeInList',NULL,'c','string','No','&nbsp;&nbsp;&nbsp;&#8226;&nbsp;',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Visit Type','2011-11-20 13:43:37'),(3646,'*.protocolVisitConfigOption.label','en','lava','crms',NULL,'protocolVisitOptionConfig','label',NULL,'i','string','No','Visit Option',NULL,25,NULL,0,NULL,NULL,NULL,NULL,'Visit Option','2010-11-19 17:16:18'),(3647,'*.protocolVisitConfigOption.notes','en','lava','crms',NULL,'protocolVisitOptionConfig','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,NULL,'Notes','2010-11-19 17:17:18'),(3648,'*.protocolVisitConfigOption.effDate','en','lava','crms',NULL,'protocolVisitOptionConfig','effDate',NULL,'i','date','No','Effective Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Visit Option Effective Date','2010-11-19 17:18:27'),(3649,'*.protocolVisitConfigOption.expDate','en','lava','crms',NULL,'protocolVisitOptionConfig','expDate',NULL,'i','date','No','Expiration Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Visit Option Expiration Date','2010-11-19 17:19:59'),(3650,'*.protocolVisitConfigOption.visitType','en','lava','crms',NULL,'protocolVisitOptionConfig','visitType',NULL,'i','range','Yes','Visit Type',NULL,NULL,NULL,0,NULL,'visit.visitTypes',NULL,NULL,'Visit Type','2010-11-21 09:35:08'),(3651,'*.protocolVisitConfigOption.defaultOption','en','lava','crms',NULL,'protocolVisitOptionConfig','defaultOption',NULL,'i','toggle','No','Default Visit Fulfillment Option',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Default Visit Fulfillment Option','2010-11-21 09:35:08'),(3652,'*.protocolVisitConfigOption.id','en','lava','crms',NULL,'protocolVisitOptionConfig','id',NULL,'c','numeric',NULL,'Visit Option ID',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Visit Option ID','2010-11-23 22:23:18'),(3653,'*.reportSetup.format','en','lava','crms',NULL,'reportSetup','format','','i','range','No','Format',NULL,NULL,10,0,'','reportSetup.format',NULL,NULL,'','2009-01-30 01:41:46'),(3654,'filter.reportSetup.customDateStart','en','lava','crms','filter','reportSetup','customDateStart','','i','date','No','Date is between',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-30 01:41:46'),(3655,'filter.reportSetup.customDateEnd','en','lava','crms','filter','reportSetup','customDateEnd','','i','date','No','      and',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-30 01:41:46'),(3656,'filter.reportSetup.projectList','en','lava','crms','filter','reportSetup','projectList','','i','multiple','No','Project(s)',NULL,NULL,20,0,'','context.projectList',NULL,NULL,'','2009-01-30 01:41:46'),(3657,'udsExtract.reportSetup.customDateStart','en','lava','crms','udsExtract','reportSetup','customDateStart',NULL,'i','datetime','No','Start Date/Time',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'2009-01-30 01:41:46'),(3658,'udsExtract.reportSetup.format','en','lava','crms','udsExtract','reportSetup','format',NULL,'i','range','No','Format',NULL,NULL,10,0,NULL,'reportSetup.formatCsv',NULL,NULL,NULL,'2009-01-30 01:41:46'),(3659,'filter.reportSetup.patientId','en','lava','crms','filter','reportSetup','patientId',NULL,'i','numeric','No','Patient ID',NULL,NULL,5,0,NULL,NULL,NULL,NULL,NULL,'2009-01-30 01:41:46'),(3660,'*.reportSetup.format','en','lava','crms',NULL,'reportSetup','format','','i','range','No','Format',NULL,NULL,10,0,'','reportSetup.format',NULL,NULL,'','2009-01-30 01:41:46'),(3661,'filter.reportSetup.customDateStart','en','lava','crms','filter','reportSetup','customDateStart','','i','date','No','Date is between',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-30 01:41:46'),(3662,'filter.reportSetup.customDateEnd','en','lava','crms','filter','reportSetup','customDateEnd','','i','date','No','      and',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-30 01:41:46'),(3663,'filter.reportSetup.projectList','en','lava','crms','filter','reportSetup','projectList','','i','multiple','No','Project(s)',NULL,NULL,20,0,'','context.projectList',NULL,NULL,'','2009-01-30 01:41:46'),(3664,'udsExtract.reportSetup.customDateStart','en','lava','crms','udsExtract','reportSetup','customDateStart',NULL,'i','date','No','Start Date',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'2009-01-30 01:41:46'),(3665,'udsExtract.reportSetup.format','en','lava','crms','udsExtract','reportSetup','format',NULL,'i','range','No','Format',NULL,NULL,10,0,NULL,'reportSetup.formatCsv',NULL,NULL,NULL,'2009-01-30 01:41:46'),(3666,'filter.reportSetup.patientId','en','lava','crms','filter','reportSetup','patientId',NULL,'i','numeric','No','Patient ID',NULL,NULL,5,0,NULL,NULL,NULL,NULL,NULL,'2009-01-30 01:41:46'),(3667,'*.task.id','en','lava','crms',NULL,'task','id','details','c','numeric','No','TaskID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'The unique ID of the task record','2009-01-24 21:28:51'),(3668,'*.task.patient_fullNameRev','en','lava','crms',NULL,'task','patient_fullNameRev','details','c','string','No','Patient',NULL,NULL,NULL,0,NULL,NULL,NULL,2,'the ID of the patient','2009-01-24 21:28:51'),(3669,'*.task.projName','en','lava','crms',NULL,'task','projName','details','i','suggest','No','Project',NULL,75,NULL,0,NULL,'enrollmentStatus.patientProjects',NULL,3,'The related project of the task (optional)','2009-01-24 21:28:51'),(3670,'filter.task.projName','en','lava','crms','filter','task','projName',NULL,'i','string','No','Project',NULL,75,NULL,0,NULL,NULL,NULL,3,'The related project of the task (optional)','2009-01-24 21:28:51'),(3671,'*.task.openedDate','en','lava','crms',NULL,'task','openedDate','details','i','date','No','Opened Date',NULL,NULL,NULL,0,NULL,NULL,NULL,4,'Date the task was opened','2009-01-24 21:28:51'),(3672,'filter.task.openedDateEnd','en','lava','crms','filter','task','openedDateEnd',NULL,'i','date','No','and',NULL,NULL,NULL,0,NULL,NULL,NULL,4,'Date the task was opened','2009-01-24 21:28:51'),(3673,'filter.task.openedDateStart','en','lava','crms','filter','task','openedDateStart',NULL,'i','date','No','Opened Date Between',NULL,NULL,NULL,0,NULL,NULL,NULL,4,'Date the task was opened','2009-01-24 21:28:51'),(3674,'*.task.openedBy','en','lava','crms',NULL,'task','openedBy','details','i','suggest','No','Opened By',NULL,25,NULL,0,NULL,'project.staffList',NULL,5,'Who opened the task','2009-01-24 21:28:51'),(3675,'filter.task.openedBy','en','lava','crms','filter','task','openedBy',NULL,'i','string','No','Opened By',NULL,25,NULL,0,NULL,NULL,NULL,5,'Who opened the task','2009-01-24 21:28:51'),(3676,'*.task.taskType','en','lava','crms',NULL,'task','taskType','details','i','suggest','No','Task Type',NULL,25,NULL,0,NULL,'task.taskType',NULL,6,'The type of task','2009-01-24 21:28:51'),(3677,'filter.task.taskType','en','lava','crms','filter','task','taskType',NULL,'i','suggest','No','Task Type',NULL,25,NULL,0,NULL,'task.taskType',NULL,6,'The type of task','2009-01-24 21:28:51'),(3678,'*.task.taskDesc','en','lava','crms',NULL,'task','taskDesc','details','i','text','No','Task Description',NULL,255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,7,'Description of the task','2009-01-24 21:28:51'),(3679,'filter.task.taskDesc','en','lava','crms','filter','task','taskDesc',NULL,'i','string','No','Task Description',NULL,255,NULL,0,NULL,NULL,NULL,7,'Description of the task','2009-01-24 21:28:51'),(3680,'*.task.dueDate','en','lava','crms',NULL,'task','dueDate','details','i','date','No','Due Date',NULL,NULL,NULL,0,NULL,NULL,NULL,8,'When the task is due','2009-01-24 21:28:51'),(3681,'filter.task.dueDateEnd','en','lava','crms','filter','task','dueDateEnd',NULL,'i','date','No','and',NULL,NULL,NULL,0,NULL,NULL,NULL,8,'When the task is due','2009-01-24 21:28:51'),(3682,'filter.task.dueDateStart','en','lava','crms','filter','task','dueDateStart',NULL,'i','date','No','Due Date Between',NULL,NULL,NULL,0,NULL,NULL,NULL,8,'When the task is due','2009-01-24 21:28:51'),(3683,'*.task.taskStatus','en','lava','crms',NULL,'task','taskStatus','details','i','suggest','No','Task Status',NULL,50,NULL,0,NULL,'task.taskStatus',NULL,9,'The task status','2009-01-24 21:28:51'),(3684,'filter.task.taskStatus','en','lava','crms','filter','task','taskStatus',NULL,'i','suggest','No','Task Status',NULL,50,NULL,0,NULL,'task.taskStatus',NULL,9,'The task status','2009-01-24 21:28:51'),(3685,'*.task.assignedTo','en','lava','crms',NULL,'task','assignedTo','details','i','range','No','Assigned To',NULL,25,NULL,0,NULL,'project.staffList',NULL,10,'Who the task is assigned to','2009-01-24 21:28:51'),(3686,'filter.task.assignedTo','en','lava','crms','filter','task','assignedTo',NULL,'i','string','No','Assigned To',NULL,25,NULL,0,NULL,NULL,NULL,10,'Who the task is assigned to','2009-01-24 21:28:51'),(3687,'*.task.workingNotes','en','lava','crms',NULL,'task','workingNotes','details','i','text','No','Working Notes',NULL,255,NULL,0,'rows=\"4\" cols=\"40\"',NULL,NULL,11,'Notes about the task','2009-01-24 21:28:51'),(3688,'filter.task.workingNotes','en','lava','crms','filter','task','workingNotes',NULL,'i','string','No','Working Notes',NULL,255,NULL,0,NULL,NULL,NULL,11,'Notes about the task','2009-01-24 21:28:51'),(3689,'*.task.closedDate','en','lava','crms',NULL,'task','closedDate','details','i','date','No','Closed Date',NULL,NULL,NULL,0,NULL,NULL,NULL,12,'When the task was completed or closed','2009-01-24 21:28:51'),(3690,'filter.task.closedDateEnd','en','lava','crms','filter','task','closedDateEnd',NULL,'i','date','No','and',NULL,NULL,NULL,0,NULL,NULL,NULL,12,'When the task was completed or closed','2009-01-24 21:28:51'),(3691,'filter.task.closedDateStart','en','lava','crms','filter','task','closedDateStart',NULL,'i','date','No','Closed Date Between',NULL,NULL,NULL,0,NULL,NULL,NULL,12,'When the task was completed or closed','2009-01-24 21:28:51'),(3692,'filter.task.patient.firstName','en','lava','crms','filter','task.patient','firstName',NULL,'i','string','No','Patient',NULL,NULL,NULL,0,NULL,NULL,NULL,2,'the ID of the patient','2009-01-24 21:28:51'),(3693,'filter.task.patient.lastName','en','lava','crms','filter','task.patient','lastName',NULL,'i','string','No','Patient',NULL,NULL,NULL,0,NULL,NULL,NULL,2,'the ID of the patient','2009-01-24 21:28:51'),(3694,'filter.visit.customDateEnd','en','lava','crms','filter','visit','customDateEnd','','i','date','No','      and',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3695,'filter.visit.customDateStart','en','lava','crms','filter','visit','customDateStart','','i','date','No','Date is between',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3696,'filter.visit.patient.firstName','en','lava','crms','filter','visit','patient.firstName','','i','string','No','First Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3697,'filter.visit.patient.lastName','en','lava','crms','filter','visit','patient.lastName','','i','string','No','Last Name',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3698,'filter.visit.projName','en','lava','crms','filter','visit','projName','','i','string','No','Project',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3699,'filter.visit.visitLocation','en','lava','crms','filter','visit','visitLocation','','i','suggest','No','Location',NULL,NULL,NULL,0,'','visit.locations',NULL,NULL,'','2009-01-24 21:28:51'),(3700,'filter.visit.visitType','en','lava','crms','filter','visit','visitType','','i','string','No','Type',NULL,NULL,NULL,0,'','',NULL,NULL,'','2009-01-24 21:28:51'),(3701,'*.visit.id','en','lava','crms',NULL,'visit','id','','c','range','Yes','Visit ID',NULL,NULL,40,0,'','visit.patientVisits',NULL,1,'','2009-01-24 21:28:51'),(3702,'*.visit.projName','en','lava','crms',NULL,'visit','projName','','i','range','Yes','Project',NULL,NULL,NULL,0,'','enrollmentStatus.patientProjects',NULL,3,'','2009-01-24 21:28:51'),(3703,'*.visit.visitLocation','en','lava','crms',NULL,'visit','visitLocation','','i','suggest','Yes','Location',NULL,NULL,NULL,0,'','visit.visitLocations',NULL,4,'','2009-01-24 21:28:51'),(3704,'*.visit.visitType','en','lava','crms',NULL,'visit','visitType','','i','range','Yes','Visit Type',NULL,NULL,NULL,0,'','visit.visitTypes',NULL,5,'','2009-01-24 21:28:51'),(3705,'*.visit.visitWith','en','lava','crms',NULL,'visit','visitWith','','i','suggest','Yes','Appt With',NULL,NULL,NULL,0,'','project.staffList',NULL,6,'','2009-01-24 21:28:51'),(3706,'filter.visit.visitWith','en','lava','crms','filter','visit','visitWith','','i','suggest','No','Appt With',NULL,NULL,NULL,0,'','project.staffList',NULL,6,'','2009-01-24 21:28:51'),(3707,'*.visit.visitDate','en','lava','crms',NULL,'visit','visitDate','','i','date','Yes','Visit Date',NULL,NULL,10,0,'','',NULL,7,'','2009-01-24 21:28:51'),(3708,'*.visit.visitStatus','en','lava','crms',NULL,'visit','visitStatus','','i','range','Yes','Status',NULL,NULL,NULL,0,'','visit.status',NULL,8,'','2009-01-24 21:28:51'),(3709,'filter.visit.visitStatus','en','lava','crms','filter','visit','visitStatus','','i','range','Yes','Status',NULL,NULL,NULL,0,'','visit.status',NULL,8,'','2009-01-24 21:28:51'),(3710,'*.visit.visitTime','en','lava','crms','','visit','visitTime','','i','time','No','Time',NULL,NULL,NULL,0,NULL,NULL,NULL,8,'Time of the visit','2009-04-29 14:00:00'),(3711,'*.visit.visitNote','en','lava','crms',NULL,'visit','visitNote','','i','text','No','Visit Notes',NULL,NULL,NULL,0,'rows=\"5\" cols=\"35\"','',NULL,9,'','2009-01-24 21:28:51'),(3712,'*.visit.visitDescrip','en','lava','crms',NULL,'visit','visitDescrip','','c','string','No','Description',NULL,NULL,NULL,0,'','',NULL,16,'','2009-01-24 21:28:51'),(3713,'*.visit.ageAtVisit','en','lava','crms',NULL,'visit','ageAtVisit','','c','numeric','No','Age At Visit',NULL,NULL,NULL,0,'','',NULL,17,'','2009-01-24 21:28:51');
/*!40000 ALTER TABLE `viewproperty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visit`
--

DROP TABLE IF EXISTS `visit`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `visit` (
  `VID` int(10) NOT NULL auto_increment,
  `PIDN` int(10) NOT NULL,
  `ProjName` varchar(75) NOT NULL,
  `VLocation` varchar(25) NOT NULL,
  `VType` varchar(25) NOT NULL,
  `VWith` varchar(25) default NULL,
  `VDate` date NOT NULL,
  `VTime` time default NULL,
  `VStatus` varchar(25) NOT NULL,
  `VNotes` varchar(255) default NULL,
  `FUMonth` char(3) default NULL,
  `FUYear` char(4) default NULL,
  `FUNote` varchar(100) default NULL,
  `WList` varchar(25) default NULL,
  `WListNote` varchar(100) default NULL,
  `WListDate` datetime default NULL,
  `VShortDesc` varchar(255) default NULL,
  `AgeAtVisit` smallint(5) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`VID`),
  KEY `visit__PIDN` (`PIDN`),
  KEY `visit__ProjName` (`ProjName`),
  KEY `visit__PIDN_PROJNAME` (`PIDN`,`ProjName`),
  KEY `visit__authfilter` (`PIDN`,`ProjName`,`VID`),
  KEY `visit__date` (`VID`,`VDate`,`VTime`),
  CONSTRAINT `visit__PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `visit__PIDN_PROJNAME` FOREIGN KEY (`PIDN`, `ProjName`) REFERENCES `enrollmentstatus` (`PIDN`, `ProjName`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `visit__ProjName` FOREIGN KEY (`ProjName`) REFERENCES `projectunit` (`ProjUnitDesc`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

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
/*!50001 CREATE TABLE `vwrptprojectpatientstatus` (
  `PIDN` int(11),
  `FullNameRev` varchar(100),
  `AGE` double(17,0),
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
) */;

--
-- Temporary table structure for view `vwrptprojectvisitlist`
--

DROP TABLE IF EXISTS `vwrptprojectvisitlist`;
/*!50001 DROP VIEW IF EXISTS `vwrptprojectvisitlist`*/;
/*!50001 CREATE TABLE `vwrptprojectvisitlist` (
  `PIDN` int(10),
  `FullNameRev` varchar(100),
  `TransLanguage` varchar(25),
  `Gender` tinyint(3),
  `AGE` double(17,0),
  `VLocation` varchar(25),
  `VType` varchar(25),
  `VWith` varchar(25),
  `VDate` date,
  `VStatus` varchar(25),
  `ProjName` varchar(75),
  `VNotes` varchar(255),
  `VDateNoTime` date
) */;

--
-- Dumping routines for database 'lava_crms'
--
DELIMITER ;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_after_set_linkdata` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_after_set_linkdata`(user_name varchar(50), host_name varchar(25),method VARCHAR(25))
BEGINIF method = 'VISIT' THEN  UPDATE temp_linkdata1, visit   SET temp_linkdata1.pidn = visit.PIDN, temp_linkdata1.link_date = visit.VDATE, temp_linkdata1.link_type = method   WHERE temp_linkdata1.link_id = visit.VID;ELSEIF method = 'INSTRUMENT' THEN  UPDATE temp_linkdata1, instrumenttracking   SET temp_linkdata1.pidn = instrumenttracking.PIDN, temp_linkdata1.link_date = instrumenttracking.DCDATE, temp_linkdata1.link_type = method   WHERE temp_linkdata1.link_id = instrumenttracking.InstrID;ELSE  UPDATE temp_linkdata1 SET link_type = method;END IF;CREATE TEMPORARY TABLE temp_linkdata (  pidn INTEGER NOT NULL,  link_date DATE NOT NULL,  link_id INTEGER NOT NULL,  link_type VARCHAR(50) NOT NULL);IF method = 'PIDN_DATE' THEN  INSERT INTO temp_linkdata(pidn,link_date,link_id,link_type)   SELECT pidn,link_date, min(link_id), link_type FROM temp_linkdata1 GROUP BY pidn,link_date,link_type;ELSE  INSERT INTO temp_linkdata(pidn,link_date,link_id,link_type)   SELECT pidn,link_date, link_id, link_type FROM temp_linkdata1 GROUP BY pidn,link_date,link_id,link_type;END IF;ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_after_set_pidns` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_after_set_pidns`(user_name varchar(50), host_name varchar(25))
BEGINCREATE TEMPORARY TABLE temp_pidn AS SELECT pidn FROM temp_pidn1 GROUP BY pidn;END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_audit_event` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_audit_event`(user_name varchar(50),host_name varchar(25),lq_query_object varchar(100),lq_query_type varchar(25))
BEGININSERT INTO audit_event_work (`audit_user`,`audit_host`,`audit_timestamp`,`action`,`action_event`)    VALUES(user_name,host_name,NOW(),CONCAT('lava.*.query.',lq_query_object),lq_query_type);  END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_check_user_auth` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_check_user_auth`(user_login varchar(50),host_name varchar(25))
BEGINDECLARE user_id int;SELECT `UID` into user_id from `authuser` where `Login` = user_login;IF(user_id > 0) THEN  SELECT  1 as user_auth;ELSE  SELECT 0 as user_auth;END IF;END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_check_version` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_check_version`(pModule varchar(25), pMajor integer, pMinor integer, pFix integer)
BEGINDECLARE CurMajor integer;DECLARE CurMinor integer;DECLARE CurFix integer;DECLARE CurVersion varchar(10);DECLARE pVersion varchar(10);DECLARE version_msg varchar(500);DECLARE UpdateNeeded integer;SELECT MAX(`Major`) into CurMajor from `versionhistory` WHERE `Module` = pModule ;SELECT MAX(`Minor`) into CurMinor from `versionhistory` WHERE `Module` = pModule and `Major` = CurMajor;SELECT MAX(`Fix`) into CurFix from `versionhistory` WHERE `Module` = pModule and `Major` = CurMajor and `Minor` = CurMinor;SET pVersion = CONCAT(cast(pMajor as CHAR),'.',cast(pMinor as  CHAR),'.',cast(pFix as CHAR));SELECT MAX(`Version`) into CurVersion FROM `versionhistory` WHERE `Module` = pModule and `Major` = CurMajor and `Minor` = CurMinor and `Fix` = CurFix;SET version_msg = 'OK';IF (CurVersion IS NULL) THEN  SET version_msg = CONCAT('Invalid Module ',Module,' passed to stored procedure lq_check_version.');ELSEIF (pMajor < CurMajor) THEN	SET version_msg = CONCAT('Your application version (',pVersion,') is out of date.  The current version is (',CurVersion,'). You must upgrade your application.');ELSEIF (pMajor > CurMajor) THEN	SET version_msg = CONCAT('Your application version (',pVersion,') is more recent than the current version supported by the database.  The current version is (',CurVersion,'). You must downgrade your application.');ELSEIF (pMinor < CurMinor) THEN  SET version_msg = CONCAT('Your application version (',pVersion,') is out of date.  The current version is (',CurVersion,'). You must upgrade your application.');ELSEIF (pMinor > CurMinor) THEN	SET version_msg = CONCAT('Your application version (',pVersion,') is more recent than the current version supported by the database.  The current version is (',CurVersion,'). You must downgrade your application.');ELSEIF (pFix < CurFix) THEN  SELECT count(*) into UpdateNeeded from `versionhistory` WHERE `Module`=  pModule and `Major` >= pMajor and `Minor` >= pMinor and `Fix` > pFix  and `UpdateRequired` = 1;  IF(UpdateNeeded > 0) THEN    SET version_msg = CONCAT('Your application version (',pVersion,') is out of date.  The current version is (',CurVersion,'). You must upgrade your application.');  ELSE     SET version_msg = CONCAT('Your application version (',pVersion,') is slightly out of date.  The current version is (',CurVersion,'). You should upgrade to the current version soon.');  END IF;ELSEIF (pFix > CurFix) THEN	SET version_msg = CONCAT('Your application version (',pVersion,') is more recent than the current version supported by the database.  The current version is (',CurVersion,'). You must downgrade your application.');END IF;select version_msg as 'version_msg';END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_clear_linkdata` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_clear_linkdata`(user_name varchar(50), host_name varchar(25))
BEGINDROP TABLE IF EXISTS temp_linkdata;DROP TABLE IF EXISTS temp_linkdata1;END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_clear_pidns` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_clear_pidns`(user_name varchar(50), host_name varchar(25))
BEGINDROP TABLE IF EXISTS temp_pidn;DROP TABLE IF EXISTS temp_pidn1;END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_get_all_pidns` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_get_all_pidns`(user_name varchar(50), host_name varchar(25))
BEGINSELECT pidn from patient order by pidn;END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_get_assessment_instruments` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_get_assessment_instruments`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGINCALL lq_audit_event(user_name,host_name,'crms.assessment.instruments',query_type);	IF query_type = 'Simple' THEN	SELECT p.pidn, i.* FROM lq_view_instruments i 		INNER JOIN temp_pidn p ON (p.PIDN = i.PIDN_Instrument)       ORDER BY p.pidn, i.DCDate,i.InstrType;ELSEIF query_type = 'SimpleAllPatients' THEN	SELECT p.pidn, i.*  FROM lq_view_instruments i 		RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = i.PIDN_Instrument)       ORDER BY p.pidn, i.DCDate,i.InstrType;END IF;END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_get_enrollment_status` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_get_enrollment_status`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGINCALL lq_audit_event(user_name,host_name,'crms.enrollment.status',query_type);IF query_type = 'Simple' THEN	SELECT p.pidn, e.*  FROM lq_view_enrollment e 		INNER JOIN temp_pidn p ON (p.PIDN = e.PIDN_Enrollment)       ORDER BY p.pidn, e.latestDate;ELSEIF query_type = 'SimpleAllPatients' THEN	SELECT p.pidn, e.* FROM lq_view_enrollment e 		RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = e.PIDN_Enrollment)       ORDER BY p.pidn, e.latestDate;END IF;END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_get_linkdata` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_get_linkdata`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
    SQL SECURITY INVOKER
BEGINIF query_type = 'VISIT' THEN	SELECT l.*, '---->' as `Visit Details`, v.* from temp_linkdata l inner join visit v on l.link_id=v.VID	ORDER BY l.pidn, l.link_date,l.link_id;ELSEIF query_type = 'INSTRUMENT' THEN 	SELECT l.*, '---->' as `Instrument Details`, i.* from temp_linkdata l inner join instrumenttracking i on l.link_id=i.InstrID	ORDER BY l.pidn, l.link_date,l.link_id;ELSE   SELECT * from temp_linkdata l  	ORDER BY l.pidn, l.link_date,l.link_id;END IF;END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_get_objects` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_get_objects`(user_name varchar(50), host_name varchar(25))
BEGINSELECT concat(`instance`,'_',`scope`,'_',`module`) as query_source,concat(`section`,'_',`target`) as query_object_name , `short_desc`, `standard`,`primary_link`,`secondary_link` from `query_objects`;END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_get_patient_demographics` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_get_patient_demographics`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGINCALL lq_audit_event(user_name,host_name,'crms.patient.demographics',query_type);	IF query_type = 'Simple' THEN	SELECT p.pidn, d.*  FROM lq_view_demographics d 		INNER JOIN temp_pidn p ON (p.PIDN = d.PIDN_Demographics)       ORDER BY p.pidn;ELSEIF query_type = 'SimpleAllPatients' THEN	SELECT p.pidn, d.*   FROM lq_view_demographics d  		 RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = d.PIDN_Demographics)       ORDER BY p.pidn;ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN 	 SELECT l.pidn, l.link_date,l.link_id,d.*  	 FROM temp_linkdata l INNER JOIN lq_view_demographics d ON (d.PIDN_Demographics=l.PIDN);END IF;END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_get_scheduling_visits` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_get_scheduling_visits`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGINCALL lq_audit_event(user_name,host_name,'crms.scheduling.visits',query_type);	IF query_type = 'Simple' THEN	SELECT p.pidn, v.*  FROM lq_view_visit v 		INNER JOIN temp_pidn p ON (p.PIDN = v.PIDN_visit)       ORDER BY p.pidn, v.vdate, v.vtype;ELSEIF query_type = 'SimpleAllPatients' THEN	SELECT p.pidn, v.*  FROM lq_view_visit v  		 RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = v.PIDN_visit)       ORDER BY p.pidn,v.vdate, v.vtype;END IF;END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_set_linkdata` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_set_linkdata`(user_name varchar(50), host_name varchar(25))
BEGINDROP TABLE IF EXISTS temp_linkdata1;DROP TABLE IF EXISTS temp_linkdata;CREATE TEMPORARY TABLE temp_linkdata1(    pidn INTEGER NOT NULL,    link_date DATE NOT NULL,    link_id INTEGER NOT NULL,    link_type varchar(25) DEFAULT NULL);END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_set_linkdata_row` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_set_linkdata_row`(user_name varchar(50), host_name varchar(25),pidn integer,link_date date, link_id integer)
BEGININSERT INTO `temp_linkdata1` (`pidn`,`link_date`,`link_id`) VALUES(pidn,link_date,link_id);END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_set_pidns` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_set_pidns`(user_name varchar(50), host_name varchar(25))
BEGINDROP TABLE IF EXISTS temp_pidn1;DROP TABLE IF EXISTS temp_pidn;CREATE TEMPORARY TABLE temp_pidn1(    pidn INTEGER NOT NULL);END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `lq_set_pidns_row` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_set_pidns_row`(user_name varchar(50), host_name varchar(25),pidn integer)
BEGININSERT INTO `temp_pidn1` (`pidn`) values (pidn);END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
DELIMITER ;

--
-- Final view structure for view `audit_entity`
--

/*!50001 DROP TABLE `audit_entity`*/;
/*!50001 DROP VIEW IF EXISTS `audit_entity`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `audit_entity` AS select `audit_entity_work`.`audit_entity_id` AS `audit_entity_id`,`audit_entity_work`.`audit_event_id` AS `audit_event_id`,`audit_entity_work`.`entity_id` AS `entity_id`,`audit_entity_work`.`entity` AS `entity`,`audit_entity_work`.`entity_type` AS `entity_type`,`audit_entity_work`.`audit_type` AS `audit_type`,`audit_entity_work`.`modified` AS `modified` from `audit_entity_work` union all select `audit_entity_history`.`audit_entity_id` AS `audit_entity_id`,`audit_entity_history`.`audit_event_id` AS `audit_event_id`,`audit_entity_history`.`entity_id` AS `entity_id`,`audit_entity_history`.`entity` AS `entity`,`audit_entity_history`.`entity_type` AS `entity_type`,`audit_entity_history`.`audit_type` AS `audit_type`,`audit_entity_history`.`modified` AS `modified` from `audit_entity_history` */;

--
-- Final view structure for view `audit_event`
--

/*!50001 DROP TABLE `audit_event`*/;
/*!50001 DROP VIEW IF EXISTS `audit_event`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `audit_event` AS select `audit_event_work`.`audit_event_id` AS `audit_event_id`,`audit_event_work`.`audit_user` AS `audit_user`,`audit_event_work`.`audit_host` AS `audit_host`,`audit_event_work`.`audit_timestamp` AS `audit_timestamp`,`audit_event_work`.`action` AS `action`,`audit_event_work`.`action_event` AS `action_event`,`audit_event_work`.`action_id_param` AS `action_id_param`,`audit_event_work`.`event_note` AS `event_note`,`audit_event_work`.`exception` AS `exception`,`audit_event_work`.`exception_message` AS `exception_message`,`audit_event_work`.`modified` AS `modified` from `audit_event_work` union all select `audit_event_history`.`audit_event_id` AS `audit_event_id`,`audit_event_history`.`audit_user` AS `audit_user`,`audit_event_history`.`audit_host` AS `audit_host`,`audit_event_history`.`audit_timestamp` AS `audit_timestamp`,`audit_event_history`.`action` AS `action`,`audit_event_history`.`action_event` AS `action_event`,`audit_event_history`.`action_id_param` AS `action_id_param`,`audit_event_history`.`event_note` AS `event_note`,`audit_event_history`.`exception` AS `exception`,`audit_event_history`.`exception_message` AS `exception_message`,`audit_event_history`.`modified` AS `modified` from `audit_event_history` */;

--
-- Final view structure for view `audit_property`
--

/*!50001 DROP TABLE `audit_property`*/;
/*!50001 DROP VIEW IF EXISTS `audit_property`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `audit_property` AS select `audit_property_work`.`audit_property_id` AS `audit_property_id`,`audit_property_work`.`audit_entity_id` AS `audit_entity_id`,`audit_property_work`.`property` AS `property`,`audit_property_work`.`index_key` AS `index_key`,`audit_property_work`.`subproperty` AS `subproperty`,`audit_property_work`.`old_value` AS `old_value`,`audit_property_work`.`new_value` AS `new_value`,`audit_property_work`.`audit_timestamp` AS `audit_timestamp`,`audit_property_work`.`modified` AS `modified` from `audit_property_work` union all select `audit_property_history`.`audit_property_id` AS `audit_property_id`,`audit_property_history`.`audit_entity_id` AS `audit_entity_id`,`audit_property_history`.`property` AS `property`,`audit_property_history`.`index_key` AS `index_key`,`audit_property_history`.`subproperty` AS `subproperty`,`audit_property_history`.`old_value` AS `old_value`,`audit_property_history`.`new_value` AS `new_value`,`audit_property_history`.`audit_timestamp` AS `audit_timestamp`,`audit_property_history`.`modified` AS `modified` from `audit_property_history` */;

--
-- Final view structure for view `audit_text`
--

/*!50001 DROP TABLE `audit_text`*/;
/*!50001 DROP VIEW IF EXISTS `audit_text`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `audit_text` AS select `audit_text_history`.`audit_property_id` AS `audit_property_id`,`audit_text_history`.`old_text` AS `old_text`,`audit_text_history`.`new_text` AS `new_text` from `audit_text_history` union all select `audit_text_work`.`audit_property_id` AS `audit_property_id`,`audit_text_work`.`old_text` AS `old_text`,`audit_text_work`.`new_text` AS `new_text` from `audit_text_work` */;

--
-- Final view structure for view `lq_view_demographics`
--

/*!50001 DROP TABLE `lq_view_demographics`*/;
/*!50001 DROP VIEW IF EXISTS `lq_view_demographics`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `lq_view_demographics` AS select `patient`.`PIDN` AS `PIDN_demographics`,`patient`.`DOB` AS `DOB`,`patient_age`.`AGE` AS `AGE`,`patient`.`Gender` AS `Gender`,`patient`.`Hand` AS `Hand`,`patient`.`Deceased` AS `Deceased`,`patient`.`DOD` AS `DOD`,`patient`.`PrimaryLanguage` AS `PrimaryLanguage`,`patient`.`TestingLanguage` AS `TestingLanguage`,`patient`.`TransNeeded` AS `TransNeeded`,`patient`.`TransLanguage` AS `TransLanguage` from (`patient` join `patient_age` on((`patient`.`PIDN` = `patient_age`.`PIDN`))) where (`patient`.`PIDN` > 0) */;

--
-- Final view structure for view `lq_view_enrollment`
--

/*!50001 DROP TABLE `lq_view_enrollment`*/;
/*!50001 DROP VIEW IF EXISTS `lq_view_enrollment`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `lq_view_enrollment` AS select `enrollmentstatus`.`EnrollStatID` AS `EnrollStatID`,`enrollmentstatus`.`PIDN` AS `PIDN_Enrollment`,`enrollmentstatus`.`ProjName` AS `ProjName`,`enrollmentstatus`.`SubjectStudyID` AS `SubjectStudyID`,`enrollmentstatus`.`ReferralSource` AS `ReferralSource`,`enrollmentstatus`.`LatestDesc` AS `LatestDesc`,`enrollmentstatus`.`LatestDate` AS `LatestDate`,`enrollmentstatus`.`LatestNote` AS `LatestNote`,`enrollmentstatus`.`ReferredDesc` AS `ReferredDesc`,`enrollmentstatus`.`ReferredDate` AS `ReferredDate`,`enrollmentstatus`.`ReferredNote` AS `ReferredNote`,`enrollmentstatus`.`DeferredDesc` AS `DeferredDesc`,`enrollmentstatus`.`DeferredDate` AS `DeferredDate`,`enrollmentstatus`.`DeferredNote` AS `DeferredNote`,`enrollmentstatus`.`EligibleDesc` AS `EligibleDesc`,`enrollmentstatus`.`EligibleDate` AS `EligibleDate`,`enrollmentstatus`.`EligibleNote` AS `EligibleNote`,`enrollmentstatus`.`IneligibleDesc` AS `IneligibleDesc`,`enrollmentstatus`.`IneligibleDate` AS `IneligibleDate`,`enrollmentstatus`.`IneligibleNote` AS `IneligibleNote`,`enrollmentstatus`.`DeclinedDesc` AS `DeclinedDesc`,`enrollmentstatus`.`DeclinedDate` AS `DeclinedDate`,`enrollmentstatus`.`DeclinedNote` AS `DeclinedNote`,`enrollmentstatus`.`EnrolledDesc` AS `EnrolledDesc`,`enrollmentstatus`.`EnrolledDate` AS `EnrolledDate`,`enrollmentstatus`.`EnrolledNote` AS `EnrolledNote`,`enrollmentstatus`.`ExcludedDesc` AS `ExcludedDesc`,`enrollmentstatus`.`ExcludedDate` AS `ExcludedDate`,`enrollmentstatus`.`ExcludedNote` AS `ExcludedNote`,`enrollmentstatus`.`WithdrewDesc` AS `WithdrewDesc`,`enrollmentstatus`.`WithdrewDate` AS `WithdrewDate`,`enrollmentstatus`.`WithdrewNote` AS `WithdrewNote`,`enrollmentstatus`.`InactiveDesc` AS `InactiveDesc`,`enrollmentstatus`.`InactiveDate` AS `InactiveDate`,`enrollmentstatus`.`InactiveNote` AS `InactiveNote`,`enrollmentstatus`.`DeceasedDesc` AS `DeceasedDesc`,`enrollmentstatus`.`DeceasedDate` AS `DeceasedDate`,`enrollmentstatus`.`DeceasedNote` AS `DeceasedNote`,`enrollmentstatus`.`AutopsyDesc` AS `AutopsyDesc`,`enrollmentstatus`.`AutopsyDate` AS `AutopsyDate`,`enrollmentstatus`.`AutopsyNote` AS `AutopsyNote`,`enrollmentstatus`.`ClosedDesc` AS `ClosedDesc`,`enrollmentstatus`.`ClosedDate` AS `ClosedDate`,`enrollmentstatus`.`ClosedNote` AS `ClosedNote`,`enrollmentstatus`.`EnrollmentNotes` AS `EnrollmentNotes`,`enrollmentstatus`.`modified` AS `modified` from `enrollmentstatus` where (`enrollmentstatus`.`EnrollStatID` > 0) */;

--
-- Final view structure for view `lq_view_instruments`
--

/*!50001 DROP TABLE `lq_view_instruments`*/;
/*!50001 DROP VIEW IF EXISTS `lq_view_instruments`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `lq_view_instruments` AS select `i`.`InstrID` AS `InstrID`,`i`.`VID` AS `VID`,`i`.`ProjName` AS `ProjName`,`i`.`PIDN` AS `PIDN_Instrument`,`i`.`InstrType` AS `InstrType`,`i`.`InstrVer` AS `InstrVer`,`i`.`DCDate` AS `DCDate`,`i`.`DCBy` AS `DCBy`,`i`.`DCStatus` AS `DCStatus`,`i`.`DCNotes` AS `DCNotes`,`i`.`ResearchStatus` AS `ResearchStatus`,`i`.`QualityIssue` AS `QualityIssue`,`i`.`QualityIssue2` AS `QualityIssue2`,`i`.`QualityIssue3` AS `QualityIssue3`,`i`.`QualityNotes` AS `QualityNotes`,`i`.`DEDate` AS `DEDate`,`i`.`DEBy` AS `DEBy`,`i`.`DEStatus` AS `DEStatus`,`i`.`DENotes` AS `DENotes`,`i`.`DVDate` AS `DVDate`,`i`.`DVBy` AS `DVBy`,`i`.`DVStatus` AS `DVStatus`,`i`.`DVNotes` AS `DVNotes`,`i`.`latestflag` AS `latestflag`,`i`.`FieldStatus` AS `FieldStatus`,`i`.`AgeAtDC` AS `AgeAtDC`,`i`.`modified` AS `modified`,`s`.`Summary` AS `summary` from (`instrumenttracking` `i` join `instrumentsummary` `s` on((`i`.`InstrID` = `s`.`InstrID`))) where (`i`.`InstrID` > 0) */;

--
-- Final view structure for view `lq_view_visit`
--

/*!50001 DROP TABLE `lq_view_visit`*/;
/*!50001 DROP VIEW IF EXISTS `lq_view_visit`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `lq_view_visit` AS select `visit`.`VID` AS `VID`,`visit`.`PIDN` AS `PIDN_Visit`,`visit`.`ProjName` AS `ProjName`,`visit`.`VLocation` AS `VLocation`,`visit`.`VType` AS `VType`,`visit`.`VWith` AS `VWith`,`visit`.`VDate` AS `VDate`,`visit`.`VTime` AS `VTime`,`visit`.`VStatus` AS `VStatus`,`visit`.`VNotes` AS `VNotes`,`visit`.`FUMonth` AS `FUMonth`,`visit`.`FUYear` AS `FUYear`,`visit`.`FUNote` AS `FUNote`,`visit`.`WList` AS `WList`,`visit`.`WListNote` AS `WListNote`,`visit`.`WListDate` AS `WListDate`,`visit`.`VShortDesc` AS `VShortDesc`,`visit`.`AgeAtVisit` AS `AgeAtVisit`,`visit`.`modified` AS `modified` from `visit` where (`visit`.`VID` > 0) */;

--
-- Final view structure for view `patient_age`
--

/*!50001 DROP TABLE `patient_age`*/;
/*!50001 DROP VIEW IF EXISTS `patient_age`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `patient_age` AS select `patient`.`PIDN` AS `PIDN`,((if(isnull(date_format(`patient`.`DOD`,_utf8'%Y')),date_format(now(),_utf8'%Y'),date_format(`patient`.`DOD`,_utf8'%Y')) - date_format(`patient`.`DOB`,_utf8'%Y')) - (if(isnull(date_format(`patient`.`DOD`,_utf8'00-%m-%d')),date_format(now(),_utf8'00-%m-%d'),date_format(`patient`.`DOD`,_utf8'00-%m-%d')) < date_format(`patient`.`DOB`,_utf8'00-%m-%d'))) AS `AGE` from `patient` */;

--
-- Final view structure for view `vwrptprojectpatientstatus`
--

/*!50001 DROP TABLE `vwrptprojectpatientstatus`*/;
/*!50001 DROP VIEW IF EXISTS `vwrptprojectpatientstatus`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vwrptprojectpatientstatus` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`pa`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `StatusDate`,`lps`.`LatestDesc` AS `Status`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`Project` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,_utf8'OVERALL' AS `Unit`,1 AS `UnitOrder` from (((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `patient_age` `pa` on((`p`.`PIDN` = `pa`.`PIDN`))) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) union select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`pa`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `LatestDate`,`lps`.`LatestDesc` AS `LatestDesc`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`ProjUnitDesc` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,`pu`.`Unit` AS `Unit`,2 AS `UnitOrder` from (((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `patient_age` `pa` on((`p`.`PIDN` = `pa`.`PIDN`))) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) where (`pu`.`Unit` is not null) */;

--
-- Final view structure for view `vwrptprojectvisitlist`
--

/*!50001 DROP TABLE `vwrptprojectvisitlist`*/;
/*!50001 DROP VIEW IF EXISTS `vwrptprojectvisitlist`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vwrptprojectvisitlist` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`TransLanguage` AS `TransLanguage`,`p`.`Gender` AS `Gender`,`pa`.`AGE` AS `AGE`,`v`.`VLocation` AS `VLocation`,`v`.`VType` AS `VType`,`v`.`VWith` AS `VWith`,`v`.`VDate` AS `VDate`,`v`.`VStatus` AS `VStatus`,`v`.`ProjName` AS `ProjName`,`v`.`VNotes` AS `VNotes`,cast(`v`.`VDate` as date) AS `VDateNoTime` from ((`patient` `p` join `visit` `v` on((`p`.`PIDN` = `v`.`PIDN`))) join `patient_age` `pa` on((`p`.`PIDN` = `pa`.`PIDN`))) where (not((`v`.`VStatus` like _latin1'%CANC%'))) */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-27 17:08:32
