-- generate code for table creation
mysql -s -r -u DB_USER -p < 2_gen_table.sql > gen_table.sql

-- run the table creation script
mysql -s -r -u DB_USER -p < gen_table.sql

-- insert metadata into viewproperty and hibernateproperty
mysql -s -r -u DB_USER -p < 3_insert_metadata.sql

-- generate code for lava development
mysql -s -r -u DB_USER -p < 4_gen_code.sql > gen_code.sql

-- optional: generate the metadata insert statements if you want to package up the metadata inserts in one file
mysql -s -r -u DB_USER -p < 5_gen_metadata.sql > gen_metadata.sql

-- generate code for lavaquery procs
mysql -s -r -u DB_USER -p < 6_gen_lavaquery.sql > gen_lavaquery.sql

-- run lavaquery creation script
mysql -s -r -u DB_USER -p < gen_lavaquery.sql

-- if you want to call a stored procedure from the command line, here's an example:
mysql -s -r -u DB_USER -p -e "CALL util_GetCreateFieldTags('ENTITY','SCOPE');" DB_NAME > OUTPUT_FILE
