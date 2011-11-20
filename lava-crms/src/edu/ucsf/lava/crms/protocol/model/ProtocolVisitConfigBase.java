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

public abstract class ProtocolVisitConfigBase extends ProtocolNodeConfig {
	public ProtocolVisitConfigBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocolTimepointConfigBase");
		this.addPropertyToAuditIgnoreList("protocolInstrumentConfigsBase");
		this.addPropertyToAuditIgnoreList("protocolVisitOptionConfigsBase");
	}
	

	private ProtocolTimepointConfigBase protocolTimepointConfigBase;
	private Set<? extends ProtocolInstrumentConfigBase> protocolInstrumentConfigsBase = new LinkedHashSet<ProtocolInstrumentConfigBase>();
	private Set<? extends ProtocolVisitOptionConfigBase> protcolVisitOptionConfigsBase = new HashSet<ProtocolVisitOptionConfigBase>();
	
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

	public Set<? extends ProtocolVisitOptionConfigBase> getProtocolVisitOptionConfigsBase() {
		return protcolVisitOptionConfigsBase;
	}

	public void setProtocolVisitOptionConfigsBase(Set<? extends ProtocolVisitOptionConfigBase> options) {
		this.protcolVisitOptionConfigsBase = options;
	}

}
