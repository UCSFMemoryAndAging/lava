package edu.ucsf.lava.core.logiccheck.model;

import java.util.Date;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.core.model.EntityManagerBase;
import edu.ucsf.lava.core.model.EntityBase.Manager;

public class LogicCheckSummary extends EntityBase {
	public static LogicCheckSummary.Manager MANAGER = new LogicCheckSummary.Manager();
	
	private Date lcDate;
	private String lcBy;
	private String lcStatus;
	private String lcReason;
	private String lcNotes;
	
	public LogicCheckSummary() {
		super();
	}

	public Date getLcDate() {
		return lcDate;
	}

	public void setLcDate(Date lcDate) {
		this.lcDate = lcDate;
	}

	public String getLcBy() {
		return lcBy;
	}

	public void setLcBy(String lcBy) {
		this.lcBy = lcBy;
	}

	public String getLcStatus() {
		return lcStatus;
	}

	public void setLcStatus(String lcStatus) {
		this.lcStatus = lcStatus;
	}

	public String getLcReason() {
		return lcReason;
	}

	public void setLcReason(String lcReason) {
		this.lcReason = lcReason;
	}

	public String getLcNotes() {
		return lcNotes;
	}

	public void setLcNotes(String lcNotes) {
		this.lcNotes = lcNotes;
	}
	
	// override this method
	public Long getEntityID() {
		return null;
	}
	// override this method
	public void setEntityID(EntityBase entity) {
	}
	
static public class Manager extends EntityBase.Manager{
		
		public Manager(){
			super(LogicCheckSummary.class);
		}
		
		public Manager(Class entityClass){
			super(entityClass);
		}
		
		// override the manager to support logic checks in sub-classes
		public LogicCheckSummary get(EntityBase entity){
			return null;	
		}
	}
}
