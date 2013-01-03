-- until the lava-crms V3.3.0 release is ready, all developers creating schema changes that will be
-- part of lava-crms V3.3.0 should update this script (i.e. the latest version of this script so
-- that you do not overwrite other developers changes)

DELIMITER ;

-- ************************************************************
-- EMORY: make the patient/caregiver changes regarding 
--   title, MName, DOD, and created/createdBy/modifiedBy fields
-- ************************************************************
ALTER TABLE `patient`
  ADD COLUMN `Title` VARCHAR(15) NULL DEFAULT NULL AFTER `FName`,
  ADD COLUMN `DODMO` SMALLINT(5) NULL DEFAULT NULL AFTER `DOD`,
  ADD COLUMN `DODDY` SMALLINT(5) NULL DEFAULT NULL AFTER `DODMO`,
  ADD COLUMN `DODYR` SMALLINT(5) NULL DEFAULT NULL AFTER `DODDY`,
  ADD COLUMN `created` DATETIME NULL DEFAULT NULL AFTER `FullNameNoSuffix`,
  ADD COLUMN `modifiedBy` VARCHAR(25) NULL DEFAULT NULL AFTER `created`;
-- changing middle initial to a middle name; data is not lost when doing this data type conversion
ALTER TABLE `patient`
  CHANGE COLUMN `MInitial` `MName` VARCHAR(25) NULL DEFAULT NULL;
ALTER TABLE `patient`
  CHANGE COLUMN `EnterBy` `createdBy` VARCHAR(25) NULL DEFAULT NULL AFTER `FullNameNoSuffix`;

-- backup DOD into archive table to be dropped at own discretion
CREATE TABLE IF NOT EXISTS `archive_crms_update_3_2_0_patientDOD` ( PIDN INT(10), DOD datetime );

-- split the DOD into its components
LOCK TABLES `patient` WRITE, `archive_crms_update_3_2_0_patientDOD` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
/*!40000 ALTER TABLE `archive_crms_update_3_2_0_patientDOD` DISABLE KEYS */;
UPDATE patient
  SET DODMO=MONTH(DOD),
      DODDY=DAYOFMONTH(DOD),
      DODYR=YEAR(DOD);
INSERT INTO `archive_crms_update_3_2_0_patientDOD` SELECT PIDN, DOD FROM `patient`;
/*!40000 ALTER TABLE `archive_crms_update_3_2_0_patientDOD` ENABLE KEYS */;
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

ALTER TABLE `patient` DROP COLUMN `DOD`;

-- EMORY: make the caregiver changes regarding title
ALTER TABLE `caregiver`
  ADD COLUMN `Title` VARCHAR(15) NULL DEFAULT NULL AFTER `FName`;

-- -----------------------------------------------------
-- View `patient_DOD`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `patient_DOD`;
CREATE OR REPLACE VIEW
`patient_DOD`
AS select `patient`.`PIDN` AS `PIDN`,
  -- empty date components are treated as "unknown"
  -- if unknown, assume a date component that would produce minimum death age
  -- return NULL if any field is skipped (should mean isDeceased=0), or the year is unknown
  CASE WHEN IFNULL(DODMO,99) IN (-6) OR IFNULL(DODDY,99) IN (-6) OR IFNULL(DODYR,9999) IN (-6,9999)
       THEN NULL
       ELSE STR_TO_DATE(CONCAT(CASE WHEN IFNULL(DODMO,99) IN (99) THEN 1 ELSE DODMO END,'-',
                               CASE WHEN IFNULL(DODDY,99) IN (99) THEN 1 ELSE DODDY END,'-',
                               DODYR), '%m-%d-%Y') END
  AS `DOD`
  FROM `patient`;

-- -----------------------------------------------------
-- View `patient_age`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `patient_age`;
CREATE OR REPLACE VIEW `patient_age`
AS select `patient`.`PIDN` AS `PIDN`,
  ((if(isnull(date_format(`patient_DOD`.DOD,_utf8'%Y')),
       date_format(now(),_utf8'%Y'),
       date_format(`patient_DOD`.`DOD`,_utf8'%Y')
      ) - date_format(`patient`.`DOB`,_utf8'%Y')
   ) - (if(isnull(date_format(`patient_DOD`.`DOD`,_utf8'00-%m-%d')),
           date_format(now(),_utf8'00-%m-%d'),
           date_format(`patient_DOD`.DOD,_utf8'00-%m-%d')
          ) < date_format(`patient`.`DOB`,_utf8'00-%m-%d')
       )
  ) AS `AGE`
  from `patient` INNER JOIN `patient_DOD` ON `patient`.PIDN=`patient_DOD`.PIDN;
  
  
DROP VIEW IF EXISTS `lq_view_demographics`;
CREATE VIEW `lq_view_demographics` AS select `patient`.`PIDN` AS `PIDN_demographics`,`patient`.`DOB` AS `DOB`,`patient_age`.`AGE` AS `AGE`,`patient`.`Gender` AS `Gender`,`patient`.`Hand` AS `Hand`,`patient`.`Deceased` AS `Deceased`,`patient_DOD`.`DOD` AS `DOD`,`patient`.`PrimaryLanguage` AS `PrimaryLanguage`,`patient`.`TestingLanguage` AS `TestingLanguage`,`patient`.`TransNeeded` AS `TransNeeded`,`patient`.`TransLanguage` AS `TransLanguage` from (`patient` join `patient_age` on((`patient`.`PIDN` = `patient_age`.`PIDN`)) join `patient_DOD` on (`patient`.`PIDN` = `patient_dod`.`PIDN`)) where (`patient`.`PIDN` > 0);

-- ************************************************************
-- EMORY: end patient/caregiver changes
-- ************************************************************
