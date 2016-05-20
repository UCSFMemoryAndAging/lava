UPDATE viewproperty SET label = 'Records with warnings (same record could also have errors or already exist)'  WHERE messageCode = '*.importLog.warnings';

UPDATE viewproperty SET scope = 'core' WHERE messageCode = '*.importLog.activeQuickFilter';

DELETE FROM versionhistory WHERE module='lava-core-data' AND version='3.5.4';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-data','3.5.4','2016-05-20',3,5,4,0);


