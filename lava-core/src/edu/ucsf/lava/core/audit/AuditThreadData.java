package edu.ucsf.lava.core.audit;

import java.sql.Timestamp;
import java.util.Date;

import edu.ucsf.lava.core.audit.model.AuditEvent;

public class AuditThreadData {

	
	private static ThreadLocal auditEventThreadLocal = new ThreadLocal() {
		//called once per thread upon initial get..
		protected synchronized Object initialValue() {
		AuditEvent auditEvent = new AuditEvent();
		auditEvent.setAuditTimestamp(new Timestamp(new Date().getTime()));
		return auditEvent;
		}
	};
	public static AuditEvent getAuditEvent(){
		return (AuditEvent)auditEventThreadLocal.get();
	}
	public static void setAuditEvent(AuditEvent auditEvent){
		auditEventThreadLocal.set(auditEvent);
	}
}
