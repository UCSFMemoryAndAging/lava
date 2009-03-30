package edu.ucsf.lava.core.audit;

import java.util.Date;

import edu.ucsf.lava.core.audit.model.AuditEvent;

public class AuditThreadData {

	
	private static ThreadLocal auditEventThreadLocal = new ThreadLocal() {
		//called once per thread upon initial get..
		protected synchronized Object initialValue() {
		AuditEvent auditEvent = new AuditEvent();
		auditEvent.setAuditTime(new Date());
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
