<?xml version="1.0"?>

<!DOCTYPE hibernate-mapping PUBLIC
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping>

<class name="edu.ucsf.memory.[scope].[module].model.scq" table="scq" select-before-update="true">

		<id name="id" type="long">
			<column name="[KEY COLUMN]" not-null="true"/>
			<generator class="identity"/>
		</id>


		<property name="scqDate" column="scq_date" type="date"/>
		<property name="scqName" column="scq_name" type="string" length="20"/>
		<property name="scq1" column="scq1" type="short"/>
		<property name="scq2" column="scq2" type="short"/>
		<property name="scq3" column="scq3" type="short"/>
		<property name="scq4" column="scq4" type="short"/>
		<property name="scq5" column="scq5" type="short"/>
		<property name="scq6" column="scq6" type="short"/>
		<property name="scq7" column="scq7" type="short"/>
		<property name="scq8" column="scq8" type="short"/>
		<property name="scq9" column="scq9" type="short"/>
		<property name="scq10" column="scq10" type="short"/>
		<property name="scq11" column="scq11" type="short"/>
		<property name="scq12" column="scq12" type="short"/>
		<property name="scq13" column="scq13" type="short"/>
		<property name="scq14" column="scq14" type="short"/>
		<property name="scq15" column="scq15" type="short"/>
		<property name="scq16" column="scq16" type="short"/>
		<property name="scq17" column="scq17" type="short"/>
		<property name="scq18" column="scq18" type="short"/>
		<property name="scq19" column="scq19" type="short"/>
		<property name="scq20" column="scq20" type="short"/>
		<property name="scq21" column="scq21" type="short"/>
		<property name="scq22" column="scq22" type="short"/>
		<property name="scq23" column="scq23" type="short"/>
		<property name="scq24" column="scq24" type="short"/>
		<property name="scq25" column="scq25" type="short"/>
		<property name="scq26" column="scq26" type="short"/>
		<property name="scq27" column="scq27" type="short"/>
		<property name="scq28" column="scq28" type="short"/>
		<property name="scq29" column="scq29" type="short"/>
		<property name="scq30" column="scq30" type="short"/>
		<property name="scq31" column="scq31" type="short"/>
		<property name="scq32" column="scq32" type="short"/>
		<property name="scq33" column="scq33" type="short"/>
		<property name="scq34" column="scq34" type="short"/>
		<property name="scq35" column="scq35" type="short"/>
		<property name="scq36" column="scq36" type="short"/>
		<property name="scq37" column="scq37" type="short"/>
		<property name="scq38" column="scq38" type="short"/>
		<property name="scq39" column="scq39" type="short"/>
		<property name="scq40" column="scq40" type="short"/>
		<property name="scqSocial" column="scq_social" type="short"/>
		<property name="scqComm" column="scq_comm" type="short"/>
		<property name="scqPatterns" column="scq_patterns" type="short"/>
		<property name="scqTotal" column="scq_total" type="short"/>


	<!-- associations -->



	<!-- filters -->
               <!--define or remove these standard filters-->
               <filter name="projectContext" condition=":projectContext)=[define]"/>
               <filter name="patient" condition=":patientId=[define]"/>


</class>


         <!-- queries  -->



</hibernate-mapping>
protected Date scqDate;
protected String scqName;
protected Short scq1;
protected Short scq2;
protected Short scq3;
protected Short scq4;
protected Short scq5;
protected Short scq6;
protected Short scq7;
protected Short scq8;
protected Short scq9;
protected Short scq10;
protected Short scq11;
protected Short scq12;
protected Short scq13;
protected Short scq14;
protected Short scq15;
protected Short scq16;
protected Short scq17;
protected Short scq18;
protected Short scq19;
protected Short scq20;
protected Short scq21;
protected Short scq22;
protected Short scq23;
protected Short scq24;
protected Short scq25;
protected Short scq26;
protected Short scq27;
protected Short scq28;
protected Short scq29;
protected Short scq30;
protected Short scq31;
protected Short scq32;
protected Short scq33;
protected Short scq34;
protected Short scq35;
protected Short scq36;
protected Short scq37;
protected Short scq38;
protected Short scq39;
protected Short scq40;
protected Short scqSocial;
protected Short scqComm;
protected Short scqPatterns;
protected Short scqTotal;
"scqDate",
"scqName",
"scq1",
"scq2",
"scq3",
"scq4",
"scq5",
"scq6",
"scq7",
"scq8",
"scq9",
"scq10",
"scq11",
"scq12",
"scq13",
"scq14",
"scq15",
"scq16",
"scq17",
"scq18",
"scq19",
"scq20",
"scq21",
"scq22",
"scq23",
"scq24",
"scq25",
"scq26",
"scq27",
"scq28",
"scq29",
"scq30",
"scq31",
"scq32",
"scq33",
"scq34",
"scq35",
"scq36",
"scq37",
"scq38",
"scq39",
"scq40",
"scqSocial",
"scqComm",
"scqPatterns",
"scqTotal",
<tags:createField property="scqDate" component="${component}"/>
<tags:createField property="scqName" component="${component}"/>
<tags:createField property="scq1" component="${component}"/>
<tags:createField property="scq2" component="${component}"/>
<tags:createField property="scq3" component="${component}"/>
<tags:createField property="scq4" component="${component}"/>
<tags:createField property="scq5" component="${component}"/>
<tags:createField property="scq6" component="${component}"/>
<tags:createField property="scq7" component="${component}"/>
<tags:createField property="scq8" component="${component}"/>
<tags:createField property="scq9" component="${component}"/>
<tags:createField property="scq10" component="${component}"/>
<tags:createField property="scq11" component="${component}"/>
<tags:createField property="scq12" component="${component}"/>
<tags:createField property="scq13" component="${component}"/>
<tags:createField property="scq14" component="${component}"/>
<tags:createField property="scq15" component="${component}"/>
<tags:createField property="scq16" component="${component}"/>
<tags:createField property="scq17" component="${component}"/>
<tags:createField property="scq18" component="${component}"/>
<tags:createField property="scq19" component="${component}"/>
<tags:createField property="scq20" component="${component}"/>
<tags:createField property="scq21" component="${component}"/>
<tags:createField property="scq22" component="${component}"/>
<tags:createField property="scq23" component="${component}"/>
<tags:createField property="scq24" component="${component}"/>
<tags:createField property="scq25" component="${component}"/>
<tags:createField property="scq26" component="${component}"/>
<tags:createField property="scq27" component="${component}"/>
<tags:createField property="scq28" component="${component}"/>
<tags:createField property="scq29" component="${component}"/>
<tags:createField property="scq30" component="${component}"/>
<tags:createField property="scq31" component="${component}"/>
<tags:createField property="scq32" component="${component}"/>
<tags:createField property="scq33" component="${component}"/>
<tags:createField property="scq34" component="${component}"/>
<tags:createField property="scq35" component="${component}"/>
<tags:createField property="scq36" component="${component}"/>
<tags:createField property="scq37" component="${component}"/>
<tags:createField property="scq38" component="${component}"/>
<tags:createField property="scq39" component="${component}"/>
<tags:createField property="scq40" component="${component}"/>
<tags:createField property="scqSocial" component="${component}"/>
<tags:createField property="scqComm" component="${component}"/>
<tags:createField property="scqPatterns" component="${component}"/>
<tags:createField property="scqTotal" component="${component}"/>
<tags:createField property="scqDate" component="${component}" entity="scq"/>
<tags:createField property="scqName" component="${component}" entity="scq"/>
<tags:createField property="scq1" component="${component}" entity="scq"/>
<tags:createField property="scq2" component="${component}" entity="scq"/>
<tags:createField property="scq3" component="${component}" entity="scq"/>
<tags:createField property="scq4" component="${component}" entity="scq"/>
<tags:createField property="scq5" component="${component}" entity="scq"/>
<tags:createField property="scq6" component="${component}" entity="scq"/>
<tags:createField property="scq7" component="${component}" entity="scq"/>
<tags:createField property="scq8" component="${component}" entity="scq"/>
<tags:createField property="scq9" component="${component}" entity="scq"/>
<tags:createField property="scq10" component="${component}" entity="scq"/>
<tags:createField property="scq11" component="${component}" entity="scq"/>
<tags:createField property="scq12" component="${component}" entity="scq"/>
<tags:createField property="scq13" component="${component}" entity="scq"/>
<tags:createField property="scq14" component="${component}" entity="scq"/>
<tags:createField property="scq15" component="${component}" entity="scq"/>
<tags:createField property="scq16" component="${component}" entity="scq"/>
<tags:createField property="scq17" component="${component}" entity="scq"/>
<tags:createField property="scq18" component="${component}" entity="scq"/>
<tags:createField property="scq19" component="${component}" entity="scq"/>
<tags:createField property="scq20" component="${component}" entity="scq"/>
<tags:createField property="scq21" component="${component}" entity="scq"/>
<tags:createField property="scq22" component="${component}" entity="scq"/>
<tags:createField property="scq23" component="${component}" entity="scq"/>
<tags:createField property="scq24" component="${component}" entity="scq"/>
<tags:createField property="scq25" component="${component}" entity="scq"/>
<tags:createField property="scq26" component="${component}" entity="scq"/>
<tags:createField property="scq27" component="${component}" entity="scq"/>
<tags:createField property="scq28" component="${component}" entity="scq"/>
<tags:createField property="scq29" component="${component}" entity="scq"/>
<tags:createField property="scq30" component="${component}" entity="scq"/>
<tags:createField property="scq31" component="${component}" entity="scq"/>
<tags:createField property="scq32" component="${component}" entity="scq"/>
<tags:createField property="scq33" component="${component}" entity="scq"/>
<tags:createField property="scq34" component="${component}" entity="scq"/>
<tags:createField property="scq35" component="${component}" entity="scq"/>
<tags:createField property="scq36" component="${component}" entity="scq"/>
<tags:createField property="scq37" component="${component}" entity="scq"/>
<tags:createField property="scq38" component="${component}" entity="scq"/>
<tags:createField property="scq39" component="${component}" entity="scq"/>
<tags:createField property="scq40" component="${component}" entity="scq"/>
<tags:createField property="scqSocial" component="${component}" entity="scq"/>
<tags:createField property="scqComm" component="${component}" entity="scq"/>
<tags:createField property="scqPatterns" component="${component}" entity="scq"/>
<tags:createField property="scqTotal" component="${component}" entity="scq"/>
<tags:createField property="scqDate" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scqName" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq1" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq2" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq3" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq4" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq5" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq6" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq7" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq8" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq9" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq10" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq11" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq12" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq13" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq14" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq15" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq16" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq17" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq18" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq19" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq20" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq21" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq22" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq23" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq24" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq25" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq26" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq27" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq28" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq29" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq30" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq31" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq32" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq33" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq34" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq35" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq36" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq37" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq38" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq39" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scq40" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scqSocial" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scqComm" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scqPatterns" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scqTotal" component="${component}" entity="${instrTypeEncoded}"/>
<tags:createField property="scqDate" component="scq"/>
<tags:createField property="scqName" component="scq"/>
<tags:createField property="scq1" component="scq"/>
<tags:createField property="scq2" component="scq"/>
<tags:createField property="scq3" component="scq"/>
<tags:createField property="scq4" component="scq"/>
<tags:createField property="scq5" component="scq"/>
<tags:createField property="scq6" component="scq"/>
<tags:createField property="scq7" component="scq"/>
<tags:createField property="scq8" component="scq"/>
<tags:createField property="scq9" component="scq"/>
<tags:createField property="scq10" component="scq"/>
<tags:createField property="scq11" component="scq"/>
<tags:createField property="scq12" component="scq"/>
<tags:createField property="scq13" component="scq"/>
<tags:createField property="scq14" component="scq"/>
<tags:createField property="scq15" component="scq"/>
<tags:createField property="scq16" component="scq"/>
<tags:createField property="scq17" component="scq"/>
<tags:createField property="scq18" component="scq"/>
<tags:createField property="scq19" component="scq"/>
<tags:createField property="scq20" component="scq"/>
<tags:createField property="scq21" component="scq"/>
<tags:createField property="scq22" component="scq"/>
<tags:createField property="scq23" component="scq"/>
<tags:createField property="scq24" component="scq"/>
<tags:createField property="scq25" component="scq"/>
<tags:createField property="scq26" component="scq"/>
<tags:createField property="scq27" component="scq"/>
<tags:createField property="scq28" component="scq"/>
<tags:createField property="scq29" component="scq"/>
<tags:createField property="scq30" component="scq"/>
<tags:createField property="scq31" component="scq"/>
<tags:createField property="scq32" component="scq"/>
<tags:createField property="scq33" component="scq"/>
<tags:createField property="scq34" component="scq"/>
<tags:createField property="scq35" component="scq"/>
<tags:createField property="scq36" component="scq"/>
<tags:createField property="scq37" component="scq"/>
<tags:createField property="scq38" component="scq"/>
<tags:createField property="scq39" component="scq"/>
<tags:createField property="scq40" component="scq"/>
<tags:createField property="scqSocial" component="scq"/>
<tags:createField property="scqComm" component="scq"/>
<tags:createField property="scqPatterns" component="scq"/>
<tags:createField property="scqTotal" component="scq"/>
<tags:listField property="scqDate" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scqName" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq1" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq2" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq3" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq4" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq5" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq6" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq7" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq8" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq9" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq10" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq11" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq12" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq13" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq14" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq15" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq16" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq17" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq18" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq19" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq20" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq21" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq22" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq23" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq24" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq25" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq26" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq27" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq28" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq29" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq30" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq31" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq32" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq33" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq34" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq35" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq36" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq37" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq38" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq39" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scq40" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scqSocial" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scqComm" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scqPatterns" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
<tags:listField property="scqTotal" component="${component}" listIndex="${iterator.index}" entityType="scq"/>
buffer.append(UdsUploadUtils.formatField(getScqDate())).append(",");
buffer.append(UdsUploadUtils.formatField(getScqName())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq1())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq2())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq3())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq4())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq5())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq6())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq7())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq8())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq9())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq10())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq11())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq12())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq13())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq14())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq15())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq16())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq17())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq18())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq19())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq20())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq21())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq22())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq23())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq24())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq25())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq26())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq27())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq28())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq29())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq30())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq31())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq32())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq33())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq34())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq35())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq36())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq37())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq38())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq39())).append(",");
buffer.append(UdsUploadUtils.formatField(getScq40())).append(",");
buffer.append(UdsUploadUtils.formatField(getScqSocial())).append(",");
buffer.append(UdsUploadUtils.formatField(getScqComm())).append(",");
buffer.append(UdsUploadUtils.formatField(getScqPatterns())).append(",");
buffer.append(UdsUploadUtils.formatField(getScqTotal())).append(",");
