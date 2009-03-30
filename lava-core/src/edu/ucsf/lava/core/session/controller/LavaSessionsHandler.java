package edu.ucsf.lava.core.session.controller;

import java.util.List;
import java.util.Locale;

import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.session.model.LavaServerInstance;
import edu.ucsf.lava.core.session.model.LavaSession;


public class LavaSessionsHandler extends BaseLavaSessionsHandler {

	public LavaSessionsHandler() {
	super();
	this.setHandledList("lavaSessions","lavaSession");
	this.setSourceProvider(new LavaSessionsSourceProvider(this));

}


public static class LavaSessionsSourceProvider extends BaseListComponentHandler.BaseListSourceProvider {
	
	public LavaSessionsSourceProvider(BaseListComponentHandler listHandler) {
		super(listHandler);
		this.listHandler.setPageSize(100);
	}

	public List loadElements(Locale locale, Object filter) {
			LavaDaoFilter daoFilter = (LavaDaoFilter)filter;
			LavaServerInstance serverInstance = ((LavaSessionsHandler)listHandler).getServerInstance();
			
			daoFilter.convertParamsToDaoParams();
			return 	LavaSession.MANAGER.getForServerInstance(serverInstance,daoFilter);
	}
	
	public List loadList(Locale locale, Object filter) {
		LavaDaoFilter daoFilter = (LavaDaoFilter)filter;
		LavaServerInstance serverInstance = ((LavaSessionsHandler)listHandler).getServerInstance();
		daoFilter.convertParamsToDaoParams();
		return ScrollablePagedListHolder.createSourceList(
				LavaSession.MANAGER.getForServerInstance(serverInstance,daoFilter),daoFilter);
	}
	
	
}







}
