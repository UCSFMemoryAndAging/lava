SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `lava_crms` DEFAULT CHARACTER SET latin1 ;
USE `lava_crms` ;

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
CREATE TABLE IF NOT EXISTS `vwrptprojectpatientstatus` (`PIDN` INT, `FullNameRev` INT, `AGE` INT, `Gender` INT, `ProjName` INT, `StatusDate` INT, `Status` INT, `StatusNote` INT, `StatusOrder` INT, `ProjUnitDesc` INT, `Project` INT, `Unit` INT, `UnitOrder` INT);

-- -----------------------------------------------------
-- Placeholder table for view `vwrptprojectvisitlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vwrptprojectvisitlist` (`PIDN` INT, `FullNameRev` INT, `TransLanguage` INT, `Gender` INT, `AGE` INT, `VLocation` INT, `VType` INT, `VWith` INT, `VDate` INT, `VStatus` INT, `ProjName` INT, `VNotes` INT, `VDateNoTime` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_demographics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_demographics` (`PIDN_demographics` INT, `DOB` INT, `AGE` INT, `Gender` INT, `Hand` INT, `Deceased` INT, `DOD` INT, `PrimaryLanguage` INT, `TestingLanguage` INT, `TransNeeded` INT, `TransLanguage` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_enrollment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_enrollment` (`EnrollStatID` INT, `PIDN_Enrollment` INT, `ProjName` INT, `SubjectStudyID` INT, `ReferralSource` INT, `LatestDesc` INT, `LatestDate` INT, `LatestNote` INT, `ReferredDesc` INT, `ReferredDate` INT, `ReferredNote` INT, `DeferredDesc` INT, `DeferredDate` INT, `DeferredNote` INT, `EligibleDesc` INT, `EligibleDate` INT, `EligibleNote` INT, `IneligibleDesc` INT, `IneligibleDate` INT, `IneligibleNote` INT, `DeclinedDesc` INT, `DeclinedDate` INT, `DeclinedNote` INT, `EnrolledDesc` INT, `EnrolledDate` INT, `EnrolledNote` INT, `ExcludedDesc` INT, `ExcludedDate` INT, `ExcludedNote` INT, `WithdrewDesc` INT, `WithdrewDate` INT, `WithdrewNote` INT, `InactiveDesc` INT, `InactiveDate` INT, `InactiveNote` INT, `DeceasedDesc` INT, `DeceasedDate` INT, `DeceasedNote` INT, `AutopsyDesc` INT, `AutopsyDate` INT, `AutopsyNote` INT, `ClosedDesc` INT, `ClosedDate` INT, `ClosedNote` INT, `EnrollmentNotes` INT, `modified` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_instruments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_instruments` (`InstrID` INT, `VID` INT, `ProjName` INT, `PIDN_Instrument` INT, `InstrType` INT, `InstrVer` INT, `DCDate` INT, `DCBy` INT, `DCStatus` INT, `DCNotes` INT, `ResearchStatus` INT, `QualityIssue` INT, `QualityIssue2` INT, `QualityIssue3` INT, `QualityNotes` INT, `DEDate` INT, `DEBy` INT, `DEStatus` INT, `DENotes` INT, `DVDate` INT, `DVBy` INT, `DVStatus` INT, `DVNotes` INT, `latestflag` INT, `FieldStatus` INT, `AgeAtDC` INT, `modified` INT, `summary` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lq_view_visit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lq_view_visit` (`VID` INT, `PIDN_Visit` INT, `ProjName` INT, `VLocation` INT, `VType` INT, `VWith` INT, `VDate` INT, `VTime` INT, `VStatus` INT, `VNotes` INT, `FUMonth` INT, `FUYear` INT, `FUNote` INT, `WList` INT, `WListNote` INT, `WListDate` INT, `VShortDesc` INT, `AgeAtVisit` INT, `modified` INT);

-- -----------------------------------------------------
-- procedure lq_after_set_linkdata
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_after_set_linkdata`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_after_set_linkdata`(user_name varchar(50), host_name varchar(25),method VARCHAR(25))
BEGIN
IF method = 'VISIT' THEN
  UPDATE temp_linkdata1, visit 
  SET temp_linkdata1.pidn = visit.PIDN, temp_linkdata1.link_date = visit.VDATE, temp_linkdata1.link_type = method 
  WHERE temp_linkdata1.link_id = visit.VID;
ELSEIF method = 'INSTRUMENT' THEN
  UPDATE temp_linkdata1, instrumenttracking 
  SET temp_linkdata1.pidn = instrumenttracking.PIDN, temp_linkdata1.link_date = instrumenttracking.DCDATE, temp_linkdata1.link_type = method 
  WHERE temp_linkdata1.link_id = instrumenttracking.InstrID;
ELSE
  UPDATE temp_linkdata1 SET link_type = method;
END IF;
#remove duplicates
CREATE TEMPORARY TABLE temp_linkdata (
  pidn INTEGER NOT NULL,
  link_date DATE NOT NULL,
  link_id INTEGER NOT NULL,
  link_type VARCHAR(50) NOT NULL);

IF method = 'PIDN_DATE' THEN
  #the link_id is an arbitrary incrementing integer, so we exclude it from determining if the row is unique  
  INSERT INTO temp_linkdata(pidn,link_date,link_id,link_type) 
  SELECT pidn,link_date, min(link_id), link_type FROM temp_linkdata1 GROUP BY pidn,link_date,link_type;
ELSE
  #the link_id is driving uniqueness and should be included in the grouping
  INSERT INTO temp_linkdata(pidn,link_date,link_id,link_type) 
  SELECT pidn,link_date, link_id, link_type FROM temp_linkdata1 GROUP BY pidn,link_date,link_id,link_type;
END IF;

ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);

END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_after_set_pidns
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_after_set_pidns`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_after_set_pidns`(user_name varchar(50), host_name varchar(25))
BEGIN
CREATE TEMPORARY TABLE temp_pidn AS SELECT pidn FROM temp_pidn1 GROUP BY pidn;
END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_clear_linkdata
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_clear_linkdata`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_clear_linkdata`(user_name varchar(50), host_name varchar(25))
BEGIN

DROP TABLE IF EXISTS temp_linkdata;
DROP TABLE IF EXISTS temp_linkdata1;
END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_clear_pidns
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_clear_pidns`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_clear_pidns`(user_name varchar(50), host_name varchar(25))
BEGIN

DROP TABLE IF EXISTS temp_pidn;
DROP TABLE IF EXISTS temp_pidn1;

END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_all_pidns
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_get_all_pidns`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_get_all_pidns`(user_name varchar(50), host_name varchar(25))
BEGIN
SELECT pidn from patient order by pidn;
END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_assessment_instruments
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_get_assessment_instruments`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_get_assessment_instruments`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN

CALL lq_audit_event(user_name,host_name,'crms.assessment.instruments',query_type);

	
IF query_type = 'Simple' THEN
	SELECT p.pidn, i.* FROM lq_view_instruments i 
		INNER JOIN temp_pidn p ON (p.PIDN = i.PIDN_Instrument) 
      ORDER BY p.pidn, i.DCDate,i.InstrType;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, i.*  FROM lq_view_instruments i 
		RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = i.PIDN_Instrument) 
      ORDER BY p.pidn, i.DCDate,i.InstrType;

END IF;
END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_enrollment_status
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_get_enrollment_status`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_get_enrollment_status`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN

CALL lq_audit_event(user_name,host_name,'crms.enrollment.status',query_type);

IF query_type = 'Simple' THEN
	SELECT p.pidn, e.*  FROM lq_view_enrollment e 
		INNER JOIN temp_pidn p ON (p.PIDN = e.PIDN_Enrollment) 
      ORDER BY p.pidn, e.latestDate;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, e.* FROM lq_view_enrollment e 
		RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = e.PIDN_Enrollment) 
      ORDER BY p.pidn, e.latestDate;

END IF;
END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_linkdata
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_get_linkdata`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_get_linkdata`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
    SQL SECURITY INVOKER
BEGIN
IF query_type = 'VISIT' THEN
	SELECT l.*, '---->' as `Visit Details`, v.* from temp_linkdata l inner join visit v on l.link_id=v.VID
	ORDER BY l.pidn, l.link_date,l.link_id;
ELSEIF query_type = 'INSTRUMENT' THEN 
	SELECT l.*, '---->' as `Instrument Details`, i.* from temp_linkdata l inner join instrumenttracking i on l.link_id=i.InstrID
	ORDER BY l.pidn, l.link_date,l.link_id;
ELSE
   SELECT * from temp_linkdata l  

	ORDER BY l.pidn, l.link_date,l.link_id;
END IF;

END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_patient_demographics
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_get_patient_demographics`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_get_patient_demographics`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN

CALL lq_audit_event(user_name,host_name,'crms.patient.demographics',query_type);
	
IF query_type = 'Simple' THEN
	SELECT p.pidn, d.*  FROM lq_view_demographics d 
		INNER JOIN temp_pidn p ON (p.PIDN = d.PIDN_Demographics) 
      ORDER BY p.pidn;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, d.*   FROM lq_view_demographics d  
		 RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = d.PIDN_Demographics) 
      ORDER BY p.pidn;
ELSEIF query_type IN ('SecondaryAll','SecondaryClosest') THEN
 	 SELECT l.pidn, l.link_date,l.link_id,d.*  
	 FROM temp_linkdata l INNER JOIN lq_view_demographics d ON (d.PIDN_Demographics=l.PIDN);
END IF;
END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_get_scheduling_visits
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_get_scheduling_visits`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_get_scheduling_visits`(user_name varchar(50), host_name varchar(25),query_type varchar(25), query_subtype VARCHAR(25), query_days INTEGER)
BEGIN

CALL lq_audit_event(user_name,host_name,'crms.scheduling.visits',query_type);

	
IF query_type = 'Simple' THEN
	SELECT p.pidn, v.*  FROM lq_view_visit v 
		INNER JOIN temp_pidn p ON (p.PIDN = v.PIDN_visit) 
      ORDER BY p.pidn, v.vdate, v.vtype;
ELSEIF query_type = 'SimpleAllPatients' THEN
	SELECT p.pidn, v.*  FROM lq_view_visit v  
		 RIGHT OUTER JOIN temp_pidn p ON (p.PIDN = v.PIDN_visit) 
      ORDER BY p.pidn,v.vdate, v.vtype;

END IF;
END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_set_linkdata
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_set_linkdata`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_set_linkdata`(user_name varchar(50), host_name varchar(25))
BEGIN


DROP TABLE IF EXISTS temp_linkdata1;
DROP TABLE IF EXISTS temp_linkdata;

CREATE TEMPORARY TABLE temp_linkdata1(
    pidn INTEGER NOT NULL,
    link_date DATE NOT NULL,
    link_id INTEGER NOT NULL,
    link_type varchar(25) DEFAULT NULL);
END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_set_linkdata_row
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_set_linkdata_row`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_set_linkdata_row`(user_name varchar(50), host_name varchar(25),pidn integer,link_date date, link_id integer)
BEGIN

INSERT INTO `temp_linkdata1` (`pidn`,`link_date`,`link_id`) VALUES(pidn,link_date,link_id);
END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_set_pidns
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_set_pidns`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_set_pidns`(user_name varchar(50), host_name varchar(25))
BEGIN

DROP TABLE IF EXISTS temp_pidn1;
DROP TABLE IF EXISTS temp_pidn;

CREATE TEMPORARY TABLE temp_pidn1(
    pidn INTEGER NOT NULL);
END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_set_pidns_row
-- -----------------------------------------------------


DROP procedure IF EXISTS `lq_set_pidns_row`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_set_pidns_row`(user_name varchar(50), host_name varchar(25),pidn integer)
BEGIN

INSERT INTO `temp_pidn1` (`pidn`) values (pidn);
END$$

DELIMITER ;
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

-- -----------------------------------------------------
-- View `lq_view_demographics`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_demographics` ;
DROP TABLE IF EXISTS `lq_view_demographics`;

CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lq_view_demographics` AS select `patient`.`PIDN` AS `PIDN_demographics`,`patient`.`DOB` AS `DOB`,`patient`.`AGE` AS `AGE`,`patient`.`Gender` AS `Gender`,`patient`.`Hand` AS `Hand`,`patient`.`Deceased` AS `Deceased`,`patient`.`DOD` AS `DOD`,`patient`.`PrimaryLanguage` AS `PrimaryLanguage`,`patient`.`TestingLanguage` AS `TestingLanguage`,`patient`.`TransNeeded` AS `TransNeeded`,`patient`.`TransLanguage` AS `TransLanguage` from `patient` where (`patient`.`PIDN` > 0);

-- -----------------------------------------------------
-- View `lq_view_enrollment`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_enrollment` ;
DROP TABLE IF EXISTS `lq_view_enrollment`;

CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lq_view_enrollment` AS select `enrollmentstatus`.`EnrollStatID` AS `EnrollStatID`,`enrollmentstatus`.`PIDN` AS `PIDN_Enrollment`,`enrollmentstatus`.`ProjName` AS `ProjName`,`enrollmentstatus`.`SubjectStudyID` AS `SubjectStudyID`,`enrollmentstatus`.`ReferralSource` AS `ReferralSource`,`enrollmentstatus`.`LatestDesc` AS `LatestDesc`,`enrollmentstatus`.`LatestDate` AS `LatestDate`,`enrollmentstatus`.`LatestNote` AS `LatestNote`,`enrollmentstatus`.`ReferredDesc` AS `ReferredDesc`,`enrollmentstatus`.`ReferredDate` AS `ReferredDate`,`enrollmentstatus`.`ReferredNote` AS `ReferredNote`,`enrollmentstatus`.`DeferredDesc` AS `DeferredDesc`,`enrollmentstatus`.`DeferredDate` AS `DeferredDate`,`enrollmentstatus`.`DeferredNote` AS `DeferredNote`,`enrollmentstatus`.`EligibleDesc` AS `EligibleDesc`,`enrollmentstatus`.`EligibleDate` AS `EligibleDate`,`enrollmentstatus`.`EligibleNote` AS `EligibleNote`,`enrollmentstatus`.`IneligibleDesc` AS `IneligibleDesc`,`enrollmentstatus`.`IneligibleDate` AS `IneligibleDate`,`enrollmentstatus`.`IneligibleNote` AS `IneligibleNote`,`enrollmentstatus`.`DeclinedDesc` AS `DeclinedDesc`,`enrollmentstatus`.`DeclinedDate` AS `DeclinedDate`,`enrollmentstatus`.`DeclinedNote` AS `DeclinedNote`,`enrollmentstatus`.`EnrolledDesc` AS `EnrolledDesc`,`enrollmentstatus`.`EnrolledDate` AS `EnrolledDate`,`enrollmentstatus`.`EnrolledNote` AS `EnrolledNote`,`enrollmentstatus`.`ExcludedDesc` AS `ExcludedDesc`,`enrollmentstatus`.`ExcludedDate` AS `ExcludedDate`,`enrollmentstatus`.`ExcludedNote` AS `ExcludedNote`,`enrollmentstatus`.`WithdrewDesc` AS `WithdrewDesc`,`enrollmentstatus`.`WithdrewDate` AS `WithdrewDate`,`enrollmentstatus`.`WithdrewNote` AS `WithdrewNote`,`enrollmentstatus`.`InactiveDesc` AS `InactiveDesc`,`enrollmentstatus`.`InactiveDate` AS `InactiveDate`,`enrollmentstatus`.`InactiveNote` AS `InactiveNote`,`enrollmentstatus`.`DeceasedDesc` AS `DeceasedDesc`,`enrollmentstatus`.`DeceasedDate` AS `DeceasedDate`,`enrollmentstatus`.`DeceasedNote` AS `DeceasedNote`,`enrollmentstatus`.`AutopsyDesc` AS `AutopsyDesc`,`enrollmentstatus`.`AutopsyDate` AS `AutopsyDate`,`enrollmentstatus`.`AutopsyNote` AS `AutopsyNote`,`enrollmentstatus`.`ClosedDesc` AS `ClosedDesc`,`enrollmentstatus`.`ClosedDate` AS `ClosedDate`,`enrollmentstatus`.`ClosedNote` AS `ClosedNote`,`enrollmentstatus`.`EnrollmentNotes` AS `EnrollmentNotes`,`enrollmentstatus`.`modified` AS `modified` from `enrollmentstatus` where (`enrollmentstatus`.`EnrollStatID` > 0);

-- -----------------------------------------------------
-- View `lq_view_instruments`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_instruments` ;
DROP TABLE IF EXISTS `lq_view_instruments`;

CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lq_view_instruments` AS select `i`.`InstrID` AS `InstrID`,`i`.`VID` AS `VID`,`i`.`ProjName` AS `ProjName`,`i`.`PIDN` AS `PIDN_Instrument`,`i`.`InstrType` AS `InstrType`,`i`.`InstrVer` AS `InstrVer`,`i`.`DCDate` AS `DCDate`,`i`.`DCBy` AS `DCBy`,`i`.`DCStatus` AS `DCStatus`,`i`.`DCNotes` AS `DCNotes`,`i`.`ResearchStatus` AS `ResearchStatus`,`i`.`QualityIssue` AS `QualityIssue`,`i`.`QualityIssue2` AS `QualityIssue2`,`i`.`QualityIssue3` AS `QualityIssue3`,`i`.`QualityNotes` AS `QualityNotes`,`i`.`DEDate` AS `DEDate`,`i`.`DEBy` AS `DEBy`,`i`.`DEStatus` AS `DEStatus`,`i`.`DENotes` AS `DENotes`,`i`.`DVDate` AS `DVDate`,`i`.`DVBy` AS `DVBy`,`i`.`DVStatus` AS `DVStatus`,`i`.`DVNotes` AS `DVNotes`,`i`.`latestflag` AS `latestflag`,`i`.`FieldStatus` AS `FieldStatus`,`i`.`AgeAtDC` AS `AgeAtDC`,`i`.`modified` AS `modified`,`s`.`Summary` AS `summary` from (`instrumenttracking` `i` join `instrumentsummary` `s` on((`i`.`InstrID` = `s`.`InstrID`))) where (`i`.`InstrID` > 0);

-- -----------------------------------------------------
-- View `lq_view_visit`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lq_view_visit` ;
DROP TABLE IF EXISTS `lq_view_visit`;

CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lq_view_visit` AS select `visit`.`VID` AS `VID`,`visit`.`PIDN` AS `PIDN_Visit`,`visit`.`ProjName` AS `ProjName`,`visit`.`VLocation` AS `VLocation`,`visit`.`VType` AS `VType`,`visit`.`VWith` AS `VWith`,`visit`.`VDate` AS `VDate`,`visit`.`VTime` AS `VTime`,`visit`.`VStatus` AS `VStatus`,`visit`.`VNotes` AS `VNotes`,`visit`.`FUMonth` AS `FUMonth`,`visit`.`FUYear` AS `FUYear`,`visit`.`FUNote` AS `FUNote`,`visit`.`WList` AS `WList`,`visit`.`WListNote` AS `WListNote`,`visit`.`WListDate` AS `WListDate`,`visit`.`VShortDesc` AS `VShortDesc`,`visit`.`AgeAtVisit` AS `AgeAtVisit`,`visit`.`modified` AS `modified` from `visit` where (`visit`.`VID` > 0);


insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-crms-model','3.0.4',NOW(),3,0,4,1);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
