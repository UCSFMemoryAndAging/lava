package edu.ucsf.lava.core.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.support.PagedListSourceProvider;

/**
 * @author jhesse
 *	This supports using a cursor on the database to load pages of a list as needed
 *  On PageListSourceProvider::LoadList() return a list of lightweight ScrollableListProxyElements
 *  one for each element in your source list.  The loadElements method will be called as needed, 
 *  return the actual instantiated elements to the loadElements method. 
 *  
 */
public interface ScrollableListSourceProvider extends PagedListSourceProvider {
	
	
	//Query the source of the list and return the requested elements.
	public List loadElements(Locale locale, Object filter);
	
	
}
