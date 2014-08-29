UPDATE viewproperty SET label = 'Content Type' WHERE entity = 'lavaFile' AND property = 'contentType';
UPDATE viewproperty SET context = 'c' WHERE entity = 'lavaFile' AND property = 'name';
UPDATE viewproperty SET context = 'c', style = 'string' WHERE entity = 'lavaFile' AND property = 'fileStatus';

DELETE from `viewproperty` where `instance`='lava' and `scope`='core' and entity in ('import','importDefinition','importLog');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.import.definitionId','en','lava','core',NULL,'import','definitionId',NULL,'i','range','Yes','Definition',NULL,NULL,NULL,0,NULL,'importDefinition.definitions',NULL,NULL,'Choose definition for import','2014-04-28 15:30:59');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.import.notes','en','lava','core',NULL,'import','notes',NULL,'i','text',NULL,'Notes',NULL,NULL,NULL,0,'rows="5" cols="35"',NULL,NULL,NULL,'Notes','2014-04-28 15:34:10');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.dateFormat','en','lava','core',NULL,'importDefinition','dateFormat',NULL,'i','suggest',NULL,'Date Format',NULL,NULL,NULL,0,NULL,'importDefinition.dateFormat',NULL,NULL,'Format for importing date fields','2014-05-24 22:17:57');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.timeFormat','en','lava','core',NULL,'importDefinition','timeFormat',NULL,'i','suggest',NULL,'Time Format',NULL,NULL,NULL,0,NULL,'importDefinition.timeFormat',NULL,NULL,'Format for importing time fields','2014-05-24 22:21:32');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.startDataRow','en','lava','core',NULL,'importDefinition','startDataRow',NULL,'i','range',NULL,'Starting Row of Data',NULL,NULL,NULL,0,NULL,'importDefinition.startDataRow',NULL,NULL,'Starting Row of Data','2014-05-24 23:14:03');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.dataFileFormat','en','lava','core',NULL,'importDefinition','dataFileFormat',NULL,'i','range',NULL,'Import Data File Format',NULL,NULL,NULL,0,NULL,'importDefinition.dataFileFormat',NULL,NULL,'Import Data File Format','2014-05-24 23:14:03');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.name','en','lava','core',NULL,'importDefinition','name',NULL,'i','string','Yes','Name',NULL,30,NULL,0,NULL,NULL,NULL,NULL,'Import Definition Name','2014-04-28 15:45:39');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.mappingFilename','en','lava','core',NULL,'importDefinition','mappingFilename',NULL,'c','string','Yes','Mapping File',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Mapping Filename','2014-04-28 15:45:39');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.category','en','lava','core',NULL,'importDefinition','category',NULL,'i','range',NULL,'Category',NULL,30,NULL,0,NULL,'importDefinition.categories',NULL,NULL,'Import Definition Category','2014-04-28 15:47:02');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.notes','en','lava','core',NULL,'importDefinition','notes',NULL,'i','text',NULL,'Notes',NULL,NULL,NULL,0,'rows="5" cols="35"',NULL,NULL,NULL,'Import Definition Notes','2014-04-28 15:52:42');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.importTimestamp','en','lava','core',NULL,'importLog','importTimestamp',NULL,'c','timestamp',NULL,'Timestamp',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Date and Time of Import','2014-04-28 15:37:32');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.importedBy','en','lava','core',NULL,'importLog','importedBy',NULL,'c','string',NULL,'Imported By',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'User who imported the data file','2014-04-28 15:39:22');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.dataFile_name','en','lava','core',NULL,'importLog','dataFile_name',NULL,'c','string',NULL,'Data File',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Name of imported data file','2014-04-28 15:40:14');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.definition_name','en','lava','core',NULL,'importLog','definition_name',NULL,'c','string',NULL,'Definition',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Name of definition used in the import','2014-04-28 15:41:13');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.totalRecords','en','lava','core',NULL,'importLog','totalRecords',NULL,'c','numeric',NULL,'Total records in data file',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Total records in data file','2014-07-22 16:46:31');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.imported','en','lava','core',NULL,'importLog','imported',NULL,'c','numeric',NULL,'Records imported',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Number of records imported','2014-07-22 16:47:53');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.updated','en','lava','core',NULL,'importLog','updated',NULL,'c','numeric',NULL,'Records Updated',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Records Updated','2014-07-22 22:30:50');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.alreadyExist','en','lava','core',NULL,'importLog','alreadyExist',NULL,'c','numeric',NULL,'Already existing records (i.e. not imported)',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Number of already existing records (i.e. not imported)','2014-07-22 16:49:50');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.errors','en','lava','core',NULL,'importLog','errors',NULL,'c','numeric',NULL,'Records with errors (i.e. could not be imported)',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Number of records with errors (i.e. could not be imported)','2014-07-22 16:51:24');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.warnings','en','lava','core',NULL,'importLog','warnings',NULL,'c','numeric',NULL,'Records with warnings (record was imported)',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Number of records with warnings (record was imported)','2014-07-22 16:52:36');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.type','en','lava','core',NULL,'importLog','type',NULL,'c','string',NULL,'Type',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Log Message Type','2014-07-23 23:41:53');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.lineNum','en','lava','core',NULL,'importLog','lineNum',NULL,'c','numeric',NULL,'Line Number',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Data File Line Number','2014-07-23 23:44:30');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.message','en','lava','core',NULL,'importLog','message',NULL,'c','string',NULL,'Message',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Log Message','2014-07-23 23:49:33');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.summaryBlock','en','lava','core',NULL,'importLog','summaryBlock',NULL,'c','string',NULL,'Summary',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Import Summary Stars','2014-07-24 01:19:05');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importLog.notes','en','lava','core',NULL,'importLog','notes',NULL,'c','text',NULL,'Notes',NULL,NULL,NULL,0,'rows="5" cols="35"',NULL,NULL,NULL,'Import Notes','2014-04-28 15:42:08');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.lavaFile.notes','en','lava','core',NULL,'lavaFile','notes',NULL,'i','text','No','Notes',NULL,255,NULL,0,'rows="3" cols="85"',NULL,NULL,NULL,'Attachment Notes','2014-07-09 10:13:04');

DELETE FROM versionhistory WHERE module='lava-core-data' AND version='3.5.0';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-data','3.5.0',NOW(),3,5,0,0);
