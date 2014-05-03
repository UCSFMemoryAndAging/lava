DROP TABLE IF EXISTS `import_definition`;

CREATE TABLE `import_definition` (
  `definition_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `category` varchar(30) DEFAULT NULL,
  `mapping_file_id` int DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`definition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `import_log`;

CREATE TABLE `import_log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `filename` varchar(100) NOT NULL,
  `definition_name` varchar(30) NULL,
  `import_timestamp` datetime NOT NULL,
  `imported_by` varchar(25) NOT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- WAIT ON THIS UNTIL TIME TO RELEASE
-- insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
-- 	VALUES ('lava-core-model','3.5.0',NOW(),3,5,0,0);


