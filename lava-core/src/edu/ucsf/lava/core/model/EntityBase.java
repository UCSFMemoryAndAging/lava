package edu.ucsf.lava.core.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang.StringUtils;
import org.hibernate.type.Type;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.metadata.MetadataManager;

/**
 * @author jhesse
 *
 */
public abstract class EntityBase implements LavaEntity, Cloneable {
	
	//Business Manager / Data Access Object Proxy
	public static EntityManager MANAGER = new EntityBase.Manager(EntityBase.class);
	

	
	//Identity properties
	protected volatile Integer hashCode;
	protected static final Map<String, Integer> SAVED_HASHES =
		Collections.synchronizedMap(new WeakHashMap<String, Integer>());
	protected Long id;
	protected String entityName;
	protected Timestamp modified; //timestamp value that all entities must have (for persistence concurrency)
	//versioning properties
	protected boolean versioned = false;
	protected String entityVersion = "";
	
	//Auditing properties
	protected boolean audited = true; //whether auditing is enabled for the entity. 
	protected String auditEntityName;
	protected String auditEntityType;
	protected  Object[] auditStateValues;  //saved audit state should not be cloned. 
	protected  String[] auditStateNames;
	protected  Type[] auditStateTypes;
	protected ArrayList auditIgnoreProperties = new ArrayList();
	
	//dirty tracking property collection
	protected HashMap<String,Object> oldValues = new HashMap<String,Object>();
	protected HashMap<String,Object> dirtyValues = new HashMap<String,Object>();
	
	
	
	public EntityBase(){
		super();
	}
	
	
	
	//Entity Identity Methods
	
	/**
	 * Gets the id of the entity.  The id is always a Long and serves at the 
	 * primary identifier for the entity for object persistence. 
	 */
	public Long getId() {
		return this.id;
	}
	
	/**
	 * Sets the id of the entity.  Called by the persitence mechanism once an
	 * identifier has been assigned to the entity. 
	 * @param id
	 */
	public void setId(Long id) {
        // If we've just been persisted and hashCode has been
        // returned then make sure other entities with this
        // ID return the already returned hash code
        if ( (this.id == null) &&
                (id != null) &&
                (hashCode != null) ) {
            SAVED_HASHES.put( this.getClass().getName().concat(id.toString()), hashCode );
        }
        this.id = id;
	}

	
	/**
	 * Custom equals method that works with the hibernate persistence layer. 
	 */
	public boolean equals(Object object){
		if(object == null){return false;}
		if(!object.getClass().equals(this.getClass())){return false;}
		return(getId() != null && getId().equals(((LavaEntity)object).getId()));
	}
	
	/**
	 * custom hashcode method to support the hibernate persistence layer. 
	 */
	public int hashCode()
	    {
	        if ( hashCode == null ) {
	            synchronized ( this ) {
	                if ( hashCode == null ) {
	                    Integer newHashCode = null;

	                    if ( id != null ) {
	                        newHashCode = SAVED_HASHES.get( this.getClass().getName().concat(id.toString()));
	                    }
	                    if ( newHashCode == null ) {
	                        if ( id != null ) {
	                            newHashCode = id.intValue();
	                        } else {
	                            newHashCode = super.hashCode();
	                        }
	                    }
	                    hashCode = newHashCode;
	                }
	            }
	        }
	        return hashCode;
	    }
	
	/**
	 * Gets the name of the entity.  Defaults to the 
	 * simple name of the entity class. 
	 */
	public String getEntityName(){
		if(this.entityName==null){
			return this.getClass().getSimpleName();
		}
		return entityName;		
	}
	
	/**
	 * Sets the name of the entity.
	 * @param entityName
	 */
	protected void setEntityName(String entityName){
		this.entityName = entityName;
	}

   
	
	/**
	 * Returns the entity name in a consistent format suitable for use in contexts where spaces, mixed punctuation,
	 * or non alphanumeric characters would be probamatic (e.g. lookup keys, file names).  The method returns the
	 * entity name striped of non-alphanumeric characters (including spaces) and converted to lowercase.  Optionally,
	 * the encoded version is appended to the entity name. 
	 * 
	 */
	public String getEntityNameEncoded(boolean includeVersion) {
		String entityNameEncoded = getEntityName();
		if(entityNameEncoded==null){return new String();}
		
		entityNameEncoded = entityNameEncoded.replaceAll("[^a-zA-Z0-9]","").toLowerCase();
		    
		if (includeVersion && this.isVersioned()) {
				return new StringBuffer(entityNameEncoded).append(this.getEntityVersionEncoded()).toString();
		}
		return entityNameEncoded;
	}
	
	/**
	 * Convenience method (includes encoded entityVersion by default)
	 */
	public String getEntityNameEncoded(){
		return this.getEntityNameEncoded(true);
	}
	
	//Timestamp 


	public Timestamp getModified() {
		return modified;
	}



	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	
	//versioning methods
	
	/**
	 * returns whether the entity is a versioned entity.  Versioned entities are
	 * generally subclasses of a parent entity type that have been created to reflect
	 * changes to an entity over time that are not "projected" back in time over existing
	 * entity objects.  Supports a consisted representation of multiple versions of an 
	 * entity, deferring implementation of entity persistence, business logic, and presentation
	 * to "per-version" code heirarchies.   
	 */
	public boolean isVersioned() {
		if(versioned==false){
			versioned = StringUtils.isNotEmpty(getEntityVersion());
		}
		return this.versioned;
	}
	
	/**
	 * Returns the entity version for the entity. 
	 */
	public String getEntityVersion() {
		return this.entityVersion;
	}
	
	/**
	 * Sets the entity version for the entity
	 */
	public void setEntityVersion(String entityVersion){
		this.entityVersion = entityVersion;
	}
	
	/** 
	 * Returns the entity version "cleaned" to be appended to the entity name to
	 * compose the versioned entity name;  The clean version is the version property
	 * with all non-alphanumeric characters converted to underscores
	 * e.g. 1/2008 is converted to 1_2008  and 1.1.0 is converted to 1_1_0
	 */
	public String getEntityVersionEncoded() {
		if(getEntityVersion() != null && !getEntityVersion().equals(DATA_CODES_LOGICAL_SKIP.toString())){
			return getEntityVersion().replaceAll("[^a-zA-Z0-9]","_");
			
		}
		return new String();
	}
	
	

	
	
	
		
	
	//Initialization Methods 
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{};
	}
	//whether to initialize assocations objects when retrieved via a list method. 
	public boolean initializeAssocationsForObjectLists(String method) {
		return true;
	}


	//override in subclass to do postcreation actions.  Called by LavaDao when a new instance is created.
	public Object postCreate() {
		return this;
	}

	// validate is called prior to saving an entity. if there is validation that can be done within
	// the entity itself, override this and throw a RuntimeException if validation fails.
	public void validate(MetadataManager metadataManager) {
		
	}
	
	
	/**
	 * Override this method (calling super.onClone() and then doing any custom 
	 * work needed for a correct clone of the subclass
	 */
	public Object onClone(Object clone){
		
		return clone;
	}
	

	/**
	 * Override this method (calling super.onDeepClone() and then doing any custom 
	 * work needed for a correct clone of the subclass
	 */
	public Object onDeepClone(Object clone){
		if(EntityBase.class.isAssignableFrom(clone.getClass())){
			EntityBase entity = (EntityBase)clone;
			entity.setAuditState(null, null, null);
		}
		return clone;
	}
	/**
	 * 	override of object.clone() does a shallow clone by default as is the java standard
	 */
	public Object clone() {
		try {
			return onClone(super.clone());
		}catch(Exception ex){
			throw new InternalError(ex.toString());
		}
	}
	
	
	
		
			
	public Object deepClone(){
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try{	
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			oos = new ObjectOutputStream(bos);
			oos.writeObject(this);   
			oos.flush();             
			ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bin);  
			return onDeepClone(ois.readObject());
		}catch(Exception e){
			throw new InternalError(e.toString());
		}finally{
			try{
				oos.close();
				ois.close();
			}catch(IOException e){}
		}
	}
	
	
	
	/*
	 * Auditing Functionality
	 */
	
	/** 
	 * Indicates whether auditing is enabled for the entity
	 */ 
	public boolean isAudited() {
		return audited;
	}
	
	/**
	 * Sets whether auditing is enabled for the entity. 
	 * @param audited
	 */
	public void setAudited(boolean audited){
		this.audited = audited;
	}
	
	
	
	/**
	 * Default entity for the audit record is the entity itself.  
	 */
	public String getAuditEntityName(){
		if(this.auditEntityName==null){
			this.auditEntityName = this.getEntityName();
		}
		return this.auditEntityName;
	}
	
	/**
	 * Allows subclasses to set the auditEntityName property if it 
	 * differs from the value returned by getEntityName().
	 * @return
	 */
	protected void setAuditEntityName(String auditEntityName){
		this.auditEntityName = auditEntityName;
	}
	
	
	/**
	 * Base type of the entity (for recording the root element type of 
	 * class heirarchies where the subclasses share a common id generating 
	 * strategy).  Default is the value returned by getEntityName()
	 */
	public String getAuditEntityType(){
		if(this.auditEntityType==null){
			this.auditEntityType = this.getEntityName();
		}
		return this.auditEntityType;
	}
	
	/**
	 * Allows subclasses to set the auditEntityType if it differs from the 
	 * value returned by getEntityName()
	 * @return
	 */
	protected void setAuditEntityType(String auditEntityType){
		this.auditEntityType = auditEntityType;
	}
	
	
	/**
	 * returns the names of the properties in the saved audit state
	 * @return
	 */
	public String[] getAuditStateNames() {
		return auditStateNames;
	}

	/**
	 * Set the names of the properties in the saved audit state
	 * @param auditStatePropertyNames
	 */
	public void setAuditStateNames(String[] auditStatePropertyNames) {
		this.auditStateNames = auditStatePropertyNames;
	}

	/** 
	 * returns the types (org.hibernate.type.Type) of the properties in the saved audit state
	 * @return
	 */
	public Type[] getAuditStateTypes() {
		return auditStateTypes;
	}
	
	
	/**
	 * sets the types (org.hibernate.type.Type) of the properties in the saved audit state
	 * @param auditStateTypes
	 */
	public void setAuditStateTypes(Type[] auditStateTypes) {
		this.auditStateTypes = auditStateTypes;
	}

	/**
	 * gets the values of the properties in the saved audit state
	 * @return
	 */
	public Object[] getAuditStateValues() {
		return auditStateValues;
	}

	/**
	 * set the values of the properties in the saved audit state
	 * @param auditStateValues
	 */
	public void setAuditStateValues(Object[] auditStateValues) {
		this.auditStateValues = auditStateValues;
	}

	/**
	 * sets the saved audit state of the entity.
	 * @param values
	 * @param names
	 * @param types
	 */
	public void setAuditState(Object[] values, String[] names, Type[] types){
		this.setAuditStateValues(values);
		this.setAuditStateNames(names);
		this.setAuditStateTypes(types);
	}

	/**
	 * returns a list of properties that the auditing mechanism should ignore 
	 * when creating audit property records for the entity.
	 * @return
	 */
	public ArrayList getAuditIgnoreProperties() {
		return auditIgnoreProperties;
	}

	/**
	 * Sets the properties of the entity that should be ignored by the auditing 
	 * mechanism.  
	 * @param auditIgnoreProperties
	 */
	public void setAuditIgnoreProperties(ArrayList auditIgnoreProperties) {
		this.auditIgnoreProperties = auditIgnoreProperties;
	}
	
	/**
	 * Add a property to the list of ignored properties.  Syntax is 
	 * [property name]  e.g. firstName 
	 * [property name].[subproperty name]  e.g. patient.firstName
	 * @param property
	 */
	public void addPropertyToAuditIgnoreList(String property){
		this.auditIgnoreProperties.add(property);
	}

	
	//Dirty Tracking
	/**
	 * Registers the property and value with the dirty tracking mechanism.  If the property 
	 * already has a value associated with it in the dirty tracking mechanism, then the value
	 * passed in becomes the dirty value, otherwise the value passed in is the old value
	 * 
	 * store Date, Time, and Timestamp values internally as java.util.Timestamp for consistent comparisons
	 * 
	 */
	protected void trackDirty(String property,Object value){
		if(property!=null){
			Object convertedValue = null;
			if(value!=null){
				if(value instanceof Date){
					convertedValue = new Timestamp(((Date)value).getTime());
				}else if(value instanceof Time){
					convertedValue = new Timestamp(((Date)value).getTime());
				}else{
					convertedValue = value;
				}
			}
			if(oldValues.containsKey(property)){
				dirtyValues.put(property,convertedValue);
			}else{
				oldValues.put(property,convertedValue);
			}
		}
	}
	/**
	 * Determines whether the property is dirty.  Only works for values that use track dirty
	 * in their property setter methods. 
	 * 
	 * @param property
	 */
	protected boolean isDirty(String property){
		if(property == null || 
			!oldValues.containsKey(property) || 
			!dirtyValues.containsKey(property)){
			return false;
		}else{
			Object oldValue = oldValues.get(property);
			Object dirtyValue = dirtyValues.get(property);
			if(oldValue==null && dirtyValue==null){
				return true;
			}else if(oldValue==null || dirtyValue==null || !oldValue.equals(dirtyValue)){
				return false;
			}
		}
		return true;
		
		
	}
	
	//persistence methods / triggers
	
	/**
	 * Called just before an entity is deleted
	 */
	public void beforeDelete(){};
	
	/**
	 * Called just after an entity is deleted
	 */
	public void afterDelete(){};
	
	public void delete() {
		MANAGER.delete(this);
	}

	public static LavaDaoFilter newFilterInstance() {
		return MANAGER.newFilterInstance(null);
	}

	public static LavaDaoFilter newFilterInstance(AuthUser user) {
		return MANAGER.newFilterInstance(user);
	}

	
	public void refresh() {
		MANAGER.refresh(this);	
	}

	public void refresh(boolean initializeDependents) {
		MANAGER.refresh(this,initializeDependents);	
		
	}

	public void release() {
		release(false);
		
	}
	public void release(boolean flush) {
		if(flush){
			MANAGER.flushAndEvict(this);
		}else{
			MANAGER.evict(this);
		}
		
	}
	
	/**
	 * Override in specific model classes to update calculated or formatted fields
	 * based on the current state of the model.  Called automatically when a save 
	 * is done. 
	 *
	 */
	public void updateCalculatedFields(){
		return;
	}
	
	/**
	 * Called by the persistence layer just prior to saving the entity when the entity 
	 * has not been persisted before.
	 * 
	 */
	public void beforeCreate(){
		updateCalculatedFields();
		return;
	}
	
	/**
	 * called by the persistence layer just after saving the entity when the entity has
	 * not been persisted before. 
	 * @return true if the entity needs to be re-saved (e.g. modifications to the entity were made
	 * that need to be persisted).
	 */
	public boolean afterCreate(){
		return false;
	}
	
	/**
	 * Called by the persistence layer just after saving the entity when the entity 
	 * has already been persisted.
	 * 
	 */
	
	public void beforeUpdate(){
		updateCalculatedFields();
	}
	
	/**
	 * called by the persistence layer just after saving the entity when the entity has
	 * been persisted before. 
	 * @return true if the entity needs to be re-saved (e.g. modifications to the entity were made
	 * that need to be persisted).
	 */
	public boolean afterUpdate(){
		return false;
	}
	
	public void save() {
		MANAGER.save(this);
		
	}
	
	public void initialize() {
		MANAGER.initialize(this);
	}

	
	/**
	 * Utility Function to strip the time from a date
	 */
	public static Date dateWithoutTime(Date dateIn){
		if(dateIn == null){return null;}
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		try{
			return dateFormat.parse(dateFormat.format(dateIn));
		}catch(Exception e){
			return null;
		}
	}
	
	

	
	
	
	
	
	public static class Manager implements EntityManager{
		private EntityManagerBase manager;
		private Class entityClass;
		
		
		public Manager(Class entityClass){
			this.entityClass = entityClass;
			manager = new EntityManagerBase();
		}
		
		public Object create() {
			return manager.create(entityClass);
		}

		public Object create(Class clazz) {
			return manager.create(clazz);
		}

		public void delete(Object object) {
			manager.delete(object);
		}

		
		public void evict(Object entity) {
			manager.evict(entity);
		}

		public void flushAndEvict(Object entity){
			manager.flushAndEvict(entity);
		}
		
		public void executeSQLProcedure(String procedureName, Object[] paramValues, int[] paramTypes, char[] paramIOFlags) {
			manager.executeSQLProcedure(procedureName, paramValues, paramTypes, paramIOFlags);
		}

		public List findByNamedQuery(String namedQuery, LavaDaoFilter filter) {
			return manager.findByNamedQuery(namedQuery, filter);
		}
		
		public  Object findOneByNamedQuery(String namedQuery, LavaDaoFilter filter) {
			return manager.findOneByNamedQuery(namedQuery, filter);
		}

		public List get() {
			return manager.get(entityClass);
		}

		public List get(Class clazz, LavaDaoFilter filter) {
			return manager.get(clazz, filter);
		}

		public List get(LavaDaoFilter filter) {
			return manager.get(entityClass,filter);
		}

		public Object getById(Long id, LavaDaoFilter filter) {
			return manager.getById(entityClass,id, filter);
		}
		

		public Object getById(Class clazz, Long id, LavaDaoFilter filter) {
			return manager.getById(clazz,id, filter);
		}

		public List<Long> getIds(LavaDaoFilter filter) {
			return manager.getIds(entityClass,filter);
		}

	
		public List<Long> getIds(Class entityClass,LavaDaoFilter filter) {
			return manager.getIds(entityClass,filter);
		}

		public Object getOne(Class clazz, LavaDaoFilter filter) {
			return manager.getOne(clazz, filter);
		}

		public Object getOne(LavaDaoFilter filter) {
			return manager.getOne(entityClass,filter);
		}

		public Integer getResultCount(LavaDaoFilter filter) {
			return manager.getResultCount(entityClass,filter);
		}

	
		public void initialize(Object entity) {
			manager.initialize(entity);
		}

		public LavaDaoFilter newFilterInstance() {
			return manager.newFilterInstance();
		}

		public LavaDaoFilter newFilterInstance(AuthUser user) {
			return manager.newFilterInstance(user);
		}

		public void refresh(Object object, boolean InitializeDependents) {
			manager.refresh(object, InitializeDependents);
		}

		public void refresh(Object object) {
			manager.refresh(object);
		}

		public void save(Object object) {
			manager.save(object);
		}

		public Class getEntityClass() {
			return entityClass;
		}


	
		
		
		
	}









}