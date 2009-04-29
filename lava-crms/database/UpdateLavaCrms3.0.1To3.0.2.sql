
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
) ENGINE=InnoDB AUTO_INCREMENT=42665 DEFAULT CHARSET=latin1;

INSERT INTO visit(VID,PIDN,ProjName,VLocation,VType,VWith,VDate,VTime,VStatus,VNotes,FUMonth,FUYear,FUNote,
WList, WListNote,WListDate,VShortDesc,AgeAtVisit,modified) SELECT VID,PIDN,ProjName,VLocation,VType,VWith,DATE(VDate),TIME(VDate),VStatus,VNotes,FUMonth,FUYear,FUNote,
WList, WListNote,WListDate,VShortDesc,AgeAtVisit,modified from temp_visit;

UPDATE visit set VTime=NULL WHERE VTIME = '00:00:00';

DROP TEMPORARY TABLE temp_visit;


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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;