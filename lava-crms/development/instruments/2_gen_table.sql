source 1_set_vars.sql

DELETE from datadictionary where instance=@instance and scope=@scope and entity=@entity;

--PUT INSERT STATEMENTS FROM datadictionary_template.xlsx HERE

SELECT concat('use ', @db, ';');
SELECT '';
call util_createtable(@entity,@scope);
SELECT '';
call util_tablekeysadd(@entity,@scope);
SELECT '';
SELECT concat('INSERT IGNORE into instrument(instrname, tablename, formname, hasversion) values(\'', @instrname, '\',\'', @table, '\',\'LavaWebOnly\',0);');