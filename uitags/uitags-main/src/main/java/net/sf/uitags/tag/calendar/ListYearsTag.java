package net.sf.uitags.tag.calendar;

import javax.servlet.jsp.JspException;

import net.sf.uitags.tag.AbstractUiTag;
import net.sf.uitags.tagutil.TaglibProperties;
import net.sf.uitags.tagutil.validation.RuntimeValidator;
import net.sf.uitags.util.Template;

public class ListYearsTag extends AbstractUiTag {
  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 100L;


  ///////////////////////////////////////////////
  ////////// Property keys (constants) //////////
  ///////////////////////////////////////////////

  private static final String PROP_LISTING_STRATEGY =
    "calendar.listYears.yearListObtainer";


  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * The 'injectTo' tag attribute.
   */
  private String injectTo;
  /**
   * The 'injectToName' tag attribute.
   */
  private String injectToName;
  /**
   * The 'yearListObtainer' tag attribute.
   */
  private String yearListObtainer;


  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////

  public void setInjectTo(String val) {
    this.injectTo = val;
  }

  public void setInjectToName(String val) {
    this.injectToName = val;
  }

  public void setYearListObtainer(String val) {
    this.yearListObtainer = val;
  }


  ///////////////////////////////
  ////////// Tag logic //////////
  ///////////////////////////////

  public int doStartTag() throws JspException {
    return SKIP_BODY;
  }

  public int doEndTag() throws JspException {
    RuntimeValidator.assertAttributeExclusive(
        "injectTo", this.injectTo, "injectToName", this.injectToName);
    RuntimeValidator.assertEitherSpecified(
        "injectTo", this.injectTo, "injectToName", this.injectToName);

    TaglibProperties props = TaglibProperties.getInstance();
    props.setRuntimeProperty(PROP_LISTING_STRATEGY, this.yearListObtainer);

    Template tpl = Template.forName(Template.CALENDAR_LIST_YEARS);
    tpl.map("listerId", this.injectTo);
    tpl.map("listerName", this.injectToName);
    tpl.map("yearListObtainer", props.get(PROP_LISTING_STRATEGY));

    CalendarTag parent = (CalendarTag) findParent(CalendarTag.class);
    parent.addChildJsCode(tpl.evalToString());

    return EVAL_PAGE;
  }
}