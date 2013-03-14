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
-- old table that might still be around
DROP TABLE IF EXISTS prot_config_option;
DROP TABLE IF EXISTS prot_node_config;

-- Protocol Config tables

CREATE TABLE `prot_node_config` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `node_type` varchar(25) NOT NULL,
  `optional` tinyint(1) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `list_order` int(11) DEFAULT NULL,
  `ProjName` varchar(75) NOT NULL,
  `label` varchar(25) NOT NULL,
  `repeating` tinyint(1) DEFAULT NULL,
  `summary` varchar(100) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `eff_date` date DEFAULT NULL,
  `exp_date` date DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`node_id`),
  -- self referencing FK to the parent of the node 
  KEY `fk_prot_node_config__parent_id` (`parent_id`),
  CONSTRAINT `fk_prot_node_config__parent_id` FOREIGN KEY (`parent_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=latin1;




CREATE TABLE `prot_protocol_config` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `descrip` varchar(255) DEFAULT NULL,
  `category` varchar(25) DEFAULT NULL,
  `first_prot_tp_conf_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `fk_prot_protocol_config__node_id` (`node_id`),
  CONSTRAINT `fk_prot_protocol_config__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;





CREATE TABLE `prot_tp_config` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `sched_win_rel_tp_id` int(11) DEFAULT NULL,
  `sched_win_rel_amt` smallint(6) DEFAULT NULL,
  `sched_win_rel_units` varchar(10) DEFAULT NULL,
  `sched_win_rel_wknd` tinyint(1) DEFAULT NULL,
  `sched_win_rel_hday` tinyint(1) DEFAULT NULL,
  `sched_win_days_from_start` smallint(6) DEFAULT NULL,
  `sched_win_size` smallint(6) DEFAULT NULL,
  `sched_win_size_units` varchar(10) DEFAULT NULL,
  `sched_win_offset` smallint(6) DEFAULT NULL,
  `sched_win_offset_units` varchar(10) DEFAULT NULL,
  `sched_auto` tinyint(1) DEFAULT NULL,
  `pri_prot_visit_conf_id` int(11) DEFAULT NULL,
  `collect_win_def` tinyint(1) DEFAULT NULL,
  `collect_win_size` smallint(6) DEFAULT NULL,
  `collect_win_size_units` varchar(10) DEFAULT NULL,
  `collect_win_offset` smallint(6) DEFAULT NULL,
  `collect_win_offset_units` varchar(10) DEFAULT NULL,
  `duration` smallint(6) DEFAULT NULL,
  `rpt_type` varchar(10) DEFAULT NULL,
  `rpt_interval` smallint(6) DEFAULT NULL,
  `rpt_int_units` varchar(10) DEFAULT NULL,
  `rpt_int_wknd` tinyint(1) DEFAULT NULL,
  `rpt_int_hday` tinyint(1) DEFAULT NULL,
  `rpt_init_num` smallint(6) DEFAULT NULL,
  `rpt_create_auto` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `fk_prot_tp_config__node_id` (`node_id`),
  KEY `fk_prot_tp_config__sched_win_rel_tp_id` (`sched_win_rel_tp_id`),
  CONSTRAINT `fk_prot_tp_config__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_tp_config__sched_win_rel_tp_id` FOREIGN KEY (`sched_win_rel_tp_id`) REFERENCES `prot_tp_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1;




CREATE TABLE `prot_visit_config` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `fk_prot_visit_config__node_id` (`node_id`),
  CONSTRAINT `fk_prot_visit_config__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;




CREATE TABLE `prot_instr_config` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(25) DEFAULT NULL,
  `collect_win_def` tinyint(1) DEFAULT NULL,
  `collect_win_prot_visit_conf_id` int(11) DEFAULT NULL,
  `collect_win_size` smallint(6) DEFAULT NULL,
  `collect_win_size_units` varchar(10) DEFAULT NULL,
  `collect_win_offset` smallint(6) DEFAULT NULL,
  `collect_win_offset_units` varchar(10) DEFAULT NULL,
  `default_comp_status` varchar(25) DEFAULT NULL,
  `default_comp_reason` varchar(25) DEFAULT NULL,
  `default_comp_note` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `fk_prot_instr_config__node_id` (`node_id`),
  KEY `fk_prot_instr_config__collect_win_prot_visit_conf_id` (`collect_win_prot_visit_conf_id`),
  CONSTRAINT `fk_prot_instr_config__collect_win_prot_visit_conf_id` FOREIGN KEY (`collect_win_prot_visit_conf_id`) REFERENCES `prot_visit_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_instr_config__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=latin1;





CREATE TABLE `prot_node_config_option` (
  `option_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL,
  `ProjName` varchar(75) NOT NULL,
  `default_option` tinyint(1) DEFAULT NULL,
  `label` varchar(25) NOT NULL,
  `summary` varchar(100) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `eff_date` date DEFAULT NULL,
  `exp_date` date DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`option_id`),
  KEY `fk_prot_node_config_option__parent_id` (`parent_id`),
  CONSTRAINT `fk_prot_node_config_option__parent_id` FOREIGN KEY (`parent_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;




CREATE TABLE `prot_visit_config_option` (
  `option_id` int(11) NOT NULL AUTO_INCREMENT,
  `ProjName` varchar(75) DEFAULT NULL,
  `visit_type` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`option_id`),
  KEY `fk_prot_visit_config_option__option_id` (`option_id`),
  CONSTRAINT `fk_prot_visit_config_option__option_id` FOREIGN KEY (`option_id`) REFERENCES `prot_node_config_option` (`option_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;





CREATE TABLE `prot_instr_config_option` (
  `option_id` int(11) NOT NULL AUTO_INCREMENT,
  `instr_type` varchar(25) DEFAULT NULL,
  `instr_ver` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`option_id`),
  KEY `fk_prot_instr_config_option__option_id` (`option_id`),
  CONSTRAINT `fk_prot_instr_config_option__option_id` FOREIGN KEY (`option_id`) REFERENCES `prot_node_config_option` (`option_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;





-- (Patient) Protocol tables

CREATE TABLE `prot_node` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `node_type` varchar(25) NOT NULL,
  `parent_id` int(11) DEFAULT NULL, -- will be NULL for Protocol nodes
  `config_node_id` int(11) NOT NULL,
  -- list_order int,
  `PIDN` int(11) NOT NULL,
  `ProjName` varchar(75) NOT NULL,
  `repeat_num` smallint(6) DEFAULT NULL,
  `strategy` smallint(6) DEFAULT NULL,
  `curr_status` varchar(25) DEFAULT NULL,
  `curr_reason` varchar(25) DEFAULT NULL,
  `curr_note` varchar(100) DEFAULT NULL,
  `comp_status` varchar(25) DEFAULT NULL,
  `comp_reason` varchar(25) DEFAULT NULL,
  `comp_note` varchar(100) DEFAULT NULL,
  `comp_status_ovr` tinyint(1) DEFAULT NULL,
  `comp_status_computed` varchar(25) DEFAULT NULL,
  `comp_by` varchar(25) DEFAULT NULL,
  `comp_date` date DEFAULT NULL,
  `sched_win_status` varchar(25) DEFAULT NULL,
  `sched_win_reason` varchar(25) DEFAULT NULL,
  `sched_win_note` varchar(100) DEFAULT NULL,
  `sched_win_anchor_date` date DEFAULT NULL,
  `sched_win_start` date DEFAULT NULL,
  `sched_win_end` date DEFAULT NULL,
  `ideal_sched_win_anchor_date` date DEFAULT NULL,
  `ideal_sched_win_start` date DEFAULT NULL,
  `ideal_sched_win_end` date DEFAULT NULL,
  `collect_win_status` varchar(25) DEFAULT NULL,
  `collect_win_reason` varchar(25) DEFAULT NULL,
  `collect_win_note` varchar(100) DEFAULT NULL,
  -- timepoint default collection window
  `collect_win_anchor_date` date DEFAULT NULL,
  `collect_win_start` date DEFAULT NULL,
  `collect_win_end` date DEFAULT NULL,
  `ideal_collect_win_anchor_date` date DEFAULT NULL,
  `ideal_collect_win_start` date DEFAULT NULL,
  `ideal_collect_win_end` date DEFAULT NULL,
  -- instrument collection window
  `instr_collect_win_start` date DEFAULT NULL,
  `instr_collect_win_end` date DEFAULT NULL,
  `instr_collect_win_anchor_date` date DEFAULT NULL,
  `ideal_instr_collect_win_start` date DEFAULT NULL,
  `ideal_instr_collect_win_end` date DEFAULT NULL,
  `ideal_instr_collect_win_anchor_date` date DEFAULT NULL,
  `summary` varchar(100) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `conf_sched_win_days_from_start` smallint(6) DEFAULT NULL,
  `VID` int(11) DEFAULT NULL,
  `InstrID` int(11) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  -- self referencing FK to the parent of the node 
  KEY `fk_prot_node__parent_id` (`parent_id`),
  KEY `fk_prot_node__config_node_id` (`config_node_id`),
  KEY `fk_prot_node__VID` (`VID`),
  KEY `fk_prot_node__InstrID` (`InstrID`),
  CONSTRAINT `fk_prot_node__config_node_id` FOREIGN KEY (`config_node_id`) REFERENCES `prot_node_config` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_node__InstrID` FOREIGN KEY (`InstrID`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_node__parent_id` FOREIGN KEY (`parent_id`) REFERENCES `prot_node` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_node__VID` FOREIGN KEY (`VID`) REFERENCES `visit` (`VID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=218 DEFAULT CHARSET=latin1;




CREATE TABLE `prot_protocol` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `assigned_date` date DEFAULT NULL,
  `EnrollStatID` int(11) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `fk_prot_protocol__node_id` (`node_id`),
  KEY `fk_prot_protocol__EnrollStatID` (`EnrollStatID`),
  CONSTRAINT `fk_prot_protocol__EnrollStatID` FOREIGN KEY (`EnrollStatID`) REFERENCES `enrollmentstatus` (`EnrollStatID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_protocol__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=198 DEFAULT CHARSET=latin1;





CREATE TABLE `prot_tp` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `pri_prot_visit_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `fk_prot_tp__node_id` (`node_id`),
  CONSTRAINT `fk_prot_tp__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=215 DEFAULT CHARSET=latin1;




CREATE TABLE `prot_visit` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `VID` int(11) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `fk_prot_visit__node_id` (`node_id`),
  KEY `fk_prot_visit__VID` (`VID`),
  CONSTRAINT `fk_prot_visit__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_visit__VID` FOREIGN KEY (`VID`) REFERENCES `visit` (`VID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=latin1;





CREATE TABLE `prot_instr` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `InstrID` int(11) DEFAULT NULL,
  `instr_collect_win_prot_visit_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `fk_prot_instr__node_id` (`node_id`),
  KEY `fk_prot_instr__InstrID` (`InstrID`),
  CONSTRAINT `fk_prot_instr__InstrID` FOREIGN KEY (`InstrID`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prot_instr__node_id` FOREIGN KEY (`node_id`) REFERENCES `prot_node` (`node_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=218 DEFAULT CHARSET=latin1;




-- the only modification here is that a column name in the temp_linkdata table was changed from pidn to PIDN
-- because when it joins to the Visit or InstrumentTracking tables, which also have a PIDN column, both are 
-- added to the InstrumentGrouping Available Fields and if user moves all Available Fields en masses to Selected
-- Fields, exact (i.e. case-sensitive) duplicates are automatically removed, but if one column is pidn and the
-- other is PIDN then one is not removed and get a subsequent VBA error on the move
DROP procedure IF EXISTS `lq_after_set_linkdata`;
DELIMITER $$

DELIMITER $$
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
#remove duplicates
CREATE TEMPORARY TABLE temp_linkdata (
  PIDN INTEGER NOT NULL,
  link_date DATE NOT NULL,
  link_id INTEGER NOT NULL,
  link_type VARCHAR(50) NOT NULL);

IF method = 'PIDN_DATE' THEN
  #the link_id is an arbitrary incrementing integer, so we exclude it from determining if the row is unique  
  INSERT INTO temp_linkdata(pidn,link_date,link_id,link_type) 
  SELECT pidn,link_date, min(link_id), link_type FROM temp_linkdata1 GROUP BY pidn,link_date,link_type;
ELSE
  #the link_id is driving uniqueness and should be included in the grouping
  INSERT INTO temp_linkdata(pidn,link_date,link_id,link_type) 
  SELECT pidn,link_date, link_id, link_type FROM temp_linkdata1 GROUP BY pidn,link_date,link_id,link_type;
END IF;



ALTER TABLE temp_linkdata ADD INDEX(pidn,link_date,link_id);


END$$

$$
DELIMITER ;


-- -----------------------------------------------------
-- procedure lq_authenticate_user
--
-- This procedure is is used for authenticating a user for LavaQuery usage. 
--
-- Authentication is done against the authuser table. So the user uses the same credentials
-- as in LAVA and can use LAVA to change their password.
-- NOTE: this only works for LAVA users where authenticationType is "LOCAL" (i.e. stored in
-- authuser. it does not work for "UCSF AD", although the LavaQuery xls file could be
-- programmed to try UCSF AD (i.e. LDAP) authentication if LOCAL authentication fails.
--
-- TODO:
-- if this authentication fails, have LavaQuery xls attempt LDAP authentication.
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_authenticate_user`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_authenticate_user`(user_login varchar(50),user_password varchar(50))
BEGIN
DECLARE user_id int;

SELECT `UID` into user_id from `authuser` where `Login` = user_login AND `password` = convert(sha2(concat(user_password,'{',`UID`,'}'),256) USING latin1);

IF(user_id > 0) THEN

  SELECT  1 as auth_user;

ELSE

  SELECT 0 as auth_user;

END IF;


END$$

$$
DELIMITER ;


DELIMITER ;


insert into versionhistory(module,version,versiondate,major,minor,fix,updaterequired)
values ('LAVAQUERYAPP','3.0.0','2012-01-27',3,0,0,0);


ALTER TABLE `projectunit`
  ADD UNIQUE INDEX `projectunit_ProjUnitDesc_unique` (`ProjUnitDesc` ASC) ;

DELIMITER ;
