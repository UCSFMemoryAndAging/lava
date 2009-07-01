SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';


insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-crms-model','3.0.3',NOW(),3,0,3,1);

-- -----------------------------------------------------
-- Table `patient`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `patient` ;

CREATE  TABLE IF NOT EXISTS `patient` (
  `PIDN` INT(10) NOT NULL AUTO_INCREMENT ,
  `LName` VARCHAR(25) NOT NULL ,
  `MInitial` CHAR(1) NULL DEFAULT NULL ,
  `FName` VARCHAR(25) NOT NULL ,
  `Suffix` VARCHAR(15) NULL DEFAULT NULL ,
  `Degree` VARCHAR(15) NULL DEFAULT NULL ,
  `DOB` DATETIME NULL DEFAULT NULL ,
  `AGE` INT(10) NULL DEFAULT NULL ,
  `Gender` TINYINT(3) NULL DEFAULT NULL ,
  `Hand` VARCHAR(25) NULL DEFAULT NULL ,
  `Deceased` TINYINT(1) NOT NULL DEFAULT '0' ,
  `DOD` DATETIME NULL DEFAULT NULL ,
  `PrimaryLanguage` VARCHAR(25) NULL DEFAULT NULL ,
  `TestingLanguage` VARCHAR(25) NULL DEFAULT NULL ,
  `TransNeeded` TINYINT(1) NULL DEFAULT '0' ,
  `TransLanguage` VARCHAR(25) NULL DEFAULT NULL ,
  `EnterBy` VARCHAR(25) NULL DEFAULT NULL ,
  `DupNameFlag` TINYINT(1) NOT NULL DEFAULT '0' ,
  `FullNameRev` VARCHAR(100) NULL DEFAULT NULL ,
  `FullName` VARCHAR(100) NULL DEFAULT NULL ,
  `FullNameRevNoSuffix` VARCHAR(100) NULL DEFAULT NULL ,
  `FullNameNoSuffix` VARCHAR(100) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`PIDN`) )
ENGINE = InnoDB
AUTO_INCREMENT = 8009
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `caregiver`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `caregiver` ;

CREATE  TABLE IF NOT EXISTS `caregiver` (
  `CareID` INT(10) NOT NULL AUTO_INCREMENT ,
  `PIDN` INT(10) NOT NULL ,
  `Lname` VARCHAR(25) NOT NULL ,
  `FName` VARCHAR(25) NOT NULL ,
  `Gender` TINYINT(3) NULL DEFAULT NULL ,
  `PTRelation` VARCHAR(25) NULL DEFAULT NULL ,
  `LivesWithPT` SMALLINT(5) NULL DEFAULT NULL ,
  `PrimaryLanguage` VARCHAR(25) NULL DEFAULT NULL ,
  `TransNeeded` SMALLINT(5) NULL DEFAULT NULL ,
  `TransLanguage` VARCHAR(25) NULL DEFAULT NULL ,
  `IsPrimContact` SMALLINT(5) NULL DEFAULT NULL ,
  `IsContact` SMALLINT(5) NULL DEFAULT NULL ,
  `IsContactNotes` VARCHAR(100) NULL DEFAULT NULL ,
  `IsCaregiver` SMALLINT(5) NULL DEFAULT NULL ,
  `IsInformant` SMALLINT(5) NULL DEFAULT NULL ,
  `IsNextOfKin` SMALLINT(5) NULL DEFAULT NULL ,
  `IsResearchSurrogate` SMALLINT(5) NULL DEFAULT NULL ,
  `IsPowerOfAttorney` SMALLINT(5) NULL DEFAULT NULL ,
  `IsOtherRole` SMALLINT(5) NULL DEFAULT NULL ,
  `OtherRoleDesc` VARCHAR(50) NULL DEFAULT NULL ,
  `Note` VARCHAR(255) NULL DEFAULT NULL ,
  `ActiveFlag` SMALLINT(5) NULL DEFAULT '1' ,
  `DOB` DATETIME NULL DEFAULT NULL ,
  `Educ` TINYINT(3) NULL DEFAULT NULL ,
  `Race` VARCHAR(25) NULL DEFAULT NULL ,
  `MaritalStatus` VARCHAR(25) NULL DEFAULT NULL ,
  `Occupation` VARCHAR(25) NULL DEFAULT NULL ,
  `Age` INT(10) NULL DEFAULT NULL ,
  `FullName` VARCHAR(100) NULL DEFAULT NULL ,
  `FullNameRev` VARCHAR(100) NULL DEFAULT NULL ,
  `ContactDesc` VARCHAR(255) NULL DEFAULT NULL ,
  `RolesDesc` VARCHAR(255) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`CareID`) ,
  INDEX `caregiver__PIDN` (`PIDN` ASC) ,
  CONSTRAINT `caregiver__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 13131
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `contactinfo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `contactinfo` ;

CREATE  TABLE IF NOT EXISTS `contactinfo` (
  `CInfoID` INT(10) NOT NULL AUTO_INCREMENT ,
  `PIDN` INT(10) NOT NULL ,
  `CareID` INT(10) NULL DEFAULT NULL ,
  `ContactPT` SMALLINT(5) NULL DEFAULT NULL ,
  `IsPTResidence` SMALLINT(5) NULL DEFAULT NULL ,
  `OptOutMAC` SMALLINT(5) NULL DEFAULT '0' ,
  `OptOutAffiliates` SMALLINT(5) NULL DEFAULT '0' ,
  `ActiveFlag` SMALLINT(5) NULL DEFAULT '1' ,
  `Address` VARCHAR(100) NULL DEFAULT NULL ,
  `Address2` VARCHAR(100) NULL DEFAULT NULL ,
  `City` VARCHAR(50) NULL DEFAULT NULL ,
  `State` CHAR(10) NULL DEFAULT NULL ,
  `Zip` VARCHAR(10) NULL DEFAULT NULL ,
  `Country` VARCHAR(50) NULL DEFAULT NULL ,
  `Phone1` VARCHAR(25) NULL DEFAULT NULL ,
  `PhoneType1` VARCHAR(10) NULL DEFAULT NULL ,
  `Phone2` VARCHAR(25) NULL DEFAULT NULL ,
  `PhoneType2` VARCHAR(10) NULL DEFAULT NULL ,
  `Phone3` VARCHAR(25) NULL DEFAULT NULL ,
  `PhoneType3` VARCHAR(10) NULL DEFAULT NULL ,
  `Email` VARCHAR(100) NULL DEFAULT NULL ,
  `Notes` VARCHAR(250) NULL DEFAULT NULL ,
  `ContactNameRev` VARCHAR(100) NULL DEFAULT NULL ,
  `ContactDesc` VARCHAR(100) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`CInfoID`) ,
  INDEX `contactinfo__PIDN` (`PIDN` ASC) ,
  INDEX `contactinfo__CareID` (`CareID` ASC) ,
  CONSTRAINT `contactinfo__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `contactinfo__CareID`
    FOREIGN KEY (`CareID` )
    REFERENCES `caregiver` (`CareID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 10011
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `projectunit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `projectunit` ;

CREATE  TABLE IF NOT EXISTS `projectunit` (
  `ProjUnitID` INT(10) NOT NULL AUTO_INCREMENT ,
  `Project` VARCHAR(25) NOT NULL ,
  `Unit` VARCHAR(25) NULL DEFAULT NULL ,
  `Status` VARCHAR(25) NOT NULL DEFAULT 'ACTIVE' ,
  `EffDate` TIMESTAMP NULL DEFAULT NULL ,
  `ExpDate` DATETIME NULL DEFAULT NULL ,
  `ProjUnitDesc` VARCHAR(75) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`ProjUnitID`) ,
  INDEX `projectunit_ProjUnitDesc` (`ProjUnitDesc` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 80
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `contactlog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `contactlog` ;

CREATE  TABLE IF NOT EXISTS `contactlog` (
  `LogID` INT(10) NOT NULL AUTO_INCREMENT ,
  `PIDN` INT(10) NOT NULL ,
  `ProjName` VARCHAR(75) NULL DEFAULT NULL ,
  `LogDate` DATE NULL DEFAULT NULL ,
  `LogTime` TIME NULL DEFAULT NULL ,
  `Method` VARCHAR(25) NOT NULL DEFAULT 'Phone' ,
  `StaffInit` SMALLINT(5) NOT NULL DEFAULT '1' ,
  `Staff` VARCHAR(50) NULL DEFAULT NULL ,
  `Contact` VARCHAR(50) NULL DEFAULT NULL ,
  `Note` TEXT NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`LogID`) ,
  INDEX `contactlog__PIDN` (`PIDN` ASC) ,
  INDEX `contactlog__ProjName` (`ProjName` ASC) ,
  CONSTRAINT `contactlog__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `contactlog__ProjName`
    FOREIGN KEY (`ProjName` )
    REFERENCES `projectunit` (`ProjUnitDesc` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `crmsauthrole`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crmsauthrole` ;

CREATE  TABLE IF NOT EXISTS `crmsauthrole` (
  `RoleID` INT(10) NOT NULL ,
  `PatientAccess` SMALLINT(5) NOT NULL DEFAULT '1' ,
  `PhiAccess` SMALLINT(5) NOT NULL DEFAULT '1' ,
  `GhiAccess` SMALLINT(5) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (`RoleID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `crmsauthuser`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crmsauthuser` ;

CREATE  TABLE IF NOT EXISTS `crmsauthuser` (
  `UID` INT(10) NOT NULL ,
  PRIMARY KEY (`UID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `crmsauthuserrole`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crmsauthuserrole` ;

CREATE  TABLE IF NOT EXISTS `crmsauthuserrole` (
  `URID` INT(10) NOT NULL ,
  `Project` VARCHAR(25) NULL DEFAULT NULL ,
  `Unit` VARCHAR(25) NULL DEFAULT NULL ,
  PRIMARY KEY (`URID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `doctor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor` ;

CREATE  TABLE IF NOT EXISTS `doctor` (
  `DocID` INT(10) NOT NULL AUTO_INCREMENT ,
  `LName` VARCHAR(25) NOT NULL ,
  `MInitial` CHAR(1) NULL DEFAULT NULL ,
  `FName` VARCHAR(25) NOT NULL ,
  `Address` VARCHAR(100) NULL DEFAULT NULL ,
  `City` VARCHAR(50) NULL DEFAULT NULL ,
  `State` CHAR(2) NULL DEFAULT NULL ,
  `Zip` VARCHAR(10) NULL DEFAULT NULL ,
  `Phone1` VARCHAR(25) NULL DEFAULT NULL ,
  `PhoneType1` VARCHAR(10) NULL DEFAULT NULL ,
  `Phone2` VARCHAR(25) NULL DEFAULT NULL ,
  `PhoneType2` VARCHAR(10) NULL DEFAULT NULL ,
  `Phone3` VARCHAR(25) NULL DEFAULT NULL ,
  `PhoneType3` VARCHAR(10) NULL DEFAULT NULL ,
  `Email` VARCHAR(100) NULL DEFAULT NULL ,
  `DocType` VARCHAR(50) NULL DEFAULT NULL ,
  `FullNameRev` VARCHAR(100) NULL DEFAULT NULL ,
  `FullName` VARCHAR(100) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`DocID`) )
ENGINE = InnoDB
AUTO_INCREMENT = 10905
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `enrollmentstatus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `enrollmentstatus` ;

CREATE  TABLE IF NOT EXISTS `enrollmentstatus` (
  `EnrollStatID` INT(10) NOT NULL AUTO_INCREMENT ,
  `PIDN` INT(10) NOT NULL ,
  `ProjName` VARCHAR(75) NULL DEFAULT NULL ,
  `SubjectStudyID` VARCHAR(10) NULL DEFAULT NULL ,
  `ReferralSource` VARCHAR(75) NULL DEFAULT NULL ,
  `LatestDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `LatestDate` DATETIME NULL DEFAULT NULL ,
  `LatestNote` VARCHAR(100) NULL DEFAULT NULL ,
  `ReferredDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `ReferredDate` DATETIME NULL DEFAULT NULL ,
  `ReferredNote` VARCHAR(100) NULL DEFAULT NULL ,
  `DeferredDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `DeferredDate` DATETIME NULL DEFAULT NULL ,
  `DeferredNote` VARCHAR(100) NULL DEFAULT NULL ,
  `EligibleDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `EligibleDate` DATETIME NULL DEFAULT NULL ,
  `EligibleNote` VARCHAR(100) NULL DEFAULT NULL ,
  `IneligibleDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `IneligibleDate` DATETIME NULL DEFAULT NULL ,
  `IneligibleNote` VARCHAR(100) NULL DEFAULT NULL ,
  `DeclinedDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `DeclinedDate` DATETIME NULL DEFAULT NULL ,
  `DeclinedNote` VARCHAR(100) NULL DEFAULT NULL ,
  `EnrolledDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `EnrolledDate` DATETIME NULL DEFAULT NULL ,
  `EnrolledNote` VARCHAR(100) NULL DEFAULT NULL ,
  `ExcludedDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `ExcludedDate` DATETIME NULL DEFAULT NULL ,
  `ExcludedNote` VARCHAR(100) NULL DEFAULT NULL ,
  `WithdrewDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `WithdrewDate` DATETIME NULL DEFAULT NULL ,
  `WithdrewNote` VARCHAR(100) NULL DEFAULT NULL ,
  `InactiveDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `InactiveDate` DATETIME NULL DEFAULT NULL ,
  `InactiveNote` VARCHAR(100) NULL DEFAULT NULL ,
  `DeceasedDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `DeceasedDate` DATETIME NULL DEFAULT NULL ,
  `DeceasedNote` VARCHAR(100) NULL DEFAULT NULL ,
  `AutopsyDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `AutopsyDate` DATETIME NULL DEFAULT NULL ,
  `AutopsyNote` VARCHAR(100) NULL DEFAULT NULL ,
  `ClosedDesc` VARCHAR(25) NULL DEFAULT NULL ,
  `ClosedDate` DATETIME NULL DEFAULT NULL ,
  `ClosedNote` VARCHAR(100) NULL DEFAULT NULL ,
  `EnrollmentNotes` VARCHAR(500) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`EnrollStatID`) ,
  INDEX `enrollmentstatus__PIDN` (`PIDN` ASC) ,
  INDEX `enrollmentstatus__ProjName` (`ProjName` ASC) ,
  CONSTRAINT `enrollmentstatus__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `enrollmentstatus__ProjName`
    FOREIGN KEY (`ProjName` )
    REFERENCES `projectunit` (`ProjUnitDesc` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 16923
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `instrument`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `instrument` ;

CREATE  TABLE IF NOT EXISTS `instrument` (
  `InstrID` INT(10) NOT NULL AUTO_INCREMENT ,
  `InstrName` VARCHAR(25) NOT NULL ,
  `TableName` VARCHAR(25) NOT NULL ,
  `FormName` VARCHAR(50) NULL DEFAULT NULL ,
  `Category` VARCHAR(25) NULL DEFAULT NULL ,
  `HasVersion` TINYINT(1) NOT NULL DEFAULT '0' ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`InstrID`) ,
  UNIQUE INDEX `InstrName` (`InstrName` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 145
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `visit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `visit` ;

CREATE  TABLE IF NOT EXISTS `visit` (
  `VID` INT(10) NOT NULL AUTO_INCREMENT ,
  `PIDN` INT(10) NOT NULL ,
  `ProjName` VARCHAR(75) NOT NULL ,
  `VLocation` VARCHAR(25) NOT NULL ,
  `VType` VARCHAR(25) NOT NULL ,
  `VWith` VARCHAR(25) NULL DEFAULT NULL ,
  `VDate` DATE NOT NULL ,
  `VTime` TIME NULL DEFAULT NULL ,
  `VStatus` VARCHAR(25) NOT NULL ,
  `VNotes` VARCHAR(255) NULL DEFAULT NULL ,
  `FUMonth` CHAR(3) NULL DEFAULT NULL ,
  `FUYear` CHAR(4) NULL DEFAULT NULL ,
  `FUNote` VARCHAR(100) NULL DEFAULT NULL ,
  `WList` VARCHAR(25) NULL DEFAULT NULL ,
  `WListNote` VARCHAR(100) NULL DEFAULT NULL ,
  `WListDate` DATETIME NULL DEFAULT NULL ,
  `VShortDesc` VARCHAR(255) NULL DEFAULT NULL ,
  `AgeAtVisit` SMALLINT(5) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`VID`) ,
  INDEX `visit__PIDN` (`PIDN` ASC) ,
  INDEX `visit__ProjName` (`ProjName` ASC) ,
  CONSTRAINT `visit__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `visit__ProjName`
    FOREIGN KEY (`ProjName` )
    REFERENCES `projectunit` (`ProjUnitDesc` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 42666
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `instrumenttracking`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `instrumenttracking` ;

CREATE  TABLE IF NOT EXISTS `instrumenttracking` (
  `InstrID` INT(10) NOT NULL AUTO_INCREMENT ,
  `VID` INT(10) NOT NULL ,
  `ProjName` VARCHAR(75) NOT NULL ,
  `PIDN` INT(10) NOT NULL ,
  `InstrType` VARCHAR(25) NOT NULL ,
  `InstrVer` VARCHAR(25) NULL DEFAULT NULL ,
  `DCDate` DATETIME NOT NULL ,
  `DCBy` VARCHAR(25) NULL DEFAULT NULL ,
  `DCStatus` VARCHAR(25) NOT NULL ,
  `DCNotes` VARCHAR(255) NULL DEFAULT NULL ,
  `ResearchStatus` VARCHAR(50) NULL DEFAULT NULL ,
  `QualityIssue` VARCHAR(50) NULL DEFAULT NULL ,
  `QualityIssue2` VARCHAR(50) NULL DEFAULT NULL ,
  `QualityIssue3` VARCHAR(50) NULL DEFAULT NULL ,
  `QualityNotes` VARCHAR(100) NULL DEFAULT NULL ,
  `DEDate` DATETIME NULL DEFAULT NULL ,
  `DEBy` VARCHAR(25) NULL DEFAULT NULL ,
  `DEStatus` VARCHAR(25) NULL DEFAULT NULL ,
  `DENotes` VARCHAR(255) NULL DEFAULT NULL ,
  `DVDate` DATETIME NULL DEFAULT NULL ,
  `DVBy` VARCHAR(25) NULL DEFAULT NULL ,
  `DVStatus` VARCHAR(25) NULL DEFAULT NULL ,
  `DVNotes` VARCHAR(255) NULL DEFAULT NULL ,
  `latestflag` TINYINT(1) NOT NULL DEFAULT '0' ,
  `FieldStatus` SMALLINT(5) NULL DEFAULT NULL ,
  `AgeAtDC` SMALLINT(5) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`InstrID`) ,
  INDEX `AgeLookup` (`InstrID` ASC, `AgeAtDC` ASC) ,
  INDEX `PIDN_InstrType_DCDate_DCStatus` (`PIDN` ASC, `InstrType` ASC, `DCDate` ASC, `DCStatus` ASC) ,
  INDEX `instrumenttracking__InstrType` (`InstrType` ASC) ,
  INDEX `instrumenttracking__VID` (`VID` ASC) ,
  INDEX `instrumenttracking__ProjName` (`ProjName` ASC) ,
  INDEX `insttumenttracking__PIDN` (`PIDN` ASC) ,
  CONSTRAINT `instrumenttracking__InstrType`
    FOREIGN KEY (`InstrType` )
    REFERENCES `instrument` (`InstrName` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `instrumenttracking__VID`
    FOREIGN KEY (`VID` )
    REFERENCES `visit` (`VID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `instrumenttracking__ProjName`
    FOREIGN KEY (`ProjName` )
    REFERENCES `projectunit` (`ProjUnitDesc` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `insttumenttracking__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 130203
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `instrumentnotes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `instrumentnotes` ;

CREATE  TABLE IF NOT EXISTS `instrumentnotes` (
  `InstrID` INT(10) NOT NULL ,
  `Section` VARCHAR(50) NOT NULL ,
  `Note` VARCHAR(2000) NULL DEFAULT NULL ,
  INDEX `instrumentnotes__instrID` (`InstrID` ASC) ,
  CONSTRAINT `instrumentnotes__instrID`
    FOREIGN KEY (`InstrID` )
    REFERENCES `instrumenttracking` (`InstrID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `instrumentsummary`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `instrumentsummary` ;

CREATE  TABLE IF NOT EXISTS `instrumentsummary` (
  `InstrID` INT(10) NOT NULL ,
  `Summary` VARCHAR(500) NULL DEFAULT NULL ,
  PRIMARY KEY (`InstrID`) ,
  INDEX `instrumentsummary__InstrID` (`InstrID` ASC) ,
  CONSTRAINT `instrumentsummary__InstrID`
    FOREIGN KEY (`InstrID` )
    REFERENCES `instrumenttracking` (`InstrID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `patientdoctors`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `patientdoctors` ;

CREATE  TABLE IF NOT EXISTS `patientdoctors` (
  `PIDNDocID` INT(10) NOT NULL AUTO_INCREMENT ,
  `DocID` INT(10) NOT NULL ,
  `PIDN` INT(10) NOT NULL ,
  `DocStat` VARCHAR(25) NULL DEFAULT NULL ,
  `DocNote` VARCHAR(100) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`PIDNDocID`) ,
  INDEX `patientdoctors__PIDN` (`PIDN` ASC) ,
  INDEX `patientdoctors__DocID` (`DocID` ASC) ,
  CONSTRAINT `patientdoctors__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `patientdoctors__DocID`
    FOREIGN KEY (`DocID` )
    REFERENCES `doctor` (`DocID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 12164
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `tasks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tasks` ;

CREATE  TABLE IF NOT EXISTS `tasks` (
  `TaskID` INT(10) NOT NULL AUTO_INCREMENT ,
  `PIDN` INT(10) NOT NULL ,
  `ProjName` VARCHAR(75) NULL DEFAULT NULL ,
  `OpenedDate` DATETIME NULL DEFAULT NULL ,
  `OpenedBy` VARCHAR(25) NULL DEFAULT NULL ,
  `TaskType` VARCHAR(25) NULL DEFAULT NULL ,
  `TaskDesc` VARCHAR(255) NULL DEFAULT NULL ,
  `DueDate` DATETIME NULL DEFAULT NULL ,
  `TaskStatus` VARCHAR(50) NULL DEFAULT NULL ,
  `AssignedTo` VARCHAR(25) NULL DEFAULT NULL ,
  `WorkingNotes` VARCHAR(255) NULL DEFAULT NULL ,
  `ClosedDate` DATETIME NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`TaskID`) ,
  INDEX `tasks__PIDN` (`PIDN` ASC) ,
  INDEX `tasks__ProjName` (`ProjName` ASC) ,
  CONSTRAINT `tasks__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tasks__ProjName`
    FOREIGN KEY (`ProjName` )
    REFERENCES `projectunit` (`ProjUnitDesc` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 3021
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `uploadedfiles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `uploadedfiles` ;

CREATE  TABLE IF NOT EXISTS `uploadedfiles` (
  `InstrID` INT(10) NOT NULL ,
  `FileName` VARCHAR(500) NULL DEFAULT NULL ,
  `FileContents` VARCHAR(16) NULL DEFAULT NULL ,
  PRIMARY KEY (`InstrID`) ,
  INDEX `uploadedfiles__InstrID` (`InstrID` ASC) ,
  CONSTRAINT `uploadedfiles__InstrID`
    FOREIGN KEY (`InstrID` )
    REFERENCES `instrumenttracking` (`InstrID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `patientconsent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `patientconsent` ;

CREATE  TABLE IF NOT EXISTS `patientconsent` (
  `ConsentID` INT(10) NOT NULL AUTO_INCREMENT ,
  `PIDN` INT(10) NOT NULL ,
  `CareID` INT(10) NULL DEFAULT NULL ,
  `ProjName` VARCHAR(75) NOT NULL ,
  `ConsentType` VARCHAR(50) NOT NULL ,
  `ConsentDate` TIMESTAMP NULL DEFAULT NULL ,
  `ExpirationDate` DATETIME NULL DEFAULT NULL ,
  `WithdrawlDate` DATETIME NULL DEFAULT NULL ,
  `Note` VARCHAR(100) NULL DEFAULT NULL ,
  `CapacityReviewBy` VARCHAR(25) NULL DEFAULT NULL ,
  `ConsentRevision` VARCHAR(10) NULL DEFAULT NULL ,
  `ConsentDeclined` VARCHAR(10) NULL DEFAULT NULL ,
  `CTreasearch` VARCHAR(10) NULL DEFAULT NULL ,
  `CTneuro` VARCHAR(10) NULL DEFAULT NULL ,
  `CTDNA` VARCHAR(10) NULL DEFAULT NULL ,
  `CTGenetic` VARCHAR(10) NULL DEFAULT NULL ,
  `CTGeneticShare` VARCHAR(10) NULL DEFAULT NULL ,
  `CTlumbar` VARCHAR(10) NULL DEFAULT NULL ,
  `CTvideo` VARCHAR(10) NULL DEFAULT NULL ,
  `CTaudio` VARCHAR(10) NULL DEFAULT NULL ,
  `CTmediaedu` VARCHAR(10) NULL DEFAULT NULL ,
  `CT1point5T` VARCHAR(10) NULL DEFAULT NULL ,
  `CT4t` VARCHAR(10) NULL DEFAULT NULL ,
  `CTotherstudy` VARCHAR(10) NULL DEFAULT NULL ,
  `CTfollowup` VARCHAR(10) NULL DEFAULT NULL ,
  `CTmusic` VARCHAR(10) NULL DEFAULT NULL ,
  `CTpart` VARCHAR(10) NULL DEFAULT NULL ,
  `CTcarepart` VARCHAR(10) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`ConsentID`) ,
  INDEX `patientconsent__PIDN` (`PIDN` ASC) ,
  INDEX `patientconsent__ProjName` (`ProjName` ASC) ,
  CONSTRAINT `patientconsent__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `patientconsent__ProjName`
    FOREIGN KEY (`ProjName` )
    REFERENCES `projectunit` (`ProjUnitDesc` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Placeholder table for view `vwrptprojectpatientstatus`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vwrptprojectpatientstatus` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `vwrptprojectvisitlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vwrptprojectvisitlist` (`id` INT);

-- -----------------------------------------------------
-- View `vwrptprojectpatientstatus`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `vwrptprojectpatientstatus` ;
DROP TABLE IF EXISTS `vwrptprojectpatientstatus`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `vwrptprojectpatientstatus` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `StatusDate`,`lps`.`LatestDesc` AS `Status`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`Project` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,_utf8'OVERALL' AS `Unit`,1 AS `UnitOrder` from ((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) union select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `LatestDate`,`lps`.`LatestDesc` AS `LatestDesc`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`ProjUnitDesc` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,`pu`.`Unit` AS `Unit`,2 AS `UnitOrder` from ((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) where (`pu`.`Unit` is not null);

-- -----------------------------------------------------
-- View `vwrptprojectvisitlist`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `vwrptprojectvisitlist` ;
DROP TABLE IF EXISTS `vwrptprojectvisitlist`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `vwrptprojectvisitlist` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`TransLanguage` AS `TransLanguage`,`p`.`Gender` AS `Gender`,`p`.`AGE` AS `AGE`,`v`.`VLocation` AS `VLocation`,`v`.`VType` AS `VType`,`v`.`VWith` AS `VWith`,`v`.`VDate` AS `VDate`,`v`.`VStatus` AS `VStatus`,`v`.`ProjName` AS `ProjName`,`v`.`VNotes` AS `VNotes`,cast(`v`.`VDate` as date) AS `VDateNoTime` from (`patient` `p` join `visit` `v` on((`p`.`PIDN` = `v`.`PIDN`))) where (not((`v`.`VStatus` like _latin1'%CANC%')));


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
