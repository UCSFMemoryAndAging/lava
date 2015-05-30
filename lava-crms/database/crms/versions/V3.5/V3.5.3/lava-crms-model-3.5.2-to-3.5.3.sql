ALTER TABLE `crms_import_definition` ADD CONSTRAINT `crms_import_definition__ProjName` 
 FOREIGN KEY (`ProjName` )
 REFERENCES `projectunit` (`ProjUnitDesc` )
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `crms_import_definition__ProjName_idx` (`ProjName` ASC) ;

ALTER TABLE `crms_import_log` ADD CONSTRAINT `crms_import_log__ProjName` 
 FOREIGN KEY (`ProjName` )
 REFERENCES `projectunit` (`ProjUnitDesc` )
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `crms_import_log__ProjName_idx` (`ProjName` ASC) ;

DELETE FROM versionhistory WHERE module='lava-crms-model' AND version='3.5.3';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-model','3.5.3','2015-05-30',3,5,3,0);


