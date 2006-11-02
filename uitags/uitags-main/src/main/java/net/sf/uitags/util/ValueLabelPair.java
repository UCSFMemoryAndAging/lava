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

import java.util.Map;

/**
 * Simple bean for grouping together a <i>value</i> and its <i>label</i>.
 *
 * @author jonni
 * @author hgani
 * @version $Id$
 */
public final class ValueLabelPair implements Map.Entry {
  /**
   * Value attribute.
   */
  private Object value;
  /**
   * Label attribute.
   */
  private Object label;

  /**
   * Creates a new instance.
   *
   * @param value the value
   * @param label the associated label
   */
  public ValueLabelPair(Object value, Object label) {
    super();
    if (value == null) {
      throw new NullPointerException("Value cannot be null");
    }
    this.value = value;
    this.label = label;
  }

  /**
   * Sets the value (Map.Entry interface).
   *
   * @param value the new value
   * @return the new value
   */
  public Object setValue(Object value) {
    this.value = value;
    return this.value;
  }

  /**
   * Returns the value (Map.Entry interface).
   *
   * @return the value
   */
  public Object getKey() {
    return this.value;
  }

  /**
   * Returns the label (Map.Entry interface).
   *
   * @return the label
   */
  public Object getValue() {
    return this.label;
  }

  /**
   * Returns the value as <code>String</code>.
   * If the value is <code>null</code>, the string "null" will
   * be returned instead.
   *
   * @return the value as <code>String</code>
   */
  public String getValueAsString() {
    return String.valueOf(this.value);
  }

  /**
   * Returns the label as <code>String</code>.
   * If the label is <code>null</code>, the string "null" will
   * be returned instead.
   *
   * @return the label as <code>String</code>
   */
  public String getLabelAsString() {
    return String.valueOf(this.label);
  }

  /** {@inheritDoc} */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof ValueLabelPair)) {
      return false;
    }

    ValueLabelPair temp = (ValueLabelPair) obj;
    return (this.value == null ?
        temp.value == null : this.value.equals(temp.value));
  }

  /** {@inheritDoc} */
  public int hashCode() {
    int hashCode = (this.value == null ? 0 : this.value.hashCode());
    return hashCode;
  }
}
