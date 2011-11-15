package edu.ucsf.lava.crms.protocol.model;

import java.util.HashSet;
import java.util.TreeSet;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * 
 * @author ctoohey
 * 
 *
 */

public abstract class ProtocolConfigBase extends ProtocolNodeConfig {
	public ProtocolConfigBase(){
		super();
		this.addPropertyToAuditIgnoreList("timepointsBase");
	}
	
	public Set<? extends ProtocolTimepointConfigBase> timepointsBase = new TreeSet<ProtocolTimepointConfigBase>();
	
	public Set<? extends ProtocolTimepointConfigBase> getTimepointsBase() {
		return timepointsBase;
	}

	public void setTimepointsBase(Set<? extends ProtocolTimepointConfigBase> timepointsBase) {
		this.timepointsBase = timepointsBase;
	}
}
