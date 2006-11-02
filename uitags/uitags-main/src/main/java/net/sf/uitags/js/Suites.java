package net.sf.uitags.js;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.uitags.util.ArrayUtils;
import net.sf.uitags.util.PropertiesLoadingException;

import org.apache.commons.collections.ExtendedProperties;

/**
 * Represents uitags suites whose JS files are to be served to the browser.
 *
 * @author jonni
 */
abstract class Suites {
  /////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  /**
   * Name of the properties file containing a map of suites to JS file names.
   */
  private static final String SUITE_PROPS = "file-names.properties";

  /**
   * The keyword to use in place of suite names in web.xml in order to
   * include JS files in all suites.
   */
  static final String KEYWORD_ALL = "all";



  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * Map of <code>String</code>s to <code>List</code>s.
   */
  protected Map mapping = new LinkedHashMap();



  //////////////////////////////////
  ////////// Construction //////////
  //////////////////////////////////

  /**
   * Provided only to allow subclassing. To create an instance, use
   * {@link #getInstance()}.
   */
  protected Suites() {
    initSuiteNameToFileNamesMapping();
  }

  static Suites getInstance(String suites) {
    if (suites == null || isKeywordAll(suites)) {
      return new AllSuites();
    }

    // Make sure the keyword "all" doesn't appear amongst other suite names.
    String[] suitesAsArray = ArrayUtils.toArrayOfTrimmed(suites);
    for (int i = 0; i < suitesAsArray.length; i++) {
      if (isKeywordAll(suitesAsArray[i])) {
        throw new IllegalArgumentException(
            "The keyword 'all' can only be used by itself.");
      }
    }

    return new NamedSuites(suitesAsArray);
  }



  ////////////////////////////
  ////////// Methods //////////
  /////////////////////////////

  private static boolean isKeywordAll(String suitesAsString) {
    return suitesAsString.trim().equalsIgnoreCase(KEYWORD_ALL);
  }

  private void initSuiteNameToFileNamesMapping() {
    ExtendedProperties props = loadMappingFromProperties();

    for (Iterator suites = props.getKeys(); suites.hasNext(); ) {
      String suiteName = (String) suites.next();
      String[] fileNames = props.getStringArray(suiteName);

      for (int i = 0; i < fileNames.length; i++) {
        fileNames[i] = fileNames[i];
      }

      mapping.put(suiteName, Arrays.asList(fileNames));
    }
  }

  private ExtendedProperties loadMappingFromProperties() {
    ExtendedProperties props = new ExtendedProperties();
    try {
      props.load(this.getClass().getResourceAsStream(SUITE_PROPS));
    }
    catch (IOException e) {
      throw new PropertiesLoadingException(e, SUITE_PROPS);
    }

    return props;
  }

  protected abstract List getFileNames();
}
