-- ------------------------------------------------------------------
-- modified visit and contact log tables to separate time from date 
-- fields.  Added properties to the authuser table to support 
-- password management. 
-- ------------------------------------------------------------------

insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-crms-model','3.0.2',NOW(),3,0,2,1);
	

delete from viewproperty where instance in ('nam53','examiner','mac') and scope = 'crms';
delete from viewproperty where instance ='ucd' and scope = 'crms' and entity = 'patient' and property IN ('hand','ucid');

delete from hibernateproperty where instance in ('clinic','site') and scope = 'crms';

CREATE TEMPORARY TABLE temp_visit AS SELECT * from visit;

DROP TABLE visit;

CREATE TABLE  `visit` (
  `VID` int(10) NOT NULL auto_increment,
  `PIDN` int(10) NOT NULL,
  `ProjName` varchar(75) NOT NULL,
  `VLocation` varchar(25) NOT NULL,
  `VType` varchar(25) NOT NULL,
  `VWith` varchar(25) default NULL,
  `VDate` date NOT NULL,
  `VTime` time NULL DEFAULT NULL,
  `VStatus` varchar(25) NOT NULL,
  `VNotes` varchar(255) default NULL,
  `FUMonth` char(3) default NULL,
  `FUYear` char(4) default NULL,
  `FUNote` varchar(100) default NULL,
  `WList` varchar(25) default NULL,
  `WListNote` varchar(100) default NULL,
  `WListDate` datetime default NULL,
  `VShortDesc` varchar(255) default NULL,
  `AgeAtVisit` smallint(5) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`VID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO visit(VID,PIDN,ProjName,VLocation,VType,VWith,VDate,VTime,VStatus,VNotes,FUMonth,FUYear,FUNote,
WList, WListNote,WListDate,VShortDesc,AgeAtVisit,modified) SELECT VID,PIDN,ProjName,VLocation,VType,VWith,DATE(VDate),TIME(VDate),VStatus,VNotes,FUMonth,FUYear,FUNote,
WList, WListNote,WListDate,VShortDesc,AgeAtVisit,modified from temp_visit;

UPDATE visit set VTime=NULL WHERE VTIME = '00:00:00';



DROP TEMPORARY TABLE temp_visit;

CREATE TEMPORARY TABLE temp_contactlog AS SELECT * from contactlog;

DROP TABLE `contactlog`;
CREATE TABLE  `contactlog` (
  `LogID` int(10) NOT NULL auto_increment,
  `PIDN` int(10) NOT NULL,
  `ProjName` varchar(75) default NULL,
  `LogDate` date default NULL,
  `LogTime` time default NULL,
  `Method` varchar(25) NOT NULL default 'Phone',
  `StaffInit` smallint(5) NOT NULL default '1',
  `Staff` varchar(50) default NULL,
  `Contact` varchar(50) default NULL,
  `Note` text default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`LogID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO contactlog(LogID,PIDN,ProjName,LogDate,LogTime,Method,StaffInit,Staff,Contact,Note,modified)
	 SELECT LogID,PIDN,ProjName,DATE(LogDate),TIME(LogDate),Method,StaffInit,Staff,Contact,Note,modified  from temp_contactlog;

DROP TEMPORARY TABLE temp_contactlog;


CREATE TEMPORARY TABLE temp_authuser AS SELECT * from authuser;

DROP TABLE IF EXISTS `authuser`;
CREATE TABLE  `authuser` (
  `UID` int(10) NOT NULL AUTO_INCREMENT,
  `UserName` varchar(50) NOT NULL,
  `Login` varchar(100) DEFAULT NULL,
  `hashedPassword` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(25) DEFAULT NULL,
  `AccessAgreementDate` date DEFAULT NULL,
  `ShortUserName` varchar(50) DEFAULT NULL,
  `ShortUserNameRev` varchar(50) DEFAULT NULL,
  `EffectiveDate` date NOT NULL,
  `ExpirationDate` date DEFAULT NULL,
  `Notes` varchar(255) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`UID`),
  UNIQUE KEY `Unique_UserName` (`UserName`),
  UNIQUE KEY `Unique_Login` (`Login`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO authuser(`UID`,`UserName`,`Login`,`hashedPassword`,`email`,`phone`,`AccessAgreementDate`,
    `ShortUserName`,`ShortUserNameRev`,`EffectiveDate`,`ExpirationDate`,`Notes`,`modified`)
    SELECT `UID`,`UserName`,`Login`,NULL,NULL,NULL,`AccessAgreementDate`,
    `ShortUserName`,`ShortUserNameRev`,`EffectiveDate`,`ExpirationDate`,`Notes`,`modified` from temp_authuser;

DROP TEMPORARY TABLE temp_authuser;
-- -----------------------------------------------------
-- Placeholder table for view `vwrptprojectpatientstatus`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vwrptprojectpatientstatus` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `vwrptprojectvisitlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vwrptprojectvisitlist` (`id` INT);

-- -----------------------------------------------------
-- View `vwrptprojectpatientstatus`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `vwrptprojectpatientstatus` ;
DROP TABLE IF EXISTS `vwrptprojectpatientstatus`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `vwrptprojectpatientstatus` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `StatusDate`,`lps`.`LatestDesc` AS `Status`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`Project` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,_utf8'OVERALL' AS `Unit`,1 AS `UnitOrder` from ((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) union select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `LatestDate`,`lps`.`LatestDesc` AS `LatestDesc`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`ProjUnitDesc` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,`pu`.`Unit` AS `Unit`,2 AS `UnitOrder` from ((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) where (`pu`.`Unit` is not null);

-- -----------------------------------------------------
-- View `vwrptprojectvisitlist`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `vwrptprojectvisitlist` ;
DROP TABLE IF EXISTS `vwrptprojectvisitlist`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `vwrptprojectvisitlist` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`TransLanguage` AS `TransLanguage`,`p`.`Gender` AS `Gender`,`p`.`AGE` AS `AGE`,`v`.`VLocation` AS `VLocation`,`v`.`VType` AS `VType`,`v`.`VWith` AS `VWith`,`v`.`VDate` AS `VDate`,`v`.`VStatus` AS `VStatus`,`v`.`ProjName` AS `ProjName`,`v`.`VNotes` AS `VNotes`,cast(`v`.`VDate` as date) AS `VDateNoTime` from (`patient` `p` join `visit` `v` on((`p`.`PIDN` = `v`.`PIDN`))) where (not((`v`.`VStatus` like _latin1'%CANC%')));

