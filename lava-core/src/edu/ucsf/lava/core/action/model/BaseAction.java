package edu.ucsf.lava.core.action.model;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import edu.ucsf.lava.core.action.ActionDefinitions;
import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.webflow.builder.FlowTypeBuilder;


/**
 * Action ID format:
 * 
 * [scope].[module].[section].[target]
 * 
 * Scope: for core, "standard" functionality the scope is always 'lava'.  For custom
 * project functionality that will override the standard action, the scope derived from
 * the project name, or more accurately, from the project projUnitDesc, e.g. 
 * e.g. 'clinic', 'adrc_ucsf' when the projUnitDesc is 'ADRC[UCSF]'
 * 
 * 
 * module: top level organizational area for the action.  e.g. people, scheduling, enrollment.
 * section: secondary organizational area within a module. e.g. contact log, caregivers, etc. 
 * 					for actions that apply to the module, the section value is 'module'
 *  				
 * 
 * target: a noun/[plural noun/adj] conbination describing the action target, e.g. 'consentHistory', 
 * 		'visitInstruments', 'projectMetrics', 'visitDetails'.   
 * 	
 * 
 * 1) Action paths are constructed from the action id 
 * 		e.g. lava.people.patient.patient --> lava/people/patient/patient.lava
 * 
 * 2) views are resolved using the target,
 * 		e.g. patient target resolves to patient.jsp
 *    and the component mode and component view are put into the model to indicate
 *    whether doing add,view,edit,delete
 * 			 
 * 
 * @author jhesse
 *
 */
public class BaseAction implements Action, Serializable, Cloneable{
	
	protected transient ActionDefinitions actionDefinitions;
	protected String instance;
	protected String scope;
	protected String module;
	protected String section;
	protected String target;
	protected String idParamName;
	protected String id;
	protected String actionUrl;
	protected String actionView;
	protected String viewBasePath;
	protected String customizedViewBasePath;
	protected String description;
	protected boolean homeDefault = false;
	protected boolean moduleDefault = false;
	protected boolean sectionDefault = false;
	protected transient FlowTypeBuilder flowTypeBuilder = null;
	protected List<String> parentFlows = new ArrayList<String>();
	protected List<String> subFlows = new ArrayList<String>();
	protected String customizedFlow; 
	protected List<String> customizingFlows = new ArrayList<String>();
	protected String defaultMode;
	
	

	protected Map params = new HashMap();

	//Called when some element of the id is changed.
	protected void resetId(){
		StringBuffer id = new StringBuffer(this.instance).append(ActionUtils.ACTION_ID_DELIMITER)
			.append(this.scope).append(ActionUtils.ACTION_ID_DELIMITER)
			.append(this.module).append(ActionUtils.ACTION_ID_DELIMITER)
			.append(this.section).append(ActionUtils.ACTION_ID_DELIMITER)
			.append(this.target).append(ActionUtils.ACTION_ID_DELIMITER);
			
		setId(new String(id));
		
	}

	public BaseAction(){
	}
	
	
	
	public boolean equalsId(Object action){
		if(!Action.class.isAssignableFrom(action.getClass())){
			return false;
		}
		Action compareTo = (Action)action;
		//if same action id then inspect parameters to see if they are the same
		if (this.getId() == compareTo.getId()){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean equals(Object action){
		return equals(action,true);
	}
	public boolean equals(Object action,boolean matchMode){
		
		if(!Action.class.isAssignableFrom(action.getClass())){
			return false;
		}
		Action compareTo = (Action)action;
		//same action id
		if (this.getId() != compareTo.getId()){
			return false;
			}
		//same entity id?
		String id = (String) this.getParam(this.idParamName);
		String compareToId = (String) compareTo.getParam(compareTo.getIdParamName());
		if ((id == null && compareToId != null) || (id != null && compareToId == null)) {
			return false; 
		}
		if (id != null && compareToId != null && !(id.equalsIgnoreCase(compareToId))){
			return false;
		}

		/*  The mode check created a large issue with the page flow (cycling back through screens that you shouldn't)
		 * which is bigger than the bug that the mode check fixes...for now we won't do the mode check..eventually we need
		 * to build a stack based action history where we push and pop actions on and off the stack/breadcrumb...
		 * jhesse 1/7/07
		 
		//same mode?
		if(matchMode){
			if(!((String)this.getParam(ActionUtils.MODE_PARAMETER_NAME))
			.equalsIgnoreCase((String)compareTo.getParam(ActionUtils.MODE_PARAMETER_NAME))){
				return false;
			}
		}
		*/
		return true;	
	}
	//TODO: deterrmine if this method is needed....doesn't look like it
	public boolean equalsIdAndEntityId(Object action){
		if(!Action.class.isAssignableFrom(action.getClass())){
			return false;
		}
		Action compareTo = (Action)action;
		//if same action id then inspect parameters to see if they are the same
		if (this.id == compareTo.getId()){
			if(((String)this.getParam(ActionUtils.EVENT_PARAMETER_NAME))
					.equalsIgnoreCase((String)compareTo.getParam(ActionUtils.EVENT_PARAMETER_NAME))){
				return true;
			}
		}
		return false;	
	}
	public void setParam(Object key, Object value){
		if(value == null){
			if (params.containsKey(key)){
				params.remove(key);
				}
		}else{
			params.put(key,value);
		}
		
	}
	public Object getParam(Object key){
		if(params.containsKey(key) && (params.get(key)!=null)){
			return params.get(key);
		}
		return null;
		
	}
	public Map getParams() {
		return params;
	}
	public void setParams(Map params) {
		this.params.clear();
		this.params.putAll(params);
	}
	public String getDescription() {
		return StringUtils.isNotEmpty(description) ? description : new String("");
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.actionUrl = "";
		this.id = id;
		
	}

	public String getIdParamName() {
		return StringUtils.isNotEmpty(idParamName) ? idParamName : "id";
	}
	public void setIdParamName(String idParamName) {
		this.idParamName = idParamName;
	}
	
	public String getModule() {
		if(StringUtils.isNotEmpty(module)){
			return module;
		}else{
			module = ActionUtils.getModule(id);
			return module;
		}
	}
	public void setModule(String module) {
		this.module = module;
		resetId();
	}
	
	public String getScope() {
		if(StringUtils.isNotEmpty(scope)){
			return scope;
		}else{
			scope = ActionUtils.getScope(id);
			return scope;
		}
	}
	public void setScope(String scope) {
		this.scope = scope;
		resetId();
	}
	public String getSection() {
		if(StringUtils.isNotEmpty(section)){
			return section;
		}else{
			section = ActionUtils.getSection(id);
			return section;
		}
	}
	public void setSection(String section) {
		this.section = section;
		resetId();
	}
	
	public String getInstance() {
		if(StringUtils.isNotEmpty(instance)){
			return instance;
		}else{
			instance = ActionUtils.getInstance(id);
			return instance;
		}
	}
	public void setInstance(String instance) {
		this.instance = instance;
		resetId();
	}
	

	
	
	
	public String getTarget() {
		if(StringUtils.isNotEmpty(target)){
			return target;
		}else{
			target = ActionUtils.getTarget(id);
			return target;
		}
	}
	public void setTarget(String target) {
		this.target = target;
		resetId();
	}

	
	public boolean recordInActionHistory(){
		return true;
	}
	
	
	

	public void setActionView(String actionView) {
		this.actionView = actionView;
	}

	public String getActionView(){
		if(this.actionView!=null){return this.actionView;}
		
		StringBuffer viewPath = new StringBuffer();
		if(isCustomizedInstanceAction() && null!=getCustomizedViewBasePath()){
			viewPath.append(getCustomizedViewBasePath());
		}else if(null!=getViewBasePath()){
			viewPath.append(getViewBasePath()).append(ActionUtils.ACTION_PATH_DELIMITER);
		}
		
		
		viewPath.append(ActionUtils.getActionPath(this.getId()));
		this.actionView = viewPath.toString();
		return actionView;
	}
	
	
	public String getActionUrlWithIdParam() {
		StringBuffer actionUrl = new StringBuffer(getActionUrl());
		boolean idParamAdded = false;
		if(params.containsKey(this.idParamName) && 
				StringUtils.isNotEmpty(getParam(this.idParamName).toString())){
			actionUrl.append("?").append(this.idParamName)
				.append("=").append(getParam(this.idParamName).toString());
			idParamAdded = true;
			}
		return new String(actionUrl);
	}

	public String getActionUrlWithModeParam() {
		StringBuffer actionUrl = new StringBuffer(getActionUrl());
		if(params.containsKey(ActionUtils.MODE_PARAMETER_NAME) && 
				StringUtils.isNotEmpty(getParam(ActionUtils.MODE_PARAMETER_NAME).toString())){
			actionUrl.append("?").append(ActionUtils.MODE_PARAMETER_NAME)
				.append("=").append(getParam(ActionUtils.MODE_PARAMETER_NAME).toString());
		}
		return new String(actionUrl);
	}

	public String getActionUrlWithParams() {
		StringBuffer actionUrl = new StringBuffer(getActionUrl());
		boolean idParamAdded = false;
		actionUrl.append("?");
		for(Object paramName: params.keySet()){
				if(StringUtils.isNotEmpty(getParam(paramName).toString()))
				{
					actionUrl.append((String)paramName)
					.append("=").append(getParam(paramName).toString()).append("&");
				}
			}
		return new String(actionUrl);
	}
	
	public String getActionUrl() {
		if(StringUtils.isNotEmpty(actionUrl)){
			return actionUrl;
		}else{
			actionUrl = ActionUtils.getActionUrl(id);
			return actionUrl;
		}
	
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	

	
	
	
	



	public boolean getHomeDefault() {
		return homeDefault;
	}

	public void setHomeDefault(boolean homeDefault) {
		this.homeDefault = homeDefault;
	}

	public boolean getModuleDefault() {
		return moduleDefault;
	}

	public void setModuleDefault(boolean projectModuleDefault) {
		this.moduleDefault = projectModuleDefault;
	}

	public boolean getSectionDefault() {
		return sectionDefault;
	}

	public void setSectionDefault(boolean projectSectionDefault) {
		this.sectionDefault = projectSectionDefault;
	}
	
	public FlowTypeBuilder getFlowTypeBuilder() {
		return flowTypeBuilder;
	}

	public void setFlowTypeBuilder(FlowTypeBuilder flowTypeBuilder) {
		this.flowTypeBuilder = flowTypeBuilder;
	}

	public String getFlowType(){
		if(this.flowTypeBuilder==null){return FlowTypeBuilder.FLOW_TYPE_NONE;}
		return this.flowTypeBuilder.getType();
	}
	
	public String getDefaultFlowMode(){
		if(this.flowTypeBuilder==null){return FlowTypeBuilder.DEFAULT_FLOW_MODE;}
		return this.flowTypeBuilder.getDefaultFlowMode();
	}
	public void clearParentFlow(String parentFlow)
	{
		if (!this.parentFlows.contains(parentFlow)){
			this.parentFlows.remove(parentFlow);
		}
	}
	
	public void clearParentFlows(){
		this.parentFlows.clear();
	}


	public void setParentFlow(String parentFlow) {
		if (!this.parentFlows.contains(parentFlow)){
			this.parentFlows.add(parentFlow);
		}
	}

	public List<String> getParentFlows() {
		return parentFlows;
	}

	public void setParentFlows(List<String> parentFlows) {
		for (String s : parentFlows){
			if (!this.parentFlows.contains(s)){
				this.parentFlows.add(s);
			}
		}
	}
	
	public void clearSubFlow(String subFlow){
		if(! this.subFlows.contains(subFlow)){
			this.subFlows.remove(subFlow);
		}
	
	}
	public void clearSubFlows(){
		this.subFlows.clear();
	}
	

	public void setSubFlow(String subFlow) {
		if(! this.subFlows.contains(subFlow)){
			this.subFlows.add(subFlow);
		}
		
	}

	public List<String> getSubFlows() {
		return subFlows;
	}

	public void setSubFlows(List<String> subFlows) {
		for (String s:subFlows){
			if (!this.subFlows.contains(s)){
				this.subFlows.add(s);
			}
		}
	}

	
	public void clearCustomizingFlow(String customizingFlow){
		if(! this.customizingFlows.contains(customizingFlow)){
			this.customizingFlows.remove(customizingFlow);
		}
	
	}
	public void clearCustomizingFlows(){
		this.customizingFlows.clear();
	}

	public List<String> getCustomizingFlows() {
		return customizingFlows;
	}

	public void setCustomizingFlows(List<String> customizingFlows) {
		this.customizingFlows = customizingFlows;
	}
	
	public void setCustomizingFlow(String customizingFlow) {
		if(! this.customizingFlows.contains(customizingFlow)){
			this.customizingFlows.add(customizingFlow);
		}
		
	}
	
	

	
	public void clearCustomizedFlow(){
		this.customizedFlow = null;
	}
	
	
	public void setCustomizedFlow(String customizedFlow) {
		this.customizedFlow = customizedFlow;
	}

	public String getCustomizedFlow() {
		return customizedFlow;
	}



	public ActionDefinitions getActionDefinitions() {
		return actionDefinitions;
	}

	public void setActionDefinitions(ActionDefinitions actionDefinitions) {
		this.actionDefinitions = actionDefinitions;
	}

			
	public Object clone(){
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try{	
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			oos = new ObjectOutputStream(bos);
			oos.writeObject(this);   
			oos.flush();             
			ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bin);  
			Action newAction = (Action)ois.readObject();
			return onClone(newAction);
		}catch(Exception e){
			throw new InternalError(e.toString());
		}finally{
			try{
				oos.close();
				ois.close();
			}catch(IOException e){}
		}
	}

	/**
	 * This method provides an opportunity to copy transient objects from the current action
	 * to the new "clone" action.   This avoids creating new copies of internally referenced objects
	 * that should not themselves be cloned.  (Note: the clone method implemented is by default a deep clone). 
	 * 
	 * Override this in subclasses of BaseAction as needed. 
	 * 
	 * @param newAction
	 * @return
	 */
	public Action onClone(Action newAction){
		if(BaseAction.class.isAssignableFrom(newAction.getClass())){
			((BaseAction)newAction).setActionDefinitions(this.actionDefinitions);
			((BaseAction)newAction).setFlowTypeBuilder(this.flowTypeBuilder);
		}
		return newAction;
	}

	public boolean isCustomizedInstanceAction(){
		if(ActionUtils.LAVA_INSTANCE_IDENTIFIER.equals(this.getInstance())){
			return false;
		}
		return true;
	}
	


	public String getCustomizedViewBasePath() {
		return customizedViewBasePath;
	}

	public void setCustomizedViewBasePath(String customizedViewBasePath) {
		this.customizedViewBasePath = customizedViewBasePath;
	}

	public String getViewBasePath() {
		return viewBasePath;
	}

	public void setViewBasePath(String viewBasePath) {
		this.viewBasePath = viewBasePath;
	}



	public String makeCacheKey(String mode){
		return new StringBuffer(getScope()).append(ActionUtils.ACTION_ID_DELIMITER)
			.append(getModule()).append(ActionUtils.ACTION_ID_DELIMITER)
			.append(getSection()).append(ActionUtils.ACTION_ID_DELIMITER)
			.append(getTarget()).append(ActionUtils.ACTION_ID_DELIMITER)
			.append(mode).toString();
	}
	
	
	public String makeCacheKey(){
		String mode = (String)getParam(ActionUtils.MODE_PARAMETER_NAME);
		if(mode==null){
			mode = getDefaultFlowMode();
		}
		return makeCacheKey(mode);
	}



	
	
}
