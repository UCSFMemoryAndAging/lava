package edu.ucsf.lava.core.dto;

import java.io.Serializable;


/**
 * A simple interface to define an object usable by a 
 * ScrollablePagedListHolder.  Used as an interface for 
 * dto classes implemented to support custom queries that 
 * do not return persistent LavaEntity objects. 
 *   
 * @author jhesse
 *
 */
public interface PagedListItemDto extends Serializable{
	
	/**
	 * What exactly the "id" would be is deferred to the implementing class.  Should not be null. 
	 * @return
	 */
	public Long getId();

}
