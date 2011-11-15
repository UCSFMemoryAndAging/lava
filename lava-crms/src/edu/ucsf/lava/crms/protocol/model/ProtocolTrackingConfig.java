package edu.ucsf.lava.crms.protocol.model;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;

/**
 * This class offers a lightweight version of each of the ProtocolConfig component types, such 
 * that the entire tree for a given ProtocolConfig hierarchcy can be retrieved by just querying 
 * the base (of course, the objects in the tree only contain the properties common across all 
 * component types, i.e. the properties in ProtocolNodeConfig). 
 * 
 * @author ctoohey
 */

public class ProtocolTrackingConfig extends ProtocolNodeConfig {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolTrackingConfig.class);
	
	public ProtocolTrackingConfig(){
		super();
	}

	private ProtocolTrackingConfig parent;
	private Set<ProtocolTrackingConfig> children = new LinkedHashSet<ProtocolTrackingConfig>();
	private Set<ProtocolOptionTrackingConfig> options = new HashSet<ProtocolOptionTrackingConfig>();

	public ProtocolTrackingConfig getParent() {
		return parent;
	}

	public void setParent(ProtocolTrackingConfig parent) {
		this.parent = parent;
	}

	public Set<ProtocolTrackingConfig> getChildren() {
		return children;
	}

	public void setChildren(Set<ProtocolTrackingConfig> children) {
		this.children = children;
	}

	public Set<ProtocolOptionTrackingConfig> getOptions() {
		return options;
	}

	public void setOptions(Set<ProtocolOptionTrackingConfig> options) {
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
