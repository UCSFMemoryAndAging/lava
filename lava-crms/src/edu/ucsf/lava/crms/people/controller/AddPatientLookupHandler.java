package edu.ucsf.lava.crms.people.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.people.model.AddPatientCommand;
import edu.ucsf.lava.crms.people.model.Patient;


public class AddPatientLookupHandler extends BaseListComponentHandler {
	
	public AddPatientLookupHandler() {
		this.setHandledList("foundPatients","patient");
		this.setEntityForStandardSourceProvider(Patient.class);
	}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = Patient.newFilterInstance(getCurrentUser(request));
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}

	public Event preEventHandled(RequestContext context, Object command, BindingResult errors) throws Exception {
		if(ActionUtils.getEventId(context).equalsIgnoreCase("addPatient__saveAdd")){
			return handlePreAddPatientSaveAdd(context,command,errors);
		}
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	protected Event handlePreAddPatientSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		Map components = ((ComponentCommand)command).getComponents();
		AddPatientCommand apc = (AddPatientCommand) components.get("addPatient");
		ScrollablePagedListHolder entityList = (ScrollablePagedListHolder) ((ComponentCommand)command).getComponents().get("foundPatients");
		LavaDaoFilter filter = (LavaDaoFilter)entityList.getFilter();
		if (apc.getPatient().getId()==null){
			filter.clearParams();
			filter.setParam("deidentified", apc.getDeidentified());
			filter.setParam("subjectId",apc.getSubjectId());
			filter.setParam("firstName",apc.getPatient().getFirstName());
			filter.setParam("lastName",apc.getPatient().getLastName());
			filter.setParam("birthDate",apc.getPatient().getBirthDate());
		}else{
			filter.clearParams();
		}
		((BaseListSourceProvider)entityList.getSourceProvider()).setListHandler(this);
		entityList.doRefresh();
		if (entityList.getNrOfElements()>0){
			
			errors.addError(new ObjectError(errors.getObjectName(),new String[]{"addPatient.foundExistingPatients"},null,""));
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
		
	
	public LavaDaoFilter onPostFilterParamConversion(LavaDaoFilter daoFilter) {

		if (daoFilter.getParam("deidentified")!=null && (Boolean)daoFilter.getParam("deidentified")){
			if(daoFilter.getParam("subjectId")==null || daoFilter.getParam("birthDate")==null){
				return emptyPatientLookupDaoFilter(daoFilter);
			}
			//setup dao params for query
			daoFilter.addDaoParam(daoFilter.daoNamedParam("firstName",daoFilter.getParam("subjectId")));
			daoFilter.addDaoParam(daoFilter.daoNamedParam("lastName",Patient.DEIDENTIFIED));
			daoFilter.addDaoParam(daoFilter.daoNamedParam("birthDate",daoFilter.getParam("birthDate")));
		}else{
			
			if(daoFilter.getParam("firstName")==null || daoFilter.getParam("lastName")==null || daoFilter.getParam("birthDate")==null){
				return emptyPatientLookupDaoFilter(daoFilter);
			}
			daoFilter.addDaoParam(daoFilter.daoNamedParam("firstName",daoFilter.getParam("firstName")));
			daoFilter.addDaoParam(daoFilter.daoNamedParam("lastName",daoFilter.getParam("lastName")));
			daoFilter.addDaoParam(daoFilter.daoNamedParam("birthDate",daoFilter.getParam("birthDate")));
		}
		return daoFilter;
	}
	
	protected LavaDaoFilter emptyPatientLookupDaoFilter(LavaDaoFilter daoFilter){
		daoFilter.addDaoParam(daoFilter.daoNamedParam("firstName",(String)null));
		daoFilter.addDaoParam(daoFilter.daoNamedParam("lastName",(String)null));
		daoFilter.addDaoParam(daoFilter.daoNamedParam("birthDate",(Date)null));
		return daoFilter;
	}
	
	public List getList(Class entityClass, LavaDaoFilter daoFilter) {
		return Patient.addPatientLookup(daoFilter);
	}

}
