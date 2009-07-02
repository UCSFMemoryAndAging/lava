SELECT 'DELETE from `viewproperty` where `instance`=''lava'' and `scope`=''core'';';
SELECT 'DELETE from `hibernateproperty` where `instance`=''lava'' and `scope`=''core'';';  
SELECT 'DELETE from `listvalues` where `ListID` in (SELECT `ListID` from `list` where `scope`=''core'');';
SELECT 'DELETE from `list` where `scope`=''core'';';
call util_CreateMetadataInsertStatements('lava','core','%');
