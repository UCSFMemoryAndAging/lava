package edu.ucsf.lava.core.dao;

import java.io.Serializable;

/**
 * Simple encapsulating interface for projections (select clause elements)
 * used in criteria queries in Hibernate.   The encapsulation is pretty much
 * a one-to-one mapping over the hibernate 3 Projections class.  This is patterned
 * after our encapsulation of the Restrictions class by the LavaDaoParam interface.
 * 
 *  While it is unlikely that we would want to plug in another persistence layer
 *  at this point, it is a simple encasulation to code and may help us somehow 
 *  down the line. 
 * @author jhesse
 *
 */
public interface LavaDaoProjection extends Serializable{

	//projections are not supported in this manner by named queries or HQL queries
	public static final String TYPE_CRITERION = "criterionProjection";

	public String getType();
	 
	
}
