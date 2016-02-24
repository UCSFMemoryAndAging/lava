UPDATE viewproperty SET list = null WHERE list = 'lavaFile.fileType';

INSERT INTO viewproperty(messageCode,locale,instance,scope,entity,property,context,style,required,label,indentLevel,modified)
values('*.lavaFile.filePath','en','lava','core','lavaFile','filePath','c','string','No','Path (internal use)',0,'2016-02-06');

INSERT INTO viewproperty(messageCode,locale,instance,scope,entity,property,context,style,required,label,indentLevel,list,modified)
values('*.lavaFile.category','en','lava','core','lavaFile','category','i','range','No','Category',0,'lavaFile.category','2016-02-06');

INSERT INTO viewproperty(messageCode,locale,instance,scope,prefix,entity,property,context,style,required,label,indentLevel,list,modified)
values('filter.lavaFile.category','en','lava','core','filter','lavaFile','category','i','range','No','Category',0,'lavaFile.category','2016-02-06');

INSERT INTO viewproperty(messageCode,locale,instance,scope,prefix,entity,property,context,style,required,label,indentLevel,list,modified)
values('filter.lavaFile.contentType','en','lava','core','filter','lavaFile','contentType','i','suggest','No','Content Type',0,'lavaFile.contentType','2016-02-06');

DELETE FROM listvalues WHERE listid IN (SELECT listid FROM list WHERE listname = 'LavaFileStatus');
DELETE FROM list WHERE listname = 'LavaFileStatus';


DELETE FROM versionhistory WHERE module='lava-core-data' AND version='3.5.3';
INSERT INTO versionhistory(`Module`,`Version`,`VersionDate`,`Major`,`Minor`,`Fix`,`UpdateRequired`)
VALUES ('lava-core-data','3.5.3','2016-02-22',3,5,3,0);


