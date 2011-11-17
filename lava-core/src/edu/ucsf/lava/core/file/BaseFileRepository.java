package edu.ucsf.lava.core.file;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.scope.ScopeSessionAttributeHandler;
/**
 * Low level common functionality for file repositories. 
 * 
 * 
 * @author jhesse
 *
 */
public class BaseFileRepository implements Comparable<FileRepository>, FileRepository {

	protected final Log logger = LogFactory.getLog(getClass());
	protected static Long DEFAULT_ORDER=new Long(1000);
	protected FileRepositories repositories;
	/**
	 * precedence of the repository.  Lower order repositories get first opportunity to handle new files.
	 */
	protected Long order;
	/**
	 * unique id of the repository 
	 */
	protected String id;
	/**
	 * Deferred implementation details pattern object.
	 */
	protected FileRepositoryStrategy strategy;
	
	public String getId() {
		return this.id;
	}

	
	public void setId(String id) {
		this.id = id;
	}

	
	public boolean handlesFile(LavaFile file) {
		return this.getStrategy().handlesFile(file);
	}	
	
	public LavaFile getFile(LavaFile file) throws FileAccessException {
		return this.getStrategy().getFile(file);
	}


	public void deleteFile(LavaFile file) throws FileAccessException {
		this.getStrategy().deleteFile(file);
	}


	public LavaFile saveFile(LavaFile file) throws FileAccessException {
		return this.getStrategy().saveFile(file);
	}


	public LavaFile saveOrUpdateFile(LavaFile file) throws FileAccessException {
		return this.getStrategy().saveOrUpdateFile(file);
	}

	public List<LavaFile> findFiles(LavaFileFilter filter)
			throws FileAccessException {
		return this.getStrategy().findFiles(filter);
	}


	public boolean test() {
		return this.getStrategy().testRepository(this.createTestFile());
	}

	protected LavaFile createTestFile(){
		LavaFile test = new LavaFile();
		test.setId((long)0);
		test.setRepositoryId(this.getId());
		test.setContent("This is a test file".getBytes());
		test.setName("testFile.txt");
		return test;
	}
	
	public FileRepositoryStrategy getStrategy() {
		return strategy;
	}


	public void setStrategy(FileRepositoryStrategy strategy) {
		this.strategy = strategy;
	}



	public Long getOrder() {
		if(this.order==null){
			this.order=this.DEFAULT_ORDER;
		}
		return this.order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}
	
	/**
	 * Compares to another repository based on order id. 
	 * @param repository
	 * @return
	 * @throws ClassCastException
	 */
	public int compareTo(FileRepository repository) throws ClassCastException {
	   return order.compareTo(repository.getOrder());    
	}


	public FileRepositories getRepositories() {
		return repositories;
	}


	public void setRepositories(FileRepositories repositories) {
		this.repositories = repositories;
		this.repositories.addRepository(this);
	}
	

	
}
