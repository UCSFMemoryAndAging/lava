package edu.ucsf.lava.core.view;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.core.view.model.BaseRenderer;
import edu.ucsf.lava.core.view.model.BaseRenderObject;
import edu.ucsf.lava.core.view.model.CssBox;
import edu.ucsf.lava.core.view.model.RenderEngineNotFoundException;
import edu.ucsf.lava.core.view.model.RenderParams;

public class ViewManager extends LavaManager {

	public static String VIEW_MANAGER_NAME = "viewManager";
	
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
    
    protected List<BaseRenderer> renderEngines = new ArrayList<BaseRenderer>();
    
	public ViewManager() {
		super(VIEW_MANAGER_NAME);
	}
	
	public void addRenderEngine(BaseRenderer renderEngine){
		renderEngines.add(renderEngine);
	}
	
	public List<BaseRenderer> getRenderEngines(){
		return renderEngines;
	}
	
    public static String generateDefaultObjectRenderCode(Object entity, RenderParams renderparams) throws Exception{
    	return generateRenderCode(entity,null,renderparams);
    }
    
	public static String generateRenderCode(Object entity, String renderObjectClassName, RenderParams renderparams) throws Exception{
		if (entity==null){return "";}
		BaseRenderer renderEngine = CoreViewUtils.findRenderEngine(entity,renderObjectClassName);
		if (renderEngine == null){
			throw new RenderEngineNotFoundException("No render engine found for entity: " + entity.toString());
		}
		
		Class renderObjectClass;
		if (renderObjectClassName==null){
			renderObjectClass = renderEngine.getDefaultRenderObjectClass();
		} else {
			renderObjectClass = Class.forName(renderObjectClassName);
		}
		
		List<BaseRenderObject> renderObjects = renderEngine.createRenderObjects(entity,renderObjectClass,renderparams);
		StringBuffer renderCode = new StringBuffer("");
		for (BaseRenderObject obj : renderObjects){
			
			String objRenderCode = ((BaseRenderObject)obj).generateRenderCode();
			if (objRenderCode != null){
				renderCode.append(objRenderCode);
			}
		}
		
		return renderCode.toString();
	}
	
	// render paramter set methods for use by function calls within jsp
	public static void setRenderParams(RenderParams renderParams, String parameters) throws Exception {
		renderParams.setRenderParams(parameters);
	}
	
	public static void setRenderParam(RenderParams renderParams, String parameter, Object value) throws Exception {
		renderParams.setRenderParam(parameter, value);
	}
	
	public static void addRenderParamElement(RenderParams renderParams, String parameter, Object value) throws Exception {
		renderParams.addRenderParamElement(parameter, value);
	}
	
}
