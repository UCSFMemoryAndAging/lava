package edu.ucsf.lava.core.metadata.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import edu.ucsf.lava.core.manager.CoreManagerUtils;
public class ReloadMetadataController extends AbstractController {


	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		CoreManagerUtils.getMetadataManager().reloadViewProperties();
		request.setAttribute("infoMessage", "Metadata reload complete");
		return new ModelAndView("/info");
	}


	

}
