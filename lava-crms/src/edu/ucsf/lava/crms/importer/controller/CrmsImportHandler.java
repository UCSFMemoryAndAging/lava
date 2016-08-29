package edu.ucsf.lava.crms.importer.controller;

import static edu.ucsf.lava.core.importer.model.ImportDefinition.CSV_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.DEFAULT_DATE_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.DEFAULT_TIME_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.TAB_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.SKIP_INDICATOR;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.STATIC_INDICATOR;
import static edu.ucsf.lava.crms.importer.model.CrmsImportDefinition.MAY_OR_MAY_NOT_EXIST;
import static edu.ucsf.lava.crms.importer.model.CrmsImportDefinition.MUST_EXIST;
import static edu.ucsf.lava.crms.importer.model.CrmsImportDefinition.MUST_NOT_EXIST;
import static edu.ucsf.lava.crms.people.model.Patient.DEIDENTIFIED;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import au.com.bytecode.opencsv.CSVReader;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.file.model.ImportFile;
import edu.ucsf.lava.core.importer.controller.ImportHandler;
import edu.ucsf.lava.core.importer.model.ImportDefinition;
import edu.ucsf.lava.core.importer.model.ImportLog;
import edu.ucsf.lava.crms.importer.model.CrmsImportLogMessage;
import edu.ucsf.lava.core.importer.model.ImportSetup;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.LavaEntity;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.type.LavaDateUtils;
import edu.ucsf.lava.crms.assessment.InstrumentManager;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.enrollment.EnrollmentManager;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.importer.model.CrmsImportDefinition;
import edu.ucsf.lava.crms.importer.model.CrmsImportLog;
import edu.ucsf.lava.crms.importer.model.CrmsImportSetup;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
import edu.ucsf.lava.crms.people.model.Caregiver;
import edu.ucsf.lava.crms.people.model.ContactInfo;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.project.ProjectManager;
import edu.ucsf.lava.crms.scheduling.VisitManager;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


/**
 * CrmsImportHandler
 * 
 * Handles the crms specific part of importing a data file.
 * 
 * @author ctoohey
 *
 */
public class CrmsImportHandler extends ImportHandler {
	protected InstrumentManager instrumentManager;
	protected EnrollmentManager enrollmentManager;
	protected VisitManager visitManager; 
	protected ProjectManager projectManager; 
	
	public CrmsImportHandler() {
		super();
		// the defaultObjectName should ideally be the same as the target part of the action which
		// uses this handler, i.e. lava.core.importer.import.import so target='import', because
		// the flow constructs event transitions using the target part of the action (at least for
		// customizing actions) while the decorator uses the defaultObjectName on eventButton that
		// will construct the event to be submitted which should match the transition
		setHandledEntity("import", CrmsImportSetup.class);
		setDefaultObjectBaseClass(ImportSetup.class);
	}
	
	public void updateManagers(Managers managers){
		super.updateManagers(managers);
		this.enrollmentManager = CrmsManagerUtils.getEnrollmentManager(managers);
		this.instrumentManager = CrmsManagerUtils.getInstrumentManager(managers);
		this.projectManager = CrmsManagerUtils.getProjectManager(managers);
		this.visitManager = CrmsManagerUtils.getVisitManager(managers);
	}

	/**
	 * The idea here is that if this is a crms application, then we always want
	 * to use the CrmsImportHandler instead of the core ImportHandler.  If scopes
	 * need to extend Import further, then they should subclass and customize this
	 * handler/action. 
	 */
	@Override
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}
	

	
	
	
//TODO: *** implement isAuthorized for project authorization (make sure proj auth flag is 
	// set on importSetup. assume it is ok that importSetup is not a persistent object
	
	
	
	public Map getBackingObjects(RequestContext context, Map components) {
		Map backingObjects = super.getBackingObjects(context, components);
		CrmsImportSetup importSetup = (CrmsImportSetup) components.get(this.getDefaultObjectName());
		
		// replace the importLog for crms
		ImportLog baseImportLog = (ImportLog) backingObjects.get("importLog");
		CrmsImportLog importLog = new CrmsImportLog(baseImportLog);
		backingObjects.put("importLog", importLog);
		
		return backingObjects;
	}
	
	
	protected Event doImport(RequestContext context, Object command, BindingResult errors) throws Exception {
		CrmsImportSetup importSetup = (CrmsImportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		CrmsImportLog importLog = (CrmsImportLog) ((ComponentCommand)command).getComponents().get("importLog");
		Event returnEvent = new Event(this,this.SUCCESS_FLOW_EVENT_ID);
		Event handlingEvent = null;

		// the CrmsImportSetup command object is used as a parameter object to pass parameters to methods which would
		// otherwise require many arguments
		// additionally it facilitates using properties from its ImportSetup superclass in this handler 
		// these include the columns array (mappingCols) and properties arrays (mappingEntities, mappingProps) 
		// that ImportHandler creates when reading the definition mapping file
		if ((returnEvent = super.doImport(context, command, errors)).getId().equals(SUCCESS_FLOW_EVENT_ID)) {
			CrmsImportDefinition importDefinition = (CrmsImportDefinition) importSetup.getImportDefinition();


			// note that if the ProjName is in the data file the importLog just represents the importDefinition projName since the import data file
			// could have different ProjName on different rows
			importLog.setProjName(importDefinition.getProjName());
			importLog.setNotes(importSetup.getNotes());

			// read data file
			ImportFile dataFile = importLog.getDataFile();
			int lineNum = 0;
			InputStream dataFileContent = new ByteArrayInputStream(dataFile.getContent());
			// open data file contents with a CSVReader for parsing CSV values, accounting for things like
			// quoted strings that contain comments, etc.
			CSVReader reader = null;
			if (importSetup.getImportDefinition().getDataFileFormat().equals(CSV_FORMAT)) {
				 reader = new CSVReader(new InputStreamReader(dataFileContent));
			}
			else if (importSetup.getImportDefinition().getDataFileFormat().equals(TAB_FORMAT)) {
				 reader = new CSVReader(new InputStreamReader(dataFileContent), '\t');
			}
			// nextLine[] is an array of values from the line
			String [] nextLine;
				
			// opencsv readNext parses the record into a String array
			while ((nextLine = reader.readNext()) != null) {
				lineNum++;
					
				// number of lines < MAX_LINES
				//if (++lineNum > MAX_LINES) {
				//	break;
				//}
						
				// skip over the data file column headers line (it has already been read into the importSetup
				// dataCols by the superclass)
				int startLine = importDefinition.getStartDataRow() != null ? importDefinition.getStartDataRow() : 2;
				if (lineNum < startLine) {
					continue;
				}
				
				importSetup.reset(); // reset created/existed flags to false, entities that are retrieved or created to null
				
				// note that indices of data array items in data file match up with indices of column and 
				// property array items in import definition mapping file
				importSetup.setDataValues(nextLine);

				// skip over blank lines. check first couple cols
				if (!StringUtils.hasText(importSetup.getDataValues()[0]) && !StringUtils.hasText(importSetup.getDataValues()[1]) && !StringUtils.hasText(importSetup.getDataValues()[2])) {
					continue;
				}

				importLog.incTotalRecords(); // includes records that cannot be exported due to some error

				// allow subclasses to custom generate revisedProjName (e.g. append unit/site to projName), which
				// is used everywhere a projName is needed in the import
				// this needs to be called for each record because site could differ for each record
				generateRevisedProjName(importDefinition, importSetup);
	
				// find existing Patient. possibly create new Patient
				if ((handlingEvent = patientExistsHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					continue;
				}

				// if no errors, continue processing import record. importSetup patientCreated indicates whether a
				// new Patient record was created or an existing Patient record was found (and given no errors, this 
				// means that all import definition flags were successfully met such that the record can be imported
				// with either a new or existing Patient)
				// (this goes for Status, Visit and instrument as well)

				// if Patient MUST_EXIST then importing assessment data, so do not deal with creating ContactInfo
				if (!importDefinition.getPatientExistRule().equals(MUST_EXIST)) {
					if ((handlingEvent = contactInfoExistsHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
						continue;
					}
				}
				
				// because caregiverExistsHandling may be reused for multiple Caregiver instances if data file has multiple Caregivers, it does not
				// directly set entities on importSetup like other existsHandling methods; instead it passes flags and instantiated entities back 
				// via the returned Event, which has attributes, and the values of these attributes are then use to set the Caregiver.
				
				// note that Caregiver and Caregiver ContactInfo are tightly bound such that both are handled together since a ContactInfo
				// record for a Caregiver has an association to that Caregiver. so import of both Caregiver and their ContactInfo data
				// are handled in the same method
				if ((handlingEvent = caregiverAndContactInfoExistsHandling(context, errors, importDefinition, importSetup, importLog, 
						importSetup.getIndexCaregiverFirstName(), importSetup.getIndexCaregiverLastName(), importSetup.getIndexCaregiverRelation(),
						importSetup.getIndexCaregiverContactInfoAddress(), importSetup.getIndexCaregiverContactInfoCity(),
						importSetup.getIndexCaregiverContactInfoState(), importSetup.getIndexCaregiverContactInfoZip(),
						importSetup.getIndexCaregiverContactInfoPhone1(), importSetup.getIndexCaregiverContactInfoPhone2(),
						importSetup.getIndexCaregiverContactInfoEmail(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					continue;
				}
				importSetup.setCaregiverCreated((Boolean) handlingEvent.getAttributes().get("caregiverCreated"));
				importSetup.setCaregiverExisted((Boolean) handlingEvent.getAttributes().get("caregiverExisted"));
				if (importSetup.isCaregiverCreated() || importSetup.isCaregiverExisted()) {
					importSetup.setCaregiver((Caregiver) handlingEvent.getAttributes().get("caregiver"));
					importSetup.setCaregiverContactInfoCreated((Boolean) handlingEvent.getAttributes().get("caregiverContactInfoCreated"));
					importSetup.setCaregiverContactInfoExisted((Boolean) handlingEvent.getAttributes().get("caregiverContactInfoExisted"));
					if (importSetup.isCaregiverContactInfoCreated() || importSetup.isCaregiverContactInfoExisted()) {
						importSetup.setCaregiverContactInfo((ContactInfo) handlingEvent.getAttributes().get("caregiverContactInfo"));
					}
				}
				
				// support importing two caregivers (e.g. Mother and Father for child patients)				
				if ((handlingEvent = caregiverAndContactInfoExistsHandling(context, errors, importDefinition, importSetup, importLog, 
						importSetup.getIndexCaregiver2FirstName(), importSetup.getIndexCaregiver2LastName(), importSetup.getIndexCaregiver2Relation(), 
						importSetup.getIndexCaregiver2ContactInfoAddress(), importSetup.getIndexCaregiver2ContactInfoCity(),
						importSetup.getIndexCaregiver2ContactInfoState(), importSetup.getIndexCaregiver2ContactInfoZip(),
						importSetup.getIndexCaregiver2ContactInfoPhone1(), importSetup.getIndexCaregiver2ContactInfoPhone2(), 
						importSetup.getIndexCaregiver2ContactInfoEmail(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					continue;
				}
				importSetup.setCaregiver2Created((Boolean) handlingEvent.getAttributes().get("caregiverCreated"));
				importSetup.setCaregiver2Existed((Boolean) handlingEvent.getAttributes().get("caregiverExisted"));
				if (importSetup.isCaregiver2Created() || importSetup.isCaregiver2Existed()) {
					importSetup.setCaregiver2((Caregiver) handlingEvent.getAttributes().get("caregiver"));
					importSetup.setCaregiver2ContactInfoCreated((Boolean) handlingEvent.getAttributes().get("caregiverContactInfoCreated"));
					importSetup.setCaregiver2ContactInfoExisted((Boolean) handlingEvent.getAttributes().get("caregiverContactInfoExisted"));
					if (importSetup.isCaregiver2ContactInfoCreated() || importSetup.isCaregiver2ContactInfoExisted()) {
						importSetup.setCaregiver2ContactInfo((ContactInfo) handlingEvent.getAttributes().get("caregiverContactInfo"));
					}
				}

				// determine if Patient is Enrolled in Project. possibly create new EnrollmentStatus
				if ((handlingEvent = enrollmentStatusExistsHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					continue;
				}
						
					
				Event instrHandlingEvent = null;
				if (!importDefinition.getPatientOnlyImport()) {
			
					// find matching Visit. possibly create new Visit
					if ((handlingEvent = visitExistsHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
						continue;
					}

					// up to 15 instruments can be imported from a single data file. for each instrument specified
					// in the import definition, find matching instrument. possibly create new instrument. 

					// if there are multiple instruments, any single instrument could not exist, exist without data or exist with data.
					// it is fine if some do not exist and others exist without data. but if any instrument is encountered that has data
					// it will not be overwritten and that is considered an error for the entire record.
					
					// i.e. all instruments are assumed to be in unison in terms of whether they have had data entered (or imported) or not.

					// so if the first instrument exists with data such that the record will not be imported (because data cannot be
					// overwritten and quite possibly the data file is being re-imported because it is cumulative) the import record
					// is aborted, i.e. do not bother trying to import data for the other instruments in the data record
					// note: if in fact the first instrument does not have data but a subsequent instrument in the same record does, then
					// the import is aborted even though the assumption has failed, i.e. some instruments have data entered but not all of them
					
					// additionally, if any one of them has an error such that it cannot be imported then none of them are imported,
					// i.e. that import record is aborted (e.g. instrument12 could have a data truncation error)


					instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
							importDefinition.getInstrType(), importDefinition.getInstrVer(), importDefinition.getInstrMappingAlias(),
							importSetup.getIndexInstrDcDate(), importSetup.getIndexInstrDcStatus(),
							"instrument", "instrCreated", "instrExisted", "instrExistedWithData", "instrExistedWithDataNoUpdate", lineNum);
					if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
						continue;
					}

					if (StringUtils.hasText(importDefinition.getInstrType2())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType2(), importDefinition.getInstrVer2(), importDefinition.getInstrMappingAlias2(),
								importSetup.getIndexInstr2DcDate(), importSetup.getIndexInstr2DcStatus(),
								"instrument2", "instr2Created", "instr2Existed", "instr2ExistedWithData", "instr2ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							// note: code is unlikely to get here because multiple instruments are assumed to be in unison.
							continue;
						}
					}
	
					if (StringUtils.hasText(importDefinition.getInstrType3())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType3(), importDefinition.getInstrVer3(), importDefinition.getInstrMappingAlias3(),
								importSetup.getIndexInstr3DcDate(), importSetup.getIndexInstr3DcStatus(),
								"instrument3", "instr3Created", "instr3Existed", "instr3ExistedWithData", "instr3ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}
					
					if (StringUtils.hasText(importDefinition.getInstrType4())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType4(), importDefinition.getInstrVer4(), importDefinition.getInstrMappingAlias4(),
								importSetup.getIndexInstr4DcDate(), importSetup.getIndexInstr4DcStatus(),
								"instrument4", "instr4Created", "instr4Existed", "instr4ExistedWithData", "instr4ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}
					
					if (StringUtils.hasText(importDefinition.getInstrType5())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType5(), importDefinition.getInstrVer5(), importDefinition.getInstrMappingAlias5(),
								importSetup.getIndexInstr5DcDate(), importSetup.getIndexInstr5DcStatus(),
								"instrument5", "instr5Created", "instr5Existed", "instr5ExistedWithData", "instr5ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}
					
					if (StringUtils.hasText(importDefinition.getInstrType6())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType6(), importDefinition.getInstrVer6(), importDefinition.getInstrMappingAlias6(),
								importSetup.getIndexInstr6DcDate(), importSetup.getIndexInstr6DcStatus(),
								"instrument6", "instr6Created", "instr6Existed", "instr6ExistedWithData", "instr6ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}
					
					if (StringUtils.hasText(importDefinition.getInstrType7())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType7(), importDefinition.getInstrVer7(), importDefinition.getInstrMappingAlias7(),
								importSetup.getIndexInstr7DcDate(), importSetup.getIndexInstr7DcStatus(),
								"instrument7", "instr7Created", "instr7Existed", "instr7ExistedWithData", "instr7ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}
					
					if (StringUtils.hasText(importDefinition.getInstrType8())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType8(), importDefinition.getInstrVer8(), importDefinition.getInstrMappingAlias8(), 
								importSetup.getIndexInstr8DcDate(), importSetup.getIndexInstr8DcStatus(),
								"instrument8", "instr8Created", "instr8Existed", "instr8ExistedWithData", "instr8ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}
					
					if (StringUtils.hasText(importDefinition.getInstrType9())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType9(), importDefinition.getInstrVer9(),importDefinition.getInstrMappingAlias9(), 
								importSetup.getIndexInstr9DcDate(), importSetup.getIndexInstr9DcStatus(),
								"instrument9", "instr9Created", "instr9Existed", "instr9ExistedWithData", "instr9ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}
					
					if (StringUtils.hasText(importDefinition.getInstrType10())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType10(), importDefinition.getInstrVer10(),importDefinition.getInstrMappingAlias10(), 
								importSetup.getIndexInstr10DcDate(), importSetup.getIndexInstr10DcStatus(),
								"instrument10", "instr10Created", "instr10Existed", "instr10ExistedWithData", "instr10ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}
					
					if (StringUtils.hasText(importDefinition.getInstrType11())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType11(), importDefinition.getInstrVer11(), importDefinition.getInstrMappingAlias11(),
								importSetup.getIndexInstr11DcDate(), importSetup.getIndexInstr11DcStatus(),
								"instrument11", "instr11Created", "instr11Existed", "instr11ExistedWithData", "instr11ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}
					
					if (StringUtils.hasText(importDefinition.getInstrType12())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType12(), importDefinition.getInstrVer12(), importDefinition.getInstrMappingAlias12(),
								importSetup.getIndexInstr12DcDate(), importSetup.getIndexInstr12DcStatus(),
								"instrument12", "instr12Created", "instr12Existed", "instr12ExistedWithData", "instr12ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}
					
					if (StringUtils.hasText(importDefinition.getInstrType13())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType13(), importDefinition.getInstrVer13(), importDefinition.getInstrMappingAlias13(),
								importSetup.getIndexInstr13DcDate(), importSetup.getIndexInstr13DcStatus(),
								"instrument13", "instr13Created", "instr13Existed", "instr13ExistedWithData", "instr13ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}
					
					if (StringUtils.hasText(importDefinition.getInstrType14())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType14(), importDefinition.getInstrVer14(), importDefinition.getInstrMappingAlias14(),
								importSetup.getIndexInstr14DcDate(), importSetup.getIndexInstr14DcStatus(),
								"instrument14", "instr14Created", "instr14Existed", "instr14ExistedWithData", "instr14ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}
					
					if (StringUtils.hasText(importDefinition.getInstrType15())) {
						instrHandlingEvent = processInstrumentExistsHandling(context, errors, importDefinition, importSetup, importLog,
								importDefinition.getInstrType15(), importDefinition.getInstrVer15(), importDefinition.getInstrMappingAlias15(),
								importSetup.getIndexInstr15DcDate(), importSetup.getIndexInstr15DcStatus(),
								"instrument15", "instr15Created", "instr15Existed", "instr15ExistedWithData", "instr15ExistedWithDataNoUpdate",  lineNum);
						if (instrHandlingEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
							continue;
						}
					}

										
					// if every instrument returned "instrExistedWithDataNoUpdate" then consider the import record as a whole as already existing such
					// that there is no import
					// otherwise, if at least one instrument in the bundle was imported, consider the import record as a whole as imported (but above
					// a warning is given for each instrument that returned "instrExistedWithDataNoUpdate")
					if (importSetup.isInstrExistedWithDataNoUpdate() &&
							(!StringUtils.hasText(importDefinition.getInstrType2()) || importSetup.isInstr2ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType3()) || importSetup.isInstr3ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType4()) || importSetup.isInstr4ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType5()) || importSetup.isInstr5ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType6()) || importSetup.isInstr6ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType7()) || importSetup.isInstr7ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType8()) || importSetup.isInstr8ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType9()) || importSetup.isInstr9ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType10()) || importSetup.isInstr10ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType11()) || importSetup.isInstr11ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType12()) || importSetup.isInstr12ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType13()) || importSetup.isInstr13ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType14()) || importSetup.isInstr14ExistedWithDataNoUpdate()) &&
							(!StringUtils.hasText(importDefinition.getInstrType15()) || importSetup.isInstr15ExistedWithDataNoUpdate())){
						// this applies to the current record as a whole, not to individual instruments if there are multiple in data file
						importLog.incAlreadyExist();
						continue;
					}

				}


// X-do pedi attachments (consents) when working in the following with LavaFile stuff				
//   download definition mapping file
//	 download data file				
// X-change mapping file format to 3 rows: row 2 is entity type, row 3 is property name (if
//  both are blank then defaults to 1st instrument and prop name == column name (row 1))				
// X-pedi new patient history import (data file with all columns, not cut off at 256 cols)
// X-should only create caregiver and caregiver contactInfo records if data exists
// X-log totals do not reflect caregivers and contactInfo records (but they could. not bothering with
//   caregiverContactInfo totals since that is tightly bound with caregiver)
// X-SPDC History Form 2 metadata populated, e.g. marco_lab...history_timestamp (which will be used for versioning)				
// X-SPDC History Form 2 only showing when run server in non-debug mode - KNOWN ISSUE				
// X-confirm that data is being loaded correctly (incl. caregiver livesWithPatient, ContactInfo is
//   for caregiver)
// X-need separate definitions for old and current versions because var names from old
//  need to map to current, e.g. field5 old maps to field6 current, whereas for current
//  field5 maps to field5		
// X-use opencsv				
// X-make mapping definition name longer (50?)				
// X-import definition UI cleanup (for now move Project near top, ahead of selection of Import
//  X-skip logic:
//      X-if Only Import Patients then disable Visit and Instrument fields
//      X-if Patient Must Not Exist rule is selected then other exist rules should be disabled and
//         set to Must Not Exist
//      X-if Caregiver Instrument then enable Caregiver Instrument Exist Rule
// X-help text all over the import definition (incl. if mapping file changes, have to re-upload)
// X-put "instructions" on crmsImport.jsp as well stating about log file and saving data file				
// X-default import definition field values in PediImportDefinitionHandler to the common case of 
//      importing instrument data
// X-default pedi specific import definition fields via a PediImportDefinition customization, i.e.
//      default to caregiver instrument

// X-subject importDefinitions and importLogs lists to Project filtering
// X-get rid of import section, default to imports section (make sure regular import fails
//  on SPDC history import)				
// X-allow Edit of import log where only the Notes field can be edited so can put in notes about things that happened in import				
// X-crmsAllImportLogs needs a QuickFilter
// X-add creation of entities as importLog CREATED messages
// X-call calculate on save
// X-custom, hard-coded truncation for certain pediLAVA imports, e.g. Sensory Profile Child
// make the mapping data file bind with Spring so on refresh it is not lost, e.g. when a required
//   field error on anohter field. keep in mind that tried this already using the Spring facility to bind
//   an uploaded file but ran into problems integrating that into our implementation of uploading files
//   so this is not a minor item
// test that deleting import definition deletes the mapping file
// figure out impact of user changing the import definition name since it is used to generate the path
//  for storing the mapping file in the repo. update that path whenever user saves import definition
//  and name field isDirty?
// why does Browse button have _ in it? (is this browser specific?)				
// X-importLogContent / crmsImportLogContent format log summary results in a table
// validation check: compare DOB to form data collection date, as sometimes user enters current date for DOB

// allowInstrUpdate flag has been enabled for SYSTEM_ADMIN role and tested, but need to test what happens
//   on error because modifying a Hibernate persistent instance (attached to the Hibernate session) so wouldn't
//   changes be persisted if there is no rollback?
// UPDATE: if database error, if swallow exception, then that is a problem because will rollback
//         if not a database error there will be a commit at the end of the request, so need to evict modified
//         objects so they will not be persisted. this is done in insert mode now.
				
// add useful Filter (and sorts) to definition and log lists, e.g. add instrument to Filter and compare to any 
//   of 10 instruments allowed per data file, if the filter specified instrument matches any one of the 10, show 
//   the import definition, import log

// X-add id columns of entities created or updated to the importLog detail				
				
// any remaining TODOs in import-related code, config, Hibernate mapping, jsp, etc.?
				
// X-make sure import events are audited to audit event log
// implement project authorization for crmsImportDefinition, crmsImport, crmsImportLog  UPDATE: may be done in lava2 implementation?
			
// other majors:
// X-FileMaker patient import
// X-FileMaker Sensory Profile Child import
// X-REDCap Sensory Profile Child import
// X-for REDCap assessment imports, test patient firstName against nickname if match against firstName failed
// X-BASC import
// ?? create preview mode, at least for development, that does not do anything to db
				
// Rankin TODOs:
//   X-migrate to MAC LAVA
//				
//   X-implement startDataRow (defaults to 2 for all imports done prior to implementation)
//				
//   X-match existing Visit on Visit Type if user sets flag to do so. even if not, Visit Type could still be
//     used when creating new Visits (default is false)				
//     NOTE: implemented but currently restricted to SYSTEM_ADMIN until decide if it is a good idea to not 
//           have to match on visitType (match defaults to TRUE)
//				
//   X-match existing	Visit on user specified time window, in days, around the visitDate in data file. set
//   to 0 for an exact date match (need info text with this) (0 is the default)				
//   (columns and metadata to support already added to db)
//      if multiple visits matched, then error
//
//   expand to work with multiple instruments 
//      X-crmsImportDefinition will have inputs for up to 10 instruments 
//   	  (columns and metadata to support already added to db, i.e. 2 thru 10 instrType/instrVer)
//      - will have to rework instrumentExistsHandling to go thru each specified instrument
// 		- use of instrType,instrVer for generateLocation for data files will just have to use that of
//   		the first instrument chosen
//                after definitionName (i.e. definitionNameEncoded)
//		  UPDATE: believe this is incorrect. both mapping files and data files go under a folder named
//   importDefinition has UI for all 10 instrType/instrVer even though could just use the values in the mapping
//    file (row 2) to determine what instrument is being imported. but there is clarity in what exactly the import
//    definition is importing by having user explicitly specify which instruments are being imported, AND had
//    to put a caregiver flag for each instrument so pretty much had to have user specify instrument types.
//    and it may facilitate some future things and makes it a bit easier to iterate thru the instruments
//    to process. For Rankin special case where IAS and Big Five Inventory have two instances in the data file
//    could even use the version field for distinguishing (version in mapping file appended to instr type in row 2?)				
//   need to validate mapping file that every row 2 instrType matches an existing instrType (and perhaps that
//    they match the instruments specified in importDefinition)
//   can use these instrument types to instantiate the instrument instance, but when setting property, will
//    be matching mapping file instrType to get the correct instr instance, i.e. seems like importSetup will 
//    need a mapping structure of instrType to instr instance	
//
//
//   IAS and Big Five Inventory have two instances in the same data file, one for pre and one for current,
//   so will be instantiating two of each so have custom entity name in mapping file to facilitate this
//   and keep each instance separate, e.g. "ias_pre" and "ias_current". these entity names can also be
//   be used to populate the timeQuest property to either "PRIOR" (or one of the "TIMEn" options?) or "CURRENT"
//
//   UDS instruments that will be submitted to NACC. 
//   packetType, visitNum, initials will be part of the data file (input by MAC examiner, not caregiver)
//   formVer will be part of import definition
//   formId is part of instrument constructor
//   ADCID hard-coded
//
//   PCRS may not be collected at some visits. So if every value for an instrument is null do not create it.
//
//   Possibly attempt to determine the value of TimeQuest based on which visit it is. 				
//
// 2.0 support instrument update with a confirmation flow state showing the user current and new values	
//     determine if update mode will just update existing data, or is a hybrid between creating new records
//     and updating existing records (will need to set importLog counters accordingly)
// 2.0  expand to work with files in folders for special not-exactly-import use cases:
//      a) for instruments that load individual patient files, e.g. e-prime instruments
//      b) for PDFs that should be attached to an existing instrument
// 2.0: validation, i.e. read property metadata to obtain type, list of valid values
// 2.0: validation on string field lengths. use max length from metadata. use importDefinition truncate
//      flag (already added to schema) to either truncate or give error.
//      in case where no max length specified, if database truncation exception thrown, then consult
//      truncate flag to truncate or give error, but what length to truncate to since do not know
//      length of database column?				
// 3.0  expand to work with files in folders for special not-exactly-import use cases:
//		      a) for instruments that load individual patient files, e.g. e-prime instruments
//		      b) for PDFs that should be attached to an existing instrument
//		i.e. iterate thru the files in a folder processing them one at a time
// 3.0 import detail data files, e.g. Freesurfer 5.1 data				
				
				if ((handlingEvent = otherExistsHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					this.updateEntityCounts(importSetup, importLog);
					continue;
				}
				
				// iterate thru the values of the current import record, setting each value on the property of an entity, as 
				// determined by the importDefinition mapping file
				if ((handlingEvent = setPropertyHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					// any time after setProperty, must call evictInstruments on processing error and before continuing to next record, so that any data that has been
					// set on instruments does not persist, but to the error
					this.evictInstruments(importDefinition, importSetup);
					continue;
				}

				// if patient only import and nothing was created then nothing to persist
				if (importDefinition.getPatientOnlyImport()) {
					if (!importSetup.isPatientCreated() && !importSetup.isContactInfoCreated() && !importSetup.isCaregiverCreated() 
							&& !importSetup.isCaregiver2Created() && !importSetup.isEnrollmentStatusCreated()) {
						importLog.incAlreadyExist();
						this.updateEntityCounts(importSetup, importLog);
						continue;
					}
				}


				if (!importDefinition.getPatientOnlyImport()) {
					Event instrCaregiverHandlingEvent = null;
				
					// if definition has flag set that this is a caregiver instrument, set the caregiver on the instrument
					if (importDefinition.getInstrCaregiver() != null && importDefinition.getInstrCaregiver()) {
						if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
							importSetup.getInstrument(), importDefinition.getInstrType(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
							this.evictInstruments(importDefinition, importSetup);
							continue;
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType2())) {
						if (importDefinition.getInstrCaregiver2() != null && importDefinition.getInstrCaregiver2()) {
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument2(), importDefinition.getInstrType2(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}
						
					if (StringUtils.hasText(importDefinition.getInstrType3())) {
						if (importDefinition.getInstrCaregiver3() != null && importDefinition.getInstrCaregiver3()) {
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument3(), importDefinition.getInstrType3(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType4())) {
						if (importDefinition.getInstrCaregiver4() != null && importDefinition.getInstrCaregiver4()) {
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument4(), importDefinition.getInstrType4(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType5())) {
						if (importDefinition.getInstrCaregiver5() != null && importDefinition.getInstrCaregiver5()) {
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument5(), importDefinition.getInstrType5(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType6())) {
						if (importDefinition.getInstrCaregiver6() != null && importDefinition.getInstrCaregiver6()) {						
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument6(), importDefinition.getInstrType6(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType7())) {
						if (importDefinition.getInstrCaregiver7() != null && importDefinition.getInstrCaregiver7()) {						
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument7(), importDefinition.getInstrType7(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType8())) {
						if (importDefinition.getInstrCaregiver8() != null && importDefinition.getInstrCaregiver8()) {						
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument8(), importDefinition.getInstrType8(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType9())) {
						if (importDefinition.getInstrCaregiver9() != null && importDefinition.getInstrCaregiver9()) {						
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument9(), importDefinition.getInstrType9(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType10())) {
						if (importDefinition.getInstrCaregiver10() != null && importDefinition.getInstrCaregiver10()) {						
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument10(), importDefinition.getInstrType10(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType11())) {
						if (importDefinition.getInstrCaregiver11() != null && importDefinition.getInstrCaregiver11()) {						
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument11(), importDefinition.getInstrType11(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType12())) {
						if (importDefinition.getInstrCaregiver12() != null && importDefinition.getInstrCaregiver12()) {						
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument12(), importDefinition.getInstrType12(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType13())) {
						if (importDefinition.getInstrCaregiver13() != null && importDefinition.getInstrCaregiver13()) {						
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument13(), importDefinition.getInstrType13(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType14())) {
						if (importDefinition.getInstrCaregiver14() != null && importDefinition.getInstrCaregiver14()) {						
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument14(), importDefinition.getInstrType14(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}

					if (StringUtils.hasText(importDefinition.getInstrType15())) {
						if (importDefinition.getInstrCaregiver15() != null && importDefinition.getInstrCaregiver15()) {						
							if ((instrCaregiverHandlingEvent = setInstrumentCaregiver(context, errors, importDefinition, importSetup, importLog, 
								importSetup.getInstrument15(), importDefinition.getInstrType15(), lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
								this.evictInstruments(importDefinition, importSetup);
								continue;
							}
						}
					}
				}


				// note that if there is a single instrument in the data file record and it already existed with data then
				// will not get to this point as that is considered an error in the sense that data cannot be imported (although
				// an update mode may be added where the existing data can be updated)
				// when there are multiple instruments in a single import record, if at least one of the instruments was created
				// or existed without data entry yet, then will get to this point, even if some or all of the other instruments
				// existed with data such that they are not imported. but, if every instrument existed with data already entered
				// then that is considered an error and will not get to this point

				if ((handlingEvent = saveImportRecord(context, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					this.evictInstruments(importDefinition, importSetup);
					continue; // do not update any counts as nothing was imported for this record				
				}
				

				// at this point all values of the import record have been successfully set on entity properties

				// update counts
				
				// applies to entire import record. increment a global count of how many data file records were imported (i.e. created)
				// or updated
				// note: it is simply enough to check for the existence of the "update" attribute, i.e. do not need to check its value
				
				// if the data file maps to multiple instruments there could be combination of created instruments, existing instruments
				// without data, and existing instruments with data already entered (but not a situation where all instruments existed
				// with data already entered as would not get that far in this case). as long as here was one instrumet that
				// was either created or existed without data and was updated, then increment the imported counter
				if (!importDefinition.getPatientOnlyImport()) {
					if (instrHandlingEvent.getAttributes() != null && instrHandlingEvent.getAttributes().get("update") != null) {
						importLog.incUpdated();
					}
					else {
						importLog.incImported();
					}
				}
				else {
					// before saving already handled situation where nothing created for patient only import, so
					// know if got this far that something was created, thus increment the import count
					importLog.incImported();
				}
				
				// these counts apply to specific entities within an import record. note that if processing was aborted for an import 
				// record the entity counts are not updated, i.e. these only pertain to the records that were successfully imported
				// (would be erroneous to record that a Patient was created when there is an error processing other parts of that
				// import record such that the import record is aborted)
				
				// if an instrument already existed with data we will not get here but that will be reported for each such import
				// record in the importLog detail messages and in that case everything already existed: Patient, EnrollmentStatus,
				// Visit and Instrument
				updateEntityCounts(importSetup, importLog);
			}
		}

		// at this point, returnEvent success means the success of the overall import. individual records 
		// may have had errors, which are logged as importLog messages and the total error count is incremented
		// returnEvent error means that the import failed as a whole and error msg is put in the command
		// object errors to be displayed
		// note: if get to this point then the event will always be SUCCESS because the only way the import as a whole will not complete
		// successfully is if a database transaction exception occurred, marking the transaction for rollback, in which cas the exception
		// propagates all the way up and never get to this code
		if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
			importLog.save();
			context.getFlowScope().put("importLogId", importLog.getId());
		}
		
		return returnEvent;
	}	
	

	protected Event validateAndMapDataFile(BindingResult errors, ImportDefinition importDefinition, ImportSetup importSetup) throws Exception {
		CrmsImportSetup crmsImportSetup = (CrmsImportSetup) importSetup;
		
		if (super.validateAndMapDataFile(errors, importDefinition, importSetup).getId().equals(ERROR_FLOW_EVENT_ID)) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
				
		// set indices here as this only needs to be done once for the entire data file. note that we are finding
		// the indices in the data file as a given property may have a different index in the mapping file and data
		// file, and these indices are used to access the data
		
		// ** the import definition mapping file second row must have entity string and third row must have 
		// property string that match exactly the entity and property name strings below  
	
		// look up the column indices of fields in the import data file that are required to search for existing
		// entities and/or populate new entities, and record the indices to be used in processing each import record
		
		// required fields for creating new Patient/EnrollmentStatus/Visit/instruments which could have the same 
		// uniform value across all records imported from a data file may be specified as part of the import 
		// definition rather then being supplied in the data file. but the data file takes precedence so first 
		// check the data file and set the index if the field has a value in the data file.
		
		// note that the entity and property are on separate lines of the mapping file and thus in separate arrays,
		// so need to check the two arrays in conjunction with each other (could have just had a single property
		// row with entity.property but if there are multiple instruments in the data file with many properties, 
		// easier to edit the mapping file with instrument names across the entity column headers instead of 
		// editing entity.property format for each property)
		
		setDataFilePropertyIndex(importSetup, "indexPatientPIDN", "patient", "PIDN");
		// note: if importing de-identified data then the data file de-identified ID column will be mapped to firstName and lastName will be mapped as "STATIC:DE-IDENTIFIED"
		setDataFilePropertyIndex(importSetup, "indexPatientFirstName", "patient", "firstName");
		// if this is a de-identified patient import there will not be a lastName in the data file so do not find its index
		if (ArrayUtils.indexOf(importSetup.getMappingCols(), STATIC_INDICATOR + DEIDENTIFIED, 0) == -1) {
			setDataFilePropertyIndex(importSetup, "indexPatientLastName", "patient", "lastName");
		}
		setDataFilePropertyIndex(importSetup, "indexPatientBirthDate", "patient", "birthDate");
		setDataFilePropertyIndex(importSetup, "indexPatientGender", "patient", "gender");

		setDataFilePropertyIndex(importSetup, "indexContactInfoAddress", "contactInfo", "address");
		setDataFilePropertyIndex(importSetup, "indexContactInfoCity", "contactInfo", "city");
		setDataFilePropertyIndex(importSetup, "indexContactInfoState", "contactInfo", "state");
		setDataFilePropertyIndex(importSetup, "indexContactInfoZip", "contactInfo", "zip");
		setDataFilePropertyIndex(importSetup, "indexContactInfoPhone1", "contactInfo", "phone1");
		setDataFilePropertyIndex(importSetup, "indexContactInfoPhone2", "contactInfo", "phone2");
		setDataFilePropertyIndex(importSetup, "indexContactInfoEmail", "contactInfo", "email");

		setDataFilePropertyIndex(importSetup, "indexCaregiverFirstName", "caregiver", "firstName");
		setDataFilePropertyIndex(importSetup, "indexCaregiverLastName", "caregiver", "lastName");
		setDataFilePropertyIndex(importSetup, "indexCaregiverRelation", "caregiver", "relation");
		setDataFilePropertyIndex(importSetup, "indexCaregiverContactInfoAddress", "caregiverContactInfo", "address");
		setDataFilePropertyIndex(importSetup, "indexCaregiverContactInfoCity", "caregiverContactInfo", "city");
		setDataFilePropertyIndex(importSetup, "indexCaregiverContactInfoState", "caregiverContactInfo", "state");
		setDataFilePropertyIndex(importSetup, "indexCaregiverContactInfoZip", "caregiverContactInfo", "zip");
		setDataFilePropertyIndex(importSetup, "indexCaregiverContactInfoPhone1", "caregiverContactInfo", "phone1");
		setDataFilePropertyIndex(importSetup, "indexCaregiverContactInfoPhone2", "caregiverContactInfo", "phone2");
		setDataFilePropertyIndex(importSetup, "indexCaregiverContactInfoEmail", "caregiverContactInfo", "email");
		
		setDataFilePropertyIndex(importSetup, "indexCaregiver2FirstName", "caregiver2", "firstName");
		setDataFilePropertyIndex(importSetup, "indexCaregiver2LastName", "caregiver2", "lastName");
		setDataFilePropertyIndex(importSetup, "indexCaregiver2Relation", "caregiver2", "relation");
		setDataFilePropertyIndex(importSetup, "indexCaregiver2ContactInfoAddress", "caregiver2ContactInfo", "address");
		setDataFilePropertyIndex(importSetup, "indexCaregiver2ContactInfoCity", "caregiver2ContactInfo", "city");
		setDataFilePropertyIndex(importSetup, "indexCaregiver2ContactInfoState", "caregiver2ContactInfo", "state");
		setDataFilePropertyIndex(importSetup, "indexCaregiver2ContactInfoZip", "caregiver2ContactInfo", "zip");
		setDataFilePropertyIndex(importSetup, "indexCaregiver2ContactInfoPhone1", "caregiver2ContactInfo", "phone1");
		setDataFilePropertyIndex(importSetup, "indexCaregiver2ContactInfoPhone2", "caregiver2ContactInfo", "phone2");
		setDataFilePropertyIndex(importSetup, "indexCaregiver2ContactInfoEmail", "caregiver2ContactInfo", "email");
		
		setDataFilePropertyIndex(importSetup, "indexEsProjName", "enrollmentStatus", "projName");
		setDataFilePropertyIndex(importSetup, "indexEsStatusDate", "enrollmentStatus", "statusDate");
		setDataFilePropertyIndex(importSetup, "indexEsStatus", "enrollmentStatus", "statusDesc");

		setDataFilePropertyIndex(importSetup, "indexVisitDate", "visit", "visitDate");
		setDataFilePropertyIndex(importSetup, "indexVisitTime", "visit", "visitTime");
		setDataFilePropertyIndex(importSetup, "indexVisitType", "visit", "visitType");
		setDataFilePropertyIndex(importSetup, "indexVisitWith", "visit", "visitWith");
		setDataFilePropertyIndex(importSetup, "indexVisitLoc", "visit", "visitLoc");
		setDataFilePropertyIndex(importSetup, "indexVisitStatus", "visit", "visitStatus");

		//TODO: for each instrument, if properties not found when matching instrType, try matching instrAlias
		setDataFilePropertyIndex(importSetup, "indexInstrDcDate", ((CrmsImportDefinition)importDefinition).getInstrType(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstrDcStatus", ((CrmsImportDefinition)importDefinition).getInstrType(), "dcStatus");
		
		setDataFilePropertyIndex(importSetup, "indexInstr2DcDate", ((CrmsImportDefinition)importDefinition).getInstrType2(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr2DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType2(), "dcStatus");

		setDataFilePropertyIndex(importSetup, "indexInstr3DcDate", ((CrmsImportDefinition)importDefinition).getInstrType3(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr3DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType3(), "dcStatus");
		
		setDataFilePropertyIndex(importSetup, "indexInstr4DcDate", ((CrmsImportDefinition)importDefinition).getInstrType4(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr4DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType4(), "dcStatus");
		
		setDataFilePropertyIndex(importSetup, "indexInstr5DcDate", ((CrmsImportDefinition)importDefinition).getInstrType5(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr5DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType5(), "dcStatus");

		setDataFilePropertyIndex(importSetup, "indexInstr6DcDate", ((CrmsImportDefinition)importDefinition).getInstrType6(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr6DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType6(), "dcStatus");

		setDataFilePropertyIndex(importSetup, "indexInstr7DcDate", ((CrmsImportDefinition)importDefinition).getInstrType7(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr7DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType7(), "dcStatus");

		setDataFilePropertyIndex(importSetup, "indexInstr8DcDate", ((CrmsImportDefinition)importDefinition).getInstrType8(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr8DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType8(), "dcStatus");

		setDataFilePropertyIndex(importSetup, "indexInstr9DcDate", ((CrmsImportDefinition)importDefinition).getInstrType9(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr9DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType9(), "dcStatus");

		setDataFilePropertyIndex(importSetup, "indexInstr10DcDate", ((CrmsImportDefinition)importDefinition).getInstrType10(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr10DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType10(), "dcStatus");

		setDataFilePropertyIndex(importSetup, "indexInstr11DcDate", ((CrmsImportDefinition)importDefinition).getInstrType11(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr11DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType11(), "dcStatus");

		setDataFilePropertyIndex(importSetup, "indexInstr12DcDate", ((CrmsImportDefinition)importDefinition).getInstrType12(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr12DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType12(), "dcStatus");

		setDataFilePropertyIndex(importSetup, "indexInstr13DcDate", ((CrmsImportDefinition)importDefinition).getInstrType13(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr13DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType13(), "dcStatus");

		setDataFilePropertyIndex(importSetup, "indexInstr14DcDate", ((CrmsImportDefinition)importDefinition).getInstrType14(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr14DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType14(), "dcStatus");

		setDataFilePropertyIndex(importSetup, "indexInstr15DcDate", ((CrmsImportDefinition)importDefinition).getInstrType15(), "dcDate");
		setDataFilePropertyIndex(importSetup, "indexInstr15DcStatus", ((CrmsImportDefinition)importDefinition).getInstrType15(), "dcStatus");

		setOtherIndices((CrmsImportDefinition)importDefinition, crmsImportSetup);

		// error on entire import if either no PIDN or no FirstName/LastName in data file			
		if (crmsImportSetup.getIndexPatientPIDN() == -1 && 
				(crmsImportSetup.getIndexPatientFirstName() == -1 || crmsImportSetup.getIndexPatientLastName() == -1)) {
			LavaComponentFormAction.createCommandError(errors, "Insufficient Patient properties. Data file must have a property that maps to patient PIDN or patient firstName and lastName in the mapping file.");
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		// error on entire import if no visitDate in data file		
		else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && crmsImportSetup.getIndexVisitDate() == -1) {
			LavaComponentFormAction.createCommandError(errors, "Data file must hove a property that maps to visit visitDate in the mapping file");
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}

		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}
	

	
	/**
	 * Subclasses should override to generate custom projName
	 * 
	 * @return
	 */
	protected void generateRevisedProjName(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup) {
		importSetup.setRevisedProjName(importSetup.getIndexEsProjName() != -1 ? importSetup.getDataValues()[importSetup.getIndexEsProjName()] : importDefinition.getProjName());
	}

	
	/**
	 * Subclasses override this to set indices for custom imports. 
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @throws Exception
	 */
	protected void setOtherIndices(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup) throws Exception {
		// do nothing
	}


	
	
	/**
	 * patientExistsHandling
	 * 
	 * Determine whether patient exists or not and act accordingly based on the importDefinition settings.
	 * 
	 * The approach to logging is to log the error when it occurs within the method but have the 
	 * caller increment the error count if an error Event is returned (in which case processing of 
	 * the current record will abort and will go to the next import record).
	 * 
	 * @param context
	 * @param errors
	 * @param importDefinition
	 * @param importSetup
	 * @param lineNum
	 * @return SUCCESS Event if no import errors with current record; ERROR EVENT if errors
	 */
	protected Event patientExistsHandling(RequestContext context, BindingResult errors, 
			CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, CrmsImportLog importLog,
			int lineNum) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EntityBase.newFilterInstance();
		List<Patient> patientList = null;
		SimpleDateFormat formatter = new SimpleDateFormat(importDefinition.getDateFormat() != null ? importDefinition.getDateFormat() : DEFAULT_DATE_FORMAT);
		String dateOrTimeAsString = null;
		Date birthDate = null;
		int birthDateYear = 0;

		// search for existing patient
		Patient p = null;

		// convert the birthDate, if supplied in the data file, to a Date
		if (importSetup.getIndexPatientBirthDate() != -1) {
			dateOrTimeAsString = importSetup.getDataValues()[importSetup.getIndexPatientBirthDate()];
			
			if (importDefinition.getDateFormat().endsWith("/yy")) {
				// if 2 digit year, set start year to 1916 so birthDate year will be from 1916 to 2015, which covers the greatest probability
				// for children and adult patients
				Calendar tempCal = Calendar.getInstance();
				tempCal.clear();
				tempCal.set(Calendar.YEAR, 1916);
				formatter.set2DigitYearStart(tempCal.getTime());
			}
			formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
			try {
				birthDate = formatter.parse(dateOrTimeAsString);
			} catch (ParseException e) {
				// likely will not be called with leniency applied
				importLog.addErrorMessage(lineNum, "Patient.birthDate is an invalid Date format, Date:" + dateOrTimeAsString);
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
			if (importDefinition.getDateFormat().endsWith("/yy")) {
				// when data file has 2 digit years, reassign dataOrTimeAsString (used for log messages) to reflect the full 4 digit year 
				dateOrTimeAsString = new SimpleDateFormat(importDefinition.getDateFormat()).format(birthDate);
			}
		}

		filter.clearDaoParams();
		if (importSetup.getIndexPatientPIDN() != -1) {
			String pidnAsString = importSetup.getDataValues()[importSetup.getIndexPatientPIDN()];
			Long pidn = null;
			try {
				pidn = Long.valueOf(pidnAsString);
			} catch (NumberFormatException ex) {
				importLog.addErrorMessage(lineNum, "PIDN Is not a number="+ pidnAsString);
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
			filter.addIdDaoEqualityParam(pidn);
			p = (Patient) Patient.MANAGER.getById(pidn);
		}
		else if (ArrayUtils.indexOf(importSetup.getMappingCols(), STATIC_INDICATOR + DEIDENTIFIED, 0) != -1) {
			filter.addDaoParam(filter.daoEqualityParam("firstName", importSetup.getDataValues()[importSetup.getIndexPatientFirstName()]));
			filter.addDaoParam(filter.daoEqualityParam("lastName", DEIDENTIFIED));
		}
		else { 
			// birthDate is optional for search as it is often not part of data files
			if (birthDate != null && !importDefinition.getAllowExtremeDates()) {
				// if date format is yyyy for year part, the parser will allow any date into the future, even 5 digit dates, so 
				// have to do range checking to catch bad date errors
				java.util.Calendar birthDateCalendar = java.util.Calendar.getInstance();
				birthDateCalendar.setTime(birthDate);
				birthDateYear = birthDateCalendar.get(java.util.Calendar.YEAR);
				java.util.Calendar nowCalendar = java.util.Calendar.getInstance();
				int nowYear = nowCalendar.get(java.util.Calendar.YEAR);
				if (birthDateYear < (nowYear - 120) || birthDateYear > nowYear) {
					importLog.addErrorMessage(lineNum, "Patient birth date has an invalid Year. Birth Date:" + dateOrTimeAsString);
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}

				filter.addDaoParam(filter.daoEqualityParam("birthDate", birthDate));
			}

			// have already validated that firstName and lastName are present in the mapping definition file if PIDN is not
			setPatientNameMatchFilter(filter, importSetup);

			patientList = Patient.MANAGER.get(Patient.class, filter);

			if (patientList.size() == 1) {
				p = (Patient) patientList.get(0);
			}
			else if (patientList.size() > 1) {
				// this should never happen. if re-running import of a data file, should just be one 
				importLog.addErrorMessage(lineNum, "Multiple Patient records matched for patient firstName:" + importSetup.getDataValues()[importSetup.getIndexPatientFirstName()] +
					" lastName:" + importSetup.getDataValues()[importSetup.getIndexPatientLastName()]); 
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}

			// since matching on patient first and last name and birth date is prone to error, consider whether correcting one
			// of these would result in the Patient being found. if that were the case, then importing this patient results in 
			// a duplicate Patient created by the import (unless the Patient MUST_EXIST was set in the import definition). it is
			// a pain to remove a duplicate Patient with data and either merge that data with the pre-existing Patient or remove it.
			
			// the probability of incorrect data is higher when there is a partial but not complete match of an existing Patient
			// record. for example, the data file could have a record that matches a Patient on firstName and lastName but not
			// birthDate. but there is no way to know whether the birthDate is incorrect in the data file, or whether this is a
			// different patient that happens to have the same name as an existing patient. So the import must create the Patient
			// because otherwise there would be no way to import this Patient into LAVA.
		
			// however, these partial matches should be flagged as warnings so that the user can review the import log and for
			// each warning they should review whether a duplicate Patient was created.
			
			// here we will attempt to identify the following partial match scenarios and log them as warnings. note that if the
			// data file has a PIDN then there is no consideration of a partial match. 
			
			// the following partial matches are considered when there is a birthDate present in the data file
		
				// firstName and lastName match (birthDate may be incorrect) 
				
				// lastName and birthDate match (firstName may be incorrect)
			 
				// firstName and birthDate match (lastName may be incorrect)
			
				// birthDate matches (firstName and lastName may be incorrect)
		
			// the following partial matches are considered when there is no birthDate in the data file, just firstName and lastName

				// lastName matches (firstName may be incorrect)
		
		
			// note: at this point we are within the clause where a Patient is being matched on firstName and lastName and birthDate (if 
			// supplied) and so we know there was not a PIDN supplied in the data file


			if (p == null && !importDefinition.getPatientExistRule().equals(MUST_EXIST)) { // TODO: and not matching on de-identifiedID 
				Set<String> matchedPatients = new LinkedHashSet<String>();
				if (birthDate != null) { // birthDate is supplied in the data file
			
					// firstName and lastName match (birthDate may be incorrect) 
					filter.clearDaoParams();
					setPatientNameMatchFilter(filter, importSetup);
					patientList = Patient.MANAGER.get(Patient.class, filter);
					// iterate thru results and concatenate each Patient matched together for a the log warning message
					for (Patient matchedPatient : patientList) {
						matchedPatients.add(matchedPatient.getFullNameWithId());
					}
					if (matchedPatients.size() > 0) {
						importLog.addWarningMessage(lineNum, "Check for duplicate patients. Patient firstName:" + importSetup.getDataValues()[importSetup.getIndexPatientFirstName()] + " lastName:" + importSetup.getDataValues()[importSetup.getIndexPatientLastName()] + " birthDate:" + dateOrTimeAsString +
						" created and the following existing patients matched on first name and last name but not birthdate:" + matchedPatients.toString());
					}
	
					
					// lastName and birthDate match (firstName may be incorrect)
					matchedPatients.clear();
					filter.clearDaoParams();
					filter.addDaoParam(filter.daoEqualityParam("lastName", importSetup.getDataValues()[importSetup.getIndexPatientLastName()]));
					filter.addDaoParam(filter.daoEqualityParam("birthDate", birthDate));
					filter.addDaoParam(filter.daoNot(filter.daoEqualityParam("firstName", importSetup.getDataValues()[importSetup.getIndexPatientLastName()])));
					
					patientList = Patient.MANAGER.get(Patient.class, filter);
					// iterate thru results and concatenate each Patient matched together for a the log warning message
					for (Patient matchedPatient : patientList) {
						matchedPatients.add(matchedPatient.getFullNameWithId());
					}
					if (matchedPatients.size() > 0) {
						importLog.addWarningMessage(lineNum, "Check for duplicate patients. Patient firstName:" + importSetup.getDataValues()[importSetup.getIndexPatientFirstName()] + " lastName:" + importSetup.getDataValues()[importSetup.getIndexPatientLastName()] + " birthDate:" + dateOrTimeAsString +
						" created and the following existing patients matched on last name and birthdate but not first name:" + matchedPatients.toString());
					}
	
					
					// firstName and birthDate match (lastName may be incorrect)
					matchedPatients.clear();
					filter.clearDaoParams();
					filter.addDaoParam(filter.daoEqualityParam("firstName", importSetup.getDataValues()[importSetup.getIndexPatientFirstName()]));
					filter.addDaoParam(filter.daoEqualityParam("birthDate", birthDate));
					filter.addDaoParam(filter.daoNot(filter.daoEqualityParam("lastName", importSetup.getDataValues()[importSetup.getIndexPatientLastName()])));
					patientList = Patient.MANAGER.get(Patient.class, filter);
					// iterate thru results and concatenate each Patient matched together for a the log warning message
					for (Patient matchedPatient : patientList) {
						matchedPatients.add(matchedPatient.getFullNameWithId());
					}
					if (matchedPatients.size() > 0) {
						importLog.addWarningMessage(lineNum, "Check for duplicate patients. Patient firstName:" + importSetup.getDataValues()[importSetup.getIndexPatientFirstName()] + " lastName:" + importSetup.getDataValues()[importSetup.getIndexPatientLastName()] + " birthDate:" + dateOrTimeAsString +
						" created and the following existing patients matched on first name and birthdate but not last name:" + matchedPatients.toString());
					}
				
					
					// birthDate matches (firstName and lastName may be incorrect)
					matchedPatients.clear();
					filter.clearDaoParams();
					filter.addDaoParam(filter.daoEqualityParam("birthDate", birthDate));
					filter.addDaoParam(filter.daoAnd(filter.daoNot(filter.daoEqualityParam("lastName", importSetup.getDataValues()[importSetup.getIndexPatientLastName()])), filter.daoNot(filter.daoEqualityParam("firstName", importSetup.getDataValues()[importSetup.getIndexPatientFirstName()]))));
					patientList = Patient.MANAGER.get(Patient.class, filter);
					// iterate thru results and concatenate each Patient matched together for a the log warning message
					for (Patient matchedPatient : patientList) {
						matchedPatients.add(matchedPatient.getFullNameWithId());
					}
					if (matchedPatients.size() > 0) {
						importLog.addWarningMessage(lineNum, "Check for duplicate patients. Patient firstName:" + importSetup.getDataValues()[importSetup.getIndexPatientFirstName()] + " lastName:" + importSetup.getDataValues()[importSetup.getIndexPatientLastName()] + " birthDate:" + dateOrTimeAsString +
						" created and the following existing patients matched on birthdate but not first name and not last name:" + matchedPatients.toString());
					}
				}
				else {
					// lastName matches (firstName may be incorrect)
					matchedPatients.clear();
					filter.clearDaoParams();
					filter.addDaoParam(filter.daoEqualityParam("lastName", importSetup.getDataValues()[importSetup.getIndexPatientLastName()]));
					filter.addDaoParam(filter.daoNot(filter.daoEqualityParam("firstName", importSetup.getDataValues()[importSetup.getIndexPatientLastName()])));
					patientList = Patient.MANAGER.get(Patient.class, filter);
					// iterate thru results and concatenate each Patient matched together for a the log warning message
					for (Patient matchedPatient : patientList) {
						matchedPatients.add(matchedPatient.getFullNameWithId());
					}
					if (matchedPatients.size() > 0) {
						importLog.addWarningMessage(lineNum, "Check for duplicate patients. Patient firstName:" + importSetup.getDataValues()[importSetup.getIndexPatientFirstName()] + " lastName:" + importSetup.getDataValues()[importSetup.getIndexPatientLastName()] + 
						" created and the following existing patients matched on last name but not first name:" + matchedPatients.toString());
					}
				}
			}
		}
			
		if (p == null) {
			if (importDefinition.getPatientExistRule().equals(MUST_EXIST)) {
				if (importSetup.getIndexPatientPIDN() != -1) {
					importLog.addErrorMessage(lineNum, "Patient does not exist violating MUST_EXIST flag. PIDN:" + importSetup.getDataValues()[importSetup.getIndexPatientPIDN()]); 
				}
				else {
					importLog.addErrorMessage(lineNum, "Patient does not exist violating MUST_EXIST flag.Line:" +  
						" First Name:" + importSetup.getDataValues()[importSetup.getIndexPatientFirstName()] + " Last Name:" + importSetup.getDataValues()[importSetup.getIndexPatientLastName()]);
				}
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}else {
				// for either MUST_NOT_EXIST or MAY_OR_MAY_NOT_EXIST instantiate the Patient
				
				// note that de-identified data will map the de-identified id to patient.firstName and a STATIC_INDICATOR value of the Patient.DEIDENTIFIED constant
			    // to patient.lastName
				if (importSetup.getIndexPatientFirstName() == -1 || !StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexPatientFirstName()])) {
					importLog.addErrorMessage(lineNum, "Cannot create Patient. First Name field (patient.firstName) is missing or has no value");
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
				// so for de-identified data there is not a value in the data file for lastName, rather there is a STATIC:DE-IDENTIFIED value in the mapping
				// file. so do not perform this error checking in that case
				if (ArrayUtils.indexOf(importSetup.getMappingCols(), STATIC_INDICATOR + DEIDENTIFIED, 0) == -1) {
					if (importSetup.getIndexPatientLastName() == -1 || !StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexPatientLastName()])) {
						importLog.addErrorMessage(lineNum, "Cannot create Patient. Last Name field (patient.lastName) is missing or has no value");
						return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
					}
				}
				// de-identified patients may not have a real birthDate, in which case the mapping file should provide a fake default birthDate (by adding
				// a column to the data file for birth date but with no data and then mapping it to patient.birthDate with a default in row 4 of the fake
				// date. cannot use STATIC: on birthDate because code expects it to be mapped from the data file (i.e. a static birth date does not make sense
				// in general). and do not want to put the fake value in the data file for every row because it may not pass as a valid birth date if it is
				// out of range of valid birth dates checked above).
				if (importSetup.getIndexPatientBirthDate() == -1 || !StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexPatientBirthDate()])) {
					importLog.addErrorMessage(lineNum, "Cannot create Patient. Date of Birth field (patient.birthDate) is missing or has no value");
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
				// de-identified patients must have a gender in the data file
				if (importSetup.getIndexPatientGender() == -1 || !StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexPatientGender()])) {
					importLog.addErrorMessage(lineNum, "Cannot create Patient. Gender field (patient.gender) is missing or has no value");
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
					
				// create Patient record
				p = createPatient(importDefinition, importSetup);
					
				// the property values will be assigned when iterating dataValues below. could assign
				// the "indexed" Patient properties here since those were found as part of determing whether the
				// Patient exists. but still need to assign other Patient properties (can not index them
				// all because do not even know what all the properties could be) and since will be 
				// iterating thru all data values just assign all properties when iterating dataValues

				// however, do set first name, last name, dob properties as they may be used in error log
				p.setFirstName(importSetup.getDataValues()[importSetup.getIndexPatientFirstName()]);
				int propIndex = -1;
				propIndex = ArrayUtils.indexOf(importSetup.getMappingCols(), STATIC_INDICATOR + DEIDENTIFIED);
				if (propIndex != -1) {
					p.setLastName(DEIDENTIFIED);
					p.setDeidentified(true);
				}
				else {
					p.setLastName(importSetup.getDataValues()[importSetup.getIndexPatientLastName()]);
				}
				p.updateCalculatedFields(); // so can use full name in log messages

				if (birthDate != null) {
					p.setBirthDate(birthDate);
				}

				// at this point have already validated that patient.gender exists in import file and has a value
				p.setGender(importSetup.getDataValues()[importSetup.getIndexPatientGender()].toLowerCase().startsWith("m") || 
						importSetup.getDataValues()[importSetup.getIndexPatientGender()].equals("1") ? (byte)1 : (byte)2);
				p.setCreated(new Date());
				p.setCreatedBy("IMPORT (" + CoreSessionUtils.getCurrentUser(sessionManager, request).getLogin() + ")");
				
				importSetup.setPatientCreated(true);
				importSetup.setPatient(p);
			}
		}
		else { // Patient already exists
			importSetup.setPatientExisted(true);
			importSetup.setPatient(p);
			if (importDefinition.getPatientExistRule().equals(MUST_NOT_EXIST)) {
				// typically with this flag the first time the import is run the Patients will not exist
				// so they will be created above. if there were some import data errors they would be fixed
				// and the script re-imported, at which point there will be these errors for all Patients that 
				// were created on first run, so record will be skipped and Patient will correctly not be
				// created again
					
				// note: this is why it is important that Patient should not be persisted until EnrollmentStatus,
				// Visit and Instrument have all been validated for errors and successfully added. because if 
				// Patient were persisted, then there were errors with Visit, Instrument, etc. when those errors 
				// were fixed and script re-imported the records will be skipped because Patient now exists
				
				// note: this differs from MAY_OR_MAY_NOT_EXIST where import of the record will continue if
				// the Patient exists (as well as if Patient does not exist as it will be created above)
				importLog.addErrorMessage(lineNum, "Patient already exists, violates Import Definition MUST_NOT_EXIST setting. Patient::" + p.getFullNameWithId());
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}
		
		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}


	/**
	 * Set the filter for matching patient names. Subclasses should override if they have custom 
	 * patient name matching.
	 * 
	 * @param filter
	 * @param importSetup
	 */
	protected void setPatientNameMatchFilter(LavaDaoFilter filter, CrmsImportSetup importSetup) {
		//TODO: consider Danny's Levenson algorithm for fuzzy matching Patient Last Name
		filter.addDaoParam(filter.daoEqualityParam("firstName", importSetup.getDataValues()[importSetup.getIndexPatientFirstName()]));
		filter.addDaoParam(filter.daoEqualityParam("lastName", importSetup.getDataValues()[importSetup.getIndexPatientLastName()]));
	}

	
	/**
	 * contactInfoExistsHandling
	 * 
	 * Determine whether ContactInfo should be created, and if so create instance. Subclasses can override to customize
	 * logic to determine whether to create a ContactInfo instance.
	 * 
	 * @param context
	 * @param errors
	 * @param importDefinition
	 * @param importSetup
	 * @param lineNum
	 * @return SUCCESS Event if no import errors with current record; ERROR EVENT if errors
	 */
	protected Event contactInfoExistsHandling(RequestContext context, BindingResult errors, 
			CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, CrmsImportLog importLog,
			int lineNum) {
		
		// the assumption is that ContactInfo is only imported as part of a new Patient import, so if a new Patient was created
		// and any ContactInfo properties are mapped and have data then create a new ContactInfo record.
		// because currently not a use case for importing new ContactInfo for already existing Patients
		// NOTE: if such a use case exists, see SpdcHistoryFormImportHandler override of this method which does search for
		// existing ContactInfo record
		if (importSetup.isPatientCreated()) {
			// check that at least one of what are considered the key ContactInfo properties has data
			if ((importSetup.getIndexContactInfoAddress() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoAddress()])) || 
					(importSetup.getIndexContactInfoCity() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoCity()])) ||  
					(importSetup.getIndexContactInfoState() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoState()])) || 
					(importSetup.getIndexContactInfoZip() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoZip()])) ||
					(importSetup.getIndexContactInfoPhone1() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoPhone1()])) || 
					(importSetup.getIndexContactInfoPhone2() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoPhone2()])) || 
					(importSetup.getIndexContactInfoEmail() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoEmail()]))) {
				ContactInfo contactInfo = createContactInfo(importDefinition, importSetup);
				contactInfo.setPatient(importSetup.getPatient());
				contactInfo.setIsCaregiver(false);
				contactInfo.setActive((short)1);
				if (importSetup.getIndexContactInfoAddress() != -1) {
					contactInfo.setAddress(importSetup.getDataValues()[importSetup.getIndexContactInfoAddress()]);
				}
				if (importSetup.getIndexContactInfoCity() != -1) {
					contactInfo.setCity(importSetup.getDataValues()[importSetup.getIndexContactInfoCity()]);
				}
				if (importSetup.getIndexContactInfoState() != -1) {
					contactInfo.setState(this.convertStateCode(importSetup.getDataValues()[importSetup.getIndexContactInfoState()], importLog, lineNum));
				}
				if (importSetup.getIndexContactInfoZip() != -1) {
					contactInfo.setZip(importSetup.getDataValues()[importSetup.getIndexContactInfoZip()]);
				}
				if (importSetup.getIndexContactInfoPhone1() != -1) {
					contactInfo.setPhone1(importSetup.getDataValues()[importSetup.getIndexContactInfoPhone1()]);
				}
				if (importSetup.getIndexContactInfoPhone2() != -1) {
					contactInfo.setPhone2(importSetup.getDataValues()[importSetup.getIndexContactInfoPhone2()]);
				}
				if (importSetup.getIndexContactInfoEmail() != -1) {
					contactInfo.setEmail(importSetup.getDataValues()[importSetup.getIndexContactInfoEmail()]);
				}
				importSetup.setContactInfoCreated(true);
				importSetup.setContactInfo(contactInfo);
			}
		}
	
		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}

	
	
	/**
	 * caregiverAndContactInfoExistsHandling
	 * 
	 * Determine whether Caregiver and their ContactInfo exists or not and create if them if not. Can assume 
	 * an exists setting of MAY_OR_MAY_NOT_EXIST where nothing is updated if the Caregiver already exists.
	 * 
	 * Caregiver and ContactInfo are handled together in the same method because ContactInfo has an association 
	 * to Caregiver, so have to find exisiting Caregiver or create a new Caregiver in order to set the 
	 * ContactInfo for a Caregiver.
	 * 
	 * A data file containing multiple caregivers ContactInfo could be imported so unlike other Handling
	 * methods, this method takes arguments for the data indices of which properties to use, and it utilizes 
	 * the Event attributes structure to return a new or existing Caregiver ContactInfo object and flags.
	 * 
	 * @param context
	 * @param errors
	 * @param importDefinition
	 * @param importSetup
	 * @param lineNum
	 * @return SUCCESS Event if no import errors with current record; ERROR EVENT if errors
	 */
	protected Event caregiverAndContactInfoExistsHandling(RequestContext context, BindingResult errors, 
			CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, CrmsImportLog importLog,
			int indexFirstName, int indexLastName, int indexRelation, int indexContactInfoAddress, int indexContactInfoCity, 
			int indexContactInfoState, int indexContactInfoZip, int indexContactInfoPhone1, int indexContactInfoPhone2, int indexContactInfoEmail,
			int lineNum) {
		LavaDaoFilter filter = EntityBase.newFilterInstance();
		List<Caregiver> caregiverList = null;
		List<ContactInfo> caregiverContactInfoList = null;

		// search for existing ContactInfo for the Caregiver
		Caregiver caregiver = null;
		Boolean caregiverCreated = null;
		Boolean caregiverExisted = null;
		ContactInfo caregiverContactInfo = null;
		Boolean caregiverContactInfoCreated = null;
		Boolean caregiverContactInfoExisted = null;

		if (indexFirstName == -1 || !StringUtils.hasText(importSetup.getDataValues()[indexFirstName]) 
			|| indexLastName == -1 || !StringUtils.hasText(importSetup.getDataValues()[indexLastName])) {
			// if no caregiver data in the data file cannot check if caregiver exists or create a new
			// caregiver
			caregiverCreated = false;
			caregiverExisted = false;
			caregiverContactInfoCreated = false;
			caregiverContactInfoExisted = false;
		}
		else {
			
			// only search if the Patient already exists because if Patient did not exist then Caregiver does not exist
			if (!importSetup.isPatientCreated()) { 
				filter.clearDaoParams();
				filter.setAlias("patient", "patient");
				filter.addDaoParam(filter.daoEqualityParam("patient.id", importSetup.getPatient().getId()));
				filter.addDaoParam(filter.daoEqualityParam("firstName", importSetup.getDataValues()[indexFirstName]));
				filter.addDaoParam(filter.daoEqualityParam("lastName", importSetup.getDataValues()[indexLastName]));
				
				caregiverList = Caregiver.MANAGER.get(Caregiver.class, filter);

				if (caregiverList.size() == 1) {
					caregiver = (Caregiver) caregiverList.get(0);
				}
				else if (caregiverList.size() > 1) {
					// this should never happen. if re-running import of a data file, should just be one 
					importLog.addErrorMessage(lineNum, "Multiple Caregiver records matched for patient " + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + 
							" and Caregiver firstName:" + importSetup.getDataValues()[indexFirstName] 
							+ " lastName:" + importSetup.getDataValues()[indexLastName]);
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
			}
			
			if (caregiver != null) {
				caregiverExisted = true;
				caregiverCreated = false;
			}
			else {
				// logic to determine whether a Caregiver should be created if there is no match, so cover the conditions where a 
				// Caregiver could be imported:
				// if this is a patient only import then yes
				// if this is an assessment import and it is a caregiver instrument and existRule is MAY_OR_MAY_NOT_EXIST then yes (as 
				//   opposed to MUST_EXIST)
				if (importDefinition.getPatientOnlyImport() || 
					(importDefinition.getInstrCaregiver() && importDefinition.getInstrCaregiverExistRule().equals(MAY_OR_MAY_NOT_EXIST))) { 
					caregiverExisted = false;
					
					// at this point either Patient was just created in which case there cannot be a Caregiver yet, or Patient
					// already existed but Caregiver could not be found
		
					if ((indexFirstName != -1 && StringUtils.hasText(importSetup.getDataValues()[indexFirstName])) && 
							(indexLastName != -1 && StringUtils.hasText(importSetup.getDataValues()[indexLastName]))) {
						caregiver = createCaregiver(importDefinition, importSetup);
						caregiver.setPatient(importSetup.getPatient());
						caregiver.setFirstName(importSetup.getDataValues()[indexFirstName]);
						caregiver.setLastName(importSetup.getDataValues()[indexLastName]);
						if (indexRelation != -1) {
							// note that relation is a range, not suggest, so imported value may not match any items in the list
							caregiver.setRelation(importSetup.getDataValues()[indexRelation]);
						}
						caregiver.setActive((short)1);
						// any other (non required) caregiver fields will be assigned in setProperty as they are encountered 
						// in the import record
						caregiverCreated = true;
					}
					else {
						caregiverCreated = false;
					}
				}
			}
				
			// if there is ContactInfo data in the import data file:
			// for a new Caregiver, create a ContactInfo record since know it could not have existed already
			// for an existing Caregiver, search for the ContactInfo record and if not found, create a ContactInfo record
			// that is associated with the Caregiver
			if ((indexContactInfoAddress != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoAddress])) || 
				(indexContactInfoCity != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoCity])) ||  
				(indexContactInfoState != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoState])) || 
				(indexContactInfoZip != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoZip])) ||
				(indexContactInfoPhone1 != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoPhone1])) || 
				(indexContactInfoPhone2 != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoPhone2])) || 
				(indexContactInfoEmail != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoEmail]))) {
				
				if (caregiverExisted) {
					// search for ContactInfo
					
					filter.clearDaoParams();
					filter.setAlias("patient", "patient");
					filter.addDaoParam(filter.daoEqualityParam("patient.id", importSetup.getPatient().getId()));
					filter.setAlias("caregiver", "caregiver");
					filter.addDaoParam(filter.daoEqualityParam("caregiver.id", caregiver.getId()));
					if (indexContactInfoAddress != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoAddress])) {
						filter.addDaoParam(filter.daoEqualityParam("address", importSetup.getDataValues()[indexContactInfoAddress]));
					}
					if (indexContactInfoCity != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoCity])) {
						filter.addDaoParam(filter.daoEqualityParam("city", importSetup.getDataValues()[indexContactInfoCity]));
					}
					if (indexContactInfoState != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoState])) {
						filter.addDaoParam(filter.daoEqualityParam("state", this.convertStateCode(importSetup.getDataValues()[indexContactInfoState], importLog, lineNum)));
					}
					if (indexContactInfoZip != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoZip])) {
						filter.addDaoParam(filter.daoEqualityParam("zip", importSetup.getDataValues()[indexContactInfoZip]));
					}
					if (indexContactInfoPhone1 != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoPhone1])) {
						filter.addDaoParam(filter.daoEqualityParam("phone1", importSetup.getDataValues()[indexContactInfoPhone1]));
					}
					if (indexContactInfoPhone2 != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoPhone2])) {
						filter.addDaoParam(filter.daoEqualityParam("phone2", importSetup.getDataValues()[indexContactInfoPhone2]));
					}
					if (indexContactInfoEmail != -1 && StringUtils.hasText(importSetup.getDataValues()[indexContactInfoEmail])) {
						filter.addDaoParam(filter.daoEqualityParam("email", importSetup.getDataValues()[indexContactInfoEmail]));
					}
					
					caregiverContactInfoList = ContactInfo.MANAGER.get(ContactInfo.class, filter);
					if (caregiverContactInfoList.size() == 1) {
						caregiverContactInfo = (ContactInfo) caregiverContactInfoList.get(0);
					}
					else if (caregiverContactInfoList.size() > 1) {
						importLog.addErrorMessage(lineNum,  "Multiple Existing ContactInfo records matched for Caregiver:" + caregiver.getFullName() +  
								" and Patient:" + importSetup.getPatient().getFullNameWithId() + 
								(indexContactInfoAddress != -1 ? " and ContactInfo Address:" + importSetup.getDataValues()[indexContactInfoAddress] : "") + 
								(indexContactInfoCity != -1 ? " and City:" + importSetup.getDataValues()[indexContactInfoCity] : "") + 
								(indexContactInfoState != -1 ? " and State:" + importSetup.getDataValues()[indexContactInfoState] : "") + 
								(indexContactInfoZip != -1 ? " and Zip:" + importSetup.getDataValues()[indexContactInfoZip] : "") +
								(indexContactInfoPhone1 != -1 ? " and Phone1:" + importSetup.getDataValues()[indexContactInfoPhone1] : "") +
								(indexContactInfoPhone2 != -1 ? " and Phone2:" + importSetup.getDataValues()[indexContactInfoPhone2] : "") +
								(indexContactInfoEmail != -1 ? " and Email:" + importSetup.getDataValues()[indexContactInfoEmail] : ""));
						return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
					}
				}
				
				if (caregiverContactInfo != null) {
					caregiverContactInfoExisted = true;
					caregiverContactInfoCreated = false;
				}
				else {
					caregiverContactInfoExisted = false;
					caregiverContactInfoCreated = true;
					caregiverContactInfo = createContactInfo(importDefinition, importSetup);
					caregiverContactInfo.setPatient(importSetup.getPatient());
					caregiverContactInfo.setIsCaregiver(true);
					// NOTE: had to refactor lava-crms ContactInfo to map Caregiver as an association rather than mapping caregiverId 
					// property since do not know caregiverId at this point given that new Caregiver has not been persisted. ORM will
					// take care of assigning caregiverId to ContactInfo at persistence
					caregiverContactInfo.setCaregiver(caregiver);
					caregiverContactInfo.setActive((short)1);
					if (indexContactInfoAddress != -1) {
						caregiverContactInfo.setAddress(importSetup.getDataValues()[indexContactInfoAddress]);
					}
					if (indexContactInfoCity != -1) {
						caregiverContactInfo.setCity(importSetup.getDataValues()[indexContactInfoCity]);
					}
					if (indexContactInfoState != -1) {
						caregiverContactInfo.setState(this.convertStateCode(importSetup.getDataValues()[indexContactInfoState], importLog, lineNum));
					}	
					if (indexContactInfoZip != -1) {
						caregiverContactInfo.setZip(importSetup.getDataValues()[indexContactInfoZip]);
					}
					if (indexContactInfoPhone1 != -1) {
						caregiverContactInfo.setPhone1(importSetup.getDataValues()[indexContactInfoPhone1]);
					}
					if (indexContactInfoPhone2 != -1) {
						caregiverContactInfo.setPhone2(importSetup.getDataValues()[indexContactInfoPhone2]);
					}
					if (indexContactInfoEmail != -1) {
						caregiverContactInfo.setEmail(importSetup.getDataValues()[indexContactInfoEmail]);
					}
				}
			}
		}
	
		Map<String,Object> eventAttrMap = new HashMap<String,Object>();
		eventAttrMap.put("caregiver", caregiver);
		eventAttrMap.put("caregiverCreated", caregiverCreated != null ? caregiverCreated : Boolean.FALSE);
		eventAttrMap.put("caregiverExisted", caregiverExisted != null ? caregiverExisted : Boolean.FALSE);
		eventAttrMap.put("caregiverContactInfo", caregiverContactInfo);
		eventAttrMap.put("caregiverContactInfoCreated", caregiverContactInfoCreated != null ? caregiverContactInfoCreated : Boolean.FALSE);
		eventAttrMap.put("caregiverContactInfoExisted", caregiverContactInfoExisted != null ? caregiverContactInfoExisted : Boolean.FALSE);
		AttributeMap attributeMap = new LocalAttributeMap(eventAttrMap);
		return new Event(this, SUCCESS_FLOW_EVENT_ID, attributeMap);
	}


	/**
	 * enrollmentStatusExistsHandling
	 * 
	 * Determine whether enrollmentStatus exists or not and act accordingly based on the importDefinition settings.
	 * 
	 * @param context
	 * @param errors
	 * @param importDefinition
	 * @param importSetup
	 * @param lineNum
	 * @return SUCCESS Event if no import errors with current record; ERROR EVENT if errors
	 */
	protected Event enrollmentStatusExistsHandling(RequestContext context, BindingResult errors, 
			CrmsImportDefinition importDefinition, CrmsImportSetup importSetup,  CrmsImportLog importLog,
			int lineNum) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EntityBase.newFilterInstance();
		List<EnrollmentStatus> enrollmentStatusList = null;
		SimpleDateFormat formatter;
		String dateOrTimeAsString = null;

		// search for existing enrollmentStatus
		EnrollmentStatus es = null;

		// if patient was just created, know that the enrollmentStatus could not exist yet, but if patient
		// was not just created, then check whether enrollmentStatus exists or not
		if (!importSetup.isPatientCreated()) { 
			filter.clearDaoParams();
			filter.setAlias("patient", "patient");
			filter.addDaoParam(filter.daoEqualityParam("patient.id", importSetup.getPatient().getId()));
			filter.addDaoParam(filter.daoEqualityParam("projName", importSetup.getRevisedProjName()));
			// note: could get the list of project enrollment statuses for the given projName and then
			// filter on a certain set of statuses, e.g. exclude 'Withdrew'. But statuses can be custom
			// for each project so that is a lot of logic and probably too much for the import definition,
			// as can generally assume that if there is data for a patient and project that the patient 
			// is currently enrolled
			
			enrollmentStatusList = EnrollmentStatus.MANAGER.get(EnrollmentStatus.class, filter);

			if (enrollmentStatusList.size() == 1) {
				es = (EnrollmentStatus) enrollmentStatusList.get(0);
			}
			else if (enrollmentStatusList.size() > 1) {
				// this should never happen. if re-running import of a data file, should just be one 
				importLog.addErrorMessage(lineNum, "Multiple EnrollmentStatus records matched for patient " + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + 
						" and project " + importSetup.getRevisedProjName());
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}
		
		String importEsStatus = importSetup.getIndexEsStatus() != -1 ? importSetup.getDataValues()[importSetup.getIndexEsStatus()] : importDefinition.getEsStatus();
		
		if (es == null) {
			if (importDefinition.getEsExistRule().equals(MUST_EXIST)) {
				importLog.addErrorMessage(lineNum, "Patient Enrollment does not exist for Project:" + importSetup.getRevisedProjName() + 
						" violating MUST_EXIST flag. Patient:" + importSetup.getPatient().getFullNameRevWithId());
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}else {
				// for either MUST_NOT_EXIST or MAY_OR_MAY_NOT_EXIST instantiate the Enrollment Status

				// enrollmentStatus date will typically not be supplied in the data file, so default to visitDate if not
				// note if 'patientOnlyImport' flag set in importDefinition there will not be a date to use 
				Date esDate = null;
				if (importSetup.getIndexEsStatusDate() != -1) {
					dateOrTimeAsString = importSetup.getDataValues()[importSetup.getIndexEsStatusDate()];
				}
				else if (importSetup.getIndexVisitDate() != -1) {
					// if the enrollment date is not supplied in the data, use the visit date as the enrollment date
					// note that for a patientOnlyImport, if there is not enrollment date in the data, then there will not be a visitDate in the data to
					// fall back on because patientOnlyImport does not import visit or instrument data 
					dateOrTimeAsString = importSetup.getDataValues()[importSetup.getIndexVisitDate()];
				}
				else {
					importLog.addWarningMessage(lineNum, "Enrollment Status Date unknown for:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()));
				}
				

				if (dateOrTimeAsString != null) {
					formatter = new SimpleDateFormat(importDefinition.getDateFormat() != null ? importDefinition.getDateFormat() : DEFAULT_DATE_FORMAT);
					if (importDefinition.getDateFormat().endsWith("/yy")) {
						// if 2 digit year, set start year to 2000 so year will be in the 21st century since all enrollment and visit dates should be
						Calendar tempCal = Calendar.getInstance();
						tempCal.clear();
						tempCal.set(Calendar.YEAR, 2000);
						formatter.set2DigitYearStart(tempCal.getTime());
					}
					formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
					try {
						esDate = formatter.parse(dateOrTimeAsString);
					} catch (ParseException e) {
						// likely will not be called with leniency applied
						importLog.addErrorMessage(lineNum, "Enrollment Status Date or Visit Date is an invalid Date format. Date:" + dateOrTimeAsString);
						return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
					}
					if (importDefinition.getDateFormat().endsWith("/yy")) {
						// when data file has 2 digit years, reassign dataOrTimeAsString (used for log messages) to reflect the full 4 digit year 
						dateOrTimeAsString = new SimpleDateFormat(importDefinition.getDateFormat()).format(esDate);
					}
					
					if (!importDefinition.getAllowExtremeDates()) {
						// if date format is yyyy for year part, the parser will allow any date into the future, even 5 digit dates, so 
						// have to do range checking to catch bad date errors
						java.util.Calendar esDateCalendar = java.util.Calendar.getInstance();
						esDateCalendar.setTime(esDate);
						int esDateYear = esDateCalendar.get(java.util.Calendar.YEAR);
						java.util.Calendar nowCalendar = java.util.Calendar.getInstance();
						int nowYear = nowCalendar.get(java.util.Calendar.YEAR);
						// cannot have an enrollmentStatus year before the MAC was founded (1998?) or in the future beyond the current year, but since
						// enrollmentStatus may use visit year and visit year could be in the future allow 2 years into the future
						if (esDateYear < (nowYear - 30) || esDateYear > (nowYear + 2)) {
							importLog.addErrorMessage(lineNum, "Enrollment Status Date or Visit Date has an invalid Year. Date:" + dateOrTimeAsString);
							return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
						}
					}
				}
					
				if (!StringUtils.hasText(importEsStatus)) {
					if (importSetup.getIndexEsStatus() != -1) {
						importLog.addErrorMessage(lineNum, "Cannot create Enrollment Status. Status field in data file (column:" + importSetup.getDataCols()[importSetup.getIndexEsStatus()] + ") has no value");
					}
					else {
						importLog.addErrorMessage(lineNum, "Cannot create Enrollment Status. No Status field supplied in data file and no value specified in definition");
					}
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
					
				// note that non-required fields will be set in the setPropertyHandling method which iterates thru all
				// property values. this could include custom, instance specfic fields. 
						
				// create Enrollment Status record
				es = createEnrollmentStatus(importDefinition, importSetup);
				es.setPatient(importSetup.getPatient());
				es.setProjName(importSetup.getRevisedProjName());
				es.setStatus(importEsStatus, esDate);
				es.updateLatestStatusValues();
				// note that for a patientOnlyImport, unless there is an enrollment date in the data, there will not be an enrollment
				// date to assign to the new enrollmentStatus (i.e. no visit date to use). updateLatestStatusValues will not set 
				// enrollmentStatus latestDesc / latestDate if the date is null, so set it explicitly here. what will happen is that
				// on save EnrollmentStatus updateCalculatedFields will call updateLatestStatusValues which will set the latest status
				// back to null. but, if overriding the EnrollmentStatus the subclass can override updateLatestStatusValues and save
				// the status values in a local var, call the superclass updateLatestStatusValues, and if statuses are still null,
				// restore them to the saved values
				if (es.getLatestDesc() == null) {
					es.setLatestDesc(importEsStatus);
				}

				importSetup.setEnrollmentStatusCreated(true);
				importSetup.setEnrollmentStatus(es);
			}
		}
		else { // EnrollmentStatus already exists
			// warning if current status is not the same as the status defined in the import definition. 
			// e.g. if an existing EnrollmentStatus is matched with latestDesc="EXCLUDED" then user should definitely
			// be warned that data is being imported for a project from which the patient is excluded. could make
			// this an error, but there are many more common situations which do not warrant an error, e.g. lastestDesc
			// is ELIGIBLE instead of ENROLLED because perhaps a coordinator forgot to update the status
			if (es.getLatestDesc() == null) {
				importLog.addWarningMessage(lineNum, "Patient:" + importSetup.getPatient().getFullName() + " does not have a current Enrollment Status and Date for Project:" + importSetup.getRevisedProjName() + ", possibly because there is no Enrollment Status Date to import");
			}
			else if (!es.getLatestDesc().equals(importEsStatus)) {
				importLog.addWarningMessage(lineNum, "Patient:" + importSetup.getPatient().getFullName() + " has an Enrollment Status of: " + es.getLatestDesc() + " for Project:" + importSetup.getRevisedProjName() + ", not:" + importEsStatus);
			}
			
			importSetup.setEnrollmentStatusExisted(true);
			importSetup.setEnrollmentStatus(es);
			if (importDefinition.getEsExistRule().equals(MUST_NOT_EXIST)) {
				// typically with this flag the first time the import is run the Enrollment Status will not 
				// exist so it will be created above. if there were some import data errors they would be fixed
				// and the script re-imported, at which point there will be these errors for all Enrollment 
				// Statuses that were created on first run, so record will be skipped and Enrollment Status
				// will correctly not be created again
								
				// note: this differs from MAY_OR_MAY_NOT_EXIST where import of the record will continue if
				// the Enrollment Status exists (as well as if Enrollment Status does not exist as it will be 
				// created above)
				importLog.addErrorMessage(lineNum, "Enrollment Status already exists, violates Import Definition MUST_NOT_EXIST setting. Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()));
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}

		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}


	
	/**
	 * visitExistHandling
	 * 
	 * Determine whether visit exists or not and act accordingly based on the importDefinition settings.
	 *  
	 * @param context
	 * @param errors
	 * @param importDefinition
	 * @param importSetup
	 * @param lineNum
	 * @return SUCCESS Event if no import errors with current record; ERROR EVENT if errors
	 */
	protected Event visitExistsHandling(RequestContext context, BindingResult errors, 
			CrmsImportDefinition importDefinition, CrmsImportSetup importSetup,  CrmsImportLog importLog,
			int lineNum) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EntityBase.newFilterInstance();
		SimpleDateFormat formatter;
		String dateOrTimeAsString;

		// search for existing Visit
		List<Visit> visitList = null;
		Visit v = null;
		Date visitDate = null;
		Time visitTime = null;
		String visitType = null;

		// visitDate is required for both matching Visit and as a required field when creating new Visit
		dateOrTimeAsString = importSetup.getDataValues()[importSetup.getIndexVisitDate()];
		formatter = new SimpleDateFormat(importDefinition.getDateFormat() != null ? importDefinition.getDateFormat() : DEFAULT_DATE_FORMAT);
		if (importDefinition.getDateFormat().endsWith("/yy")) {
			// if 2 digit year, set start year to 2000 so year will be in the 21st century since all enrollment and visit dates should be
			Calendar tempCal = Calendar.getInstance();
			tempCal.clear();
			tempCal.set(Calendar.YEAR, 2000);
			formatter.set2DigitYearStart(tempCal.getTime());
		}
		formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
		try {
			visitDate = formatter.parse(dateOrTimeAsString);
		} catch (ParseException e) {
			// likely will not occur with leniency applied
			importLog.addErrorMessage(lineNum, "Visit.visitDate is an invalid Date format. Date:" + dateOrTimeAsString);
			return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
		}
		if (importDefinition.getDateFormat().endsWith("/yy")) {
			// when data file has 2 digit years, reassign dataOrTimeAsString (used for log messages) to reflect the full 4 digit year 
			dateOrTimeAsString = new SimpleDateFormat(importDefinition.getDateFormat()).format(visitDate);
		}

		if (!importDefinition.getAllowExtremeDates()) {
			// if date format is yyyy for year part, the parser will allow any date into the future, even 5 digit dates, so 
			// have to do range checking to catch bad date errors
			java.util.Calendar visitDateCalendar = java.util.Calendar.getInstance();
			visitDateCalendar.setTime(visitDate);
			int visitDateYear = visitDateCalendar.get(java.util.Calendar.YEAR);
			java.util.Calendar nowCalendar = java.util.Calendar.getInstance();
			int nowYear = nowCalendar.get(java.util.Calendar.YEAR);
			// allow for visit dates a number of years before the MAC started (in 1998?) and 2 years into the future from the current year
			if (visitDateYear < (nowYear - 30) || visitDateYear > (nowYear + 2)) {
				importLog.addErrorMessage(lineNum, "Visit.visitDate has an invalid Year. Date:" + dateOrTimeAsString);
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}

		if (importSetup.getIndexVisitTime() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexVisitTime()])) {
			Date visitTimeAsDate = null;
			dateOrTimeAsString = importSetup.getDataValues()[importSetup.getIndexVisitTime()];
			formatter = new SimpleDateFormat(importDefinition.getTimeFormat() != null ? importDefinition.getTimeFormat() : DEFAULT_TIME_FORMAT);
			try{
				visitTimeAsDate = formatter.parse(dateOrTimeAsString);
				visitTime = LavaDateUtils.getTimePart(visitTimeAsDate);
			}catch (ParseException e){
				importLog.addErrorMessage(lineNum, "Visit.visitTime is an invalid Time format. Time:" + dateOrTimeAsString);
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}
		
		// match on the visitType value from the data file or specified in the import definition
		// note that this value is also used to populate any visits that are created by the import
		// if definition is such that not matching on visitType and visits will not be created (i.e. visit MUST_EXIST),
		// then visitType would not need to be required. the latter is not considered here, but is considered below
		// if a new visit is to be created
		//NOTE: setting matchVisitTypeFlag is currently restricted to SYSTEM_ADMIN because still debating about 
		//whether this is useful/good idea for users to be able to turn this off. Downsides exist, e.g. may match incorrect visit and/or 
		//multiple visits and incorrectly associate assessment data. for users match defaults to TRUE so must match on visitType.
		visitType = importSetup.getIndexVisitType() != -1 ? importSetup.getDataValues()[importSetup.getIndexVisitType()] : importDefinition.getVisitType();
		if (importDefinition.getMatchVisitTypeFlag() != null && importDefinition.getMatchVisitTypeFlag() && visitType == null) {
			if (importSetup.getIndexVisitType() != -1) {
				importLog.addErrorMessage(lineNum, "Cannot match visit. Visit Type field in data file (column:" + importSetup.getDataCols()[importSetup.getIndexVisitType()] + ") has no value");
			}
			else {
				importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit Type field not supplied in data file and no value specified in definition");									
			}
			return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
		}
		
		// regardless of whether enrollmentStatus was created or not, search for a matching visit, i.e. even if the
		// enrollmentStatus was just created, there could be an existing visit for the patient that matches on date
		// that could be for a different project than what is specified in the import definition or the data file. this
		// reflects the fact that patients are often co-enrolled in multiple projects and if assessment data is to be
		// imported under some Project X for a Visit Date, need to check that the same assessment data does not exist 
		// for the Visit Date under any Project, not just Project X
		// so the DaoParam on projName is commented out below, to illustrate this point
		
		// this handles this scenario. Patient AB comes in and has a Sensory Profile - Child assessment done under
		// Project Pedi Evo Training in 2009. In 2010 the same patient has a Sensory Profile - Child which is done
		// for Project SPD WES. The export data file for Sensory Profile - Child is cumulative over time such that
		// it now contains two Sensory Profile - Child records for Patient AB: in 2009 and 2010. When importing the
		// data file in 2010 under Project SPD WES, if the Visit match included a check for Project, it would not 
		// find the Visit for SPD WES and properly create the Visit and then the Sensory Profile - Child in the Visit.
		// However, it would not find an SPD WES Visit in 2009 since that was a Pedi Evo Training Visit and it will
		// therefore erroneously create an SPD WES Visit in 2009 and import the 2009 Sensory Profile - Child data
		// which already is imported and exists under a Pedi Evo Training Visit in 2009 (on the exact same date as
		// the SPD WES Visit). By removing the Visit match on Project, this will not happen. Ignoring Project, the 
		// system will match a Visit for patient AB in 2009 that has a Sensory Profile - Child and therefore will
		// not import data that has already been imported.
		filter.clearDaoParams();
		filter.setAlias("patient", "patient");
		filter.addDaoParam(filter.daoEqualityParam("patient.id", importSetup.getPatient().getId()));
		// filter.addDaoParam(filter.daoEqualityParam("projName", importSetup.getRevisedProjName()));
		if (StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexVisitDate()])) {

			// do not have a flag for whether both date and time must match. just assume that whichever is provided should
			// match. if only date can just do a full date comparison, i.e. should not need datepart because the date in
			// the Visit is just the datepart and the date in the data file will just be a date

			// currently do not handle existing columns that have date and time in same column. not sure if this will
			// be encountered

			if (importDefinition.getVisitWindow() == null || importDefinition.getVisitWindow().equals((short)0)) {
				// if visit window is 0 or not specified, then search for a LAVA visit with an exact match on the visit date (and time if supplied)
				// to the record in the data file
				
				// note: could also use daoDateAndTimeEqualityParam
				filter.addDaoParam(filter.daoEqualityParam("visitDate", visitDate)); // visitDate validated and instantiated earlier in this method
				if (importSetup.getIndexVisitTime() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexVisitTime()])) {
					filter.addDaoParam(filter.daoEqualityParam("visitTime", visitTime)); // visitTime validated and instantiated earlier in this method
				}
			}
			else{
				// if visit window specified, search for an existing Visit where the data file visit date is within the visit window (+/- in days)
				// of a LAVA Visit
				
				// note that if multiple visits are matched within the visit window, an error is given for the current import record. could instead
				// do a proximity search to find the closest visit if multiple are found, but decided not to do that because there is no guarantee
				// that the Visit is the correct Visit to import the data into just because it is closer to the data file visit date than another 
				// Visit that is within the visit window
				
				// based on importDefinition visitWindow, determine the startDate and endDate to use for the query
				// for the purpose of visit windows, ignore the time part of the LAVA visit date and any time supplied in the data file, unless
				// this becomes important, which seems doubtful
				Calendar startDateCal = Calendar.getInstance();
				startDateCal.setTime(visitDate);
				startDateCal.add(Calendar.DATE, -(importDefinition.getVisitWindow()));
				Date startDate = startDateCal.getTime();
				
				Calendar endDateCal = Calendar.getInstance();
				endDateCal.setTime(visitDate);
				endDateCal.add(Calendar.DATE, importDefinition.getVisitWindow());
				Date endDate = endDateCal.getTime();

				// note: could use daoDateAndTImeBetweenParam
				filter.addDaoParam(filter.daoBetweenParam("visitDate", startDate, endDate));
			}
			
			//NOTE: setting matchVisitTypeFlag is currently restricted to SYSTEM_ADMIN because still debating about 
			//whether this is useful/good idea for users to be able to turn this off. Downsides exist, e.g. may match incorrect visit and/or 
			//multiple visits and incorrectly associate assessment data. for users match defaults to TRUE so must match on visitType.
			if (importDefinition.getMatchVisitTypeFlag() != null && importDefinition.getMatchVisitTypeFlag() && visitType != null) {
				if (StringUtils.hasText(visitType)) {
					filter.addDaoParam(filter.daoEqualityParam("visitType", visitType));
				}
			}
			
			filter.addDaoParam(filter.daoNot(filter.daoEqualityParam("visitStatus", "Cancelled")));

			// note: since just want a single Visit record, originally used the get method and caught the IncorrectResultSizeDataAccessException
			// if there were more than one match, but that database exception marks the transaction for rollback so when attempting to commit
			// the records added to the importLog this generates the UnexpectedRollbackException with an HTTP 500 page
			visitList = Visit.MANAGER.get(Visit.class, filter);

			// if visit window is 0 (or not set) then looking for an exact date match. should never get more than one visit then (assuming match
			// on visitType included). if re-running import of a data file, should just be one instance of a given visitType on a given date
			// however, if no visitType supplied in import definition, could match multiple visits on same date, in which case
			// would not know which one to use. this is why the matchVisitTypeFlag flag defaults to TRUE and is restricted to
			// SYSTEM_ADMIN until further notice

			if (visitList.size() == 1) {
				v = (Visit) visitList.get(0);
			}
			else if (visitList.size() > 1) {
				if (visitType != null) {
					importLog.addErrorMessage(lineNum, "Multiple Visit records matched for Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + 
							" and Visit Date:" + dateOrTimeAsString + " and Visit Type:" + visitType + " and Visit Window:+/-" + importDefinition.getVisitWindow() + " Days");
				}
				else {
					importLog.addErrorMessage(lineNum, "Multiple Visit records matched for Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + 
							" and Visit Date:" + dateOrTimeAsString + " and Visit Window:+/-" + importDefinition.getVisitWindow() + " Days. Specify Visit Type in Import Definition to match on single Visit");
				}
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}
		else {
			// this is not the same as Visit does not exist because do not have fields to check that the
			// Visit does or does not exist
			importLog.addErrorMessage(lineNum, "Cannot determine if Visit exists or not. Column:" + importSetup.getDataCols()[importSetup.getIndexVisitDate()] + " has no data");
			return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
		}
		
		if (v == null) {
			if (importDefinition.getVisitExistRule().equals(MUST_EXIST)) {
				importLog.addErrorMessage(lineNum, "Visit does not exist for Patient:" + importSetup.getPatient().getFullNameRev() + " Project:" + importSetup.getRevisedProjName() + " violating MUST_EXIST flag");
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}else {
				// for either MUST_NOT_EXIST or MAY_OR_MAY_NOT_EXIST instantiate the Visit

				// get fields that were not already obtained for querying for existing Visit. note that these fields are
				// required in LAVA, but they are not required in the import definition, because it may be that a single
				// visitType / visitLocation / visitWith / visitStatus does not apply to all visits that are created
				// within a single import file
				
				// since the MUST_NOT_EXIST and MAY_OR_MAY_NOT_EXIST flags imply that a visit could be created, visitType must
				// be required, so must present in the data file or specified in the import definition
				if (!StringUtils.hasText(visitType)) {
					if (importSetup.getIndexVisitType() != -1) {
						importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit Type field in data file (column:" + importSetup.getDataCols()[importSetup.getIndexVisitType()] + ") has no value");
					}
					else {
						importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit Type field not supplied in data file and no value specified in definition");									
					}
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}

				
				String visitLoc = importSetup.getIndexVisitLoc() != -1 ? importSetup.getDataValues()[importSetup.getIndexVisitLoc()] : importDefinition.getVisitLoc();
				// note there is a catch-all "Home" entry in the Visit Location list that can be used generically. could add more as needed
				if (!StringUtils.hasText(visitLoc)) {
					if (importSetup.getIndexVisitLoc() != -1) {
						importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit Location field in data file (column:" + importSetup.getDataCols()[importSetup.getIndexVisitLoc()] + ") has no value");
					}
					else {
						importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit Location field not supplied in data file and no value specified in definition");									
					}
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
				
				String visitWith = importSetup.getIndexVisitWith() != -1 ? importSetup.getDataValues()[importSetup.getIndexVisitWith()] : importDefinition.getVisitWith();
				// if the user name comes from the data file, check to see whether it matches a LAVA user in the staffList, i.e. matches shortUserNameRev
				if (importSetup.getIndexVisitWith() != -1) {
					// in case the data file just contains user last name use a like query to match shortUserNameRev
					List<AuthUser> userList = null;
					filter.clearDaoParams();
					filter.clearAliases();
					filter.addDaoParam(filter.daoLikeParam("shortUserNameRev", visitWith));
					userList = AuthUser.MANAGER.get(filter);
					// if don't match anything or match more than one, then just leave visitWith as is
					if (userList.size() == 1) {
						visitWith = ((AuthUser)userList.get(0)).getShortUserNameRev();
					}
				}				
				
				/** in case decide to require visitWith
				if (!StringUtils.hasText(visitWith)) {
					if (importSetup.getIndexVisitWith() != -1) {
						importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit With field in data file (column:" + importSetup.getDataCols()[importSetup.getIndexVisitWith()] + ") has no value");
					}
					else {
						importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit With field not supplied in data file and no value specified in definition");									
					}
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
				**/

				String visitStatus = importSetup.getIndexVisitStatus() != -1 ? importSetup.getDataValues()[importSetup.getIndexVisitStatus()] : importDefinition.getVisitStatus();
				if (!StringUtils.hasText(visitStatus)) {
					if (importSetup.getIndexVisitStatus() != -1) {
						importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit Status field in data file (column:" + importSetup.getDataCols()[importSetup.getIndexVisitStatus()] + ") has no value");
					}
					else {
						importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit Status field not supplied in data file and no value specified in definition");									
					}
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}

				// create Visit record
				v = createVisit(importDefinition, importSetup);
				v.setPatient(importSetup.getPatient());
				v.setProjName(importSetup.getRevisedProjName());
				v.setVisitType(visitType);
				// visitDate and visitTime have already been converted above in search for Visit
				v.setVisitDate(visitDate);
				v.setVisitTime(visitTime);
				v.setVisitLocation(visitLoc);
				v.setVisitWith(visitWith);
				v.setVisitStatus(visitStatus);

				// note that non-required fields will be set in the setPropertyHandling method which iterates thru all
				// property values. this could include custom, instance specfic fields. 
						
				importSetup.setVisitCreated(true);
				importSetup.setVisit(v);
			}
		}
		else { // Visit already exists
			importSetup.setVisitExisted(true);
			importSetup.setVisit(v);
			if (importDefinition.getVisitExistRule().equals(MUST_NOT_EXIST)) {
				// typically with this flag the first time the import is run the Enrollment Status will not 
				// exist so it will be created above. if there were some import data errors they would be fixed
				// and the script re-imported, at which point there will be these errors for all Enrollment 
				// Statuses that were created on first run, so record will be skipped and Enrollment Status
				// will correctly not be created again
					
				// note: this differs from MAY_OR_MAY_NOT_EXIST where import of the record will continue if
				// the Enrollment Status exists (as well as if Enrollment Status does not exist as it will be 
				// created above)
				importLog.addErrorMessage(lineNum, "Visit already exists, violates Import Definition MUST_NOT_EXIST setting. Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()));
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}

		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}
		

	
	/*
	 * processInstrumentHandling
	 * 
	 * This is called for each instrument that is specified in the import definition. This code attempts to match
	 * an existing instrument or create a new instrument. If it finds an existing instrument it determines whether
	 * that instrument has already been data entered or not. If the instrument already has data, then it may or may
	 * not update that data, based on the import definition. 
	 * 
	 * If any instrument cannot be processed an error Event is returned and processing of the current data record
	 * is aborted.
	 */
	protected Event processInstrumentExistsHandling(RequestContext context, BindingResult errors, 
			CrmsImportDefinition importDefinition, CrmsImportSetup importSetup,  CrmsImportLog importLog,
			String instrType, String instrVer, String instrMappingAlias, int indexInstrDcDate, int indexInstrDcStatus, 
			String instrumentPropName, String instrCreatedPropName, String instrExistedPropName, String instrExistedWithDataPropName,
			String instrExistedWithDataNoUpdatePropName, int lineNum) throws Exception {
		Event instrHandlingEvent = null;
		
		// find matching instrument. possibly create new instrument. type of instrument specified in the 
		// importDefinition
		if ((instrHandlingEvent = instrumentExistsHandling(context, errors, importDefinition, importSetup, importLog, 
				instrType, instrVer, instrMappingAlias, indexInstrDcDate, indexInstrDcStatus, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
			// error will be returned to caller, which will then skip the rest of the current record and continue to the next record
		}
		else { // assume instrHandlingEvent.getId() == success
			if (instrHandlingEvent.getAttributes() != null && instrHandlingEvent.getAttributes().get("instrument") != null) {
				BeanUtils.setProperty(importSetup, instrumentPropName, (Instrument)instrHandlingEvent.getAttributes().get("instrument")); 
			}
			
			// set flags that will be used to increment instrument counts for the successful import records
			
			if (instrHandlingEvent.getAttributes() != null && instrHandlingEvent.getAttributes().get("instrCreated") != null) {
				BeanUtils.setProperty(importSetup, instrCreatedPropName, Boolean.TRUE); 
			}
			if (instrHandlingEvent.getAttributes() != null && instrHandlingEvent.getAttributes().get("instrExisted") != null) {
				BeanUtils.setProperty(importSetup, instrExistedPropName, Boolean.TRUE); 
			}
			// "instrExistedWithData" will only be reported if importing in "update" mode (which may not be available to users yet)
			if (instrHandlingEvent.getAttributes() != null && instrHandlingEvent.getAttributes().get("instrExistedWithData") != null) {
				BeanUtils.setProperty(importSetup, instrExistedWithDataPropName, Boolean.TRUE); 
			}
			// "instrExistedWithDataNoUpdate" will be considered an error if all instruments in the import return it, but if at least
			// on instrument returns the above flags ("instrCreated", "instrExisted" or "instrExistedWithData") then it will not be considered
			// an error, since at least one instrument was successfully imported
			if (instrHandlingEvent.getAttributes() != null && instrHandlingEvent.getAttributes().get("instrExistedWithDataNoUpdate") != null) {
				BeanUtils.setProperty(importSetup, instrExistedWithDataNoUpdatePropName, Boolean.TRUE); 
			}
		}
		return instrHandlingEvent;
	}
	
	
	/**
	 * instrumentExistsHandling
	 * 
	 * Determine whether instrument exists or not and act accordingly based on the importDefinition
	 * settings. 
	 * 
	 * @param context
	 * @param errors
	 * @param importDefinition
	 * @param importSetup
	 * @param lineNum
	 * @param instrType
	 * @param instrVer
	 * @param indexInstrDcDate
	 * @param indexInstrDcStatus
	 * @return SUCCESS Event if no import errors with current record; ERROR EVENT if errors
	 */
	protected Event instrumentExistsHandling(RequestContext context, BindingResult errors, 
			CrmsImportDefinition importDefinition, CrmsImportSetup importSetup,  CrmsImportLog importLog,
			String instrType, String instrVer, String instrMappingAlias, int indexInstrDcDate, int indexInstrDcStatus, 
			int lineNum) {
		
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EntityBase.newFilterInstance();
		SimpleDateFormat formatter, msgDateFormatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

		String dateOrTimeAsString;
		Map<String,Object> eventAttrMap = new HashMap<String,Object>();
		AttributeMap attributeMap = new LocalAttributeMap(eventAttrMap);
		
		// search for existing instrument
		List<Instrument> instrList = null;
		Instrument instr = null;

		Class instrClazz =instrumentManager.getInstrumentClass(Instrument.getInstrTypeEncoded(instrType, instrVer));

		// determine dcDate for search
		// convert DCDate
		Date dcDate = null;
		// if not supplied in data file then it defaults to visit date when adding new instrument
		if (indexInstrDcDate != -1) {
			dateOrTimeAsString = importSetup.getDataValues()[indexInstrDcDate];
			formatter = new SimpleDateFormat(importDefinition.getDateFormat() != null ? importDefinition.getDateFormat() : DEFAULT_DATE_FORMAT);
			if (importDefinition.getDateFormat().endsWith("/yy")) {
				// if 2 digit year, set start year to 2000 so year will be in the 21st century since all enrollment and visit dates should be
				Calendar tempCal = Calendar.getInstance();
				tempCal.clear();
				tempCal.set(Calendar.YEAR, 2000);
				formatter.set2DigitYearStart(tempCal.getTime());
			}
			formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
			try {
				dcDate = formatter.parse(dateOrTimeAsString);
			} catch (ParseException e) {
				// likely will not occur with leniency applied
				importLog.addErrorMessage(lineNum, "Instrument.dcDate is an invalid Date format. Date:" + dateOrTimeAsString);
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
			if (importDefinition.getDateFormat().endsWith("/yy")) {
				// when data file has 2 digit years, reassign dataOrTimeAsString (used for log messages) to reflect the full 4 digit year 
				dateOrTimeAsString = new SimpleDateFormat(importDefinition.getDateFormat()).format(dcDate);
			}
			
			if (!importDefinition.getAllowExtremeDates()) {
				// if date format is yyyy for year part, the parser will allow any date into the future, even 5 digit dates, so 
				// have to do range checking to catch bad date errors
				java.util.Calendar dcDateCalendar = java.util.Calendar.getInstance();
				dcDateCalendar.setTime(dcDate);
				int dcDateYear = dcDateCalendar.get(java.util.Calendar.YEAR);
				java.util.Calendar nowCalendar = java.util.Calendar.getInstance();
				int nowYear = nowCalendar.get(java.util.Calendar.YEAR);
				// allow for data collection dates a number of years before the MAC started (in 1998?) and 2 years into the future from the current year
				if (dcDateYear < (nowYear - 30) || dcDateYear > (nowYear + 2)) {
					importLog.addErrorMessage(lineNum, "Instrument.dcDate has an invalid Year. Date:" + dateOrTimeAsString);
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
			}
		}
		else {
			dcDate = importSetup.getVisit().getVisitDate();
		}

		// if Visit just created, know instrument could not exist. otherwise, check if instrument exists or not
		if (!importSetup.isVisitCreated()) {
			filter.clearDaoParams();
			filter.setAlias("patient", "patient");
			filter.addDaoParam(filter.daoEqualityParam("patient.id", importSetup.getPatient().getId()));
			filter.addDaoParam(filter.daoEqualityParam("projName", importSetup.getRevisedProjName()));
			filter.setAlias("visit", "visit");
			filter.addDaoParam(filter.daoEqualityParam("visit.id", importSetup.getVisit().getId()));
			filter.addDaoParam(filter.daoEqualityParam("instrType", importDefinition.getInstrType()));
			filter.addDaoParam(filter.daoEqualityParam("dcDate", dcDate));
			filter.addDaoParam(filter.daoEqualityParam("instrType", instrType));

			// subclasses can override and set additional filter matching to find existing instrument. note that the impetus for this is when a data
			// file has multiple instances of the same instrument and the custom matching method can add search filter param specific to the instances
			// that is currently being processed
			setCustomInstrumentMatchFilter(filter, importDefinition, importSetup, instrType, instrVer, instrMappingAlias);

			// note: since just want a single Instrument record, originally used the get method and caught the IncorrectResultSizeDataAccessException
			// if there were more than one match, but that database exception marks the transaction for rollback so when attempting to commit
			// the records added to the importLog this generates the UnexpectedRollbackException with an HTTP 500 page
			instrList = Instrument.MANAGER.get(instrClazz, filter);

			if (instrList.size() == 1) {
				instr = (Instrument) instrList.get(0);
			}
			else if (instrList.size() > 1) {
					importLog.addErrorMessage(lineNum, "Instrument " + instrType + " returns multiple matches for Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + 
							this.getVisitInfo(importDefinition, importSetup));
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}
		
		if (instr == null) {
			if (importDefinition.getInstrExistRule().equals(MUST_EXIST)) {
				importLog.addErrorMessage(lineNum, "Instrument " + instrType + "  does not exist violating MUST_EXIST flag. Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) 
						+ this.getVisitInfo(importDefinition, importSetup));
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}else {
				// instrument does not exist so instantiate
				
				String dcStatus = indexInstrDcStatus != -1 ? importSetup.getDataValues()[indexInstrDcStatus] : importDefinition.getInstrDcStatus();
				if (!StringUtils.hasText(dcStatus)) {
					if (indexInstrDcStatus != -1) {
						importLog.addErrorMessage(lineNum, "Cannot create Instrument " + instrType + ". Instrument DC Status field in data file (column:" + importSetup.getDataCols()[indexInstrDcStatus] + ") has no value");
					}
					else {
						importLog.addErrorMessage(lineNum, "Cannot create Instrument " + instrType + ". Instrument DC Status field not supplied in data file and no value specified in definition");									
					}
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
				
				// if existing visit was matched with a visit window, the data collection date in the data file, which is mapped to visit.visitWith, might
				// be different than the matched Visit visitDate, but the value in the data file should represent the data collection date for the instrument
				dateOrTimeAsString = importSetup.getDataValues()[importSetup.getIndexVisitDate()];
				formatter = new SimpleDateFormat(importDefinition.getDateFormat() != null ? importDefinition.getDateFormat() : DEFAULT_DATE_FORMAT);
				formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
				try {
					dcDate = formatter.parse(dateOrTimeAsString);
				} catch (ParseException e) {
					// likely will not occur with leniency applied
					importLog.addErrorMessage(lineNum, "Visit.visitDate is an invalid Date format (when using for new instrument dcDate). Date:" + dateOrTimeAsString);
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}

				try {
					instr = createInstrument(context, importDefinition, importSetup, instrClazz, instrType, instrVer, dcDate, dcStatus);
					// note: dc and de properties set in saveImportRecord
			
				}
				catch (Exception ex) {
					importLog.addErrorMessage(lineNum, "Error instantiating instrument " + instrType + ". Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) 
							+ this.getVisitInfo(importDefinition, importSetup));
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
			}
			eventAttrMap.put("instrCreated", Boolean.TRUE);
			eventAttrMap.put("instrument", instr);
		}
		else { // instrument already exists
			eventAttrMap.put("instrument", instr);
			if (importDefinition.getInstrExistRule().equals(MUST_NOT_EXIST)) {
				importLog.addErrorMessage(lineNum, "Instrument " + instrType + "  already exists violating Import Definition MUST_NOT_EXIST setting. Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName())
						+ this.getVisitInfo(importDefinition, importSetup));
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
			else {
				// MAY_OR_MAY_NOT_EXIST or MUST_EXIST
				
				// this is where exist flag handling differs for instruments than for Patient, EnrollmentStatus, Visit.
				// need a further check in the case of instruments to make sure it does not have date entered, and if it
				// does then only proceed with import if the allowInstrUpdate flag is set in the import definition.
				// if used allow..Update flags for Patient, EnrollmentStatus and Visit, then just their mere existence
				// would be enough to consider the flag, i.e. not whether they have been data entered or not, because
				// if they exist they must have been data entered because of required fields validation. instruments
				// can exist without being data entered.

				// note that this is also different than flags for updating Patient,EnrollmentStatus and Visit because 
				// those would not affect whether the instrument data is imported or not. so while the instrument 
				// flag can be handled at the level of the import record in terms of skipping the whole import 
				// record or not, the Patient,EnrollmentStatus and,Visit update flags would not dictate this, and so 
				// their allow..Update flags would be enforced at the individual property setting level
				
				// using deDate to determine if instrument has been data entered. not looking for a specific deStatus
				// such as 'Complete' since data entry could have any number of deStatus values
				if (instr.getDeDate() == null) {
					// in this case since the instrument has not had any data entered it is ok to use the existing instrument
					// for the import
					eventAttrMap.put("instrExisted", Boolean.TRUE);
					// NOTE: do not dirty the instrument here by updating the status properties because if the instrument exists and
					// there are multiple instruments, retrieving the next instrument will result in a flush which will update the 
					// auditEffDate of the existing instrument and result in StaleObjectStateException on commit. instead, set these 
					// fields when saving instruments, after all instruments have been created or retrieved 
					// note: would be ok to set visitStatus because the visit does not have to be flushed when retrieving an instrument
					//       but that is done with the rest of the status updates in save
				}
				else {
					// CURRENTLY ONLY ENABLED IN THE UI FOR IMPORT_ADMIN, i.e. user cannot set this flag in the import definition yet. need
					// to implement a confirmation flow state showing user existing and new values so they can confirm overwrite
					if (importDefinition.getAllowInstrUpdate()) {
						eventAttrMap.put("instrExistedWithData", Boolean.TRUE);
						// set an attribute on the return event, "update", used to increment the global count for import records as
						// a whole (if multiple instruments in single import record and no errors, if one is "update" they should all
						// be in sync and return "update", so the "update" returned from the last instrument is the one used to increment
						// the global count
						eventAttrMap.put("update", Boolean.TRUE);
						if (instr.getDeNotes() == null) {
							instr.setDeNotes("");
						}
						instr.setDeNotes(instr.getDeNotes() + "  Data Updated via Import on " + new Date() + " by:" + CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request).getShortUserNameRev());				
					}
					else {
						// this is not an error in the sense that the there was a problem, but after all instruments have been processed and they
						// all matched instruments that had already been data entered, then it is considered an error so that the record will be aborted,
						// i.e. it is likely that a data file with this record was already imported (or somebody data entered all the instrument data).
						// but give a warning for each individual instrument
						importLog.addWarningMessage(lineNum, "Instrument " + instrType + "  exists and has already been data entered. Cannot overwrite per Import Definition. Patient:" + 
							(importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + this.getVisitInfo(importDefinition, importSetup));

						// set an attribute on the return event, "instrExistedWithDataNoUpdate", used to determine whether the import record
						// should be aborted
						eventAttrMap.put("instrExistedWithDataNoUpdate", Boolean.TRUE);
					}
				}
			}
		}
		
		return new Event(this, SUCCESS_FLOW_EVENT_ID, attributeMap);
	}



	/**
	 * Set the filter for matching patient names. Subclasses should override if they have custom 
	 * patient name matching.
	 * 
	 * @param filter
	 * @param importSetup
	 */
	protected void setCustomInstrumentMatchFilter(LavaDaoFilter filter, CrmsImportDefinition importDefinition, CrmsImportSetup importSetup,
			String instrType, String instrVer, String instrMappingAlias) {
		
		// base class implementation do nothing
		
	}

	
	
	/**
	 * otherExistsHandling
	 * 
	 * Subclasses should override to handle additional entities beyond Patient/EnrollmentStatus/
	 * Visit/instrument.
	 * 
	 * @param context
	 * @param errors
	 * @param importDefinition
	 * @param importSetup
	 * @param lineNum
	 * @return
	 */
	protected Event otherExistsHandling(RequestContext context, BindingResult errors, 
			CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, CrmsImportLog importLog, 
			int lineNum) {
		// do nothing
		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}
	
	

	
	/**
	 * Subclasses override to instantiate a custom, typically instance specific Patient subclass.
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @return
	 */
	protected Patient createPatient(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup) {
		return new Patient();
	}

	
	/** 
	 * Subclasses override to instantiate a custom, typically instance specific ContactInfo subclass.
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @return
	 */
	protected ContactInfo createContactInfo(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup) {
		return new ContactInfo();
	}

	
	/**
	 * Subclasses override to instantiate a custom, typically instance specific Caregiver subclass.
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @return
	 */
	protected Caregiver createCaregiver(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup) {
		return new Caregiver();
	}

	
	/**
	 * Subclasses override to instantiate a custom, typically instance specific Patient subclass.
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @return
	 */
	protected EnrollmentStatus createEnrollmentStatus(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup) {
		return enrollmentManager.getEnrollmentStatusPrototype(importSetup.getRevisedProjName());
	}

	
	/** 
	 * Subclasses override to instantiate a custom, typically instance specific Visit subclass.
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @return
	 */
	protected Visit createVisit(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup) {
		return new Visit();
	}

	/**
	 * Subclasses override if instrument creation requires custom behavior.
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @param instrClazz
	 * @param p
	 * @param projName
	 * @param v
	 * @param instrType
	 * @param dcDate
	 * @param dcStatus
	 * @return
	 */
	protected Instrument createInstrument(RequestContext context, CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, 
			Class instrClazz, String instrType, String instrVer, Date dcDate, String dcStatus) {
//TODO: if instrVer is specified, use it to instantiate an instrument with the specified version.		
//		In simple cases, just setInstrVer, setFormVer after assessmentService.create returns newly instantiated object Consult the ChangeVersion proc for each instrument.
//		May need to instantiate a different class depending upon the version (UdsFamilyHistory3 vs. UdsFamilyHistory2). Since nothing has been persisted a given class will use the given Hibernate mapping so no issue.
//
//		For existing instrument match, will have to add instrVer to the match (if no match, could match without instrVer and if there is a match give an error message instead of proceeding)
//
//		Hardest thing may be this:  will need to make sure instrument has that version (definition UI could use the same technique that Change Version does to determine the list of versions for a given instrument. would have to refresh page anytime an instrument is selected). May be easier to enforce at run-time, createInstrument will have hard-coded versions to determine what to do, so those can be used to validate version.

		// create instrument-specific object
		return Instrument.create(instrClazz, importSetup.getPatient(), importSetup.getVisit(), importSetup.getRevisedProjName(), 
				instrType, dcDate, dcStatus);
	}
	

	
	/**
	 * setPropertyHandling
	 * 
	 * Iterate over all field/property values in the current data import record, setting the
	 * value on the entity property designated by the import mapping file column and property
	 * with the same column index. 
	 * 
	 * This method is about matching each data value with the entity and property on which it
	 * should be set. When the entity and property are determined then a setProperty method
	 * is called to actually set the imported data value on an entity property. 
	 * 
	 * @param context
	 * @param errors
	 * @param importDefinition
	 * @param importSetup
	 * @param importLog
	 * @param lineNum
	 * @return success Event to continue processing this record, error Event to abort processing this record 
	 */
	protected Event setPropertyHandling(RequestContext context, BindingResult errors, 
			CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, CrmsImportLog importLog, int lineNum) throws Exception {
		Event returnEvent = new Event(this, SUCCESS_FLOW_EVENT_ID);
		String definitionColName, definitionPropName, definitionEntityName, definitionDefaultValue;
		
		// the primary part of an assessment import is importing the instrument variables. to make the end users job easier, support
		// ignoring case on the instrument property names in the import definition mapping file. part of the implementation for that 
		// involves acquiring the set of all correct instrument property names, and that would be expensive to retrieve when processing
		// each property, so instead just retrieve it once here and pass it into setProperty for each instrument property

		// this is not done for patient, enrollmentStatus or visit properties. those are case-sensitive. some of those properties are
		// required. essentially those properties can be considered part of a formal import specification so requiring case-sensitivity
		// for them is acceptable
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
		try {
			if (!importDefinition.getPatientOnlyImport()) {
				instrPropNamesMap = PropertyUtils.describe(importSetup.getInstrument());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType2())) {
				instr2PropNamesMap = PropertyUtils.describe(importSetup.getInstrument2());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType3())) {
				instr3PropNamesMap = PropertyUtils.describe(importSetup.getInstrument3());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType4())) {
				instr4PropNamesMap = PropertyUtils.describe(importSetup.getInstrument4());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType5())) {
				instr5PropNamesMap = PropertyUtils.describe(importSetup.getInstrument5());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType6())) {
				instr6PropNamesMap = PropertyUtils.describe(importSetup.getInstrument6());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType7())) {
				instr7PropNamesMap = PropertyUtils.describe(importSetup.getInstrument7());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType8())) {
				instr8PropNamesMap = PropertyUtils.describe(importSetup.getInstrument8());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType9())) {
				instr9PropNamesMap = PropertyUtils.describe(importSetup.getInstrument9());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType10())) {
				instr10PropNamesMap = PropertyUtils.describe(importSetup.getInstrument10());
			}		
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType11())) {
				instr11PropNamesMap = PropertyUtils.describe(importSetup.getInstrument11());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType12())) {
				instr12PropNamesMap = PropertyUtils.describe(importSetup.getInstrument12());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType13())) {
				instr13PropNamesMap = PropertyUtils.describe(importSetup.getInstrument13());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType14())) {
				instr14PropNamesMap = PropertyUtils.describe(importSetup.getInstrument14());
			}
			if (!importDefinition.getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType15())) {
				instr15PropNamesMap = PropertyUtils.describe(importSetup.getInstrument15());
			}
		}
		catch (InvocationTargetException ex2) {
			importLog.addErrorMessage(lineNum, "[InvocationTargetException] Error on PropertyUtils.describe. Patient:" + 
					(importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + this.getVisitInfo(importDefinition, importSetup));
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		catch (IllegalAccessException ex2) {
			importLog.addErrorMessage(lineNum, "[IllegalAccessException] Error on PropertyUtils.describe. Patient:" + 
					(importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + this.getVisitInfo(importDefinition, importSetup));
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		catch (NoSuchMethodException ex2) {
			importLog.addErrorMessage(lineNum, "[NoSuchException] Error on PropertyUtils.describe. Patient:" + 
					(importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + this.getVisitInfo(importDefinition, importSetup));
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}

		// iterate thru the mapping array so don't miss static value mappings, which would not be known from the data array
		// since it was validated that every column in the data file has a corresponding column in the mapping file, will get every data value to set
		// if there are any mapping array columns that do not correspond to the data array and are not static values, ignore them
		for (int mappingIndex = 0; mappingIndex < importSetup.getMappingCols().length; mappingIndex++) {
			returnEvent = new Event(this, SUCCESS_FLOW_EVENT_ID);

			// use type Integer for this index so can test for null when retrieving from mappingColDataCol Map
			Integer dataIndex = importSetup.getMappingColDataCol().get(mappingIndex);
			
			definitionColName = importSetup.getMappingCols()[mappingIndex];
			// skip fields when import definition mapping column name prefixed by SKIP_INDICATOR 
			// note: column name following SKIP_INDICATOR must match data column name, this is validated earlier in the import process
			// TODO: check existing mapping files in lava3 (pedi) to alter how they use XX as this was the old lava3 code:
			//	if (definitionColName.startsWith("XX") || 
			//		(definitionEntityName != null && definitionEntityName.startsWith("XX")) ||
			//		(definitionPropName != null && definitionPropName.startsWith("XX"))) { 
			if (definitionColName.startsWith(SKIP_INDICATOR)) { 
				continue;
			}

			definitionEntityName = importSetup.getMappingEntities()[mappingIndex]; // this is instrType, not instrTypeEncoded
			definitionPropName = importSetup.getMappingProps()[mappingIndex];
			if (importSetup.getMappingDefaults() == null || !StringUtils.hasText(importSetup.getMappingDefaults()[mappingIndex])) {
				definitionDefaultValue = null;
			}
			else {
				definitionDefaultValue = importSetup.getMappingDefaults()[mappingIndex];
			}
			
			logger.info("dataIndex=" + dataIndex + " mappingIndex=" + mappingIndex + " colName=" + definitionColName + " entityName=" + definitionEntityName + " propName=" + definitionPropName + " defaultValue=" + definitionDefaultValue + " lineNum=" + lineNum);

			Object staticValue = null;
			if (definitionColName.startsWith(STATIC_INDICATOR)) { 
				staticValue = definitionColName.substring(STATIC_INDICATOR.length());
			}
			

			// Set Property Values

			// instruments

			// note that if the instrument should not be updated because it has already been data entered and user
			// has specified not to overwrite in this case (in the import definition), the import record will have 
			// already been skipped and we will not get to the setting of properties
			
			// shorthand can be used in the mapping file for the first instrument specified in the 
			// importDefinition mapping file. the entity can be left blank and the first instrument will be 
			// used as the entity. this eases creation of mapping files because:
			// a) most of the time there is only one instrument in a data file so no need to populate the entity
			//    in the mapping for all instrument variables
			// b) an instrument may have many, many variables so this cuts down on what has to be set up in
			//    the mapping file for each variable
			// note that all other entities must supply the entity in the entity row, i.e. Patient, EnrollmentStatus,
			// Visit, etc. and all instruments beyond the first one
			if (!StringUtils.hasText(definitionEntityName)) {
				definitionEntityName = ((CrmsImportDefinition)importDefinition).getInstrMappingAlias() != null ? ((CrmsImportDefinition)importDefinition).getInstrMappingAlias() : importSetup.getInstrument().getInstrType();
			}

			// give this first instrument first shot at determining if the property applies to it, to support
			// this shorthand

			if (dataIndex == null && staticValue == null) {
				// do nothing. mapping file column does not reference a variable and value in the data file, nor is it a default value.
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && 
				(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias()))) {
				// for all instruments, if the property name is left blank in the mapping file that means that the
				// column name is the same as the property name, so there is no need to redundantly specify the property 
				// name as well.
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				
				// set property on the first instrument specified in importDefinition
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instrPropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType2()) &&
						(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument2().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias2()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument2(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr2PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType3()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument3().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias3()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument3(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr3PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType4()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument4().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias4()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument4(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr4PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType5()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument5().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias5()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument5(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr5PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType6()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument6().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias6()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument6(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr6PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType7()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument7().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias7()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument7(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr7PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType8()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument8().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias8()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument8(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr8PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType9()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument9().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias9()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument9(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr9PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType10()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument10().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias10()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument10(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr10PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType11()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument11().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias11()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument11(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr11PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType12()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument12().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias12()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument12(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr12PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType13()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument13().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias13()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument13(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr13PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType14()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument14().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias14()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument14(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr14PropNamesMap.keySet(), staticValue, lineNum);
			}
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && StringUtils.hasText(importDefinition.getInstrType15()) &&
					(definitionEntityName.equalsIgnoreCase(importSetup.getInstrument15().getInstrType()) || definitionEntityName.equalsIgnoreCase(((CrmsImportDefinition)importDefinition).getInstrMappingAlias15()))) {
				String propName = (!StringUtils.hasText(definitionPropName) ? definitionColName : definitionPropName);
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument15(), definitionEntityName, propName, definitionDefaultValue, dataIndex, instr15PropNamesMap.keySet(), staticValue, lineNum);
			}
			//Patient properties
			else if (definitionEntityName.equalsIgnoreCase("patient")) {
				// if decide to allow updates on existing Patients, then have the flags in data model to do: 
				// (importSetup.isPatientCreated() || importDefinition.getAllowPatientUpdates())
				// same for EnrollmentStatus, Visit
				
				//thinking is that should not have Patient, EnrollmentStatus, Visit allowUpdate flags because import 
				//will be used for either 
				//a) creating these if they do not exist and importing assessment data, or
				//b) importing assessment data, 
				//i.e. import is not a mechanism for updating Patient, EnrollmentStatus and Visit data
				if (importSetup.isPatientCreated()) {
					// don't need to set properties already set when Patient was created
					if (!definitionPropName.equalsIgnoreCase("firstName") && !definitionPropName.equalsIgnoreCase("lastName") && !definitionPropName.equalsIgnoreCase("birthDate")
							&& !definitionPropName.equalsIgnoreCase("gender")) {
						returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getPatient(), definitionEntityName, definitionPropName, definitionDefaultValue, dataIndex, null, staticValue, lineNum);
					}
				}
			}
			//ContactInfo properties
			else if (definitionEntityName.equalsIgnoreCase("contactInfo")) {
				if (importSetup.isContactInfoCreated()) {
					// don't need to set properties already set when ContactInfo was created
					if (!definitionPropName.equalsIgnoreCase("address") && !definitionPropName.equalsIgnoreCase("city") &&
							!definitionPropName.equalsIgnoreCase("state") && !definitionPropName.equalsIgnoreCase("zip") &&
							!definitionPropName.equalsIgnoreCase("phone1") && !definitionPropName.equalsIgnoreCase("phone2") && 
							!definitionPropName.equalsIgnoreCase("email")) {
						returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getContactInfo(), definitionEntityName, definitionPropName, definitionDefaultValue, dataIndex, null, staticValue, lineNum);
					}
				}
			}
			//Caregiver properties
			else if (definitionEntityName.equalsIgnoreCase("caregiver")) {
				if (importSetup.isCaregiverCreated()) {
					// don't need to set properties already set when Caregiver was created
					if (!definitionPropName.equalsIgnoreCase("firstName") && !definitionPropName.equalsIgnoreCase("lastName")) {
						returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getCaregiver(), definitionEntityName, definitionPropName, definitionDefaultValue, dataIndex, null, staticValue, lineNum);
					}
				}
			}
			//Caregiver ContactInfo properties
			else if (definitionEntityName.equalsIgnoreCase("caregiverContactInfo")) {
				if (importSetup.isCaregiverContactInfoCreated()) {
					// don't need to set properties already set when Caregiver ContactInfo was created
					if (!definitionPropName.equalsIgnoreCase("address") && !definitionPropName.equalsIgnoreCase("city") &&
							!definitionPropName.equalsIgnoreCase("state") && !definitionPropName.equalsIgnoreCase("zip") &&
							!definitionPropName.equalsIgnoreCase("phone1") && !definitionPropName.equalsIgnoreCase("phone2") &&
							!definitionPropName.equalsIgnoreCase("email")) {
						returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getCaregiverContactInfo(), definitionEntityName, definitionPropName, definitionDefaultValue, dataIndex, null, staticValue, lineNum);
					}
				}
			}
			//Caregiver2 properties
			else if (definitionEntityName.equalsIgnoreCase("caregiver2")) {
				if (importSetup.isCaregiver2Created()) {
					// don't need to set properties already set when Caregiver2 was created
					if (!definitionPropName.equalsIgnoreCase("firstName") && !definitionPropName.equalsIgnoreCase("lastName")) {
						returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getCaregiver2(), definitionEntityName, definitionPropName, definitionDefaultValue, dataIndex, null, staticValue, lineNum);
					}
				}
			}
			//Caregiver2 ContactInfo properties
			else if (definitionEntityName.equalsIgnoreCase("caregiver2ContactInfo")) {
				if (importSetup.isCaregiver2ContactInfoCreated()) {
					// don't need to set properties already set when Caregiver2 ContactInfo was created
					if (!definitionPropName.equalsIgnoreCase("address") && !definitionPropName.equalsIgnoreCase("city") &&
							!definitionPropName.equalsIgnoreCase("state") && !definitionPropName.equalsIgnoreCase("zip") &&
							!definitionPropName.equalsIgnoreCase("phone1") && !definitionPropName.equalsIgnoreCase("phone2") &&
							!definitionPropName.equalsIgnoreCase("email")) {
						returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getCaregiver2ContactInfo(), definitionEntityName, definitionPropName, definitionDefaultValue, dataIndex, null, staticValue, lineNum);
					}
				}
			}
			//EnrollmentStatus properties
			else if (definitionEntityName.equalsIgnoreCase("enrollmentStatus")) {
				if (importSetup.isEnrollmentStatusCreated()) {
					// don't need to set properties already set when EnrollmentStatus was created
					if (!definitionPropName.equalsIgnoreCase("projName") && !definitionPropName.equalsIgnoreCase("statusDate") && !definitionPropName.equalsIgnoreCase("statusDesc")) {
						returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getEnrollmentStatus(), definitionEntityName, definitionPropName, definitionDefaultValue, dataIndex, null, staticValue, lineNum);
					}
				}
			}
			//Visit properties
			else if (!((CrmsImportDefinition)importDefinition).getPatientOnlyImport() && definitionEntityName.equalsIgnoreCase("visit")) {
				if (importSetup.isVisitCreated()) {
					// don't need to set properties already set when Visit was created
					if (!definitionPropName.equalsIgnoreCase("visitDate") && !definitionPropName.equalsIgnoreCase("visitType") && !definitionPropName.equalsIgnoreCase("visitLocation")
							&& !definitionPropName.equalsIgnoreCase("visitWith") && !definitionPropName.equalsIgnoreCase("visitStatus")) {
						returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getVisit(), definitionEntityName, definitionPropName, definitionDefaultValue, dataIndex, null, staticValue, lineNum);
					}
				}
			}
			else {
				// allow subclasses to set entity properties for any custom behavior
				returnEvent = setOtherPropertyHandling(importDefinition, importSetup, importLog, dataIndex, definitionEntityName, definitionPropName, definitionDefaultValue, instrPropNamesMap.keySet(), staticValue, lineNum);
			}
			
			// abort import of the current record if there was an error setting the imported value on the property
			if (returnEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}
		
		return returnEvent;
	}
	

	
	/**
	 * setInstrumentCaregiver
	 *
	 * If the instrument has a caregiver (informant) property, set a caregiver on the property if the data file contains
	 * a caregiver (i.e. first and last name). The first and last name of the caregiver would either match an existing 
	 * caregiver or populate a new caregiver.
	 * 
	 * The instrument caregiver property must be named "caregiver" and must be of type Caregiver. Caregiver instruments
	 * that currently only have a caregiver id property must be refactored to also include a Caregiver property (this does
	 * not require any change to the database schema. Hibernate mapping must change to an association, Java code must have
	 * a Caregiver property and special setter code, handler must support changing Caregiver). 
	 * The reason this is required rather than just setting the caregiver id property is because if a new Caregiver has
	 * just been created by this import, the new Caregiver has not been persisted and thus does not have a caregiver
	 * id to set on the instrument. By setting the Caregiver instead of an id, Hibernate will take care of setting 
	 * the caregiver id on the instrument when the transaction to save all of the entities created for all of the import
	 * records is committed to the database.
	 * 
	 * @param context
	 * @param errors
	 * @param importDefinition
	 * @param importSetup
	 * @param importLog
	 * @param instrument
	 * @param instrType
	 * @param lineNum
	 * @return success Event to continue processing this record, error Event to abort processing this record 
	 */
	protected Event setInstrumentCaregiver(RequestContext context, BindingResult errors, 
			CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, CrmsImportLog importLog, 
			Instrument instrument, String instrType, int lineNum) throws Exception {
		Event returnEvent = new Event(this, SUCCESS_FLOW_EVENT_ID);

		if (importSetup.isCaregiverCreated() || importSetup.isCaregiverExisted()) {
			// note: if there are multiple instruments in the data file, each caregiver instrument will set its caregiver property to the
			// same Caregiver, i.e. the Cargiver that was either created or matched based on a caregiver first and last name in the data file
			// in other words, the assumption is that the same caregiver is used for all instruments in the same record of the data file 
			
			//note: if for some reason encounter a caregiver instrument where the name of the Caregiver property is not "caregiver" then 
			//could add a caregiver property name to CrmsImportDefinition
			instrument.setCaregiver(importSetup.getCaregiver());
		}
		else {
			importLog.addWarningMessage(lineNum, "No Caregiver found or created to assign to instrument:" + instrType + " for patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()));
		}
			
		return returnEvent;
	}
	
	
	
	/**
	 * Subclasses override this if setting a property involves any custom behavior.
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @param entity
	 * @param propName
	 * @param dataIndex
 	 * @param propNamesSet	populated in setPropertyHandling and any other methods that call setProperty. contains 
 	 * 						all of the property names of the entity to which propName belongs to facilitate 
 	 * 						ignoring case when matching propName to property on entity. this is only populated for 
 	 * 						instrument properties and is empty when setting a property for a non instrument entity
 	 * @param lineNum
	 * @throws Exception
	 */
	protected Event setProperty(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup,	CrmsImportLog importLog, LavaEntity entity, String entityName, 
			String propName, String definitionDefaultValue, Integer dataIndex, Set<String> propNamesSet, Object staticValue, int lineNum) throws Exception {
		String entityPropName = propName; // if there is a case mismatch the entityPropName will be reset to the correct case
		boolean propIgnoreCaseMatch = false;

		// BeanUtils.setProperty requires registering a converter setting Date and DateTime values. The import definition has the expected
		// formats for dates and times in the data file, so register patterns for just the date, and for the date and time, so that a data
		// file value with either can be set
		DateConverter converter = new DateConverter();
		converter.setPatterns(new String[]{importDefinition.getDateFormat() + " " + importDefinition.getTimeFormat(), importDefinition.getDateFormat()});
		this.getConvertUtilsBean().register(converter, Date.class);

		// if not mapped as a staticValue, get the value for the property from the dataValues array
		if (staticValue == null) {
			// default BeanUtils converter will set empty values to a default which is not null, so change the
			// behavior so property is set to null
			// note: could just skip null values since property value is already null on new instrument, but if
			// this code were to be used for an import update then would need to set null values
		
			//TODO: consult the metadata for each property
			// 1) if the property is a string/text value then check the length of the data vs. the max string length. 
			//    add a flag to import definition about how user wants this handled: either do not import record and 
			//    create error, or truncate the string to the max length and import it and create warning
			// 2) validate data value by obtaining metadata for the entity.property, i.e. list of valid values
		
			// handle blank data values
			if (!StringUtils.hasText(importSetup.getDataValues()[dataIndex])) {
				
				// determine if there is either a default for the current property in the mapping file, or a global default from the import definition
				// "NULL" in import definition mapping file as a default will leave the value blank instead of setting it to the import definition instrument
				// default code 
				if (StringUtils.hasText(definitionDefaultValue) && definitionDefaultValue.equalsIgnoreCase("NULL")) {
					// leave the value blank 
					
					// temporarily change the conversion handling so if no value to convert, just sets property null instead of
					// throwing an exception
					// false -use a default value instead of throwing a conversion exception (for any conversions)
					// true - use null for the default value
					// -1 - array types defaulted to null
					this.getConvertUtilsBean().register(false, true, -1);
				}
				else if (StringUtils.hasText(definitionDefaultValue) || StringUtils.hasText(importDefinition.getInstrDefaultCode())) {
					String defaultValue = StringUtils.hasText(definitionDefaultValue) ? definitionDefaultValue : importDefinition.getInstrDefaultCode();
					// register so that default value is used if there is no data value to convert
					this.getConvertUtilsBean().register(false, false, -1);
					// register the default value for each possible data type
					// note: leaving Boolean out for now. could not use it for import definition global default since that is a standard
					// error code and those values can not be converted to Boolean
					this.getConvertUtilsBean().register(new StringConverter(defaultValue), String.class);
					this.getConvertUtilsBean().register(new ByteConverter(defaultValue), Byte.class);
					this.getConvertUtilsBean().register(new ShortConverter(defaultValue), Short.class);
					this.getConvertUtilsBean().register(new LongConverter(defaultValue), Long.class);
					this.getConvertUtilsBean().register(new FloatConverter(defaultValue), Float.class);
					this.getConvertUtilsBean().register(new DoubleConverter(defaultValue), Double.class);
				}
				else {
					
					// temporarily change the conversion handling so if no value to convert, just sets property null instead of
					// throwing an exception
					// false -use a default value instead of throwing a conversion exception (for any conversions)
					// true - use null for the default value
					// -1 - array types defaulted to null
					this.getConvertUtilsBean().register(false, true, -1);
				}
			}

			logger.info("setting entityName="+entityName+" propName="+propName+" to value="+importSetup.getDataValues()[dataIndex] + " lineNum=" + lineNum);
		}
		else {
			// this really doesn't make sense, i.e. that there would be a static value of null. instead, should just not map the property
			if (!StringUtils.hasText(staticValue.toString())) {
				this.getConvertUtilsBean().register(false, true, -1);
			}
			logger.info("setting entityName="+entityName+" propName="+propName+" to " + STATIC_INDICATOR + " value="+staticValue + " lineNum=" + lineNum);
		}


		// for error messaging 
		String visitDateAsString = ((CrmsImportDefinition)importDefinition).getPatientOnlyImport() ? "" : "Visit Date:" + importSetup.getVisit().getVisitDate(); 

		try {
			// try for a case-sensitive exact match first
			
			// do a getSimpleProperty to determine if the mapped property name matches a property. if it does not
			// then this method will throw NoSuchMethodException. have to do this separately from setProperty because
			// BeanUtils.setProperty does not throw that exception and does not indicate anything if the property 
			// does not exist (PropertyUtils.setProperty does, but using BeanUtils.setProperty because it does
			// automatic type conversion without having to explicitly specify the type)

			// NOTE: if need to support setting an indexed or nested property, will need to modify this. The call to 
			// getSimpleProperty will throw this exception:
			// IllegalArgumentException - if the property name is nested or indexed
			// so could catch this exception and call various other methods, such as PropertyUtils.getMappedProperty
			// for now, the instrument notes property is a Map and all instruments have the property, so we just skip
			// the check in that case.
			
			if (!(entity instanceof Instrument) || !propName.startsWith("notes")) {
				Object propValue = PropertyUtils.getSimpleProperty(entity, propName);
			}
		}
		catch (NoSuchMethodException ex) {
			// currently propNamesSet is an empty set, rather than null, when case insensitive matching should be used,
			// so this clause will never be met and will always go to the else clause
			if (propNamesSet == null) {
				// property does not exist so this is an error - and since propNamesSet not supplied not checking whether 
				// the property exists when ignoring case. this would be the case for all non-instrument properties since 
				// only doing match ignoring case for instrument properties
				importLog.addErrorMessage(lineNum, "Property name in import definition mapping file does not exist. Check spelling of property name. Property:" + propName + " Value:" + (staticValue == null ? importSetup.getDataValues()[dataIndex] : staticValue) +  
						" Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + this.getVisitInfo(importDefinition, importSetup));
				return new Event(this, ERROR_FLOW_EVENT_ID);
			}
			else {
				// if case-sensitive match failed, try a case-insensitive match iterating through all properties of the instrument
				// until a match is found
				
				// if the set of property names for the entity was populated then facilitate ignoring case when matching the 
				// property name as specified in the import definition mapping file against the real property name. this is 
				// primarily for instruments since in most cases the bulk of the import will be instrument variable data, and 
				// makes it easier on those creating import definition mapping files so they don't have to be concerned with 
				// an exact match on case
				
				// if the mapped property name does not exist, before returning an error, see if it is just a case
				// mismatch and if so use the correct property name.
				
				// non-instrument entities will not populate propNamesSet so it will be empty and the following loop will 
				// not iterate and there will be an error
				for (String beanPropName : propNamesSet){
					if (propName.equalsIgnoreCase(beanPropName)) {
						entityPropName = beanPropName;
						propIgnoreCaseMatch = true;
						break;
					}
				}

				if (!propIgnoreCaseMatch) {
					importLog.addErrorMessage(lineNum, "Property name in import definition mapping file does not exist in database. Check spelling of property name. Property:" + propName + " Value:" + (staticValue == null ? importSetup.getDataValues()[dataIndex] : staticValue) +  
						" Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + this.getVisitInfo(importDefinition, importSetup));
					return new Event(this, ERROR_FLOW_EVENT_ID);
				}
				else {
					// only report case mismatch warnings on the first data record because every data record uses the same mapping property names and will have
					// the same warnings
					// update: not much point in giving this warning. could potentially be many needless warning messages so commenting out for now
					//if (importDefinition.getStartDataRow().equals((short)lineNum)) {
					//	importLog.addWarningMessage(lineNum, "Value was imported, but there was a case mismatch between property name in import definition mapping file:" + propName + " and real property name:" + entityPropName + " for all rows");
					//}
				}
			}
		}

		try {
			// use Apache Commons BeanUtils rather than PropertyUtils as BeanUtils will convert the data value
			// from String to its correct type
			
			if (staticValue == null) {
				BeanUtils.setProperty(entity, entityPropName, importSetup.getDataValues()[dataIndex]);
			}
			else {
				BeanUtils.setProperty(entity, entityPropName, staticValue);
			}
		}
		catch (InvocationTargetException ex) {
			importLog.addErrorMessage(lineNum, "[InvocationTargetException] setProperty: Data file index:" + dataIndex 
					+ " Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + this.getVisitInfo(importDefinition, importSetup)
					+ " Data file variable:" + importSetup.getDataCols()[dataIndex] + " Data file value:" + importSetup.getDataValues()[dataIndex] 
					+ " Mapping entity:" + entityName + " Mapping property:" + propName
					+ "<br>Message:" + ex.getMessage());
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		catch (IllegalAccessException ex) {
			importLog.addErrorMessage(lineNum, "[IllegalAccessException] setProperty: Data file index:" + dataIndex 
					+ " Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + this.getVisitInfo(importDefinition, importSetup)
					+ " Data file variable:" + importSetup.getDataCols()[dataIndex] + " Data file value:" + importSetup.getDataValues()[dataIndex] 
					+ " Mapping entity:" + entityName + " Mapping property:" + propName
					+ "<br>Message:" + ex.getMessage());
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		catch (Exception ex) {
			importLog.addErrorMessage(lineNum, "[Exception] setProperty: Data file index:" + dataIndex 
					+ " Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + this.getVisitInfo(importDefinition, importSetup)
					+ " Data file variable:" + importSetup.getDataCols()[dataIndex] + " Data file value:" + importSetup.getDataValues()[dataIndex] 
					+ " Mapping entity:" + entityName + " Mapping property:" + propName
					+ "<br>Message:" + ex.getMessage());
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}

		if (staticValue == null) {
			if (!StringUtils.hasText(importSetup.getDataValues()[dataIndex])) {
				// resume throwing exceptions (second and third arguments ignored in this case)
				this.setupBeanUtilConverters(importSetup);
			}
		}		
		else {
			if (!StringUtils.hasText(staticValue.toString())) {
				// resume throwing exceptions (second and third arguments ignored in this case)
				this.setupBeanUtilConverters(importSetup);
			}
		}

		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}

	/**
	 * Subclasses override this to set a value on a property of an entity other than Patient,
	 * Visit, EnrollmentStatus or the instrument. 
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @param importLog
	 * @param dataIndex
	 * @param propNamesSet
	 * @param lineNum
	 * @throws Exception
	 */
	protected Event setOtherPropertyHandling(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, 
			CrmsImportLog importLog, Integer dataIndex, String definitionEntityName, String definitionPropName, String definitionDefaultValue,
			Set<String> propNamesSet, Object staticValue, int lineNum) throws Exception {
		// if property was not set in setPropertyHandling then it is likely that there is a mapping problem, or it is
		// a custom property that a subclass should handle in an overridden setOtherPropertyHandling
		importLog.addErrorMessage(lineNum, "Property not set: Data file index:" + dataIndex 
				+ " Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) + this.getVisitInfo(importDefinition, importSetup)
				+ " Data file variable:" + importSetup.getDataCols()[dataIndex] + " Data file value:" + importSetup.getDataValues()[dataIndex] 
				+ " Mapping entity:" + definitionEntityName + " Mapping property:" + definitionPropName);
		return new Event(this, ERROR_FLOW_EVENT_ID);
	}


	/**
	 * saveImportRecord
	 * 
	 * Persist the import record to the applicable entities. 
	 * 
	 * Subclasses should override if they involve additional entities, such as ContactInfo or
	 * Caregiver, or for other custom handling. Make sure they call this superclass method. 
	 */
	protected Event saveImportRecord(RequestContext context, CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, CrmsImportLog importLog, int lineNum) {
		// for new entities must explicitly save since not associated with a Hibernate session. for
		// updates, entity was retrieved and thus attached to a session and Hibernate dirty checking should
		// implicitly update the entity

		try {
			if (importSetup.isPatientCreated()) {
				importSetup.getPatient().save();
				logger.info("saving Patient lineNum="+lineNum);
				importLog.addCreatedMessage(lineNum, "PATIENT CREATED:" + importSetup.getPatient().getFullName(),
						this.getCrmsImportLogMessageInfo("Patient", importDefinition, importSetup, null));
			}
			if (importSetup.isContactInfoCreated()) {
				logger.info("saving ContactInfo lineNum="+lineNum);
				importSetup.getContactInfo().save();
				importLog.addCreatedMessage(lineNum, "CONTACT INFO CREATED, Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()),
						this.getCrmsImportLogMessageInfo("Contact Info", importDefinition, importSetup, null));
			}
			if (importSetup.isCaregiverCreated()) {
				logger.info("saving Caregiver lineNum="+lineNum);
				importSetup.getCaregiver().save();
				importLog.addCreatedMessage(lineNum, "CAREGIVER CREATED, Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) +
					" Caregiver:" + importSetup.getCaregiver().getFullName(),
					this.getCrmsImportLogMessageInfo("Caregiver", importDefinition, importSetup, null));
			}
			if (importSetup.isCaregiverContactInfoCreated()) {
				logger.info("saving Caregiver ContactInfo lineNum="+lineNum);
				importSetup.getCaregiverContactInfo().save();
				importLog.addCreatedMessage(lineNum, "CAREGIVER CONTACT INFO CREATED, Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) +
					" Caregiver:" + importSetup.getCaregiver().getFullName(),
					this.getCrmsImportLogMessageInfo("Caregiver Contact Info", importDefinition, importSetup, null));
			}
			if (importSetup.isCaregiver2Created()) {
				logger.info("saving Caregiver2 lineNum="+lineNum);
				importSetup.getCaregiver2().save();
				importLog.addCreatedMessage(lineNum, "CAREGIVER CREATED, Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) +
					" Caregiver:" + importSetup.getCaregiver2().getFullName(),
					this.getCrmsImportLogMessageInfo("Caregiver2", importDefinition, importSetup, null));
			}
			if (importSetup.isCaregiver2ContactInfoCreated()) {
				logger.info("saving Caregiver2 ContactInfo lineNum="+lineNum);
				importSetup.getCaregiver2ContactInfo().save();
				importLog.addCreatedMessage(lineNum, "CAREGIVER CONTACT INFO CREATED, Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) +
					" Caregiver:" + importSetup.getCaregiver2().getFullName(),
					this.getCrmsImportLogMessageInfo("Caregiver2 Contact Info", importDefinition, importSetup, null));
			}
			if (importSetup.isEnrollmentStatusCreated()) {
				logger.info("saving EnrollmentStatus lineNum="+lineNum);
				importSetup.getEnrollmentStatus().save();
				importLog.addCreatedMessage(lineNum, "ENROLLMENTSTATUS CREATED, Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) +
					" Project:" + importSetup.getRevisedProjName(),
					this.getCrmsImportLogMessageInfo("Enrollment Status", importDefinition, importSetup, null));
			}
			
			if (!importDefinition.getPatientOnlyImport()) {

				if (importSetup.isVisitCreated()) {
					logger.info("saving Visit lineNum="+lineNum);
					importSetup.getVisit().save();
					importLog.addCreatedMessage(lineNum, "VISIT CREATED, Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) +
						this.getVisitInfo(importDefinition, importSetup),
						this.getCrmsImportLogMessageInfo("Visit", importDefinition, importSetup, null));
				}
				
				if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstrCreated(), importSetup.isInstrExisted(), importSetup.isInstrExistedWithData(), 
						importDefinition.getInstrCalculate(), importSetup.getInstrument(), importDefinition.getInstrType()).getId().equals(ERROR_FLOW_EVENT_ID))) {
					return new Event(this, ERROR_FLOW_EVENT_ID);
				}
				if (StringUtils.hasText(importDefinition.getInstrType2())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr2Created(), importSetup.isInstr2Existed(), importSetup.isInstr2ExistedWithData(), 
							importDefinition.getInstrCalculate2(), importSetup.getInstrument2(), importDefinition.getInstrType2()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType3())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr3Created(), importSetup.isInstr3Existed(), importSetup.isInstr3ExistedWithData(), 
							importDefinition.getInstrCalculate3(), importSetup.getInstrument3(), importDefinition.getInstrType3()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType4())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr4Created(), importSetup.isInstr4Existed(), importSetup.isInstr4ExistedWithData(), 
							importDefinition.getInstrCalculate4(), importSetup.getInstrument4(), importDefinition.getInstrType4()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType5())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr5Created(), importSetup.isInstr5Existed(), importSetup.isInstr5ExistedWithData(), 
							importDefinition.getInstrCalculate5(), importSetup.getInstrument5(), importDefinition.getInstrType5()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType6())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr6Created(), importSetup.isInstr6Existed(), importSetup.isInstr6ExistedWithData(), 
							importDefinition.getInstrCalculate6(), importSetup.getInstrument6(), importDefinition.getInstrType6()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType7())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr7Created(), importSetup.isInstr7Existed(), importSetup.isInstr7ExistedWithData(), 
							importDefinition.getInstrCalculate7(), importSetup.getInstrument7(), importDefinition.getInstrType7()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType8())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr8Created(), importSetup.isInstr8Existed(), importSetup.isInstr8ExistedWithData(), 
							importDefinition.getInstrCalculate8(), importSetup.getInstrument8(), importDefinition.getInstrType8()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType9())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr9Created(), importSetup.isInstr9Existed(), importSetup.isInstr9ExistedWithData(), 
							importDefinition.getInstrCalculate9(), importSetup.getInstrument9(), importDefinition.getInstrType9()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType10())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr10Created(), importSetup.isInstr10Existed(), importSetup.isInstr10ExistedWithData(), 
							importDefinition.getInstrCalculate10(), importSetup.getInstrument10(), importDefinition.getInstrType10()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType11())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr11Created(), importSetup.isInstr11Existed(), importSetup.isInstr11ExistedWithData(), 
							importDefinition.getInstrCalculate11(), importSetup.getInstrument11(), importDefinition.getInstrType11()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType12())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr12Created(), importSetup.isInstr12Existed(), importSetup.isInstr12ExistedWithData(), 
							importDefinition.getInstrCalculate12(), importSetup.getInstrument12(), importDefinition.getInstrType12()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType13())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr13Created(), importSetup.isInstr13Existed(), importSetup.isInstr13ExistedWithData(), 
							importDefinition.getInstrCalculate13(), importSetup.getInstrument13(), importDefinition.getInstrType13()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType14())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr14Created(), importSetup.isInstr14Existed(), importSetup.isInstr14ExistedWithData(), 
							importDefinition.getInstrCalculate14(), importSetup.getInstrument14(), importDefinition.getInstrType14()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
				if (StringUtils.hasText(importDefinition.getInstrType15())) {
					if ((saveImportRecordInstrHelper(context, importDefinition, importSetup, importLog, lineNum, importSetup.isInstr15Created(), importSetup.isInstr15Existed(), importSetup.isInstr15ExistedWithData(), 
							importDefinition.getInstrCalculate15(), importSetup.getInstrument15(), importDefinition.getInstrType15()).getId().equals(ERROR_FLOW_EVENT_ID))) {
						return new Event(this, ERROR_FLOW_EVENT_ID);
					}
				}
			}
		}
		catch (Exception e) {
			int i = 0;
			// e = InvalidDataAccessResourceException
			// e.cause = DataException
			// e.cause.cause = MysqlDataTruncation
			//  could potentially parse violating property out of cause.message e.g. "Data too long for column 'sp56_list' at row 1"
			
			//TODO: if data truncation exception iterate thru all properties, querying metadata and if property
			// style is: "suggest", "string" or "text", get the max length from the metadata and iterate thru
			// the properties comparing value against max length:
			// if user set flag to truncate and warn: truncate data that exceeds max length and create warning and try again
			// if user set flag to error out on the current record, stop processing this patient record and create error
			
			
			importLog.addErrorMessage(lineNum, "Exception on save. " +
					" Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) +
					"<br>Message:" + e.getMessage() +
					"<br>RootCause:" + ExceptionUtils.getRootCauseMessage(e));
			
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		
		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}
	
	
	protected Event saveImportRecordInstrHelper(RequestContext context, CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, CrmsImportLog importLog, int lineNum,
			Boolean instrCreated, Boolean instrExisted, Boolean instrExistedWithData, Boolean instrCalculate, 
			Instrument instrument, String instrType) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();

		// note that if exception is throw the exception handling in the calling method will catch

		if (instrCreated || instrExisted || (importDefinition.getAllowInstrUpdate() && instrExistedWithData)) {
			
			//TODO: set these status properties after setProperty and before saveImportRecords so that when data is flushed during saveImportRecords
			// when saving prior instrument, the instrument will not be dirty again and thus persisted again when it is saved, which would result in
			// a StaleObjectStateException for a Hibernate optimistic locking failure
				
			/** TODO: at the moment, calculate is done by default because save() calls beforeCreate/afterCreate or beforeUpdate/afterUpdate 
			 * first see if calculate will not be done merely by not importing the individual items that are used to computer summary scores
			 *           if that does not work, need to add a flag on the Instrument class (default TRUE) and set/unset it here so the instrument beforeCreate/afterCreate 
			 *           and beforeUpdate/afterUpdate are only called if the flag is set
			if (!instrCalculate) {
				//TODO: set a flag on Instrument so that it will not run calculations in the before/after hooks
			}
			**/

			instrument.setDcBy(importSetup.getVisit().getVisitWith());
			instrument.setDeBy("IMPORTED");
			instrument.setDeDate(new Date());
			// if import record does not have any errors then instrument will be saved so set data entry
			// status complete; if there are errors import record will be skipped and instrument will not 
			// be saved
			instrument.setDeStatus("Complete");
			instrument.setDeNotes("Data Imported by:" + CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request).getShortUserNameRev());

			if (instrExisted) {
				if (instrument.getVisit().getVisitStatus().equalsIgnoreCase("SCHEDULED")) {
					// even if Visit was not created, this update will be persisted via Hibernate automatic dirty checking
					instrument.getVisit().setVisitStatus("COMPLETE");
				}
				
				if (instrument.getDcStatus().equalsIgnoreCase("Scheduled")) {
					instrument.setDcStatus("Complete");
				}
				
				// if existing visit was matched with a visit window, the data collection date in the data file, which is mapped to visit.visitWith, might
				// be different than the matched Visit visitDate, but the value in the data file should represent the data collection date for the instrument
				String dateOrTimeAsString = importSetup.getDataValues()[importSetup.getIndexVisitDate()];
				SimpleDateFormat formatter = new SimpleDateFormat(importDefinition.getDateFormat() != null ? importDefinition.getDateFormat() : DEFAULT_DATE_FORMAT);
				formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
				// if get this far then the string has already been successfully parsed into a Date in visitExistsHandling so no need to catch exception here
				instrument.setDcDate(formatter.parse(dateOrTimeAsString)); 
			}

				
			logger.info("saving instrument "+ instrument.getInstrType() + " lineNum="+lineNum);
			//TODO: lava2 migration, create a saveNoFlush method and call that here
			importSetup.getInstrument().save();
				
			if (instrCreated) {
				importLog.addCreatedMessage(lineNum, "INSTRUMENT CREATED, Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) +
					this.getVisitInfo(importDefinition, importSetup) + " Instrument:" + instrType,
					this.getCrmsImportLogMessageInfo("Instrument", importDefinition, importSetup, instrument));
			}
			if (instrExisted) {
				importLog.addCreatedMessage(lineNum, "EXISTING INSTRUMENT WITH NO DATA UPDATED, Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) +
					this.getVisitInfo(importDefinition, importSetup) + " Instrument:" + instrType + " (" + instrument.getId() + ")",
					this.getCrmsImportLogMessageInfo("Instrument", importDefinition, importSetup, instrument));
			}
			//NOTE: currently the allowInstrUpdate flag is only enabled in the UI for SYSTEM_ADMIN until implement a user confirmation flow that
			// permits overwriting existing data. Probably want an importLog.addUpdatedMessage method
			if (instrExistedWithData) {
				importLog.addCreatedMessage(lineNum, "EXISTING INSTRUMENT WITH DATA UPDATED (OVERWRITTEN), Patient:" + (importSetup.isPatientExisted() ? importSetup.getPatient().getFullNameWithId() : importSetup.getPatient().getFullName()) +
					this.getVisitInfo(importDefinition, importSetup) + " Instrument:" + instrType + "(" + instrument.getId() + ")",
					this.getCrmsImportLogMessageInfo("Instrument", importDefinition, importSetup, instrument));
			}
		}
		
		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}
	
		
	/**
	 * Called at the end of processing an import record (that was not aborted due to an error).
	 * 
	 */
	protected void updateEntityCounts(CrmsImportSetup importSetup, CrmsImportLog importLog) {
		if (importSetup.isPatientCreated()) {
			importLog.incNewPatients();
		}
		if (importSetup.isPatientExisted()) {
			importLog.incExistingPatients();
		}
		if (importSetup.isContactInfoCreated()) {
			importLog.incNewContactInfo();
		}
		if (importSetup.isContactInfoExisted()) {
			importLog.incExistingContactInfo();
		}
		if (importSetup.isCaregiverCreated()) {
			importLog.incNewCaregivers();
		}
		if (importSetup.isCaregiverExisted()) {
			importLog.incExistingCaregivers();
		}
		if (importSetup.isCaregiverContactInfoCreated()) {
			importLog.incNewCaregiverContactInfo();
		}
		if (importSetup.isCaregiverContactInfoExisted()) {
			importLog.incExistingCaregiverContactInfo();
		}
		// note that caregiver2 and caregiver both count together for NewCaregivers, ExistingCaregivers and NewCaregiverContactInfo
		if (importSetup.isCaregiver2Created()) {
			importLog.incNewCaregivers();
		}
		if (importSetup.isCaregiver2Existed()) {
			importLog.incExistingCaregivers();
		}
		if (importSetup.isCaregiver2ContactInfoCreated()) {
			importLog.incNewCaregiverContactInfo();
		}
		if (importSetup.isCaregiver2ContactInfoExisted()) {
			importLog.incExistingCaregiverContactInfo();
		}
		if (importSetup.isEnrollmentStatusCreated()) {
			importLog.incNewEnrollmentStatuses();
		}
		if (importSetup.isEnrollmentStatusExisted()) {
			importLog.incExistingEnrollmentStatuses();
		}
		if (importSetup.isVisitCreated()) {
			importLog.incNewVisits();
		}
		if (importSetup.isVisitExisted()) {
			importLog.incExistingVisits();
		}
		if (importSetup.isInstrCreated()) {
			importLog.incNewInstruments();
		}
		if (importSetup.isInstrExisted()) {
			importLog.incExistingInstruments();
		}
		if (importSetup.isInstrExistedWithData()) {
			importLog.incExistingInstrumentsWithData();
		}
	}
	
	
	/**
	 * Subclasses should override if the state property of a ContactInfo record is coded and needs to be converted
	 * to a standard two character state abbreviation.
	 * 
	 * @param stateCodeAsString
	 * @param importLog
	 * @param lineNum
	 * @return
	 */
	protected String convertStateCode(String stateCodeAsString, CrmsImportLog importLog, int lineNum) {
		return stateCodeAsString;
	}

	
	
	@Override
	public Map addReferenceData(RequestContext context, Object command,	BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//	load up dynamic lists
	 	StateDefinition state = context.getCurrentState();
		model = super.addReferenceData(context, command, errors, model); 
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		CrmsImportSetup crmsImportSetup = (CrmsImportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
	 	if (state.getId().equals("edit")) {
			// note that this list is filtered via projectAuth filter. CrmsAuthUser getAuthDaoFilters determines the projects to
			// which a user has some kind of access. However, the list must be further filtered based on permissions to make sure 
			// the user has the import permission for each project in the list.
			Map<String,String> projList = listManager.getDynamicList(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request), "context.projectList");
			projList = CrmsAuthUtils.filterProjectListByPermission(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request),
					CoreSessionUtils.getCurrentAction(sessionManager,request), projList);
			dynamicLists.put("context.projectList", projList);
		}
		model.put("dynamicLists", dynamicLists);
		return model; 
	}


	protected String getVisitInfo(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup) {
		if (importDefinition.getPatientOnlyImport()) {
			return "";
		}
		
		StringBuffer visitInfo = new StringBuffer(" Visit:").append(new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(importSetup.getVisit().getVisitDate()));
		visitInfo.append(" ").append(importSetup.getVisit().getVisitType());
		// if visit was just created as part of the import it will not have an id yet
		if (importSetup.getVisit().getId() != null) {
			visitInfo.append(" (").append(importSetup.getVisit().getId()).append(")");
		}
		return visitInfo.toString();
	}
	
	
	/** 
	 * Helper method to saveImportRecord to generate the importLog record content for saving an entity
	 * 
	 * @param entityType
	 * @param importDefinition
	 * @param importSetup
	 * @param instr
	 * @return
	 */
	protected CrmsImportLogMessage getCrmsImportLogMessageInfo(String entityType, CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, Instrument instr) {
		CrmsImportLogMessage info = new CrmsImportLogMessage()
;
		info.setEntityType(entityType);
		if (importSetup.getPatient() != null) {
			info.setPatientId(importSetup.getPatient().getId());
			info.setLastName(importSetup.getPatient().getLastName());
			info.setFirstName(importSetup.getPatient().getFirstName());
			info.setBirthDate(importSetup.getPatient().getBirthDate());
		}
		if (importSetup.getEnrollmentStatus() != null) {
			info.setEnrollStatId(importSetup.getEnrollmentStatus().getId());
			info.setProjName(importSetup.getEnrollmentStatus().getProjName());
		}
		if (!importDefinition.getPatientOnlyImport()) {
			if (importSetup.getVisit() != null) {
				info.setVisitId(importSetup.getVisit().getId());
				info.setVisitDate(importSetup.getVisit().getVisitDate());
				info.setVisitType(importSetup.getVisit().getVisitType());
				if (instr != null) {
					info.setInstrId(instr.getId());
					info.setInstrType(instr.getInstrType());
					info.setInstrVer(instr.getInstrVer());
				}
			}
		}
		return info;
	}
	

	/**
	 * If there is an error processing an import record, then processing should be aborted and any already existing
	 * instruments (that were added to the Hibernate session upon retrieval) that have been modified should be evicted
	 * because otherwise they would be persisted as dirty objects when the request completes and the transaction is
	 * committed.
	 * 
	 * Note that if there is a database error, then this is not necessary, as the transaction will be marked for rollback
	 * and will be rolled back when the request completes and attempts to commit the transaction (also, database exceptions
	 * are caught and re-thrown with embellished error messaging, and the exception bubbles up all the way to the 
	 * browser HTTP Status 500. Could make catch and make that cleaner and handle exception and go to error page, but the
	 * thought was that the importLog view is the error page since it reports all of the errors with the import. but should
	 * go to an error page on rollback as that is better than an HTTP Status 500)
	 * 
	 * @param importDefinition
	 * @param importSetup
	 */
	protected void evictInstruments(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup) {
		if (importSetup.getInstrument() != null && (importSetup.isInstrExisted() || importSetup.isInstrExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument());
		}
		if (importSetup.getInstrument2() != null && (importSetup.isInstr2Existed() || importSetup.isInstr2ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument2());
		}
		if (importSetup.getInstrument3() != null && (importSetup.isInstr3Existed() || importSetup.isInstr3ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument3());
		}
		if (importSetup.getInstrument4() != null && (importSetup.isInstr4Existed() || importSetup.isInstr4ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument4());
		}
		if (importSetup.getInstrument5() != null && (importSetup.isInstr5Existed() || importSetup.isInstr5ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument5());
		}
		if (importSetup.getInstrument6() != null && (importSetup.isInstr6Existed() || importSetup.isInstr6ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument6());
		}
		if (importSetup.getInstrument7() != null && (importSetup.isInstr7Existed() || importSetup.isInstr7ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument7());
		}
		if (importSetup.getInstrument8() != null && (importSetup.isInstr8Existed() || importSetup.isInstr8ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument8());
		}
		if (importSetup.getInstrument9() != null && (importSetup.isInstr9Existed() || importSetup.isInstr9ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument9());
		}
		if (importSetup.getInstrument10() != null && (importSetup.isInstr10Existed() || importSetup.isInstr10ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument10());
		}
		if (importSetup.getInstrument11() != null && (importSetup.isInstr11Existed() || importSetup.isInstr11ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument11());
		}
		if (importSetup.getInstrument12() != null && (importSetup.isInstr12Existed() || importSetup.isInstr12ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument12());
		}
		if (importSetup.getInstrument13() != null && (importSetup.isInstr13Existed() || importSetup.isInstr13ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument13());
		}
		if (importSetup.getInstrument14() != null && (importSetup.isInstr14Existed() || importSetup.isInstr14ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument14());
		}
		if (importSetup.getInstrument15() != null && (importSetup.isInstr15Existed() || importSetup.isInstr15ExistedWithData())) {
			Instrument.MANAGER.evict(importSetup.getInstrument15());
		}
	}

}
