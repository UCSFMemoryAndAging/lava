package edu.ucsf.lava.core.reporting.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RecompilingLavaJasperReportsMultiFormatView extends
		LavaJasperReportsMultiFormatView {

	// override so that the report design file (.jrxml) is compiled every time the report is filled. 
	// this is great for development so report modifications can be seen on the fly without redeploying. 
	// as far as production goes, the compilation hit is imperceptible to the user, so not a problem 
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		initApplicationContext(); // forces report compilation
		super.renderMergedOutputModel(model, request, response);
	}

}
