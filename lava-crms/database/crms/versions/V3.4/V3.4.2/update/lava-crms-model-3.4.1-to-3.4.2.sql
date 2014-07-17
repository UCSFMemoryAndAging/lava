ALTER TABLE `contactinfo` ADD COLUMN `PrefContact` varchar(20) NULL DEFAULT NULL AFTER `Country`; 
ALTER TABLE `contactinfo` ADD COLUMN `Phone1BestTime` varchar(30) NULL DEFAULT NULL AFTER `PhoneType1`;
ALTER TABLE `contactinfo` ADD COLUMN `Phone2BestTime` varchar(30) NULL DEFAULT NULL AFTER `PhoneType2`;
ALTER TABLE `contactinfo` ADD COLUMN `Phone3BestTime` varchar(30) NULL DEFAULT NULL AFTER `PhoneType3`;

INSERT INTO versionhistory(module,version,versiondate,major,minor,fix,updaterequired)
VALUES ('lava-crms-model','3.4.2','2014-07-11',3,4,2,0);
