package edu.ucsf.lava.core.audit.model;

import java.sql.Timestamp;
import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;


public class AuditPropertyHistory extends EntityBase{

	public static EntityManager MANAGER = new EntityBase.Manager(AuditPropertyHistory.class);
	
	private edu.ucsf.lava.core.audit.model.AuditEntityHistory auditEntity;
	private String property;
	private String indexKey;
	private String subproperty;
	private String oldValue;
	private String newValue;
	private Timestamp auditTimestamp;
	private String oldText;
	private String newText;

	
	public AuditPropertyHistory() {
		super();
		this.setAudited(false);
	}

	
	public edu.ucsf.lava.core.audit.model.AuditEntityHistory getAuditEntity() {
		return auditEntity;
	}
	public void setAuditEntity(edu.ucsf.lava.core.audit.model.AuditEntityHistory auditEntity) {
		this.auditEntity = auditEntity;
	}
	public Timestamp getAuditTimestamp() {
		return auditTimestamp;
	}
	public void setAuditTimestamp(Timestamp auditTime) {
		this.auditTimestamp = auditTime;
	}
	
	
	public String getIndexKey() {
		return indexKey;
	}
	public void setIndexKey(String indexKey) {
		this.indexKey = indexKey;
	}
	public String getNewText() {
		return newText;
	}
	public void setNewText(String newText) {
		this.newText = newText;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getOldText() {
		return oldText;
	}
	public void setOldText(String oldText) {
		this.oldText = oldText;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getSubproperty() {
		return subproperty;
	}
	public void setSubproperty(String subproperty) {
		this.subproperty = subproperty;
	}

	
}
