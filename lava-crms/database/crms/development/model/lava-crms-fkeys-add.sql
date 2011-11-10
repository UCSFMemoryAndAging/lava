ALTER TABLE `crmsauthrole` ADD CONSTRAINT `crmsauthrole__RoleID` 
 FOREIGN KEY (`RoleID` )
 REFERENCES `authrole` (`RoleID` )
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `crmsauthrole__RoleID` (`RoleID` ASC) ;


ALTER TABLE `crmsauthuser` ADD CONSTRAINT `crmsauthuser__UID` 
 FOREIGN KEY (`UID` )
 REFERENCES `authuser` (`UID` )
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `crmsauthuser__UID` (`UID` ASC) ;

ALTER TABLE `crmsauthuserrole` ADD CONSTRAINT `crmsauthuserrole__URID` 
 FOREIGN KEY (`URID` )
 REFERENCES `authuserrole` (`URID` )
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `crmsauthuserrole__URID` (`URID` ASC) ;

 ALTER TABLE `crms_file` ADD CONSTRAINT `crms_file__patient` 
 FOREIGN KEY (`pidn` )
 REFERENCES `patient` (`PIDN` )
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `crms_file__patient` (`pidn` ASC) ;

ALTER TABLE `crms_file` ADD CONSTRAINT `crms_file__enrollment` 
 FOREIGN KEY (`enroll_stat_id` )
 REFERENCES `enrollmentstatus` (`EnrollStatID` )
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `crms_file__enrollment` (`enroll_stat_id` ASC) ;


ALTER TABLE `crms_file` ADD CONSTRAINT `crms_file__visit` 
 FOREIGN KEY (`vid` )
 REFERENCES `visit` (`VID` )
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `crms_file__visit` (`vid` ASC) ;


ALTER TABLE `crms_file` ADD CONSTRAINT `crms_file__instrument`
 FOREIGN KEY (`instr_id` )
 REFERENCES `instrumenttracking` (`InstrID` )
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `crms_file__instrument` (`instr_id` ASC) ;