SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `lava_crms` DEFAULT CHARACTER SET latin1 ;
USE `lava_crms` ;

-- -----------------------------------------------------
-- Table `lava_crms`.`patient`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`patient` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`patient` (
  `PIDN` INT(10) NOT NULL AUTO_INCREMENT ,
  `LName` VARCHAR(25) NOT NULL ,
  `MInitial` CHAR(1) NULL DEFAULT NULL ,
  `FName` VARCHAR(25) NOT NULL ,
  `Suffix` VARCHAR(15) NULL DEFAULT NULL ,
  `Degree` VARCHAR(15) NULL DEFAULT NULL ,
  `DOB` DATETIME NULL DEFAULT NULL ,
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
-- Table `lava_crms`.`caregiver`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`caregiver` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`caregiver` (
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
    REFERENCES `lava_crms`.`patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 13131
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`contactinfo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`contactinfo` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`contactinfo` (
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
    REFERENCES `lava_crms`.`patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `contactinfo__CareID`
    FOREIGN KEY (`CareID` )
    REFERENCES `lava_crms`.`caregiver` (`CareID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 10011
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`projectunit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`projectunit` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`projectunit` (
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
-- Table `lava_crms`.`contactlog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`contactlog` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`contactlog` (
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
  INDEX `contactLog__authfilter` (`PIDN` ASC, `ProjName` ASC, `LogID` ASC) ,
  CONSTRAINT `contactlog__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `lava_crms`.`patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `contactlog__ProjName`
    FOREIGN KEY (`ProjName` )
    REFERENCES `lava_crms`.`projectunit` (`ProjUnitDesc` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`crmsauthrole`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`crmsauthrole` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`crmsauthrole` (
  `RoleID` INT(10) NOT NULL ,
  `PatientAccess` SMALLINT(5) NOT NULL DEFAULT '1' ,
  `PhiAccess` SMALLINT(5) NOT NULL DEFAULT '1' ,
  `GhiAccess` SMALLINT(5) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (`RoleID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`crmsauthuser`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`crmsauthuser` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`crmsauthuser` (
  `UID` INT(10) NOT NULL ,
  PRIMARY KEY (`UID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`crmsauthuserrole`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`crmsauthuserrole` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`crmsauthuserrole` (
  `URID` INT(10) NOT NULL ,
  `Project` VARCHAR(25) NULL DEFAULT NULL ,
  `Unit` VARCHAR(25) NULL DEFAULT NULL ,
  PRIMARY KEY (`URID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`doctor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`doctor` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`doctor` (
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
-- Table `lava_crms`.`enrollmentstatus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`enrollmentstatus` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`enrollmentstatus` (
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
  INDEX `enrollmentstatus__authfilter` (`PIDN` ASC, `ProjName` ASC, `EnrollStatID` ASC) ,
  CONSTRAINT `enrollmentstatus__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `lava_crms`.`patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `enrollmentstatus__ProjName`
    FOREIGN KEY (`ProjName` )
    REFERENCES `lava_crms`.`projectunit` (`ProjUnitDesc` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 16923
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`instrument`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`instrument` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`instrument` (
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
-- Table `lava_crms`.`visit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`visit` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`visit` (
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
  INDEX `visit__PIDN_PROJNAME` (`PIDN` ASC, `ProjName` ASC) ,
  INDEX `visit__authfilter` (`PIDN` ASC, `ProjName` ASC, `VID` ASC) ,
  INDEX `visit__date` (`VID` ASC, `VDate` ASC, `VTime` ASC) ,
  CONSTRAINT `visit__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `lava_crms`.`patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `visit__ProjName`
    FOREIGN KEY (`ProjName` )
    REFERENCES `lava_crms`.`projectunit` (`ProjUnitDesc` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `visit__PIDN_PROJNAME`
    FOREIGN KEY (`PIDN` , `ProjName` )
    REFERENCES `lava_crms`.`enrollmentstatus` (`PIDN` , `ProjName` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 42666
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`instrumenttracking`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`instrumenttracking` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`instrumenttracking` (
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
  INDEX `instrumenttracking__PIDN` (`PIDN` ASC) ,
  INDEX `instrumenttracking__authfilter` (`PIDN` ASC, `ProjName` ASC, `InstrID` ASC) ,
  CONSTRAINT `instrumenttracking__InstrType`
    FOREIGN KEY (`InstrType` )
    REFERENCES `lava_crms`.`instrument` (`InstrName` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `instrumenttracking__VID`
    FOREIGN KEY (`VID` )
    REFERENCES `lava_crms`.`visit` (`VID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `instrumenttracking__ProjName`
    FOREIGN KEY (`ProjName` )
    REFERENCES `lava_crms`.`projectunit` (`ProjUnitDesc` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `insttumenttracking__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `lava_crms`.`patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 130203
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`instrumentnotes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`instrumentnotes` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`instrumentnotes` (
  `InstrID` INT(10) NOT NULL ,
  `Section` VARCHAR(50) NOT NULL ,
  `Note` VARCHAR(2000) NULL DEFAULT NULL ,
  INDEX `instrumentnotes__instrID` (`InstrID` ASC) ,
  CONSTRAINT `instrumentnotes__instrID`
    FOREIGN KEY (`InstrID` )
    REFERENCES `lava_crms`.`instrumenttracking` (`InstrID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`instrumentsummary`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`instrumentsummary` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`instrumentsummary` (
  `InstrID` INT(10) NOT NULL ,
  `Summary` VARCHAR(500) NULL DEFAULT NULL ,
  PRIMARY KEY (`InstrID`) ,
  INDEX `instrumentsummary__InstrID` (`InstrID` ASC) ,
  CONSTRAINT `instrumentsummary__InstrID`
    FOREIGN KEY (`InstrID` )
    REFERENCES `lava_crms`.`instrumenttracking` (`InstrID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`patientdoctors`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`patientdoctors` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`patientdoctors` (
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
    REFERENCES `lava_crms`.`patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `patientdoctors__DocID`
    FOREIGN KEY (`DocID` )
    REFERENCES `lava_crms`.`doctor` (`DocID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 12164
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`tasks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`tasks` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`tasks` (
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
    REFERENCES `lava_crms`.`patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tasks__ProjName`
    FOREIGN KEY (`ProjName` )
    REFERENCES `lava_crms`.`projectunit` (`ProjUnitDesc` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 3021
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`uploadedfiles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`uploadedfiles` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`uploadedfiles` (
  `InstrID` INT(10) NOT NULL ,
  `FileName` VARCHAR(500) NULL DEFAULT NULL ,
  `FileContents` VARCHAR(16) NULL DEFAULT NULL ,
  PRIMARY KEY (`InstrID`) ,
  INDEX `uploadedfiles__InstrID` (`InstrID` ASC) ,
  CONSTRAINT `uploadedfiles__InstrID`
    FOREIGN KEY (`InstrID` )
    REFERENCES `lava_crms`.`instrumenttracking` (`InstrID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`patientconsent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`patientconsent` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`patientconsent` (
  `ConsentID` INT(10) NOT NULL AUTO_INCREMENT ,
  `PIDN` INT(10) NOT NULL ,
  `CareID` INT(10) NULL DEFAULT NULL ,
  `ProjName` VARCHAR(75) NOT NULL ,
  `ConsentType` VARCHAR(50) NOT NULL ,
  `HIPAA` TINYINT(4) NULL DEFAULT NULL ,
  `ConsentDate` TIMESTAMP NULL DEFAULT NULL ,
  `ExpirationDate` DATETIME NULL DEFAULT NULL ,
  `WithdrawlDate` DATETIME NULL DEFAULT NULL ,
  `Note` VARCHAR(100) NULL DEFAULT NULL ,
  `CapacityReviewBy` VARCHAR(25) NULL DEFAULT NULL ,
  `ConsentRevision` VARCHAR(10) NULL DEFAULT NULL ,
  `ConsentDeclined` VARCHAR(10) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`ConsentID`) ,
  INDEX `patientconsent__PIDN` (`PIDN` ASC) ,
  INDEX `patientconsent__ProjName` (`ProjName` ASC) ,
  CONSTRAINT `patientconsent__PIDN`
    FOREIGN KEY (`PIDN` )
    REFERENCES `lava_crms`.`patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `patientconsent__ProjName`
    FOREIGN KEY (`ProjName` )
    REFERENCES `lava_crms`.`projectunit` (`ProjUnitDesc` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`crms_file`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`crms_file` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`crms_file` (
  `id` INT NOT NULL ,
  `pidn` INT NULL COMMENT '	' ,
  `enroll_stat_id` INT NULL COMMENT '	' ,
  `vid` INT NULL COMMENT '		' ,
  `instr_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_crms_file__pidn` (`pidn` ASC) ,
  INDEX `fk_crms_file__enroll_stat_id` (`enroll_stat_id` ASC) ,
  INDEX `fk_crms_file__vid` (`vid` ASC) ,
  INDEX `fk_crms_file__instr_id` (`instr_id` ASC) ,
  CONSTRAINT `fk_crms_file__pidn`
    FOREIGN KEY (`pidn` )
    REFERENCES `lava_crms`.`patient` (`PIDN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_crms_file__enroll_stat_id`
    FOREIGN KEY (`enroll_stat_id` )
    REFERENCES `lava_crms`.`enrollmentstatus` (`EnrollStatID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_crms_file__vid`
    FOREIGN KEY (`vid` )
    REFERENCES `lava_crms`.`visit` (`VID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_crms_file__instr_id`
    FOREIGN KEY (`instr_id` )
    REFERENCES `lava_crms`.`instrumenttracking` (`InstrID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `lava_crms`.`cbt_tracking`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`cbt_tracking` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`cbt_tracking` (
  `instr_id` INT(11) NOT NULL ,
  `filename` VARCHAR(500) NULL DEFAULT NULL ,
  `task` VARCHAR(25) NULL DEFAULT NULL ,
  `version` VARCHAR(10) NULL DEFAULT NULL ,
  `version_date` DATE NULL DEFAULT NULL ,
  `language` VARCHAR(10) NULL DEFAULT NULL ,
  `form` VARCHAR(10) NULL DEFAULT NULL ,
  `adult_child` VARCHAR(10) NULL DEFAULT NULL ,
  PRIMARY KEY (`instr_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_node_config`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_node_config` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_node_config` (
  `node_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `parent_id` INT(11) NULL DEFAULT NULL ,
  `list_order` INT(11) NULL DEFAULT NULL ,
  `ProjName` VARCHAR(75) NOT NULL ,
  `label` VARCHAR(25) NOT NULL ,
  `summary` VARCHAR(100) NULL DEFAULT NULL ,
  `notes` VARCHAR(255) NULL DEFAULT NULL ,
  `eff_date` DATE NULL DEFAULT NULL ,
  `exp_date` DATE NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP ,
  PRIMARY KEY (`node_id`) ,
  INDEX `fk_prot_node_config__parent_id` (`parent_id` ASC) ,
  CONSTRAINT `fk_prot_node_config__parent_id`
    FOREIGN KEY (`parent_id` )
    REFERENCES `lava_crms`.`prot_node_config` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_config_option`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_config_option` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_config_option` (
  `option_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `parent_id` INT(11) NOT NULL ,
  `ProjName` VARCHAR(75) NOT NULL ,
  `default_option` TINYINT(1) NULL DEFAULT NULL ,
  `label` VARCHAR(25) NOT NULL ,
  `notes` VARCHAR(255) NULL DEFAULT NULL ,
  `eff_date` DATE NULL DEFAULT NULL ,
  `exp_date` DATE NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP ,
  PRIMARY KEY (`option_id`) ,
  INDEX `fk_prot_config_option__parent_id` (`parent_id` ASC) ,
  CONSTRAINT `fk_prot_config_option__parent_id`
    FOREIGN KEY (`parent_id` )
    REFERENCES `lava_crms`.`prot_node_config` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_node`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_node` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_node` (
  `node_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `parent_id` INT(11) NULL DEFAULT NULL ,
  `config_node_id` INT(11) NOT NULL ,
  `list_order` INT(11) NULL DEFAULT NULL ,
  `PIDN` INT(11) NOT NULL ,
  `ProjName` VARCHAR(75) NOT NULL ,
  `strategy` SMALLINT(6) NOT NULL ,
  `curr_status` VARCHAR(25) NULL DEFAULT NULL ,
  `curr_reason` VARCHAR(25) NULL DEFAULT NULL ,
  `curr_note` VARCHAR(100) NULL DEFAULT NULL ,
  `comp_status` VARCHAR(25) NULL DEFAULT NULL ,
  `comp_reason` VARCHAR(25) NULL DEFAULT NULL ,
  `comp_note` VARCHAR(100) NULL DEFAULT NULL ,
  `comp_by` VARCHAR(25) NULL DEFAULT NULL ,
  `comp_date` DATE NULL DEFAULT NULL ,
  `sched_win_status` VARCHAR(25) NULL DEFAULT NULL ,
  `sched_win_reason` VARCHAR(25) NULL DEFAULT NULL ,
  `sched_win_note` VARCHAR(100) NULL DEFAULT NULL ,
  `collect_win_status` VARCHAR(25) NULL DEFAULT NULL ,
  `collect_win_reason` VARCHAR(25) NULL DEFAULT NULL ,
  `collect_win_note` VARCHAR(100) NULL DEFAULT NULL ,
  `assign_desc` VARCHAR(100) NULL DEFAULT NULL ,
  `notes` VARCHAR(255) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP ,
  PRIMARY KEY (`node_id`) ,
  INDEX `fk_prot_node__parent_id` (`parent_id` ASC) ,
  INDEX `fk_prot_node__config_node_id` (`config_node_id` ASC) ,
  CONSTRAINT `fk_prot_node__parent_id`
    FOREIGN KEY (`parent_id` )
    REFERENCES `lava_crms`.`prot_node` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_node__config_node_id`
    FOREIGN KEY (`config_node_id` )
    REFERENCES `lava_crms`.`prot_node_config` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_instr`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_instr` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_instr` (
  `node_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `InstrID` INT(11) NULL DEFAULT NULL ,
  `collect_anchor_date` DATE NULL DEFAULT NULL ,
  `collect_win_start` DATE NULL DEFAULT NULL ,
  `collect_win_end` DATE NULL DEFAULT NULL ,
  PRIMARY KEY (`node_id`) ,
  INDEX `fk_prot_instr__node_id` (`node_id` ASC) ,
  INDEX `fk_prot_instr__InstrID` (`InstrID` ASC) ,
  CONSTRAINT `fk_prot_instr__node_id`
    FOREIGN KEY (`node_id` )
    REFERENCES `lava_crms`.`prot_node` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_instr__InstrID`
    FOREIGN KEY (`InstrID` )
    REFERENCES `lava_crms`.`instrumenttracking` (`InstrID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_visit_config`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_visit_config` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_visit_config` (
  `node_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `optional` TINYINT(1) NULL DEFAULT NULL ,
  `category` VARCHAR(25) NULL DEFAULT NULL ,
  PRIMARY KEY (`node_id`) ,
  INDEX `fk_prot_visit_config__node_id` (`node_id` ASC) ,
  CONSTRAINT `fk_prot_visit_config__node_id`
    FOREIGN KEY (`node_id` )
    REFERENCES `lava_crms`.`prot_node_config` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_instr_config`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_instr_config` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_instr_config` (
  `node_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `optional` TINYINT(1) NULL DEFAULT NULL ,
  `category` VARCHAR(25) NULL DEFAULT NULL ,
  `collect_win_def` TINYINT(1) NULL DEFAULT NULL ,
  `collect_win_prot_visit_conf_id` INT(11) NULL DEFAULT NULL ,
  `collect_win_size` SMALLINT(6) NULL DEFAULT NULL ,
  `collect_win_offset` SMALLINT(6) NULL DEFAULT NULL ,
  `default_comp_status` VARCHAR(25) NULL DEFAULT NULL ,
  `default_comp_reason` VARCHAR(25) NULL DEFAULT NULL ,
  `default_comp_note` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`node_id`) ,
  INDEX `fk_prot_instr_config__node_id` (`node_id` ASC) ,
  INDEX `fk_prot_instr_config__collect_win_prot_visit_conf_id` (`collect_win_prot_visit_conf_id` ASC) ,
  CONSTRAINT `fk_prot_instr_config__node_id`
    FOREIGN KEY (`node_id` )
    REFERENCES `lava_crms`.`prot_node_config` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_instr_config__collect_win_prot_visit_conf_id`
    FOREIGN KEY (`collect_win_prot_visit_conf_id` )
    REFERENCES `lava_crms`.`prot_visit_config` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_instr_config_option`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_instr_config_option` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_instr_config_option` (
  `option_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `instr_type` VARCHAR(25) NULL DEFAULT NULL ,
  PRIMARY KEY (`option_id`) ,
  INDEX `fk_prot_instr_config_option__option_id` (`option_id` ASC) ,
  CONSTRAINT `fk_prot_instr_config_option__option_id`
    FOREIGN KEY (`option_id` )
    REFERENCES `lava_crms`.`prot_config_option` (`option_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_protocol`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_protocol` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_protocol` (
  `node_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `assigned_date` DATE NULL DEFAULT NULL ,
  `EnrollStatID` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`node_id`) ,
  INDEX `fk_prot_protocol__node_id` (`node_id` ASC) ,
  INDEX `fk_prot_protocol__EnrollStatID` (`EnrollStatID` ASC) ,
  CONSTRAINT `fk_prot_protocol__node_id`
    FOREIGN KEY (`node_id` )
    REFERENCES `lava_crms`.`prot_node` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_protocol__EnrollStatID`
    FOREIGN KEY (`EnrollStatID` )
    REFERENCES `lava_crms`.`enrollmentstatus` (`EnrollStatID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_protocol_config`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_protocol_config` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_protocol_config` (
  `node_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `category` VARCHAR(25) NULL DEFAULT NULL ,
  `first_prot_tp_conf_id` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`node_id`) ,
  INDEX `fk_prot_protocol_config__node_id` (`node_id` ASC) ,
  CONSTRAINT `fk_prot_protocol_config__node_id`
    FOREIGN KEY (`node_id` )
    REFERENCES `lava_crms`.`prot_node_config` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_tp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_tp` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_tp` (
  `node_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `sched_anchor_date` DATE NULL DEFAULT NULL ,
  `sched_win_start` DATE NULL DEFAULT NULL ,
  `sched_win_end` DATE NULL DEFAULT NULL ,
  `pri_prot_visit_id` INT(11) NULL DEFAULT NULL ,
  `collect_anchor_date` DATE NULL DEFAULT NULL ,
  `collect_win_start` DATE NULL DEFAULT NULL ,
  `collect_win_end` DATE NULL DEFAULT NULL ,
  PRIMARY KEY (`node_id`) ,
  INDEX `fk_prot_tp__node_id` (`node_id` ASC) ,
  CONSTRAINT `fk_prot_tp__node_id`
    FOREIGN KEY (`node_id` )
    REFERENCES `lava_crms`.`prot_node` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_tp_config`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_tp_config` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_tp_config` (
  `node_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `optional` TINYINT(1) NULL DEFAULT NULL ,
  `sched_win_rel_tp_id` INT(11) NULL DEFAULT NULL ,
  `sched_win_rel_amt` SMALLINT(6) NULL DEFAULT NULL ,
  `sched_win_rel_units` SMALLINT(6) NULL DEFAULT NULL ,
  `sched_win_rel_mode` SMALLINT(6) NULL DEFAULT NULL ,
  `sched_win_days_from_start` SMALLINT(6) NULL DEFAULT NULL ,
  `sched_win_size` SMALLINT(6) NULL DEFAULT NULL ,
  `sched_win_offset` SMALLINT(6) NULL DEFAULT NULL ,
  `duration` SMALLINT(6) NULL DEFAULT NULL ,
  `sched_auto` TINYINT(1) NULL DEFAULT NULL ,
  `pri_prot_visit_conf_id` INT(11) NULL DEFAULT NULL ,
  `collect_win_def` TINYINT(1) NULL DEFAULT NULL ,
  `collect_win_size` SMALLINT(6) NULL DEFAULT NULL ,
  `collect_win_offset` SMALLINT(6) NULL DEFAULT NULL ,
  `repeating` TINYINT(1) NULL DEFAULT NULL ,
  `rpt_interval` SMALLINT(6) NULL DEFAULT NULL ,
  `rpt_init_num` SMALLINT(6) NULL DEFAULT NULL ,
  `rpt_create_auto` TINYINT(1) NULL DEFAULT NULL ,
  PRIMARY KEY (`node_id`) ,
  INDEX `fk_prot_tp_config__node_id` (`node_id` ASC) ,
  INDEX `fk_prot_tp_config__sched_win_rel_tp_id` (`sched_win_rel_tp_id` ASC) ,
  CONSTRAINT `fk_prot_tp_config__node_id`
    FOREIGN KEY (`node_id` )
    REFERENCES `lava_crms`.`prot_node_config` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_tp_config__sched_win_rel_tp_id`
    FOREIGN KEY (`sched_win_rel_tp_id` )
    REFERENCES `lava_crms`.`prot_tp_config` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_visit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_visit` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_visit` (
  `node_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `VID` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`node_id`) ,
  INDEX `fk_prot_visit__node_id` (`node_id` ASC) ,
  INDEX `fk_prot_visit__VID` (`VID` ASC) ,
  CONSTRAINT `fk_prot_visit__node_id`
    FOREIGN KEY (`node_id` )
    REFERENCES `lava_crms`.`prot_node` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_visit__VID`
    FOREIGN KEY (`VID` )
    REFERENCES `lava_crms`.`visit` (`VID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lava_crms`.`prot_visit_config_option`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lava_crms`.`prot_visit_config_option` ;

CREATE  TABLE IF NOT EXISTS `lava_crms`.`prot_visit_config_option` (
  `option_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `ProjName` VARCHAR(75) NULL DEFAULT NULL ,
  `visit_type` VARCHAR(25) NULL DEFAULT NULL ,
  PRIMARY KEY (`option_id`) ,
  INDEX `fk_prot_visit_config_option__option_id` (`option_id` ASC) ,
  CONSTRAINT `fk_prot_visit_config_option__option_id`
    FOREIGN KEY (`option_id` )
    REFERENCES `lava_crms`.`prot_config_option` (`option_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Placeholder table for view `lava_crms`.`vwrptprojectpatientstatus`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_crms`.`vwrptprojectpatientstatus` (`PIDN` INT, `FullNameRev` INT, `AGE` INT, `Gender` INT, `ProjName` INT, `StatusDate` INT, `Status` INT, `StatusNote` INT, `StatusOrder` INT, `ProjUnitDesc` INT, `Project` INT, `Unit` INT, `UnitOrder` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lava_crms`.`vwrptprojectvisitlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_crms`.`vwrptprojectvisitlist` (`PIDN` INT, `FullNameRev` INT, `TransLanguage` INT, `Gender` INT, `AGE` INT, `VLocation` INT, `VType` INT, `VWith` INT, `VDate` INT, `VStatus` INT, `ProjName` INT, `VNotes` INT, `VDateNoTime` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lava_crms`.`lq_view_demographics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_crms`.`lq_view_demographics` (`PIDN_demographics` INT, `DOB` INT, `AGE` INT, `Gender` INT, `Hand` INT, `Deceased` INT, `DOD` INT, `PrimaryLanguage` INT, `TestingLanguage` INT, `TransNeeded` INT, `TransLanguage` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lava_crms`.`lq_view_enrollment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_crms`.`lq_view_enrollment` (`EnrollStatID` INT, `PIDN_Enrollment` INT, `ProjName` INT, `SubjectStudyID` INT, `ReferralSource` INT, `LatestDesc` INT, `LatestDate` INT, `LatestNote` INT, `ReferredDesc` INT, `ReferredDate` INT, `ReferredNote` INT, `DeferredDesc` INT, `DeferredDate` INT, `DeferredNote` INT, `EligibleDesc` INT, `EligibleDate` INT, `EligibleNote` INT, `IneligibleDesc` INT, `IneligibleDate` INT, `IneligibleNote` INT, `DeclinedDesc` INT, `DeclinedDate` INT, `DeclinedNote` INT, `EnrolledDesc` INT, `EnrolledDate` INT, `EnrolledNote` INT, `ExcludedDesc` INT, `ExcludedDate` INT, `ExcludedNote` INT, `WithdrewDesc` INT, `WithdrewDate` INT, `WithdrewNote` INT, `InactiveDesc` INT, `InactiveDate` INT, `InactiveNote` INT, `DeceasedDesc` INT, `DeceasedDate` INT, `DeceasedNote` INT, `AutopsyDesc` INT, `AutopsyDate` INT, `AutopsyNote` INT, `ClosedDesc` INT, `ClosedDate` INT, `ClosedNote` INT, `EnrollmentNotes` INT, `modified` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lava_crms`.`lq_view_instruments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_crms`.`lq_view_instruments` (`InstrID` INT, `VID` INT, `ProjName` INT, `PIDN_Instrument` INT, `InstrType` INT, `InstrVer` INT, `DCDate` INT, `DCBy` INT, `DCStatus` INT, `DCNotes` INT, `ResearchStatus` INT, `QualityIssue` INT, `QualityIssue2` INT, `QualityIssue3` INT, `QualityNotes` INT, `DEDate` INT, `DEBy` INT, `DEStatus` INT, `DENotes` INT, `DVDate` INT, `DVBy` INT, `DVStatus` INT, `DVNotes` INT, `latestflag` INT, `FieldStatus` INT, `AgeAtDC` INT, `modified` INT, `summary` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lava_crms`.`lq_view_visit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_crms`.`lq_view_visit` (`VID` INT, `PIDN_Visit` INT, `ProjName` INT, `VLocation` INT, `VType` INT, `VWith` INT, `VDate` INT, `VTime` INT, `VStatus` INT, `VNotes` INT, `FUMonth` INT, `FUYear` INT, `FUNote` INT, `WList` INT, `WListNote` INT, `WListDate` INT, `VShortDesc` INT, `AgeAtVisit` INT, `modified` INT);

-- -----------------------------------------------------
-- Placeholder table for view `lava_crms`.`patient_age`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lava_crms`.`patient_age` (`PIDN` INT, `AGE` INT);

-- -----------------------------------------------------
-- procedure lq_after_set_linkdata
-- -----------------------------------------------------

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_after_set_linkdata`;

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

CREATE TEMPORARY TABLE temp_linkdata (
  pidn INTEGER NOT NULL,
  link_date DATE NOT NULL,
  link_id INTEGER NOT NULL,
  link_type VARCHAR(50) NOT NULL);

IF method = 'PIDN_DATE' THEN
  INSERT INTO temp_linkdata(pidn,link_date,link_id,link_type) 
  SELECT pidn,link_date, min(link_id), link_type FROM temp_linkdata1 GROUP BY pidn,link_date,link_type;
ELSE
  INSERT INTO temp_linkdata(pidn,link_date,link_id,link_type) 
  SELECT pidn,link_date, link_id, link_type FROM temp_linkdata1 GROUP BY pidn,link_date,link_id,link_type;
END IF;

ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);

END$$

DELIMITER ;
-- -----------------------------------------------------
-- procedure lq_after_set_pidns
-- -----------------------------------------------------

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_after_set_pidns`;

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

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_clear_linkdata`;

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

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_clear_pidns`;

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

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_get_all_pidns`;

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

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_get_assessment_instruments`;

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

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_get_enrollment_status`;

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

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_get_linkdata`;

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

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_get_patient_demographics`;

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

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_get_scheduling_visits`;

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

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_set_linkdata`;

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

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_set_linkdata_row`;

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

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_set_pidns`;

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

USE `lava_crms`;
DROP procedure IF EXISTS `lava_crms`.`lq_set_pidns_row`;

DELIMITER $$
USE `lava_crms`$$
CREATE  PROCEDURE `lq_set_pidns_row`(user_name varchar(50), host_name varchar(25),pidn integer)
BEGIN

INSERT INTO `temp_pidn1` (`pidn`) values (pidn);
END$$

DELIMITER ;
-- -----------------------------------------------------
-- View `lava_crms`.`vwrptprojectpatientstatus`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lava_crms`.`vwrptprojectpatientstatus` ;
DROP TABLE IF EXISTS `lava_crms`.`vwrptprojectpatientstatus`;
USE `lava_crms`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `vwrptprojectpatientstatus` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`pa`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `StatusDate`,`lps`.`LatestDesc` AS `Status`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`Project` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,_utf8'OVERALL' AS `Unit`,1 AS `UnitOrder` from ((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `patient_age` `pa` on ((`p`.`PIDN` = `pa`.`PIDN`)) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) union select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`pa`.`AGE` AS `AGE`,`p`.`Gender` AS `Gender`,`lps`.`ProjName` AS `ProjName`,`lps`.`LatestDate` AS `LatestDate`,`lps`.`LatestDesc` AS `LatestDesc`,`lps`.`LatestNote` AS `StatusNote`,(case `lps`.`LatestDesc` when _utf8'ACTIVE' then 0 when _utf8'FOLLOW-UP' then 1 when _utf8'CANCELED' then 5 when _utf8'CLOSED' then 6 when _utf8'INACTIVE' then 4 when _utf8'PRE-APPOINTMENT' then 2 when _utf8'PENDING' then 3 when _utf8'ENROLLED' then 7 when _utf8'DECEASED' then 8 when _utf8'DECEASED-PERFORMED' then 9 when _utf8'DECEASED-NOT PERFORMED' then 10 when _utf8'REFERRED' then 11 when _utf8'ELIGIBLE' then 12 when _utf8'INELIGIBLE' then 13 when _utf8'WITHDREW' then 14 when _utf8'EXCLUDED' then 15 when _utf8'DECLINED' then 16 else 17 end) AS `StatusOrder`,`pu`.`ProjUnitDesc` AS `ProjUnitDesc`,`pu`.`Project` AS `Project`,`pu`.`Unit` AS `Unit`,2 AS `UnitOrder` from ((`patient` `p` join `enrollmentstatus` `lps` on((`p`.`PIDN` = `lps`.`PIDN`))) join `patient_age` `pa` on ((`p`.`PIDN` = `pa`.`PIDN`)) join `projectunit` `pu` on((`lps`.`ProjName` = `pu`.`ProjUnitDesc`))) where (`pu`.`Unit` is not null);

-- -----------------------------------------------------
-- View `lava_crms`.`vwrptprojectvisitlist`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lava_crms`.`vwrptprojectvisitlist` ;
DROP TABLE IF EXISTS `lava_crms`.`vwrptprojectvisitlist`;
USE `lava_crms`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `vwrptprojectvisitlist` AS select `p`.`PIDN` AS `PIDN`,`p`.`FullNameRev` AS `FullNameRev`,`p`.`TransLanguage` AS `TransLanguage`,`p`.`Gender` AS `Gender`,`pa`.`AGE` AS `AGE`,`v`.`VLocation` AS `VLocation`,`v`.`VType` AS `VType`,`v`.`VWith` AS `VWith`,`v`.`VDate` AS `VDate`,`v`.`VStatus` AS `VStatus`,`v`.`ProjName` AS `ProjName`,`v`.`VNotes` AS `VNotes`,cast(`v`.`VDate` as date) AS `VDateNoTime` from (`patient` `p` join `visit` `v` on((`p`.`PIDN` = `v`.`PIDN`)) join `patient_age` `pa` on ((`p`.`PIDN` = `pa`.`PIDN`))) where (not((`v`.`VStatus` like _latin1'%CANC%')));

-- -----------------------------------------------------
-- View `lava_crms`.`lq_view_demographics`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lava_crms`.`lq_view_demographics` ;
DROP TABLE IF EXISTS `lava_crms`.`lq_view_demographics`;
USE `lava_crms`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lava_crms`.`lq_view_demographics` AS select `lava_crms`.`patient`.`PIDN` AS `PIDN_demographics`,`lava_crms`.`patient`.`DOB` AS `DOB`,`lava_crms`.`patient_age`.`AGE` AS `AGE`,
`lava_crms`.`patient`.`Gender` AS `Gender`,`lava_crms`.`patient`.`Hand` AS `Hand`,
`lava_crms`.`patient`.`Deceased` AS `Deceased`,`lava_crms`.`patient`.`DOD` AS `DOD`,
`lava_crms`.`patient`.`PrimaryLanguage` AS `PrimaryLanguage`,
`lava_crms`.`patient`.`TestingLanguage` AS `TestingLanguage`,
`lava_crms`.`patient`.`TransNeeded` AS `TransNeeded`,`lava_crms`.`patient`.`TransLanguage` AS `TransLanguage` 
from `lava_crms`.`patient` inner join `lava_crms`.`patient_age` on `lava_crms`.`patient`.`PIDN` = `lava_crms`.`patient_age`.`PIDN` where (`lava_crms`.`patient`.`PIDN` > 0);

-- -----------------------------------------------------
-- View `lava_crms`.`lq_view_enrollment`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lava_crms`.`lq_view_enrollment` ;
DROP TABLE IF EXISTS `lava_crms`.`lq_view_enrollment`;
USE `lava_crms`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lava_crms`.`lq_view_enrollment` AS select `lava_crms`.`enrollmentstatus`.`EnrollStatID` AS `EnrollStatID`,`lava_crms`.`enrollmentstatus`.`PIDN` AS `PIDN_Enrollment`,`lava_crms`.`enrollmentstatus`.`ProjName` AS `ProjName`,`lava_crms`.`enrollmentstatus`.`SubjectStudyID` AS `SubjectStudyID`,`lava_crms`.`enrollmentstatus`.`ReferralSource` AS `ReferralSource`,`lava_crms`.`enrollmentstatus`.`LatestDesc` AS `LatestDesc`,`lava_crms`.`enrollmentstatus`.`LatestDate` AS `LatestDate`,`lava_crms`.`enrollmentstatus`.`LatestNote` AS `LatestNote`,`lava_crms`.`enrollmentstatus`.`ReferredDesc` AS `ReferredDesc`,`lava_crms`.`enrollmentstatus`.`ReferredDate` AS `ReferredDate`,`lava_crms`.`enrollmentstatus`.`ReferredNote` AS `ReferredNote`,`lava_crms`.`enrollmentstatus`.`DeferredDesc` AS `DeferredDesc`,`lava_crms`.`enrollmentstatus`.`DeferredDate` AS `DeferredDate`,`lava_crms`.`enrollmentstatus`.`DeferredNote` AS `DeferredNote`,`lava_crms`.`enrollmentstatus`.`EligibleDesc` AS `EligibleDesc`,`lava_crms`.`enrollmentstatus`.`EligibleDate` AS `EligibleDate`,`lava_crms`.`enrollmentstatus`.`EligibleNote` AS `EligibleNote`,`lava_crms`.`enrollmentstatus`.`IneligibleDesc` AS `IneligibleDesc`,`lava_crms`.`enrollmentstatus`.`IneligibleDate` AS `IneligibleDate`,`lava_crms`.`enrollmentstatus`.`IneligibleNote` AS `IneligibleNote`,`lava_crms`.`enrollmentstatus`.`DeclinedDesc` AS `DeclinedDesc`,`lava_crms`.`enrollmentstatus`.`DeclinedDate` AS `DeclinedDate`,`lava_crms`.`enrollmentstatus`.`DeclinedNote` AS `DeclinedNote`,`lava_crms`.`enrollmentstatus`.`EnrolledDesc` AS `EnrolledDesc`,`lava_crms`.`enrollmentstatus`.`EnrolledDate` AS `EnrolledDate`,`lava_crms`.`enrollmentstatus`.`EnrolledNote` AS `EnrolledNote`,`lava_crms`.`enrollmentstatus`.`ExcludedDesc` AS `ExcludedDesc`,`lava_crms`.`enrollmentstatus`.`ExcludedDate` AS `ExcludedDate`,`lava_crms`.`enrollmentstatus`.`ExcludedNote` AS `ExcludedNote`,`lava_crms`.`enrollmentstatus`.`WithdrewDesc` AS `WithdrewDesc`,`lava_crms`.`enrollmentstatus`.`WithdrewDate` AS `WithdrewDate`,`lava_crms`.`enrollmentstatus`.`WithdrewNote` AS `WithdrewNote`,`lava_crms`.`enrollmentstatus`.`InactiveDesc` AS `InactiveDesc`,`lava_crms`.`enrollmentstatus`.`InactiveDate` AS `InactiveDate`,`lava_crms`.`enrollmentstatus`.`InactiveNote` AS `InactiveNote`,`lava_crms`.`enrollmentstatus`.`DeceasedDesc` AS `DeceasedDesc`,`lava_crms`.`enrollmentstatus`.`DeceasedDate` AS `DeceasedDate`,`lava_crms`.`enrollmentstatus`.`DeceasedNote` AS `DeceasedNote`,`lava_crms`.`enrollmentstatus`.`AutopsyDesc` AS `AutopsyDesc`,`lava_crms`.`enrollmentstatus`.`AutopsyDate` AS `AutopsyDate`,`lava_crms`.`enrollmentstatus`.`AutopsyNote` AS `AutopsyNote`,`lava_crms`.`enrollmentstatus`.`ClosedDesc` AS `ClosedDesc`,`lava_crms`.`enrollmentstatus`.`ClosedDate` AS `ClosedDate`,`lava_crms`.`enrollmentstatus`.`ClosedNote` AS `ClosedNote`,`lava_crms`.`enrollmentstatus`.`EnrollmentNotes` AS `EnrollmentNotes`,`lava_crms`.`enrollmentstatus`.`modified` AS `modified` from `lava_crms`.`enrollmentstatus` where (`lava_crms`.`enrollmentstatus`.`EnrollStatID` > 0);

-- -----------------------------------------------------
-- View `lava_crms`.`lq_view_instruments`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lava_crms`.`lq_view_instruments` ;
DROP TABLE IF EXISTS `lava_crms`.`lq_view_instruments`;
USE `lava_crms`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lava_crms`.`lq_view_instruments` AS select `i`.`InstrID` AS `InstrID`,`i`.`VID` AS `VID`,`i`.`ProjName` AS `ProjName`,`i`.`PIDN` AS `PIDN_Instrument`,`i`.`InstrType` AS `InstrType`,`i`.`InstrVer` AS `InstrVer`,`i`.`DCDate` AS `DCDate`,`i`.`DCBy` AS `DCBy`,`i`.`DCStatus` AS `DCStatus`,`i`.`DCNotes` AS `DCNotes`,`i`.`ResearchStatus` AS `ResearchStatus`,`i`.`QualityIssue` AS `QualityIssue`,`i`.`QualityIssue2` AS `QualityIssue2`,`i`.`QualityIssue3` AS `QualityIssue3`,`i`.`QualityNotes` AS `QualityNotes`,`i`.`DEDate` AS `DEDate`,`i`.`DEBy` AS `DEBy`,`i`.`DEStatus` AS `DEStatus`,`i`.`DENotes` AS `DENotes`,`i`.`DVDate` AS `DVDate`,`i`.`DVBy` AS `DVBy`,`i`.`DVStatus` AS `DVStatus`,`i`.`DVNotes` AS `DVNotes`,`i`.`latestflag` AS `latestflag`,`i`.`FieldStatus` AS `FieldStatus`,`i`.`AgeAtDC` AS `AgeAtDC`,`i`.`modified` AS `modified`,`s`.`Summary` AS `summary` from (`lava_crms`.`instrumenttracking` `i` join `lava_crms`.`instrumentsummary` `s` on((`i`.`InstrID` = `s`.`InstrID`))) where (`i`.`InstrID` > 0);

-- -----------------------------------------------------
-- View `lava_crms`.`lq_view_visit`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lava_crms`.`lq_view_visit` ;
DROP TABLE IF EXISTS `lava_crms`.`lq_view_visit`;
USE `lava_crms`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `lava_crms`.`lq_view_visit` AS select `lava_crms`.`visit`.`VID` AS `VID`,`lava_crms`.`visit`.`PIDN` AS `PIDN_Visit`,`lava_crms`.`visit`.`ProjName` AS `ProjName`,`lava_crms`.`visit`.`VLocation` AS `VLocation`,`lava_crms`.`visit`.`VType` AS `VType`,`lava_crms`.`visit`.`VWith` AS `VWith`,`lava_crms`.`visit`.`VDate` AS `VDate`,`lava_crms`.`visit`.`VTime` AS `VTime`,`lava_crms`.`visit`.`VStatus` AS `VStatus`,`lava_crms`.`visit`.`VNotes` AS `VNotes`,`lava_crms`.`visit`.`FUMonth` AS `FUMonth`,`lava_crms`.`visit`.`FUYear` AS `FUYear`,`lava_crms`.`visit`.`FUNote` AS `FUNote`,`lava_crms`.`visit`.`WList` AS `WList`,`lava_crms`.`visit`.`WListNote` AS `WListNote`,`lava_crms`.`visit`.`WListDate` AS `WListDate`,`lava_crms`.`visit`.`VShortDesc` AS `VShortDesc`,`lava_crms`.`visit`.`AgeAtVisit` AS `AgeAtVisit`,`lava_crms`.`visit`.`modified` AS `modified` from `lava_crms`.`visit` where (`lava_crms`.`visit`.`VID` > 0);

-- -----------------------------------------------------
-- View `lava_crms`.`patient_age`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `lava_crms`.`patient_age` ;
DROP TABLE IF EXISTS `lava_crms`.`patient_age`;
USE `lava_crms`;
CREATE  OR REPLACE VIEW `lava_crms`.`patient_age` AS
SELECT `PIDN`, ((if(isnull(date_format(`DOD`,'%Y')), date_format(now(),'%Y'), date_format(`DOD`,'%Y')) - date_format(`DOB`,'%Y')) - (if(isnull(date_format(`DOD`,'00-%m-%d')), date_format(now(),'00-%m-%d'), date_format(`DOD`,'00-%m-%d')) < date_format(`DOB`,'00-%m-%d'))) as `AGE`
FROM `patient` 
;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
