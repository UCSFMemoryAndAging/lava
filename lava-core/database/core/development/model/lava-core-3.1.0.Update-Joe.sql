delimiter $$

CREATE TABLE `lava_file` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `file_type` varchar(255) default NULL,
  `content_type` varchar(100) default NULL,
  `file_status_date` date default NULL,
  `file_status` varchar(50) default NULL,
  `file_status_by` varchar(50) default NULL,
  `repository_id` varchar(100) default NULL,
  `file_id` varchar(100) default NULL,
  `location` varchar(1000) default NULL,
  `checksum` varchar(100) default NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`),
  KEY `content_type` (`id`,`content_type`),
  KEY `repository_info` (`id`,`repository_id`,`file_id`,`location`(767))
) ENGINE=InnoDB  DEFAULT CHARSET=latin1$$



