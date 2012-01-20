package edu.ucsf.lava.crms.protocol.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * This class offers a lightweight version of each of the ProtocolConfig component types, such 
 * that the entire tree for a given ProtocolConfig hierarchcy can be retrieved by just querying 
 * the base (of course, the objects in the tree only contain the properties common across all 
 * component types, i.e. the properties in ProtocolNodeConfig). 
 * 
 * @author ctoohey
 */

public class ProtocolConfigTracking extends ProtocolNodeConfig {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolConfigTracking.class);
	
	public ProtocolConfigTracking(){
		super();
	}

	private ProtocolConfigTracking parent;
	private Set<ProtocolConfigTracking> children = new LinkedHashSet<ProtocolConfigTracking>();
	private Set<ProtocolConfigOptionTracking> options = new HashSet<ProtocolConfigOptionTracking>();

	public ProtocolConfigTracking getParent() {
		return parent;
	}

	public void setParent(ProtocolConfigTracking parent) {
		this.parent = parent;
	}

	public Set<ProtocolConfigTracking> getChildren() {
		return children;
	}

	public void setChildren(Set<ProtocolConfigTracking> children) {
		this.children = children;
	}

	public Set<ProtocolConfigOptionTracking> getOptions() {
		return options;
	}

	public void setOptions(Set<ProtocolConfigOptionTracking> options) {
		this.options = options;
	}


/***	
	public int compareTo(ProtocolTracking protocolTracking) throws ClassCastException {
		if (this.getListOrder() > protocolTracking.getListOrder()) {
			return 1;
		}
		else if (this.getListOrder() < protocolTracking.getListOrder()) {
			return -1;
		}
		else {
			return 0;
		}
	}
***/	
	
}
