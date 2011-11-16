package edu.ucsf.lava.core.metadata.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.metadata.ViewPropertyAttributeParser;
import edu.ucsf.lava.core.model.EntityBase;



public class ViewProperty extends EntityBase {
	public static ViewProperty.Manager MANAGER = new ViewProperty.Manager();
	
	public static String LIST_SORT_PARAM_NAME="sort";
	public static String LIST_FORMAT_PARAM_NAME="format";
	public static String LIST_CODES_PARAM_NAME="codes";
	public static final String ANY_PLACEHOLDER="*";
	public static final String DELIMITER=".";

	private String messageCode;
	private String locale;
	private String instance;
	private String scope;
	private String prefix;
	private String entity;
	private String property;
	private String section;
	private String context;
	private String style;
	private String label;
	private String label2;	
	private String required;
	private Short maxLength;
	private Short size;
	private Short indentLevel;
	private String attributes;
	private String list;
	private String listAttributes;
	private Integer propOrder;
	private String quickHelp;
	
	public ViewProperty(){
		super();
		this.setAudited(false);
	}
	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel2() {
		return label2;
	}

	public void setLabel2(String label2) {
		this.label2 = label2;
	}	
	
	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getQuickHelp() {
		return quickHelp;
	}

	public void setQuickHelp(String quickHelp) {
		this.quickHelp = quickHelp;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	

	public Short getIndentLevel() {
		return indentLevel;
	}
	public void setIndentLevel(Short indentLevel) {
		this.indentLevel = indentLevel;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public Integer getPropOrder() {
		return propOrder;
	}

	public void setPropOrder(Integer propOrder) {
		this.propOrder = propOrder;
	}

	public Short getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Short maxLength) {
		this.maxLength = maxLength;
	}

      

	public Short getSize() {
		return size;
	}

	public void setSize(Short size) {
		this.size = size;
	}
	
	
	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		updateMessageCode();
	}
	
	public void updateMessageCode(){
		StringBuffer buffer = new StringBuffer();
		String prefix = getPrefix();
		if(prefix==null){
			buffer.append(ANY_PLACEHOLDER);
		}else{
			buffer.append(prefix.substring(0,1).toLowerCase()).append(prefix.substring(1,prefix.length()));
		}
		String entity = getEntity();
		if(entity==null){
			buffer.append(ANY_PLACEHOLDER);
		}else{
			buffer.append(entity.substring(0,1).toLowerCase()).append(entity.substring(1,entity.length()));
		}
		String property = getProperty();
		if(property==null){
			buffer.append(ANY_PLACEHOLDER);
		}else{
			buffer.append(property.substring(0,1).toLowerCase()).append(property.substring(1,property.length()));
		}
		setMessageCode(buffer.toString());
	}
	
	
	public Map<String,String> getListConfigParams() {
		if(StringUtils.isEmpty(listAttributes)){ new HashMap<String,String>();}
		
		return ViewPropertyAttributeParser.parseAttributes(listAttributes);
	}
		
	



	

	public String getListAttributes() {
		return listAttributes;
	}

	public void setListAttributes(String listAttributes) {
		this.listAttributes = listAttributes;
	}
	
	
    
    static public class Manager extends EntityBase.Manager{
    
    	public Manager(){
			super(ViewProperty.class);
		}
	
		
    public List getPropertiesForEntity(String entity){
    		LavaDaoFilter filter = ViewProperty.newFilterInstance();
    		filter.addDaoParam(filter.daoEqualityParam("entity", entity));
    		return get(ViewProperty.class,filter);
    	}
    }
        
        
        
        
        
}
