package edu.ucsf.lava.crms.assessment.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.crms.assessment.InstrumentManager;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;

/** 
 * 
 * @author ctoohey
 * 
 * This class is used to invoke the save method on instrument objects. This in turn will invoke any
 * "calculations" in beforeUpdate (which in turn calls updateCalculatedFields) and afterUpdate. This
 * is typically used for instruments whose data has been imported into the database where   
 * calculations on that data need to be done, because it was not persisted via the application. 
 *
 */
public class CalculateController extends AbstractController {
	InstrumentManager instrumentManager;
	SessionManager sessionManager;
	protected LavaDaoFilter filter;
	protected List<Long> instrIdList; // the list of instrument ids to be calculated
	protected Class clazz; // the class of the current instrument being calculated.
	
	// use this to specify classes whose save method is/should not be supported by this controller
	protected static final String[] UNSUPPORTED = {"macdiagnosis"};
	
	// This controller is invoked via the following URL: https://..IP../INSTANCE/assessment/instrument/calc.lava
	// TWO parameters are possible:
	//    1) instrTypeEncoded=INSTR_TYPE_ENCODED, where INSTR_TYPE_ENCODED has no spaces and is all lowercase, e.g.
	//       if this parameters is used, ALL instruments for this instrument type will be saved/calculated.
	//	  2) instrIdsArray=INSTR_IDS, where INSTR_IDS are the instrIds (in typical array format, i.e. [123,234,345])
	//		 of the instruments you want to save/calculate. the nice thing is that they do not have to all be of a certain
	//		 instrument type. this is useful if you only want to call save on certain records, regardless of instrType
	//
	// Here are some examples:
	// http://localhost:8080/smd/crms/assessment/instrument/calc.lava?instrTypeEncoded=updrs
	// https://mac.ucsf.edu/mac/crms/assessment/instrument/calc.lava?instrTypeEncoded=setshifting
	// https://mac.ucsf.edu/mac/crms/assessment/instrument/calc.lava?instrIdsArray=[12345,12346,12347,12348]
	// https://mac.ucsf.edu/mac/crms/assessment/instrument/calc.lava?instrIdsArray=[12345] (for a single record)
	
	// for JBoss, this should only be invoked after changing the transaction propagation behavior to PROPAGATION_REQUIRES_NEW
	// see below for more info
	
	// this could be run on a development instance pointing at a production database server.
	// if so, change the JBoss database server configuration, and REMEMBER TO CHANGE IT BACK
	// after you are done 
	
	// NOTE: MacDiagnosis has its own CalculateController. See MacDiagnosisCalculateController.java
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
	        this.instrumentManager = CrmsManagerUtils.getInstrumentManager();
                this.sessionManager = CoreManagerUtils.getSessionManager();
		
		// obtain the instrTypeEncoded from the "instrTypeEncoded" request parameter, if it exists
		String instrTypeEncodedParam = request.getParameter("instrTypeEncoded");
		
		// obtain the string representing array of instrIds from the "instrIdsArray" request parameter, if it exists
		String instrIdsArrayParam = request.getParameter("instrIdsArray");

		// NOTE: if using JBoss and JTA transactions (not clear yet about performance in Tomcat with
		// Hibernate transactions when instrument has detail records, but fine in Tomcat with Hibernate
		// transactions for other instruments):
		// because instrument types with large numbers of instruments and instruments with many details
		// and instruments with upload files take so long to retrieve, it is not desirable to hold
		// an open transaction on the entire list (not to mention the fact that a transaction timeout
		// exception can be thrown). so instead of retrieving the entire list of instruments
		// of a given type, just obtain the list of id's (which is non-transactional and does not involve
		// retrieving details or upload files), and then iterate thru the id's, retrieving the instruments
		// one at a time transactionally, invoking save, and then saving the entity in a separate
		// transaction, which is achieved by changing the transaction propagation behavior such that
		// transaction boundaries are on each service call as opposed to spanning the entire request,
		// which is the transaction behavior normally used. 
		// TO BE CLEAR, this means modifying the transaction configuration in lava-dao.xml such that
		// propagationBehaviorName is changed from PROPAGATION_REQUIRED to PROPAGATION_REQUIRES_NEW and
		// redeploying the webapp. The typical scenario is to redeploy a development instance of the webapp
		// with this setting which is pointing to the database on which a instrument calculation should be 
		// done
		
		this.filter = InstrumentTracking.newFilterInstance(CoreSessionUtils.getCurrentUser(this.sessionManager, request));
		this.instrIdList = new ArrayList();
		Instrument instr;
		this.clazz = null;

		if (instrIdsArrayParam != null) {
			calculateByIds(request, instrIdsArrayParam);
		} else if (instrTypeEncodedParam != null) {
			calculateByType(request, instrTypeEncodedParam);
		} else {
			logger.info("calculate failed. no valid parameters: instrTypeEncoded, instrIdsArray");
		}
		
		return new ModelAndView("/info");
	}
		

	public void calculateByType(HttpServletRequest request, String instrTypeEncodedParam) throws Exception {
		this.clazz = this.instrumentManager.getInstrumentClass(instrTypeEncodedParam);
		this.instrIdList = InstrumentTracking.MANAGER.getIds(this.clazz, this.filter);
		
		String className = this.clazz.getSimpleName();
		if (!isSupported(className)) {
			logger.info("skipping save method for " + className + ". not supported in this controller.");
			return;
		} else {
			logger.info("beginning save/calculate for " + className + ". total objects to be calculated: " + this.instrIdList.size());
			int counter = 0;
			int total = this.instrIdList.size();
			for (Long instrId : this.instrIdList) {
				counter++;
				logger.info("calling save/calculate for " + className + " object id=" + instrId + " (" + counter + " of " + total + ")");
				calculateInstrId(instrId);
			}
			logger.info("calculate complete. calculated " + counter + "/" + this.instrIdList.size() + " objects");
			request.setAttribute("infoMessage", "Calculate complete. Calculated " + counter + "/" + this.instrIdList.size() + " objects.");
		}
	}

	public void calculateByIds(HttpServletRequest request, String instrIdsArrayParam) throws Exception {
		if (instrIdsArrayParam.startsWith("[") && instrIdsArrayParam.endsWith("]")) {
			String[] instrIdsListStringArray = instrIdsArrayParam.substring(1,instrIdsArrayParam.length()-1).split(",");
			this.instrIdList.clear();
			for (String s : instrIdsListStringArray) {
				this.instrIdList.add(new Long(s));
			}		
		} else {
			logger.info("calculate failed. 'instrIdsArray' parameter is malformed (ex. missing open and close brackets).");
			return;
		}
		
		int counter = 0;
		int total = this.instrIdList.size();
		List<Long> failed = new ArrayList();
		
		logger.info("beginning calculate for instrument array of size: " + total);
		for (Long instrId : this.instrIdList) {
			counter++;
			setInstrClass(instrId);
			if (this.clazz==null) {
				logger.info("skipping calculate for object id=" + instrId + " (NOT FOUND) (" + counter + " of " + total + ")");
				failed.add(instrId);
				this.filter.clearDaoParams();
			} else {
				String className = this.clazz.getSimpleName();
				if (!isSupported(className)) {
					logger.info("skipping calculate for " + className + " object id=" + instrId  + " (NOT SUPPORTED) (" + counter + " of " + total + ")");
					failed.add(instrId);
					this.filter.clearDaoParams();
				} else {
					logger.info("calling save/calculate for " + className + " object id=" + instrId + " (" + counter + " of " + total + ")");
					calculateInstrId(instrId);
				}
			}
		}
		
		int completed = total - failed.size();
		logger.info("calculate complete. calculated " + completed + "/" + total + " objects");
		request.setAttribute("infoMessage", "Calculate complete. Calculated " + completed + "/" + total + " objects.");
		StringBuffer block = new StringBuffer("objects that could not be saved/calculated: ");
		if (failed.size()==0) {
			block.append("none");
		} else {
			block.append(failed.toString());
		}
		logger.info(new String(block));		
	}


	public void calculateInstrId(Long instrId) throws Exception {
		this.filter.addIdDaoEqualityParam(instrId);
		Instrument instr = (Instrument) Instrument.MANAGER.getOne(this.clazz, this.filter);
		instr.save();
		this.filter.clearDaoParams();
	}
	
	public void setInstrClass(Long instrId) {
//TODO: try using Instrument.MANAGER so the following will obtain an instance of the actual class type, 
// and will not then have to re-retrieve an instance of the class in calculateInstrId			
		InstrumentTracking instrTracking = (InstrumentTracking) InstrumentTracking.MANAGER.getById(instrId, this.filter);
		if (instrTracking == null) {
		    this.clazz = null;
		}
		else {
		    this.clazz = this.instrumentManager.getInstrumentClass(instrTracking.getInstrTypeEncoded());
		}
	}

	// OVERRIDE THIS IN SUBCLASSES
	public boolean isSupported(String s) {
		for (int i=0; i<UNSUPPORTED.length; i++) {
			if (s.toLowerCase().startsWith(UNSUPPORTED[i].toLowerCase())) return false;
		}
		return true;
	}

}
