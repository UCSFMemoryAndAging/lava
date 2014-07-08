DROP TABLE IF EXISTS `crms_import_definition`;

CREATE TABLE `crms_import_definition` (
  `definition_id` int NOT NULL,
  `patient_exist_rule` smallint NULL,
  `patient_update` boolean NULL,
  `es_exist_rule` smallint NULL,
  `es_update` boolean NULL,
  `es_status_supplied` boolean NULL,
  `es_status` varchar(25) NULL,
  `visit_exist_rule` smallint NULL,
  `visit_update` boolean NULL,
  `visit_type` varchar(25) NULL,
  `visit_loc` varchar(25) NULL,
  `visit_with` varchar(25) NULL,
  `visit_status` varchar(25) NULL,
  `instr_exist_rule` smallint NULL,
  `instr_update` boolean NULL,
  `instr_type` varchar(25) NULL,
  `instr_ver` varchar(25) NULL,
  `instr_dc_status` varchar(25) NULL,
  PRIMARY KEY (`definition_id`),
  CONSTRAINT `crms_import_definition__definition_id` FOREIGN KEY (`definition_id`) REFERENCES `import_definition` (`definition_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `crms_import_log`;

CREATE TABLE `crms_import_log` (
  `log_id` int NOT NULL,
  `ProjName` varchar(75) NOT NULL,
  PRIMARY KEY (`log_id`),
  CONSTRAINT `crms_import_log__log_id` FOREIGN KEY (`log_id`) REFERENCES `import_log` (`log_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- WAIT ON THIS UNTIL READY FOR RELEASE
-- insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
-- 	VALUES ('lava-crms-model','3.5.0',NOW(),3,5,0,0);
