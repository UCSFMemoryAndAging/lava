ALTER TABLE `import_definition` MODIFY `name` VARCHAR(50);
ALTER TABLE `import_definition` MODIFY `category` VARCHAR(50);

-- add truncation to import_definition as a flag for how to handle truncation. either throw an error or truncate to the metadata length
ALTER TABLE import_definition ADD COLUMN truncate SMALLINT NULL DEFAULT NULL AFTER time_format;

-- import_log_message message needs to be much larger than 255 to accomodate exception msgs, cause, stack trace
ALTER TABLE `import_log_message` MODIFY `message` VARCHAR(5000);

DELETE FROM versionhistory WHERE module='lava-core-model' AND version='3.5.1';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-model','3.5.1','2014-11-05',3,5,1,0);


