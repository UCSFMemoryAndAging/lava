package edu.ucsf.lava.core.logiccheck.model;

import edu.ucsf.lava.core.logiccheck.LogicCheckEntityConfigs;

public class LogicCheckEntityConfig {
	private String entityName;
	LogicCheckEntityConfigs entityConfigs;
	private String className;
	private Class clazz;
	
	public LogicCheckEntityConfig() {};
	
	public String getEntityName() {
		return entityName;
	}
	
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public LogicCheckEntityConfigs getEntityConfigs() {
		return entityConfigs;
	}

	public void setEntityConfigs(LogicCheckEntityConfigs entityConfigs) {
		this.entityConfigs = entityConfigs;
	}

	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
		
		try {
			setClazz(Class.forName(className));
		}
		catch (ClassNotFoundException ex){
			//TODO: may need to log
		}
	}
	
	public Class getClazz() {
		return clazz;
	}
	
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	
	

}
