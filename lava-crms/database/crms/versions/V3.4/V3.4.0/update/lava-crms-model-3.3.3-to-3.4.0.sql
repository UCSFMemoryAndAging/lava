-- until the lava-core V3.4.0 release is ready, all developers creating schema changes that will be
-- part of lava-core V3.4.0 should update this script (i.e. the latest version of this script so
-- that you do not overwrite other developers changes)

-- ************************************************************
-- EMORY: start logiccheck changes
-- ************************************************************
CREATE TABLE `crmslogicchecksummary` (
  `id` int(11) NOT NULL,
  `PIDN` int(11) DEFAULT NULL,
  `EnrollStatID` int(11) DEFAULT NULL,
  `VID` int(11) DEFAULT NULL,
  `InstrID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lcsummary_PIDN` (`PIDN`),
  KEY `lcsummary_EnrollStatID` (`EnrollStatID`),
  KEY `lcsummary_VID` (`VID`),
  KEY `lcsummary_InstrID` (`InstrID`),
  KEY `lcsummary_id` (`id`),
  CONSTRAINT `lcsummary_EnrollStatID` FOREIGN KEY (`EnrollStatID`) REFERENCES `enrollmentstatus` (`EnrollStatID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lcsummary_id` FOREIGN KEY (`id`) REFERENCES `logicchecksummary` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lcsummary_InstrID` FOREIGN KEY (`InstrID`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lcsummary_PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lcsummary_VID` FOREIGN KEY (`VID`) REFERENCES `visit` (`VID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `crmslogiccheckissue` (
  `id` int(11) NOT NULL,
  `PIDN` int(11) DEFAULT NULL,
  `EnrollStatID` int(11) DEFAULT NULL,
  `VID` int(11) DEFAULT NULL,
  `InstrID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lcissue_PIDN` (`PIDN`),
  KEY `lcissue_EnrollStatID` (`EnrollStatID`),
  KEY `lcissue_VID` (`VID`),
  KEY `lcissue_InstrID` (`InstrID`),
  KEY `lcissue_id` (`id`),
  CONSTRAINT `lcissue_EnrollStatID` FOREIGN KEY (`EnrollStatID`) REFERENCES `enrollmentstatus` (`EnrollStatID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lcissue_id` FOREIGN KEY (`id`) REFERENCES `logiccheckissue` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lcissue_InstrID` FOREIGN KEY (`InstrID`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lcissue_PIDN` FOREIGN KEY (`PIDN`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lcissue_VID` FOREIGN KEY (`VID`) REFERENCES `visit` (`VID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `instrumentlogiccheck` (
  `CheckDefId` int(11) NOT NULL AUTO_INCREMENT,
  `instrVers` varchar(45) DEFAULT NULL,
  `visitTypes` varchar(45) DEFAULT NULL,
  `checkCodes` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`CheckDefId`),
  KEY `lc_CheckDefID` (`CheckDefId`),
  CONSTRAINT `lc_CheckDefID` FOREIGN KEY (`CheckDefId`) REFERENCES `logiccheck` (`CheckDefId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `instrument_reference` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `InstrType` varchar(25) NOT NULL,
  `InstrVer` varchar(25) NOT NULL,
  `JavaSimpleClass` varchar(255) NOT NULL,
  `ProjectFormID` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ************************************************************
-- EMORY: end logiccheck changes
-- ************************************************************

insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-crms-model','3.4.0','2013-10-24',3,4,0,0);
