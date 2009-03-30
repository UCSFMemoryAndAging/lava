package edu.ucsf.lava.crms.assessment.model;

import edu.ucsf.lava.crms.assessment.InstrumentDefinitions;

// instrument configuration data structure
public class InstrumentConfig {
	String instrTypeEncoded;
	InstrumentDefinitions instrumentDefinitions;
	String className;
	Class clazz;
	// possible flows are: "enter", "enterReview", "upload" (temporarily, "print" indicates that
	// the instrument has a CRUD report design file allowing user to print the instrument)
	String supportedFlows; // comma-separated values
	Boolean enterFlow = false;
	Boolean enterReviewFlow = false;
	Boolean collectFlow = false;
	Boolean uploadFlow = false; 
	Boolean crudReport; // temporary, until all instruments have CRUD reports
	// this flags whether an instrument uses the shared instrument flows (lava.assessment.instrument.instrument.*)
	// or has its own flows. this is not configured in the *-dao.xml file but is determined from the *-action.xml
	// flow configuration by the AssessmentService.
	Boolean hasOwnFlows;
	Boolean details = false; // does the instrument have detail records 
	Boolean verify = true; // can the instrument be verified (via double entry) 
	String currentVersionAlias;
	public InstrumentConfig() {};
	
	public String getInstrTypeEncoded() {
		return instrTypeEncoded;
	}


	public void setInstrTypeEncoded(String instrTypeEncoded) {
		this.instrTypeEncoded = instrTypeEncoded;
	}


	public String getClassName() {
		return this.className;
	}
	
	public void setClassName(String className) {
		this.className = className;
		
		try {
			setClazz(Class.forName(className));
		}
		catch (ClassNotFoundException ex){
			//this will have already been logged during addModelClass in InstrumentDao
		}
	}
	
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	
	public Class getClazz() {
		return this.clazz;
	}
	
	public String getSupportedFlows() {
		return this.supportedFlows;
	}
	
	public void setSupportedFlows(String supportedFlows) {
		this.supportedFlows = supportedFlows;
		String[] flows = supportedFlows.split(",");
		for (String flow:flows) {
			if (flow.equalsIgnoreCase("enter")) {
				setEnterFlow(true);
			}
			if (flow.equalsIgnoreCase("enterReview")) {
				setEnterReviewFlow(true);
			}
			if (flow.equalsIgnoreCase("collect")) {
				setCollectFlow(true);
			}
			if (flow.equalsIgnoreCase("upload")) {
				setUploadFlow(true);
			}
			if (flow.equalsIgnoreCase("print")) {
				setCrudReport(true);
			}
		}
/**		
		if (this.enterFlow && this.enterReviewFlow) {
			throw new RuntimeException("Can NOT define both 'enter' and 'enterReview' flow for " + className);
		}
**/		
	}
	
	/**
	 * Convenience method to determine if a particular flow is supported.
	 * @param flow The flow
	 * @return Boolean TRUE if flow supported, FALSE if flow not supported
	 */
	public Boolean supportsFlow(String flow) {
		if (flow.equals("enter")) {
			return getEnterFlow(); 
		}
		if (flow.equals("enterReview")) {
			return getEnterReviewFlow();
		}
		if (flow.equals("collect")) {
			return getCollectFlow();
		}
		if (flow.equals("upload")) {
			return getUploadFlow();
		}
		return Boolean.FALSE;
	}
	
	public void setEnterFlow(Boolean enterFlow) {
		this.enterFlow = enterFlow;
	}
	
	public Boolean getEnterFlow() {
		return this.enterFlow;
	}
	
	public void setEnterReviewFlow(Boolean enterReviewFlow) {
		this.enterReviewFlow = enterReviewFlow;
	}
	
	public Boolean getEnterReviewFlow() {
		return this.enterReviewFlow;
	}
	
	public void setCollectFlow(Boolean collectFlow) {
		this.collectFlow = collectFlow;
	}
	
	public Boolean getCollectFlow() {
		return this.collectFlow;
	}
	
	public void setUploadFlow(Boolean uploadFlow) {
		this.uploadFlow = uploadFlow;
	}
	
	public Boolean getUploadFlow() {
		return this.uploadFlow;
	}

	public void setCrudReport(Boolean crudReport) {
		this.crudReport = crudReport;
	}
	
	public Boolean getCrudReport() {
		return this.crudReport;
	}

	public void setHasOwnFlows(Boolean hasOwnFlows) {
		this.hasOwnFlows = hasOwnFlows;
	}
	
	public Boolean getHasOwnFlows() {
		return this.hasOwnFlows;
	}

	public void setDetails(Boolean details) {
		this.details = details;
	}
	
	public Boolean getDetails() {
		return this.details;
	}

	public void setVerify(Boolean verify) {
		this.verify = verify;
	}
	
	public Boolean getVerify() {
		return this.verify;
	}

	public String getCurrentVersionAlias() {
		return currentVersionAlias;
	}

	public void setCurrentVersionAlias(String currentVersionAlias) {
		this.currentVersionAlias = currentVersionAlias;
	}


	public InstrumentDefinitions getInstrumentDefinitions() {
		return instrumentDefinitions;
	}


	public void setInstrumentDefinitions(InstrumentDefinitions instrumentDefinitions) {
		this.instrumentDefinitions = instrumentDefinitions;
	}


	
}
