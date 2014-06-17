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
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `maxLength`, `indentLevel`, `quickHelp`)
  VALUES ('*.patient.familyStudy', 'en', 'lava', 'crms', 'Patient', 'familyStudy', '', 'i', 'string', 'No', 'Family Study', 50, 0, 'Family Study');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.relationToProband'; 
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.patient.relationToProband', 'en', 'lava', 'crms', 'Patient', 'relationToProband', '', 'i', 'range', 'No', 'Relation to Proband', 0, 'patient.relationToProband', 'Relation to Proband');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.probandCandidate';  
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.patient.probandCandidate', 'en', 'lava', 'crms', 'Patient', 'probandCandidate', '', 'i', 'range', 'No', 'Proband Candidate', 0, 'generic.yesNoZero', 'Proband Candidate');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.twinZygosity'; 
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.patient.twinZygosity', 'en', 'lava', 'crms', 'Patient', 'twinZygosity', '', 'i', 'range', 'No', 'Twin Zygosity', 0, 'patient.twinZygosity', 'Twin Zygosity');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.twinId'; 
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `maxLength`, `size`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.patient.twinId', 'en', 'lava', 'crms', 'Patient', 'twinId', '', 'i', 'string', 'No', 'Twin ID', 2, 2, 0, NULL, 'Twin ID');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.relationNotes'; 
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `maxLength`, `indentLevel`, `attributes`, `quickHelp`)
  VALUES ('*.patient.relationNotes', 'en', 'lava', 'crms', 'Patient', 'relationNotes', '', 'i', 'text', 'No', 'Relation Notes', 100, 0, 'rows="2" cols="50"', 'Relation Notes');
  
-- this is temporary and will move down to EnrollmentStatus
DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.projSiteId'; 
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `maxLength`, `size`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.patient.projSiteId', 'en', 'lava', 'crms', 'Patient', 'projSiteId', '', 'i', 'string', 'No', 'Project Site ID', 25, 20, 0, NULL, 'Project Site ID');

DELETE FROM `listvalues` WHERE `ListID` IN (SELECT `ListID` FROM `List` WHERE `ListName` = 'patientFamilyStatus');
DELETE FROM `list` WHERE `ListName` = 'patientFamilyStatus';
INSERT INTO `list` (`ListName`,`instance`,`scope`,`NumericKey`,`modified`) VALUES('patientFamilyStatus','lava','crms',1,NOW());
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Individual',NULL,1,NOW() FROM `list` where `ListName`='patientFamilyStatus';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Family',NULL,2,NOW() FROM `list` where `ListName`='patientFamilyStatus';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Family Study',NULL,3,NOW() FROM `list` where `ListName`='patientFamilyStatus';

DELETE FROM `listvalues` WHERE `ListID` IN (SELECT `ListID` FROM `List` WHERE `ListName` = 'patientRelationToProband');
DELETE FROM `list` WHERE `ListName` = 'patientRelationToProband';
INSERT INTO `list` (`ListName`,`instance`,`scope`,`NumericKey`,`modified`) VALUES('patientRelationToProband','lava','crms',1,NOW());
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Proband',NULL,1,NOW() FROM `list` where `ListName`='patientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Mother',NULL,2,NOW() FROM `list` where `ListName`='patientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Father',NULL,3,NOW() FROM `list` where `ListName`='patientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Sibling 1',NULL,4,NOW() FROM `list` where `ListName`='patientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Sibling 2',NULL,5,NOW() FROM `list` where `ListName`='patientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Sibling 3',NULL,6,NOW() FROM `list` where `ListName`='patientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Sibling 4',NULL,7,NOW() FROM `list` where `ListName`='patientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Sibling 5',NULL,8,NOW() FROM `list` where `ListName`='patientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Cousin 1',NULL,9,NOW() FROM `list` where `ListName`='patientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Cousin 2',NULL,10,NOW() FROM `list` where `ListName`='patientRelationToProband';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Cousin 3',NULL,11,NOW() FROM `list` where `ListName`='patientRelationToProband';

DELETE FROM `listvalues` WHERE `ListID` IN (SELECT `ListID` FROM `List` WHERE `ListName` = 'patientTwinZygosity');
DELETE FROM `list` WHERE `ListName` = 'patientTwinZygosity';
INSERT INTO `list` (`ListName`,`instance`,`scope`,`NumericKey`,`modified`) VALUES('patientTwinZygosity','lava','crms',1,NOW());
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','1','MZ',1,NOW() FROM `list` where `ListName`='patientTwinZygosity';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','2','Same-sex DZ',2,NOW() FROM `list` where `ListName`='patientTwinZygosity';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','3','Opposite-sex DZ',3,NOW() FROM `list` where `ListName`='patientTwinZygosity';

DELETE FROM versionhistory WHERE module='lava-crms-data' AND version='3.4.2';
INSERT INTO versionhistory(module,version,versiondate,major,minor,fix,updaterequired)
VALUES ('lava-crms-data','3.4.2',NOW(),3,4,2,0);

