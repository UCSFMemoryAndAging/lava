package edu.ucsf.lava.core.spring;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;

public class MapMerger implements FactoryBean{
	 private Map map = new LinkedHashMap();

	 
	  public MapMerger(Map<String,Map> maps) {
	    for (String mapName : maps.keySet()) {
	      if (maps.get(mapName) != null) {
	        map.putAll(maps.get(mapName));
	      }
	    }
	  }

	  public Object getObject() throws Exception {
	    return map;
	  }

	  public Class getObjectType() {
	    return map.getClass();
	  }

	  public boolean isSingleton() {
	    return true;
	  }
}
