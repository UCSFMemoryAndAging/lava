DROP TABLE IF EXISTS `import_definition`;

CREATE TABLE `import_definition` (
  `definition_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `category` varchar(30) DEFAULT NULL,
  `mapping_file_id` int DEFAULT NULL,
  `data_file_format` varchar(25) NULL,
  `start_data_row` smallint NULL,
  `date_format` varchar(15) NULL,
  `time_format` varchar(15) NULL,
  `notes` varchar(500) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `import_definition_name_unique` (`name`),
  PRIMARY KEY (`definition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `import_definition_mapping_file`;

CREATE TABLE `import_definition_mapping_file` (
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  CONSTRAINT `import_definition_mapping_file__id` FOREIGN KEY (`id`) REFERENCES `lava_file` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `import_log`;

CREATE TABLE `import_log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `import_timestamp` datetime NOT NULL,
  `imported_by` varchar(25) NOT NULL,
  `data_file_id` int DEFAULT NULL,
  `definition_name` varchar(30) NULL,
  `total_records` int NULL,
  `imported` int NULL,
  `updated` int NULL,
  `already_exist` int NULL,
  `errors` int NULL,
  `warnings` int NULL,
  `notes` varchar(500) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `import_log_message`;

CREATE TABLE `import_log_message` (
  `message_id` int NOT NULL AUTO_INCREMENT,
  `log_id` int NOT NULL,
  `list_index` int NOT NULL,
  `msg_type` varchar(10) NOT NULL,
  `line_num` int NULL,
  `message` varchar(255) NOT NULL,
  PRIMARY KEY (`message_id`),
  KEY `fk_import_log_message__log_id` (`log_id`),
  CONSTRAINT `fk_import_log_message__log_id` FOREIGN KEY (`log_id`) REFERENCES `import_log` (`log_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-model','3.5.0',NOW(),3,5,0,0);


