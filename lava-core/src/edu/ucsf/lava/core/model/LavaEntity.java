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
	
	//Model level "triggers" 
	public void beforeCreate();
	public boolean afterCreate();
	public void beforeUpdate();
	public boolean afterUpdate();
	public void beforeDelete();
	public void afterDelete();
	
	
	
	
	
}
