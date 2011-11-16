package edu.ucsf.lava.core.metadata;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.environment.EnvironmentManager;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;
import edu.ucsf.lava.core.metadata.model.ViewProperty;
import static edu.ucsf.lava.core.metadata.model.ViewProperty.*;

/*
 * This class provides access to the view property metadata via a messagesource interface.
 * 
 * The view property metadata is loaded into a locale specific hashmap with a key that
 * is the concatonated messageCode and "metadata item".  Message code itself is a concatonation
 * of prefix.entity.property in the view properties table.  "*" represents a wildcard
 * in the viewProperties table.  
 * 
 * The order of message code resolution is:
 * 1) exact match to lang.prefix.entity.property.item.
 * 2) assume no prefix passed in, so match lang.*.entity.property.item
 * 3) assume no prefix or entity passed in so match lang.*.*.property.item
 * 4) assume that the first part of the code passed in is an unmatched prefix...so
 * 		drop it an then match against lang.*.entity.property.item
 * 5) [if we find we need others add them here]
 * 
 */
public class ViewPropertyMessageSource extends AbstractMessageSource implements ManagersAware{
	
	private HashMap messageMap;
	public static final Locale DEFAULT_LOCALE= new Locale("en");
	protected EnvironmentManager environmentManager;
	
	
	//a runtime cache to store the results of the complicated lookup logic for any particular message code and locale. 
	protected HashMap<String,String> resolutionCache = new HashMap<String,String>();
	
	/**
	 * utility function...returns LAVA_INSTANCE_IDENTIFIER is environment manager not yet set. 
	 * @return
	 */
	protected String getWebAppInstance(){
		if(null == environmentManager){return ActionUtils.LAVA_INSTANCE_IDENTIFIER;}
		return environmentManager.getInstanceName();
	}

	
	
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		HashMap map = getMessageMap();
		String langCode = locale.getLanguage();
		String codeKey = new StringBuffer(langCode).append(DELIMITER).append(code).toString();
		if(resolutionCache.containsKey(codeKey)){
			return resolutionCache.get(codeKey);
		}
		//try webapp instance first, then lava instance
		for (String instance: new String[]{getWebAppInstance(),ActionUtils.LAVA_INSTANCE_IDENTIFIER})
		{
			
			//first look for properties in the instance scope, then look in lava scope
			StringBuffer codeToMatch = new StringBuffer(langCode).append(DELIMITER)
				.append(instance).append(DELIMITER)
				.append(code);
			
			//	lookup exact match to lang.code
			if(map.containsKey(codeToMatch.toString())){
				String value = (String)map.get(codeToMatch.toString());
				resolutionCache.put(codeKey,value);
				return value;
			}
			//lookup match to any prefix  lang.*.code
			codeToMatch = new StringBuffer(langCode).append(DELIMITER)
					.append(instance).append(DELIMITER)
					.append(ANY_PLACEHOLDER).append(DELIMITER).append(code);
			if(map.containsKey(codeToMatch.toString())){
				String value = (String)map.get(codeToMatch.toString());
				resolutionCache.put(codeKey,value);
				return value;
			}		
		
			//lookup match to any prefix any entity lang.*.*.code
			codeToMatch = new StringBuffer(langCode).append(DELIMITER)
				.append(instance).append(DELIMITER)
				.append(ANY_PLACEHOLDER).append(DELIMITER)
				.append(ANY_PLACEHOLDER).append(DELIMITER).append(code);
		
			if(map.containsKey(codeToMatch.toString())){
				String value = (String)map.get(codeToMatch.toString());
				resolutionCache.put(codeKey,value);
				return value;
			}		
		
			//drop first part of code (assuming an unmatched prefix) and match to any prefix
			Matcher codeMatcher = Pattern.compile("(.*?\\.)(.*)").matcher(code);
			if(codeMatcher.matches()){
				codeToMatch = new StringBuffer(langCode).append(DELIMITER)
					.append(instance).append(DELIMITER)
					.append(ANY_PLACEHOLDER).append(DELIMITER).append(codeMatcher.group(2));
	
				if(map.containsKey(codeToMatch.toString())){
					String value = (String)map.get(codeToMatch.toString());
					resolutionCache.put(codeKey,value);
					return value;
				}		
			}
		}
		
		/*
		 * 	We haven't found it yet, so if the locale passed in has the same language as the default locale then 
		 * return null as we haven't found the code otherwise call this
		 * same funcation recursively with the default locale
		 */
		if(langCode.equalsIgnoreCase(DEFAULT_LOCALE.getLanguage())){
				resolutionCache.put(codeKey, null);
				return null;
		}else{
			return this.resolveCodeWithoutArguments(code, DEFAULT_LOCALE);
		}
	}




	//just a wrapper to put the message into a messageformat object
	protected MessageFormat resolveCode(String code, Locale locale) {
		String message = this.resolveCodeWithoutArguments(code, locale);
		if (message == null) {
			return null;
			}
		return new MessageFormat(message);
		
	
	}
	
	public void reload(Map<Long,String> viewPropIdToListRequestIdMap){
		
		//first reload the parent message source if it is reloadable by clearing the cache. 
		MessageSource parent = this.getParentMessageSource();
		if(ReloadableResourceBundleMessageSource.class.isAssignableFrom(parent.getClass())){
			((ReloadableResourceBundleMessageSource)parent).clearCache();
		}
		
		//now reload the viewProperty metadata
		List<ViewProperty> props = ViewProperty.MANAGER.get();
		HashMap propMap = new HashMap(props.size());
		StringBuffer key;
		for (ViewProperty prop : props){
			//label
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("label");
			propMap.put(key.toString(), prop.getLabel() != null ? prop.getLabel() : "");

			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
			.append(prop.getInstance()).append(DELIMITER)
			.append(prop.getMessageCode()).append(DELIMITER)
			.append("label2");
			propMap.put(key.toString(), prop.getLabel2() != null ? prop.getLabel2() : "");
					
			//style
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("style");
			propMap.put(key.toString(), prop.getStyle() != null ? prop.getStyle() : "");

			//context
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("context");
			propMap.put(key.toString(), prop.getContext() != null ? prop.getContext() : "");

			//required
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("required");
			propMap.put(key.toString(), prop.getRequired() != null ? prop.getRequired() : "");
			
			//list
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("listName");
			propMap.put(key.toString(), prop.getList() != null ? prop.getList() : "");

			//list attributes
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("listAttributes");
			propMap.put(key.toString(), prop.getListAttributes() != null ? prop.getListAttributes() : "");

			//list request id
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("listRequestId");
			propMap.put(key.toString(), viewPropIdToListRequestIdMap.containsKey(prop.getId()) ? 
					viewPropIdToListRequestIdMap.get(prop.getId()) : "");

			//order
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("order");
			propMap.put(key.toString(), prop.getPropOrder() != null ? prop.getPropOrder() : "");
			
			//attributes
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("widgetAttributes");
			propMap.put(key.toString(), prop.getAttributes() != null ? prop.getAttributes() : "");

			//section
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("section");
			propMap.put(key.toString(), prop.getSection() != null ? prop.getSection() : "");

			//quickHelp
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("quickHelp");
			propMap.put(key.toString(), prop.getQuickHelp() != null ? prop.getQuickHelp() : "");

			//maxTextLength
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("maxTextLength");
			propMap.put(key.toString(), prop.getMaxLength() != null ? prop.getMaxLength().toString() : "");

			//textBoxSize
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("textBoxSize");
			propMap.put(key.toString(), prop.getSize() != null ? prop.getSize().toString() : "");

			//	indentLevel
			key = new StringBuffer(prop.getLocale()).append(DELIMITER)
				.append(prop.getInstance()).append(DELIMITER)
				.append(prop.getMessageCode()).append(DELIMITER)
				.append("indentLevel");
			propMap.put(key.toString(), prop.getIndentLevel() != null ? prop.getIndentLevel().toString() : "");

			
			
		}
		this.setMessageMap(propMap);
		this.resolutionCache = new HashMap<String,String>();
	}



	public synchronized HashMap getMessageMap() {
		return messageMap;
	}




	public synchronized void setMessageMap(HashMap messageMap) {
		this.messageMap = messageMap;
	}




	public void updateManagers(Managers managers) {
		this.environmentManager = CoreManagerUtils.getEnvironmentManager(managers);
	}
	
	
	
	
}
