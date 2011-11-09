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

public class ProtocolInstrumentBase extends ProtocolNode {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolInstrumentBase.class);
	
	public ProtocolInstrumentBase(){
		super();
		this.addPropertyToAuditIgnoreList("visit");
		this.addPropertyToAuditIgnoreList("options");
	}

	private ProtocolVisitBase visit;
	private Set<ProtocolInstrumentOptionBase> options = new HashSet<ProtocolInstrumentOptionBase>();

	public ProtocolVisitBase getVisit() {
		return visit;
	}

	public void setVisit(ProtocolVisitBase visit) {
		this.visit = visit;
	}

	public Set<ProtocolInstrumentOptionBase> getOptions() {
		return options;
	}

	public void setOptions(Set<ProtocolInstrumentOptionBase> options) {
		this.options = options;
	}
	
	public void addOption(ProtocolInstrumentOptionBase option) {
		option.setInstrument(this);
		this.options.add(option);
	}
	
}
