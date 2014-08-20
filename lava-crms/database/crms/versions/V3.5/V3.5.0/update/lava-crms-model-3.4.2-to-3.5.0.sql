ALTER TABLE `crms_file` ADD COLUMN `consent_id` INT DEFAULT NULL AFTER `enroll_stat_id`;

ALTER TABLE `crms_file` ADD CONSTRAINT `fk_crms_file__consent_id`
 FOREIGN KEY (`consent_id` )
 REFERENCES `patientconsent` (`ConsentID`)
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `fk_crms_file__consent_id` (`consent_id` ASC);

DROP TABLE IF EXISTS `crms_import_definition`;

CREATE TABLE `crms_import_definition` (
  `definition_id` int NOT NULL,
  `ProjName` varchar(75) NOT NULL,
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
  `new_patients` int NOT NULL,
  `existing_patients` int NOT NULL,
  `new_contact_info` int NOT NULL,
  `existing_contact_info` int NOT NULL,
  `new_caregivers` int NOT NULL,
  `existing_caregivers` int NOT NULL,
  `new_caregivers_contact_info` int NOT NULL,
  `existing_caregivers_contact_info` int NOT NULL,
  `new_enroll_statuses` int NOT NULL,
  `existing_enroll_statuses` int NOT NULL,
  `new_visits` int NOT NULL,
  `existing_visits` int NOT NULL,
  `new_instrs` int NOT NULL,
  `existing_instrs` int NOT NULL,
  `existing_instrs_w_data` int NOT NULL,
  PRIMARY KEY (`log_id`),
  CONSTRAINT `crms_import_log__log_id` FOREIGN KEY (`log_id`) REFERENCES `import_log` (`log_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DELETE FROM versionhistory WHERE module='lava-crms-model' AND version='3.5.0';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-model','3.5.0',NOW(),3,5,0,0);

