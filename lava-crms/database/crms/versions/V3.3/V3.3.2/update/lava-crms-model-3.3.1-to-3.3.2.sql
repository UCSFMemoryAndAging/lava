-- ************************************************************
-- EMORY: start patient.InactiveDate changes
-- ************************************************************

ALTER TABLE `patient` 
  ADD COLUMN `InactiveDate` DATETIME NULL DEFAULT NULL AFTER `notes` ;

-- -----------------------------------------------------
-- View `vwrptprojectpatientstatus`
-- Change: consider soft-deleted patients (patient.InactiveDate)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vwrptprojectpatientstatus`;
CREATE OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `vwrptprojectpatientstatus`
  AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`pa`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `StatusDate`,`lps`.`LatestDesc` AS `Status`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`Project` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,_utf8'OVERALL' AS `Unit`,1 AS `UnitOrder` from ((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN` AND `p`.`InactiveDate` IS NULL))) join `patient_age` `pa` on ((`p`.`PIDN` = `pa`.`PIDN`)) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) union select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`pa`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `LatestDate`,`lps`.`LatestDesc` AS `LatestDesc`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`ProjUnitDesc` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,`pu`.`Unit` AS `Unit`,2 AS `UnitOrder` from ((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `patient_age` `pa` on ((`p`.`PIDN` = `pa`.`PIDN`)) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) where (`pu`.`Unit` is not null);

  -- -----------------------------------------------------
-- View `vwrptprojectvisitlist`
-- Change: consider soft-deleted patients (patient.InactiveDate)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vwrptprojectvisitlist`;
CREATE OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `vwrptprojectvisitlist` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`TransLanguage` AS `TransLanguage`,`p`.`Gender` AS `Gender`,`pa`.`AGE` AS `AGE`,`v`.`VLocation` AS `VLocation`,`v`.`VType` AS `VType`,`v`.`VWith` AS `VWith`,`v`.`VDate` AS `VDate`,`v`.`VStatus` AS `VStatus`,`v`.`ProjName` AS `ProjName`,`v`.`VNotes` AS `VNotes`,cast(`v`.`VDate` as date) AS `VDateNoTime` from (`patient` `p` join `visit` `v` on((`p`.`PIDN` = `v`.`PIDN`)) join `patient_age` `pa` on ((`p`.`PIDN` = `pa`.`PIDN`) AND `p`.`InactiveDate` IS NULL)) where (not((`v`.`VStatus` like _latin1'%CANC%')));

-- ************************************************************
-- EMORY:  end patient.InactiveDate changes
-- ************************************************************

-- ************************************************************
-- EMORY: start enrollmentstatus.SubjectStudyID changes
-- ************************************************************

-- increase subjectstudyid from 10 to 15 max characters
ALTER TABLE `enrollmentstatus`
  CHANGE COLUMN `SubjectStudyID` `SubjectStudyID` VARCHAR(15) NULL DEFAULT NULL;
  
UPDATE viewproperty
  SET maxLength=15
  WHERE instance='lava' AND scope='crms' AND entity='enrollmentStatus' AND property='subjectStudyId';


-- ************************************************************
-- EMORY: end enrollmentstatus.SubjectStudyID changes
-- ************************************************************

  
insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-crms-model','3.3.2',NOW(),3,3,2,0);
