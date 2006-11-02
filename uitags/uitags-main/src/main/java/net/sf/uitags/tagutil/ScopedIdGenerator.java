/**
 * Nov 24, 2004
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

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.jsp.PageContext;

/**
 * Generates a <code>long</code> value which is guaranteed to be unique for a
 * given combination of key and scope. This is useful when a counter
 * is needed for assigning unique names to generated HTML/JavaScript
 * entities.
 *
 * @author jonni
 * @version $Id$
 */
public final class ScopedIdGenerator {
  /**
   * Non-instantiable by client.
   */
  private ScopedIdGenerator() {
    super();
  }

  /**
   * Generates a unique <code>long</code> value for a given combination
   * of key and scope only if the <i>groupId</i> is unique. Otherwise,
   * the previously generated value that corresponds to that particular
   * <i>groupId</i> is returned. A <code>null</code> <i>groupId</i> means
   * that the ID is not grouped,
   *
   * @param scope the scope of the ID
   * @param key the key (aka name) of the ID
   * @param pageContext provides access to the different scopes
   * @param groupId the group ID
   * @return a unique ID value
   * @throws IllegalArgumentException if <code>scope</code> value is illegal
   * @throws NullPointerException if <code>key</code> is <code>null</code>
   * @throws IllegalStateException if trying to access a non-existant
   *     session
   */
  public static long nextId(
      int scope, String key, PageContext pageContext, String groupId) {
    // Check the arguments
    if (scope != PageContext.PAGE_SCOPE &&
        scope != PageContext.REQUEST_SCOPE &&
        scope != PageContext.SESSION_SCOPE &&
        scope != PageContext.APPLICATION_SCOPE) {
      throw new IllegalArgumentException(
          "Scope value must be one of the constants declared in PageContext.");
    }
    if (key == null) {
      throw new NullPointerException("key can't be null.");
    }

    // If scope is page or request, there's no possibility of concurrent
    // access, simply call the worker method.
    if (scope == PageContext.PAGE_SCOPE || scope == PageContext.REQUEST_SCOPE) {
      return nextIdVal(scope, key, pageContext, groupId);
    }
    // If scope if session or application, value may be accessed concurrently,
    // use synchronization.
    else {
      synchronized (ScopedIdGenerator.class) {
        return nextIdVal(scope, key, pageContext, groupId);
      }
    }
  }

  /**
   * Generates a <code>long</code> value which is guaranteed to be unique for a
   * given combination of key and scope.
   *
   * @param scope the scope of the ID
   * @param key the key (aka name) of the ID
   * @param pageContext provides access to the different scopes
   * @return a unique ID value
   * @throws IllegalArgumentException if <code>scope</code> value is illegal
   * @throws NullPointerException if <code>key</code> is <code>null</code>
   * @throws IllegalStateException if trying to access a non-existant
   *     session
   */
  public static long nextId(int scope, String key, PageContext pageContext) {
    return nextId(scope, key, pageContext, null);
  }

  /**
   * The worker method that does the actual work of ID generation. It doesn't
   * support multi-threading; the caller has to synchronize the method call
   * when appropriate.
   * <p>
   * A <code>null</code> <i>groupId</i> means that the ID is not grouped,
   * therefore the returned ID will always be unique. When <i>groupId</i> is
   * specified, the returned ID is unique only if the <i>groupId</i> is unique.
   *
   * @see #nextId(int, String, PageContext, String)
   * @see #nextIdVal(int, String, PageContext)
   *
   * @param scope the scope of the ID
   * @param key the key (aka name) of the ID
   * @param pageContext provides access to the different scopes
   * @param groupId the group ID
   * @return a unique ID value
   * @throws IllegalStateException when aborting to avoid overwriting someone
   *     else's scoped attribute or if trying to access a non-existant
   *     session
   */
  private static long nextIdVal(
      int scope, String key, PageContext pageContext, String groupId) {
    //return nextIdVal(scope, key, pageContext, null);
    // Read the current value of the unique ID
    Id currId;
    try {
      currId = (Id) pageContext.getAttribute(key, scope);
    }
    catch (ClassCastException e) {
      throw new IllegalStateException(
          "Cannot create an ID named '" + key + "' in scope '" + scope + "'." +
          " Another object is occupying that spot.");
    }

    // Below we'll increment the ID value and put it back to the scope.
    // The risk of accidentally overwriting someone else's scoped attribute
    // has been eliminated by the cast above. A ClassCastException would've
    // been thrown if a scoped attribute whose type is not ScopedIdGenerator.Id
    // already exists under the same name. This works assuming the caller of
    // this method does synchronization correctly.

    long newId = 1;
    if (currId == null) {
      currId = new Id();
      pageContext.setAttribute(key, currId, scope);
    }
    else if (currId.contains(groupId)) {
      return currId.getValue(groupId);
    }
    else {
      newId = currId.getCurrentValue() + 1;
    }

    currId.addValue(groupId, newId);
    return newId;
  }

  /**
   * Holds the ID value that is stored in one of the scopes. This special
   * type is needed to eliminate the risk of overwriting someone else's scoped
   * attribute.
   */
  private static final class Id implements Serializable {
    private static final long serialVersionUID = 100L;

    /**
     * The ID value to hold
     */
    private long currentValue;

    /**
     * Unique ID values keyed on group IDs.
     */
    private HashMap valueMap;

    /**
     * Creates and initializes this object.
     */
    private Id() {
      this.valueMap = new HashMap();
    }

    /**
     * Adds a value along with its group.
     *
     * @param value the ID value to hold
     * @param groupId the group ID
     */
    private void addValue(String groupId, long value) {
      this.valueMap.put(groupId, new Long(value));
      this.currentValue = value;
    }

    /**
     * Checks whether a group ID exists.
     *
     * @param groupId the group ID
     * @return true if the group exists, false otherwise
     */
    private boolean contains(String groupId) {
      // groupId == null means no group ID was specified
      if (groupId == null) {
        return false;
      }
      return this.valueMap.containsKey(groupId);
    }

    /**
     * Returns the ID value corresponding to the specified group ID.
     *
     * @param groupId the group ID
     * @return the ID value
     */
    private long getValue(String groupId) {
      // groupId == null means no group ID was specified
      if (groupId == null) {
        return this.currentValue;
      }
      return ((Long) this.valueMap.get(groupId)).longValue();
    }

    /**
     * Returns the current ID value.
     *
     * @return the ID value
     */
    private long getCurrentValue() {
      return this.currentValue;
    }
  }
}
