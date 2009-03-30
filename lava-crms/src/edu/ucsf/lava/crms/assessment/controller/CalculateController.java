package edu.ucsf.lava.crms.assessment.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.crms.assessment.InstrumentManager;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class CalculateController extends AbstractController {

	
	// this controller is invoked via the following URL:
	// https://..IP../INSTANCE/assessment/instrument/calc.lava?instrTypeEncoded=INSTR_TYPE_ENCODED 
	//   where INSTR_TYPE_ENCODED has no spaces and is all lowercase, e.g.
	// https://mac.ucsf.edu/mac/assessment/instrument/calc.lava?instrTypeEncoded=setshifting
	
	// this should only be invoked after changing the transaction propagation behavior to PROPAGATION_REQUIRES_NEW
	// see below for more info
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// obtain the instrTypeEncoded from the "instr" request parameter
		String instrTypeEncoded = request.getParameter("instrTypeEncoded");
		SessionManager sessionManager = CoreManagerUtils.getSessionManager();
		InstrumentManager instrumentManager = CrmsManagerUtils.getInstrumentManager();
		
		
		// because instrument types with large numbers of instruments and instruments with many details
		// and instruments with upload files take so long to retrieve, it is not be desireable to hold
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
		
		// obtain a list of instrument id's for the given instrTypeEncoded
		LavaDaoFilter filter = InstrumentTracking.newFilterInstance(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request));
		Class instrumentClass = instrumentManager.getInstrumentClass(instrTypeEncoded);
		List<Long> instrIdList = InstrumentTracking.MANAGER.getIds(instrumentClass,filter);
		
		// iterate over the list, invoking the calculate method on each element
		
		Instrument instr;
		for (Long instrId : instrIdList) {
			logger.info("calling calculate method for " + instrTypeEncoded + " object id=" + instrId);
			filter.addIdDaoEqualityParam(instrId);
			instr = (Instrument) Instrument.MANAGER.getOne(instrumentClass, filter);
			instr.calculate();
			instr.save();
			filter.clearDaoParams(); 
		}
		
		return new ModelAndView();
	}

}
