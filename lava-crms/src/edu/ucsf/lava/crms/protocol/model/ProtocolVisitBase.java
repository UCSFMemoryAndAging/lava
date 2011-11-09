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

public class ProtocolVisitBase extends ProtocolNode {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolVisitBase.class);
	
	public ProtocolVisitBase(){
		super();
		this.addPropertyToAuditIgnoreList("timepoint");
		this.addPropertyToAuditIgnoreList("instruments");
		this.addPropertyToAuditIgnoreList("options");
	}
	
	private ProtocolTimepointBase timepoint;
	private Set<ProtocolInstrumentBase> instruments = new LinkedHashSet<ProtocolInstrumentBase>();
	private Set<ProtocolVisitOptionBase> options = new HashSet<ProtocolVisitOptionBase>();
	
	public ProtocolTimepointBase getTimepoint() {
		return timepoint;
	}

	public void setTimepoint(ProtocolTimepointBase timepoint) {
		this.timepoint = timepoint;
	}
	
	public Set<ProtocolInstrumentBase> getInstruments() {
		return instruments;
	}

	public void setInstruments(Set<ProtocolInstrumentBase> instruments) {
		this.instruments = instruments;
	}

	// note the argument needs to be ProtocolInstrument not ProtocolInstrumentBase due to
	// some reflection weirdness
	public void addInstrument(ProtocolInstrument instrument) {
		instrument.setVisit(this);
		this.instruments.add(instrument);
	}
	
	public Set<ProtocolVisitOptionBase> getOptions() {
		return options;
	}

	public void setOptions(Set<ProtocolVisitOptionBase> options) {
		this.options = options;
	}

	public void addOption(ProtocolVisitOptionBase option) {
		option.setVisit(this);
		this.options.add(option);
	}

}
