package edu.ucsf.lava.crms.protocol.model;

import java.util.List;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;

/**
 * 
 * @author ctoohey
 * 
 * ProtocolInstrumentOption is a fulfillment option for the ProtocolInstrument that is associated with
 * instances of this class by a common node value.
 *
 * ProtocolInstrumentOption serves two main purposes:
 * a) supply properties needed to instantiate the instrument
 * b) serve as a possible instrument that could fulfill the corresponding treatment slot in the protocol 
 *    possibly within constraints that are also defined in this class or its superclass. multiple instances
 *    of this class associated with the same ProtocolInstrument represent alternatives for fulfillment. 
 */
public class ProtocolInstrumentOption extends ProtocolInstrumentOptionBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolInstrumentOption.class);
	
	public ProtocolInstrumentOption(){
		super();
	}
	
	private String instrType; 
	
	// a custom interaction window allows a specific interaction to override the default interaction
	// window defined in the timepoint containing the interaction 
// only seems to make sense for an instrument to be able to override the anchor time and window
// and not override with customWindowType="Float" where this individual instrument is collected
// relative to other instruments because with customWindowType="Float" the instruments are working
// as a group and there should be only one collectWindow definition and customWindow definitions
// for individual instruments does not make sense	
//	private String customWindowType;
	private ProtocolVisit customCollectWinAnchorVisit;
	// collectWinAnchorVisitId facilitates modifying the collectWinAnchorVisit association. the user selects a 
	// collectWinAnchorVisit and that collectWinAnchorVisitId is bound to this property. if it differs from this 
	// entity's existing collectWinAnchorVisit id, the collectWinAnchorVisitId is used to retrieve its collectWinAnchorVisit 
	// which is then set on this entity via setCollectWinAnchorVisit to change the associated collectWinAnchorVisit
	private Long customCollectWinAnchorVisitId;
	private Short customCollectWinSize;
	private Short customCollectWinOffset;
	private String customCollectWinStatus;
	
	public String getInstrType() {
		return instrType;
	}
	public void setInstrType(String instrType) {
		this.instrType = instrType;
	}
	public ProtocolVisit getCustomCollectWinAnchorVisit() {
		return customCollectWinAnchorVisit;
	}
	public void setCustomCollectWinAnchorVisit(ProtocolVisit customCollectWinAnchorVisit) {
		this.customCollectWinAnchorVisit = customCollectWinAnchorVisit;
		if (this.customCollectWinAnchorVisit != null) {
			this.customCollectWinAnchorVisitId = this.customCollectWinAnchorVisit.getId();
		}
	}
	public Long getCustomCollectWinAnchorVisitId() {
		return customCollectWinAnchorVisitId;
	}
	public void setCustomCollectWinAnchorVisitId(Long customCollectWinAnchorVisitId) {
		this.customCollectWinAnchorVisitId = customCollectWinAnchorVisitId;
	}
	public Short getCustomCollectWinSize() {
		return customCollectWinSize;
	}
	public void setCustomCollectWinSize(Short customCollectWinSize) {
		this.customCollectWinSize = customCollectWinSize;
	}
	public Short getCustomCollectWinOffset() {
		return customCollectWinOffset;
	}
	public void setCustomCollectWinOffset(Short customCollectWinOffset) {
		this.customCollectWinOffset = customCollectWinOffset;
	}
	public String getCustomCollectWinStatus() {
		return customCollectWinStatus;
	}
	public void setCustomCollectWinStatus(String customCollectWinStatus) {
		this.customCollectWinStatus = customCollectWinStatus;
	}
}
