package edu.ucsf.lava.core.importer.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;

import edu.ucsf.lava.core.auth.AuthDaoUtils;
import edu.ucsf.lava.core.auth.AuthUserPermissionCache;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;

public class ImportLog extends EntityBase {
	private String templateName;
	private AuthUser user;
	private Timestamp importTimestamp;
	private String dataFilePath;
	private Long recordsImported;
	private Long recordsNotImported;
	private List<String> msgs; // warnings, errors
	
	public ImportLog(){
		super();
	}
	
	
}
