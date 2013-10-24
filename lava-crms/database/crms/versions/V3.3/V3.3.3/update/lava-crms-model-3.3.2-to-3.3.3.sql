-- *********************************************************************
-- UCSF: Add `notes` and `legacy_column` fields to datadictionary table
-- *********************************************************************

ALTER TABLE `datadictionary` ADD COLUMN `notes` varchar(500) NULL DEFAULT NULL AFTER `db_default` ;
ALTER TABLE `datadictionary` ADD COLUMN `legacy_column` varchar(50) NULL DEFAULT NULL AFTER `notes` ;

insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-crms-model','3.3.3',NOW(),3,3,3,0);
