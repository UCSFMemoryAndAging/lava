delimiter $$

CREATE TABLE `import_template` (
  `import_template_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `mapping_filepath` varchar(500) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`import_template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$



insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-core-model','3.5.0',NOW(),3,5,0,0);

