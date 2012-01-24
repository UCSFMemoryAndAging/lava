SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

ALTER TABLE `list` ADD COLUMN `instance` VARCHAR(25) NOT NULL DEFAULT 'lava'  AFTER `ListName` ;

ALTER TABLE `listvalues` ADD COLUMN `instance` VARCHAR(25) NOT NULL DEFAULT 'lava'  AFTER `ListID` ,
	ADD COLUMN `scope` VARCHAR(25) NOT NULL DEFAULT '[not set]'  AFTER `instance` , CHANGE COLUMN `ValueDesc` `ValueDesc` VARCHAR(255) NULL DEFAULT NULL  ;

	ALTER TABLE `viewproperty` ADD COLUMN `label2` VARCHAR(25) NULL DEFAULT NULL  AFTER `label` ;

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
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

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


DELIMITER $$

CREATE PROCEDURE `lq_get_objects` (user_name varchar(50), host_name varchar(25))
BEGIN
SELECT concat(`instance`,'_',`scope`,'_',`module`) as query_source,concat(`section`,'_',`target`) as query_object_name , `short_desc`, `standard`,`primary_link`,`secondary_link` from `query_objects`;
END$$

DELIMITER ;
DELIMITER $$

CREATE PROCEDURE `lq_audit_event` (user_name varchar(50),host_name varchar(25),lq_query_object varchar(100),lq_query_type varchar(25))
BEGIN



INSERT INTO audit_event_work (`audit_user`,`audit_host`,`audit_timestamp`,`action`,`action_event`)

    VALUES(user_name,host_name,NOW(),CONCAT('lava.*.query.',lq_query_object),lq_query_type);


  
END


$$

DELIMITER ;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `lq_check_user_auth`(user_login varchar(50),host_name varchar(25))
BEGIN
DECLARE user_id int;
SELECT `UID` into user_id from `authuser` where `Login` = user_login;
IF(user_id > 0) THEN
  SELECT  1 as user_auth;
ELSE
  SELECT 0 as user_auth;
END IF;

END$$

DELIMITER ;
DELIMITER $$

CREATE  PROCEDURE `lq_check_version`(pModule varchar(25), pMajor integer, pMinor integer, pFix integer)
BEGIN

DECLARE CurMajor integer;
DECLARE CurMinor integer;
DECLARE CurFix integer;
DECLARE CurVersion varchar(10);
DECLARE pVersion varchar(10);
DECLARE version_msg varchar(500);
DECLARE UpdateNeeded integer;
SELECT MAX(`Major`) into CurMajor from `versionhistory` WHERE `Module` = pModule ;
SELECT MAX(`Minor`) into CurMinor from `versionhistory` WHERE `Module` = pModule and `Major` = CurMajor;
SELECT MAX(`Fix`) into CurFix from `versionhistory` WHERE `Module` = pModule and `Major` = CurMajor and `Minor` = CurMinor;
SET pVersion = CONCAT(cast(pMajor as CHAR),'.',cast(pMinor as  CHAR),'.',cast(pFix as CHAR));
SELECT MAX(`Version`) into CurVersion FROM `versionhistory` WHERE `Module` = pModule and `Major` = CurMajor and `Minor` = CurMinor and `Fix` = CurFix;

SET version_msg = 'OK';

IF (CurVersion IS NULL) THEN
  SET version_msg = CONCAT('Invalid Module ',Module,' passed to stored procedure lq_check_version.');
ELSEIF (pMajor < CurMajor) THEN
	SET version_msg = CONCAT('Your application version (',pVersion,') is out of date.  The current version is (',CurVersion,'). You must upgrade your application.');
ELSEIF (pMajor > CurMajor) THEN
	SET version_msg = CONCAT('Your application version (',pVersion,') is more recent than the current version supported by the database.  The current version is (',CurVersion,'). You must downgrade your application.');
ELSEIF (pMinor < CurMinor) THEN
  SET version_msg = CONCAT('Your application version (',pVersion,') is out of date.  The current version is (',CurVersion,'). You must upgrade your application.');
ELSEIF (pMinor > CurMinor) THEN
	SET version_msg = CONCAT('Your application version (',pVersion,') is more recent than the current version supported by the database.  The current version is (',CurVersion,'). You must downgrade your application.');
ELSEIF (pFix < CurFix) THEN
  SELECT count(*) into UpdateNeeded from `versionhistory` WHERE `Module`=  pModule and `Major` >= pMajor and `Minor` >= pMinor and `Fix` > pFix  and `UpdateRequired` = 1;
  IF(UpdateNeeded > 0) THEN
    SET version_msg = CONCAT('Your application version (',pVersion,') is out of date.  The current version is (',CurVersion,'). You must upgrade your application.');
  ELSE 
    SET version_msg = CONCAT('Your application version (',pVersion,') is slightly out of date.  The current version is (',CurVersion,'). You should upgrade to the current version soon.');
  END IF;
ELSEIF (pFix > CurFix) THEN
	SET version_msg = CONCAT('Your application version (',pVersion,') is more recent than the current version supported by the database.  The current version is (',CurVersion,'). You must downgrade your application.');
END IF;

select version_msg as 'version_msg';

END$$

DELIMITER ;

insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-core-model','3.1.0',NOW(),3,1,0,1);
	
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
