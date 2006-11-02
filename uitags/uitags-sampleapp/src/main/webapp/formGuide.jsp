<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="uic" uri="http://uitags.sf.net/uitags" %>
<c:import url="/common/objects.jsp" />

<c:import url="/common/header.jsp">
  <c:param name="title"><formGuide> Example</c:param>
  <c:param name="style">
    th {
      text-align:left;
      font-weight:bold;
    }
  </c:param>
  <c:param name="headContents">
    <script type="text/javascript" src="<c:url value="${requestScope.commonJs}" />"></script>
  </c:param>
</c:import>

<form action="">

  <h3>Removing or Hiding Irrelevant Widgets</h3>
  <table>
    <tr>
      <th colspan="2">Postal Address</th>
    </tr>
    <tr>
      <td colspan="2">
        <input type="checkbox" id="removePostalSection" value="true" />
        <label for="removePostalSection">
          Same as residential (REMOVE)
        </label>
        <input type="checkbox" id="hidePostalSection" value="true" style="margin-left:50px;" />
        <label for="hidePostalSection">
          Same as residential (DISABLE)
        </label>
      </td>
    </tr>
    <tr>
      <td><label id="postalAddressLabel">Address:</label></td>
      <td><input type="text" name="postalAddress" /></td>
    </tr>
    <tr>
      <td><label id="postalCountryLabel">Country:</label></td>
      <td><select name="postalCountry"><option>Afghanistan</option></select></td>
    </tr>
  </table>
  <uic:formGuide>
    <uic:observe elementId="removePostalSection" forValue="true" />
    <uic:remove  elementId="postalAddressLabel" />
    <uic:remove  elementId="postalCountryLabel" />
    <uic:remove  elementName="postalAddress" />
    <uic:remove  elementName="postalCountry" />
  </uic:formGuide>
  <uic:formGuide>
    <uic:observe elementId="hidePostalSection" forValue="true" />
    <uic:disable elementId="postalAddressLabel" />
    <uic:disable elementId="postalCountryLabel" />
    <uic:disable elementName="postalAddress" />
    <uic:disable elementName="postalCountry" />
  </uic:formGuide>

  <h3>Observing a Checkbox Group</h3>
  <div>
    <input type="checkbox" id="termsAccepted" name="accepted" value="terms" />
    <label for="termsAccepted">
      I accept the terms and conditions.
    </label>
  </div>
  <div>
    <input type="checkbox" id="licenceAgreementAccepted" name="accepted" value="licenceAgreement" />
    <label for="licenceAgreementAccepted">
      I accept the licence agreement.
    </label>
  </div>
  <input type="submit" value="Download" name="download" />
  <uic:formGuide>
    <uic:observe elementName="accepted" forValue="terms" />
    <uic:observe elementName="accepted" forValue="licenceAgreement" />
    <uic:enable  elementName="download" />
  </uic:formGuide>

  <h3>Observing a Radio Group</h3>
  <table>
    <tr>
      <td colspan="3">Search for:</td>
    </tr>
    <tr>
      <td>
        <input type="radio" name="searchFor" value="keywords" checked="checked" />
        Keywords:
      </td>
      <td>
        <input type="text" name="keywords" />
      </td>
      <td>
        <input type="submit" id="keywordsSearchSubmit" value="Search" />
      </td>
    </tr>
    <tr>
      <td>
        <input type="radio" name="searchFor" value="author" />
        Author:
      </td>
      <td>
        <input type="text" name="author" />
      </td>
      <td>
        <input type="submit" id="authorSearchSubmit" value="Search" />
      </td>
    </tr>
  </table>
  <uic:formGuide>
    <uic:observe elementName="searchFor" forValue="keywords" />
    <uic:disable elementName="author" />
    <uic:disable elementId="authorSearchSubmit" />
  </uic:formGuide>
  <uic:formGuide>
    <uic:observe elementName="searchFor" forValue="author" />
    <uic:disable elementName="keywords" />
    <uic:disable elementId="keywordsSearchSubmit" />
  </uic:formGuide>

  <h3>Observing a Select Box</h3>
  <div>
    Your favorite programming language:
    <select name="favoriteLanguage">
      <option value="1" selected="selected">Java</option>
      <option value="2">C</option>
      <option value="-1">Other - specify</option>
    </select>
    <input type="text" id="specifiedFavoriteLanguage" name="specifiedFavoriteLanguage" />
  </div>
  <uic:formGuide>
    <uic:observe elementName="favoriteLanguage" forValue="-1" />
    <uic:enable  elementId="specifiedFavoriteLanguage" />
  </uic:formGuide>

  <h3>Observing for an Empty Select Box</h3>
  <div>
    <div>Selecting at least one option will enable the clear button.</div>
    <select id="clearTarget" multiple="multiple" size="4">
      <option>Selecting any</option>
      <option>of these will</option>
      <option>enable the</option>
      <option>Clear button</option>
    </select>
  </div>
  <input type="button" value="Clear" id="clearTrigger" />
  <uic:select type="none" injectTo="clearTrigger" applyTo="clearTarget" />
  <uic:formGuide>
    <uic:observeForNull elementId="clearTarget" />
    <uic:disable elementId="clearTrigger" />
  </uic:formGuide>
</form>

<c:import url="/common/footer.jsp" />
