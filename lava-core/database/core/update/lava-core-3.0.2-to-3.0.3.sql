
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

