-- formatting import definition page
UPDATE viewproperty SET size=19 WHERE messageCode LIKE '*.importDefinition.instrType%';
UPDATE viewproperty SET size=8,style='string',attributes = NULL WHERE messageCode LIKE '*.importDefinition.instrVer%';
UPDATE viewproperty SET label='Data Collection Status' WHERE messageCode = '*.importDefinition.instrDcStatus';
UPDATE viewproperty SET label='Location' WHERE messageCode = '*.importDefinition.visitLoc';
UPDATE viewproperty SET label='Visit Window', quickHelp='Match Visit Within +/- Days from Visit Date in Data File', size=5 WHERE messageCode = '*.importDefinition.visitWindow';

DELETE FROM versionhistory WHERE module='lava-crms-data' AND version='3.5.4';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-data','3.5.4','2015-07-03',3,5,4,0);


