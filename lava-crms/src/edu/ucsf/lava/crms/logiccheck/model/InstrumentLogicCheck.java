package edu.ucsf.lava.crms.logiccheck.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;
import edu.ucsf.lava.core.logiccheck.controller.LogicCheckUtils;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.logiccheck.controller.CrmsLogicCheckUtils;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
import edu.ucsf.lava.crms.model.CrmsEntity;

public class InstrumentLogicCheck extends CrmsLogicCheck {
	public static InstrumentLogicCheck.Manager MANAGER = new InstrumentLogicCheck.Manager();
	
	protected String instrVers;
	protected String visitTypes;
	protected String checkCodes;

	public String getInstrVers() {
		return instrVers;
	}

	public void setInstrVers(String instrVers) {
		this.instrVers = instrVers;
	}
	
	public String getVisitTypes() {
		return visitTypes;
	}

	public void setVisitTypes(String visitTypes) {
		this.visitTypes = visitTypes;
	}
	
	public String getCheckCodes() {
		return checkCodes;
	}

	public void setCheckCodes(String checkCodes) {
		this.checkCodes = checkCodes;
	}
	
	@Override
	// An instrument checkCode will depend on the instrument version and visit type, looking up the appropriate
	//   code value in the checkCodes list
	public String getCheckCode(Long entityID) {
		
		if (this.getCheckCodes() == null) return super.getCheckCode(entityID);
		String[] tokens_checkCodes = this.getCheckCodes().split(",");
		// return immediately if only one listed
		if (tokens_checkCodes.length==1) return this.getCheckCodes();

		// return the corresponding code in the list of checkCodes for this particular instrument version and visit type
		InstrumentTracking instr = (InstrumentTracking)InstrumentTracking.MANAGER.getById(entityID);
		
		// find the instrVer index (zero-indexed)
		int index_of_instrVer = -1;
		String[] tokens = this.getInstrVers().split(",");
		for (int x=0; x<tokens.length; x++) {
			if (instr.getInstrVer().equals(tokens[x])) {
				index_of_instrVer = x;
				break;
			}
		}
		if (index_of_instrVer == -1) super.getCheckCode(entityID);  // should never happen
		
		// find the visitType index (zero-indexed)
		int index_of_visitType = -1;
		tokens = this.getVisitTypes().split(",");
		int visitType_size = tokens.length;
		for (int x=0; x<tokens.length; x++) {
			if (instr.getVisit().getVisitType().startsWith(tokens[x])) {
				index_of_visitType = x;
				break;
			}
		}
		if (index_of_visitType == -1) super.getCheckCode(entityID);	// should never happen

		// find total index
		int index = index_of_instrVer * visitType_size + index_of_visitType;
		
		// return the code string at that index
		return tokens_checkCodes[index];
	}

/* TQR:20120525 - don't see a need to call this, since matching logic checks are already done in InstrumentLogicCheck.MANAGER.get()
	@Override
	public boolean isEntityMatch(EntityBase entity) {
		
		// assumption: instr is the specific instrument, not just InstrumentTracking, else instr.getVisit() fails
		Instrument instr = (Instrument)entity;
		// do the filtering in the ordering most likely to filter out non-matching checks first
		if (!getEntity1classname().equals(entity.getClass().getName())) return false;
		
		// Note: instrVers is a comma-delimited list of matching versions 
		String[] tokens = this.getInstrVers().split(",");
		boolean found = false;
		for (int x=0; x<tokens.length; x++) {
			if (instr.getInstrVer().equals(tokens[x])) {
				found = true;
				break;
			}
		}
		if (!found) return false;

		// visit types are a comma-delimited list of abbreviated visit types; it matches any visit type starting
		//   with any string in the list
		tokens = this.getVisitTypes().split(",");
		found = false;
		for (int x=0; x<tokens.length; x++) {
			if (instr.getVisit().getVisitType().startsWith(tokens[x])) {
				found = true;
				break;
			}
		}
		if (!found) return false;
		
		return true;
	}
*/
	public List<EntityBase> getMatchingEntities() {

		EntityManager manager = CrmsManagerUtils.getLogicCheckManager().getEntityManager(this.getEntity1classname());
		if (manager == null) return null;
		LavaDaoFilter filter = manager.newFilterInstance();
		
		filter.setAlias("visit", "visit");
		filter.addDaoParam(getEntityDaoParam_VisitTypes(filter));
		filter.addDaoParam(getEntityDaoParam_InstrVers(filter));
		
		return manager.get(filter);
	}
	
	protected LavaDaoParam getEntityDaoParam_VisitTypes(LavaDaoFilter filter) {
		
		// assume alias of "visit" already set
		//filter.setAlias("visit", "visit");
		
		LavaDaoParam paramTempOr;
		LavaDaoParam paramTemp;
		
		// for visit types, the logic check definition contains a shortened form of every visit type that gets defined
		// separated by commas.  It matches up to any visit type *starting* with the shortened form.  So if an instrument's
		// visit could be two types starting with the same letters, it is up to the definition here to make sure it
		// supplies the correct amount of letters to distinguish it.
		paramTempOr = null;
		String[] tokens = this.getVisitTypes().split(",");
		for (int x=0; x<tokens.length; x++) {
			paramTemp = filter.daoLikeParam("visit.visitType", tokens[x] + "%");
			if (paramTempOr == null)
				paramTempOr = paramTemp;
			else
				paramTempOr = filter.daoOr(paramTempOr, paramTemp);
    	}
		
		return paramTempOr;
	}
	
	protected LavaDaoParam getEntityDaoParam_InstrVers(LavaDaoFilter filter) {
		
		// assume alias of "visit" already set
		//filter.setAlias("visit", "visit");
		
		LavaDaoParam paramTempOr;
		LavaDaoParam paramTemp;
		
		paramTempOr = null;
		String[] tokens = this.getInstrVers().split(",");
		for (int x=0; x<tokens.length; x++) {
			paramTemp = filter.daoEqualityParam("instrVer", tokens[x]);
			if (paramTempOr == null)
				paramTempOr = paramTemp;
			else
				paramTempOr = filter.daoOr(paramTempOr, paramTemp);
		}
		return paramTempOr;
	}

	@Override
	public boolean doLogicCheckPrimaryLogic(EntityBase entity, String[] checkDescDataAppend) throws Exception {
		
		Instrument instr = (Instrument)entity;
		// Instruments are unique in that they could be created without having any data yet
		if (instr.getDeStatus() == null)  // i.e. never saved
			return false;  // no need to perform actual check; known to not be problem
		
		return super.doLogicCheckPrimaryLogic(entity, checkDescDataAppend);
	}
	
	public String evaluateFieldValuesExpression(String text, EntityBase entity, String[] checkDescDataAppend) throws Exception {
		Instrument instr = (Instrument)entity;
		
		if (text!=null && entity!=null) {
			// expand any "special variables" (such as "age") for instruments
			// Special variables:
			//   "age" = age at visit
			if (text.contains("age")) {
				Short ageAtVisit = instr.getVisit().getAgeAtVisit();
				if (ageAtVisit == null)
					throw new Exception("notAnIssue"); // cannot trigger an error if nothing to check against
				
				String ageAtVisitString = ageAtVisit.toString();
				text = text.replace("age", ageAtVisitString);
				checkDescDataAppend[checkDescDataAppend.length-1] += "[age="+ageAtVisitString+"]";
			}
		}
		
		return super.evaluateFieldValuesExpression(text, entity, checkDescDataAppend);
	}
	
	@Override
	// see notes in LogicCheck.doLogicCheckOperator()
	// override to support more operators
	public boolean doLogicCheckOperator(EntityBase entity, String operator, String fieldname, String fieldvalues, String entityclassname, String fieldvalues_alternate, String[] checkDescDataAppend) throws Exception {
		Instrument instr = (Instrument)entity;
		boolean is_crossform = false;
		
		if (entityclassname != null && !entityclassname.equals(this.getEntity1classname())) {  // then we also know this is for entity2
			is_crossform = true;
			// for instruments, this will mean a form in the "same visit" (i.e. crossform check)
			instr = CrmsLogicCheckUtils.getInstrumentOfSameVisit(instr, entityclassname);
			// check to see if dependent form has ever been saved; if not, return as if no issue
			if (instr == null || instr.getDeStatus() == null) throw new Exception("dependentNeverSaved");
		}
		
		if (operator.equals("IN_SET_PRIORVISIT")) {
			return CrmsLogicCheckUtils.doOperator_InSetAnyPriorVisit(instr, checkDescDataAppend, fieldname, fieldvalues);
		} else if (operator.equals("KNOWN_PRIORVISIT")) {
			return CrmsLogicCheckUtils.doOperator_KnownValuePriorVisit(instr, checkDescDataAppend, fieldname, fieldvalues);
		} else if (operator.equals("TOO_DIFFERENT_ANY_PRIORVISIT")) {
			// cond2 cannot be specified, so should never be a crossform
			if (is_crossform) throw new Exception("Condition 2 cannot be specified");
			return CrmsLogicCheckUtils.doOperator_TooDifferentThanAnyPriorVisit(instr, checkDescDataAppend, fieldname, fieldvalues, fieldvalues_alternate);
		} else if (operator.equals("TOO_DIFFERENT_LAST_PRIORVISIT")) {
			// cond2 cannot be specified, so should never be a crossform
			if (is_crossform) throw new Exception("Condition 2 cannot be specified");
			return CrmsLogicCheckUtils.doOperator_TooDifferentThanLastPriorVisit(instr, checkDescDataAppend, fieldname, fieldvalues, fieldvalues_alternate);
		} else if (operator.equals("EQUALS_FIRSTVALUE_PRIORVISIT")) {
			return CrmsLogicCheckUtils.doOperator_EqualsFirstValuePriorVisit(instr, checkDescDataAppend, fieldname);
		} else if (operator.equals("LARGEST_PRIORVISIT")) {
			return CrmsLogicCheckUtils.doOperator_LargestValuePriorVisit(instr, checkDescDataAppend, fieldname, fieldvalues);
		} 

		
		// ADD NEW LOGIC CHECK OPERATORS HERE
		
		if (is_crossform)
			return super.doLogicCheckOperator(instr, operator, fieldname, fieldvalues, entityclassname, fieldvalues_alternate, checkDescDataAppend);
		else
			return super.doLogicCheckOperator(entity, operator, fieldname, fieldvalues, entityclassname, fieldvalues_alternate, checkDescDataAppend);
	}
	
	@Override	
	public String getCheckDescOperator(String operator, boolean whetherNegate, String fieldname, String fieldItemNum, String fieldvalues, String fieldvalues_alternate, boolean whetherEntity2) {

		// if operator was null, allow the exception to occur, so as to know the definition was invalid
		if (operator.equals("IN_SET_PRIORVISIT")) {
			return "A prior visit had value of "+expandFieldValuesString(fieldvalues, whetherNegate);
		} else if (operator.equals("KNOWN_PRIORVISIT")) {
			return "A prior visit "+(whetherNegate?"did not have ": "had ")+"a known value";
		} else if (operator.equals("TOO_DIFFERENT_ANY_PRIORVISIT")) {
			return fieldname.toUpperCase()+" ("+fieldItemNum.toUpperCase()+") : a prior visit has value differing by "+(whetherNegate?"less than or equal to ":"more than ")+fieldvalues;
		} else if (operator.equals("TOO_DIFFERENT_LAST_PRIORVISIT")) {
			return fieldname.toUpperCase()+" ("+fieldItemNum.toUpperCase()+") : last visit has value differing by "+(whetherNegate?"less than or equal to ":"more than ")+fieldvalues;
		} else if (operator.equals("EQUALS_FIRSTVALUE_PRIORVISIT")) {
			return fieldname.toUpperCase()+" ("+fieldItemNum.toUpperCase()+") "+(whetherNegate?"does not match ":"matches ")+"first value supplied from prior visits";
		} else if (operator.equals("LARGEST_PRIORVISIT")) {
			return fieldname.toUpperCase()+" ("+fieldItemNum.toUpperCase()+") is "+(whetherNegate?"not ":"")+"the largest value supplied from prior visits";
		} 
		
		return super.getCheckDescOperator(operator, whetherNegate, fieldname, fieldItemNum, fieldvalues, fieldvalues_alternate, whetherEntity2);
	}

	@Override
	public List getDependentEntities(EntityBase entity) {
		
		// dependent entities are those who need to be rechecked for this particular logic check
		//   when the given entity is changed
		// NOTE: it is already determined that this logic check applies to the given entity
		
		// Example: check 1: (UdsHealthHistory.seizures IN_SET 0 AND UdsHealthHistory.seizures IN_SET_PRIORVISIT 1)
		// the given entity was the initiator, i.e. was in a prior visit
		// we will return all entities that need to be rechecked because of the changes there
		
		// note: do not return entities that doesn't match InstrVer and VisitType
		// Example: if check 1 above is for version 1 only; do not return version 2 entities
		
		// if crossform check
		if (this.getCond2operator()!=null
			&& (this.getCond2operator().equals("IN_SET")
				|| this.getCond2operator().equals("COUNTIF_IN_EQUALS_COUNT"))
			&& this.getEntity2classname() != null && !this.getEntity2classname().equals(this.getEntity1classname())
			) {
			// the dependent entity will be one for the same *visit* as this entity
			// so simply use the entity1classname (not entity2) along with this entity's visit id to find the dependent entity
			Instrument instr = (Instrument)entity;
			EntityManager manager = CrmsManagerUtils.getLogicCheckManager().getEntityManager(this.getEntity1classname());
			if (manager == null) return null;
			LavaDaoFilter filter = manager.newFilterInstance();
			filter.setAlias("visit", "visit");
			filter.addDaoParam(filter.daoEqualityParam("visit.id", instr.getVisit().getId()));
			// apply the instrument-specific filters
			filter.addDaoParam(getEntityDaoParam_VisitTypes(filter));
			filter.addDaoParam(getEntityDaoParam_InstrVers(filter));
			Instrument dependentInstr = (Instrument) manager.getOne(filter);
			if (dependentInstr == null) return null;
			List<EntityBase> dependentInstrList = new ArrayList<EntityBase>();
			dependentInstrList.add(dependentInstr);
			return dependentInstrList;  // note: only returning one entity for crossform
		}
		// Check priorvisit dependencies.  Multiple dependencies or single dependency may be returned depending
		//   on logic check type.
		if ((this.getCond2operator()!=null
			 && (this.getCond2operator().equals("IN_SET_PRIORVISIT")
			     || this.getCond2operator().equals("KNOWN_PRIORVISIT"))) // no need to check cond1 for IN_SET_PRIORVISIT, since cond1 always belongs to *primary * entity by definition, not earlier ones
			|| (this.getPrimaryLogic().equals("COND1")  // some PRIORVISIT operators can be defined in cond1 if the primary logic doesn't use cond2
				&& this.getCond1operator()!=null
				&& (this.getCond1operator().equals("TOO_DIFFERENT_ANY_PRIORVISIT")
					|| this.getCond1operator().equals("EQUALS_FIRSTVALUE_PRIORVISIT")
					|| this.getCond1operator().equals("LARGEST_PRIORVISIT")))
			) { 
			// the given entity must have been one in a prior visit, return *later* forms of entity1classname since
			//   later ones are those that depend on this entity
			// Use entity1classname even when the operator is for condition 2; a previous step determined this logic check
			// applied to this given entity already

			Instrument instr = (Instrument)entity;
			// this logiccheck is the one to be run on the *dependent* entity, so the dependent entity is found by matching to entity1classname (not entity2classname)
			EntityManager manager = CrmsManagerUtils.getLogicCheckManager().getEntityManager(this.getEntity1classname());
			if (manager == null) return null;
			LavaDaoFilter filter = manager.newFilterInstance();
			filter.setAlias("patient", "patient");
			filter.addDaoParam(filter.daoEqualityParam("patient.id", instr.getPatient().getId()));
			filter.addDaoParam(filter.daoEqualityParam("projName", instr.getProjName()));
			filter.setAlias("visit", "visit");
			filter.addDaoParam(filter.daoDateAndTimeGreaterThanParam("visit.visitDate", "visit.visitTime", instr.getVisit().getVisitDate(), instr.getVisit().getVisitTime()));
			// apply the instrument-specific filters
			filter.addDaoParam(getEntityDaoParam_VisitTypes(filter));
			filter.addDaoParam(getEntityDaoParam_InstrVers(filter));
			List<Instrument> dependentInstrList = (List<Instrument>)manager.get(filter);
			if (dependentInstrList.isEmpty()) return null;
			return dependentInstrList; // manager.get(CrmsManagerUtils.getInstrumentManager().getInstrumentClass(dependentInstrList.get(0).getInstrTypeEncoded()), filter);
		}
		if (this.getPrimaryLogic().equals("COND1")
			&& this.getCond1operator()!=null && this.getCond1operator().equals("TOO_DIFFERENT_LAST_PRIORVISIT")) { 
			// in this case, it will *only* be the next visit that will be dependent on the logic in the given entity
			Instrument instr = (Instrument)entity;
			// this logiccheck is the one to be run on the *dependent* entity, so the dependent entity is found by matching to entity1classname (not entity2classname)
			EntityManager manager = CrmsManagerUtils.getLogicCheckManager().getEntityManager(this.getEntity1classname());
			if (manager == null) return null;
			LavaDaoFilter filter = manager.newFilterInstance();
			filter.setAlias("patient", "patient");
			filter.addDaoParam(filter.daoEqualityParam("patient.id", instr.getPatient().getId()));
			filter.addDaoParam(filter.daoEqualityParam("projName", instr.getProjName()));
			filter.setAlias("visit", "visit");
			filter.addDaoParam(filter.daoDateAndTimeGreaterThanParam("visit.visitDate", "visit.visitTime", instr.getVisit().getVisitDate(), instr.getVisit().getVisitTime()));
			filter.addSort("visit.visitDate", true);
			filter.addSort("visit.visitTime", true);
			// grab all later visits, but only return the first (TODO: there may be a way to do this within the query)
			List<Instrument> allInstruments = (List<Instrument>)manager.get(filter);
			if (allInstruments.isEmpty()) return null;
			Instrument nextInstrument = allInstruments.get(0);
			// now apply it again to see if next instrument meets the instrument-specific filter for *this* check
			// note: we could not do this with last filter, as we wanted to know the next visit, regardless of (say) version
			filter.addDaoParam(getEntityDaoParam_VisitTypes(filter));
			filter.addDaoParam(getEntityDaoParam_InstrVers(filter));
			allInstruments = (List<Instrument>)manager.get(filter);
			if (allInstruments.isEmpty()) return null;
			if (!nextInstrument.getId().equals(allInstruments.get(0).getId())) return null;
			
			List<Instrument> dependentEntities = new ArrayList<Instrument>();
			dependentEntities.add(nextInstrument);
			return dependentEntities;
			
		}	
		// ADD NEW LOGIC OPERATORS HERE
			
		
		return super.getDependentEntities(entity);		
	}
	
	static public class Manager extends EntityBase.Manager{
		
		public Manager(){
			super(InstrumentLogicCheck.class);
		}
		
		// get all matching logic checks for this entity
		public List get(CrmsEntity entity){
			
			LavaDaoFilter filter = InstrumentLogicCheck.MANAGER.newFilterInstance();
			Instrument instrument = (Instrument)entity;
			
			if (entity != null) {
				filter.addDaoParam(filter.daoEqualityParam("entity1classname", entity.getClass().getSimpleName()));
				filter.addDaoParam(getLogicCheckDaoParam_InstrVers(filter, instrument));
				filter.addDaoParam(getLogicCheckDaoParam_VisitTypes(filter, instrument));
			}
			List<InstrumentLogicCheck> logicchecks = (List<InstrumentLogicCheck>)InstrumentLogicCheck.MANAGER.get(filter);
			
			logicchecks = filterByVisitType(logicchecks, instrument);
			
			return logicchecks;
		}
		
		protected LavaDaoParam getLogicCheckDaoParam_InstrVers(LavaDaoFilter filter, Instrument instrument) {
			// for instrVers, do a LIKE; this works as long as version numbers are separated by commas
			// TODO: this will not work if instrVers contains multi-digit versions (e.g. "9,10,12" will incorrectly match versions 1,2,9,10,12)
			return filter.daoLikeParam("instrVers", "%"+instrument.getInstrVer()+"%");
		}
		
		protected LavaDaoParam getLogicCheckDaoParam_VisitTypes(LavaDaoFilter filter, Instrument instrument) {
			// we'll have to do better filtering for visitTypes after this query, but there is a chance to 
			// filter out some (possibly most) if we at least require the first letter. 
			// TODO: this may allow false positives a visit type starts with the same letter as a non-first letter in visitTypes
			return filter.daoLikeParam("visitTypes", "%"+instrument.getVisit().getVisitType().substring(0,1)+"%");
		}
		
		protected List<InstrumentLogicCheck> filterByVisitType(List<InstrumentLogicCheck> logicchecks, Instrument instrOfVisit) {
			// do a more exact filtering for visitTypes; this was too complicated with a database query
			// visit types are full text descriptions, but logiccheck.visitTypes is a comma-separsated list of visit prefixes (likely initials)
			// it matches when any prefix matches the full text
			// NOTE: though this is not too elegant, it was better than creating a separate InstrumentLogicCheck sub-class/table
			//  for uds instruments only, and better than having to specify the entire visit type inside
			//  InstrumentLogicCheck.visitTypes
			String[] tokens;
			boolean found;
			Iterator<InstrumentLogicCheck> lcIter = logicchecks.iterator();
			while (lcIter.hasNext()) {
				InstrumentLogicCheck logiccheck = (InstrumentLogicCheck)lcIter.next();
				tokens = logiccheck.getVisitTypes().split(",");
				found = false;
				for (int x=0; x<tokens.length; x++) {
					// find if visit's type starts with the token string
					if (instrOfVisit.getVisit().getVisitType().startsWith(tokens[x])) {
						found = true;
						break;
					}
				}
				if (!found)	lcIter.remove();
			}
			return logicchecks;
		}
		
		// get all matching logic checks whose dependent entity type is the given entity
		// that means a list of logic checks that could apply to other entities because of a change to this entity
		public List getDependentChecks(CrmsEntity entity) {
			// find all logic checks whose:
			//  (1) secondary entity matches this entity, when different (when crossform)
			//  (2) this entity is targeted b/c of operator type (e.g. priorvisit operator)
			//  (3) CUSTOM checkType (allow entity sub-class to override, so always return them)
			
			// NOTE on CrossForm checks: assume the dependent instrument belongs to same visit
			LavaDaoFilter filter = InstrumentLogicCheck.MANAGER.newFilterInstance();
			Instrument instrument = (Instrument)entity;
			
			// note: we do not want to filter PRIORVISIT operators by the primary entity's instrVer and visitType
			//   since the primary entity and the eventual dependent entity may not match
			//   *Do* filter if it is neither a PRIORVISIT nor CUSTOM operator (e.g. crossform).
			// Example: check 1: (UdsHealthHistory.seizures IN_SET 0 AND UdsHealthHistory.seizures IN_SET_PRIORVISIT 1)
			//   Since this check could apply to version 1 visits, do not dismiss this check just because this
			//   entity is version 2
			
			// EMORY: To help understand, this can be replicated by the following MySQL query:
			// (this assumes an instrument_reference table which connects the Java simple class name with the entityclassname,
			//  which was intended for testing, not as part of lava-crms scope)
			/*
			SELECT *
			FROM logiccheck lc NATURAL JOIN instrumentlogiccheck ilc
			  INNER JOIN instrumenttracking i ON InstrID=83
			  INNER JOIN visit v ON i.VID=v.VID
			  INNER JOIN emorylava.instrument_reference ref ON i.InstrType = ref.InstrType AND i.InstrVer = ref.InstrVer
			WHERE
			 ((ilc.instrVers LIKE CONCAT('%',i.InstrVer,'%')
			   AND entity2classname=ref.JavaSimpleClass
			   AND entity2classname != entity1classname)  -- crossform
			  OR (entity1classname=ref.JavaSimpleClass
			      AND cond2operator LIKE '%_PRIORVISIT') -- priorvisit in 
			  OR (entity1classname=ref.JavaSimpleClass
			      AND primaryLogic='COND1'
			      AND cond1operator LIKE '%_PRIORVISIT'
			      AND (v.VType!='Initial Assessment'
			           OR (v.VType='Initial Assessment'
			               OR cond1operator LIKE '%_INITIALVISIT')
			           ))
			  OR primaryLogic='CUSTOM'
			)
			*/
			if (entity != null) {
				LavaDaoParam paramFromCrossForm = filter.daoAnd(getLogicCheckDaoParam_InstrVers(filter,instrument),
																filter.daoAnd(filter.daoEqualityParam("entity2classname", entity.getClass().getSimpleName()),
																			  filter.daoNot(filter.daoEqualityParam("entity2classname", "entity1classname"))));
				LavaDaoParam paramFromMultiVisit1 = filter.daoAnd(filter.daoEqualityParam("entity1classname", entity.getClass().getSimpleName()),
						  										  filter.daoLikeParam("cond2operator", "%_PRIORVISIT%"));  // IN_SET_PRIORVISIT can only be found in cond2
				LavaDaoParam paramConsiderCond1MultiVisit = filter.daoLikeParam("cond1operator", "%_PRIORVISIT%");
				if (instrument.getVisit().getVisitType().equals("Initial Assessment")) {
					paramConsiderCond1MultiVisit = filter.daoOr(paramConsiderCond1MultiVisit,
																filter.daoLikeParam("cond1operator", "%_INITIALVISIT%"));
				}
				LavaDaoParam paramFromMultiVisit2 = filter.daoAnd(filter.daoEqualityParam("entity1classname", entity.getClass().getSimpleName()),
							 									  filter.daoAnd(filter.daoEqualityParam("primaryLogic", "COND1"), // some _PRIORVISIT and _INITIALVISIT may be found in cond1
							 											  		paramConsiderCond1MultiVisit));
				
				LavaDaoParam paramFromCustom = filter.daoEqualityParam("primaryLogic", "CUSTOM");
				filter.addDaoParam(filter.daoOr(filter.daoOr(filter.daoOr(paramFromCrossForm, paramFromMultiVisit1), paramFromMultiVisit2),paramFromCustom));
			}
			List<InstrumentLogicCheck> logicchecks = (List<InstrumentLogicCheck>)InstrumentLogicCheck.MANAGER.get(filter);
			

			
			//logicchecks = filterByVisitType(logicchecks, instrument);
			return logicchecks;
		}
		
	}

}
