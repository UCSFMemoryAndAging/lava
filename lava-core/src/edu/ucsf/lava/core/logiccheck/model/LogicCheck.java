package edu.ucsf.lava.core.logiccheck.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.logiccheck.controller.LogicCheckUtils;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.core.logiccheck.model.LogicCheckIssue;


public class LogicCheck extends EntityBase {
	public static EntityManager MANAGER = new EntityBase.Manager(LogicCheck.class);
	
	protected String codeID; // unique to the uniqueness criteria of this logic check
	protected Byte enabled; // if not enabled, then will never be activated
	protected Byte isalert;  // is an alert (1) or error (0)
	protected String checkDesc;  // custom description to override calculated description
	protected String primaryLogic; // determines how to interpret fields for conditions 1 and 2
	
	protected String cond1operator;
	protected Byte cond1negate;
	protected String entity1classname;  // simple class name matches full name in LogicCheckEntityConfig; class must be specified in a bean
	protected String field1name;
	protected String field1itemNum;
	protected String field1values;
	protected String field1values_alt;
	
	protected String cond2operator;
	protected Byte cond2negate;
	protected String entity2classname;	// simple class name matches full name in LogicCheckEntityConfig; class must be specified in a bean
	protected String field2name;
	protected String field2itemNum;
	protected String field2values;
	protected String field2values_alt;

	protected Date activeDate;  // if NULL, logic check is inactive and ignored
	protected String notes;     // back-end notes
	
	public String getCodeID() {
		return codeID;
	}

	public void setCodeID(String codeID) {
		this.codeID = codeID;
	}

	public String getCheckCode(Long entityID) {
		// default behavior is to return codeID
		return getCodeID();
	}
	
	public Byte getEnabled() {
		return enabled;
	}

	public void setEnabled(Byte enabled) {
		this.enabled = enabled;
	}

	public Byte getIsalert() {
		return isalert;
	}

	public void setIsalert(Byte isalert) {
		this.isalert = isalert;
	}
	
	// convenience function
	public boolean isAlert() {
		return (this.isalert == null || this.isalert.equals((byte)0)) ? false : true;
	}
	
	public String getCheckDesc() {
		return checkDesc;
	}
	
	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}
	
	public String getPrimaryLogic() {
		return primaryLogic;
	}

	public void setPrimaryLogic(String primaryLogic) {
		this.primaryLogic = primaryLogic;
	}

	public Date getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}
	
	public String getCond1operator() {
		return cond1operator;
	}

	public void setCond1operator(String cond1operator) {
		this.cond1operator = cond1operator;
	}

	public Byte getCond1negate() {
		return cond1negate;
	}

	public void setCond1negate(Byte cond1negate) {
		this.cond1negate = cond1negate;
	}
	
	// convenience function
	public boolean isCond1negate() {
		return (this.cond1negate == null || this.cond1negate.equals((byte)0)) ? false : true;
	}

	public String getEntity1classname() {
		return entity1classname;
	}

	public void setEntity1classname(String entity1classname) {
		this.entity1classname = entity1classname;
	}

	public String getField1name() {
		return field1name;
	}

	public void setField1name(String field1name) {
		this.field1name = field1name;
	}

	public String getField1itemNum() {
		return field1itemNum;
	}

	public void setField1itemNum(String field1itemNum) {
		this.field1itemNum = field1itemNum;
	}

	public String getField1values() {
		return field1values;
	}

	public void setField1values(String field1values) {
		this.field1values = field1values;
	}

	public String getField1values_alt() {
		return field1values_alt;
	}

	public void setField1values_alt(String field1valuesAlt) {
		field1values_alt = field1valuesAlt;
	}

	public String getCond2operator() {
		return cond2operator;
	}

	public void setCond2operator(String cond2operator) {
		this.cond2operator = cond2operator;
	}

	public Byte getCond2negate() {
		return cond2negate;
	}

	public void setCond2negate(Byte cond2negate) {
		this.cond2negate = cond2negate;
	}
	
	// convenience function
	public boolean isCond2negate() {
		return (this.cond2negate == null || this.cond2negate.equals((byte)0)) ? false : true;
	}
	
	public String getEntity2classname() {
		return entity2classname;
	}

	public void setEntity2classname(String entity2classname) {
		this.entity2classname = entity2classname;
	}

	public String getField2name() {
		return field2name;
	}

	public void setField2name(String field2name) {
		this.field2name = field2name;
	}

	public String getField2itemNum() {
		return field2itemNum;
	}

	public void setField2itemNum(String field2itemNum) {
		this.field2itemNum = field2itemNum;
	}
	
	public String getField2values() {
		return field2values;
	}

	public void setField2values(String field2values) {
		this.field2values = field2values;
	}

	public String getField2values_alt() {
		return field2values_alt;
	}

	public void setField2values_alt(String field2valuesAlt) {
		field2values_alt = field2valuesAlt;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	protected String expandFieldValuesString(String values) {
		return expandFieldValuesString(values, false);
	}
	
	protected String expandFieldValuesString(String values, boolean negate) {
		if (values == null) return "";
		
		String[] tokens = values.split(",");
		String expanded_string = (negate ? "not ":"")+tokens[0];
		for (int x=1; x<tokens.length; x++)
			expanded_string += (negate ? " AND not " : " OR ") + tokens[x];

		return expanded_string;
	}
	
	public LogicCheckIssue createNewIssue() {
		// override to support another scope's entities
		return null;
	}
	
/* TQR:20120525 - not seen as needed any longer
	public boolean isEntityMatch(EntityBase entity) {
		// currently no core entities are supported
		return false;
	}
*/
	
	// return all entities matching this logic check definition (i.e. that it would apply to)
	public List<EntityBase> getMatchingEntities() {
		return null;
	}
	
	public void activate() {
		// do not activate disabled definitions; instead, ensure no issues currently exist for it
		boolean is_enabled = this.getEnabled() != null && !this.getEnabled().equals((byte)0);
		
		// ignore any already active definitions, but let through disabled definitions to flush out possible remaining issues
		if (this.getActiveDate() != null && is_enabled) return;

		if (is_enabled) {
			// set as active now so that if there are any saves that are done while this is being
			// run on all the instruments, then that save would include this logic check.  In the worst
			// case, the logic checks would be done multiple times on the same entity, which isn't a problem
			this.setActiveDate(new Date());
			this.save();
		} else {
			// ensure disabled checks are inactive
			if (this.getActiveDate() != null) {
				this.setActiveDate(null);
				this.save();
			}
			
			// as a shortcut, return early if a disabled check has no current issues
			// there is no need to go through all entities and update the summaries in this case
			// in this way, once disabled, time won't be spent considering this check over and over
			LavaDaoFilter filter = LogicCheckIssue.MANAGER.newFilterInstance();
			filter.setAlias("definition", "definition");
			filter.addDaoParam(filter.daoEqualityParam("definition.id", this.getId()));
			List<LogicCheckIssue> lcissues = (List<LogicCheckIssue>)LogicCheckIssue.MANAGER.get(filter);
			if (lcissues.size() == 0) return;  // no need to do more
		}
		
		List<LogicCheckIssue> lcissues;
		EntityBase entity_data;
					
		// apply this logic check definition to all matching entities.
		List<EntityBase> entities = this.getMatchingEntities();
		
		// note: cannot count on *all* logiccheckissues being there until we return from activate()

		if (entities != null) {
			boolean entity_lcissues_altered;
			for (EntityBase entity : entities) {
				entity_lcissues_altered = false;
				lcissues = (List<LogicCheckIssue>)entity.getLogicCheckIssues();
				if (is_enabled) {
					this.doLogicCheckMethod((EntityBase)entity, lcissues);
					entity_lcissues_altered = true;
				} else {
					// delete all issues of disabled definitions, and update summary for entity
					LogicCheckIssue lcissue;
					Iterator<LogicCheckIssue> iter = lcissues.iterator();
					while (iter.hasNext()) {
						lcissue = iter.next();
						if (lcissue.getDefinition().getId().equals(this.getId())) {
							iter.remove();
							lcissue.delete();
							entity_lcissues_altered = true;
						}						
					}	
				}

				if (entity_lcissues_altered) {
					// update the entities logic check summary fields that contain status
					LogicCheckUtils.updateLogicCheckStatus(entity, lcissues);
				}
			}
		}
		
	}
	
	public void doCheck_PersistLogicCheckIssue( 
					EntityBase entity,
					List lcissues,
					boolean isLogicIssue,
					boolean isValidDef,
					String[] checkDescDataAppend) {  // if an exception occurred during the check, then considered an invalidDef issue
		
		// using the parameters, check if error on this form.
		//   a) if not error and on error_list, remove
		//   b) if error and not on error_list, add
		boolean logiccheck_already_persisted = false;
		boolean lcissue_changed;
		if (lcissues != null) {
			for (LogicCheckIssue lcissue : (List<LogicCheckIssue>)lcissues) {
				if (lcissue.getDefinition().getId().equals((long)this.getId())) {
					logiccheck_already_persisted = true;
					if (!isLogicIssue) {
						// delete the entry
						lcissues.remove(lcissue);
						lcissue.delete();
					}
					if (isLogicIssue) {
						lcissue_changed = false;
						// ensure "invalidDef" flag set correctly (as code may have been changed since last time issue was determined)
						if ((isValidDef && (lcissue.getInvalidDef() != null && !lcissue.getInvalidDef().equals((byte)0)))
							||
							(!isValidDef && (lcissue.getInvalidDef() == null || lcissue.getInvalidDef().equals((byte)0)))) {
							// then they differ, so set it correctly and save
							lcissue.setInvalidDef(isValidDef ? (byte)0 : (byte) 1);
							lcissue_changed = true;
						}
						// ensure checkDescDataAppend is still the same, since (say) prior visit data can change outside of the workflow of this issue's entity
						String checkDescDataAppendValue = null;
						if (checkDescDataAppend != null && checkDescDataAppend.length > 0 && checkDescDataAppend[0].length() > 0)
							if (checkDescDataAppend[0].length() > 500)  // just in case
								checkDescDataAppendValue = checkDescDataAppend[0].substring(0,500);
							else
								checkDescDataAppendValue = checkDescDataAppend[0];
						if ((checkDescDataAppendValue==null && lcissue.getCheckDescDataAppend()!=null)
							||
							(checkDescDataAppendValue!=null && lcissue.getCheckDescDataAppend()==null)
							||
 							(checkDescDataAppendValue!=lcissue.getCheckDescDataAppend())) {
							lcissue.setCheckDescDataAppend(checkDescDataAppendValue);
							lcissue_changed = true;
						}

						if (lcissue_changed)
							lcissue.save();
					}
					break;  // already found, no use in traversing list further
				}			
			}
		}
		if (!logiccheck_already_persisted && isLogicIssue) {
			// add an entry
			LogicCheckIssue lcissue = createNewIssue();
			lcissue.setEntityID(entity);
			lcissue.setDefinition(this);
			// if a checkDescDataAppend was specified, add it here
			String checkDescDataAppendValue = null;
			if (checkDescDataAppend != null && checkDescDataAppend.length > 0 && checkDescDataAppend[0].length() > 0)
				if (checkDescDataAppend[0].length() > 500)  // just in case
					checkDescDataAppendValue = checkDescDataAppend[0].substring(0,500);
				else
					checkDescDataAppendValue = checkDescDataAppend[0];
			lcissue.setCheckDescDataAppend(checkDescDataAppendValue);
			// use the definition to determine if the check is an alert or error
			// initialize `verified` appropriately (should be null if not an alert)
			lcissue.setVerified(this.getIsalert() == null || this.getIsalert().equals((byte)0) ? null : (byte)0);
			lcissue.setInvalidDef(isValidDef ? (byte)0 : (byte)1);
			lcissue.save();
			// add to lcissues here so we don't have to query again
			lcissues.add(lcissue);
		}	
	}
	
	// when performing a logic check, always call this method, not doLogicCheckPrimaryLogic()
	public void doLogicCheckMethod(EntityBase entity, List lcissues) {
		boolean isLogicIssue = false;
		String[] checkDescDataAppend = new String[]{""};  // an array, but assuming only one entry; this is to pass it around as parameter
		try {
			isLogicIssue = doLogicCheckPrimaryLogic(entity, checkDescDataAppend);
		} catch (Exception ex) {
			// METADATA exception: an exception is thrown if metadata or "unknown" values were found in any significant values involved
			// in this logic.  Since a "negate" operator was used, we could not simply return "false" (meaning
			// no logic issue), as the "negate" would turn this into "true".  Instead, we *always* want to return
			// false, so we use an exception to skip all other processing.
			// CROSSFORM NEVER SAVED exception: same as above, but the significiant value was one in a form never saved before, so
			// we wish there to be *no* issue
			if (ex.getMessage() != null && ex.getMessage().equals("metadataFound")) {
				isLogicIssue = false; 
			} else if (ex.getMessage() != null && ex.getMessage().equals("dependentNeverSaved")) {
				isLogicIssue = false;  
			} else if (ex.getMessage() != null && ex.getMessage().equals("notAnIssue")) {  // provided to skip the operator negation
				isLogicIssue = false; 
			} else {  // invalidDef=true
				// Since we want to be aware of this bug, both as programmers (to fix) and as a user (to not assume
				// no logic check issue), add it as a logic check alert, yet a special one.  It will be persisted, so
				// we can easily check on any logic check code that are producing exceptions.  Since it is only an
				// alert (not an error), the user can still continue with the flow, yet knowing it may not be correct
				// This also provides us a easy way to validate testing, when we run the logic checks on all 
				// instruments (i.e. multiple combinations) in a staging environment before production
				doCheck_PersistLogicCheckIssue(entity, lcissues, true, false, null);
				return;
			}
		}
		// note that if a definition was never "handled", then it will remain as *not* a problem.  There will be no issues
		//   found for that definition.
		doCheck_PersistLogicCheckIssue(entity, lcissues, isLogicIssue, true, checkDescDataAppend);
		
	}
	
	
	// return true if a logic check issue was found
	public boolean doLogicCheckPrimaryLogic(EntityBase entity, String[] checkDescDataAppend) throws Exception {
		// override to support more primary logic
		boolean cond1_result;
		boolean cond2_result;
		
		
		// before doing any operator logic on fieldvalues, expand any "special variables" (such as "age")
		//   and evaluate any expressions
		String fieldvalues1_eval, fieldvalues1_alt_eval, fieldvalues2_eval, fieldvalues2_alt_eval;
		fieldvalues1_eval = this.evaluateFieldValuesExpression(this.getField1values(), entity, checkDescDataAppend);
		fieldvalues1_alt_eval = this.evaluateFieldValuesExpression(this.getField1values_alt(), entity, checkDescDataAppend);
		fieldvalues2_eval = this.evaluateFieldValuesExpression(this.getField2values(), entity, checkDescDataAppend);
		fieldvalues2_alt_eval = this.evaluateFieldValuesExpression(this.getField2values_alt(), entity, checkDescDataAppend);
		
		
		// Note: the primary logic of "OR" (disjunction) can be accomplished by either of the following:
		// (1) having separate logic checks (i.e. two different logic checks for the same field),
		// (2) if using the IN_SET operator, the possible fieldvalues are implicitly OR'ed by using commas  
		
		if (this.getPrimaryLogic().equals("CUSTOM")) {
			// if custom, then the instrument itself defines it
			return CoreManagerUtils.getLogicCheckManager().doLogicCheckPrimaryLogicCustom(entity, this, checkDescDataAppend);
			// entity.doLogicCheckPrimaryLogic(this, checkDescDataAppend);
		} else if (this.getPrimaryLogic().equals("COND1")) {
			cond1_result = this.doLogicCheckOperator(entity, this.getCond1operator(), this.getField1name(), fieldvalues1_eval, this.getEntity1classname(), fieldvalues1_alt_eval, checkDescDataAppend);
			if (this.isCond1negate()) cond1_result = !cond1_result;
			return cond1_result;
		} else if (this.getPrimaryLogic().equals("COND1_AND_COND2")) {
			cond1_result = this.doLogicCheckOperator(entity, this.getCond1operator(), this.getField1name(), fieldvalues1_eval, this.getEntity1classname(), fieldvalues1_alt_eval, checkDescDataAppend);
			if (this.isCond1negate()) cond1_result = !cond1_result;
			if (cond1_result == false) return false; // no need to check condition 2 in this case
			cond2_result = this.doLogicCheckOperator(entity, this.getCond2operator(), this.getField2name(), fieldvalues2_eval, this.getEntity2classname(), fieldvalues2_alt_eval, checkDescDataAppend);
			if (this.isCond2negate()) cond2_result = !cond2_result;
			return cond2_result;  // already know cond1_result == true
		} else if (this.getPrimaryLogic().equals("COND1_EQUALS_COND2") || this.getPrimaryLogic().equals("COND1_NOTEQUALS_COND2")) {
			// Note: There was a question whether to make COND1_NOTEQUALS_COND2 a separate primary logic type, or to expand our logiccheck columns to include
			// a "negatePrimaryLogic" field, of some sort.  It was decided to make it a separate primary logic type because the negation of COND1_AND_COND2
			// was non-intuitive (i.e. what would we expect to gain from negating COND1_AND_COND2?)  If it makes sense as logic in the future, then we can
			// consider the more flexible solution of adding a new negatePrimaryLogic field.
			cond1_result = this.doLogicCheckOperator(entity, this.getCond1operator(), this.getField1name(), fieldvalues1_eval, this.getEntity1classname(), fieldvalues1_alt_eval, checkDescDataAppend);
			if (this.isCond1negate()) cond1_result = !cond1_result;
			cond2_result = this.doLogicCheckOperator(entity, this.getCond2operator(), this.getField2name(), fieldvalues2_eval, this.getEntity2classname(), fieldvalues2_alt_eval, checkDescDataAppend);
			if (this.isCond2negate()) cond2_result = !cond2_result;
			return this.getPrimaryLogic().equals("COND1_EQUALS_COND2") ? (cond1_result == cond2_result)
			                                                           : (cond1_result != cond2_result);  
		}
		// ADD NEW PRIMARY LOGIC HERE

		// primary logic not defined so throw an exception so that invalidDef can be set
		throw new RuntimeException("Logic Check exception: invalid primaryLogic definition ("+this.getPrimaryLogic()+")");
	}
	
	// Calculate the logic check description, if possible.  This is what the user sees to describe a logic check that had an issue.
	// For consistency, use a period at the end.
	// This is only used if the logic check definition doesn't define a checkDesc
	public String getCheckDescCalculated() {
		String checkDescCalculated = "";
		if (this.getPrimaryLogic().equals("CUSTOM")) {
			checkDescCalculated = "Custom check."; // this should have been overridden by a checkDesc, but provide at least some text if checkDesc==null
		} else if (this.getPrimaryLogic().equals("COND1")) {
			checkDescCalculated = this.getCheckDescOperator(this.getCond1operator(), this.isCond1negate(), this.getField1name(), this.getField1itemNum(), this.getField1values(), this.getField1values_alt(), false) + ".";
		} else if (this.getPrimaryLogic().equals("COND1_AND_COND2")) {
			checkDescCalculated = this.getCheckDescOperator(this.getCond1operator(), this.isCond1negate(), this.getField1name(), this.getField1itemNum(), this.getField1values(), this.getField1values_alt(), false)
			       + ". "
			       + this.getCheckDescOperator(this.getCond2operator(), this.isCond2negate(), this.getField2name(), this.getField2itemNum(), this.getField2values(), this.getField2values_alt(), true)
			       + ".";
		} else if (this.getPrimaryLogic().equals("COND1_EQUALS_COND2") || this.getPrimaryLogic().equals("COND1_NOTEQUALS_COND2")) {
			checkDescCalculated = (this.getPrimaryLogic().equals("COND1_EQUALS_COND2") ? "Only one of the following conditions should be true: "
																					   : "The following conditions should be both true or both false: ")
			   + this.getCheckDescOperator(this.getCond1operator(), this.isCond1negate(), this.getField1name(), this.getField1itemNum(), this.getField1values(), this.getField1values_alt(), false)
		       + ". "
		       + this.getCheckDescOperator(this.getCond2operator(), this.isCond2negate(), this.getField2name(), this.getField2itemNum(), this.getField2values(), this.getField2values_alt(), true)
		       + ". ";
		} else {
			checkDescCalculated = "Undefined description of this primary logic.";
		}
		// ADD NEW PRIMARY LOGIC CHECK DESCRIPTIONS HERE
		
		// This string append comes from NACC error code descriptions, where if it is a real error (not an alert), then it says "This is not allowed."
		checkDescCalculated += (this.isAlert() ? "" : " This is not allowed.");
		return checkDescCalculated;
	}
	
	// fieldvalues can contain expressions to evaluate:
	// 1) special variables get replaced with their values (also add these to checkDescDataAppend)
	// 2) expressions, denoted by parentheses, also get replaced with their values
	public String evaluateFieldValuesExpression(String text, EntityBase entity, String[] checkDescDataAppend) throws Exception {

		if (text==null) return null; // nothing to evaluate
		
		// expand any "special variables"
		// Special variables:
		if (entity != null) { // should always be the case
			// none defined for base class
		}
		
		// evaluate any values in between parentheses
		// this is a VERY BASIC evaluator; only one plus OR one minus is allowed
		// the logic is not foreseen to be more complicated, so was not worth the programming effort yet
		// (if get any more complex, heavily recommend recursive functions here)
		String str_to_eval;
		int index_of_operator;
		Float eval_result;
		int count=0;
		while(text.matches(".*\\(.*\\).*")) {
			count++;
			// replace first parentheses match
			str_to_eval = text.substring(text.indexOf("("), text.indexOf(")")+1); // guaranteed to exist
			index_of_operator =  str_to_eval.indexOf("+");
			if (index_of_operator >= 0) {
				// '+' found
				eval_result = Float.valueOf(str_to_eval.substring(1,index_of_operator));
				eval_result += Float.valueOf(str_to_eval.substring(index_of_operator+1,str_to_eval.indexOf(")")));
				text = text.replace(str_to_eval, eval_result.toString());
			} else {
				index_of_operator =  str_to_eval.indexOf("-");
				if (index_of_operator >= 0) {
					// '-' found
					eval_result = Float.valueOf(str_to_eval.substring(1,index_of_operator));
					eval_result -= Float.valueOf(str_to_eval.substring(index_of_operator+1,str_to_eval.indexOf(")")));
					text = text.replace(str_to_eval, eval_result.toString());
				} else {
					throw new RuntimeException("Logic Check: Invalid fieldvalues evaluation");
				}
			}
			
			// add this break, just in case something was missed in the parsing so as to prevent infinite loops
			if (count > 10) {
				throw new RuntimeException("Logic Check: Invalid fieldvalues evaluation: infinite loop");
			}
		}
		return text;
	}
	
	// Perform this operator's logic on the parameters passed.
	// Notes for new logic check operators:
	//   1) when naming an operator, consider how the negation ties into it (does it make sense if negated?).  Avoid double negatives.
	//   2) code the logic to handle metadata and NULL values appropriately (in entity AND any connected entities)
	//     a) once a metadata/NULL value is found and means "then not an issue", throw a "notAnIssue" exception (bypassing rest of primary logic as well) 
	//     b) if a metadata/NULL is to be ignored for one part of the logic calculation, but the logic continues its check,
	//        then do not throw and allow it to return whether an issue or not.  The later negation logic is applied in this case.
	//   3) if checkDescDataAppend is used, the wording should be useful for both "negate" possibilities
	// override to support more operators
	public boolean doLogicCheckOperator(EntityBase entity, String operator, String fieldname, String fieldvalues, String entityclassname, String fieldvalues_alternate, String[] checkDescDataAppend) throws Exception {

		// crossform checks should have already been handled in subclass, replacing entity if needed
		//  so invalid definition if entity does not match entityclassname; a generic entity has no
		//  notion of how a second entity relates to itself
		if (entityclassname != null && !entityclassname.equals(entity.getClass().getSimpleName())) {
			throw new Exception("cross form check not handled");
		}
		// if operator was null, allow the exception to occur, so as to know the definition was invalid
		if (operator.equals("IN_SET")) {
			return LogicCheckUtils.doOperator_InSet(entity, checkDescDataAppend, fieldname, fieldvalues);
		} else if (operator.equals("ISBLANK")) {
			return LogicCheckUtils.doOperator_IsBlank(entity, checkDescDataAppend, fieldname);
		} else if (operator.equals("ISKNOWN")) {
			return LogicCheckUtils.doOperator_IsKnown(entity, checkDescDataAppend, fieldname, fieldvalues);
		} else if (operator.equals("EQUALS_STRING_LENGTH")) {
			return LogicCheckUtils.doOperator_EqualsStringLength(entity, checkDescDataAppend, fieldname, fieldvalues);
		} else if (operator.equals("COUNTIF_IN_EQUALS_COUNT")) {
			return LogicCheckUtils.doOperator_CountIfIn_Equals_Count(entity, checkDescDataAppend, fieldname, fieldvalues, fieldvalues_alternate);
		}
		// ADD NEW OPERATOR LOGIC HERE

		// operator not defined so throw an exception so that invalidDef can be set
		throw new RuntimeException("Logic Check exception: invalid operator definition ("+operator+")");

	}
	
	// override to support more operators
	// return phrase describing this operator; used in conjunction with primary logic description
	// Guideline 1: do not provide punctuation at the end (the primary logic description will provide it)
	// Guideline 2: handle whetherNegate 
	public String getCheckDescOperator(String operator, boolean whetherNegate, String fieldname, String fieldItemNum, String fieldvalues, String fieldvalues_alternate, boolean whetherEntity2) {

		String header;
		
		header = "The data element";
		if (fieldname.contains(",")) header += "s";
		header += " " + fieldname.toUpperCase();
		// if entity2, this could reference an entity that is different than the primary one; if so, print the entity2 name
		if (whetherEntity2
			&& (this.getEntity2classname() != null && !this.getEntity2classname().equals(this.getEntity1classname())))
			header += " ("+this.getEntity2classname()+": item "+fieldItemNum+")";
		else
			header += " ("+fieldItemNum+")";
				
		// if operator was null, allow the exception to occur, so as to know the definition was invalid
		if (operator.equals("IN_SET")) {
			return header + " is "+expandFieldValuesString(fieldvalues, whetherNegate);
		} else if (operator.equals("ISBLANK")) {
			// "whetherNegate=false" means this meets the condition if the data element is found blank (not specified)
			return header + " is "+(whetherNegate?"":"not ")+"specified";
		} else if (operator.equals("ISKNOWN")) {
			// "whetherNegate=false" means this meets the condition if the data element is found as known
			return header + " is "+(whetherNegate?"un":"")+"known";
		} else if (operator.equals("EQUALS_STRING_LENGTH")) {
			return header + " is "+(whetherNegate?"not ":"")+"exactly length "+fieldvalues;
		} else if (operator.equals("COUNTIF_IN_EQUALS_COUNT")) {
			return header + ": the number of values that are "+expandFieldValuesString(fieldvalues, false)
			              + " came out to be "+expandFieldValuesString(fieldvalues_alternate, whetherNegate);
		}
		// ADD NEW OPERATOR DESCRIPTIONS HERE
		
		return "[Undefined description for logic operator]";
	}
	
	// Based on check type and specific entities, return the dependent entities (i.e. when the logic
	// crosses multiple entities, then the currently checked entity is not the only one involved in
	// the logic)
	// Any entities returned must be the full data (e.g. UdsSubjectDemo, not UdsInstrumentTracking)
	public List getDependentEntities(EntityBase entity) {
		// depending on the operators, calculate all dependent entities

		if (this.getPrimaryLogic().equals("CUSTOM")) {
			return entity.getDependentEntities();  // only the entity itself will know about its custom logic
		}
		// ADD NEW LOGIC CHECK DESCRIPTIONS HERE
		
		return null;		
	}
}
