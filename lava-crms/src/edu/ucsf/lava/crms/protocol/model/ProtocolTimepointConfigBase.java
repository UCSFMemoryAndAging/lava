package edu.ucsf.lava.crms.protocol.model;

import java.util.LinkedHashSet;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;


/**
 *   
 * @author ctoohey
 *
 *
 */
public abstract class ProtocolTimepointConfigBase extends ProtocolNodeConfig implements Comparable<ProtocolTimepointConfig> {
	
	public ProtocolTimepointConfigBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocolConfigBase");
		this.addPropertyToAuditIgnoreList("protocolVisitConfigsBase");
	}

	private ProtocolConfigBase protocolConfigBase;
	private Set<? extends ProtocolVisitConfigBase> protocolVisitConfigsBase = new LinkedHashSet<ProtocolVisitConfigBase>();

	public ProtocolConfigBase getProtocolConfigBase() {
		return protocolConfigBase;
	}

	public void setProtocolConfigBase(ProtocolConfigBase protocolBase) {
		this.protocolConfigBase = protocolBase;
	}

	public Set<? extends ProtocolVisitConfigBase> getProtocolVisitConfigsBase() {
		return protocolVisitConfigsBase;
	}

	public void setProtocolVisitConfigsBase(Set<? extends ProtocolVisitConfigBase> protocolVisitConfigs) {
		this.protocolVisitConfigsBase = protocolVisitConfigs;
	}

	/**
	 * The Comparable interface requires the compareTo method. In this case, this method will never
	 * be called because all instances will be of the subclass ProtocolTimepointConfig so its compareTo
	 * method will override this. This exists here because the Comparable interface should be implemented
	 * on the type of the collection, i.e. in ProtocolConfigBase the type of the timepoints collection
	 * is Set<ProtocolTimepointConfigBase> 
	 */
	public int compareTo(ProtocolTimepointBase protocolTimepoint) throws ClassCastException {
		return 0;
	}

	
}
