package edu.ucsf.lava.crms.protocol.model;

import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;

/**
 * @author ctoohey
 * 
 * This class exists to allow protocol tree retrieval queries which only involving the protocol 
 * protocol option base table. 
 */

public class ProtocolOptionTrackingConfig extends ProtocolOptionConfigBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolOptionTrackingConfig.class);
	
	public ProtocolOptionTrackingConfig(){
		super();
	}

}
