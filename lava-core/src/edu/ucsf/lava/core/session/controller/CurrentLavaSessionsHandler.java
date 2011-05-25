package edu.ucsf.lava.core.session.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.core.session.model.LavaServerInstance;
import edu.ucsf.lava.core.session.model.LavaSession;


public class CurrentLavaSessionsHandler extends BaseLavaSessionsHandler {
	public CurrentLavaSessionsHandler() {
	super();
	this.setHandledList("currentLavaSessions","lavaSession");
	CurrentLavaSessionsSourceProvider sourceProvider = new CurrentLavaSessionsSourceProvider(this);
	sourceProvider.setSessionManager(this.sessionManager);
	this.setSourceProvider(sourceProvider);
}

public static class CurrentLavaSessionsSourceProvider extends BaseListComponentHandler.BaseListSourceProvider {
	protected SessionManager sessionManager;
	
	public CurrentLavaSessionsSourceProvider(BaseListComponentHandler listHandler) {
		super(listHandler);
		this.listHandler.setPageSize(1000);
	}
	
	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public List loadElements(Locale locale, Object filter) {
			//LavaDaoFilter daoFilter = (LavaDaoFilter)filter;
			//LavaServerInstance serverInstance = ((CurrentLavaSessionsHandler)listHandler).getServerInstance();
			
			//daoFilter.convertParamsToDaoParams();
			//return 	LavaSession.MANAGER.getCurrent(serverInstance,daoFilter);
		List results = new ArrayList<LavaSession>();
		results.addAll(sessionManager.getLavaSessions().values());
		
		//sort is descending order by time
		Collections.sort(results, 
						new Comparator<LavaSession>() {
							public int compare(LavaSession session1, LavaSession session2) {
								return -1 * session1.getAccessTimestamp().compareTo(session2.getAccessTimestamp());
							}
						});
		return results;
	}
	
	public List loadList(Locale locale, Object filter) {
		LavaDaoFilter daoFilter = (LavaDaoFilter)filter;
		//LavaServerInstance serverInstance = ((CurrentLavaSessionsHandler)listHandler).getServerInstance();
		//daoFilter.convertParamsToDaoParams();
		
		List results = new ArrayList<LavaSession>();
		results.addAll(sessionManager.getLavaSessions().values());
		//sort in descending order by time
		Collections.sort(results, 
						new Comparator<LavaSession>() {
							public int compare(LavaSession session1, LavaSession session2) {
								return -1 * session1.getAccessTimestamp().compareTo(session2.getAccessTimestamp());
							}
						});
		
		return ScrollablePagedListHolder.createSourceList(results,daoFilter);
	}
	
	
}







}
