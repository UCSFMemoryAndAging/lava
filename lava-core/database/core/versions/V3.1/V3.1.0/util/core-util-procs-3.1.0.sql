DELIMITER $$

DROP PROCEDURE IF EXISTS `util_FixMetadataPropertyNames`$$
CREATE PROCEDURE `util_FixMetadataPropertyNames` (EntityIn varchar(50))
BEGIN

-- run repeatedly while there are still properties with '_' chars                                                                           
-- for an entity to remove the '_' char and capitalize the following char,
-- because table column naming convention is to use '_' as word separator
-- while Hibernate and Java property names do not

UPDATE hibernateproperty SET
property =
CONCAT(SUBSTRING_INDEX(property,'_',1),
UPPER(SUBSTRING(property,LOCATE('_',property)+1,1)),
SUBSTRING(property,LOCATE('_',property)+2)),
hibernateProperty =
CONCAT(SUBSTRING_INDEX(hibernateProperty,'_',1),
UPPER(SUBSTRING(hibernateProperty,LOCATE('_',hibernateProperty)+1,1)),
SUBSTRING(hibernateProperty,LOCATE('_',hibernateProperty)+2))
WHERE entity = EntityIn AND hibernateProperty like '%\_%';


UPDATE viewproperty SET
messageCode = CONCAT(SUBSTRING_INDEX(messageCode,'_',1),
UPPER(SUBSTRING(messageCode,LOCATE('_',messageCode)+1,1)),
SUBSTRING(messageCode,LOCATE('_',messageCode)+2)),
property = CONCAT(SUBSTRING_INDEX(property,'_',1),
UPPER(SUBSTRING(property,LOCATE('_',property)+1,1)),
SUBSTRING(property,LOCATE('_',property)+2))
WHERE entity = EntityIn and property like '%\_%';

END$$

DROP PROCEDURE IF EXISTS `util_AddTableToHibernateProperty`$$
CREATE PROCEDURE `util_AddTableToHibernateProperty`(TableNameIn varchar (50),EntityIn varchar (50),ScopeIn varchar(25))
BEGIN

INSERT INTO `hibernateproperty` (`scope`,`entity`,`property`,`dbTable`,`dbColumn`,`dbType`,`dbLength`,
`dbPrecision`,`dbScale`,`dbOrder`,`hibernateProperty`,`hibernateType`,`hibernateClass`,`hibernateNotNull`)
  SELECT ScopeIn, EntityIn, CONCAT(LOWER(LEFT(`COLUMN_NAME`,1)),RIGHT(`COLUMN_NAME`,LENGTH(`COLUMN_NAME`)-1)),
   TableNameIn, `COLUMN_NAME`, `DATA_TYPE`,
   CASE WHEN `CHARACTER_MAXIMUM_LENGTH` < 10000 THEN `CHARACTER_MAXIMUM_LENGTH` ELSE NULL END, `NUMERIC_PRECISION`, `NUMERIC_SCALE`,
    `ORDINAL_POSITION`, CONCAT(LOWER(LEFT(`COLUMN_NAME`,1)),RIGHT(`COLUMN_NAME`,LENGTH(`COLUMN_NAME`)-1)),
		CASE `DATA_TYPE` WHEN 'datetime' THEN 'timestamp'
			WHEN 'float' THEN 'float'
			WHEN 'image' THEN 'binary'
			WHEN 'int' THEN 'long'
			WHEN 'timestamp' THEN 'timestamp'
			WHEN 'text' THEN 'text'
			WHEN 'decimal' THEN 'float'
			WHEN 'numeric' THEN 'float'
			WHEN 'char' THEN 'character'
			WHEN 'nvarchar' THEN 'string'
			WHEN 'binary' THEN 'binary'
			WHEN 'tinyint' THEN 'byte'
			WHEN 'date' THEN 'date'
			WHEN 'time' THEN 'time'
			WHEN 'smalldatetime' THEN 'timestamp'
			WHEN 'varchar' THEN 'string'
			WHEN 'bit' THEN 'boolean'
			WHEN 'smallint' THEN 'short'
			ELSE 'UNMAPPED TYPE' END,
		NULL, CASE WHEN `IS_NULLABLE`='No' THEN 'Yes' ELSE 'No' END
	FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_NAME`=TableNameIn order by `ORDINAL_POSITION`;

END $$


DROP PROCEDURE IF EXISTS `util_AddTableToMetaData`$$
CREATE  PROCEDURE  `util_AddTableToMetaData`(TableNameIn varchar (50),EntityIn varchar (50),ScopeIn varchar(25))
BEGIN

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`entity`,`property`,`required`,`maxLength`,`propOrder`)
   SELECT CONCAT('*.',EntityIn, '.',LOWER(LEFT(`COLUMN_NAME`,1)),RIGHT(`COLUMN_NAME`,LENGTH(`COLUMN_NAME`)-1)),
   'en','lava',ScopeIn, EntityIn, CONCAT(LOWER(LEFT(`COLUMN_NAME`,1)),RIGHT(`COLUMN_NAME`,LENGTH(`COLUMN_NAME`)-1)),
        CASE WHEN `IS_NULLABLE`='No' THEN 'Yes' ELSE 'No' END,
        CASE WHEN `CHARACTER_MAXIMUM_LENGTH` < 10000 THEN `CHARACTER_MAXIMUM_LENGTH` ELSE NULL END, `ORDINAL_POSITION`
	FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_NAME`=TableNameIn order by `ORDINAL_POSITION`;
END $$




DROP PROCEDURE IF EXISTS `util_CreateMetadataInsertStatements`$$

CREATE PROCEDURE `util_CreateMetadataInsertStatements`(InstanceMask varchar(50), ScopeMask varchar(50), EntityMask varchar (50))
BEGIN

IF InstanceMask IS NULL THEN
  SET InstanceMask = 'lava';
END IF;

IF ScopeMask IS NULL THEN
  SET ScopeMask = '%';
END IF;

IF EntityMask IS NULL THEN
  SET EntityMask = '%';
END IF;

SELECT CONCAT('INSERT INTO datadictionary (`instance`,`scope`,`entity`,`prop_order`,`prop_name`,`prop_description`,`data_values`,`data_calculation`,',
	'`required`,`db_table`,`db_column`,`db_order`,`db_datatype`,`db_datalength`,`db_nullable`,`db_default`,`modified`) VALUES(',
	CASE WHEN `instance` IS NULL THEN 'NULL,' ELSE CONCAT('''',`instance`,''',') END,
	CASE WHEN `scope` IS NULL THEN 'NULL,' ELSE CONCAT('''',`scope`,''',') END,
	CASE WHEN `entity` IS NULL THEN 'NULL,' ELSE CONCAT('''',`entity`,''',') END,
	CASE WHEN `prop_order` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`prop_order` as char),',') END,
	CASE WHEN `prop_name` IS NULL THEN 'NULL,' ELSE CONCAT('''',`prop_name`,''',') END,
	CASE WHEN `prop_description` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(`prop_description`,'''','\\'''),''',') END,
	CASE WHEN `data_values` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(`data_values`,'''','\\'''),''',') END,
	CASE WHEN `data_calculation` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(`data_calculation`,'''','\\'''),''',') END,
	CASE WHEN `required` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`required` as char),',') END,
	CASE WHEN `db_table` IS NULL THEN 'NULL,' ELSE CONCAT('''',`db_table`,''',') END,
	CASE WHEN `db_column` IS NULL THEN 'NULL,' ELSE CONCAT('''',`db_column`,''',') END,
	CASE WHEN `db_order` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`db_order` as char),',') END,
	CASE WHEN `db_datatype` IS NULL THEN 'NULL,' ELSE CONCAT('''',`db_datatype`,''',') END,
	CASE WHEN `db_datalength` IS NULL THEN 'NULL,' ELSE CONCAT('''',`db_datalength`,''',') END,
	CASE WHEN `db_nullable` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`db_nullable` as char),',') END,
	CASE WHEN `db_default` IS NULL THEN 'NULL,' ELSE CONCAT('''',`db_default`,''',') END,
	CASE WHEN `modified` IS NULL THEN 'NULL' ELSE CONCAT('''',CAST(`modified` as char),'''') END,
	');')
	FROM `datadictionary` WHERE `entity` Like EntityMask and `instance` like InstanceMask and `scope` like ScopeMask
	ORDER BY `scope`, `entity`, `prop_order`;

SELECT CONCAT('INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,',
            '`context`,`style`,`required`,`label`,`label2`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,',
            '`propOrder`,`quickHelp`,`modified`) VALUES(',
        	  CASE WHEN `messageCode` IS NULL THEN 'NULL,' ELSE CONCAT('''',`messageCode`,''',') END,
	          CASE WHEN `locale` IS NULL THEN 'NULL,' ELSE CONCAT('''',`locale`,''',') END,
	          CASE WHEN `instance` IS NULL THEN 'NULL,' ELSE CONCAT('''',`instance`,''',') END,
        	  CASE WHEN `scope` IS NULL THEN 'NULL,' ELSE CONCAT('''',`scope`,''',') END,
        	  CASE WHEN `prefix` IS NULL THEN 'NULL,' ELSE CONCAT('''',`prefix`,''',') END,
        	  CASE WHEN `entity` IS NULL THEN 'NULL,' ELSE CONCAT('''',`entity`,''',') END,
        	  CASE WHEN `property` IS NULL THEN 'NULL,' ELSE CONCAT('''',`property`,''',') END,
            CASE WHEN `section` IS NULL THEN 'NULL,' ELSE CONCAT('''',`section`,''',') END,
            CASE WHEN `context` IS NULL THEN 'NULL,' ELSE CONCAT('''',`context`,''',') END,
            CASE WHEN `style` IS NULL THEN 'NULL,' ELSE CONCAT('''',`style`,''',') END,
            CASE WHEN `required` IS NULL THEN 'NULL,' ELSE CONCAT('''',`required`,''',') END,
            CASE WHEN `label` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(`label`,'''','\\'''),''',') END,
            CASE WHEN `label2` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(`label2`,'''','\\'''),''',') END,            
            CASE WHEN `maxLength` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`maxLength` as char),',') END,
            CASE WHEN `size` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`size` as char),',') END,
            CASE WHEN `indentLevel` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`indentLevel` as char),',') END,
            CASE WHEN `attributes` IS NULL THEN 'NULL,' ELSE CONCAT('''',`attributes`,''',') END,
            CASE WHEN `list` IS NULL THEN 'NULL,' ELSE CONCAT('''',`list`,''',') END,
            CASE WHEN `listAttributes` IS NULL THEN 'NULL,' ELSE CONCAT('''',`listAttributes`,''',') END,
            CASE WHEN `propOrder` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`propOrder` as char),',') END,
            CASE WHEN `quickHelp` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(`quickHelp`,'''','\\'''),''',') END,
           CASE WHEN `modified` IS NULL THEN 'NULL' ELSE CONCAT('''',CAST(`modified` as char),'''') END,
            ');')
            FROM `viewproperty` WHERE `entity` Like EntityMask and
                                    `instance` like InstanceMask and
                                    `scope` like ScopeMask
            ORDER BY `entity`, `propOrder`;

SELECT CONCAT('INSERT INTO hibernateproperty (`instance`,`scope`,`entity`,`property`,`dbTable`,`dbColumn`,`dbType`,',
              '`dbLength`,`dbPrecision`,`dbScale`,`dbOrder`,`hibernateProperty`,`hibernateType`,`hibernateClass`,',
              '`hibernateNotNull`,`modified`) VALUES(',
        	  CASE WHEN `instance` IS NULL THEN 'NULL,' ELSE CONCAT('''',`instance`,''',') END,
        	  CASE WHEN `scope` IS NULL THEN 'NULL,' ELSE CONCAT('''',`scope`,''',') END,
        	  CASE WHEN `entity` IS NULL THEN 'NULL,' ELSE CONCAT('''',`entity`,''',') END,
        	  CASE WHEN `property` IS NULL THEN 'NULL,' ELSE CONCAT('''',`property`,''',') END,
            CASE WHEN `dbTable` IS NULL THEN 'NULL,' ELSE CONCAT('''',`dbTable`,''',') END,
            CASE WHEN `dbColumn` IS NULL THEN 'NULL,' ELSE CONCAT('''',`dbColumn`,''',') END,
            CASE WHEN `dbType` IS NULL THEN 'NULL,' ELSE CONCAT('''',`dbType`,''',') END,
            CASE WHEN `dbLength` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`dbLength` as char),',') END,
            CASE WHEN `dbPrecision` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`dbPrecision` as char),',') END,
            CASE WHEN `dbScale` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`dbScale` as char),',') END,
            CASE WHEN `dbOrder` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`dbOrder` as char),',') END,
            CASE WHEN `hibernateProperty` IS NULL THEN 'NULL,' ELSE CONCAT('''',`hibernateProperty`,''',') END,
            CASE WHEN `hibernateType` IS NULL THEN 'NULL,' ELSE CONCAT('''',`hibernateType`,''',') END,
            CASE WHEN `hibernateClass` IS NULL THEN 'NULL,' ELSE CONCAT('''',`hibernateClass`,''',') END,
            CASE WHEN `hibernateNotNull` IS NULL THEN 'NULL,' ELSE CONCAT('''',`hibernateNotNull`,''',') END,
            CASE WHEN `modified` IS NULL THEN 'NULL' ELSE CONCAT('''',CAST(`modified` as char),'''') END,
            ');')
            FROM `hibernateproperty` WHERE `entity` Like EntityMask and
                                    `instance` like InstanceMask and
                                    `scope` like ScopeMask
            ORDER BY `entity`, `dbOrder`;

SELECT CONCAT('INSERT INTO `list` (`ListName`,`instance`,`scope`,`NumericKey`,`modified`) VALUES(',
        	  CASE WHEN `ListName` IS NULL THEN 'NULL,' ELSE CONCAT('''',`ListName`,''',') END,
        	  CASE WHEN `instance` IS NULL THEN 'NULL,' ELSE CONCAT('''',`instance`,''',') END,
        	  CASE WHEN `scope` IS NULL THEN 'NULL,' ELSE CONCAT('''',`scope`,''',') END,
        	  CASE WHEN `NumericKey` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`NumericKey` as char),',') END,
              CASE WHEN `modified` IS NULL THEN 'NULL' ELSE CONCAT('''',CAST(`modified` as char),'''') END,
            ');')
            FROM `list` WHERE `instance` like InstanceMask and `scope` like ScopeMask
            ORDER BY `ListName`; 

SELECT CONCAT('INSERT INTO `listvalues` (`ListID`,`instance`,`scope`,`ValueKey`,`ValueDesc`,`OrderID`,`modified`)',
			' SELECT `ListID`,',
             CASE WHEN lv.`instance` IS NULL THEN 'NULL,' ELSE CONCAT('''',lv.`instance`,''',') END,
        	  CASE WHEN lv.`scope` IS NULL THEN 'NULL,' ELSE CONCAT('''',lv.`scope`,''',') END,
             CASE WHEN lv.`ValueKey` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(lv.`ValueKey`,'''','\\'''),''',') END,
        	  CASE WHEN lv.`ValueDesc` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(lv.`ValueDesc`,'''','\\'''),''',') END,
				  CASE WHEN lv.`OrderID` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(lv.`OrderID` as char),',') END,
              CASE WHEN lv.`modified` IS NULL THEN 'NULL' ELSE CONCAT('''',CAST(lv.`modified` as char),'''') END,
            ' FROM `list` where `ListName`=''',l.`ListName`,''';')
            FROM `listvalues` lv INNER JOIN `list` l on l.`ListId`=lv.`ListID` WHERE  lv.`instance` like InstanceMask and lv.`scope` like ScopeMask
            ORDER BY l.`ListName`, lv.ORDERID, lv.ValueKey;

END


$$


DROP PROCEDURE IF EXISTS `util_GetCreateFieldTags`$$
CREATE  PROCEDURE  `util_GetCreateFieldTags`(EntityIn varchar(50), ScopeIn VARCHAR(25))
BEGIN


SELECT CONCAT('<tags:createField property="',`property`,'" component="${component}"/>')
FROM `viewproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn Order By `propOrder`;


SELECT CONCAT('<tags:createField property="',`property`,'" component="${component}" entity="',
  LOWER(LEFT(`Entity`,1)),RIGHT(`Entity`,LENGTH(`Entity`)-1),'"/>')
FROM `viewproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn Order By `propOrder`;


SELECT CONCAT('<tags:createField property="',`property`,'" component="${component}" entity="${instrTypeEncoded}"/>')
FROM `viewproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn Order By `propOrder`;


SELECT CONCAT('<tags:createField property="',`property`,'" component="',
  LOWER(LEFT(`Entity`,1)),RIGHT(`Entity`,LENGTH(`Entity`)-1),'"/>')
FROM `viewproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn Order By `propOrder`;


SELECT CONCAT('<tags:listField property="',`property`,'" component="${component}" listIndex="${iterator.index}" entityType="',
  LOWER(LEFT(`Entity`,1)),RIGHT(`Entity`,LENGTH(`Entity`)-1),'"/>')
FROM `viewproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn Order By `propOrder`;


END $$



DROP PROCEDURE IF EXISTS `util_GetJavaModelProperties`$$
CREATE  PROCEDURE  `util_GetJavaModelProperties`(EntityIn varchar(50), ScopeIn VARCHAR(25))
BEGIN

SELECT CONCAT('protected ',CASE WHEN `HibernateType` IN('many-to-one','one-to-many','one-to-one') THEN `HibernateClass`
     WHEN `HibernateType` = 'Timestamp' THEN 'Date'
     ELSE CONCAT(UPPER(LEFT(`HibernateType`,1)),RIGHT(`HibernateType`,LENGTH(`HibernateType`)-1)) END,
	  ' ',`HibernateProperty`,';')
FROM `hibernateproperty` WHERE `entity`=EntityIn and `Scope`=ScopeIn ORDER BY `DBTable`,`DBOrder`;

END $$



DROP PROCEDURE IF EXISTS `util_GetResultFields`$$
CREATE PROCEDURE  `util_GetResultFields`(EntityIn varchar(50), ScopeIn VARCHAR(25))
BEGIN

SELECT CONCAT('"',`property`,'",')
FROM `viewproperty`
WHERE `entity`=EntityIn AND context='r' AND `scope`=ScopeIn
ORDER BY `propOrder`;

END $$



DROP PROCEDURE IF EXISTS `util_HibernateMapping`$$
CREATE  PROCEDURE  `util_HibernateMapping`(EntityIn varchar(50), ScopeIn Varchar(25))
BEGIN

SELECT CONCAT('<?xml version="1.0"?>

<!DOCTYPE hibernate-mapping PUBLIC
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping>

<class name="edu.ucsf.memory.[scope].[module].model.', EntityIn,'" table="', EntityIn,'" select-before-update="true">

		<id name="id" type="long">
			<column name="[KEY COLUMN]" not-null="true"/>
			<generator class="identity"/>
		</id>

')
FROM `hibernateproperty` WHERE `Entity` = EntityIn and `Scope` = ScopeIn GROUP BY `Entity`,`Scope`;

SELECT CONCAT('\t\t<',

    CASE WHEN `HibernateType` IN('many-to-one','one-to-many','one-to-one') THEN `HibernateType` ELSE 'property' END,

    ' name="',`HibernateProperty`,
    '" column="',`DBColumn`,'"',

    CASE WHEN `HibernateType` IN('many-to-one','one-to-many','one-to-one')
		   THEN CONCAT(' class="', COALESCE(`HibernateClass`,''),'"') ELSE CONCAT(' type="',`HibernateType`,'"') END,


   CASE WHEN `DBType` IN ('numeric','float','decimal')
		  THEN CONCAT(' precision="',CAST(`DBprecision` as CHAR),
                  '" scale="',CAST(DBScale as CHAR),'"') ELSE ''END,


    CASE WHEN `DBType` IN ('char','varchar','nchar','nvarchar','binary','varbinary')
		  THEN CONCAT(' length="',CAST(`DBLength` as CHAR),'"') ELSE '' END,


    CASE WHEN `HibernateNotNull` = 'Yes' THEN ' not-null="true"' ELSE '' END,'/>')
FROM `hibernateproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn ORDER BY `DBTable`,`DBOrder`;

SELECT '

	<!-- associations -->



	<!-- filters -->
               <!--define or remove these standard filters-->
               <filter name="projectContext" condition=":projectContext)=[define]"/>
               <filter name="patient" condition=":patientId=[define]"/>


</class>


         <!-- queries  -->



</hibernate-mapping>'
FROM `hibernateproperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn GROUP BY `Entity`,`Scope`;

END $$


DROP PROCEDURE IF EXISTS `util_GenerateCode`$$
CREATE PROCEDURE  `util_GenerateCode`(EntityIn varchar(50), ScopeIn VARCHAR(25))
BEGIN

CALL util_HibernateMapping(EntityIn,ScopeIn);
CALL util_GetJavaModelProperties(EntityIn,ScopeIn);
CALL util_GetResultFields(EntityIn,ScopeIn);
CALL util_GetCreateFieldTags(EntityIn,ScopeIn);

END $$



DELIMITER ;


DROP PROCEDURE IF EXISTS `util_ColumnList`;

DELIMITER $$

CREATE PROCEDURE util_ColumnList(TableName varchar(50), Pattern varchar(50))
BEGIN

CALL util_ColumnNameSelect('`###`,', TableName, Pattern);

END

$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `util_ColumnNameSelect`;

DELIMITER $$

CREATE PROCEDURE util_ColumnNameSelect(Statement varchar (1500), TableName varchar(100), ColumnLike varchar(50))
BEGIN

DECLARE strToFind VARCHAR(2000);
DECLARE strToUse VARCHAR(2000);
DECLARE i INT;
DECLARE sqlStmt VARCHAR(2000);

SET strToFind = '###';
SET strToUse = 'COLUMN_NAME';
SET i = LOCATE(strToFind, Statement);

WHILE i <> 0 DO
  SET Statement=CONCAT(LEFT(Statement, i-1), '\', ', strToUse, ', \'', RIGHT(Statement, LENGTH(Statement)-(i+(CASE WHEN LENGTH(strToFind)=0 THEN 0 ELSE LENGTH(strToFind)-1 END))));
  SET i = LOCATE(strToFind, Statement);
END WHILE;

SET sqlStmt = CONCAT('SELECT CONCAT(\'', Statement, '\') FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA=\'', SCHEMA(), '\' AND ', 'TABLE_NAME=\'', TableName, '\' AND COLUMN_NAME LIKE \'', ColumnLike, '\' ORDER BY ORDINAL_POSITION;');

SELECT sqlStmt;
SET @sqlStmt=sqlStmt;
PREPARE stmt FROM @sqlStmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

END

$$
DELIMITER ;


-- --------------------------------------------------------------------------------
-- util_AddEntityToMetaData
-- --------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `util_AddEntityToMetaData`;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `util_AddEntityToMetaData`(EntityIn varchar (50), ScopeIn varchar(25))
BEGIN

INSERT INTO `viewproperty` (`messageCode`,`locale`,`instance`,`scope`,`entity`,`property`,`required`,`maxLength`,`propOrder`)
   SELECT CONCAT('*.',EntityIn, '.',LOWER(LEFT(`prop_name`,1)),RIGHT(`prop_name`,LENGTH(`prop_name`)-1)),
   'en','lava',ScopeIn, EntityIn, CONCAT(LOWER(LEFT(`prop_name`,1)),RIGHT(`prop_name`,LENGTH(`prop_name`)-1)),
        CASE WHEN `required`='1' THEN 'Yes' ELSE 'No' END,
        CASE WHEN `db_datalength` < 10000 THEN `db_datalength` ELSE NULL END, `prop_order`
	FROM `datadictionary` WHERE `entity`=EntityIn AND `scope`=ScopeIn AND prop_name<>'instr_id' order by `prop_order`;
END

$$

DELIMITER ;

-- --------------------------------------------------------------------------------
-- util_AddEntityToHibernateProperty
-- --------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `util_AddEntityToHibernateProperty`;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `util_AddEntityToHibernateProperty`(EntityIn varchar (50),ScopeIn varchar(25))
BEGIN

INSERT INTO `hibernateproperty` (`scope`,`entity`,`property`,`dbTable`,`dbColumn`,`dbType`,`dbLength`,
`dbPrecision`,`dbScale`,`dbOrder`,`hibernateProperty`,`hibernateType`,`hibernateClass`,`hibernateNotNull`)
  SELECT ScopeIn, EntityIn, CONCAT(LOWER(LEFT(`COLUMN_NAME`,1)),RIGHT(`COLUMN_NAME`,LENGTH(`COLUMN_NAME`)-1)),
   db_table, `COLUMN_NAME`, `DATA_TYPE`,
   CASE WHEN `CHARACTER_MAXIMUM_LENGTH` < 10000 THEN `CHARACTER_MAXIMUM_LENGTH` ELSE NULL END, `NUMERIC_PRECISION`, `NUMERIC_SCALE`,
    `ORDINAL_POSITION`, CONCAT(LOWER(LEFT(`COLUMN_NAME`,1)),RIGHT(`COLUMN_NAME`,LENGTH(`COLUMN_NAME`)-1)),
		CASE `DATA_TYPE` WHEN 'datetime' THEN 'timestamp'
			WHEN 'float' THEN 'float'
			WHEN 'image' THEN 'binary'
			WHEN 'int' THEN 'long'
			WHEN 'timestamp' THEN 'timestamp'
			WHEN 'text' THEN 'text'
			WHEN 'decimal' THEN 'float'
			WHEN 'numeric' THEN 'float'
			WHEN 'char' THEN 'character'
			WHEN 'nvarchar' THEN 'string'
			WHEN 'binary' THEN 'binary'
			WHEN 'tinyint' THEN 'byte'
			WHEN 'date' THEN 'date'
			WHEN 'time' THEN 'time'
			WHEN 'smalldatetime' THEN 'timestamp'
			WHEN 'varchar' THEN 'string'
			WHEN 'bit' THEN 'boolean'
			WHEN 'smallint' THEN 'short'
			ELSE 'UNMAPPED TYPE' END,
		NULL, CASE WHEN `IS_NULLABLE`='No' THEN 'Yes' ELSE 'No' END
	FROM `INFORMATION_SCHEMA`.`COLUMNS` c INNER JOIN DataDictionary d on c.TABLE_NAME=d.db_table and c.COLUMN_NAME=d.db_column
  WHERE d.entity=EntityIn AND COLUMN_NAME<>'instr_id' AND TABLE_NAME<>'instrumenttracking'
  ORDER BY `ORDINAL_POSITION`;

END

$$

DELIMITER ;

-- --------------------------------------------------------------------------------
-- util_CreateTable
-- --------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `util_CreateTable`;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `util_CreateTable`(EntityIn varchar (50), ScopeIn varchar(25))
BEGIN

select CONCAT('DROP TABLE IF EXISTS `',db_table, '`;') from datadictionary where entity=EntityIn and scope=ScopeIn group by db_table order by db_table;

select CONCAT('CREATE TABLE IF NOT EXISTS `', db_table, '` (
  `instr_id` INT NOT NULL ,')
from datadictionary where entity=EntityIn and scope=ScopeIn group by db_table order by db_table;

select CONCAT('  `', db_column, '` ', db_datatype,
  case when `db_datalength` is not null and `db_datalength`<>'' then CONCAT('(', db_datalength, ')') else '' end,
  ' ',
  case when `db_nullable`=0 then CONCAT('NOT NULL DEFAULT ',db_default) else CONCAT('NULL DEFAULT ',db_default) end,
' ,'
) from datadictionary where entity=EntityIn and scope=ScopeIn group by db_table, db_order order by db_table, db_order;


select '  PRIMARY KEY (`instr_id`) )
ENGINE = InnoDB;';
END

$$
DELIMITER ;

-- --------------------------------------------------------------------------------
-- util_TableKeysAdd
-- --------------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS `util_TableKeysAdd`;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `util_TableKeysAdd`(EntityIn varchar (50), ScopeIn varchar(25))
BEGIN

select CONCAT('ALTER TABLE `', db_table, '` ADD CONSTRAINT `', db_table, '__instr_id`
 FOREIGN KEY (`instr_id` )
 REFERENCES `instrumenttracking` (`InstrID`)
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `', db_table, '__instr_id` (`instr_id` ASC);')
from datadictionary where entity=EntityIn and scope=ScopeIn group by db_table order by db_table;

END

$$
DELIMITER ;

-- --------------------------------------------------------------------------------
-- util_TableKeysDrop
-- --------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `util_TableKeysDrop`;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `util_TableKeysDrop`(EntityIn varchar (50), ScopeIn varchar(25))
BEGIN

select CONCAT('ALTER TABLE `', db_table, '` DROP FOREIGN KEY `', db_table, '__instr_id`;
ALTER TABLE `', db_table, '` DROP INDEX `', db_table, '__instr_id`;')
from datadictionary where entity=EntityIn and scope=ScopeIn group by db_table order by db_table;

END
$$
DELIMITER ;


