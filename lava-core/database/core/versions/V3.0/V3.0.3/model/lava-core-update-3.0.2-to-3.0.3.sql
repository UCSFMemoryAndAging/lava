
-- ------------------------------------------------------
-- modifying system tables to create separate time and date fields and
-- to conform with new standard usage of date, time, and timestamp terminology
-- 
-- Not preserving existing data because there are no production 3.0.1 "open" lava systems yet.
-- -------------------------------------------------------

insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-core-model','3.0.3',NOW(),3,0,3,1);
-- -----------------------------------------------------
-- Table `lava_session`
-- -----------------------------------------------------

CREATE TEMPORARY TABLE temp_authuser AS SELECT * from authuser;


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
  `authenticationType` VARCHAR(10) NULL DEFAULT 'LOCAL' ,
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

INSERT INTO `authuser` (`UID`,`UserName`,`Login`,`email`,`phone`,`AccessAgreementDate`,`ShortUserName`,
	  `ShortUserNameRev`,`EffectiveDate`,`ExpirationDate`,`Notes`,`authenticationType`,`password`,
	  `passwordExpiration`,`passwordResetToken`,`passwordResetExpiration`,`failedLoginCount`,
	  `lastFailedLogin`,`accountLocked`,`modified`)
	  SELECT `UID`,`UserName`,`Login`,`email`,`phone`,`AccessAgreementDate`,`ShortUserName`,
	  `ShortUserNameRev`,`EffectiveDate`,`ExpirationDate`,`Notes`,CASE WHEN hashedPassword IS NULL THEN 'XML CONFIG' ELSE 'LOCAL' END,`hashedPassword`,
	  NULL,NULL,NULL,NULL,NULL,NULL,`modified` from temp_authuser;
	  
DROP TEMPORARY TABLE temp_authuser;


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
  `description` VARCHAR(50) NULL DEFAULT NULL ,
  `location` VARCHAR(100) NULL DEFAULT NULL ,
  `start_date` DATE NOT NULL ,
  `start_time` TIME NOT NULL ,
  `end_date` DATE NOT NULL ,
  `end_time` TIME NOT NULL ,
  `status` VARCHAR(25) NULL ,
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
  `user_id` INT NOT NULL ,
  `role` VARCHAR(25) NOT NULL ,
  `status` VARCHAR(25) NOT NULL ,
  `notes` VARCHAR(100) NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`attendee_id`) ,
  INDEX `attendee__appointment` (`appointment_id` ASC) ,
  CONSTRAINT `attendee__appointment`
    FOREIGN KEY (`appointment_id`)
    REFERENCES `appointment` (`appointment_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `appointment_change`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `appointment_change` ;

CREATE  TABLE IF NOT EXISTS `appointment_change` (
  `appointment_change_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `appointment_id` INT UNSIGNED NOT NULL ,
  `type` VARCHAR(25) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `change_by` INT NULL ,
  `change_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`appointment_change_id`) ,
  INDEX `appointment_change__appointment` (`appointment_id` ASC) ,
  CONSTRAINT `appointment_change__appointment`
    FOREIGN KEY (`appointment_id`)
    REFERENCES `appointment` (`appointment_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

