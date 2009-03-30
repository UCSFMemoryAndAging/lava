package edu.ucsf.lava.core.manager;

import java.io.Serializable;

public class AppInfo implements Serializable {

    private String version;
    private String databaseName;

    public AppInfo() {}

    public String getVersion() {
	return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDatabaseName() {
	return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

}    
