SELECT 'DELETE from `viewproperty` where `instance`=''lava'' and `scope`=''core-import'';';
SELECT 'DELETE from `hibernateproperty` where `instance`=''lava'' and `scope`=''core-import'';';  
SELECT 'DELETE from `listvalues` where `instance`=''lava'' and `scope`=''core-import'';';  
SELECT 'DELETE from `list` where `instance`=''lava'' and `scope`=''core-import'';';  
call util_CreateMetadataInsertStatements('lava','core-import','%');
