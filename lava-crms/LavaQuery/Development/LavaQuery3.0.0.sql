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



DELETE from query_objects where instance='lava' and scope='crms' and module='query' and section='nacc';

INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsappraisal','UDS Appraisal',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udscdr','UDS CDR',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsdiagnosis','UDS Diagnosis',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsfamilyhistory1','UDS Family History(V1)',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsfamilyhistory2','UDS Family History(V2)',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsfamilyhistory2Extended','UDS Family History(V2-Extended)',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsfaq','UDS FAQ',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsformchecklist','UDS Form Checklist',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsgds','UDS GDS',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udshachinski','UDS Hachinski',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udshealthhistory','UDS Health History',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsinformantdemo','UDS Informant Demo',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udslabsimaging','UDS Labs Imaging',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsmedications2','UDS Medication Details (V2)',1,0,0);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsmilestone','UDS Milestone',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsneuropsych','UDS Neuropsych',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsnpi','UDS NPI',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsphoneinclusion','UDS Phone Inclusion',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsphysical','UDS Physical',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udssubjectdemo','UDS Subject Demo',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udssymptomsonset','UDS Symptoms Onset',1,1,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','nacc','udsupdrs','UDS UPDRS',1,1,1);


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
-- procedure lq_check_user_auth
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




-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsappraisal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsappraisal` (`instrid` INT, `normal` INT, `focldef` INT, `gaitdis` INT, `eyemove` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsfaq`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsfaq` (`instrid` INT, `bills` INT, `taxes` INT, `shopping` INT, `games` INT, `stove` INT, `mealprep` INT, `events` INT, `payattn` INT, `remdates` INT, `travel` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsformchecklist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsformchecklist` (`instrid` INT, `a2sub` INT, `a2not` INT, `a2comm` INT, `a3sub` INT, `a3not` INT, `a3comm` INT, `a4sub` INT, `a4not` INT, `a4comm` INT, `b1sub` INT, `b1not` INT, `b1comm` INT, `b2sub` INT, `b2not` INT, `b2comm` INT, `b3sub` INT, `b3not` INT, `b3comm` INT, `b5sub` INT, `b5not` INT, `b5comm` INT, `b6sub` INT, `b6not` INT, `b6comm` INT, `b7sub` INT, `b7not` INT, `b7comm` INT, `b8sub` INT, `b8not` INT, `b8comm` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsgds`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsgds` (`instrid` INT, `nogds` INT, `satis` INT, `dropact` INT, `empty` INT, `bored` INT, `spirits` INT, `afraid` INT, `happy` INT, `helpless` INT, `stayhome` INT, `memprob` INT, `wondrful` INT, `wrthless` INT, `energy` INT, `hopeless` INT, `better` INT, `gds` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udscdr`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udscdr` (`instrid` INT, `memory` INT, `orient` INT, `judgement` INT, `commun` INT, `homehobb` INT, `perscare` INT, `cdrsum` INT, `cdrglob` INT, `comport` INT, `cdrlang` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsdiagnosis`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsdiagnosis` (`instrid` INT, `whodiddx` INT, `normcog` INT, `demented` INT, `mciamem` INT, `mciaplus` INT, `mciaplan` INT, `mciapatt` INT, `mciapex` INT, `mciapvis` INT, `mcinon1` INT, `mcin1lan` INT, `mcin1att` INT, `mcin1ex` INT, `mcin1vis` INT, `mcinon2` INT, `mcin2lan` INT, `mcin2att` INT, `mcin2ex` INT, `mcin2vis` INT, `impnomci` INT, `probad` INT, `probadif` INT, `possad` INT, `possadif` INT, `dlb` INT, `dlbif` INT, `vasc` INT, `vascif` INT, `vascps` INT, `vascpsif` INT, `alcdem` INT, `alcdemif` INT, `demun` INT, `demunif` INT, `ftd` INT, `ftdif` INT, `ppaph` INT, `ppaphif` INT, `pnaph` INT, `semdeman` INT, `semdemag` INT, `ppaothr` INT, `psp` INT, `pspif` INT, `cort` INT, `cortif` INT, `hunt` INT, `huntif` INT, `prion` INT, `prionif` INT, `meds` INT, `medsif` INT, `dysill` INT, `dysillif` INT, `dep` INT, `depif` INT, `othpsy` INT, `othpsyif` INT, `downs` INT, `downsif` INT, `park` INT, `parkif` INT, `stroke` INT, `strokif` INT, `hyceph` INT, `hycephif` INT, `brninj` INT, `brninjif` INT, `neop` INT, `neopif` INT, `cogoth` INT, `cogothif` INT, `cogothx` INT, `cogoth2` INT, `cogoth2if` INT, `cogoth2x` INT, `cogoth3` INT, `cogoth3if` INT, `cogoth3x` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsfamilyhistory1`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsfamilyhistory1` (`instrid` INT, `a3chg` INT, `parchg` INT, `momdem` INT, `momonset` INT, `momage` INT, `momdage` INT, `daddem` INT, `dadonset` INT, `dadage` INT, `daddage` INT, `sibchg` INT, `twin` INT, `twintype` INT, `sibs` INT, `sibsdem` INT, `sib1ons` INT, `sib1age` INT, `sib2ons` INT, `sib2age` INT, `sib3ons` INT, `sib3age` INT, `sib4ons` INT, `sib4age` INT, `sib5ons` INT, `sib5age` INT, `sib6ons` INT, `sib6age` INT, `kidchg` INT, `kids` INT, `kidsdem` INT, `kids1ons` INT, `kids1age` INT, `kids2ons` INT, `kids2age` INT, `kids3ons` INT, `kids3age` INT, `kids4ons` INT, `kids4age` INT, `kids5ons` INT, `kids5age` INT, `kids6ons` INT, `kids6age` INT, `relchg` INT, `relsdem` INT, `rel1ons` INT, `rel1age` INT, `rel2ons` INT, `rel2age` INT, `rel3ons` INT, `rel3age` INT, `rel4ons` INT, `rel4age` INT, `rel5ons` INT, `rel5age` INT, `rel6ons` INT, `rel6age` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsfamilyhistory2`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsfamilyhistory2` (`instrid` INT, `momyob` INT, `momliv` INT, `momyod` INT, `momdem` INT, `momonset` INT, `dadyob` INT, `dadliv` INT, `dadyod` INT, `daddem` INT, `dadonset` INT, `twin` INT, `twintype` INT, `sibs` INT, `sib1yob` INT, `sib1liv` INT, `sib1yod` INT, `sib1dem` INT, `sib1ons` INT, `sib2yob` INT, `sib2liv` INT, `sib2yod` INT, `sib2dem` INT, `sib2ons` INT, `sib3yob` INT, `sib3liv` INT, `sib3yod` INT, `sib3dem` INT, `sib3ons` INT, `sib4yob` INT, `sib4liv` INT, `sib4yod` INT, `sib4dem` INT, `sib4ons` INT, `sib5yob` INT, `sib5liv` INT, `sib5yod` INT, `sib5dem` INT, `sib5ons` INT, `sib6yob` INT, `sib6liv` INT, `sib6yod` INT, `sib6dem` INT, `sib6ons` INT, `sib7yob` INT, `sib7liv` INT, `sib7yod` INT, `sib7dem` INT, `sib7ons` INT, `sib8yob` INT, `sib8liv` INT, `sib8yod` INT, `sib8dem` INT, `sib8ons` INT, `sib9yob` INT, `sib9liv` INT, `sib9yod` INT, `sib9dem` INT, `sib9ons` INT, `sib10yob` INT, `sib10liv` INT, `sib10yod` INT, `sib10dem` INT, `sib10ons` INT, `kids` INT, `kid1yob` INT, `kid1liv` INT, `kid1yod` INT, `kid1dem` INT, `kid1ons` INT, `kid2yob` INT, `kid2liv` INT, `kid2yod` INT, `kid2dem` INT, `kid2ons` INT, `kid3yob` INT, `kid3liv` INT, `kid3yod` INT, `kid3dem` INT, `kid3ons` INT, `kid4yob` INT, `kid4liv` INT, `kid4yod` INT, `kid4dem` INT, `kid4ons` INT, `kid5yob` INT, `kid5liv` INT, `kid5yod` INT, `kid5dem` INT, `kid5ons` INT, `kid6yob` INT, `kid6liv` INT, `kid6yod` INT, `kid6dem` INT, `kid6ons` INT, `kid7yob` INT, `kid7liv` INT, `kid7yod` INT, `kid7dem` INT, `kid7ons` INT, `kid8yob` INT, `kid8liv` INT, `kid8yod` INT, `kid8dem` INT, `kid8ons` INT, `kid9yob` INT, `kid9liv` INT, `kid9yod` INT, `kid9dem` INT, `kid9ons` INT, `kid10yob` INT, `kid10liv` INT, `kid10yod` INT, `kid10dem` INT, `kid10ons` INT, `relsdem` INT, `rel1yob` INT, `rel1liv` INT, `rel1yod` INT, `rel1ons` INT, `rel2yob` INT, `rel2liv` INT, `rel2yod` INT, `rel2ons` INT, `rel3yob` INT, `rel3liv` INT, `rel3yod` INT, `rel3ons` INT, `rel4yob` INT, `rel4liv` INT, `rel4yod` INT, `rel4ons` INT, `rel5yob` INT, `rel5liv` INT, `rel5yod` INT, `rel5ons` INT, `rel6yob` INT, `rel6liv` INT, `rel6yod` INT, `rel6ons` INT, `rel7yob` INT, `rel7liv` INT, `rel7yod` INT, `rel7ons` INT, `rel8yob` INT, `rel8liv` INT, `rel8yod` INT, `rel8ons` INT, `rel9yob` INT, `rel9liv` INT, `rel9yod` INT, `rel9ons` INT, `rel10yob` INT, `rel10liv` INT, `rel10yod` INT, `rel10ons` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsfamilyhistory2extended`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsfamilyhistory2extended` (`instrid` INT, `sib11yob` INT, `sib11liv` INT, `sib11yod` INT, `sib11dem` INT, `sib11ons` INT, `sib12yob` INT, `sib12liv` INT, `sib12yod` INT, `sib12dem` INT, `sib12ons` INT, `sib13yob` INT, `sib13liv` INT, `sib13yod` INT, `sib13dem` INT, `sib13ons` INT, `sib14yob` INT, `sib14liv` INT, `sib14yod` INT, `sib14dem` INT, `sib14ons` INT, `sib15yob` INT, `sib15liv` INT, `sib15yod` INT, `sib15dem` INT, `sib15ons` INT, `sib16yob` INT, `sib16liv` INT, `sib16yod` INT, `sib16dem` INT, `sib16ons` INT, `sib17yob` INT, `sib17liv` INT, `sib17yod` INT, `sib17dem` INT, `sib17ons` INT, `sib18yob` INT, `sib18liv` INT, `sib18yod` INT, `sib18dem` INT, `sib18ons` INT, `sib19yob` INT, `sib19liv` INT, `sib19yod` INT, `sib19dem` INT, `sib19ons` INT, `sib20yob` INT, `sib20liv` INT, `sib20yod` INT, `sib20dem` INT, `sib20ons` INT, `kid11yob` INT, `kid11liv` INT, `kid11yod` INT, `kid11dem` INT, `kid11ons` INT, `kid12yob` INT, `kid12liv` INT, `kid12yod` INT, `kid12dem` INT, `kid12ons` INT, `kid13yob` INT, `kid13liv` INT, `kid13yod` INT, `kid13dem` INT, `kid13ons` INT, `kid14yob` INT, `kid14liv` INT, `kid14yod` INT, `kid14dem` INT, `kid14ons` INT, `kid15yob` INT, `kid15liv` INT, `kid15yod` INT, `kid15dem` INT, `kid15ons` INT, `rel11yob` INT, `rel11liv` INT, `rel11yod` INT, `rel11ons` INT, `rel12yob` INT, `rel12liv` INT, `rel12yod` INT, `rel12ons` INT, `rel13yob` INT, `rel13liv` INT, `rel13yod` INT, `rel13ons` INT, `rel14yob` INT, `rel14liv` INT, `rel14yod` INT, `rel14ons` INT, `rel15yob` INT, `rel15liv` INT, `rel15yod` INT, `rel15ons` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udshachinski`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udshachinski` (`instrid` INT, `abrupt` INT, `stepwise` INT, `somatic` INT, `emot` INT, `hxhyper` INT, `hxstroke` INT, `foclsym` INT, `foclsign` INT, `hachin` INT, `cvdcog` INT, `strokcog` INT, `cvdimag` INT, `cvdimag1` INT, `cvdimag2` INT, `cvdimag3` INT, `cvdimag4` INT, `cvdimagx` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udshealthhistory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udshealthhistory` (`instrid` INT, `cvhatt` INT, `cvafib` INT, `cvangio` INT, `cvbypass` INT, `cvpace` INT, `cvchf` INT, `cvothr` INT, `cvothrx` INT, `cbstroke` INT, `strok1yr` INT, `strok2yr` INT, `strok3yr` INT, `strok4yr` INT, `strok5yr` INT, `strok6yr` INT, `cbtia` INT, `tia1yr` INT, `tia2yr` INT, `tia3yr` INT, `tia4yr` INT, `tia5yr` INT, `tia6yr` INT, `cbothr` INT, `cbothrx` INT, `pd` INT, `pdyr` INT, `pdothr` INT, `pdothryr` INT, `seizures` INT, `traumbrf` INT, `traumext` INT, `traumchr` INT, `ncothr` INT, `ncothrx` INT, `hyperten` INT, `hypercho` INT, `diabetes` INT, `b12def` INT, `thyroid` INT, `incontu` INT, `incontf` INT, `dep2yrs` INT, `depothr` INT, `alcohol` INT, `tobac30` INT, `tobac100` INT, `smokyrs` INT, `packsper` INT, `quitsmok` INT, `abusothr` INT, `abusx` INT, `psycdis` INT, `psycdisx` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsinformantdemo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsinformantdemo` (`instrid` INT, `inbirmo` INT, `inbiryr` INT, `insex` INT, `newinf` INT, `inhisp` INT, `inhispor` INT, `inhispox` INT, `inrace` INT, `inracex` INT, `inrasec` INT, `inrasecx` INT, `inrater` INT, `inraterx` INT, `ineduc` INT, `inrelto` INT, `inreltox` INT, `inlivwth` INT, `invisits` INT, `incalls` INT, `inrely` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udslabsimaging`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udslabsimaging` (`instrid` INT, `ctflm` INT, `ctdig` INT, `mri1flm` INT, `mri1dig` INT, `mri2flm` INT, `mri2dig` INT, `mri3flm` INT, `mri3dig` INT, `mrispflm` INT, `mrispdig` INT, `spectflm` INT, `spectdig` INT, `petflm` INT, `petdig` INT, `dna` INT, `csfantem` INT, `serum` INT, `apoe` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsmedications2`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsmedications2` (`instrid` INT, `anymeds` INT, `drugid` INT, `brand` INT, `generic` INT, `notlisted` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsmilestone`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsmilestone` (`instrid` INT, `deceased` INT, `deathmo` INT, `deathdy` INT, `deathyr` INT, `autopsy` INT, `discont` INT, `discmo` INT, `discdy` INT, `discyr` INT, `discreas` INT, `discreax` INT, `rejoined` INT, `nursehome` INT, `nursemo` INT, `nursedy` INT, `nurseyr` INT, `protocol` INT, `npsytest` INT, `npcogimp` INT, `npphyill` INT, `nphomen` INT, `nprefus` INT, `npothrea` INT, `npothrex` INT, `phyndata` INT, `phycog` INT, `phyill` INT, `phyhome` INT, `phyrefus` INT, `phyoth` INT, `phyothx` INT, `udsactiv` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsneuropsych`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsneuropsych` (`instrid` INT, `mmseloc` INT, `mmselan` INT, `mmselanx` INT, `mmseorda` INT, `mmseorlo` INT, `pentagon` INT, `mmse` INT, `npsycloc` INT, `npsylan` INT, `npsylanx` INT, `logimo` INT, `logiday` INT, `logiyr` INT, `logiprev` INT, `logimem` INT, `digif` INT, `digiflen` INT, `digib` INT, `digiblen` INT, `animals` INT, `veg` INT, `traila` INT, `trailarr` INT, `trailali` INT, `trailb` INT, `trailbrr` INT, `trailbli` INT, `wais` INT, `memunits` INT, `memtime` INT, `boston` INT, `cogstat` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsnpi`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsnpi` (`instrid` INT, `npiqinf` INT, `npiqinfx` INT, `del` INT, `delsev` INT, `hall` INT, `hallsev` INT, `agit` INT, `agitsev` INT, `depd` INT, `depdsev` INT, `anx` INT, `anxsev` INT, `elat` INT, `elatsev` INT, `apa` INT, `apasev` INT, `disn` INT, `disnsev` INT, `irr` INT, `irrsev` INT, `mot` INT, `motsev` INT, `nite` INT, `nitesev` INT, `app` INT, `appsev` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsphoneinclusion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsphoneinclusion` (`instrid` INT, `telcog` INT, `telill` INT, `telhome` INT, `telrefu` INT, `telothr` INT, `telothrx` INT, `telmile` INT, `telinper` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsphysical`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsphysical` (`instrid` INT, `height` INT, `weight` INT, `bpsys` INT, `bpdias` INT, `hrate` INT, `vision` INT, `viscorr` INT, `viswcorr` INT, `hearing` INT, `hearaid` INT, `hearwaid` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udssubjectdemo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udssubjectdemo` (`instrid` INT, `inmds` INT, `reason` INT, `reasonx` INT, `refer` INT, `referx` INT, `prestat` INT, `prespart` INT, `source` INT, `birthmo` INT, `birthyr` INT, `sex` INT, `hispanic` INT, `hispor` INT, `hisporx` INT, `race` INT, `racex` INT, `racesec` INT, `racesecx` INT, `raceter` INT, `raceterx` INT, `primlang` INT, `primlanx` INT, `educ` INT, `livsit` INT, `livsitx` INT, `independ` INT, `residenc` INT, `residenx` INT, `zip` INT, `maristat` INT, `maristax` INT, `handed` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udssymptomsonset`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udssymptomsonset` (`instrid` INT, `b9chg` INT, `decsub` INT, `decin` INT, `decclin` INT, `decage` INT, `cogmem` INT, `cogjudg` INT, `coglang` INT, `cogvis` INT, `cogattn` INT, `cogfluc` INT, `cogothr` INT, `cogothrx` INT, `cogfrst` INT, `cogfrstx` INT, `cogmode` INT, `cogmodex` INT, `beapathy` INT, `bedep` INT, `bevhall` INT, `beahall` INT, `bevwell` INT, `bedel` INT, `bedisin` INT, `beirrit` INT, `beagit` INT, `beperch` INT, `berem` INT, `beothr` INT, `beothrx` INT, `befrst` INT, `befrstx` INT, `bemode` INT, `bemodex` INT, `mogait` INT, `mofalls` INT, `motrem` INT, `moslow` INT, `mofrst` INT, `momode` INT, `momodex` INT, `momopark` INT, `course` INT, `frstchg` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_udsupdrs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_udsupdrs` (`instrid` INT, `pdnormal` INT, `speech` INT, `speechx` INT, `facexp` INT, `facexpx` INT, `trestfac` INT, `trestfax` INT, `trestrhd` INT, `trestrhx` INT, `trestlhd` INT, `trestlhx` INT, `trestrft` INT, `trestrfx` INT, `trestlft` INT, `trestlfx` INT, `tractrhd` INT, `tractrhx` INT, `tractlhd` INT, `tractlhx` INT, `rigdneck` INT, `rigdnex` INT, `rigduprt` INT, `rigduprx` INT, `rigduplf` INT, `rigduplx` INT, `rigdlort` INT, `rigdlorx` INT, `rigdlolf` INT, `rigdlolx` INT, `tapsrt` INT, `tapsrtx` INT, `tapslf` INT, `tapslfx` INT, `handmovr` INT, `handmvrx` INT, `handmovl` INT, `handmvlx` INT, `handaltr` INT, `handatrx` INT, `handaltl` INT, `handatlx` INT, `legrt` INT, `legrtx` INT, `leglf` INT, `leglfx` INT, `arising` INT, `arisingx` INT, `posture` INT, `posturex` INT, `gait` INT, `gaitx` INT, `posstab` INT, `posstabx` INT, `bradykin` INT, `bradykix` INT, `packet` INT, `formid` INT, `formver` INT, `adcid` INT, `ptid` INT, `visitmo` INT, `visitday` INT, `visityr` INT, `visitnum` INT, `initials` INT, `packetdate` INT, `packetby` INT, `packetstatus` INT, `packetreason` INT, `packetnotes` INT, `dsdate` INT, `dsby` INT, `dsstatus` INT, `dsreason` INT, `dsnotes` INT, `lcdate` INT, `lcby` INT, `lcstatus` INT, `lcreason` INT, `lcnotes` INT);

-- -----------------------------------------------------
-- procedure lq_get_nacc_udsappraisal
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsappraisal`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsappraisal`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.nacc.udsappraisal',query_type);




	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsappraisal i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Appraisal' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsappraisal i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Appraisal'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsappraisal d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
   SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsappraisal d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Appraisal'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsappraisal d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Appraisal'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Appraisal';
	ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;
		ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsappraisal d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udscdr
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udscdr`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udscdr`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.nacc.udscdr',query_type);

	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udscdr i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS CDR' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udscdr i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS CDR'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udscdr d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udscdr d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS CDR'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udscdr d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS CDR'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS CDR';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udscdr d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsdiagnosis
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsdiagnosis`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsdiagnosis`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.nacc.udsdiagnosis',query_type);

	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsdiagnosis i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Diagnosis' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsdiagnosis i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Diagnosis'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsdiagnosis d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsdiagnosis d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Diagnosis'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsdiagnosis d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Diagnosis'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Diagnosis';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsdiagnosis d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsfamilyhistory1
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsfamilyhistory1`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsfamilyhistory1`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.nacc.udsfamilyhistory1',query_type);

	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsfamilyhistory1 i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Family History' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsfamilyhistory1 i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Family History'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsfamilyhistory1 d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsfamilyhistory1 d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Family History' and i2.InstrVer = '1' AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsfamilyhistory1 d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Family History' and i2.InstrVer = '1' AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Family History' and i2.InstrVer = '1';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsfamilyhistory1 d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsfamilyhistory2
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsfamilyhistory2`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsfamilyhistory2`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.nacc.udsfamilyhistory2',query_type);

	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsfamilyhistory2 i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Family History' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsfamilyhistory2 i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Family History'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsfamilyhistory2 d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsfamilyhistory2 d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Family History' and i2.InstrVer = '2' AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsfamilyhistory2 d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Family History' and i2.InstrVer = '2' AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Family History' and i2.InstrVer = '2';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsfamilyhistory2 d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsfamilyhistory2extended
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsfamilyhistory2extended`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsfamilyhistory2extended`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.nacc.udsfamilyhistory2extended',query_type);

	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsfamilyhistory2extended i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Family History' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsfamilyhistory2extended i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Family History'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsfamilyhistory2extended d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsfamilyhistory2extended d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Family History' and i2.InstrVer = '2' AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsfamilyhistory2extended d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Family History' and i2.InstrVer = '2' AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Family History' and i2.InstrVer = '2';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsfamilyhistory2extended d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsfaq
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsfaq`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsfaq`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.nacc.udsfaq',query_type);

	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsfaq i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS FAQ' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsfaq i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS FAQ'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsfaq d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsfaq d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS FAQ'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsfaq d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS FAQ'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS FAQ';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsfaq d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsformchecklist
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsformchecklist`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsformchecklist`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.nacc.udsformchecklist',query_type);

	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsformchecklist i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Form Checklist' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsformchecklist i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Form Checklist'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsformchecklist d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsformchecklist d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Form Checklist'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsformchecklist d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Form Checklist'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Form Checklist';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsformchecklist d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsgds
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsgds`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsgds`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN



CALL lq_audit_event(user_name,host_name,'crms.nacc.udsgds',query_type);

	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsgds i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS GDS' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsgds i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS GDS'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsgds d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsgds d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS GDS'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsgds d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS GDS'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS GDS';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsgds d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udshachinski
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udshachinski`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udshachinski`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN

	CALL lq_audit_event(user_name,host_name,'crms.nacc.udshachinski',query_type);
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udshachinski i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Hachinski' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udshachinski i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Hachinski'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udshachinski d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udshachinski d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Hachinski'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udshachinski d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Hachinski'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Hachinski';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udshachinski d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udshealthhistory
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udshealthhistory`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udshealthhistory`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN

CALL lq_audit_event(user_name,host_name,'crms.nacc.udshealthhistory',query_type);
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udshealthhistory i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Health History' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udshealthhistory i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Health History'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udshealthhistory d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udshealthhistory d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Health History'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udshealthhistory d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Health History'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Health History';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udshealthhistory d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsinformantdemo
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsinformantdemo`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsinformantdemo`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN
CALL lq_audit_event(user_name,host_name,'crms.nacc.udsinformantdemo',query_type);
	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsinformantdemo i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Informant Demo' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsinformantdemo i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Informant Demo'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsinformantdemo d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsinformantdemo d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Informant Demo'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsinformantdemo d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Informant Demo'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Informant Demo';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsinformantdemo d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udslabsimaging
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udslabsimaging`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udslabsimaging`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN
CALL lq_audit_event(user_name,host_name,'crms.nacc.udslabsimaging',query_type);
	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udslabsimaging i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Labs Imaging' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udslabsimaging i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Labs Imaging'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udslabsimaging d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udslabsimaging d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Labs Imaging'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udslabsimaging d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Labs Imaging'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Labs Imaging';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udslabsimaging d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsmedications2
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsmedications2`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsmedications2`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN
CALL lq_audit_event(user_name,host_name,'crms.nacc.udsmedication2',query_type);
	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsmedications2 i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Medications' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsmedications2 i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Medications'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsmedications2 d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsmedications2 d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Medications' and i2.InstrVer = '2' AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsmedications2 d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Medications' and i2.InstrVer = '2' AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Medications' and i2.InstrVer = '2';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsmedications2 d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsmilestone
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsmilestone`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsmilestone`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN
CALL lq_audit_event(user_name,host_name,'crms.nacc.udsmilestone',query_type);
	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsmilestone i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Milestone' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsmilestone i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Milestone'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsmilestone d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsmilestone d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Milestone'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsmilestone d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Milestone'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Milestone';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsmilestone d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsneuropsych
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsneuropsych`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsneuropsych`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN
CALL lq_audit_event(user_name,host_name,'crms.nacc.udsneuropsych',query_type);
	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsneuropsych i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Neuropsych' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsneuropsych i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Neuropsych'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsneuropsych d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsneuropsych d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Neuropsych'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsneuropsych d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Neuropsych'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Neuropsych';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsneuropsych d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsnpi
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsnpi`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsnpi`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN
CALL lq_audit_event(user_name,host_name,'crms.nacc.udsnpi',query_type);
	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsnpi i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS NPI' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsnpi i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS NPI'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsnpi d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsnpi d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS NPI'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsnpi d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS NPI'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS NPI';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsnpi d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsphoneinclusion
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsphoneinclusion`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsphoneinclusion`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN
CALL lq_audit_event(user_name,host_name,'crms.nacc.udsphoneinclusion',query_type);
	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsphoneinclusion i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Phone Inclusion' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsphoneinclusion i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Phone Inclusion'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsphoneinclusion d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsphoneinclusion d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Phone Inclusion'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsphoneinclusion d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Phone Inclusion'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Phone Inclusion';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsphoneinclusion d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsphysical
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsphysical`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsphysical`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN

CALL lq_audit_event(user_name,host_name,'crms.nacc.udsphysical',query_type);
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsphysical i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Physical' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsphysical i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Physical'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsphysical d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsphysical d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Physical'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsphysical d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Physical'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Physical';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsphysical d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udssubjectdemo
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udssubjectdemo`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udssubjectdemo`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN
CALL lq_audit_event(user_name,host_name,'crms.nacc.udssubjectdemo',query_type);
	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udssubjectdemo i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Subject Demo' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udssubjectdemo i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Subject Demo'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udssubjectdemo d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udssubjectdemo d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Subject Demo'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udssubjectdemo d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Subject Demo'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Subject Demo';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udssubjectdemo d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udssymptomsonset
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udssymptomsonset`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udssymptomsonset`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN
CALL lq_audit_event(user_name,host_name,'crms.nacc.udssymptomsonset',query_type);
	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udssymptomsonset i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS Symptoms Onset' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udssymptomsonset i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS Symptoms Onset'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udssymptomsonset d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udssymptomsonset d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS Symptoms Onset'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udssymptomsonset d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS Symptoms Onset'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS Symptoms Onset';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udssymptomsonset d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_nacc_udsupdrs
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_get_nacc_udsupdrs`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_get_nacc_udsupdrs`(user_name varchar(50), host_name varchar(50),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN
CALL lq_audit_event(user_name,host_name,'crms.nacc.udsupdrs',query_type);
	
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it 
		INNER JOIN lq_view_udsupdrs i ON (it.InstrID = i.InstrID) 
		INNER JOIN temp_pidn p ON (p.PIDN = it.PIDN) 
      WHERE it.InstrType = 'UDS UPDRS' or it.InstrType is null 
      ORDER BY p.pidn, it.DCDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, it.InstrType, it.DCDate, it.DCStatus, it.AgeAtDC, i.* FROM instrumenttracking it  
	 	INNER JOIN lq_view_udsupdrs i ON (it.InstrID = i.InstrID)  
	  RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = it.PIDN)  
	WHERE it.InstrType =  'UDS UPDRS'  or it.InstrType is null 
	ORDER BY P.pidn, it.DCDate;

ELSEIF query_type = 'PrimaryAll' THEN 
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
		INNER JOIN lq_view_udsupdrs d ON (i.InstrID=d.InstrID) 
	WHERE NOT i.DCStatus = 'Scheduled' AND NOT i.DCStatus like 'Canceled%' 
	ORDER BY p.pidn, i.DCDate, i.InstrID ;
   	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryLatest' THEN  
	CREATE TEMPORARY TABLE temp_linkdata as 

  SELECT p.pidn, i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.*  
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN)  
	           INNER JOIN lq_view_udsupdrs d ON (i.InstrID=d.InstrID) 
	WHERE i.DCDate = (SELECT MAX(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND  
		i2.InstrType =  'UDS UPDRS'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%') 
	ORDER BY p.pidn, i.DCDate, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type = 'PrimaryFirst' THEN

  CREATE TEMPORARY TABLE temp_linkdata as 
	SELECT p.pidn,i.DCDate as link_date,i.InstrID as link_id, i.InstrType, i.DCDate, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_pidn p INNER JOIN instrumenttracking i ON (p.PIDN=i.PIDN) 
		INNER JOIN lq_view_udsupdrs d ON (i.InstrID=d.InstrID)
	WHERE i.DCDate = (SELECT MIN(i2.DCDate) from instrumenttracking i2 WHERE i2.PIDN=p.PIDN AND 
		i2.InstrType =  'UDS UPDRS'  AND NOT i2.DCStatus = 'Scheduled' AND NOT i2.DCStatus like 'Canceled%')
	ORDER BY p.pidn, i.InstrID;
  	ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);	
  SELECT * from temp_linkdata;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	#Create candidate table with secondary instruments 
	CREATE TEMPORARY TABLE temp_secondary_candidates AS
   SELECT l.pidn, l.link_date, l.link_id, i2.InstrType, i2.InstrID, DATEDIFF(l.link_date, i2.DCDate) AS Days 
	 FROM temp_linkdata l INNER JOIN instrumenttracking i2 ON (i2.PIDN=l.PIDN) 
	 WHERE i2.InstrType = 'UDS UPDRS';

		ALTER TABLE temp_secondary_candidates ADD INDEX(pidn,link_date,link_id,Days);
	
	#get rid of earlier or later instruments as necessary
	IF query_subtype = 'Earlier' THEN
	  	DELETE from temp_secondary_candidates WHERE Days >0;
	ELSEIF query_subtype = 'MoreRecent' THEN
		DELETE from temp_secondary_candidates WHERE Days <0;
	END IF;
	

	#limit records to specified day range      
	DELETE FROM temp_secondary_candidates WHERE abs(Days) > query_days;

    	#only keep closest if appropriate
	IF query_type = 'SecondaryClosest' THEN
		
      CREATE TEMPORARY TABLE temp_secondary_closest AS
        SELECT pidn,link_date,link_id,MIN(ABS(Days)) as min_days 
        FROM temp_secondary_candidates
        GROUP BY pidn,link_date,link_id;

ALTER TABLE temp_secondary_closest ADD INDEX (pidn,link_date,link_id);
		
      DELETE FROM temp_secondary_candidates
      WHERE ABS(days) <> 
        (SELECT min_days 
        FROM temp_secondary_closest s2 
        WHERE (s2.pidn = temp_secondary_candidates.pidn and s2.link_date=temp_secondary_candidates.link_date and s2.link_id=temp_secondary_candidates.link_id));
      DROP TABLE temp_secondary_closest;

  END IF;


	SELECT l.pidn, l.link_date,l.link_id, i.InstrType,i.DCDate, temp_secondary_candidates.days as DayDiff, i.DCStatus, i.AgeAtDC, d.* 
	FROM temp_linkdata l LEFT OUTER JOIN temp_secondary_candidates 
     ON (l.pidn=temp_secondary_candidates.pidn and l.link_date = temp_secondary_candidates.link_date and l.link_id=temp_secondary_candidates.link_id) 
		LEFT JOIN instrumenttracking i ON (temp_secondary_candidates.InstrID=i.InstrID)
		LEFT JOIN lq_view_udsupdrs d ON (i.InstrID=d.InstrID) ORDER BY l.pidn, l.link_date, l.link_id;

	DROP TABLE temp_secondary_candidates;
END IF;
END$$

$$
DELIMITER ;


DELIMITER ;
-- -----------------------------------------------------
-- View `lq_view_udsappraisal`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsappraisal` ;
DROP TABLE IF EXISTS `lq_view_udsappraisal`;
DELIMITER $$
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsappraisal` AS select `t1`.`InstrID` AS `instrid`,`t2`.`NORMAL` AS `normal`,`t2`.`FOCLDEF` AS `focldef`,`t2`.`GAITDIS` AS `gaitdis`,`t2`.`EYEMOVE` AS `eyemove`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udsappraisal` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Appraisal')
$$
DELIMITER ;

;

-- -----------------------------------------------------
-- View `lq_view_udsfaq`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsfaq` ;
DROP TABLE IF EXISTS `lq_view_udsfaq`;
DELIMITER $$
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsfaq` AS select `t1`.`InstrID` AS `instrid`,`t2`.`BILLS` AS `bills`,`t2`.`TAXES` AS `taxes`,`t2`.`SHOPPING` AS `shopping`,`t2`.`GAMES` AS `games`,`t2`.`STOVE` AS `stove`,`t2`.`MEALPREP` AS `mealprep`,`t2`.`EVENTS` AS `events`,`t2`.`PAYATTN` AS `payattn`,`t2`.`REMDATES` AS `remdates`,`t2`.`TRAVEL` AS `travel`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udsfaq` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS FAQ')
$$
DELIMITER ;

;

-- -----------------------------------------------------
-- View `lq_view_udsformchecklist`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsformchecklist` ;
DROP TABLE IF EXISTS `lq_view_udsformchecklist`;
DELIMITER $$
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsformchecklist` AS select `t1`.`InstrID` AS `instrid`,`t2`.`A2SUB` AS `a2sub`,`t2`.`A2NOT` AS `a2not`,`t2`.`A2COMM` AS `a2comm`,`t2`.`A3SUB` AS `a3sub`,`t2`.`A3NOT` AS `a3not`,`t2`.`A3COMM` AS `a3comm`,`t2`.`A4SUB` AS `a4sub`,`t2`.`A4NOT` AS `a4not`,`t2`.`A4COMM` AS `a4comm`,`t2`.`B1SUB` AS `b1sub`,`t2`.`B1NOT` AS `b1not`,`t2`.`B1COMM` AS `b1comm`,`t2`.`B2SUB` AS `b2sub`,`t2`.`B2NOT` AS `b2not`,`t2`.`B2COMM` AS `b2comm`,`t2`.`B3SUB` AS `b3sub`,`t2`.`B3NOT` AS `b3not`,`t2`.`B3COMM` AS `b3comm`,`t2`.`B5SUB` AS `b5sub`,`t2`.`B5NOT` AS `b5not`,`t2`.`B5COMM` AS `b5comm`,`t2`.`B6SUB` AS `b6sub`,`t2`.`B6NOT` AS `b6not`,`t2`.`B6COMM` AS `b6comm`,`t2`.`B7SUB` AS `b7sub`,`t2`.`B7NOT` AS `b7not`,`t2`.`B7COMM` AS `b7comm`,`t2`.`B8SUB` AS `b8sub`,`t2`.`B8NOT` AS `b8not`,`t2`.`B8COMM` AS `b8comm`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udsformchecklist` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Form Checklist')
$$
DELIMITER ;

;

-- -----------------------------------------------------
-- View `lq_view_udsgds`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsgds` ;
DROP TABLE IF EXISTS `lq_view_udsgds`;
DELIMITER $$
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsgds` AS select `t1`.`InstrID` AS `instrid`,`t2`.`NOGDS` AS `nogds`,`t2`.`SATIS` AS `satis`,`t2`.`DROPACT` AS `dropact`,`t2`.`EMPTY` AS `empty`,`t2`.`BORED` AS `bored`,`t2`.`SPIRITS` AS `spirits`,`t2`.`AFRAID` AS `afraid`,`t2`.`HAPPY` AS `happy`,`t2`.`HELPLESS` AS `helpless`,`t2`.`STAYHOME` AS `stayhome`,`t2`.`MEMPROB` AS `memprob`,`t2`.`WONDRFUL` AS `wondrful`,`t2`.`WRTHLESS` AS `wrthless`,`t2`.`ENERGY` AS `energy`,`t2`.`HOPELESS` AS `hopeless`,`t2`.`BETTER` AS `better`,`t2`.`GDS` AS `gds`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udsgds` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS GDS')
$$
DELIMITER ;

;

-- -----------------------------------------------------
-- View `lq_view_udscdr`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udscdr` ;
DROP TABLE IF EXISTS `lq_view_udscdr`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udscdr` AS select `t1`.`InstrID` AS `instrid`,`t2`.`MEMORY` AS `memory`,`t2`.`ORIENT` AS `orient`,`t2`.`JUDGEMENT` AS `judgement`,`t2`.`COMMUN` AS `commun`,`t2`.`HOMEHOBB` AS `homehobb`,`t2`.`PERSCARE` AS `perscare`,`t2`.`CDRSUM` AS `cdrsum`,`t2`.`CDRGLOB` AS `cdrglob`,`t2`.`COMPORT` AS `comport`,`t2`.`CDRLANG` AS `cdrlang`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udscdr` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS CDR');

-- -----------------------------------------------------
-- View `lq_view_udsdiagnosis`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsdiagnosis` ;
DROP TABLE IF EXISTS `lq_view_udsdiagnosis`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsdiagnosis` AS select `t1`.`InstrID` AS `instrid`,`t2`.`WHODIDDX` AS `whodiddx`,`t2`.`NORMCOG` AS `normcog`,`t2`.`DEMENTED` AS `demented`,`t2`.`MCIAMEM` AS `mciamem`,`t2`.`MCIAPLUS` AS `mciaplus`,`t2`.`MCIAPLAN` AS `mciaplan`,`t2`.`MCIAPATT` AS `mciapatt`,`t2`.`MCIAPEX` AS `mciapex`,`t2`.`MCIAPVIS` AS `mciapvis`,`t2`.`MCINON1` AS `mcinon1`,`t2`.`MCIN1LAN` AS `mcin1lan`,`t2`.`MCIN1ATT` AS `mcin1att`,`t2`.`MCIN1EX` AS `mcin1ex`,`t2`.`MCIN1VIS` AS `mcin1vis`,`t2`.`MCINON2` AS `mcinon2`,`t2`.`MCIN2LAN` AS `mcin2lan`,`t2`.`MCIN2ATT` AS `mcin2att`,`t2`.`MCIN2EX` AS `mcin2ex`,`t2`.`MCIN2VIS` AS `mcin2vis`,`t2`.`IMPNOMCI` AS `impnomci`,`t2`.`PROBAD` AS `probad`,`t2`.`PROBADIF` AS `probadif`,`t2`.`POSSAD` AS `possad`,`t2`.`POSSADIF` AS `possadif`,`t2`.`DLB` AS `dlb`,`t2`.`DLBIF` AS `dlbif`,`t2`.`VASC` AS `vasc`,`t2`.`VASCIF` AS `vascif`,`t2`.`VASCPS` AS `vascps`,`t2`.`VASCPSIF` AS `vascpsif`,`t2`.`ALCDEM` AS `alcdem`,`t2`.`ALCDEMIF` AS `alcdemif`,`t2`.`DEMUN` AS `demun`,`t2`.`DEMUNIF` AS `demunif`,`t2`.`FTD` AS `ftd`,`t2`.`FTDIF` AS `ftdif`,`t2`.`PPAPH` AS `ppaph`,`t2`.`PPAPHIF` AS `ppaphif`,`t2`.`PNAPH` AS `pnaph`,`t2`.`SEMDEMAN` AS `semdeman`,`t2`.`SEMDEMAG` AS `semdemag`,`t2`.`PPAOTHR` AS `ppaothr`,`t2`.`PSP` AS `psp`,`t2`.`PSPIF` AS `pspif`,`t2`.`CORT` AS `cort`,`t2`.`CORTIF` AS `cortif`,`t2`.`HUNT` AS `hunt`,`t2`.`HUNTIF` AS `huntif`,`t2`.`PRION` AS `prion`,`t2`.`PRIONIF` AS `prionif`,`t2`.`MEDS` AS `meds`,`t2`.`MEDSIF` AS `medsif`,`t2`.`DYSILL` AS `dysill`,`t2`.`DYSILLIF` AS `dysillif`,`t2`.`DEP` AS `dep`,`t2`.`DEPIF` AS `depif`,`t2`.`OTHPSY` AS `othpsy`,`t2`.`OTHPSYIF` AS `othpsyif`,`t2`.`DOWNS` AS `downs`,`t2`.`DOWNSIF` AS `downsif`,`t2`.`PARK` AS `park`,`t2`.`PARKIF` AS `parkif`,`t2`.`STROKE` AS `stroke`,`t2`.`STROKIF` AS `strokif`,`t2`.`HYCEPH` AS `hyceph`,`t2`.`HYCEPHIF` AS `hycephif`,`t2`.`BRNINJ` AS `brninj`,`t2`.`BRNINJIF` AS `brninjif`,`t2`.`NEOP` AS `neop`,`t2`.`NEOPIF` AS `neopif`,`t2`.`COGOTH` AS `cogoth`,`t2`.`COGOTHIF` AS `cogothif`,`t2`.`COGOTHX` AS `cogothx`,`t2`.`COGOTH2` AS `cogoth2`,`t2`.`COGOTH2IF` AS `cogoth2if`,`t2`.`COGOTH2X` AS `cogoth2x`,`t2`.`COGOTH3` AS `cogoth3`,`t2`.`COGOTH3IF` AS `cogoth3if`,`t2`.`COGOTH3X` AS `cogoth3x`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udsdiagnosis` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Diagnosis');

-- -----------------------------------------------------
-- View `lq_view_udsfamilyhistory1`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsfamilyhistory1` ;
DROP TABLE IF EXISTS `lq_view_udsfamilyhistory1`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsfamilyhistory1` AS select `t1`.`InstrID` AS `instrid`,`t2`.`A3CHG` AS `a3chg`,`t2`.`PARCHG` AS `parchg`,`t2`.`MOMDEM` AS `momdem`,`t2`.`MOMONSET` AS `momonset`,`t2`.`MOMAGE` AS `momage`,`t2`.`MOMDAGE` AS `momdage`,`t2`.`DADDEM` AS `daddem`,`t2`.`DADONSET` AS `dadonset`,`t2`.`DADAGE` AS `dadage`,`t2`.`DADDAGE` AS `daddage`,`t2`.`SIBCHG` AS `sibchg`,`t2`.`TWIN` AS `twin`,`t2`.`TWINTYPE` AS `twintype`,`t2`.`SIBS` AS `sibs`,`t2`.`SIBSDEM` AS `sibsdem`,`t2`.`SIB1ONS` AS `sib1ons`,`t2`.`SIB1AGE` AS `sib1age`,`t2`.`SIB2ONS` AS `sib2ons`,`t2`.`SIB2AGE` AS `sib2age`,`t2`.`SIB3ONS` AS `sib3ons`,`t2`.`SIB3AGE` AS `sib3age`,`t2`.`SIB4ONS` AS `sib4ons`,`t2`.`SIB4AGE` AS `sib4age`,`t2`.`SIB5ONS` AS `sib5ons`,`t2`.`SIB5AGE` AS `sib5age`,`t2`.`SIB6ONS` AS `sib6ons`,`t2`.`SIB6AGE` AS `sib6age`,`t2`.`KIDCHG` AS `kidchg`,`t2`.`KIDS` AS `kids`,`t2`.`KIDSDEM` AS `kidsdem`,`t2`.`KIDS1ONS` AS `kids1ons`,`t2`.`KIDS1AGE` AS `kids1age`,`t2`.`KIDS2ONS` AS `kids2ons`,`t2`.`KIDS2AGE` AS `kids2age`,`t2`.`KIDS3ONS` AS `kids3ons`,`t2`.`KIDS3AGE` AS `kids3age`,`t2`.`KIDS4ONS` AS `kids4ons`,`t2`.`KIDS4AGE` AS `kids4age`,`t2`.`KIDS5ONS` AS `kids5ons`,`t2`.`KIDS5AGE` AS `kids5age`,`t2`.`KIDS6ONS` AS `kids6ons`,`t2`.`KIDS6AGE` AS `kids6age`,`t2`.`RELCHG` AS `relchg`,`t2`.`RELSDEM` AS `relsdem`,`t2`.`REL1ONS` AS `rel1ons`,`t2`.`REL1AGE` AS `rel1age`,`t2`.`REL2ONS` AS `rel2ons`,`t2`.`REL2AGE` AS `rel2age`,`t2`.`REL3ONS` AS `rel3ons`,`t2`.`REL3AGE` AS `rel3age`,`t2`.`REL4ONS` AS `rel4ons`,`t2`.`REL4AGE` AS `rel4age`,`t2`.`REL5ONS` AS `rel5ons`,`t2`.`REL5AGE` AS `rel5age`,`t2`.`REL6ONS` AS `rel6ons`,`t2`.`REL6AGE` AS `rel6age`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udsfamilyhistory1` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where ((`t1`.`InstrType` = _latin1'UDS Family History') and (`t1`.`InstrVer` = _latin1'1'));

-- -----------------------------------------------------
-- View `lq_view_udsfamilyhistory2`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsfamilyhistory2` ;
DROP TABLE IF EXISTS `lq_view_udsfamilyhistory2`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsfamilyhistory2` AS select `t1`.`InstrID` AS `instrid`,`t2`.`MOMYOB` AS `momyob`,`t2`.`MOMLIV` AS `momliv`,`t2`.`MOMYOD` AS `momyod`,`t2`.`MOMDEM` AS `momdem`,`t2`.`MOMONSET` AS `momonset`,`t2`.`DADYOB` AS `dadyob`,`t2`.`DADLIV` AS `dadliv`,`t2`.`DADYOD` AS `dadyod`,`t2`.`DADDEM` AS `daddem`,`t2`.`DADONSET` AS `dadonset`,`t2`.`TWIN` AS `twin`,`t2`.`TWINTYPE` AS `twintype`,`t2`.`SIBS` AS `sibs`,`t2`.`SIB1YOB` AS `sib1yob`,`t2`.`SIB1LIV` AS `sib1liv`,`t2`.`SIB1YOD` AS `sib1yod`,`t2`.`SIB1DEM` AS `sib1dem`,`t2`.`SIB1ONS` AS `sib1ons`,`t2`.`SIB2YOB` AS `sib2yob`,`t2`.`SIB2LIV` AS `sib2liv`,`t2`.`SIB2YOD` AS `sib2yod`,`t2`.`SIB2DEM` AS `sib2dem`,`t2`.`SIB2ONS` AS `sib2ons`,`t2`.`SIB3YOB` AS `sib3yob`,`t2`.`SIB3LIV` AS `sib3liv`,`t2`.`SIB3YOD` AS `sib3yod`,`t2`.`SIB3DEM` AS `sib3dem`,`t2`.`SIB3ONS` AS `sib3ons`,`t2`.`SIB4YOB` AS `sib4yob`,`t2`.`SIB4LIV` AS `sib4liv`,`t2`.`SIB4YOD` AS `sib4yod`,`t2`.`SIB4DEM` AS `sib4dem`,`t2`.`SIB4ONS` AS `sib4ons`,`t2`.`SIB5YOB` AS `sib5yob`,`t2`.`SIB5LIV` AS `sib5liv`,`t2`.`SIB5YOD` AS `sib5yod`,`t2`.`SIB5DEM` AS `sib5dem`,`t2`.`SIB5ONS` AS `sib5ons`,`t2`.`SIB6YOB` AS `sib6yob`,`t2`.`SIB6LIV` AS `sib6liv`,`t2`.`SIB6YOD` AS `sib6yod`,`t2`.`SIB6DEM` AS `sib6dem`,`t2`.`SIB6ONS` AS `sib6ons`,`t2`.`SIB7YOB` AS `sib7yob`,`t2`.`SIB7LIV` AS `sib7liv`,`t2`.`SIB7YOD` AS `sib7yod`,`t2`.`SIB7DEM` AS `sib7dem`,`t2`.`SIB7ONS` AS `sib7ons`,`t2`.`SIB8YOB` AS `sib8yob`,`t2`.`SIB8LIV` AS `sib8liv`,`t2`.`SIB8YOD` AS `sib8yod`,`t2`.`SIB8DEM` AS `sib8dem`,`t2`.`SIB8ONS` AS `sib8ons`,`t2`.`SIB9YOB` AS `sib9yob`,`t2`.`SIB9LIV` AS `sib9liv`,`t2`.`SIB9YOD` AS `sib9yod`,`t2`.`SIB9DEM` AS `sib9dem`,`t2`.`SIB9ONS` AS `sib9ons`,`t2`.`SIB10YOB` AS `sib10yob`,`t2`.`SIB10LIV` AS `sib10liv`,`t2`.`SIB10YOD` AS `sib10yod`,`t2`.`SIB10DEM` AS `sib10dem`,`t2`.`SIB10ONS` AS `sib10ons`,`t3`.`KIDS` AS `kids`,`t3`.`KID1YOB` AS `kid1yob`,`t3`.`KID1LIV` AS `kid1liv`,`t3`.`KID1YOD` AS `kid1yod`,`t3`.`KID1DEM` AS `kid1dem`,`t3`.`KID1ONS` AS `kid1ons`,`t3`.`KID2YOB` AS `kid2yob`,`t3`.`KID2LIV` AS `kid2liv`,`t3`.`KID2YOD` AS `kid2yod`,`t3`.`KID2DEM` AS `kid2dem`,`t3`.`KID2ONS` AS `kid2ons`,`t3`.`KID3YOB` AS `kid3yob`,`t3`.`KID3LIV` AS `kid3liv`,`t3`.`KID3YOD` AS `kid3yod`,`t3`.`KID3DEM` AS `kid3dem`,`t3`.`KID3ONS` AS `kid3ons`,`t3`.`KID4YOB` AS `kid4yob`,`t3`.`KID4LIV` AS `kid4liv`,`t3`.`KID4YOD` AS `kid4yod`,`t3`.`KID4DEM` AS `kid4dem`,`t3`.`KID4ONS` AS `kid4ons`,`t3`.`KID5YOB` AS `kid5yob`,`t3`.`KID5LIV` AS `kid5liv`,`t3`.`KID5YOD` AS `kid5yod`,`t3`.`KID5DEM` AS `kid5dem`,`t3`.`KID5ONS` AS `kid5ons`,`t3`.`KID6YOB` AS `kid6yob`,`t3`.`KID6LIV` AS `kid6liv`,`t3`.`KID6YOD` AS `kid6yod`,`t3`.`KID6DEM` AS `kid6dem`,`t3`.`KID6ONS` AS `kid6ons`,`t3`.`KID7YOB` AS `kid7yob`,`t3`.`KID7LIV` AS `kid7liv`,`t3`.`KID7YOD` AS `kid7yod`,`t3`.`KID7DEM` AS `kid7dem`,`t3`.`KID7ONS` AS `kid7ons`,`t3`.`KID8YOB` AS `kid8yob`,`t3`.`KID8LIV` AS `kid8liv`,`t3`.`KID8YOD` AS `kid8yod`,`t3`.`KID8DEM` AS `kid8dem`,`t3`.`KID8ONS` AS `kid8ons`,`t3`.`KID9YOB` AS `kid9yob`,`t3`.`KID9LIV` AS `kid9liv`,`t3`.`KID9YOD` AS `kid9yod`,`t3`.`KID9DEM` AS `kid9dem`,`t3`.`KID9ONS` AS `kid9ons`,`t3`.`KID10YOB` AS `kid10yob`,`t3`.`KID10LIV` AS `kid10liv`,`t3`.`KID10YOD` AS `kid10yod`,`t3`.`KID10DEM` AS `kid10dem`,`t3`.`KID10ONS` AS `kid10ons`,`t4`.`RELSDEM` AS `relsdem`,`t4`.`REL1YOB` AS `rel1yob`,`t4`.`REL1LIV` AS `rel1liv`,`t4`.`REL1YOD` AS `rel1yod`,`t4`.`REL1ONS` AS `rel1ons`,`t4`.`REL2YOB` AS `rel2yob`,`t4`.`REL2LIV` AS `rel2liv`,`t4`.`REL2YOD` AS `rel2yod`,`t4`.`REL2ONS` AS `rel2ons`,`t4`.`REL3YOB` AS `rel3yob`,`t4`.`REL3LIV` AS `rel3liv`,`t4`.`REL3YOD` AS `rel3yod`,`t4`.`REL3ONS` AS `rel3ons`,`t4`.`REL4YOB` AS `rel4yob`,`t4`.`REL4LIV` AS `rel4liv`,`t4`.`REL4YOD` AS `rel4yod`,`t4`.`REL4ONS` AS `rel4ons`,`t4`.`REL5YOB` AS `rel5yob`,`t4`.`REL5LIV` AS `rel5liv`,`t4`.`REL5YOD` AS `rel5yod`,`t4`.`REL5ONS` AS `rel5ons`,`t4`.`REL6YOB` AS `rel6yob`,`t4`.`REL6LIV` AS `rel6liv`,`t4`.`REL6YOD` AS `rel6yod`,`t4`.`REL6ONS` AS `rel6ons`,`t4`.`REL7YOB` AS `rel7yob`,`t4`.`REL7LIV` AS `rel7liv`,`t4`.`REL7YOD` AS `rel7yod`,`t4`.`REL7ONS` AS `rel7ons`,`t4`.`REL8YOB` AS `rel8yob`,`t4`.`REL8LIV` AS `rel8liv`,`t4`.`REL8YOD` AS `rel8yod`,`t4`.`REL8ONS` AS `rel8ons`,`t4`.`REL9YOB` AS `rel9yob`,`t4`.`REL9LIV` AS `rel9liv`,`t4`.`REL9YOD` AS `rel9yod`,`t4`.`REL9ONS` AS `rel9ons`,`t4`.`REL10YOB` AS `rel10yob`,`t4`.`REL10LIV` AS `rel10liv`,`t4`.`REL10YOD` AS `rel10yod`,`t4`.`REL10ONS` AS `rel10ons`,`t5`.`Packet` AS `packet`,`t5`.`FormID` AS `formid`,`t5`.`FormVer` AS `formver`,`t5`.`ADCID` AS `adcid`,`t5`.`PTID` AS `ptid`,`t5`.`VisitMo` AS `visitmo`,`t5`.`VisitDay` AS `visitday`,`t5`.`VisitYr` AS `visityr`,`t5`.`VisitNum` AS `visitnum`,`t5`.`Initials` AS `initials`,`t5`.`PacketDate` AS `packetdate`,`t5`.`PacketBy` AS `packetby`,`t5`.`PacketStatus` AS `packetstatus`,`t5`.`PacketReason` AS `packetreason`,`t5`.`PacketNotes` AS `packetnotes`,`t5`.`DSDate` AS `dsdate`,`t5`.`DSBy` AS `dsby`,`t5`.`DSStatus` AS `dsstatus`,`t5`.`DSReason` AS `dsreason`,`t5`.`DSNotes` AS `dsnotes`,`t5`.`LCDate` AS `lcdate`,`t5`.`LCBy` AS `lcby`,`t5`.`LCStatus` AS `lcstatus`,`t5`.`LCReason` AS `lcreason`,`t5`.`LCNotes` AS `lcnotes` from ((((`instrumenttracking` `t1` join `udsfamilyhistory2` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udsfamilyhistorychildren2` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) join `udsfamilyhistoryrelatives2` `t4` on((`t1`.`InstrID` = `t4`.`InstrID`))) join `udstracking` `t5` on((`t1`.`InstrID` = `t5`.`InstrID`))) where ((`t1`.`InstrType` = _latin1'UDS Family History') and (`t1`.`InstrVer` = _latin1'2'));

-- -----------------------------------------------------
-- View `lq_view_udsfamilyhistory2extended`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsfamilyhistory2extended` ;
DROP TABLE IF EXISTS `lq_view_udsfamilyhistory2extended`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsfamilyhistory2extended` AS select `t1`.`InstrID` AS `instrid`,`t2`.`SIB11YOB` AS `sib11yob`,`t2`.`SIB11LIV` AS `sib11liv`,`t2`.`SIB11YOD` AS `sib11yod`,`t2`.`SIB11DEM` AS `sib11dem`,`t2`.`SIB11ONS` AS `sib11ons`,`t2`.`SIB12YOB` AS `sib12yob`,`t2`.`SIB12LIV` AS `sib12liv`,`t2`.`SIB12YOD` AS `sib12yod`,`t2`.`SIB12DEM` AS `sib12dem`,`t2`.`SIB12ONS` AS `sib12ons`,`t2`.`SIB13YOB` AS `sib13yob`,`t2`.`SIB13LIV` AS `sib13liv`,`t2`.`SIB13YOD` AS `sib13yod`,`t2`.`SIB13DEM` AS `sib13dem`,`t2`.`SIB13ONS` AS `sib13ons`,`t2`.`SIB14YOB` AS `sib14yob`,`t2`.`SIB14LIV` AS `sib14liv`,`t2`.`SIB14YOD` AS `sib14yod`,`t2`.`SIB14DEM` AS `sib14dem`,`t2`.`SIB14ONS` AS `sib14ons`,`t2`.`SIB15YOB` AS `sib15yob`,`t2`.`SIB15LIV` AS `sib15liv`,`t2`.`SIB15YOD` AS `sib15yod`,`t2`.`SIB15DEM` AS `sib15dem`,`t2`.`SIB15ONS` AS `sib15ons`,`t2`.`SIB16YOB` AS `sib16yob`,`t2`.`SIB16LIV` AS `sib16liv`,`t2`.`SIB16YOD` AS `sib16yod`,`t2`.`SIB16DEM` AS `sib16dem`,`t2`.`SIB16ONS` AS `sib16ons`,`t2`.`SIB17YOB` AS `sib17yob`,`t2`.`SIB17LIV` AS `sib17liv`,`t2`.`SIB17YOD` AS `sib17yod`,`t2`.`SIB17DEM` AS `sib17dem`,`t2`.`SIB17ONS` AS `sib17ons`,`t2`.`SIB18YOB` AS `sib18yob`,`t2`.`SIB18LIV` AS `sib18liv`,`t2`.`SIB18YOD` AS `sib18yod`,`t2`.`SIB18DEM` AS `sib18dem`,`t2`.`SIB18ONS` AS `sib18ons`,`t2`.`SIB19YOB` AS `sib19yob`,`t2`.`SIB19LIV` AS `sib19liv`,`t2`.`SIB19YOD` AS `sib19yod`,`t2`.`SIB19DEM` AS `sib19dem`,`t2`.`SIB19ONS` AS `sib19ons`,`t2`.`SIB20YOB` AS `sib20yob`,`t2`.`SIB20LIV` AS `sib20liv`,`t2`.`SIB20YOD` AS `sib20yod`,`t2`.`SIB20DEM` AS `sib20dem`,`t2`.`SIB20ONS` AS `sib20ons`,`t3`.`KID11YOB` AS `kid11yob`,`t3`.`KID11LIV` AS `kid11liv`,`t3`.`KID11YOD` AS `kid11yod`,`t3`.`KID11DEM` AS `kid11dem`,`t3`.`KID11ONS` AS `kid11ons`,`t3`.`KID12YOB` AS `kid12yob`,`t3`.`KID12LIV` AS `kid12liv`,`t3`.`KID12YOD` AS `kid12yod`,`t3`.`KID12DEM` AS `kid12dem`,`t3`.`KID12ONS` AS `kid12ons`,`t3`.`KID13YOB` AS `kid13yob`,`t3`.`KID13LIV` AS `kid13liv`,`t3`.`KID13YOD` AS `kid13yod`,`t3`.`KID13DEM` AS `kid13dem`,`t3`.`KID13ONS` AS `kid13ons`,`t3`.`KID14YOB` AS `kid14yob`,`t3`.`KID14LIV` AS `kid14liv`,`t3`.`KID14YOD` AS `kid14yod`,`t3`.`KID14DEM` AS `kid14dem`,`t3`.`KID14ONS` AS `kid14ons`,`t3`.`KID15YOB` AS `kid15yob`,`t3`.`KID15LIV` AS `kid15liv`,`t3`.`KID15YOD` AS `kid15yod`,`t3`.`KID15DEM` AS `kid15dem`,`t3`.`KID15ONS` AS `kid15ons`,`t4`.`REL11YOB` AS `rel11yob`,`t4`.`REL11LIV` AS `rel11liv`,`t4`.`REL11YOD` AS `rel11yod`,`t4`.`REL11ONS` AS `rel11ons`,`t4`.`REL12YOB` AS `rel12yob`,`t4`.`REL12LIV` AS `rel12liv`,`t4`.`REL12YOD` AS `rel12yod`,`t4`.`REL12ONS` AS `rel12ons`,`t4`.`REL13YOB` AS `rel13yob`,`t4`.`REL13LIV` AS `rel13liv`,`t4`.`REL13YOD` AS `rel13yod`,`t4`.`REL13ONS` AS `rel13ons`,`t4`.`REL14YOB` AS `rel14yob`,`t4`.`REL14LIV` AS `rel14liv`,`t4`.`REL14YOD` AS `rel14yod`,`t4`.`REL14ONS` AS `rel14ons`,`t4`.`REL15YOB` AS `rel15yob`,`t4`.`REL15LIV` AS `rel15liv`,`t4`.`REL15YOD` AS `rel15yod`,`t4`.`REL15ONS` AS `rel15ons`,`t5`.`Packet` AS `packet`,`t5`.`FormID` AS `formid`,`t5`.`FormVer` AS `formver`,`t5`.`ADCID` AS `adcid`,`t5`.`PTID` AS `ptid`,`t5`.`VisitMo` AS `visitmo`,`t5`.`VisitDay` AS `visitday`,`t5`.`VisitYr` AS `visityr`,`t5`.`VisitNum` AS `visitnum`,`t5`.`Initials` AS `initials`,`t5`.`PacketDate` AS `packetdate`,`t5`.`PacketBy` AS `packetby`,`t5`.`PacketStatus` AS `packetstatus`,`t5`.`PacketReason` AS `packetreason`,`t5`.`PacketNotes` AS `packetnotes`,`t5`.`DSDate` AS `dsdate`,`t5`.`DSBy` AS `dsby`,`t5`.`DSStatus` AS `dsstatus`,`t5`.`DSReason` AS `dsreason`,`t5`.`DSNotes` AS `dsnotes`,`t5`.`LCDate` AS `lcdate`,`t5`.`LCBy` AS `lcby`,`t5`.`LCStatus` AS `lcstatus`,`t5`.`LCReason` AS `lcreason`,`t5`.`LCNotes` AS `lcnotes` from ((((`instrumenttracking` `t1` join `udsfamilyhistory2` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udsfamilyhistorychildren2` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) join `udsfamilyhistoryrelatives2` `t4` on((`t1`.`InstrID` = `t4`.`InstrID`))) join `udstracking` `t5` on((`t1`.`InstrID` = `t5`.`InstrID`))) where ((`t1`.`InstrType` = _latin1'UDS Family History') and (`t1`.`InstrVer` = _latin1'2'));

-- -----------------------------------------------------
-- View `lq_view_udshachinski`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udshachinski` ;
DROP TABLE IF EXISTS `lq_view_udshachinski`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udshachinski` AS select `t1`.`InstrID` AS `instrid`,`t2`.`ABRUPT` AS `abrupt`,`t2`.`STEPWISE` AS `stepwise`,`t2`.`SOMATIC` AS `somatic`,`t2`.`EMOT` AS `emot`,`t2`.`HXHYPER` AS `hxhyper`,`t2`.`HXSTROKE` AS `hxstroke`,`t2`.`FOCLSYM` AS `foclsym`,`t2`.`FOCLSIGN` AS `foclsign`,`t2`.`HACHIN` AS `hachin`,`t2`.`CVDCOG` AS `cvdcog`,`t2`.`STROKCOG` AS `strokcog`,`t2`.`CVDIMAG` AS `cvdimag`,`t2`.`CVDIMAG1` AS `cvdimag1`,`t2`.`CVDIMAG2` AS `cvdimag2`,`t2`.`CVDIMAG3` AS `cvdimag3`,`t2`.`CVDIMAG4` AS `cvdimag4`,`t2`.`CVDIMAGX` AS `cvdimagx`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udshachinski` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Hachinski');

-- -----------------------------------------------------
-- View `lq_view_udshealthhistory`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udshealthhistory` ;
DROP TABLE IF EXISTS `lq_view_udshealthhistory`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udshealthhistory` AS select `t1`.`InstrID` AS `instrid`,`t2`.`CVHATT` AS `cvhatt`,`t2`.`CVAFIB` AS `cvafib`,`t2`.`CVANGIO` AS `cvangio`,`t2`.`CVBYPASS` AS `cvbypass`,`t2`.`CVPACE` AS `cvpace`,`t2`.`CVCHF` AS `cvchf`,`t2`.`CVOTHR` AS `cvothr`,`t2`.`CVOTHRX` AS `cvothrx`,`t2`.`CBSTROKE` AS `cbstroke`,`t2`.`STROK1YR` AS `strok1yr`,`t2`.`STROK2YR` AS `strok2yr`,`t2`.`STROK3YR` AS `strok3yr`,`t2`.`STROK4YR` AS `strok4yr`,`t2`.`STROK5YR` AS `strok5yr`,`t2`.`STROK6YR` AS `strok6yr`,`t2`.`CBTIA` AS `cbtia`,`t2`.`TIA1YR` AS `tia1yr`,`t2`.`TIA2YR` AS `tia2yr`,`t2`.`TIA3YR` AS `tia3yr`,`t2`.`TIA4YR` AS `tia4yr`,`t2`.`TIA5YR` AS `tia5yr`,`t2`.`TIA6YR` AS `tia6yr`,`t2`.`CBOTHR` AS `cbothr`,`t2`.`CBOTHRX` AS `cbothrx`,`t2`.`PD` AS `pd`,`t2`.`PDYR` AS `pdyr`,`t2`.`PDOTHR` AS `pdothr`,`t2`.`PDOTHRYR` AS `pdothryr`,`t2`.`SEIZURES` AS `seizures`,`t2`.`TRAUMBRF` AS `traumbrf`,`t2`.`TRAUMEXT` AS `traumext`,`t2`.`TRAUMCHR` AS `traumchr`,`t2`.`NCOTHR` AS `ncothr`,`t2`.`NCOTHRX` AS `ncothrx`,`t2`.`HYPERTEN` AS `hyperten`,`t2`.`HYPERCHO` AS `hypercho`,`t2`.`DIABETES` AS `diabetes`,`t2`.`B12DEF` AS `b12def`,`t2`.`THYROID` AS `thyroid`,`t2`.`INCONTU` AS `incontu`,`t2`.`INCONTF` AS `incontf`,`t2`.`DEP2YRS` AS `dep2yrs`,`t2`.`DEPOTHR` AS `depothr`,`t2`.`ALCOHOL` AS `alcohol`,`t2`.`TOBAC30` AS `tobac30`,`t2`.`TOBAC100` AS `tobac100`,`t2`.`SMOKYRS` AS `smokyrs`,`t2`.`PACKSPER` AS `packsper`,`t2`.`QUITSMOK` AS `quitsmok`,`t2`.`ABUSOTHR` AS `abusothr`,`t2`.`ABUSX` AS `abusx`,`t2`.`PSYCDIS` AS `psycdis`,`t2`.`PSYCDISX` AS `psycdisx`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udshealthhistory` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Health History');

-- -----------------------------------------------------
-- View `lq_view_udsinformantdemo`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsinformantdemo` ;
DROP TABLE IF EXISTS `lq_view_udsinformantdemo`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsinformantdemo` AS select `t1`.`InstrID` AS `instrid`,`t2`.`INBIRMO` AS `inbirmo`,`t2`.`INBIRYR` AS `inbiryr`,`t2`.`INSEX` AS `insex`,`t2`.`NEWINF` AS `newinf`,`t2`.`INHISP` AS `inhisp`,`t2`.`INHISPOR` AS `inhispor`,`t2`.`INHISPOX` AS `inhispox`,`t2`.`INRACE` AS `inrace`,`t2`.`INRACEX` AS `inracex`,`t2`.`INRASEC` AS `inrasec`,`t2`.`INRASECX` AS `inrasecx`,`t2`.`INRATER` AS `inrater`,`t2`.`INRATERX` AS `inraterx`,`t2`.`INEDUC` AS `ineduc`,`t2`.`INRELTO` AS `inrelto`,`t2`.`INRELTOX` AS `inreltox`,`t2`.`INLIVWTH` AS `inlivwth`,`t2`.`INVISITS` AS `invisits`,`t2`.`INCALLS` AS `incalls`,`t2`.`INRELY` AS `inrely`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udsinformantdemo` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Informant Demo');

-- -----------------------------------------------------
-- View `lq_view_udslabsimaging`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udslabsimaging` ;
DROP TABLE IF EXISTS `lq_view_udslabsimaging`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udslabsimaging` AS select `t1`.`InstrID` AS `instrid`,`t2`.`CTFLM` AS `ctflm`,`t2`.`CTDIG` AS `ctdig`,`t2`.`MRI1FLM` AS `mri1flm`,`t2`.`MRI1DIG` AS `mri1dig`,`t2`.`MRI2FLM` AS `mri2flm`,`t2`.`MRI2DIG` AS `mri2dig`,`t2`.`MRI3FLM` AS `mri3flm`,`t2`.`MRI3DIG` AS `mri3dig`,`t2`.`MRISPFLM` AS `mrispflm`,`t2`.`MRISPDIG` AS `mrispdig`,`t2`.`SPECTFLM` AS `spectflm`,`t2`.`SPECTDIG` AS `spectdig`,`t2`.`PETFLM` AS `petflm`,`t2`.`PETDIG` AS `petdig`,`t2`.`DNA` AS `dna`,`t2`.`CSFANTEM` AS `csfantem`,`t2`.`SERUM` AS `serum`,`t2`.`APOE` AS `apoe`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udslabsimaging` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Labs Imaging');

-- -----------------------------------------------------
-- View `lq_view_udsmedications2`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsmedications2` ;
DROP TABLE IF EXISTS `lq_view_udsmedications2`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsmedications2` AS select `t1`.`InstrID` AS `instrid`,`t2`.`ANYMEDS` AS `anymeds`,`t4`.`DRUGID` AS `drugid`,`t4`.`Brand` AS `brand`,`t4`.`Generic` AS `generic`,`t4`.`NotListed` AS `notlisted`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from (((`instrumenttracking` `t1` join `udsmedications2` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udsmedicationsdetails2` `t4` on((`t1`.`InstrID` = `t4`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Medications');

-- -----------------------------------------------------
-- View `lq_view_udsmilestone`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsmilestone` ;
DROP TABLE IF EXISTS `lq_view_udsmilestone`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsmilestone` AS select `t1`.`InstrID` AS `instrid`,`t2`.`DECEASED` AS `deceased`,`t2`.`DEATHMO` AS `deathmo`,`t2`.`DEATHDY` AS `deathdy`,`t2`.`DEATHYR` AS `deathyr`,`t2`.`AUTOPSY` AS `autopsy`,`t2`.`DISCONT` AS `discont`,`t2`.`DISCMO` AS `discmo`,`t2`.`DISCDY` AS `discdy`,`t2`.`DISCYR` AS `discyr`,`t2`.`DISCREAS` AS `discreas`,`t2`.`DISCREAX` AS `discreax`,`t2`.`REJOINED` AS `rejoined`,`t2`.`NURSEHOME` AS `nursehome`,`t2`.`NURSEMO` AS `nursemo`,`t2`.`NURSEDY` AS `nursedy`,`t2`.`NURSEYR` AS `nurseyr`,`t2`.`PROTOCOL` AS `protocol`,`t2`.`NPSYTEST` AS `npsytest`,`t2`.`NPCOGIMP` AS `npcogimp`,`t2`.`NPPHYILL` AS `npphyill`,`t2`.`NPHOMEN` AS `nphomen`,`t2`.`NPREFUS` AS `nprefus`,`t2`.`NPOTHREA` AS `npothrea`,`t2`.`NPOTHREX` AS `npothrex`,`t2`.`PHYNDATA` AS `phyndata`,`t2`.`PHYCOG` AS `phycog`,`t2`.`PHYILL` AS `phyill`,`t2`.`PHYHOME` AS `phyhome`,`t2`.`PHYREFUS` AS `phyrefus`,`t2`.`PHYOTH` AS `phyoth`,`t2`.`PHYOTHX` AS `phyothx`,`t2`.`UDSACTIV` AS `udsactiv`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udsmilestone` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Milestone');

-- -----------------------------------------------------
-- View `lq_view_udsneuropsych`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsneuropsych` ;
DROP TABLE IF EXISTS `lq_view_udsneuropsych`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsneuropsych` AS select `t1`.`InstrID` AS `instrid`,`t2`.`MMSELOC` AS `mmseloc`,`t2`.`MMSELAN` AS `mmselan`,`t2`.`MMSELANX` AS `mmselanx`,`t2`.`MMSEORDA` AS `mmseorda`,`t2`.`MMSEORLO` AS `mmseorlo`,`t2`.`PENTAGON` AS `pentagon`,`t2`.`MMSE` AS `mmse`,`t2`.`NPSYCLOC` AS `npsycloc`,`t2`.`NPSYLAN` AS `npsylan`,`t2`.`NPSYLANX` AS `npsylanx`,`t2`.`LOGIMO` AS `logimo`,`t2`.`LOGIDAY` AS `logiday`,`t2`.`LOGIYR` AS `logiyr`,`t2`.`LOGIPREV` AS `logiprev`,`t2`.`LOGIMEM` AS `logimem`,`t2`.`DIGIF` AS `digif`,`t2`.`DIGIFLEN` AS `digiflen`,`t2`.`DIGIB` AS `digib`,`t2`.`DIGIBLEN` AS `digiblen`,`t2`.`ANIMALS` AS `animals`,`t2`.`VEG` AS `veg`,`t2`.`TRAILA` AS `traila`,`t2`.`TRAILARR` AS `trailarr`,`t2`.`TRAILALI` AS `trailali`,`t2`.`TRAILB` AS `trailb`,`t2`.`TRAILBRR` AS `trailbrr`,`t2`.`TRAILBLI` AS `trailbli`,`t2`.`WAIS` AS `wais`,`t2`.`MEMUNITS` AS `memunits`,`t2`.`MEMTIME` AS `memtime`,`t2`.`BOSTON` AS `boston`,`t2`.`COGSTAT` AS `cogstat`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udsneuropsych` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Neuropsych');

-- -----------------------------------------------------
-- View `lq_view_udsnpi`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsnpi` ;
DROP TABLE IF EXISTS `lq_view_udsnpi`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsnpi` AS select `t1`.`InstrID` AS `instrid`,`t2`.`NPIQINF` AS `npiqinf`,`t2`.`NPIQINFX` AS `npiqinfx`,`t2`.`DEL` AS `del`,`t2`.`DELSEV` AS `delsev`,`t2`.`HALL` AS `hall`,`t2`.`HALLSEV` AS `hallsev`,`t2`.`AGIT` AS `agit`,`t2`.`AGITSEV` AS `agitsev`,`t2`.`DEPD` AS `depd`,`t2`.`DEPDSEV` AS `depdsev`,`t2`.`ANX` AS `anx`,`t2`.`ANXSEV` AS `anxsev`,`t2`.`ELAT` AS `elat`,`t2`.`ELATSEV` AS `elatsev`,`t2`.`APA` AS `apa`,`t2`.`APASEV` AS `apasev`,`t2`.`DISN` AS `disn`,`t2`.`DISNSEV` AS `disnsev`,`t2`.`IRR` AS `irr`,`t2`.`IRRSEV` AS `irrsev`,`t2`.`MOT` AS `mot`,`t2`.`MOTSEV` AS `motsev`,`t2`.`NITE` AS `nite`,`t2`.`NITESEV` AS `nitesev`,`t2`.`APP` AS `app`,`t2`.`APPSEV` AS `appsev`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udsnpi` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS NPI');

-- -----------------------------------------------------
-- View `lq_view_udsphoneinclusion`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsphoneinclusion` ;
DROP TABLE IF EXISTS `lq_view_udsphoneinclusion`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsphoneinclusion` AS select `t1`.`InstrID` AS `instrid`,`t2`.`TELCOG` AS `telcog`,`t2`.`TELILL` AS `telill`,`t2`.`TELHOME` AS `telhome`,`t2`.`TELREFU` AS `telrefu`,`t2`.`TELOTHR` AS `telothr`,`t2`.`TELOTHRX` AS `telothrx`,`t2`.`TELMILE` AS `telmile`,`t2`.`TELINPER` AS `telinper`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udsphoneinclusion` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Phone Inclusion');

-- -----------------------------------------------------
-- View `lq_view_udsphysical`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsphysical` ;
DROP TABLE IF EXISTS `lq_view_udsphysical`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsphysical` AS select `t1`.`InstrID` AS `instrid`,`t2`.`HEIGHT` AS `height`,`t2`.`WEIGHT` AS `weight`,`t2`.`BPSYS` AS `bpsys`,`t2`.`BPDIAS` AS `bpdias`,`t2`.`HRATE` AS `hrate`,`t2`.`VISION` AS `vision`,`t2`.`VISCORR` AS `viscorr`,`t2`.`VISWCORR` AS `viswcorr`,`t2`.`HEARING` AS `hearing`,`t2`.`HEARAID` AS `hearaid`,`t2`.`HEARWAID` AS `hearwaid`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udsphysical` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Physical');

-- -----------------------------------------------------
-- View `lq_view_udssubjectdemo`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udssubjectdemo` ;
DROP TABLE IF EXISTS `lq_view_udssubjectdemo`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udssubjectdemo` AS select `t1`.`InstrID` AS `instrid`,`t2`.`INMDS` AS `inmds`,`t2`.`REASON` AS `reason`,`t2`.`REASONX` AS `reasonx`,`t2`.`REFER` AS `refer`,`t2`.`REFERX` AS `referx`,`t2`.`PRESTAT` AS `prestat`,`t2`.`PRESPART` AS `prespart`,`t2`.`SOURCE` AS `source`,`t2`.`BIRTHMO` AS `birthmo`,`t2`.`BIRTHYR` AS `birthyr`,`t2`.`SEX` AS `sex`,`t2`.`HISPANIC` AS `hispanic`,`t2`.`HISPOR` AS `hispor`,`t2`.`HISPORX` AS `hisporx`,`t2`.`RACE` AS `race`,`t2`.`RACEX` AS `racex`,`t2`.`RACESEC` AS `racesec`,`t2`.`RACESECX` AS `racesecx`,`t2`.`RACETER` AS `raceter`,`t2`.`RACETERX` AS `raceterx`,`t2`.`PRIMLANG` AS `primlang`,`t2`.`PRIMLANX` AS `primlanx`,`t2`.`EDUC` AS `educ`,`t2`.`LIVSIT` AS `livsit`,`t2`.`LIVSITX` AS `livsitx`,`t2`.`INDEPEND` AS `independ`,`t2`.`RESIDENC` AS `residenc`,`t2`.`RESIDENX` AS `residenx`,`t2`.`ZIP` AS `zip`,`t2`.`MARISTAT` AS `maristat`,`t2`.`MARISTAX` AS `maristax`,`t2`.`HANDED` AS `handed`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udssubjectdemo` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Subject Demo');

-- -----------------------------------------------------
-- View `lq_view_udssymptomsonset`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udssymptomsonset` ;
DROP TABLE IF EXISTS `lq_view_udssymptomsonset`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udssymptomsonset` AS select `t1`.`InstrID` AS `instrid`,`t2`.`B9CHG` AS `b9chg`,`t2`.`DECSUB` AS `decsub`,`t2`.`DECIN` AS `decin`,`t2`.`DECCLIN` AS `decclin`,`t2`.`DECAGE` AS `decage`,`t2`.`COGMEM` AS `cogmem`,`t2`.`COGJUDG` AS `cogjudg`,`t2`.`COGLANG` AS `coglang`,`t2`.`COGVIS` AS `cogvis`,`t2`.`COGATTN` AS `cogattn`,`t2`.`COGFLUC` AS `cogfluc`,`t2`.`COGOTHR` AS `cogothr`,`t2`.`COGOTHRX` AS `cogothrx`,`t2`.`COGFRST` AS `cogfrst`,`t2`.`COGFRSTX` AS `cogfrstx`,`t2`.`COGMODE` AS `cogmode`,`t2`.`COGMODEX` AS `cogmodex`,`t2`.`BEAPATHY` AS `beapathy`,`t2`.`BEDEP` AS `bedep`,`t2`.`BEVHALL` AS `bevhall`,`t2`.`BEAHALL` AS `beahall`,`t2`.`BEVWELL` AS `bevwell`,`t2`.`BEDEL` AS `bedel`,`t2`.`BEDISIN` AS `bedisin`,`t2`.`BEIRRIT` AS `beirrit`,`t2`.`BEAGIT` AS `beagit`,`t2`.`BEPERCH` AS `beperch`,`t2`.`BEREM` AS `berem`,`t2`.`BEOTHR` AS `beothr`,`t2`.`BEOTHRX` AS `beothrx`,`t2`.`BEFRST` AS `befrst`,`t2`.`BEFRSTX` AS `befrstx`,`t2`.`BEMODE` AS `bemode`,`t2`.`BEMODEX` AS `bemodex`,`t2`.`MOGAIT` AS `mogait`,`t2`.`MOFALLS` AS `mofalls`,`t2`.`MOTREM` AS `motrem`,`t2`.`MOSLOW` AS `moslow`,`t2`.`MOFRST` AS `mofrst`,`t2`.`MOMODE` AS `momode`,`t2`.`MOMODEX` AS `momodex`,`t2`.`MOMOPARK` AS `momopark`,`t2`.`COURSE` AS `course`,`t2`.`FRSTCHG` AS `frstchg`,`t3`.`Packet` AS `packet`,`t3`.`FormID` AS `formid`,`t3`.`FormVer` AS `formver`,`t3`.`ADCID` AS `adcid`,`t3`.`PTID` AS `ptid`,`t3`.`VisitMo` AS `visitmo`,`t3`.`VisitDay` AS `visitday`,`t3`.`VisitYr` AS `visityr`,`t3`.`VisitNum` AS `visitnum`,`t3`.`Initials` AS `initials`,`t3`.`PacketDate` AS `packetdate`,`t3`.`PacketBy` AS `packetby`,`t3`.`PacketStatus` AS `packetstatus`,`t3`.`PacketReason` AS `packetreason`,`t3`.`PacketNotes` AS `packetnotes`,`t3`.`DSDate` AS `dsdate`,`t3`.`DSBy` AS `dsby`,`t3`.`DSStatus` AS `dsstatus`,`t3`.`DSReason` AS `dsreason`,`t3`.`DSNotes` AS `dsnotes`,`t3`.`LCDate` AS `lcdate`,`t3`.`LCBy` AS `lcby`,`t3`.`LCStatus` AS `lcstatus`,`t3`.`LCReason` AS `lcreason`,`t3`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udssymptomsonset` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udstracking` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS Symptoms Onset');

-- -----------------------------------------------------
-- View `lq_view_udsupdrs`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_udsupdrs` ;
DROP TABLE IF EXISTS `lq_view_udsupdrs`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `lq_view_udsupdrs` AS select `t1`.`InstrID` AS `instrid`,`t3`.`PDNORMAL` AS `pdnormal`,`t3`.`SPEECH` AS `speech`,`t3`.`SPEECHX` AS `speechx`,`t3`.`FACEXP` AS `facexp`,`t3`.`FACEXPX` AS `facexpx`,`t3`.`TRESTFAC` AS `trestfac`,`t3`.`TRESTFAX` AS `trestfax`,`t3`.`TRESTRHD` AS `trestrhd`,`t3`.`TRESTRHX` AS `trestrhx`,`t3`.`TRESTLHD` AS `trestlhd`,`t3`.`TRESTLHX` AS `trestlhx`,`t3`.`TRESTRFT` AS `trestrft`,`t3`.`TRESTRFX` AS `trestrfx`,`t3`.`TRESTLFT` AS `trestlft`,`t3`.`TRESTLFX` AS `trestlfx`,`t3`.`TRACTRHD` AS `tractrhd`,`t3`.`TRACTRHX` AS `tractrhx`,`t3`.`TRACTLHD` AS `tractlhd`,`t3`.`TRACTLHX` AS `tractlhx`,`t3`.`RIGDNECK` AS `rigdneck`,`t3`.`RIGDNEX` AS `rigdnex`,`t3`.`RIGDUPRT` AS `rigduprt`,`t3`.`RIGDUPRX` AS `rigduprx`,`t3`.`RIGDUPLF` AS `rigduplf`,`t3`.`RIGDUPLX` AS `rigduplx`,`t3`.`RIGDLORT` AS `rigdlort`,`t3`.`RIGDLORX` AS `rigdlorx`,`t3`.`RIGDLOLF` AS `rigdlolf`,`t3`.`RIGDLOLX` AS `rigdlolx`,`t3`.`TAPSRT` AS `tapsrt`,`t3`.`TAPSRTX` AS `tapsrtx`,`t3`.`TAPSLF` AS `tapslf`,`t3`.`TAPSLFX` AS `tapslfx`,`t3`.`HANDMOVR` AS `handmovr`,`t3`.`HANDMVRX` AS `handmvrx`,`t3`.`HANDMOVL` AS `handmovl`,`t3`.`HANDMVLX` AS `handmvlx`,`t3`.`HANDALTR` AS `handaltr`,`t3`.`HANDATRX` AS `handatrx`,`t3`.`HANDALTL` AS `handaltl`,`t3`.`HANDATLX` AS `handatlx`,`t3`.`LEGRT` AS `legrt`,`t3`.`LEGRTX` AS `legrtx`,`t3`.`LEGLF` AS `leglf`,`t3`.`LEGLFX` AS `leglfx`,`t3`.`ARISING` AS `arising`,`t3`.`ARISINGX` AS `arisingx`,`t3`.`POSTURE` AS `posture`,`t3`.`POSTUREX` AS `posturex`,`t3`.`GAIT` AS `gait`,`t3`.`GAITX` AS `gaitx`,`t3`.`POSSTAB` AS `posstab`,`t3`.`POSSTABX` AS `posstabx`,`t3`.`BRADYKIN` AS `bradykin`,`t3`.`BRADYKIX` AS `bradykix`,`t2`.`Packet` AS `packet`,`t2`.`FormID` AS `formid`,`t2`.`FormVer` AS `formver`,`t2`.`ADCID` AS `adcid`,`t2`.`PTID` AS `ptid`,`t2`.`VisitMo` AS `visitmo`,`t2`.`VisitDay` AS `visitday`,`t2`.`VisitYr` AS `visityr`,`t2`.`VisitNum` AS `visitnum`,`t2`.`Initials` AS `initials`,`t2`.`PacketDate` AS `packetdate`,`t2`.`PacketBy` AS `packetby`,`t2`.`PacketStatus` AS `packetstatus`,`t2`.`PacketReason` AS `packetreason`,`t2`.`PacketNotes` AS `packetnotes`,`t2`.`DSDate` AS `dsdate`,`t2`.`DSBy` AS `dsby`,`t2`.`DSStatus` AS `dsstatus`,`t2`.`DSReason` AS `dsreason`,`t2`.`DSNotes` AS `dsnotes`,`t2`.`LCDate` AS `lcdate`,`t2`.`LCBy` AS `lcby`,`t2`.`LCStatus` AS `lcstatus`,`t2`.`LCReason` AS `lcreason`,`t2`.`LCNotes` AS `lcnotes` from ((`instrumenttracking` `t1` join `udstracking` `t2` on((`t1`.`InstrID` = `t2`.`InstrID`))) join `udsupdrs` `t3` on((`t1`.`InstrID` = `t3`.`InstrID`))) where (`t1`.`InstrType` = _latin1'UDS UPDRS');


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

