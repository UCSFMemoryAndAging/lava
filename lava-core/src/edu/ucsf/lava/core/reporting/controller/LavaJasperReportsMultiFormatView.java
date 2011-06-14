package edu.ucsf.lava.core.reporting.controller;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JasperPrint;

import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

public class LavaJasperReportsMultiFormatView  extends JasperReportsMultiFormatView {
	

	// The Spring class AbstractJasperReportsView goes about finding a data source like this:
	// 1) checks for a model object under the specified reportDataKey first
	// 2) if not found, looks for a value of type JRDataSource, java.util.Collection, object array 
	//    in the model (in that order)
	// 3) if not found, use the configured javax.sql.DataSource (jdbcDataSource property)
	
	// The problem with this is for reports which have queries embedded in the Jasper
	// report file and which use the jdbcDataSource. In this case, Spring will resolve the data source 
	// to be some Collection or Array in the model (and we have plenty of those in the model)
	// instead of the jdbcDataSource. The solution is to override the getReportTypes method
	// so that in step 2) above, Spring does not look for Collection or object array types, and
	// therefore uses the jdbcDataSource.
	// For non-report reports (i.e. for printing entity and list), the reportDataKey is always
	// configured (because the model object is the data source, as opposed to SQL/stored proc
	// embedded in the report design file), so the data source is resolved in step 1) above
	protected Class[] getReportDataTypes() {
		return new Class[] {JRDataSource.class, JRDataSourceProvider.class};
	}

	@Override
	public Properties getContentDispositionMappings() {
		// TODO Auto-generated method stub
		return super.getContentDispositionMappings();
	}

	// EMORY change: override to give reports a better filename
	@Override
	protected void renderReport(JasperPrint populatedReport, Map model, HttpServletResponse response) throws Exception {
        
        // replace content disposition header filename with the report names.
        Properties contentDispositions = this.getContentDispositionMappings();
        
        Enumeration enumContDispKeys = contentDispositions.keys();
        // iterate over all disposition mappings and replace the word _rep_name_ with the reportName
        while(enumContDispKeys.hasMoreElements()){
            Object contDispKey = enumContDispKeys.nextElement();
            // check whether string before cast.
            if(contDispKey instanceof String){
                // get the disposition string
                String dispositionStr = contentDispositions.getProperty((String)contDispKey);
                // set the new value in the properties
                contentDispositions.setProperty((String)contDispKey,dispositionStr.replace("_rep_name_",populatedReport.getName()));
            }
        }

		super.renderReport(populatedReport,model,response);
	}

	
}
