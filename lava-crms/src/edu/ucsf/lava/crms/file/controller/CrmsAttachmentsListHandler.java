package edu.ucsf.lava.crms.file.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaEqualityParamHandler;
import edu.ucsf.lava.core.dao.LavaNullParamHandler;
import edu.ucsf.lava.core.file.controller.AttachmentsListHandler;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.enrollment.model.Consent;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

/*
 * This handler has been configured as a secondary handler on CrmsComponentFormAction (via the
 * crmsEntityComponentFormAction bean configuration) so that any action that uses CrmsComponentFormAction
 * will automatically have support for a secondary attachment list component.
 * 
 * This works in conjunction with including an attachments list view as part of the overall view
 * for the action, e.g. enrollmentStatus.jsp has
 * 
 * 	<c:set var="id"><tags:componentProperty component="${component}" property="id"/></c:set>
 *	<c:import url="/WEB-INF/jsp/crms/enrollment/attachments/enrollmentAttachmentListContent.jsp">
 *		<c:param name="pageName">${component}</c:param>
 *		<c:param name="enrollStatId">${id}</c:param>
 *	</c:import>
 *
 *  enrollmentAttachmentListContent.jsp displays a list which is obtained by this controller.
 *  
 *  Note that for the attachment list to work properly, webflow transitions to the list item actions
 *  must be in place. e.g. for enrollmentStatus, the enrollmentAttachment action specifies the 
 *  enrollmentStatus action as a parentFlow, as follows:
 *  	<bean id="lava.crms.enrollment.attachments.enrollmentAttachment" parent="crmsEntityFlowAction">
 *			<property name="parentFlows"><list>
 *              ...
 *				<value>lava.crms.enrollment.status.enrollmentStatus</value>
 *			</list></property>
 *	</bean>	
 *
 *  
 *  The list has an Add button, e.g.
 *  <tags:eventButton buttonText="Add" component="assessmentAttachment" action="add" pageName="${pageName}" javascript="submitted=true;" parameters="instrId,${instrId}"/> 
 *  
 *  This action will allow the user to create an attachment, a CrmsFile entity, where the instrId 
 *  property of CrmsFile references the Instrument entity of the current view.
 *  
 *  Note that eventButton is used instead of listActionURLButton because the eventButton posts/binds the current form so if
 *  in edit mode for the Instrument, any modified values will be submitted and retained upon returning to that form (in
 *  edit mode) from the attachment CRUD form.
 *  
 */

public class CrmsAttachmentsListHandler extends AttachmentsListHandler {
	
	public CrmsAttachmentsListHandler(){
		this.setHandledList("attachments", CrmsFile.class, "attachment");
		this.setEntityForStandardSourceProvider(CrmsFile.class);
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = CrmsSessionUtils.setFilterProjectContext(sessionManager, request, CrmsFile.newFilterInstance(this.getCurrentUser(request)));
		
		// retrieve entity context
		if (components.containsKey("instrument")){
			filter.addParamHandler(new LavaEqualityParamHandler("instrId"));
			filter.setParam("instrId", ((Instrument)components.get("instrument")).getId());
		} else if (components.containsKey("enrollmentStatus")){
			filter.addParamHandler(new LavaEqualityParamHandler("enrollStatId"));
			filter.setParam("enrollStatId", ((EnrollmentStatus)components.get("enrollmentStatus")).getId());
		} else if (components.containsKey("consent")){
			filter.addParamHandler(new LavaEqualityParamHandler("consentId"));
			filter.setParam("consentId", ((Consent)components.get("consent")).getId());
		} else if (components.containsKey("visit")){
			filter.addParamHandler(new LavaEqualityParamHandler("visitId"));
			filter.setParam("visitId", ((Visit)components.get("visit")).getId());
		} else if (components.containsKey("patient")){
			filter.addParamHandler(new LavaEqualityParamHandler("pidn"));
			filter.setParam("pidn", ((Patient)components.get("patient")).getId());
			filter.addParamHandler(new LavaNullParamHandler("enrollStatId"));
			filter.setParam("enrollStatId", null);
			filter.addParamHandler(new LavaNullParamHandler("consentId"));
			filter.setParam("consentId", null);
			filter.addParamHandler(new LavaNullParamHandler("visitId"));
			filter.setParam("visitId", null);
			filter.addParamHandler(new LavaNullParamHandler("instrId"));
			filter.setParam("instrId", null);
		} else{
			filter.setParam("DO_NOT_EXECUTE", true);
		}
		
		return filter;
	}

	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}

	@Override
	public Event preEventHandled(RequestContext context, Object command,
			BindingResult errors) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		String eventName = ActionUtils.getEventName(context);
		if (eventName.equals("confirmDelete")) {
			// have to delete attachments before deleting entity (CrmsFile has foreign key associations
			// to Patient/EnrollmentStatus/Consent/Visit/Instrument, so deleting the latter would fail
			// if any attached CrmsFile still exists)
			ScrollablePagedListHolder listHolder = (ScrollablePagedListHolder) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
			List<CrmsFile> attachments = (List<CrmsFile>) listHolder.getSourceAsEntityList(); 
			for (CrmsFile file : attachments) {
				file.delete(); // LavaFile.afterDelete will delete the file from repository
			}
		}
		
		return super.preEventHandled(context, command, errors);
	}

}
