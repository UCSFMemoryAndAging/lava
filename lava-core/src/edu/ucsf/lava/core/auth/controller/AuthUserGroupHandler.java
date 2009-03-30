package edu.ucsf.lava.core.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthGroup;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.auth.model.AuthUserGroup;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;

public class AuthUserGroupHandler extends BaseEntityComponentHandler {
	public AuthUserGroupHandler() {
		super();
		setHandledEntity("authUserGroup", AuthUserGroup.class);
		this.setRequiredFields(new String[]{
				"groupId",
				"userId"});
	}

	
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthUserGroup userGroup = (AuthUserGroup)command;
		if(request.getParameterMap().containsKey("uid")){
			String userId = request.getParameter("uid");
			if(StringUtils.isNumeric(userId)){
				AuthUser user = (AuthUser)AuthUser.MANAGER.getOne(getFilterWithId(request,Long.valueOf(userId)));
				if(user != null){
					userGroup.setUser(user);
				}
			}
		}
		if(request.getParameterMap().containsKey("gid")){
			String groupId = request.getParameter("gid");
			if(StringUtils.isNumeric(groupId)){
				AuthGroup group = (AuthGroup)AuthGroup.MANAGER.getOne(getFilterWithId(request,Long.valueOf(groupId)));
				if(group != null){
					userGroup.setGroup(group);
				}
			}
		}
		return command;
	}
	
	
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleGroupChange(context,command, errors);
		handleUserChange(context,command, errors);
		return super.doSave(context, command, errors);
	}
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleGroupChange(context,command, errors);
		handleUserChange(context,command, errors);
		return super.doSaveAdd(context, command, errors);
	}

	protected Event doReRender(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleGroupChange(context,command, errors);
		handleUserChange(context,command, errors);
		return super.doReRender(context, command, errors);
	}
	
	protected void handleGroupChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthUserGroup aug = (AuthUserGroup)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(aug.getGroupId(),aug.getGroup())){
			if(aug.getGroupId()==null){
				aug.setGroup(null); 	//clear the association
			}else{
				AuthGroup g = (AuthGroup)AuthGroup.MANAGER.getOne(this.getFilterWithId(request,aug.getGroupId()));
				aug.setGroup(g);
			}
		}
	}

	protected void handleUserChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthUserGroup aug = (AuthUserGroup)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(aug.getUserId(),aug.getUser())){
			if(aug.getUserId()==null){
				aug.setUser(null); 	//clear the association
			}else{
				AuthUser u = (AuthUser)AuthUser.MANAGER.getOne(this.getFilterWithId(request, aug.getUserId()));
				aug.setUser(u);
			}
		}
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//load up dynamic lists
	
	
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		AuthUserGroup aug = (AuthUserGroup)((ComponentCommand)command).getComponents().get("authUserGroup");
		dynamicLists.put("auth.users", listManager.getDynamicList(getCurrentUser(request),"auth.users"));
		dynamicLists.put("auth.groups", listManager.getDynamicList(getCurrentUser(request),"auth.groups"));
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	

}
