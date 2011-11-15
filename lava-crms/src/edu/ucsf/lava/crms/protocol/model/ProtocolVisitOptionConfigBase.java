package edu.ucsf.lava.crms.protocol.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * This class exists to allow protocol tree retrieval queries which only involving the
 * protocol option config base table. This class is mapped such that it is not a subclass of 
 * the protocol option config base table.
 * 
 * @author ctoohey
 * 
 *
 */

public class ProtocolVisitOptionConfigBase extends ProtocolOptionConfigBase {
	public ProtocolVisitOptionConfigBase(){
		super();
		this.addPropertyToAuditIgnoreList("visitBase");
	}

	private ProtocolVisitConfigBase visitBase;

	public ProtocolVisitConfigBase getVisitBase() {
		return visitBase;
	}

	public void setVisitBase(ProtocolVisitConfigBase visit) {
		this.visitBase = visit;
	}
}
