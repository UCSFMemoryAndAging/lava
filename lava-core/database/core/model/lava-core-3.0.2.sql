SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `lava-core` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `lava-core`;

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
  PRIMARY KEY (`audit_entity_id`) )
ENGINE = InnoDB
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
  PRIMARY KEY (`audit_entity_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


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
AUTO_INCREMENT = 1
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
  PRIMARY KEY (`audit_property_id`) )
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
  PRIMARY KEY (`audit_property_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `audit_text_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_text_history` ;

CREATE  TABLE IF NOT EXISTS `audit_text_history` (
  `audit_property_id` INT(11) NOT NULL ,
  `old_text` TEXT NULL DEFAULT NULL ,
  `new_text` TEXT NULL DEFAULT NULL ,
  PRIMARY KEY (`audit_property_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `audit_text_work`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_text_work` ;

CREATE  TABLE IF NOT EXISTS `audit_text_work` (
  `audit_property_id` INT(11) NOT NULL ,
  `old_text` TEXT NULL DEFAULT NULL ,
  `new_text` TEXT NULL DEFAULT NULL ,
  PRIMARY KEY (`audit_property_id`) )
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
AUTO_INCREMENT = 1
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
  PRIMARY KEY (`PermID`) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
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
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `authuser`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `authuser` ;

CREATE  TABLE IF NOT EXISTS `authuser` (
  `UID` INT(10) NOT NULL AUTO_INCREMENT ,
  `UserName` VARCHAR(50) NOT NULL ,
  `Login` VARCHAR(100) NULL DEFAULT NULL ,
  `hashedPassword` VARCHAR(100) NULL DEFAULT NULL ,
  `email` VARCHAR(100) NULL DEFAULT NULL ,
  `phone` VARCHAR(25) NULL DEFAULT NULL ,
  `AccessAgreementDate` DATE NULL DEFAULT NULL ,
  `ShortUserName` VARCHAR(50) NULL DEFAULT NULL ,
  `ShortUserNameRev` VARCHAR(50) NULL DEFAULT NULL ,
  `EffectiveDate` DATE NOT NULL ,
  `ExpirationDate` DATE NULL DEFAULT NULL ,
  `Notes` VARCHAR(255) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`UID`) ,
  UNIQUE INDEX `Unique_UserName` (`UserName` ASC) ,
  UNIQUE INDEX `Unique_Login` (`Login` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
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
  PRIMARY KEY (`UGID`) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
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
  PRIMARY KEY (`URID`) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
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
AUTO_INCREMENT = 1
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
  PRIMARY KEY (`lava_session_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
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
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `list`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `list` ;

CREATE  TABLE IF NOT EXISTS `list` (
  `ListID` INT(10) NOT NULL AUTO_INCREMENT ,
  `ListName` VARCHAR(50) NOT NULL ,
  `NumericKey` TINYINT(1) NOT NULL DEFAULT '0' ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`ListID`) ,
  INDEX `ListName` (`ListName` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
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
  INDEX `cListID` (`ListID` ASC) ,
  INDEX `ValueKey` (`ValueKey` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
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
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Placeholder table for view `audit_entity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_entity` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `audit_event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_event` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `audit_property`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_property` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `audit_text`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_text` (`id` INT);

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
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `audit_event` AS select `audit_event_work`.`audit_event_id` AS `audit_event_id`,`audit_event_work`.`audit_user` AS `audit_user`,`audit_event_work`.`audit_host` AS `audit_host`,`audit_event_work`.`audit_timestamp` AS `audit_timestamp`,`audit_event_work`.`action` AS `action`,`audit_event_work`.`action_event` AS `action_event`,`audit_event_work`.`action_id_param` AS `action_id_param`,`audit_event_work`.`event_note` AS `event_note`,`audit_event_work`.`exception` AS `exception`,`audit_event_work`.`exception_message` AS `exception_message`,`audit_event_work`.`modified` AS `modified` from `audit_event_work` union all select `audit_event_history`.`audit_event_id` AS `audit_event_id`,`audit_event_history`.`audit_user` AS `audit_user`,`audit_event_history`.`audit_host` AS `audit_host`,`audit_event_history`.`audit_timestamp` AS `audit_timestamp`,`audit_event_history`.`action` AS `action`,`audit_event_history`.`action_event` AS `action_event`,`audit_event_history`.`action_id_param` AS `action_id_param`,`audit_event_history`.`event_note` AS `event_note`,`audit_event_history`.`exception` AS `exception`,`audit_event_history`.`exception_message` AS `exception_message`,`audit_event_history`.`modified` AS `modified` from `audit_event_history`;

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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
