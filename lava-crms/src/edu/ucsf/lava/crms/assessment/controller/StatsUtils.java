package edu.ucsf.lava.crms.assessment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.math.stat.descriptive.SynchronizedDescriptiveStatistics;

public class StatsUtils {
	
	// calculate the count, mean, median and std deviation for a property in a list where
	// a filter can be applied to limit which elements of the list are considered in the calculation
	//
	// requirements: a root property name is passed in which is used to contruct the summary
	//               property names on the summary entity, as follows:
	//               entityPropRoot + "C" = count property
	//               entityPropRoot + "A" = mean property
	//               entityPropRoot + "M" = median property
	//               entityPropRoot + "S" = std deviation property
	//
	// includeFilter (optional) a Map of property name/values which must be matched for a given
	//				detail record to be included in the calculation
	// excludeFilter (optional) a Map of property name/values which must be matched for a given
	//				detail record which has been included by the includeFilter to be excluded 
	// 				from the calculation

	// convenience method when there are no filters, i.e. all detail records should be included in the calculations
	public static void calcStdSummaryStats(Object summaryEntity, String entityPropRoot, List details, String prop, Class propClass) throws Exception {
		calcStdSummaryStats(summaryEntity, entityPropRoot, details, prop, propClass, new HashMap<String,Object>(), new HashMap<String,Object>());
	}
	
	public static void calcStdSummaryStats(Object summaryEntity, String entityPropRoot, List details, String prop, Class propClass, Map<String,Object> includeFilter, Map<String, Object> excludeFilter) throws Exception {
		SynchronizedDescriptiveStatistics stats = new SynchronizedDescriptiveStatistics();

		for (Object detail : details) {
			
			// process includeFilter (no filter properties means that each detail record should
			// be included in calculation, and filterMatch returns true in this case) 
			if (filterMatch(detail, includeFilter)) {

				// first check if there are an excludeFilter properties. if not, do not apply
				// the filter
				if (excludeFilter.size() == 0 || !filterMatch(detail, excludeFilter)) {
					
					// add the value to the stats structure
					if (propClass == Short.class) {
						stats.addValue((Short)PropertyUtils.getProperty(detail, prop));
					}
					else if (propClass == Integer.class) {
						stats.addValue((Integer)PropertyUtils.getProperty(detail, prop));
					}
					else if (propClass == Long.class) {
						stats.addValue((Long)PropertyUtils.getProperty(detail, prop));
					}
					else if (propClass == Float.class) {
						stats.addValue((Float)PropertyUtils.getProperty(detail, prop));
					}
				}
			}
		}
		
		// construct property names for the stats using the entityPropRoot, calc the stats and
		// and set the values
		
		// count
		Short count = (short) stats.getN();
		BeanUtils.setProperty(summaryEntity, entityPropRoot + "C", count);
		if (count > 0) {
			// mean
			BeanUtils.setProperty(summaryEntity, entityPropRoot + "A", stats.getMean());
			
			// median
			BeanUtils.setProperty(summaryEntity, entityPropRoot + "M", stats.getPercentile(50));
			
			// std dev
			BeanUtils.setProperty(summaryEntity, entityPropRoot + "S", stats.getStandardDeviation());
		}
	}
	
	// count the number of items in a list that match a filter
	// detailExcludeSet (optional) is a set of which detail indices should be excluded from the calculations
	
	// convenience method when there are no filters, i.e. all detail records should be included in the calculations
	public static void calcCountStats(Object summaryEntity, String entityProperty, List details) throws Exception {
		calcCountStats(summaryEntity, entityProperty, details, new HashMap<String, Object>(), new HashMap<String, Object>());
	}
	
	public static void calcCountStats(Object summaryEntity, String entityProperty, List details, Map<String,Object> includeFilter, Map<String, Object> excludeFilter) throws Exception {
		Short count = 0;
		for (Object detail : details) {
			// process includeFilter (no filter properties means that each detail record should
			// be included in calculation, and filterMatch returns true in this case) 
			if (filterMatch(detail, includeFilter)) {

				// first check if there are an excludeFilter properties. if not, do not apply
				// the filter
				if (excludeFilter.size() == 0 || !filterMatch(detail, excludeFilter)) {
					count++;
				}
			}
		}
		
		BeanUtils.setProperty(summaryEntity, entityProperty, count);
	}
	

	private static boolean filterMatch(Object detail, Map<String,Object> filter) throws Exception {
		// set flag true, so as soon as their is a mismatch, set flag false and exit loop
		boolean match = true;
		for (Map.Entry<String,Object> entry : filter.entrySet()) {
			String filterProp = entry.getKey();
			Object filterValue = entry.getValue();

			// run the current detail thru the filter(s). if any filter does not match, set the
			// match flag false and exit the loop

			if (PropertyUtils.getProperty(detail, filterProp) == null && filterValue != null) {
				match = false;
				break;
			}

			// ** for String, using matches method, so the filter value can be a regular expression 
			if (filterValue instanceof String) {
				if (!((String)PropertyUtils.getProperty(detail, filterProp)).matches((String)filterValue)) {
					match = false;
					break;
				}
			}
			else if (filterValue instanceof Short) {
				if (!((Short)filterValue).equals((Short)PropertyUtils.getProperty(detail, filterProp))) {
					match = false;
					break;
				}

			}
			else if (filterValue instanceof Long) {
				if (!((Long)filterValue).equals((Long)PropertyUtils.getProperty(detail, filterProp))) {
					match = false;
					break;
				}

			}
			else if (filterValue instanceof Integer) {
				if (!((Integer)filterValue).equals((Integer)PropertyUtils.getProperty(detail, filterProp))) {
					match = false;
					break;
				}

			}
			else if (filterValue instanceof Set) {
				Object setMember = ((Set)filterValue).iterator().next();
				if (setMember != null) {
					if (setMember instanceof String) {
						if (!((Set)filterValue).contains((String)PropertyUtils.getProperty(detail, filterProp))) {
							match = false;
							break;
						}
					}
					else if (setMember instanceof Short) {
						if (!((Set)filterValue).contains((Short)PropertyUtils.getProperty(detail, filterProp))) {
							match = false;
							break;
						}
					}
				}
				else {
					match = false;
					break;
				}
			}
		}
		
		return match;
	}
	
}
