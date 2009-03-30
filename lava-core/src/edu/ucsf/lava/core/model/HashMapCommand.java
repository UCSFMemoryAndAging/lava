package edu.ucsf.lava.core.model;

import java.io.Serializable;
import java.util.HashMap;

public class HashMapCommand implements Serializable {

	private HashMap hashMap = new HashMap();

	public void setHashMap(HashMap hashMap) {
		this.hashMap = hashMap;
	}

	public HashMap getHashMap() {
		return this.hashMap;
	}
}
