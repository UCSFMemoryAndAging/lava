package edu.ucsf.lava.crms.enrollment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;



public class ProjectUnitUtils {
	public final static String ANY_WILDCARD = "*";
	public static final String PROJECTUNIT_FORMAT = "(.*)\\s\\[(.*)\\]$";
	public static final int PROJ_INDEX = 0;
	public static final int UNIT_INDEX = 1;
	
	
	
	public static boolean matches(String projUnitDesc, String compareDesc){
		String[] parts = parseProjectUnit(projUnitDesc);
		String[] compareParts = parseProjectUnit(compareDesc);
		return (doProjectsMatch(parts,compareParts) && doUnitsMatch(parts,compareParts));
	}
		
	
	protected static boolean doProjectsMatch(String[] parts, String[] compareParts){
		//if project is undefined on either side then no match
		if(!isProjectDefined(parts)||!isProjectDefined(compareParts)){
			return false;
		}else if(isProjectWildcard(parts) || isProjectWildcard(compareParts) ||
				StringUtils.equals(parts[PROJ_INDEX],compareParts[PROJ_INDEX])){
			return true;
		}else{
			return false;
		}
	}
	
	protected static boolean doUnitsMatch(String[] parts, String[] compareParts){
		//if either side is a wildcard then there is a match
		if(isUnitWildcard(parts) || isUnitWildcard(compareParts)){
			return true;
		}
		//if either side is a wildcard at the project level with no unit defined, then there is a match
		else if((isProjectWildcard(parts)&&!isUnitDefined(parts))||
				(isProjectWildcard(compareParts)&&!isUnitDefined(compareParts))){
				return true;
		//if both units are not defined then there is a match
		}else if(!isUnitDefined(parts) && !isUnitDefined(compareParts)){
			return true;
		//if they are both defined and they match..then of course, there is a match
		}else if(isUnitDefined(parts) && isUnitDefined(compareParts) &&
				StringUtils.equals(parts[UNIT_INDEX],compareParts[UNIT_INDEX])){
			return true;
		}else{
			return false;
		}
	}

							   
	public static String getProject(String projUnitDesc){
		String[] parts = parseProjectUnit(projUnitDesc);
		if(parts.length > PROJ_INDEX){
			return parts[PROJ_INDEX];
		}else{
			return new String();
		}
	}
	
	public static String getUnit(String projUnitDesc){
		String[] parts = parseProjectUnit(projUnitDesc);
		if(parts.length > UNIT_INDEX){
			return parts[UNIT_INDEX];
		}else{
			return new String();
		}
	}
	
	
	
	public static boolean isProjectDefined(String projUnitDesc){
		return isProjectDefined(parseProjectUnit(projUnitDesc));
	}
	protected static boolean isProjectDefined(String[] parts){
		return parts.length>PROJ_INDEX && 
				StringUtils.isNotEmpty(parts[PROJ_INDEX]);
	}
	public static boolean isUnitDefined(String projUnitDesc){
		return isUnitDefined(parseProjectUnit(projUnitDesc));
	}
	protected static boolean isUnitDefined(String[] parts){
		return parts.length>UNIT_INDEX && 
		StringUtils.isNotEmpty(parts[UNIT_INDEX]);
	}
	
	public static boolean isUnitWildcard(String projUnitDesc){
		return isUnitWildcard(parseProjectUnit(projUnitDesc));
	}
	protected static boolean isUnitWildcard(String[] parts){
		return (isUnitDefined(parts)&&
				StringUtils.equals(parts[UNIT_INDEX],ANY_WILDCARD));
	}
	
	public static boolean isProjectWildcard(String projUnitDesc){
		return isProjectWildcard(parseProjectUnit(projUnitDesc));
	}
	
	protected static boolean isProjectWildcard(String[] parts){
		return (isProjectDefined(parts) &&
				StringUtils.equals(parts[PROJ_INDEX],ANY_WILDCARD));
	}
	/**
	 * Matches a project unit description of '*' or '* [*]'
	 * @param projUnitDesc
	 * @return
	 */
	public static boolean isProjectUnitWildcard(String projUnitDesc){
		String[] parts = parseProjectUnit(projUnitDesc);
		if(isProjectWildcard(parts) &&
			(isUnitWildcard(parts) || !isUnitDefined(parts))){
				return true;
		}
		return false;
	}
	/**
	 * Returns a description of the project unit for this rule. 
	 * Description follows this pattern:
	 * 		project is empty = empty string
	 * 		project is wildcard and unit is wildcard or empty = wildcard
	 * 		project is wildcard and unit is defined = wildcard [unit]
	 *		project is defined and unit is empty = project
	 *		project is defined and unit is wildcard = project [wildcard]
	 *		project and unit are defined = project [unit]
	 *
	 * @return
	 */
	public static String getDescription(String project, String unit){
		String[] parts = new String[]{project,unit};
		
		if(!isProjectDefined(parts)){
			return new String();
		}else if(isProjectWildcard(parts) && isUnitWildcard(parts)){
			return ANY_WILDCARD;
		}else if(isUnitDefined(parts)){
			return StringUtils.join(new String[]{project," [",unit,"]"});
		}else{
			return project;
		}
	}
	

	
	
	private static String[] parseProjectUnit(String projectUnit){
		Matcher projectUnitMatcher = Pattern.compile(PROJECTUNIT_FORMAT).matcher(projectUnit);
		if (!projectUnitMatcher.matches()){
			return new String[]{projectUnit};
		}
		return new String[]{projectUnitMatcher.group(PROJ_INDEX+1),
						projectUnitMatcher.group(UNIT_INDEX+1)};
	}
}
