UPDATE import_definition SET date_format = 'M/d/yy' WHERE date_format = 'MM/dd/yy';
UPDATE import_definition SET date_format = 'M/d/yyyy' WHERE date_format = 'MM/dd/yyyy';

DELETE FROM versionhistory WHERE module='lava-core-data' AND version='3.5.5';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-data','3.5.5','2018-03-28',3,5,5,0);

