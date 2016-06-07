ALTER TABLE `import_definition`
  ADD COLUMN `created` DATETIME NULL DEFAULT NULL AFTER `truncate`,
  ADD COLUMN `created_by` VARCHAR(25) NULL DEFAULT NULL AFTER `created`;

DELETE FROM versionhistory WHERE module='lava-core-model' AND version='3.5.4';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-model','3.5.4','2016-06-06',3,5,4,0);





