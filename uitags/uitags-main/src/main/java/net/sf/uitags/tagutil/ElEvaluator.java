/**
 * Nov 15, 2004
 *
 * Copyright 2004 uitags
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.uitags.tagutil;

import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 * Shields the rest of the taglib from and provides a simpler interface to
 * Apache's expression evaluator.
 *
 * @author jonni
 * @version $Id$
 */
public final class ElEvaluator {
  /**
   * The tag whose attribute values are to be evaluated.
   */
  private Tag tag;
  /**
   * The tag's page context.
   */
  private PageContext pageContext;

  /**
   * Creates an evaluator, suppling information about the tag it will be
   * woking for.
   *
   * @param tag         the tag whose attribute values are to be evaluated
   * @param pageContext the tag's page context
   */
  public ElEvaluator(Tag tag, PageContext pageContext) {
    this.tag = tag;
    this.pageContext = pageContext;
  }

  /**
   * Evaluates the expression <code>expr</code> to produce an object of
   * the <code>expected</code> type.
   *
   * @param attrName the name of the attribute being evaluated
   * @param expr     the expression to evaluate
   * @param expected the expected type of the evaluation result
   * @return evaluation result or <code>null</code> if <code>expr</code> is
   *     <code>null</code>
   * @throws RuntimeException if failed to perform evaluation
   */
  public Object toObject(String attrName, String expr, Class expected) {
    try {
      return (expr == null) ? null :
          ExpressionEvaluatorManager.evaluate(
          attrName, expr, expected, this.tag, this.pageContext);
    }
    catch (JspException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Evaluates the expression <code>expr</code> to produce a
   * <code>String</code>.
   *
   * @param attrName the name of the attribute being evaluated
   * @param expr     the expression to evaluate
   * @return evaluation result or <code>null</code> if <code>expr</code> is
   *     <code>null</code>
   * @throws RuntimeException if failed to perform evaluation
   */
  public String toString(String attrName, String expr) {
    try {
      return (expr == null) ? null :
          (String) ExpressionEvaluatorManager.evaluate(
          attrName, expr, String.class, this.tag, this.pageContext);
    }
    catch (JspException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Evaluates the expression <code>expr</code> to produce an
   * <code>Integer</code>.
   *
   * @param attrName the name of the attribute being evaluated
   * @param expr     the expression to evaluate
   * @return evaluation result or <code>null</code> if <code>expr</code> is
   *     <code>null</code>
   * @throws RuntimeException if failed to perform evaluation
   */
  public Integer toIntegerObject(String attrName, String expr) {
    try {
      return (expr == null) ? null :
          (Integer) ExpressionEvaluatorManager.evaluate(
          attrName, expr, Integer.class, this.tag, this.pageContext);
    }
    catch (JspException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Evaluates the expression <code>expr</code> to produce an
   * <code>int</code>.
   *
   * @param attrName the name of the attribute being evaluated
   * @param expr     the expression to evaluate
   * @return evaluation result
   */
  public int toIntegerValue(String attrName, String expr) {
    return toIntegerObject(attrName, expr).intValue();
  }

  /**
   * Evaluates the expression <code>expr</code> to produce a
   * <code>Boolean</code>.
   *
   * @param attrName the name of the attribute being evaluated
   * @param expr     the expression to evaluate
   * @return evaluation result or <code>null</code> if <code>expr</code> is
   *     <code>null</code>
   * @throws RuntimeException if failed to perform evaluation
   */
  public Boolean toBooleanObject(String attrName, String expr) {
    try {
      return (expr == null) ? null :
          (Boolean) ExpressionEvaluatorManager.evaluate(
          attrName, expr, Boolean.class, this.tag, this.pageContext);
    }
    catch (JspException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Evaluates the expression <code>expr</code> to produce an
   * <code>boolean</code>.
   *
   * @param attrName the name of the attribute being evaluated
   * @param expr     the expression to evaluate
   * @return evaluation result
   */
  public boolean toBooleanValue(String attrName, String expr) {
    return toBooleanObject(attrName, expr).booleanValue();
  }

  /**
   * Evaluates the expression <code>expr</code> to produce a
   * <code>Collection</code>.
   *
   * @param attrName the name of the attribute being evaluated
   * @param expr     the expression to evaluate
   * @return evaluation result or <code>null</code> if <code>expr</code> is
   *     <code>null</code>
   * @throws RuntimeException if failed to perform evaluation
   */
  public Collection toCollection(String attrName, String expr) {
    try {
      return (expr == null) ? null :
          (Collection) ExpressionEvaluatorManager.evaluate(
          attrName, expr, Collection.class, this.tag, this.pageContext);
    }
    catch (JspException e) {
      throw new RuntimeException(e);
    }
  }
}