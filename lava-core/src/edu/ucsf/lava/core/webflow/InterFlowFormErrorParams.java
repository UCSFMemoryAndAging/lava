package edu.ucsf.lava.core.webflow;

public class InterFlowFormErrorParams {
	
	public InterFlowFormErrorParams(String[] codes, Object[] arguments) {
		this.codes = codes;
		this.arguments = arguments;
	}
	private String[] codes;
	private Object[] arguments;
	
	public Object[] getArguments() {
		return arguments;
	}
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}
	public String[] getCodes() {
		return codes;
	}
	public void setCodes(String[] codes) {
		this.codes = codes;
	}
}
