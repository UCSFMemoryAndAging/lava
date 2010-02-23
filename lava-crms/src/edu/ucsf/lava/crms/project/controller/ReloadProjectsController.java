package edu.ucsf.lava.crms.project.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import edu.ucsf.lava.crms.manager.CrmsManagerUtils;


public class ReloadProjectsController extends AbstractController {

	
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		CrmsManagerUtils.getProjectManager().reloadProjects();
		arg0.setAttribute("infoMessage", "Projects reload complete");
		return new ModelAndView("/info");
	}







}
