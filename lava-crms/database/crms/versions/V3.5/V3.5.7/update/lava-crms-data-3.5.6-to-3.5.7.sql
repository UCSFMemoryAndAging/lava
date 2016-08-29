INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,`context`,`style`,`list`,`attributes`,`required`,`label`,`quickHelp`,`propOrder`,`maxLength`,`size`,`modified`) VALUES ('*.importDefinition.allowExtremeDates','en','lava','crms',NULL,'importDefinition','allowExtremeDates',NULL,'i','toggle',NULL,NULL,NULL,'Allow extreme dates?','Allow extreme dates for birth date and visit date/instrument data collection date to support de-identified import or other rare situations where dates are not present',NULL,NULL,NULL,'2016-08-28');

DELETE FROM versionhistory WHERE module='lava-crms-data' AND version='3.5.7';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-data','3.5.7','2016-08-28',3,5,7,0);

