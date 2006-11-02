/**
 * Nov 23, 2004
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

/**
 * Simple bean for grouping together a pair of <code>Objects</code>.
 *
 * @author hgani
 * @version $Id$
 */
public final class ObjectPair {
  /**
   * The first object in the pair.
   */
  private Object first;
  /**
   * The second object in the pair.
   */
  private Object second;

  /**
   * Creates a new pair instance.
   *
   * @param first the first object
   * @param second the second second
   */
  public ObjectPair(Object first, Object second) {
    super();
    this.first = first;
    this.second = second;
  }

  /**
   * Returns the first object.
   *
   * @return the first
   */
  public Object getFirstObject() {
    return this.first;
  }

  /**
   * Returns the second object.
   *
   * @return the second
   */
  public Object getSecondObject() {
    return this.second;
  }

  /** {@inheritDoc} */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof ObjectPair)) {
      return false;
    }

    ObjectPair temp = (ObjectPair) obj;

    // since this class represents a generic pair of objects, both objects
    // are taken into account
    return
        (this.first == null ?
            temp.first == null : this.first.equals(temp.first)) &&
        (this.second == null ?
            temp.second == null : this.second.equals(temp.second));
  }

  /** {@inheritDoc} */
  public int hashCode() {
    return
        (this.first == null ? 0 : this.first.hashCode()) ^
        (this.second == null ? 0 : this.second.hashCode());
  }
}
