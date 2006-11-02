<%--
  - Provides common header.
  - @param title
  - @param style
  - @param viewSource
  --%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html>
<head>
  <title>uitags Demo - <c:out value="${param.title}" /></title>
  <style type="text/css">
    body, p, td, th, span, div {
      font:11px verdana;
    }
    body {
      padding:0px;
      margin:0px;
      background-color:#f4f4f4;
    }
    ul, ol {
      margin-left:10px;
      padding:0px;
    }
    input, select, textarea {
      border:1px solid #aaaaaa;
    }
  </style>
  <style type="text/css"><c:out value="${param.style}" /></style>

  <c:out value="${param.headContents}" escapeXml="false" />
</head>

<body>
<div style="padding:10px; background-color:#dde8f3; border-bottom:1px solid #aaaaaa;">
  <h3>uitags Demo - <c:out value="${param.title}" /></h3>
  <a href="<c:url value="http://uitags.sourceforge.net/" />">Project Home</a>
  &nbsp;&nbsp;
  <a href="<c:url value="/index.jsp" />">Demo Home</a>
  <c:if test="${param.viewSource != 'false'}">
    &nbsp;&nbsp;
    <a href="<c:url value="${pageContext.request.servletPath}.txt" />">View Source</a>
  </c:if>

  <div style="float: right; position: relative; bottom: 35px;">
  <a href="http://validator.w3.org/check?uri=referer"><img
      style="border: 0px; height: 31px; width:88px;"
      src="includes/img/valid-xhtml11.png" alt="Valid XHTML 1.1" /></a>
  </div>
</div>

<div style="padding:10px;">
