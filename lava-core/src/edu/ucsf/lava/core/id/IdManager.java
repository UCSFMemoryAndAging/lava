package edu.ucsf.lava.core.id;

import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.core.model.EntityBase;

/**
 * This manager centralizes all logic related to IDs.
 * 
 * @author trobbie
 */

public class IdManager extends LavaManager {

	public static String ID_MANAGER_NAME="idManager";
	
	protected IdMappings idMappings;

	public IdManager() {
		super(ID_MANAGER_NAME);
	}
	
	public IdMappings getIdMappings() {
		return idMappings;
	}

	public void setIdMappings(IdMappings idMappings) {
		this.idMappings = idMappings;
	}
	/** 
	 * Id Mapping access method.
	 * @param entity, idPropName
	 * @return
	 */
	protected IdMapping getIdMapping(EntityBase entity, String idPropName) {
		return this.idMappings.getIdMapping(entity, idPropName);
	}
	
	/**
	 * Get id based logic used for this entity and property. 
	 * 
	 * @param entity and ID property which is being determined
	 * @return ID
	 */
	public String getEntityPropId(EntityBase entity, String idPropName) {
		IdMapping idmapping = getIdMapping(entity, idPropName);
		if (idmapping == null) return null;	// it is not guaranteed that an IdMapping is found to handle this entity-property
		return idmapping.getEntityPropId(entity, idPropName);
	}
	

	
}
