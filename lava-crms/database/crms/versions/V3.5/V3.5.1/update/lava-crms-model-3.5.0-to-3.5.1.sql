-- redefine with count columns allowing NULL instead of NOT NULL
DROP TABLE IF EXISTS `crms_import_log`;

CREATE TABLE `crms_import_log` (
  `log_id` int NOT NULL,
  `ProjName` varchar(75) NOT NULL,
  `new_patients` int NULL,
  `existing_patients` int NULL,
  `new_contact_info` int NULL,
  `existing_contact_info` int NULL,
  `new_caregivers` int NULL,
  `existing_caregivers` int NULL,
  `new_caregivers_contact_info` int NULL,
  `existing_caregivers_contact_info` int NULL,
  `new_enroll_statuses` int NULL,
  `existing_enroll_statuses` int NULL,
  `new_visits` int NULL,
  `existing_visits` int NULL,
  `new_instrs` int NULL,
  `existing_instrs` int NULL,
  `existing_instrs_w_data` int NULL,
  PRIMARY KEY (`log_id`),
  CONSTRAINT `crms_import_log__log_id` FOREIGN KEY (`log_id`) REFERENCES `import_log` (`log_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DELETE FROM versionhistory WHERE module='lava-crms-model' AND version='3.5.1';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-model','3.5.1','2014-10-29',3,5,1,0);

