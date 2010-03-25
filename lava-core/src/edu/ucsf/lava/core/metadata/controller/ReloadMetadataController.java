package edu.ucsf.lava.core.metadata.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
public class ReloadMetadataController extends AbstractController {

	public static final String METADATA_CACHE_KEY_PREFIX = "edu.ucsf.lava.metadataCacheKey";

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		
		CoreManagerUtils.getMetadataManager().reloadViewProperties();
		clearApplicationContextMetadataCache(request);
		request.setAttribute("infoMessage", "Metadata reload complete");
		return new ModelAndView("/info");
	}

	/**
	 * Iterate through the application scope attributes and remove any that 
	 * are associated with the jsp / jstl metadata tags caching mechanism
	 * 
	 * @param request
	 */
	protected void clearApplicationContextMetadataCache(HttpServletRequest request){
		ServletContext context = request.getSession().getServletContext();
		Enumeration appScopeAttributes = request.getSession().getServletContext().getAttributeNames();
		while(appScopeAttributes.hasMoreElements()){
			String name = (String)appScopeAttributes.nextElement();
			if(name.startsWith(METADATA_CACHE_KEY_PREFIX)){
				context.removeAttribute(name);
			}
			
		}
		
	}
	

}
