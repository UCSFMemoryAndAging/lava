package edu.ucsf.lava.core.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for dealing with ascending date ranges 
 * 
 *  (Didn't use an existing datarange class because of 
 *  licensing implications)
 * 
 * @author jhesse
 *
 */
public class DateRange {
	
	
	protected Date start;
	protected Date end;
	
	public DateRange(){
		
	}
	public DateRange(Date start, Date end){
		this.start = start;
		this.end = end;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	
	/**
	 * has a start date
	 * @return
	 */
	public boolean hasStart(){
		return !(start==null);
	}
	
	/**
	 * has an end date
	 * @return
	 */
	public boolean hasEnd(){
		return !(end==null);
	}
	
	/**
	 * has both a start date and an end date and is 
	 * ascending (start before end)
	 * @return
	 */
	public boolean hasRange(){
		return hasStart() && hasEnd() && getStart().before(getEnd());
		
	}
	
	/**
	 * True if the date passed in is between start and end date
	 * inclusive of end points.
	 * 
	 * @param date
	 * @return
	 */
	public boolean contains(Date date){
		if(date==null || !hasRange()){return false;}
		if (date.after(getStart()) && date.before(getEnd())){return true;}
		if (date.equals(getStart()) || date.equals(getEnd())){return true;}
		
		
		return false;
	}
	
		
	/**
	 * If this range and the range passed in have valid ranges that overlap then return true.
	 * Overlapping is defined as a start date or end date of one range that is within the other range. 
	 * There is also a check to see if the overlap is simply that one range follows the other
	 * with the end time of one range = to the start time of the other (contiguous). 
	 * Contiguous ranges are not considered overlapping. 
	 * @param rangeIn
	 * @return
	 */
	public boolean overlaps(DateRange rangeIn){
		if(rangeIn==null || !rangeIn.hasRange() || !this.hasRange()) {return false;}
		
		if(this.contains(rangeIn.getStart())|| this.contains(rangeIn.getEnd())||
			rangeIn.contains(this.getEnd()) || rangeIn.contains(this.getStart())){
			
			if(!isContiguous(rangeIn)){
				return true;
			}
		}
		return false;
	}
		
	/**
	 * returns true if start and end are null
	 * @return
	 */
	public boolean isEmpty(){
		return (getStart()==null && getEnd()==null);
	}
	
	/**
	 * returns true if both ranges are empty or if
	 * both ranges have valid ranges with the same start and end dates
	 * @param rangeIn
	 * @return
	 */
	public boolean equals(DateRange rangeIn){
		if(rangeIn==null || rangeIn.isEmpty()){
			if(this.isEmpty()){
				return true;
			}else{
				return false;
			}
		}
		if(rangeIn.hasRange()&& this.hasRange()){
			if(this.getStart().equals(rangeIn.getStart()) &&
				this.getEnd().equals(rangeIn.getEnd())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if this range and the rangeIn share a start/end date
	 * @param rangeIn
	 * @return
	 */
	public boolean isContiguous(DateRange rangeIn){
		if(rangeIn!=null && rangeIn.hasRange() && this.hasRange()){
			if((this.getStart().equals(rangeIn.getEnd())) ||
			  (this.getEnd().equals(rangeIn.getStart()))){
				return true;
			}
		}
		return false;
	}
	/**
	 * returns true if the date range is within a single calendar day
	 * This is true if the end date is less than or equal to 12 Mindnight of
	 * day represented by start date. 
	 * 
	 * @return
	 */
	public boolean isWithinOneDay(){
		if (!hasRange()){return false;}
		
		Date midnight = getMidnightOfDate(getStart());
		if(getEnd().before(midnight) || getEnd().equals(midnight)){
			return true;
		}
		return false;
	}
	
	
	public static Date getMidnightOfDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH,1);
		calendar.set(Calendar.HOUR_OF_DAY,00);
		calendar.set(Calendar.MINUTE,00);
		calendar.set(Calendar.SECOND,00);
		return calendar.getTime();
	}
	/**
	 * Return the length of the date range converted to minutes
	 * @return
	 */
	public Long getRangeInMinutes(){
		return getRangeInMillis() / (60 * 1000);
	}
	
	/**
	 * Return the length of the date range in milliseconds
	 * @return
	 */
	public Long getRangeInMillis(){
		if(this.hasRange()){
			return new Long(getEnd().getTime() - getStart().getTime());
		}else{
			return new Long(0);
		}
	}
	
	/**
	 * returns a new daterange with the overlapping range of this
	 * range and the rangeIn
	 * @param rangeIn
	 * @return the overlapping range or an empty range if no overlap
	 */
	public DateRange getOverlap(DateRange rangeIn){
		DateRange overlap = new DateRange();
		
		if(!overlaps(rangeIn)){return overlap;}
		
		if(this.contains(rangeIn.getStart())){
			overlap.setStart(rangeIn.getStart());
		}else{
			overlap.setStart(this.getStart());
		}
		
		if(this.contains(rangeIn.getEnd())){
			overlap.setEnd(rangeIn.getEnd());
		}else{
			overlap.setEnd(this.getEnd());
		}
		return overlap;
			
	}
	
	
	/**
	 * Return the overlap between the range and the rangeIn in minutes.
	 * @param rangeIn
	 * @return 0 if no overlap
	 */
	public Long getOverlapInMinutes(DateRange rangeIn){
		return getOverlap(rangeIn).getRangeInMinutes();
	}
	
	
	/* Convenience Formatting Methods */
	
	protected String getStartDesc(DateFormat format){
		StringBuffer rangeDesc = new StringBuffer();
		if(hasStart()){
			rangeDesc.append(format.format(getStart()));
		}
		return rangeDesc.toString();
	}
	
	protected String getEndDesc(DateFormat format){
		StringBuffer rangeDesc = new StringBuffer();
		if(hasEnd()){
			rangeDesc.append(format.format(getEnd()));
		}
		return rangeDesc.toString();
	}
	
	protected String getRangeDesc(DateFormat dateFormat, DateFormat timeFormat, String separator){
		StringBuffer rangeDesc = new StringBuffer();
		if(hasStart()){
			if(dateFormat!=null){
				rangeDesc.append(dateFormat.format(getStart()));
			}
			if(timeFormat!=null){
				rangeDesc.append(timeFormat.format(getStart()));
			}
		}
		if(hasEnd()){
			rangeDesc.append(separator);
			
			if(dateFormat!=null && !isWithinOneDay()){
				rangeDesc.append(dateFormat.format(getEnd()));
			}
			if(timeFormat!=null){
				rangeDesc.append(timeFormat.format(getEnd()));
			}
		}
		return rangeDesc.toString();
	}
	
	public String getShortRangeDesc(){
		return getRangeDesc(new SimpleDateFormat("MM/dd/yyyy "),new SimpleDateFormat("h:mma")," - ");
		
	}
	
	public String getShortTimeDesc(){
		return getRangeDesc(null,new SimpleDateFormat("h:mma")," - ");
		
	}
	
	/*
	<c:choose>
	<c:when test="${range == 'All'}">
		All Dates
 	</c:when>
	<c:when test="${range == 'Day' && empty shortFormat}">
		<fmt:formatDate value="${beginDate}" pattern="EEEE  MMMM d yyyy"/>
 	</c:when>
	<c:when test="${range == 'Day' && not empty shortFormat}">
		<fmt:formatDate value="${beginDate}" pattern="MMM dd, yyyy"/>
 	</c:when>
 	<c:when test="${(range == 'Week' || range == 'Custom') && empty shortFormat}">
 	    <fmt:formatDate value="${beginDate}" pattern="EEEE  MMMM d yyyy"/> to <fmt:formatDate value="${endDate}" pattern="EEEE  MMMM d yyyy"/>
    </c:when>
 	<c:when test="${(range == 'Week' || range == 'Custom') && not empty shortFormat}">
 	    <fmt:formatDate value="${beginDate}" pattern="MMM dd, yyyy"/> - <fmt:formatDate value="${endDate}" pattern="MMM dd, yyyy"/>
    </c:when>
    <c:when test="${range == 'Month'}">
    	<fmt:formatDate value="${beginDate}" pattern="MMMM yyyy"/>
    </c:when>
</c:choose>        		
	*/
	
	
	
}
