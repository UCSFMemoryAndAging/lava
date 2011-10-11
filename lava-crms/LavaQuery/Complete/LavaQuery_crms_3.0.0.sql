SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';




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


DELETE from query_objects where instance='lava' and scope='crms' and module='query' and section<>'nacc';

INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','patient','demographics','Demographics',1,0,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','enrollment','status','Enrollment Status',1,0,0);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','scheduling','visits','Visits',1,0,0);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','assessment','instruments','Instrument Tracking',1,0,0);
  
  
  


-- -----------------------------------------------------
-- procedure lq_get_objects
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_objects`;
DELIMITER $$

DELIMITER $$
CREATE PROCEDURE `lq_get_objects` (user_name varchar(50), host_name varchar(25))
BEGIN
SELECT concat(`instance`,'_',`scope`,'_',`module`) as query_source,concat(`section`,'_',`target`) as query_object_name , `short_desc`, `standard`,`primary_link`,`secondary_link` from `query_objects`;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_audit_event
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_audit_event`;

DELIMITER $$
CREATE PROCEDURE `lq_audit_event` (user_name varchar(50),host_name varchar(25),lq_query_object varchar(100),lq_query_type varchar(25))
BEGIN



INSERT INTO audit_event_work (`audit_user`,`audit_host`,`audit_timestamp`,`action`,`action_event`)

    VALUES(user_name,host_name,NOW(),CONCAT('lava.*.query.',lq_query_object),lq_query_type);


  
END


$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_authenticate_user
--
-- This procedure is is used for authenticating a user for LavaQuery usage. 
--
-- Authentication is done against the authuser table. So the user uses the same credentials
-- as in LAVA and can use LAVA to change their password.
-- NOTE: this only works for LAVA users where authenticationType is "LOCAL" (i.e. stored in
-- authuser. it does not work for "UCSF AD", although the LavaQuery xls file could be
-- programmed to try UCSF AD (i.e. LDAP) authentication if LOCAL authentication fails.
--
-- TODO:
-- if this authentication fails, have LavaQuery xls attempt LDAP authentication.
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_authenticate_user`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_authenticate_user`(user_login varchar(50),user_password varchar(50))
BEGIN
DECLARE user_id int;

SELECT `UID` into user_id from `authuser` where `Login` = user_login AND `password` = convert(sha2(concat(user_password,'{',`UID`,'}'),256) USING latin1);

IF(user_id > 0) THEN

  SELECT  1 as auth_user;

ELSE

  SELECT 0 as auth_user;

END IF;


END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_check_user_auth
--
-- NOTE: currently not used as lq_authenticate_user encompasses this check. however, if
-- implement an authentication chain where LDAP is attempted if lq_authenticate_user fails,
-- then this will go back into use, so keep around
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_check_user_auth`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_check_user_auth`(user_login varchar(50),host_name varchar(25))
BEGIN
DECLARE user_id int;

SELECT `UID` into user_id from `authuser` where `Login` = user_login;

IF(user_id > 0) THEN

  SELECT  1 as user_auth;

ELSE

  SELECT 0 as user_auth;

END IF;


END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_check_version
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_check_version`;
DELIMITER $$

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

$$
DELIMITER ;





-- -----------------------------------------------------
-- Placeholder table for view `lq_view_demographics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_demographics` (`PIDN_demographics` INT, `DOB` INT, `AGE` INT, `Gender` INT, `Hand` INT, `Deceased` INT, `DOD` INT, `PrimaryLanguage` INT, `TestingLanguage` INT, `TransNeeded` INT, `TransLanguage` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_enrollment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_enrollment` (`EnrollStatID` INT, `PIDN_Enrollment` INT, `ProjName` INT, `SubjectStudyID` INT, `ReferralSource` INT, `LatestDesc` INT, `LatestDate` INT, `LatestNote` INT, `ReferredDesc` INT, `ReferredDate` INT, `ReferredNote` INT, `DeferredDesc` INT, `DeferredDate` INT, `DeferredNote` INT, `EligibleDesc` INT, `EligibleDate` INT, `EligibleNote` INT, `IneligibleDesc` INT, `IneligibleDate` INT, `IneligibleNote` INT, `DeclinedDesc` INT, `DeclinedDate` INT, `DeclinedNote` INT, `EnrolledDesc` INT, `EnrolledDate` INT, `EnrolledNote` INT, `ExcludedDesc` INT, `ExcludedDate` INT, `ExcludedNote` INT, `WithdrewDesc` INT, `WithdrewDate` INT, `WithdrewNote` INT, `InactiveDesc` INT, `InactiveDate` INT, `InactiveNote` INT, `DeceasedDesc` INT, `DeceasedDate` INT, `DeceasedNote` INT, `AutopsyDesc` INT, `AutopsyDate` INT, `AutopsyNote` INT, `ClosedDesc` INT, `ClosedDate` INT, `ClosedNote` INT, `EnrollmentNotes` INT, `modified` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_instruments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_instruments` (`InstrID` INT, `VID` INT, `ProjName` INT, `PIDN_Instrument` INT, `InstrType` INT, `InstrVer` INT, `DCDate` INT, `DCBy` INT, `DCStatus` INT, `DCNotes` INT, `ResearchStatus` INT, `QualityIssue` INT, `QualityIssue2` INT, `QualityIssue3` INT, `QualityNotes` INT, `DEDate` INT, `DEBy` INT, `DEStatus` INT, `DENotes` INT, `DVDate` INT, `DVBy` INT, `DVStatus` INT, `DVNotes` INT, `latestflag` INT, `FieldStatus` INT, `AgeAtDC` INT, `modified` INT, `summary` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_visit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_visit` (`VID` INT, `PIDN_Visit` INT, `ProjName` INT, `VLocation` INT, `VType` INT, `VWith` INT, `VDate` INT, `VTime` INT, `VStatus` INT, `VNotes` INT, `FUMonth` INT, `FUYear` INT, `FUNote` INT, `WList` INT, `WListNote` INT, `WListDate` INT, `VShortDesc` INT, `AgeAtVisit` INT, `modified` INT);

-- -----------------------------------------------------
-- procedure lq_after_set_linkdata
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_after_set_linkdata`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_after_set_linkdata`(user_name varchar(50), host_name varchar(25),method VARCHAR(25))
BEGIN
IF method = 'VISIT' THEN
  UPDATE temp_linkdata1, visit 
  SET temp_linkdata1.pidn = visit.PIDN, temp_linkdata1.link_date = visit.VDATE, temp_linkdata1.link_type = method 
  WHERE temp_linkdata1.link_id = visit.VID;
ELSEIF method = 'INSTRUMENT' THEN
  UPDATE temp_linkdata1, instrumenttracking 
  SET temp_linkdata1.pidn = instrumenttracking.PIDN, temp_linkdata1.link_date = instrumenttracking.DCDATE, temp_linkdata1.link_type = method 
  WHERE temp_linkdata1.link_id = instrumenttracking.InstrID;
ELSE
  UPDATE temp_linkdata1 SET link_type = method;
END IF;
#remove duplicates
CREATE TEMPORARY TABLE temp_linkdata (
  pidn INTEGER NOT NULL,
  link_date DATE NOT NULL,
  link_id INTEGER NOT NULL,
  link_type VARCHAR(50) NOT NULL);

IF method = 'PIDN_DATE' THEN
  #the link_id is an arbitrary incrementing integer, so we exclude it from determining if the row is unique  
  INSERT INTO temp_linkdata(pidn,link_date,link_id,link_type) 
  SELECT pidn,link_date, min(link_id), link_type FROM temp_linkdata1 GROUP BY pidn,link_date,link_type;
ELSE
  #the link_id is driving uniqueness and should be included in the grouping
  INSERT INTO temp_linkdata(pidn,link_date,link_id,link_type) 
  SELECT pidn,link_date, link_id, link_type FROM temp_linkdata1 GROUP BY pidn,link_date,link_id,link_type;
END IF;



ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);


END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_after_set_pidns
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_after_set_pidns`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_after_set_pidns`(user_name varchar(50), host_name varchar(25))
BEGIN
CREATE TEMPORARY TABLE temp_pidn AS SELECT pidn FROM temp_pidn1 GROUP BY pidn;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_clear_linkdata
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_clear_linkdata`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_clear_linkdata`(user_name varchar(50), host_name varchar(25))
BEGIN

DROP TABLE IF EXISTS temp_linkdata;
DROP TABLE IF EXISTS temp_linkdata1;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_clear_pidns
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_clear_pidns`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_clear_pidns`(user_name varchar(50), host_name varchar(25))
BEGIN

DROP TABLE IF EXISTS temp_pidn;
DROP TABLE IF EXISTS temp_pidn1;

END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_all_pidns
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_all_pidns`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_all_pidns`(user_name varchar(50), host_name varchar(25))
BEGIN
SELECT pidn from patient order by pidn;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_assessment_instruments
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_assessment_instruments`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_assessment_instruments`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.assessment.instruments',query_type);


	
IF query_type = 'Simple' THEN
	SELECT p.pidn, i.* FROM lq_view_instruments i 
		INNER JOIN temp_pidn p ON (p.PIDN = i.PIDN_Instrument) 
      ORDER BY p.pidn, i.DCDate,i.InstrType;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, i.*  FROM lq_view_instruments i 
		RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = i.PIDN_Instrument) 
      ORDER BY p.pidn, i.DCDate,i.InstrType;

END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_enrollment_status
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_enrollment_status`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_enrollment_status`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.enrollment.status',query_type);


IF query_type = 'Simple' THEN
	SELECT p.pidn, e.*  FROM lq_view_enrollment e 
		INNER JOIN temp_pidn p ON (p.PIDN = e.PIDN_Enrollment) 
      ORDER BY p.pidn, e.latestDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, e.* FROM lq_view_enrollment e 
		RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = e.PIDN_Enrollment) 
      ORDER BY p.pidn, e.latestDate;

END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_linkdata
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_linkdata`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_linkdata`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
    SQL SECURITY INVOKER
BEGIN
IF query_type = 'VISIT' THEN
	SELECT l.*, '---->' as `Visit Details`, v.* from temp_linkdata l inner join visit v on l.link_id=v.VID
	ORDER BY l.pidn, l.link_date,l.link_id;
ELSEIF query_type = 'INSTRUMENT' THEN 
	SELECT l.*, '---->' as `Instrument Details`, i.* from temp_linkdata l inner join instrumenttracking i on l.link_id=i.InstrID
	ORDER BY l.pidn, l.link_date,l.link_id;
ELSE
   SELECT * from temp_linkdata l  

	ORDER BY l.pidn, l.link_date,l.link_id;
END IF;

END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_patient_demographics
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_patient_demographics`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_patient_demographics`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.patient.demographics',query_type);
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, d.*  FROM lq_view_demographics d 
		INNER JOIN temp_pidn p ON (p.PIDN = d.PIDN_Demographics) 
      ORDER BY p.pidn;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, d.*   FROM lq_view_demographics d  
		 RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = d.PIDN_Demographics) 
      ORDER BY p.pidn;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	 SELECT l.pidn, l.link_date,l.link_id,d.*  
	 FROM temp_linkdata l INNER JOIN lq_view_demographics d ON (d.PIDN_Demographics=l.PIDN);
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_scheduling_visits
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_scheduling_visits`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_scheduling_visits`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.scheduling.visits',query_type);


	
IF query_type = 'Simple' THEN
	SELECT p.pidn, v.*  FROM lq_view_visit v 
		INNER JOIN temp_pidn p ON (p.PIDN = v.PIDN_visit) 
      ORDER BY p.pidn, v.vdate, v.vtype;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, v.*  FROM lq_view_visit v  
		 RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = v.PIDN_visit) 
      ORDER BY p.pidn,v.vdate, v.vtype;

END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_set_linkdata
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_set_linkdata`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_set_linkdata`(user_name varchar(50), host_name varchar(25))
BEGIN


DROP TABLE IF EXISTS temp_linkdata1;
DROP TABLE IF EXISTS temp_linkdata;

CREATE TEMPORARY TABLE temp_linkdata1(
    pidn INTEGER NOT NULL,
    link_date DATE NOT NULL,
    link_id INTEGER NOT NULL,
    link_type varchar(25) DEFAULT NULL);
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_set_linkdata_row
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_set_linkdata_row`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_set_linkdata_row`(user_name varchar(50), host_name varchar(25),pidn integer,link_date date, link_id integer)
BEGIN

INSERT INTO `temp_linkdata1` (`pidn`,`link_date`,`link_id`) VALUES(pidn,link_date,link_id);
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_set_pidns
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_set_pidns`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_set_pidns`(user_name varchar(50), host_name varchar(25))
BEGIN

DROP TABLE IF EXISTS temp_pidn1;
DROP TABLE IF EXISTS temp_pidn;

CREATE TEMPORARY TABLE temp_pidn1(
    pidn INTEGER NOT NULL);
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_set_pidns_row
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_set_pidns_row`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_set_pidns_row`(user_name varchar(50), host_name varchar(25),pidn integer)
BEGIN

INSERT INTO `temp_pidn1` (`pidn`) values (pidn);
END$$

$$
DELIMITER ;


DELIMITER ;


-- -----------------------------------------------------
-- View `lq_view_demographics`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_demographics` ;
DROP TABLE IF EXISTS `lq_view_demographics`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lq_view_demographics` AS select `patient`.`PIDN` AS `PIDN_demographics`,`patient`.`DOB` AS `DOB`,`patient`.`AGE` AS `AGE`,`patient`.`Gender` AS `Gender`,`patient`.`Hand` AS `Hand`,`patient`.`Deceased` AS `Deceased`,`patient`.`DOD` AS `DOD`,`patient`.`PrimaryLanguage` AS `PrimaryLanguage`,`patient`.`TestingLanguage` AS `TestingLanguage`,`patient`.`TransNeeded` AS `TransNeeded`,`patient`.`TransLanguage` AS `TransLanguage` from `patient` where (`patient`.`PIDN` > 0);

-- -----------------------------------------------------
-- View `lq_view_enrollment`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_enrollment` ;
DROP TABLE IF EXISTS `lq_view_enrollment`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lq_view_enrollment` AS select `enrollmentstatus`.`EnrollStatID` AS `EnrollStatID`,`enrollmentstatus`.`PIDN` AS `PIDN_Enrollment`,`enrollmentstatus`.`ProjName` AS `ProjName`,`enrollmentstatus`.`SubjectStudyID` AS `SubjectStudyID`,`enrollmentstatus`.`ReferralSource` AS `ReferralSource`,`enrollmentstatus`.`LatestDesc` AS `LatestDesc`,`enrollmentstatus`.`LatestDate` AS `LatestDate`,`enrollmentstatus`.`LatestNote` AS `LatestNote`,`enrollmentstatus`.`ReferredDesc` AS `ReferredDesc`,`enrollmentstatus`.`ReferredDate` AS `ReferredDate`,`enrollmentstatus`.`ReferredNote` AS `ReferredNote`,`enrollmentstatus`.`DeferredDesc` AS `DeferredDesc`,`enrollmentstatus`.`DeferredDate` AS `DeferredDate`,`enrollmentstatus`.`DeferredNote` AS `DeferredNote`,`enrollmentstatus`.`EligibleDesc` AS `EligibleDesc`,`enrollmentstatus`.`EligibleDate` AS `EligibleDate`,`enrollmentstatus`.`EligibleNote` AS `EligibleNote`,`enrollmentstatus`.`IneligibleDesc` AS `IneligibleDesc`,`enrollmentstatus`.`IneligibleDate` AS `IneligibleDate`,`enrollmentstatus`.`IneligibleNote` AS `IneligibleNote`,`enrollmentstatus`.`DeclinedDesc` AS `DeclinedDesc`,`enrollmentstatus`.`DeclinedDate` AS `DeclinedDate`,`enrollmentstatus`.`DeclinedNote` AS `DeclinedNote`,`enrollmentstatus`.`EnrolledDesc` AS `EnrolledDesc`,`enrollmentstatus`.`EnrolledDate` AS `EnrolledDate`,`enrollmentstatus`.`EnrolledNote` AS `EnrolledNote`,`enrollmentstatus`.`ExcludedDesc` AS `ExcludedDesc`,`enrollmentstatus`.`ExcludedDate` AS `ExcludedDate`,`enrollmentstatus`.`ExcludedNote` AS `ExcludedNote`,`enrollmentstatus`.`WithdrewDesc` AS `WithdrewDesc`,`enrollmentstatus`.`WithdrewDate` AS `WithdrewDate`,`enrollmentstatus`.`WithdrewNote` AS `WithdrewNote`,`enrollmentstatus`.`InactiveDesc` AS `InactiveDesc`,`enrollmentstatus`.`InactiveDate` AS `InactiveDate`,`enrollmentstatus`.`InactiveNote` AS `InactiveNote`,`enrollmentstatus`.`DeceasedDesc` AS `DeceasedDesc`,`enrollmentstatus`.`DeceasedDate` AS `DeceasedDate`,`enrollmentstatus`.`DeceasedNote` AS `DeceasedNote`,`enrollmentstatus`.`AutopsyDesc` AS `AutopsyDesc`,`enrollmentstatus`.`AutopsyDate` AS `AutopsyDate`,`enrollmentstatus`.`AutopsyNote` AS `AutopsyNote`,`enrollmentstatus`.`ClosedDesc` AS `ClosedDesc`,`enrollmentstatus`.`ClosedDate` AS `ClosedDate`,`enrollmentstatus`.`ClosedNote` AS `ClosedNote`,`enrollmentstatus`.`EnrollmentNotes` AS `EnrollmentNotes`,`enrollmentstatus`.`modified` AS `modified` from `enrollmentstatus` where (`enrollmentstatus`.`EnrollStatID` > 0);

-- -----------------------------------------------------
-- View `lq_view_instruments`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_instruments` ;
DROP TABLE IF EXISTS `lq_view_instruments`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lq_view_instruments` AS select `i`.`InstrID` AS `InstrID`,`i`.`VID` AS `VID`,`i`.`ProjName` AS `ProjName`,`i`.`PIDN` AS `PIDN_Instrument`,`i`.`InstrType` AS `InstrType`,`i`.`InstrVer` AS `InstrVer`,`i`.`DCDate` AS `DCDate`,`i`.`DCBy` AS `DCBy`,`i`.`DCStatus` AS `DCStatus`,`i`.`DCNotes` AS `DCNotes`,`i`.`ResearchStatus` AS `ResearchStatus`,`i`.`QualityIssue` AS `QualityIssue`,`i`.`QualityIssue2` AS `QualityIssue2`,`i`.`QualityIssue3` AS `QualityIssue3`,`i`.`QualityNotes` AS `QualityNotes`,`i`.`DEDate` AS `DEDate`,`i`.`DEBy` AS `DEBy`,`i`.`DEStatus` AS `DEStatus`,`i`.`DENotes` AS `DENotes`,`i`.`DVDate` AS `DVDate`,`i`.`DVBy` AS `DVBy`,`i`.`DVStatus` AS `DVStatus`,`i`.`DVNotes` AS `DVNotes`,`i`.`latestflag` AS `latestflag`,`i`.`FieldStatus` AS `FieldStatus`,`i`.`AgeAtDC` AS `AgeAtDC`,`i`.`modified` AS `modified`,`s`.`Summary` AS `summary` from (`instrumenttracking` `i` join `instrumentsummary` `s` on((`i`.`InstrID` = `s`.`InstrID`))) where (`i`.`InstrID` > 0);

-- -----------------------------------------------------
-- View `lq_view_visit`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_visit` ;
DROP TABLE IF EXISTS `lq_view_visit`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lq_view_visit` AS select `visit`.`VID` AS `VID`,`visit`.`PIDN` AS `PIDN_Visit`,`visit`.`ProjName` AS `ProjName`,`visit`.`VLocation` AS `VLocation`,`visit`.`VType` AS `VType`,`visit`.`VWith` AS `VWith`,`visit`.`VDate` AS `VDate`,`visit`.`VTime` AS `VTime`,`visit`.`VStatus` AS `VStatus`,`visit`.`VNotes` AS `VNotes`,`visit`.`FUMonth` AS `FUMonth`,`visit`.`FUYear` AS `FUYear`,`visit`.`FUNote` AS `FUNote`,`visit`.`WList` AS `WList`,`visit`.`WListNote` AS `WListNote`,`visit`.`WListDate` AS `WListDate`,`visit`.`VShortDesc` AS `VShortDesc`,`visit`.`AgeAtVisit` AS `AgeAtVisit`,`visit`.`modified` AS `modified` from `visit` where (`visit`.`VID` > 0);





DROP PROCEDURE IF EXISTS `util_CreateLQView`;

DELIMITER $$

CREATE PROCEDURE util_CreateLQView(TableName varchar(50), Instrument varchar(50))
BEGIN

DECLARE lqTargetName varchar(50);
DECLARE sqlText varchar(10000) DEFAULT '';
DECLARE columnName varchar(50);
DECLARE selectColumns varchar(10000) DEFAULT '';
DECLARE done INT DEFAULT 0;
DECLARE c CURSOR FOR SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME=TableName;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

SET lqTargetName = lower(Instrument);
SET lqTargetName = replace(lqTargetName, ' ', '');

SET sqlText = CONCAT(
'-- -----------------------------------------------------\n',
'-- view lq_view_', lqTargetName, '\n',
'-- -----------------------------------------------------\n',
'DROP VIEW IF EXISTS `lq_view_', lqTargetName, '`;\n',
'CREATE OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lq_view_', lqTargetName, '` AS select \n'

);

OPEN c;
read_loop: LOOP
  FETCH c INTO columnName;
  IF done THEN
    LEAVE read_loop;
  END IF;
  SET selectColumns = CONCAT(selectColumns, '`', columnName, '`,\n');
END LOOP;
CLOSE c;

SET selectColumns = LEFT(selectColumns, length(selectColumns)-2);

SET sqlText = CONCAT(
sqlText,
selectColumns,
'\nFROM `instrumenttracking` `t1` inner join `', TableName, '` `t2` on (`t1`.`InstrID` = `t2`.`instr_id`)\n',
'WHERE `t1`.`InstrType` = \'', Instrument, '\';\n'
);

SELECT sqlText;

END

$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `util_CreateLQProc`;

DELIMITER $$

CREATE PROCEDURE util_CreateLQProc(LQ_Scope varchar(50), LQ_Section varchar(50), LQ_Target varchar(50), Instrument varchar(50), DataSource varchar(50))
BEGIN

SELECT CONCAT('\n',
'-- -----------------------------------------------------\n',
'-- procedure lq_get_', LQ_Section, '_', LQ_Target, '\n',
'-- -----------------------------------------------------\n\n',
'DROP PROCEDURE IF EXISTS `lq_get_', LQ_Section, '_', LQ_Target, '`;\n',
'DELIMITER $$\n\n',
'CREATE PROCEDURE `lq_get_', LQ_Section, '_', LQ_Target, '`(user_name varchar(50), host_name varchar(50), query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)\n',
'BEGIN\n',
'CALL lq_audit_event(user_name, host_name, \'', LQ_Scope, '.', LQ_Section, '.', LQ_Target, '\', query_type);\n\n',
'IF query_type = \'Simple\' THEN\n',
'	SELECT p.PIDN, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it \n',
'		INNER JOIN ', DataSource, ' i ON (it.InstrID = i.instr_id) \n',
'		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) \n',
'	WHERE it.InstrType = \'', Instrument, '\' or it.InstrType is null \n',
'	ORDER BY p.pidn, it.DCDate;\n',
'      \n',
'ELSEIF query_type = \'SimpleAllPatients\' THEN\n',
'	SELECT p.PIDN, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  \n',
'		INNER JOIN ', DataSource, ' i ON (it.InstrID = i.instr_id)  \n',
'		RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  \n',
'	WHERE it.InstrType =  \'', Instrument, '\' or it.InstrType is null \n',
'	ORDER BY P.pidn, it.DCDate;\n',
'	\n',
'ELSEIF query_type = \'PrimaryAll\' THEN \n',
'	CREATE TEMPORARY TABLE temp_linkdata as \n',
'		SELECT p.PIDN,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  \n',
'		FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  \n',
'			INNER JOIN ', DataSource, ' d ON (i.InstrID=d.instr_id) \n',
'		WHERE NOT i.DCStatus = \'Scheduled\' AND NOT i.DCStatus like \'Canceled%\' \n',
'		ORDER BY p.pidn, i.DCDate, i.InstrID ;\n',
'	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	\n',
'	SELECT * from temp_linkdata;\n',
'	\n',
'ELSEIF query_type = \'PrimaryLatest\' THEN  \n',
'	CREATE TEMPORARY TABLE temp_linkdata as \n',
'		SELECT p.PIDN, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  \n',
'		FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  \n',
'			INNER JOIN ', DataSource, ' d ON (i.InstrID=d.instr_id) \n',
'		WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  \n',
'			i2.InstrType = \'', Instrument, '\' AND NOT i2.DCStatus = \'Scheduled\' AND NOT i2.DCStatus like \'Canceled%\') \n',
'		ORDER BY p.pidn, i.DCDate, i.InstrID;\n',
'	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	\n',
'	SELECT * from temp_linkdata;\n',
'	\n',
'ELSEIF query_type = \'PrimaryFirst\' THEN\n',
'	CREATE TEMPORARY TABLE temp_linkdata as \n',
'		SELECT p.PIDN,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* \n',
'		FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) \n',
'			INNER JOIN ', DataSource, ' d ON (i.InstrID=d.instr_id)\n',
'		WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND \n',
'			i2.InstrType = \'', Instrument, '\' AND NOT i2.DCStatus = \'Scheduled\' AND NOT i2.DCStatus like \'Canceled%\')\n',
'		ORDER BY p.pidn, i.InstrID;\n',
'	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	\n',
'	SELECT * from temp_linkdata;\n',
'	\n',
'ELSEIF query_type IN (\'SecondaryAll\',\'SecondaryClosest\') THEN\n',
'	#Create candidate table with secondary instruments \n',
'	CREATE TEMPORARY TABLE temp_secondary_candidates AS\n',
'		SELECT l.PIDN, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days \n',
'		FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) \n',
'		WHERE i2.InstrType = \'', Instrument, '\';\n',
'	ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);\n',
'	\n',
'	#get rid of earlier or later instruments as necessary\n',
'	IF query_subtype = \'Earlier\' THEN DELETE from temp_secondary_candidates WHERE Days >0;\n',
'	ELSEIF query_subtype = \'MoreRecent\' THEN DELETE from temp_secondary_candidates WHERE Days <0;\n',
'	END IF;\n',
'	\n',
'	#limit records to specified day range      \n',
'	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;\n',
'\n',
'	#only keep closest if appropriate\n',
'	IF query_type = \'SecondaryClosest\' THEN\n',
'		CREATE TEMPORARY TABLE temp_secondary_closest AS\n',
'			SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days \n',
'			FROM temp_secondary_candidates\n',
'			GROUP BY pidn,link_date,link_id;\n',
'		ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);\n',
'		DELETE FROM temp_secondary_candidates\n',
'			WHERE ABS(days) <> \n',
'				(SELECT min_days \n',
'				FROM temp_secondary_closest s2 \n',
'				WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));\n',
'		DROP TABLE temp_secondary_closest;\n',
'	END IF;\n',
'\n',
'	SELECT l.PIDN, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* \n',
'	FROM temp_linkdata l\n',
'		LEFT OUTER JOIN temp_secondary_candidates ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) \n',
'		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)\n',
'		LEFT JOIN ', DataSource, ' d ON (i.InstrID=d.instr_id) ORDER BY l.pidn, l.link_date, l.link_id;\n',
'\n',
'	DROP TABLE temp_secondary_candidates;\n',
'\n',
'END IF;\n',
'\n',
'END$$\n',
'\n',
'$$\n',
'DELIMITER ;\n',
'\n');

END

$$
DELIMITER ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
