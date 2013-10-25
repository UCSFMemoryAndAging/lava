package edu.ucsf.lava.core.logiccheck;

import java.lang.reflect.Field;
import java.util.Map;

import edu.ucsf.lava.core.logiccheck.model.LogicCheck;
import edu.ucsf.lava.core.logiccheck.model.LogicCheckEntityConfig;
import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * This manager centralizes all logic related to Logic Checks.
 * 
 * @author trobbie
 */

public class LogicCheckManager extends LavaManager {

	public static String LOGICCHECK_MANAGER_NAME="logiccheckManager";
	
	protected LogicCheckMappings logiccheckMappings;
	// entityConfigs describe the entities that are being logic checked, specified in beans
	protected LogicCheckEntityConfigs entityConfigs;
	
	public LogicCheckManager() {
		super(LOGICCHECK_MANAGER_NAME);
	}
	
	public LogicCheckMappings getLogiccheckMappings() {
		return logiccheckMappings;
	}

	public void setLogiccheckMappings(LogicCheckMappings logiccheckMappings) {
		this.logiccheckMappings = logiccheckMappings;
	}
	/** 
	 * Logiccheck Mapping access method.
	 * @param entity, code_id
	 * @return
	 */
	protected LogicCheckMapping getLogiccheckMapping(EntityBase entity, LogicCheck lc) {
		return this.logiccheckMappings.getLogiccheckMapping(entity, lc);
	}
	
	/**
	 * Get id based logic used for this entity and property. 
	 * 
	 * @param entity and logiccheck being checked, checkDescDataAppend is used to return custom appended text
	 * @return whetherAnIssue
	 */
	public boolean doLogicCheckPrimaryLogicCustom(EntityBase entity, LogicCheck lc, String[] checkDescDataAppend) throws Exception {
		LogicCheckMapping logiccheckMapping = getLogiccheckMapping(entity, lc);
		if (logiccheckMapping == null) return false;	// it is not guaranteed that an LogicCheckMapping is found to handle this entity-code_id
		return logiccheckMapping.doLogicCheckPrimaryLogicCustom(entity, lc, checkDescDataAppend);
	}
	
	public Map<String,LogicCheckEntityConfig> getEntityConfig() {
		return entityConfigs.getEntityConfigs();
	}
	
	public LogicCheckEntityConfigs getEntityConfigs() {
		return entityConfigs;
	}

	public void setEntityConfigs(LogicCheckEntityConfigs entityConfigs) {
		this.entityConfigs = entityConfigs;
	}

	public Class<EntityBase> getEntityClass(String entityName) {
		if (entityConfigs.get(entityName) == null) {
			return null; // unimplemented instrument
		}
		else {
			return entityConfigs.get(entityName).getClazz();
		}
	}
	
	// get a new entity manager so as to grab filters and entity sets
	public EntityManager getEntityManager(String entityName) {
		Class<EntityBase> clazz = getEntityClass(entityName);
		if (clazz == null) return null;
		EntityManager manager;
		try {
			Field f = clazz.getDeclaredField("MANAGER");
			manager = (EntityManager)f.get(null);
		} catch(NoSuchFieldException ex) {
			return null;
		} catch(IllegalAccessException ex) {
			return null;
		}
		return manager;
	}
}
