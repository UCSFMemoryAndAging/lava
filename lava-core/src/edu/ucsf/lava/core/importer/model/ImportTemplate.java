package edu.ucsf.lava.core.importer.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class ImportTemplate extends EntityBase {
	
	public static EntityManager MANAGER = new EntityBase.Manager(ImportTemplate.class);

	private String name;
// should not need if a) subclass repo and override createFieldId to generate the mappingFilePath and
// b) persist the mappingFilePath
//	private String mappingFilename;
	// this is the fileIdPropertyName of the repository configuration, used to store where the file is located
	// in the repository
	// this field is not persisted. the value is computed where the path is the PIDN and instrID and the
	// value of repository getFileId which 
	// LavaFileDao getFileId gets the repository and calls repository getFileId with entity as argument, but in
	// the case of CBT the fileId is derived from PIDN and InstrID so CbtFileDao overrides getFileId and
	// simply returns entity.getFilename, so our derived FileManager class may need to do this as well. not sure
	// because why doesn't PdfTrackingFileDao do this as well even though it also stores PDFs under a PIDN
	// and possibly other directory. Do these persist this field ???
	// see where CBT derives this in CalculateController but also need to look to see where CBT dervies this
	// when storing the file in the repository
	
	private String mappingFilePath; 
	
// create the core-import.xml version of crms-import.xml (but no module, section defaults)	
// create the CrmsImportTemplate (should this be the CoreImportTemplate? not per Auth naming
	// optional Project property which would be useful for filtering lists of both ImportLog and ImportTemplate
	// would then have to figure out whether could authorize via Project if present and not if not present
// create CrmsImportLog 
// create all the handlers specified in core-import.xml (base class handlers) and crms-import.xml (subclassed)
// GitHub PUSH of feature-import_tool branch to remote	
// get Add Template working
//   get Upload Template Mapping CSV file working, i.e storing: file repo Strategy and Repo subclasses
// get Importing working:
	// choose a template
	// choose a data file
	// on import:
		// read the template mapping file into a Map (data file column keys to entity.property values)
		// read the data file column headers, iterating thru the template Mapping file to find each
		//  column in the mapping file and store the CSV index (instead of current technique of having
		//  a var for each column index, could have Map fpr each entity mapping propety to the CSV index
		//  and since the # entity's and enitty types will be variable across all templates could 
		//  dynamically create a Map of these entity Maps
	
		// upon reading a row of data, process Patient Map first then Caregiver, Contact Info and other
		// Patient module entities, then Visit, then assessments
		// read row of data into CSV array and then use the indexes from the Entity map to get the
		// value for a given property and use reflection to set the value on the property
	
		// TODO will be a validation step to query the metadata for an entity.property to validate
	 	// against a range/dropdown, etc.
	
	
	// CrmsInsertImportTemplate
	
	// if Patients template will need to indicate which field or fields to use to look for already existing Patient
	//   also for Patient need initial Project for EnrollmentStatus
	// radio buttons / dropdown
	//   if Patient does not exist, add Patient (for new patients, e.g. new patient history form)
	//   if Patient does not exist, skip import for this record (for assessment imports)
	// also  
	//   if Patient exists, log warning (new patients but patient might exist for some reason. Patient is
	//      obviously not added since they exist, but other records in row are imported)
	
	// if Visits template will need to know name of date field to use for VisitDate and Project / Visit Type / Visit With
	//   and VisiDate and optionally Visit Type to looking for already existing Visit
	// need to check if Patient is Enrolled in the Visit's Project
	
	// if Assessment need instrument name and dcDate and check for already existing instrument (and what if instrument
	// exists but has no data entered --- that would be considered a 1.0 insert rather than a 2.0 update)
	
	// CrmsUpdateImportTemplate
	// not sure if there will be a template mapping file, in which case maybe template file should not exist at core
	// level and put into CrmsInsertImportTemplate, but feeling is that it should be left in core and just
	// not used here (for this envisioning an export which uses reflection to essentially export the template,
	// but with data, and then import is using that same export file so there is no template mapping file
	// needed, i.e. the data file is also the template mapping file
	
	
	private String notes;

	public ImportTemplate(){
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMappingFilePath() {
		return mappingFilePath;
	}

	public void setMappingFilePath(String mappingFilePath) {
		this.mappingFilePath = mappingFilePath;
	}

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
