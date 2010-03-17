package edu.ucsf.lava.core.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class LavaResponseHeaderFilter implements Filter {
	
	protected FilterConfig config;
	public void destroy() {
		

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResp = (HttpServletResponse)response;

		Enumeration e = config.getInitParameterNames();

		while (e.hasMoreElements()) {
		String headerName = (String)e.nextElement();
		String headerValue = config.getInitParameter(headerName);
		httpResp.addHeader(headerName, headerValue);
		}
		chain.doFilter(request, response);
	}
	
	public void init(FilterConfig config) throws ServletException {
		this.config = config;

	}

}
