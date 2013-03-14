-- this was put in the temporary 3.1.0 to 3.2.0 update script

-- -----------------------------------------------------
-- procedure lq_authenticate_user
--
-- This procedure is is used for authenticating a user for LavaQuery usage. 
--
-- Authentication is done against the authuser table. So the user uses the same credentials
-- as in LAVA and can use LAVA to change their password.
-- NOTE: this only works for LAVA users where authenticationType is "LOCAL" (i.e. stored in
-- authuser. it does not work for "UCSF AD", although the LavaQuery xls file could be
-- programmed to try UCSF AD (i.e. LDAP) authentication if LOCAL authentication fails.
--
-- TODO:
-- if this authentication fails, have LavaQuery xls attempt LDAP authentication.
-- -----------------------------------------------------
DROP procedure IF EXISTS `lq_authenticate_user`;
DELIMITER $$

DELIMITER $$
CREATE  PROCEDURE `lq_authenticate_user`(user_login varchar(50),user_password varchar(50))
BEGIN
DECLARE user_id int;

SELECT `UID` into user_id from `authuser` where `Login` = user_login AND `password` = convert(sha2(concat(user_password,'{',`UID`,'}'),256) USING latin1);

IF(user_id > 0) THEN

  SELECT  1 as auth_user;

ELSE

  SELECT 0 as auth_user;

END IF;


END$$

$$
DELIMITER ;


DELIMITER ;


insert into versionhistory(module,version,versiondate,major,minor,fix,updaterequired)
values ('LAVAQUERYAPP','3.0.0','2012-01-27',3,0,0,0);

