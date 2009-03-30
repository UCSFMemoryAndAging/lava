package edu.ucsf.lava.core.dao.hibernate;

import java.io.File;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;

public class LavaConfiguration extends Configuration {

	

	/**
	 * Exclude temp files stored in CVS directories in eclipse during development. 
	 */
	public Configuration addDirectory(File dir) throws MappingException {
		File[] files = dir.listFiles();
		for ( int i = 0; i < files.length ; i++ ) {
			if ( files[i].isDirectory() ) {
				addDirectory( files[i] );
			}
			else if ( files[i].getName().endsWith( ".hbm.xml" )) {
				//added this check here
				if(!files[i].getPath().contains("CVS")){
					addFile( files[i] );
				}
			}
		}
		return this;
	}
}
