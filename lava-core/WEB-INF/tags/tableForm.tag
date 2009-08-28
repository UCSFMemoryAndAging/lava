<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- create a table to be used as part of a form, i.e. the cells contain editable 
     data elements

	 note: unlike a listing, the data elements within the cells of a tableForm may
	       contain labels, although the labels must be short for it to work, and the 
	       labels are always to the left of the data element, regardless of what the
	       metadata says.
	       the tableForm style class is used to accomodate these labels.
--%>
<%@ attribute name="listingStyle" required="false"  
			description="the style class which determines the table width, e.g. 
           	'shortListing, 'listing', 'wideListing', veryWideListing'. if not
            specified, defaults to 'listing' " %>
<div class="inlineTable">  
<table class="${not empty listingStyle ? listingStyle : 'listing'} tableForm">  
  <jsp:doBody/>
</table>
</div>

