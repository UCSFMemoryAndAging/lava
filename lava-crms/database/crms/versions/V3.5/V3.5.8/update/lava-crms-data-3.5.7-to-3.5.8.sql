INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) 
VALUES ('filter.instrument.quickFilter','en','lava','crms','filter','instrument','quickFilter',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,'instrument.quickFilter',NULL,NULL,NULL,'2017-01-24');

DELETE FROM versionhistory WHERE module='lava-crms-data' AND version='3.5.8';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-data','3.5.8','2017-01-24',3,5,8,0);
