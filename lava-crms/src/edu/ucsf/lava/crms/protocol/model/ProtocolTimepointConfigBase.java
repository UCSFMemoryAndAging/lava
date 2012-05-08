package edu.ucsf.lava.crms.protocol.model;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 *   
 * @author ctoohey
 *
 *
 */
public abstract class ProtocolTimepointConfigBase extends ProtocolNodeConfig implements Comparable<ProtocolTimepointConfig> {
	
	public ProtocolTimepointConfigBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocolConfigBase");
		this.addPropertyToAuditIgnoreList("protocolVisitConfigsBase");
	}

	private ProtocolConfigBase protocolConfigBase;
	private Set<? extends ProtocolVisitConfigBase> protocolVisitConfigsBase = new LinkedHashSet<ProtocolVisitConfigBase>();

	public ProtocolConfigBase getProtocolConfigBase() {
		return protocolConfigBase;
	}

	public void setProtocolConfigBase(ProtocolConfigBase protocolBase) {
		this.protocolConfigBase = protocolBase;
	}

	public Set<? extends ProtocolVisitConfigBase> getProtocolVisitConfigsBase() {
		return protocolVisitConfigsBase;
	}

	public void setProtocolVisitConfigsBase(Set<? extends ProtocolVisitConfigBase> protocolVisitConfigs) {
		this.protocolVisitConfigsBase = protocolVisitConfigs;
	}

}
