package edu.ucsf.lava.core.id;

import edu.ucsf.lava.core.model.EntityBase;

public abstract class AbstractIdMappingStrategy implements IdMappingStrategy {

	protected IdMapping idmapping;
	
	abstract public String getEntityPropId(EntityBase entity, String idPropName);
	abstract public boolean handlesEntityProperty(EntityBase entity, String idPropName);
	
	public IdMapping getIdmapping() {
		return idmapping;
	}
	
	public void setIdmapping(IdMapping idmapping) {
		this.idmapping = idmapping;
	}

	
}
