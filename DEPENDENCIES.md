###Version
The latest stable release of the "lava" repository projects is represented by the version of the latest Tag in the repository, i.e. there is a single version number for all projects in the "lava" repository.

Tag version numbers are formatted as MAJOR.MINOR.PATCH where a letter ('a', 'b', etc.) may be appended to the PATCH for hot fixes.

Note that other repositories that depend on the "lava" repository, such as "lava-uds", do not keep version numbers in sync with the "lava" repository. However, the major version number is kept in sync, so it should be clear that a version of "lava-uds" will not work with a version of "lava" where the major version number is different.

The CHANGES file tracks releases with change notes.



###Environment Dependencies
- Java JDK 1.6 or later
- Apache Tomcat 5 or later
- MySQL 5.5 or later



###Source Dependencies
The "lava" repository does not have any dependencies on other repositories. Rather, it contains core infrastructure code that application level repositories, or shared application modules, depend on.

For example, the "lava-uds" repository has dependencies on the "lava" repository, as it contains both the "lava-app-demo" project, which is an application, and the "lava-crms-nacc" and "lava-crms-ignav-nacc" projects, which are shared application modules that can be used by a number of applications. These projects would be used together with projects from the "lava" repository and would have dependencies on those projects.



###Database Schema
The "versionhistory" table tracks which database update scripts have been run against a database. Database update scripts can be "data" scripts for metadata and other static application data, and "model" scripts for database schema changes, including views and stored procedures.

To obtain database model and data updates there are SQL scripts under the "database" directory of each project. Within the "database" folder, the scripts for production databases are under the "versions" folder. Within the "versions" folder all scripts under the "update" folders are candidates to be run.

Given the information in the "versionhistory" table about what database update scripts have been run against the database, to update the database to the latest version run all database update scripts that have a later version than indicated by "versionhistory". The scripts themselves will update "versionhistory" upon successful completion.

note: it is not guaranteed that database update scripts are idempotent although developers should be striving to create such scripts

