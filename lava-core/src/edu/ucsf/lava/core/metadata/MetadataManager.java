package edu.ucsf.lava.core.metadata;

import java.util.Locale;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.core.manager.CoreManagerUtils;

public class MetadataManager extends LavaManager{
	public static String METADATA_MANAGER_NAME = "metadataManager"; 
	protected ViewPropertyMessageSource messageSource;

	public MetadataManager() {
		super(METADATA_MANAGER_NAME);
	}
		
	
	//get message source and reload
	public void reloadViewProperties(){
		messageSource.reload(CoreManagerUtils.getListManager().initializeMetadataListRequestCache());
	}

	public void setMessageSource(ViewPropertyMessageSource messageSource) {
		this.messageSource = messageSource;
	}


	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		return messageSource.getMessage(resolvable, locale);
	}


	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		return messageSource.getMessage(code, args, locale);
	}


	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		return messageSource.getMessage(code, args, defaultMessage, locale);
	}

	
	
	
}
