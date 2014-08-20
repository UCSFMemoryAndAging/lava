-- *********************************************************************
-- Add columns for family relationships
-- *********************************************************************

ALTER TABLE `patient` ADD COLUMN `FamilyID` INT NULL DEFAULT NULL AFTER `InactiveDate`;
ALTER TABLE `patient` ADD COLUMN `FamilyStatus` VARCHAR(25) NULL DEFAULT NULL AFTER `FamilyID`;
ALTER TABLE `patient` ADD COLUMN `FamilyStudy` VARCHAR(50) NULL DEFAULT NULL AFTER `FamilyStatus`;
ALTER TABLE `patient` ADD COLUMN `RelationToProband` VARCHAR(25) NULL DEFAULT NULL AFTER `FamilyStudy`;
ALTER TABLE `patient` ADD COLUMN `ProbandCandidate` BOOLEAN NULL DEFAULT NULL AFTER `RelationToProband`;
ALTER TABLE `patient` ADD COLUMN `Twin` SMALLINT NULL DEFAULT NULL AFTER `ProbandCandidate`;
ALTER TABLE `patient` ADD COLUMN `TwinZygosity` SMALLINT NULL DEFAULT NULL AFTER `Twin`;
ALTER TABLE `patient` ADD COLUMN `TwinID` SMALLINT NULL DEFAULT NULL AFTER `TwinZygosity`;
ALTER TABLE `patient` ADD COLUMN `RelationNotes` VARCHAR(100) NULL DEFAULT NULL AFTER `TwinID`;

ALTER TABLE `enrollmentstatus` CHANGE COLUMN `SubjectStudyID` `SubjectStudyID` VARCHAR(25) NULL DEFAULT NULL;

-- new fields to support project level patient id string:FamilyID-ProjCode-SiteCode-FamilyRelationCoding
ALTER TABLE `projectunit` ADD COLUMN `ProjCode` VARCHAR(4) NULL DEFAULT NULL AFTER `Project`;
ALTER TABLE `projectunit` ADD COLUMN `UnitCode` VARCHAR(4) NULL DEFAULT NULL AFTER `Unit`;

DELETE FROM versionhistory WHERE module='lava-crms-model' AND version='3.4.1';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-crms-model','3.4.1',NOW(),3,4,1,0);


