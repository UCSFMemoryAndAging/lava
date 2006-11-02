package net.sf.uitags.js;

import junit.framework.TestCase;

public class FileSystemBasedFileFinderTest extends TestCase {
  public void testDirNameCorrectionTrimsWhiteSpace() {
    FileSystemBasedFileFinder fileFinder =
      new FileSystemBasedFileFinder(null, " /testDir/ ");
    assertEquals("/testDir/", fileFinder.getCorrectedDirName());
  }

  public void testDirNameCorrectionAddsLeadingAndTrailingSlash() {
    FileSystemBasedFileFinder fileFinder =
      new FileSystemBasedFileFinder(null, "includes/js");
    assertEquals("/includes/js/", fileFinder.getCorrectedDirName());
  }
}
