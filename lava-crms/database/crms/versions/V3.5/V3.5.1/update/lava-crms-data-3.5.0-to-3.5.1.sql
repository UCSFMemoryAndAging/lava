INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) 
VALUES('*.importDefinition.patientOnlyImport','en','lava','crms',NULL,'importDefinition','patientOnlyImport',NULL,'i','toggle',NULL,'Only Import Patients',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Only Import Patients, not Visits or Instruments','2014-04-28 16:33:13');

DELETE FROM versionhistory WHERE module='lava-crms-data' AND version='3.5.1';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-data','3.5.1','2014-11-05',3,5,1,0);

