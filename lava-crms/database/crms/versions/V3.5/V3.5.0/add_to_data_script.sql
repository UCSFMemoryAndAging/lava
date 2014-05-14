-- make re-runnable


update viewproperty set label = 'Content Type' where entity = 'crmsFile' and property = 'contentType';
update viewproperty set context = 'c' where entity = 'crmsFile' and property = 'name';


-- list 486 = 'CrmsFileContentType'
insert into listvalues (listid, instance, scope, ValueKey, ValueDesc)
values (486,'lava','crms','GENERAL','Chart Scan');

insert into listvalues (listid, instance, scope, ValueKey, ValueDesc)
values (486,'lava','crms','GENERAL','Pathology Report');

insert into listvalues (listid, instance, scope, ValueKey, ValueDesc)
values (486,'lava','crms','GENERAL','Other');


