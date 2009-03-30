package edu.ucsf.lava.core.list.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.SimpleFormController;

import edu.ucsf.lava.core.list.ListManager;
import edu.ucsf.lava.core.list.model.LabelValueBean;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;



/* this is just a temporary test class to display all of the static
 * lists created by the list manager
 */

public class ListListsController extends SimpleFormController implements ManagersAware {
	
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
   
    protected ListManager listManager;
    
	
	public ListListsController() {
		// just put LabelValueBean to put something here
	    setCommandClass(LabelValueBean.class);
	}

	protected Map referenceData(HttpServletRequest request) throws Exception {
		// put the contents of the lists Map into the model
		Map<String,Map<String,String>> lists = null;
		if (request.getParameter("with") != null) {
			lists = new HashMap<String,Map<String,String>>(listManager.getDefaultStaticLists());
		}
		else {
			lists = new HashMap<String,Map<String,String>>(listManager.getDefaultStaticListsWithoutCodes());
		}
		/*
		// alternatively, put the lists Map itself into the model 
		Map lists = new HashMap();
		lists.put("staticLists", listService.getStaticLists());
		*/
		return lists;
	}
	
	protected void doSubmitAction(Object command) throws Exception {
		
	}
	
	public void updateManagers(Managers managers) {
		this.listManager = CoreManagerUtils.getListManager(managers);
		}

	

	
}
