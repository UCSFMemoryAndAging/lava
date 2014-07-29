package edu.ucsf.lava.core.file.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.model.EntityBase;


/**
 * Base class for entities that wrap and annotate files (typically stored externally to the database).
 * @author jhesse
 *
 */
public class LavaFile extends EntityBase {
	public static final String DEFAULT_NOFILE_TYPE = "No File";
	public static final String DEFAULT_NOFILE_STATUS = "No File";
	public static final String DEFAULT_UPLOADED_STATUS = "Uploaded";	
	public static LavaFile.Manager MANAGER = new LavaFile.Manager(LavaFile.class);
	
	public LavaFile(){
		//content is not audited by the standard database audit methods. 
		this.addPropertyToAuditIgnoreList("content");
		this.addPropertyToAuditIgnoreList("contentLength");
		this.setFileType(DEFAULT_NOFILE_TYPE);
		this.setFileStatus(DEFAULT_NOFILE_STATUS);
		}
	
	
	
	/**
	 * The name of the file (e.g 'Attachment1.pdf').    
	 * 
	 */
	protected String name;
	
	/**
	 * String representation of the file type.  Typically this would be the file 
	 * extension or mime type. 
	 */
	protected String fileType;
	
	/**
	 * The actual file contents.  Lazily loaded.
	 */
	protected byte[] content;
	
	/**
	 * The type of content stored in the file (e.g. "Neuroreport","Consent Form").
	 */
	protected String contentType;

	/**
	 * Date that the file last changed.
	 */
	protected Date fileStatusDate;
	
	/**
	 * The status at last change.
	 */
	protected String fileStatus;
	
	/**
	 * User who performed last change. 
	 */
	protected String fileStatusBy;
	
	/**
	 * The unique string identifier for the repository that stored the file content
	 */
	protected String repositoryId;
	
	/**
	 * Unique id (name) of the file within the repository. 
	 */
	protected String fileId;
	
	/**
	 * The location of the file content within the repository.  For file system based 
	 * repositories this is typically a fullpath (with filename) relative to the 
	 * repository root path. 
	 */
	protected String location;
	
	/**
	 * A repository specific checksum of the file content. 
	 */
	protected String checksum;
	
	/**
	 * Whether file contents are loaded.
	 */
	protected boolean contentLoaded = false;
	
	
	
	public String getRepositoryId() {
		return repositoryId;
	}
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	
	
	public Date getFileStatusDate() {
		return fileStatusDate;
	}
	public void setFileStatusDate(Date fileStatusDate) {
		this.fileStatusDate = fileStatusDate;
	}
	public String getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	public String getFileStatusBy() {
		return fileStatusBy;
	}
	public void setFileStatusBy(String fileStatusBy) {
		this.fileStatusBy = fileStatusBy;
	}
	public boolean isContentLoaded() {
		return contentLoaded;
	}
	public void setContentLoaded(boolean contentLoaded) {
		this.contentLoaded = contentLoaded;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * returns the file content (loading from repository if necessary).
	 * @return the content or null if no content exists. 
	 */
	public byte[] getContent(){
		if(this.fileId!=null && this.content==null){
			//The content is not loaded yet from the repository
			MANAGER.getFile(this);
			
			}
		return this.content;
	}
	
	/**
	 * Sets the file content, calculating and setting the checksum.
	 * If the checksum value changes then the record will be marked as dirty.
	 *  
	 * @param content
	 */
	public void setContent(byte[] content) {
		String newChecksum = LavaFileUtils.getChecksum(content);
		this.setChecksum(newChecksum);
		this.content = content;
		
	}
	
	public int getContentLength() {
		if(this.getContent()==null){return 0;}
		return this.getContent().length;
	}
	

	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.trackDirty("checksum",checksum);
		this.checksum = checksum;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	

	
	/**
	 * Delete any file in the repository for this deleted FileEntity.
	 */
	@Override
	public void afterDelete() {
		MANAGER.deleteFile(this);
	}
	/**
	 * Save the file contents if they exist, and then resave the 
	 * FileEntity (the file contents save will update properties 
	 * of the FileEntity such as location in the repository). 
	 */
	@Override
	public boolean afterCreate() {
		if(this.getChecksum()!=null){
			MANAGER.saveFile(this);
			return true;
		}
		return false;
	}
	
	public LavaFile saveFile() {
		return MANAGER.saveFile(this);
	}
	
	public LavaFile saveOrUpdateFile() {
		return MANAGER.saveOrUpdateFile(this);
	}
	
	public void deleteFile() {
		MANAGER.deleteFile(this);
	}
	
	/**
	 * Save or delete the file contents if needed, and then resave the 
	 * FileEntity (the file contents save will update properties 
	 * of the FileEntity such as location in the repository). 
	 */
	
	@Override
	public boolean afterUpdate() {
		if(this.isDirty("checksum")){
			if(this.getChecksum()==null){
				MANAGER.deleteFile(this);
			}else{
				MANAGER.saveFile(this);	
			}
			return true;
		}
		return false;
	}

	/**
	 * Utility method to generate detailed error messages in exceptions and 
	 * logging output. 
	 * @return
	 */
	public String toLogString(){
		StringBuffer logOutput = new StringBuffer("id==").append(this.getId())
					.append(";RepositoryId==").append(this.getRepositoryId())
					.append(";FileId==").append(this.getFileId())
					.append(";Location==").append(this.getLocation())
					.append(";ContentType==").append(this.getContentType())
					.append(";Name==").append(this.getName())
					.append(";Checksum==").append(this.getChecksum())
					.append(";ContentLoaded==").append(this.isContentLoaded());
		return logOutput.toString();
					
					
					
					
	}
	
	
	public String getStatusBlock(){
		StringBuffer block = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String statusDateAsText = null;
		if (fileStatusDate != null) {
			statusDateAsText = dateFormat.format(fileStatusDate);
		}
		if(StringUtils.isNotEmpty(statusDateAsText)) { 
			block.append(statusDateAsText);
		}
		if(StringUtils.isNotEmpty(this.fileStatusBy)) { 
				block.append(" (").append(this.fileStatusBy).append(")\n");
		}
		
		if(StringUtils.isNotEmpty(fileStatus)) { 
			block.append("Status: ").append(fileStatus);
		}
		
		return new String(block);
	}


	static public class Manager extends EntityBase.Manager {
		
		
		
		private LavaFileManager fileManager;
		private Class entityClass;
		
	
		
		
		public Manager(Class entityClass){
			super(entityClass);
			fileManager = new LavaFileManager();
		}



		public LavaFile getFile(LavaFile file) throws FileAccessException {
			return fileManager.getFile(file);
		}



		public void deleteFile(LavaFile file) throws FileAccessException {
			fileManager.deleteFile(file);
			
		}


		public LavaFile saveFile(LavaFile file) throws FileAccessException {
			return fileManager.saveFile(file);
		}

		
		public LavaFile saveOrUpdateFile(LavaFile file) throws FileAccessException {
			return fileManager.saveOrUpdateFile(file);
		}
	}
}
