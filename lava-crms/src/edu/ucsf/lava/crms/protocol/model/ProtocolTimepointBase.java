package edu.ucsf.lava.crms.protocol.model;

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
public class ProtocolTimepointBase extends ProtocolNode implements Comparable<ProtocolTimepointBase> {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolTimepointBase.class);
	
	public ProtocolTimepointBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocol");
		this.addPropertyToAuditIgnoreList("visits");
	}

	private ProtocolBase protocol;
	private Set<ProtocolVisitBase> visits = new LinkedHashSet<ProtocolVisitBase>();

	public ProtocolBase getProtocol() {
		return protocol;
	}

	public void setProtocol(ProtocolBase protocol) {
		this.protocol = protocol;
	}
	
	public Set<ProtocolVisitBase> getVisits() {
		return visits;
	}

	public void setVisits(Set<ProtocolVisitBase> visits) {
		this.visits = visits;
	}
	
	// note the argument needs to be ProtocolVisit not ProtocolVisitBase due to
	// some reflection weirdness
	public void addVisit(ProtocolVisit visit) {
		visit.setTimepoint(this);
		this.visits.add(visit);
	}

	public int compareTo(ProtocolTimepointBase protocolTimepoint) throws ClassCastException {
		if (this.getListOrder() > protocolTimepoint.getListOrder()) {
			return 1;
		}
		else if (this.getListOrder() < protocolTimepoint.getListOrder()) {
			return -1;
		}
		else {
			return 0;
		}
	}
	
}
