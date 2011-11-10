package edu.ucsf.lava.core.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.file.model.LavaFile;


/**
 * Collection wrapper that handles auto-wiring of spring defined
 * repositories and access to repositories from the FileManger.
 * @author jhesse
 *
 */
public class FileRepositories { 
	private Map<String,FileRepository> repositories = new HashMap<String, FileRepository>();
	private ArrayList<FileRepository> orderedRepositories = new ArrayList();
	
	
	public Map<String, FileRepository> getRepositories() {
		return repositories;
	}

	public List<FileRepository> getOrderedRepositories(){
		return orderedRepositories;
	}

	public void setRepositories(Map<String, FileRepository> repositories) {
		this.repositories = repositories;
	}
	
	
	
	public FileRepository getRepository(LavaFile file){
		for(FileRepository repository: orderedRepositories){
			if(repository.handlesFile(file)){
				return repository;
			}
		}
		//should never get here.  There should always be a default repository that handles all files.
		return null;
	}
	
	public FileRepository getRepository(LavaFileFilter filter){
		return this.getRepository(filter.getTemplate());
	}
	
	
	protected void setOrderedRepositories(){
		ArrayList<BaseFileRepository> ordered = new ArrayList(repositories.values());
		Collections.sort(ordered);
		this.orderedRepositories = new ArrayList<FileRepository>(ordered);
	}

	/**
	 * Adds a repository, taking order into account. 
	 * @param repository
	 */
	public void addRepository(FileRepository repository){
		if(repository!=null){
			this.repositories.put(repository.getId(),repository);
		}
		setOrderedRepositories();
	}
	
}
