SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

ALTER TABLE `authusergroup` DROP FOREIGN KEY `authusergroup_GID` ;

ALTER TABLE `authuserrole` DROP FOREIGN KEY `authuserrole_UID` ;

ALTER TABLE `authusergroup` 
  ADD CONSTRAINT `authusergroup_GID`
  FOREIGN KEY (`GID` )
  REFERENCES `authgroup` (`GID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, DROP INDEX `authusergroup_GID` 
, ADD INDEX `authusergroup_GID` (`GID` ASC) ;

ALTER TABLE `authuserrole` 
  ADD CONSTRAINT `authuserrole_UID`
  FOREIGN KEY (`UID` )
  REFERENCES `authuser` (`UID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, DROP INDEX `authuserrole_UID` 
, ADD INDEX `authuserrole_UID` (`UID` ASC) ;

ALTER TABLE `list` ADD COLUMN `instance` VARCHAR(25) NOT NULL DEFAULT 'lava'  AFTER `ListName` ;

ALTER TABLE `listvalues` ADD COLUMN `instance` VARCHAR(25) NOT NULL DEFAULT 'lava'  AFTER `ListID` , ADD COLUMN `scope` VARCHAR(25) NOT NULL DEFAULT '[not set]' AFTER `instance` , CHANGE COLUMN `ValueDesc` `ValueDesc` VARCHAR(255) NULL DEFAULT NULL  ;

ALTER TABLE `viewproperty` ADD COLUMN `label2` VARCHAR(25) NULL DEFAULT NULL  AFTER `label` ;

ALTER TABLE `calendar` ADD COLUMN `work_days` VARCHAR(100) NULL DEFAULT NULL  AFTER `description` , ADD COLUMN `work_begin_time` TIME NULL DEFAULT NULL  AFTER `work_days` , ADD COLUMN `work_end_time` TIME NULL DEFAULT NULL  AFTER `work_begin_time` ;

ALTER TABLE `resource_calendar` ADD COLUMN `peak_usage_days` VARCHAR(100) NULL DEFAULT NULL  AFTER `contact_id` , ADD COLUMN `peak_usage_begin_time` TIME NULL DEFAULT NULL  AFTER `peak_usage_days` , ADD COLUMN `peak_usage_end_time` TIME NULL DEFAULT NULL  AFTER `peak_usage_begin_time` ;

CREATE  TABLE IF NOT EXISTS `lava_file` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(255) NULL DEFAULT NULL ,
  `file_type` VARCHAR(255) NULL DEFAULT NULL ,
  `content_type` VARCHAR(100) NULL DEFAULT NULL ,
  `file_status_date` DATE NULL DEFAULT NULL ,
  `file_status` VARCHAR(50) NULL DEFAULT NULL ,
  `file_status_by` VARCHAR(50) NULL DEFAULT NULL ,
  `repository_id` VARCHAR(100) NULL DEFAULT NULL ,
  `file_id` VARCHAR(100) NULL DEFAULT NULL ,
  `location` VARCHAR(1000) NULL DEFAULT NULL ,
  `checksum` VARCHAR(100) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) ,
  INDEX `content_type` (`id` ASC, `content_type` ASC) ,
  INDEX `repository_info` (`id` ASC, `repository_id` ASC, `file_id` ASC, `location` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `datadictionary` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `instance` VARCHAR(25) NOT NULL DEFAULT 'lava' ,
  `scope` VARCHAR(25) NOT NULL ,
  `entity` VARCHAR(100) NOT NULL ,
  `prop_order` SMALLINT(6) NULL DEFAULT NULL ,
  `prop_name` VARCHAR(100) NULL DEFAULT NULL ,
  `prop_description` VARCHAR(500) NULL DEFAULT NULL ,
  `data_values` VARCHAR(500) NULL DEFAULT NULL ,
  `data_calculation` VARCHAR(500) NULL DEFAULT NULL ,
  `required` TINYINT(4) NULL DEFAULT '1' ,
  `db_table` VARCHAR(50) NULL DEFAULT NULL ,
  `db_column` VARCHAR(50) NULL DEFAULT NULL ,
  `db_order` SMALLINT(6) NULL DEFAULT NULL ,
  `db_datatype` VARCHAR(20) NULL DEFAULT NULL ,
  `db_datalength` VARCHAR(20) NULL DEFAULT NULL ,
  `db_nullable` TINYINT(4) NULL DEFAULT '1' ,
  `db_default` VARCHAR(50) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `lava_session` ;

DROP TABLE IF EXISTS `lavaserverinstance` ;



DROP procedure IF EXISTS `lq_audit_event`;

DELIMITER $$

CREATE PROCEDURE `lq_audit_event` (user_name varchar(50),host_name varchar(25),lq_query_object varchar(100),lq_query_type varchar(25))BEGININSERT INTO audit_event_work (`audit_user`,`audit_host`,`audit_timestamp`,`action`,`action_event`)    VALUES(user_name,host_name,NOW(),CONCAT('lava.*.query.',lq_query_object),lq_query_type);  END$$

DELIMITER ;

DROP procedure IF EXISTS `lq_check_version`;

DELIMITER $$

CREATE  PROCEDURE `lq_check_version`(pModule varchar(25), pMajor integer, pMinor integer, pFix integer)
BEGIN

DECLARE CurMajor integer;DECLARE CurMinor integer;DECLARE CurFix integer;DECLARE CurVersion varchar(10);DECLARE pVersion varchar(10);
DECLARE version_msg varchar(500);DECLARE UpdateNeeded integer;SELECT MAX(`Major`) into CurMajor from `versionhistory` WHERE `Module` = pModule ;SELECT MAX(`Minor`) into CurMinor from `versionhistory` WHERE `Module` = pModule and `Major` = CurMajor;SELECT MAX(`Fix`) into CurFix from `versionhistory` WHERE `Module` = pModule and `Major` = CurMajor and `Minor` = CurMinor;SET pVersion = CONCAT(cast(pMajor as CHAR),'.',cast(pMinor as  CHAR),'.',cast(pFix as CHAR));SELECT MAX(`Version`) into CurVersion FROM `versionhistory` WHERE `Module` = pModule and `Major` = CurMajor and `Minor` = CurMinor and `Fix` = CurFix;
SET version_msg = 'OK';

IF (CurVersion IS NULL) THEN  SET version_msg = CONCAT('Invalid Module ',Module,' passed to stored procedure lq_check_version.');
ELSEIF (pMajor < CurMajor) THEN	SET version_msg = CONCAT('Your application version (',pVersion,') is out of date.  The current version is (',CurVersion,'). You must upgrade your application.');
ELSEIF (pMajor > CurMajor) THEN	SET version_msg = CONCAT('Your application version (',pVersion,') is more recent than the current version supported by the database.  The current version is (',CurVersion,'). You must downgrade your application.');
ELSEIF (pMinor < CurMinor) THEN  SET version_msg = CONCAT('Your application version (',pVersion,') is out of date.  The current version is (',CurVersion,'). You must upgrade your application.');
ELSEIF (pMinor > CurMinor) THEN	SET version_msg = CONCAT('Your application version (',pVersion,') is more recent than the current version supported by the database.  The current version is (',CurVersion,'). You must downgrade your application.');
ELSEIF (pFix < CurFix) THEN  SELECT count(*) into UpdateNeeded from `versionhistory` WHERE `Module`=  pModule and `Major` >= pMajor and `Minor` >= pMinor and `Fix` > pFix  and `UpdateRequired` = 1;
  IF(UpdateNeeded > 0) THEN
    SET version_msg = CONCAT('Your application version (',pVersion,') is out of date.  The current version is (',CurVersion,'). You must upgrade your application.');
  ELSE 
    SET version_msg = CONCAT('Your application version (',pVersion,') is slightly out of date.  The current version is (',CurVersion,'). You should upgrade to the current version soon.');
  END IF;
ELSEIF (pFix > CurFix) THEN	SET version_msg = CONCAT('Your application version (',pVersion,') is more recent than the current version supported by the database.  The current version is (',CurVersion,'). You must downgrade your application.');
END IF;

select version_msg as 'version_msg';

END$$

DELIMITER ;

insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-core-model','3.1.0',NOW(),3,1,0,1);
	
	
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
