package edu.ucsf.lava.crms.assessment.model;

import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;

/**
 * InstrumentTracking
 * 
 * This class is used for lightweight instrument instances, i.e. those containing only
 * the core instrument tracking portion of an instrument table. It is used for instances
 * of instruments in listing, where each element could be a different type of instrument,
 * and for setting the instrument in the session context, and for retrieving an unimplemented
 * instrument for the purpose of deleting it. 
 * 
 * The reason it is used instead of Instrument is so that polymorphic queries are avoided,
 * because there are no subclasses of InstrumentTracking. In contrast, Instrument is the 
 * superclass of the instrument hierarchy, so mapping Instrument and querying on it would
 * result in a polymorphic query, joining to every subclass, e.g. in the case of a list
 * of instruments, returning instances of all the instrument specific classes in the list. 
 * Given the number of instruments in the Instrument class hierarchy, the number of joins
 * involved would be prohibitive.
 */
public class InstrumentTracking extends Instrument {

	public static EntityManager MANAGER = new EntityBase.Manager(InstrumentTracking.class);

	public InstrumentTracking() {
		super();
	}
	
	public InstrumentTracking(Patient p, Visit v, String projName, String instrType, Date dcDate, String dcStatus) {
		super(p, v, projName, instrType, dcDate, dcStatus);
	}
	
}

