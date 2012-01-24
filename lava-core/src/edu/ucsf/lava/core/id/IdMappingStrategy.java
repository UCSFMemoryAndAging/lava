package edu.ucsf.lava.core.id;

import edu.ucsf.lava.core.model.EntityBase;

/**
 * A Strategy pattern interface used to abstract out the logic for determining what the 
 * default id mapping is given an existing entity and property
 * 
 * @author trobbie
 *
 */

public interface IdMappingStrategy {
	/** 
	 * Does this Id Mapping handle this entity and property.
	 * @param file
	 * @return
	 */
	public boolean handlesEntityProperty(EntityBase entity, String idPropName);
	
	/**
	 * Get id based logic used for this entity and property. 
	 * 
	 * @param entity and ID property which is being determined
	 * @return ID
	 */
	public String getEntityPropId(EntityBase entity, String idPropName);
	
}

