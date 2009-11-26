package edu.ucsf.lava.crms.assessment.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

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
import edu.ucsf.lava.crms.session.CrmsSessionUtils;
//import edu.ucsf.lava.service.AssessmentService;
//import edu.ucsf.lava.service.SessionService;

public class CalculateController extends AbstractController {
//	protected AssessmentService assessmentService;
//	protected SessionService sessionService;
        InstrumentManager instrumentManager;
        SessionManager sessionManager;
	protected LavaDaoFilter filter;
	protected List<Long> instrIdList; // the list of instrument ids to be calculated
	protected Class clazz; // the class of the current instrument being calculated.
	
	// use this to specify classes whose calculate method is/should not be supported by this controller
	protected static final String[] UNSUPPORTED = {"macdiagnosis"};
	
	// This controller is invoked via the following URL: https://..IP../INSTANCE/assessment/instrument/calc.lava
	// TWO paramters are possible:
	//    1) instrTypeEncoded=INSTR_TYPE_ENCODED, where INSTR_TYPE_ENCODED has no spaces and is all lowercase, e.g.
	//       if this parameters is used, all instruments for this instrument type will be calculated
	//    2) instrId=INSTR_ID, where INSTR_ID is the InstrId of the instrument you want to calculate
	//	 this is useful if you only want to call calculate on a single instrument
        //
	// Here are some examples:
	// https://mac.ucsf.edu:7630/smd/crms/assessment/instrument/calc.lava?instrTypeEncoded=setshifting
	// https://mac.ucsf.edu:7630/smd/crms/assessment/instrument/calc.lava?instrId=12345
	
	// this should only be invoked after changing the transaction propagation behavior to PROPAGATION_REQUIRES_NEW
	// see below for more info
	
	// this is typically run on a development instance pointing at a production database server.
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

		// UPDATE: not clear if the below is applicable to Tomcat using Hibernate transactions, as it
		// is for JBoss using JTA transactions:
		// because instrument types with large numbers of instruments and instruments with many details
		// and instruments with upload files take so long to retrieve, it is not desirable to hold
		// an open transaction on the entire list (not to mention the fact that a transaction timeout
		// exception can be thrown). so instead of retrieving the entire list of instruments
		// of a given type, just obtain the list of id's (which is non-transactional and does not involve
		// retrieving details or upload files), and then iterate thru the id's, retrieving the instruments
		// one at a time transactionally, invoking calculate, and then saving the entity in a separate
		// transaction, which is achieved by changing the transaction propagation behavior such that
		// transaction boundaries are on each service call as opposed to spanning the entire request,
		// which is the transaction behavior normally used. 
		
		// TO BE CLEAR, this means modifying the transaction configuration in lava-dao.xml such that
		// propagationBehaviorName is changed from PROPAGATION_REQUIRED to PROPAGATION_REQUIRES_NEW and
		// redeploying the webapp. The typical scenario is to redeploy a development instance of the webapp
		// with this setting which is pointing to the database on which a instrument calculation should be 
		// done
		
                //// LavaDaoFilter filter = assessmentService.newFilterInstance(sessionService.getCurrentUser(request));
		this.filter = InstrumentTracking.newFilterInstance(CoreSessionUtils.getCurrentUser(this.sessionManager, request));
		this.instrIdList = new ArrayList();
		Instrument instr;
		this.clazz = null;

		if (instrIdsArrayParam != null) {
			calculateByIds(instrIdsArrayParam);
		} else if (instrTypeEncodedParam != null) {
			calculateByType(instrTypeEncodedParam);
		} else {
			logger.info("calculate failed. no valid parameters: instrTypeEncoded, instrIdsArray");
		}
		
		return new ModelAndView();
	}
		

	public void calculateByType(String instrTypeEncodedParam) throws Exception {
	        ////this.instrIdList = this.assessmentService.getInstrumentIdsByType(this.filter, instrTypeEncodedParam);
		////this.clazz = this.assessmentService.getInstrumentClass(instrTypeEncodedParam);
		this.clazz = this.instrumentManager.getInstrumentClass(instrTypeEncodedParam);
		this.instrIdList = InstrumentTracking.MANAGER.getIds(this.clazz, this.filter);
		
		String className = this.clazz.getSimpleName();
		if (!isSupported(className)) {
			logger.info("skipping calculate method for " + className + ". not supported in this controller.");
			return;
		} else {
			logger.info("beginning calculate for " + className + ". total objects to be calculated: " + this.instrIdList.size());
			int counter = 0;
			int total = this.instrIdList.size();
			for (Long instrId : this.instrIdList) {
				counter++;
				logger.info("calling calculate for " + className + " object id=" + instrId + " (" + counter + " of " + total + ")");
				calculateInstrId(instrId);
			}
			logger.info("calculate complete. calculated " + counter + "/" + this.instrIdList.size() + " objects");
		}
	}

	public void calculateByIds(String instrIdsArrayParam) throws Exception {
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
					logger.info("calling calculate for " + className + " object id=" + instrId + " (" + counter + " of " + total + ")");
					calculateInstrId(instrId);
				}
			}
		}
		
		int completed = total - failed.size();
		logger.info("calculate complete. calculated " + completed + "/" + total + " objects");
		StringBuffer block = new StringBuffer("objects that could not be calculated: ");
		if (failed.size()==0) {
			block.append("none");
		} else {
			block.append(failed.toString());
		}
		logger.info(new String(block));		
	}


	public void calculateInstrId(Long instrId) throws Exception {
		this.filter.addIdDaoEqualityParam(instrId);
		////Instrument instr = (Instrument) this.assessmentService.get(this.clazz, this.filter);
		Instrument instr = (Instrument) Instrument.MANAGER.getOne(this.clazz, this.filter);
// should not have to call calculate anymore. instead, the call to save will call all of the LavaEntity triggers
////		instr.calculate();
		////this.assessmentService.save(instr);
		instr.save();
		this.filter.clearDaoParams();
	}
	
	public void setInstrClass(Long instrId) {
                ////InstrumentTracking instrTracking = this.assessmentService.getInstrumentTracking(instrId, this.filter);
		////this.clazz = instrTracking==null ? null : this.assessmentService.getInstrumentClass(instrTracking.getInstrTypeEncoded());
//TRY USING Instrument.MANAGER so the following will obtain an instance of the actual class type, 
// and will not then have to re-retrieve an instance of that class below			
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

/**
	public void setAssessmentService(AssessmentService assessmentService) {
		this.assessmentService = assessmentService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
**/
}
