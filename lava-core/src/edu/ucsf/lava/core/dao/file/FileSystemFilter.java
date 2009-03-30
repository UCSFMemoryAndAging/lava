package edu.ucsf.lava.core.dao.file;

import java.io.File;

public class FileSystemFilter implements java.io.FileFilter {

	private Boolean onlyDir;
	private String regExpr;
	
	public FileSystemFilter(String regExpr, Boolean onlyDir) {
		this.onlyDir = onlyDir;
		this.regExpr = regExpr;
	}
	public FileSystemFilter(String regExpr) {
		this.onlyDir = false;
		this.regExpr = regExpr;
	}
	
	public boolean accept(File f) {
        if (f.getName().matches(regExpr) && (!onlyDir || (onlyDir && f.isDirectory()))) {
        	return true;
        }
        return false;
	}
	
}
