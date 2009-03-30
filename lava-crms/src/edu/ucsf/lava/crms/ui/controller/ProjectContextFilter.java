package edu.ucsf.lava.crms.ui.controller;

import java.io.Serializable;

import edu.ucsf.lava.crms.project.model.Project;

public class ProjectContextFilter implements Serializable {
	
	private Project project;
	private String projectName;
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
		if (this.project != null)
		{		
			this.projectName = this.project.getName();
		}
		else
		{
			this.projectName = "";
		}
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	

}
