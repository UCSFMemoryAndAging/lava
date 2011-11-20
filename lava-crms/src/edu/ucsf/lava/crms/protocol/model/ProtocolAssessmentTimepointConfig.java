package edu.ucsf.lava.crms.protocol.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class ProtocolAssessmentTimepointConfig extends ProtocolTimepointConfig {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolAssessmentTimepointConfig.class);

	public ProtocolAssessmentTimepointConfig() {
		super();
		this.setAuditEntityType("ProtocolAssessmentTimepointConfig");
	}

	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{
				/*this.getProtocolBase(),*/ 
				this.getProtocolConfigBase().getProtocolTimepointConfigsBase(), 
				this.getProtocolVisitConfigsBase()};
	}
	
// collection window is the default collection window during which all
// instruments in this timepoint should be completed, i.e. all instruments within
// the timepoint must have a designated status that means they are complete.
// note that this implies that it is the instruments within the visits that are
// checked to determine whether an instrument collection window has been met or 
// not, not the visits themselves.

// note that even if the scheduling
// window is not met, the collection window could be met (and vice versa)

	// private String interWindowType;
	private Short collectWinSize;
	private Short collectWinOffset;
	
	public Short getCollectWinSize() {
		return collectWinSize;
	}

	public void setCollectWinSize(Short collectWinSize) {
		this.collectWinSize = collectWinSize;
	}

	public Short getCollectWinOffset() {
		return collectWinOffset;
	}

	public void setCollectWinOffset(Short collectWinOffset) {
		this.collectWinOffset = collectWinOffset;
	}
	
}
