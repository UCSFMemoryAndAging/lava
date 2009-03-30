package edu.ucsf.lava.crms.assessment;

import java.util.HashMap;
import java.util.Map;

import edu.ucsf.lava.crms.assessment.model.InstrumentConfig;


public class InstrumentDefinitions {
	private Map<String,InstrumentConfig> definitions = new HashMap<String, InstrumentConfig>();

	public Map<String, InstrumentConfig> getDefinitions() {
		return definitions;
	}

	public InstrumentConfig get(String instrTypeEncoded) {
		if(definitions.containsKey(instrTypeEncoded)){
			return definitions.get(instrTypeEncoded);
		}
		return null;
		
	}

	public void setDefinitions(Map<String, InstrumentConfig> definitions) {
		this.definitions = definitions;
	}
	
	public void addInstrumentDefinition(InstrumentConfig config){
		this.definitions.put(config.getInstrTypeEncoded(), config);
		if(config.getCurrentVersionAlias()!=null){
			definitions.put(config.getCurrentVersionAlias(), config);
		}
	}
	
}
