package edu.ucsf.lava.core.reporting.controller;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;

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

}
