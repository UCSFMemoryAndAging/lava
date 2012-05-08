package edu.ucsf.lava.crms.protocol.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * @author ctoohey
 * 
 *
 */

public abstract class ProtocolVisitConfigBase extends ProtocolNodeConfig {
	public ProtocolVisitConfigBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocolTimepointConfigBase");
		this.addPropertyToAuditIgnoreList("protocolInstrumentConfigsBase");
		this.addPropertyToAuditIgnoreList("protocolVisitConfigOptionsBase");
	}
	

	private ProtocolTimepointConfigBase protocolTimepointConfigBase;
	private Set<? extends ProtocolInstrumentConfigBase> protocolInstrumentConfigsBase = new LinkedHashSet<ProtocolInstrumentConfigBase>();
	private Set<? extends ProtocolVisitConfigOptionBase> protocolVisitConfigOptionsBase = new HashSet<ProtocolVisitConfigOptionBase>();
	
	public ProtocolTimepointConfigBase getProtocolTimepointConfigBase() {
		return protocolTimepointConfigBase;
	}

	public void setProtocolTimepointConfigBase(ProtocolTimepointConfigBase protocolTimepointConfig) {
		this.protocolTimepointConfigBase = protocolTimepointConfig;
	}
	
	public Set<? extends ProtocolInstrumentConfigBase> getProtocolInstrumentConfigsBase() {
		return protocolInstrumentConfigsBase;
	}

	public void setProtocolInstrumentConfigsBase(Set<? extends ProtocolInstrumentConfigBase> protocolInstrumentConfigs) {
		this.protocolInstrumentConfigsBase = protocolInstrumentConfigs;
	}

	public Set<? extends ProtocolVisitConfigOptionBase> getProtocolVisitConfigOptionsBase() {
		return protocolVisitConfigOptionsBase;
	}

	public void setProtocolVisitConfigOptionsBase(Set<? extends ProtocolVisitConfigOptionBase> options) {
		this.protocolVisitConfigOptionsBase = options;
	}

}
