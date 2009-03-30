package edu.ucsf.lava.core.controller;

import java.io.Serializable;
import java.util.HashMap;

//A command wrapper for components based controllers
public class ComponentCommand implements Serializable{

	private HashMap components = new HashMap();

	/**
	 * @return Returns the components.
	 */
	public HashMap getComponents() {
		return components;
	}

	/**
	 * @param components The components to set.
	 */
	public void setComponents(HashMap components) {
		this.components = components;
	}


}
