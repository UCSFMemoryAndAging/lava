package edu.ucsf.lava.core.model;



/**
 * @author jhesse
 * This is a placeholder for component handlers that do not have an entity component
 */
public class EntityPlaceholder extends EntityBase {
	public static EntityManager MANAGER = new EntityBase.Manager(EntityPlaceholder.class);
	
	public Long getId() {
		return new Long(0);
	}


	public boolean getPatientAuth() {
		return false;
	}
	

}
