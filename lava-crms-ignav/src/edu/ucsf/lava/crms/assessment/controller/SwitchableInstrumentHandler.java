package edu.ucsf.lava.crms.assessment.controller;

import java.util.Map;

import edu.ucsf.lava.crms.assessment.controller.upload.FileLoader;

public class SwitchableInstrumentHandler extends InstrumentHandler {

	public SwitchableInstrumentHandler() {
		super();
		requiredFieldEvents.add("switch");  // switch does a save, so definitely check required fields
	}

	public SwitchableInstrumentHandler(Map<String, FileLoader> fileLoaders) {
		super(fileLoaders);
	}
	
}
