package edu.ucsf.lava.core.list.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Comparator;

// this class is used for elements of a List intended for viewing
// where each element has a visual label, as well as a value used
// when the element is selected from the list

public class LabelValueBean implements Comparable<LabelValueBean>, Serializable {
	private String label;
	private String value;
	private Integer orderIndex;
	
	public LabelValueBean() {}
	
	public LabelValueBean(LabelValueBean lvb) {
		this.orderIndex = lvb.getOrderIndex();
		this.label = lvb.getLabel();
		this.value = lvb.getValue();
	}

	
	public LabelValueBean(String label, String value) {
		this.label = label;
		this.value = value;
	}
	
	// typically used when the value is an id
	public LabelValueBean(String label, Long valueAsLong) {
		this.label = label;
		this.value = valueAsLong.toString();
	}
	
	// constructor for queries that also retrieve the orderIndex column to be used for 
	// sorting after the query
	public LabelValueBean(String label, String value, Integer orderIndex) {
		this.orderIndex = orderIndex;
		this.label = label;
		this.value = value;
	}

	// constructor for decimal values that need to be converted to a specific string format
	// the designator parameter is purely used to distinguish this constructor from the others
	// 
	// the reason this constructor is needed is to convert decimal values to String in the
	// same way that Spring MVC converts decimal values of a command (domain) object to
	// String, so that the Spring converted decimal will exactly match the list converted
	// decimal in an HTML select box. there must be an exact string match in order for the
	// Spring converted decimal representing the value of a domain property to be selected
	// in the select box (when an entity is saved, the HTML request parameters representing 
	// decimal values are converted to floating point variables in the domain (command) 
	// object, and these in turn are passed to the database as query parameters for decimal 
	// database fields. when an entity is retrieved, these decimal values are retrieved as part 
	// of the Spring MVC command object, and converted to String values for use by HTML. this
	// conversion is done by Spring, and generally includes a sign and one fractional digit
	// (because the range of decimal values used thus far just have one fractional digit))
	//
	// by default, this was not happening because the String values in the valueKey column 
	// of the ListValues table were not correctly representing decimal values in the 
	// following ways:
	// 1) .0 should be 0.0, .5 should be 0.5
	// 2) when the standard or skip error codes are included in such a list, these codes need
	//    to be treated as decimals, and their string valueKey value need to represent
	//    decimals but do not; they have values of -9,-8, etc. instead of -9.0, -8.0
	// both problems are solved by this constructor (note: there is a decimal version of the
	// query to obtain standard and skip error codes which use this constructor)
	//
	// so here we have valueKey as a string database field incorrectly representing
	// decimal values. in order to correct this, the string field needs to first be converted
	// into a decimal varible (Float) so that it can then be formatted into a string
	// properly representing the decimal value
	//
	// note: alternatively, the data in the database could have been modified to conform
	//       but since this data is shared between Lava and LavaWeb, do not want to modify
	//       any reference data like this. also, do not want to have an integral and decimal
	//       copy of standard error codes, skip error codes and other lists, as this is 
	//       basically duplicates of the same data, which is undesireable at the database level
	public LabelValueBean(String label, String value, double designator) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumFractionDigits(1);
		Float tempFloat = Float.valueOf(value);
		this.value = numberFormat.format(tempFloat);
		this.label = label;
	}

	public String getLabel() {return this.label;}
	public void setLabel(String label) {this.label = label;}
	
	public String getValue() {return this.value;}
	public void setValue(String value) {this.value = value;}
	
	public Integer getOrderIndex() {return this.orderIndex;}
	public void setOrderIndex(Integer orderIndex) {this.orderIndex = orderIndex;}
	
	/* 
	 * Compares based on the "label" field (natural ordering).
	 */
	public int compareTo(LabelValueBean anotherLabelValue) throws ClassCastException {
	    return label.compareTo(anotherLabelValue.getLabel()==null ? "" : anotherLabelValue.getLabel());
	}
	
	
	/*
	 * Compares based on the "label" field.
	 * 
	 * note: this is an anonymous inner class, not a static inner class, so there
	 *       is no "class" and class name specified. valueComparator is the name 
	 *       of a static member field of the LabelValueBean class
	 */
	public static Comparator<LabelValueBean> labelComparator = new Comparator<LabelValueBean>() {
	    public int compare(LabelValueBean labelValueBean1, LabelValueBean labelValueBean2) {
			return labelValueBean1.getLabel().compareTo(labelValueBean2.getLabel());   
		}
	};

	
	
	/*
	 * Compares based on the "value" field.
	 * 
	 * note: this is an anonymous inner class, not a static inner class, so there
	 *       is no "class" and class name specified. valueComparator is the name 
	 *       of a static member field of the LabelValueBean class
	 */
	public static Comparator<LabelValueBean> valueComparator = new Comparator<LabelValueBean>() {
	    public int compare(LabelValueBean labelValueBean1, LabelValueBean labelValueBean2) {
			return labelValueBean1.getValue().compareTo(labelValueBean2.getValue());   
		}
	};

	
	/*
	 * Compares based on the "value" field as a numeric integer value.
	 * 
	 * note: this is an anonymous inner class, not a static inner class, so there
	 *       is no "class" and class name specified. valueComparator is the name 
	 *       of a static member field of the LabelValueBean class
	 */
	public static Comparator<LabelValueBean> valueNumericComparator = new Comparator<LabelValueBean>() {
	    public int compare(LabelValueBean labelValueBean1, LabelValueBean labelValueBean2) {
			Integer value1 = null, value2 = null;
			try {
				value1 = Integer.valueOf(labelValueBean1.getValue());
				value2 = Integer.valueOf(labelValueBean2.getValue());
			}
			catch (NumberFormatException nfe) {
				//				logger.error("error converting String to Integer in LabelValueBean valueNumericComparator");
				// throw ...
			}
			return value1.compareTo(value2);   
		}
	};


	/*
	 * Compares based on the "value" field as a numeric decimal value.
	 * 
	 * note: this is an anonymous inner class, not a static inner class, so there
	 *       is no "class" and class name specified. valueComparator is the name 
	 *       of a static member field of the LabelValueBean class
	 */
	public static Comparator<LabelValueBean> valueDecimalComparator = new Comparator<LabelValueBean>() {
	    public int compare(LabelValueBean labelValueBean1, LabelValueBean labelValueBean2) {
			Float value1 = null, value2 = null;
			try {
				value1 = Float.valueOf(labelValueBean1.getValue());
				value2 = Float.valueOf(labelValueBean2.getValue());
			}
			catch (NumberFormatException nfe) {
				//				logger.error("error converting String to Float in LabelValueBean valueDecimalComparator");
				// throw ...
			}
			return value1.compareTo(value2);   
		}
	};
	

	/*
	 * Compares based on the "order" field.
	 * 
	 * note: this is an anonymous inner class, not a static inner class, so there
	 *       is no "class" and class name specified. valueComparator is the name 
	 *       of a static member field of the LabelValueBean class
	 */ 
	public static Comparator<LabelValueBean> orderIndexComparator = new Comparator<LabelValueBean>() {
	    public int compare(LabelValueBean labelValueBean1, LabelValueBean labelValueBean2) {
			return labelValueBean1.getOrderIndex().compareTo(labelValueBean2.getOrderIndex());
	    }
	};
	
	/*
	 * Compares based on the "order" field first then uses the label field to resolve any ties
	 * 
	 * note: this is an anonymous inner class, not a static inner class, so there
	 *       is no "class" and class name specified. valueComparator is the name 
	 *       of a static member field of the LabelValueBean class
	 */ 
	public static Comparator<LabelValueBean> orderIndexLabelComparator = new Comparator<LabelValueBean>() {
	    public int compare(LabelValueBean labelValueBean1, LabelValueBean labelValueBean2) {
			int orderResult = labelValueBean1.getOrderIndex().compareTo(labelValueBean2.getOrderIndex());
			if (orderResult == 0){
				return LabelValueBean.labelComparator.compare(labelValueBean1, labelValueBean2);
			} else{
				return orderResult;
			}
	    }
	};

	/*
	 * Compares based on the "order" field first then uses the value field (as string) to resolve any ties
	 * 
	 * note: this is an anonymous inner class, not a static inner class, so there
	 *       is no "class" and class name specified. valueComparator is the name 
	 *       of a static member field of the LabelValueBean class
	 */ 
	public static Comparator<LabelValueBean> orderIndexValueComparator = new Comparator<LabelValueBean>() {
	    public int compare(LabelValueBean labelValueBean1, LabelValueBean labelValueBean2) {
			int orderResult = labelValueBean1.getOrderIndex().compareTo(labelValueBean2.getOrderIndex());
			if (orderResult == 0){
				return LabelValueBean.valueComparator.compare(labelValueBean1, labelValueBean2);
			} else{
				return orderResult;
			}
	    }
	};
	
	/*
	 * Compares based on the "order" field first then uses the value field (as numeric) to resolve any ties
	 * 
	 * note: this is an anonymous inner class, not a static inner class, so there
	 *       is no "class" and class name specified. valueComparator is the name 
	 *       of a static member field of the LabelValueBean class
	 */ 
	public static Comparator<LabelValueBean> orderIndexValueNumericComparator = new Comparator<LabelValueBean>() {
	    public int compare(LabelValueBean labelValueBean1, LabelValueBean labelValueBean2) {
			int orderResult = labelValueBean1.getOrderIndex().compareTo(labelValueBean2.getOrderIndex());
			if (orderResult == 0){
				return LabelValueBean.valueNumericComparator.compare(labelValueBean1, labelValueBean2);
			} else{
				return orderResult;
			}
	    }
	};
	/*
	 * Compares based on the "order" field first then uses the value field (as decimal) to resolve any ties
	 * 
	 * note: this is an anonymous inner class, not a static inner class, so there
	 *       is no "class" and class name specified. valueComparator is the name 
	 *       of a static member field of the LabelValueBean class
	 */ 
	public static Comparator<LabelValueBean> orderIndexValueDecimalComparator = new Comparator<LabelValueBean>() {
	    public int compare(LabelValueBean labelValueBean1, LabelValueBean labelValueBean2) {
			int orderResult = labelValueBean1.getOrderIndex().compareTo(labelValueBean2.getOrderIndex());
			if (orderResult == 0){
				return LabelValueBean.valueDecimalComparator.compare(labelValueBean1, labelValueBean2);
			} else{
				return orderResult;
			}
	    }
	};
	
}
