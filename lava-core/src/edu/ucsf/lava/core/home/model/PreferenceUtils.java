package edu.ucsf.lava.core.home.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ucsf.lava.core.auth.model.AuthUser;

public class PreferenceUtils {

	public static List removeOverriddenDefaults(List prefs){
		Set<String> userPrefNames = new HashSet<String>();
		List filteredPrefs = new ArrayList();
		/* add user preferences to final list and identify names 
		    for later comparison against defaults */
		for (Object o : prefs){
			Preference pref = (Preference)o;
			if (!pref.isDefault()){
				filteredPrefs.add(o);
				userPrefNames.add(pref.getFullName());
			}
		}
		// filter out redundant default preferences
		for (Object o : prefs){
			Preference pref = (Preference)o;
			if (pref.isDefault() && !userPrefNames.contains(pref.getFullName())){
				filteredPrefs.add(o);
			}
		}
		
		return filteredPrefs;
	}
	
	public static String getPrefValue(AuthUser user, String context, String name, String defaultValue){
		
		Preference userPref = Preference.MANAGER.get(user, context, name);
		if (userPref!=null){
			return userPref.getValue();
		} else {
			// no user pref found, try default value
			Preference defaultPref = Preference.MANAGER.get(null, context, name);
			if (defaultPref!=null){
				return defaultPref.getValue();
			} else {
				// no default pref for the context exists, try user global pref of same name
				Preference userGlobalPref = Preference.MANAGER.get(user, null, name);
				if (userGlobalPref!=null){
					return userGlobalPref.getValue();
				} else {
					// no user global pref found, try default global pref
					Preference defaultGlobalPref = Preference.MANAGER.get(null, null, name);
					if (defaultGlobalPref!=null){
						return defaultGlobalPref.getValue();
					}
				}
			}
		}
		
		return defaultValue;
	}
	
	public static void setPrefValue(AuthUser user, String context, String name, String value){
		Preference pref = Preference.MANAGER.get(user, context, name);
		if (pref!=null){
			pref.setValue(value);
			pref.save();
		} else {
			if (user!=null){
				// create user pref from default
				Preference defaultPref = Preference.MANAGER.get(null, context, name);
				if (defaultPref!=null){
					Preference userPref = Preference.MANAGER.createUserPref(user, defaultPref);
					userPref.setValue(value);
					userPref.save();
				}
			}
		}
	}
	
}
