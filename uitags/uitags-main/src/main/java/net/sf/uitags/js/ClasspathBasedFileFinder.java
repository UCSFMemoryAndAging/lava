package net.sf.uitags.js;

import java.io.IOException;
import java.io.InputStream;

final class ClasspathBasedFileFinder extends FileFinder {

  protected InputStream openInputStream(String fileName) throws IOException {
    return this.getClass().getResourceAsStream(fileName);
  }

  protected boolean supportsFilesNotFromSuites() {
    return false;
  }

  protected long getLastModified(String fileName) throws IOException {
    return this.getClass().
        getResource(fileName).openConnection().getLastModified();
  }
}
