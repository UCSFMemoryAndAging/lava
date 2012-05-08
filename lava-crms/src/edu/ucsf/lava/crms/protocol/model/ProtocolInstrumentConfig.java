package edu.ucsf.lava.crms.protocol.model;

import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class ProtocolInstrumentConfig extends ProtocolInstrumentConfigBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolInstrumentConfig.class);
	
	public ProtocolInstrumentConfig(){
		super();
		this.setAuditEntityType("ProtocolInstrumentConfig");		
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		// options are eagerly fetched
		return new Object[]{
// should not need the following since options now are eagerly fetched				
////				this.getProtocolInstrumentConfigOptionsBase()
				};
	}

	private String category;
	// a custom collection window allows an instrument to override the default collection
	// window defined in the timepoint configuration to which this instrument belongs
	private Boolean customCollectWinDefined;
// only seems to make sense for an instrument to be able to override the anchor time and window
// and not override with customWindowType="Float" where this individual instrument is collected
// relative to other instruments because with customWindowType="Float" the instruments are working
// as a group and there should be only one collectWindow definition and customWindow definitions
// for individual instruments does not make sense	
//	private String customWindowType;
	private ProtocolVisitConfig customCollectWinProtocolVisitConfig;
	// customCollectWinProtocolVisitConfigId facilitates modifying the customCollectWinProtocolVisitConfig association. the user selects a 
	// customCollectWinProtocolVisitConfig and that customCollectWinProtocolVisitConfigId is bound to this property. if it differs from this 
	// entity's existing customCollectWinProtocolVisitConfig id, the customCollectWinProtocolVisitConfigId is used to retrieve its customCollectWinProtocolVisitConfig 
	// which is then set on this entity via setCustomCollectWinProtocolVisitConfig to change the associated customCollectWinProtocolVisitConfig
	private Long customCollectWinProtocolVisitConfigId;
	private Short customCollectWinSize;
	private Short customCollectWinOffset;
	
	private String defaultCompStatus;
	private String defaultCompReason;
	private String defaultCompNote;
	
	
	/**
	 * Convenience methods to convert ProtocolInstrumentConfigBase method types to types of 
	 * this subclass, since if an object of this class exists we know we can safely downcast. 
	 */
	public ProtocolVisitConfig getProtocolVisitConfig() {
		return (ProtocolVisitConfig) super.getProtocolVisitConfigBase();
	}
	public void setProtocolVisitConfig(ProtocolVisitConfig protocolVisitBase) {
		super.setProtocolVisitConfigBase(protocolVisitBase);
	}
	public Set<ProtocolInstrumentConfigOption> getOptions() {
		return (Set<ProtocolInstrumentConfigOption>) super.getProtocolInstrumentConfigOptionsBase();
	}
	public void setOptions(Set<ProtocolInstrumentConfigOption> options) {
		super.setProtocolInstrumentConfigOptionsBase(options);
	}

	
	/*
	 * Method to add a ProtocolInstrumentOptionConfig to an ProtocolInstrumentConfig's collection, managing the bi-directional relationship.
	 */	
	public void addOption(ProtocolInstrumentConfigOption protocolInstrumentConfigOption) {
		protocolInstrumentConfigOption.setProtocolInstrumentConfig(this);
		this.getOptions().add(protocolInstrumentConfigOption);
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public Boolean getCustomCollectWinDefined() {
		return customCollectWinDefined;
	}

	public void setCustomCollectWinDefined(Boolean customCollectWinDefined) {
		this.customCollectWinDefined = customCollectWinDefined;
	}

	public ProtocolVisitConfig getCustomCollectWinProtocolVisitConfig() {
		return customCollectWinProtocolVisitConfig;
	}
	public void setCustomCollectWinProtocolVisitConfig(ProtocolVisitConfig customCollectWinProtocolVisitConfig) {
		this.customCollectWinProtocolVisitConfig = customCollectWinProtocolVisitConfig;
		if (this.customCollectWinProtocolVisitConfig != null) {
			this.customCollectWinProtocolVisitConfigId = this.customCollectWinProtocolVisitConfig.getId();
		}
	}
	public Long getCustomCollectWinProtocolVisitConfigId() {
		return customCollectWinProtocolVisitConfigId;
	}
	public void setCustomCollectWinProtocolVisitConfigId(Long customCollectWinProtocolVisitConfigId) {
		this.customCollectWinProtocolVisitConfigId = customCollectWinProtocolVisitConfigId;
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
	public String getDefaultCompStatus() {
		return defaultCompStatus;
	}
	public void setDefaultCompStatus(String defaultCompStatus) {
		this.defaultCompStatus = defaultCompStatus;
	}
	public String getDefaultCompReason() {
		return defaultCompReason;
	}
	public void setDefaultCompReason(String defaultCompReason) {
		this.defaultCompReason = defaultCompReason;
	}
	public String getDefaultCompNote() {
		return defaultCompNote;
	}
	public void setDefaultCompNote(String defaultCompNote) {
		this.defaultCompNote = defaultCompNote;
	}
	
	protected void updateSummary() {
		StringBuffer block = new StringBuffer();
		if (this.getCustomCollectWinDefined()) {
			block.append("Custom Collect Window\n");
			block.append("From Visit: ").append(this.getCustomCollectWinProtocolVisitConfig() != null ? this.getCustomCollectWinProtocolVisitConfig().getLabel() : "").append("\n");
			block.append("Offset:").append(this.getCustomCollectWinOffset()).append("  Size:").append(this.getCustomCollectWinSize()).append("\n");
		}
		if (this.getOptional()) {
			block.append(" (optional)");
		}
		
		this.setSummary(block.toString());
	}
	
	
	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		
		this.updateSummary();
		
		for (ProtocolInstrumentConfigOption instrConfigOption : this.getOptions()) {
			instrConfigOption.updateSummary();
		}
	}

	public void beforeCreate() {
		super.beforeCreate();
		this.setNodeType(INSTRUMENT_NODE);
	}

}
