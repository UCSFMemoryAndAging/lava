SELECT 'DELETE from viewproperty where instance=\'lava\' and scope=\'core\';';
SELECT 'DELETE from hibernateproperty where instance=\'lava\' and scope=\'core\';';  

call util_CreateMetadataInsertStatements('lava','core','%');
