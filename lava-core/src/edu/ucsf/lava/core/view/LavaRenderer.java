package edu.ucsf.lava.core.view;

import java.util.List;

import edu.ucsf.lava.core.view.model.BaseRenderObject;
import edu.ucsf.lava.core.view.model.RenderParams;

public interface LavaRenderer {
	
	public List<BaseRenderObject> createRenderObjects(Object entity, Class renderObjectClass, RenderParams renderParams) throws Exception;
	public List<BaseRenderObject> createRenderObjects(Object entity, RenderParams renderParams) throws Exception;
	public String generateRenderCode(List<BaseRenderObject> renderObjects) throws Exception;
}
