SELECT 'DELETE from viewproperty where instance=\'lava\' and scope=\'crms\';';
SELECT 'DELETE from hibernateproperty where instance=\'lava\' and scope=\'crms\';';  
SELECT 'DELETE from `listvalues` where `ListID` in (SELECT `ListID` from `list` where `scope`=''crms'');';
SELECT 'DELETE from `list` where `scope`=''crms'';';
call util_CreateMetadataInsertStatements('lava','crms','%');
