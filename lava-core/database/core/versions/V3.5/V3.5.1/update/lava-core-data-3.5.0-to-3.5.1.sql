UPDATE viewproperty SET maxLength=50 WHERE entity = 'importDefinition' AND property = 'name';
UPDATE viewproperty SET maxLength=50 WHERE entity = 'importDefinition' AND property = 'category';

INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) 
VALUES('*.importDefinition.truncate','en','lava','crms',NULL,'importDefinition','truncate',NULL,'i','range',NULL,'Allow truncation?',NULL,NULL,NULL,0,NULL,'generic.yesNoZero',NULL,NULL,'Allow truncation if text exceeds max length?', '2014-11-04 16:33:13');

-- activeQuickFilter metadata record is only used so the static list for the activeQuickFilter property is loaded
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) 
VALUES('*.importLog.activeQuickFilter','en','lava','app-pedi','','importLog','activeQuickFilter','','i','range','No','Message Quick Filter',NULL,25,NULL,0,NULL,'importLog.quickFilter',NULL,NULL,'Import Log Messages Quick Filter','2014-11-16 10:02:41'); 

UPDATE viewproperty SET size = 40 WHERE messageCode = '*.importDefinition.name';
UPDATE viewproperty SET size = 40 WHERE messageCode = '*.import.definitionId';

DELETE FROM versionhistory WHERE module='lava-core-data' AND version='3.5.1';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-data','3.5.1','2014-12-03',3,5,1,0);
