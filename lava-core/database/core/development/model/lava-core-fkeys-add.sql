ALTER TABLE `authusergroup` ADD CONSTRAINT `authusergroup_GID` 
 FOREIGN KEY (`GID` )
 REFERENCES `authgroup` (`GID` )
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `authusergroup_GID` (`GID` ASC) ;

 