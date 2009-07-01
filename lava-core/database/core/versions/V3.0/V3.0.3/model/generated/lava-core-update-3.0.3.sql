SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE  TABLE IF NOT EXISTS `lava_core`.`appointment_change` (
  `appointment_change_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `appointment_id` INT(11) UNSIGNED NOT NULL ,
  `type` VARCHAR(25) NOT NULL ,
  `description` VARCHAR(255) NULL DEFAULT NULL ,
  `change_by` INT(10) NOT NULL ,
  `change_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`appointment_change_id`) ,
  INDEX `appointment_change__appointment` (`appointment_id` ASC) ,
  INDEX `appointment_change__change_by` (`change_by` ASC) ,
  CONSTRAINT `appointment_change__appointment`
    FOREIGN KEY (`appointment_id` )
    REFERENCES `lava_core`.`appointment` (`appointment_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `appointment_change__change_by`
    FOREIGN KEY (`change_by` )
    REFERENCES `lava_core`.`authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `lava_core`.`appointment` (
  `appointment_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `calendar_id` INT(11) UNSIGNED NOT NULL ,
  `organizer_id` INT(11) NOT NULL ,
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
    REFERENCES `lava_core`.`calendar` (`calendar_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `lava_core`.`attendee` (
  `attendee_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `appointment_id` INT(11) UNSIGNED NOT NULL ,
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
    REFERENCES `lava_core`.`appointment` (`appointment_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `attendee__user_id`
    FOREIGN KEY (`user_id` )
    REFERENCES `lava_core`.`authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `lava_core`.`calendar` (
  `calendar_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `type` VARCHAR(25) NOT NULL ,
  `name` VARCHAR(100) NOT NULL ,
  `description` VARCHAR(255) NULL DEFAULT NULL ,
  `notes` TEXT NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`calendar_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `lava_core`.`resource_calendar` (
  `calendar_id` INT(11) UNSIGNED NOT NULL ,
  `resource_type` VARCHAR(25) NOT NULL ,
  `location` VARCHAR(100) NULL DEFAULT NULL ,
  `contact_id` INT(10) NOT NULL ,
  PRIMARY KEY (`calendar_id`) ,
  INDEX `resource_calendar__calendar` (`calendar_id` ASC) ,
  INDEX `resource_calendar__user_id` (`contact_id` ASC) ,
  CONSTRAINT `resource_calendar__calendar`
    FOREIGN KEY (`calendar_id` )
    REFERENCES `lava_core`.`calendar` (`calendar_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `resource_calendar__user_id`
    FOREIGN KEY (`contact_id` )
    REFERENCES `lava_core`.`authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

ALTER TABLE `lava_core`.`audit_entity_history` 
  ADD CONSTRAINT `audit_entity_history__audit_event_id`
  FOREIGN KEY (`audit_event_id` )
  REFERENCES `lava_core`.`audit_event_history` (`audit_event_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `audit_entity_history__audit_event_id` (`audit_event_id` ASC) ;

ALTER TABLE `lava_core`.`audit_entity_work` 
  ADD CONSTRAINT `audit_entity_work__audit_event_id`
  FOREIGN KEY (`audit_event_id` )
  REFERENCES `lava_core`.`audit_event_work` (`audit_event_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `audit_entity_work__audit_event_id` (`audit_event_id` ASC) ;

ALTER TABLE `lava_core`.`audit_property_history` 
  ADD CONSTRAINT `audit_property_history__audit_entity_id`
  FOREIGN KEY (`audit_entity_id` )
  REFERENCES `lava_core`.`audit_entity_history` (`audit_entity_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `audit_property_history__audit_entity_id` (`audit_entity_id` ASC) ;

ALTER TABLE `lava_core`.`audit_property_work` 
  ADD CONSTRAINT `audit_property_work__audit_entity_id`
  FOREIGN KEY (`audit_entity_id` )
  REFERENCES `lava_core`.`audit_entity_work` (`audit_entity_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `audit_property_work__audit_entity_id` (`audit_entity_id` ASC) ;

ALTER TABLE `lava_core`.`audit_text_history` CHANGE COLUMN `audit_property_id` `audit_property_id` INT(10) NOT NULL  , 
  ADD CONSTRAINT `audit_text_history__audit_property_id`
  FOREIGN KEY (`audit_property_id` )
  REFERENCES `lava_core`.`audit_property_history` (`audit_property_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `audit_text_history__audit_property_id` (`audit_property_id` ASC) ;

ALTER TABLE `lava_core`.`audit_text_work` CHANGE COLUMN `audit_property_id` `audit_property_id` INT(10) NOT NULL  , 
  ADD CONSTRAINT `audit_text_work__audit_property_id`
  FOREIGN KEY (`audit_property_id` )
  REFERENCES `lava_core`.`audit_property_work` (`audit_property_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `audit_text_work__audit_property_id` (`audit_property_id` ASC) ;

ALTER TABLE `lava_core`.`authpermission` 
  ADD CONSTRAINT `authpermission_RoleID`
  FOREIGN KEY (`RoleID` )
  REFERENCES `lava_core`.`authrole` (`RoleID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `authpermission_RoleID` (`RoleID` ASC) ;

ALTER TABLE `lava_core`.`authuser` DROP COLUMN `hashedPassword` , ADD COLUMN `accountLocked` TIMESTAMP NULL DEFAULT NULL  AFTER `Notes` , ADD COLUMN `authenticationType` VARCHAR(10) NULL DEFAULT 'EXTERNAL'  AFTER `Notes` , ADD COLUMN `failedLoginCount` SMALLINT(6) NULL DEFAULT NULL  AFTER `authenticationType` , ADD COLUMN `lastFailedLogin` TIMESTAMP NULL DEFAULT NULL  AFTER `failedLoginCount` , ADD COLUMN `passwordExpiration` TIMESTAMP NULL DEFAULT NULL  AFTER `authenticationType` , ADD COLUMN `passwordResetExpiration` TIMESTAMP NULL DEFAULT NULL  AFTER `passwordExpiration` , ADD COLUMN `passwordResetToken` VARCHAR(100) NULL DEFAULT NULL  AFTER `passwordExpiration` , ADD COLUMN `password` VARCHAR(100) NULL DEFAULT NULL  AFTER `authenticationType` ;

ALTER TABLE `lava_core`.`authusergroup` 
  ADD CONSTRAINT `authusergroup_GID`
  FOREIGN KEY (`UGID` )
  REFERENCES `lava_core`.`authgroup` (`GID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `authusergroup_UID`
  FOREIGN KEY (`UID` )
  REFERENCES `lava_core`.`authuser` (`UID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `authusergroup_GID` (`UGID` ASC) 
, ADD INDEX `authusergroup_UID` (`UID` ASC) ;

ALTER TABLE `lava_core`.`authuserrole` 
  ADD CONSTRAINT `authuserrole_GID`
  FOREIGN KEY (`GID` )
  REFERENCES `lava_core`.`authgroup` (`GID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `authuserrole_RoleID`
  FOREIGN KEY (`RoleID` )
  REFERENCES `lava_core`.`authrole` (`RoleID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `authuserrole_UID`
  FOREIGN KEY (`URID` )
  REFERENCES `lava_core`.`authuser` (`UID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `authuserrole_GID` (`GID` ASC) 
, ADD INDEX `authuserrole_RoleID` (`RoleID` ASC) 
, ADD INDEX `authuserrole_UID` (`URID` ASC) ;

ALTER TABLE `lava_core`.`lava_session` 
  ADD CONSTRAINT `lavasession__server_instance_id`
  FOREIGN KEY (`server_instance_id` )
  REFERENCES `lava_core`.`lavaserverinstance` (`ServerInstanceID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `lavasession__user_id`
  FOREIGN KEY (`user_id` )
  REFERENCES `lava_core`.`authuser` (`UID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `lavasession__server_instance_id` (`server_instance_id` ASC) 
, ADD INDEX `lavasession__user_id` (`user_id` ASC) ;

ALTER TABLE `lava_core`.`listvalues` 
  ADD CONSTRAINT `list__ListID`
  FOREIGN KEY (`ListID` )
  REFERENCES `lava_core`.`list` (`ListID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `ListID` (`ListID` ASC) 
, ADD INDEX `list__ListID` (`ListID` ASC) 
, DROP INDEX `cListID` ;


-- -----------------------------------------------------
-- Placeholder table for view `lava_core`.`audit_entity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_core`.`audit_entity` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lava_core`.`audit_event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_core`.`audit_event` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lava_core`.`audit_property`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_core`.`audit_property` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lava_core`.`audit_text`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_core`.`audit_text` (`id` INT);

-- -----------------------------------------------------
-- View `lava_core`.`audit_entity`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_core`.`audit_entity`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `audit_entity` AS select `audit_entity_work`.`audit_entity_id` AS `audit_entity_id`,`audit_entity_work`.`audit_event_id` AS `audit_event_id`,`audit_entity_work`.`entity_id` AS `entity_id`,`audit_entity_work`.`entity` AS `entity`,`audit_entity_work`.`entity_type` AS `entity_type`,`audit_entity_work`.`audit_type` AS `audit_type`,`audit_entity_work`.`modified` AS `modified` from `audit_entity_work` union all select `audit_entity_history`.`audit_entity_id` AS `audit_entity_id`,`audit_entity_history`.`audit_event_id` AS `audit_event_id`,`audit_entity_history`.`entity_id` AS `entity_id`,`audit_entity_history`.`entity` AS `entity`,`audit_entity_history`.`entity_type` AS `entity_type`,`audit_entity_history`.`audit_type` AS `audit_type`,`audit_entity_history`.`modified` AS `modified` from `audit_entity_history`;

-- -----------------------------------------------------
-- View `lava_core`.`audit_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_core`.`audit_event`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `audit_event` AS select `audit_event_work`.`audit_event_id` AS `audit_event_id`,`audit_event_work`.`audit_user` AS `audit_user`,`audit_event_work`.`audit_host` AS `audit_host`,`audit_event_work`.`audit_timestamp` AS `audit_timestamp`,`audit_event_work`.`action` AS `action`,`audit_event_work`.`action_event` AS `action_event`,`audit_event_work`.`action_id_param` AS `action_id_param`,`audit_event_work`.`event_note` AS `event_note`,`audit_event_work`.`exception` AS `exception`,`audit_event_work`.`exception_message` AS `exception_message`,`audit_event_work`.`modified` AS `modified` from `audit_event_work` union all select `audit_event_history`.`audit_event_id` AS `audit_event_id`,`audit_event_history`.`audit_user` AS `audit_user`,`audit_event_history`.`audit_host` AS `audit_host`,`audit_event_history`.`audit_timestamp` AS `audit_timestamp`,`audit_event_history`.`action` AS `action`,`audit_event_history`.`action_event` AS `action_event`,`audit_event_history`.`action_id_param` AS `action_id_param`,`audit_event_history`.`event_note` AS `event_note`,`audit_event_history`.`exception` AS `exception`,`audit_event_history`.`exception_message` AS `exception_message`,`audit_event_history`.`modified` AS `modified` from `audit_event_history`;

-- -----------------------------------------------------
-- View `lava_core`.`audit_property`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_core`.`audit_property`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `audit_property` AS select `audit_property_work`.`audit_property_id` AS `audit_property_id`,`audit_property_work`.`audit_entity_id` AS `audit_entity_id`,`audit_property_work`.`property` AS `property`,`audit_property_work`.`index_key` AS `index_key`,`audit_property_work`.`subproperty` AS `subproperty`,`audit_property_work`.`old_value` AS `old_value`,`audit_property_work`.`new_value` AS `new_value`,`audit_property_work`.`audit_timestamp` AS `audit_timestamp`,`audit_property_work`.`modified` AS `modified` from `audit_property_work` union all select `audit_property_history`.`audit_property_id` AS `audit_property_id`,`audit_property_history`.`audit_entity_id` AS `audit_entity_id`,`audit_property_history`.`property` AS `property`,`audit_property_history`.`index_key` AS `index_key`,`audit_property_history`.`subproperty` AS `subproperty`,`audit_property_history`.`old_value` AS `old_value`,`audit_property_history`.`new_value` AS `new_value`,`audit_property_history`.`audit_timestamp` AS `audit_timestamp`,`audit_property_history`.`modified` AS `modified` from `audit_property_history`;

-- -----------------------------------------------------
-- View `lava_core`.`audit_text`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_core`.`audit_text`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `audit_text` AS select `audit_text_history`.`audit_property_id` AS `audit_property_id`,`audit_text_history`.`old_text` AS `old_text`,`audit_text_history`.`new_text` AS `new_text` from `audit_text_history` union all select `audit_text_work`.`audit_property_id` AS `audit_property_id`,`audit_text_work`.`old_text` AS `old_text`,`audit_text_work`.`new_text` AS `new_text` from `audit_text_work`;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
