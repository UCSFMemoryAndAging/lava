package edu.ucsf.lava.core.home.model;

import java.util.Date;
import java.util.List;

import edu.ucsf.lava.core.auth.AuthDaoUtils;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;

public class Preference extends EntityBase {

	public static Preference.Manager MANAGER = new Preference.Manager();
	
	private Long id;
	private AuthUser user;
	private String context;
	private String name;
	private String description;
	private String value;
	private Short visible;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AuthUser getUser() {
		return user;
	}
	public void setUser(AuthUser user) {
		this.user = user;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public Short getVisible() {
		return visible;
	}
	public void setVisible(Short visible) {
		this.visible = visible;
	}
	public Boolean isDefault(){
		return (this.user == null);
	}
	
	public String getFullName(){
		String fullName = "";
		if (this.getContext()!=null){
			fullName = fullName.concat(this.getContext()).concat(".");
		}
		fullName = fullName.concat(this.getName());
		return fullName;
	}
	
	public Boolean isEquivalent(Preference pref){
		if (pref==null){return false;}
		return (this.getFullName().equals(pref.getFullName()));
	}
	
	static public class Manager extends EntityBase.Manager{
		
		public Manager(){
			super(Preference.class);
		}
		
		public List get(AuthUser user){
			LavaDaoFilter filter = newFilterInstance();
			if (user!=null){
				filter.setAlias("user", "user");
				filter.addDaoParam(filter.daoEqualityParam("user.id", user.getId()));
			} else {
				filter.addDaoParam(filter.daoNull("user"));
			}
			
			return Preference.MANAGER.get(filter);
	
		}
		
		public List get(AuthUser user, String context){
			LavaDaoFilter filter = newFilterInstance();
			
			if (user!=null){
				filter.setAlias("user", "user");
				filter.addDaoParam(filter.daoEqualityParam("user.id", user.getId()));
			} else {
				filter.addDaoParam(filter.daoNull("user"));
			}

			if (context!=null){
				filter.addDaoParam(filter.daoEqualityParam("context", context));
			} else {
				filter.addDaoParam(filter.daoNull("context"));
			}
			
			return Preference.MANAGER.get(filter);
		}
		
		public Preference get(AuthUser user, String context, String name){
			LavaDaoFilter filter = newFilterInstance();
			
			if (name!=null){
				
				filter.addDaoParam(filter.daoEqualityParam("name", name));
				
				if (user!=null){
					filter.setAlias("user", "user");
					filter.addDaoParam(filter.daoEqualityParam("user.id", user.getId()));
				} else {
					filter.addDaoParam(filter.daoNull("user"));
				}

				if (context!=null){
					filter.addDaoParam(filter.daoEqualityParam("context", context));
				} else {
					filter.addDaoParam(filter.daoNull("context"));
				}

			} else {
				return null;
			}
			
			return (Preference)Preference.MANAGER.getOne(filter);
		}
		
		
		public Preference createUserPref(AuthUser user, Preference defaultPref){
			if (user==null || defaultPref==null){return null;}
			Preference userPref = (Preference)defaultPref.deepClone();
			userPref.setUser(user);
			userPref.setId(null);
			userPref.save();
			return userPref;
		}
		
		public Preference createUserPrefById(AuthUser user, Long defaultPrefId){
			Preference defaultPref = (Preference)this.getById(defaultPrefId, newFilterInstance(null));
			return createUserPref(user, defaultPref);
		}
		
	}
	
}
