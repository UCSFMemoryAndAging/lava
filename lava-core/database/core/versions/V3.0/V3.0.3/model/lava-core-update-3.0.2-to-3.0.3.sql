SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';



CREATE TEMPORARY TABLE `temp_authuser` LIKE `authuser`;

INSERT INTO `temp_authuser` select * from `authuser`;


DROP TABLE IF EXISTS `authuser` ;

CREATE  TABLE IF NOT EXISTS `authuser` (
  `UID` INT(10) NOT NULL AUTO_INCREMENT ,
  `UserName` VARCHAR(50) NOT NULL ,
  `Login` VARCHAR(100) NULL DEFAULT NULL ,
  `email` VARCHAR(100) NULL DEFAULT NULL ,
  `phone` VARCHAR(25) NULL DEFAULT NULL ,
  `AccessAgreementDate` DATE NULL DEFAULT NULL ,
  `ShortUserName` VARCHAR(50) NULL DEFAULT NULL ,
  `ShortUserNameRev` VARCHAR(50) NULL DEFAULT NULL ,
  `EffectiveDate` DATE NOT NULL ,
  `ExpirationDate` DATE NULL DEFAULT NULL ,
  `Notes` VARCHAR(255) NULL DEFAULT NULL ,
  `authenticationType` VARCHAR(10) NULL DEFAULT 'LOCAL' ,
  `password` VARCHAR(100) NULL DEFAULT NULL ,
  `passwordExpiration` TIMESTAMP NULL DEFAULT NULL ,
  `passwordResetToken` VARCHAR(100) NULL DEFAULT NULL ,
  `passwordResetExpiration` TIMESTAMP NULL DEFAULT NULL ,
  `failedLoginCount` SMALLINT NULL DEFAULT NULL ,
  `lastFailedLogin` TIMESTAMP NULL DEFAULT NULL ,
  `accountLocked` TIMESTAMP NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`UID`) )
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = latin1;

CREATE UNIQUE INDEX `Unique_UserName` ON `authuser` (`UserName` ASC) ;

CREATE UNIQUE INDEX `Unique_Login` ON `authuser` (`Login` ASC) ;


INSERT INTO `authuser` (`UID`,`UserName`,`Login`,`email`,`phone`,`AccessAgreementDate`,`ShortUserName`,
	  `ShortUserNameRev`,`EffectiveDate`,`ExpirationDate`,`Notes`,`authenticationType`,`password`,
	  `passwordExpiration`,`passwordResetToken`,`passwordResetExpiration`,`failedLoginCount`,
	  `lastFailedLogin`,`accountLocked`,`modified`)
	  SELECT `UID`,`UserName`,`Login`,`email`,`phone`,`AccessAgreementDate`,`ShortUserName`,
	  `ShortUserNameRev`,`EffectiveDate`,`ExpirationDate`,`Notes`,CASE WHEN hashedPassword IS NULL THEN 'XML CONFIG' ELSE 'LOCAL' END,`hashedPassword`,
	  NULL,NULL,NULL,NULL,NULL,NULL,`modified` from temp_authuser;
	  
DROP TEMPORARY TABLE temp_authuser;



CREATE TEMPORARY TABLE `temp_list` LIKE `list`;

INSERT INTO `temp_list` select * from `list`;

ALTER TABLE `listvalues` DROP FOREIGN KEY `FK_ListID`; 

DROP TABLE IF EXISTS `list` ;

CREATE  TABLE IF NOT EXISTS `list` (
  `ListID` INT(10) NOT NULL AUTO_INCREMENT ,
  `ListName` VARCHAR(50) NOT NULL ,
  `scope` VARCHAR(25) NOT NULL ,
  `NumericKey` TINYINT(1) NOT NULL DEFAULT '0' ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`ListID`),
  UNIQUE INDEX `ListName` (`ListName` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 468
DEFAULT CHARACTER SET = latin1;




INSERT INTO `list` (`ListID`,`ListName`,`scope`,`NumericKey`,`modified`)
	  SELECT `ListID`,`ListName`,'scope-not-set',`NumericKey`,`modified` from temp_list;
	  
DROP TEMPORARY TABLE temp_list;

UPDATE `list` set `scope`='crms' where `ListName`='CaregiverMaritalStatus';
UPDATE `list` set `scope`='crms' where `ListName`='ConsentType';
UPDATE `list` set `scope`='crms' where `ListName`='ContactMethods';
UPDATE `list` set `scope`='crms' where `ListName`='ContactRelations';
UPDATE `list` set `scope`='crms' where `ListName`='DataCollectionStatus';
UPDATE `list` set `scope`='crms' where `ListName`='DataEntryStatus';
UPDATE `list` set `scope`='crms' where `ListName`='DataValidationStatus';
UPDATE `list` set `scope`='crms' where `ListName`='DoctorStatus';
UPDATE `list` set `scope`='crms' where `ListName`='Education';
UPDATE `list` set `scope`='crms' where `ListName`='Gender';
UPDATE `list` set `scope`='crms' where `ListName`='Handedness';
UPDATE `list` set `scope`='crms' where `ListName`='InstrumentQualityIssue';
UPDATE `list` set `scope`='crms' where `ListName`='InstrumentResearchStatus';
UPDATE `list` set `scope`='crms' where `ListName`='InstrumentVersions';
UPDATE `list` set `scope`='core' where `ListName`='LavaSessionStatus';
UPDATE `list` set `scope`='crms' where `ListName`='MaritalStatus';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='mdsstatcurrentstat';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='mdsstatdiscrea';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='MultumDrugLookup';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath10';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath11';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath13A';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NaccPath13B';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NaccPath14F';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath17A';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath18A';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath18B';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath18C';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath19';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath7';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath8A';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath8B';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath8C';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath8D';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPath9';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPathMildModSevere';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCPathYesNo';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NaccPathYesNoOnly';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='NACCSubmissionStatus';
UPDATE `list` set `scope`='core' where `ListName`='NavigationListPageSize';
UPDATE `list` set `scope`='crms' where `ListName`='Occupation';
UPDATE `list` set `scope`='crms' where `ListName`='PatientLanguage';
UPDATE `list` set `scope`='crms' where `ListName`='PhoneType';
UPDATE `list` set `scope`='crms' where `ListName`='PrimaryCaregiver';
UPDATE `list` set `scope`='crms' where `ListName`='ProbablePossibleNo';
UPDATE `list` set `scope`='crms' where `ListName`='ProjectStatus';
UPDATE `list` set `scope`='crms' where `ListName`='ProjectStatusType';
UPDATE `list` set `scope`='crms' where `ListName`='RACE';
UPDATE `list` set `scope`='crms' where `ListName`='ReferralSources';
UPDATE `list` set `scope`='crms' where `ListName`='SkipErrorCodes';
UPDATE `list` set `scope`='crms' where `ListName`='SpanishOrigin';
UPDATE `list` set `scope`='crms' where `ListName`='StaffList';
UPDATE `list` set `scope`='crms' where `ListName`='StandardErrorCodes';
UPDATE `list` set `scope`='crms' where `ListName`='States';
UPDATE `list` set `scope`='crms' where `ListName`='TaskStatus';
UPDATE `list` set `scope`='crms' where `ListName`='TaskType';
UPDATE `list` set `scope`='core' where `ListName`='TextYesNo';
UPDATE `list` set `scope`='core' where `ListName`='TextYesNoDK';
UPDATE `list` set `scope`='core' where `ListName`='TextYesNoNA';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1handed';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1hispanic';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1indepd';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1lang';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1livesit';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1marital';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1padctype';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1presdis';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1prespart';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1primreason';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1race';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1race2';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1refsource';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa1residence';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa2freq';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa2relation';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa3twintype';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa4frequnit';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa4medunit';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa5packsmoke';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa5presence3';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsa5presence4';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsb201';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsb202';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsb7diff';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsb9domain';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsb9fuchg';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsb9mode';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsb9overall';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsb9predombeh';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsb9predomcog';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsb9predommot';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsc1admin';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsc1cogstat';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsc1cogstatV2';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsc1lang';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='UDSCenterIDs';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsd1ifpres';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsd1ifpressht';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsd1presab';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsd1response';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='UDSErrorCodes';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='UDSFormIDs';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='UDSFormLogicCheckReason';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='UDSFormLogicCheckStatus';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='UDSFormSubmissionReason';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='UDSFormSubmissionStatus';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsM1Discont';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsm1nurse';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsM1Protocol';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsm1status';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsnoyes01';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsNoYesNA';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsnoyesunknown019';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='UDSPacketReason';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='UDSPacketStatus';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='UDSPacketTypes';
UPDATE `list` set `scope`='crms-nacc' where `ListName`='udsZ1Reason';
UPDATE `list` set `scope`='crms' where `ListName`='UsualSomeRare';
UPDATE `list` set `scope`='crms' where `ListName`='UsualSomeRareDK';
UPDATE `list` set `scope`='crms' where `ListName`='VisitLocations';
UPDATE `list` set `scope`='crms' where `ListName`='VisitStatus';
UPDATE `list` set `scope`='crms' where `ListName`='VisitType';
UPDATE `list` set `scope`='core' where `ListName`='YESNO';
UPDATE `list` set `scope`='core' where `ListName`='YESNODK';
UPDATE `list` set `scope`='core' where `ListName`='YesNoDK_Zero';
UPDATE `list` set `scope`='crms' where `ListName`='YesNoScale_NoCorrect';
UPDATE `list` set `scope`='crms' where `ListName`='YesNoScale_YesCorrect';
UPDATE `list` set `scope`='core' where `ListName`='YesNoUnknown';
UPDATE `list` set `scope`='core' where `ListName`='YesNo_Zero';




CREATE  TABLE IF NOT EXISTS `appointment_change` (
  `appointment_change_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `appointment_id` INT(11) UNSIGNED NOT NULL ,
  `type` VARCHAR(25) NOT NULL ,
  `description` VARCHAR(255) NULL DEFAULT NULL ,
  `change_by` INT(10) NOT NULL ,
  `change_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`appointment_change_id`) ,
  INDEX `appointment_change__appointment` (`appointment_id` ASC) ,
  INDEX `appointment_change__change_by` (`change_by` ASC) ,
  CONSTRAINT `appointment_change__appointment`
    FOREIGN KEY (`appointment_id` )
    REFERENCES `appointment` (`appointment_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `appointment_change__change_by`
    FOREIGN KEY (`change_by` )
    REFERENCES `authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `appointment` (
  `appointment_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `calendar_id` INT(11) UNSIGNED NOT NULL ,
  `organizer_id` INT(11) NOT NULL ,
  `type` VARCHAR(25) NOT NULL ,
  `description` VARCHAR(100) NULL DEFAULT NULL ,
  `location` VARCHAR(100) NULL DEFAULT NULL ,
  `start_date` DATE NOT NULL ,
  `start_time` TIME NOT NULL ,
  `end_date` DATE NOT NULL ,
  `end_time` TIME NOT NULL ,
  `status` VARCHAR(25) NULL DEFAULT NULL ,
  `notes` TEXT NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`appointment_id`) ,
  INDEX `appointment__calendar` (`calendar_id` ASC) ,
  CONSTRAINT `appointment__calendar`
    FOREIGN KEY (`calendar_id` )
    REFERENCES `calendar` (`calendar_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `attendee` (
  `attendee_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `appointment_id` INT(11) UNSIGNED NOT NULL ,
  `user_id` INT(10) NOT NULL ,
  `role` VARCHAR(25) NOT NULL ,
  `status` VARCHAR(25) NOT NULL ,
  `notes` VARCHAR(100) NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`attendee_id`) ,
  INDEX `attendee__appointment` (`appointment_id` ASC) ,
  INDEX `attendee__user_id` (`user_id` ASC) ,
  CONSTRAINT `attendee__appointment`
    FOREIGN KEY (`appointment_id` )
    REFERENCES `appointment` (`appointment_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `attendee__user_id`
    FOREIGN KEY (`user_id` )
    REFERENCES `authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `calendar` (
  `calendar_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `type` VARCHAR(25) NOT NULL ,
  `name` VARCHAR(100) NOT NULL ,
  `description` VARCHAR(255) NULL DEFAULT NULL ,
  `notes` TEXT NULL DEFAULT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`calendar_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `resource_calendar` (
  `calendar_id` INT(11) UNSIGNED NOT NULL ,
  `resource_type` VARCHAR(25) NOT NULL ,
  `location` VARCHAR(100) NULL DEFAULT NULL ,
  `contact_id` INT(10) NOT NULL ,
  PRIMARY KEY (`calendar_id`) ,
  INDEX `resource_calendar__calendar` (`calendar_id` ASC) ,
  INDEX `resource_calendar__user_id` (`contact_id` ASC) ,
  CONSTRAINT `resource_calendar__calendar`
    FOREIGN KEY (`calendar_id` )
    REFERENCES `calendar` (`calendar_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `resource_calendar__user_id`
    FOREIGN KEY (`contact_id` )
    REFERENCES `authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

ALTER TABLE `audit_entity_history` 
  ADD CONSTRAINT `audit_entity_history__audit_event_id`
  FOREIGN KEY (`audit_event_id` )
  REFERENCES `audit_event_history` (`audit_event_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `audit_entity_history__audit_event_id` (`audit_event_id` ASC) ;

ALTER TABLE `audit_entity_work` 
  ADD CONSTRAINT `audit_entity_work__audit_event_id`
  FOREIGN KEY (`audit_event_id` )
  REFERENCES `audit_event_work` (`audit_event_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `audit_entity_work__audit_event_id` (`audit_event_id` ASC) ;

ALTER TABLE `audit_property_history` 
  ADD CONSTRAINT `audit_property_history__audit_entity_id`
  FOREIGN KEY (`audit_entity_id` )
  REFERENCES `audit_entity_history` (`audit_entity_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `audit_property_history__audit_entity_id` (`audit_entity_id` ASC) ;

ALTER TABLE `audit_property_work` 
  ADD CONSTRAINT `audit_property_work__audit_entity_id`
  FOREIGN KEY (`audit_entity_id` )
  REFERENCES `audit_entity_work` (`audit_entity_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `audit_property_work__audit_entity_id` (`audit_entity_id` ASC) ;

ALTER TABLE `audit_text_history` CHANGE COLUMN `audit_property_id` `audit_property_id` INT(10) NOT NULL  , 
  ADD CONSTRAINT `audit_text_history__audit_property_id`
  FOREIGN KEY (`audit_property_id` )
  REFERENCES `audit_property_history` (`audit_property_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `audit_text_history__audit_property_id` (`audit_property_id` ASC) ;

ALTER TABLE `audit_text_work` CHANGE COLUMN `audit_property_id` `audit_property_id` INT(10) NOT NULL  , 
  ADD CONSTRAINT `audit_text_work__audit_property_id`
  FOREIGN KEY (`audit_property_id` )
  REFERENCES `audit_property_work` (`audit_property_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `audit_text_work__audit_property_id` (`audit_property_id` ASC) ;

ALTER TABLE `authpermission` 
  ADD CONSTRAINT `authpermission_RoleID`
  FOREIGN KEY (`RoleID` )
  REFERENCES `authrole` (`RoleID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `authpermission_RoleID` (`RoleID` ASC) ;






ALTER TABLE `authusergroup` 
  ADD CONSTRAINT `authusergroup_GID`
  FOREIGN KEY (`UGID` )
  REFERENCES `authgroup` (`GID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `authusergroup_UID`
  FOREIGN KEY (`UID` )
  REFERENCES `authuser` (`UID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `authusergroup_GID` (`UGID` ASC) 
, ADD INDEX `authusergroup_UID` (`UID` ASC) ;

ALTER TABLE `authuserrole` 
  ADD CONSTRAINT `authuserrole_GID`
  FOREIGN KEY (`GID` )
  REFERENCES `authgroup` (`GID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `authuserrole_RoleID`
  FOREIGN KEY (`RoleID` )
  REFERENCES `authrole` (`RoleID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `authuserrole_UID`
  FOREIGN KEY (`URID` )
  REFERENCES `authuser` (`UID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `authuserrole_GID` (`GID` ASC) 
, ADD INDEX `authuserrole_RoleID` (`RoleID` ASC) 
, ADD INDEX `authuserrole_UID` (`URID` ASC) ;

ALTER TABLE `lava_session` 
  ADD CONSTRAINT `lavasession__server_instance_id`
  FOREIGN KEY (`server_instance_id` )
  REFERENCES `lavaserverinstance` (`ServerInstanceID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `lavasession__user_id`
  FOREIGN KEY (`user_id` )
  REFERENCES `authuser` (`UID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `lavasession__server_instance_id` (`server_instance_id` ASC) 
, ADD INDEX `lavasession__user_id` (`user_id` ASC) ;



ALTER TABLE `listvalues` 
  ADD CONSTRAINT `listvalues__listID`
  FOREIGN KEY (`ListID` )
  REFERENCES `list` (`ListID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `ListID` (`ListID` ASC) 
, ADD INDEX `listvalues__listID` (`ListID` ASC) 
, DROP INDEX `cListID` ;


-- -----------------------------------------------------
-- Placeholder table for view `audit_entity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_entity` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `audit_event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_event` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `audit_property`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_property` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `audit_text`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit_text` (`id` INT);

-- -----------------------------------------------------
-- View `audit_entity`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_entity`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `audit_entity` AS select `audit_entity_work`.`audit_entity_id` AS `audit_entity_id`,`audit_entity_work`.`audit_event_id` AS `audit_event_id`,`audit_entity_work`.`entity_id` AS `entity_id`,`audit_entity_work`.`entity` AS `entity`,`audit_entity_work`.`entity_type` AS `entity_type`,`audit_entity_work`.`audit_type` AS `audit_type`,`audit_entity_work`.`modified` AS `modified` from `audit_entity_work` union all select `audit_entity_history`.`audit_entity_id` AS `audit_entity_id`,`audit_entity_history`.`audit_event_id` AS `audit_event_id`,`audit_entity_history`.`entity_id` AS `entity_id`,`audit_entity_history`.`entity` AS `entity`,`audit_entity_history`.`entity_type` AS `entity_type`,`audit_entity_history`.`audit_type` AS `audit_type`,`audit_entity_history`.`modified` AS `modified` from `audit_entity_history`;

-- -----------------------------------------------------
-- View `audit_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_event`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `audit_event` AS select `audit_event_work`.`audit_event_id` AS `audit_event_id`,`audit_event_work`.`audit_user` AS `audit_user`,`audit_event_work`.`audit_host` AS `audit_host`,`audit_event_work`.`audit_timestamp` AS `audit_timestamp`,`audit_event_work`.`action` AS `action`,`audit_event_work`.`action_event` AS `action_event`,`audit_event_work`.`action_id_param` AS `action_id_param`,`audit_event_work`.`event_note` AS `event_note`,`audit_event_work`.`exception` AS `exception`,`audit_event_work`.`exception_message` AS `exception_message`,`audit_event_work`.`modified` AS `modified` from `audit_event_work` union all select `audit_event_history`.`audit_event_id` AS `audit_event_id`,`audit_event_history`.`audit_user` AS `audit_user`,`audit_event_history`.`audit_host` AS `audit_host`,`audit_event_history`.`audit_timestamp` AS `audit_timestamp`,`audit_event_history`.`action` AS `action`,`audit_event_history`.`action_event` AS `action_event`,`audit_event_history`.`action_id_param` AS `action_id_param`,`audit_event_history`.`event_note` AS `event_note`,`audit_event_history`.`exception` AS `exception`,`audit_event_history`.`exception_message` AS `exception_message`,`audit_event_history`.`modified` AS `modified` from `audit_event_history`;

-- -----------------------------------------------------
-- View `audit_property`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_property`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `audit_property` AS select `audit_property_work`.`audit_property_id` AS `audit_property_id`,`audit_property_work`.`audit_entity_id` AS `audit_entity_id`,`audit_property_work`.`property` AS `property`,`audit_property_work`.`index_key` AS `index_key`,`audit_property_work`.`subproperty` AS `subproperty`,`audit_property_work`.`old_value` AS `old_value`,`audit_property_work`.`new_value` AS `new_value`,`audit_property_work`.`audit_timestamp` AS `audit_timestamp`,`audit_property_work`.`modified` AS `modified` from `audit_property_work` union all select `audit_property_history`.`audit_property_id` AS `audit_property_id`,`audit_property_history`.`audit_entity_id` AS `audit_entity_id`,`audit_property_history`.`property` AS `property`,`audit_property_history`.`index_key` AS `index_key`,`audit_property_history`.`subproperty` AS `subproperty`,`audit_property_history`.`old_value` AS `old_value`,`audit_property_history`.`new_value` AS `new_value`,`audit_property_history`.`audit_timestamp` AS `audit_timestamp`,`audit_property_history`.`modified` AS `modified` from `audit_property_history`;

-- -----------------------------------------------------
-- View `audit_text`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `audit_text`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `audit_text` AS select `audit_text_history`.`audit_property_id` AS `audit_property_id`,`audit_text_history`.`old_text` AS `old_text`,`audit_text_history`.`new_text` AS `new_text` from `audit_text_history` union all select `audit_text_work`.`audit_property_id` AS `audit_property_id`,`audit_text_work`.`old_text` AS `old_text`,`audit_text_work`.`new_text` AS `new_text` from `audit_text_work`;



insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-core-model','3.0.3',NOW(),3,0,3,1);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
