SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';


ALTER TABLE `authusergroup` DROP FOREIGN KEY `authusergroup_GID`; 
ALTER TABLE `authusergroup` DROP INDEX `authusergroup_GID`; 
ALTER TABLE `authusergroup` ADD CONSTRAINT `authusergroup_GID`
            FOREIGN KEY (`GID`)
            REFERENCES `authgroup` (`GID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

            
-- TODO: when it is time to version this script, i.e. when a cvs update done by a user also requires an accompanying 
-- database change script, then:
-- 1) move this script to versions into the model directory of the next version,
--    and rename it to reflect the prior and new version numbers
-- 2) update the version history table. modify and uncomment:
-- insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
--	VALUES ('lava-core-model','3.0.3',NOW(),3,0,3,1);
--
-- The script can also be versioned even when there are database changes that do not have any source in cvs
-- that is dependent on the changes, in order to make the script available to users. Or, the user can just
-- obtain the script from the development/model directory and run it.

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
            