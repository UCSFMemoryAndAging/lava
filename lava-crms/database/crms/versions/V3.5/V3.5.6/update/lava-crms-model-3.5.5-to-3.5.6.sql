ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias varchar(25) NULL DEFAULT NULL AFTER `instr_ver`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias2 varchar(25) NULL DEFAULT NULL AFTER `instr_ver2`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias3 varchar(25) NULL DEFAULT NULL AFTER `instr_ver3`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias4 varchar(25) NULL DEFAULT NULL AFTER `instr_ver4`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias5 varchar(25) NULL DEFAULT NULL AFTER `instr_ver5`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias6 varchar(25) NULL DEFAULT NULL AFTER `instr_ver6`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias7 varchar(25) NULL DEFAULT NULL AFTER `instr_ver7`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias8 varchar(25) NULL DEFAULT NULL AFTER `instr_ver8`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias9 varchar(25) NULL DEFAULT NULL AFTER `instr_ver9`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias10 varchar(25) NULL DEFAULT NULL AFTER `instr_ver10`;

ALTER TABLE crms_import_definition ADD COLUMN instr_calculate BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate2 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver2;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate3 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver3;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate4 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver4;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate5 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver5;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate6 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver6;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate7 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver7;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate8 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver8;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate9 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver9;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate10 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver10;

ALTER TABLE crms_import_definition ADD COLUMN instr_type11 varchar(25) NULL DEFAULT NULL AFTER `instr_calculate10`;
ALTER TABLE crms_import_definition ADD COLUMN instr_ver11 varchar(25) NULL DEFAULT NULL AFTER `instr_type11`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias11 varchar(25) NULL DEFAULT NULL AFTER `instr_ver11`;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver11 BOOLEAN NULL DEFAULT NULL AFTER instr_mapping_alias11;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate11 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver11;

ALTER TABLE crms_import_definition ADD COLUMN instr_type12 varchar(25) NULL DEFAULT NULL AFTER `instr_calculate11`;
ALTER TABLE crms_import_definition ADD COLUMN instr_ver12 varchar(25) NULL DEFAULT NULL AFTER `instr_type12`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias12 varchar(25) NULL DEFAULT NULL AFTER `instr_ver12`;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver12 BOOLEAN NULL DEFAULT NULL AFTER instr_mapping_alias12;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate12 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver12;

ALTER TABLE crms_import_definition ADD COLUMN instr_type13 varchar(25) NULL DEFAULT NULL AFTER `instr_calculate12`;
ALTER TABLE crms_import_definition ADD COLUMN instr_ver13 varchar(25) NULL DEFAULT NULL AFTER `instr_type13`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias13 varchar(25) NULL DEFAULT NULL AFTER `instr_ver13`;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver13 BOOLEAN NULL DEFAULT NULL AFTER instr_mapping_alias13;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate13 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver13;

ALTER TABLE crms_import_definition ADD COLUMN instr_type14 varchar(25) NULL DEFAULT NULL AFTER `instr_calculate13`;
ALTER TABLE crms_import_definition ADD COLUMN instr_ver14 varchar(25) NULL DEFAULT NULL AFTER `instr_type14`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias14 varchar(25) NULL DEFAULT NULL AFTER `instr_ver14`;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver14 BOOLEAN NULL DEFAULT NULL AFTER instr_mapping_alias14;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate14 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver14;

ALTER TABLE crms_import_definition ADD COLUMN instr_type15 varchar(25) NULL DEFAULT NULL AFTER `instr_calculate14`;
ALTER TABLE crms_import_definition ADD COLUMN instr_ver15 varchar(25) NULL DEFAULT NULL AFTER `instr_type15`;
ALTER TABLE crms_import_definition ADD COLUMN instr_mapping_alias15 varchar(25) NULL DEFAULT NULL AFTER `instr_ver15`;
ALTER TABLE crms_import_definition ADD COLUMN instr_caregiver15 BOOLEAN NULL DEFAULT NULL AFTER instr_mapping_alias15;
ALTER TABLE crms_import_definition ADD COLUMN instr_calculate15 BOOLEAN NULL DEFAULT NULL AFTER instr_caregiver15;

  
DROP TABLE IF EXISTS `crms_import_log_message`;

CREATE TABLE `crms_import_log_message` (
  `message_id` int(11) NOT NULL,
  EntityType varchar(25) NULL DEFAULT NULL,
  PIDN int NULL DEFAULT NULL,
  LName varchar(25) NULL DEFAULT NULL,
  FName varchar(25) NULL DEFAULT NULL,
  DOB datetime NULL DEFAULT NULL,
  EnrollStatID int NULL DEFAULT NULL,
  `ProjName` varchar(75) NULL DEFAULT NULL,
  VID int NULL DEFAULT NULL,
  VDate datetime NULL DEFAULT NULL,
  VType varchar(25) NULL DEFAULT NULL,
  InstrID int NULL DEFAULT NULL,
  InstrType varchar(25) NULL DEFAULT NULL,
  InstrVer varchar(25) NULL DEFAULT NULL,
  PRIMARY KEY (`message_id`),
  CONSTRAINT `crms_import_log_message__message_id` FOREIGN KEY (`message_id`) REFERENCES `import_log_message` (`message_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO crms_import_log_message(message_id) SELECT message_id FROM import_log_message;


DELETE FROM versionhistory WHERE module='lava-crms-model' AND version='3.5.6';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-model','3.5.6','2016-06-06',3,5,6,0);


