-- make re-runnable

update viewproperty set label = 'Content Type' where entity = 'lavaFile' and property = 'contentType';
update viewproperty set context = 'c' where entity = 'lavaFile' and property = 'name';


