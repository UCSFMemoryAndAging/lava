SELECT 'DELETE from `viewproperty` where `instance`=''lava'' and `scope`=''crms-import'';';
SELECT 'DELETE from `hibernateproperty` where `instance`=''lava'' and `scope`=''crms-import'';';  
SELECT 'DELETE from `listvalues` where `instance`=''lava'' and `scope`=''crms-import'';';  
SELECT 'DELETE from `list` where `instance`=''lava'' and `scope`=''crms-import'';';  
call util_CreateMetadataInsertStatements('lava','crms-import','%');
