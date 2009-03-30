package edu.ucsf.lava.core.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthPermission;
import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;


public class AuthPermissionHandler extends BaseEntityComponentHandler {
	public AuthPermissionHandler() {
		super();
		setHandledEntity("authPermission", AuthPermission.class);
		this.setRequiredFields(new String[]{
				"roleId",
				"permitDeny",
				"scope",
				"module",
				"section",
				"target",
				"mode"});
	}

	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleRoleChange(context,command, errors);
		return super.doSave(context, command, errors);
	}
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleRoleChange(context,command, errors);
		return super.doSaveAdd(context, command, errors);
	}

	protected Event doReRender(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleRoleChange(context,command, errors);
		return super.doReRender(context, command, errors);
	}
	
	protected void handleRoleChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthPermission ap = (AuthPermission)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(ap.getRoleId(),ap.getRole())){
			if(ap.getRoleId()==null){
				ap.setRole(null); 	//clear the association
			}else{
				AuthRole r = (AuthRole)AuthRole.MANAGER.getOne(getFilterWithId(request, ap.getRoleId()));
				ap.setRole(r);
			}
		}
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthPermission permission = (AuthPermission)command;
		if(request.getParameterMap().containsKey("roleId")){
			String roleId = request.getParameter("roleId");
			if(StringUtils.isNumeric(roleId)){
				AuthRole role = (AuthRole)AuthRole.MANAGER.getOne(getFilterWithId(request,Long.valueOf(roleId)));
				if(role != null){
					permission.setRole(role);
				}
			}
		}
		
		return command;
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
	
		dynamicLists.put("auth.roles", listManager.getDynamicList(getCurrentUser(request),"auth.roles"));
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
	

}
