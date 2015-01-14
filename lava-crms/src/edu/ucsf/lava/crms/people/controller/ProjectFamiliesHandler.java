package edu.ucsf.lava.crms.people.controller;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProjectFamiliesHandler extends CrmsListComponentHandler {

	public ProjectFamiliesHandler() {
		super();
		// there is no Family entity per se; rather, a Family is a Patient with a familyStatus of "Family" (or
		// "Family Study") and relationToProband of "Proband" (there can only be one "Proband" per family)
		// the familyId is the patientId of this Patient, and all other family members are Patients with
		// the same familyId
		this.setHandledList("projectFamilies","patient");
		this.setSourceProvider(new ProjectFamiliesSourceProvider(this, Patient.class));
		this.setPageSize(100);
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = CrmsSessionUtils.setFilterProjectContext(sessionManager,request,Patient.newFilterInstance(getCurrentUser(request)));
		filter.addDefaultSort("fullNameRev",true);
		return filter;
	}


	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}


	/**
	 * ProjectFamiliesSourceProvider 
	 */
	public static class ProjectFamiliesSourceProvider extends CrmsListComponentHandler.BaseListSourceProvider {
		
    	public ProjectFamiliesSourceProvider(CrmsListComponentHandler listHandler, Class entityClass) {
    		super(listHandler, entityClass);
    	}
    
		public List loadElements(Locale locale, Object filter) {
			LavaDaoFilter daoFilter = (LavaDaoFilter)filter;
			daoFilter.convertParamsToDaoParams();
			daoFilter.addDaoParam(daoFilter.daoInParam("familyStatus",new String[]{Patient.FAMILY_STATUS_FAMILY,Patient.FAMILY_STATUS_FAMILYSTUDY}));
			daoFilter.addDaoParam(daoFilter.daoEqualityParam("relationToProband", Patient.FAMILY_RELATION_PROBAND));
			return listHandler.getList(entityClass,daoFilter);
		}
		
		public List loadList(Locale locale, Object filter) {
    		LavaDaoFilter daoFilter = (LavaDaoFilter)filter;
			daoFilter.convertParamsToDaoParams();
			daoFilter.addDaoParam(daoFilter.daoInParam("familyStatus",new String[]{Patient.FAMILY_STATUS_FAMILY,Patient.FAMILY_STATUS_FAMILYSTUDY}));
			daoFilter.addDaoParam(daoFilter.daoEqualityParam("relationToProband", Patient.FAMILY_RELATION_PROBAND));
			return ScrollablePagedListHolder.createSourceList(listHandler.getList(entityClass,daoFilter),daoFilter);
		}
	}

}
