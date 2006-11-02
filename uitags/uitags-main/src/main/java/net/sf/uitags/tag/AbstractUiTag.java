package net.sf.uitags.tag;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sf.uitags.tagutil.validation.DeferredValidationException;

/**
 * Parent of all tag handlers in uitags.
 *
 * @author jonni
 */
public abstract class AbstractUiTag extends BodyTagSupport {
  private static final String PARENT_LIST_KEY =
      AbstractUiTag.class.getName() + "parentList";

  private List visibleParentList;

  public void setPageContext(PageContext pageContext) {
    super.setPageContext(pageContext);

    this.visibleParentList = (List) this.pageContext.getAttribute(
        PARENT_LIST_KEY, PageContext.REQUEST_SCOPE);
    if (this.visibleParentList == null) {
      this.visibleParentList = new LinkedList();
      this.pageContext.setAttribute(PARENT_LIST_KEY,
          this.visibleParentList, PageContext.REQUEST_SCOPE);
    }
  }

  /**
   * Returns the tag name (the custom tag name used in JSP) of this tag handler.
   *
   * @return the tag name (the custom tag name used in JSP) of this tag handler
   */
  public String getTagName() {
    return resolveTagNameForClass(this.getClass());
  }

  /**
   * Given a tag handler <code>Class</code>, returns its <i>tag name</i>
   * (the custom tag name used in JSP).
   * This method assumes that tag handler classes are named according to its
   * tag name, suffixed with the word "Tag".
   *
   * @param tagClass the tag handler class whose tag name is to be resolved
   * @return the tag name. If the tag handler class name doesn't conform to
   *         the convention, returns the unqualified name of the supplied class.
   */
  private String resolveTagNameForClass(Class tagClass) {
    String className = getUnqualifiedClassName(tagClass);
    if (className.endsWith("Tag")) {
      String firstCharInLowerCase = className.substring(0, 1).toLowerCase();
      return firstCharInLowerCase + omitFirstCharAndLastThreeChars(className);
    }

    return className;
  }

  private String getUnqualifiedClassName(Class tagClass) {
    String qualifiedClassName = tagClass.getName();
    int cutOffPosition = qualifiedClassName.lastIndexOf('.');
    return qualifiedClassName.substring(cutOffPosition + 1);
  }

  private String omitFirstCharAndLastThreeChars(String target) {
    return target.substring(1, target.length() - 3);
  }

  /**
   * Puts this tag handler into the request scope, allowing nested children tags
   * to access it. This mechanism is used instead of the built-in
   * <code>TagSupport.findAncestorWithClass()</code> to allow tags
   * cooperate eventhough they are wrapped in different tag files.
   * A side effect of this mechanism is that an instance of a tag cannot
   * be nested under another instance of the same tag.
   * <p>
   * Typically a tag that wants to expose itself to the children calls
   * this method in <code>doStartTag()</code>. The clean-up method
   * ({@link #makeInvisibleFromChildren()}) is then called in
   * <code>doEndTag()</code> (must not forget to call this clean-up method).
   * Once the parent class has made itself visible to the children,
   * the children can call {@link #findParent(Class)} to find the parent.
   *
   * @throws DeferredValidationException if the attribute already exists in
   *                                     the request scope, which almost always
   *                                     means that this tag has been nested
   *                                     under another instance of the same tag
   */
  protected final void makeVisibleToChildren() {
    this.visibleParentList.add(this);
  }

  /**
   * See {@link #makeVisibleToChildren()}.
   */
  protected final void makeInvisibleFromChildren() {
    this.visibleParentList.remove(this);
  }

  /**
   * See {@link #makeVisibleToChildren()}.
   *
   * @param tagClass the class of the parent tag handler to find
   * @return the parent tag handler
   * @throws DeferredValidationException if the parent tag handler was not found
   */
  protected final AbstractUiTag findParent(Class tagClass) {
    // traverse in reverse direction because the closest parent is in the
    // newest entry
    int lastIndex = this.visibleParentList.size();
    for (ListIterator iter = this.visibleParentList.listIterator(lastIndex);
        iter.hasPrevious(); ) {
      Object potentialParent = iter.previous();
      if (tagClass.isInstance(potentialParent)) {
        return (AbstractUiTag) potentialParent;
      }
    }

    throw new DeferredValidationException("The parent tag '" +
        resolveTagNameForClass(tagClass) + "' cannot be found.");
  }

  /**
   * Convenience method for printing to the output stream that throws
   * <code>JspException</code> instead of <code>IOException</code>.
   *
   * @param toPrint the value to print
   * @throws JspException wrapping the original <code>IOException</code>
   */
  protected final void println(Object toPrint) throws JspException {
    try {
      this.pageContext.getOut().println(toPrint);
    }
    catch (IOException e) {
      throw new JspException(e);
    }
  }
}
