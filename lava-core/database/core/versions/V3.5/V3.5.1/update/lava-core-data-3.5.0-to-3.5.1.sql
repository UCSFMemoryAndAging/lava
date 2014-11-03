UPDATE viewproperty SET maxLength=50 WHERE entity = 'importDefinition' AND property = 'name';
UPDATE viewproperty SET maxLength=50 WHERE entity = 'importDefinition' AND property = 'category';

DELETE FROM versionhistory WHERE module='lava-core-data' AND version='3.5.1';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-data','3.5.1','2014-10-29',3,5,1,0);
