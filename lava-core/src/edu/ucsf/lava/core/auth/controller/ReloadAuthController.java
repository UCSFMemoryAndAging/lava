package edu.ucsf.lava.core.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import edu.ucsf.lava.core.manager.CoreManagerUtils;
public class ReloadAuthController extends AbstractController {

	
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		// TODO Auto-generated method stub
		CoreManagerUtils.getAuthManager().reloadRoleCache();
		arg0.setAttribute("infoMessage", "Auth Role Cache reload complete");
		return new ModelAndView("/info");
	}



	

}
