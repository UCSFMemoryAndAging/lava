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
-- Populate `query_objects` with crms data elements
-- -----------------------------------------------------
DELETE from query_objects where instance='lava' and scope='crms' and module='query' and section in ('patient','enrollment','scheduling','assessment');

INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','patient','demographics','Demographics',1,0,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','patient','family','Family',1,0,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','enrollment','status','Enrollment Status',1,0,0);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','scheduling','visits','Visits',1,0,0);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','assessment','instruments','Instrument Tracking',1,0,0);
  

DROP PROCEDURE IF EXISTS `lq_check_version`;
DELIMITER $$

-- -----------------------------------------------------
-- procedure lq_check_version
-- -----------------------------------------------------
CREATE PROCEDURE `lq_check_version`(pModule varchar(25), pMajor integer, pMinor integer, pFix integer)
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

END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_check_user_auth`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_check_user_auth
--
-- NOTE: currently not used as lq_authenticate_user encompasses this check. however, if
-- implement an authentication chain where LDAP is attempted if lq_authenticate_user fails,
-- then this will go back into use, so keep around
-- -----------------------------------------------------
CREATE PROCEDURE `lq_check_user_auth`(user_login varchar(50),host_name varchar(25))
BEGIN
DECLARE user_id int;
SELECT `UID` into user_id from `authuser` where `Login` = user_login;
IF(user_id > 0) THEN
  SELECT  1 as user_auth;
ELSE
  SELECT 0 as user_auth;
END IF;

END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS `lq_authenticate_user`;
DELIMITER $$
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
CREATE PROCEDURE `lq_authenticate_user`(user_login varchar(50),user_password varchar(50))
BEGIN 
DECLARE user_id int;

SELECT `UID` into user_id from `authuser` 
where `Login` = user_login 
AND `password` = convert(sha2(concat(user_password,'{',`UID`,'}'),256) USING latin1);

IF(user_id > 0) THEN
 SELECT  1 as auth_user;
ELSE
 SELECT 0 as auth_user;
END IF;

END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_get_objects`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_get_objects
-- -----------------------------------------------------
CREATE PROCEDURE `lq_get_objects`(user_name varchar(50), host_name varchar(25))
BEGIN
SELECT concat(`instance`,'_',`scope`,'_',`module`) as query_source,concat(`section`,'_',`target`) as query_object_name , `short_desc`, `standard`,`primary_link`,`secondary_link` from `query_objects`;
END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_clear_pidns`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_clear_pidns
-- -----------------------------------------------------
CREATE PROCEDURE `lq_clear_pidns`(user_name varchar(50), host_name varchar(25))
BEGIN

DROP TABLE IF EXISTS temp_pidn;
DROP TABLE IF EXISTS temp_pidn1;

END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS `lq_get_all_pidns`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_get_all_pidns
-- -----------------------------------------------------
CREATE PROCEDURE `lq_get_all_pidns`(user_name varchar(50), host_name varchar(25))
BEGIN
SELECT pidn from patient order by pidn;
END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_set_pidns`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_set_pidns
-- -----------------------------------------------------
CREATE PROCEDURE `lq_set_pidns`(user_name varchar(50), host_name varchar(25))
BEGIN

DROP TABLE IF EXISTS temp_pidn1;
DROP TABLE IF EXISTS temp_pidn;

CREATE TEMPORARY TABLE temp_pidn1(
    pidn INTEGER NOT NULL);
END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_set_pidns_row`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_set_pidns_row
-- -----------------------------------------------------
CREATE PROCEDURE `lq_set_pidns_row`(user_name varchar(50), host_name varchar(25),pidn integer)
BEGIN

INSERT INTO `temp_pidn1` (`pidn`) values (pidn);
END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_after_set_pidns`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_after_set_pidns
-- -----------------------------------------------------
CREATE PROCEDURE `lq_after_set_pidns`(user_name varchar(50), host_name varchar(25))
BEGIN
CREATE TEMPORARY TABLE temp_pidn AS SELECT pidn FROM temp_pidn1 GROUP BY pidn;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS `lq_clear_linkdata`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_clear_linkdata
-- -----------------------------------------------------
CREATE PROCEDURE `lq_clear_linkdata`(user_name varchar(50), host_name varchar(25))
BEGIN

DROP TABLE IF EXISTS temp_linkdata;
DROP TABLE IF EXISTS temp_linkdata1;
END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_get_linkdata`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_get_linkdata
-- -----------------------------------------------------
CREATE PROCEDURE `lq_get_linkdata`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
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

END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_set_linkdata`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_set_linkdata
-- -----------------------------------------------------
CREATE PROCEDURE `lq_set_linkdata`(user_name varchar(50), host_name varchar(25))
BEGINDROP TABLE IF EXISTS temp_linkdata1;DROP TABLE IF EXISTS temp_linkdata;CREATE TEMPORARY TABLE temp_linkdata1(    pidn INTEGER NOT NULL,    link_date DATE NOT NULL,    link_id INTEGER NOT NULL,    link_type varchar(25) DEFAULT NULL);END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_set_linkdata_row`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_set_linkdata_row
-- -----------------------------------------------------
CREATE PROCEDURE `lq_set_linkdata_row`(user_name varchar(50), host_name varchar(25),pidn integer,link_date date, link_id integer)
BEGININSERT INTO `temp_linkdata1` (`pidn`,`link_date`,`link_id`) VALUES(pidn,link_date,link_id);END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_after_set_linkdata`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_after_set_linkdata
-- -----------------------------------------------------
CREATE PROCEDURE `lq_after_set_linkdata`(user_name varchar(50), host_name varchar(25),method VARCHAR(25))
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

CREATE TEMPORARY TABLE temp_linkdata (
  PIDN INTEGER NOT NULL,
  link_date DATE NOT NULL,
  link_id INTEGER NOT NULL,
  link_type VARCHAR(50) NOT NULL);

IF method = 'PIDN_DATE' THEN
  
  INSERT INTO temp_linkdata(pidn,link_date,link_id,link_type) 
  SELECT pidn,link_date, min(link_id), link_type FROM temp_linkdata1 GROUP BY pidn,link_date,link_type;
ELSE
  
  INSERT INTO temp_linkdata(pidn,link_date,link_id,link_type) 
  SELECT pidn,link_date, link_id, link_type FROM temp_linkdata1 GROUP BY pidn,link_date,link_id,link_type;
END IF;



ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);


END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_audit_event`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_audit_event
-- -----------------------------------------------------
CREATE PROCEDURE `lq_audit_event`(user_name varchar(50),host_name varchar(25),lq_query_object varchar(100),lq_query_type varchar(25))
BEGIN



INSERT INTO audit_event_work (`audit_user`,`audit_host`,`audit_timestamp`,`action`,`action_event`)

    VALUES(user_name,host_name,NOW(),CONCAT('lava.*.query.',lq_query_object),lq_query_type);


  
END $$
DELIMITER ;



-- -----------------------------------------------------
-- Placeholder table for view `lq_view_demographics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_demographics` (`PIDN_demographics` INT, `DOB` INT, `AGE` INT, `Gender` INT, `Hand` INT, `Deceased` INT, `DOD` INT, `PrimaryLanguage` INT, `TestingLanguage` INT, `TransNeeded` INT, `TransLanguage` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_family
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_family` (`PIDN_Family` INT, `FamilyID` INT, `FamilyStatus` INT, `FamilyStudy` INT, `RelationToProband` INT, `Twin` INT, `TwinZygosity` INT, `TwinID` INT, `RelationNotes` INT);

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




DROP PROCEDURE IF EXISTS `lq_get_patient_demographics`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_get_patient_demographics
-- -----------------------------------------------------
CREATE PROCEDURE `lq_get_patient_demographics`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
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
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS `lq_get_patient_family`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_get_patient_demographics
-- -----------------------------------------------------
CREATE PROCEDURE `lq_get_patient_family`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN

CALL lq_audit_event(user_name,host_name,'crms.patient.family',query_type);
	
IF query_type = 'Simple' THEN
	SELECT p.PIDN, d.* FROM lq_view_family d 
		INNER JOIN temp_pidn p ON (p.PIDN = d.PIDN_Family) 
      ORDER BY p.pidn;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.PIDN, d.* FROM lq_view_family d  
		 RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = d.PIDN_Family) 
      ORDER BY p.pidn;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	 SELECT l.PIDN, l.link_date, l.link_id, d.*  
	 FROM temp_linkdata l INNER JOIN lq_view_family d ON (d.PIDN_Family=l.PIDN);
END IF;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS `lq_get_enrollment_status`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_get_enrollment_status
-- -----------------------------------------------------
CREATE PROCEDURE `lq_get_enrollment_status`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
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
END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_get_scheduling_visits`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_get_scheduling_visits
-- -----------------------------------------------------
CREATE PROCEDURE `lq_get_scheduling_visits`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
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
END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `lq_get_assessment_instruments`;
DELIMITER $$
-- -----------------------------------------------------
-- procedure lq_get_assessment_instruments
-- -----------------------------------------------------
CREATE PROCEDURE `lq_get_assessment_instruments`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
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
END $$
DELIMITER ;




-- -----------------------------------------------------
-- View `lq_view_demographics`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_demographics` ;
DROP TABLE IF EXISTS `lq_view_demographics`;

CREATE VIEW `lq_view_demographics` AS select `patient`.`PIDN` AS `PIDN_demographics`,`patient`.`DOB` AS `DOB`,`patient_age`.`AGE` AS `AGE`,`patient`.`Gender` AS `Gender`,`patient`.`Hand` AS `Hand`,`patient`.`Deceased` AS `Deceased`,`patient_dod`.`DOD` AS `DOD`,`patient`.`PrimaryLanguage` AS `PrimaryLanguage`,`patient`.`TestingLanguage` AS `TestingLanguage`,`patient`.`TransNeeded` AS `TransNeeded`,`patient`.`TransLanguage` AS `TransLanguage` from (`patient` join `patient_age` on((`patient`.`PIDN` = `patient_age`.`PIDN`)) join `patient_dod` on (`patient`.`PIDN` = `patient_dod`.`PIDN`)) where (`patient`.`PIDN` > 0);

-- -----------------------------------------------------
-- View `lq_view_family`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_family` ;
DROP TABLE IF EXISTS `lq_view_family`;

CREATE VIEW `lq_view_family` AS select `patient`.`PIDN` AS `PIDN_Family`,`patient`.`FamilyID` AS `FamilyID`,`patient`.`FamilyStatus` AS `FamilyStatus`,`patient`.`FamilyStudy` AS `FamilyStudy`,`patient`.`RelationToProband` AS `RelationToProband`,`patient`.`Twin` AS `Twin`,`patient`.`TwinZygosity` AS `TwinZygosity`,`patient`.`TwinID` AS `TwinID`,`patient`.`RelationNotes` AS `RelationNotes` from ((`patient` join `patient_age` on((`patient`.`PIDN` = `patient_age`.`PIDN`))) join `patient_dod` on((`patient`.`PIDN` = `patient_dod`.`PIDN`))) where (`patient`.`PIDN` > 0);

-- -----------------------------------------------------
-- View `lq_view_enrollment`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_enrollment` ;
DROP TABLE IF EXISTS `lq_view_enrollment`;

CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lq_view_enrollment` AS select `enrollmentstatus`.`EnrollStatID` AS `EnrollStatID`,`enrollmentstatus`.`PIDN` AS `PIDN_Enrollment`,`enrollmentstatus`.`ProjName` AS `ProjName`,`enrollmentstatus`.`SubjectStudyID` AS `SubjectStudyID`,`enrollmentstatus`.`ReferralSource` AS `ReferralSource`,`enrollmentstatus`.`LatestDesc` AS `LatestDesc`,`enrollmentstatus`.`LatestDate` AS `LatestDate`,`enrollmentstatus`.`LatestNote` AS `LatestNote`,`enrollmentstatus`.`ReferredDesc` AS `ReferredDesc`,`enrollmentstatus`.`ReferredDate` AS `ReferredDate`,`enrollmentstatus`.`ReferredNote` AS `ReferredNote`,`enrollmentstatus`.`DeferredDesc` AS `DeferredDesc`,`enrollmentstatus`.`DeferredDate` AS `DeferredDate`,`enrollmentstatus`.`DeferredNote` AS `DeferredNote`,`enrollmentstatus`.`EligibleDesc` AS `EligibleDesc`,`enrollmentstatus`.`EligibleDate` AS `EligibleDate`,`enrollmentstatus`.`EligibleNote` AS `EligibleNote`,`enrollmentstatus`.`IneligibleDesc` AS `IneligibleDesc`,`enrollmentstatus`.`IneligibleDate` AS `IneligibleDate`,`enrollmentstatus`.`IneligibleNote` AS `IneligibleNote`,`enrollmentstatus`.`DeclinedDesc` AS `DeclinedDesc`,`enrollmentstatus`.`DeclinedDate` AS `DeclinedDate`,`enrollmentstatus`.`DeclinedNote` AS `DeclinedNote`,`enrollmentstatus`.`EnrolledDesc` AS `EnrolledDesc`,`enrollmentstatus`.`EnrolledDate` AS `EnrolledDate`,`enrollmentstatus`.`EnrolledNote` AS `EnrolledNote`,`enrollmentstatus`.`ExcludedDesc` AS `ExcludedDesc`,`enrollmentstatus`.`ExcludedDate` AS `ExcludedDate`,`enrollmentstatus`.`ExcludedNote` AS `ExcludedNote`,`enrollmentstatus`.`WithdrewDesc` AS `WithdrewDesc`,`enrollmentstatus`.`WithdrewDate` AS `WithdrewDate`,`enrollmentstatus`.`WithdrewNote` AS `WithdrewNote`,`enrollmentstatus`.`InactiveDesc` AS `InactiveDesc`,`enrollmentstatus`.`InactiveDate` AS `InactiveDate`,`enrollmentstatus`.`InactiveNote` AS `InactiveNote`,`enrollmentstatus`.`DeceasedDesc` AS `DeceasedDesc`,`enrollmentstatus`.`DeceasedDate` AS `DeceasedDate`,`enrollmentstatus`.`DeceasedNote` AS `DeceasedNote`,`enrollmentstatus`.`AutopsyDesc` AS `AutopsyDesc`,`enrollmentstatus`.`AutopsyDate` AS `AutopsyDate`,`enrollmentstatus`.`AutopsyNote` AS `AutopsyNote`,`enrollmentstatus`.`ClosedDesc` AS `ClosedDesc`,`enrollmentstatus`.`ClosedDate` AS `ClosedDate`,`enrollmentstatus`.`ClosedNote` AS `ClosedNote`,`enrollmentstatus`.`EnrollmentNotes` AS `EnrollmentNotes`,`enrollmentstatus`.`modified` AS `modified` from `enrollmentstatus` where (`enrollmentstatus`.`EnrollStatID` > 0);

-- -----------------------------------------------------
-- View `lq_view_visit`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_visit` ;
DROP TABLE IF EXISTS `lq_view_visit`;

CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lq_view_visit` AS select `visit`.`VID` AS `VID`,`visit`.`PIDN` AS `PIDN_Visit`,`visit`.`ProjName` AS `ProjName`,`visit`.`VLocation` AS `VLocation`,`visit`.`VType` AS `VType`,`visit`.`VWith` AS `VWith`,`visit`.`VDate` AS `VDate`,`visit`.`VTime` AS `VTime`,`visit`.`VStatus` AS `VStatus`,`visit`.`VNotes` AS `VNotes`,`visit`.`FUMonth` AS `FUMonth`,`visit`.`FUYear` AS `FUYear`,`visit`.`FUNote` AS `FUNote`,`visit`.`WList` AS `WList`,`visit`.`WListNote` AS `WListNote`,`visit`.`WListDate` AS `WListDate`,`visit`.`VShortDesc` AS `VShortDesc`,`visit`.`AgeAtVisit` AS `AgeAtVisit`,`visit`.`modified` AS `modified` from `visit` where (`visit`.`VID` > 0);


-- -----------------------------------------------------
-- View `lq_view_instruments`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_instruments` ;
DROP TABLE IF EXISTS `lq_view_instruments`;

CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lq_view_instruments` AS select `i`.`InstrID` AS `InstrID`,`i`.`VID` AS `VID`,`i`.`ProjName` AS `ProjName`,`i`.`PIDN` AS `PIDN_Instrument`,`i`.`InstrType` AS `InstrType`,`i`.`InstrVer` AS `InstrVer`,`i`.`DCDate` AS `DCDate`,`i`.`DCBy` AS `DCBy`,`i`.`DCStatus` AS `DCStatus`,`i`.`DCNotes` AS `DCNotes`,`i`.`ResearchStatus` AS `ResearchStatus`,`i`.`QualityIssue` AS `QualityIssue`,`i`.`QualityIssue2` AS `QualityIssue2`,`i`.`QualityIssue3` AS `QualityIssue3`,`i`.`QualityNotes` AS `QualityNotes`,`i`.`DEDate` AS `DEDate`,`i`.`DEBy` AS `DEBy`,`i`.`DEStatus` AS `DEStatus`,`i`.`DENotes` AS `DENotes`,`i`.`DVDate` AS `DVDate`,`i`.`DVBy` AS `DVBy`,`i`.`DVStatus` AS `DVStatus`,`i`.`DVNotes` AS `DVNotes`,`i`.`latestflag` AS `latestflag`,`i`.`FieldStatus` AS `FieldStatus`,`i`.`AgeAtDC` AS `AgeAtDC`,`i`.`modified` AS `modified`,`s`.`Summary` AS `summary` from (`instrumenttracking` `i` join `instrumentsummary` `s` on((`i`.`InstrID` = `s`.`InstrID`))) where (`i`.`InstrID` > 0);


