package edu.ucsf.lava.core.auth.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.webflow.execution.Event;

import edu.ucsf.lava.core.auth.PasswordDelegate;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;

public class AuthUserPasswordDto implements Serializable{


	protected AuthUser user;
	protected String oldPassword;
	protected String newPassword;
	protected String newPasswordConfirm;
	protected boolean supportsChange = false;
	protected boolean supportsReset = false;
	protected String changeMessageCode = "passwordDelegate.changePassword.notSupportedError";
	protected String resetMessageCode = "passwordDelegate.resetPassword.notSupportedError";
	
	public AuthUserPasswordDto() {
		super();
	}
	
	public AuthUserPasswordDto(AuthUser user) {
		this();
		this.user = user;
	}
	
	public AuthUserPasswordDto(AuthUser user, PasswordDelegate delegate) {
		this(user);
		configureSupportInfo(delegate);
	}
	
	public void configureSupportInfo(PasswordDelegate delegate){
		if(delegate!=null){
			this.supportsChange = delegate.getSupportsPasswordChange();
			this.supportsReset = delegate.getSupportsPasswordReset();
			this.changeMessageCode = delegate.getPasswordChangeMessageCode();
			this.resetMessageCode = delegate.getPasswordResetMessageCode();
		}
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getNewPasswordConfirm() {
		return newPasswordConfirm;
	}
	public void setNewPasswordConfirm(String newPasswordConfirm) {
		this.newPasswordConfirm = newPasswordConfirm;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	public void clear(){
		this.oldPassword = this.newPassword = this.newPasswordConfirm = null;
	}
	public AuthUser getUser() {
		return user;
	}
	public void setUser(AuthUser user) {
		this.user = user;
	}

	public String getChangeMessageCode() {
		return changeMessageCode;
	}

	public void setChangeMessageCode(String changeMessageCode) {
		this.changeMessageCode = changeMessageCode;
	}

	public String getResetMessageCode() {
		return resetMessageCode;
	}

	public void setResetMessageCode(String resetMessageCode) {
		this.resetMessageCode = resetMessageCode;
	}

	public boolean isSupportsChange() {
		return supportsChange;
	}

	public void setSupportsChange(boolean supportsChange) {
		this.supportsChange = supportsChange;
	}

	public boolean isSupportsReset() {
		return supportsReset;
	}

	public void setSupportsReset(boolean supportsReset) {
		this.supportsReset = supportsReset;
	}
	
	
	
}
