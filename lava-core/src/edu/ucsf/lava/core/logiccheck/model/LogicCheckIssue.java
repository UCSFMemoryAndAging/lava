package edu.ucsf.lava.core.logiccheck.model;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.core.logiccheck.model.LogicCheck;

public class LogicCheckIssue extends EntityBase {
	public static EntityManager MANAGER = new EntityBase.Manager(LogicCheckIssue.class);

	protected LogicCheck definition;

	protected String checkDescDataAppend;   // used to append data details to the checkDesc
	
	protected Byte verified;				// whether user verified an alert
	protected AuthUser verified_modifier;	// to track who last modified the confirmation
	protected Date verified_modifieddate;	// date of confirmation last modified
	
	protected Byte invalidDef;				// an invalidDef issue is one where the code produced an exception and needs to be checked out; it is not necessarily a valid logic check issue

	public LogicCheckIssue() {
		super();
		this.verified = (byte)0;
	}
	
	public LogicCheckIssue(Byte verified) {
		super();
		this.verified = verified;
	}
	
	public String getCheckDescDataAppend() { return checkDescDataAppend; }
	public void setCheckDescDataAppend(String checkDescDataAppend) { this.checkDescDataAppend = checkDescDataAppend; }

	public Byte getVerified() {return verified;}
	
	public void setVerified(Byte verified) {
		this.verified = verified;
		this.trackDirty("verified",verified);
	}

	public AuthUser getVerified_modifier() {return verified_modifier;}
	public void setVerified_modifier(AuthUser verifiedModifier) {verified_modifier = verifiedModifier;}

	public Date getVerified_modifieddate() {return verified_modifieddate;}
	public void setVerified_modifieddate(Date verifiedModifieddate) {verified_modifieddate = verifiedModifieddate;}

	public Byte getInvalidDef() {return invalidDef;}
	public void setInvalidDef(Byte invalidDef) {this.invalidDef = invalidDef;}
	
	public LogicCheck getDefinition() {return definition;}
	public void setDefinition(LogicCheck definition) {this.definition = definition;}
	
	public String getCheckCode() {
		// check codes could be dependent on this specific entity, e.g. UDS instruments returning a different
		//  checkCode for different version/visitType combinations
		return definition.getCheckCode(getEntityID());
	}
	
	public boolean getIsalert() {
		return definition.isAlert();
	}
	
	public String getField1itemNum() {
		return definition.getField1itemNum();
	}
	
	// override this method to handle entities
	public Long getEntityID() {
		return null;
	}

	// override this method to handle entities
	public void setEntityID(EntityBase entity) {
	}

	public EntityBase getEntity() {
		return null;
	}
	
	public String getCheckDescOutput() {
		String checkDescOutput;
		// only use calculated values if definition's checkDesc is null 
		// (i.e. any checkDesc in the table will override any app calculation)
		if (definition.getCheckDesc() != null)
			checkDescOutput = definition.getCheckDesc();
		else
			checkDescOutput = definition.getCheckDescCalculated();
		
		// append any data found during logic check calculation
		if (this.getCheckDescDataAppend() != null)
			checkDescOutput += " " + this.getCheckDescDataAppend();
		
		return checkDescOutput;
	}
	
	// if verification changed (e.g. through checkbox click), then note the
	//  user and time of this modification, and resave
	public void flushVerificationChange(RequestContext context, SessionManager sessionManager) {
		if (this.isDirty("verified")) {
			HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
			AuthUser authUser = CoreSessionUtils.getCurrentUser(sessionManager, request);
			// whether user checked it or uncheck it, we note its change and who did that last modification
			this.setVerified_modifier(authUser);
			this.setVerified_modifieddate(new Date()); // current time
			save();	
		}
		
	}
	
}
