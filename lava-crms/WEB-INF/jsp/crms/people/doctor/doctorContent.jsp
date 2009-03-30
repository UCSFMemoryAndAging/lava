<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

 
  
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">name</page:param>
  <page:param name="sectionNameKey">doctor.name.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="id"  component="${component}"/>
<tags:createField property="firstName"  component="${component}"/>
<tags:createField property="middleInitial" component="${component}"/>
<tags:createField property="lastName" component="${component}"/>
<tags:createField property="docType" component="${component}"/>
</page:applyDecorator>    

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">location</page:param>
  <page:param name="sectionNameKey">doctor.location.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="address" component="${component}"/>
<tags:createField property="city"  component="${component}"/>
<tags:createField property="state"  component="${component}"/>
<tags:createField property="zip"  component="${component}"/>
<tags:createField property="phone1" component="${component}"/>
<tags:createField property="phoneType1"  component="${component}"/>
<tags:createField property="phone2"  component="${component}"/>
<tags:createField property="phoneType2"  component="${component}"/>
<tags:createField property="phone3" component="${component}"/>
<tags:createField property="phoneType3"  component="${component}"/>
<tags:createField property="email"  component="${component}"/>
</page:applyDecorator>    
