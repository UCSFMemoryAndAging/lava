package edu.ucsf.lava.crms.assessment.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CalcUtils {
	
	// common calculation methods to use in an instrument's calculate() function, such as adding and dividing numbers
	// within the context of negative numbers usually representing missing values that can't be calculated
	
	public static final int ERROR_CODE_CANNOT_CALCULATE = -5;
	
	public static Double add(Number[] nums) {
		Double sum = new Double(0);
		for (int i=0; i<nums.length; i++) {
			if (nums[i]==null || nums[i].doubleValue()<0) return new Double(ERROR_CODE_CANNOT_CALCULATE);
			sum += nums[i].doubleValue();
		}
		return sum;
	}
	
	// uncalculable are numbers that if encountered should return a "-5" cannot calculate error code
	public static Double add(Number[] nums, Number[] uncalculable) {
		for (int i=0; i<nums.length; i++) {
			for (int j=0; j<uncalculable.length; j++) {
				if (nums[i]==null || nums[i].doubleValue()==uncalculable[j].doubleValue()) return new Double(ERROR_CODE_CANNOT_CALCULATE);
			}
		}
		return add(nums);
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

}
