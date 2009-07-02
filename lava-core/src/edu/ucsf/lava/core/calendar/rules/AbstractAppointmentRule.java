package edu.ucsf.lava.core.calendar.rules;

import java.util.ArrayList;
import java.util.List;

import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.metadata.MetadataManager;

public abstract class AbstractAppointmentRule implements AppointmentRule {

	protected MetadataManager metadataManager;
	
	protected MetadataManager getMetadataManager(){
		if(metadataManager!=null){return metadataManager;}
		
		metadataManager = CoreManagerUtils.getMetadataManager();
		return metadataManager;
	}
	
}
