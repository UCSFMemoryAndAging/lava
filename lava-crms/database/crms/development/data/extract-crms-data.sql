SELECT 'DELETE from `viewproperty` where `instance`=''lava'' and `scope`=''crms'';';
SELECT 'DELETE from `hibernateproperty` where `instance`=''lava'' and `scope`=''crms'';';  
SELECT 'DELETE from `listvalues` where `instance`=''lava'' and `scope`=''crms'';';  
SELECT 'DELETE from `list` where `instance`=''lava'' and `scope`=''crms'';';  
call util_CreateMetadataInsertStatements('lava','crms','%');
