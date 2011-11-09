package edu.ucsf.lava.crms.protocol.model;

import java.util.List;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
/// import edu.ucsf.lava.crms.protocol.model.ProtocolTimepoint.ReorderingInfo;

public class ProtocolAssessmentTimepoint extends ProtocolTimepoint {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolAssessmentTimepoint.class);

	public ProtocolAssessmentTimepoint() {
		super();
		this.setAuditEntityType("ProtocolAssessmentTimepoint");
	}

	public Object[] getAssociationsToInitialize(String method) {
		// initialize parent to the Protocol level as need the projName of the protocol
		return new Object[]{this.getProtocol(), this.getProtocol().getTimepoints(), this.getVisits()};
	}
	
// collection window is the default collection window during which all
// instruments in this timepoint should be completed, i.e. all instruments within
// the timepoint must have a designated status that means they are complete.
// note that this implies that it is the instruments within the visits that are
// checked to determine whether an instrument collection window has been met or 
// not, not the visits themselves.

// note that even if the scheduling
// window is not met, the collection window could be met (and vice versa)

	// a Visit that is the anchor for the collect window (note that unlike the
	// scheduling window, there is no collectWinDaysFrom offset from the anchor; the anchor
	// is the window anchor)
	private ProtocolVisit collectWinAnchorVisit;
	// collectWinAnchorVisitId facilitates modifying the collectWinAnchorVisit association. the user selects a 
	// collectWinAnchorVisit and that collectWinAnchorVisitId is bound to this property. if it differs from this 
	// entity's existing collectWinAnchorVisit id, the collectWinAnchorVisitId is used to retrieve its collectWinAnchorVisit 
	// which is then set on this entity via setCollectWinAnchorVisit to change the associated collectWinAnchorVisit
	private Long collectWinAnchorVisitId;
	// private String interWindowType;
	private Short collectWinSize;
	private Short collectWinOffset;
	// the completion status(es), one of which must be met for the instrument to
	// be considered
	// complete. this can be a single status string, or a regular expression
	// such as multiple status
	// strings separated by the '|' char
	private String collectWinStatus;
	
	public ProtocolVisit getCollectWinAnchorVisit() {
		return collectWinAnchorVisit;
	}

	public void setCollectWinAnchorVisit(ProtocolVisit collectWinAnchorVisit) {
		this.collectWinAnchorVisit = collectWinAnchorVisit;
		if (this.collectWinAnchorVisit != null) {
			this.collectWinAnchorVisitId = this.collectWinAnchorVisit.getId();
		}
	}

	public Long getCollectWinAnchorVisitId() {
		return collectWinAnchorVisitId;
	}

	public void setCollectWinAnchorVisitId(Long collectWinAnchorVisitId) {
		this.collectWinAnchorVisitId = collectWinAnchorVisitId;
	}

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

	public String getCollectWinStatus() {
		return collectWinStatus;
	}

	public void setCollectWinStatus(String collectWinStatus) {
		this.collectWinStatus = collectWinStatus;
	}

	
}
