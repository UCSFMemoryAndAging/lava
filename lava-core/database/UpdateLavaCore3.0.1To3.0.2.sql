DROP TABLE `lavasession`;

CREATE TABLE  `lava_session` (
  `lava_session_id` int(10) NOT NULL auto_increment,
  `server_instance_id` int(10) NOT NULL,
  `http_session_id` varchar(64) default NULL,
  `current_status` varchar(25) NOT NULL default 'NEW',
  `user_id` int(10) default NULL,
  `user_name` varchar(50) default NULL,
  `host_name` varchar(50) default NULL,
  `create_timestamp` timestamp NULL default NULL,
  `access_timestamp` timestamp NULL default NULL,
  `expire_timestamp` datetime default NULL,
  `disconnect_date` date default NULL,
  `disconnect_time` time default NULL,
  `disconnect_message` varchar(500) default NULL,
  `notes` varchar(255) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`lava_session_id`)
) ENGINE=InnoDB AUTO_INCREMENT=233 DEFAULT CHARSET=latin1;


DROP TABLE `audit_event_history`;

CREATE TABLE  `audit_event_history` (
  `audit_event_id` int(10) NOT NULL auto_increment,
  `audit_user` varchar(50) NOT NULL,
  `audit_host` varchar(25) NOT NULL,
  `audit_timestamp` timestamp NULL,
  `action` varchar(255) NOT NULL,
  `action_event` varchar(50) NOT NULL,
  `action_id_param` varchar(50) default NULL,
  `event_note` varchar(255) default NULL,
  `exception` varchar(255) default NULL,
  `exception_message` varchar(255) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`audit_event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE `audit_event_work`;

CREATE TABLE  `audit_event_work` (
  `audit_event_id` int(10) NOT NULL auto_increment,
  `audit_user` varchar(50) NOT NULL,
  `audit_host` varchar(25) NOT NULL,
  `audit_timestamp` timestamp NULL,
  `action` varchar(255) NOT NULL,
  `action_event` varchar(50) NOT NULL,
  `action_id_param` varchar(50) default NULL,
  `event_note` varchar(255) default NULL,
  `exception` varchar(255) default NULL,
  `exception_message` varchar(255) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`audit_event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=969 DEFAULT CHARSET=latin1;

DROP VIEW Audit_event;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW  `audit_event` AS select `audit_event_work`.`audit_event_id` AS `audit_event_id`,`audit_event_work`.`audit_user` AS `audit_user`,`audit_event_work`.`audit_host` AS `audit_host`,`audit_event_work`.`audit_timestamp` AS `audit_timestamp`,`audit_event_work`.`action` AS `action`,`audit_event_work`.`action_event` AS `action_event`,`audit_event_work`.`action_id_param` AS `action_id_param`,`audit_event_work`.`event_note` AS `event_note`,`audit_event_work`.`exception` AS `exception`,`audit_event_work`.`exception_message` AS `exception_message`,`audit_event_work`.`modified` AS `modified` from `audit_event_work` union all select `audit_event_history`.`audit_event_id` AS `audit_event_id`,`audit_event_history`.`audit_user` AS `audit_user`,`audit_event_history`.`audit_host` AS `audit_host`,`audit_event_history`.`audit_timestamp` AS `audit_timestamp`,`audit_event_history`.`action` AS `action`,`audit_event_history`.`action_event` AS `action_event`,`audit_event_history`.`action_id_param` AS `action_id_param`,`audit_event_history`.`event_note` AS `event_note`,`audit_event_history`.`exception` AS `exception`,`audit_event_history`.`exception_message` AS `exception_message`,`audit_event_history`.`modified` AS `modified` from `audit_event_history`;

DROP TABLE `audit_property_history`;
CREATE TABLE  `audit_property_history` (
  `audit_property_id` int(10) NOT NULL auto_increment,
  `audit_entity_id` int(10) NOT NULL,
  `property` varchar(100) NOT NULL,
  `index_key` varchar(100) default NULL,
  `subproperty` varchar(255) default NULL,
  `old_value` varchar(255) NOT NULL,
  `new_value` varchar(255) NOT NULL,
  `audit_timestamp` timestamp NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`audit_property_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE `audit_property_work`;

CREATE TABLE `audit_property_work` (
  `audit_property_id` int(10) NOT NULL auto_increment,
  `audit_entity_id` int(10) NOT NULL,
  `property` varchar(100) NOT NULL,
  `index_key` varchar(100) default NULL,
  `subproperty` varchar(255) default NULL,
  `old_value` varchar(255) NOT NULL,
  `new_value` varchar(255) NOT NULL,
  `audit_timestamp` timestamp NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`audit_property_id`)
) ENGINE=InnoDB AUTO_INCREMENT=284 DEFAULT CHARSET=latin1;
DROP VIEW audit_property;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW  `audit_property` AS select `audit_property_work`.`audit_property_id` AS `audit_property_id`,`audit_property_work`.`audit_entity_id` AS `audit_entity_id`,`audit_property_work`.`property` AS `property`,`audit_property_work`.`index_key` AS `index_key`,`audit_property_work`.`subproperty` AS `subproperty`,`audit_property_work`.`old_value` AS `old_value`,`audit_property_work`.`new_value` AS `new_value`,`audit_property_work`.`audit_timestamp` AS `audit_timestamp`,`audit_property_work`.`modified` AS `modified` from `audit_property_work` union all select `audit_property_history`.`audit_property_id` AS `audit_property_id`,`audit_property_history`.`audit_entity_id` AS `audit_entity_id`,`audit_property_history`.`property` AS `property`,`audit_property_history`.`index_key` AS `index_key`,`audit_property_history`.`subproperty` AS `subproperty`,`audit_property_history`.`old_value` AS `old_value`,`audit_property_history`.`new_value` AS `new_value`,`audit_property_history`.`audit_timestamp` AS `audit_timestamp`,`audit_property_history`.`modified` AS `modified` from `audit_property_history`;



ALTER TABLE authuser CHANGE EffectiveDate EffectiveDate DATE NOT NULL;
ALTER TABLE authuser CHANGE ExpirationDate ExpirationDate DATE NULL DEFAULT NULL;
ALTER TABLE authuser CHANGE AccessAgreementDate AccessAgreementDate DATE NULL DEFAULT NULL;
ALTER TABLE authgroup CHANGE EffectiveDate EffectiveDate DATE NOT NULL;
ALTER TABLE authgroup CHANGE ExpirationDate ExpirationDate DATE NULL DEFAULT NULL;

