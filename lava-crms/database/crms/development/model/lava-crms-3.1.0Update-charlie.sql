-- this is a temporary file which will be integrated into the crms model
-- when protocol is released

DROP TABLE IF EXISTS prot_instr;
DROP TABLE IF EXISTS prot_visit;
DROP TABLE IF EXISTS prot_assess_tp;
DROP TABLE IF EXISTS prot_tp;
DROP TABLE IF EXISTS prot_protocol;
DROP TABLE IF EXISTS prot_node;
DROP TABLE IF EXISTS prot_instr_opt_config;
DROP TABLE IF EXISTS prot_instr_config;
DROP TABLE IF EXISTS prot_repeat_tp_config;
DROP TABLE IF EXISTS prot_assess_tp_config;
DROP TABLE IF EXISTS prot_tp_config;
DROP TABLE IF EXISTS prot_visit_opt_config;
DROP TABLE IF EXISTS prot_visit_config;
DROP TABLE IF EXISTS prot_protocol_config;
DROP TABLE IF EXISTS prot_opt_config;
DROP TABLE IF EXISTS prot_node_config;

-- Protocol Config tables

CREATE TABLE prot_node_config (
node_id int NOT NULL AUTO_INCREMENT,
parent_id int, -- will be NULL for Protocol nodes 
list_order int, -- will be NULL for Protocol nodes since they are never children
ProjName varchar(75) NOT NULL,
label varchar(25) NOT NULL,
notes varchar(255),
eff_date date,
exp_date date,
modified timestamp,
PRIMARY KEY (node_id),
-- self referencing FK to the parent of the node 
KEY fk_prot_node_config__parent_id (parent_id),
CONSTRAINT fk_prot_node_config__parent_id FOREIGN KEY (parent_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_opt_config (
option_id int NOT NULL AUTO_INCREMENT,
parent_id int NOT NULL,
ProjName varchar(75) NOT NULL, 
default_option boolean,
label varchar(25) NOT NULL,
notes varchar(255),
eff_date date,
exp_date date,
modified timestamp,
PRIMARY KEY (option_id),
KEY fk_prot_opt_config__parent_id (parent_id),
CONSTRAINT fk_prot_opt_config__parent_id FOREIGN KEY (parent_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_protocol_config (
node_id int NOT NULL AUTO_INCREMENT,
category varchar(25),
PRIMARY KEY (node_id),
KEY fk_prot_protocol_config__node_id (node_id),
CONSTRAINT fk_prot_protocol_config__node_id FOREIGN KEY (node_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


-- note: create prot_visit before prot_assess_tp as the latter has a FK reference to prot_visit
CREATE TABLE prot_visit_config (
node_id int NOT NULL AUTO_INCREMENT,
optional boolean,
PRIMARY KEY (node_id),
KEY fk_prot_visit_config__node_id (node_id),
CONSTRAINT fk_prot_visit_config__node_id FOREIGN KEY (node_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_visit_opt_config (
option_id int NOT NULL AUTO_INCREMENT,
visit_type varchar(25),
PRIMARY KEY (option_id),
KEY fk_prot_visit_opt_config__option_id (option_id),
CONSTRAINT fk_prot_visit_opt_config__option_id FOREIGN KEY (option_id) REFERENCES prot_opt_config (option_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_tp_config (
node_id int NOT NULL AUTO_INCREMENT,
first_timepoint boolean,
optional boolean,
sched_win_anchor_timepoint_id int,
sched_win_days_from_anchor smallint,
sched_win_days_from_start smallint,
sched_win_size smallint,
sched_win_offset smallint,
PRIMARY KEY (node_id),
KEY fk_prot_tp_config__node_id (node_id),
CONSTRAINT fk_prot_tp_config__node_id FOREIGN KEY (node_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
KEY fk_prot_tp_config__sched_win_anchor_timepoint_id (sched_win_anchor_timepoint_id),
CONSTRAINT fk_prot_tp_config__sched_win_anchor_timepoint_id FOREIGN KEY (sched_win_anchor_timepoint_id) REFERENCES prot_tp_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_assess_tp_config (
node_id int NOT NULL AUTO_INCREMENT,
collect_win_anchor_visit_id int,
collect_win_size smallint,
collect_win_offset smallint,
collect_win_status varchar(25),
PRIMARY KEY (node_id),
KEY fk_prot_assess_tp_config__node_id (node_id),
CONSTRAINT fk_prot_assess_tp_config__node_id FOREIGN KEY (node_id) REFERENCES prot_tp_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
-- TODO: because of circular FK constraints (that prot_visit_config has a FK back to this prot_assess_tp_config), 
-- need to add dropping of constraints in order to run this script with the following key/constraint enabled:
-- KEY fk_prot_assess_tp_config__collect_win_anchor_visit_id (collect_win_anchor_visit_id),-- 
-- CONSTRAINT fk_prot_assess_tp_config__collect_win_anchor_visit_id FOREIGN KEY (collect_win_anchor_visit_id) REFERENCES prot_visit_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_repeat_tp_config (
node_id int NOT NULL AUTO_INCREMENT,
repeat_type varchar(25), -- e.g. treatment_type for a treatment schedule
total smallint,
tp_interval smallint,
weekends boolean,
holidays boolean,
PRIMARY KEY (node_id),
KEY fk_prot_tp_repeat_config__node_id (node_id),
CONSTRAINT fk_prot_tp_repeat_config__node_id FOREIGN KEY (node_id) REFERENCES prot_tp_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_instr_config (
node_id int NOT NULL AUTO_INCREMENT,
optional boolean,
PRIMARY KEY (node_id),
KEY fk_prot_instr_config__node_id (node_id),
CONSTRAINT fk_prot_instr_config__node_id FOREIGN KEY (node_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_instr_opt_config (
option_id int NOT NULL AUTO_INCREMENT,
instr_type varchar(25),
collect_win_anchor_visit_id int,
collect_win_size smallint,
collect_win_offset smallint,
collect_win_status varchar(25),
PRIMARY KEY (option_id),
KEY fk_prot_instr_opt_config__option_id (option_id),
CONSTRAINT fk_prot_instr_opt_config__option_id FOREIGN KEY (option_id) REFERENCES prot_opt_config (option_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
KEY fk_prot_instr_opt_config__collect_win_anchor_visit_id (collect_win_anchor_visit_id),
CONSTRAINT fk_prot_instr_opt_config__collect_win_anchor_visit_id FOREIGN KEY (collect_win_anchor_visit_id) REFERENCES prot_visit_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;



-- (Patient) Protocol tables

CREATE TABLE prot_node (
node_id int NOT NULL AUTO_INCREMENT,
parent_id int, -- will be NULL for Protocol nodes 
config_node_id int NOT NULL,
list_order int, -- will be NULL for Protocol nodes since they are never children
PIDN int NOT NULL,
ProjName varchar(75) NOT NULL,
strategy smallint NOT NULL,
curr_status varchar(25),
curr_reason varchar(25),
curr_note varchar(100),
notes varchar(255),
modified timestamp,
PRIMARY KEY (node_id),
-- self referencing FK to the parent of the node 
KEY fk_prot_node__parent_id (parent_id),
CONSTRAINT fk_prot_node__parent_id FOREIGN KEY (parent_id) REFERENCES prot_node (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
KEY fk_prot_node__config_node_id (config_node_id),
CONSTRAINT fk_prot_node__config_node_id FOREIGN KEY (config_node_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_protocol (
node_id int NOT NULL AUTO_INCREMENT,
enrolled_date date,
PRIMARY KEY (node_id),
KEY fk_prot_protocol__node_id (node_id),
CONSTRAINT fk_prot_protocol__node_id FOREIGN KEY (node_id) REFERENCES prot_node (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_tp (
node_id int NOT NULL AUTO_INCREMENT,
sched_win_status varchar(25),
sched_win_reason varchar(25),
sched_win_note varchar(100),
sched_win_start date,
sched_win_end date,
PRIMARY KEY (node_id),
KEY fk_prot_tp__node_id (node_id),
CONSTRAINT fk_prot_tp__node_id FOREIGN KEY (node_id) REFERENCES prot_node (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;
 

CREATE TABLE prot_assess_tp (
node_id int NOT NULL AUTO_INCREMENT,
collect_win_status varchar(25),
collect_win_reason varchar(25),
collect_win_note varchar(100),
collect_win_start date,
collect_win_end date,
PRIMARY KEY (node_id),
KEY fk_prot_assess_tp__node_id (node_id),
CONSTRAINT fk_prot_assess_tp__node_id FOREIGN KEY (node_id) REFERENCES prot_tp (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_visit (
node_id int NOT NULL AUTO_INCREMENT,
VID int,
PRIMARY KEY (node_id),
KEY fk_prot_visit__node_id (node_id),
CONSTRAINT fk_prot_visit__node_id FOREIGN KEY (node_id) REFERENCES prot_node (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
KEY fk_prot_visit__VID (VID),
CONSTRAINT fk_prot_visit__VID FOREIGN KEY (VID) REFERENCES visit (VID) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_instr (
node_id int NOT NULL AUTO_INCREMENT,
InstrID int,
collect_win_status varchar(25),
collect_win_reason varchar(25),
collect_win_note varchar(100),
cust_collect_win_start date,
cust_collect_win_end date,
PRIMARY KEY (node_id),
KEY fk_prot_instr__node_id (node_id),
CONSTRAINT fk_prot_instr__node_id FOREIGN KEY (node_id) REFERENCES prot_node (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
KEY fk_prot_instr__InstrID (InstrID),
CONSTRAINT fk_prot_instr__InstrID FOREIGN KEY (InstrID) REFERENCES instrumenttracking (InstrID) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


