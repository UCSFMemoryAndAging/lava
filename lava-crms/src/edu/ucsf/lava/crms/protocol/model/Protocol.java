package edu.ucsf.lava.crms.protocol.model;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * 
 * @author ctoohey
 *
 *
 */
public class Protocol extends ProtocolBase {
	public static EntityManager MANAGER = new EntityBase.Manager(Protocol.class);
	
	public Protocol(){
		super();
		this.setAuditEntityType("Protocol");
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.getTimepoints()};
	}

	private String category;

    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
}
