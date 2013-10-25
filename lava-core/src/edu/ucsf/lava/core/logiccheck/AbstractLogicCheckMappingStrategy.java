package edu.ucsf.lava.core.logiccheck;

import edu.ucsf.lava.core.logiccheck.model.LogicCheck;
import edu.ucsf.lava.core.model.EntityBase;

public abstract class AbstractLogicCheckMappingStrategy implements LogicCheckMappingStrategy {

	protected LogicCheckMapping logiccheckmapping;
	
	abstract public boolean handlesEntityAndLogicCheck(EntityBase entity, LogicCheck lc);
	abstract public boolean doLogicCheckPrimaryLogicCustom(EntityBase entity, LogicCheck lc, String[] checkDescDataAppend) throws Exception;
	
	public LogicCheckMapping getLogiccheckmapping() {
		return logiccheckmapping;
	}
	
	public void setLogiccheckmapping(LogicCheckMapping logiccheckmapping) {
		this.logiccheckmapping = logiccheckmapping;
	}

	
}
