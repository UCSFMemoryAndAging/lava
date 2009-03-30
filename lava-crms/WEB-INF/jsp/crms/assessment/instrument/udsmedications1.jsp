<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udsmedications1"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="pmeds"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Medications</page:param>
  <page:param name="quicklinks">prescrip,nonPrescrip,vitSupp</page:param>
   
<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsCommon.jsp">
	<c:param name="entity" value="${instrTypeEncoded}"/>
	<c:param name="view" value="${componentView}"/>
	<c:param name="component" value="${component}"/>
</c:import>


<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"><spring:message code="udsmedications1.instructions1"/></page:param>
  <page:param name="instructions2"><spring:message code="udsmedications1.instructions2"/></page:param>
	<tags:createField property="pmeds" entity="${instrTypeEncoded}" component="${component}"/>
	
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">prescrip</page:param>
  <page:param name="sectionNameKey">udsmedications1.prescrip.section</page:param>
  <page:param name="view">${componentView}</page:param>

	<tags:tableForm>  
		<tags:listRow>
			<tags:listColumnHeader labelKey="udsmedications1.prescrip.firstHeaderCol0" width="25%"/>
			<tags:listColumnHeader  labelKey="udsmedications1.prescrip.firstHeaderCol1_2" width="20%"/>
			<tags:listColumnHeader  labelKey="udsmedications1.prescrip.firstHeaderCol3_4" width="20%"/>
			<tags:listColumnHeader  labelKey="udsmedications1.prescrip.firstHeaderCol5" width="15%"/>
			<tags:listColumnHeader labelKey="udsmedications1.prescrip.firstHeaderCol6_7" width="20%"/>
		</tags:listRow>
		<tags:listRow>
			<tags:listColumnHeader labelKey="udsmedications1.prescrip.secondHeaderCol0" width="25%"/>
			<tags:listColumnHeader labelKey="udsmedications1.prescrip.secondHeaderCol1_2" width="20%"/>
			<tags:listColumnHeader labelKey="udsmedications1.prescrip.secondHeaderCol3_4" width="20%"/>
			<tags:listColumnHeader labelKey="udsmedications1.prescrip.secondHeaderCol5" width="15%"/>
			<tags:listColumnHeader labelKey="udsmedications1.prescrip.secondHeaderCol6_7" width="20%"/>
		</tags:listRow>

		<c:forTokens items="a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t" delims="," var="item">
			<tags:listRow>
			<tags:listCell><tags:createField property="pm${item}" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="pm${item}s,pm${item}su" separator=" / " entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="pm${item}f,pm${item}fu" separator=" / " entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="pm${item}p" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="pm${item}pf,pm${item}pfu" separator=" / " entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			</tags:listRow>
		</c:forTokens>
	</tags:tableForm>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="view">${componentView}</page:param>
  
<tags:createField property="nmeds" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
   <page:param name="sectionId">nonPrescrip</page:param>
  <page:param name="sectionNameKey">udsmedications1.nonPrescrip.section</page:param>
   <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
  
  
  <tags:tableForm>  
		<tags:listRow>
			<tags:listColumnHeader labelKey="udsmedications1.nonPrescrip.firstHeaderCol0" width="30%"/>
			<tags:listColumnHeader  labelKey="udsmedications1.nonPrescrip.firstHeaderCol1_2" width="30%"/>
			<tags:listColumnHeader  labelKey="udsmedications1.nonPrescrip.firstHeaderCol3_4" width="30%"/>
		</tags:listRow>
		<tags:listRow>
			<tags:listColumnHeader labelKey="udsmedications1.nonPrescrip.secondHeaderCol0" width="30%"/>
			<tags:listColumnHeader labelKey="udsmedications1.nonPrescrip.secondHeaderCol1_2" width="30%"/>
			<tags:listColumnHeader labelKey="udsmedications1.nonPrescrip.secondHeaderCol3_4" width="30%"/>
		</tags:listRow>

		<c:forTokens items="a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t" delims="," var="item">
			<tags:listRow>
			<tags:listCell><tags:createField property="nm${item}" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="nm${item}s,nm${item}su" separator=" / " entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="nm${item}f,nm${item}fu" separator=" / " entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			</tags:listRow>
		</c:forTokens>
	</tags:tableForm>
</page:applyDecorator>


<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="view">${componentView}</page:param>
  
<tags:createField property="vitasups" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
   <page:param name="sectionId">vitSupp</page:param>
  <page:param name="sectionNameKey">udsmedications1.vitSupp.section</page:param>
   <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
  
  
  <tags:tableForm>  
		<tags:listRow>
			<tags:listColumnHeader labelKey="udsmedications1.vitSupp.firstHeaderCol0" width="30%"/>
			<tags:listColumnHeader  labelKey="udsmedications1.vitSupp.firstHeaderCol1_2" width="30%"/>
			<tags:listColumnHeader  labelKey="udsmedications1.vitSupp.firstHeaderCol3_4" width="30%"/>
		</tags:listRow>
		<tags:listRow>
			<tags:listColumnHeader labelKey="udsmedications1.vitSupp.secondHeaderCol0" width="30%"/>
			<tags:listColumnHeader labelKey="udsmedications1.vitSupp.secondHeaderCol1_2" width="30%"/>
			<tags:listColumnHeader labelKey="udsmedications1.vitSupp.secondHeaderCol3_4" width="30%"/>
		</tags:listRow>

		<c:forTokens items="a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t" delims="," var="item">
			<tags:listRow>
			<tags:listCell><tags:createField property="vs${item}" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="vs${item}s,vs${item}su" separator=" / " entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="vs${item}f,vs${item}fu" separator=" / " entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			</tags:listRow>
		</c:forTokens>
	</tags:tableForm>
</page:applyDecorator>



<c:if test="${componentMode != 'vw'}">
<c:forEach begin="0" end="1" var="current">
  <c:choose>
    <c:when test="${componentView == 'doubleEnter' || (componentView == 'compare' && current == 1)}">
      <c:set var="componentPrefix" value="compareInstrument"/>
    </c:when>
    <c:otherwise>
      <c:set var="componentPrefix" value="instrument"/>
    </c:otherwise>
  </c:choose>
  <c:if test="${current == 0 || (current == 1 && componentView == 'compare')}">


<%-- pmeds Yes=1 --%>
<ui:formGuide>
  <ui:observe elementIds="pmeds" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="pma,pmas,pmaf,pmapf,pmb,pmbs,pmbf,pmbpf,pmc,pmcs,pmcf,pmcpf,pmd,pmds,pmdf,pmdpf,pme,pmes,pmef,pmepf,
  	pmf,pmfs,pmff,pmfpf,pmg,pmgs,pmgf,pmgpf,pmh,pmhs,pmhf,pmhpf,pmi,pmis,pmif,pmipf,pmj,pmjs,pmjf,pmjpf,pmk,pmks,pmkf,pmkpf,
  	pml,pmls,pmlf,pmlpf,pmm,pmms,pmmf,pmmpf,pmn,pmns,pmnf,pmnpf,pmo,pmos,pmof,pmopf,pmp,pmps,pmpf,pmppf,pmq,pmqs,pmqf,pmqpf,
  	pmr,pmrs,pmrf,pmrpf,pms,pmss,pmsf,pmspf,pmt,pmts,pmtf,pmtpf" component="${componentPrefix}"/>
  <ui:unskip elementIds="pmasu,pmafu,pmap,pmapfu,pmbsu,pmbfu,pmbp,pmbpfu,pmcsu,pmcfu,pmcp,pmcpfu,pmdsu,pmdfu,pmdp,pmdpfu,
  	pmesu,pmefu,pmep,pmepfu,pmfsu,pmffu,pmfp,pmfpfu,pmgsu,pmgfu,pmgp,pmgpfu,pmhsu,pmhfu,pmhp,pmhpfu,
  	pmisu,pmifu,pmip,pmipfu,pmjsu,pmjfu,pmjp,pmjpfu,pmksu,pmkfu,pmkp,pmkpfu,pmlsu,pmlfu,pmlp,pmlpfu,
  	pmmsu,pmmfu,pmmp,pmmpfu,pmnsu,pmnfu,pmnp,pmnpfu,pmosu,pmofu,pmop,pmopfu,pmpsu,pmpfu,pmpp,pmppfu,
  	pmqsu,pmqfu,pmqp,pmqpfu,pmrsu,pmrfu,pmrp,pmrpfu,pmssu,pmsfu,pmsp,pmspfu,pmtsu,pmtfu,pmtp,pmtpfu"
  component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>           

<%-- nmeds Yes=1 --%>
<ui:formGuide>
  <ui:observe elementIds="nmeds" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="nma,nmas,nmaf,nmb,nmbs,nmbf,nmc,nmcs,nmcf,nmd,nmds,nmdf,nme,nmes,nmef,nmf,nmfs,nmff,nmg,nmgs,nmgf,
  	nmh,nmhs,nmhf,nmi,nmis,nmif,nmj,nmjs,nmjf,nmk,nmks,nmkf,nml,nmls,nmlf,nmm,nmms,nmmf,nmn,nmns,nmnf,nmo,nmos,nmof,
  	nmp,nmps,nmpf,nmq,nmqs,nmqf,nmr,nmrs,nmrf,nms,nmss,nmsf,nmt,nmts,nmtf" component="${componentPrefix}"/>
  <ui:unskip elementIds="nmasu,nmafu,nmbsu,nmbfu,nmcsu,nmcfu,nmdsu,nmdfu,nmesu,nmefu,nmfsu,nmffu,nmgsu,nmgfu,nmhsu,nmhfu,
  	nmisu,nmifu,nmjsu,nmjfu,nmksu,nmkfu,nmlsu,nmlfu,nmmsu,nmmfu,nmnsu,nmnfu,nmosu,nmofu,nmpsu,nmpfu,nmqsu,nmqfu,
  	nmrsu,nmrfu,nmssu,nmsfu,nmtsu,nmtfu"
  component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>           

<%-- vitasups Yes=1 --%>
<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:observe elementIds="vitasups" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="vsa,vsas,vsaf,vsb,vsbs,vsbf,vsc,vscs,vscf,vsd,vsds,vsdf,vse,vses,vsef,vsf,vsfs,vsff,vsg,vsgs,vsgf,
  	vsh,vshs,vshf,vsi,vsis,vsif,vsj,vsjs,vsjf,vsk,vsks,vskf,vsl,vsls,vslf,vsm,vsms,vsmf,vsn,vsns,vsnf,vso,vsos,vsof,
  	vsp,vsps,vspf,vsq,vsqs,vsqf,vsr,vsrs,vsrf,vss,vsss,vssf,vst,vsts,vstf"
  component="${componentPrefix}"/>
  <ui:unskip elementIds="vsasu,vsafu,vsbsu,vsbfu,vscsu,vscfu,vsdsu,vsdfu,vsesu,vsefu,vsfsu,vsffu,vsgsu,vsgfu,vshsu,vshfu,
  	vsisu,vsifu,vsjsu,vsjfu,vsksu,vskfu,vslsu,vslfu,vsmsu,vsmfu,vsnsu,vsnfu,vsosu,vsofu,vspsu,vspfu,vsqsu,vsqfu,
  	vsrsu,vsrfu,vsssu,vssfu,vstsu,vstfu"
  component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>           
 
</c:if>
</c:forEach>
</c:if>



</page:applyDecorator>    
</page:applyDecorator>    
	    
