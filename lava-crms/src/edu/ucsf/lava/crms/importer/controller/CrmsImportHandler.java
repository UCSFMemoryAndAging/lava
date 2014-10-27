package edu.ucsf.lava.crms.importer.controller;

import static edu.ucsf.lava.core.importer.model.ImportDefinition.CSV_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.DEFAULT_DATE_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.DEFAULT_TIME_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.TAB_FORMAT;
import static edu.ucsf.lava.crms.importer.model.CrmsImportDefinition.MUST_EXIST;
import static edu.ucsf.lava.crms.importer.model.CrmsImportDefinition.MUST_NOT_EXIST;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import au.com.bytecode.opencsv.CSVReader;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.file.model.ImportFile;
import edu.ucsf.lava.core.importer.controller.ImportHandler;
import edu.ucsf.lava.core.importer.model.ImportDefinition;
import edu.ucsf.lava.core.importer.model.ImportLog;
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

			importLog.setProjName(importDefinition.getProjName());
			importLog.setNotes(importSetup.getNotes());

			// read data file
// NOTE: remember to review jfesenko data load script		
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
				if (lineNum == 1) {
					continue;
				}
				
				initEntityFlags(importSetup);
				
				importLog.incTotalRecords(); // includes records that cannot be exported due to some error

				// note that indices of data array items in data file match up with indices of column and 
				// property array items in import definition mapping file
				importSetup.setDataValues(nextLine);

				// allow subclasses to custom generate revisedProjName (e.g. append unit/site to projName), which
				// is used everywhere a projName is needed in the import
				// this needs to be called for each record because site could differ for each record
				generateRevisedProjName(importDefinition, importSetup);
	
				// find existing Patient. possibly create new Patient
				if ((handlingEvent = patientExistsHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					importLog.incErrors();
					continue;
				}

				// if no errors, continue processing import record. importSetup patientCreated indicates whether a
				// new Patient record was created or an existing Patient record was found (and given no errors, this 
				// means that all import definition flags were successfully met such that the record can be imported
				// with either a new or existing Patient)
				// (this goes for EnrollmentStatus, Visit and instrument as well)

				//TODO: implement contactInfoExistsHandling and caregiverContactInfoExistsHandler as needed
/** NullPointer on 
		at edu.ucsf.lava.crms.people.model.Caregiver.updateFullName(Caregiver.java:375)
        at edu.ucsf.lava.crms.people.model.Caregiver.updateCalculatedFields(Caregiver.java:354)
				if ((handlingEvent = caregiverExistsHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					importLog.incErrors();
					continue;
				}
 */
				
//RIGHT HERE: call caregiverExistsHandling again for caregiver2 (pass first/last name indices as arguments, get stuff back as attributes				

				// determine if Patient is Enrolled in Project. possibly create new EnrollmentStatus
				if ((handlingEvent = enrollmentStatusExistsHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					importLog.incErrors();
					continue;
				}
						
					
				// find matching Visit. possibly create new Visit
				if ((handlingEvent = visitExistsHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					importLog.incErrors();
					continue;
				}


				// find matching instrument. possibly create new instrument. type of instrument specified in the 
				// importDefinition
				Event instrHandlingEvent = null;
				if ((instrHandlingEvent = instrumentExistsHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					// it is simply enough to check for the existence of the "alreadyExists" attribute, i.e. do not need to check its value
					if (instrHandlingEvent.getAttributes() != null && instrHandlingEvent.getAttributes().get("alreadyExists") != null) {
						importLog.incAlreadyExist();
					}
					else {
						importLog.incErrors();
					}
					continue;
				}

//RIGHT HERE
// create a link from the importLog to the importDefinition so user can quickly see what
// to support this, definition needs to be a subflow of log
				
// X-do pedi attachments (consents) when working in the following with LavaFile stuff				
//   download definition mapping file
//	 download data file				

// X-change mapping file format to 3 rows: row 2 is entity type, row 3 is property name (if
//  both are blank then defaults to 1st instrument and prop name == column name (row 1))				

// pedi new patient history import (data file with all columns, not cut off at 256 cols)
// should only create caregiver and caregiver contactInfo records if data 
// log totals do not reflect caregivers and contactInfo records
// SPDC History Form 2 metadata not populated				
// SPDC History Form 2 only showing when run server in non-debug mode)				
// confirm that data is being loaded correctly (incl. caregiver livesWithPatient, ContactInfo is
//   for caregiver)
// need separate definitions for old and current versions because var names from old
//  need to map to current, e.g. field5 old maps to field6 current, whereas for current
//  field5 maps to field5		
// get rid of import section, default to imports section (make sure regular import fails
//  on SPDC history import				
				
// X-open csv				
	
// OT: Add Patient skip logic on Community Dx should disable following field unless "6 - other"			
				
// pertinent TODOs in code, config, Hibernate mapping, jsp, etc.
	
// make mapping definition name longer (50?)				
// import definition UI cleanup (for now move Project near top, ahead of selection of Import
//  MappingData File since the project refresh removes mapping file selection)				
//  (why does Browse button have _ in it?)				
// importLogContent / crmsImportLogContent format log summary results in a table
// importLog/crmsImportLog needs to get rid of edit
// crmsAllImportLogs needs a Filter
// add creation of entities as importLog info messages (test with WESChecklist)
// ?? create preview mode, at least for development, that does not do anything to db				

// X-call calculate on save (or is it done automatically?)
				
// truncation solution: add import def flag: Truncate to fit field length, then retry, create warning?   or  abort this record, create error, continue w next record				
				
// other majors:
// BASC import
				
// Rankin TODOs:
//   migrate to MAC LAVA
//				
//   implement startDataRow (defaults to 2 for all imports done prior to implementation)
//				
//   match existing Visit on Visit Type if user sets flag to do so. even if not, Visit Type could still be
//   used when creating new Visits (default is false)				
//   (columns and metadata to support already added to db)
//				
//   match existing	Visit on user specified time window, in days, around the visitDate in data file. set
//   to 0 for an exact date match (need info text with this) (0 is the default)				
//   (columns and metadata to support already added to db)
//				
//   expand to work with multiple instruments (crmsImportDefinition will have inputs for up to 10 
//   instruments, and will have to rework instrumentExistsHandling to go thru each specified instrument,
//   and use of instrType,instrVer for generateLocation for data files will just have to use that of
//   the first instrument chosen)
//   (columns and metadata to support already added to db, i.e. 2 thru 10 instrType/instrVer)
//				
// 2.0  expand to work with files in folders for special not-exactly-import use cases:
//      a) for instruments that load individual patient files, e.g. e-prime instruments
//      b) for PDFs that should be attached to an existing instrument
// 2.0: validation, i.e. read property metadata to obtain type, list of valid values
// 
// 3.0 import detail data files, e.g. Freesurfer 5.1 data				
				
				if ((handlingEvent = otherExistsHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					importLog.incErrors();
					continue;
				}
					
				// iterate thru the values of the current import record, setting each value on the property of an entity, as 
				// determined by the importDefinition mapping file
					if ((handlingEvent = setPropertyHandling(context, errors, importDefinition, importSetup, importLog, lineNum)).getId().equals(ERROR_FLOW_EVENT_ID)) {
					importLog.incErrors();
					continue;
				}

				// at this point all values of the import record have been successfully set on entity properties

				//TODO: when enable updating existing instrument data:
				// not calling save on the entity should solve not persisting new records that should be skipped
				// but what if existing entities are modified and then record is to be skipped?
				// Hibernate would implicitly save changes so would have to explicitly rollback. 
				// however, use cases don't support modifying existing Patient/ES/Visit, only an existing Instrument
				// so review how CRUD editing cancel is done and try calling refresh on the modified object 
				// (could fool around with CRUD editing and take out refresh on cancel just to see if changes 
				// are persisted without explicit call to save)				
				saveImportRecord(importDefinition, importSetup);
				
				// update counts
				
				// applies to entire import record
				// it is simply enough to check for the existence of the "update" attribute, i.e. do not need to check its value
				if (instrHandlingEvent.getAttributes() != null && instrHandlingEvent.getAttributes().get("update") != null) {
					importLog.incUpdated();
				}
				else {
					importLog.incImported();
				}
				
				// these counts apply to specific entities within an import record
				updateEntityCounts(importSetup, importLog);
			}
		}

		// at this point, returnEvent success means the success of the overall import. individual records 
		// may have had errors, which are logged as importLog messages and the total error count is incremented
		// returnEvent error means that the import failed as a whole and error msg is put in the command
		// object errors to be displayed
		if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
			importLog.save();
		}
		
		return returnEvent;
	}	
	

	protected Event validateDataFile(BindingResult errors, ImportDefinition importDefinition, ImportSetup importSetup) throws Exception {
		CrmsImportSetup crmsImportSetup = (CrmsImportSetup) importSetup;
		int propIndex;
		if (super.validateDataFile(errors, importDefinition, importSetup).getId().equals(ERROR_FLOW_EVENT_ID)) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		
		// set indices here as this only needs to be done once for the entire data file
		
		// ** the import definition mapping file second row must have entity string and third row must have 
		// property string that match exactly the entity and property name strings below  
	
		// look up the indices of fields in the import definition mapping file properties row that are required 
		// to search for existing entities and/or populate new entities, and record the indices to be used in 
		// processing each import record
		// required fields for creating new Patient/EnrollmentStatus/Visit/instrument which could have the same 
		// uniform value across all records imported from a data file may be specified as part of the import 
		// definition rather then being supplied in the data file. but the data file takes precedent so first 
		// check the data file and set the index if the field has a value in the data file.
		
		// note that the entity and property are on separate lines of the mapping file and thus in separate arrays,
		// so need to check the two arrays in conjunction with each other (could have just had a single property
		// row with entity.property but if there are multiple instruments in the data file with many properties, 
		// easier to edit the mapping file with instrument names across the entity column headers instead of 
		// editing entity.property format for each property)

		crmsImportSetup.setIndexPatientPIDN(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "PIDN", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("patient")) {
				// only for matching existing Patient; will never set this property b/c new Patient ids are generated by db
				crmsImportSetup.setIndexPatientPIDN(propIndex);
				break;
			}
		}
		
		crmsImportSetup.setIndexPatientFirstName(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "firstName", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("patient")) {
				crmsImportSetup.setIndexPatientFirstName(propIndex); 
				break;
			}
		}
		
		crmsImportSetup.setIndexPatientLastName(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "lastName", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("patient")) {
				crmsImportSetup.setIndexPatientLastName(propIndex);
				break;
			}
		}

		crmsImportSetup.setIndexPatientBirthDate(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "birthDate", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("patient")) {
				crmsImportSetup.setIndexPatientBirthDate(propIndex);
				break;
			}
		}
		
		crmsImportSetup.setIndexPatientGender(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "gender", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("patient")) {
				crmsImportSetup.setIndexPatientGender(propIndex);
				break;
			}
		}
		
		crmsImportSetup.setIndexContactInfoAddress(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "address", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("contactInfo")) {
				crmsImportSetup.setIndexContactInfoAddress(propIndex); 
				break;
			}
		}
		
		crmsImportSetup.setIndexContactInfoCity(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "city", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("contactInfo")) {
				crmsImportSetup.setIndexContactInfoCity(propIndex); 
				break;
			}
		}
		
		crmsImportSetup.setIndexContactInfoState(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "state", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("contactInfo")) {
				crmsImportSetup.setIndexContactInfoState(propIndex); 
				break;
			}
		}
		
		crmsImportSetup.setIndexContactInfoZip(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "zip", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("contactInfo")) {
				crmsImportSetup.setIndexContactInfoZip(propIndex); 
				break;
			}
		}
		
		crmsImportSetup.setIndexContactInfoPhone1(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "phone1", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("contactInfo")) {
				crmsImportSetup.setIndexContactInfoPhone1(propIndex); 
				break;
			}
		}
		
		crmsImportSetup.setIndexContactInfoEmail(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "email", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("contactInfo")) {
				crmsImportSetup.setIndexContactInfoEmail(propIndex); 
				break;
			}
		}
		
		crmsImportSetup.setIndexCaregiverFirstName(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "firstName", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("caregiver")) {
				crmsImportSetup.setIndexCaregiverFirstName(propIndex); 
				break;
			}
		}
//RIGHT HERE: caregiver2 first/last name indices (then can removed from SpdcHistoryFormImportSetup		
		crmsImportSetup.setIndexCaregiverLastName(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "lastName", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("caregiver")) {
				crmsImportSetup.setIndexCaregiverLastName(propIndex); 
				break;
			}
		}

		crmsImportSetup.setIndexEsStatusDate(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "date", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("enrollmentStatus")) {
				crmsImportSetup.setIndexEsStatusDate(propIndex); 
				break;
			}
		}

		crmsImportSetup.setIndexEsStatus(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "status", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("enrollmentStatus")) {
				crmsImportSetup.setIndexEsStatus(propIndex); 
				break;
			}
		}

		crmsImportSetup.setIndexVisitDate(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "visitDate", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("visit")) {
				crmsImportSetup.setIndexVisitDate(propIndex); 
				break;
			}
		}

		crmsImportSetup.setIndexVisitTime(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "visitTime", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("visit")) {
				crmsImportSetup.setIndexVisitTime(propIndex); 
				break;
			}
		}

		crmsImportSetup.setIndexVisitType(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "visitType", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("visit")) {
				crmsImportSetup.setIndexVisitType(propIndex); 
				break;
			}
		}

		crmsImportSetup.setIndexVisitWith(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "visitWith", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("visit")) {
				crmsImportSetup.setIndexVisitWith(propIndex); 
				break;
			}
		}

		crmsImportSetup.setIndexVisitLoc(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "visitLoc", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("visit")) {
				crmsImportSetup.setIndexVisitLoc(propIndex); 
				break;
			}
		}

		crmsImportSetup.setIndexVisitStatus(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "visitStatus", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("visit")) {
				crmsImportSetup.setIndexVisitStatus(propIndex); 
				break;
			}
		}

		// note if data collection date is not in the data file then the visit date is used to populate it in new instruments
		crmsImportSetup.setIndexInstrDcDate(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "dcDate", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("instrument")) {
				crmsImportSetup.setIndexInstrDcDate(propIndex); 
				break;
			}
		}
		
		crmsImportSetup.setIndexInstrDcStatus(-1);
		propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), "dcStatus", propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase("instrument")) {
				crmsImportSetup.setIndexInstrDcStatus(propIndex); 
				break;
			}
		}
	
		setOtherIndices((CrmsImportDefinition)importDefinition, crmsImportSetup);

		//TODO: move these checks to the CrmsImportDefinitionHandler
		// error on entire import if either no PIDN or no FirstName/LastName 			
		if (crmsImportSetup.getIndexPatientPIDN() == -1 && 
				(crmsImportSetup.getIndexPatientFirstName() == -1 || crmsImportSetup.getIndexPatientLastName() == -1)) {
			LavaComponentFormAction.createCommandError(errors, "Insufficient Patient properties (must have PIDN or FirstName Lastname) in Import Definition mapping file");
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		// error on entire import if no visitDate 			
		else if (crmsImportSetup.getIndexVisitDate() == -1) {
			LavaComponentFormAction.createCommandError(errors, "Import Definition mapping file must have 'visit.visitDate' property to link import record to a date");
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}

		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}
	

	/**
	 * The created and existed flags are used by the logic involved in processing each import
	 * record, as well as for updating the importLog counts.
	 */
	protected void initEntityFlags(CrmsImportSetup importSetup) {
		importSetup.setPatientCreated(false);
		importSetup.setPatientExisted(false);
		importSetup.setContactInfoCreated(false);
		importSetup.setContactInfoExisted(false);
		importSetup.setCaregiverCreated(false);
		importSetup.setCaregiverExisted(false);
		importSetup.setCaregiver2Created(false);
		importSetup.setCaregiver2Existed(false);
		importSetup.setEnrollmentStatusCreated(false);
		importSetup.setEnrollmentStatusExisted(false);
		importSetup.setVisitCreated(false);
		importSetup.setVisitExisted(false);
		importSetup.setInstrCreated(false);
		importSetup.setInstrExisted(false);
		importSetup.setInstrExistedWithData(false);
	}

	
	
	/**
	 * Subclasses should override to generate custom projName
	 * 
	 * @return
	 */
	protected void generateRevisedProjName(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup) {
		importSetup.setRevisedProjName(importDefinition.getProjName());
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
		SimpleDateFormat formatter;
		String dateOrTimeAsString;
		Date birthDate = null;

		// search for existing patient
		Patient p = null;

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
		else { 
			// birthDate is optional for search as it is often not part of data files
			if (importSetup.getIndexPatientBirthDate() != -1) {
				dateOrTimeAsString = importSetup.getDataValues()[importSetup.getIndexPatientBirthDate()];
				formatter = new SimpleDateFormat(importDefinition.getDateFormat() != null ? importDefinition.getDateFormat() : DEFAULT_DATE_FORMAT);
				formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
				try {
					birthDate = formatter.parse(dateOrTimeAsString);
				} catch (ParseException e) {
					// likely will not be called with leniency applied
					importLog.addErrorMessage(lineNum, "Patient.birthDate is an invalid Date format, Date:" + dateOrTimeAsString);
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
				
				// because if date format is yyyy for year part, the parser will allow any date into the future, even 5 digit dates, so 
				// have to do range checking to catch bad date errors
				java.util.Calendar birthDateCalendar = java.util.Calendar.getInstance();
				birthDateCalendar.setTime(birthDate);
				int birthDateYear = birthDateCalendar.get(java.util.Calendar.YEAR);
				java.util.Calendar nowCalendar = java.util.Calendar.getInstance();
				int nowYear = nowCalendar.get(java.util.Calendar.YEAR);
				if (birthDateYear < (nowYear - 100) || birthDateYear > nowYear) {
					importLog.addErrorMessage(lineNum, "Patient DOB has an invalid Year. DOB:" + dateOrTimeAsString);
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}

				filter.addDaoParam(filter.daoEqualityParam("birthDate", birthDate));
			}

			// have already validated that firstName and lastName are present in the mapping definition file if PIDN is not
			setPatientNameMatchFilter(filter, importSetup);
			
			try {
				p = (Patient) Patient.MANAGER.getOne(filter);
			}
			// this should never happen. if re-running import of a data file, should just be one 
			catch (IncorrectResultSizeDataAccessException ex) {
				importLog.addErrorMessage(lineNum, "Duplicate Patient records for patient firstName:" + importSetup.getDataValues()[importSetup.getIndexPatientFirstName()] +
						" lastName:" + importSetup.getDataValues()[importSetup.getIndexPatientLastName()]); 
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}
			
		if (p == null) {
			if (importDefinition.getPatientExistRule().equals(MUST_EXIST)) {
				if (importSetup.getIndexPatientPIDN() != -1) {
					importLog.addErrorMessage(lineNum, "Patient does not exist violating MUST_NOT_EXIST flag. PIDN:" + importSetup.getDataValues()[importSetup.getIndexPatientPIDN()]); 
				}
				else {
					importLog.addErrorMessage(lineNum, "Patient does not exist violating MUST_NOT_EXIST flag.Line:" +  
						" First Name:" + importSetup.getDataValues()[importSetup.getIndexPatientFirstName()] + " Last Name:" + importSetup.getDataValues()[importSetup.getIndexPatientLastName()]);
				}
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}else {
				// for either MUST_NOT_EXIST or MAY_OR_MAY_NOT_EXIST instantiate the Patient
				
				if (importSetup.getIndexPatientFirstName() == -1 || !StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexPatientFirstName()])) {
					importLog.addErrorMessage(lineNum, "Cannot create Patient. First Name field (patient.firstName) is missing or has no value");
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
				if (importSetup.getIndexPatientLastName() == -1 || !StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexPatientLastName()])) {
					importLog.addErrorMessage(lineNum, "Cannot create Patient. Last Name field (patient.lastName) is missing or has no value");
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
				if (importSetup.getIndexPatientBirthDate() == -1 || !StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexPatientBirthDate()])) {
					importLog.addErrorMessage(lineNum, "Cannot create Patient. Date of Birth field (patient.birthDate) is missing or has no value");
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
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
				p.setLastName(importSetup.getDataValues()[importSetup.getIndexPatientLastName()]);
				p.updateCalculatedFields(); // so can use full name in log messages
				// if the birthDate conversion was not done yet, i.e. PIDN was supplied such that a PIDN match was done (and failed)
				if (birthDate == null) {
//look at making this data conversion into a small helper method									
					dateOrTimeAsString = importSetup.getDataValues()[importSetup.getIndexPatientBirthDate()];
					formatter = new SimpleDateFormat(importDefinition.getDateFormat() != null ? importDefinition.getDateFormat() : DEFAULT_DATE_FORMAT);
					formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
					try {
						birthDate = formatter.parse(dateOrTimeAsString);
					} catch (ParseException e) {
						// likely will not be called with leniency applied
						importLog.addErrorMessage(lineNum, "Patient.birthDate is an invalid Date format, Date:" + dateOrTimeAsString);
						return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
					}
				}
				p.setBirthDate(birthDate);
				// at this point have already validated that patient.gender exists in import file and has a value
				p.setGender(importSetup.getDataValues()[importSetup.getIndexPatientGender()].toLowerCase().startsWith("m") || 
						importSetup.getDataValues()[importSetup.getIndexPatientGender()].equals("1") ? (byte)1 : (byte)2);
				p.setCreated(new Date());
				p.setCreatedBy("IMPORT (" + CoreSessionUtils.getCurrentUser(sessionManager, request).getLogin() + ")");
				
				importSetup.setPatientCreated(true);
				importSetup.setPatient(p);
				
				// Contact Info
				// the assumption is that ContactInfo is only imported as part of a new Patient import, so if any ContactInfo 
				// properties are mapped they are only processed here after a new Patient has been created. if any ContactInfo
				// properties have values then create a new ContactInfo record
				// address, address2, city, state, zip, preferredContactMethod, phone1, phone1Type, phone2, phone2Type, phone3, phone3Type, email
				if ((importSetup.getIndexContactInfoAddress() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoAddress()])) || 
						(importSetup.getIndexContactInfoCity() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoCity()])) ||  
						(importSetup.getIndexContactInfoState() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoState()])) || 
						(importSetup.getIndexContactInfoZip() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoZip()])) ||
						(importSetup.getIndexContactInfoPhone1() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoPhone1()])) || 
						(importSetup.getIndexContactInfoEmail() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexContactInfoEmail()]))) {
					ContactInfo contactInfo = createContactInfo(importDefinition, importSetup);
					contactInfo.setPatient(p);
					contactInfo.setIsCaregiver(false);
					contactInfo.setActive((short)1);
					if (importSetup.getIndexContactInfoAddress() != -1) {
						contactInfo.setAddress(importSetup.getDataValues()[importSetup.getIndexContactInfoAddress()]);
					}
					if (importSetup.getIndexContactInfoCity() != -1) {
						contactInfo.setCity(importSetup.getDataValues()[importSetup.getIndexContactInfoCity()]);
					}
					if (importSetup.getIndexContactInfoState() != -1) {
						contactInfo.setState(importSetup.getDataValues()[importSetup.getIndexContactInfoState()]);
					}
					if (importSetup.getIndexContactInfoZip() != -1) {
						contactInfo.setZip(importSetup.getDataValues()[importSetup.getIndexContactInfoZip()]);
					}
					if (importSetup.getIndexContactInfoPhone1() != -1) {
						contactInfo.setPhone1(importSetup.getDataValues()[importSetup.getIndexContactInfoPhone1()]);
					}
					if (importSetup.getIndexContactInfoEmail() != -1) {
						contactInfo.setEmail(importSetup.getDataValues()[importSetup.getIndexContactInfoEmail()]);
					}
					importSetup.setContactInfoCreated(true);
					importSetup.setContactInfo(contactInfo);
				}
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
	 * caregiverExistsHandling
	 * 
	 * Determine whether Caregiver exists or not and create if it does not. Can can assume an exists setting
	 * of MAY_OR_MAY_NOT_EXIST where nothing is updated if the Caregiver already exists.
	 * 
	 * @param context
	 * @param errors
	 * @param importDefinition
	 * @param importSetup
	 * @param lineNum
	 * @return SUCCESS Event if no import errors with current record; ERROR EVENT if errors
	 */
	protected Event caregiverExistsHandling(RequestContext context, BindingResult errors, 
			CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, CrmsImportLog importLog,
			int lineNum) {
//RIGHT HERE: move SpdcHistoryFormImportHandler caregiverExistsHandling here which takes indexFirst/LastName
//and makes this reusable for importing multiple caregivers and should be able to get rid of the SpdcHistoryFormImportHandler method	
// keep attributes to return values as that is what makes it reusable as well		
		LavaDaoFilter filter = EntityBase.newFilterInstance();

		// search for existing Caregiver
		Caregiver caregiver = null;

		// only search if the Patient already exists because if Patient did not exist then Caregiver does not exist
		if (!importSetup.isPatientCreated()) { 
			filter.clearDaoParams();
			filter.setAlias("patient", "patient");
			filter.addDaoParam(filter.daoEqualityParam("patient.id", importSetup.getPatient().getId()));
			filter.addDaoParam(filter.daoEqualityParam("firstName", importSetup.getDataValues()[importSetup.getIndexCaregiverFirstName()]));
			filter.addDaoParam(filter.daoEqualityParam("lastName", importSetup.getDataValues()[importSetup.getIndexCaregiverLastName()]));
			try {
				caregiver = (Caregiver) Caregiver.MANAGER.getOne(filter);
			}
			// this should never happen. if re-running import of a data file, should just be one 
			catch (IncorrectResultSizeDataAccessException ex) {
				importLog.addErrorMessage(lineNum, "Duplicate Caregiver records for patient " + importSetup.getPatient().getFullNameWithId() + 
						" and Caregiver firstName:" + importSetup.getDataValues()[importSetup.getIndexPatientFirstName()] 
						+ " lastName:" + importSetup.getDataValues()[importSetup.getIndexPatientLastName()]);
				return new Event(this, "error"); // to abort processing this import record
			}
		}
		
		if (caregiver == null) {
			// required fields firstName, lastName, and other non-required fields will be assigned in setProperty as they
			// are encountered in the import record
//RIGHT HERE: do not create Caregiver records if all fields are empty
			caregiver = createCaregiver(importDefinition, importSetup);
			caregiver.setPatient(importSetup.getPatient());
			caregiver.setActive((short)1);
			importSetup.setCaregiverCreated(true);
			importSetup.setCaregiver(caregiver);
			
			// if a new Caregiver is created, automatically create a new ContactInfo record for that Caregiver. any
			// caregiverContactInfo properties are set in setPropertyHandling
//RIGHT HERE: decide whether to check for existence of data in at least one of address1/city/state/zip/phone1/email			
			ContactInfo contactInfo = createContactInfo(importDefinition, importSetup);
			contactInfo.setPatient(importSetup.getPatient());
			contactInfo.setIsCaregiver(true);
			contactInfo.setActive((short)1);
//RIGHT HERE: need to add this to CrmsImportSetup			
			importSetup.setCaregiverContactInfo(contactInfo);
		}
		else { 
			importSetup.setCaregiverExisted(true);
			importSetup.setCaregiver(caregiver);
		}
	
		return new Event(this, SUCCESS_FLOW_EVENT_ID);
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
		SimpleDateFormat formatter;
		String dateOrTimeAsString;

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
			try {
				es = (EnrollmentStatus) EnrollmentStatus.MANAGER.getOne(filter);
			}
			// this should never happen. if re-running import of a data file, should just be one 
			catch (IncorrectResultSizeDataAccessException ex) {
				importLog.addErrorMessage(lineNum, "Duplicate EnrollmentStatus records for patient " + importSetup.getPatient().getFullNameWithId() + 
						" and project " + importSetup.getRevisedProjName());
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}
		
		if (es == null) {
			if (importDefinition.getEsExistRule().equals(MUST_EXIST)) {
				importLog.addErrorMessage(lineNum, "Patient Enrollment does not exist for Project:" + importSetup.getRevisedProjName() + 
						" violating MUST_NOT_EXIST flag. Patient:" + importSetup.getPatient().getFullNameRevWithId());
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}else {
				// for either MUST_NOT_EXIST or MAY_OR_MAY_NOT_EXIST instantiate the Enrollment Status

				// enrollmentStatus date will typically not be supplied in the data file, so default to visitDate if not
				Date esDate = null;
				dateOrTimeAsString = importSetup.getIndexEsStatusDate() != -1 ? importSetup.getDataValues()[importSetup.getIndexEsStatusDate()] : importSetup.getDataValues()[importSetup.getIndexVisitDate()];
				formatter = new SimpleDateFormat(importDefinition.getDateFormat() != null ? importDefinition.getDateFormat() : DEFAULT_DATE_FORMAT);
				formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
				try {
					esDate = formatter.parse(dateOrTimeAsString);
				} catch (ParseException e) {
					// likely will not be called with leniency applied
					importLog.addErrorMessage(lineNum, "Enrollment Status Date or Visit Date is an invalid Date format. Date:" + dateOrTimeAsString);
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
				// because if date format is yyyy for year part, the parser will allow any date into the future, even 5 digit dates, so 
				// have to do range checking to catch bad date errors
				java.util.Calendar esDateCalendar = java.util.Calendar.getInstance();
				esDateCalendar.setTime(esDate);
				int esDateYear = esDateCalendar.get(java.util.Calendar.YEAR);
				java.util.Calendar nowCalendar = java.util.Calendar.getInstance();
				int nowYear = nowCalendar.get(java.util.Calendar.YEAR);
				// allow for dates 5 years into the future
				if (esDateYear < (nowYear - 100) || esDateYear > (nowYear + 5)) {
					importLog.addErrorMessage(lineNum, "Enrollment Status Date or Visit Date has an invalid Year. Date:" + dateOrTimeAsString);
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
					
				String esStatus = importSetup.getIndexEsStatus() != -1 ? importSetup.getDataValues()[importSetup.getIndexEsStatus()] : importDefinition.getEsStatus();
				if (!StringUtils.hasText(esStatus)) {
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
				es.setStatus(esStatus, esDate);
				es.updateLatestStatusValues();

				importSetup.setEnrollmentStatusCreated(true);
				importSetup.setEnrollmentStatus(es);
			}
		}
		else { // EnrollmentStatus already exists
			importSetup.setEnrollmentStatusExisted(true);
			importSetup.setEnrollmentStatus(es);
			if (importDefinition.getPatientExistRule().equals(MUST_NOT_EXIST)) {
				// typically with this flag the first time the import is run the Enrollment Status will not 
				// exist so it will be created above. if there were some import data errors they would be fixed
				// and the script re-imported, at which point there will be these errors for all Enrollment 
				// Statuses that were created on first run, so record will be skipped and Enrollment Status
				// will correctly not be created again
						
				// note: this differs from MAY_OR_MAY_NOT_EXIST where import of the record will continue if
				// the Enrollment Status exists (as well as if Enrollment Status does not exist as it will be 
				// created above)
				importLog.addErrorMessage(lineNum, "Enrollment Status already exists, violates Import Definition MUST_NOT_EXIST setting. Patient:" + importSetup.getPatient().getFullNameWithId());
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
		Visit v = null;
		Date visitDate = null;
		Time visitTime = null;
		String visitType = null;

		// visitDate is required for both matching Visit and as a required field when creating new Visit
		dateOrTimeAsString = importSetup.getDataValues()[importSetup.getIndexVisitDate()];
		formatter = new SimpleDateFormat(importDefinition.getDateFormat() != null ? importDefinition.getDateFormat() : DEFAULT_DATE_FORMAT);
		formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
		try {
			visitDate = formatter.parse(dateOrTimeAsString);
		} catch (ParseException e) {
			// likely will not occur with leniency applied
			importLog.addErrorMessage(lineNum, "Visit.visitDate is an invalid Date format. Date:" + dateOrTimeAsString);
			return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
		}

		// because if date format is yyyy for year part, the parser will allow any date into the future, even 5 digit dates, so 
		// have to do range checking to catch bad date errors
		java.util.Calendar visitDateCalendar = java.util.Calendar.getInstance();
		visitDateCalendar.setTime(visitDate);
		int visitDateYear = visitDateCalendar.get(java.util.Calendar.YEAR);
		java.util.Calendar nowCalendar = java.util.Calendar.getInstance();
		int nowYear = nowCalendar.get(java.util.Calendar.YEAR);
		// allow for dates 5 years into the future
		if (visitDateYear < (nowYear - 100) || visitDateYear > (nowYear + 5)) {
			importLog.addErrorMessage(lineNum, "Visit.visitDate has an invalid Year. Date:" + dateOrTimeAsString);
			return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
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
		
		
		// visitType is optional for the search; it typically is not in generated data files, and if the import
		// is such that new Visits will not be created then it need not be specified in the definition.
		
		// note that it is not required in the definition, so that if new visits are created and it is not defined
		// then the new visits will not have a visitType. this is odd but this is because it may not be accurate
		// to assign the same visitType to every visit created within the same import
		visitType = importSetup.getIndexVisitType() != -1 ? importSetup.getDataValues()[importSetup.getIndexVisitType()] : importDefinition.getVisitType();
		if (visitType != null) {
			if (StringUtils.hasText(visitType)) {
				filter.addDaoParam(filter.daoEqualityParam("visitType", visitType));
			}
		}
		
		// if enrollmentStatus was just created (whether patient just created or patient already existed) then 
		// know that the Visit could not exist yet. otherwise, check to see if Visit exists or not.
		if (!importSetup.isEnrollmentStatusCreated()) {
			filter.clearDaoParams();
			filter.setAlias("patient", "patient");
			filter.addDaoParam(filter.daoEqualityParam("patient.id", importSetup.getPatient().getId()));
			filter.addDaoParam(filter.daoEqualityParam("projName", importSetup.getRevisedProjName()));
			if (StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexVisitDate()])) {

				// do not have a flag for whether both date and time must match. just assume that whichever is provided should
				// match. if only date can just do a full date comparison, i.e. should not need datepart because the date in
				// the Visit is just the datepart and the date in the data file will just be a date

				// currently do not handle existing columns that have date and time in same column. not sure if this will
				// be encountered
				
				filter.addDaoParam(filter.daoEqualityParam("visitDate", visitDate));
				if (importSetup.getIndexVisitTime() != -1 && StringUtils.hasText(importSetup.getDataValues()[importSetup.getIndexVisitTime()])) {
					filter.addDaoParam(filter.daoEqualityParam("visitTime", visitTime));
				}
				// note: could also use daoDateAndTimeEqualityParam
				filter.addDaoParam(filter.daoNot(filter.daoEqualityParam("visitStatus", "Cancelled")));
				
				try {
					v = (Visit) Visit.MANAGER.getOne(filter);
				}
				// this should never happen. if re-running import of a data file, should just be one 
				catch (IncorrectResultSizeDataAccessException ex) {
					importLog.addErrorMessage(lineNum, "Duplicate Visit records for Patient:" + importSetup.getPatient().getFullNameWithId() + 
							" and Visit Date:" + dateOrTimeAsString);
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
			}
			else {
				// this is not the same as Visit does not exist because do not have fields to check that the
				// Visit does or does not exist
				importLog.addErrorMessage(lineNum, "Cannot determine if Visit exists or not. Column:" + importSetup.getDataCols()[importSetup.getIndexVisitDate()] + " has no data");
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}
		
		if (v == null) {
			if (importDefinition.getVisitExistRule().equals(MUST_EXIST)) {
				importLog.addErrorMessage(lineNum, "Visit does not exist for Patient:" + importSetup.getPatient().getFullNameRevWithId() + " Project:" + importSetup.getRevisedProjName() + " violating MUST_NOT_EXIST flag");
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}else {
				// for either MUST_NOT_EXIST or MAY_OR_MAY_NOT_EXIST instantiate the Enrollment Status

				// get fields that were not already obtained for querying for existing Visit. note that these fields are
				// required in LAVA, but they are not required in the import definition, because it may be that a single
				// visitType / visitLocation / visitWith / visitStatus does not apply to all visits that are created
				// within a single import file
				
				String visitLoc = importSetup.getIndexVisitLoc() != -1 ? importSetup.getDataValues()[importSetup.getIndexVisitLoc()] : importDefinition.getVisitLoc();
				/** in case decide to require visitLoc
				if (!StringUtils.hasText(visitLoc)) {
					if (importSetup.getIndexVisitLoc() != -1) {
						importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit Location field in data file (column:" + importSetup.getDataCols()[importSetup.getIndexVisitLoc()] + ") has no value");
					}
					else {
						importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit Location field not supplied in data file and no value specified in definition");									
					}
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
				**/
				
				String visitWith = importSetup.getIndexVisitWith() != -1 ? importSetup.getDataValues()[importSetup.getIndexVisitWith()] : importDefinition.getVisitWith();
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
				/** in case decide to require visitStatus
				if (!StringUtils.hasText(visitStatus)) {
					if (importSetup.getIndexVisitStatus() != -1) {
						importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit Status field in data file (column:" + importSetup.getDataCols()[importSetup.getIndexVisitStatus()] + ") has no value");
					}
					else {
						importLog.addErrorMessage(lineNum, "Cannot create Visit. Visit Status field not supplied in data file and no value specified in definition");									
					}
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
				**/

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
			if (importDefinition.getPatientExistRule().equals(MUST_NOT_EXIST)) {
				// typically with this flag the first time the import is run the Enrollment Status will not 
				// exist so it will be created above. if there were some import data errors they would be fixed
				// and the script re-imported, at which point there will be these errors for all Enrollment 
				// Statuses that were created on first run, so record will be skipped and Enrollment Status
				// will correctly not be created again
					
				// note: this differs from MAY_OR_MAY_NOT_EXIST where import of the record will continue if
				// the Enrollment Status exists (as well as if Enrollment Status does not exist as it will be 
				// created above)
				importLog.addErrorMessage(lineNum, "Visit already exists, violates Import Definition MUST_NOT_EXIST setting. Patient:" + importSetup.getPatient().getFullNameWithId());
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}

		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}
		

	
	/**
	 * insrtumentExistsHandler
	 * 
	 * Determine whether instrument exists or not and act accordingly based on the importDefinition
	 * settings. 
	 * 
	 * @param context
	 * @param errors
	 * @param importDefinition
	 * @param importSetup
	 * @param lineNum
	 * @return SUCCESS Event if no import errors with current record; ERROR EVENT if errors
	 */
	protected Event instrumentExistsHandling(RequestContext context, BindingResult errors, 
			CrmsImportDefinition importDefinition, CrmsImportSetup importSetup,  CrmsImportLog importLog,
			int lineNum) {
		
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EntityBase.newFilterInstance();
		SimpleDateFormat formatter, msgDateFormatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

		String dateOrTimeAsString;
		Map<String,Object> eventAttrMap = new HashMap<String,Object>();
		AttributeMap attributeMap = new LocalAttributeMap(eventAttrMap);
		
		// search for existing instrument
		Instrument instr = null;

		Class instrClazz =instrumentManager.getInstrumentClass(
				Instrument.getInstrTypeEncoded(importDefinition.getInstrType(), importDefinition.getInstrVer()));

		// determine dcDate for search
		// convert DCDate
		Date dcDate = null;
		// if not supplied in data file then it defaults to visit date when adding new instrument
		if (importSetup.getIndexInstrDcDate() != -1) {
			dateOrTimeAsString = importSetup.getDataValues()[importSetup.getIndexInstrDcDate()];
			formatter = new SimpleDateFormat(importDefinition.getDateFormat() != null ? importDefinition.getDateFormat() : DEFAULT_DATE_FORMAT);
			formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
			try {
				dcDate = formatter.parse(dateOrTimeAsString);
			} catch (ParseException e) {
				// likely will not occur with leniency applied
				importLog.addErrorMessage(lineNum, "Instrumet.dcDate is an invalid Date format. Date:" + dateOrTimeAsString);
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
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
			try {
				instr = (Instrument) Instrument.MANAGER.getOne(instrClazz, filter);
			}
			catch (IncorrectResultSizeDataAccessException ex) {
				importLog.addErrorMessage(lineNum, "Duplicate " + importDefinition.getInstrType() + " records for Patient:" + importSetup.getPatient().getFullNameWithId() + 
						" and Visit Date:" + msgDateFormatter.format(importSetup.getVisit().getVisitDate()));
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}
		
		if (instr == null) {
			if (importDefinition.getInstrExistRule().equals(MUST_EXIST)) {
				importLog.addErrorMessage(lineNum, "Instrument does not exist. Patient:" + importSetup.getPatient().getFullNameWithId() 
						+ " Visit Date:" + msgDateFormatter.format(importSetup.getVisit().getVisitDate()));
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}else {
				// instrument does not exist so instantiate
				
				String dcStatus = importSetup.getIndexInstrDcStatus() != -1 ? importSetup.getDataValues()[importSetup.getIndexInstrDcStatus()] : importDefinition.getInstrDcStatus();
				if (!StringUtils.hasText(dcStatus)) {
					if (importSetup.getIndexInstrDcStatus() != -1) {
						importLog.addErrorMessage(lineNum, "Cannot create Instrument. Instrument DC Status field in data file (column:" + importSetup.getDataCols()[importSetup.getIndexInstrDcStatus()] + ") has no value");
					}
					else {
						importLog.addErrorMessage(lineNum, "Cannot create Instrument. Instrument DC Status field not supplied in data file and no value specified in definition");									
					}
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}

				try {
					instr = createInstrument(importDefinition, importSetup, instrClazz, dcDate, dcStatus);
					instr.setDcBy(importSetup.getVisit().getVisitWith());
					instr.setDeBy("IMPORTED");
					instr.setDeDate(new Date());
					// if import record does not have any errors then instrument will be saved so set data entry
					// status complete; if there are errors import record will be skipped and instrument will not 
					// be saved, so it is ok if setting data entry status "Complete" here
					instr.setDeStatus("Complete");
					instr.setDeNotes("Data Imported by:" + CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request).getShortUserNameRev());				}
				catch (Exception ex) {
					importLog.addErrorMessage(lineNum, "Error instantiating instrument. Patient:" + importSetup.getPatient().getFullNameWithId() 
							+ " and Visit Date:" + msgDateFormatter.format(importSetup.getVisit().getVisitDate()));
					return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
				}
			}
			importSetup.setInstrCreated(true);
			importSetup.setInstrument(instr);
		}
		else { // instrument already exists
			importSetup.setInstrument(instr);
			if (importDefinition.getInstrExistRule().equals(MUST_NOT_EXIST)) {
				importLog.addErrorMessage(lineNum, "Instrument already exists violating Import Definition MUST_NOT_EXIST setting. Patient:" + importSetup.getPatient().getFullNameWithId()
						+ " and Visit Date:" + msgDateFormatter.format(importSetup.getVisit().getVisitDate()));
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
				// can be exist without being data entered.

				// note that this is also different than flags for updating Patient,EnrollmentStatus and Visit because 
				// those would not affect whether the instrument data is imported or not. so while the instrument 
				// flag can be handled at the level of the import record in terms of skipping the whole import 
				// record or not, the Patient,EnrollmentStatus and,Visit update flags would not dictate this, and so 
				// their allow..Update flags would be enforced at the individual property setting level
				
				// using deDate to determine if instrument has been data entered. not looking for a specific deStatus
				// such as 'Complete' since data entry could have any number of deStatus values
				if (instr.getDeDate() == null) {
					importSetup.setInstrExisted(true);
				}
				else {
					if (importDefinition.getAllowInstrUpdate()) {
						importSetup.setInstrExistedWithData(true);
						// set an attribute on the return event so the caller can distinguish between an error and the
						// record already exists
						eventAttrMap.put("update", Boolean.TRUE);
					}
					else {
						// this is not an error in the sense that the there was a problem; rather the ERROR Event is 
						// returned so the current record will not be imported since data already exists, and it is likely
						// that a data file with this record was already imported. 
						importSetup.setInstrExistedWithData(true);
						importLog.addDebugMessage(lineNum, "Instrument exists and has already been data entered. Cannot overwrite per Import Definition. Patient:" + 
							importSetup.getPatient().getFullNameWithId() + " and Visit Date:" + msgDateFormatter.format(importSetup.getVisit().getVisitDate()));

						// set an attribute on the return event so the caller can distinguish between an error and the
						// record already exists
						eventAttrMap.put("alreadyExists", Boolean.TRUE);
						return new Event(this, ERROR_FLOW_EVENT_ID, attributeMap); // to abort processing this import record
					}
				}
			}
		}
		
		return new Event(this, SUCCESS_FLOW_EVENT_ID, attributeMap);
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
	protected Instrument createInstrument(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, 
			Class instrClazz, Date dcDate, String dcStatus) {
		return Instrument.create(instrClazz, importSetup.getPatient(), importSetup.getVisit(), importSetup.getRevisedProjName(), 
				importDefinition.getInstrType(), dcDate, dcStatus);
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
		String definitionColName, definitionPropName, definitionEntityName;
		for (int i = 0; i < importSetup.getDataValues().length; i++) {
			returnEvent = new Event(this, SUCCESS_FLOW_EVENT_ID);
			
			definitionColName = importSetup.getMappingCols()[i];
			definitionEntityName = importSetup.getMappingEntities()[i];
			definitionPropName = importSetup.getMappingProps()[i];
			
logger.info("i="+i+" colName="+definitionColName+ " entityName="+definitionEntityName+" propName="+definitionPropName);

			// skip fields with column name or property name prefixed by XX						
			if (definitionColName.startsWith("XX") || 
				(definitionEntityName != null && definitionEntityName.startsWith("XX")) ||
				(definitionPropName != null && definitionPropName.startsWith("XX"))) { 
				// do nothing
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

			// give this first instrument first shot at determining if the property applies to it, to support
			// this shorthand
			else if (!StringUtils.hasText(definitionEntityName)) {
				// missing entity means shorthand to use the first instrument specified in importDefinition
				
				// for all instruments, if the property name is left blank in the mapping file that means that the
				// column name is the same as the property name, so there is no need to redundantly specify the property 
				// name as well.
				String propName = null;
				if (!StringUtils.hasText(definitionPropName)) {
					propName = definitionColName;
				}
				else {
					propName = definitionPropName;
				}
				// set property on the first instrument specified in importDefinition
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument(), propName, i, lineNum);
			}
			else if (definitionEntityName.equalsIgnoreCase(importSetup.getInstrument().getInstrType())) {

				// for all instruments, if the property name is left blank in the mapping file that means that the
				// column name is the same as the property name, so there is no need to redundantly specify the property 
				// name as well.
				String propName = null;
				if (!StringUtils.hasText(definitionPropName)) {
					propName = definitionColName;
				}
				else {
					propName = definitionPropName;
				}
				
				//TODO: handle case where entity is an instrType to support multiple instrument data imports
				//mappingPropName.startsWith(..each of the importDefinition instrType (10 of them)). this could also match
				//the default instrument if mapping file puts in entity name (instrType) for it.
				//if startsWith matches, given that instrumentExistsHandler will have iterated across all importDefinition
				//instrTypes instantiating each, use the corresponding instantiated instrument (importSetup will have
				//properties instrument, instrument2, instrument3, etc. that correspond with importDefinition 
				//instrType, instrTyp2, instrType3, etc. (and don't forget about instrVer)
				
				//could go with either having mappingEntities be instrType or instrTypeEncoded. if the latter,
				//instrExistsHandling will instantiate the Instrument from which instrTypeEncoded can be obtained
				//(or it can be obtained passing importDefinition instrType to the static getInstrTypeEncoded method
				//instrTypeEncoded would be a bit off for the users. instrType just have to be careful with spaces, etc.
				//that there is an exact match
				returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getInstrument(), propName, i, lineNum);
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
						returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getPatient(), definitionPropName, i, lineNum);
					}
				}
			}
			//ContactInfo properties
			else if (definitionEntityName.equalsIgnoreCase("contactInfo")) {
				if (importSetup.isContactInfoCreated()) {
					// don't need to set properties already set when ContactInfo was created
					if (!definitionPropName.equalsIgnoreCase("address") && !definitionPropName.equalsIgnoreCase("city") &&
							!definitionPropName.equalsIgnoreCase("state") && !definitionPropName.equalsIgnoreCase("zip") &&
							!definitionPropName.equalsIgnoreCase("phone1") && !definitionPropName.equalsIgnoreCase("email")) {
						returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getContactInfo(), definitionPropName, i, lineNum);
					}
				}
			}
			//Caregiver properties
			else if (definitionEntityName.equalsIgnoreCase("caregiver")) {
				if (importSetup.isCaregiverCreated()) {
					// don't need to set properties already set when Caregiver was created
					if (!definitionPropName.equalsIgnoreCase("firstName") && !definitionPropName.equalsIgnoreCase("lastName")) {
						returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getCaregiver(), definitionPropName, i, lineNum);
					}
				}
			}
//RIGHT HERE: add handling for "caregiverContactInfo" (change SpdcHistoryForm mapping to historyCaregiverContactInfo)			
//RIGHT HERE: add handling for "caregiver2"	(change SpdcHistoryForm mapping to historyCaregiver2)		
//RIGHT HERE: add handling for "caregiver2ContactInfo" (change SpdcHistoryForm mapping to historyCaregiver2ContactInfo)			
			//EnrollmentStatus properties
			else if (definitionEntityName.equalsIgnoreCase("enrollmentStatus")) {
				if (importSetup.isEnrollmentStatusCreated()) {
					returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getEnrollmentStatus(), definitionPropName, i, lineNum);
				}
			}
			//Visit properties
			else if (definitionEntityName.equalsIgnoreCase("visit")) {
				if (importSetup.isVisitCreated()) {
					// don't need to set properties already set when Visit was created
					if (!definitionPropName.equalsIgnoreCase("visitDate") && !definitionPropName.equalsIgnoreCase("visitType") && !definitionPropName.equalsIgnoreCase("visitLocation")
							&& !definitionPropName.equalsIgnoreCase("visitWith") && !definitionPropName.equalsIgnoreCase("visitStatus")) {
						returnEvent = this.setProperty(importDefinition, importSetup, importLog, importSetup.getVisit(), definitionPropName, i, lineNum);
					}
				}
			}
			else {
				// allow subclasses to set entity properties for any custom behavior
				returnEvent = setOtherPropertyHandling(importDefinition, importSetup, importLog, i, lineNum);
			}
			
			// abort import of the current record if there was an error setting the imported value on the property
			if (returnEvent.getId().equals(ERROR_FLOW_EVENT_ID)) {
				return new Event(this, ERROR_FLOW_EVENT_ID); // to abort processing this import record
			}
		}
		
		return returnEvent;
	}
	

	/**
	 * Subclasses override this is setting a property involves any custom behavior.
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @param entity
	 * @param propName
	 * @param i
	 * @throws Exception
	 */
	protected Event setProperty(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup,	CrmsImportLog importLog, LavaEntity entity, String propName, int i, int lineNum) throws Exception {
		// default BeanUtils converter will set empty values to a default which is not null, so change the
		// behavior so property is set to null
		// note: could just skip null values since property value is already null on new instrument, but if
		// this code were to be used for and import update then would need to set null values
		
		//TODO: consult the metadata for each property
		// if the property is a string/text value then check the length of the data vs. the max string length. 
		//   add a flag to import definition about how user wants this handled: either do not import record and 
		//   create error, or truncate the string to the max length and import it and create warning
		// validate data value by obtaining metadata for the entity.property, i.e. list of valid values
		
		if (!StringUtils.hasText(importSetup.getDataValues()[i])) {
			// false -use a default value instead of throwing a conversion exception (for any conversions)
			// true - use null for the default value
			// -1 - array types defaulted to null
			this.getConvertUtilsBean().register(false, true, -1);
		}
		try {
			// use Apache Commons BeanUtils rather than PropertyUtils as BeanUtils will convert the data value
			// from String to its correct type
			
logger.info("setting prop name="+propName+" to value="+importSetup.getDataValues()[i]);			
			BeanUtils.setProperty(entity, propName, importSetup.getDataValues()[i]);
		}
		catch (InvocationTargetException ex) {
			importLog.addErrorMessage(lineNum, "[InvocationTargetException] Error setting property: Property:" + propName + " Value:" + importSetup.getDataValues()[i] +  
					" Patient:" + importSetup.getPatient().getFullNameWithId() + "Visit Date:" + importSetup.getVisit().getVisitDate());
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		catch (IllegalAccessException ex) {
			importLog.addErrorMessage(lineNum, "[IllegalAccessException] Error setting property: Property:" + propName + " Value:" + importSetup.getDataValues()[i] +  
					" Patient:" + importSetup.getPatient().getFullNameWithId() + "Visit Date:" + importSetup.getVisit().getVisitDate());
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		if (!StringUtils.hasText(importSetup.getDataValues()[i])) {
			// resume throwing exceptions (second and third arguments ignored in this case)
			this.setupBeanUtilConverters();
		}								
		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}

	/**
	 * Subclasses override this to set a value on a property of an entity other than Patient,
	 * Visit, EnrollmentStatus or the instrument. 
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @param i
	 * @throws Exception
	 */
	protected Event setOtherPropertyHandling(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup, 
			CrmsImportLog importLog, int i, int lineNum) throws Exception {
		// do nothing
		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}


	/**
	 * saveImportRecord
	 * 
	 * Persist the import record to the applicable entities. 
	 * 
	 * Subclasses should override if they involve additional entities, such as ContactInfo or
	 * Caregiver, or for other custom handling. Make sure they call this superclass method. 
	 */
	protected void saveImportRecord(CrmsImportDefinition importDefinition, CrmsImportSetup importSetup) {
		// for new entities must explicitly save since not associated with a Hibernate session. for
		// updates, entity was retrieved and thus attached to a session and Hibernate dirty checking should
		// implicitly update the entity

		//TODO: test that update persists. may need to explicitly save in all cases		
		
		if (importSetup.isPatientCreated()) {
			importSetup.getPatient().save();
		}
		if (importSetup.isContactInfoCreated()) {
			importSetup.getContactInfo().save();
		}
		if (importSetup.isCaregiverCreated()) {
			importSetup.getCaregiver().save();
		}
		if (importSetup.isEnrollmentStatusCreated()) {
			importSetup.getEnrollmentStatus().save();
		}
		if (importSetup.isVisitCreated()) {
			importSetup.getVisit().save();
		}
		// allowInstrUpdate is used to determine whether an already existing instrument which has already
		// been data entered can be updated
		try {
			if (importSetup.isInstrCreated() || importDefinition.getAllowInstrUpdate()) {
				importSetup.getInstrument().save();
			}
		}
		catch (Exception e) {
			int i = 0;
			// e = InvalidDataAccessResourceException
			// e.cause = DataException
			// e.cause.cause = MysqlDataTruncation
			//  could potentially parse violating property out of cause.message e.g. "Data too long for column 'sp56_list' at row 1"
			
			// if data truncation exception iterate thru all properties, querying metadata and if property
			// style is: "suggest", "string" or "text", get the max length from the metadata and iterate thru
			// the properties comparing value against max length:
			// if user set flag to truncate and warn: truncate data that exceeds max length and create warning and try again
			// if user set flag to error out on the current record, stop processing this patient record and create error
			
			
			
			
		}
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
		// note: do not have existed for Caregiver ContactInfo since don't check for that, i.e. this ContactInfo
		// is added a) when a new Caregiver is created, and b) if it exists in the data file
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
		// both instrument existed flags will be set but "WithData" takes precedence
		if (importSetup.isInstrExistedWithData()) {
			importLog.incExistingInstrumentsWithData();
		}
		else if (importSetup.isInstrExisted()) {
			importLog.incExistingInstruments();
		}
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

	
	
}
