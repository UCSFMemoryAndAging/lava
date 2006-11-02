/**
 * Jun 27, 2005
 *
 * Copyright 2004 - 2005 uitags
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

import javax.servlet.jsp.PageContext;

import junit.framework.TestCase;

import org.springframework.mock.web.MockPageContext;

/**
 * Tests {@link net.sf.uitags.tagutil.ScopedIdGenerator}.
 *
 * @author jonni
 * @version $Id$
 */
public final class ScopedIdGeneratorTest extends TestCase {
  /**
   * Main method for the tests.
   *
   * @param args main arguments
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(ScopedIdGeneratorTest.class);
  }

  /** {@inheritDoc} */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /** {@inheritDoc} */
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /**
   * Ensures that {@link ScopedIdGenerator#nextId(int, String, PageContext)}
   * generates unique IDs.
   */
  public void testUngroupedNextId() {
    long id0, id1, id2;
    PageContext pageContext = new MockPageContext();

    id0 = ScopedIdGenerator.nextId(
        PageContext.PAGE_SCOPE, "id", pageContext);
    id1 = ScopedIdGenerator.nextId(
        PageContext.PAGE_SCOPE, "id", pageContext);
    id2 = ScopedIdGenerator.nextId(
        PageContext.PAGE_SCOPE, "id", pageContext);
    assertTrue(id0 != id1);
    assertTrue(id1 != id2);
    assertTrue(id2 != id0);

    id0 = ScopedIdGenerator.nextId(
        PageContext.REQUEST_SCOPE, "id", pageContext);
    id1 = ScopedIdGenerator.nextId(
        PageContext.REQUEST_SCOPE, "id", pageContext);
    id2 = ScopedIdGenerator.nextId(
        PageContext.REQUEST_SCOPE, "id", pageContext);
    assertTrue(id0 != id1);
    assertTrue(id1 != id2);
    assertTrue(id2 != id0);

    // Session scope can't be tested as we haven't got a HTTP session

    id0 = ScopedIdGenerator.nextId(
        PageContext.APPLICATION_SCOPE, "id", pageContext);
    id1 = ScopedIdGenerator.nextId(
        PageContext.APPLICATION_SCOPE, "id", pageContext);
    id2 = ScopedIdGenerator.nextId(
        PageContext.APPLICATION_SCOPE, "id", pageContext);
    assertTrue(id0 != id1);
    assertTrue(id1 != id2);
    assertTrue(id2 != id0);
  }

  /**
   * Ensures that {@link ScopedIdGenerator#nextId(int, String, PageContext)}
   * generates unique IDs.
   */
  public void testGroupedNextId() {
    long id0, id1, id2;
    PageContext pageContext = new MockPageContext();

    id0 = ScopedIdGenerator.nextId(
        PageContext.PAGE_SCOPE, "id", pageContext, "group1");
    id1 = ScopedIdGenerator.nextId(
        PageContext.PAGE_SCOPE, "id", pageContext, "group1");
    id2 = ScopedIdGenerator.nextId(
        PageContext.PAGE_SCOPE, "id", pageContext, "group2");
    assertTrue(id0 == id1);
    assertTrue(id1 != id2);
    assertTrue(id2 != id0);

    id0 = ScopedIdGenerator.nextId(
        PageContext.REQUEST_SCOPE, "id", pageContext, null);
    id1 = ScopedIdGenerator.nextId(
        PageContext.REQUEST_SCOPE, "id", pageContext, "group2");
    id2 = ScopedIdGenerator.nextId(
        PageContext.REQUEST_SCOPE, "id", pageContext, "group2");
    assertTrue(id0 != id1);
    assertTrue(id1 == id2);
    assertTrue(id2 != id0);

    // Session scope can't be tested as we haven't got a HTTP session

    id0 = ScopedIdGenerator.nextId(
        PageContext.APPLICATION_SCOPE, "id", pageContext, "group1");
    id1 = ScopedIdGenerator.nextId(
        PageContext.APPLICATION_SCOPE, "id", pageContext, "group2");
    id2 = ScopedIdGenerator.nextId(
        PageContext.APPLICATION_SCOPE, "id", pageContext, "group3");
    assertTrue(id0 != id1);
    assertTrue(id1 != id2);
    assertTrue(id2 != id0);
  }
}
