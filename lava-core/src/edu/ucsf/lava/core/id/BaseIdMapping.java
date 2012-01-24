package edu.ucsf.lava.core.id;

import edu.ucsf.lava.core.model.EntityBase;

/**
 * Low level common functionality for id mappings.  The BaseIdMapping here does not
 * *guarantee* being the default IdMapping.
 * 
 * @author trobbie
 */

public class BaseIdMapping implements IdMapping {
	/**
	 * unique id of the id mapping 
	 */
	protected String id;
	
	/**
	 * Deferred implementation details pattern object.
	 */
	protected IdMappingStrategy strategy;
	
	protected IdMappings idmappings;
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public IdMappingStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(IdMappingStrategy strategy) {
		this.strategy = strategy;
	}

	public IdMappings getIdMappings() {
		return idmappings;
	}

	public void setIdMappings(IdMappings idmappings) {
		this.idmappings = idmappings;
		this.idmappings.addIdMapping(this);
	}
	
	public boolean handlesEntityProperty(EntityBase entity, String idPropName) {
		// if no strategy assigned, then return false
		// i.e. let the defaultLavaIdMapping bean definition determine whether there is a default handling of IDs, and
		// if so to reference a strategy that returns true for all handling
		if (this.getStrategy() == null) return false;
		return this.getStrategy().handlesEntityProperty(entity, idPropName);
	}
	
	public String getEntityPropId(EntityBase entity, String idPropName) {
		// no need to check for null strategy since can only be called if strategy exists and handles it
		return this.getStrategy().getEntityPropId(entity, idPropName);
	}
}
