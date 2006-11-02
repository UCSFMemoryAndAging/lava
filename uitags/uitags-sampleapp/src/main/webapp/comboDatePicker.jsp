<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="uic" uri="http://uitags.sf.net/uitags" %>
<%@ taglib prefix="ui"  tagdir="/WEB-INF/tags" %>

<c:import url="/common/objects.jsp" />

<c:import url="/common/header.jsp">
  <c:param name="title">Date Picker Example</c:param>
  <c:param name="style">
    .uiPanel_panel {
      background-color:#ffffff;
      border:solid 1px #4d94de;
      width:200px;
      visibility:hidden;
      position:absolute; /* Required to make the browser render properly */
    }

    .dragHandle {
      background-color:#4d94de;
      height: 19px;
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
      cursor:pointer;
    }

    .uiCalendar_cellWeekday {
      background-color:#ffffff;
      cursor:pointer;
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
      cursor:auto;
    }

    .uiCalendar_nextMonth, .uiCalendar_previousMonth,
    .uiCalendar_nextYear, .uiCalendar_previousYear {
      cursor:pointer;
    }

    #dobNowUpdater {
      color:#4d94de;
    }
  </c:param>
  <c:param name="headContents">
    <script type="text/javascript" src="<c:url value="${requestScope.comboDatePickerJs}" />"></script>
  </c:param>
</c:import>

Date of birth:
<input id="dob" type="text" value="04/05/2006" size="10" />

<input type="image" id="dobIcon" src="includes/img/calendar.png"
    style="border:0px; cursor:pointer; vertical-align:middle;" />

<uic:panel id="dobCalendarPanel" anchorTo="dobIcon"
    listener="new uiPanel_TriggerBlocker('dobCalendarPanel', 'dobIcon')">
  <ui:panelHandle css="dragHandle" />

  <uic:calendar>
    <uic:updateDate injectTo="dobIcon" dateObtainer="new uiCalendar_WidgetDateObtainerStrategy('dob', null, 'dd/MM/yyyy')" />

    <div>
      <div style="float:left; clear:left; margin:2px; margin-right:5px;"><a id="dobNowUpdater" href="" onclick="return false;">Now</a></div>
      <uic:updateDate injectTo="dobNowUpdater" />

      <div style="float:left;"><select id="dobMonthLister"><option /></select></div>
      <uic:listMonths injectTo="dobMonthLister" />

      <div style="float:left;"><ui:monthSpinner /></div>

      <div style="float:left;"><select id="dobYearLister"><option /></select></div>
      <uic:listYears injectTo="dobYearLister" />

      <div style="float:left;"><ui:yearSpinner /></div>
    </div>

    <div style="clear:left;">
      <uic:calendarGrid actionResolver="new uiCalendar_ActionResolverStrategy('dob', null, 'dobCalendarPanel', 'dd/MM/yyyy')" />
    </div>
  </uic:calendar>

  <uic:show injectTo="dobIcon" on="click" />
  <uic:hide injectTo="dobCalendarPanel" on="blur" />
</uic:panel>

dd/mm/yyyy

<c:import url="/common/footer.jsp" />
