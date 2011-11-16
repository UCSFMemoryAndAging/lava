-- add label2 to viewproperty table; if it already exists then just ignore the error
ALTER TABLE `viewproperty` ADD COLUMN `label2` varchar(25) AFTER `label`;

-- --------------------------------------------------------------------------------
-- datadictionary
-- --------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `datadictionary` (
`id` int NOT NULL AUTO_INCREMENT,
`instance` varchar(25) NOT NULL DEFAULT 'lava',
`scope` varchar(25) NOT NULL,
`entity` varchar(100) NOT NULL,
`prop_order` smallint DEFAULT NULL ,
`prop_name` varchar(100) DEFAULT NULL,
`prop_description` varchar(500) DEFAULT NULL,
`data_values` varchar(500) DEFAULT NULL,
`data_calculation` varchar(500) DEFAULT NULL,
`required` tinyint DEFAULT '1',
`db_table` varchar(50) DEFAULT NULL,
`db_column` varchar(50) DEFAULT NULL,
`db_order` smallint DEFAULT NULL,
`db_datatype` varchar(20) DEFAULT NULL,
`db_datalength` varchar(20) DEFAULT NULL,
`db_nullable` tinyint DEFAULT '1',
`db_default` varchar(50) DEFAULT NULL,
`modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (`id`)
) ENGINE = InnoDB; 


