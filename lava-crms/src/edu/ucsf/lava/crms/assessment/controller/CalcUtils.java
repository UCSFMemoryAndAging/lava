package edu.ucsf.lava.crms.assessment.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Months;

// common calculation methods to use in an instrument's calculate() function, such as adding and dividing numbers
// within the context of negative numbers usually representing missing values that can't be calculated

public class CalcUtils {
		
	public static final int ERROR_CODE_CANNOT_CALCULATE = -5;
	
	// if any number is null or negative, return ERROR_CODE_CANNOT_CALCULATE
	public static Double add(Number[] nums) {
		Double sum = new Double(0);
		for (int i=0; i<nums.length; i++) {
			if (nums[i]==null || nums[i].doubleValue()<0) return new Double(ERROR_CODE_CANNOT_CALCULATE);
			sum += nums[i].doubleValue();
		}
		return sum;
	}
	
	// excludes any negative values in the sum
	public static Double add(Number[] nums, Boolean ignoreNegatives) {
		Double sum = new Double(0);
		for (int i=0; i<nums.length; i++) {
			if (nums[i]==null || (ignoreNegatives && nums[i].doubleValue()<0)) continue;
			sum += nums[i].doubleValue();
		}
		return sum;
	}
	
	// uncalculable are numbers that if encountered should return ERROR_CODE_CANNOT_CALCULATE
	public static Double add(Number[] nums, Number[] uncalculable) {
		for (int i=0; i<nums.length; i++) {
			for (int j=0; j<uncalculable.length; j++) {
				if (nums[i]==null || nums[i].doubleValue()==uncalculable[j].doubleValue()) return new Double(ERROR_CODE_CANNOT_CALCULATE);
			}
		}
		return add(nums);
	}
	
	// ignores specified numbers in calculating a sum. useful for values that should obviously ignored like "-6" (logical skip) values or "9" (n/a) values
	public static Double addWithIgnore(Number[] nums, Number[] ignore) {
		Double sum = new Double(0);
		for (int i=0; i<nums.length; i++) {
			boolean ignoreNum = false;
			for (int j=0; j<ignore.length; j++) {
				if (nums[i]==null || ignore[j]==null) continue;
				if (nums[i].doubleValue()==ignore[j].doubleValue()){
					ignoreNum=true;
				}
			}
			if(!ignoreNum){
				if (nums[i]==null || nums[i].doubleValue()<0) return new Double(ERROR_CODE_CANNOT_CALCULATE);
				sum += nums[i].doubleValue();
			}
		}
		return sum;
	}	
	
	public static Double mean(Number[] nums) {
		Double sum = new Double(0);
		int count = 0;
		for (int i=0; i<nums.length; i++) {
			if (nums[i]==null || nums[i].doubleValue()<0) return new Double(ERROR_CODE_CANNOT_CALCULATE);
			count++;
			sum += nums[i].doubleValue();
		}
		return sum/count;
	}
	
	public static Double mean(Number[] nums, Boolean ignoreNegatives) {
		Double sum = new Double(0);
		int count = 0;
		for (int i=0; i<nums.length; i++) {
			if (nums[i]==null || (ignoreNegatives && nums[i].doubleValue()<0)) continue;
			count++;
			sum += nums[i].doubleValue();
		}
		return sum/count;
	}	
	
	public static Short reverseScore(Short num, int reverse) {
		if (num==null || num.shortValue()<0) return null;
		return (short) (reverse-num.intValue());
	}
	
	public static Double multiply(Number[] nums) {
		if (nums[0]==null) return new Double(ERROR_CODE_CANNOT_CALCULATE);
		Double product = nums[0].doubleValue();
		if (product.doubleValue()<0) return new Double(ERROR_CODE_CANNOT_CALCULATE);
		for (int i=1; i<nums.length; i++) {
			if (nums[i]==null || nums[i].doubleValue()<0) return new Double(ERROR_CODE_CANNOT_CALCULATE);
			product = product * nums[i].doubleValue();
		}
		return product;
	}	
	
	public static Double divide(Number dividend, Number divisor) {
		if (dividend==null || divisor==null || dividend.doubleValue()<0 || divisor.doubleValue()<0) return new Double(ERROR_CODE_CANNOT_CALCULATE);
		return dividend.doubleValue() / divisor.doubleValue();
	}
	
	// divideByZero is the number to return if the divisor is zero; aka a code for infinity
	public static Double divide(Number dividend, Number divisor, Number divideByZero) {
		if (divisor.intValue()==0) return divideByZero.doubleValue();
		return divide(dividend, divisor);
	}
	
	// counts how many numbers in 'nums' are not null and greater or equal to 0
	public static Short count(Number[] nums) {
		Short count = (short) 0;
		for (int i=0; i<nums.length; i++) {
				if (nums[i]==null || nums[i].doubleValue()<0) continue;
				count++;
		}
		return count;
	}
	
	// counts how many numbers in 'nums' are in the range specified by 'range'
	public static Short count(Number[] nums, Number[] range) {
		Short count = (short) 0;
		for (int i=0; i<nums.length; i++) {
			for (int j=0; j<range.length; j++) {
				if (nums[i]==null || range[j]==null) continue;
				if (range[j].doubleValue()==nums[i].doubleValue())
					count++;
			}
		}
		return count;
	}
	
	public static String neuropsych_t_score_range(Number tscore) {
		if (tscore==null) return null;
		short num = tscore.shortValue();
		if (num>=20 && num<=30) return "Impaired";
		else if (num>=31 && num <=36) return "Borderline";
		else if (num>=37 && num <=43) return "Low Average";
		else if (num>=44 && num <=56) return "Average";
		else if (num>=57 && num <=63) return "High Average";
		else if (num>=64 && num <=69) return "Superior";
		else if (num>=70 && num <=80) return "Very Superior";
		return null;
	}
	
	public static Integer getMonthsBetween(Date beginDate, Date endDate) {
		if (beginDate==null || endDate==null) return null;
		DateTime begin = new DateTime(beginDate);
		DateTime end = new DateTime(endDate);
		int months = Months.monthsBetween(begin, end).getMonths();
		return months;
	}
}
