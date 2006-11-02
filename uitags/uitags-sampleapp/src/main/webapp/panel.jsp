<%@ page language="java" import="java.util.*" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="uic" uri="http://uitags.sf.net/uitags" %>
<%@ taglib prefix="ui"  tagdir="/WEB-INF/tags" %>

<c:import url="/common/objects.jsp" />

<c:import url="/common/header.jsp">
  <c:param name="title"><panel> Example</c:param>
  <c:param name="style">
    #draggablePanel {
      width:200px;
      height:100px;
      border:solid 1px #4d94de;
    }

    #draggablePanelHandle {
      height:20px;
      background-color:#4d94de;
    }

    #hideablePanel {
      width:200px;
      height:100px;
      border:solid 1px #4d94de;
      visibility:hidden;
    }

    #richHandlePanel {
      width:400px;
      height:100px;
      border:solid 1px #4d94de;
      visibility:hidden;
    }

    .richHandle {
      background-color:#4d94de;
      height:19px;
    }

    #mouseAttachedPanel {
      width:200px;
      height:50px;
      border:solid 1px #4d94de;
      background-color:#ffffe1;
      visibility:hidden;
    }
  </c:param>
  <c:param name="headContents">
    <script type="text/javascript" src="<c:url value="${requestScope.commonJs}" />"></script>
  </c:param>
</c:import>


<h3>Draggable Panel</h3>
<uic:panel id="draggablePanel">
  <div id="draggablePanelHandle"></div>
  <uic:drag injectTo="draggablePanelHandle" />
  This is a draggable panel.
</uic:panel>

<h3 style="margin-top:120px;">Hideable Panel</h3>
<img id="helpIcon" src="includes/img/help.png" style="cursor: pointer;" />
<uic:panel id="hideablePanel" anchorTo="helpIcon">
  <uic:show injectTo="helpIcon" on="mouseover" />
  <uic:hide injectTo="helpIcon" on="mouseout" delay="1000" />
  <uic:hide injectTo="hideablePanel" on="mouseout" delay="1000" />
  This is a hideable panel. This panel is configured to appear onmouseover
  and to hide onmouseout after 1 second delay. A common use case for this
  panel is for displaying <i>help</i> information.
</uic:panel>
<%--
  - NOTE: the anchor-based positioning might be off
  - a bit in mozilla due to the use of non-zero border for the containing panel
  - (this is presumably Mozilla's bug as it doesn't happen in Opera).
  --%>

<h3 style="margin-top:120px;">Panel with Rich Handle</h3>
<input type="button" id="richHandlePanelShowTrigger" value="Show" />
<uic:panel id="richHandlePanel">
  <uic:show injectTo="richHandlePanelShowTrigger" on="click" />
  <uic:hide injectTo="richHandlePanel" on="blur" />
  <ui:panelHandle css="richHandle" />
  <p style="clear:left;">
    This is a hideable panel with rich handle. This panel is configured
    to appear onclick and to hide onblur (the panel "blurs" if you click
    outside it). The handle has a button on the left to make the panel
    "sticky". A sticky panel doesn't hide onblur.
  </p>
</uic:panel>

<h3 style="margin-top:120px;">Panel Which Pops Up at Mouse Pointer</h3>
<p>Click on the textarea below to get a pop-up panel which appears at the tip of the mouse pointer.</p>
<textarea id="textareaNeedingExplanation" rows="4" cols="40"></textarea>
<uic:panel id="mouseAttachedPanel">
  <uic:show injectTo="textareaNeedingExplanation"
      on="click" position="mouse" />
  <uic:hide injectTo="textareaNeedingExplanation" on="mouseout" />
  <uic:hide injectTo="mouseAttachedPanel" on="mouseout" />
  Move your mouse pointer out of the textarea to hide this panel.
</uic:panel>

<c:import url="/common/footer.jsp" />
