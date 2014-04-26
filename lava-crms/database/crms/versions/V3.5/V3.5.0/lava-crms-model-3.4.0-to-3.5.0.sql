delimiter $$

DROP TABLE IF EXISTS `crms_import_template`$$

CREATE TABLE `crms_import_template` (
  `template_id` int NOT NULL,
  `patient_must_exist` boolean NULL,
  `visit_must_exist` boolean NULL,
  `visit_type_supplied` boolean NULL,
  `visit_type` varchar(25) NULL,
  `visit_loc_supplied` boolean NULL,
  `visit_loc` varchar(25) NULL,
  `visit_with_supplied` boolean NULL,
  `visit_with` varchar(25) NULL,
  `visit_status_supplied` boolean NULL,
  `visit_status` varchar(25) NULL,
  PRIMARY KEY (`template_id`),
  CONSTRAINT `crms_import_template__template_id` FOREIGN KEY (`template_id`) REFERENCES `import_template` (`template_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

DROP TABLE IF EXISTS `crms_import_log`$$

CREATE TABLE `crms_import_log` (
  `log_id` int NOT NULL,
  `ProjName` varchar(75) NOT NULL,
  PRIMARY KEY (`log_id`)
  CONSTRAINT `crms_import_log__log_id` FOREIGN KEY (`log_id`) REFERENCES `import_log` (`log_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$


insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-crms-model','3.5.0',NOW(),3,5,0,0);

delimiter $$

