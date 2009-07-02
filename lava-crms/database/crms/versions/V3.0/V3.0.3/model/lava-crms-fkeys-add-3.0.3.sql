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

 