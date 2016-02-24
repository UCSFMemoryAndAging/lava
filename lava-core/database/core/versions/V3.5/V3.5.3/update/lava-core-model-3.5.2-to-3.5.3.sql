ALTER TABLE lava_file ADD COLUMN category varchar(25) NULL DEFAULT NULL AFTER `file_type`;

DELETE FROM versionhistory WHERE module='lava-core-model' AND version='3.5.3';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-model','3.5.3','2016-02-22',3,5,3,0);


