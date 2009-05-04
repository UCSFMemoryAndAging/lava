DELIMITER $$


-- run repeatedly while there are still properties with '_' chars                                                                           
-- for an entity
   
DROP PROCEDURE IF EXISTS `util_FixMetadataPropertyNames`$$
CREATE PROCEDURE `util_FixMetadataPropertyNames` (EntityIn varchar(50))
BEGIN

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
property =
CONCAT(SUBSTRING_INDEX(property,'_',1),
UPPER(SUBSTRING(property,LOCATE('_',property)+1,1)),
SUBSTRING(property,LOCATE('_',property)+2))
WHERE entity = EntityIn and property like '%\_%';

END$$

DROP PROCEDURE IF EXISTS `util_AddTableToHibernateProperty`$$
CREATE PROCEDURE  `util_AddTableToHibernateProperty`(TableNameIn varchar (50),EntityIn varchar (50),ScopeIn varchar(25))
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

INSERT INTO `ViewProperty` (`messageCode`,`locale`,`instance`,`scope`,`entity`,`property`,`required`,`maxLength`,`propOrder`)
   SELECT CONCAT('*.',EntityIn, '.',LOWER(LEFT(`COLUMN_NAME`,1)),RIGHT(`COLUMN_NAME`,LENGTH(`COLUMN_NAME`)-1)),
   'en','lava',ScopeIn, EntityIn, CONCAT(LOWER(LEFT(`COLUMN_NAME`,1)),RIGHT(`COLUMN_NAME`,LENGTH(`COLUMN_NAME`)-1)),
        CASE WHEN `IS_NULLABLE`='No' THEN 'Yes' ELSE 'No' END,
        CASE WHEN `CHARACTER_MAXIMUM_LENGTH` < 10000 THEN `CHARACTER_MAXIMUM_LENGTH` ELSE NULL END, `ORDINAL_POSITION`
	FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_NAME`=TableNameIn order by `ORDINAL_POSITION`;
END $$





DROP PROCEDURE IF EXISTS `util_CreateMetadataInsertStatements`$$
CREATE  PROCEDURE `util_CreateMetadataInsertStatements`(InstanceMask varchar(50), ScopeMask varchar(50), EntityMask varchar (50))
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



SELECT CONCAT('INSERT INTO viewproperty (`messageCode`,`locale`,`instance`,`scope`,`prefix`,`entity`,`property`,`section`,',
            '`context`,`style`,`required`,`label`,`maxLength`,`size`,`indentLevel`,`attributes`,`list`,`listAttributes`,',
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
            CASE WHEN `label` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(`label`,'\'','\\\''),''',') END,
            CASE WHEN `maxLength` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`maxLength` as char),',') END,
            CASE WHEN `size` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`size` as char),',') END,
            CASE WHEN `indentLevel` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`indentLevel` as char),',') END,
            CASE WHEN `attributes` IS NULL THEN 'NULL,' ELSE CONCAT('''',`attributes`,''',') END,
            CASE WHEN `list` IS NULL THEN 'NULL,' ELSE CONCAT('''',`list`,''',') END,
            CASE WHEN `listAttributes` IS NULL THEN 'NULL,' ELSE CONCAT('''',`listAttributes`,''',') END,
            CASE WHEN `propOrder` IS NULL THEN 'NULL,' ELSE CONCAT(CAST(`propOrder` as char),',') END,
            CASE WHEN `quickHelp` IS NULL THEN 'NULL,' ELSE CONCAT('''',REPLACE(`quickHelp`,'\'','\\\''),''',') END,
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


END $$


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
CREATE DEFINER=`root`@`localhost` PROCEDURE  `util_GetResultFields`(EntityIn varchar(50), ScopeIn VARCHAR(25))
BEGIN

SELECT CONCAT('           "',`property`,'",')
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

SELECT CONCAT('       <',

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
FROM `HibernateProperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn ORDER BY `DBTable`,`DBOrder`;

SELECT '

	<!-- associations -->



	<!-- filters -->
               <!--define or remove these standard filters-->
               <filter name="projectContext" condition=":projectContext)=[define]"/>
               <filter name="patient" condition=":patientId=[define]"/>


</class>


         <!-- queries  -->



</hibernate-mapping>'
FROM `HibernateProperty` WHERE `Entity` = EntityIn and `Scope`=ScopeIn GROUP BY `Entity`,`Scope`;

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