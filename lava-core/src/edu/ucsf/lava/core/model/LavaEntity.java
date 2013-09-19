package edu.ucsf.lava.core.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.hibernate.type.Type;

import edu.ucsf.lava.core.metadata.MetadataManager;

public interface LavaEntity extends Serializable {
   
	
	//standard missing data codes
	public static Long DATA_CODES_CANNOT_TOTAL = new Long(-5);
	public static Long DATA_CODES_LOGICAL_SKIP = new Long(-6);
	public static Long DATA_CODES_INCOMPLETE = new Long(-7);
	public static Long DATA_CODES_UNUSED = new Long(-8);
	public static Long DATA_CODES_MISSING = new Long(-9);
	
	public static Float DATA_CODES_CANNOT_TOTAL_FLOAT = new Float(-5.0);
	public static Float DATA_CODES_LOGICAL_SKIP_FLOAT = new Float(-6.0);
	public static Float DATA_CODES_INCOMPLETE_FLOAT = new Float(-7.0);
	public static Float DATA_CODES_UNUSED_FLOAT = new Float(-8.0);
	public static Float DATA_CODES_MISSING_FLOAT = new Float(-9.0);
	
	//identity methods
	public Long getId();
	public Object clone();
	public Object deepClone();
	
	public String getEntityName();
	public String getEntityNameEncoded(boolean includeVersion);
	public String getEntityNameEncoded();
	
	//entity versioning methods
	public boolean isVersioned();
	public String getEntityVersion();
	public String getEntityVersionEncoded();
	public void setEntityVersion(String entityVersion);
	
	
	
	//entity auditing methods
	public boolean isAudited();
	public void setAudited(boolean audited);
	public String getAuditEntityName();
	public String getAuditEntityType();
	public String[] getAuditStateNames();
	public void setAuditStateNames(String[] auditStatePropertyNames);
	public Type[] getAuditStateTypes();
	public void setAuditStateTypes(Type[] auditStateTypes);
	public Object[] getAuditStateValues();
	public void setAuditStateValues(Object[] auditStateValues);
	public void setAuditState(Object[] values, String[] names, Type[] types);
	public ArrayList getAuditIgnoreProperties();
	public void setAuditIgnoreProperties(ArrayList auditIgnoreProperties);
	public void addPropertyToAuditIgnoreList(String property);

	
	//persistence / creation methods
	public void validate(MetadataManager metadataManager);
	public void save();
	public void delete();
	public void refresh();
	public void refresh(boolean initializeDependents);
	public void release();
	public Object[] getAssociationsToInitialize(String method);
	public boolean initializeAssocationsForObjectLists(String method);
	public Object postCreate();
	public void initialize();
	
	//Model level "triggers" 
	public void beforeCreate();
	public boolean afterCreate();
	public void beforeUpdate();
	public boolean afterUpdate();
	public void beforeDelete();
	public void afterDelete();
	
	/* Allow entities to be locked.  Used by the views to determine
	 *    if view allows access to modifying actions on this entity */
	public boolean getLocked();
	
	
}
