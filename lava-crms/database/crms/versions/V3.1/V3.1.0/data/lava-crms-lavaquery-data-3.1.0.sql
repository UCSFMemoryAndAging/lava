SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';



DELETE from query_objects where instance='lava' and scope='crms' and module='query' and section<>'nacc';

INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','patient','demographics','Demographics',1,0,1);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','enrollment','status','Enrollment Status',1,0,0);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','scheduling','visits','Visits',1,0,0);
INSERT INTO query_objects(instance,scope,module,section,target,short_desc,standard,primary_link,secondary_link) 
  VALUES('lava','crms','query','assessment','instruments','Instrument Tracking',1,0,0);
  
  
insert into versionhistory(module,version,versiondate,major,minor,fix,updaterequired)
values ('LAVAQUERYAPP','3.0.0','2012-01-27',3,0,0,0);
