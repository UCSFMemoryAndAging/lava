package edu.ucsf.lava.core.id;

import edu.ucsf.lava.core.model.EntityBase;

public interface IdMapping {
	/**
	 * Return the string id of the id mapping.
	 * @return
	 */
	public String getId();
	
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
