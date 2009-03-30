package edu.ucsf.lava.core.audit.model;

import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;


public class AuditProperty extends EntityBase{
	
	public static EntityManager MANAGER = new EntityBase.Manager(AuditProperty.class);
	
	public static final String AUDIT_PROPERTY_CREATED = "{CREATED}";
	public static final String AUDIT_PROPERTY_DELETED = "{DELETED}";
	public static final String AUDIT_PROPERTY_NULL = "{NULL}";
	public static final String GENERIC_VALUE_DESC = "{VALUE}";
	
	
	private AuditEntity auditEntity;
	private String property;
	private String indexKey;
	private String subproperty;
	private String oldValue;
	private String newValue;
	private Date auditTime;
	private String oldText;
	private String newText;

	
	
	
	public AuditProperty() {
		super();
		this.setAudited(false);
	}

	public AuditProperty(AuditEntity auditEntity, String property, String oldValue, String newValue) {
		this(auditEntity, property,null,null,oldValue,newValue);
	}
	
	public AuditProperty(AuditEntity auditEntity, String property, String indexKey, String subproperty, String oldValue, String newValue) {
		this();
		this.auditEntity = auditEntity;
		this.auditTime = (this.auditEntity != null) ? auditEntity.getAuditEvent().getAuditTime() : null;
		this.property = property;
		this.indexKey = indexKey;
		this.subproperty = subproperty;
		this.oldValue = oldValue;
		this.newValue = newValue;
		
		//trim long strings to 150 characters and put full text in text columns
		if(this.oldValue != null && this.oldValue.length() > 255){
			this.oldText = this.oldValue;
			this.oldValue = this.oldValue.substring(0, 150).concat(" [...]");
		}
		if(this.newValue != null && this.newValue.length() > 255){
			this.newText = this.newValue;
			this.newValue = this.newValue.substring(0, 150).concat(" [...]");
		}
		
		this.auditEntity.getAuditProperties().add(this);
	
	}
	public AuditEntity getAuditEntity() {
		return auditEntity;
	}
	public void setAuditEntity(AuditEntity auditEntity) {
		this.auditEntity = auditEntity;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
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
