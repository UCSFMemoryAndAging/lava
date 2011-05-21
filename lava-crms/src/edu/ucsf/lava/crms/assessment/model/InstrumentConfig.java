package edu.ucsf.lava.crms.assessment.model;

import edu.ucsf.lava.crms.assessment.InstrumentDefinitions;

// instrument configuration data structure
public class InstrumentConfig {
	private String instrTypeEncoded;
	private InstrumentDefinitions instrumentDefinitions;
	private String className;
	private Class clazz;
	// possible flows are: "enter", "enterReview", "upload" (temporarily, "print" indicates that
	// the instrument has a CRUD report design file allowing user to print the instrument)
	private String supportedFlows; // comma-separated values
	private Boolean enterFlow = false;
	private Boolean enterReviewFlow = false;
	private Boolean collectFlow = false;
	private Boolean uploadFlow = false; 
	// used to hide instrument from user interface altogether, where the instrument exists in the instrument 
	// metadata table containing the list of instrument names for use by other applications, e.g. lava query
	private Boolean hidden = false; 
	// diagnosis is treated like an instrument as it is instrument-like in terms of being associated with 
	// a visit, being versionable, having an enhanced flow, and having multiple types (requiring
	// the user to specify which type to Add and mapping that to a Java class via instrumentConfig). however,
	// there may be a need to identify a diagnosis as distinct from an instrument, e.g. a listing that includes
	// or excludes diagnoses. this flag facilitates such a distinction. when flagging a diagnosis instrument,
	// set the instrType as well so that a persistent property is available for queries.
	private Boolean diagnosis = false;
	private String instrType; // currently only used hidden or diagnosis set "true"
	private Boolean crudReport; // temporary, until all instruments have CRUD reports
	private Boolean verify = true; // can the instrument be verified (via double entry)
	private String currentVersionAlias;
	
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
  		int editLikeFlows = 0;
  		if (this.enterFlow) {editLikeFlows++;};
  		if (this.enterReviewFlow) {editLikeFlows++;};
  		if (this.uploadFlow) {editLikeFlows++;};
		if (editLikeFlows > 1) {
			throw new RuntimeException("Can NOT define more than one edit-like flow for " + className);
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
	
	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public void setUploadFlow(Boolean uploadFlow) {
		this.uploadFlow = uploadFlow;
	}
	
	public Boolean getUploadFlow() {
		return this.uploadFlow;
	}

	public String getInstrType() {
		return instrType;
	}

	public void setInstrType(String instrType) {
		this.instrType = instrType;
	}

	public void setCrudReport(Boolean crudReport) {
		this.crudReport = crudReport;
	}
	
	public Boolean getCrudReport() {
		return this.crudReport;
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

	public Boolean getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(Boolean diagnosis) {
		this.diagnosis = diagnosis;
	}

	public InstrumentDefinitions getInstrumentDefinitions() {
		return instrumentDefinitions;
	}


	public void setInstrumentDefinitions(InstrumentDefinitions instrumentDefinitions) {
		this.instrumentDefinitions = instrumentDefinitions;
	}


	
}
