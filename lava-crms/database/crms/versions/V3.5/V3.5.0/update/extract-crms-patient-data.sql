SELECT 'DELETE from `viewproperty` where `instance`=''lava'' and `scope`=''crms'' and `entity` = ''patient'';';
SELECT 'DELETE from `hibernateproperty` where `instance`=''lava'' and `scope`=''crms'' and `entity` = ''patient'';';  
SELECT 'DELETE from `listvalues` where `instance`=''lava'' and `scope`=''crms'' and `entity` = ''patient'';';  
SELECT 'DELETE from `list` where `instance`=''lava'' and `scope`=''crms'' and `entity` = ''patient'';';  
call util_CreateMetadataInsertStatements('lava','crms','patient');
