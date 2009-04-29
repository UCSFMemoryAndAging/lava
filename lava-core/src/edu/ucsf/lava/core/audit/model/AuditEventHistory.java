package edu.ucsf.lava.core.audit.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;


public class AuditEventHistory extends EntityBase{

	public static EntityManager MANAGER = new EntityBase.Manager(AuditEventHistory.class);

	private String auditUser;
	private String auditHost;
	private Timestamp auditTimestamp;
	private String action;
	private String actionEvent;
	private String actionIdParam;
	private String eventNote;
	private String exception;
	private String exceptionMessage;
	private Set<AuditEntity> auditEntities = new HashSet();
	
	public AuditEventHistory(){
		super();
		this.setAudited(false);
	}
	public Set<AuditEntity> getAuditEntities() {
		return auditEntities;
	}
	public void setAuditEntities(Set<AuditEntity> auditEntities) {
		this.auditEntities = auditEntities;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getActionEvent() {
		return actionEvent;
	}
	public void setActionEvent(String actionEvent) {
		this.actionEvent = actionEvent;
	}
	public String getActionIdParam() {
		return actionIdParam;
	}
	public void setActionIdParam(String actionIdParam) {
		this.actionIdParam = actionIdParam;
	}
	public String getAuditHost() {
		return auditHost;
	}
	public void setAuditHost(String auditHost) {
		this.auditHost = auditHost;
	}
	public Timestamp getAuditTimestamp() {
		return auditTimestamp;
	}
	public void setAuditTimestamp(Timestamp auditTime) {
		this.auditTimestamp = auditTime;
	}
	public String getAuditUser() {
		return auditUser;
	}
	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
	public String getEventNote() {
		return eventNote;
	}
	public void setEventNote(String eventNote) {
		this.eventNote = eventNote;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}


	
}
