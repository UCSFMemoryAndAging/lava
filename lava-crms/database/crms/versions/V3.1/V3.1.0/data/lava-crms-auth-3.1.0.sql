
INSERT INTO `authgroup` (GID,GroupName,EffectiveDate,ExpirationDate,Notes,modified) VALUES (1,'Admins','2012-01-01',NULL,'Admins (can perform all actions)','2012-01-01 00:00:00');
INSERT INTO `authgroup` (GID,GroupName,EffectiveDate,ExpirationDate,Notes,modified) VALUES (2,'Coordinators','2012-01-01',NULL,'Coordinators (can perform all non-admin actions in any project)','2012-01-01 00:00:00');

INSERT INTO `authuser` (UID,UserName,Login,email,phone,AccessAgreementDate,ShortUserName,ShortUserNameRev,EffectiveDate,ExpirationDate,Notes,authenticationType,password,passwordExpiration,passwordResetToken,passwordResetExpiration,failedLoginCount,lastFailedLogin,accountLocked,modified) VALUES (1,'Admin','admin','admin@admin.org','555-555-5555','2009-01-01','Admin','Admin','2009-01-01',NULL,'','XML CONFIG',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2012-01-01 00:00:00');
INSERT INTO `authuser` (UID,UserName,Login,email,phone,AccessAgreementDate,ShortUserName,ShortUserNameRev,EffectiveDate,ExpirationDate,Notes,authenticationType,password,passwordExpiration,passwordResetToken,passwordResetExpiration,failedLoginCount,lastFailedLogin,accountLocked,modified) VALUES (2,'Demo','demo','demo@demo.org','555-555-5555','2009-01-01','Demo','Demo','2009-01-01',NULL,'Demonstration user.','XML CONFIG',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2012-01-01 00:00:00');

INSERT INTO `crmsauthuser` (UID) VALUES (1);
INSERT INTO `crmsauthuser` (UID) VALUES (2);

INSERT INTO `authusergroup` (UGID,UID,GID,Notes,modified) VALUES (1,1,1,NULL,'2012-01-01 00:00:00');
INSERT INTO `authusergroup` (UGID,UID,GID,Notes,modified) VALUES (2,2,2,NULL,'2012-01-01 00:00:00');

INSERT INTO `authrole` (RoleID,RoleName,Notes,modified) VALUES (-1,'DEFAULT PERMISSIONS','This role groups together default permissions that apply to all roles','2012-01-01 00:00:00');
INSERT INTO `authrole` (RoleID,RoleName,Notes,modified) VALUES (1,'SYSTEM ADMIN','System Admin: staff who need full access to administrative functionality and read only access to data.','2012-01-01 00:00:00');
INSERT INTO `authrole` (RoleID,RoleName,Notes,modified) VALUES (2,'COORDINATOR','Project Coordinators: staff with responsibility for recruitment, enrollment, scheduling, assessment, and project administration','2012-01-01 00:00:00');
INSERT INTO `authrole` (RoleID,RoleName,Notes,modified) VALUES (3,'PATIENT ACCESS DENIED','Removes the projects/units matched by the role assignment from the list of projects that give the user patient access privileges.  This does remove access to project data where the user already has access to the patient.','2012-01-01 00:00:00');
INSERT INTO `authrole` (RoleID,RoleName,Notes,modified) VALUES (4,'PROJECT ACCESS DENIED','Access is not granted to patients (or project data) matched by this role assignment.  If user has access to patient from another role, they will have patient access, but not access to project/unit data matched by this role.','2012-01-01 00:00:00');

INSERT INTO `crmsauthrole` (RoleID,PatientAccess,PhiAccess,GhiAccess) VALUES (-1,0,0,0);
INSERT INTO `crmsauthrole` (RoleID,PatientAccess,PhiAccess,GhiAccess) VALUES (1,1,1,0);
INSERT INTO `crmsauthrole` (RoleID,PatientAccess,PhiAccess,GhiAccess) VALUES (2,1,1,0);
INSERT INTO `crmsauthrole` (RoleID,PatientAccess,PhiAccess,GhiAccess) VALUES (3,0,0,0);
INSERT INTO `crmsauthrole` (RoleID,PatientAccess,PhiAccess,GhiAccess) VALUES (4,0,0,0);

INSERT INTO `authuserrole` (URID,RoleID,UID,GID,Notes,modified) VALUES (1,1,NULL,1,NULL,'2012-01-01 00:00:00');
INSERT INTO `authuserrole` (URID,RoleID,UID,GID,Notes,modified) VALUES (2,2,NULL,2,NULL,'2012-01-01 00:00:00');

INSERT INTO `crmsauthuserrole` (URID,Project,Unit) VALUES (1,'*','*');
INSERT INTO `crmsauthuserrole` (URID,Project,Unit) VALUES (2,'*','*');

INSERT INTO `authpermission` (PermID,RoleID,PermitDeny,Scope,Module,Section,Target,Mode,Notes,modified) VALUES (6,1,'PERMIT','*','*','*','*','*','permits access to all modules','2012-01-01 00:00:00');
INSERT INTO `authpermission` (PermID,RoleID,PermitDeny,Scope,Module,Section,Target,Mode,Notes,modified) VALUES (9,2,'PERMIT','*','*','*','*','*','permits access to all modules','2012-01-01 00:00:00');
INSERT INTO `authpermission` (PermID,RoleID,PermitDeny,Scope,Module,Section,Target,Mode,Notes,modified) VALUES (39,-1,'DENY','core','admin','*','*','*','Restricts access to admin module to all users by default','2012-01-01 00:00:00');
INSERT INTO `authpermission` (PermID,RoleID,PermitDeny,Scope,Module,Section,Target,Mode,Notes,modified) VALUES (50,1,'PERMIT','core','admin','*','*','*','Allows admins to access the admin module.','2012-01-01 00:00:00');