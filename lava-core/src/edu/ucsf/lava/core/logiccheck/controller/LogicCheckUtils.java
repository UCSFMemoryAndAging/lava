package edu.ucsf.lava.core.logiccheck.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.StringUtils;

import edu.ucsf.lava.core.logiccheck.model.LogicCheck;
import edu.ucsf.lava.core.logiccheck.model.LogicCheckIssue;
import edu.ucsf.lava.core.logiccheck.model.LogicCheckSummary;
import edu.ucsf.lava.core.model.EntityBase;

// common logic check functions, used as utility
// or logic check functions that would never be overridden by sub-classes
public class LogicCheckUtils {
	
	// perform logic checks on this entity that had just changed, and persist any issues
	public static void doLogicChecks(EntityBase entity) {
		// get current list of issues to use to modify while we traverse the logic checks
		List<LogicCheckIssue> lcissues = (List<LogicCheckIssue>)entity.getLogicCheckIssues();
		
		// do all matching checks that are found in the logiccheck definition table for this entity
		List<LogicCheck> logicchecks = entity.getLogicChecks();
		if (logicchecks != null && logicchecks.size() > 0) {
			for (LogicCheck logiccheck : logicchecks) {
				// do not check any disabled or inactive definitions
				if (logiccheck.getEnabled() == null || logiccheck.getEnabled().equals((byte)0)
					|| logiccheck.getActiveDate() == null) {
					// remove any issues pertaining to this logic check
					if (lcissues != null) {
						LogicCheckIssue lcissue;
						Iterator<LogicCheckIssue> iter = lcissues.iterator();
						while (iter.hasNext()) {
							lcissue = iter.next();
							if (lcissue.getDefinition().getId().equals((long)logiccheck.getId())) {
								iter.remove();
								lcissue.delete();
							}
						}
					}
					continue;  // onward to next logic check
				}
				
				// this check is to be performed on this entity
				logiccheck.doLogicCheckMethod(entity, lcissues);

			}
		} else if (lcissues != null && lcissues.size() > 0) { // no logic checks found to apply to this entity, but issues were found
			// Then also ensure there are no *issues* found for this entity.  A logiccheck definition may have changed that
			// previously could have specified this entity and added an issue at that time.  We wish to remove that now.
			LogicCheckIssue lcissue;
			Iterator<LogicCheckIssue> iter = lcissues.iterator();
			while (iter.hasNext()) {
				lcissue = iter.next();
				iter.remove();
				lcissue.delete();
			}
		}
		
		// update the entities logic check summary fields that contain status
		LogicCheckUtils.updateLogicCheckStatus(entity, lcissues);
	}
	
	// this entity may be referenced by a crossform logic check done on another form; in fact, possibly multiple checks
	// find those logic checks and the corresponding entities, and apply those logic checks again to the entities
	// those corresponding entities are essentially "dependent" upon this entity for logic consistency
	public static void doLogicChecksOnDependentEntities(EntityBase entity) {
		// define a custom class here so we can create an ordered set of pairs of EntityBase and LogicCheck
		final class EntityLCPair {
			EntityBase entity;
			LogicCheck logiccheck;
			public EntityLCPair(EntityBase entity, LogicCheck logiccheck) {
				this.entity=entity;
				this.logiccheck=logiccheck;
			};
			EntityBase getEntity() { return this.entity; };
			LogicCheck getLogiccheck() { return this.logiccheck; };
			// not sure if equals() needs to be overridden, but just in case
			@Override
			public boolean equals(Object obj) {
				if (this == obj) return true;
				if (obj == null) return false;
				if (!(obj instanceof EntityLCPair))
	                return false;
				EntityLCPair other = (EntityLCPair) obj;
				if (this.entity.equals(other.entity)
					&& this.logiccheck.equals(other.logiccheck)) return true;
				return false;
			}
		}
		// ordering is not really that important, as long as same entities are
		// next to each other in a sorted list
		final class EntityLCPairComparator implements Comparator<EntityLCPair> {
			public int compare(EntityLCPair ob1, EntityLCPair ob2){
				// we must consider "logiccheck" ids else if only compare by "entity", then same entities return 0,
				//   which means same element, thus won't be added at all
				if (ob2.entity.getId() > ob1.entity.getId())
					return 1;
				else if (ob2.entity.getId() < ob1.entity.getId())
					return -1;
				else {
					if (ob2.logiccheck.getId() > ob1.logiccheck.getId())
						return 1;
					else if (ob2.logiccheck.getId() < ob1.logiccheck.getId())
						return -1;
					else
						return 0;  // same, thus don't add as separate entity
				}
			}
		}
		
		TreeSet<EntityLCPair> logicchecks_todo = null;
		
		// for every logic check that could be dependent on this entity,
		//   find all the dependent entities and save the entity-logiccheck pair in a list to apply later
		List<LogicCheck> dependentLogicchecks = entity.getLogicChecks_Dependents();
		// the resulting list is all logic checks that *could* be applied to another entity
		if (dependentLogicchecks != null) {
			for (LogicCheck dependentLogiccheck : dependentLogicchecks) {
				// ignore any disabled definitions
				if (dependentLogiccheck.getEnabled() == null || dependentLogiccheck.getEnabled().equals((byte)0)) continue;
				// ignore any inactive definitions
				if (dependentLogiccheck.getActiveDate() == null) continue;
				
				// find the dependent entities to use for this check
				List<EntityBase> listDependentEntities = dependentLogiccheck.getDependentEntities(entity);
				if (listDependentEntities != null) {
					for (EntityBase dependentEntity : listDependentEntities) {
						// add this check/entity as a pair to a list that will be used to perform the checks later, so
						//  that entity data doesn't need to be pulled multiple times (useful for instrument entities)
						if (logicchecks_todo==null) logicchecks_todo = new TreeSet<EntityLCPair>(new EntityLCPairComparator());
						logicchecks_todo.add(new EntityLCPair(dependentEntity,dependentLogiccheck));
					}
				}
			}
		}
		
		// Process each pair to do logic checks on each entity found.  Since the list is ordered by entity,
		//  getLogicCheckIssues() and updateLogicCheckStatus() are programmed below to only run once per entity
		EntityBase dependentEntity;
		EntityBase lastEntity = null;
		LogicCheck dependentLogiccheck;
		List<LogicCheckIssue> lcissues = null;
		if (logicchecks_todo != null) {
			for (EntityLCPair pair : logicchecks_todo) {
				dependentEntity = pair.getEntity();
				dependentLogiccheck = pair.getLogiccheck();
				if (!dependentEntity.equals(lastEntity)) {
					// first save the lastEntity's logiccheck status to finish off the last entity
					if (lastEntity != null) LogicCheckUtils.updateLogicCheckStatus(lastEntity, lcissues);
					// new entity, so new logic check issues
					lcissues = (List<LogicCheckIssue>)dependentEntity.getLogicCheckIssues();
					lastEntity = dependentEntity;
				}
				// this check is to be performed on the dependent entity
				dependentLogiccheck.doLogicCheckMethod(dependentEntity, lcissues);
			}
	    }
		// the last updateLogicCheckStatus() was never done, so do it
		if (lastEntity != null) LogicCheckUtils.updateLogicCheckStatus(lastEntity, lcissues);
		
	}
	
	// update logic check summary associated with this entity; if no issues, then no summary entry should be in table
	public static void updateLogicCheckStatus(EntityBase entity, List lcissues) {
		boolean found_error = false;
		boolean found_unver_alert = false;
		if (lcissues != null) {
			for (LogicCheckIssue lcissue : (List<LogicCheckIssue>)lcissues) {
				if (lcissue.getInvalidDef() != null && lcissue.getInvalidDef().equals((byte)1))
					continue;  // if invalidDef, ignore
				if (!lcissue.getIsalert())
					found_error = true;
				else if (lcissue.getVerified() == null || lcissue.getVerified().equals(new Byte((byte)0))) 
					found_unver_alert = true;
				if (found_error && found_unver_alert) break; // no use in checking more
			}
		}
		
		LogicCheckSummary lcsummary = entity.getLogicCheckSummary();
		if (found_error || found_unver_alert) {
			// if an issue, and summary not found, create summary now
			if (lcsummary == null) {
				lcsummary = (LogicCheckSummary)entity.getLogicCheckSummaryManager().create();
				lcsummary.setEntityID(entity);
				lcsummary.save();
			}
			// update the status
			lcsummary.setLcStatus(found_error || found_unver_alert ? "Failed" : "Passed");
			lcsummary.setLcReason(found_error ? (found_unver_alert ? "Errors and Unver. Alerts" : "Errors")
					                     : (found_unver_alert ? "Unver. Alerts" : null));
			lcsummary.setLcDate(new Date());
			lcsummary.save();
		} else { // no issue
			// if no issue, but summary found, then delete the summary
			if (lcsummary != null) {
				lcsummary.delete();
			}
		}
	}
	
	public static boolean isLogicCheckProblem(List<LogicCheckIssue> lcissues) {
		if (lcissues != null) {
			// a logic check issue is a problem if it is an error or if it is an unverified alert, but never if it is invalidDef to begin with
			for (LogicCheckIssue lcissue : lcissues) {
				if (lcissue.getInvalidDef() != null && lcissue.getInvalidDef().equals((byte)1))
					continue;  // if invalidDef, ignore
				if (!lcissue.getIsalert())
					return true; // then an error found
				if (lcissue.getVerified() == null || lcissue.getVerified().equals(new Byte((byte)0))) 
					return true; // then unverified alert found
			}
		}
		return false;
	}
		

	public static Object getFieldValue(EntityBase entity, String fieldname) {
		// Use Java reflection
		Method method;
		String methodName;
		Object returnObject;
		
		returnObject = null;
		methodName = "get" + StringUtils.upperCase(fieldname.substring(0, 1)) + fieldname.substring(1);

		// throw again any exceptions occurring from reflection invocation so that it will
		// get appropriately noted as an "invalidDef" issue
		
		// find method
		try {
		  method = entity.getClass().getMethod(methodName);
		  // invoke
		  try {
			returnObject = method.invoke(entity);
		  //} catch (IllegalArgumentException e) {
		  } catch (IllegalAccessException e) {
			  throw new RuntimeException("Logic Check low-level error");
		  } catch (InvocationTargetException e) {
			  throw new RuntimeException("Logic Check low-level error");
		  }
		//} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Logic Check low-level error");
		}
		return returnObject;
		
	}
	
	// use this on fields that are strings; ignore null values
	public static boolean isMetadataField(Object field_value) {
		try {
			// strings are assigned string values of "-7", "-9", etc. if intended as metadata (e.g. logic skip)
			float field_float = Float.parseFloat(field_value.toString());  // use float to check both ints and floats
			if (field_float < 0) return true;
		} catch(NumberFormatException ex) {
			// then wasn't even a number; this is perfectly fine since was supposed to be a string
		}
		catch(NullPointerException ex) {
			// allow null values to pass through; expect caller to handle nulls appropriately
		}
		return false;
	}
	
	public static void sortLogicCheckIssues(List<? extends LogicCheckIssue> lcissues) {
		// This comparator will end up sorting by field1itemNum as a concatenated string of number/string/number/string,
		// instead of field1itemNum as a pure string
		final Comparator<LogicCheckIssue> ITEMNUM_ASC =
            new Comparator<LogicCheckIssue>() {
				public int compare(LogicCheckIssue lc1, LogicCheckIssue lc2) {
					if (lc1.getField1itemNum()==null && lc2.getField1itemNum()==null) return 0;
					if (lc1.getField1itemNum()==null) return -1;
					if (lc2.getField1itemNum()==null) return 1;
					
					final Pattern itemNumPattern = Pattern.compile("^(\\d*)(\\D*)(\\d*)(\\D*)$");  // only support four levels
					final Matcher matcher1 = itemNumPattern.matcher(lc1.getField1itemNum());
					final Matcher matcher2 = itemNumPattern.matcher(lc2.getField1itemNum());
					if (!matcher1.matches() || !matcher2.matches()) return 0;  // cannot compare; should never happen
					int string_compare;
					for (int groupIndex=1; groupIndex < matcher1.groupCount(); groupIndex++) {
						if (groupIndex > matcher2.groupCount()) return 1;  // i.e. if itemNum1 has more chars, then itemNum1 > itemNum2
						// every odd index will be numeric
						if (groupIndex % 2 == 1) {
							if (!NumberUtils.isNumber(matcher1.group(groupIndex))) return -1;
							short match1 = Short.parseShort(matcher1.group(groupIndex));
							if (!NumberUtils.isNumber(matcher2.group(groupIndex))) return 1;
							short match2 = Short.parseShort(matcher2.group(groupIndex));
							if (match1 < match2) return -1;
							if (match1 > match2) return 1;
						} else { // every even index will be a string
							string_compare = matcher1.group(groupIndex).compareTo(matcher2.group(groupIndex));
							if (string_compare != 0) return string_compare; // i.e. we've determined which is larger
						}
						// else if it matched exactly, then continue with next group
					}
					// still no difference
					// TODO: consider further comparison with (say) InstrCheckCode
					return 0;
				}
			};
		// before returning the list, sort based on an interpretation of field1itemNum
		Collections.sort(lcissues, ITEMNUM_ASC);
		
	}
	
	public static boolean isValueInSet(String field_value, String fieldvalues_in_set) throws Exception {
		// figure out whether field_value is represented in fieldvalues_in_set.  The values can be any strings unless
		//   fieldvalues_in_set elicits that values must be a number (e.g.
		//   a dash ('-') along with only numbers will suggest a range of numbers).
		
		// if metadata is to be interpreted any other way, ensure the caller checks first and does not call this
		if (isMetadataField(field_value)) throw new Exception("metadataFound");
		
		// check to see if values must be interpreted as numbers
		boolean use_number_values_necessarily = false;
		Double field_number = null;  // only used if use_number_values_necessarily
		// dashes with only numbers (and commas and dots) suggest a range, else a string
		use_number_values_necessarily = fieldvalues_in_set.contains("-")
			                            && !fieldvalues_in_set.trim().matches("^.*[^0-9,\\.\\-]+.*$"); // the match finds any values other than 0-9, comma, dot, and dash 
		if (use_number_values_necessarily) {
			// interpret the field_value as a number
			try {
				field_number = Double.parseDouble(field_value.trim());
			} catch (NumberFormatException nfe) {
				// I cannot imagine a case where the field type is non-numeric and we would be matching on numbers,
				// so take back the assumption that this must be interpreted as numbers
				field_number = null;
			}
		}
		if (field_number == null)
			use_number_values_necessarily = false;
		
		// Interpret commas in the multi-value string as OR's.
		Double poss_number;
		String poss_value_trimmed;
		String[] poss_values = fieldvalues_in_set.split(",");
		boolean found_in_list = false;
		for (String poss_value : poss_values) {
			poss_value_trimmed = poss_value.trim();
			if (use_number_values_necessarily) {
				if (poss_value_trimmed.contains("-")) {
					// interpret the two sides as min and max numbers
					// note: more dashes will cause Exception, which will make definition invalid (which is what we want to know)
					double min_num = Double.parseDouble(poss_value_trimmed.substring(0,poss_value_trimmed.indexOf("-")));
					double max_num = Double.parseDouble(poss_value_trimmed.substring(poss_value_trimmed.indexOf("-")+1,poss_value_trimmed.length()));
					if (field_number >= min_num && field_number <= max_num)
						return true;
				} else {
					poss_number = Double.parseDouble(poss_value_trimmed);
					if (poss_number.equals(field_number)) {
						return true;
					}
				}				
			} else { // compare as strings			
				// find if equal (trim to ignore surrounding whitespace)
				if (poss_value_trimmed.equals(field_value.trim())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean doOperator_InSet(EntityBase entity,
			String[] checkDescDataAppend,
			String fieldname,
			String fieldvalues) throws Exception {
		// LOGIC: is the field value in the set of fieldvalues?
		
		Object field_obj = LogicCheckUtils.getFieldValue(entity, fieldname);
		if (field_obj == null) throw new Exception("metadataFound");
		String field_value = field_obj.toString();
		
		return isValueInSet(field_value, fieldvalues);
	}
	
	public static boolean doOperator_IsBlank(EntityBase entity,
			String[] checkDescDataAppend,
			String fieldname) throws Exception {
		// LOGIC: is the field value blank?
		Object field_obj = LogicCheckUtils.getFieldValue(entity, fieldname);
		if (field_obj == null) return true;
		String field_value = (String)field_obj.toString();
		return (field_value == null || field_value.equals("") || isMetadataField(field_value));
	}

	public static boolean doOperator_IsKnown(EntityBase entity,
			String[] checkDescDataAppend,
			String fieldname,
			String fieldvalues_unknown) throws Exception {
		// LOGIC: does the field value contain "known" data?
		//Note: fieldvalues denote values that are considered "unknown" (not real)
		//Assume: fieldname refers to numeric field
		
		Object field_obj = LogicCheckUtils.getFieldValue(entity, fieldname);
		if (field_obj == null) return false;
		if (LogicCheckUtils.isMetadataField(field_obj)) return false;
		
		Float field_float = ((Number)field_obj).floatValue();
		
		String[] unknown_values = fieldvalues_unknown.split(",");
		for (String unknown_value : unknown_values) {
			Float unknown_float = Float.parseFloat(unknown_value);
			if (unknown_float != null && unknown_float.equals(field_float)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean doOperator_EqualsStringLength(EntityBase entity,
			String[] checkDescDataAppend,
			String fieldname,
			String fieldvalues) throws Exception {
		// LOGIC: is the field value a string of exactly "fieldvalues" length?
		String field_value = (String)LogicCheckUtils.getFieldValue(entity, fieldname);
		if (field_value == null) throw new Exception("metadataFound");
		if (isMetadataField(field_value)) throw new Exception("metadataFound");
		
		Integer expected_length = Integer.parseInt(fieldvalues);
		if (field_value == null || field_value.equals("")) return false; // blank fields are ok here; assume the logic *skip* checks would ensure non-blanks on required fields
		return (field_value.length() == expected_length);
	}

	public static boolean doOperator_CountIfIn_Equals_Count(EntityBase entity,
			String[] checkDescDataAppend,
			String fieldnames,
			String fieldvalues,
			String fieldvalues_alt) throws Exception {
		// LOGIC: count the fields in fieldnames list whose values are in fieldvalues and return true if that count number is in fieldvalues_alt
		// NOTE: this can only be used with one operator; condition 2 cannot be assigned
		
		// Example: Out of field1, field2, and field3, return true if more than 1 of them equal "0".
		//  In this example, field1values = '0' and field2values = '2-3' (since know three is the max count)
		
		Object field_obj;
		String field_value;
		int count=0;
		String[] fieldnames_list = fieldnames.split(",");
		for (String fieldname : fieldnames_list) {
			field_obj = LogicCheckUtils.getFieldValue(entity, fieldname.trim());
			if (field_obj == null || isMetadataField(field_obj)) continue;  // ignore metadata values during the counting
			field_value = field_obj.toString();
			if (isValueInSet(field_value, fieldvalues))
				count++;
		}
		
		String count_as_string = String.valueOf(count);
		return isValueInSet(count_as_string, fieldvalues_alt);			
	}
}
