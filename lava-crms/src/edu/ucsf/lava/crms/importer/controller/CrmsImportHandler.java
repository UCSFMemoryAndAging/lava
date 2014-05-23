package edu.ucsf.lava.crms.importer.controller;

import static edu.ucsf.lava.core.importer.model.ImportDefinition.DEFAULT_DATE_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.DEFAULT_TIME_FORMAT;

import java.io.ByteArrayInputStream;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.importer.controller.ImportHandler;
import edu.ucsf.lava.core.importer.model.ImportSetup;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.type.LavaDateUtils;
import edu.ucsf.lava.crms.assessment.InstrumentManager;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.enrollment.EnrollmentManager;
import edu.ucsf.lava.crms.importer.model.CrmsImportDefinition;
import edu.ucsf.lava.crms.importer.model.CrmsImportSetup;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
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
		this.setRequiredFields(StringUtils.addStringToArray(this.getRequiredFields(), "projName"));
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
	
		
	
	//TODO: implement isAuthorized for project authorization (make sure proj auth flag is 
	// set on importSetup. assume it is ok that importSetup is not a persistent object
	
	
	
	
	
//NOTE: more than likely this will be a helper method override rather than doImport because the crms functionality
//is in the  middle of basic import setup and import finish stuff
	protected Event doImport(RequestContext context, Object command, BindingResult errors) throws Exception {
//TODO: should ImportSetup eagerly load ImportDefinition because right now explicitly loading it in core ImportHandler		
		CrmsImportSetup importSetup = (CrmsImportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		Event returnEvent = new Event(this,this.SUCCESS_FLOW_EVENT_ID);
		
		// projName is required if the import is inserting new Patients, Visits and Instruments, so check to make
		// sure it has been specified in the definition mappingFile or in CrmsImportSetup
		// for now projName is a required field
		//?? would need to distinguish INSERT from UPDATE because if UPDATE would not necessarily need projName
		
		// marker indices
		int iPatientPIDN, iPatientFirstName, iPatientLastName, iPatientBirthDate;
		// needed? int iProjName;
		int iVisitDate, iVisitTime, iVisitType, iVisitWith, iVisitLocation, iVisitStatus;
		int iDcDate, iDcStatus;
		
		// lava-core ImportHandler reads the definition mapping file into a columns array (mappingCols) and 
		// properties array (mappingProps)
		if ((returnEvent = super.doImport(context, command, errors)).getId().equals(SUCCESS_FLOW_EVENT_ID)) {
			CrmsImportDefinition importDefinition = (CrmsImportDefinition) importSetup.getImportDefinition();
			String[] mappingCols = importSetup.getMappingCols();
			String[] mappingProps = importSetup.getMappingProps();
		
			/** redundant with below which will assign -1 if not found
			iPatientPIDN = iPatientFirstName = iPatientLastName = iPatientBirthDate = iProjName = iVisitDate = iVisitTime = 
					iVisitType = iVisitWith = iVisitLocation = iVisitStatus = -1;
					*/
			iPatientPIDN = ArrayUtils.indexOf(importSetup.getMappingProps(), "patient.PIDN"); // only for matching existing Patient; will never set this property b/c new Patient ids are generated by db
			iPatientFirstName = ArrayUtils.indexOf(importSetup.getMappingProps(), "patient.firstName");
			iPatientLastName = ArrayUtils.indexOf(importSetup.getMappingProps(), "patient.lastName");
			iPatientBirthDate = ArrayUtils.indexOf(importSetup.getMappingProps(), "patient.birthDate");
			// needed? iProjName = ArrayUtils.indexOf(importSetup.getMappingProps(), "project.projName");
//TODO: check supplied flag for each of these			
			iVisitDate = ArrayUtils.indexOf(importSetup.getMappingProps(), "visit.visitDate");
			iVisitTime = ArrayUtils.indexOf(importSetup.getMappingProps(), "visit.visitTime");
			iVisitType = ArrayUtils.indexOf(importSetup.getMappingProps(), "visit.visitType");
			iVisitWith = ArrayUtils.indexOf(importSetup.getMappingProps(), "visit.visitWith");
			iVisitLocation = ArrayUtils.indexOf(importSetup.getMappingProps(), "visit.visitLocation");
			iVisitStatus = ArrayUtils.indexOf(importSetup.getMappingProps(), "visit.visitStatus");
			iDcDate = ArrayUtils.indexOf(importSetup.getMappingProps(), "instrument.dcDate");
			if (iDcDate == -1) {
				iDcDate = ArrayUtils.indexOf(importSetup.getMappingProps(), importDefinition.getInstrType() + ".dcDate");
			}
			iDcStatus = ArrayUtils.indexOf(importSetup.getMappingProps(), "instrument.dcStatus");
			if (iDcStatus == -1) {
				iDcStatus = ArrayUtils.indexOf(importSetup.getMappingProps(), importDefinition.getInstrType() + ".dcStatus");
			}
		
			// for the specified definition, read the definition mapping file into a Map (or two Maps; one with column names
			// as keys, one with property names as keys)
			// UPDATE: may not need a Map
			// read in array of column names (presumably copy/pasted directly from a sample data file into the mapping file)
			// read in array of property names. store indices of special columns, i.e. patient.PIDN, patient.firstName, patient.lastName, patient.birthDate,
	//		     project.projName, visit.visitDate, etc.  
			// note: indices of column array match indices of property array
			// will get an array of column names from first row of data file - validate that is matches exactly the column array of the mapping file
			// read a line of data into data array	
					
// NOTE: remmeber to review jfesenko data load script		
					
			// iterate thru the data array and property array in lockstep
			// use the special indices to query: Patient exists? get PIDN    Enrollment exists?     Visit exists? get VID
			// log errors, e.g. Patient Does Not Exist (Could be that record does not match a Patient but should), 
	//				Patient Already Exist, 
	//		 	    Record Matches Multiple Patients		
			// if everything checks out:
					// instantiate the instrument TODO: specify instrument type in CrmsImportDefinition and figure out how to get class to instantiate here
					// continue iterating (or start from beginning in case patient, visit data is after some data fields)
					// for each instrument property (property where blank or there is no '.' use reflection to set the property value
					// if blank then use the column name as the property name
				
			LavaFile dataFile = this.getUploadFile(context, ((ComponentCommand)command).getComponents(), errors);
			Scanner fileScanner = new Scanner(new ByteArrayInputStream(dataFile.getContent()), "UTF-8");
			String currentLine;
		
			if (fileScanner.hasNextLine()) {
				currentLine = fileScanner.nextLine().trim();
	//TODO: need file format separator(comma or tab) in ImportDefinition			
				importSetup.setDataCols(currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
					
				// ****
				//TODO: validate mappingColumns and dataColumn arrays
					
				
				
				LavaDaoFilter filter = EntityBase.newFilterInstance();
				int lineNum = 1;
				String[] dataValues;
				SimpleDateFormat formatter;
				String dateOrTimeAsString;
				Integer skipRecordCount = 0;
//TODO: figure out errors / skipped records
//what to put in import log?
//  each record that was skipped? if rerun a script could but entire data file in import log, so maybe not
//  records that were skipped because already exist, but records that were skipped because of problems
//  for records skipped because already exist, can just have a skip count for that (skippedAlreadyExistsCount)

// i.e. if instrument already exists (with data) then no error, just increment a count for already exists,
// but if does not exist and cannot be created for whatever reason then an error
// errors should be things that the user can try to correct either in the mapping definition or
// the data file 
		
// how to handle skipping records when Patient and Enrollment and Visit and Instrument records have been
// added? Hibernate will persist what is dirty but it Hibernote does not know about object, i.e. have not
// called Hibernate add, right? for update just set objects null?
				while (fileScanner.hasNextLine()) {
					// number of lines < MAX_LINES
					//if (++lineNum > MAX_LINES) {
					//	break;
					//}
						
					currentLine = fileScanner.nextLine().trim();
//TODO: prob do not need a dataValues as a property to share with superclass ala mappingProps and mappingCols 				
					dataValues = currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					

					
					// find matching Patient
					Patient p = null;
					filter.clearDaoParams();
					if (iPatientPIDN != -1) {
						String pidnAsString = dataValues[iPatientPIDN];
						Long pidn = null;
						try {
							pidn = Long.valueOf(pidnAsString);
						} catch (NumberFormatException ex) {
							logger.error("PIDN Is not a number="+ pidnAsString);
						}
						filter.addIdDaoEqualityParam(pidn);
						p = (Patient) Patient.MANAGER.getById(pidn);
					}
					else if (iPatientLastName != -1 && iPatientFirstName != -1) {
						filter.addDaoParam(filter.daoEqualityParam("firstName", dataValues[iPatientFirstName]));
						filter.addDaoParam(filter.daoEqualityParam("lastName", dataValues[iPatientLastName]));
						if (iPatientBirthDate != -1) {
							Date birthDate = null;
							dateOrTimeAsString = dataValues[iPatientBirthDate];
							formatter = new SimpleDateFormat(importDefinition.getBirthDateFormat() != null ? importDefinition.getBirthDateFormat() : DEFAULT_DATE_FORMAT);
							formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
							try {
								birthDate = formatter.parse(dateOrTimeAsString);
							} catch (ParseException e) {
								// likely will not be called with leniency applied
								skipRecordCount++;
								logger.error("Patient.birthDate is an invalid Date format in row=" + lineNum);
								continue;
							}
							filter.addDaoParam(filter.daoEqualityParam("birthDate", birthDate));
						}
						p = (Patient) Patient.MANAGER.getOne(filter);
						
						if (p == null) {
							// examine flags to determine course of action:
							// radio buttons:
							// Patient Must Exist
							// Patient Must Not Exist (create Patient record)
							// If Patient Does Not Exist Create Patient Record
							
							// UPDATE: thinking there are 4 courses of action, 2 if Patient exists, 2 if Patient does not exist
							// If Patient Exists:
							//   Skip import record (log)
							//   Import record into existing Patient
							// If Patient does not exist:
							//   Skip import record (log)
							//   Add Patient and import record into new Patient
							// so two variables:
							// patientExistsAction
							// patientNotExistsAction
							
							// UPDATE: Taking the 4 permutations between the 2 variables, one of them is 
							// meaningless, i.e. Skip / Skip, so there are only 3 realistic possibilities
							// after all. 
							// so just have the one variable, but change from Boolean patientMustExist to patientExistsAction
							// (or something like that) if type Short to code label
						}
						
					}
					else {
						// this is not the same as Patient does not exist because do not have fields to check that the
						// Patient does or does not exist
						skipRecordCount++;
						logger.error("Insufficient Patient fields to determine if Patient exists or not");
					}
						
					// determine if Patient is Enrolled in Project
					// importSetup.getProjName();
//TODO: query for Patient Enrollment (if just added Patient above obviously no reason to query)
// query EnrollmentStatus on PIDN and Project. Then check current enrollment status, because if 
// not enrolled that is the same as no enrollment (but different error msg)
					
					// flags (radio buttons)
					// 1. Patient Must be Enrolled
					// 2. Patient Must Not be Enrolled Yet 
					// 3. If Patient is Not Enrolled Enroll Patient
					// UPDATE:
					// If Patient Enrolled in Project:
					//    Skip import record (log)
					//    Import record into Patient / Project
					// If Patient NOT Enrolled in Project
					//    Skip import record (log)
					//    Enroll Patient in Project and import record
					// 2 variables:
					// patientEnrolledAction
					// patientNotEnrolledAction
					// UPDATE: per Patient flags just need a patientEnrolledAction with the 3 possible courses of action
					
						
					// find matching Visit
					Visit v = null;
					// do not have a flag for whether both date and time both match. just assume that whichever is provided should
					// match. if only date can just do a full date comparison, i.e. should not need datepart because the date in
					// the Visit is just the datepart and the date in the data file will just be a date
//UPDATE: actually a TODO: for future improvement would be to handle existing columns that have date and time in same column. not sure
//if will encounter such a thing or not					
					filter.clearDaoParams();
					if (iVisitDate != -1) {
						Date visitDate = null;
						dateOrTimeAsString = dataValues[iVisitDate];
						formatter = new SimpleDateFormat(importDefinition.getVisitDateFormat() != null ? importDefinition.getVisitDateFormat() : DEFAULT_DATE_FORMAT);
						formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
						try {
							visitDate = formatter.parse(dateOrTimeAsString);
						} catch (ParseException e) {
							// likely will not occur with leniency applied
							skipRecordCount++;
							logger.error("Visit.visitDate is an invalid Date format in row=" + lineNum);
							continue;
						}
						filter.addDaoParam(filter.daoEqualityParam("visitDate", visitDate));
						if (iVisitTime != -1) {
							Date visitTimeAsDate = null;
							Time visitTime = null;
							dateOrTimeAsString = dataValues[iVisitTime];
							formatter = new SimpleDateFormat(importDefinition.getVisitTimeFormat() != null ? importDefinition.getVisitTimeFormat() : DEFAULT_TIME_FORMAT);
// ?? setLenient for time value conversion ?? 			
							try{
								visitTimeAsDate = formatter.parse(dateOrTimeAsString);
								visitTime = LavaDateUtils.getTimePart(visitTimeAsDate);
							}catch (ParseException e){
								skipRecordCount++;
								logger.error("Visit.visitTime is an invalid Time format in row=" + lineNum);
								continue;
							}
							filter.addDaoParam(filter.daoEqualityParam("visitTime", visitTime));
						}
						// note: could also use daoDateAndTimeEqualityParam
//TODO: set the Patient !!!! and Project and VisitType(?? yes if in data file or supplied in CrmsImportDefinition)						
						v = (Visit) Visit.MANAGER.getOne(filter);
					}
					
					if (v == null) {
						// examine import definition settings to determine course of action
						
						// If Visit Exists:
						//   Skip import record (log)
						//   Import record into Visit
						// If Visit does not exist:
						//   Skip import record (log)
						//   Add Visit and import record into new Visit
						// 2 variables:
						// visitExistsAction
						// visitNotExistsAction
						// UPDATE: per Patient just need VisitExistsAction to code 3 courses of action:
						// 		Visit Must Exist
						// 		Visit Must NOT Exist
						//		If Visit does NOT Exist, Add Visit
		
					

					}

						
					// instantiate instrument
//TODO: figure out import definition flags, prob:
//  Instrument Must Exist (but do not import over a data entered Instrument) 
//  Instrument Must Not Exist
//  If Instrument does Not Exist Add Instrument					

					Instrument instrument = new Instrument();
					instrument.setInstrType(importDefinition.getInstrType());
					Class instrClazz =instrumentManager.getInstrumentClass(instrument.getInstrTypeEncoded());
					
					// convert DCDate
					Date dcDate = null;
					// if not supplied in data file then it defaults to visit date when adding new instrument
					if (iDcDate != -1) {
						dateOrTimeAsString = dataValues[iDcDate];
						formatter = new SimpleDateFormat(importDefinition.getInstrDcDateFormat() != null ? importDefinition.getInstrDcDateFormat() : DEFAULT_DATE_FORMAT);
						formatter.setLenient(true); // to avoid exceptions; we check later to see if leniency was applied
						try {
							dcDate = formatter.parse(dateOrTimeAsString);
						} catch (ParseException e) {
							// likely will not occur with leniency applied
							skipRecordCount++;
							logger.error("Instrumet.dcDate is an invalid Date format in row=" + lineNum);
							continue;
						}
					}
					
					String dcStatus = null;
					if (importDefinition.getInstrDcStatusSupplied() != null && importDefinition.getInstrDcStatusSupplied()) {
						dcStatus = importDefinition.getInstrDcStatus();
					}
					else {
dcStatus = "Complete";
/*
						if (iDcStatus != -1) {
							dcStatus = dataValues[iDcStatus];
						}
						else {
							skipRecordCount++;
//TOOD: put in messages.properties, put definition name in message
							logger.error("DC Status not supplied in data file per Import Definition");
							continue;
						}
*/						
					}
					
					instrument = Instrument.create(instrClazz, p, v, importSetup.getProjName(), importDefinition.getInstrType(),
							dcDate != null ? dcDate : v.getVisitDate(), dcStatus);

					int arrayIndex = 0;
					for (int i = 0; i < dataValues.length; i++) {
						// set each instrument property on the newly instantiated instrument
						
						// instrument properties -- as opposed to other entities in the import record -- are designated by:
						// blank/empty string in the import definition mapping file property row, meaning that the property
						// name is the same as the import column name in the column now (row 1)
						// or
						// import definition mapping file property has a property name without an entity name, i.e. does not have
						// an entity and '.' character prefixing the property name (if the data import file contains data for 
						// multiple instruments this refers to the "primary" instrument)
						// or
						// import definition mapping file property contains the instrument name (type) followed by the '.'
						// character and the property name
// use StringUtils isEmpty						
						if ((importSetup.getMappingProps()[i] == null || importSetup.getMappingProps()[i].length() == 0) ||
							importSetup.getMappingProps()[i].indexOf(".") == -1 ||
							importSetup.getMappingProps()[i].startsWith(instrument.getInstrTypeEncoded())) 
						{
							String propName = null;
							if (importSetup.getMappingProps()[i] == null || importSetup.getMappingProps()[i].length() == 0) {
								propName = importSetup.getMappingCols()[i].substring(importSetup.getMappingCols()[i].indexOf("."));
							}
							else if (importSetup.getMappingProps()[i].indexOf(".") == -1) {
								propName = importSetup.getMappingProps()[i];
							}
							else if (importSetup.getMappingProps()[i].startsWith(instrument.getInstrTypeEncoded())) {
								propName = importSetup.getMappingProps()[i].substring(importSetup.getMappingProps()[i].indexOf("."));
							}
//TODO: make sure empty string values are set as null. could either use BeanUtils in that case of register
// converter default values as shown in big comment following this method
							// use Apache Commons BeanUtils rather than PropertyUtils as BeanUtils will convert the data value
							// from String to its correct type
							if (propName != null) {
								BeanUtils.setProperty(instrument, propName, dataValues[i]);
							}
						}
						
						instrument.save();
					}
					
					lineNum++;
				}	
			}
			else {
				//TODO: error msg: data file does not contain the first row of column names
				LavaComponentFormAction.createCommandError(errors, "Data file does not contain any rows");
				returnEvent = new Event(this,this.ERROR_FLOW_EVENT_ID);
			}
		}

		return returnEvent;
	}	
	
/**	
	> If you don't want "conversion" then you can use
	> org.apache.commons.beanutils.PropertyUtils.setProperty() method - but 
	> the "value" object has to be the correct type (or null).
	>
	> For PropertyUtils see: http://tinyurl.com/yf9f6wk
	>
	> BeanUtils adds conversion to PropertyUtils  - it tries to convert the 
	> value you specified to the correct type of the the bean's property.
	> These converters have behaviour defined on how to handle "null" values 
	> and operate in two modes:
	>  - throw an exception
	>  - use a default value
	>
	> The default set of converters which are "registered" with BeanUtils 
	> have a default value specified - so for example the converter 
	> registered for Integer types has a default value of zero. This is why 
	> you can't set a "null" value at the moment. If you want null values to 
	> be set then you need to register converter implementations for those 
	> types with a default value of null. So for example you would do something
	like...
	>
	>  ConvertUtils.register(new IntegerConverter(null), Integer.class);
	>  ConvertUtils.register(new DoubleConverter(null), Double.class);
	>
	> (Note: the "null" value in the constructors is the default value)
	>
	> For ConvertUtils see: http://tinyurl.com/ydhq85s For converters see:
	> http://tinyurl.com/yl2pl2q
	>
	
	
	ConvertUtilsBean convertUtilsBean = BeanUtilsBean.getInstance().getConvertUtils();
	convertUtilsBean.register(false, true, -1);
	false do not throw conversion exception (for any conversions)
	true if exception use null as default value
	-1 array types defaulted to null
**/	

	
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
