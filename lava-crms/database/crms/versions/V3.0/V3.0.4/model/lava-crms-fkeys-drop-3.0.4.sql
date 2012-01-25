ALTER TABLE `crmsauthrole` DROP FOREIGN KEY `crmsauthrole__RoleID`;
ALTER TABLE `crmsauthuser` DROP FOREIGN KEY `crmsauthuser__UID`;
ALTER TABLE `crmsauthuserrole` DROP FOREIGN KEY `crmsauthuserrole__URID`; 
 
ALTER TABLE `crmsauthrole` DROP INDEX `crmsauthrole__RoleID`;
ALTER TABLE `crmsauthuser` DROP INDEX `crmsauthuser__UID`;
ALTER TABLE `crmsauthuserrole` DROP INDEX `crmsauthuserrole__URID`; 
 
