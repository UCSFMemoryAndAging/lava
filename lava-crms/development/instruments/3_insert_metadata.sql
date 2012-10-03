source 0_set_vars.sql

delete from hibernateproperty where instance=@instance and scope=@scope and entity=@entity;

delete from viewproperty where instance=@instance and scope=@scope and entity=@entity;

call util_AddEntityToMetaData(@entity, @scope);

call util_AddEntityToHibernateProperty(@entity, @scope);

update datadictionary d inner join viewproperty v on (d.entity=v.entity and d.prop_name=v.property)
set v.required=case when d.required=1 then 'Yes' else 'No' end, v.label=d.prop_description, v.quickHelp=d.prop_description
where v.entity=@entity;

update datadictionary d inner join hibernateproperty h on (d.entity=h.entity and d.prop_name=h.property)
set h.property=d.prop_name, h.hibernateProperty=d.prop_name
where h.entity=@entity;