-- somehow duplicate importDefinition.visitWindow records in viewproperty, except for the list, so remove the one with the list that does not exist
DELETE FROM viewproperty WHERE list = 'importDefinition.visitMatchRange';

-- add new imoprt definition fields
ALTER TABLE crms_import_definition ADD COLUMN match_visit_proj_flag BOOLEAN NULL DEFAULT NULL AFTER `visit_window`;
ALTER TABLE crms_import_definition ADD COLUMN match_visit_time_flag BOOLEAN NULL DEFAULT NULL AFTER `match_visit_proj_flag`;

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) 
VALUES ('*.importDefinition.matchVisitProjFlag','en','lava','crms',NULL,'importDefinition','matchVisitProjFlag',NULL,'i','toggle',NULL,'Match on Visit Project',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Match Existing Visit on Project','2018-03-21');

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) 
VALUES ('*.importDefinition.matchVisitTimeFlag','en','lava','crms',NULL,'importDefinition','matchVisitTimeFlag',NULL,'i','toggle',NULL,'Match on Visit Date AND Time',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Match Existing Visit on Time in addition to Date','2018-03-21');

DELETE FROM versionhistory WHERE module='lava-crms-model' AND version='3.5.9';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-model','3.5.9','2018-03-28',3,5,9,0);

DELETE FROM versionhistory WHERE module='lava-crms-data' AND version='3.5.9';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-data','3.5.9','2018-03-28',3,5,9,0);

