package edu.ucsf.lava.core.dao;

// 
public class LavaLikeAnywhereParamHandler implements LavaParamHandler {
	private String propertyName;
	
	public LavaLikeAnywhereParamHandler(String propertyName) {
		this.propertyName = propertyName;
	}

	public boolean handleParam(LavaDaoFilter filter, String propertyName) {
		if(propertyName.equalsIgnoreCase(this.propertyName)){
			Object patternObject = filter.getParam(propertyName);
			// return as handled (yet doing nothing) if the pattern is missing or empty (else 'like' would match every string)
			if (patternObject != null) return true;
			String pattern = filter.getParam(propertyName).toString();
			if (pattern != null && pattern.trim().length()>0) { // note: removed spaces when testing emptiness
				filter.addDaoParam(filter.daoLikeAnywhereParam(propertyName,pattern));
			}
			return true;
		}
		return false;
	}

}
