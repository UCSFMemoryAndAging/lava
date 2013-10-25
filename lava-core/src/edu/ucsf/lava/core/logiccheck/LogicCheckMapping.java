package edu.ucsf.lava.core.logiccheck;

import edu.ucsf.lava.core.logiccheck.model.LogicCheck;
import edu.ucsf.lava.core.model.EntityBase;

public interface LogicCheckMapping {
	/**
	 * Return the unique string id of the mapping.
	 * @return
	 */
	public String getId();
	
	/** 
	 * Does this logiccheck Mapping handle this entity and logiccheck.
	 * @param file
	 * @return
	 */
	public boolean handlesEntityAndLogicCheck(EntityBase entity, LogicCheck lc);

	/**
	 * Perform doLogicCheckPrimaryLogicCustom() based on the custom logic for this entity and logiccheck 
	 * 
	 * @param entity and logiccheck being checked, checkDescDataAppend is used to return custom appended text
	 * @return whetherAnIssue
	 */
	public boolean doLogicCheckPrimaryLogicCustom(EntityBase entity, LogicCheck lc, String[] checkDescDataAppend) throws Exception;
}
