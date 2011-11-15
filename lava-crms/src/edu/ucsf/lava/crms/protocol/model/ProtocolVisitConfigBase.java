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
		this.addPropertyToAuditIgnoreList("timepointBase");
		this.addPropertyToAuditIgnoreList("instrumentsBase");
		this.addPropertyToAuditIgnoreList("optionsBase");
	}
	

	private ProtocolTimepointConfigBase timepointConfigBase;
	private Set<? extends ProtocolInstrumentConfigBase> instrumentsBase = new LinkedHashSet<ProtocolInstrumentConfigBase>();
	private Set<? extends ProtocolVisitOptionConfigBase> optionsBase = new HashSet<ProtocolVisitOptionConfigBase>();
	
	public ProtocolTimepointConfigBase getTimepointConfigBase() {
		return timepointConfigBase;
	}

	public void setTimepointConfigBase(ProtocolTimepointConfigBase timepoint) {
		this.timepointConfigBase = timepoint;
	}
	
	public Set<? extends ProtocolInstrumentConfigBase> getInstrumentsBase() {
		return instrumentsBase;
	}

	public void setInstrumentsBase(Set<? extends ProtocolInstrumentConfigBase> instruments) {
		this.instrumentsBase = instruments;
	}

	public Set<? extends ProtocolVisitOptionConfigBase> getOptionsBase() {
		return optionsBase;
	}

	public void setOptionsBase(Set<? extends ProtocolVisitOptionConfigBase> options) {
		this.optionsBase = options;
	}

}
