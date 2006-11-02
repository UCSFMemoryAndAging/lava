package net.sf.uitags.js;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents all suites in uitags.
 *
 * @author jonni
 */
final class AllSuites extends Suites {

  protected List getFileNames() {
    List fileNames = new ArrayList();
    for (Iterator i = this.mapping.values().iterator(); i.hasNext(); ) {
      fileNames.addAll((List) i.next());
    }

    return fileNames;
  }

}
