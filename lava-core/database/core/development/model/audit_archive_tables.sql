-- these tables are for archiving the audit logging tables when they get too large
-- they are typically created in a separate database from the production database

-- NOTE: these tables are currently part of the model and complete scripts. if they
-- are going to be created in a separate database just for audit archiving, as 
-- recommended, then this script should be run on the separate database. the copies
-- of these history tables on the production database will not be used and can be 
-- dropped.  

-- also see the move_audit_logs.sql script in the util folder 


drop table if exists audit_entity_history;
create table audit_entity_history (
audit_entity_id int not null,
audit_event_id int not null,
entity_id int not null,
entity varchar(100) not null,
entity_type varchar(100) not null,
audit_type varchar(10) not null,
modified timestamp not null);

drop table if exists audit_event_history;
create table audit_event_history (
audit_event_id int not null,
audit_user varchar(50) not null,
audit_host varchar(25) not null,
audit_time datetime not null,
action varchar(255) not null,
action_event varchar(50) not null,
action_id_param varchar(50) null,
event_note varchar(255) null,
exception varchar(255) null,
exception_message varchar(255) null,
modified timestamp not null);

drop table if exists audit_property_history;
create table audit_property_history (
audit_property_id int not null,
audit_entity_id int not null,
property varchar(100) not null,
index_key varchar(100) null,
subproperty varchar(255) null,
old_value varchar(255) not null,
new_value varchar(255) not null,
audit_time datetime not null,
modified timestamp not null);

drop table if exists audit_text_history;
create table audit_text_history (
audit_property_id char(10) not null,
old_text longtext null,
new_text longtext null);



