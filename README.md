##lava

The "lava" repository contains the core infrastructural projects used to build LAVA web applications. 

LAVA is an open source web application framework developed at the UCSF Memory and Aging Center (MAC).  LAVA was initially developed as an application for clinical researchers to centrally manage project administration and data collection for multiple related research protocols.Over time, the LAVA framework has been refined into both a general purpose web application framework and a specifically purposed clinical research management solution.   

By describing LAVA as a web application framework we simply mean that LAVA provides one approach to providing the core services and functionality required for web application development.  These services are built upon standard open source libraries and lower level web application frameworks.

One objective for freely sharing the LAVA framework is to give a jump start to other application developers (particularly those that work in the context of academic research), enabling them to develop web applications more quickly and with less expense.   

Another, more complex, objective is the development of a community of academic research developers sharing a common platform; with the goals of learning from other developers  and incrementally improving the quality and sophistication of the tools available to the community for developing research-oriented software solutions.

This is not to exclude other developers from adopting LAVA as a development environment, but rather to simply describe where we, as the originators of the LAVA platform, are coming from, and where we plan to focus our efforts in our future development of LAVA. 

NOTE: LAVA does not meet FDA requirement 21 CFR Part 11 so it should not be used for clinical trials that require this FDA compliance



###Projects

The following Eclipse projects are contained in the "lava" repository:

- lava-core
> Provides the core web application infrastructure, supporting:
    - Entity CRUD
    - Entity field validation (domain list creation)
    - Entity lists with filters and sorting
    - JSPs with page decoration, LAVA tag library, skip logic tag library
    - Stylesheet supporting web page layout conforming to the Oracle BLAF recommendations
    - Tabs (modules) and Sub-tabs (sections) and sidebars
    - Integration with Spring Framework (Spring MVC, Spring WebFlow)
    - DAO Abstraction Layer
    - Hibernate O/R Mapping DAO implementation
    - MySQL Database
    - Calendar functionality
    - Jasper Reports
    - Auditing
    - Authentication and Authorization
    - Session Management
    
- lava-crms
> Builds upon lava-core with support for:
    - Patient, Caregiver, Contact Info, Contact Log, Doctors
    - Project (research study)
    - Enrollment Status for each Patient in a Project
    - Research Consents
    - Scheduling Visits
    - Patient Assessments (Instruments)
    - Instrument Double Data Entry 
    - Instrument Status
    - Setting Patient and/or Project Context
    
- lava-crms-ignav
> Enhancement to lava-crms with instrument group navigation feature.

- ui-tags
> JSP tag library that outputs Javascript to provide client-side skip logic


###Query Tool
An automated Excel workbook called "LAVA Query" is available to extract data sets from a LAVA application database. The tool provides a unique approach to iteratively refining the desired patient cohort and features proximity queries to retrieve data from various assessments that are all within a given time window. Custom data objects can be created as database views and made available to the tool.


###Documentation
The "LAVA Development Guide" and other documentation are in this repository in the "lava-core" project "docs" folder.



    
    
    
