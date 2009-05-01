

delete from viewproperty where instance in ('nam53','examiner','mac') and scope = 'crms';

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

