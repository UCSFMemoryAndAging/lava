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

public class PatientProtocolTracking extends PatientProtocolNode {
	public static EntityManager MANAGER = new EntityBase.Manager(PatientProtocolTracking.class);
	
	public PatientProtocolTracking(){
		super();
	}

	private PatientProtocolTracking parent;
	private ProtocolTracking protocolCounterpart; 
	private Set<PatientProtocolTracking> children = new LinkedHashSet<PatientProtocolTracking>();

	public PatientProtocolTracking getParent() {
		return parent;
	}

	public void setParent(PatientProtocolTracking parent) {
		this.parent = parent;
	}
	
	public ProtocolTracking getProtocolCounterpart() {
		return protocolCounterpart;
	}

	public void setProtocolCounterpart(ProtocolTracking protocolCounterpart) {
		this.protocolCounterpart = protocolCounterpart;
	}

	public Set<PatientProtocolTracking> getChildren() {
		return children;
	}

	public void setChildren(Set<PatientProtocolTracking> children) {
		this.children = children;
	}

}
