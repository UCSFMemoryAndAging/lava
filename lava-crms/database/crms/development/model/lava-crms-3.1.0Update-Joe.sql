delimiter $$

CREATE TABLE `crms_file` (
  `id` int(11) NOT NULL,
  `pidn` int(11) default NULL COMMENT '	',
  `enroll_stat_id` int(11) default NULL COMMENT '	',
  `vid` int(11) default NULL COMMENT '		',
  `instr_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `crms_file__patient` (`pidn`),
  KEY `crms_file__enrollment` (`enroll_stat_id`),
  KEY `crms_file__visit` (`vid`),
  KEY `crms_file__instrument` (`instr_id`),
  CONSTRAINT `crms_file__instrument` FOREIGN KEY (`instr_id`) REFERENCES `instrumenttracking` (`InstrID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `crms_file__enrollment` FOREIGN KEY (`enroll_stat_id`) REFERENCES `enrollmentstatus` (`EnrollStatID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `crms_file__patient` FOREIGN KEY (`pidn`) REFERENCES `patient` (`PIDN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `crms_file__visit` FOREIGN KEY (`vid`) REFERENCES `visit` (`VID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

