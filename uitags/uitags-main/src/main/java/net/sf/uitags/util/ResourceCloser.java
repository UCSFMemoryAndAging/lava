package net.sf.uitags.util;

import java.io.InputStream;
import java.io.Reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides overloaded methods to close resources. The methods have been
 * designed to never fail, even if the underlying resource's
 * <code>close()</code> method fails (a message is logged instead).
 * This is so that one failing <code>close()</code> does not abruptly stop
 * the cleaning up of the resources in the same <code>finally</code> block.
 *
 * @author jonni
 */
public final class ResourceCloser {
  private static final Log log = LogFactory.getLog(ResourceCloser.class);

  private ResourceCloser() {
    // Non-instantiable utility class.
  }

  public static void close(Reader resource) {
    if (resource != null) {
      try {
        resource.close();
      }
      catch (Exception e) {
        if (log.isWarnEnabled()) {
          log.warn(
              "Error encountered while attempting to close a Resource.", e);
        }
      }
    }
  }

  public static void close(InputStream resource) {
    if (resource != null) {
      try {
        resource.close();
      }
      catch (Exception e) {
        if (log.isWarnEnabled()) {
          log.warn(
              "Error encountered while attempting to close an InputStream.", e);
        }
      }
    }
  }
}
