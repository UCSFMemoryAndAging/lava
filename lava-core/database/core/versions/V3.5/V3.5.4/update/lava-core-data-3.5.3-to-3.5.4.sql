UPDATE viewproperty SET label = 'Records with warnings (same record could also have errors or already exist)'  WHERE messageCode = '*.importLog.warnings';

UPDATE viewproperty SET scope = 'core' WHERE messageCode = '*.importLog.activeQuickFilter';

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) 
VALUES ('*.importDefinition.created','en','lava','core', NULL,'importDefinition','created',NULL,'c','string',NULL,NULL,NULL,'Created','Import Definition Creation Date',NULL,NULL,NULL,'2016-05-30 22:09:25');

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) 
VALUES ('*.importDefinition.createdBy','en','lava','core',NULL,'importDefinition','createdBy',NULL,'c','string',NULL,NULL,NULL,'Created By','Import Definition Created By',NULL,NULL,NULL,'2016-05-30 22:09:25');
  
DELETE FROM versionhistory WHERE module='lava-core-data' AND version='3.5.4';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-data','3.5.4','2016-06-06',3,5,4,0);


