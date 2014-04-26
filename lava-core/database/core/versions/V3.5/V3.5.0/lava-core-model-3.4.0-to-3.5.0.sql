delimiter $$

CREATE TABLE `import_template` (
  `template_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `category` varchar(30) NOT NULL,
  `mapping_file_id` int DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

CREATE TABLE `import_log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `filename` varchar(100) NOT NULL,
  `template_name` varchar(30) NULL,
  `import_timestamp` timestamp NOT NULL;
  `imported_by` varchar(25)) NOT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$


insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-core-model','3.5.0',NOW(),3,5,0,0);

delimiter $$

