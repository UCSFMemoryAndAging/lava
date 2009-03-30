<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- create a table to be used as part of a form, i.e. the cells contain editable 
     data elements

	 note: unlike a listing, the data elements within the cells of a tableForm may
	       contain labels, although the labels must be short for it to work, and the 
	       labels are always to the left of the data element, regardless of what the
	       metadata says.
	       the tableForm style class is used to accomodate these labels.
--%>
<div class="inlineTable">  
<table class="listing tableForm">  
  <jsp:doBody/>
</table>
</div>

