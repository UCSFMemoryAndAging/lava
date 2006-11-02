package net.sf.uitags.js;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents suites which are explicitly nominated by the web developer.
 *
 * @author jonni
 */
final class NamedSuites extends Suites {
  private String[] suiteNames;

  NamedSuites(String[] suiteNames) {
    super();
    this.suiteNames = suiteNames;
  }

  protected List getFileNames() {
    List fileNames = new ArrayList();
    for (int i = 0; i < suiteNames.length; i++) {
      fileNames.addAll((List) this.mapping.get(this.suiteNames[i]));
    }

    return fileNames;
  }
}
