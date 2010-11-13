package edu.ucsf.lava.core.view;

import java.util.List;

import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.view.model.BaseRenderer;

public class CoreViewUtils {
	
	public static BaseRenderer findRenderEngine(Object entity, String renderObjectClassName){
	List<BaseRenderer> renderEngines = CoreManagerUtils.getViewManager().getRenderEngines();
		for (BaseRenderer renderEngine : renderEngines){
			if (renderEngine.handlesEntity(entity) && renderEngine.handlesRenderObjectClass(renderObjectClassName)){
				return renderEngine;
			}
		}
		return null;
	}

}
