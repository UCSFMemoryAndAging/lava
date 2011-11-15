package edu.ucsf.lava.crms.protocol.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * 
 * @author ctoohey
 * 
 * ProtocolInstrumentOptionConfig is a fulfillment option for the ProtocolInstrumentConfig, which 
 * has a collection of options. One of the options is the default, and any others are alternate
 * ways in which the instrument config can be fulfilled.   
 */
public class ProtocolInstrumentOptionConfig extends ProtocolInstrumentOptionConfigBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolInstrumentOptionConfig.class);
	
	public ProtocolInstrumentOptionConfig(){
		super();
		this.setAuditEntityType("ProtocolInstrumentOptionConfig");		
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
	private ProtocolVisitConfig customCollectWinAnchorVisit;
	// collectWinAnchorVisitId facilitates modifying the collectWinAnchorVisit association. the user selects a 
	// collectWinAnchorVisit and that collectWinAnchorVisitId is bound to this property. if it differs from this 
	// entity's existing collectWinAnchorVisit id, the collectWinAnchorVisitId is used to retrieve its collectWinAnchorVisit 
	// which is then set on this entity via setCollectWinAnchorVisit to change the associated collectWinAnchorVisit
	private Long customCollectWinAnchorVisitId;
	private Short customCollectWinSize;
	private Short customCollectWinOffset;
	private String customCollectWinStatus;
	
	/**
	 * Convenience methods to convert ProtocolInstrumentOptionConfigBase method types to types 
	 * of this subclass, since if an object of this class exists we know we can safely downcast. 
	 */
	public ProtocolInstrumentConfig getInstrument() {
		return (ProtocolInstrumentConfig) super.getInstrumentBase();
	}
	public void setInstrument(ProtocolInstrumentConfig protocolInstrument) {
		super.setInstrumentBase(protocolInstrument);
	}
	
	public String getInstrType() {
		return instrType;
	}
	public void setInstrType(String instrType) {
		this.instrType = instrType;
	}
	public ProtocolVisitConfig getCustomCollectWinAnchorVisit() {
		return customCollectWinAnchorVisit;
	}
	public void setCustomCollectWinAnchorVisit(ProtocolVisitConfig customCollectWinAnchorVisit) {
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
