package edu.ucsf.lava.crms.assessment.model;

import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

import edu.ucsf.lava.crms.model.CrmsEntity;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;

public class Instrument extends CrmsEntity {
	
	public static EntityManager MANAGER = new EntityBase.Manager(Instrument.class);
	
	private Visit visit;
	private String projName;
	private Patient patient;
	private String instrType;
	private String instrVer;
	private Date dcDate;
	private String dcBy;
	private String dcStatus;
	private String dcNotes;
	private String researchStatus;
	private String qualityIssue;
	private String qualityIssue2;
	private String qualityIssue3;
	private String qualityNotes;
	private Date deDate;
	private String deBy;
	private String deStatus;
	private String deNotes;
	private Date dvDate;
	private String dvBy;
	private String dvStatus;
	private String dvNotes;
	private Boolean latestFlag; //set by trigger
	private Short ageAtDC; //set by trigger
	private Map notes = new HashMap();


	
	// InstrumentSummary table
	private String summary;
	
	// UploadedFiles table (store just the most recent uploaded file for this instrument)
	private String filename;
	private String fileContents;

	
	/* this constructor is used to instantiate a new Instrument object as a placeholder for add until
	 * the type of instrument is known. ALL instrument subclasses must implement this constructor
	 * to appease Hibernate (otherwise, org.hibernate.InstantiationException: No default constructor for entity),
	 * but typically do nothing inside of the constructor (and if so, Java implicitly
	 * calls the superclass constructor with no arguments, i.e. super() is inserted)
	 * 
	 * the next constructor is used to instantiate an instrument-specific instrument to be persisted via 
	 * the service/DAO layers 
	 */
	public Instrument() {
		super();
		this.setAuditEntityType("Instrument");
		this.setProjectAuth(true);
		}

	/* constructor used for add instrument, which sets values for all properties which have not null
	   constraints in the database (and do not have database defaults)
	   
	   i.e. ALL instruments must implement this constructor, even if they do not have any specific initialization
	   beyond this base class version, because Java Reflection API is used to find this constructor method
	   on instrument specific classes (to facilitate for a generic instrument creation technique)
	   
	   in this constructor for each instrument, assign any field defaults. if the instrument has multiple
	   versions, the instrVer field should be assigned the latest version.
	   
	   instrumentHandler initializeNewCommandInstance sets patient based on the current Patient in context,
	   and sets visit, dcDate, dcStatus and projName based on the Visit to which the instrument is being
	   added. 
	   instrType is selected by the user and set on the instance to persist in doSaveAdd */
	public Instrument(Patient p, Visit v, String projName, String instrType, Date dcDate, String dcStatus) {
		this();
		setPatient(p);
		setVisit(v);
		setProjName(projName);
		setInstrType(instrType);
		setDcDate(dcDate);
		setDcStatus(dcStatus);
	}

	
	public static Instrument create(Class clazz, Patient p, Visit v, String projName, String instrType, Date dcDate, String dcStatus) {
		Instrument instrObj = null;
		// since Patient and Visit could be Hibernate proxies, do not pass p.getClass(), v.getClass() as it may not find the method.
		// also, runtime type of dcDate may be java.sql.Timestamp if it was loaded via Hibernate, but will be dcDate if constructed
		// via create, so to make both work, use Date.class, not dcDate.getClass
		// so just use static .class for all 
		try {
			instrObj = (Instrument) clazz.getConstructor(new Class[]{Patient.class, Visit.class, String.class, String.class, Date.class, String.class})
				.newInstance(new Object[]{p,v,projName,instrType,dcDate,dcStatus});
		}
		catch (Exception ex) {
			//TODO: convert to an application (unchecked) exception and rethrow
		}
		return instrObj;
	}

	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.visit, this.patient,this.notes};
	}

	//Too expensive to initialize for instruments....
	//TODO:check this assumption that the expense is too great
	public boolean initializeAssocationsForObjectLists(String method) {
		return false;
	}


	public String getInstrTypeEncoded(Boolean includeVersion) {
		return getEntityNameEncoded(includeVersion);
	}
	
	public String getInstrTypeEncoded(){
		return this.getEntityNameEncoded(true);
	}
	
		
    public Visit getVisit() {return this.visit;}
	public void setVisit(Visit visit) {this.visit = visit;}
	
	public String getProjName() {return projName;}
	public void setProjName (String projName) {this.projName = projName;}
	
	public Patient getPatient() {return this.patient;}
	public void setPatient(Patient patient) {this.patient = patient;}
	
	public String getInstrType() {return instrType;}
	public void setInstrType(String instrType) {
		this.instrType = instrType;
		this.setEntityName(instrType);
	}
	
	public String getInstrVer() {return instrVer;}
	
	public void setInstrVer(String instrVer) {
		this.instrVer = instrVer;
	
		//handle bug where instrVer was set to -8 by old stored procedures. 
		if(instrVer !=null && !instrVer.equals(DATA_CODES_UNUSED.toString())){
			setEntityVersion(instrVer);
		}
	}
	
	

	public Date getDcDate() {return dcDate;}
	public void setDcDate(Date dcDate) {this.dcDate = dcDate;}
	
	public String getDcBy() {return dcBy;}
	public void setDcBy(String dcBy) {this.dcBy = dcBy;}
	
	public String getDcStatus() {return dcStatus;}
	public void setDcStatus(String dcStatus) {this.dcStatus = dcStatus;}
	
	public String getDcNotes() {return dcNotes;}
	public void setDcNotes(String dcNotes) {this.dcNotes = dcNotes;}
	
	public String getResearchStatus() {return researchStatus;}
	public void setResearchStatus(String researchStatus) {this.researchStatus = researchStatus;}

	public String getQualityIssue() {return qualityIssue;}
	public void setQualityIssue(String qualityIssue) {this.qualityIssue = qualityIssue;}

	public String getQualityIssue2() {return qualityIssue2;}
	public void setQualityIssue2(String qualityIssue2) {this.qualityIssue2 = qualityIssue2;}

	public String getQualityIssue3() {return qualityIssue3;}
	public void setQualityIssue3(String qualityIssue3) {this.qualityIssue3 = qualityIssue3;}
	
	public String getQualityNotes() {return qualityNotes;}
	public void setQualityNotes(String qualityNotes) {this.qualityNotes = qualityNotes;}
	
	public Date getDeDate() {return deDate;}
	public void setDeDate(Date deDate) {this.deDate = deDate;}
	
	public String getDeBy() {return deBy;}
	public void setDeBy(String deBy) {this.deBy = deBy;}
	
	public String getDeStatus() {return deStatus;}
	public void setDeStatus(String deStatus) {this.deStatus = deStatus;}
	
	public String getDeNotes() {return deNotes;}
	public void setDeNotes(String deNotes) {this.deNotes = deNotes;}
	
	public Date getDvDate() {return dvDate;}
	public void setDvDate(Date dvDate) {this.dvDate = dvDate;}
	
	public String getDvBy() {return dvBy;}
	public void setDvBy(String dvBy) {this.dvBy = dvBy;}
	
	public String getDvStatus() {return dvStatus;}
	public void setDvStatus(String dvStatus) {this.dvStatus = dvStatus;}
	
	public String getDvNotes() {return dvNotes;}
	public void setDvNotes(String dvNotes) {this.dvNotes = dvNotes;}
	
	public String getCollectionStatusBlock(){
		StringBuffer block = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String dcDateAsText = null;
		if (dcDate != null) {
			dcDateAsText = dateFormat.format(dcDate);
		}
		if(StringUtils.isNotEmpty(this.dcStatus)) { block.append(this.dcStatus).append("\n");}
		if(StringUtils.isNotEmpty(this.dcBy)) { block.append(this.dcBy).append("\n");}
		if(StringUtils.isNotEmpty(dcDateAsText)) { block.append(dcDateAsText);}
		
		
		return new String(block);
	}
	public String getEntryStatusBlock(){
		StringBuffer block = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String deDateAsText = null;
		if (deDate != null) {
			deDateAsText = dateFormat.format(deDate);
		}
		
		if(StringUtils.isNotEmpty(this.deStatus)) { block.append(this.deStatus).append("\n");}
		if(StringUtils.isNotEmpty(this.deBy)) { block.append(this.deBy).append("\n");}
		if(StringUtils.isNotEmpty(deDateAsText)) { block.append(deDateAsText);}
		
		return new String(block);
	}
	public String getVerifyStatusBlock(){
		StringBuffer block = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String dvDateAsText = null;
		if (dvDate != null) {
			dvDateAsText = dateFormat.format(dvDate);
		}
		
		if(StringUtils.isNotEmpty(this.dvStatus)) { block.append(this.dvStatus).append("\n");}
		if(StringUtils.isNotEmpty(this.dvBy)) { block.append(this.dvBy).append("\n");}
		if(StringUtils.isNotEmpty(dvDateAsText)) { block.append(dvDateAsText);}
		
		return new String(block);
	}
	
	public String getStatusBlock(){
		StringBuffer block = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String dcDateAsText = null;
		if (dcDate != null) {
			dcDateAsText = dateFormat.format(dcDate);
		}
		String deDateAsText = null;
		if (deDate != null) {
			deDateAsText = dateFormat.format(deDate);
		}
		String dvDateAsText = null;
		if (dvDate != null) {
			dvDateAsText = dateFormat.format(dvDate);
		}

		block.append("Collected: ").
			append(StringUtils.defaultString(this.dcStatus,"")).append(" ").
			append(StringUtils.defaultString(this.dcBy,"")).append(" ").
			append(StringUtils.defaultString(dcDateAsText,"")).append("\n");
		block.append("Entered: ").
			append(StringUtils.defaultString(this.deStatus,"")).append(" ").
			append(StringUtils.defaultString(this.deBy,"")).append(" ").
			append(StringUtils.defaultString(deDateAsText,"")).append("\n");
		block.append("Verified: ").
			append(StringUtils.defaultString(this.dvStatus,"")).append(" ").
			append(StringUtils.defaultString(this.dvBy,"")).append(" ").
			append(StringUtils.defaultString(dvDateAsText,""));
		return new String(block);
	}
	
	public Boolean getLatestFlag() {return latestFlag;}
	public void setLatestFlag(Boolean latestFlag) {this.latestFlag = latestFlag;}
	
	public Short getAgeAtDC() {return ageAtDC;}
	public void setAgeAtDC(Short ageAtDC) {this.ageAtDC = ageAtDC;}
	
	public Map getNotes() {return notes;}
	public void setNotes(Map notes) {this.notes = notes;}
	
	


	// InstrumentSummary table
	public String getSummary() {return this.summary;}
	public void setSummary(String summary) {this.summary = summary;}
	
	// UploadedFiles table
	public String getFilename() {return this.filename;}
	public void setFilename(String filename) {this.filename = filename;}
	
	public String getFileContents() {return this.fileContents;}
	public void setFileContents(String fileContents) {this.fileContents = fileContents;}
	

		// override in instruments which have details
	public List getDetails() {
		return null;
	}
		

	


	
	// required result fields
	
	// required result fields serve two purposes:
	// 1. they must be have a value when a form submission is validated or the validation
	//    will fail, the user will be returned to the page with errors designating which 
	//    required fields are missing a value.
	// 2. in the enter flow, double enter compare, those fields which are required are
	//    the fields which are compared. non-required fields are not compared.
	//    note: any result field that can be required field must have metadata context='r'
	//          because that will result in two input fields for the field on the enter
	//          flow compare page

	// because of skip logic, a given field may be required in some situations but not 
	// required in others. however, since most instrument result fields are set to a missing
	// date code (e.g. LOGICAL_SKIP = -6) when they are skipped, they would still pass
	// required field validation since they have some value, even if it is a skip code.
	// therefore, such fields can be considered required fields at all times.
	
	// conditional required fields are fields that may not be required in some situation due
	// to skip logic AND which can be blank (null) in that situation. typically, the field
	// has been disabled and is blank so that the user can not input a value, so the field
	// should not be included in the set of required fields.
	
	// an example of this is a date field that has been set disabled and blank due to skip
	// logic, because a date field can not be assigned a missing date code,e.g. -6, 
	// because type mismatch validation will fail because the the code can not be formatted as 
	// a date. thus, the date result field should not be required in this situation.
	
	// furthermore, if such a result field can be disabled and set blank, but the field obtains
    // a value via a database trigger, it should also not be required for the purposes of
	// the enter flow compare, because the primary instrument value will be the calculated
	// value, and the compare instrument value will be null (and the user can not change it
	// because it is disabled) and the compare will fail. an example of this is a conditional
	// total field, where the user has chosen not to input the total such that it will be
	// calculated from the individual field values. thus, the total field should not be 
	// required in this situation.

	// so in cases where skip logic could disable and set to blank an otherwise required
	// result field, the field should be omitted from the set of required fields. this
	// means that the getRequiredResultFields method should perform the same skip logic
	// that is used in the view to skip the field and set it to blank.
	
	// subclasses of instruments that do not have multiple versions override this.
	// the set of required fields is used to enforce required field validation, and is also
	// used for the "enter" flow where all required fields must be double entered
	public String[] getRequiredResultFields() {
		if (this.instrVer != null && this.instrVer.length() > 0) {
			return getRequiredResultFields(this.instrVer);
		}
		return new String[0];
	}

	// subclasses of instruments that have multiple versions override this
	public String[] getRequiredResultFields(String instrVer) {
		return new String[0];
	}
	

	// override in subclasses
	// all instrument to set fields to unused = -8
	// generally this is for instruments with multiple versions where some versions do not use 
	// all of the fields. in this case, the instrument should override markUnusedFields(String instrVer)
	// and set all fields that are not used for a particular version to unused = -8
	public void markUnusedFields() {
		if (this.instrVer != null && this.instrVer.length() > 0) {
			markUnusedFields(this.instrVer);
		}
	}

	// subclasses of instruments that have multiple versions override this
	public void markUnusedFields(String instrVer) {
	}
	

	public void calculate() throws Exception {
	}
	
	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		updateAgeAtDC();
		updateLatestFlag();
	}
	
	/**
	 * TODO: need to figure out how to do this in the model. 
	 *
	 */
	public void updateLatestFlag(){
		
	}
	public void updateAgeAtDC(){
		if(getPatient()!=null){
			Integer age = calcAge(getPatient().getBirthDate(),getDcDate());
			if(age > getPatient().getAge()){
				age = getPatient().getAge();
			}
			setAgeAtDC((age==null)?null:age.shortValue());
		}else{
			setAgeAtDC(null);
		}
	}
	
	/** clear
	 * 
	 *  Clear every field. Typically used to clear fields in a command object before the
	 *  model is sent to the view, e.g. when adding instruments repeatedly on same view.
	 */
	public void clear() {
		id = null;
		visit = null;
		projName = null;
		patient = null;
		instrType = null;
		instrVer = null;
		dcDate = null;
		dcBy = null;
		dcStatus = null;
		dcNotes = null;
		researchStatus = null;
		qualityIssue = null;
		qualityIssue2 = null;
		qualityIssue3 = null;
		qualityNotes = null;
		deDate = null;
		deBy = null;
		deStatus = null;
		deNotes = null;
		dvDate = null;
		dvBy = null;
		dvStatus = null;
		dvNotes = null;
		latestFlag = null;
		ageAtDC = null;
		notes = new HashMap();
		summary = null;
	}
	

	
	public static List<InstrumentTracking> findInstruments(LavaDaoFilter filter){
		filter.setAlias("visit","visit");
		filter.addDefaultSort("dcDate",false);
		return MANAGER.get(InstrumentTracking.class,filter);
	}
	
	
	
	
	
//	 called by instruments which load file of detail records to calculate summary totals
	public void calcSummaryTotals() {
		StringBuffer procName = new StringBuffer("sp").append(getEntityName()).append("CalcTotals_LavaWeb");
		MANAGER.executeSQLProcedure(procName.toString(), new Object[] {getId()}, new int[] {Types.INTEGER},new char[] {'i'});
	}

	public void changeVersion(String version){
		StringBuffer procName = new StringBuffer("proc_").append(getInstrTypeEncoded(false)).append("_change_version");
		MANAGER.executeSQLProcedure(procName.toString(), new Object[] {getId(),getInstrVer()}, new int[] {Types.INTEGER,Types.VARCHAR},new char[] {'i','i'});
	}

	@Override
	public boolean getLocked() {
		// an instrument is considered locked if its parent visit is locked
		if (getVisit() == null) return super.getLocked();
		
		// this instrument is likely holding proxy values, 
		//   so cannot do a direct lookup; grab visit from id
		Long visitid = getVisit().getId();
		Visit visit = (Visit)Visit.MANAGER.getById(visitid, Visit.newFilterInstance());
		return visit.getLocked();

	}
	
	/**
	 * This method is used to determine whether and instrument list is specific to a single
	 * type of instrument because of the user filtering that has been applied (in which case,
	 * instrument specific functionality can be applied, e.g. if exporting the list).
	 * 
	 *  Subclasses should override this to make the determination.
	 * 
	 */
	public boolean isFilterInstrSpecific(String filterInstrType) {
		return false;
	}
	
	/**
	 * The default filename when exporting an instrument list. Instrument-specific subclasses
	 * can override this to provide an instrument-specific filename;
	 * 
	 * @return the default filename, not including a filename extension
	 */
	public String getExportListDefaultFilename() {
		Date today = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return new StringBuffer("instruments").append(dateFormat.format(today)).toString();
	}
	
	public String[] getExportCommonColHeaders() {
		return new String[]{"PATIENT","DATE","VISIT","MEASURE","COLLECTION STATUS","DATA ENTRY STATUS","VERIFY STATUS"};
	}
	
	public String[] getExportSummaryColHeaders() {
		return new String[]{"SUMMARY"};
	}
	
	public String[] getExportCommonData() {
		List data = new ArrayList();
		StringBuffer commonData = new StringBuffer();
		data.add(commonData.append("\"").append(getPatient().getFullNameRevNoSuffix()).append("\"").toString());
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		data.add(dateFormat.format(getVisit().getVisitDate()));
		data.add(getVisit().getVisitType());
		data.add(getInstrType());
		// since status includes username, e.g. dcBy, which include a comma, enclose in quotes
		commonData.setLength(0);
		data.add(commonData.append("\"").append(getCollectionStatusBlock()).append("\"").toString());
		commonData.setLength(0);
		data.add(commonData.append("\"").append(getEntryStatusBlock()).append("\"").toString());
		commonData.setLength(0);
		data.add(commonData.append("\"").append(getVerifyStatusBlock()).append("\"").toString());
		return (String[]) data.toArray(new String[]{});
	}

	/**
	 * Instrument specific subclasses should override this if they have summary data 
	 * which should be exported to a csv.
	 * 
	 * @param instrument
	 * @return
	 */
	public String[] getExportSummaryData() {
		return new String[]{new StringBuffer().append("\"").append(StringUtils.defaultString(this.getSummary())).append("\"").toString()};
	}

}
