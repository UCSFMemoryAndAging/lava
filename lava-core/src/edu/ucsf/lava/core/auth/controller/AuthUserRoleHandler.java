package edu.ucsf.lava.core.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthGroup;
import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.auth.model.AuthUserGroup;
import edu.ucsf.lava.core.auth.model.AuthUserRole;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;

public class AuthUserRoleHandler extends BaseEntityComponentHandler {
	public AuthUserRoleHandler() {
		super();
		setHandledEntity("authUserRole", AuthUserRole.class);
		this.setRequiredFields(new String[]{
				"roleId","scope"});
	}

	
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthUserRole userRole = (AuthUserRole)command;
		if(request.getParameterMap().containsKey("roleId")){
			String roleId = request.getParameter("roleId");
			if(StringUtils.isNumeric(roleId)){
				AuthRole role = (AuthRole)AuthRole.MANAGER.getOne(getFilterWithId(request,Long.valueOf(roleId)));
				if(role != null){
					userRole.setRole(role);
				}
			}
		}
		if(request.getParameterMap().containsKey("gid")){
			String groupId = request.getParameter("gid");
			if(StringUtils.isNumeric(groupId)){
				AuthGroup group = (AuthGroup)AuthGroup.MANAGER.getOne(getFilterWithId(request,Long.valueOf(groupId)));
				if(group != null){
					userRole.setGroup(group);
				}
			}
		}
		if(request.getParameterMap().containsKey("uid")){
			String userId = request.getParameter("uid");
			if(StringUtils.isNumeric(userId)){
				AuthUser user = (AuthUser)AuthUser.MANAGER.getOne(getFilterWithId(request,Long.valueOf(userId)));
				if(user != null){
					userRole.setUser(user);
				}
			}
		}
	
		return command;
	}
	
	
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleRoleChange(context,command, errors);
		handleGroupChange(context,command, errors);
		handleUserChange(context,command, errors);
		Event validationResult = this.validate(context, command, errors);
		if(validationResult.getId().equals(SUCCESS_FLOW_EVENT_ID)){
			return super.doSave(context, command, errors);
		}
		return validationResult;
	}
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleRoleChange(context,command, errors);
		handleGroupChange(context,command, errors);
		handleUserChange(context,command, errors);
		Event validationResult = this.validate(context, command, errors);
		if(validationResult.getId().equals(SUCCESS_FLOW_EVENT_ID)){
			return super.doSaveAdd(context, command, errors);
		}
		return validationResult;
	}

	protected Event doReRender(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleRoleChange(context,command, errors);
		handleGroupChange(context,command, errors);
		handleUserChange(context,command, errors);
		return super.doReRender(context, command, errors);
	}
	
	protected void handleGroupChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthUserRole aur = (AuthUserRole)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(aur.getGroupId(),aur.getGroup())){
			if(aur.getGroupId()==null){
				aur.setGroup(null); 	//clear the association
			}else{
				AuthGroup g = (AuthGroup)AuthGroup.MANAGER.getOne(getFilterWithId(request, aur.getGroupId()));
				aur.setGroup(g);
			}
		}
	}

	protected Event validate(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthUserRole aur = (AuthUserRole)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(aur.getGroupId()==null && aur.getUserId()==null){
			errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"authUserRoleNoUserOrGroup.command"},null, ""));
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	
	protected void handleRoleChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthUserRole aur = (AuthUserRole)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(aur.getRoleId(),aur.getRole())){
			if(aur.getRoleId()==null){
				aur.setRole(null); 	//clear the association
			}else{
				AuthRole r = (AuthRole)AuthRole.MANAGER.getOne(getFilterWithId(request, aur.getRoleId()));
				aur.setRole(r);
			}
		}
	}
	protected void handleUserChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthUserRole aur = (AuthUserRole)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(aur.getUserId(),aur.getUser())){
			if(aur.getUserId()==null){
				aur.setUser(null); 	//clear the association
			}else{
				AuthUser u = (AuthUser)AuthUser.MANAGER.getOne(getFilterWithId(request, aur.getUserId()));
				aur.setUser(u);
			}
		}
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		AuthUserGroup aug = (AuthUserGroup)((ComponentCommand)command).getComponents().get("authUserGroup");
		dynamicLists.put("auth.users", listManager.getDynamicList(getCurrentUser(request),"auth.users"));
		dynamicLists.put("auth.groups", listManager.getDynamicList(getCurrentUser(request),"auth.groups"));
		dynamicLists.put("auth.roles", listManager.getDynamicList(getCurrentUser(request),"auth.roles"));
		dynamicLists.put("projectUnit.projects", listManager.getDynamicList(getCurrentUser(request),"projectUnit.projects"));
		dynamicLists.put("projectUnit.units", listManager.getDynamicList(getCurrentUser(request),"projectUnit.units"));
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	

}
