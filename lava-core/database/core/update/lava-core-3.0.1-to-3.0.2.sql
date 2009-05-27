
-- ------------------------------------------------------
-- modifying system tables to create separate time and date fields and
-- to conform with new standard usage of date, time, and timestamp terminology
-- 
-- Not preserving existing data because there are no production 3.0.1 "open" lava systems yet.
-- -------------------------------------------------------

insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-core-model','3.0.2',NOW(),3,0,2,1);
-- -----------------------------------------------------
-- Table `lava_session`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lavasession` ;

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
-- Placeholder table for view `audit_event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_event` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `audit_property`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_property` (`id` INT);


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



ALTER TABLE authuser CHANGE EffectiveDate EffectiveDate DATE NOT NULL;
ALTER TABLE authuser CHANGE ExpirationDate ExpirationDate DATE NULL DEFAULT NULL;
ALTER TABLE authuser CHANGE AccessAgreementDate AccessAgreementDate DATE NULL DEFAULT NULL;
ALTER TABLE authgroup CHANGE EffectiveDate EffectiveDate DATE NOT NULL;
ALTER TABLE authgroup CHANGE ExpirationDate ExpirationDate DATE NULL DEFAULT NULL;
