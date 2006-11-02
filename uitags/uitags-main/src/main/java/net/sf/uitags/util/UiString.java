/**
 * Nov 14, 2004
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
package net.sf.uitags.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.map.ListOrderedMap;

/**
 * Generates <code>String</code> by substituting placeholders within a
 * <i>template</i> string with named/positional parameters.
 *
 * @author hgani
 * @version $Id$
 */
public final class UiString {
  /**
   * Generates normal string.
   */
  public static final int OPTION_NONE = 0;
  /**
   * Surround parameters with double quotes.
   */
  public static final int OPTION_AUTO_SURROUND = 1;
  /**
   * Encode parameters for use within <i>HTML</i> code.
   */
  public static final int OPTION_HTML_ENCODING = 2;
  /**
   * Escape parameters' <i>Javascript</i> special characters.
   */
  public static final int OPTION_JS_ESCAPE = 4;
  /**
   * Process parameters with all options turned on.
   */
  public static final int OPTION_ALL =
      OPTION_AUTO_SURROUND | OPTION_HTML_ENCODING | OPTION_JS_ESCAPE;

  /**
   * Named parameters for the string generation.
   */
  private OrderedMap pairs;
  /**
   * Positional parameters for the string generation.
   */
  private ArrayList params;
  /**
   * The formatting template.
   */
  private String template;
  /**
   * String generation options.
   */
  private int options;
  /**
   * String that surrounds each parameter, when OPTION_AUTO_SURROUND is set.
   */
  private String surroundString = "\"";

  /**
   * Creates a "string constructor" object with default options.
   *
   * @param template the template
   */
  public UiString(String template) {
    this(template, OPTION_NONE);
  }

  /**
   * Constructs a "string constructor" object with options that will be
   * applied to every paremeter it processes.
   *
   * @param template the template
   * @param options the default options, can be combined using bitwise OR.
   */
  public UiString(String template, int options) {
    this.template = template;
    this.params = new ArrayList();
    this.pairs = new ListOrderedMap();
    setDefaultOptions(options);
  }

  /**
   * Sets the default options for the <code>String</code> construction.
   *
   * @param opts the default options, can be combined using bitwise OR.
   */
  public void setDefaultOptions(int opts) {
    if (opts < OPTION_NONE) {
      throw new IllegalArgumentException("Invalid options value.");
    }
    if (opts > OPTION_ALL) {
      throw new IllegalArgumentException("Invalid options value.");
    }
    this.options = opts;
  }

  /**
   * See {@link #OPTION_NONE}.
   */
  public void clearOptions() {
    setDefaultOptions(OPTION_NONE);
  }

  /**
   * See {@link #OPTION_AUTO_SURROUND}.
   */
  public void autoSurround() {
    setDefaultOptions(this.options | OPTION_AUTO_SURROUND);
  }

  /**
   * See {@link #OPTION_HTML_ENCODING}.
   */
  public void htmlEncode() {
    setDefaultOptions(this.options | OPTION_HTML_ENCODING);
  }

  /**
   * See {@link #OPTION_JS_ESCAPE}.
   */
  public void jsEscape() {
    setDefaultOptions(this.options | OPTION_JS_ESCAPE);
  }

  /**
   * Sets the <code>String</code> to surround each parameter. This is
   * useful only when OPTION_AUTO_SURROUND is set.
   *
   * @param str the <code>String</code>
   */
  public void setSurroundString(String str) {
    this.surroundString = str;
  }

  /**
   * Binds a positional parameter. Its position depends on the order of
   * the bindings. It is relative to the number of parameters that have
   * been bound before the current binding.
   *
   * @param param the parameter
   */
  public void bind(Object param) {
    bind(param, this.options);
  }

  /**
   * Binds a positional parameter with options.
   *
   * @see #bind(Object)
   * @param param the parameter
   * @param opts the options
   */
  public void bind(Object param, int opts) {
    if (isMapped()) {
      throw new IllegalStateException("Bind cannot be used along with map.");
    }
    this.params.add(process(param, opts));
  }

  /**
   * Maps a parameter to a key or name.
   *
   * @param key the key or name
   * @param value the variable or object
   */
  public void map(String key, Object value) {
    map(key, value, this.options);
  }

  /**
   * Maps a parameter to a key or name, specifying the string generation
   * options.
   *
   * @param key the key or name
   * @param value the variable or object
   * @param opts the options
   */
  public void map(String key, Object value, int opts) {
    if (isBound()) {
      throw new IllegalStateException("Map cannot be used along with bind.");
    }
    this.pairs.put(key, process(value, opts));
  }

  /**
   * Checks whether the parameters are named (previously set using
   * <code>map</code> methods).
   *
   * @return true if it is, false otherwise
   */
  private boolean isMapped() {
    return this.pairs.size() != 0;
  }

  /**
   * Checks whether the parameters are positional (previously set using
   * <code>bind</code> methods).
   *
   * @return true if it is, false otherwise
   */
  private boolean isBound() {
    return this.params.size() != 0;
  }

  /**
   * Processes parameter according to the specified options.
   *
   * @param obj the parameter
   * @param opts the options
   * @return the processed string
   */
  private String process(Object obj, int opts) {
    String temp = String.valueOf(obj);
    if (isSet(opts, OPTION_HTML_ENCODING)) {
      temp = encodeHtml(temp);
    }

    if (isSet(opts, OPTION_JS_ESCAPE)) {
      temp = escapeJs(temp);
    }

    if (isSet(opts, OPTION_AUTO_SURROUND)) {
      temp = this.surroundString + temp + this.surroundString;
    }

    return temp;
  }

  /**
   * Clears all parameters.
   */
  public void clearBindParameters() {
    this.params.clear();
  }

  /**
   * Checks whether a specific option is set.
   *
   * @param opts the specified options combination
   * @param specific a certain option to test with
   * @return true if the <code>specific</code> option is in the
   *     <code>options</code> combination
   */
  private boolean isSet(int opts, int specific) {
    return (opts & specific) != 0;
  }

  /**
   * Encodes string for safe use within <i>HTML</i> code.
   *
   * @param str the string to encode
   * @return the encoded string
   */
  private String encodeHtml(String str) {
    StringBuffer buffer = new StringBuffer();
    char curr;
    for (int i = 0; i < str.length(); i++) {
      curr = str.charAt(i);
      switch (curr) {
        case '<':
          buffer.append("&lt;");
          break;
        case '>':
          buffer.append("&gt;");
          break;
        case '&':
          buffer.append("&amp;");
          break;
        case ' ':
          buffer.append("&nbsp;");
          break;
        case '"':
          buffer.append("&quot;");
          break;
        case '\'':
          buffer.append("&#039;");
          break;
        default:
          buffer.append(curr);
      }
    }
    return String.valueOf(buffer);
  }

  /**
   * Escape <i>Javascript</i> special characters. Currently this does the
   * exactly the same thing as {@link #escapeJava(String)}.
   *
   * @param str the string to encode
   * @return the encoded string
   */
  private String escapeJs(String str) {
    return escapeJava(str);
  }

  /**
   * Escape <i>Java</i> special characters.
   *
   * @param str the string to encode
   * @return the encoded string
   */
  private String escapeJava(String str) {
    StringBuffer buffer = new StringBuffer();
    char curr;
    for (int i = 0; i < str.length(); i++) {
      curr = str.charAt(i);
      switch (curr) {
        case '\'':
          buffer.append("\\\'");
          break;
        case '"':
          buffer.append("\\\"");
          break;
        case '\\':
          buffer.append("\\\\");
          break;
        default:
          buffer.append(curr);
      }
    }
    return String.valueOf(buffer);
  }

  /**
   * Constructs a formatted string from template and specified parameters.
   * Because we cannot always determine that a parameter has at least
   * matching holder, for consistency reason, we do not perform validation
   * for non-matching parameters/holders (no matter whether <i>mapping</i>
   * technique or <i>binding</i> technique is used).
   * <p>
   * When the mapped String itself is of form <code>{key}</code>, where
   * <code>key</code> is the name of the subsequent holder, the expected
   * behaviour, is that when the subsequent holder is parsed, the holder
   * will be replaced with its mapped String.
   * For example:
   * Template: "{a} and {b}"
   * If, {a} map to {b}
   * If, {b} map to bValue
   * Constructed String: "bValue and bValue"
   * <p>
   * So, if the intended mapped String is "{b}" instead of referring to
   * another holder, the braces should be escaped with <code>\</code>.
   * For example:
   * Template: "{a} and {b}"
   * If, {a} map to \{b\}
   * If, {b} map to bValue
   * Constructed String: "{b} and bValue"
   * <p>
   * It is strongly recommended to always avoid having a holder-like mapped
   * String, because the constructed String really depends on the order
   * of the mapping specified by the programmer.
   * For example:
   * Template: "{a} and {b} and {c}"
   * Assumming that the mapping was done in the following order:
   * If, {c} map to {b}
   * If, {b} map to bValue
   * If, {a} map to {b}
   * Constructed String: "{b} and bValue and bValue"
   *
   * @return the formatted string
   */
  public String construct() {
    if (isBound()) {
      try {
        return simpleConstruct(this.template,
            (String[]) this.params.toArray(new String[this.params.size()]));
      }
      catch (IllegalArgumentException e) {
        throw new IllegalStateException(e.getMessage());
      }
    }

    String result = this.template;
    //Map.Entry entry;
    //Iterator iter = this.pairs.entrySet().iterator();
    Iterator iter = this.pairs.orderedMapIterator();
    while (iter.hasNext()) {
      //entry = (Map.Entry) iter.next();
      String key = (String) iter.next();

      // we need to escape the slashes twice (for both the pattern
      // and the replacement), because they are part of the
      // regular expression
      result = result.replaceAll("\\{" + key + "\\}",
          escapeJava(String.valueOf(this.pairs.get(key))));
    }
    result = result.replaceAll("\\\\\\{", "\\{");
    result = result.replaceAll("\\\\\\}", "\\}");

    return result;
  }

  /**
   * Constructs a formatted string without having to instantiate this class.
   * If the number bound parameters and the number of place holders do not
   * matched, any parameters/holders that do not have corresponding matchings
   * will be ignored.
   *
   * @see #construct()
   * @param template the format template
   * @param params the positional parameters array
   * @return the formatted string
   * @throws NumberFormatException if invalid parameter position number
   *     encountered
   * @throws IllegalArgumentException if there is no corresponding
   *     positional parameter
   */
  public static String simpleConstruct(String template, String[] params) {
    int currIndex = 0;
    int openIndex = 0;
    int closeIndex = 0;
    int listIndex = 0;
    StringBuffer result = new StringBuffer();
    boolean loop = true;

    // any "{num}" pattern will be referred as node
    while (true) {
      openIndex = template.indexOf("{", currIndex);
      if (openIndex < 0) {
        loop = false;
      }
      else {
        closeIndex = template.indexOf("}", openIndex);
        if (closeIndex < 0) {
          loop = false;
        }
      }
      if (!loop) {
        result.append(template.substring(currIndex, template.length()));
        break;
      }
      result.append(template.substring(currIndex, openIndex));
      try {
        listIndex = Integer.parseInt(
            template.substring(openIndex + 1, closeIndex));
        if (listIndex < 0) {  // not a valid node, ignore the right brace
          throw new NumberFormatException(
              "Invalid parameter position index '" + listIndex + "'.");
        }
        if (listIndex >= params.length) {  // just ignore
          // put back the holder
          String holder = "{" + listIndex + "}";
          result.append(holder);
          currIndex = openIndex + holder.length();
        }
        else {
          result.append(String.valueOf(params[listIndex]));
          // having processed the current node, we can be certain that
          // the next node is after the current right brace
          currIndex = closeIndex + 1;
        }
      }
      catch (NumberFormatException x) {  // not a valid node
        result.append("{");
        // nothing else to do, find the next node
        // start next search from current open index, because there
        // may be another left brace between the current left brace
        // and the current right brace
        currIndex = openIndex + 1;
      }
    }
    return result.toString();
  }
}