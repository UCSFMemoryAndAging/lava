package edu.ucsf.lava.crms.people.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaNonParamHandler;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.people.model.Caregiver;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class FamilyMembersHandler extends CrmsListComponentHandler {

	public FamilyMembersHandler(){
		super();
		// family members are Patient entities
		this.setHandledList("familyMembers","patient");
		this.setSourceProvider(new FamilyMembersSourceProvider(this, Patient.class));
		this.setPageSize(100);
		CrmsSessionUtils.setIsPatientContext(this);
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = Patient.newFilterInstance(getCurrentUser(request));
		setCurrentPatientToProband(context);
		Patient patient = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
		// use param with LavaNonParamHandler as a mechanism to transfer the familyId value to the SourceProvider
		if (patient != null){
			filter.setParam("familyId", patient.getId());
		} else {
			filter.setParam("familyId", null);  //this should never happen, and this will never return any incorrect family members
		}
		filter.addParamHandler(new LavaNonParamHandler("familyId"));
	
		filter.addDefaultSort("fullNameRevNoSuffix",true);
		return filter;
		
	}


	//change to proband if necessary
	public void setCurrentPatientToProband(RequestContext context) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Patient patient = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
		if (patient != null && patient.getFamilyId() !=null && !patient.getFamilyId().equals(patient.getId())){
			CrmsSessionUtils.setCurrentPatient(sessionManager, request, patient.getFamilyId());
		}
	}

	public void updateFilterFromContext(LavaDaoFilter filter,RequestContext context, Map components) {
		
	}



	/**
	 * FamilyMembersSourceProvider 
	 */
	public static class FamilyMembersSourceProvider extends CrmsListComponentHandler.BaseListSourceProvider {
		public FamilyMembersSourceProvider(CrmsListComponentHandler listHandler, Class entityClass) {
			super(listHandler, entityClass);
		}

		public List loadElements(Locale locale, Object filter) {
			LavaDaoFilter daoFilter = (LavaDaoFilter)filter;
			daoFilter.convertParamsToDaoParams();
			Long familyId = (Long) daoFilter.getParam("familyId");
			if(familyId == null){
				return new ArrayList();
			}
			daoFilter.addDaoParam(daoFilter.daoEqualityParam("familyId",familyId));
			return listHandler.getList(entityClass,daoFilter);
		}
		
		public List loadList(Locale locale, Object filter) {
			LavaDaoFilter daoFilter = (LavaDaoFilter)filter;
			daoFilter.convertParamsToDaoParams();
			Long familyId = (Long) daoFilter.getParam("familyId");
			if(familyId == null){
				return ScrollablePagedListHolder.createSourceList(new ArrayList(),daoFilter);
			}
			daoFilter.addDaoParam(daoFilter.daoEqualityParam("familyId",familyId));
			return ScrollablePagedListHolder.createSourceList(listHandler.getList(entityClass,daoFilter),daoFilter);
		}
	}

}
