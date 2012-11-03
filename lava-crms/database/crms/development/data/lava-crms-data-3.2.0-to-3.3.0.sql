-- until the lava-crms V3.3.0 release is ready, all developers creating metadata changes that will be
-- part of lava-crms V3.3.0 should update this script (i.e. the latest version of this script so
-- that you do not overwrite other developers changes)

-- EMORY: part of patient/caregiver changes
LOCK TABLES `viewproperty` WRITE;
/*!40000 ALTER TABLE `viewproperty` DISABLE KEYS */;

INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `attributes`, `list`, `quickHelp`)
  VALUES ('*.patient.title', 'en', 'lava', 'crms', 'Patient', 'title', 'ID and Core Demographics', 'i', 'range', 'No', 'Title', 0, '', 'generic.nameTitle', '');
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `maxLength`, `size`, `indentLevel`, `attributes`, `list`, `propOrder`, `quickHelp`)
  VALUES ('*.patient.deathMonth', 'en', 'lava', 'crms', 'Patient', 'deathMonth', 'ID and Core Demographics', 'i', 'range', 'No', 'Date of Death (mm/dd/yyyy)', 2, 2, 0, '', 'generic.deathdateMonth', 14, '');
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `maxLength`, `size`, `indentLevel`, `attributes`, `list`, `propOrder`, `quickHelp`)
  VALUES ('*.patient.deathDay', 'en', 'lava', 'crms', 'Patient', 'deathDay', 'ID and Core Demographics', 'i', 'range', 'No', '', 2, 2, 0, '', 'generic.deathdateDay', 14, '');
INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `maxLength`, `size`, `indentLevel`, `attributes`, `list`, `propOrder`, `quickHelp`)
  VALUES ('*.patient.deathYear', 'en', 'lava', 'crms', 'Patient', 'deathYear', 'ID and Core Demographics', 'i', 'range', 'No', '', 2, 2, 0, '', 'generic.deathdateYear', 14, '');

UPDATE `viewproperty`
  SET `messageCode`='*.addPatient.patient_middleName', `property`='patient_middleName', `label`='Middle Name', `maxLength`=25
  WHERE scope='crms' AND entity='addPatient' AND property='patient_middleInitial';
UPDATE `viewproperty`
  SET `messageCode`='*.patient.middleName', `property`='middleName', `label`='Middle Name', `maxLength`=25, size=NULL
  WHERE scope='crms' AND entity='patient' AND property='middleInitial';

INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `indentLevel`, `attributes`, `list`, `quickHelp`)
  VALUES ('*.caregiver.title', 'en', 'lava', 'crms', 'Caregiver', 'title', 'Caregiver Info / Demographics', 'i', 'range', 'No', 'Title', 0, '', 'generic.nameTitle', '');

/*!40000 ALTER TABLE `viewproperty` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `list` WRITE,`listvalues` WRITE;
/*!40000 ALTER TABLE `list` DISABLE KEYS */;
/*!40000 ALTER TABLE `listvalues` DISABLE KEYS */;
INSERT INTO list (ListName, instance, scope, NumericKey)
  SELECT 'NameTitle', 'lava','crms', 0;
SET @LISTID = (SELECT ListID FROM list WHERE ListName='NameTitle');
INSERT INTO listvalues (ListID, instance, scope, ValueKey, ValueDesc, OrderID) SELECT @LISTID, 'lava', 'crms', 'Mr.', NULL, 1;
INSERT INTO listvalues (ListID, instance, scope, ValueKey, ValueDesc, OrderID) SELECT @LISTID, 'lava', 'crms', 'Mrs.', NULL, 2;
INSERT INTO listvalues (ListID, instance, scope, ValueKey, ValueDesc, OrderID) SELECT @LISTID, 'lava', 'crms', 'Ms.', NULL, 3;
INSERT INTO listvalues (ListID, instance, scope, ValueKey, ValueDesc, OrderID) SELECT @LISTID, 'lava', 'crms', 'Miss', NULL, 4;
INSERT INTO listvalues (ListID, instance, scope, ValueKey, ValueDesc, OrderID) SELECT @LISTID, 'lava', 'crms', 'Dr.', NULL, 5;
INSERT INTO listvalues (ListID, instance, scope, ValueKey, ValueDesc, OrderID) SELECT @LISTID, 'lava', 'crms', 'Rev.', NULL, 6;


/*!40000 ALTER TABLE `listvalues` ENABLE KEYS */;
/*!40000 ALTER TABLE `list` ENABLE KEYS */;
UNLOCK TABLES;