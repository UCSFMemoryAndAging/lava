package edu.ucsf.lava.core.audit.model;

import java.util.HashSet;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;


public class AuditEntity extends EntityBase{

	public static EntityManager MANAGER = new EntityBase.Manager(AuditEntity.class);
	
	public static final String AUDIT_TYPE_SELECT = "SELECT";
	public static final String AUDIT_TYPE_INSERT = "INSERT";
	public static final String AUDIT_TYPE_UPDATE = "UPDATE";
	public static final String AUDIT_TYPE_DELETE = "DELETE";
	
	
	private edu.ucsf.lava.core.audit.model.AuditEvent auditEvent;
	private Long entityId;
	private String entity;
	private String entityType;
	private String auditType;

	private Set<AuditProperty> auditProperties = new HashSet();
	
	
	public AuditEntity() {
		super();
		this.setAudited(false);
	}
	public AuditEntity(AuditEvent auditEvent, Long entityId, String entity, String entityType, String auditType) {
		this();
		this.auditEvent = auditEvent;
		this.entityId = entityId;
		this.entity = entity;
		this.entityType = entityType;
		this.auditType = auditType;
		this.auditEvent.getAuditEntities().add(this);
		
	}
	public Set<AuditProperty> getAuditProperties() {
		return auditProperties;
	}
	public void setAuditProperties(Set<AuditProperty> auditProperties) {
		this.auditProperties = auditProperties;
	}
	public edu.ucsf.lava.core.audit.model.AuditEvent getAuditEvent() {
		return auditEvent;
	}
	public void setAuditEvent(edu.ucsf.lava.core.audit.model.AuditEvent auditEvent) {
		this.auditEvent = auditEvent;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}


	
}
