package edu.ucsf.lava.crms.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.crms.project.model.Project;

public class ProjectManager extends LavaManager {
	public static String PROJECT_MANAGER_NAME="projectManager";
	private HashMap <String,Project>projectsByName;
	
	public ProjectManager() {
		super(PROJECT_MANAGER_NAME);
	}

	protected void initializeProjectList(){
		if (projectsByName==null){
			reloadProjects();
		}
	}
	public void reloadProjects(){
		HashMap <String,Project> projectsMap = new HashMap<String,Project>(); 
		
		
		List<Project> projects = Project.MANAGER.get();
		
		for (Project p : projects){
			projectsMap.put(p.getName(),p);
		}
		setProjectsByName(projectsMap);


	}
		
	// note: the name is the projUnitDesc name of the project, since name is just an alias for projUnitDesc
	public Project getProject(String name) {
		initializeProjectList();
		return (Project) getProjectsByName().get(name);
	}

	
	
	public Map <String,Project> getProjects() {
		initializeProjectList();
		return getProjectsByName();
		
	}

	public synchronized HashMap<String, Project> getProjectsByName() {
		initializeProjectList();
		return projectsByName;
	}

	public synchronized void setProjectsByName(HashMap<String, Project> projectsByName) {
		this.projectsByName = projectsByName;
	}
	

	
}
