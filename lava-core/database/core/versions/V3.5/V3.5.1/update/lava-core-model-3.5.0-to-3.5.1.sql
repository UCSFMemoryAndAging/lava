ALTER TABLE `import_definition` MODIFY `name` VARCHAR(50);
ALTER TABLE `import_definition` MODIFY `category` VARCHAR(50);

DELETE FROM versionhistory WHERE module='lava-core-model' AND version='3.5.1';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-model','3.5.1','2014-10-29',3,5,1,0);


