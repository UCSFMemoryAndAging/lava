package edu.ucsf.lava.crms.protocol.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.LavaEntity;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.people.model.Patient;
///import edu.ucsf.lava.crms.protocol.controller.ProtocolNodeUtils;
import edu.ucsf.lava.crms.protocol.model.PatientProtocol;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolAssessmentTimepoint;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolInstrument;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolNode;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolTimepoint;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolTracking;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolVisit;
import edu.ucsf.lava.crms.protocol.model.Protocol;
import edu.ucsf.lava.crms.protocol.model.ProtocolAssessmentTimepoint;
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrument;
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrumentBase;
import edu.ucsf.lava.crms.protocol.model.ProtocolNode;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepoint;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointBase;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisit;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitBase;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitOption;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class PatientProtocolHandler extends CrmsEntityComponentHandler {
	protected static final String PATIENT_PROTOCOL_TREE = "patientProtocolTree";

	public PatientProtocolHandler() {
		super();
		setHandledEntity("patientProtocol", edu.ucsf.lava.crms.protocol.model.PatientProtocol.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
		setRequiredFields(new String[]{"protocolId","enrolledDate"});
		return getRequiredFields();
	}
	
	
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		Map backingObjects = super.getBackingObjects(context, components);
		
		if (flowMode.equals("add")) return backingObjects;

		// retrieve the entire patientProtocol hierarchy tree for display in patientProtocol View mode
		PatientProtocol patientProtocol = (PatientProtocol) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("protocolId", patientProtocol.getId()));
		PatientProtocolTracking patientProtocolTree = (PatientProtocolTracking) EntityBase.MANAGER.findOneByNamedQuery("patientProtocol.protocolTree", filter);
		backingObjects.put("patientProtocolTree", patientProtocolTree);
		return backingObjects;
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		// do not init project to current project; the protocol that is selected will determine the project
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		PatientProtocol patientProtocol = (PatientProtocol) command;
		
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager,request);
		if (p != null){
			patientProtocol.setPatient(p);
		}
		return command;
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		PatientProtocol patientProtocol = (PatientProtocol)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		
		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		Map allProtocols = listManager.getDynamicList(getCurrentUser(request),"protocol.allProtocols");
		dynamicLists.put("protocol.allProtocols", allProtocols);
		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		PatientProtocol patientProtocol = (PatientProtocol)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		Patient patient = patientProtocol.getPatient();
//		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);

		// retrieve the Protocol and create a parallel structure, i.e. PatientProtocol
		// note: patientProtocol.protocolId is populated directly from view based on user input
		
		// use a named query to retrieve the entiry Protocol tree, as retrieving it via criteria query (getOne)
		// will only initialize the timepoints collection, but nothing below that. the HQL named query eagerly
		// fetches every level of the protocol tree (except the options since those are not needed here)
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("protocolId", patientProtocol.getProtocolId()));
		Protocol protocol = (Protocol) EntityBase.MANAGER.findOneByNamedQuery("protocol.completeProtocolTree", filter);
		
		// for now a protocol is only associated with one project, so that is the projName used for patientProtocol
		String projName = protocol.getProjName();
		
		// patientProtocol already has the patient set
		patientProtocol.setProtocol(protocol);
		patientProtocol.setProjName(protocol.getProjName());
		
		
		for (ProtocolTimepointBase protocolTimepoint: protocol.getTimepoints()) {
			PatientProtocolTimepoint patientProtocolTimepoint = null;
			if (protocolTimepoint.getClass().isAssignableFrom(ProtocolAssessmentTimepoint.class)) {
				patientProtocolTimepoint = new PatientProtocolAssessmentTimepoint();
				patientProtocol.addTimepoint(patientProtocolTimepoint);
				patientProtocolTimepoint.setProtocolTimepoint((ProtocolTimepoint)protocolTimepoint);
				patientProtocolTimepoint.setPatient(patientProtocol.getPatient());
				patientProtocolTimepoint.setProjName(protocolTimepoint.getProjName());
			}

			for (ProtocolVisitBase protocolVisit: protocolTimepoint.getVisits()) {
				PatientProtocolVisit patientProtocolVisit = new PatientProtocolVisit();
				patientProtocolTimepoint.addVisit(patientProtocolVisit);
				patientProtocolVisit.setProtocolVisit((ProtocolVisit)protocolVisit);
				patientProtocolVisit.setPatient(patientProtocol.getPatient());
				patientProtocolVisit.setProjName(protocolVisit.getProjName());
				
				for (ProtocolInstrumentBase protocolInstrument: protocolVisit.getInstruments()) {
					PatientProtocolInstrument patientProtocolInstrument = new PatientProtocolInstrument();
					patientProtocolVisit.addInstrument(patientProtocolInstrument);
					patientProtocolInstrument.setProtocolInstrument((ProtocolInstrument)protocolInstrument);
					patientProtocolInstrument.setPatient(patientProtocol.getPatient());
					patientProtocolInstrument.setProjName(protocolInstrument.getProjName());
				}
			}			
		}
		
		return super.doSaveAdd(context, command, errors);
	}
	
	
}
