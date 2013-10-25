package edu.ucsf.lava.crms.logiccheck.controller;

import java.util.List;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.logiccheck.controller.LogicCheckUtils;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.logiccheck.model.InstrumentLogicCheck;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;

public class CrmsLogicCheckUtils {
	// perform all logic check calculations on all instruments
	// Note: this would be needed when a new logic check definition had been added in the database
	public static void recalculateLogicchecks() {
		// it is hard to prevent doing all checks if we want to ensure all changes are flushed out
		// we could add a field to the logiccheck that could flag it as new, but then we
		// are relying on the database changer to set this flag.  This could easily be forgotten while
		// doing an UPDATE.  To be safe, do *all* checks again.  Ouch.
		// Another idea would be to add a Lava form for this table (admin only), so that it always gets
		// changed through it instead of directly in the database.  We could ensure flag is set correctly.
		
		// only Instrument logic checks are recalculated
		//LavaDaoFilter filter = InstrumentLogicCheck.MANAGER.newFilterInstance();
		List<InstrumentLogicCheck> logicchecks = (List<InstrumentLogicCheck>)InstrumentLogicCheck.MANAGER.get();
		for (InstrumentLogicCheck logiccheck : logicchecks) {
			logiccheck.activate();
		}
	}
	
	//TODO: this utility function can be moved to another class, since not logic check specific
	public static List<Instrument> getPriorInstrumentsOfSameType(Instrument instrument) {
		LavaDaoFilter filter = Instrument.MANAGER.newFilterInstance();
		filter.setAlias("patient","patient");
		filter.addDaoParam(filter.daoEqualityParam("patient.id",new Long(instrument.getPatient().getId())));
		filter.addDaoParam(filter.daoEqualityParam("projName", instrument.getProjName()));
		filter.addDaoParam(filter.daoEqualityParam("instrType", instrument.getInstrType()));
		filter.setAlias("visit","visit");
		filter.addDaoParam(filter.daoDateAndTimeLessThanParam("visit.visitDate", "visit.visitTime", instrument.getVisit().getVisitDate(), instrument.getVisit().getVisitTime()));
		filter.addSort("visit.visitDate", true);
		List<Instrument> instruments = Instrument.MANAGER.get(instrument.getClass(), filter);
		// now have a list of all patient's previous instruments of this type, ordered by visit date
		return instruments;
	}	
	
	public static Instrument getInstrumentOfSameVisit(Instrument instrumentWithSameVisit, String entityclassname) {
		EntityManager manager = CrmsManagerUtils.getLogicCheckManager().getEntityManager(entityclassname);
		if (manager == null) return null;
		LavaDaoFilter filter = manager.newFilterInstance();
		
		//LavaDaoFilter filter = InstrumentTracking.MANAGER.newFilterInstance();
		filter.setAlias("visit","visit");
		filter.addDaoParam(filter.daoEqualityParam("visit.id", new Long(instrumentWithSameVisit.getVisit().getId())));
		//filter.addDaoParam(filter.daoEqualityParam("instrType", new String(field2_instrType)));
		// grab the instrumenttracking data first so that we can calculate instrTypeEncoded
		//InstrumentTracking instr = (InstrumentTracking)InstrumentTracking.MANAGER.getOne(filter);
		//if (instr == null) return null;
		//LavaDaoFilter filter_data = Instrument.MANAGER.newFilterInstance();
		//filter_data.addDaoParam(filter_data.daoEqualityParam("id", instr.getId()));
		//return (Instrument)Instrument.MANAGER.getOne(CrmsManagerUtils.getInstrumentManager().getInstrumentClass(instr.getInstrTypeEncoded()), filter_data);
		return (Instrument) manager.getOne(filter);
	}
	
	public static boolean doOperator_InSetAnyPriorVisit(Instrument instrument,
			String[] checkDescDataAppend,
			String fieldname,
			String fieldvalues) throws Exception {
		//LOGIC: is there a prior visit whose field value is in fieldvalues?
		boolean result = false;
		
		checkDescDataAppend[checkDescDataAppend.length-1] += "[Prior values of: ";
			
		List<Instrument> prior_instruments = getPriorInstrumentsOfSameType(instrument);
		int index=0;
		for (Instrument prior_instr : prior_instruments) {
			if (index != 0) checkDescDataAppend[checkDescDataAppend.length-1] += ", ";
			index++;
			Object prior_obj = LogicCheckUtils.getFieldValue(prior_instr, fieldname);
			if (prior_obj == null) {
				checkDescDataAppend[checkDescDataAppend.length-1] += "&lt;empty&gt;";
				continue; // null values should not cause issues
			}
			String prior_value = (String)prior_obj.toString();
			checkDescDataAppend[checkDescDataAppend.length-1] += prior_value;
			
			if (LogicCheckUtils.isMetadataField(prior_value)) continue; // ignore prior metadata; move on
			result = result || LogicCheckUtils.isValueInSet(prior_value, fieldvalues);
		}
		checkDescDataAppend[checkDescDataAppend.length-1] += "]";
		
		return result;	
	}
	
	public static boolean doOperator_KnownValuePriorVisit(Instrument instrument,
			String[] checkDescDataAppend,
			String fieldname,
			String fieldvalues_unknown) throws Exception {
		//LOGIC: is there a prior visit who has known data?
		//Note: fieldvalues denote values that are considered "unknown" (not real)
		//Assume: fieldname refers to numeric field
		boolean result = false;
		boolean found_as_unknown;
		
		checkDescDataAppend[checkDescDataAppend.length-1] += "[Prior values of: ";
		
		String[] unknown_values = fieldvalues_unknown.split(",");
		
		List<Instrument> prior_instruments = getPriorInstrumentsOfSameType(instrument);
		int index=0;
		for (Instrument prior_instr : prior_instruments) {
			if (index != 0) checkDescDataAppend[checkDescDataAppend.length-1] += ", ";
			index++;
			Object prior_obj = LogicCheckUtils.getFieldValue(prior_instr, fieldname);
			if (prior_obj == null) {
				checkDescDataAppend[checkDescDataAppend.length-1] += "&lt;empty&gt;";
				continue; // not known
			}
			if (LogicCheckUtils.isMetadataField(prior_obj)) {
				checkDescDataAppend[checkDescDataAppend.length-1] += "&lt;empty&gt;";
				continue; // not known
			}
			Float prior_float = ((Number)prior_obj).floatValue();
			checkDescDataAppend[checkDescDataAppend.length-1] += prior_obj.toString();
			
			found_as_unknown = false;
			for (String unknown_value : unknown_values) {
				Float unknown_float = Float.parseFloat(unknown_value);
				if (unknown_float != null && unknown_float.equals(prior_float)) {
					found_as_unknown = true;
					break;
				}
			}
			if (found_as_unknown) continue;
			result = true;  // a known value was found
		}
		checkDescDataAppend[checkDescDataAppend.length-1] += "]";
		
		return result;	
	}
	
	public static boolean doOperator_TooDifferentThanAnyPriorVisit(Instrument instrument,
			String[] checkDescDataAppend,
			String fieldname,
			String fieldvalues,
			String fieldvalues_unknown) throws Exception {
		//LOGIC: is there a prior visit whose field value difference is larger than "fieldvalues"?
		boolean result=false;
		
		checkDescDataAppend[checkDescDataAppend.length-1] += "[Prior values of: ";
		
		Object field_obj = LogicCheckUtils.getFieldValue(instrument, fieldname);
		if (field_obj == null) throw new Exception("metadataFound"); 
		Float field_value = ((Number)field_obj).floatValue();
		if (LogicCheckUtils.isMetadataField(field_value)) throw new Exception("metadataFound");
		
		// we cannot do comparisons if current visit's value is unknown (else the negate logic is chaotic)
		String[] unknown_values = fieldvalues_unknown.split(",");
		for (String unknown_value : unknown_values) {
			Float unknown_float = Float.parseFloat(unknown_value);
			if (unknown_float != null && unknown_float.equals(field_value)) {
				throw new Exception("notAnIssue");  // "unknown" values are not to be compared
			}
		}
		
		// assume field1_values is one value
		Float acceptable_diff = Float.parseFloat(fieldvalues);
		
		// traverse all prior visits to check if any values differ by a greater amount than acceptable offset
		List<Instrument> prior_instruments = getPriorInstrumentsOfSameType(instrument);
		int index=0;
		boolean non_empty_value_found = false;
		for (Instrument prior_instr : prior_instruments) {
			if (index != 0) checkDescDataAppend[checkDescDataAppend.length-1] += ", ";
			index++;
			Object prior_obj = LogicCheckUtils.getFieldValue(prior_instr, fieldname);
			if (prior_obj == null) { 
				checkDescDataAppend[checkDescDataAppend.length-1] += "&lt;empty&gt;";
				// null values are ignored (not an issue)
			} else {
				Float prior_value = ((Number)prior_obj).floatValue();
				if (LogicCheckUtils.isMetadataField(prior_value)) { 
					checkDescDataAppend[checkDescDataAppend.length-1] += "&lt;empty&gt;";
					// metadata values are ignored (not an issue)
				} else {
					// when printing checkDescDataAppend, eliminate the decimal if possible (in case wasn't float to begin with)
					if (prior_value.floatValue() == Math.ceil(prior_value.floatValue()))
						checkDescDataAppend[checkDescDataAppend.length-1] += new Integer((int)Math.ceil(prior_value.floatValue())).toString();
					else
						checkDescDataAppend[checkDescDataAppend.length-1] += prior_value.toString();
					boolean equals_unknown = false;
					for (String unknown_value : unknown_values) {
						Float unknown_float = Float.parseFloat(unknown_value);
						if (unknown_float != null && unknown_float.equals(prior_value)) {
							equals_unknown = true;
							break;
						}
					}
					if (!equals_unknown) { // "unknown" values are ignored (not an issue)
						non_empty_value_found = true;
						result = (Math.abs(prior_value - field_value) > acceptable_diff);
					}
				}
			}
		}
		checkDescDataAppend[checkDescDataAppend.length-1] += "]";
		
		if (!non_empty_value_found) // then no real data was compared, so never an issue
			throw new Exception("notAnIssue");
		
		return result;
	}
	
	public static boolean doOperator_TooDifferentThanLastPriorVisit(Instrument instrument,
			String[] checkDescDataAppend,
			String fieldname,
			String fieldvalues,
			String fieldvalues_unknown) throws Exception {
		//LOGIC: does the last prior visit have a field value difference larger than "fieldvalues"?
		boolean result=false;
		
		checkDescDataAppend[checkDescDataAppend.length-1] += "[Last value of: ";
		
		Object field_obj = LogicCheckUtils.getFieldValue(instrument, fieldname);
		if (field_obj == null) throw new Exception("metadataFound"); 
		Float field_value = ((Number)field_obj).floatValue();
		if (LogicCheckUtils.isMetadataField(field_value)) throw new Exception("metadataFound");
		
		// we cannot do comparisons if current visit's value is unknown (else the negate logic is chaotic)
		String[] unknown_values = fieldvalues_unknown.split(",");
		for (String unknown_value : unknown_values) {
			Float unknown_float = Float.parseFloat(unknown_value);
			if (unknown_float != null && unknown_float.equals(field_value)) {
				throw new Exception("notAnIssue");  // "unknown" values are not to be compared
			}
		}
		
		// assume field1_values is one value
		Float acceptable_diff = Float.parseFloat(fieldvalues);
		
		// get last prior visit to check if its value differs by a greater amount than acceptable offset
		List<Instrument> prior_instruments = getPriorInstrumentsOfSameType(instrument);
		boolean non_empty_value_found = false;
		Instrument last_instr = null;
		for (Instrument prior_instr : prior_instruments) {
			last_instr = prior_instr;
		};
		
		Object prior_obj;
		if (last_instr==null)
			prior_obj = null;
		else
			prior_obj = LogicCheckUtils.getFieldValue(last_instr, fieldname);
		
		if (prior_obj == null) { 
			checkDescDataAppend[checkDescDataAppend.length-1] += "&lt;empty&gt;";
			// null values are ignored (not an issue)
		} else {
			Float prior_value = ((Number)prior_obj).floatValue();
			if (LogicCheckUtils.isMetadataField(prior_value)) { 
				checkDescDataAppend[checkDescDataAppend.length-1] += "&lt;empty&gt;";
				// metadata values are ignored (not an issue)
			} else {
				// when printing checkDescDataAppend, eliminate the decimal if possible (in case wasn't float to begin with)
				if (prior_value.floatValue() == Math.ceil(prior_value.floatValue()))
					checkDescDataAppend[checkDescDataAppend.length-1] += new Integer((int)Math.ceil(prior_value.floatValue())).toString();
				else
					checkDescDataAppend[checkDescDataAppend.length-1] += prior_value.toString();
				boolean equals_unknown = false;
				for (String unknown_value : unknown_values) {
					Float unknown_float = Float.parseFloat(unknown_value);
					if (unknown_float != null && unknown_float.equals(prior_value)) {
						equals_unknown = true;
						break;
					}
				}
				if (!equals_unknown) { // "unknown" values are ignored (not an issue)
					non_empty_value_found = true;
					result = (Math.abs(prior_value - field_value) > acceptable_diff);
				}
			}			
		}
		checkDescDataAppend[checkDescDataAppend.length-1] += "]";
		
		if (!non_empty_value_found) // then no real data was compared, so never an issue
			throw new Exception("notAnIssue");
		
		return result;
	}
	
	public static boolean doOperator_EqualsFirstValuePriorVisit(Instrument instrument,
			String[] checkDescDataAppend,
			String fieldname) throws Exception {
		//LOGIC: does the field value equal the first found value?  If no "real" prior values, never an issue.
		// Note: current visit metadata IS compared, even if prior metadata is ignored
		// e.g. once a year is specified in a visit, one may expect that year to be used in future visits
		boolean result = false;
		
		checkDescDataAppend[checkDescDataAppend.length-1] += "[First prior value is: ";
		
		Object thisvisit_value = LogicCheckUtils.getFieldValue(instrument, fieldname);
		
		// traverse all prior visits to find first visit that specifies a real value for this field
		List<Instrument> prior_instruments = getPriorInstrumentsOfSameType(instrument);
		Object prior_value = null;
		boolean found_prior_value = false;
		for (Instrument prior_instr : prior_instruments) {
			prior_value = LogicCheckUtils.getFieldValue(prior_instr, fieldname);
			if (prior_value == null) continue;  // NULL is not a real data value
			if (LogicCheckUtils.isMetadataField(prior_value)) continue; // metadata is not a real data value
			checkDescDataAppend[checkDescDataAppend.length-1] += prior_value.toString();
			found_prior_value = true;
			break;
		}
		
		if (found_prior_value) {
			result = (thisvisit_value != null)
			               && thisvisit_value.equals(prior_value);
			// note: if thisvisit_value is null/metadata, return false; do not throw exception
		} else {
			// no prior real value found, so nothing to compare to, so simply return without an issue (bypassing possible later negation)
			throw new Exception("notAnIssue");
		}
		
		checkDescDataAppend[checkDescDataAppend.length-1] += "]";
		
		return result;
	}
	
	public static boolean doOperator_LargestValuePriorVisit(Instrument instrument,
			String[] checkDescDataAppend,
			String fieldname,
			String fieldvalues_unknown) throws Exception {
		//LOGIC: is value larger than or equal to all prior values?  If no "real" prior values, never an issue.
		// Note: current visit metadata IS compared, even if prior metadata is ignored
		// e.g. once number of years is specified in a visit, one may expect that number to only increase in future years, if at all
		boolean result = false;
		boolean larger_value_found = false;
		
		checkDescDataAppend[checkDescDataAppend.length-1] += "[Prior values of: ";
		
		Object field_obj = LogicCheckUtils.getFieldValue(instrument, fieldname);
		if (field_obj == null) throw new Exception("metadataFound"); 
		Float field_value = ((Number)field_obj).floatValue();
		if (LogicCheckUtils.isMetadataField(field_value)) throw new Exception("metadataFound");
		
		// we cannot do comparisons if current visit's value is unknown (else the negate logic is chaotic)
		String[] unknown_values = fieldvalues_unknown.split(",");
		for (String unknown_value : unknown_values) {
			Float unknown_float = Float.parseFloat(unknown_value);
			if (unknown_float != null && unknown_float.equals(field_value)) {
				throw new Exception("notAnIssue");  // "unknown" values are not to be compared
			}
		}
		
		// traverse all prior visits to check if any values are larger
		List<Instrument> prior_instruments = getPriorInstrumentsOfSameType(instrument);
		int index=0;
		boolean non_empty_value_found = false;
		for (Instrument prior_instr : prior_instruments) {
			if (index != 0) checkDescDataAppend[checkDescDataAppend.length-1] += ", ";
			index++;
			Object prior_obj = LogicCheckUtils.getFieldValue(prior_instr, fieldname);
			if (prior_obj == null) { 
				checkDescDataAppend[checkDescDataAppend.length-1] += "&lt;empty&gt;";
				continue;	// null values are ignored
			}
			Float prior_value = ((Number)prior_obj).floatValue();
			if (LogicCheckUtils.isMetadataField(prior_value)) { 
				checkDescDataAppend[checkDescDataAppend.length-1] += "&lt;empty&gt;";
				continue;	// metadata values are ignored
			}
			// append value; use prior_obj instead of prior_value here in output, to avoid unnecessary decimals
			checkDescDataAppend[checkDescDataAppend.length-1] += prior_obj.toString();  
			boolean equals_unknown = false;
			for (String unknown_value : unknown_values) {
				Float unknown_float = Float.parseFloat(unknown_value);
				if (unknown_float != null && unknown_float.equals(prior_value)) {
					equals_unknown = true;
					break;
				}
			}
			if (equals_unknown) continue;  // "unknown" values are ignored
			
			non_empty_value_found = true;
			if (prior_value > field_value) {
				larger_value_found = true;
			}
		}
		checkDescDataAppend[checkDescDataAppend.length-1] += "]";
		
		if (!non_empty_value_found) // then no real data was compared, so never an issue
			throw new Exception("notAnIssue");
		
		result = !larger_value_found;
		
		return result;
	}
	
	
}
