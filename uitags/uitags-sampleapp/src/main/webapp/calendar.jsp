<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="uic" uri="http://uitags.sf.net/uitags" %>
<%@ taglib prefix="ui"  tagdir="/WEB-INF/tags" %>

<c:import url="/common/objects.jsp" />

<c:import url="/common/header.jsp">
  <c:param name="title"><calendar> Example</c:param>
  <c:param name="style">
    #mycalContainer {
      background-color:#ffffff;
      border:solid 1px #4d94de;
      padding-top:3px;
      width:200px;
    }

    .uiCalendar_grid {
      margin-top:10px;
      width:100%;
    }

    .uiCalendar_grid td {
      border:1px solid #4d94de;
      width:14.3%;
    }

    .uiCalendar_cellWeekend {
      background-color:#dbeaf5;
    }

    .uiCalendar_cellWeekday {
      background-color:#ffffff;
    }

    .uiCalendar_headWeekend, .uiCalendar_headWeekday {
      background-color:#87cefa;
      color:#ffffff;
    }

    .uiCalendar_selected {
      background-color:#ffb6c1;
    }

    .uiCalendar_otherMonth {
      color:#ccccbb;
    }

    .uiCalendar_nextMonth, .uiCalendar_previousMonth,
    .uiCalendar_nextYear, .uiCalendar_previousYear {
    }

    #dobNowUpdater {
      color:#4d94de;
    }
  </c:param>
  <c:param name="headContents">
    <script type="text/javascript" src="<c:url value="${requestScope.commonJs}" />"></script>
  </c:param>
</c:import>

<div id="mycalContainer">
  <uic:calendar>
    <div>
      <div style="float:left; clear:left; margin:2px; margin-right:5px;"><a id="mycalNowUpdater" href="javascript:void(0);">Now</a></div>
      <uic:updateDate injectTo="mycalNowUpdater" />

      <div style="float:left;"><select id="mycalMonthLister"><option /></select></div>
      <uic:listMonths injectTo="mycalMonthLister" />

      <div style="float:left;"><ui:monthSpinner /></div>

      <div style="float:left;"><select id="mycalYearLister"><option /></select></div>
      <uic:listYears injectTo="mycalYearLister" />

      <div style="float:left;"><ui:yearSpinner /></div>
    </div>

    <div style="clear:left;">
      <uic:calendarGrid>
        <uic:attribute name="style" value="border: solid 1px black;" />
      </uic:calendarGrid>
    </div>
  </uic:calendar>
</div>

<c:import url="/common/footer.jsp" />
