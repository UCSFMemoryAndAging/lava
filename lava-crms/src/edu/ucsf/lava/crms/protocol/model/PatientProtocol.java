package edu.ucsf.lava.crms.protocol.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * 
 * @author ctoohey
 *
 *
 */
public class PatientProtocol extends PatientProtocolBase {
	public static EntityManager MANAGER = new EntityBase.Manager(PatientProtocol.class);
	
	public PatientProtocol(){
		super();
		this.setAuditEntityType("PatientProtocol");
		this.enrolledDate = new Date();
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.getProtocol(),this.getPatient(),this.getTimepoints()};
	}

	private Date enrolledDate;
	
	/**
	 * Convenience method to return Protocol instead of ProtocolBase
	 */
//	public Protocol getProtocol() {
//		return (Protocol) super.getProtocol();
//	}

	public Date getEnrolledDate() {
		return enrolledDate;
	}

	public void setEnrolledDate(Date enrolledDate) {
		this.enrolledDate = enrolledDate;
	}

}
