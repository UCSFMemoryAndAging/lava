LOCK TABLES `viewproperty` WRITE;
/*!40000 ALTER TABLE `viewproperty` DISABLE KEYS */;

DELETE FROM `viewproperty` WHERE `messageCode` = '*.patient.created';
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `attributes`, `list`, `quickHelp`)
  VALUES ('*.patient.created', 'en', 'lava', 'crms', 'patient', 'created', '', 'c', 'date', 'No', 'Created', 0, '', '', '');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.contactInfo.preferredContactMethod';
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `list`, `quickHelp`)
  VALUES ('*.contactInfo.preferredContactMethod', 'en', 'lava', 'crms', 'contactInfo', 'preferredContactMethod', '', 'i', 'suggest', 'No', 'Preferred Method of Contact', 0, 'contactInfo.preferredContactMethod', 'Preferred Contact Method');
  
DELETE FROM `viewproperty` WHERE `messageCode` = '*.contactInfo.bestTimePhone1';
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `quickHelp`)
  VALUES ('*.contactInfo.bestTimePhone1', 'en', 'lava', 'crms', 'contactInfo', 'bestTimePhone1', '', 'i', 'string', 'No', 'First Phone Best Time to Call', 0, 'First Phone Best Time to Call');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.contactInfo.bestTimePhone2';
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `quickHelp`)
  VALUES ('*.contactInfo.bestTimePhone2', 'en', 'lava', 'crms', 'contactInfo', 'bestTimePhone2', '', 'i', 'string', 'No', 'Second Phone Best Time to Call', 0, 'Second Phone Best Time to Call');

DELETE FROM `viewproperty` WHERE `messageCode` = '*.contactInfo.bestTimePhone3';
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `quickHelp`)
  VALUES ('*.contactInfo.bestTimePhone3', 'en', 'lava', 'crms', 'contactInfo', 'bestTimePhone3', '', 'i', 'string', 'No', 'Third Phone Best Time to Call', 0, 'Third Phone Best Time to Call');

DELETE FROM `listvalues` WHERE `ListID` IN (SELECT `ListID` FROM `list` WHERE `ListName` = 'PreferredContactMethod');
DELETE FROM `list` WHERE `ListName` = 'PreferredContactMethod';
INSERT INTO `list` (`ListName`,`instance`,`scope`,`NumericKey`,`modified`) VALUES('PreferredContactMethod','lava','crms',0,NOW());
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','First Phone',NULL,1,NOW() FROM `list` where `ListName`='PreferredContactMethod';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Second Phone',NULL,2,NOW() FROM `list` where `ListName`='PreferredContactMethod';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Third Phone',NULL,3,NOW() FROM `list` where `ListName`='PreferredContactMethod';
INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`) SELECT `ListID`,'lava','crms','Email',NULL,4,NOW() FROM `list` where `ListName`='PreferredContactMethod';
  
/*!40000 ALTER TABLE `viewproperty` ENABLE KEYS */;
UNLOCK TABLES;

DELETE FROM versionhistory WHERE module='lava-crms-data' AND version='3.4.2';
INSERT INTO versionhistory(module,version,versiondate,major,minor,fix,updaterequired)
VALUES ('lava-crms-data','3.4.2','2014-07-11',3,4,2,0);
