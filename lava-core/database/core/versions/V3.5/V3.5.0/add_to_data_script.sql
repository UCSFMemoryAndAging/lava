-- make re-runnable
-- this will be part of a new lava-core-data-3.5.0.sql script, as well as an update lava-core
-- to 3.5.0 update script

update viewproperty set label = 'Content Type' where entity = 'lavaFile' and property = 'contentType';
update viewproperty set context = 'c' where entity = 'lavaFile' and property = 'name';


