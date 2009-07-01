SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `` ;

ALTER TABLE `lava_crms`.`patientconsent` 
  ADD CONSTRAINT `patientconsent__PIDN`
  FOREIGN KEY (`PIDN` )
  REFERENCES `lava_crms`.`patient` (`PIDN` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `patientconsent__ProjName`
  FOREIGN KEY (`ProjName` )
  REFERENCES `lava_crms`.`projectunit` (`ProjUnitDesc` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `patientconsent__PIDN` (`PIDN` ASC) 
, ADD INDEX `patientconsent__ProjName` (`ProjName` ASC) ;

ALTER TABLE `lava_crms`.`caregiver` 
  ADD CONSTRAINT `caregiver__PIDN`
  FOREIGN KEY (`PIDN` )
  REFERENCES `lava_crms`.`patient` (`PIDN` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `caregiver__PIDN` (`PIDN` ASC) ;

ALTER TABLE `lava_crms`.`contactinfo` 
  ADD CONSTRAINT `contactinfo__CareID`
  FOREIGN KEY (`CareID` )
  REFERENCES `lava_crms`.`caregiver` (`CareID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `contactinfo__PIDN`
  FOREIGN KEY (`PIDN` )
  REFERENCES `lava_crms`.`patient` (`PIDN` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `contactinfo__CareID` (`CareID` ASC) 
, ADD INDEX `contactinfo__PIDN` (`PIDN` ASC) ;

ALTER TABLE `lava_crms`.`contactlog` 
  ADD CONSTRAINT `contactlog__PIDN`
  FOREIGN KEY (`PIDN` )
  REFERENCES `lava_crms`.`patient` (`PIDN` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `contactlog__ProjName`
  FOREIGN KEY (`ProjName` )
  REFERENCES `lava_crms`.`projectunit` (`ProjUnitDesc` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `contactlog__PIDN` (`PIDN` ASC) 
, ADD INDEX `contactlog__ProjName` (`ProjName` ASC) ;

ALTER TABLE `lava_crms`.`enrollmentstatus` 
  ADD CONSTRAINT `enrollmentstatus__PIDN`
  FOREIGN KEY (`PIDN` )
  REFERENCES `lava_crms`.`patient` (`PIDN` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `enrollmentstatus__ProjName`
  FOREIGN KEY (`ProjName` )
  REFERENCES `lava_crms`.`projectunit` (`ProjUnitDesc` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `enrollmentstatus__PIDN` (`PIDN` ASC) 
, ADD INDEX `enrollmentstatus__ProjName` (`ProjName` ASC) ;

ALTER TABLE `lava_crms`.`instrumentnotes` 
  ADD CONSTRAINT `instrumentnotes__instrID`
  FOREIGN KEY (`InstrID` )
  REFERENCES `lava_crms`.`instrumenttracking` (`InstrID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `instrumentnotes__instrID` (`InstrID` ASC) ;

ALTER TABLE `lava_crms`.`instrumentsummary` 
  ADD CONSTRAINT `instrumentsummary__InstrID`
  FOREIGN KEY (`InstrID` )
  REFERENCES `lava_crms`.`instrumenttracking` (`InstrID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `instrumentsummary__InstrID` (`InstrID` ASC) ;

ALTER TABLE `lava_crms`.`instrumenttracking` 
  ADD CONSTRAINT `instrumenttracking__InstrType`
  FOREIGN KEY (`InstrType` )
  REFERENCES `lava_crms`.`instrument` (`InstrName` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `instrumenttracking__ProjName`
  FOREIGN KEY (`ProjName` )
  REFERENCES `lava_crms`.`projectunit` (`ProjUnitDesc` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `instrumenttracking__VID`
  FOREIGN KEY (`VID` )
  REFERENCES `lava_crms`.`visit` (`VID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `insttumenttracking__PIDN`
  FOREIGN KEY (`PIDN` )
  REFERENCES `lava_crms`.`patient` (`PIDN` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `instrumenttracking__InstrType` (`InstrType` ASC) 
, ADD INDEX `instrumenttracking__ProjName` (`ProjName` ASC) 
, ADD INDEX `instrumenttracking__VID` (`VID` ASC) 
, ADD INDEX `insttumenttracking__PIDN` (`PIDN` ASC) ;

ALTER TABLE `lava_crms`.`patientdoctors` 
  ADD CONSTRAINT `patientdoctors__DocID`
  FOREIGN KEY (`DocID` )
  REFERENCES `lava_crms`.`doctor` (`DocID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `patientdoctors__PIDN`
  FOREIGN KEY (`PIDN` )
  REFERENCES `lava_crms`.`patient` (`PIDN` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `patientdoctors__DocID` (`DocID` ASC) 
, ADD INDEX `patientdoctors__PIDN` (`PIDN` ASC) ;

ALTER TABLE `lava_crms`.`tasks` 
  ADD CONSTRAINT `tasks__PIDN`
  FOREIGN KEY (`PIDN` )
  REFERENCES `lava_crms`.`patient` (`PIDN` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `tasks__ProjName`
  FOREIGN KEY (`ProjName` )
  REFERENCES `lava_crms`.`projectunit` (`ProjUnitDesc` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `tasks__PIDN` (`PIDN` ASC) 
, ADD INDEX `tasks__ProjName` (`ProjName` ASC) ;

ALTER TABLE `lava_crms`.`uploadedfiles` 
  ADD CONSTRAINT `uploadedfiles__InstrID`
  FOREIGN KEY (`InstrID` )
  REFERENCES `lava_crms`.`instrumenttracking` (`InstrID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `uploadedfiles__InstrID` (`InstrID` ASC) ;

ALTER TABLE `lava_crms`.`visit` 
  ADD CONSTRAINT `visit__PIDN`
  FOREIGN KEY (`PIDN` )
  REFERENCES `lava_crms`.`patient` (`PIDN` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `visit__ProjName`
  FOREIGN KEY (`ProjName` )
  REFERENCES `lava_crms`.`projectunit` (`ProjUnitDesc` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `visit__PIDN` (`PIDN` ASC) 
, ADD INDEX `visit__ProjName` (`ProjName` ASC) ;


-- -----------------------------------------------------
-- Placeholder table for view ``.`vwrptprojectpatientstatus`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ``.`vwrptprojectpatientstatus` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view ``.`vwrptprojectvisitlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ``.`vwrptprojectvisitlist` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lava_crms`.`vwrptprojectpatientstatus`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_crms`.`vwrptprojectpatientstatus` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lava_crms`.`vwrptprojectvisitlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_crms`.`vwrptprojectvisitlist` (`id` INT);

-- -----------------------------------------------------
-- View ``.`vwrptprojectpatientstatus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS ``.`vwrptprojectpatientstatus`;
DROP VIEW IF EXISTS ``.`vwrptprojectpatientstatus` ;

-- -----------------------------------------------------
-- View ``.`vwrptprojectvisitlist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS ``.`vwrptprojectvisitlist`;
DROP VIEW IF EXISTS ``.`vwrptprojectvisitlist` ;

-- -----------------------------------------------------
-- View `lava_crms`.`vwrptprojectpatientstatus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`vwrptprojectpatientstatus`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `vwrptprojectpatientstatus` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `StatusDate`,`lps`.`LatestDesc` AS `Status`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`Project` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,_utf8'OVERALL' AS `Unit`,1 AS `UnitOrder` from ((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) union select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `LatestDate`,`lps`.`LatestDesc` AS `LatestDesc`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`ProjUnitDesc` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,`pu`.`Unit` AS `Unit`,2 AS `UnitOrder` from ((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) where (`pu`.`Unit` is not null);

-- -----------------------------------------------------
-- View `lava_crms`.`vwrptprojectvisitlist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`vwrptprojectvisitlist`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `vwrptprojectvisitlist` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`TransLanguage` AS `TransLanguage`,`p`.`Gender` AS `Gender`,`p`.`AGE` AS `AGE`,`v`.`VLocation` AS `VLocation`,`v`.`VType` AS `VType`,`v`.`VWith` AS `VWith`,`v`.`VDate` AS `VDate`,`v`.`VStatus` AS `VStatus`,`v`.`ProjName` AS `ProjName`,`v`.`VNotes` AS `VNotes`,cast(`v`.`VDate` as date) AS `VDateNoTime` from (`patient` `p` join `visit` `v` on((`p`.`PIDN` = `v`.`PIDN`))) where (not((`v`.`VStatus` like _latin1'%CANC%')));

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
