/**
o * Mar 25, 2005
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
 * An adapter class which makes possible things which Velocity can't usually do.
 *
 * @author hgani
 * @version $Id$
 */
public class VelocityAdapter {
  /**
   * Instantiates {@link UiString} class.
   *
   * @param arg1 the first argument
   * @return the instance
   */
  public UiString createUiString(String arg1) {
    // There is an overloaded UiString constructor that can receive a
    // second argument for define a few options (via constants). However,
    // Velocity templates cannot access class constants. Furthermore, it
    // cannot perform bitwise operations, which is needed for combining
    // several options). Due to this limitation, we don't need to expose
    // the overloaded constructor that receives the second argument.
    //
    // Nevertheless, UiString options can still be set by invoking some of
    // its option-related methods from the VM templates.
    return new UiString(arg1);
  }

  /**
   * Returns the length of the supplied array. If the supplied array is
   * <code>null</code>, returns 0.
   *
   * @param arr the array whose length is to be returned
   * @return the length of the given array
   */
  public int getArrayLength(Object[] arr) {
    return (arr == null)? 0 : arr.length;
  }
}
