package edu.ucsf.lava.core.view.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.view.LavaRenderer;

public abstract class BaseRenderer implements LavaRenderer {

	protected List<Class> handledEntityClasses = new ArrayList<Class>();
	protected List<Class> handledRenderObjectClasses = new ArrayList<Class>();
	
	// attributes are set by the propertyOverrideConfigurer bean using the context .properties files
	protected Map<String,Object> attributes = new HashMap<String, Object>();
	
	public BaseRenderer(){
		CoreManagerUtils.getViewManager().addRenderEngine(this);
	}
		
	public abstract List<BaseRenderObject> createRenderObjects(Object entity, Class renderObjectClass, RenderParams renderParams) throws Exception;
	
	public List<BaseRenderObject> createRenderObjects(Object entity, RenderParams renderParams) throws Exception {
		return createRenderObjects(entity, getDefaultRenderObjectClass(), renderParams);
	}
	
	// convenience method for returning render code for all render objects
	public String generateRenderCode(List<BaseRenderObject> renderObjects) throws Exception {
		String renderCode = "";
		for (BaseRenderObject ro : renderObjects){
			renderCode.concat(ro.generateRenderCode());
		}
		return renderCode;
	}
	
	public List<Class> getHandledEntityClasses() {
		return handledEntityClasses;
	}
	public void setHandledEntityClasses(List<Class> handledEntityClasses) {
		this.handledEntityClasses = handledEntityClasses;
	}
	public void addHandledEntityClass(Class clazz){
		this.handledEntityClasses.add(clazz);
	}
	public Boolean handlesEntity(Object entity){
		if (entity==null){return false;}
		for (Class handledEntityClass : handledEntityClasses){
			if (handledEntityClass.isAssignableFrom(entity.getClass())){
				return true;
			}
		}
		return false;
	}
	
	public List<Class> getHandledRenderObjectClasses() {
		return handledRenderObjectClasses;
	}
	public Class getDefaultRenderObjectClass(){
		return this.getHandledRenderObjectClasses().iterator().next();
	}
	public void setHandledRenderObjectClasses(List<Class> handledRenderObjectClasses) {
		this.handledRenderObjectClasses = handledRenderObjectClasses;
	}
	public void addHandledRenderObjectClass(Class clazz){
		if (BaseRenderObject.class.isAssignableFrom(clazz)){
			this.handledRenderObjectClasses.add(clazz);
		}
	}
	public Boolean handlesRenderObjectClass(String className){
		if (className==null){ return true; }
		
		try {
			return this.handledRenderObjectClasses.contains(Class.forName(className));
		} catch(Exception e) {
			return false;
		}
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	
	private Object getAttr(String attrName) throws Exception{
		if (!this.attributes.containsKey(attrName)){
			throw new MissingRendererAttributeException("Attribute " + attrName + " not found");
		}
		return this.attributes.get(attrName);
	}
	
	protected Object attr(String attrName) throws Exception {
		Object o = getAttr(attrName);
		if (!attrValid(attrName, o)){
			throw new InvalidRendererAttributeException("Value of " + o.toString() + " for attribute " + attrName + " is not valid");
		}
		return o;
	}
	
	protected String strAttr(String attrName) throws Exception {
		String s = getAttr(attrName).toString();
		if (s.contentEquals("null")){return null;}
		if (!attrValid(attrName, s)){
			throw new InvalidRendererAttributeException("Value of " + s + " for attribute " + attrName + " is not valid");
		}
		return s;
	}
	
	protected Integer intAttr(String attrName) throws Exception {
		String s = getAttr(attrName).toString();
		if (s.contentEquals("") || s.contentEquals("null")){return null;}
		Integer i;
		try {
			i = Integer.valueOf(s);
		} catch (Exception e){
			throw new RendererAttributeClassException("Error converting attribute " + attrName + " to type Integer");
		}
		if (!attrValid(attrName, i)){
			throw new InvalidRendererAttributeException("Value of " + s + " for attribute " + attrName + " is not valid");
		}
		return i;
	}
	
	protected Double dblAttr(String attrName) throws Exception {
		String s = getAttr(attrName).toString();
		if (s.contentEquals("") || s.contentEquals("null")){return null;}
		Double d;
		try {
			d = Double.valueOf(s);
		} catch (Exception e){
			throw new RendererAttributeClassException("Error converting attribute " + attrName + " to type Double");
		}
		if (!attrValid(attrName, d)){
			throw new InvalidRendererAttributeException("Value of " + s + " for attribute " + attrName + " is not valid");
		}
		return d;
	}
	
	protected Float fltAttr(String attrName) throws Exception {
		String s = getAttr(attrName).toString();
		if (s.contentEquals("") || s.contentEquals("null")){return null;}
		Float f;
		try {
			f = Float.valueOf(s);
		} catch (Exception e){
			throw new RendererAttributeClassException("Error converting attribute " + attrName + " to type Float");
		}
		if (!attrValid(attrName, f)){
			throw new InvalidRendererAttributeException("Value of " + s + " for attribute " + attrName + " is not valid");
		}
		return f;
	}
	
	protected Boolean boolAttr(String attrName) throws Exception {
		String s = getAttr(attrName).toString();
		if (s.contentEquals("") || s.contentEquals("null")){return null;}
		Boolean b;
		try {
			b = Boolean.valueOf(s);
		} catch (Exception e){
			throw new RendererAttributeClassException("Error converting attribute " + attrName + " to type Boolean");
		}
		if (!attrValid(attrName, b)){
			throw new InvalidRendererAttributeException("Value of " + s + " for attribute " + attrName + " is not valid");
		}
		return b;
	}
	
	// override to provide attribute level validation
	protected Boolean attrValid(String attrName, Object attrValue){
		return true;
	}
	
}
