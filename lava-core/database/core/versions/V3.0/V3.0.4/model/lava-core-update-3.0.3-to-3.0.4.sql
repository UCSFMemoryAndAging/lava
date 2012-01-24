SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------                                                                                                                       
-- Table structure for preference                                                                                                                     
-- ----------------------------                                                                                                                       
DROP TABLE IF EXISTS `preference`;
CREATE TABLE `preference` (
  `preference_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) DEFAULT NULL,
  `context` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `visible` int(11) NOT NULL DEFAULT '1',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`preference_id`),
  KEY `preference__user_id` (`user_id`),
  CONSTRAINT `preference__user_id` FOREIGN KEY (`user_id`) REFERENCES `authuser` (`UID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;

-- ----------------------------                                                                                                                       
-- Records                                                                                                                         
-- ----------------------------                                                                                                                       
INSERT INTO `preference` VALUES ('1', null, 'calendar', 'displayRange', 'Default View (e.g. Month, Week)', 'Month', '0', '2010-01-26 14:10:06');
INSERT INTO `preference` VALUES ('2', null, 'calendar', 'showDayLength', 'Sets day length in week or day views to display either full day or work day', 'Work Day', '0', '2010-01-26 17:13:23');

insert into versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
	VALUES ('lava-core-model','3.0.4',NOW(),3,0,4,1);
