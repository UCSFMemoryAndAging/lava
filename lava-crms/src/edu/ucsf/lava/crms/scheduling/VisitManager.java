package edu.ucsf.lava.crms.scheduling;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.crms.scheduling.model.Visit;

public class VisitManager extends LavaManager  {
	public static String VISIT_MANAGER_NAME = "visitManager";
	public static final String ANY_PROJECT_KEY="ANY";
	public static final String ANY_VISIT_KEY="ANY";
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
    protected Map<String,Visit> visitPrototypes;
	protected Visit defaultVisitPrototype;


	public VisitManager(){
		super(VISIT_MANAGER_NAME);
	}

	/* 
	 * gets an visit prototype based on the project name and visit type spcified
	 * Use the following order (most specific to least specific):
	 * 	projName-visitType
	 * 	projName(without unit)-visitType
	 * 	any-visitType
	 * 	projName-any
	 * 	projName(without Unit)-any
	 */
	public Visit getVisitPrototype(String projName, String visitType) {
		Visit prototype = getDefaultVisitPrototype(); 
		String lookupKey = null;
		String projectNoUnit = null;
		
		if(visitPrototypes==null || visitPrototypes.size()==0 ||
			(projName == null && visitType == null)){
			return (Visit) prototype.deepClone();
		}
		//set projname and visittype to default keys if not provided
		if(projName==null){ projName=ANY_PROJECT_KEY;}
		if(visitType==null){ visitType=ANY_VISIT_KEY;}
		
		//try projname-visittype
		lookupKey = new StringBuffer(projName).append("~").append(visitType).toString();
		if(visitPrototypes.containsKey(lookupKey)){
			return (Visit) visitPrototypes.get(lookupKey).deepClone();
		}
			
		//try projname(without Unit)-visittype
		if(projName.contains("[")){
			projectNoUnit = projName.substring(0, projName.indexOf("[")).trim();
			lookupKey = new StringBuffer(projectNoUnit).append("~").append(visitType).toString();
			if(visitPrototypes.containsKey(lookupKey)){
				return (Visit) visitPrototypes.get(lookupKey).deepClone();
			}
		}
		//try any project-visittype
		lookupKey = new StringBuffer(ANY_PROJECT_KEY).append("~").append(visitType).toString();
		if(visitPrototypes.containsKey(lookupKey)){
			return (Visit) visitPrototypes.get(lookupKey).deepClone();
		}
		//try projname-any visit type
		lookupKey = new StringBuffer(projName).append("~").append(ANY_VISIT_KEY).toString();
		if(visitPrototypes.containsKey(lookupKey)){
			return (Visit) visitPrototypes.get(lookupKey).deepClone();
		}
		//try (projname without unit)-any visit type
		if(projName.contains("[")){
			lookupKey = new StringBuffer(projectNoUnit).append("~").append(ANY_VISIT_KEY).toString();
			if(visitPrototypes.containsKey(lookupKey)){
				return (Visit) visitPrototypes.get(lookupKey).deepClone();
			}
		}
		return (Visit) prototype.deepClone();
		
	}

	public Visit getDefaultVisitPrototype() {
		if(defaultVisitPrototype==null){
			defaultVisitPrototype = (Visit)Visit.MANAGER.create();
		}
		return defaultVisitPrototype;
	}



	public void setDefaultVisitPrototype(Visit defaultVisitPrototype) {
		this.defaultVisitPrototype = defaultVisitPrototype;
	}


	
	

	public Map<String, Visit> getVisitPrototypes() {
		return visitPrototypes;
	}



	public void setVisitPrototypes(Map<String, Visit> visitPrototypes) {
		this.visitPrototypes = visitPrototypes;
	}
	
}

