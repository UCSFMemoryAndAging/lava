SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';


-- -----------------------------------------------------
-- Table `audit_event_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_event_history` ;

CREATE  TABLE IF NOT EXISTS `audit_event_history` (
  `audit_event_id` INT(10) NOT NULL AUTO_INCREMENT ,
  `audit_user` VARCHAR(50) NOT NULL ,
  `audit_host` VARCHAR(25) NOT NULL ,
  `audit_timestamp` TIMESTAMP NULL DEFAULT NULL ,
  `action` VARCHAR(255) NOT NULL ,
  `action_event` VARCHAR(50) NOT NULL ,
  `action_id_param` VARCHAR(50) NULL DEFAULT NULL ,
  `event_note` VARCHAR(255) NULL DEFAULT NULL ,
  `exception` VARCHAR(255) NULL DEFAULT NULL ,
  `exception_message` VARCHAR(255) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`audit_event_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `audit_entity_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_entity_history` ;

CREATE  TABLE IF NOT EXISTS `audit_entity_history` (
  `audit_entity_id` INT(10) NOT NULL AUTO_INCREMENT ,
  `audit_event_id` INT(10) NOT NULL ,
  `entity_id` INT(10) NOT NULL ,
  `entity` VARCHAR(100) NOT NULL ,
  `entity_type` VARCHAR(100) NOT NULL ,
  `audit_type` VARCHAR(10) NOT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`audit_entity_id`) ,
  INDEX `audit_entity_history__audit_event_id` (`audit_event_id` ASC) ,
  CONSTRAINT `audit_entity_history__audit_event_id`
    FOREIGN KEY (`audit_event_id` )
    REFERENCES `audit_event_history` (`audit_event_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `audit_event_work`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_event_work` ;

CREATE  TABLE IF NOT EXISTS `audit_event_work` (
  `audit_event_id` INT(10) NOT NULL AUTO_INCREMENT ,
  `audit_user` VARCHAR(50) NOT NULL ,
  `audit_host` VARCHAR(25) NOT NULL ,
  `audit_timestamp` TIMESTAMP NULL DEFAULT NULL ,
  `action` VARCHAR(255) NOT NULL ,
  `action_event` VARCHAR(50) NOT NULL ,
  `action_id_param` VARCHAR(50) NULL DEFAULT NULL ,
  `event_note` VARCHAR(255) NULL DEFAULT NULL ,
  `exception` VARCHAR(255) NULL DEFAULT NULL ,
  `exception_message` VARCHAR(255) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`audit_event_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 1071
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `audit_entity_work`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_entity_work` ;

CREATE  TABLE IF NOT EXISTS `audit_entity_work` (
  `audit_entity_id` INT(10) NOT NULL AUTO_INCREMENT ,
  `audit_event_id` INT(10) NOT NULL ,
  `entity_id` INT(10) NOT NULL ,
  `entity` VARCHAR(100) NOT NULL ,
  `entity_type` VARCHAR(100) NULL DEFAULT NULL ,
  `audit_type` VARCHAR(10) NOT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`audit_entity_id`) ,
  INDEX `audit_entity_work__audit_event_id` (`audit_event_id` ASC) ,
  CONSTRAINT `audit_entity_work__audit_event_id`
    FOREIGN KEY (`audit_event_id` )
    REFERENCES `audit_event_work` (`audit_event_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 103
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `audit_property_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_property_history` ;

CREATE  TABLE IF NOT EXISTS `audit_property_history` (
  `audit_property_id` INT(10) NOT NULL AUTO_INCREMENT ,
  `audit_entity_id` INT(10) NOT NULL ,
  `property` VARCHAR(100) NOT NULL ,
  `index_key` VARCHAR(100) NULL DEFAULT NULL ,
  `subproperty` VARCHAR(255) NULL DEFAULT NULL ,
  `old_value` VARCHAR(255) NOT NULL ,
  `new_value` VARCHAR(255) NOT NULL ,
  `audit_timestamp` TIMESTAMP NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`audit_property_id`) ,
  INDEX `audit_property_history__audit_entity_id` (`audit_entity_id` ASC) ,
  CONSTRAINT `audit_property_history__audit_entity_id`
    FOREIGN KEY (`audit_entity_id` )
    REFERENCES `audit_entity_history` (`audit_entity_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `audit_property_work`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_property_work` ;

CREATE  TABLE IF NOT EXISTS `audit_property_work` (
  `audit_property_id` INT(10) NOT NULL AUTO_INCREMENT ,
  `audit_entity_id` INT(10) NOT NULL ,
  `property` VARCHAR(100) NOT NULL ,
  `index_key` VARCHAR(100) NULL DEFAULT NULL ,
  `subproperty` VARCHAR(255) NULL DEFAULT NULL ,
  `old_value` VARCHAR(255) NOT NULL ,
  `new_value` VARCHAR(255) NOT NULL ,
  `audit_timestamp` TIMESTAMP NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`audit_property_id`) ,
  INDEX `audit_property_work__audit_entity_id` (`audit_entity_id` ASC) ,
  CONSTRAINT `audit_property_work__audit_entity_id`
    FOREIGN KEY (`audit_entity_id` )
    REFERENCES `audit_entity_work` (`audit_entity_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 328
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `audit_text_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_text_history` ;

CREATE  TABLE IF NOT EXISTS `audit_text_history` (
  `audit_property_id` INT(10) NOT NULL ,
  `old_text` TEXT NULL DEFAULT NULL ,
  `new_text` TEXT NULL DEFAULT NULL ,
  PRIMARY KEY (`audit_property_id`) ,
  INDEX `audit_text_history__audit_property_id` (`audit_property_id` ASC) ,
  CONSTRAINT `audit_text_history__audit_property_id`
    FOREIGN KEY (`audit_property_id` )
    REFERENCES `audit_property_history` (`audit_property_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `audit_text_work`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_text_work` ;

CREATE  TABLE IF NOT EXISTS `audit_text_work` (
  `audit_property_id` INT(10) NOT NULL ,
  `old_text` TEXT NULL DEFAULT NULL ,
  `new_text` TEXT NULL DEFAULT NULL ,
  PRIMARY KEY (`audit_property_id`) ,
  INDEX `audit_text_work__audit_property_id` (`audit_property_id` ASC) ,
  CONSTRAINT `audit_text_work__audit_property_id`
    FOREIGN KEY (`audit_property_id` )
    REFERENCES `audit_property_work` (`audit_property_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `authgroup`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `authgroup` ;

CREATE  TABLE IF NOT EXISTS `authgroup` (
  `GID` INT(10) NOT NULL AUTO_INCREMENT ,
  `GroupName` VARCHAR(50) NOT NULL ,
  `EffectiveDate` DATE NOT NULL ,
  `ExpirationDate` DATE NULL DEFAULT NULL ,
  `Notes` VARCHAR(255) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`GID`) )
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `authrole`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `authrole` ;

CREATE  TABLE IF NOT EXISTS `authrole` (
  `RoleID` INT(10) NOT NULL AUTO_INCREMENT ,
  `RoleName` VARCHAR(25) NOT NULL ,
  `Notes` VARCHAR(255) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`RoleID`) )
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `authpermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `authpermission` ;

CREATE  TABLE IF NOT EXISTS `authpermission` (
  `PermID` INT(10) NOT NULL AUTO_INCREMENT ,
  `RoleID` INT(10) NOT NULL ,
  `PermitDeny` VARCHAR(10) NOT NULL ,
  `Scope` VARCHAR(50) NOT NULL ,
  `Module` VARCHAR(50) NOT NULL ,
  `Section` VARCHAR(50) NOT NULL ,
  `Target` VARCHAR(50) NOT NULL ,
  `Mode` VARCHAR(25) NOT NULL ,
  `Notes` VARCHAR(100) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`PermID`) ,
  INDEX `authpermission_RoleID` (`RoleID` ASC) ,
  CONSTRAINT `authpermission_RoleID`
    FOREIGN KEY (`RoleID` )
    REFERENCES `authrole` (`RoleID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 54
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `authuser`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `authuser` ;

CREATE  TABLE IF NOT EXISTS `authuser` (
  `UID` INT(10) NOT NULL AUTO_INCREMENT ,
  `UserName` VARCHAR(50) NOT NULL ,
  `Login` VARCHAR(100) NULL DEFAULT NULL ,
  `email` VARCHAR(100) NULL DEFAULT NULL ,
  `phone` VARCHAR(25) NULL DEFAULT NULL ,
  `AccessAgreementDate` DATE NULL DEFAULT NULL ,
  `ShortUserName` VARCHAR(50) NULL DEFAULT NULL ,
  `ShortUserNameRev` VARCHAR(50) NULL DEFAULT NULL ,
  `EffectiveDate` DATE NOT NULL ,
  `ExpirationDate` DATE NULL DEFAULT NULL ,
  `Notes` VARCHAR(255) NULL DEFAULT NULL ,
  `authenticationType` VARCHAR(10) NULL DEFAULT 'EXTERNAL' ,
  `password` VARCHAR(100) NULL DEFAULT NULL ,
  `passwordExpiration` TIMESTAMP NULL DEFAULT NULL ,
  `passwordResetToken` VARCHAR(100) NULL DEFAULT NULL ,
  `passwordResetExpiration` TIMESTAMP NULL DEFAULT NULL ,
  `failedLoginCount` SMALLINT NULL DEFAULT NULL ,
  `lastFailedLogin` TIMESTAMP NULL DEFAULT NULL ,
  `accountLocked` TIMESTAMP NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`UID`) ,
  UNIQUE INDEX `Unique_UserName` (`UserName` ASC) ,
  UNIQUE INDEX `Unique_Login` (`Login` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `authusergroup`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `authusergroup` ;

CREATE  TABLE IF NOT EXISTS `authusergroup` (
  `UGID` INT(10) NOT NULL AUTO_INCREMENT ,
  `UID` INT(10) NOT NULL ,
  `GID` INT(10) NOT NULL ,
  `Notes` VARCHAR(255) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`UGID`) ,
  INDEX `authusergroup_UID` (`UID` ASC) ,
  INDEX `authusergroup_GID` (`UGID` ASC) ,
  CONSTRAINT `authusergroup_UID`
    FOREIGN KEY (`UID` )
    REFERENCES `authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `authusergroup_GID`
    FOREIGN KEY (`UGID` )
    REFERENCES `authgroup` (`GID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `authuserrole`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `authuserrole` ;

CREATE  TABLE IF NOT EXISTS `authuserrole` (
  `URID` INT(10) NOT NULL AUTO_INCREMENT ,
  `RoleID` INT(10) NOT NULL ,
  `UID` INT(10) NULL DEFAULT NULL ,
  `GID` INT(10) NULL DEFAULT NULL ,
  `Notes` VARCHAR(255) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`URID`) ,
  INDEX `authuserrole_RoleID` (`RoleID` ASC) ,
  INDEX `authuserrole_UID` (`URID` ASC) ,
  INDEX `authuserrole_GID` (`GID` ASC) ,
  CONSTRAINT `authuserrole_RoleID`
    FOREIGN KEY (`RoleID` )
    REFERENCES `authrole` (`RoleID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `authuserrole_UID`
    FOREIGN KEY (`URID` )
    REFERENCES `authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `authuserrole_GID`
    FOREIGN KEY (`GID` )
    REFERENCES `authgroup` (`GID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `hibernateproperty`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hibernateproperty` ;

CREATE  TABLE IF NOT EXISTS `hibernateproperty` (
  `id` INT(10) NOT NULL AUTO_INCREMENT ,
  `instance` VARCHAR(25) NOT NULL DEFAULT 'lava' ,
  `scope` VARCHAR(25) NOT NULL ,
  `entity` VARCHAR(100) NOT NULL ,
  `property` VARCHAR(100) NOT NULL ,
  `dbTable` VARCHAR(50) NOT NULL ,
  `dbColumn` VARCHAR(50) NOT NULL ,
  `dbType` VARCHAR(50) NULL DEFAULT NULL ,
  `dbLength` SMALLINT(5) NULL DEFAULT NULL ,
  `dbPrecision` SMALLINT(5) NULL DEFAULT NULL ,
  `dbScale` SMALLINT(5) NULL DEFAULT NULL ,
  `dbOrder` SMALLINT(5) NULL DEFAULT NULL ,
  `hibernateProperty` VARCHAR(50) NULL DEFAULT NULL ,
  `hibernateType` VARCHAR(50) NULL DEFAULT NULL ,
  `hibernateClass` VARCHAR(250) NULL DEFAULT NULL ,
  `hibernateNotNull` VARCHAR(50) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 1625
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lavaserverinstance`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lavaserverinstance` ;

CREATE  TABLE IF NOT EXISTS `lavaserverinstance` (
  `ServerInstanceID` INT(10) NOT NULL AUTO_INCREMENT ,
  `ServerDescription` VARCHAR(255) NULL DEFAULT NULL ,
  `CreateTime` TIMESTAMP NULL DEFAULT NULL ,
  `DisconnectTime` DATETIME NULL DEFAULT NULL ,
  `DisconnectWarningMinutes` INT(10) NULL DEFAULT NULL ,
  `DisconnectMessage` VARCHAR(500) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`ServerInstanceID`) )
ENGINE = InnoDB
AUTO_INCREMENT = 79
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_session`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_session` ;

CREATE  TABLE IF NOT EXISTS `lava_session` (
  `lava_session_id` INT(10) NOT NULL AUTO_INCREMENT ,
  `server_instance_id` INT(10) NOT NULL ,
  `http_session_id` VARCHAR(64) NULL DEFAULT NULL ,
  `current_status` VARCHAR(25) NOT NULL DEFAULT 'NEW' ,
  `user_id` INT(10) NULL DEFAULT NULL ,
  `user_name` VARCHAR(50) NULL DEFAULT NULL ,
  `host_name` VARCHAR(50) NULL DEFAULT NULL ,
  `create_timestamp` TIMESTAMP NULL DEFAULT NULL ,
  `access_timestamp` TIMESTAMP NULL DEFAULT NULL ,
  `expire_timestamp` DATETIME NULL DEFAULT NULL ,
  `disconnect_date` DATE NULL DEFAULT NULL ,
  `disconnect_time` TIME NULL DEFAULT NULL ,
  `disconnect_message` VARCHAR(500) NULL DEFAULT NULL ,
  `notes` VARCHAR(255) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`lava_session_id`) ,
  INDEX `lavasession__server_instance_id` (`server_instance_id` ASC) ,
  INDEX `lavasession__user_id` (`user_id` ASC) ,
  CONSTRAINT `lavasession__server_instance_id`
    FOREIGN KEY (`server_instance_id` )
    REFERENCES `lavaserverinstance` (`ServerInstanceID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `lavasession__user_id`
    FOREIGN KEY (`user_id` )
    REFERENCES `authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 243
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `list`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `list` ;

CREATE  TABLE IF NOT EXISTS `list` (
  `ListID` INT(10) NOT NULL AUTO_INCREMENT ,
  `ListName` VARCHAR(50) NOT NULL ,
  `scope` VARCHAR(25) NOT NULL ,
  `NumericKey` TINYINT(1) NOT NULL DEFAULT '0' ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`ListID`) ,
  UNIQUE INDEX `ListName` (`ListName` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 468
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `listvalues`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `listvalues` ;

CREATE  TABLE IF NOT EXISTS `listvalues` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT ,
  `ListID` INT(10) NOT NULL ,
  `ValueKey` VARCHAR(100) NOT NULL ,
  `ValueDesc` VARCHAR(100) NULL DEFAULT NULL ,
  `OrderID` INT(10) NOT NULL DEFAULT '0' ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`ID`) ,
  INDEX `ListID` (`ListID` ASC) ,
  INDEX `ValueKey` (`ValueKey` ASC) ,
  INDEX `listvalues__listID` (`ListID` ASC) ,
  CONSTRAINT `listvalues__listID`
    FOREIGN KEY (`ListID` )
    REFERENCES `list` (`ListID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 24376
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `versionhistory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `versionhistory` ;

CREATE  TABLE IF NOT EXISTS `versionhistory` (
  `Module` VARCHAR(25) NOT NULL ,
  `Version` VARCHAR(10) NOT NULL ,
  `VersionDate` DATETIME NOT NULL ,
  `Major` INT(10) NOT NULL ,
  `Minor` INT(10) NOT NULL ,
  `Fix` INT(10) NOT NULL ,
  `UpdateRequired` TINYINT(1) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (`Module`, `Version`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `viewproperty`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `viewproperty` ;

CREATE  TABLE IF NOT EXISTS `viewproperty` (
  `id` INT(10) NOT NULL AUTO_INCREMENT ,
  `messageCode` VARCHAR(255) NULL DEFAULT NULL ,
  `locale` VARCHAR(10) NOT NULL DEFAULT 'en' ,
  `instance` VARCHAR(25) NOT NULL DEFAULT 'lava' ,
  `scope` VARCHAR(25) NOT NULL ,
  `prefix` VARCHAR(50) NULL DEFAULT NULL ,
  `entity` VARCHAR(100) NOT NULL ,
  `property` VARCHAR(100) NOT NULL ,
  `section` VARCHAR(50) NULL DEFAULT NULL ,
  `context` VARCHAR(10) NULL DEFAULT NULL ,
  `style` VARCHAR(25) NULL DEFAULT NULL ,
  `required` VARCHAR(3) NULL DEFAULT NULL ,
  `label` VARCHAR(500) NULL DEFAULT NULL ,
  `maxLength` SMALLINT(5) NULL DEFAULT NULL ,
  `size` SMALLINT(5) NULL DEFAULT NULL ,
  `indentLevel` SMALLINT(5) NULL DEFAULT '0' ,
  `attributes` VARCHAR(100) NULL DEFAULT NULL ,
  `list` VARCHAR(50) NULL DEFAULT NULL ,
  `listAttributes` VARCHAR(100) NULL DEFAULT NULL ,
  `propOrder` INT(10) NULL DEFAULT NULL ,
  `quickHelp` VARCHAR(500) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 2479
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `calendar`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `calendar` ;

CREATE  TABLE IF NOT EXISTS `calendar` (
  `calendar_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `type` VARCHAR(25) NOT NULL ,
  `name` VARCHAR(100) NOT NULL ,
  `description` VARCHAR(255) NULL DEFAULT NULL ,
  `notes` TEXT NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`calendar_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `appointment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `appointment` ;

CREATE  TABLE IF NOT EXISTS `appointment` (
  `appointment_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `calendar_id` INT UNSIGNED NOT NULL ,
  `organizer_id` INT NOT NULL ,
  `type` VARCHAR(25) NOT NULL ,
  `description` VARCHAR(100) NULL DEFAULT NULL ,
  `location` VARCHAR(100) NULL DEFAULT NULL ,
  `start_date` DATE NOT NULL ,
  `start_time` TIME NOT NULL ,
  `end_date` DATE NOT NULL ,
  `end_time` TIME NOT NULL ,
  `status` VARCHAR(25) NULL DEFAULT NULL ,
  `notes` TEXT NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`appointment_id`) ,
  INDEX `appointment__calendar` (`calendar_id` ASC) ,
  CONSTRAINT `appointment__calendar`
    FOREIGN KEY (`calendar_id` )
    REFERENCES `calendar` (`calendar_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `attendee`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `attendee` ;

CREATE  TABLE IF NOT EXISTS `attendee` (
  `attendee_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `appointment_id` INT UNSIGNED NOT NULL ,
  `user_id` INT(10) NOT NULL ,
  `role` VARCHAR(25) NOT NULL ,
  `status` VARCHAR(25) NOT NULL ,
  `notes` VARCHAR(100) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`attendee_id`) ,
  INDEX `attendee__appointment` (`appointment_id` ASC) ,
  INDEX `attendee__user_id` (`user_id` ASC) ,
  CONSTRAINT `attendee__appointment`
    FOREIGN KEY (`appointment_id` )
    REFERENCES `appointment` (`appointment_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `attendee__user_id`
    FOREIGN KEY (`user_id` )
    REFERENCES `authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `resource_calendar`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `resource_calendar` ;

CREATE  TABLE IF NOT EXISTS `resource_calendar` (
  `calendar_id` INT UNSIGNED NOT NULL ,
  `resource_type` VARCHAR(25) NOT NULL ,
  `location` VARCHAR(100) NULL DEFAULT NULL ,
  `contact_id` INT(10) NOT NULL ,
  PRIMARY KEY (`calendar_id`) ,
  INDEX `resource_calendar__calendar` (`calendar_id` ASC) ,
  INDEX `resource_calendar__user_id` (`contact_id` ASC) ,
  CONSTRAINT `resource_calendar__calendar`
    FOREIGN KEY (`calendar_id` )
    REFERENCES `calendar` (`calendar_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `resource_calendar__user_id`
    FOREIGN KEY (`contact_id` )
    REFERENCES `authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `preference`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `preference` ;

CREATE  TABLE IF NOT EXISTS `preference` (
  `preference_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `user_id` INT(10) NULL DEFAULT NULL ,
  `context` VARCHAR(255) NULL DEFAULT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `description` VARCHAR(1000) NULL DEFAULT NULL ,
  `value` VARCHAR(255) NULL DEFAULT NULL ,
  `visible` INT(11) NOT NULL DEFAULT '1' ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`preference_id`) ,
  INDEX `preference__user_id` (`user_id` ASC) ,
  CONSTRAINT `preference__user_id`
    FOREIGN KEY (`user_id` )
    REFERENCES `authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `query_objects`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `query_objects` ;

CREATE  TABLE IF NOT EXISTS `query_objects` (
  `query_object_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `instance` VARCHAR(50) NOT NULL DEFAULT 'lava' ,
  `scope` VARCHAR(50) NOT NULL ,
  `module` VARCHAR(50) NOT NULL ,
  `section` VARCHAR(50) NOT NULL ,
  `target` VARCHAR(50) NOT NULL ,
  `short_desc` VARCHAR(50) NULL DEFAULT NULL ,
  `standard` TINYINT(4) NOT NULL DEFAULT '1' ,
  `primary_link` TINYINT(4) NOT NULL DEFAULT '1' ,
  `secondary_link` TINYINT(4) NOT NULL DEFAULT '1' ,
  `notes` TEXT NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`query_object_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Placeholder table for view `audit_entity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_entity` (`audit_entity_id` INT, `audit_event_id` INT, `entity_id` INT, `entity` INT, `entity_type` INT, `audit_type` INT, `modified` INT);

-- -----------------------------------------------------
-- Placeholder table for view `audit_event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_event` (`audit_event_id` INT, `audit_user` INT, `audit_host` INT, `audit_timestamp` INT, `action` INT, `action_event` INT, `action_id_param` INT, `event_note` INT, `exception` INT, `exception_message` INT, `modified` INT);

-- -----------------------------------------------------
-- Placeholder table for view `audit_property`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_property` (`audit_property_id` INT, `audit_entity_id` INT, `property` INT, `index_key` INT, `subproperty` INT, `old_value` INT, `new_value` INT, `audit_timestamp` INT, `modified` INT);

-- -----------------------------------------------------
-- Placeholder table for view `audit_text`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_text` (`audit_property_id` INT, `old_text` INT, `new_text` INT);

-- -----------------------------------------------------
-- procedure lq_audit_event
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_audit_event`;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `lq_audit_event`(user_name varchar(50),host_name varchar(25),lq_query_object varchar(100),lq_query_type varchar(25))
BEGININSERT INTO audit_event_work (`audit_user`,`audit_host`,`audit_timestamp`,`action`,`action_event`)    VALUES(user_name,host_name,NOW(),CONCAT('lava.*.query.',lq_query_object),lq_query_type);  END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_check_user_auth
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_check_user_auth`;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `lq_check_user_auth`(user_login varchar(50),host_name varchar(25))
BEGINDECLARE user_id int;SELECT `UID` into user_id from `authuser` where `Login` = user_login;IF(user_id > 0) THEN  SELECT  1 as user_auth;ELSE  SELECT 0 as user_auth;END IF;END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_check_version
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_check_version`;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `lq_check_version`(pModule varchar(25), pMajor integer, pMinor integer, pFix integer)
BEGINDECLARE CurMajor integer;DECLARE CurMinor integer;DECLARE CurFix integer;DECLARE CurVersion varchar(10);DECLARE pVersion varchar(10);DECLARE version_msg varchar(500);DECLARE UpdateNeeded integer;SELECT MAX(`Major`) into CurMajor from `versionhistory` WHERE `Module` = pModule ;SELECT MAX(`Minor`) into CurMinor from `versionhistory` WHERE `Module` = pModule and `Major` = CurMajor;SELECT MAX(`Fix`) into CurFix from `versionhistory` WHERE `Module` = pModule and `Major` = CurMajor and `Minor` = CurMinor;SET pVersion = CONCAT(cast(pMajor as CHAR),'.',cast(pMinor as  CHAR),'.',cast(pFix as CHAR));SELECT MAX(`Version`) into CurVersion FROM `versionhistory` WHERE `Module` = pModule and `Major` = CurMajor and `Minor` = CurMinor and `Fix` = CurFix;SET version_msg = 'OK';IF (CurVersion IS NULL) THEN  SET version_msg = CONCAT('Invalid Module ',Module,' passed to stored procedure lq_check_version.');ELSEIF (pMajor < CurMajor) THEN	SET version_msg = CONCAT('Your application version (',pVersion,') is out of date.  The current version is (',CurVersion,'). You must upgrade your application.');ELSEIF (pMajor > CurMajor) THEN	SET version_msg = CONCAT('Your application version (',pVersion,') is more recent than the current version supported by the database.  The current version is (',CurVersion,'). You must downgrade your application.');ELSEIF (pMinor < CurMinor) THEN  SET version_msg = CONCAT('Your application version (',pVersion,') is out of date.  The current version is (',CurVersion,'). You must upgrade your application.');ELSEIF (pMinor > CurMinor) THEN	SET version_msg = CONCAT('Your application version (',pVersion,') is more recent than the current version supported by the database.  The current version is (',CurVersion,'). You must downgrade your application.');ELSEIF (pFix < CurFix) THEN  SELECT count(*) into UpdateNeeded from `versionhistory` WHERE `Module`=  pModule and `Major` >= pMajor and `Minor` >= pMinor and `Fix` > pFix  and `UpdateRequired` = 1;  IF(UpdateNeeded > 0) THEN    SET version_msg = CONCAT('Your application version (',pVersion,') is out of date.  The current version is (',CurVersion,'). You must upgrade your application.');  ELSE     SET version_msg = CONCAT('Your application version (',pVersion,') is slightly out of date.  The current version is (',CurVersion,'). You should upgrade to the current version soon.');  END IF;ELSEIF (pFix > CurFix) THEN	SET version_msg = CONCAT('Your application version (',pVersion,') is more recent than the current version supported by the database.  The current version is (',CurVersion,'). You must downgrade your application.');END IF;select version_msg as 'version_msg';END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_objects
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_get_objects`;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `lq_get_objects`(user_name varchar(50), host_name varchar(25))
BEGINSELECT concat(`instance`,'_',`scope`,'_',`module`) as query_source,concat(`section`,'_',`target`) as query_object_name , `short_desc`, `standard`,`primary_link`,`secondary_link` from `query_objects`;END$$

DELIMITER ;
-- -----------------------------------------------------
-- View `audit_entity`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `audit_entity` ;
DROP TABLE IF EXISTS `audit_entity`;

CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `audit_entity` AS select `audit_entity_work`.`audit_entity_id` AS `audit_entity_id`,`audit_entity_work`.`audit_event_id` AS `audit_event_id`,`audit_entity_work`.`entity_id` AS `entity_id`,`audit_entity_work`.`entity` AS `entity`,`audit_entity_work`.`entity_type` AS `entity_type`,`audit_entity_work`.`audit_type` AS `audit_type`,`audit_entity_work`.`modified` AS `modified` from `audit_entity_work` union all select `audit_entity_history`.`audit_entity_id` AS `audit_entity_id`,`audit_entity_history`.`audit_event_id` AS `audit_event_id`,`audit_entity_history`.`entity_id` AS `entity_id`,`audit_entity_history`.`entity` AS `entity`,`audit_entity_history`.`entity_type` AS `entity_type`,`audit_entity_history`.`audit_type` AS `audit_type`,`audit_entity_history`.`modified` AS `modified` from `audit_entity_history`;

-- -----------------------------------------------------
-- View `audit_event`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `audit_event` ;
DROP TABLE IF EXISTS `audit_event`;

CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `audit_event` AS select `audit_event_work`.`audit_event_id` AS `audit_event_id`,`audit_event_work`.`audit_user` AS `audit_user`,`audit_event_work`.`audit_host` AS `audit_host`,`audit_event_work`.`audit_timestamp` AS `audit_timestamp`,`audit_event_work`.`action` AS `action`,`audit_event_work`.`action_event` AS `action_event`,`audit_event_work`.`action_id_param` AS `action_id_param`,`audit_event_work`.`event_note` AS `event_note`,`audit_event_work`.`exception` AS `exception`,`audit_event_work`.`exception_message` AS `exception_message`,`audit_event_work`.`modified` AS `modified` from `audit_event_work` union all select `audit_event_history`.`audit_event_id` AS `audit_event_id`,`audit_event_history`.`audit_user` AS `audit_user`,`audit_event_history`.`audit_host` AS `audit_host`,`audit_event_history`.`audit_timestamp` AS `audit_timestamp`,`audit_event_history`.`action` AS `action`,`audit_event_history`.`action_event` AS `action_event`,`audit_event_history`.`action_id_param` AS `action_id_param`,`audit_event_history`.`event_note` AS `event_note`,`audit_event_history`.`exception` AS `exception`,`audit_event_history`.`exception_message` AS `exception_message`,`audit_event_history`.`modified` AS `modified` from `audit_event_history`;

-- -----------------------------------------------------
-- View `audit_property`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `audit_property` ;
DROP TABLE IF EXISTS `audit_property`;

CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `audit_property` AS select `audit_property_work`.`audit_property_id` AS `audit_property_id`,`audit_property_work`.`audit_entity_id` AS `audit_entity_id`,`audit_property_work`.`property` AS `property`,`audit_property_work`.`index_key` AS `index_key`,`audit_property_work`.`subproperty` AS `subproperty`,`audit_property_work`.`old_value` AS `old_value`,`audit_property_work`.`new_value` AS `new_value`,`audit_property_work`.`audit_timestamp` AS `audit_timestamp`,`audit_property_work`.`modified` AS `modified` from `audit_property_work` union all select `audit_property_history`.`audit_property_id` AS `audit_property_id`,`audit_property_history`.`audit_entity_id` AS `audit_entity_id`,`audit_property_history`.`property` AS `property`,`audit_property_history`.`index_key` AS `index_key`,`audit_property_history`.`subproperty` AS `subproperty`,`audit_property_history`.`old_value` AS `old_value`,`audit_property_history`.`new_value` AS `new_value`,`audit_property_history`.`audit_timestamp` AS `audit_timestamp`,`audit_property_history`.`modified` AS `modified` from `audit_property_history`;

-- -----------------------------------------------------
-- View `audit_text`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `audit_text` ;
DROP TABLE IF EXISTS `audit_text`;

CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `audit_text` AS select `audit_text_history`.`audit_property_id` AS `audit_property_id`,`audit_text_history`.`old_text` AS `old_text`,`audit_text_history`.`new_text` AS `new_text` from `audit_text_history` union all select `audit_text_work`.`audit_property_id` AS `audit_property_id`,`audit_text_work`.`old_text` AS `old_text`,`audit_text_work`.`new_text` AS `new_text` from `audit_text_work`;


insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-core-model','3.0.4',NOW(),3,0,4,1);

insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('LAVAQUERYAPP','3.0.0',NOW(),3,0,0,1);	
	
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
