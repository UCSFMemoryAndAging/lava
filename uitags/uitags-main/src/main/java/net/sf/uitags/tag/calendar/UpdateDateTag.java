package net.sf.uitags.tag.calendar;

import javax.servlet.jsp.JspException;

import net.sf.uitags.tag.AbstractUiTag;
import net.sf.uitags.tagutil.TaglibProperties;
import net.sf.uitags.tagutil.validation.RuntimeValidator;
import net.sf.uitags.util.Template;

public class UpdateDateTag extends AbstractUiTag {
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

  private static final String PROP_EVENT =
      "calendar.updateDate.event";

  private static final String PROP_DATE_OBTAINER =
      "calendar.updateDate.dateObtainer";


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
   * The 'event' tag attribute.
   */
  private String eventName;
  /**
   * The 'dateObtainer' tag attribute.
   */
  private String dateObtainer;


  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////

  public void setInjectTo(String val) {
    this.injectTo = val;
  }

  public void setInjectToName(String val) {
    this.injectToName = val;
  }

  public void setEvent(String val) {
    this.eventName = val;
  }

  public void setDateObtainer(String val) {
    this.dateObtainer = val;
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
    props.setRuntimeProperty(PROP_EVENT, this.eventName);
    props.setRuntimeProperty(PROP_DATE_OBTAINER, this.dateObtainer);

    Template tpl = Template.forName(Template.CALENDAR_UPDATE_DATE);
    tpl.map("triggerId", this.injectTo);
    tpl.map("triggerName", this.injectToName);
    tpl.map("triggerEvent", props.get(PROP_EVENT));
    tpl.map("dateObtainer", props.get(PROP_DATE_OBTAINER));

    CalendarTag parent = (CalendarTag) findParent(CalendarTag.class);
    parent.addChildJsCode(tpl.evalToString());

    return EVAL_PAGE;
  }
}
