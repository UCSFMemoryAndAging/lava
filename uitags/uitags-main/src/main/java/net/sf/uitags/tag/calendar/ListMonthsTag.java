package net.sf.uitags.tag.calendar;

import javax.servlet.jsp.JspException;

import net.sf.uitags.tag.AbstractUiTag;
import net.sf.uitags.tagutil.TaglibProperties;
import net.sf.uitags.tagutil.i18n.MessageFinder;
import net.sf.uitags.tagutil.i18n.MessageFinderFactory;
import net.sf.uitags.tagutil.validation.RuntimeValidator;
import net.sf.uitags.util.ArrayUtils;
import net.sf.uitags.util.Template;

public class ListMonthsTag extends AbstractUiTag {
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

  private static final String PROP_MONTH_LABELS =
      "calendar.listMonths.monthLabels";


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
   * The 'monthLabels' tag attribute.
   */
  private String[] monthLabels;


  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////

  public void setInjectTo(String val) {
    this.injectTo = val;
  }

  public void setInjectToName(String val) {
    this.injectToName = val;
  }

  public void setMonthLabels(String[] val) {
    this.monthLabels = val;
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

    if (this.monthLabels == null) {
      TaglibProperties props = TaglibProperties.getInstance();
      MessageFinder messageFinder =
          MessageFinderFactory.getInstance(this.pageContext);
      this.monthLabels =
          ArrayUtils.toArray(messageFinder.get(props.get(PROP_MONTH_LABELS)));
    }

    Template tpl = Template.forName(Template.CALENDAR_LIST_MONTHS);
    tpl.map("listerId", this.injectTo);
    tpl.map("listerName", this.injectToName);
    tpl.map("monthLabels", this.monthLabels);

    CalendarTag parent = (CalendarTag) findParent(CalendarTag.class);
    parent.addChildJsCode(tpl.evalToString());

    return EVAL_PAGE;
  }
}
