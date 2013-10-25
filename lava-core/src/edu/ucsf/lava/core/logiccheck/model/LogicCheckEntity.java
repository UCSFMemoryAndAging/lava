package edu.ucsf.lava.core.logiccheck.model;

import java.util.List;

public interface LogicCheckEntity {
	// get all issues currently assigned to this entity
	// override to support logic checks for sub-classes
	// the resulting list of logic check issues should match ones produced by getLogicChecks()
	public List getLogicCheckIssues();
	
	// get all logic check definitions to be done on this entity
	// override to support logic checks for sub-classes
	// the resulting list of logic checks should match the ones that produce getLogicCheckIssues()
	public List getLogicChecks();
		
	// get all logic checks to be done on this entity because it is a secondary entity
	public List getLogicChecks_Dependents();
	
	// return the summary manager used for this entity
	public LogicCheckSummary.Manager getLogicCheckSummaryManager();
	
	// return the summary used for this entity; this should use getLogicCheckSummaryManager()
	public LogicCheckSummary getLogicCheckSummary();
		
	// override to define *CUSTOM* logic checks supporting dependent entities (i.e. when the custom logic checks multiple entities)
	// before handling, be sure to first do the correct condition checks, like we do in doLogicCheckPrimaryLogic(LogicCheck)
	public List getDependentEntities();
}
