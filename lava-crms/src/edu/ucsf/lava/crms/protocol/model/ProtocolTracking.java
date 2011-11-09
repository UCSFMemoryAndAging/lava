package edu.ucsf.lava.crms.protocol.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * 
 * @author ctoohey
 * 
 *
 */

public class ProtocolTracking extends ProtocolNode {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolTracking.class);
	
	public ProtocolTracking(){
		super();
	}

	// override ProtocolNode so nothing is initialized because ProtocolTracking queries are configured to
	// eagerly fetch all associations (i.e. children, options)
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{};
	}
	
	private ProtocolTracking parent;
	private Set<ProtocolTracking> children = new LinkedHashSet<ProtocolTracking>();
	private Set<ProtocolOptionTracking> options = new HashSet<ProtocolOptionTracking>();

	public ProtocolTracking getParent() {
		return parent;
	}

	public void setParent(ProtocolTracking parent) {
		this.parent = parent;
	}

	public Set<ProtocolTracking> getChildren() {
		return children;
	}

	public void setChildren(Set<ProtocolTracking> children) {
		this.children = children;
	}

	public Set<ProtocolOptionTracking> getOptions() {
		return options;
	}

	public void setOptions(Set<ProtocolOptionTracking> options) {
		this.options = options;
	}

}
