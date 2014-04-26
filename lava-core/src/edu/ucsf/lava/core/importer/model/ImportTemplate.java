package edu.ucsf.lava.core.importer.model;

import org.springframework.web.multipart.MultipartFile;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class ImportTemplate extends EntityBase {
	
	public static EntityManager MANAGER = new EntityBase.Manager(ImportTemplate.class);

	private String name;
//?? could add: private String category; in case get so many import templates could add a sort/filter
// by category to make it easy to find. but what would categories be? if left this freeform then 
// doubtful there would be orgznied categories so not much help. so maybe put the field in but
// don't use it for now
	private String category;
	
// should not need if a) subclass repo and override createFileId to generate the mappingFilePath and
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
	// see where CBT derives this in CalculateController but also need to look to see where CBT derives this
	// when storing the file in the repository
	
	private MultipartFile mappingFileInput; 
	private LavaFile mappingFile;
	
	
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
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public MultipartFile getMappingFileInput() {
		return mappingFileInput;
	}

	public void setMappingFileInput(MultipartFile mappingFileInput) {
		this.mappingFileInput = mappingFileInput;
	}

	public LavaFile getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(LavaFile mappingFile) {
		this.mappingFile = mappingFile;
	}

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
