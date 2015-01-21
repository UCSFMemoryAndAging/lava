-- ************************************************************
-- EMORY: start logiccheck changes
-- ************************************************************
CREATE TABLE `logiccheck` (
  `CheckDefId` int(11) NOT NULL AUTO_INCREMENT,
  `codeID` varchar(25) NOT NULL,
  `enabled` tinyint(1) DEFAULT '0',
  `isalert` tinyint(1) NOT NULL,
  `checkDesc` varchar(300) DEFAULT NULL,
  `primaryLogic` varchar(45) NOT NULL,
  `cond1operator` varchar(45) DEFAULT NULL,
  `cond1negate` tinyint(1) DEFAULT '0',
  `entity1classname` varchar(25) DEFAULT NULL,
  `field1name` varchar(100) DEFAULT NULL,
  `field1itemNum` varchar(45) DEFAULT NULL,
  `field1values` varchar(45) DEFAULT NULL,
  `field1values_alt` varchar(45) DEFAULT NULL,
  `cond2operator` varchar(45) DEFAULT NULL,
  `cond2negate` tinyint(1) DEFAULT '0',
  `entity2classname` varchar(25) DEFAULT NULL,
  `field2name` varchar(100) DEFAULT NULL,
  `field2itemNum` varchar(45) DEFAULT NULL,
  `field2values` varchar(45) DEFAULT NULL,
  `field2values_alt` varchar(45) DEFAULT NULL,
  `activeDate` datetime DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `scope` varchar(25) NOT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`CheckDefId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `logiccheckissue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `CheckDefId` int(11) DEFAULT NULL,
  `checkDescDataAppend` varchar(500) DEFAULT NULL,
  `verified` tinyint(1) DEFAULT NULL,
  `verified_modifier_uid` int(11) DEFAULT NULL,
  `verified_modifieddate` timestamp NULL DEFAULT NULL,
  `invalidDef` tinyint(1) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `instrument_logicchecks_confirmed_modifier` (`verified_modifier_uid`),
  KEY `lcissue_CheckDefId` (`CheckDefId`),
  CONSTRAINT `lcissue_CheckDefId` FOREIGN KEY (`CheckDefId`) REFERENCES `logiccheck` (`CheckDefId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lcissue_confirmed_modifier` FOREIGN KEY (`verified_modifier_uid`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `logicchecksummary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `LCDate` datetime DEFAULT NULL,
  `LCBy` varchar(25) DEFAULT NULL,
  `LCStatus` varchar(25) DEFAULT NULL,
  `LCReason` varchar(25) DEFAULT NULL,
  `LCNotes` varchar(100) DEFAULT NULL,
  `modified` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ************************************************************
-- EMORY: end logiccheck changes
-- ************************************************************

insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-core-model','3.4.0','2013-10-24',3,4,0,0);

