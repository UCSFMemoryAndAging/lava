SELECT 'DELETE from `viewproperty` where `instance`=''lava'' and `scope`=''crms'';';
SELECT 'DELETE from `hibernateproperty` where `instance`=''lava'' and `scope`=''crms'';';  
SELECT 'DELETE from `listvalues` where `instance`=''lava'' and `scope`=''crms'';';  
SELECT '-- need to delete ''crms-app-demo'' listvalues which use ''crms'' scope lists because otherwise';
SELECT '-- will have FK constraint violations when delete from list where scope is ''crms''';
SELECT '-- modify accordingly for apps other than the demo app, and store the modified file in the app project under a database/local/crms folder
SELECT 'DELETE from `listvalues` where `instance`=''lava'' and `scope`=''crms-app-demo''; -- ''crms-app-demo'' data can restored by running lava-app-demo demo-ucsf-config-data-3.1.0.sql and demo-uds-config-data-3.1.0.sql';
SELECT '-- ''InstrumentVersions'' is unique in that it is a ''crms'' scope list that has ''crms-nacc'' scope listvalues (for apps that use';
SELECT '-- ''crms-nacc'') so cannot delete the list because there will be FK constraint violation.';
SELECT '-- for all non-nacc apps, list values have app scope so not an issue, i.e. list values will have been deleted above so do not have to deal with InstrumentVersions list values in ''crms-nacc'' scope';
SELECT 'DELETE from `list` where `instance`=''lava'' and `scope`=''crms'' and listname != ''InstrumentVersions'';';
call util_CreateMetadataInsertStatements('lava','crms','%');
