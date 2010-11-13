package edu.ucsf.lava.core.dao.hibernate;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import edu.ucsf.lava.core.dao.LavaDaoParam;
import edu.ucsf.lava.core.type.LavaDateUtils;

/**
 * Utility class to abstract the complexity of the boolean query logic associated 
 * with querying date/times stored in separate date and time columns in the underlying database
 * 
 * @author jhesse
 *
 */
public class LavaDateTimeHibernateCriteriaUtils {

	
	
	
	public static Criterion daoOverlaps(String startDatePropertyName, String startTimePropertyName, String endDatePropertyName, String endTimePropertyName,
			Date startDateParam, Time startTimeParam, Date endDateParam, Time endTimeParam) {
	
		Criterion criterion = Restrictions.or(
									Restrictions.or(
											daoBetween(startDatePropertyName, startTimePropertyName, endDatePropertyName, endTimePropertyName, startDateParam, startTimeParam),
											daoBetween(startDatePropertyName, startTimePropertyName, endDatePropertyName, endTimePropertyName, endDateParam, endTimeParam)
											),
									Restrictions.or(
											daoBetween(startDatePropertyName, startTimePropertyName, startDateParam, startTimeParam, endDateParam, endTimeParam),	
											daoBetween(endDatePropertyName, endTimePropertyName, startDateParam, startTimeParam, endDateParam, endTimeParam)
											)
									);
		return criterion;
	}
	
		
	/**
	 * Create a querty criterion to see if the date and tiem params are between the start and end dates/time properties
	 * 
	 * @param startDatePropertyName 
	 * @param startTimePropertyName
	 * @param endDatePropertyName
	 * @param endTimePropertyName
	 * @param dateParam
	 * @param timeParam
	 * @return
	 */
	public static Criterion daoBetween(String startDatePropertyName, String startTimePropertyName, String endDatePropertyName, String endTimePropertyName,
				Date dateParam, Time timeParam) {
		
		/**
		 * conditions 
		 * 1) dbStartDate/Time  <= date / time params
		 * AND
		 * 2) dbEndDate/Time >= date / time params
		 * 
		 * 
		 */
	Criterion criterion = Restrictions.and(
								daoLessThan(startDatePropertyName, startTimePropertyName, LavaDateUtils.getDatePart(dateParam), (timeParam == null)?LavaDateUtils.getDayEndTime():timeParam),
								daoGreaterThan(endDatePropertyName, endTimePropertyName, LavaDateUtils.getDatePart(dateParam), (timeParam == null)? LavaDateUtils.getDayStartTime():timeParam)
								);
			return criterion;
		
	}
				
	/**
	 * Create a query criterion to see if the date/time represented by the db properties is between the start and end dates.  This method
	 * ignores any time portions of the dates passed in and uses the beginning of the start day (00:00:00) and the end of the end day (23:59:59) for the 
	 * times.
	 * @param datePropertyName
	 * @param timePropertyName
	 * @param startDateParam
	 * @param endDateParam
	 * @return
	 */
	public static Criterion daoBetween(String datePropertyName, String timePropertyName, Date startDateParam, Date endDateParam) {
		return daoBetween(datePropertyName,timePropertyName,LavaDateUtils.getDatePart(startDateParam),LavaDateUtils.getDayStartTime(),LavaDateUtils.getDatePart(endDateParam),LavaDateUtils.getDayEndTime());
	}
		
	/**
	 * Create a query criterion  to see if the date/time respresented by the db properties is between the start and end params passed in.
	 * If time params are null, they are replaced with the start of the day (00:00:00 for the start time and the end of the day (23:59:59) for the end Time
	 * 
	 * @param datePropertyName  
	 * @param timePropertyName
	 * @param startDateParam The start date (time part ignored)
	 * @param startTimeParam The start time (null defaults to 00:00:00)
	 * @param endDateParam The end date (time part ignores)
	 * @param endTimeParam The end time (null defaults to 23:59:59)
	 * @return
	 */
	public static Criterion daoBetween(String datePropertyName, String timePropertyName, Date startDateParam, Time startTimeParam, Date endDateParam, Time endTimeParam) {
			/**
			 * conditions 
			 * 1) dbDateTime >= startDateTimeParams
			 * AND
			 * 2) dbDateTime <= endDateTimeParams
			 * 
			 */
		Criterion criterion = Restrictions.and(
									daoGreaterThan(datePropertyName, timePropertyName, LavaDateUtils.getDatePart(startDateParam), (startTimeParam == null)?LavaDateUtils.getDayStartTime():startTimeParam),
									daoLessThan(datePropertyName, timePropertyName, LavaDateUtils.getDatePart(endDateParam), (endTimeParam == null)? LavaDateUtils.getDayEndTime():endTimeParam)
									);
				return criterion;
		
	}

	/**
	 * Create a query criterion to check the equality of the date and time passed in to the properties.  Handles nulls (e.g. if null timeParam, routine checks to 
	 * see if the timeProperty is null). 
	 * 
	 * @param datePropertyName
	 * @param timePropertyName
	 * @param dateParam  The date to check (time portion is ignored)
	 * @param timeParam  The time to check
	 * @return
	 */
	public static Criterion daoEquality(String datePropertyName, String timePropertyName, Date dateParam, Time timeParam) {
		/**
		 * conditions
		 * 1) dateParam = dbDate 
		 * AND 
		 * 2) timeParam = dbTime
		 * 
		 */
		Criterion dateCriterion = (dateParam == null) ? Restrictions.isNull(datePropertyName) : Restrictions.eq(datePropertyName, LavaDateUtils.getDatePart(dateParam));
		Criterion timeCriterion = (timeParam == null) ? Restrictions.isNull(timePropertyName) : Restrictions.eq(timePropertyName, timeParam);
		
		Criterion criterion = Restrictions.and(dateCriterion,timeCriterion);
		
		return criterion;
	}

	/**
	 *  Create a query criterion  to see if the db property is greater than or equal to the params passed in.
	 * @param datePropertyName 
	 * @param timePropertyName
	 * @param dateParam The date to check (time part ignored)
	 * @param timeParam The time to check (null defaults to 00:00:00)
	 * @return
	 */
	public static Criterion daoGreaterThanOrEqual(String datePropertyName, String timePropertyName, Date dateParam, Time timeParam) {
			
			/**
			 * conditions
			 * 1) dateParam = dbDate AND timeParam >= dbTime
			 * or
			 * 2) dateParam > dbDate  
			 */
			Date datePartOnly = LavaDateUtils.getDatePart(dateParam);

			Criterion criterion = Restrictions.or(
										Restrictions.gt(datePropertyName,datePartOnly),
										Restrictions.and(
												Restrictions.eq(datePropertyName, datePartOnly),
												Restrictions.ge(timePropertyName,(timeParam==null)?LavaDateUtils.getDayStartTime():timeParam)
												)
										);
			return criterion;
	}

	/**
	 *  Create a query criterion to see if the db property is greater than the params passed in.
	 * @param datePropertyName 
	 * @param timePropertyName
	 * @param dateParam The date to check (time part ignored)
	 * @param timeParam The time to check (null defaults to 00:00:00)
	 * @return
	 */
	public static Criterion daoGreaterThan(String datePropertyName, String timePropertyName, Date dateParam, Time timeParam) {
		/**
		 * conditions
		 * 1) dateParam = dbDate AND timeParam > dbTime
		 * or
		 * 2) dateParam > dbDate  
		 */
		Date datePartOnly = LavaDateUtils.getDatePart(dateParam);

		Criterion criterion = Restrictions.or(
									Restrictions.gt(datePropertyName,datePartOnly),
									Restrictions.and(
											Restrictions.eq(datePropertyName, datePartOnly),
											Restrictions.gt(timePropertyName,(timeParam==null)?LavaDateUtils.getDayStartTime():timeParam)
											)
									);
		return criterion;
		}
	
	
	/**
	 *  Create a query criterion to see if the db property is less than or equal to the params passed in.
	 * @param datePropertyName 
	 * @param timePropertyName
	 * @param dateParam The date to check (time part ignored)
	 * @param timeParam The time to check (null defaults to 23:59:59)
	 * @return
	 */
	public static Criterion daoLessThanOrEqual(String datePropertyName, String timePropertyName, Date dateParam, Time timeParam) {
		/**
		 * conditions
		 * 1) dateParam = dbDate AND timeParam <= dbTime
		 * or 
		 * 2) dateParam < dbDate  
		 */
		
		Date datePartOnly = LavaDateUtils.getDatePart(dateParam);
		
		Criterion criterion = Restrictions.or(
									Restrictions.lt(datePropertyName,datePartOnly),
									Restrictions.and(
											Restrictions.eq(datePropertyName, datePartOnly),
											Restrictions.le(timePropertyName,(timeParam==null)?LavaDateUtils.getDayEndTime():timeParam)
											)
									);
		return criterion;

	}

	/**
	 *  Create a query criterion to see if the db property is less than the params passed in.
	 * @param datePropertyName 
	 * @param timePropertyName
	 * @param dateParam The date to check (time part ignored)
	 * @param timeParam The time to check (null defaults to 23:59:59)
	 * @return
	 */
	public static Criterion daoLessThan(String datePropertyName, String timePropertyName, Date dateParam, Time timeParam) {
		/**
		 * conditions
		 * 1) dateParam = dbDate AND timeParam < dbTime
		 * or 
		 * 2) dateParam < dbDate  
		 */
		Date datePartOnly = LavaDateUtils.getDatePart(dateParam);
		
		Criterion criterion = Restrictions.or(
									Restrictions.lt(datePropertyName,datePartOnly),
									Restrictions.and(
											Restrictions.eq(datePropertyName, datePartOnly),
											Restrictions.lt(timePropertyName,(timeParam==null)?LavaDateUtils.getDayEndTime():timeParam)
											)
									);
		return criterion;
	}

	/**
	 * Create a criterion to check that the db properties are not null.  If either date part or time part is not null 
	 * then not null. 
	 * @param datePropertyName
	 * @param timePropertyName
	 * @return
	 */
	public static Criterion daoNotNull(String datePropertyName, String timePropertyName) {
		/**
		 * Conditions
		 * dbDate not null 
		 * or 
		 * dbTime not null
		 */
		Criterion criterion = Restrictions.or(
				Restrictions.isNotNull(datePropertyName),
				Restrictions.isNotNull(timePropertyName)
				);
		return criterion;
	}
	/**
	 * Create a criterion to check that the db properties is null.  If both date part and time part are null 
	 * then is null. 
	 * @param datePropertyName
	 * @param timePropertyName
	 * @return
	 */
	public static Criterion daoNull(String datePropertyName, String timePropertyName) {
		/**
		 * Conditions
		 * dbDate is  null 
		 * and
		 * dbTime is null
		 */
		Criterion criterion = Restrictions.and(
				Restrictions.isNull(datePropertyName),
				Restrictions.isNull(timePropertyName)
				);
		return criterion;

	
	}
	
	
	
}
