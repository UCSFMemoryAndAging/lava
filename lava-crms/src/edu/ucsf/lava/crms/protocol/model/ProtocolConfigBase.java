package edu.ucsf.lava.crms.protocol.model;

import java.util.Set;
import java.util.TreeSet;

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
	
	private Set<? extends ProtocolTimepointConfigBase> protocolTimepointConfigsBase = new TreeSet<ProtocolTimepointConfigBase>();
	
	public Set<? extends ProtocolTimepointConfigBase> getProtocolTimepointConfigsBase() {
		return protocolTimepointConfigsBase;
	}

	public void setProtocolTimepointConfigsBase(Set<? extends ProtocolTimepointConfigBase> protocolTimepointConfigsBase) {
		this.protocolTimepointConfigsBase = protocolTimepointConfigsBase;
	}
}
