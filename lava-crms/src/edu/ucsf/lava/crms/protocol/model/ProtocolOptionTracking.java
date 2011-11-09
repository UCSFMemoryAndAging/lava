package edu.ucsf.lava.crms.protocol.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * This class exists to allow protocol tree retrieval queries which only involving the protocol node and protocol option
 * base tables. This class is mapped such that it is not a subclass of the protocol option base table.
 * 
 * @author ctoohey
 * 
 *
 */

public class ProtocolOptionTracking extends ProtocolOptionBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolOptionTracking.class);
	
	public ProtocolOptionTracking(){
		super();
	}

}
