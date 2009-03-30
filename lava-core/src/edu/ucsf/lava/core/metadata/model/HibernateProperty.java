package edu.ucsf.lava.core.metadata.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;



public class HibernateProperty extends EntityBase {

	public static EntityManager MANAGER = new EntityBase.Manager(HibernateProperty.class);
	
	private String scope;
	private String entity;
	private String property;
	private String dbTable;
	private String dbColumn;
	private String dbType;
	private Short dbLength;
	private Short dbPrecision;
	private Short dbScale;
	private Short dbOrder;
	private String hibernateProperty;
	private String hibernateType;
	private String hibernateClass;
	private String hibernateNotNull;
	
	public HibernateProperty(){
		super();
		this.setAudited(false);
	}
	
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDbColumn() {
		return dbColumn;
	}

	public void setDbColumn(String dbColumn) {
		this.dbColumn = dbColumn;
	}

	public Short getDbLength() {
		return dbLength;
	}

	public void setDbLength(Short dbLength) {
		this.dbLength = dbLength;
	}

	public Short getDbOrder() {
		return dbOrder;
	}

	public void setDbOrder(Short dbOrder) {
		this.dbOrder = dbOrder;
	}

	public Short getDbPrecision() {
		return dbPrecision;
	}

	public void setDbPrecision(Short dbPrecision) {
		this.dbPrecision = dbPrecision;
	}

	public Short getDbScale() {
		return dbScale;
	}

	public void setDbScale(Short dbScale) {
		this.dbScale = dbScale;
	}

	public String getDbTable() {
		return dbTable;
	}

	public void setDbTable(String dbTable) {
		this.dbTable = dbTable;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getHibernateClass() {
		return hibernateClass;
	}

	public void setHibernateClass(String hibernateClass) {
		this.hibernateClass = hibernateClass;
	}

	public String getHibernateNotNull() {
		return hibernateNotNull;
	}

	public void setHibernateNotNull(String hibernateNotNull) {
		this.hibernateNotNull = hibernateNotNull;
	}

	public String getHibernateProperty() {
		return hibernateProperty;
	}

	public void setHibernateProperty(String hibernateProperty) {
		this.hibernateProperty = hibernateProperty;
	}

	public String getHibernateType() {
		return hibernateType;
	}

	public void setHibernateType(String hibernateType) {
		this.hibernateType = hibernateType;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
}
