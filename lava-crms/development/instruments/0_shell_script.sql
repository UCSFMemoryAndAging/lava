mysql -s -r -u DB_USER -p < 2_gen_table.sql > gen_table.sql
mysql -s -r -u DB_USER -p < gen_table.sql
mysql -s -r -u DB_USER -p < 3_insert_metadata.sql
mysql -s -r -u DB_USER -p < 4_gen_code.sql > gen_code.sql
mysql -s -r -u DB_USER -p < 5_gen_metadata.sql > gen_metadata.sql
mysql -s -r -u DB_USER -p < 6_gen_lavaquery.sql > gen_lavaquery.sql
mysql -s -r -u DB_USER -p < gen_lavaquery.sql
