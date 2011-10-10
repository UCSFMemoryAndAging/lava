package edu.ucsf.lava.core.dao.hibernate;

import org.hibernate.criterion.Projection;

import edu.ucsf.lava.core.dao.LavaDaoProjection;

public class DaoHibernateCriterionProjection implements LavaDaoProjection {

	protected Projection projection;
	
	
	public DaoHibernateCriterionProjection(Projection projection) {
		super();
		this.projection = projection;
	}


	public String getType() {
		return LavaDaoProjection.TYPE_CRITERION;
	}


	public Projection getProjection() {
		return projection;
	}


	public void setProjection(Projection projection) {
		this.projection = projection;
	}

	
	
}
