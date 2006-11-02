package net.sf.uitags.js;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletContext;

final class FileSystemBasedFileFinder extends FileFinder {
  private ServletContext servletContext;
  private String containingDirName;

  FileSystemBasedFileFinder(
      ServletContext servletContext, String containingDirName) {
    this.servletContext = servletContext;
    this.containingDirName = correctDirName(containingDirName);
  }

  private String correctDirName(String name) {
    name = name.trim();

    // Ensure dir starts with a slash
    if (!name.startsWith("/")) {
      name = "/" + name;
    }

    // Ensure dir ends with a slash
    if (!name.endsWith("/")) {
      name = name + "/";
    }

    return name;
  }

  protected InputStream openInputStream(String fileName) throws IOException {
    return getUrl(fileName).openStream();
  }

  protected boolean supportsFilesNotFromSuites() {
    return true;
  }

  protected long getLastModified(String fileName) throws IOException {
    return getUrl(fileName).openConnection().getLastModified();
  }

  private URL getUrl(String fileName) throws IOException {
    return this.servletContext.getResource(this.containingDirName + fileName);
  }

  /**
   * This method is provided for the sole purpose of allowing testing.
   */
  String getCorrectedDirName() {
    return this.containingDirName;
  }
}
