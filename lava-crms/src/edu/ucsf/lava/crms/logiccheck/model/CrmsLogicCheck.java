package edu.ucsf.lava.crms.logiccheck.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.logiccheck.controller.LogicCheckUtils;
import edu.ucsf.lava.core.logiccheck.model.LogicCheck;
import edu.ucsf.lava.core.logiccheck.model.LogicCheckIssue;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
import edu.ucsf.lava.crms.model.CrmsEntity;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;


public class CrmsLogicCheck extends LogicCheck {
	public static CrmsLogicCheck.Manager MANAGER = new CrmsLogicCheck.Manager();
	
	@Override
	public LogicCheckIssue createNewIssue() {
		return (LogicCheckIssue)CrmsLogicCheckIssue.MANAGER.create();
	}
	

	@Override
	public String getCheckDescCalculated() {
		return super.getCheckDescCalculated();
	}
	
	public boolean doLogicCheckOperator(EntityBase entity, String operator, String fieldname, String fieldvalues, String entityclassname, String fieldvalues_alternate, String[] checkDescDataAppend) throws Exception {
		return super.doLogicCheckOperator(entity, operator, fieldname, fieldvalues, entityclassname, fieldvalues_alternate, checkDescDataAppend);
	}
	
	static public class Manager extends EntityBase.Manager{
		
		public Manager(){
			super(CrmsLogicCheck.class);
		}
		
		// get all matching definitions for this entity
		public List get(CrmsEntity entity){
			return null;
			//LavaDaoFilter filter = CrmsLogicCheck.MANAGER.newFilterInstance();
			//List<CrmsLogicCheck> defs = (List<CrmsLogicCheck>)CrmsLogicCheck.MANAGER.get(filter);
			//return defs;
		}
	}
	
}
