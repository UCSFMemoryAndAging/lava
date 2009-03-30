package edu.ucsf.lava.core.session.controller;

import java.util.List;
import java.util.Locale;

import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.session.model.LavaServerInstance;
import edu.ucsf.lava.core.session.model.LavaSession;


public class CurrentLavaSessionsHandler extends BaseLavaSessionsHandler {

	
	
	public CurrentLavaSessionsHandler() {
	super();
	this.setHandledList("currentLavaSessions","lavaSession");
	this.setSourceProvider(new CurrentLavaSessionsSourceProvider(this));

}




public static class CurrentLavaSessionsSourceProvider extends BaseListComponentHandler.BaseListSourceProvider {
	
	public CurrentLavaSessionsSourceProvider(BaseListComponentHandler listHandler) {
		super(listHandler);
		this.listHandler.setPageSize(100);
	}

	public List loadElements(Locale locale, Object filter) {
			LavaDaoFilter daoFilter = (LavaDaoFilter)filter;
			LavaServerInstance serverInstance = ((CurrentLavaSessionsHandler)listHandler).getServerInstance();
			
			daoFilter.convertParamsToDaoParams();
			return 	LavaSession.MANAGER.getCurrent(serverInstance,daoFilter);
	}
	
	public List loadList(Locale locale, Object filter) {
		LavaDaoFilter daoFilter = (LavaDaoFilter)filter;
		LavaServerInstance serverInstance = ((CurrentLavaSessionsHandler)listHandler).getServerInstance();
		daoFilter.convertParamsToDaoParams();
		return ScrollablePagedListHolder.createSourceList(
				LavaSession.MANAGER.getCurrent(serverInstance,daoFilter),daoFilter);
	}
	
	
}







}
