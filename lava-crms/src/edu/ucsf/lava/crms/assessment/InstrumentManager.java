package edu.ucsf.lava.crms.assessment;

import static edu.ucsf.lava.core.action.ActionUtils.LAVA_INSTANCE_IDENTIFIER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentConfig;



public class InstrumentManager extends LavaManager {
	
	public static String INSTRUMENT_MANAGER_NAME = "instrumentManager";
	private boolean configHasOwnFlows = false;

	public static final String ANY_PROJECT_KEY="ANY";
	public static final String ANY_VISIT_KEY="ANY";
	protected Map<String,List<Instrument>> instrumentPrototypes;
	protected InstrumentDefinitions instrumentDefinitions;
	protected ActionManager actionManager;
	

	public InstrumentManager(){
		super(INSTRUMENT_MANAGER_NAME);
	}

	/* getInstrumentConfig
	 * 
	 * Returns a map of instrument configurations, which include which individual instrument config data
	 * such as whether the instrument has been implemented yet, which flows an instrument uses, whether
	 * it has its own flows or shares the core instrument flows, etc.
	 * The key value is the instrument type encoded, as defined in the Instrument class. 
	 */
		public Map<String,InstrumentConfig> getInstrumentConfig() 
	{
		// instrument configuration data for all implemented instruments. the key to the map is
		// instrTypeEncoded. this map is also used to determine which instruments have been implemented
		// or not; only implemented instruments appear in the map, so if a key is not present it
		// means that instrument is not implemented

		
		// in addition to the configuration data from the *-dao.xml, need to set the
		// hasOwnFlows flag based on flow configuration data in *-actions.xml
		
		// iterate over the actions, and when the action matches the instrument, determine
		// whether it has its own flow or not via its action's flowType
		if (!configHasOwnFlows) {configureHasOwnFlows();}
		return instrumentDefinitions.getDefinitions();
	}
		
	protected void configureHasOwnFlows(){
		configHasOwnFlows = true;
		
		for (Map.Entry<String, InstrumentConfig> instrConfigEntry : instrumentDefinitions.getDefinitions().entrySet()) {
			
			// do not consider aliases since the alias may not match an action, and jsp's will
			// never use the alias to reference hasOwnFlows
			if (instrConfigEntry.getValue().getCurrentVersionAlias() != null 
					&& instrConfigEntry.getKey().equals(instrConfigEntry.getValue().getCurrentVersionAlias())) {
				continue;
			}
			
			String instrTypeEncoded = instrConfigEntry.getKey();
			InstrumentConfig instrConfig = instrConfigEntry.getValue();
			
			// all MAC Diagnosis versions have their own flows because they have a custom handler, MacdiagnosisHandler
			if (instrTypeEncoded.startsWith("macdiagnosis")) {
				instrConfig.setHasOwnFlows(true);
				continue;
			}
			
			Action instrAction = null;
			
			// first see if there is a core action for the instrument
			StringBuffer coreActionId = new StringBuffer(LAVA_INSTANCE_IDENTIFIER).append(".crms.assessment.instrument.")
				.append(instrTypeEncoded);
			if ((instrAction = actionManager.getActionRegistry().getAction(coreActionId.toString())) != null) {
				// any flow type other than "instrumentCommon" means the instrument has its own flows
				if (!instrAction.getFlowType().equals("instrumentCommon")) {
					instrConfig.setHasOwnFlows(true);
					continue;
				}
			}
			
			// if there is no core action, see if there is an instance specific action
			StringBuffer instanceActionId = new StringBuffer(actionManager.webappInstanceName).append(".crms.assessment.instrument.")
				.append(instrTypeEncoded);
			if ((instrAction = actionManager.getActionRegistry().getAction(instanceActionId.toString())) != null) {
				if (!instrAction.getFlowType().equals("instrumentCommon")) {
					instrConfig.setHasOwnFlows(true);
					continue;
				}
			}
				
			instrConfig.setHasOwnFlows(false);
		}
	}
	
	public Class getInstrumentClass(String instrTypeEncoded) {
		if (instrumentDefinitions.get(instrTypeEncoded) == null) {
			return null; // unimplemented instrument
		}
		else {
			return instrumentDefinitions.get(instrTypeEncoded).getClazz();
		}
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
	public List getInstrumentPrototypes(String projName, String visitType) {
		List<Instrument> prototypes = null;
		List<Instrument> clones = new ArrayList<Instrument>();
		Boolean found = false;
		String lookupKey = null;
		String projectNoUnit = null;
		
		if(instrumentPrototypes==null || instrumentPrototypes.size()==0 ||
			(projName == null && visitType == null)){
			return clones;
		}
		//set projname and visittype to default keys if not provided
		if(projName==null){ projName=ANY_PROJECT_KEY;}
		if(visitType==null){ visitType=ANY_VISIT_KEY;}
		
		//try projname-visittype
		lookupKey = new StringBuffer(projName).append("~").append(visitType).toString();
		if(instrumentPrototypes.containsKey(lookupKey)){
			prototypes = instrumentPrototypes.get(lookupKey);
			found = true;
		}
			
		//try projname(without Unit)-visittype
		if(!found && projName.contains("[")){
			projectNoUnit = projName.substring(0, projName.indexOf("[")).trim();
			lookupKey = new StringBuffer(projectNoUnit).append("~").append(visitType).toString();
			if(instrumentPrototypes.containsKey(lookupKey)){
				prototypes = instrumentPrototypes.get(lookupKey);
				found = true;
			}
		}
		//try any project-visittype
		lookupKey = new StringBuffer(ANY_PROJECT_KEY).append("~").append(visitType).toString();
		if(!found && instrumentPrototypes.containsKey(lookupKey)){
			prototypes = instrumentPrototypes.get(lookupKey);
			found = true;
		}
		//try projname-any visit type
		lookupKey = new StringBuffer(projName).append("~").append(ANY_VISIT_KEY).toString();
		if(!found && instrumentPrototypes.containsKey(lookupKey)){
			prototypes = instrumentPrototypes.get(lookupKey);
			found = true;
		}
		//try (projname without unit)-any visit type
		if(!found && projName.contains("[")){
			lookupKey = new StringBuffer(projectNoUnit).append("~").append(ANY_VISIT_KEY).toString();
			if(instrumentPrototypes.containsKey(lookupKey)){
				prototypes = instrumentPrototypes.get(lookupKey);
				found = true;
			}
		}
		
		if(!found){return clones;}
		
		for(Instrument i : prototypes){
			clones.add((Instrument)i.deepClone());
		}
		
		return clones;
		
	}
	
	public Map<String, List<Instrument>> getInstrumentPrototypes() {
		return instrumentPrototypes;
	}

	public void setInstrumentPrototypes(Map<String, List<Instrument>> instrumentGroupPrototypes) {
		this.instrumentPrototypes = instrumentGroupPrototypes;
	}

	

	public InstrumentDefinitions getInstrumentDefinitions() {
		return instrumentDefinitions;
	}

	public void setInstrumentDefinitions(InstrumentDefinitions instrumentDefinitions) {
		this.instrumentDefinitions = instrumentDefinitions;
	}


	public void updateManagers(Managers managers) {
		super.updateManagers(managers);
		actionManager = CoreManagerUtils.getActionManager(managers);
	}

	
	
	

	
}