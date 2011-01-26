package edu.ucsf.lava.core.reporting.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JRExporter;

import org.springframework.web.servlet.view.jasperreports.JasperReportsCsvView;

public class LavaJasperReportsCsvView extends JasperReportsCsvView {
	
	// override so that the report design file (.jrxml) is compiled every time the report is filled. 
	// this is great for development so report modifications can be seen on the fly without redeploying. 
	// as far as production goes, the compilation hit is imperceptible to the user, so not a problem 
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		initApplicationContext(); // forces report compilation
		super.renderMergedOutputModel(model, request, response);
	}

	

	public void setExporterParameters(Map parameters) {
		// override the record delimiter because the default is the system delimiter for the OS 
		// that the server is running on, e.g. Linux is newline = \n = \u000a
		// this is not done in lava-reports.xml because it did not work when set there. Java interpreted
		// the backslashes literally instead of escape chars
		parameters.put("net.sf.jasperreports.engine.export.JRCsvExporterParameter.RECORD_DELIMITER", "\r\n");
		super.setExporterParameters(parameters);
	}

	// EMORY change:
	// must change default handling of double quotes within csv rows
	protected JRExporter createExporter() {
		return new LavaJRCsvExporter();
	}

}
