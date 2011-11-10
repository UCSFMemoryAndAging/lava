ALTER TABLE `crmsauthrole` DROP FOREIGN KEY `crmsauthrole__RoleID`;
ALTER TABLE `crmsauthuser` DROP FOREIGN KEY `crmsauthuser__UID`;
ALTER TABLE `crmsauthuserrole` DROP FOREIGN KEY `crmsauthuserrole__URID`; 
 
ALTER TABLE `crmsauthrole` DROP INDEX `crmsauthrole__RoleID`;
ALTER TABLE `crmsauthuser` DROP INDEX `crmsauthuser__UID`;
ALTER TABLE `crmsauthuserrole` DROP INDEX `crmsauthuserrole__URID`; 
 
ALTER TABLE `crms_file` DROP FOREIGN KEY `crms_file__patient`;
ALTER TABLE `crms_file` DROP FOREIGN KEY `crms_file__enrollment`;
ALTER TABLE `crms_file` DROP FOREIGN KEY `crms_file__visit`;
ALTER TABLE `crms_file` DROP FOREIGN KEY `crms_file__instrument`;


ALTER TABLE `crms_file` DROP INDEX `crms_file__patient`;
ALTER TABLE `crms_file` DROP INDEX `crms_file__enrollment`;
ALTER TABLE `crms_file` DROP INDEX `crms_file__visit`;
ALTER TABLE `crms_file` DROP INDEX `crms_file__instrument`;

