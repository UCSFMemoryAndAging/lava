package edu.ucsf.lava.core.logiccheck;

import edu.ucsf.lava.core.logiccheck.model.LogicCheck;
import edu.ucsf.lava.core.model.EntityBase;

/**
 * Low level common functionality for logiccheck mappings.  The BaseLogicCheckMapping here does not
 * *guarantee* being the default LogicCheckMapping, since the bean referencing this can be overridden.
 * 
 * @author trobbie
 */

public class BaseLogicCheckMapping implements LogicCheckMapping {
	/**
	 * unique string for this mapping, defined in bean
	 */
	protected String id;
	
	/**
	 * Deferred implementation details pattern object.
	 */
	protected LogicCheckMappingStrategy strategy;
	
	protected LogicCheckMappings logiccheckMappings;
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public LogicCheckMappingStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(LogicCheckMappingStrategy strategy) {
		this.strategy = strategy;
	}

	public LogicCheckMappings getLogiccheckMappings() {
		return logiccheckMappings;
	}

	public void setLogiccheckMappings(LogicCheckMappings logiccheckMappings) {
		this.logiccheckMappings = logiccheckMappings;
		this.logiccheckMappings.addLogiccheckMapping(this);
	}
	
	public boolean handlesEntityAndLogicCheck(EntityBase entity, LogicCheck lc) {
		// if no strategy assigned, then return false
		// i.e. let the defaultLavaLogiccheckMapping bean definition determine whether there is a default handling of logic checks, and
		// if so to reference a strategy that returns true for all handling
		if (this.getStrategy() == null) return false;
		return this.getStrategy().handlesEntityAndLogicCheck(entity, lc);
	}
	
	public boolean doLogicCheckPrimaryLogicCustom(EntityBase entity, LogicCheck lc, String[] checkDescDataAppend) throws Exception {
		// no need to check for null strategy since can only be called if strategy exists and handles it
		return this.getStrategy().doLogicCheckPrimaryLogicCustom(entity, lc, checkDescDataAppend);
	}
}
