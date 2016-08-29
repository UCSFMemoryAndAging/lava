ALTER TABLE crms_import_definition ADD COLUMN allow_extreme_dates BOOLEAN NULL DEFAULT NULL AFTER patient_only;
UPDATE crms_import_definition SET allow_extreme_dates = 0;

DELETE FROM versionhistory WHERE module='lava-crms-model' AND version='3.5.7';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-model','3.5.7','2016-08-28',3,5,7,0);


