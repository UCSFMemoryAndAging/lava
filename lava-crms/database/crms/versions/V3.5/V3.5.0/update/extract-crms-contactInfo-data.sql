SELECT 'DELETE from `viewproperty` where `instance`=''lava'' and `scope`=''crms'' and `entity` = ''contactInfo'';';
SELECT 'DELETE from `hibernateproperty` where `instance`=''lava'' and `scope`=''crms'' and `entity` = ''contactInfo'';';  
SELECT 'DELETE from `listvalues` where `instance`=''lava'' and `scope`=''crms'' and `entity` = ''contactInfo'';';  
SELECT 'DELETE from `list` where `instance`=''lava'' and `scope`=''crms'' and `entity` = ''contactInfo'';';  
call util_CreateMetadataInsertStatements('lava','crms','contactInfo');
