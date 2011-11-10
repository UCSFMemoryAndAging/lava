package edu.ucsf.lava.core.file;

import edu.ucsf.lava.core.file.model.LavaFile;

/**
 * Class used to pass criteria for finding files to a file repository
 * @author jhesse
 *
 */
public class LavaFileFilter extends Object{
	

	protected LavaFile template;

	
	public LavaFileFilter(){
		super();
	}
	public LavaFileFilter(LavaFile template){
		this.template = template;
	}
	
	public LavaFile getTemplate() {
		return template;
	}

	public void setTemplate(LavaFile template) {
		this.template = template;
	}
	
	
	

}
