package net.sf.uitags.js;

import junit.framework.TestCase;

public class SuitesTest extends TestCase {
  public void testNullSuiteNameGivesAllSuites() {
    assertTrue(Suites.getInstance(null) instanceof AllSuites);
  }

  public void testKeywordAllForSuiteNameGivesAllSuites() {
    assertTrue(Suites.getInstance(Suites.KEYWORD_ALL) instanceof AllSuites);
  }

  public void testKeywordAllMixedWithOtherWordsCausesException() {
    try {
      Suites.getInstance("all,mixed with other words");
      fail("Should've gotten an exception.");
    }
    catch (IllegalArgumentException expected) {
      // OK - exception expected.
    }
  }
}
