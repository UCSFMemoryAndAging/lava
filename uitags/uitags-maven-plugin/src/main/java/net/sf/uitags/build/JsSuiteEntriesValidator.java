/**
 * Nov 20, 2004
 *
 * Copyright 2004 uitags
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.uitags.build;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal validatesJsSuiteEntries
 * @description Validity checker for Javascript suite entries.
 * @author hgani
 * @author jonni
 * @version $Id$
 */
public final class JsSuiteEntriesValidator extends AbstractMojo {
  //////////////////////////////////////
  ////////// Maven parameters //////////
  //////////////////////////////////////

  /**
   * The directory containing the JavaScript source files.
   * @parameter
   * @required
   */
  private String sourceDir;
  /**
   * The properties file containing entries to verify against.
   * @parameter
   * @required
   */
  private String entriesFile;


  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  private File jsBaseDirectory;
  private JsFileFilter fileFilter;
  private JsSuiteFilter suiteFilter;


  ////////////////////////////////////////////
  ////////// Initialization Methods //////////
  ////////////////////////////////////////////

  /**
   * Processes Maven parameters and save the results to instance variables.
   */
  private void init() throws MojoFailureException {
    this.jsBaseDirectory = new File(this.sourceDir);
    if (!this.jsBaseDirectory.isDirectory()) {
      getLog().error("Invalid directory name: " + this.sourceDir);
      throw new MojoFailureException(
          "Invalid directory name: " + this.sourceDir);
    }

    this.fileFilter = new JsFileFilter();
    this.suiteFilter = new JsSuiteFilter();
  }


  ////////////////////////////////////
  ////////// Action Methods //////////
  ////////////////////////////////////

  private Map getExtractedSuiteFileNameMap() {
    Properties suiteFileProp = new SuiteProperties();
    File[] suiteDirs = getSuiteDirectories(this.jsBaseDirectory);
    for (int i = 0; i < suiteDirs.length; ++i) {
      String key = suiteDirs[i].getName();
      String value = implode(getJsFiles(suiteDirs[i]));
      suiteFileProp.setProperty(key, value);
    }
    return suiteFileProp;
  }

  private String implode(File[] files) {
    StringBuffer buffer = new StringBuffer();
    if (files.length > 0) {
      buffer.append(getPathRelativeToBaseDirectory(files[0]));
      for (int i = 1; i < files.length; ++i) {
        buffer.append("," + getPathRelativeToBaseDirectory(files[i]));
      }
    }
    return buffer.toString();
  }

  private String getPathRelativeToBaseDirectory(File file) {
    String baseName = StringUtils.escapePattern(
        this.jsBaseDirectory.getPath());
    String path = file.getPath().replaceFirst(baseName, "");
    return path.replaceFirst("^/*", "");
  }

  private File[] getJsFiles(File suiteDir) {
    return suiteDir.listFiles(this.fileFilter);
  }

  private File[] getSuiteDirectories(File jsBaseDir) {
    return jsBaseDir.listFiles(this.suiteFilter);
  }

  private Map getSuiteFileNamesFromEntriesFile() throws MojoFailureException {
    Properties availableSuites = new Properties();
    try {
      FileInputStream inputStream = new FileInputStream(this.entriesFile);
      availableSuites.load(inputStream);
    }
    catch (IOException e) {
      throw new MojoFailureException(
          "Unable to load entries file: '" + this.entriesFile + "'.");
    }

    return availableSuites;
  }

  public void execute() throws MojoFailureException {
    init();

    Map extractedMap = getExtractedSuiteFileNameMap();
    Map savedMap = getSuiteFileNamesFromEntriesFile();

    List suiteDifference = getSuiteDifference(extractedMap, savedMap);
    assertNoMissingSuites(suiteDifference, "properties file");
    suiteDifference = getSuiteDifference(savedMap, extractedMap);
    assertNoMissingSuites(suiteDifference, "file system");

    Map fileNameDifference = getFileNameDifference(extractedMap, savedMap);
    assertNoMissingFiles(fileNameDifference, "properties file");
    fileNameDifference = getFileNameDifference(savedMap, extractedMap);
    assertNoMissingFiles(fileNameDifference, "file system");
  }

  private List getSuiteDifference(Map map1, Map map2) {
    List results = new ArrayList();
    for (Iterator iter = map1.keySet().iterator(); iter.hasNext(); ) {
      String key = (String) iter.next();
      if (!map2.containsKey(key)) {
        results.add(key);
      }
    }
    return results;
  }

  private void assertNoMissingSuites(List difference, String entriesName)
      throws MojoFailureException {
    if (difference.size() > 0) {
      getLog().error("Suites missing from " + entriesName + ":");
      for (Iterator iter = difference.iterator(); iter.hasNext(); ) {
        getLog().error(iter.next().toString());
      }

      throw new MojoFailureException(
          "There are suites missing from " + entriesName + ".");
    }
  }

  private Map getFileNameDifference(Map map1, Map map2) {
    Map results = new HashMap();
    for (Iterator iter = map1.entrySet().iterator(); iter.hasNext(); ) {
      Map.Entry entry = (Map.Entry) iter.next();
      String key = (String) entry.getKey();

      if (map2.containsKey(key)) {
        String value1 = (String) entry.getValue();
        String value2 = (String) map2.get(key);
        results.put(key, getFileNameDifference(value1, value2));
      }
    }

    return results;
  }

  private List getFileNameDifference(String fileNames1, String fileNames2) {
    String[] fileNameArray1 = fileNames1.split(",");
    String[] fileNameArray2 = fileNames2.split(",");
    String baseDir = this.jsBaseDirectory.getAbsolutePath() + "/";

    List difference = new ArrayList();
    for (int i = 0; i < fileNameArray1.length; ++i) {
      File file1 = new File(baseDir + fileNameArray1[i]);

      boolean exist = false;
      for (int j = 0; j < fileNameArray2.length; ++j) {
        File file2 = new File(baseDir + fileNameArray2[j]);
        if (file1.equals(file2)) {
          exist = true;
        }
      }

      if (!exist) {
        difference.add(file1);
      }
    }
    return difference;
  }

  private void assertNoMissingFiles(Map difference, String entriesName)
      throws MojoFailureException {
    boolean hasError = false;

    for (Iterator iter = difference.entrySet().iterator(); iter.hasNext(); ) {
      Map.Entry entry = (Map.Entry) iter.next();
      String suiteName = (String) entry.getKey();
      List fileNameList = (List) entry.getValue();

      if (fileNameList.size() > 0) {
        Object[] fileNameArray = fileNameList.toArray(
            new Object[fileNameList.size()]);
        getLog().error("Files in suite " + suiteName +
            " missing from " + entriesName + ": " +
            new ArrayList(Arrays.asList(fileNameArray)));
        hasError = true;
      }
    }

    if (hasError) {
      throw new MojoFailureException(
          "There are files missing from " + entriesName + ".");
    }
  }


  ////////////////////////////////////
  ////////// Static Methods //////////
  ////////////////////////////////////

  public static void main(String args[]) throws MojoFailureException {
    if (args.length != 2) {
      printUsage();
    }

    JsSuiteEntriesValidator generator = new JsSuiteEntriesValidator();
    generator.sourceDir = args[0];
    generator.entriesFile = args[1];
    generator.execute();
  }

  private static void printUsage() {
    System.err.println("Usage: PROGRAM JS_BASE_DIR ENTRIES_FILE");
    System.exit(1);
  }


  ///////////////////////////////////
  ////////// Inner Classes //////////
  ///////////////////////////////////

  private static class JsFileFilter implements FileFilter {
    public boolean accept(File file) {
      if (file.isFile() && file.getPath().endsWith(".js")) {
        return true;
      }
      return false;
    }
  }

  private static class JsSuiteFilter implements FileFilter {
    private static Set blackList = new HashSet();

    static {
      blackList.add("CVS");
      blackList.add(".svn");
      blackList.add("excludes");
    }

    public boolean accept(File file) {
      if (file.isDirectory() &&
          !blackList.contains(file.getName())) {
        return true;
      }
      return false;
    }
  }

  private static class SuiteProperties extends Properties {
    private static final long serialVersionUID = 1L;

    private static Properties suites = new Properties();

    static {
      suites.put("html", "core");
      suites.put("util", "core");
    }

    public Object setProperty(String key, String value) {
      if (suites.containsKey(key)) {
        key = suites.getProperty(key);
        String previous = getProperty(key);
        if (previous != null) {
          value = previous + "," + value;
        }
      }
      return super.setProperty(key, value);
    }
  }
}
