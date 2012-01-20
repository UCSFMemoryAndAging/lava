package edu.ucsf.lava.crms.protocol.model;

import java.util.Set;
import java.util.TreeSet;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * This class offers a lightweight version of each of the Protocol component types, such that
 * the entire tree for a given Protocol can be retrieved by just querying the base (of course, 
 * the objects in the tree only contain the properties common across all component types, i.e.
 * the properties in ProtocolNode). 
 * 
 * @author ctoohey
 * 
 *
 */

public class ProtocolTracking extends ProtocolNode {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolTracking.class);
	
	public ProtocolTracking(){
		super();
		this.setProjectAuth(true);
	}

	private ProtocolTracking parent;
	private ProtocolConfigTracking config; 
	// use TreeSet so collection can be sorted chronologically in subclasses, if desired
	private Set<ProtocolTracking> children = new TreeSet<ProtocolTracking>();

	public ProtocolTracking getParent() {
		return parent;
	}

	public void setParent(ProtocolTracking parent) {
		this.parent = parent;
	}
	
	public ProtocolConfigTracking getConfig() {
		return config;
	}

	public void setConfig(ProtocolConfigTracking config) {
		this.config = config;
	}

	public Set<ProtocolTracking> getChildren() {
		return children;
	}

	public void setChildren(Set<ProtocolTracking> children) {
		this.children = children;
	}
	
	/**
	 * ProtocolTracking does not do any calculations as it is a lightweight representation
	 * used for all types of Protocol components and does not have the properties required
	 * for calculations, as these live in the subclasses.
	 */
	public void calculate() {
		
	}
	
	/**
	 * ProtocolTracking does not do any calculations as it is a lightweight representation
	 * used for all types of Protocol components and does not have the properties required
	 * for calculations, as these live in the subclasses.
	 */
	public void updateStatus() {
		
	}
	
}
