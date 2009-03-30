package edu.ucsf.lava.core.dao.hibernate;

import org.hibernate.criterion.Criterion;

import edu.ucsf.lava.core.dao.LavaDaoParam;
public class DaoHibernateCriterionParam implements LavaDaoParam {
	
	private Criterion criterion;
	public DaoHibernateCriterionParam(Criterion criterion) {
		this.criterion = criterion;
	}
	
	public String getType() {
		return this.TYPE_CRITERION;
	}

	public Criterion getCriterion(){
		return this.criterion;
	}
	
}
