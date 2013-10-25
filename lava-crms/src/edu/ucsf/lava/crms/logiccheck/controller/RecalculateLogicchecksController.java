package edu.ucsf.lava.crms.logiccheck.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class RecalculateLogicchecksController extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		
		CrmsLogicCheckUtils.recalculateLogicchecks();
		arg0.setAttribute("infoMessage", "Logic Check active state has been applied.  All newly-activated logic checks have been applied to their pertitent entities.");
		return new ModelAndView("/info");
	}

}
