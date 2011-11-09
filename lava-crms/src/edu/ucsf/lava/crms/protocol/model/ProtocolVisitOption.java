package edu.ucsf.lava.crms.protocol.model;

import edu.ucsf.lava.core.model.EntityBase;

/**
 * 
 * @author ctoohey
 * 
 * ActivityOption
 * 
 * Base class for classes which serve as options for fulfilling the protocol requirements for
 * subclasses of the Activity class. Each instance of a subclass of this class serves as one
 * alternative to fulfilling its associated protocol node, e.g. ProtocolVisit. One of the
 * options serves as the default which can be used for automatically creating the entity
 * which the protocol node represents.   
 * 
 */

import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;

/**
 * 
 * @author ctoohey
 * 
 * ProtocolVisitOption is a fulfillment option for the ProtocolVisit that is associated with
 * instances of this class by a common node value.
 *
 * ProtocolVisitOption serves two main purposes:
 * a) supply properties needed to  instantiate the Visit
 * b) serve as a possible visit that could fulfill the corresponding visit slot in the protocol 
 *    possibly within constraints that are also defined in this class or its superclass. multiple instances
 *    of this class associated with the same ProtocolVisit represent alternatives for fulfillment.
 */

public class ProtocolVisitOption extends ProtocolVisitOptionBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolVisitOption.class);
	
	public ProtocolVisitOption(){
		super();
	}
	
	private String visitType; 

	public String getVisitType() {
		return visitType;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
}
