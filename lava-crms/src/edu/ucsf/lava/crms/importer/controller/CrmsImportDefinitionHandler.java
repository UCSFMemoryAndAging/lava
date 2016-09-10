package edu.ucsf.lava.crms.importer.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.importer.controller.ImportDefinitionHandler;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.assessment.InstrumentManager;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.importer.model.CrmsImportDefinition;
import static edu.ucsf.lava.crms.importer.model.CrmsImportDefinition.*;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
import edu.ucsf.lava.crms.project.ProjectManager;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;
import edu.ucsf.lava.crms.assessment.model.Instrument;


/**
 * CrmsImportDefinitionHandler
 * 
 * Handles the CRUD for CrmsImportDefinition 
 * 
 * @author ctoohey
 *
 */
public class CrmsImportDefinitionHandler extends ImportDefinitionHandler {
	protected ProjectManager projectManager; 
	protected InstrumentManager instrumentManager;

	public CrmsImportDefinitionHandler() {
		super();
		setHandledEntity("importDefinition", CrmsImportDefinition.class);
		// projName is required if the import is inserting new Patients, EnrollmentStatuses, Visit and Instruments,
		// if dealing with pre-existing entities may not need projName, but for the most part will be creating the
		// instrument at a minimum so make it required
		// there is also conditional validation performed in the conditionalValidation method 
		this.setRequiredFields(StringUtils.mergeStringArrays(this.getRequiredFields(), 
			new String[]{"projName", "patientExistRule", "esExistRule"}));
	}
	
	/**
	 * The idea here is that if this is a crms application, then we always want
	 * to use the CrmsImportDefinitionHandler instead of the core ImportDefinitionHandler. If scopes
	 * need to extend ImportDefinition further, then they should subclass and customize this
	 * handler/action. 
	 */
	@Override
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}

	public void updateManagers(Managers managers){
		super.updateManagers(managers);
		this.projectManager = CrmsManagerUtils.getProjectManager(managers);
		this.instrumentManager = CrmsManagerUtils.getInstrumentManager(managers);
	}
	
	@Override
	public Map addReferenceData(RequestContext context, Object command,	BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsImportDefinition crmsImportDefinition = (CrmsImportDefinition) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
	 	StateDefinition state = context.getCurrentState();

		//	load up dynamic lists
	 	String projName = crmsImportDefinition.getProjName(); 
	 	
		model = super.addReferenceData(context, command, errors, model); 
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
	 	if (state.getId().equals("add") || state.getId().equals("edit")) {
	 		// context.projectList
			// note that this list is filtered via projectAuth filter. CrmsAuthUser getAuthDaoFilters determines the projects to
			// which a user has some kind of access. However, the list must be further filtered based on permissions to make sure 
			// the user has the import permission for each project in the list.
			Map<String,String> projList = listManager.getDynamicList(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request), "context.projectList");
			projList = CrmsAuthUtils.filterProjectListByPermission(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request),
					CoreSessionUtils.getCurrentAction(sessionManager,request), projList);
			dynamicLists.put("context.projectList", projList);

			if (projName != null) {
				// visit.visitTypes
				dynamicLists.put("visit.visitTypes", listManager.getDynamicList("visit.visitTypes", 
						"projectName", projName, String.class));
				// visit.visitLocations
				dynamicLists.put("visit.visitLocations", listManager.getDynamicList("visit.visitLocations", 
						"projectName", projName, String.class));
				// visit.visitWith
				dynamicLists.put("project.staffList", listManager.getDynamicList("project.projectStaffList", 
						"projectName", projName, String.class));
			
				// enrollmentStatus.projectStatus
				//try for project status based on full projName
				dynamicLists.put("enrollmentStatus.projectStatus",listManager.getDynamicList("enrollmentStatus.projectStatus", 
						"projectName", projName, String.class));
				//if no entries found (size is 1 because "","" entry added to all dynamic lists), then use just project part as the project name leaving off unit 
				if (dynamicLists.get("enrollmentStatus.projectStatus").size()==1){
					String project = projectManager.getProject(projName).getProject();
					dynamicLists.put("enrollmentStatus.projectStatus",listManager.getDynamicList("enrollmentStatus.projectStatus", 
						"projectName", project, String.class));
				}
				//if no entries found based on projName or just project without the unit then get general statuses
				if (dynamicLists.get("enrollmentStatus.projectStatus").size()==1){
					dynamicLists.put("enrollmentStatus.projectStatus",listManager.getDynamicList("enrollmentStatus.projectStatus", 
							"projectName", "GENERAL", String.class));
				}
			}
			model.put("dynamicLists", dynamicLists);
		}
		return model; 
	}
	

	protected Event mappingFilePropertyValidation(RequestContext context, Object command, BindingResult errors, 
			String[] mappingCols, String[] mappingEntities, String[] mappingProps) throws Exception{
		CrmsImportDefinition importDefinition = (CrmsImportDefinition) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		Map<String,Object> instrPropNamesMap = new HashMap<String,Object>(),
				instr2PropNamesMap = new HashMap<String,Object>(),
				instr3PropNamesMap = new HashMap<String,Object>(),
				instr4PropNamesMap = new HashMap<String,Object>(),
				instr5PropNamesMap = new HashMap<String,Object>(),
				instr6PropNamesMap = new HashMap<String,Object>(),
				instr7PropNamesMap = new HashMap<String,Object>(),
				instr8PropNamesMap = new HashMap<String,Object>(),
				instr9PropNamesMap = new HashMap<String,Object>(),
				instr10PropNamesMap = new HashMap<String,Object>(),
				instr11PropNamesMap = new HashMap<String,Object>(),
				instr12PropNamesMap = new HashMap<String,Object>(),
				instr13PropNamesMap = new HashMap<String,Object>(),
				instr14PropNamesMap = new HashMap<String,Object>(),
				instr15PropNamesMap = new HashMap<String,Object>();
		Class instrClazz;
		Instrument instr = null, instr2 = null, instr3 = null, instr4 = null, instr5 = null, instr6 = null, instr7 = null, 
				instr8 = null, instr9 = null, instr10 = null, instr11 = null, instr12 = null, instr13 = null, instr14 = null, instr15 = null;

		if (super.mappingFilePropertyValidation(context, command, errors, mappingCols, mappingEntities, mappingProps).getId().equals(ERROR_FLOW_EVENT_ID)) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}

		//  validate that the minimum fields are present in the mapping file to match a patient and a visit

		int indexPatientPIDN = ArrayUtils.indexOf(mappingProps, "PIDN");
		// check that entity is "patient" (even though highly unlikely any other entity would have a "PIDN" property)  
		if (indexPatientPIDN != -1 && !mappingEntities[indexPatientPIDN].equalsIgnoreCase("patient")) {
			indexPatientPIDN = -1;
		}
		int indexPatientFirstName = ArrayUtils.indexOf(mappingProps, "firstName");
		if (indexPatientFirstName != -1 && !mappingEntities[indexPatientFirstName].equalsIgnoreCase("patient")) {
			indexPatientFirstName = -1;
		}
		int indexPatientLastName = ArrayUtils.indexOf(mappingProps, "lastName");
		if (indexPatientLastName != -1 && !mappingEntities[indexPatientLastName].equalsIgnoreCase("patient")) {
			indexPatientLastName = -1;
		}
		if (indexPatientPIDN == -1 && (indexPatientFirstName == -1 || indexPatientLastName == -1)) {
			LavaComponentFormAction.createCommandError(errors, "Insufficient patient properties. Must have patient in Row 2 with either PIDN or firstName and lastName in Row 3 of mapping file");
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		
		if (!importDefinition.getPatientOnlyImport()) {
			int indexVisitDate = ArrayUtils.indexOf(mappingProps, "visitDate");
			if (indexVisitDate != -1 && !mappingEntities[indexVisitDate].equalsIgnoreCase("visit")) {
				indexVisitDate = -1;
			}
			if (indexVisitDate == -1) {
				LavaComponentFormAction.createCommandError(errors, "Insufficient visit properties. Must have visit in Row 2 with visitDate in Row 3 of mapping file");
				return new Event(this, ERROR_FLOW_EVENT_ID);
			}
		}
		
		// before validating the individual properties, validate that the instrument name or alias in the mapping file matches an entity name or
		// alias in the import definition
		Set<String> invalidEntities = new LinkedHashSet<String>();
		for (int mappingIndex = 0; mappingIndex < mappingEntities.length; mappingIndex++) {
			String mappingEntity = mappingEntities[mappingIndex];
			if (mappingCols[mappingIndex].startsWith(SKIP_INDICATOR)) {
				continue;
			}
			if (!importDefinition.getPatientOnlyImport()) {	
				// if null that is valid, because it defaults to the first instrument
				if (!StringUtils.hasText(mappingEntity)) {
					mappingEntity = importDefinition.getInstrType() != null ? importDefinition.getInstrType() : importDefinition.getInstrMappingAlias();
				}
			}
			
			// non-instrument entity name matches can be case insensitive because during the import validateAndMapDataFile / setDataFilePropertyIndex does a case-insensitive match on the entity name (and
			// then the resulting index of the property is used to get the property value to retrieve or create an entity instance).
			// note: non-instrument entity properties, on the other hand, are not case insensitive because BeanUtils setProperty needs an exact match (although have removed this requirement for instrument
			// property names for user convenience by doing a case insensitive match of the mapping file property name against the real property name and then using the real property name. could do the
			// same for non-instrument properties)
			if (mappingEntity != null && (mappingEntity.equalsIgnoreCase("patient") || mappingEntity.equalsIgnoreCase("contactInfo") || mappingEntity.equalsIgnoreCase("caregiver") || mappingEntity.equalsIgnoreCase("caregiverContactInfo") || 
					mappingEntity.equalsIgnoreCase("caregiver2") || mappingEntity.equalsIgnoreCase("caregiver2ContactInfo") || mappingEntity.equalsIgnoreCase("enrollmentStatus") || mappingEntity.equalsIgnoreCase("visit"))) {
				continue;
			}
			// instrument matches can use equalsIgnoreCase because the import definition instrType is used to generate instrTypeEncoded to determine which instrument Class to retrieve
			// or create, and generating instrTypeEncoded converts all characters to lowercase
			if (!importDefinition.getPatientOnlyImport() && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType2()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType2()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias2()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType3()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType3()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias3()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType4()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType4()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias4()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType5()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType5()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias5()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType6()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType6()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias6()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType7()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType7()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias7()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType8()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType8()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias8()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType9()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType9()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias9()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType10()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType10()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias10()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType11()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType11()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias11()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType12()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType12()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias12()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType13()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType13()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias13()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType14()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType14()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias14()))) {
				continue;
			}
			if (StringUtils.hasText(importDefinition.getInstrType15()) && (mappingEntity.equalsIgnoreCase(importDefinition.getInstrType15()) || mappingEntity.equalsIgnoreCase(importDefinition.getInstrMappingAlias15()))) {
				continue;
			}
			// if got this far then the entity name is Row 2 of the mapping file is invalid
			invalidEntities.add(mappingEntity);
		}
		
		if (invalidEntities.size() > 0) {
			LavaComponentFormAction.createCommandError(errors, "The following mapping entity names (in row 2 of mapping file) are not valid (instrument entity names must match an instrument or alias in the definition).<br/>" + invalidEntities.toString());
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		
		
		// now validate that the instrument properties in the mapping file exist

/********		
TODO: find out which property getter throws an exception on PropertyUtils.describe, resulting in InvocationTargetException. does not happen in lava2 so could
 try to isolate it by comparing with that. Also, could debug into that source code to find out.
		if (!importDefinition.getPatientOnlyImport()) {	
			try {
				instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType(), importDefinition.getInstrVer()));
				// create an instrument with null Patient and Visit, etc. since just need an instantiated object to get all its property names
				instr = Instrument.create(instrClazz, null, null, null, null, null, null);
				instrPropNamesMap = PropertyUtils.describe(instr);

				if (StringUtils.hasText(importDefinition.getInstrType2())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType2(), importDefinition.getInstrVer2()));
					instr2 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr2PropNamesMap = PropertyUtils.describe(instr2);
				}
				if (StringUtils.hasText(importDefinition.getInstrType3())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType3(), importDefinition.getInstrVer3()));
					instr3 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr3PropNamesMap = PropertyUtils.describe(instr3);
				}
				if (StringUtils.hasText(importDefinition.getInstrType4())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType4(), importDefinition.getInstrVer4()));
					instr4 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr4PropNamesMap = PropertyUtils.describe(instr4);
				}
				if (StringUtils.hasText(importDefinition.getInstrType5())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType5(), importDefinition.getInstrVer5()));
					instr5 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr5PropNamesMap = PropertyUtils.describe(instr5);
				}
				if (StringUtils.hasText(importDefinition.getInstrType6())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType6(), importDefinition.getInstrVer6()));
					instr6 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr6PropNamesMap = PropertyUtils.describe(instr6);
				}
				if (StringUtils.hasText(importDefinition.getInstrType7())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType7(), importDefinition.getInstrVer7()));
					instr7 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr7PropNamesMap = PropertyUtils.describe(instr7);
				}
				if (StringUtils.hasText(importDefinition.getInstrType8())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType8(), importDefinition.getInstrVer8()));
					instr8 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr8PropNamesMap = PropertyUtils.describe(instr8);
				}
				if (StringUtils.hasText(importDefinition.getInstrType9())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType9(), importDefinition.getInstrVer9()));
					instr9 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr9PropNamesMap = PropertyUtils.describe(instr9);
				}
				if (StringUtils.hasText(importDefinition.getInstrType10())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType10(), importDefinition.getInstrVer10()));
					instr10 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr10PropNamesMap = PropertyUtils.describe(instr10);
				}
				if (StringUtils.hasText(importDefinition.getInstrType11())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType11(), importDefinition.getInstrVer11()));
					instr11 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr11PropNamesMap = PropertyUtils.describe(instr11);
				}
				if (StringUtils.hasText(importDefinition.getInstrType12())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType12(), importDefinition.getInstrVer12()));
					instr12 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr12PropNamesMap = PropertyUtils.describe(instr12);
				}
				if (StringUtils.hasText(importDefinition.getInstrType13())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType13(), importDefinition.getInstrVer13()));
					instr13 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr13PropNamesMap = PropertyUtils.describe(instr13);
				}
				if (StringUtils.hasText(importDefinition.getInstrType14())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType14(), importDefinition.getInstrVer14()));
					instr14 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr14PropNamesMap = PropertyUtils.describe(instr14);
				}
				if (StringUtils.hasText(importDefinition.getInstrType15())) {
					instrClazz = this.instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(importDefinition.getInstrType15(), importDefinition.getInstrVer15()));
					instr15 = Instrument.create(instrClazz,	null, null, null, null, null, null);
					instr15PropNamesMap = PropertyUtils.describe(instr15);
				}
			}
			catch (InvocationTargetException ex) {
				LavaComponentFormAction.createCommandError(errors, "[InvocationTargetException] Error on Validation. PropertyUtils.describe.<br/>" + ex.getMessage());
				return new Event(this, ERROR_FLOW_EVENT_ID);
			}
			catch (IllegalAccessException ex) {
				LavaComponentFormAction.createCommandError(errors, "[IllegalAccessException] Error on Validation. PropertyUtils.describe.<br/>" + ex.getMessage());
				return new Event(this, ERROR_FLOW_EVENT_ID);
			}
			catch (NoSuchMethodException ex) {
				LavaComponentFormAction.createCommandError(errors, "[NoSuchException] Error on Validation. PropertyUtils.describe.<br/>" + ex.getMessage());
				return new Event(this, ERROR_FLOW_EVENT_ID);
			}
	
			Set<String> invalidProps = new LinkedHashSet<String>();
			String definitionColName, definitionPropName, definitionEntityName;
			for (int mappingIndex = 0; mappingIndex < mappingCols.length; mappingIndex++) {
				if (mappingCols[mappingIndex].startsWith("SKIP:")) {
					continue;
				}
				
				definitionEntityName = mappingEntities[mappingIndex];  // this is instrType, not instrTypeEncoded
				// if entity not specified, defaults to first instrument
				if (!StringUtils.hasText(definitionEntityName)) {
					definitionEntityName = ((CrmsImportDefinition)importDefinition).getInstrMappingAlias() != null ? ((CrmsImportDefinition)importDefinition).getInstrMappingAlias() : ((CrmsImportDefinition)importDefinition).getInstrType();
				}
				definitionPropName = mappingProps[mappingIndex];
				// if property not specified, defaults to column (variable) name
				if (!StringUtils.hasText(definitionPropName)) {
					definitionPropName = mappingCols[mappingIndex];
				}
				
				// determine which instrument this property is for and test if it is in the property map for that instrument
				if (definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias())) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr, instrPropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType2()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType2()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias2()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr2, instr2PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType3()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType3()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias3()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr3, instr3PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType4()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType4()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias4()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr4, instr4PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType5()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType5()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias5()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr5, instr5PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType6()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType6()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias6()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr6, instr6PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType7()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType7()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias7()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr7, instr7PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType8()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType8()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias8()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr8, instr8PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType9()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType9()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias9()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr9, instr9PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType10()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType10()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias10()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr10, instr10PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType11()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType11()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias11()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr11, instr11PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType12()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType12()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias12()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr12, instr12PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType13()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType13()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias13()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr13, instr13PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType14()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType14()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias14()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr14, instr14PropNamesMap.keySet(), invalidProps);
				}
				else if (StringUtils.hasText(importDefinition.getInstrType15()) &&
						(definitionEntityName.equalsIgnoreCase(importDefinition.getInstrType15()) || definitionEntityName.equalsIgnoreCase(importDefinition.getInstrMappingAlias15()))) {
					propertyValidationHelper(definitionEntityName, definitionPropName, instr15, instr15PropNamesMap.keySet(), invalidProps);
				}
			}
			
			if (invalidProps.size() > 0) {
				LavaComponentFormAction.createCommandError(errors, "The following mapping instrument fields (in row 3 of mapping file) do not exist.<br/>" + invalidProps.toString());
				return new Event(this,ERROR_FLOW_EVENT_ID);
			}
			
			instr = instr2 = instr3 = instr4 = instr5 = instr6 = instr7 = instr8 = instr9 = instr10 = instr11 = instr12 = instr13 = instr14 = instr15 = null;
		}
*****/	
		
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	
	protected void propertyValidationHelper(String definitionEntityName, String definitionPropName, 
			Instrument instr, Set<String> instrPropNamesSet, Set invalidProps) throws Exception {
		String instrPropName = definitionPropName; // if there is a case mismatch the instrPropName will be reset to the correct case
		boolean propIgnoreCaseMatch = false;
		try {
			// try for a case-sensitive exact match first
			
			// do a getSimpleProperty to determine if the mapped property name matches a property. if it does not
			// then this method will throw NoSuchMethodException. 
			// NOTE: if need to support setting an indexed or nested property, will need to modify this. The call to 
			// getSimpleProperty will throw this exception:
			// IllegalArgumentException - if the property name is nested or indexed
			// so could catch this exception and call various other methods, such as PropertyUtils.getMappedProperty
			// for now, the instrument notes property is a Map and all instruments have the property, so we just skip
			// the check in that case.
			
			if (!definitionPropName.startsWith("notes")) {
				Object propValue = PropertyUtils.getSimpleProperty(instr, definitionPropName);
			}
		}
		catch (NoSuchMethodException ex) {
			// if case-sensitive match failed, try a case-insensitive match iterating through all properties of the instrument
			// until a match is found
			
			// facilitate ignoring case when matching the property name as specified in the import definition mapping 
			// file against the real property name. 
			
			// if the mapped property name does not exist, before returning an error, see if it is just a case
			// mismatch and if so use the correct property name.
			
			for (String beanPropName : instrPropNamesSet){
				if (definitionPropName.equalsIgnoreCase(beanPropName)) {
					instrPropName = beanPropName;
					propIgnoreCaseMatch = true;
					break;
				}
			}
	
			if (!propIgnoreCaseMatch) {
				invalidProps.add(definitionEntityName + "." + definitionPropName);
			}
		}
		
	}
	




	
	protected Event conditionalValidation(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsImportDefinition crmsImportDefinition = (CrmsImportDefinition) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());

		if (super.conditionalValidation(context, command, errors).getId().equals(ERROR_FLOW_EVENT_ID)) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}

		// look at required fields. since they are conditionally determined, can not set them in the
		// standard way, since that takes place before binding, before properties have the values on
		// which conditional logic depends
		
		if (crmsImportDefinition.getEsExistRule().equals(MAY_OR_MAY_NOT_EXIST) || crmsImportDefinition.getEsExistRule().equals(MUST_NOT_EXIST)) {
			if (crmsImportDefinition.getEsStatus() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "esStatus", getDefaultObjectName());
			}
		}

		if (!crmsImportDefinition.getPatientOnlyImport()) {
			
			if (crmsImportDefinition.getVisitExistRule() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "visitExistRule", getDefaultObjectName());
			}
			// note that visitWith is not a required Visit property so not checking for that
			/* update: cannot require because these values may be supplied in the data file, and cannot validate here because do not know
			 * whether or not data files will have these values
			 * could consider having user explicitly setting a flag to indicate the values will be in the data file, but bottom line is that
			 * the user will get an import error if trying to load a data file and the visit fields are neither in the import definition or in
			 * the data file
			else if (crmsImportDefinition.getVisitExistRule().equals(MAY_OR_MAY_NOT_EXIST) || crmsImportDefinition.getVisitExistRule().equals(MUST_NOT_EXIST)) {
				if (crmsImportDefinition.getVisitType() == null || crmsImportDefinition.getVisitLoc() == null || crmsImportDefinition.getVisitStatus() == null) {
					LavaComponentFormAction.createCommandError(errors, "importDefinition.visitFields.required", null);
				}
			}
			 */

			if (crmsImportDefinition.getInstrExistRule() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "instrExistRule", getDefaultObjectName());
			}
			else if (crmsImportDefinition.getInstrType() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "instrType", getDefaultObjectName());
			}
			else if (crmsImportDefinition.getInstrExistRule().equals(MAY_OR_MAY_NOT_EXIST) || crmsImportDefinition.getInstrExistRule().equals(MUST_NOT_EXIST)) {
				if (crmsImportDefinition.getInstrDcStatus() == null) {
					LavaComponentFormAction.createRequiredFieldError(errors, "instrDcStatus", getDefaultObjectName());
				}
			}
			
			if (crmsImportDefinition.getInstrCaregiver()) {
				if (crmsImportDefinition.getInstrCaregiverExistRule() == null) {
					LavaComponentFormAction.createRequiredFieldError(errors, "instrCaregiverExistRule", getDefaultObjectName());
				}
			}
		}
		
		if (errors.hasFieldErrors() || errors.hasErrors()) {
			LavaComponentFormAction.createCommandErrorsForFieldErrors(errors);
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	

	protected Event mappingFileValidation(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsImportDefinition crmsImportDefinition = (CrmsImportDefinition) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());

		Event returnEvent = super.mappingFileValidation(context, command, errors);
		
		//TODO: validate that the mapping file has either a PIDN or firstName/lastName fields, and
		// a visitDate field. This would involve opening/reading the mapping file entity names (row 2) and property names
		// (row 3) into an array and searching them using ArrayUtils.indexOf 
		// presently this validation is being done in the CrmsImportHandler validateDataFile method but would
		// be better to catch it earlier, and when done here remove it from there	

		
		return returnEvent;
	}
	
}
