package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrument;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisit;

public class ProtocolInstrumentHandler extends CrmsEntityComponentHandler {

	public ProtocolInstrumentHandler() {
		super();
		setHandledEntity("protocolInstrument", edu.ucsf.lava.crms.protocol.model.ProtocolInstrument.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{});
	    return getRequiredFields();
	}

	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolInstrument protocolInstrument = (ProtocolInstrument)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		// TODO: create a list of existing instruments that match based on the instrument options
		// instrTypes
		
		
		
		
		model.put("dynamicLists", dynamicLists);
		
		// so protocolConfig label can appear on page title 
		model.put("protocolConfigLabel", protocolInstrument.getVisit().getTimepoint().getProtocol().getProtocolConfig().getLabel());
		
		return super.addReferenceData(context, command, errors, model);
	}
	
	/**
	 * Helper method to facilitate changing the Instrument associated with the ProtocolInstrument
	 * after the user has changed the instrument in the view, binding a new instrId.
	 * 
	 * @param context
	 * @param command
	 * @param errors
	 */
	protected void handleInstrumentChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolInstrument patientProtocolInstrument = (ProtocolInstrument)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(patientProtocolInstrument.getInstrId(),patientProtocolInstrument.getInstrument())){
			if(patientProtocolInstrument.getInstrId()==null){
				patientProtocolInstrument.setInstrument(null); 	//clear the association
			}else{
				Instrument instrument = (Instrument) Instrument.MANAGER.getById(patientProtocolInstrument.getInstrId(), Instrument.newFilterInstance(getCurrentUser(request)));
				patientProtocolInstrument.setInstrument(instrument);
			}
		}
		else {
			patientProtocolInstrument.setInstrument(null); 	//clear the association
			patientProtocolInstrument.setInstrId(null);
		}
	}
	
	
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		this.handleInstrumentChange(context, command, errors);
		return super.doSaveAdd(context, command, errors);
	}
	
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception{
		this.handleInstrumentChange(context, command, errors);
		return super.doSaveAdd(context, command, errors);
	}	
	
}
