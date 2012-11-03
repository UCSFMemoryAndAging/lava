package edu.ucsf.lava.core.audit;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import edu.ucsf.lava.core.audit.model.AuditEntity;
import edu.ucsf.lava.core.audit.model.AuditEvent;
import edu.ucsf.lava.core.audit.model.AuditProperty;
import edu.ucsf.lava.core.dto.PagedListItemDto;
import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.core.model.EntityBase;

public class AuditManager extends LavaManager{
	
	public static String AUDIT_MANAGER_NAME="auditManager";
	//Thread local storage approach to the "current event"
	protected static ThreadLocal currentAuditEvent = new ThreadLocal();
	
	
	public AuditManager(){
		super(AUDIT_MANAGER_NAME);
	}
	
	protected static AuditEvent getCurrentAuditEvent(){
		return (AuditEvent)currentAuditEvent.get();
	}
	protected static void setCurrentAuditEvent(AuditEvent auditEvent){
		currentAuditEvent.set(auditEvent);
	}
	
	protected static void clearCurrentAuditEvent(){
		currentAuditEvent.set(null);
	}
	
	
		
	public void finalizeAuditing(){
		if (isCurrentEventAudited()){
			saveAuditing(true);
			clearCurrentAuditEvent();
		}
	}
	//clear the audit event when there is an unhandled exception...
	//perhpas we should us a new database session to store the exception information....
	static public void unhandledException(Exception e){
		
			clearCurrentAuditEvent();
	}

	//record the excpetion on the audit event when there is a handled exception...
	static public void handledException(Exception e){
		AuditEvent ae = getCurrentAuditEvent();
		if(ae!=null){
			ae.setException(ae.getClass().getName().substring(0,255));
			ae.setException(ae.toString().substring(0,255));
		}
	}

	
	public void saveAuditing(){
		saveAuditing(false);
	}

	public void saveAuditing(Boolean release){
		AuditEvent event = getCurrentAuditEvent();
		if(event != null){
			event.save();
			if(release){
				event.release();
			}
		}
	
	}
	
	public boolean isCurrentEventAudited(){
		AuditEvent event = getCurrentAuditEvent();
		return (event==null) ? false : true;
	}
	
	public void initializeAuditing(String actionId, String flowEvent, String idParam, HttpServletRequest request){
		this.initializeAuditing(actionId, flowEvent, idParam, request.getRemoteUser(), request.getRemoteHost());

	}
	
	public void initializeAuditing(String actionId, String flowEvent, String idParam, String user, String host){
		AuditEvent auditEvent = new AuditEvent();
		auditEvent.setAuditTimestamp(new Timestamp(new Date().getTime()));
		auditEvent.setAction((actionId.length()>255)? actionId.substring(0,255):actionId);
		auditEvent.setActionEvent((flowEvent.length()>50)? flowEvent.substring(0,50):flowEvent);
		auditEvent.setActionIdParam(idParam);
		auditEvent.setAuditHost(host);
		auditEvent.setAuditUser(user); 
		setCurrentAuditEvent(auditEvent);
	}
	
	public AuditEntity auditEntity(EntityBase entity, String auditType){
		if(!isCurrentEventAudited() || !getCurrentAuditEvent().doAudit(auditType)){return null;}
		return new AuditEntity(getCurrentAuditEvent(), entity.getId(), entity.getAuditEntityName(), 
				entity.getAuditEntityType(), auditType);
	}
	
	// We may wish to track an entity accessed through a list DTO, e.g. tracking individual patients
	// whose ePHI was shown in a FindPatient list.  Note that this will happen only during read accesses
	// (SELECT audit type) since DTOs aren't persistent (i.e. no UPDATE/DELETE/INSERT).
	public AuditEntity auditEntityFromListItemDto(PagedListItemDto dto, String auditType){
		if(!isCurrentEventAudited() || !getCurrentAuditEvent().doAudit(auditType)){return null;}
		return new AuditEntity(getCurrentAuditEvent(), dto.getId(), dto.getAuditParentEntityName(), 
				dto.getAuditParentEntityType(), auditType);
	}
	
	public boolean objectIsAudited(Object entity){
		if(EntityBase.class.isAssignableFrom(entity.getClass()) && ((EntityBase)entity).isAudited()){
			return true;
		}
		return false;
	}
	

	protected boolean isInternalProperty(String property){
		if(	property.equals("modified")||
			property.equals("class") ||
			property.endsWith(".class")){
			return true;
			}
		return false;

	}
	protected boolean isIgnoreProperty(EntityBase entity, String property){
		if(property==null){return false;}
		
		List<String> ignore = entity.getAuditIgnoreProperties();
		for(String pattern : ignore){
			if(property.matches(pattern)){
				return true;
			}
		}
		return false;
			
	}
	protected boolean areValuesBasicTypesOrNull(Object oldValue, Object newValue){
		
		
		if(oldValue != null &&
			!oldValue.equals(AuditProperty.AUDIT_PROPERTY_CREATED) &&
			!oldValue.equals(AuditProperty.AUDIT_PROPERTY_DELETED) &&
			!isBasicType(oldValue)){
			return false;
		}
		
		if(	newValue != null &&
			!newValue.equals(AuditProperty.AUDIT_PROPERTY_CREATED) &&
			!newValue.equals(AuditProperty.AUDIT_PROPERTY_DELETED) &&
			!isBasicType(newValue)){
			return false;
		}
		return true;
	}
	
	
	/**
	 * Is value a "basic" type, that can be audited simply by calling toString().  
	 * or a LavaEntity that the prepareAuditValue knows how to get as getId().toString().
	 * 
	 * @param value
	 * @return
	 */
	protected boolean isBasicType(Object value){
		if(value==null){return false;}
		if(value instanceof String ||
				Number.class.isAssignableFrom(value.getClass()) ||
				Date.class.isAssignableFrom(value.getClass()) ||
				isLavaEntityType(value) ||
				value instanceof Boolean ||
				value instanceof Character ||
				value instanceof StringBuffer ||
				value instanceof Currency)
		{
			return true;
		}
		return false;
	}
	
	protected boolean isCollection(Object value){
		if(value == null){return false;}
		if(Map.class.isAssignableFrom(value.getClass()) 
				|| Collection.class.isAssignableFrom(value.getClass())
				|| value.getClass().isArray()){
			return true;
		}
		return false;
	}
	
	
	protected boolean isLavaEntityType(Object value){
		if(value==null){return false;}
		if(EntityBase.class.isAssignableFrom(value.getClass())){
				return true;
		}
		return false;
	}
	
	
	protected String prepareAuditValue(Object value){
		if(value == null){return AuditProperty.AUDIT_PROPERTY_NULL;}
		//all dates need to be converted to timestamp format
		if(Date.class.isAssignableFrom(value.getClass()) && !(value instanceof Timestamp)){
			return new Timestamp(((Date)value).getTime()).toString();
		}
		if(isLavaEntityType(value)){
			Object id = ((EntityBase)value).getId();
			return (id==null)? AuditProperty.AUDIT_PROPERTY_NULL : id.toString();
		}
		return value.toString();
	}
	

	
		
	/**
	 * Utility function to convert collection objects to arrays of objects
	 */
	
	protected Object[] getValuesFromCollection(Object collection){
		if(collection==null)return null;
		
		if(Map.class.isAssignableFrom(collection.getClass())){
			Map map = (Map)collection;
			return map.values().toArray();
		}else if (Collection.class.isAssignableFrom(collection.getClass())){
			return ((Collection)collection).toArray();
		}else if (collection.getClass().isArray()){
			return (Object[])collection;
		}
		return null;
	}
	
	/**
	 * Utility function to extract "names" (i.e. map keys or list indexes) from a collection into a String array
	 * (todo: find a better approach to this...I just threw it together -- jhesse)
	 * This will only work with "ordered" collections  
	 */
	protected String[] getNamesFromCollection(Object collection){
		String[] names = null;
		if(collection == null){return names;}
		if(Map.class.isAssignableFrom(collection.getClass())){
			Map map = (Map)collection;
			Object[] keys = map.keySet().toArray();
			names = new String[keys.length];
			for(int i = 0;i<keys.length;i++){
				names[i]=keys[i].toString();
			}
		}else if (Collection.class.isAssignableFrom(collection.getClass())){
			int i = ((Collection)collection).size();
			names = new String[i];
			for(int j = 0;j<i;j++){
				names[j]=new Integer(j).toString();
			}
		}else if(collection.getClass().isArray()){
			Object[] array = (Object[])collection;
			names = new String[array.length];
			for(int i = 0;i<array.length;i++){
				names[i]=new Integer(i).toString();
			}
		}
		return names;
	}
	
	protected String generatePropertyPrefix(String prefix, String property){
		StringBuffer buffer = new StringBuffer();
		if(prefix != null) {
			buffer.append(prefix).append((property!=null)? ".":"");
		}
		if(property!=null){
			buffer.append(property);
		}
		return buffer.toString();
		
			
	}
	
	
	/**
	 * This is the main method called by the AuditManager to generate AuditProperty records.  This method's signature is designed to 
	 * match up to the object[] and string[] property collections available to the hibernate listeners.  This signature also allows this method
	 * to be called vis recursion when a property in the object arrays passed into the method themselves hold collection objects.  The utility methods
	 * getNamesFromCollection() and getValuesFromCollection flatten the collection objects into respective object and string arrays. (an important point 
	 * is that this depends on all collections being of the "ordered" type.  If we want to handle unordered collections then we need to get the names and
	 * values in a single function  
	 * 
	 * @param auditEntity  -- The parent object that the AuditProperty objects will be added to. 
	 * @param entity -- the actual entity being audited. 
	 * @param propertyPrefix -- if this method is called via recursion to handle collection properties, this value will be the 
	 * 							property "path" up to this point in the recursion.  The parameter is null upon the initial call
	 * @param initialValues -- an object array of the initial values for the entity.  May be null for newly created entities
	 * @param initialNames -- a string array of the initial property names (indexed the initialValues array).  May be null for newly created entities  
	 * @param currentValues -- an object array of the current values for the entity.  May be null for deleted entities
	 * @param currentNames a string array of the current property names (indexed to currentValues array).  May be null for deleted entities 
	 */
	protected void auditProperties(AuditEntity auditEntity, EntityBase entity, String propertyPrefix, Object[] initialValues, String[] initialNames, Object[] currentValues, String[] currentNames){
		//first loop through initial properties creating update or delete audit records as needed
		if(initialValues !=null){
			for (int initialIndex = 0; initialIndex < initialNames.length;initialIndex++){
				
				//determine whether there is a property name match between initial and current value arrays
				boolean foundInCurrent = true;
				int currentIndex = initialIndex;
				if(currentValues == null || currentValues.length < initialIndex+1){
					foundInCurrent = false;
				}else if(!initialNames[initialIndex].equals(currentNames[initialIndex])){
					foundInCurrent = false;
					for(currentIndex = 0;currentIndex < currentNames.length && foundInCurrent == false;currentIndex++){
						if(initialNames[initialIndex].equals(currentNames[currentIndex])){
							foundInCurrent = true;
						}
					}
				}
				
				
				//if the property value is a collection then properly format the collections into object arrays and recurse back into this function
				if(isCollection(initialValues[initialIndex]) || (foundInCurrent && isCollection(currentValues[currentIndex]))){
					//first check to make sure we aren't supposed to ignore this collection
					String property = generatePropertyPrefix(propertyPrefix,initialNames[initialIndex]);
					if(isInternalProperty(property) || isIgnoreProperty(entity,property)){return;}
						
					this.auditProperties(auditEntity, entity, property, 
							getValuesFromCollection(initialValues[initialIndex]),
							getNamesFromCollection(initialValues[initialIndex]),
							getValuesFromCollection((foundInCurrent)? currentValues[currentIndex]:null),
							getNamesFromCollection((foundInCurrent)? currentValues[currentIndex]:null));
				}else{
					//create the audit property -- do an update is there was a property name match, otherwise do a delete
					this.auditProperty(auditEntity, entity,(propertyPrefix!=null) ? propertyPrefix : initialNames[initialIndex],
							(propertyPrefix!=null) ? initialNames[initialIndex]:null,	initialValues[initialIndex],
									(foundInCurrent)? currentValues[currentIndex]:AuditProperty.AUDIT_PROPERTY_DELETED);
				}
			}//end loop through initial values
		}
		
		
		//now loop through current values array looking for properties not contained in initialValues array
		if(currentValues !=null){
			for (int currentIndex = 0; currentIndex < currentNames.length;currentIndex++){
				//determine whether the property is found in the initial values array
				boolean foundInInitial = true;
				int initialIndex = currentIndex;
				if(initialValues == null || initialValues.length < currentIndex+1 ){
					foundInInitial = false;
				}else if(!currentNames[currentIndex].equals(initialNames[currentIndex])){
					foundInInitial = false;
					for(initialIndex = 0;initialIndex < initialNames.length && foundInInitial == false;initialIndex++){
						if(currentNames[currentIndex].equals(initialNames[initialIndex])){
							foundInInitial = true;
						}
					}
				}
				

				//if the property was not found in the initial array then we know it need a "created" audit record.
				if(!foundInInitial){
					
					
					
					//if the property value is a collection then properly format the collections into object arrays and recurse back into this function
					if(isCollection(currentValues[currentIndex])){
						//first check to make sure we aren't supposed to ignore this collection
						String property = generatePropertyPrefix(propertyPrefix,currentNames[initialIndex]);
						if(isInternalProperty(property) || isIgnoreProperty(entity,property)){return;}
						this.auditProperties(auditEntity, entity, generatePropertyPrefix(propertyPrefix,currentNames[currentIndex]), 
								getValuesFromCollection(null),
								getNamesFromCollection(null),
								getValuesFromCollection(currentValues[currentIndex]),
								getNamesFromCollection(currentValues[currentIndex]));
					}else{
						this.auditProperty(auditEntity, entity,	(propertyPrefix!=null) ? propertyPrefix : currentNames[currentIndex],
							(propertyPrefix!=null) ? currentNames[currentIndex]:null, 
							AuditProperty.AUDIT_PROPERTY_CREATED,currentValues[currentIndex]);
					}
				}
			}//end loop through current values
				
		}
		
		return;
	}
	

	/**
	 * @param auditEntity
	 * @param entity
	 * @param property
	 * @param subproperty
	 * @param oldValue
	 * @param newValue
	 */
	protected void auditProperty(AuditEntity auditEntity, EntityBase entity,String property, String subproperty, Object oldValue, Object newValue){
		
		String fullProperty = property;
		if(subproperty!=null){fullProperty = new StringBuffer(property).append(".").append(subproperty).toString();}

//ctoohey: temporary fix for:
//org.hibernate.AssertionFailure: collection [...] was not processed by flush()
//if(isInternalProperty(fullProperty) || isIgnoreProperty(entity,fullProperty)){return;}
		
		//If these values are basic types (or null) then simply do the audit
		if(areValuesBasicTypesOrNull(oldValue,newValue)){
			//first check to make sure that the property should not be skipped
			if(isInternalProperty(fullProperty) || isIgnoreProperty(entity,fullProperty)){return;}
			
			//do the audit if values are not the same
			if(!prepareAuditValue(oldValue).equals(prepareAuditValue(newValue))){
				AuditProperty ap = new AuditProperty(auditEntity, property,null,subproperty,
					prepareAuditValue(oldValue),prepareAuditValue(newValue));
			}
		}
		//otherwise if not a basic type (checked above) then it is an object with properties that need to be iterated
		else {
			
			//	get property descripters for the object (using either new or old value
			PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(
								(oldValue==AuditProperty.AUDIT_PROPERTY_CREATED? newValue : oldValue));
			for (int i=0;i<descriptors.length;i++){
				String name = descriptors[i].getName();
				//first check to make sure that the property should not be skipped
				String objectProperty = new StringBuffer(fullProperty).append(".").append(name).toString();
				if(!isInternalProperty(objectProperty) && !isIgnoreProperty(entity,objectProperty)){
					try{	
						//get the values of the property from the objects
						Object newProperty = (newValue==AuditProperty.AUDIT_PROPERTY_DELETED) ? 
													newValue : PropertyUtils.getProperty(newValue,name);
						
						Object oldProperty = (oldValue==AuditProperty.AUDIT_PROPERTY_CREATED) ? 
								oldValue : PropertyUtils.getProperty(oldValue,name);
						
						//now check to see if this property is itself a collection that will need to be iterated
						if(isCollection(newProperty) || isCollection(oldProperty)){
								//call back out into higher level auditProperties that handles collections
								this.auditProperties(auditEntity, entity, objectProperty, 
										getValuesFromCollection(oldProperty),getNamesFromCollection(oldProperty),
										getValuesFromCollection(newProperty),getNamesFromCollection(newProperty));
						}else{
							//do the audit if the properties are not equal
							if(!prepareAuditValue(oldProperty).equals(prepareAuditValue(newProperty))){
								AuditProperty ap = new AuditProperty(auditEntity, property,subproperty,name,
									prepareAuditValue(oldProperty),prepareAuditValue(newProperty));
							}
						}
					}catch(Exception e){};
				}
			}
		}
	}
	
	
	
}
