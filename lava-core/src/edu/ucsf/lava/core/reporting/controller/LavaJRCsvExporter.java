package edu.ucsf.lava.core.reporting.controller;

import java.util.StringTokenizer;

import net.sf.jasperreports.engine.export.JRCsvExporter;

public class LavaJRCsvExporter extends JRCsvExporter {

	// EMORY change:
	/* 
	 *   Our csv rows are handled as one field (see nacc-reports.xml), leaving the
	 *   report handling the responsibility to add double quotes to individual fields 
	 *   in the csv row.  Thus we do not want the JRCsvExporter default handling when it
	 *   finds double quotes.
	 */
	@Override
	protected String prepareText(String source) {
		String str = null;
	
		if (source != null)
		{
			boolean putQuotes = false;
		
			if (source.indexOf(delimiter) >= 0)
			{
				putQuotes = true;
			}
		
			StringBuffer sbuffer = new StringBuffer();
//			StringTokenizer tkzer = new StringTokenizer(source, "\"\n", true);
			StringTokenizer tkzer = new StringTokenizer(source, "\n", true);
			String token = null;
			while(tkzer.hasMoreTokens())
			{
				token = tkzer.nextToken();
//				if ("\"".equals(token))
//				{
//					putQuotes = true;
//					sbuffer.append("\"\"");
//				}
//				else
				if ("\n".equals(token))
				{
					//sbuffer.append(" ");
					putQuotes = true;
					sbuffer.append("\n");
				}
				else
				{
					sbuffer.append(token);
				}
			}
			
			str = sbuffer.toString();
			
			if (putQuotes)
			{
				str = "\"" + str + "\"";
			}
		}
	
		return str;
	}
	
}
