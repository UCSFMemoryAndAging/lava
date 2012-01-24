package edu.ucsf.lava.core.id;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.ucsf.lava.core.model.EntityBase;

public class IdMappings {
	private Map<String, IdMapping> idMappings = new HashMap<String, IdMapping>();
	
	
	public Map<String, IdMapping> getIdMappings() {
		return idMappings;
	}

	public void setIdMappings(Map<String, IdMapping> idMappings) {
		this.idMappings = idMappings;
	}

	public IdMapping getIdMapping(EntityBase entity, String idPropName){
		// find the IdMapping that handles this entity-property
		for(Entry<String, IdMapping> entry : idMappings.entrySet()) {
			if(entry.getValue().handlesEntityProperty(entity, idPropName)){
				return entry.getValue();
			}
		}
		// it is not guaranteed that there is a default IdMapping; return null
		return null;
	}
	
	/**
	 * Adds an id mapping. 
	 * @param idMapping
	 */
	public void addIdMapping(IdMapping idMapping){
		if(idMapping!=null){
			this.idMappings.put(idMapping.getId(),idMapping);
		}
	}
	
}
