mysql -s -r -u root -p < 2_gen_table.sql > gen_table.sql
mysql -s -r -u root -p < gen_table.sql
mysql -s -r -u root -p < 3_insert_metadata.sql
mysql -s -r -u root -p < 4_gen_code.sql > gen_code.sql
mysql -s -r -u root -p < 5_gen_metadata.sql > gen_metadata.sql
mysql -s -r -u root -p < 6_gen_lavaquery.sql > gen_lavaquery.sql
