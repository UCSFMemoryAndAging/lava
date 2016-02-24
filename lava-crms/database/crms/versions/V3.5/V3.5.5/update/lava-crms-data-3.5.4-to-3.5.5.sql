-- crmsFile.category has a different list than lavaFile.category so needs its own metadata row
INSERT INTO viewproperty(messageCode,locale,instance,scope,entity,property,context,style,required,label,indentLevel,list,modified)
values('*.crmsFile.category','en','lava','crms','crmsFile','category','i','range','No','Category',0,'crmsFile.category','2016-02-06');

INSERT INTO viewproperty(messageCode,locale,instance,scope,prefix,entity,property,context,style,required,label,indentLevel,list,modified)
values('*.crmsFile.projName','en','lava','crms','filter','crmsFile','projName','i','range','No','Project',0,'enrollmentStatus.patientProjects','2016-02-06');

INSERT INTO viewproperty(messageCode,locale,instance,scope,prefix,entity,property,context,style,required,label,indentLevel,list,modified)
values('filter.crmsFile.contentType','en','lava','crms','filter','crmsFile','contentType','i','suggest','No','Content Type',0,'crmsFile.contentType','2016-02-06');


-- TODO: ?get rid of CrmsFile metadata for pidn, enrollStatId, visitId, instrId since not inputting those




DELETE FROM versionhistory WHERE module='lava-crms-data' AND version='3.5.5';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-crms-data','3.5.5','2016-02-22',3,5,5,0);

