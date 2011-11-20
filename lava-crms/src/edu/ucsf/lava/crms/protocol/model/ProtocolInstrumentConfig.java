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
		return new Object[]{this.getProtocolInstrumentOptionConfigsBase()};
	}

	private Boolean optional;
	// a custom collection window allows an instrument to override the default collection
	// window defined in the timepoint configuration to which this instrument belongs 
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
	private ProtocolInstrumentOptionConfig defaultOption;
	// defaultOptionId facilitates modifying the defaultOption association. the user selects a 
	// defaultOption (ProtocolInstrumentOptionConfig) and that defaultOptionId is bound to this property. 
	// if it differs from this entity's existing defaultOption id, the defaultOptionId is used to 
	// retrieve its defaultOption which is then set on this entity via setDefaultOption to change 
	// the associated defaultOption
	private Long defaultOptionId;
	
	
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
	public Set<ProtocolInstrumentOptionConfig> getOptions() {
		return (Set<ProtocolInstrumentOptionConfig>) super.getProtocolInstrumentOptionConfigsBase();
	}
	public void setOptions(Set<ProtocolInstrumentOptionConfig> options) {
		super.setProtocolInstrumentOptionConfigsBase(options);
	}

	
	/*
	 * Method to add a ProtocolInstrumentOptionConfig to an ProtocolInstrumentConfig's collection, managing the bi-directional relationship.
	 */	
	public void addOption(ProtocolInstrumentOptionConfig protocolInstrumentOptionConfig) {
		protocolInstrumentOptionConfig.setProtocolInstrumentConfig(this);
		this.getOptions().add(protocolInstrumentOptionConfig);
	}
	
	public Boolean getOptional() {
		return optional;
	}
	public void setOptional(Boolean optional) {
		this.optional = optional;
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

	public ProtocolInstrumentOptionConfig getDefaultOption() {
		return defaultOption;
	}

	public void setDefaultOption(ProtocolInstrumentOptionConfig defaultOption) {
		this.defaultOption = defaultOption;
		if (this.defaultOption != null) {
			this.defaultOptionId = this.defaultOption.getId();
		}
	}

	public Long getDefaultOptionId() {
		return defaultOptionId;
	}
	
	public void setDefaultOptionId(Long defaultOptionId) {
		this.defaultOptionId = defaultOptionId;
	}
	
	
}
