-- until the lava-crms V3.4.0 release is ready, all developers creating metadata changes that will be
-- part of lava-crms V3.4.0 should update this script (i.e. the latest version of this script so
-- that you do not overwrite other developers changes)

LOCK TABLES `viewproperty` WRITE;

INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `maxLength`, `indentLevel`, `attributes`, `list`, `quickHelp`)
  VALUES ('*.patient.notes', 'en', 'lava', 'crms', 'Patient', 'notes', '', 'i', 'text', 'No', 'Notes', 2000, 0, 'rows="5" cols="50"', NULL, 'Notes');

INSERT INTO `viewproperty` (`messageCode`, `locale`, `instance`, `scope`, `entity`, `property`, `section`, `context`, `style`, `required`, `label`, `maxLength`, `indentLevel`, `attributes`, `list`, `quickHelp`)
  VALUES ('*.addPatient.patient_notes', 'en', 'lava', 'crms', 'addPatient', 'patient_notes', '', 'i', 'text', 'No', 'Notes', 2000, 0, 'rows="5" cols="50"', NULL, 'Notes');

UNLOCK TABLES;

INSERT INTO versionhistory(module,version,versiondate,major,minor,fix,updaterequired)
VALUES ('lava-crms-data','3.3.1','2013-02-25',3,3,1,0);
