package edu.ucsf.lava.core.calendar.view;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import edu.ucsf.lava.core.calendar.CalendarDaoUtils;
import edu.ucsf.lava.core.calendar.controller.CalendarHandlerUtils;
import edu.ucsf.lava.core.view.model.MissingRenderParamException;
import edu.ucsf.lava.core.view.model.RenderParams;

public class CalendarRenderParams extends RenderParams{

	public CalendarRenderParams() {
		super();
		this.addRenderParam("calendarDateRange", String.class, RenderParams.REQUIRED_PARAM);
		this.addRenderParam("calendarDateStart", Date.class, RenderParams.REQUIRED_PARAM);
		this.addRenderParam("dayLength", String.class, RenderParams.REQUIRED_PARAM);
		this.addRenderParam("apptIdList", List.class, RenderParams.OPTIONAL_PARAM);
		this.addRenderParam("apptContentList", List.class, RenderParams.OPTIONAL_PARAM);
		this.addRenderParam("apptUrlList", List.class, RenderParams.OPTIONAL_PARAM);
		this.addRenderParam("dayClickEventUrl", String.class, RenderParams.OPTIONAL_PARAM);
		this.addRenderParam("timeClickEventUrl", String.class, RenderParams.OPTIONAL_PARAM);
	}

	@Override
	public Boolean validRenderParamValue(String paramName, Object paramValue) {
		// parameter value validation (called by the setRenderParam method)
		// NOTE: a check for valid paramValue class type is done prior to this call
		// 		and is not necessary to do here
		if (paramValue == null){
			return false;
		}
		if (paramName.contentEquals("calendarDateRange")){
			if (((String)paramValue).contentEquals(CalendarDaoUtils.DISPLAY_RANGE_DAY) || 
					((String)paramValue).contentEquals(CalendarDaoUtils.DISPLAY_RANGE_WEEK) ||
					((String)paramValue).contentEquals(CalendarDaoUtils.DISPLAY_RANGE_MONTH)){
				return true;
			} else {
				return false;
			}
		}
		if (paramName.contentEquals("dayLength")){
			if (((String)paramValue).contentEquals(CalendarDaoUtils.SHOW_DAYLENGTH_FULLDAY) || 
					((String)paramValue).contentEquals(CalendarDaoUtils.SHOW_DAYLENGTH_WORKDAY)){
				return true;
			} else {
				return false;
			}
		}
		
		// make sure any List parameter contains objects of correct class
		// (only test final item since list will most likely be built element by element 
		//	via the ViewManager call addRenderParamElement)
		if (paramName.contentEquals("apptIdList")){
			List paramList = ((List)paramValue);
			if (paramList.isEmpty() || !java.lang.Long.class.isInstance(paramList.get(paramList.size() - 1))){
				return false;
			}
		}
		if (paramName.contentEquals("apptContentList") || paramName.contentEquals("apptUrlList")){
			List paramList = ((List)paramValue);
			if (paramList.isEmpty() || !java.lang.String.class.isInstance(paramList.get(paramList.size() - 1))){
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void checkRequiredParams() throws Exception {
		// make sure appointment list params are all of the same length
		super.checkRequiredParams();
		List apptIdList = (List)this.getRenderParam("apptIdList");
		List apptContentList = (List)this.getRenderParam("apptContentList");
		if (apptIdList != null){
			if (apptContentList == null || apptContentList.size() != apptIdList.size()){
				throw new MissingRenderParamException("Mismatch of render parameter list sizes");
			}
		}
	}
	
	
	
}

	