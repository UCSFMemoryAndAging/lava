-- NOTE: alternatively to this script, can just use the lava-app-pedi 
-- database/local/crms/development/data/extract-crms-data.sql extract script to generate 
-- lava-crms-data-VERSION.sql for transferring all metadata changes. easier as opposed to 
-- adding individual changes to a script like this, but then again a script like this makes it
-- clear what metadata was added, changed, deleted

UPDATE viewproperty SET label='New instrument (bundles) Created',quickHelp='New instruments (bundles) Created' WHERE messageCode = '*.importLog.newInstruments';
UPDATE viewproperty SET label='Already Existing Instrument (bundles) with no data',quickHelp='Already Existing Instrument (bundles) with no data' WHERE messageCode = '*.importLog.existingInstruments';
UPDATE viewproperty SET label='Already Existing Instrument (bundles) With Data (data not imported)',quickHelp='Already Existing Instrument (bundles) With Data (data not imported)' WHERE messageCode = '*.importLog.existingInstrumentsWithData';

-- import properties metadata
-- DELETE from `viewproperty` where `instance`='lava' and `scope`='crms' and entity = 'importDefinition' and property like instrAlias%;
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias','en','lava','crms',NULL,'importDefinition','instrMappingAlias',NULL,'i','string',NULL,'Alias',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Alias for Instrument Mapping','2016-04-25 22:09:25');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias2','en','lava','crms',NULL,'importDefinition','instrMappingAlias2',NULL,'i','string',NULL,'Alias',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Alias for Instrument 2 Mapping','2016-04-25 22:09:25');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias3','en','lava','crms',NULL,'importDefinition','instrMappingAlias3',NULL,'i','string',NULL,'Alias',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Alias for Instrument 3 Mapping','2016-04-25 22:09:25');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias4','en','lava','crms',NULL,'importDefinition','instrMappingAlias4',NULL,'i','string',NULL,'Alias',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Alias for Instrument 4 Mapping','2016-04-25 22:09:25');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias5','en','lava','crms',NULL,'importDefinition','instrMappingAlias5',NULL,'i','string',NULL,'Alias',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Alias for Instrument 5 Mapping','2016-04-25 22:09:25');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias6','en','lava','crms',NULL,'importDefinition','instrMappingAlias6',NULL,'i','string',NULL,'Alias',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Alias for Instrument 6 Mapping','2016-04-25 22:09:25');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias7','en','lava','crms',NULL,'importDefinition','instrMappingAlias7',NULL,'i','string',NULL,'Alias',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Alias for Instrument 7 Mapping','2016-04-25 22:09:25');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias8','en','lava','crms',NULL,'importDefinition','instrMappingAlias8',NULL,'i','string',NULL,'Alias',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Alias for Instrument 8 Mapping','2016-04-25 22:09:25');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias9','en','lava','crms',NULL,'importDefinition','instrMappingAlias9',NULL,'i','string',NULL,'Alias',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Alias for Instrument 9 Mapping','2016-04-25 22:09:25');
INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias10','en','lava','crms',NULL,'importDefinition','instrMappingAlias10',NULL,'i','string',NULL,'Alias',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'Alias for Instrument 10 Mapping','2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`maxLength`,`size`,`attributes`,`list`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias11','en','lava','crms',NULL,'importDefinition','instrMappingAlias11',NULL,'i','string',NULL,'Alias',25,NULL,NULL,NULL,NULL,'Alias for Instrument 11 Mapping','2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`maxLength`,`size`,`attributes`,`list`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias12','en','lava','crms',NULL,'importDefinition','instrMappingAlias12',NULL,'i','string',NULL,'Alias',25,NULL,NULL,NULL,NULL,'Alias for Instrument 12 Mapping','2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`maxLength`,`size`,`attributes`,`list`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias13','en','lava','crms',NULL,'importDefinition','instrMappingAlias13',NULL,'i','string',NULL,'Alias',25,NULL,NULL,NULL,NULL,'Alias for Instrument 13 Mapping','2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`maxLength`,`size`,`attributes`,`list`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias14','en','lava','crms',NULL,'importDefinition','instrMappingAlias14',NULL,'i','string',NULL,'Alias',25,NULL,NULL,NULL,NULL,'Alias for Instrument 14 Mapping','2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`maxLength`,`size`,`attributes`,`list`,`propOrder`,`quickHelp`,`modified`) VALUES('*.importDefinition.instrMappingAlias15','en','lava','crms',NULL,'importDefinition','instrMappingAlias15',NULL,'i','string',NULL,'Alias',25,NULL,NULL,NULL,NULL,'Alias for Instrument 15 Mapping','2016-04-25 22:09:25');

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate','en','lava','crms',NULL,'importDefinition','instrCalculate',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate2','en','lava','crms',NULL,'importDefinition','instrCalculate2',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate3','en','lava','crms',NULL,'importDefinition','instrCalculate3',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate4','en','lava','crms',NULL,'importDefinition','instrCalculate4',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate5','en','lava','crms',NULL,'importDefinition','instrCalculate5',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate6','en','lava','crms',NULL,'importDefinition','instrCalculate6',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate7','en','lava','crms',NULL,'importDefinition','instrCalculate7',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate8','en','lava','crms',NULL,'importDefinition','instrCalculate8',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate9','en','lava','crms',NULL,'importDefinition','instrCalculate9',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate10','en','lava','crms',NULL,'importDefinition','instrCalculate10',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate11','en','lava','crms',NULL,'importDefinition','instrCalculate11',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate12','en','lava','crms',NULL,'importDefinition','instrCalculate12',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate13','en','lava','crms',NULL,'importDefinition','instrCalculate13',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate14','en','lava','crms',NULL,'importDefinition','instrCalculate14',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCalculate15','en','lava','crms',NULL,'importDefinition','instrCalculate15',NULL,'i','toggle',NULL,NULL,NULL,'Run calculate on imported data?','Run calculations on each record after the data is imported?',NULL,NULL,NULL,'2016-04-25 22:09:25');

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrType11','en','lava','crms',NULL,'importDefinition','instrType11',NULL,'i','range','instrumentMetadata.instrTypes',NULL,'Yes','Instrument 11','Instrument 11 for imported data',NULL,NULL,19,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrVer11','en','lava','crms',NULL,'importDefinition','instrVer11',NULL,'i','string',NULL,NULL,NULL,'Instrument 11 Version','Version for New Instrument 11 Records',NULL,NULL,8,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCaregiver11','en','lava','crms',NULL,'importDefinition','instrCaregiver11',NULL,'i','toggle',NULL,NULL,NULL,'Caregiver instrument?','Is this instrument collected from a caregiver?',NULL,NULL,NULL,'2016-04-25 22:09:25');

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrType12','en','lava','crms',NULL,'importDefinition','instrType12',NULL,'i','range','instrumentMetadata.instrTypes',NULL,'Yes','Instrument 12','Instrument 12 for imported data',NULL,NULL,19,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrVer12','en','lava','crms',NULL,'importDefinition','instrVer12',NULL,'i','string',NULL,NULL,NULL,'Instrument 12 Version','Version for New Instrument 12 Records',NULL,NULL,8,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCaregiver12','en','lava','crms',NULL,'importDefinition','instrCaregiver12',NULL,'i','toggle',NULL,NULL,NULL,'Caregiver instrument?','Is this instrument collected from a caregiver?',NULL,NULL,NULL,'2016-04-25 22:09:25');

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrType13','en','lava','crms',NULL,'importDefinition','instrType13',NULL,'i','range','instrumentMetadata.instrTypes',NULL,'Yes','Instrument 13','Instrument 13 for imported data',NULL,NULL,19,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrVer13','en','lava','crms',NULL,'importDefinition','instrVer13',NULL,'i','string',NULL,NULL,NULL,'Instrument 13 Version','Version for New Instrument 13 Records',NULL,NULL,8,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCaregiver13','en','lava','crms',NULL,'importDefinition','instrCaregiver13',NULL,'i','toggle',NULL,NULL,NULL,'Caregiver instrument?','Is this instrument collected from a caregiver?',NULL,NULL,NULL,'2016-04-25 22:09:25');

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrType14','en','lava','crms',NULL,'importDefinition','instrType14',NULL,'i','range','instrumentMetadata.instrTypes',NULL,'Yes','Instrument 14','Instrument 14 for imported data',NULL,NULL,19,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrVer14','en','lava','crms',NULL,'importDefinition','instrVer14',NULL,'i','string',NULL,NULL,NULL,'Instrument 14 Version','Version for New Instrument 14 Records',NULL,NULL,8,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCaregiver14','en','lava','crms',NULL,'importDefinition','instrCaregiver14',NULL,'i','toggle',NULL,NULL,NULL,'Caregiver instrument?','Is this instrument collected from a caregiver?',NULL,NULL,NULL,'2016-04-25 22:09:25');

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrType15','en','lava','crms',NULL,'importDefinition','instrType15',NULL,'i','range','instrumentMetadata.instrTypes',NULL,'Yes','Instrument 15','Instrument 15 for imported data',NULL,NULL,19,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrVer15','en','lava','crms',NULL,'importDefinition','instrVer15',NULL,'i','string',NULL,NULL,NULL,'Instrument 15 Version','Version for New Instrument 15 Records',NULL,NULL,8,'2016-04-25 22:09:25');
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.instrCaregiver15','en','lava','crms',NULL,'importDefinition','instrCaregiver15',NULL,'i','toggle',NULL,NULL,NULL,'Caregiver instrument?','Is this instrument collected from a caregiver?',NULL,NULL,NULL,'2016-04-25 22:09:25');

UPDATE viewproperty SET messageCode = '*.importDefinition.matchVisitTypeFlag', property='matchVisitTypeFlag' WHERE messageCode = '*.importDefinition.matchVisitType';

INSERT INTO `viewproperty` (`messageCode`,`locale`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`) VALUES ('*.importDefinition.matchVisitType','en','lava',NULL,'importDefinition','matchVisitType',NULL,'i','range','visit.visitTypes',NULL,'No','Match Visit Type','Match Visit Type #1',NULL,NULL,30);
INSERT INTO `viewproperty` (`messageCode`,`locale`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`) VALUES ('*.importDefinition.matchVisitType2','en','lava',NULL,'importDefinition','matchVisitType2',NULL,'i','range','visit.visitTypes',NULL,'No','Match Visit Type','Match Visit Type #2',NULL,NULL,30);
INSERT INTO `viewproperty` (`messageCode`,`locale`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`) VALUES ('*.importDefinition.matchVisitType3','en','lava',NULL,'importDefinition','matchVisitType3',NULL,'i','range','visit.visitTypes',NULL,'No','Match Visit Type','Match Visit Type #3',NULL,NULL,30);

INSERT INTO `viewproperty` (`messageCode`,`locale`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`) 
VALUES ('*.importDefinition.instrDefaultCode','en','lava',NULL,'importDefinition','instrDefaultCode',NULL,'i','range','reference.stdErrorCodes',NULL,'No','Global Default','Global default for missing (i.e. blank) values',NULL,NULL,19);



DELETE FROM versionhistory WHERE module='lava-crms-data' AND version='3.5.6';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-data','3.5.6','2016-06-06',3,5,6,0);

