-- MySQL dump 10.11
--
-- Host: localhost    Database: lava_core
-- ------------------------------------------------------
-- Server version	5.0.56sp1-enterprise-gpl-log

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
) ENGINE=InnoDB AUTO_INCREMENT=1770 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `hibernateproperty`
--

LOCK TABLES `hibernateproperty` WRITE;
/*!40000 ALTER TABLE `hibernateproperty` DISABLE KEYS */;
INSERT INTO `hibernateproperty` VALUES (1625,'lava','core','appointment','id','appointment','reservation_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-03-31 20:35:27'),(1626,'lava','core','appointment','calendar','appointment','calendar_id','int',NULL,10,0,2,'calendar','many-to-one','edu.ucsf.lava.core.resource.model.ResourceCalendar','Yes','2009-03-31 20:35:27'),(1627,'lava','core','appointment','organizer','appointment','organizer_id','int',NULL,10,0,3,'owner','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-03-31 20:35:27'),(1628,'lava','core','appointment','type','appointment','type','varchar',25,NULL,NULL,4,'type','string',NULL,'Yes','2009-03-31 20:35:27'),(1629,'lava','core','appointment','description','appointment','description','varchar',100,NULL,NULL,5,'description','string',NULL,'No','2009-03-31 20:35:27'),(1630,'lava','core','appointment','location','appointment','location','varchar',100,NULL,NULL,6,'location','string',NULL,'No','2009-03-31 20:35:27'),(1631,'lava','core','appointment','startDate','appointment','start_date','date',NULL,NULL,NULL,7,'startDate','date',NULL,'Yes','2009-03-31 20:35:27'),(1632,'lava','core','appointment','startTime','appointment','start_time','time',NULL,NULL,NULL,8,'startTime','time',NULL,'Yes','2009-03-31 20:35:27'),(1633,'lava','core','appointment','endDate','appointment','end_date','date',NULL,NULL,NULL,9,'endDate','date',NULL,'Yes','2009-06-02 22:27:56'),(1634,'lava','core','appointment','endTime','appointment','end_time','time',NULL,NULL,NULL,10,'endTime','time',NULL,NULL,'2009-06-02 22:28:28'),(1635,'lava','core','appointment','status','appointment','status','varchar',25,NULL,NULL,11,'status','string',NULL,'No','2009-05-11 19:45:09'),(1636,'lava','core','appointment','notes','appointment','notes','text',NULL,NULL,NULL,11,'notes','string',NULL,'No','2009-03-31 20:35:27'),(1637,'lava','core','attendee','id','attendee','attendee_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-06-03 20:40:16'),(1638,'lava','core','attendee','appointment','attendee','appointment_id','int',NULL,10,0,2,'appointment','many-to-one','edu.ucsf.lava.core.calendar.model.Appointment','Yes','2009-06-03 20:40:16'),(1639,'lava','core','attendee','user','attendee','user_id','int',NULL,10,0,3,'user','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-06-03 20:40:16'),(1640,'lava','core','attendee','role','attendee','role','varchar',25,NULL,NULL,4,'role','string',NULL,'Yes','2009-06-03 20:40:16'),(1641,'lava','core','attendee','status','attendee','status','varchar',25,NULL,NULL,5,'status','string',NULL,'Yes','2009-06-03 20:40:16'),(1642,'lava','core','attendee','notes','attendee','notes','varchar',100,NULL,NULL,6,'notes','string',NULL,'No','2009-06-03 20:40:16'),(1643,'lava','core','auditEntity','id','audit_entity_work','audit_entity_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1644,'lava','core','auditEntity','auditEvent','audit_entity_work','audit_event_id','int',NULL,10,0,2,'auditEvent','many-to-one','edu.ucsf.memory.lava.model.AuditEvent','Yes','2009-01-25 05:25:56'),(1645,'lava','core','auditEntity','entityId','audit_entity_work','entity_id','int',NULL,10,0,3,'entityId','long',NULL,'Yes','2009-01-25 05:25:56'),(1646,'lava','core','auditEntity','entity','audit_entity_work','entity','varchar',100,NULL,NULL,4,'entity','string',NULL,'Yes','2009-01-25 05:25:56'),(1647,'lava','core','auditEntity','entityType','audit_entity_work','entity_type','varchar',100,NULL,NULL,5,'entityType','string',NULL,'Yes','2009-01-25 05:25:56'),(1648,'lava','core','auditEntity','auditType','audit_entity_work','audit_type','varchar',10,NULL,NULL,6,'auditType','string',NULL,'Yes','2009-01-25 05:25:56'),(1649,'lava','core','auditEntity','hversion','audit_entity_work','hversion','int',NULL,10,0,7,'hversion','long',NULL,'Yes','2009-01-25 05:25:56'),(1650,'lava','core','auditEntityHistory','id','audit_entity','audit_entity_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1651,'lava','core','auditEntityHistory','auditEvent','audit_entity','audit_event_id','int',NULL,10,0,2,'auditEvent','many-to-one','edu.ucsf.memory.lava.model.AuditEvent','Yes','2009-01-25 05:25:56'),(1652,'lava','core','auditEntityHistory','entityId','audit_entity','entity_id','int',NULL,10,0,3,'entityId','long',NULL,'Yes','2009-01-25 05:25:56'),(1653,'lava','core','auditEntityHistory','entity','audit_entity','entity','varchar',100,NULL,NULL,4,'entity','string',NULL,'Yes','2009-01-25 05:25:56'),(1654,'lava','core','auditEntityHistory','entityType','audit_entity','entity_type','varchar',100,NULL,NULL,5,'entityType','string',NULL,'Yes','2009-01-25 05:25:56'),(1655,'lava','core','auditEntityHistory','auditType','audit_entity','audit_type','varchar',10,NULL,NULL,6,'auditType','string',NULL,'Yes','2009-01-25 05:25:56'),(1656,'lava','core','auditEntityHistory','hversion','audit_entity','hversion','int',NULL,10,0,7,'hversion','long',NULL,'Yes','2009-01-25 05:25:56'),(1657,'lava','core','auditEvent','id','audit_event_work','audit_event_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1658,'lava','core','auditEvent','auditUser','audit_event_work','audit_user','varchar',50,NULL,NULL,2,'auditUser','string',NULL,'Yes','2009-01-25 05:25:56'),(1659,'lava','core','auditEvent','auditHost','audit_event_work','audit_host','varchar',25,NULL,NULL,3,'auditHost','string',NULL,'Yes','2009-01-25 05:25:56'),(1660,'lava','core','auditEvent','auditTimestamp','audit_event_work','audit_timestamp','timestamp',NULL,23,3,4,'auditTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1661,'lava','core','auditEvent','action','audit_event_work','action','varchar',255,NULL,NULL,5,'action','string',NULL,'Yes','2009-01-25 05:25:56'),(1662,'lava','core','auditEvent','actionEvent','audit_event_work','action_event','varchar',50,NULL,NULL,6,'actionEvent','string',NULL,'Yes','2009-01-25 05:25:56'),(1663,'lava','core','auditEvent','actionIdParam','audit_event_work','action_id_param','varchar',50,NULL,NULL,7,'actionIdParam','string',NULL,'No','2009-01-25 05:25:56'),(1664,'lava','core','auditEvent','eventNote','audit_event_work','event_note','varchar',255,NULL,NULL,8,'eventNote','string',NULL,'No','2009-01-25 05:25:56'),(1665,'lava','core','auditEvent','exception','audit_event_work','exception','varchar',255,NULL,NULL,9,'exception','string',NULL,'No','2009-01-25 05:25:56'),(1666,'lava','core','auditEvent','exceptionMessage','audit_event_work','exception_message','varchar',255,NULL,NULL,10,'exceptionMessage','string',NULL,'No','2009-01-25 05:25:56'),(1667,'lava','core','auditEvent','hversion','audit_event_work','hversion','int',NULL,10,0,11,'hversion','long',NULL,'Yes','2009-01-25 05:25:56'),(1668,'lava','core','auditEventHistory','id','audit_event','audit_event_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1669,'lava','core','auditEventHistory','auditUser','audit_event','audit_user','varchar',50,NULL,NULL,2,'auditUser','string',NULL,'Yes','2009-01-25 05:25:56'),(1670,'lava','core','auditEventHistory','auditHost','audit_event','audit_host','varchar',25,NULL,NULL,3,'auditHost','string',NULL,'Yes','2009-01-25 05:25:56'),(1671,'lava','core','auditEventHistory','auditTimestamp','audit_event','audit_timestamp','timestamp',NULL,23,3,4,'auditTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1672,'lava','core','auditEventHistory','action','audit_event','action','varchar',255,NULL,NULL,5,'action','string',NULL,'Yes','2009-01-25 05:25:56'),(1673,'lava','core','auditEventHistory','actionEvent','audit_event','action_event','varchar',50,NULL,NULL,6,'actionEvent','string',NULL,'Yes','2009-01-25 05:25:56'),(1674,'lava','core','auditEventHistory','actionIdParam','audit_event','action_id_param','varchar',50,NULL,NULL,7,'actionIdParam','string',NULL,'No','2009-01-25 05:25:56'),(1675,'lava','core','auditEventHistory','eventNote','audit_event','event_note','varchar',255,NULL,NULL,8,'eventNote','string',NULL,'No','2009-01-25 05:25:56'),(1676,'lava','core','auditEventHistory','exception','audit_event','exception','varchar',255,NULL,NULL,9,'exception','string',NULL,'No','2009-01-25 05:25:56'),(1677,'lava','core','auditEventHistory','exceptionMessage','audit_event','exception_message','varchar',255,NULL,NULL,10,'exceptionMessage','string',NULL,'No','2009-01-25 05:25:56'),(1678,'lava','core','auditEventHistory','hversion','audit_event','hversion','int',NULL,10,0,11,'hversion','long',NULL,'Yes','2009-01-25 05:25:56'),(1679,'lava','core','auditProperty','id','audit_property_work','audit_property_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1680,'lava','core','auditProperty','auditEntity','audit_property_work','audit_entity_id','int',NULL,10,0,2,'auditEntity','many-to-one','edu.ucsf.memory.lava.model.AuditEntity','Yes','2009-01-25 05:25:56'),(1681,'lava','core','auditProperty','property','audit_property_work','property','varchar',100,NULL,NULL,3,'property','string',NULL,'Yes','2009-01-25 05:25:56'),(1682,'lava','core','auditProperty','indexKey','audit_property_work','index_key','varchar',100,NULL,NULL,4,'indexKey','string',NULL,'No','2009-01-25 05:25:56'),(1683,'lava','core','auditProperty','subproperty','audit_property_work','subproperty','varchar',255,NULL,NULL,5,'subproperty','string',NULL,'No','2009-01-25 05:25:56'),(1684,'lava','core','auditProperty','oldValue','audit_property_work','old_value','varchar',255,NULL,NULL,6,'oldValue','string',NULL,'Yes','2009-01-25 05:25:56'),(1685,'lava','core','auditProperty','newValue','audit_property_work','new_value','varchar',255,NULL,NULL,7,'newValue','string',NULL,'Yes','2009-01-25 05:25:56'),(1686,'lava','core','auditProperty','auditTimestamp','audit_property_work','audit_timestamp','timestamp',NULL,23,3,8,'auditTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1687,'lava','core','auditProperty','oldText','audit_text_work','old_text','text',16,NULL,NULL,9,'oldText','text',NULL,'No','2009-01-25 05:25:56'),(1688,'lava','core','auditProperty','newText','audit_text_work','new_text','text',16,NULL,NULL,10,'newText','text',NULL,'No','2009-01-25 05:25:56'),(1689,'lava','core','auditProperty','hversion','audit_property_work','hversion','int',NULL,10,0,11,'hversion','long',NULL,'No','2009-01-25 05:25:56'),(1690,'lava','core','auditPropertyHistory','id','audit_property','audit_property_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1691,'lava','core','auditPropertyHistory','auditEntity','audit_property','audit_entity_id','int',NULL,10,0,2,'auditEntity','many-to-one','edu.ucsf.memory.lava.model.AuditEntity','Yes','2009-01-25 05:25:56'),(1692,'lava','core','auditPropertyHistory','property','audit_property','property','varchar',100,NULL,NULL,3,'property','string',NULL,'Yes','2009-01-25 05:25:56'),(1693,'lava','core','auditPropertyHistory','indexKey','audit_property','index_key','varchar',100,NULL,NULL,4,'indexKey','string',NULL,'No','2009-01-25 05:25:56'),(1694,'lava','core','auditPropertyHistory','subproperty','audit_property','subproperty','varchar',255,NULL,NULL,5,'subproperty','string',NULL,'No','2009-01-25 05:25:56'),(1695,'lava','core','auditPropertyHistory','oldValue','audit_property','old_value','varchar',255,NULL,NULL,6,'oldValue','string',NULL,'Yes','2009-01-25 05:25:56'),(1696,'lava','core','auditPropertyHistory','newValue','audit_property','new_value','varchar',255,NULL,NULL,7,'newValue','string',NULL,'Yes','2009-01-25 05:25:56'),(1697,'lava','core','auditPropertyHistory','auditTimestamp','audit_property','audit_timestamp','timestamp',NULL,23,3,8,'auditTimestamp','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1698,'lava','core','auditPropertyHistory','oldText','audit_text','old_text','text',16,NULL,NULL,9,'oldText','text',NULL,'No','2009-01-25 05:25:56'),(1699,'lava','core','auditPropertyHistory','newText','audit_text','new_text','text',16,NULL,NULL,10,'newText','text',NULL,'No','2009-01-25 05:25:56'),(1700,'lava','core','auditPropertyHistory','hversion','audit_property','hversion','int',NULL,10,0,11,'hversion','long',NULL,'No','2009-01-25 05:25:56'),(1701,'lava','core','authGroup','id','authgroup','GID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1702,'lava','core','authGroup','groupName','authgroup','GroupName','varchar',50,NULL,NULL,2,'groupName','string',NULL,'Yes','2009-01-25 05:25:56'),(1703,'lava','core','authGroup','effectiveDate','authgroup','EffectiveDate','date',NULL,16,0,3,'effectiveDate','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1704,'lava','core','authGroup','expirationDate','authgroup','ExpirationDate','date',NULL,16,0,4,'expirationDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1705,'lava','core','authGroup','notes','authgroup','Notes','varchar',255,NULL,NULL,5,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1706,'lava','core','authPermission','id','authpermission','PermID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1707,'lava','core','authPermission','role','authpermission','RoleID','varchar',25,NULL,NULL,2,'role','many-to-one','edu.ucsf.memory.lava.model.AuthRole','Yes','2009-01-25 05:25:56'),(1708,'lava','core','authPermission','module','authpermission','Module','varchar',25,NULL,NULL,4,'module','string',NULL,'Yes','2009-01-25 05:25:56'),(1709,'lava','core','authPermission','permitDeny','authpermission','PermitDeny','varchar',10,NULL,NULL,4,'permitDeny','string',NULL,'Yes','2009-01-25 05:25:56'),(1710,'lava','core','authPermission','section','authpermission','Section','varchar',25,NULL,NULL,5,'section','string',NULL,'Yes','2009-01-25 05:25:56'),(1711,'lava','core','authPermission','target','authpermission','Target','varchar',25,NULL,NULL,6,'target','string',NULL,'Yes','2009-01-25 05:25:56'),(1712,'lava','core','authPermission','mode','authpermission','Mode','varchar',25,NULL,NULL,7,'mode','string',NULL,'Yes','2009-01-25 05:25:56'),(1713,'lava','core','authPermission','notes','authpermission','Notes','varchar',100,NULL,NULL,10,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1714,'lava','core','authRole','id','authrole','RoleID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1715,'lava','core','authRole','roleName','authrole','RoleName','varchar',25,NULL,NULL,2,'roleName','string',NULL,'Yes','2009-01-25 05:25:56'),(1716,'lava','core','authRole','patientAccess','authrole','PatientAccess','smallint',NULL,5,0,3,'patientAccess','short',NULL,'Yes','2009-01-25 05:25:56'),(1717,'lava','core','authRole','phiAccess','authrole','PhiAccess','smallint',NULL,5,0,4,'phiAccess','short',NULL,'Yes','2009-01-25 05:25:56'),(1718,'lava','core','authRole','patientAccess','authrole','GhiAccess','smallint',NULL,5,0,5,'ghiAccess','short',NULL,'Yes','2009-01-25 05:25:56'),(1719,'lava','core','authRole','notes','authrole','Notes','varchar',255,NULL,NULL,8,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1720,'lava','core','authUser','id','authuser','UID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1721,'lava','core','authUser','userName','authuser','UserName','varchar',50,NULL,NULL,2,'userName','string',NULL,'Yes','2009-01-25 05:25:56'),(1722,'lava','core','authUser','login','authuser','Login','varchar',100,NULL,NULL,3,'login','string',NULL,'No','2009-01-25 05:25:56'),(1723,'lava','core','authUser','email','authuser','email','varchar',100,NULL,NULL,4,'email','string',NULL,'No','2009-05-12 18:53:20'),(1724,'lava','core','authUser','phone','authuser','phone','varchar',25,NULL,NULL,5,'phone','string',NULL,'No','2009-05-12 18:53:20'),(1725,'lava','core','authUser','accessAgreementDate','authuser','AccessAgreementDate','date',NULL,16,0,7,'accessAgreementDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1726,'lava','core','authUser','shortUserName','authuser','ShortUserName','varchar',53,NULL,NULL,8,'shortUserName','string',NULL,'No','2009-01-25 05:25:56'),(1727,'lava','core','authUser','shortUserNameRev','authuser','ShortUserNameRev','varchar',54,NULL,NULL,9,'shortUserNameRev','string',NULL,'No','2009-01-25 05:25:56'),(1728,'lava','core','authUser','effectiveDate','authuser','EffectiveDate','date',NULL,16,0,10,'effectiveDate','timestamp',NULL,'Yes','2009-01-25 05:25:56'),(1729,'lava','core','authUser','expirationDate','authuser','ExpirationDate','date',NULL,16,0,11,'expirationDate','timestamp',NULL,'No','2009-01-25 05:25:56'),(1730,'lava','core','authUser','authenticationType','authuser','authenticationType','varchar',10,NULL,NULL,12,'authenticationType','string',NULL,'No','2009-05-12 18:53:20'),(1731,'lava','core','authUser','notes','authuser','Notes','varchar',255,NULL,NULL,12,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1732,'lava','core','authUser','password','authuser','password','varchar',100,NULL,NULL,13,'password','string',NULL,'No','2009-05-12 18:53:20'),(1733,'lava','core','authUser','passwordExpiration','authuser','passwordExpiration','timestamp',NULL,NULL,NULL,14,'passwordExpiration','timestamp',NULL,'No','2009-05-12 18:53:20'),(1734,'lava','core','authUser','passwordResetToken','authuser','passwordResetToken','varchar',100,NULL,NULL,15,'passwordResetToken','string',NULL,'No','2009-05-12 18:53:20'),(1735,'lava','core','authUser','passwordResetExpiration','authuser','passwordResetExpiration','timestamp',NULL,NULL,NULL,16,'passwordResetExpiration','timestamp',NULL,'No','2009-05-12 18:53:20'),(1736,'lava','core','authUser','failedLoginCount','authuser','failedLoginCount','smallint',NULL,5,0,17,'failedLoginCount','short',NULL,'No','2009-05-12 18:53:20'),(1737,'lava','core','authUser','lastFailedLogin','authuser','lastFailedLogin','timestamp',NULL,NULL,NULL,18,'lastFailedLogin','timestamp',NULL,'No','2009-05-12 18:53:20'),(1738,'lava','core','authUser','accountLocked','authuser','accountLocked','timestamp',NULL,NULL,NULL,19,'accountLocked','timestamp',NULL,'No','2009-05-12 18:53:20'),(1739,'lava','core','authUserGroup','id','authusergroup','UGID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1740,'lava','core','authUserGroup','user','authusergroup','UID','int',NULL,10,0,2,'user','many-to-one','edu.ucsf.memory.lava.model.AuthUser','Yes','2009-01-25 05:25:56'),(1741,'lava','core','authUserGroup','group','authusergroup','GID','int',NULL,10,0,3,'group','many-to-one','edu.ucsf.memory.lava.model.AuthGroup','Yes','2009-01-25 05:25:56'),(1742,'lava','core','authUserGroup','notes','authusergroup','Notes','varchar',255,NULL,NULL,6,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1743,'lava','core','authUserRole','id','authuserrole','URID','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-01-25 05:25:56'),(1744,'lava','core','authUserRole','role','authuserrole','RoleID','varchar',25,NULL,NULL,3,'role','many-to-one','edu.ucsf.memory.lava.model.AuthRole','Yes','2009-01-25 05:25:56'),(1745,'lava','core','authUserRole','project','authuserrole','Project','varchar',25,NULL,NULL,4,'project','string',NULL,'No','2009-01-25 05:25:56'),(1746,'lava','core','authUserRole','unit','authuserrole','Unit','varchar',25,NULL,NULL,5,'unit','string',NULL,'No','2009-01-25 05:25:56'),(1747,'lava','core','authUserRole','user','authuserrole','UID','int',NULL,10,0,7,'user','many-to-one','edu.ucsf.memory.lava.model.AuthUser','No','2009-01-25 05:25:56'),(1748,'lava','core','authUserRole','group','authuserrole','GID','int',NULL,10,0,8,'group','many-to-one','edu.ucsf.memory.lava.model.AuthGroup','No','2009-01-25 05:25:56'),(1749,'lava','core','authUserRole','notes','authuserrole','Notes','varchar',255,NULL,NULL,12,'notes','string',NULL,'No','2009-01-25 05:25:56'),(1750,'lava','core','calendar','calendar_id','calendar','calendar_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-03-31 19:09:35'),(1751,'lava','core','calendar','type','calendar','type','varchar',25,NULL,NULL,2,'type','string',NULL,'Yes','2009-06-02 22:27:08'),(1752,'lava','core','calendar','name','calendar','name','varchar',100,NULL,NULL,3,'name','string',NULL,'Yes','2009-03-31 19:09:35'),(1753,'lava','core','calendar','description','calendar','description','varchar',255,NULL,NULL,4,'description','string',NULL,'No','2009-03-31 19:09:35'),(1754,'lava','core','calendar','notes','calendar','notes','text',NULL,NULL,NULL,5,'notes','string',NULL,'No','2009-03-31 19:09:35'),(1755,'lava','core','lavaFile','id','lava_file','id','int',NULL,10,0,1,'id','long',NULL,'Yes','2011-10-19 18:05:17'),(1756,'lava','core','lavaFile','name','lava_file','name','varchar',255,NULL,NULL,2,'name','string',NULL,'No','2011-10-19 18:05:17'),(1757,'lava','core','lavaFile','fileType','lava_file','file_type','varchar',255,NULL,NULL,3,'fileType','string',NULL,'No','2011-10-19 18:05:17'),(1758,'lava','core','lavaFile','contentType','lava_file','content_type','varchar',100,NULL,NULL,4,'contentType','string',NULL,'No','2011-10-19 18:05:17'),(1759,'lava','core','lavaFile','fileStatusDate','lava_file','file_status_date','date',NULL,NULL,NULL,5,'fileStatusDate','date',NULL,'No','2011-10-19 18:05:17'),(1760,'lava','core','lavaFile','fileStatus','lava_file','file_status','varchar',50,NULL,NULL,6,'fileStatus','string',NULL,'No','2011-10-19 18:05:17'),(1761,'lava','core','lavaFile','fileStatusBy','lava_file','file_status_by','varchar',50,NULL,NULL,7,'fileStatusBy','string',NULL,'No','2011-10-19 18:05:17'),(1762,'lava','core','lavaFile','repositoryId','lava_file','repository_id','varchar',100,NULL,NULL,8,'repositoryId','string',NULL,'No','2011-10-19 18:05:17'),(1763,'lava','core','lavaFile','fileId','lava_file','file_id','varchar',100,NULL,NULL,9,'fileId','string',NULL,'No','2011-10-19 18:05:17'),(1764,'lava','core','lavaFile','location','lava_file','location','varchar',1000,NULL,NULL,10,'location','string',NULL,'No','2011-10-19 18:05:17'),(1765,'lava','core','lavaFile','checksum','lava_file','checksum','varchar',100,NULL,NULL,11,'checksum','string',NULL,'No','2011-10-19 18:05:17'),(1766,'lava','core','resourceCalendar','id','resource_calendar','calendar_id','int',NULL,10,0,1,'id','long',NULL,'Yes','2009-06-03 17:28:00'),(1767,'lava','core','resourceCalendar','resourceType','resource_calendar','resource_type','varchar',25,NULL,NULL,2,'resourceType','string',NULL,'Yes','2009-06-03 17:28:00'),(1768,'lava','core','resourceCalendar','location','resource_calendar','location','varchar',100,NULL,NULL,3,'location','string',NULL,'No','2009-06-03 17:28:00'),(1769,'lava','core','resourceCalendar','contact','resource_calendar','contact_id','int',NULL,10,0,4,'contact','many-to-one','edu.ucsf.lava.core.auth.model.AuthUser','Yes','2009-06-03 17:28:00');
/*!40000 ALTER TABLE `hibernateproperty` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=481 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `list`
--

LOCK TABLES `list` WRITE;
/*!40000 ALTER TABLE `list` DISABLE KEYS */;
INSERT INTO `list` VALUES (468,'LavaFileContentType','lava','core',0,'2011-11-05 00:41:43'),(469,'LavaFileStatus','lava','core',0,'2011-11-05 00:45:18'),(470,'LavaSessionStatus','lava','core',0,'2009-01-25 04:57:59'),(471,'NavigationListPageSize','lava','core',1,'2009-01-25 04:57:59'),(472,'TextYesNo','lava','core',0,'2009-01-25 04:57:59'),(473,'TextYesNoDK','lava','core',0,'2009-01-25 04:57:59'),(474,'TextYesNoNA','lava','core',0,'2009-01-25 04:57:59'),(475,'YESNO','lava','core',1,'2009-01-25 04:57:59'),(476,'YESNODK','lava','core',1,'2009-01-25 04:57:59'),(477,'YesNoDK_Zero','lava','core',0,'2009-01-25 04:57:59'),(478,'YesNoUnknown','lava','core',1,'2009-01-25 04:57:59'),(479,'YesNoZeroNA','lava','core',1,'2009-05-12 00:16:47'),(480,'YesNo_Zero','lava','core',0,'2009-01-25 04:57:59');
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
) ENGINE=InnoDB AUTO_INCREMENT=24415 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `listvalues`
--

LOCK TABLES `listvalues` WRITE;
/*!40000 ALTER TABLE `listvalues` DISABLE KEYS */;
INSERT INTO `listvalues` VALUES (24376,468,'lava','core','GENERAL','Image',0,'2011-11-05 00:44:15'),(24377,468,'lava','core','GENERAL','Document',0,'2011-11-05 00:44:15'),(24378,469,'lava','core','GENERAL','Uploaded',0,'2011-11-05 00:46:17'),(24379,470,'lava','core','NEW',NULL,1,'2009-01-25 04:57:59'),(24380,470,'lava','core','ACTIVE',NULL,2,'2009-01-25 04:57:59'),(24381,470,'lava','core','LOGOFF',NULL,3,'2009-01-25 04:57:59'),(24382,470,'lava','core','EXPIRED',NULL,4,'2009-01-25 04:57:59'),(24383,470,'lava','core','DISCONNECTED',NULL,5,'2009-01-25 04:57:59'),(24384,471,'lava','core','10','10/page',0,'2009-01-25 04:57:59'),(24385,471,'lava','core','100','100/page',0,'2009-01-25 04:57:59'),(24386,471,'lava','core','15','15/page',0,'2009-01-25 04:57:59'),(24387,471,'lava','core','25','25/page',0,'2009-01-25 04:57:59'),(24388,471,'lava','core','250','250/page',0,'2009-01-25 04:57:59'),(24389,471,'lava','core','5','5/page',0,'2009-01-25 04:57:59'),(24390,471,'lava','core','50','50/page',0,'2009-01-25 04:57:59'),(24391,472,'lava','core','Yes',NULL,1,'2009-01-25 04:57:59'),(24392,472,'lava','core','No',NULL,2,'2009-01-25 04:57:59'),(24393,473,'lava','core','Yes',NULL,1,'2009-01-25 04:57:59'),(24394,473,'lava','core','No',NULL,2,'2009-01-25 04:57:59'),(24395,473,'lava','core','Don\'t Know',NULL,3,'2009-01-25 04:57:59'),(24396,474,'lava','core','Yes',NULL,1,'2009-01-25 04:57:59'),(24397,474,'lava','core','No',NULL,2,'2009-01-25 04:57:59'),(24398,474,'lava','core','N/A',NULL,3,'2009-01-25 04:57:59'),(24399,475,'lava','core','1','Yes',0,'2009-01-25 04:57:59'),(24400,475,'lava','core','2','No',0,'2009-01-25 04:57:59'),(24401,476,'lava','core','1','Yes',0,'2009-01-25 04:57:59'),(24402,476,'lava','core','2','No',0,'2009-01-25 04:57:59'),(24403,476,'lava','core','9','Don\'t Know',0,'2009-01-25 04:57:59'),(24404,477,'lava','core','0','No',0,'2009-01-25 04:57:59'),(24405,477,'lava','core','1','Yes',0,'2009-01-25 04:57:59'),(24406,477,'lava','core','9','Don\'t Know',0,'2009-01-25 04:57:59'),(24407,478,'lava','core','0','No',0,'2009-01-25 04:57:59'),(24408,478,'lava','core','1','Yes',0,'2009-01-25 04:57:59'),(24409,478,'lava','core','9','Unknown',0,'2009-01-25 04:57:59'),(24410,479,'lava','core','1','Yes',1,'2009-05-12 00:17:48'),(24411,479,'lava','core','0','No',2,'2009-05-12 00:17:48'),(24412,479,'lava','core','9','N/A',3,'2009-05-12 00:17:48'),(24413,480,'lava','core','0','No',0,'2009-01-25 04:57:59'),(24414,480,'lava','core','1','Yes',0,'2009-01-25 04:57:59');
/*!40000 ALTER TABLE `listvalues` ENABLE KEYS */;
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
INSERT INTO `preference` VALUES (1,NULL,'calendar','displayRange','Default View (e.g. Month, Week)','Month',0,'2010-01-26 22:10:06'),(2,NULL,'calendar','showDayLength','Sets day length in week or day views to display either full day or work day','Work Day',0,'2010-01-27 01:13:23'),(4,NULL,'baseList','pageSize','The default page view size for lists.','25',1,'2012-01-21 00:37:29');
/*!40000 ALTER TABLE `preference` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `query_objects`
--

LOCK TABLES `query_objects` WRITE;
/*!40000 ALTER TABLE `query_objects` DISABLE KEYS */;
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
INSERT INTO `versionhistory` VALUES ('lava-core-model','3.1.0','2012-01-24 14:39:15',3,1,0,1),('LAVAQUERYAPP','3.0.0','2012-01-24 14:39:53',3,0,0,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=2642 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `viewproperty`
--

LOCK TABLES `viewproperty` WRITE;
/*!40000 ALTER TABLE `viewproperty` DISABLE KEYS */;
INSERT INTO `viewproperty` VALUES (2479,'*.appointment.id','en','lava','core',NULL,'appointment','id','details','c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2009-03-31 20:35:39'),(2480,'*.appointment.calendar.name','en','lava','core',NULL,'appointment','calendar.name','details','c','string','No','Calendar',NULL,NULL,NULL,0,NULL,NULL,NULL,2,NULL,'2009-03-31 20:35:39'),(2481,'*.appointment.organizerId','en','lava','core','','appointment','organizerId','details','c','range','No','Organizer',NULL,NULL,NULL,NULL,'','appointment.organizer','',3,'','2009-03-31 20:35:39'),(2482,'*.appointment.type','en','lava','core',NULL,'appointment','type','details','i','range','No','Type',NULL,NULL,NULL,0,NULL,'appointment.type',NULL,4,NULL,'2009-03-31 20:35:39'),(2483,'*.appointment.description','en','lava','core',NULL,'appointment','description','details','i','text','No','Description',NULL,100,NULL,0,NULL,NULL,NULL,5,NULL,'2009-03-31 20:35:39'),(2484,'*.appointment.location','en','lava','core',NULL,'appointment','location','details','i','string','No','Location',NULL,100,NULL,0,NULL,NULL,NULL,6,NULL,'2009-03-31 20:35:39'),(2485,'*.appointment.startDate','en','lava','core','','appointment','startDate','details','i','date','Yes','Start Date',NULL,NULL,10,0,NULL,NULL,NULL,7,NULL,'2009-04-16 15:31:52'),(2486,'*.appointment.startTime','en','lava','core','','appointment','startTime','details','i','time','Yes','Time',NULL,NULL,NULL,0,'',NULL,'',8,'','2009-04-02 04:49:05'),(2487,'*.appointment.endDate','en','lava','core','','appointment','endDate','details','i','date','Yes','End Date',NULL,NULL,10,0,NULL,NULL,NULL,9,NULL,'2009-04-27 20:47:43'),(2488,'*.appointment.endTime','en','lava','core','','appointment','endTime','details','i','time','Yes','Time',NULL,NULL,NULL,0,NULL,NULL,NULL,10,NULL,'2009-04-16 15:30:06'),(2489,'*.appointment.status','en','lava','core',NULL,'appointment','status','details','i','range','No','Status',NULL,NULL,NULL,0,NULL,'resourceReservation.status',NULL,10,NULL,'2009-05-11 19:43:15'),(2490,'*.appointment.notes','en','lava','core',NULL,'appointment','notes','notes','i','unlimitedtext','No','Notes',NULL,NULL,NULL,0,'rows=\"10\" cols=\"45\"',NULL,NULL,11,NULL,'2009-03-31 20:35:39'),(2491,'*.appointment.organizer.userName','en','lava','core',NULL,'appointment','organizer.userName','details','c','string','No','Organizer',NULL,NULL,NULL,0,NULL,NULL,NULL,14,'Organizer Name','2009-06-11 17:00:00'),(2492,'*.attendee.userId','en','lava','core',NULL,'attendee','userId','details','i','range','Yes','Attendee',NULL,NULL,NULL,0,NULL,'attendee.attendee',NULL,3,'Attendee','2009-06-03 20:40:16'),(2493,'*.attendee.role','en','lava','core',NULL,'attendee','role','details','i','range','Yes','Role',NULL,25,NULL,0,NULL,'attendee.role',NULL,4,'Attendee Role','2009-06-03 20:40:16'),(2494,'*.attendee.status','en','lava','core',NULL,'attendee','status','details','i','range','Yes','Status',NULL,25,NULL,0,NULL,'attendee.status',NULL,5,'Attendee Status','2009-06-03 20:40:16'),(2495,'*.attendee.notes','en','lava','core',NULL,'attendee','notes','details','i','test','No','Notes',NULL,100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,6,'Notes','2009-06-03 20:40:16'),(2496,'*.auditEntityHistory.id','en','lava','core',NULL,'auditEntityHistory','id',NULL,'c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-25 05:28:51'),(2497,'*.auditEntityHistory.entityId','en','lava','core',NULL,'auditEntityHistory','entityId',NULL,'c','numeric','Yes','Entity ID',NULL,NULL,NULL,0,NULL,NULL,NULL,3,'ID of the Entity','2009-01-25 05:28:51'),(2498,'*.auditEntityHistory.entity','en','lava','core',NULL,'auditEntityHistory','entity',NULL,'c','string','Yes','Entity',NULL,100,NULL,0,NULL,NULL,NULL,4,'Base entity name, e.g. Patient, Instrument (this is the entity name where the autoincrementing id field is defined)','2009-01-25 05:28:51'),(2499,'*.auditEntityHistory.entityType','en','lava','core',NULL,'auditEntityHistory','entityType',NULL,'c','string','No','Entity Type',NULL,100,NULL,0,NULL,NULL,NULL,5,'Optional subtype of the entity (e.g. MacPatient, BedsideScreen','2009-01-25 05:28:51'),(2500,'*.auditEntityHistory.auditType','en','lava','core',NULL,'auditEntityHistory','auditType',NULL,'c','string','Yes','Audit Type',NULL,10,NULL,0,NULL,NULL,NULL,6,'The type of auditing for the entity (e.g. CREATE, READ, UPDATE, DELETE)','2009-01-25 05:28:51'),(2501,'*.auditEventHistory.id','en','lava','core',NULL,'auditEventHistory','id',NULL,'c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-25 05:28:51'),(2502,'*.auditEventHistory.auditUser','en','lava','core',NULL,'auditEventHistory','auditUser',NULL,'c','string','Yes','Audit User',NULL,50,NULL,0,NULL,NULL,NULL,2,'The user who initiated the event','2009-01-25 05:28:51'),(2503,'*.auditEventHistory.auditHost','en','lava','core',NULL,'auditEventHistory','auditHost',NULL,'c','string','Yes','Audit Host',NULL,25,NULL,0,NULL,NULL,NULL,3,'The host (machine) that the event was initiated from','2009-01-25 05:28:51'),(2504,'*.auditEventHistory.auditTime','en','lava','core',NULL,'auditEventHistory','auditTime',NULL,'c','datetime','Yes','Audit Time',NULL,NULL,NULL,0,NULL,NULL,NULL,4,'The time that the event was initiated','2009-01-25 05:28:51'),(2505,'*.auditEventHistory.action','en','lava','core',NULL,'auditEventHistory','action',NULL,'c','string','Yes','Action',NULL,255,NULL,0,NULL,NULL,NULL,5,'The action id od the event','2009-01-25 05:28:51'),(2506,'*.auditEventHistory.actionEvent','en','lava','core',NULL,'auditEventHistory','actionEvent',NULL,'c','string','Yes','Action Event',NULL,50,NULL,0,NULL,NULL,NULL,6,'The event type of the event (e.g. add, view, delete, edit, list)','2009-01-25 05:28:51'),(2507,'*.auditEventHistory.actionIdParam','en','lava','core',NULL,'auditEventHistory','actionIdParam',NULL,'c','string','No','ID Param',NULL,50,NULL,0,NULL,NULL,NULL,7,'If an ID parameter was supplied for the event','2009-01-25 05:28:51'),(2508,'*.auditEventHistory.eventNote','en','lava','core',NULL,'auditEventHistory','eventNote',NULL,'c','text','No','Note',NULL,255,NULL,0,NULL,NULL,NULL,8,'An optional note field for the event','2009-01-25 05:28:51'),(2509,'*.auditEventHistory.exception','en','lava','core',NULL,'auditEventHistory','exception',NULL,'c','text','No','Exception',NULL,255,NULL,0,NULL,NULL,NULL,9,'If the event resulted in a handled exception','2009-01-25 05:28:51'),(2510,'*.auditEventHistory.exceptionMessage','en','lava','core',NULL,'auditEventHistory','exceptionMessage',NULL,'c','text','No','Exception Message',NULL,255,NULL,0,NULL,NULL,NULL,10,'The message associated with the handled exception.','2009-01-25 05:28:51'),(2511,'*.auditPropertyHistory.id','en','lava','core',NULL,'auditPropertyHistory','id',NULL,'c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-25 05:28:51'),(2512,'*.auditPropertyHistory.property','en','lava','core',NULL,'auditPropertyHistory','property',NULL,'c','string','Yes','Property Name',NULL,100,NULL,0,NULL,NULL,NULL,3,'The name of the entity property','2009-01-25 05:28:51'),(2513,'*.auditPropertyHistory.indexKey','en','lava','core',NULL,'auditPropertyHistory','indexKey',NULL,'c','string','No','Index Key Value',NULL,100,NULL,0,NULL,NULL,NULL,4,'If the property is a collection, the index into the collection for this particular subproperty value','2009-01-25 05:28:51'),(2514,'*.auditPropertyHistory.subproperty','en','lava','core',NULL,'auditPropertyHistory','subproperty',NULL,'c','string','No','Subproperty Name',NULL,255,NULL,0,NULL,NULL,NULL,5,'The name of the subproperty when theproperty is a collection','2009-01-25 05:28:51'),(2515,'*.auditPropertyHistory.oldValue','en','lava','core',NULL,'auditPropertyHistory','oldValue',NULL,'c','string','Yes','Old Value',NULL,255,NULL,0,NULL,NULL,NULL,6,'The old value or {CREATED} when the record is for a new value','2009-01-25 05:28:51'),(2516,'*.auditPropertyHistory.newValue','en','lava','core',NULL,'auditPropertyHistory','newValue',NULL,'c','string','Yes','New Value',NULL,255,NULL,0,NULL,NULL,NULL,7,'The new value or {DELETED} when the record is for a property deletion','2009-01-25 05:28:51'),(2517,'*.auditPropertyHistory.auditTimestamp','en','lava','core',NULL,'auditPropertyHistory','auditTimestamp',NULL,'c','timestamp','Yes','Audit Time',NULL,NULL,NULL,0,NULL,NULL,NULL,8,'The time of the event (copied from the Audit_Event table for convenience','2009-01-25 05:28:51'),(2518,'*.auditPropertyHistory.oldText','en','lava','core',NULL,'auditPropertyHistory','oldText',NULL,'c','unlimitedtext','Yes','Old Text Value',NULL,16,NULL,0,'rows=\"10\" cols=\"80\"',NULL,NULL,9,'The old text value or {CREATED} if the record if for a property creation','2009-01-25 05:28:51'),(2519,'*.auditPropertyHistory.newText','en','lava','core',NULL,'auditPropertyHistory','newText',NULL,'c','unlimitedtext','Yes','New Text Value',NULL,16,NULL,0,'rows=\"10\" cols=\"80\"',NULL,NULL,10,'The new text value of {DELETED} if the record is for a property deletion','2009-01-25 05:28:51'),(2520,'*.authGroup.id','en','lava','core',NULL,'authGroup','id','details','c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the authorization group','2009-01-25 05:28:51'),(2521,'*.authGroup.disabled','en','lava','core',NULL,'authGroup','disabled','status','i','range','Yes','Disabled',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,2,'Disabled','2009-01-25 05:28:51'),(2522,'*.authGroup.groupName','en','lava','core',NULL,'authGroup','groupName','details','i','string','Yes','Group Name',NULL,50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-25 05:28:51'),(2523,'*.authGroup.groupNameWithStatus','en','lava','core',NULL,'authGroup','groupNameWithStatus',NULL,'c','string','Yes','Group Name',NULL,50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-25 05:28:51'),(2524,'*.authGroup.effectiveDate','en','lava','core',NULL,'authGroup','effectiveDate','status','i','date','Yes','Effective Date',NULL,NULL,NULL,0,NULL,NULL,NULL,3,'Effective Date of the authorization group','2009-01-25 05:28:51'),(2525,'*.authGroup.expirationDate','en','lava','core',NULL,'authGroup','expirationDate','status','i','date','Yes','Expiration Date',NULL,NULL,NULL,0,NULL,NULL,NULL,4,'Expiration Date of the authorization group','2009-01-25 05:28:51'),(2526,'*.authGroup.notes','en','lava','core',NULL,'authGroup','notes','note','i','text','Yes','Notes',NULL,255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,5,'Notes about the authorization group','2009-01-25 05:28:51'),(2527,'*.authPermission.id','en','lava','core',NULL,'authPermission','id','details','c','numeric','Yes','Permission ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the permission','2009-01-25 05:28:51'),(2528,'*.authPermission.role.id','en','lava','core',NULL,'authPermission','role.id',NULL,'c','numeric','Yes','Role ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the role','2009-01-25 05:28:51'),(2529,'*.authPermission.role.roleName','en','lava','core',NULL,'authPermission','role.roleName',NULL,'i','string','Yes','Role Name',NULL,25,NULL,0,NULL,NULL,NULL,2,'Name of the role','2009-01-25 05:28:51'),(2530,'*.authPermission.roleId','en','lava','core',NULL,'authPermission','roleId','details','i','range','Yes','Role',NULL,25,NULL,0,NULL,'auth.roles',NULL,2,'The role that the permission applies to','2009-01-25 05:28:51'),(2531,'*.authPermission.permitDeny','en','lava','core',NULL,'authPermission','permitDeny','details','i','range','Yes','Permit / Deny',NULL,10,NULL,0,NULL,'authPermission.permitDeny',NULL,3,'Whether to PERMIT or DENY the permission to the role','2009-01-25 05:28:51'),(2532,'*.authPermission.scope','en','lava','core','','authPermission','scope','details','i','string','Yes','Scope',NULL,25,0,0,'','','',4,'Scope','2009-01-01 08:00:00'),(2533,'*.authPermission.module','en','lava','core',NULL,'authPermission','module','details','i','suggest','Yes','Module',NULL,25,NULL,0,NULL,NULL,NULL,5,'the moule that the permission covers','2009-01-25 05:28:51'),(2534,'*.authPermission.section','en','lava','core',NULL,'authPermission','section','details','i','suggest','Yes','Section',NULL,25,NULL,0,NULL,NULL,NULL,6,'the section that the permission covers','2009-01-25 05:28:51'),(2535,'*.authPermission.target','en','lava','core',NULL,'authPermission','target','details','i','suggest','Yes','Target',NULL,25,NULL,0,NULL,NULL,NULL,7,'the target that the permission covers','2009-01-25 05:28:51'),(2536,'*.authPermission.mode','en','lava','core',NULL,'authPermission','mode','details','i','suggest','Yes','Mode',NULL,25,NULL,0,NULL,NULL,NULL,8,'the mode that the permission covers','2009-01-25 05:28:51'),(2537,'*.authPermission.notes','en','lava','core',NULL,'authPermission','notes','note','i','text','No','Notes',NULL,100,NULL,0,'rows=\"2\" cols=\"40\"',NULL,NULL,9,'Notes','2009-01-25 05:28:51'),(2538,'*.authRole.id','en','lava','core',NULL,'authRole','id',NULL,'c','numeric','Yes','Role ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the role','2009-01-25 05:28:51'),(2539,'*.authRole.roleName','en','lava','core',NULL,'authRole','roleName',NULL,'i','string','Yes','Role Name',NULL,25,NULL,0,NULL,NULL,NULL,2,'Name of the role','2009-01-25 05:28:51'),(2540,'*.authRole.patientAccess','en','lava','core',NULL,'authRole','patientAccess',NULL,'i','range','Yes','Patient Access',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,3,'This role confers patient access to the user','2009-01-25 05:28:51'),(2541,'*.authRole.phiAccess','en','lava','core',NULL,'authRole','phiAccess',NULL,'i','range','Yes','PHI Access',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,4,'This role confers access to Protected Health Identifiers/Informantion','2009-01-25 05:28:51'),(2542,'*.authRole.ghiAccess','en','lava','core',NULL,'authRole','ghiAccess',NULL,'i','range','Yes','Genetic Access',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,5,'This role confers access to genetic health information to the user','2009-01-25 05:28:51'),(2543,'*.authRole.notes','en','lava','core',NULL,'authRole','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,8,'Notes','2009-01-25 05:28:51'),(2544,'*.authRole.notes','en','lava','core',NULL,'authRole','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,8,'Notes','2009-01-25 05:28:51'),(2545,'*.authRole.summaryInfo','en','lava','core',NULL,'authRole','summaryInfo','details','c','string','No','Summary',NULL,255,NULL,0,NULL,NULL,NULL,13,'Summary information for the role','2009-01-01 08:00:00'),(2546,'*.authUser.id','en','lava','core',NULL,'authUser','id',NULL,'c','numeric','Yes','User ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the user','2009-01-25 05:28:51'),(2547,'*.authUser.role.id','en','lava','core',NULL,'authUser','role.id','details','c','numeric','Yes','Role ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID of the role','2009-01-25 05:28:51'),(2548,'*.authUser.userName','en','lava','core',NULL,'authUser','userName','details','i','string','Yes','User Name',NULL,50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2549,'*.authUser.userNameWithStatus','en','lava','core',NULL,'authUser','userNameWithStatus',NULL,'c','string','Yes','User Name',NULL,50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name] with status','2009-01-25 05:28:51'),(2550,'filter.authUser.userName','en','lava','core','filter','authUser','userName',NULL,'i','string','No','User Name',NULL,50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2551,'*.authUser.login','en','lava','core',NULL,'authUser','login','details','i','string','No','Network Login',NULL,100,NULL,0,NULL,NULL,NULL,3,'Network Login for the user','2009-01-25 05:28:51'),(2552,'filter.authUser.login','en','lava','core','filter','authUser','login',NULL,'i','string','No','Network Login',NULL,100,NULL,0,NULL,NULL,NULL,3,'Network Login for the user','2009-01-25 05:28:51'),(2553,'*.authUser.email','en','lava','core',NULL,'authUser','email','details','i','text','No','Email',NULL,100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,4,'Email','2009-05-12 21:45:03'),(2554,'*.authUser.phone','en','lava','core',NULL,'authUser','phone','details','i','string','No','Phone',NULL,25,NULL,0,NULL,NULL,NULL,5,'Phone','2009-05-12 21:45:03'),(2555,'*.authUser.accessAgreementDate','en','lava','core',NULL,'authUser','accessAgreementDate','status','i','date','No','Access Agreement Date',NULL,NULL,NULL,0,NULL,NULL,NULL,7,'Date the user signed an Access Agreement for using the system','2009-01-25 05:28:51'),(2556,'filter.authUser.accessAgreementDateEnd','en','lava','core','filter','authUser','accessAgreementDateEnd',NULL,'i','date','No','and',NULL,NULL,NULL,0,NULL,NULL,NULL,7,'Date the user signed an Access Agreement for using the system','2009-01-25 05:28:51'),(2557,'filter.authUser.accessAgreementDateStart','en','lava','core','filter','authUser','accessAgreementDateStart',NULL,'i','date','No','Agreement Date Between',NULL,NULL,NULL,0,NULL,NULL,NULL,7,'Date the user signed an Access Agreement for using the system','2009-01-25 05:28:51'),(2558,'*.authUser.effectiveDate','en','lava','core',NULL,'authUser','effectiveDate','status','i','date','Yes','Effective Date',NULL,NULL,NULL,0,NULL,NULL,NULL,8,'Effective Date','2009-01-25 05:28:51'),(2559,'filter.authUser.effectiveDateEnd','en','lava','core','filter','authUser','effectiveDateEnd',NULL,'i','date','No','and',NULL,NULL,NULL,0,NULL,NULL,NULL,8,'Effective Date','2009-01-25 05:28:51'),(2560,'filter.authUser.effectiveDateStart','en','lava','core','filter','authUser','effectiveDateStart',NULL,'i','date','No','Effective Date Between',NULL,NULL,NULL,0,NULL,NULL,NULL,8,'Effective Date','2009-01-25 05:28:51'),(2561,'*.authUser.expirationDate','en','lava','core',NULL,'authUser','expirationDate','status','i','date','No','Expiration Date',NULL,NULL,NULL,0,NULL,NULL,NULL,9,'Expiration Date','2009-01-25 05:28:51'),(2562,'filter.authUser.expirationDateEnd','en','lava','core','filter','authUser','expirationDateEnd',NULL,'i','date','No','and',NULL,NULL,NULL,0,NULL,NULL,NULL,9,'Expiration Date','2009-01-25 05:28:51'),(2563,'filter.authUser.expirationDateStart','en','lava','core','filter','authUser','expirationDateStart',NULL,'i','date','No','Expiration Date Between',NULL,NULL,NULL,0,NULL,NULL,NULL,9,'Expiration Date','2009-01-25 05:28:51'),(2564,'*.authUser.shortUserName','en','lava','core',NULL,'authUser','shortUserName','details','c','string','No','Short User Name',NULL,53,NULL,0,NULL,NULL,NULL,10,'Shortened User name','2009-01-25 05:28:51'),(2565,'*.authUser.shortUserNameRev','en','lava','core',NULL,'authUser','shortUserNameRev','details','c','string','No','Short User Name Reversed',NULL,54,NULL,0,NULL,NULL,NULL,11,'Shortened User Name Reversed','2009-01-25 05:28:51'),(2566,'*.authUser.notes','en','lava','core',NULL,'authUser','notes','note','i','text','No','Notes',NULL,255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,12,'Notes','2009-01-25 05:28:51'),(2567,'*.authUser.authenticationType','en','lava','core',NULL,'authUser','authenticationType','details','i','range','Yes','Auth Type',NULL,10,NULL,0,NULL,'authUser.authenticationType',NULL,12,'Authentication Type','2009-05-12 21:45:03'),(2568,'*.authUser.disabled','en','lava','core',NULL,'authUser','disabled','status','i','range','Yes','Disabled',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,13,'Disabled','2009-01-25 05:28:51'),(2569,'*.authUser.password','en','lava','core',NULL,'authUser','password','local','c','text','No','Password (hashed)',NULL,100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,13,'Password Hash','2009-05-12 21:45:03'),(2570,'*.authUser.passwordExpiration','en','lava','core',NULL,'authUser','passwordExpiration','local','c','timestamp','No','Password Exp.',NULL,NULL,NULL,0,NULL,NULL,NULL,14,'Password Expiration','2009-05-12 21:45:03'),(2571,'*.authUser.passwordResetToken','en','lava','core',NULL,'authUser','passwordResetToken','local','c','text','No','Reset Token',NULL,100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,15,'Password Reset Token','2009-05-12 21:45:03'),(2572,'*.authUser.passwordResetExpiration','en','lava','core',NULL,'authUser','passwordResetExpiration','local','c','timestamp','No','Reset Exp.',NULL,NULL,NULL,0,NULL,NULL,NULL,16,'Password Reset Token Expires','2009-05-12 21:45:03'),(2573,'*.authUser.failedLoginCount','en','lava','core',NULL,'authUser','failedLoginCount','local','c','numeric','No','Failed Logins',NULL,NULL,NULL,0,NULL,NULL,NULL,17,'Failed Login Attempts','2009-05-12 21:45:03'),(2574,'*.authUser.lastFailedLogin','en','lava','core',NULL,'authUser','lastFailedLogin','local','c','timestamp','No','Last Failed Login',NULL,NULL,NULL,0,NULL,NULL,NULL,18,'Last Failed Logon Attempt','2009-05-12 21:45:03'),(2575,'*.authUser.accountLocked','en','lava','core',NULL,'authUser','accountLocked','local','c','timestamp','No','Account Locked',NULL,NULL,NULL,0,NULL,NULL,NULL,19,'Account Locked Timestamp','2009-05-12 21:45:03'),(2576,'*.authUserGroup.group.id','en','lava','core',NULL,'authUserGroup','group.id',NULL,'c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the authorization group','2009-01-25 05:28:51'),(2577,'*.authUserGroup.id','en','lava','core',NULL,'authUserGroup','id',NULL,'c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the User Role Assocaition','2009-01-25 05:28:51'),(2578,'*.authUserGroup.user.id','en','lava','core',NULL,'authUserGroup','user.id','details','c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique Record ID','2009-01-25 05:28:51'),(2579,'*.authUserGroup.user.id','en','lava','core',NULL,'authUserGroup','user.id','details','c','numeric','Yes','User ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID of the User','2009-01-25 05:28:51'),(2580,'*.authUserGroup.group.groupNameWithStatus','en','lava','core',NULL,'authUserGroup','group.groupNameWithStatus',NULL,'i','string','Yes','Group Name',NULL,50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-25 05:28:51'),(2581,'*.authUserGroup.user.login','en','lava','core',NULL,'authUserGroup','user.login','details','i','string','Yes','User Name',NULL,50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2582,'*.authUserGroup.user.userNameWithStatus','en','lava','core',NULL,'authUserGroup','user.userNameWithStatus','details','i','string','Yes','User Name',NULL,50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2583,'*.authUserGroup.userId','en','lava','core',NULL,'authUserGroup','userId','details','i','range','Yes','User',NULL,NULL,NULL,0,NULL,'auth.users',NULL,2,'Unique ID of the User','2009-01-25 05:28:51'),(2584,'*.authUserGroup.groupId','en','lava','core',NULL,'authUserGroup','groupId','details','i','range','Yes','Group',NULL,NULL,NULL,0,NULL,'auth.groups',NULL,3,'Unique ID of the Group','2009-01-25 05:28:51'),(2585,'*.authUserGroup.notes','en','lava','core',NULL,'authUserGroup','notes','note','i','text','No','Notes',NULL,255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,6,'Notes','2009-01-25 05:28:51'),(2586,'*.authUserPasswordDto.oldPassword','en','lava','core',NULL,'authUserPasswordDto','oldPassword',NULL,'i','password','Yes','Current Password',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Current Password','2009-05-12 21:45:03'),(2587,'*.authUserPasswordDto.newPassword','en','lava','core',NULL,'authUserPasswordDto','newPassword',NULL,'i','password','Yes','New Password',NULL,NULL,NULL,0,NULL,NULL,NULL,2,'New Password','2009-05-12 21:45:03'),(2588,'*.authUserPasswordDto.newPasswordConfirm','en','lava','core',NULL,'authUserPasswordDto','newPasswordConfirm',NULL,'i','password','Yes','New Password Confirm',NULL,NULL,NULL,0,NULL,NULL,NULL,3,'New Password Confirm','2009-05-12 21:45:03'),(2589,'*.authUserRole.group.id','en','lava','core',NULL,'authUserRole','group.id',NULL,'c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the authorization group','2009-01-25 05:28:51'),(2590,'*.authUserRole.id','en','lava','core',NULL,'authUserRole','id','details','c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID for the User/Group Role Association','2009-01-25 05:28:51'),(2591,'*.authUserRole.role.id','en','lava','core',NULL,'authUserRole','role.id',NULL,'c','numeric','Yes','Role ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique id for the role','2009-01-25 05:28:51'),(2592,'*.authUserRole.user.id','en','lava','core',NULL,'authUserRole','user.id',NULL,'c','numeric','Yes','User ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,'Unique ID of the User','2009-01-25 05:28:51'),(2593,'*.authUserRole.group.groupNameWithStatus','en','lava','core',NULL,'authUserRole','group.groupNameWithStatus',NULL,'i','string','Yes','Group Name',NULL,50,50,0,NULL,NULL,NULL,2,'Name of the authorization group','2009-01-25 05:28:51'),(2594,'*.authUserRole.role.roleName','en','lava','core',NULL,'authUserRole','role.roleName',NULL,'i','string','Yes','Role Name',NULL,25,NULL,0,NULL,NULL,NULL,2,'Name of the role','2009-01-25 05:28:51'),(2595,'*.authUserRole.user.userNameWithStatus','en','lava','core',NULL,'authUserRole','user.userNameWithStatus',NULL,'i','string','Yes','User Name',NULL,50,NULL,0,NULL,NULL,NULL,2,'Name of the User [First Name Last Name]','2009-01-25 05:28:51'),(2596,'*.authUserRole.role.summaryInfo','en','lava','core',NULL,'authUserRole','role.summaryInfo',NULL,'i','range','Yes','Summary',NULL,255,NULL,0,NULL,NULL,NULL,3,'Summary info about the role','2009-01-25 05:28:51'),(2597,'*.authUserRole.roleId','en','lava','core',NULL,'authUserRole','roleId','details','i','range','Yes','Role',NULL,25,NULL,0,NULL,'auth.roles',NULL,3,'The role name','2009-01-25 05:28:51'),(2598,'*.authUserRole.userId','en','lava','core',NULL,'authUserRole','userId','details','i','range','No','User',NULL,NULL,NULL,0,NULL,'auth.users',NULL,7,'The user to assign the role to','2009-01-25 05:28:51'),(2599,'*.authUserRole.groupId','en','lava','core',NULL,'authUserRole','groupId','details','i','range','No','Group',NULL,NULL,NULL,0,NULL,'auth.groups',NULL,8,'the group to assign the role to','2009-01-25 05:28:51'),(2600,'*.authUserRole.notes','en','lava','core',NULL,'authUserRole','notes','note','i','text','No','Notes',NULL,255,NULL,0,'rows=\"3\" cols=\"40\"',NULL,NULL,12,'Notes','2009-01-25 05:28:51'),(2601,'*.authUserRole.summaryInfo','en','lava','core','','authUserRole','summaryInfo','details','c','string','No','Summary',NULL,255,NULL,0,NULL,NULL,'',13,'Summary information for the role assignment','2009-01-01 08:00:00'),(2602,'*.calendar.id','en','lava','core',NULL,'calendar','id','details','c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2009-03-31 17:56:31'),(2603,'*.calendar.type','en','lava','core','','calendar','type','details','c','range','Yes','Type',NULL,25,NULL,0,NULL,'calendar.type','',2,'Type','2009-06-10 07:00:00'),(2604,'*.calendar.name','en','lava','core',NULL,'calendar','name','details','i','string','Yes','Name',NULL,100,NULL,0,NULL,NULL,NULL,3,NULL,'2009-03-31 17:56:31'),(2605,'*.calendar.description','en','lava','core',NULL,'calendar','description','details','i','text','No','Description',NULL,255,NULL,0,'rows=\"4\" cols=\"45\"',NULL,NULL,4,NULL,'2009-03-31 17:56:31'),(2606,'*.calendar.notes','en','lava','core',NULL,'calendar','notes','notes','i','unlimitedtext','No','Notes',NULL,NULL,NULL,0,'rows=\"10\" cols=\"45\"',NULL,NULL,5,NULL,'2009-03-31 17:56:31'),(2607,'*.lavaFile.id','en','lava','core',NULL,'lavaFile','id','fileInfo','c','numeric','Yes','ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2011-10-20 22:03:20'),(2608,'*.lavaFile.statusBlock','en','lava','core',NULL,'lavaFile','statusBlock','summary','c','text','No','Status',NULL,NULL,NULL,0,'rows=\"2\" cols=\"80\"',NULL,NULL,1,NULL,'2011-11-05 03:35:36'),(2609,'*.lavaFile.name','en','lava','core',NULL,'lavaFile','name','fileInfo','i','string','No','File Name',NULL,255,40,0,NULL,NULL,NULL,2,NULL,'2011-10-20 22:03:20'),(2610,'*.lavaFile.fileType','en','lava','core',NULL,'lavaFile','fileType','fileInfo','c','suggest','No','File Type',NULL,255,40,0,NULL,'lavaFile.fileType',NULL,3,NULL,'2011-10-20 22:03:20'),(2611,'*.lavaFile.contentType','en','lava','core',NULL,'lavaFile','contentType','fileInfo','i','suggest','No','File Contents',NULL,100,40,0,NULL,'lavaFile.contentType',NULL,4,NULL,'2011-10-20 22:03:20'),(2612,'*.lavaFile.fileStatusDate','en','lava','core',NULL,'lavaFile','fileStatusDate','status','i','date','No','Status Date',NULL,NULL,NULL,0,NULL,NULL,NULL,5,NULL,'2011-10-20 22:03:20'),(2613,'*.lavaFile.fileStatus','en','lava','core',NULL,'lavaFile','fileStatus','status','i','range','No','Status',NULL,50,NULL,0,NULL,'lavaFile.status',NULL,6,NULL,'2011-10-20 22:03:20'),(2614,'*.lavaFile.fileStatusBy','en','lava','core',NULL,'lavaFile','fileStatusBy','status','i','range','No','Status By',NULL,50,NULL,0,NULL,'lavaFile.statusBy',NULL,7,NULL,'2011-10-20 22:03:20'),(2615,'*.lavaFile.repositoryId','en','lava','core',NULL,'lavaFile','repositoryId','repositoryInfo','c','string','No','Repository ID',NULL,100,40,0,NULL,NULL,NULL,8,NULL,'2011-10-20 22:03:20'),(2616,'*.lavaFile.fileId','en','lava','core',NULL,'lavaFile','fileId','repositoryInfo','c','string','No','File ID',NULL,100,40,0,NULL,NULL,NULL,9,NULL,'2011-10-20 22:03:20'),(2617,'*.lavaFile.location','en','lava','core',NULL,'lavaFile','location','repositoryInfo','c','text','No','Location',NULL,1000,40,0,'rows=\"3\" cols=\"80\"',NULL,NULL,10,NULL,'2011-10-20 22:03:20'),(2618,'*.lavaFile.checksum','en','lava','core',NULL,'lavaFile','checksum','fileInfo','c','string','No','Checksum',NULL,100,40,0,NULL,NULL,NULL,11,NULL,'2011-10-20 22:03:20'),(2619,'*.lavaSession.id','en','lava','core',NULL,'LavaSession','id',NULL,'c','numeric','Yes','Session ID',NULL,NULL,NULL,0,NULL,NULL,NULL,1,NULL,'2009-01-25 05:28:51'),(2620,'*.lavaSession.serverInstanceId','en','lava','core',NULL,'LavaSession','serverInstanceId',NULL,'c','numeric','Yes','Server Instance ID',NULL,NULL,NULL,0,NULL,NULL,NULL,2,NULL,'2009-01-25 05:28:51'),(2621,'*.lavaSession.createTimestamp','en','lava','core',NULL,'LavaSession','createTimestamp',NULL,'c','datetime','Yes','Created Time',NULL,NULL,NULL,0,NULL,NULL,NULL,3,NULL,'2009-01-25 05:28:51'),(2622,'*.lavaSession.accessTimestamp','en','lava','core',NULL,'LavaSession','accessTimestamp',NULL,'c','datetime','Yes','Accessed Time',NULL,NULL,NULL,0,NULL,NULL,NULL,4,NULL,'2009-01-25 05:28:51'),(2623,'*.lavaSession.expireTimestamp','en','lava','core',NULL,'LavaSession','expireTimestamp',NULL,'c','datetime','Yes','Expiration Time',NULL,NULL,NULL,0,NULL,NULL,NULL,5,NULL,'2009-01-25 05:28:51'),(2624,'*.lavaSession.currentStatus','en','lava','core',NULL,'LavaSession','currentStatus',NULL,'c','range','Yes','Current Status',NULL,NULL,NULL,0,NULL,'lavaSession.status',NULL,6,NULL,'2009-01-25 05:28:51'),(2625,'*.lavaSession.userId','en','lava','core',NULL,'LavaSession','userId',NULL,'c','numeric','No','User ID',NULL,NULL,NULL,0,NULL,NULL,NULL,7,NULL,'2009-01-25 05:28:51'),(2626,'*.lavaSession.username','en','lava','core',NULL,'LavaSession','username',NULL,'c','string','No','Username',NULL,NULL,NULL,0,NULL,NULL,NULL,8,NULL,'2009-01-25 05:28:51'),(2627,'*.lavaSession.hostname','en','lava','core',NULL,'LavaSession','hostname',NULL,'c','string','No','Hostname',NULL,NULL,NULL,0,NULL,NULL,NULL,9,NULL,'2009-01-25 05:28:51'),(2628,'*.lavaSession.httpSessionId','en','lava','core',NULL,'LavaSession','httpSessionId',NULL,'c','string','No','HTTP Session',NULL,NULL,40,0,NULL,NULL,NULL,10,NULL,'2009-01-25 05:28:51'),(2629,'*.lavaSession.disconnectTime','en','lava','core',NULL,'LavaSession','disconnectTime',NULL,'i','time','No','Time',NULL,NULL,NULL,0,NULL,NULL,NULL,11,NULL,'2009-01-25 05:28:51'),(2630,'*.lavaSession.disconnectMessage','en','lava','core',NULL,'LavaSession','disconnectMessage',NULL,'i','text','No','Disconnect Message',NULL,NULL,NULL,0,'rows=\"4\", cols=\"45\"',NULL,NULL,12,NULL,'2009-01-25 05:28:51'),(2631,'*.lavaSession.notes','en','lava','core',NULL,'LavaSession','notes',NULL,'i','text','No','Notes',NULL,NULL,NULL,0,'rows=\"4\" cols=\"45\"',NULL,NULL,13,NULL,'2009-01-25 05:28:51'),(2632,'*.lavaSession.disconnectDate','en','lava','core',NULL,'lavaSession','disconnectDate',NULL,'i','date','No','Disconnect Date',NULL,10,NULL,0,NULL,NULL,NULL,16,NULL,'2009-04-27 22:11:44'),(2633,'*.reservation.organizer.userName','en','lava','core',NULL,'reservation','organizer.userName','details','c','string','No','Reserved By',NULL,NULL,NULL,0,NULL,NULL,NULL,14,'Reserved By','2009-06-11 16:40:26'),(2634,'*.reservation.organizerId','en','lava','core',NULL,'reservation','organizerId','details','i','range','Yes','Reserved By',NULL,NULL,NULL,0,NULL,'appointment.organizer',NULL,15,'Reserved By','2009-06-11 16:37:29'),(2635,'*.resourceCalendar.resourceType','en','lava','core',NULL,'resourceCalendar','resource_type','resource','i','range','Yes','Resource Type',NULL,25,NULL,0,NULL,'resourceCalendar.resourceType',NULL,2,'Resource Type','2009-06-03 17:27:39'),(2636,'*.resourceCalendar.location','en','lava','core',NULL,'resourceCalendar','location','resource','i','text','No','Location',NULL,100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,3,'Location','2009-06-03 17:27:39'),(2637,'*.resourceCalendar.contactId','en','lava','core',NULL,'resourceCalendar','contactId','resource','i','range','Yes','Contact',NULL,NULL,NULL,0,NULL,'resourceCalendar.contact',NULL,4,'Contact','2009-06-03 17:27:39'),(2638,'*.resourceCalendar.contact.email','en','lava','core',NULL,'resourceCalendar','contact.email','resource','c','string','No','Contact Email',NULL,100,NULL,0,NULL,NULL,NULL,6,'Contact Email','2009-06-03 17:27:39'),(2639,'*.resourceCalendar.contact.phone','en','lava','core',NULL,'resourceCalendar','contact.phone','resource','c','string','No','Contact Phone',NULL,25,NULL,0,NULL,NULL,NULL,7,'Contact Phone','2009-06-03 17:27:39'),(2640,'*.userInfo.email','en','lava','core',NULL,'userInfo','email','details','i','text','No','Email',NULL,100,NULL,0,'rows=\"2\" cols=\"25\"',NULL,NULL,1,'Email','2009-05-26 23:48:36'),(2641,'*.userInfo.phone','en','lava','core',NULL,'userInfo','phone','details','i','string','No','Phone',NULL,25,NULL,0,NULL,NULL,NULL,2,'Phone','2009-05-26 23:49:22');
/*!40000 ALTER TABLE `viewproperty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'lava_core'
--
DELIMITER ;;
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
/*!50003 DROP PROCEDURE IF EXISTS `lq_get_objects` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `lq_get_objects`(user_name varchar(50), host_name varchar(25))
BEGINSELECT concat(`instance`,'_',`scope`,'_',`module`) as query_source,concat(`section`,'_',`target`) as query_object_name , `short_desc`, `standard`,`primary_link`,`secondary_link` from `query_objects`;END */;;
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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-24 22:42:28
