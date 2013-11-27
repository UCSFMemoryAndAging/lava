source 1_set_vars.sql
SELECT concat('DELETE from datadictionary where instance=\'lava\' and scope=\'', @scope, '\' and entity=\'', @entity, '\';');
SELECT '';
SELECT concat('DELETE from viewproperty where instance=\'lava\' and scope=\'', @scope, '\' and entity=\'', @entity, '\';');
SELECT '';
SELECT concat('DELETE from hibernateproperty where instance=\'lava\' and scope=\'', @scope, '\' and entity=\'', @entity, '\';');
SELECT '';
call util_CreateMetadataInsertStatements(@instance, @scope, @entity);