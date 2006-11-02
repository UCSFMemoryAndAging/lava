package net.sf.uitags.js;

import java.util.List;

import junit.framework.TestCase;

public class AllSuitesTest extends TestCase {
  private AllSuites suites;

  protected void setUp() throws Exception {
    super.setUp();

    // The cast to AllSuites ensures that we are testing the right class.
    this.suites = (AllSuites) Suites.getInstance(Suites.KEYWORD_ALL);
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    this.suites = null;
  }

  public void testCorrectNumberOfSuitesAreRetrieved() {
    List fileNames = this.suites.getFileNames();

    assertEquals(44, fileNames.size());
  }

  public void testAllSuitesAreRetrievedInOrder() {
    List fileNames = this.suites.getFileNames();

    assertEquals("util/Object.js", fileNames.get(0));
    assertEquals("util/Exception.js", fileNames.get(1));
    assertEquals("select/Suite.js", fileNames.get(42));
    assertEquals("select/Driver.js", fileNames.get(43));
  }
}
