package edu.ucsf.lava.core.dao.file;
/*
 *  Basic model object to wrap a file object 
 */
public class LavaFile {
	
	private String repositoryId;
	private String id;
	private String contentType;
	private String name;
	private byte[] content;
	
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public int getContentLength() {
		if(content == null){return 0;}
		return content.length;
	}
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRepositoryId() {
		return repositoryId;
	}
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}
	
	
	
	
	
	
}
