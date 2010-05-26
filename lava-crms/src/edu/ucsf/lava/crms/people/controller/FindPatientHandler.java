package edu.ucsf.lava.crms.people.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDateRangeParamHandler;
import edu.ucsf.lava.core.dao.LavaIgnoreParamHandler;
import edu.ucsf.lava.crms.people.dto.FindPatientListItemDto;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class FindPatientHandler extends BaseListComponentHandler {

	
	public FindPatientHandler() {
		this.setHandledList("findPatient","patient");
		this.setSourceProvider(new FindPatientSourceProvider(this));
		this.setPageSize(50);
	}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter f = CrmsSessionUtils.setFilterProjectContext(sessionManager,request,Patient.newFilterInstance(getCurrentUser(request))
				.addDefaultSort("fullNameRevNoSuffix",true));
	
	
		
		//setup custom dateParamHandlers
		f.addParamHandler(new LavaDateRangeParamHandler("birthDate"));
		f.addParamHandler(new LavaDateRangeParamHandler("deathDate"));
		
		//turn off standard processing of params that need custom handling
		f.addParamHandler(new LavaIgnoreParamHandler("contactInfoEmail"));
		f.addParamHandler(new LavaIgnoreParamHandler("contactInfoPhone"));
		f.addParamHandler(new LavaIgnoreParamHandler("caregiverLastName"));
		f.addParamHandler(new LavaIgnoreParamHandler("caregiverFirstName"));
		
		return f;
	}
	
	

	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	
	}

	
	

	

	//TODO: this should be exposed in a better way
	public Map getBackingObjects(RequestContext context, Map components) {
		Map backingObjects = super.getBackingObjects(context, components);
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) backingObjects.get(this.getDefaultObjectName());
		plh.toggleFilterOn();
		return backingObjects;
	}





	/**
	 * ProjectPatientsSourceProvider 
	 */
	public static class FindPatientSourceProvider extends BaseListComponentHandler.BaseListSourceProvider {
    	public FindPatientSourceProvider(BaseListComponentHandler listHandler) {
    		super(listHandler);
    	}
    
		public List loadElements(Locale locale, Object filter) {
			LavaDaoFilter daoFilter = resetFilter((LavaDaoFilter)filter);
   			if(daoFilter.getParams().isEmpty()){
   				return new ArrayList();
   			}
			
			return convertResultsToDto(Patient.MANAGER.get(daoFilter),daoFilter);
		}
		
		public List loadList(Locale locale, Object filter) {
			LavaDaoFilter daoFilter = resetFilter((LavaDaoFilter)filter);
			if(daoFilter.getParams().isEmpty()){
   				return new ArrayList();
   			}
			
			return ScrollablePagedListHolder.createSourceList(convertResultsToDto(
					Patient.MANAGER.get(daoFilter),daoFilter),daoFilter);
		}
	
	
		
		
		
		protected List convertResultsToDto(List results, LavaDaoFilter filter){
			ArrayList dtoList = new ArrayList(results.size());
			for(Object result:results){
				if(result.getClass().isArray()){
					Object[] resultArray = (Object[])result;
					FindPatientListItemDto dto = new FindPatientListItemDto();
					int i = 0;
					//transform common results
					dto.setId((Long)resultArray[i++]);
					dto.setFullNameRevNoSuffix((String)resultArray[i++]);
					dto.setBirthDate((Date)resultArray[i++]);
					dto.setDeathDate((Date)resultArray[i++]);
					
					//transform caregiver fields
					if(!filter.isParamEmpty("caregiverFirstName") || 
							!filter.isParamEmpty("caregiverLastName")){
						dto.setCaregiverFullNameRev((String)resultArray[i++]);
					}
					if(!filter.isParamEmpty("contactInfoPhone") || 
							!filter.isParamEmpty("contactInfoEmail")){
						
						dto.setPhone1((String)resultArray[i++]);
						dto.setPhoneType1((String)resultArray[i++]);
						dto.setPhone2((String)resultArray[i++]);
						dto.setPhoneType2((String)resultArray[i++]);
						dto.setPhone3((String)resultArray[i++]);
						dto.setPhoneType3((String)resultArray[i++]);
						dto.setEmail((String)resultArray[i++]);
					}
					
					
				
					
					dtoList.add(dto);
				}
				
			}
			return dtoList;
		}
		
		protected LavaDaoFilter resetFilter(LavaDaoFilter filter){
			filter.clearAliases();
			filter.clearDaoProjections();
			filter.clearDaoParams();
			
			//add base properties
			filter.addDaoProjection(filter.daoGroupProjection("id"));  //Patient ID
			filter.addDaoProjection(filter.daoGroupProjection("fullNameRevNoSuffix"));
			filter.addDaoProjection(filter.daoGroupProjection("birthDate"));
			filter.addDaoProjection(filter.daoGroupProjection("deathDate"));
	
			
			//now check filter properties and add aliases / projections for the results
			if(!filter.isParamEmpty("caregiverFirstName") || 
					!filter.isParamEmpty("caregiverLastName")){
				filter.setAlias("caregivers","caregiver");
				filter.addDaoProjection(filter.daoGroupProjection("caregiver.fullNameRev"));
			}else if(this.sortContainsAlias("caregiver",filter)){
				filter.clearSort(); //clear filter because prior sort is now invalid based on supplied filter criteria
			}
			//get both phones and email regardless of which is specified, because it really just is about joining in the table or not
			if(!filter.isParamEmpty("contactInfoPhone") || 
					!filter.isParamEmpty("contactInfoEmail")){
				filter.setAlias("contactInfo","contactInfo");
				filter.addDaoProjection(filter.daoGroupProjection("contactInfo.phone1"));
				filter.addDaoProjection(filter.daoGroupProjection("contactInfo.phoneType1"));
				filter.addDaoProjection(filter.daoGroupProjection("contactInfo.phone2"));
				filter.addDaoProjection(filter.daoGroupProjection("contactInfo.phoneType2"));
				filter.addDaoProjection(filter.daoGroupProjection("contactInfo.phone3"));
				filter.addDaoProjection(filter.daoGroupProjection("contactInfo.phoneType3"));
				filter.addDaoProjection(filter.daoGroupProjection("contactInfo.email"));
				}else if(this.sortContainsAlias("contactInfo",filter)){
					filter.clearSort(); //clear filter because prior sort is now invalid based on supplied filter criteria
				}
			
			
			
			
				//do standard param handling
			filter.convertParamsToDaoParams();
			
			
			
			//add phone params
			if(!filter.isParamEmpty("contactInfoPhone")){
				String param = (String)filter.getParam("contactInfoPhone");
				filter.addDaoParam(
						filter.daoOr(filter.daoLikeParam("contactInfo.phone1", param), 
								filter.daoOr(filter.daoLikeParam("contactInfo.phone2", param),
										filter.daoLikeParam("contactInfo.phone3", param)
										)
								)
												
						);
				}
			if(!filter.isParamEmpty("contactInfoEmail")){
				String param = (String)filter.getParam("contactInfoEmail");
				filter.addDaoParam(	filter.daoLikeParam("contactInfo.email", param));
				}
			
			//add cargiver name params
			if(!filter.isParamEmpty("caregiverLastName")){
				String param = (String)filter.getParam("caregiverLastName");
				filter.addDaoParam(filter.daoLikeParam("caregiver.lastName", param));
				}
			
			if(!filter.isParamEmpty("caregiverFirstName")){
				String param = (String)filter.getParam("caregiverFirstName");
				filter.addDaoParam(filter.daoLikeParam("caregiver.firstName", param));
				}
			
			
			return filter;
			
			
			
		}
		protected boolean sortContainsAlias(String alias, LavaDaoFilter filter){
			for (Object key:filter.getSort().keySet()){
				if(((String)key).startsWith(alias)){
					return true;
				}
			
			}
			return false;
		}
	}	
	
	
	

}
