package edu.ucsf.lava.core.dao.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class StoredProcHibernateCallback implements HibernateCallback {

    String storedProcCall;
    Object[] paramValues;
    int[] paramTypes; // array of java.sql.Types constants
    char[] paramIOFlags;

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    /*
     * 
     * @param storedProcCall stored procedure call string
     * @param paramValues parameter values
     * @param paramTypes parameter types as java.sql.Types
     * @param paramIOFlags input param = 'i', output param = 'o'
     */
    public StoredProcHibernateCallback(String storedProcCall, Object[] paramValues, int[] paramTypes, char[] paramIOFlags) {
    	this.storedProcCall = storedProcCall;
    	this.paramValues = paramValues;
    	this.paramTypes = paramTypes;
    	this.paramIOFlags = paramIOFlags;
    }

    public Object doInHibernate(Session session) throws HibernateException, SQLException {

    	Connection con = session.connection();
    	CallableStatement proc = null;
    	try{
    		proc = con.prepareCall(this.storedProcCall);
    		
    		// iterate thru the parameters, setting values for input parameters, and registering output parameters 
			for (int i=0; i < this.paramValues.length; i++) {
				// proc parameters are set ordinally and are 1-based.
				if (this.paramIOFlags[i] == 'i') {
					proc.setObject(i+1, this.paramValues[i], this.paramTypes[i]);
				}
				else if (this.paramIOFlags[i] == 'o') {
					// register the parameter as an output parameter
		    		proc.registerOutParameter(i+1, this.paramTypes[i]); 
				}
			}

    		proc.execute();

    		// iterate thru the parameters to return the value of output parameters 
			for (int i=0; i < this.paramValues.length; i++) {
				if (this.paramIOFlags[i] == 'o') {
					// register the parameter as an output parameter
		    		proc.registerOutParameter(this.paramValues.length, this.paramTypes[this.paramTypes.length-1]); 
		    		this.paramValues[i] = proc.getObject(i+1);
				}
			}
    	}
	// HibernateCallback converts Hibernate and SQL checked exceptions to unchecked org.springframework.dao 
	//  DataAccessExceptions which propagate up to the service layer and view layer, so need not be handled here
	finally{
	    proc.close();
    	}
	return null;
    }
}
