package edu.ucsf.lava.core.logiccheck;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.logiccheck.model.LogicCheck;

/**
 * A Strategy pattern interface used to abstract out the logic for determining how to handle custom logic checks 
 * given an entity and logiccheck
 * 
 * @author trobbie
 *
 */

public interface LogicCheckMappingStrategy {
	/** 
	 * Does this logiccheck Mapping handle this entity and logiccheck.
	 * @param entity and logiccheck
	 * @return
	 */
	public boolean handlesEntityAndLogicCheck(EntityBase entity, LogicCheck lc);
	
	/**
	 * Perform primary logic based on entity and this specific logic check 
	 * 
	 * @param entity and logiccheck, checkDescDataAppend is used to return custom appended text
	 * @return whetherAnIssue
	 */
	public boolean doLogicCheckPrimaryLogicCustom(EntityBase entity, LogicCheck lc, String[] checkDescDataAppend) throws Exception;
	
}

