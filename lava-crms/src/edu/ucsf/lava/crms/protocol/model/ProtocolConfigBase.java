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
		this.addPropertyToAuditIgnoreList("protocolTimepointConfigsBase");
	}
	
	public Set<? extends ProtocolTimepointConfigBase> protocolTimepointConfigsBase = new TreeSet<ProtocolTimepointConfigBase>();
	
	public Set<? extends ProtocolTimepointConfigBase> getProtocolTimepointConfigsBase() {
		return protocolTimepointConfigsBase;
	}

	public void setProtocolTimepointConfigsBase(Set<? extends ProtocolTimepointConfigBase> protocolTimepointConfigsBase) {
		this.protocolTimepointConfigsBase = protocolTimepointConfigsBase;
	}
}
