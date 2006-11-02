package net.sf.uitags.js;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import junit.framework.TestCase;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockServletContext;

public class JsFilesTest extends TestCase {
  private JsFiles jsFiles;

  protected void tearDown() throws Exception {
    super.tearDown();
    this.jsFiles = null;
  }

  private void setUpJsFilesWithClasspathBasedFileFinder() {
    Suites suites = Suites.getInstance("core");

    ResourceLoader classpathResourceLoader = new ResourceLoader() {
      public Resource getResource(String location) {
        return new ClassPathResource(location);
      }
    };
    FileFinder fileFinder = FileFinder.getInstance(
        new MockServletContext(classpathResourceLoader), null);

    this.jsFiles = new JsFiles(suites, fileFinder);
  }

  private void setUpJsFilesWithFileSystemBasedFileFinder() {
    Suites suites = Suites.getInstance("core");

    ResourceLoader fileSystemResourceLoader = new ResourceLoader() {
      public Resource getResource(String location) {
        return new FileSystemResource("." + location);
      }
    };
    FileFinder fileFinder = FileFinder.getInstance(
        new MockServletContext(fileSystemResourceLoader), "src/main/js");

    this.jsFiles = new JsFiles(suites, fileFinder);
  }



  ////////////////////////////////////////////////////////////
  ////////// The following test a JsFiles that uses //////////
  ////////// ClasspathBasedFileFinder               //////////
  ////////////////////////////////////////////////////////////

  public void testDebugJsIsNotIncludedInDebugModeIfUsingClasspathBasedFileFinder()
      throws IOException {
    setUpJsFilesWithClasspathBasedFileFinder();

    this.jsFiles.setInDebugMode(true);
    String contents = this.jsFiles.getContents();
    assertTrue(contents.indexOf("uiGlobal_defaultLevel =") == -1);
  }

  public void testDebugJsIsNotIncludedInNonDebugModeIfUsingClasspathBasedFileFinder()
      throws IOException {
    setUpJsFilesWithClasspathBasedFileFinder();

    String contents = this.jsFiles.getContents();
    assertTrue(contents.indexOf("uiGlobal_defaultLevel =") == -1);
  }



  //////////////////////////////////////////////////////////
  ////////// The following test a JsFiles that uses //////////
  ////////// FileSystemBasedFileFinder              //////////
  ////////////////////////////////////////////////////////////

  public void testDebugJsIsIncludedInDebugModeIfUsingFileSystemBasedFileFinder()
      throws IOException {
    setUpJsFilesWithFileSystemBasedFileFinder();

    this.jsFiles.setInDebugMode(true);
    String contents = this.jsFiles.getContents();
    assertTrue(contents.indexOf("uiGlobal_defaultLevel =") > -1);
  }

  public void testDebugJsIsNotIncludedInNonDebugModeIfUsingFileSystemBasedFileFinder()
      throws IOException {
    setUpJsFilesWithFileSystemBasedFileFinder();

    String contents = this.jsFiles.getContents();
    assertTrue(contents.indexOf("uiGlobal_defaultLevel =") == -1);
  }



  /////////////////////////////////
  ////////// Other tests //////////
  /////////////////////////////////

  public void testUsesLatestLastModifiedOfAllFiles() throws IOException {
    Resource resource = new FileSystemResourceLoader().getResource(
        "./src/main/js/html/Element.js");
    File fileToTouch = resource.getFile();
    long latestTimestamp = new Date().getTime();
    assertTrue(fileToTouch.setLastModified(latestTimestamp));

    // We can only use FileSystemBasedFileFinder for testing the timestamp.
    setUpJsFilesWithFileSystemBasedFileFinder();

    // The file system may not go as far as the milliseconds so allow for up
    // to one second difference.
    long delta = 1000;
    assertEquals(latestTimestamp, this.jsFiles.getLastModified(), delta);
  }
}
