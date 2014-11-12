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

-- add patient only flag for importing patient only data files with no visit or instruments
ALTER TABLE crms_import_definition ADD COLUMN patient_only BOOLEAN NULL DEFAULT NULL AFTER patient_exist_rule;

ALTER TABLE `contactinfo` MODIFY `Phone1` VARCHAR(50);
ALTER TABLE `contactinfo` MODIFY `Phone2` VARCHAR(50);
ALTER TABLE `contactinfo` MODIFY `Phone3` VARCHAR(50);

-- add caregiver instrument flag for specific handling needed to set caregiver on the instrument
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver SMALLINT NULL DEFAULT NULL AFTER instr_ver;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver2 SMALLINT NULL DEFAULT NULL AFTER instr_ver2;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver3 SMALLINT NULL DEFAULT NULL AFTER instr_ver3;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver4 SMALLINT NULL DEFAULT NULL AFTER instr_ver4;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver5 SMALLINT NULL DEFAULT NULL AFTER instr_ver5;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver6 SMALLINT NULL DEFAULT NULL AFTER instr_ver6;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver7 SMALLINT NULL DEFAULT NULL AFTER instr_ver7;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver8 SMALLINT NULL DEFAULT NULL AFTER instr_ver8;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver9 SMALLINT NULL DEFAULT NULL AFTER instr_ver9;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver10 SMALLINT NULL DEFAULT NULL AFTER instr_ver10;

DELETE FROM versionhistory WHERE module='lava-crms-model' AND version='3.5.1';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-model','3.5.1','2014-11-05',3,5,1,0);

