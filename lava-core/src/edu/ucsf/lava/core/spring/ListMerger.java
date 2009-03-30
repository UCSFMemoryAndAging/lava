package edu.ucsf.lava.core.spring;



	import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;


	public class ListMerger implements FactoryBean {
		 private List list = new ArrayList();

		 
		  public ListMerger(List<List> items) {
		    for (List item : items) {
		      if (item != null) {
		        list.addAll(item);
		      }
		    }
		  }

		  public Object getObject() throws Exception {
		    return list;
		  }

		  public Class getObjectType() {
		    return list.getClass();
		  }

		  public boolean isSingleton() {
		    return true;
		  }

}
