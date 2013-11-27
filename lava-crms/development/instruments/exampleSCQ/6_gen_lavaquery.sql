source 1_set_vars.sql

SELECT concat('use ', @db, ';');
SELECT '';
SELECT concat('INSERT IGNORE INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) values(\'', @instance, '\',\'', @lqscope, '\',\'query\',\'', @lqsection, '\',\'', @lqtarget, '\',\'', @instrname, '\',1,1,1);');
SELECT '';

call util_CreateLQView(@table, @instrname);
call util_CreateLQProc(@scope, @lqsection, @lqtarget, @instrname, @table);
