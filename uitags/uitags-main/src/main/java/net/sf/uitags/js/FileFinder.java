package net.sf.uitags.js;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;

import net.sf.uitags.util.ResourceCloser;

/**
 * A class that knows how to find (and access the contents of) a file.
 *
 * @author jonni
 */
abstract class FileFinder {
  static FileFinder getInstance(
      ServletContext context, String containingDirName) {
    if (containingDirName == null) {
      return new ClasspathBasedFileFinder();
    }

    return new FileSystemBasedFileFinder(context, containingDirName);
  }

  final String readContents(String fileName) throws IOException {
    StringBuffer out = new StringBuffer();

    InputStream in = null;
    BufferedReader contents = null;
    try {
      in = openInputStream(fileName);
      contents = new BufferedReader(new InputStreamReader(in));
      while (contents.ready()) {
        out.append(contents.readLine());
        out.append("\n");
      }
    }
    finally {
      ResourceCloser.close(contents);
      ResourceCloser.close(in);
    }

    return out.toString();
  }

  /**
   * To be implemented by the child class, this method opens an input stream.
   * As soon as this method returns the child class should give up control of
   * the input stream. It should not, for example, attempt to close the
   * input stream.
   */
  protected abstract InputStream openInputStream(String fileName)
      throws IOException;

  /**
   * Returns a <code>boolean</code> to indicate whether the finder is able
   * to find a file that is not part of uitags suites.
   */
  protected abstract boolean supportsFilesNotFromSuites();

  /**
   * Returns the specified file's last modified time or 0 if unknown.
   */
  protected abstract long getLastModified(String fileName)
      throws IOException;
}
