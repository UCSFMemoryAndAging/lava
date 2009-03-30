package edu.ucsf.lava.core.audit.model;

import java.util.HashSet;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;


public class AuditEntityHistory extends EntityBase{

	public static EntityManager MANAGER = new EntityBase.Manager(AuditEntityHistory.class);

	
	private edu.ucsf.lava.core.audit.model.AuditEventHistory auditEvent;
	private Long entityId;
	private String entity;
	private String entityType;
	private String auditType;

	private Set<AuditProperty> auditProperties = new HashSet();

	public AuditEntityHistory(){
		super();
		this.setAudited(false);
	}
	
	public Set<AuditProperty> getAuditProperties() {
		return auditProperties;
	}
	public void setAuditProperties(Set<AuditProperty> auditProperties) {
		this.auditProperties = auditProperties;
	}
	public edu.ucsf.lava.core.audit.model.AuditEventHistory getAuditEvent() {
		return auditEvent;
	}
	public void setAuditEvent(edu.ucsf.lava.core.audit.model.AuditEventHistory auditEvent) {
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
