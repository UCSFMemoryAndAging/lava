package edu.ucsf.lava.crms.protocol.model;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * @author ctoohey
 * 
 *
 */

public abstract class ProtocolBase extends ProtocolNode {
	public ProtocolBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocolConfigBase");
		this.addPropertyToAuditIgnoreList("protocolTimepointsBase");
	}

	private ProtocolConfigBase protocolConfigBase;
	// protocolConfigId is part of the mechanism required to set the ProtocolConfig association when a user
	// assigns a Patient to a ProtocolConfig to create a Protocol. it allows the user to designate a  
	// ProtocolConfig in the view. the id is used to retrieve the ProtocolConfig in order to create the
	// corresponding Protocol structure. 
	// note: this must be declare in this class because it is set in setProtocolConfigBase
	private Long protocolConfigId; 
	private Set<? extends ProtocolTimepointBase> protocolTimepointsBase = new LinkedHashSet<ProtocolTimepointBase>();
	
	public ProtocolConfigBase getProtocolConfigBase() {
		return protocolConfigBase;
	}

	public void setProtocolConfigBase(ProtocolConfigBase protocolBase) {
		this.protocolConfigBase = protocolBase;
		// this is the paradigm used to set associations via the UI
		if (protocolBase != null) {
			this.protocolConfigId = protocolBase.getId();
		}
	}
	
	public Long getProtocolConfigId() {
		return protocolConfigId;
	}

	public void setProtocolConfigId(Long protocolConfigId) {
		this.protocolConfigId = protocolConfigId;
	}

	public Set<? extends ProtocolTimepointBase> getProtocolTimepointsBase() {
		return protocolTimepointsBase;
	}

	public void setProtocolTimepointsBase(Set<? extends ProtocolTimepointBase> protocolTimepoints) {
		this.protocolTimepointsBase = protocolTimepoints;
	}

	
}
