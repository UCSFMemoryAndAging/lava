-- *********************************************************************
-- Patient metadata for family relationships
-- *********************************************************************

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.familyId';
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.patient.familyId', 'en', 'lava', 'crms', 'Patient', 'familyId', '', 'i', 'range', 'No', 'Family', 0, 'patient.familyIds', 'Family');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.familyStatus';
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.patient.familyStatus', 'en', 'lava', 'crms', 'Patient', 'familyStatus', '', 'i', 'range', 'No', 'Family Status', 0, 'patient.familyStatus', 'Family Status');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.familyStudy';
INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,`propOrder`,`quickHelp`) 
  VALUES('*.patient.familyStudy','en','lava','crms',NULL,'Patient','familyStudy','','i','string','No','Family Study',NULL,50,NULL,0,NULL,NULL,NULL,NULL,'Family Study');
  
DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.relationToProband'; 
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.patient.relationToProband', 'en', 'lava', 'crms', 'Patient', 'relationToProband', '', 'i', 'range', 'No', 'Relation to Proband', 0, 'patient.relationToProband', 'Relation to Proband');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.probandCandidate';  
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.patient.probandCandidate', 'en', 'lava', 'crms', 'Patient', 'probandCandidate', '', 'i', 'range', 'No', 'Proband Candidate', 0, 'generic.yesNoZero', 'Proband Candidate');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.twin'; 
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.patient.twin', 'en', 'lava', 'crms', 'Patient', 'twin', '', 'i', 'range', 'No', 'Twin', 0, 'generic.yesNoZero', 'Is patient a twin?');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.twinZygosity'; 
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.patient.twinZygosity', 'en', 'lava', 'crms', 'Patient', 'twinZygosity', '', 'i', 'range', 'No', 'Twin Zygosity', 1, 'patient.twinZygosity', 'Twin Zygosity');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.twinId'; 
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `maxLength`, `size`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.patient.twinId', 'en', 'lava', 'crms', 'Patient', 'twinId', '', 'i', 'string', 'No', 'Twin ID', 2, 2, 1, NULL, 'Twin ID');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.relationNotes'; 
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `maxLength`, `indentLevel`, `attributes`, `quickHelp`)
  VALUES ('*.patient.relationNotes', 'en', 'lava', 'crms', 'Patient', 'relationNotes', '', 'i', 'text', 'No', 'Relation Notes', 100, 0, 'rows="2" cols="50"', 'Relation Notes');
  
DELETE FROM `listvalues` WHERE `ListID` IN (SELECT `ListID` FROM `list` WHERE `ListName` = 'PatientFamilyStatus');
DELETE FROM `list` WHERE `ListName` = 'PatientFamilyStatus';
INSERT INTO `list` (`ListName`,`instance`,`scope`,`NumericKey`,`modified`) VALUES('PatientFamilyStatus','lava','crms',1,NOW());
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Individual',NULL,1,NOW() FROM `list` where `ListName`='PatientFamilyStatus';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Family',NULL,2,NOW() FROM `list` where `ListName`='PatientFamilyStatus';

DELETE FROM `listvalues` WHERE `ListID` IN (SELECT `ListID` FROM `list` WHERE `ListName` = 'PatientRelationToProband');
DELETE FROM `list` WHERE `ListName` = 'PatientRelationToProband';
INSERT INTO `list` (`ListName`,`instance`,`scope`,`NumericKey`,`modified`) VALUES('PatientRelationToProband','lava','crms',1,NOW());
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Proband',NULL,1,NOW() FROM `list` where `ListName`='PatientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Mother',NULL,2,NOW() FROM `list` where `ListName`='PatientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Father',NULL,3,NOW() FROM `list` where `ListName`='PatientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Sibling 1',NULL,4,NOW() FROM `list` where `ListName`='PatientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Sibling 2',NULL,5,NOW() FROM `list` where `ListName`='PatientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Sibling 3',NULL,6,NOW() FROM `list` where `ListName`='PatientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Sibling 4',NULL,7,NOW() FROM `list` where `ListName`='PatientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Sibling 5',NULL,8,NOW() FROM `list` where `ListName`='PatientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Cousin 1',NULL,9,NOW() FROM `list` where `ListName`='PatientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Cousin 2',NULL,10,NOW() FROM `list` where `ListName`='PatientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Cousin 3',NULL,11,NOW() FROM `list` where `ListName`='PatientRelationToProband';

DELETE FROM `listvalues` WHERE `ListID` IN (SELECT `ListID` FROM `list` WHERE `ListName` = 'PatientTwinZygosity');
DELETE FROM `list` WHERE `ListName` = 'PatientTwinZygosity';
INSERT INTO `list` (`ListName`,`instance`,`scope`,`NumericKey`,`modified`) VALUES('PatientTwinZygosity','lava','crms',1,NOW());
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','1','MZ',1,NOW() FROM `list` where `ListName`='PatientTwinZygosity';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','2','Same-sex DZ',2,NOW() FROM `list` where `ListName`='PatientTwinZygosity';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','3','Opposite-sex DZ',3,NOW() FROM `list` where `ListName`='PatientTwinZygosity';

DELETE FROM versionhistory WHERE module='lava-crms-data' AND version='3.4.1';
INSERT INTO versionhistory(module,version,versiondate,major,minor,fix,updaterequired)
VALUES ('lava-crms-data','3.4.1','2014-06-17',3,4,1,0);

