package edu.ucsf.lava.crms.importer.controller;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.importer.controller.ImportHandler;
import edu.ucsf.lava.core.importer.model.ImportSetup;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.assessment.InstrumentManager;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.enrollment.EnrollmentManager;
import edu.ucsf.lava.crms.importer.model.CrmsImportDefinition;
import edu.ucsf.lava.crms.importer.model.CrmsImportSetup;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
import edu.ucsf.lava.crms.project.ProjectManager;
import edu.ucsf.lava.crms.scheduling.VisitManager;
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
		CrmsImportSetup crmsImportSetup = (CrmsImportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		CrmsImportDefinition importDefinition = (CrmsImportDefinition) crmsImportSetup.getImportDefinition();
		Event returnEvent = new Event(this,this.SUCCESS_FLOW_EVENT_ID);
		
		// projName is required if the import is inserting new Patients, Visits and Instruments, so check to make
		// sure it has been specified in the definition mappingFile or in CrmsImportSetup
		//TODO: check for projName
		//?? would need to distinguish INSERT from UPDATE because if UPDATE would not necessarily need projName
		
		// marker indices
		int iPatientPIDN, iPatientFirstName, iPatientLastName, iPatientBirthdate;
		int iProjName;
		int iVisitDate, iVisitTime, iVisitType, iVisitWith, iVisitLocation, iVisitStatus;
		
		
		// lava-core ImportHandler reads the definition mapping file into a columns array and properties array
		if ((returnEvent = super.doImport(context, command, errors)).getId().equals(SUCCESS_FLOW_EVENT_ID)) {
		
			/** redundant with below which will assign -1 if not found
			iPatientPIDN = iPatientFirstName = iPatientLastName = iPatientBirthdate = iProjName = iVisitDate = iVisitTime = 
					iVisitType = iVisitWith = iVisitLocation = iVisitStatus = -1;
					*/
			iPatientPIDN = ArrayUtils.indexOf(crmsImportSetup.getMappingProps(), "patient.PIDN"); // only for matching existing Patient; will never set this property b/c new Patient ids are generated by db
			iPatientFirstName = ArrayUtils.indexOf(crmsImportSetup.getMappingProps(), "patient.firstName");
			iPatientLastName = ArrayUtils.indexOf(crmsImportSetup.getMappingProps(), "patient.lastName");
			iPatientBirthdate = ArrayUtils.indexOf(crmsImportSetup.getMappingProps(), "patient.birthDate");
			iProjName = ArrayUtils.indexOf(crmsImportSetup.getMappingProps(), "project.projName");
			iVisitDate = ArrayUtils.indexOf(crmsImportSetup.getMappingProps(), "visit.visitDate");
			iVisitTime = ArrayUtils.indexOf(crmsImportSetup.getMappingProps(), "visit.visitTime");
			iVisitType = ArrayUtils.indexOf(crmsImportSetup.getMappingProps(), "visit.visitType");
			iVisitWith = ArrayUtils.indexOf(crmsImportSetup.getMappingProps(), "visit.visitWith");
			iVisitLocation = ArrayUtils.indexOf(crmsImportSetup.getMappingProps(), "visit.visitLocation");
			iVisitStatus = ArrayUtils.indexOf(crmsImportSetup.getMappingProps(), "visit.visitStatus");
		
		
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
					// for each instrument property (property where there is no '.' use reflection to set the property value
				
			LavaFile dataFile = this.getUploadFile(context, ((ComponentCommand)command).getComponents(), errors);
			Scanner fileScanner = new Scanner(new ByteArrayInputStream(dataFile.getContent()), "UTF-8");
			String currentLine;
			if (fileScanner.hasNextLine()) {
				currentLine = fileScanner.nextLine().trim();
	//TODO: need file format separator(comma or tab) in ImportDefinition			
				crmsImportSetup.setDataCols(currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
					
				//TODO: validate mappingColumns and dataColumn arrays
					
				int lineNum = 0;
				while (fileScanner.hasNextLine()) {
					// number of lines < MAX_LINES
					//if (++lineNum > MAX_LINES) {
					//	break;
					//}
						
					currentLine = fileScanner.nextLine().trim();
					crmsImportSetup.setDataValues(currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
				
					// find matching Patient
						
					// determine Project
						
					// find matching Visit
						
					// instantiate instrument
					Instrument instrument = new Instrument();
					instrument.setInstrType(importDefinition.getInstrType());
					Class instrClazz =instrumentManager.getInstrumentClass(instrument.getInstrTypeEncoded());
						
					int arrayIndex = 0;
					// iterate over dataValues
					// if value is an instrument property (i.e. no '.')
							// use Java reflection or Apache reflection bean utils to set:
							// instr.setProperty(mappingProperties[arrayIndex] = dataValues[arrayIndex]
						
					// ?? save after each instrument added or at end after all instruments added
						
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
