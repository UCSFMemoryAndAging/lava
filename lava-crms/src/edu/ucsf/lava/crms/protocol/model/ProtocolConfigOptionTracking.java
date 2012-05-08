package edu.ucsf.lava.crms.protocol.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * @author ctoohey
 * 
 * This class exists to allow protocol tree retrieval queries which only involving the protocol 
 * protocol option base table. 
 */

public class ProtocolConfigOptionTracking extends ProtocolNodeConfigOption {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolConfigOptionTracking.class);
	
	public ProtocolConfigOptionTracking(){
		super();
	}

}
