-- audit logs are typically archived to the audit history tables in a separate database
-- replace all occurences of _AUDIT_HISTORY_SCHEMA_ with the name of the database where the
-- audit history tables reside,
-- e.g. lava_msc so the result is lava_msc.audit_event_history, etc.

-- the model script audit_archive_tables should be run to create the history tables in the
-- _AUDIT_HISTORY_SCHEMA_ prior to running this script (and the db user running this script
-- will need permissions on the history tables in that database)

CREATE TEMPORARY TABLE max_audit_id SELECT MAX(audit_event_id) AS max_audit_event_id FROM audit_event;

CREATE TEMPORARY TABLE max_entity_id SELECT MAX(audit_entity_id) AS max_id FROM audit_entity;

CREATE TEMPORARY TABLE max_property_id SELECT MAX(audit_property_id) AS max_id FROM audit_property;

INSERT INTO lava_msc.audit_event_history 
SELECT * FROM audit_event 
WHERE audit_event_id <= (SELECT max_audit_event_id FROM max_audit_id);

INSERT INTO lava_msc.audit_entity_history 
SELECT * FROM audit_entity 
WHERE audit_entity_id <= (SELECT max_id FROM max_entity_id);

INSERT INTO lava_msc.audit_property_history 
SELECT * FROM audit_property
WHERE audit_property_id <= (SELECT max_id FROM max_property_id);

INSERT INTO lava_msc.audit_text_history 
SELECT * FROM audit_text 
WHERE audit_property_id <= (SELECT max_id FROM max_property_id);


DELETE FROM audit_text_history 
WHERE audit_property_id <= (SELECT max_id FROM max_property_id);

DELETE FROM audit_text_work  
WHERE audit_property_id <= (SELECT max_id FROM max_property_id);


DELETE FROM audit_property_history  
WHERE audit_property_id <= (SELECT max_id FROM max_property_id);

DELETE FROM audit_property_work 
WHERE audit_property_id <= (SELECT max_id FROM max_property_id);


DELETE FROM audit_entity_history 
WHERE audit_entity_id <= (SELECT max_id FROM max_entity_id);

DELETE FROM audit_entity_work 
WHERE audit_entity_id <= (SELECT max_id FROM max_entity_id);


DELETE FROM audit_event_history 
WHERE audit_event_id <= (SELECT max_audit_event_id FROM max_audit_id);

DELETE FROM audit_event_work 
WHERE audit_event_id <= (SELECT max_audit_event_id FROM max_audit_id);


