package edu.ucsf.lava.crms.assessment.controller;

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
	public static void calcStdSummaryStats(Object summaryEntity, String entityPropRoot, List details, String prop, Class propClass, Map<String,Object> filter) throws Exception {
		SynchronizedDescriptiveStatistics stats = new SynchronizedDescriptiveStatistics();

		for (Object detail : details) {
			
			// run the current detail thru the filter(s). if any filter does not match, set the
			// match flag false and exit the loop
			boolean match = true;
			for (Map.Entry<String,Object> entry : filter.entrySet()) {
				String filterProp = entry.getKey();
				Object filterValue = entry.getValue();
				
				if (PropertyUtils.getProperty(detail, filterProp) == null) {
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
					// Set is assumed to be a Set of String, typically used to filter on all possible trial
					// name values for the "running" variable
					if (!((Set)filterValue).contains((String)PropertyUtils.getProperty(detail, filterProp))) {
						match = false;
						break;
					}
				}
			}
			
			if (match) {
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
	public static void calcCountStats(Object summaryEntity, String entityProperty, List details, Map<String,Object> filter) throws Exception {
		Short count = 0;
		for (Object detail : details) {
			
			// run the current detail thru the filter(s). if any filter does not match, set the
			// match flag false and exit the loop
			boolean match = true;
			for (Map.Entry<String,Object> entry : filter.entrySet()) {
				String filterProp = entry.getKey();
				Object filterValue = entry.getValue();
				
				if (PropertyUtils.getProperty(detail, filterProp) == null) {
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
					// Set is assumed to be a Set of String, typically used to filter on all possible trial
					// name values for the "running" variable
					if (!((Set)filterValue).contains((String)PropertyUtils.getProperty(detail, filterProp))) {
						match = false;
						break;
					}
				}
			}
			
			if (match) {
				count++;
			}
		}
		
		BeanUtils.setProperty(summaryEntity, entityProperty, count);
	}
	

}
