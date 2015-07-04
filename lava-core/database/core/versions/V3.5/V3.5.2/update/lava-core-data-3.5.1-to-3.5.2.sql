UPDATE viewproperty SET context='i',style='text', attributes='rows="5" cols="35"' WHERE messageCode = '*.importLog.notes';

DELETE FROM versionhistory WHERE module='lava-core-data' AND version='3.5.2';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-data','3.5.2','2015-07-03',3,5,2,0);


