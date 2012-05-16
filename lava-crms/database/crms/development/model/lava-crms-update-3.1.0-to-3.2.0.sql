-- this update script will go away when ready for the V3.2.0 release. instead the
-- MySQL Workbench Model will be updated, and then users will update by generating a diff
-- script of their database vs. the MySQL Workbench Model

-- until the lava-crms V3.2.0 release is ready, all developers creating schema changes that will be
-- part of lava-crms V3.2.0 should update this script (i.e. the latest version of this script so
-- that you do not overwrite other developers changes)

DROP TABLE IF EXISTS prot_instr;
DROP TABLE IF EXISTS prot_visit;
DROP TABLE IF EXISTS prot_tp;
DROP TABLE IF EXISTS prot_protocol;
DROP TABLE IF EXISTS prot_node;
DROP TABLE IF EXISTS prot_instr_config_option;
DROP TABLE IF EXISTS prot_instr_config;
DROP TABLE IF EXISTS prot_visit_config_option;
DROP TABLE IF EXISTS prot_visit_config;
DROP TABLE IF EXISTS prot_tp_config;
DROP TABLE IF EXISTS prot_protocol_config;
DROP TABLE IF EXISTS prot_node_config_option;
DROP TABLE IF EXISTS prot_node_config;

-- Protocol Config tables

CREATE TABLE prot_node_config (
node_id int NOT NULL AUTO_INCREMENT,
node_type varchar(25) NOT NULL,
optional boolean,
parent_id int, -- will be NULL for Protocol nodes 
list_order int, -- will be NULL for Protocol nodes since they are never children
ProjName varchar(75) NOT NULL,
label varchar(25) NOT NULL,
repeating boolean,
summary varchar(100),
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


CREATE TABLE prot_protocol_config (
node_id int NOT NULL AUTO_INCREMENT,
descrip varchar(255),
category varchar(25),
first_prot_tp_conf_id int,
PRIMARY KEY (node_id),
KEY fk_prot_protocol_config__node_id (node_id),
CONSTRAINT fk_prot_protocol_config__node_id FOREIGN KEY (node_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;

CREATE TABLE prot_tp_config (
node_id int NOT NULL AUTO_INCREMENT,
sched_win_rel_tp_id int,
sched_win_rel_amt smallint,
sched_win_rel_units varchar(10),
sched_win_rel_wknd boolean,
sched_win_rel_hday boolean,
sched_win_days_from_start smallint,
sched_win_size smallint,
sched_win_size_units varchar(10),
sched_win_offset smallint,
sched_win_offset_units varchar(10),
sched_auto boolean,
pri_prot_visit_conf_id int,
collect_win_def boolean,
collect_win_size smallint,
collect_win_size_units varchar(10),
collect_win_offset smallint,
collect_win_offset_units varchar(10),
duration smallint,
rpt_type varchar(10), 
rpt_interval smallint,
rpt_int_units varchar(10),
rpt_int_wknd boolean,
rpt_int_hday boolean,
rpt_init_num smallint,
rpt_create_auto boolean,
PRIMARY KEY (node_id),
KEY fk_prot_tp_config__node_id (node_id),
CONSTRAINT fk_prot_tp_config__node_id FOREIGN KEY (node_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
KEY fk_prot_tp_config__sched_win_rel_tp_id (sched_win_rel_tp_id),
CONSTRAINT fk_prot_tp_config__sched_win_rel_tp_id FOREIGN KEY (sched_win_rel_tp_id) REFERENCES prot_tp_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_visit_config (
node_id int NOT NULL AUTO_INCREMENT,
category varchar(25),
PRIMARY KEY (node_id),
KEY fk_prot_visit_config__node_id (node_id),
CONSTRAINT fk_prot_visit_config__node_id FOREIGN KEY (node_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_instr_config (
node_id int NOT NULL AUTO_INCREMENT,
category varchar(25),
collect_win_def boolean,
collect_win_prot_visit_conf_id int,
collect_win_size smallint,
collect_win_size_units varchar(10),
collect_win_offset smallint,
collect_win_offset_units varchar(10),
default_comp_status varchar(25),
default_comp_reason varchar(25),
default_comp_note varchar(100),
PRIMARY KEY (node_id),
KEY fk_prot_instr_config__node_id (node_id),
CONSTRAINT fk_prot_instr_config__node_id FOREIGN KEY (node_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
KEY fk_prot_instr_config__collect_win_prot_visit_conf_id (collect_win_prot_visit_conf_id),
CONSTRAINT fk_prot_instr_config__collect_win_prot_visit_conf_id FOREIGN KEY (collect_win_prot_visit_conf_id) REFERENCES prot_visit_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_node_config_option (
option_id int NOT NULL AUTO_INCREMENT,
parent_id int NOT NULL,
ProjName varchar(75) NOT NULL, 
default_option boolean,
label varchar(25) NOT NULL,
summary varchar(100),
notes varchar(255),
eff_date date,
exp_date date,
modified timestamp,
PRIMARY KEY (option_id),
KEY fk_prot_node_config_option__parent_id (parent_id),
CONSTRAINT fk_prot_node_config_option__parent_id FOREIGN KEY (parent_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_visit_config_option (
option_id int NOT NULL AUTO_INCREMENT,
ProjName varchar(75),
visit_type varchar(25),
PRIMARY KEY (option_id),
KEY fk_prot_visit_config_option__option_id (option_id),
CONSTRAINT fk_prot_visit_config_option__option_id FOREIGN KEY (option_id) REFERENCES prot_node_config_option (option_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_instr_config_option (
option_id int NOT NULL AUTO_INCREMENT,
instr_type varchar(25),
instr_ver varchar(25),
PRIMARY KEY (option_id),
KEY fk_prot_instr_config_option__option_id (option_id),
CONSTRAINT fk_prot_instr_config_option__option_id FOREIGN KEY (option_id) REFERENCES prot_node_config_option (option_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;



-- (Patient) Protocol tables

CREATE TABLE prot_node (
node_id int NOT NULL AUTO_INCREMENT,
node_type varchar(25) NOT NULL,
parent_id int, -- will be NULL for Protocol nodes 
config_node_id int NOT NULL,
-- list_order int,
PIDN int NOT NULL,
ProjName varchar(75) NOT NULL,
repeat_num smallint,
strategy smallint,
curr_status varchar(25),
curr_reason varchar(25),
curr_note varchar(100),
comp_status varchar(25),
comp_reason varchar(25),
comp_note varchar(100),
comp_status_ovr boolean,
comp_status_computed varchar(25),
comp_by varchar(25),
comp_date date,
sched_win_status varchar(25),
sched_win_reason varchar(25),
sched_win_note varchar(100),
sched_win_anchor_date date,
sched_win_start date,
sched_win_end date,
ideal_sched_win_anchor_date date,
ideal_sched_win_start date,
ideal_sched_win_end date,
VID int,
collect_win_status varchar(25),
collect_win_reason varchar(25),
collect_win_note varchar(100),
-- timepoint default collection window
collect_win_anchor_date date,
collect_win_start date,
collect_win_end date,
ideal_collect_win_anchor_date date,
ideal_collect_win_start date,
ideal_collect_win_end date,
-- instrument collection window
instr_collect_win_start date,
instr_collect_win_end date,
instr_collect_win_anchor_date date,
ideal_instr_collect_win_start date,
ideal_instr_collect_win_end date,
ideal_instr_collect_win_anchor_date date,
InstrID int,
summary varchar(100),
notes varchar(255),
modified timestamp,
PRIMARY KEY (node_id),
-- self referencing FK to the parent of the node 
KEY fk_prot_node__parent_id (parent_id),
CONSTRAINT fk_prot_node__parent_id FOREIGN KEY (parent_id) REFERENCES prot_node (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
KEY fk_prot_node__config_node_id (config_node_id),
CONSTRAINT fk_prot_node__config_node_id FOREIGN KEY (config_node_id) REFERENCES prot_node_config (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
KEY fk_prot_node__VID (VID),
CONSTRAINT fk_prot_node__VID FOREIGN KEY (VID) REFERENCES visit (VID) ON DELETE NO ACTION ON UPDATE NO ACTION,
KEY fk_prot_node__InstrID (InstrID),
CONSTRAINT fk_prot_node__InstrID FOREIGN KEY (InstrID) REFERENCES instrumenttracking (InstrID) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_protocol (
node_id int NOT NULL AUTO_INCREMENT,
assigned_date date,
EnrollStatID int,
PRIMARY KEY (node_id),
KEY fk_prot_protocol__node_id (node_id),
CONSTRAINT fk_prot_protocol__node_id FOREIGN KEY (node_id) REFERENCES prot_node (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
KEY fk_prot_protocol__EnrollStatID (EnrollStatID),
CONSTRAINT fk_prot_protocol__EnrollStatID FOREIGN KEY (EnrollStatID) REFERENCES enrollmentstatus (EnrollStatID) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_tp (
node_id int NOT NULL AUTO_INCREMENT,
pri_prot_visit_id int,
PRIMARY KEY (node_id),
KEY fk_prot_tp__node_id (node_id),
CONSTRAINT fk_prot_tp__node_id FOREIGN KEY (node_id) REFERENCES prot_node (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;
 

CREATE TABLE prot_visit (
node_id int NOT NULL AUTO_INCREMENT,
PRIMARY KEY (node_id),
KEY fk_prot_visit__node_id (node_id),
CONSTRAINT fk_prot_visit__node_id FOREIGN KEY (node_id) REFERENCES prot_node (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


CREATE TABLE prot_instr (
node_id int NOT NULL AUTO_INCREMENT,
instr_collect_win_prot_visit_id int,
PRIMARY KEY (node_id),
KEY fk_prot_instr__node_id (node_id),
CONSTRAINT fk_prot_instr__node_id FOREIGN KEY (node_id) REFERENCES prot_node (node_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;

