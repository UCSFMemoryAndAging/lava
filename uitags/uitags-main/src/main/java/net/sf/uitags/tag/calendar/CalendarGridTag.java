package net.sf.uitags.tag.calendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import net.sf.uitags.tag.AbstractUiTag;
import net.sf.uitags.tagutil.AttributeSupport;
import net.sf.uitags.tagutil.AttributeSupportHelper;
import net.sf.uitags.tagutil.ScopedIdGenerator;
import net.sf.uitags.tagutil.TaglibProperties;
import net.sf.uitags.tagutil.i18n.MessageFinder;
import net.sf.uitags.tagutil.i18n.MessageFinderFactory;
import net.sf.uitags.util.ArrayUtils;

public class CalendarGridTag extends AbstractUiTag implements AttributeSupport {
  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 100L;
  /**
   * Key of the scoped attribute that stores auto-incremented ID for
   * instances of this tag handler.
   */
  private static final String TAG_INSTANCE_ID_KEY =
      "net.sf.uitags.tag.calendar.CalendarGridTag.instanceId";


  ///////////////////////////////////////////////
  ////////// Property keys (constants) //////////
  ///////////////////////////////////////////////

  private static final String PROP_CSS_CLASS =
      "calendar.calendarGrid.class";

  private static final String PROP_WEEK_LABELS =
      "calendar.calendarGrid.weekLabels";

  private static final String PROP_CLASS_RESOLVER =
      "calendar.calendarGrid.classResolver";


  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * The 'class' tag attribute.
   */
  private String cssClass;
  /**
   * The 'weekLabels' tag attribute.
   */
  private String[] weekLabels;
  /**
   * The 'classResolver' tag attribute.
   */
  private String classResolver;
  /**
   * The 'actionResolver' tag attribute.
   */
  private String actionResolver;
  /**
   * Helper for tag that allows arbitrary HTML attributes.
   */
  private AttributeSupportHelper attributeHelper;


  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////

  public void setId(String val) {
    super.setId(val);
  }

  public void setClass(String val) {
    this.cssClass = val;
  }

  public void setWeekLabels(String[] val) {
    this.weekLabels = val;
  }

  public void setClassResolver(String val) {
    this.classResolver = val;
  }

  public void setActionResolver(String val) {
    this.actionResolver = val;
  }


  ///////////////////////////////
  ////////// Tag logic //////////
  ///////////////////////////////

  public int doStartTag() throws JspException {
    this.attributeHelper = new AttributeSupportHelper();

    return EVAL_BODY_INCLUDE;
  }

  public int doEndTag() throws JspException {
    CalendarTag parentCalendar = (CalendarTag) findParent(CalendarTag.class);
    parentCalendar.setCalendarGrid(this);

    return EVAL_PAGE;
  }

  private String generateUniqueId() {
    return "uiCalendar_calendarGrid" + ScopedIdGenerator.nextId(
        PageContext.REQUEST_SCOPE, TAG_INSTANCE_ID_KEY, this.pageContext);
  }

  /** {@inheritDoc} */
  public void addAttribute(String attrName, String attrValue) {
    this.attributeHelper.addAttribute(attrName, attrValue);
  }


  //////////////////////////////////////////////////////////
  ////////// To be used by the parent CalendarTag //////////
  //////////////////////////////////////////////////////////

  /*
   * This method is defined as public by the superclass so we can't narrow
   * the visibility to package-private.
   */
  public String getId() {
    if (super.getId() == null) {
      super.setId(generateUniqueId());
    }
    return super.getId();
  }

  String getCssClass() {
    TaglibProperties props = TaglibProperties.getInstance();
    props.setRuntimeProperty(PROP_CSS_CLASS, this.cssClass);

    return props.get(PROP_CSS_CLASS);
  }

  String[] getWeekLabels() {
    if (this.weekLabels == null) {
      TaglibProperties props = TaglibProperties.getInstance();
      MessageFinder messageFinder =
          MessageFinderFactory.getInstance(this.pageContext);
      this.weekLabels =
          ArrayUtils.toArray(messageFinder.get(props.get(PROP_WEEK_LABELS)));
    }

    return this.weekLabels;
  }

  String getClassResolver() {
    TaglibProperties props = TaglibProperties.getInstance();
    props.setRuntimeProperty(PROP_CLASS_RESOLVER, this.classResolver);

    return props.get(PROP_CLASS_RESOLVER);
  }

  String getActionResolver() {
    return this.actionResolver;
  }

  String getCustomHtmlAttributes() {
    return this.attributeHelper.eval();
  }
}
