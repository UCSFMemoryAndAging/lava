<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>



	<tags:crmsNavSection module="mylava" section="welcome" textCode="section.welcome" />
	<tags:crmsNavSection module="mylava" section="myCalendar" textCode="section.myCalendar" lastSection="true"/>
	

	<tags:crmsNavSection module="people" section="findPatient" textCode="section.findPatient" />
	<tags:crmsNavSection module="people" section="patient" textCode="section.patient" />
    <tags:crmsNavSection module="people" section="caregiver" textCode="section.caregiver" />
  	<tags:crmsNavSection module="people" section="contactLog" textCode="section.contactLog" />
    <tags:crmsNavSection module="people" section="contactInfo" textCode="section.contactInfo" />
    <tags:crmsNavSection module="people" section="doctor" textCode="section.doctor" />
    <tags:crmsNavSection module="people" section="task" textCode="section.task" lastSection="true"/>
  
    
    <tags:crmsNavSection module="enrollment" section="status" textCode="section.status" />
    <tags:crmsNavSection module="enrollment" section="consent" textCode="section.consent" lastSection="true"/>
    
  
	<tags:crmsNavSection module="scheduling" section="patientCalendar" textCode="section.patientCalendar" disabled="${empty currentPatient ? true : false}"/>
    <tags:crmsNavSection module="scheduling" section="projectCalendar" textCode="section.projectCalendar" lastSection="true"/>
      
    <tags:crmsNavSection module="assessment" section="summary" textCode="section.summary" />
    <tags:crmsNavSection module="assessment" section="uds" textCode="section.uds" lastSection="true"/>
    
   	<tags:crmsNavSection module="reporting" section="reports" textCode="section.reports" lastSection="true"/>
    
    <tags:coreNavSection module="admin" section="auth" textCode="section.auth" />
    <tags:coreNavSection module="admin" section="session" textCode="section.session" lastSection="true"/>
    