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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal reduceSize
 * @description Reduces the size of the JS source files.
 * @author hgani
 * @author jonni
 * @version $Id$
 */
public final class SimpleJsFileSizeReducer extends AbstractMojo {
  //////////////////////////////////////
  ////////// Maven parameters //////////
  //////////////////////////////////////

  /**
   * The directory containing the JavaScript source files.
   * @parameter
   * @required
   */
  private String inputDir;
  /**
   * The directory to contained process JavaScript files.
   * @parameter
   * @required
   */
  private String outputDir;



  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  private File jsInputDirectory;
  private File jsOutputDirectory;
  private JsFileFilter fileFilter;
  private JsDirectoryFilter directoryFilter;



  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Processes Maven parameters and save the results to instance variables.
   */
  private void init() throws MojoFailureException {
    this.jsInputDirectory = getDirectoryHandle(this.inputDir, false);
    this.jsOutputDirectory = getDirectoryHandle(this.outputDir, true);

    this.fileFilter = new JsFileFilter();
    this.directoryFilter = new JsDirectoryFilter();
  }

  private File getDirectoryHandle(String dirPath, boolean createIfNotExists)
      throws MojoFailureException {
    File dir = new File(dirPath);
    if (!dir.exists() && createIfNotExists) {
      dir.mkdirs();
    }

    if (!dir.isDirectory()) {
      getLog().error("Invalid directory name: " + dirPath);
      throw new MojoFailureException("Invalid directory name: " + dirPath);
    }
    return dir;
  }



  ////////////////////////////////////
  ////////// Action Methods //////////
  ////////////////////////////////////

  private File[] getJsFilePaths(File baseDir) {
    File[] jsFiles = baseDir.listFiles(this.fileFilter);
    List allJsFiles = new ArrayList();
    addFilesToList(jsFiles, allJsFiles);

    File[] subDirs = baseDir.listFiles(this.directoryFilter);
    for (int i = 0; i < subDirs.length; ++i) {
      jsFiles = getJsFilePaths(subDirs[i]);
      addFilesToList(jsFiles, allJsFiles);
    }
    return (File[]) allJsFiles.toArray(new File[allJsFiles.size()]);
  }

  private void addFilesToList(File[] files, List list) {
    for (int i = 0; i < files.length; ++i) {
      list.add(files[i]);
    }
  }

  private File[] getAllSourceJsFilePaths() {
    return getJsFilePaths(this.jsInputDirectory);
  }

  private File[] getAllTargetJsFilePaths() {
    return getJsFilePaths(this.jsOutputDirectory);
  }

  private String stripFileContent(File jsFile) throws IOException {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(jsFile)));

    String line;
    CommentStripper stripper = new CommentStripper();
    StringBuffer buffer = new StringBuffer();
    while ((line = reader.readLine()) != null) {
      line = stripComments(line, stripper) + "\n";
      line = stripEverythingIfOnlyContainSpaces(line);
      buffer.append(line);
    }
    return buffer.toString();
  }

  private void performFileSizeReduction(File jsFile) {
    try {
      String content = stripFileContent(jsFile);
      getOutputStreamFromInputFile(jsFile).print(content);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private PrintStream getOutputStreamFromInputFile(File inputFile)
      throws FileNotFoundException {
    String relativePath = getPathRelativeToInputDirectory(inputFile);
    String outputPath = this.jsOutputDirectory.getAbsolutePath() +
        "/" + relativePath;
    ensureDirectoryExists(new File(outputPath).getParentFile());
    return new PrintStream(new FileOutputStream(outputPath));
  }

  private void ensureDirectoryExists(File directory) {
    if (!directory.exists()) {
      directory.mkdir();
    }
    else if (!directory.isDirectory()) {
      throw new IllegalArgumentException(
          directory.getName() + " is not a directory.");
    }
  }

  private String stripComments(String line, CommentStripper stripper) {
    stripper.startLine();
    for (int i = 0; i < line.length(); ++i) {
      if (!stripper.processCharacter(line.charAt(i))) {
        return stripper.endLine();
      }
    }
    return stripper.endLine();
  }

  private String stripEverythingIfOnlyContainSpaces(String line) {
    return line.replaceFirst("^[ \t\n]*$", "");
  }

  private String getPathRelativeToInputDirectory(File file) {
    String baseName = StringUtils.escapePattern(
        this.jsInputDirectory.getPath());
    String path = file.getPath().replaceFirst(baseName, "");
    return path.replaceFirst("^/*", "");
  }

  public void execute() throws MojoFailureException {
    init();

    File[] targetJsFiles = getAllTargetJsFilePaths();
    getLog().info("Deleting files in target directory:");
    for (int i = 0; i < targetJsFiles.length; ++i) {
      getLog().info("Deleting " + targetJsFiles[i].getAbsolutePath());
      targetJsFiles[i].delete();
    }

    File[] sourceJsFiles = getAllSourceJsFilePaths();
    System.out.println("Reducing the size of source javascript files:");
    for (int i = 0; i < sourceJsFiles.length; ++i) {
      getLog().info("Stripping " + sourceJsFiles[i].getAbsolutePath());
      performFileSizeReduction(sourceJsFiles[i]);
    }
  }



  ////////////////////////////////////
  ////////// Static Methods //////////
  ////////////////////////////////////

  public static void main(String args[]) throws MojoFailureException {
    if (args.length != 2) {
      printUsage();
    }

    SimpleJsFileSizeReducer reducer = new SimpleJsFileSizeReducer();
    reducer.inputDir = args[0];
    reducer.outputDir = args[1];
    reducer.execute();
  }

  private static void printUsage() {
    System.err.println("Usage: PROGRAM INPUT_BASE_DIR OUTPUT_BASE_DIR");
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

  private static class JsDirectoryFilter implements FileFilter {
    private static Set blackList = new HashSet();

    static {
      blackList.add("CVS");
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

  private static class CommentStripper {
    private StringBuffer buffer;
    private boolean insideSingleQuote;
    private boolean insideDoubleQuote;
    private boolean slashEncountered;
    private boolean asteriskEncountered;
    private boolean insideMultiLinesComment;

    private CommentStripper() {
      this.insideMultiLinesComment = false;
      this.asteriskEncountered = false;
    }

    private void startLine() {
      this.buffer = new StringBuffer();
      this.insideSingleQuote = false;
      this.insideDoubleQuote = false;
      this.slashEncountered = false;
//      this.asteriskEncountered = false;
    }

    /**
     * @param currentChar currently processed char
     * @return true if the processing can continue, false if there is
     *     no need to process the rest of the line
     */
    private boolean processCharacter(char currentChar) {
      if (shouldIgnoreMultiLinesCommentContent(currentChar)) {
        return true;
      }

      switch (currentChar) {
        case '\'' :
          toggleSingleQuote();
          break;
        case '"' :
          toggleDoubleQuote();
          break;
        case '/' :
          if (checkIfSingleLineCommentStarted()) {
            return false;
          }
          if (this.insideMultiLinesComment &&
              checkIfMultiLinesCommentEnded()) {
            return true;
          }
          break;
        case '*' :
          if (this.insideMultiLinesComment) {
            this.asteriskEncountered = true;
          }
          else {
            this.insideMultiLinesComment = checkIfMultiLinesCommentStarted();
          }
          break;
        default :
          makeUpForAnyIgnoredSlash();
          dismissAnyPosibility();
      }
      storeOnlyIfConfirmed(currentChar);

      return true;
    }

    private boolean shouldIgnoreMultiLinesCommentContent(char currentChar) {
      return this.insideMultiLinesComment &&
          !hasPosibilityToEndMultiLinesComment(currentChar);
    }

    private boolean hasPosibilityToEndMultiLinesComment(char currentChar) {
      return this.asteriskEncountered || currentChar == '*';
    }

    private void toggleSingleQuote() {
      this.insideSingleQuote = !this.insideSingleQuote;
    }

    private void toggleDoubleQuote() {
      this.insideDoubleQuote = !this.insideDoubleQuote;
    }

    private boolean insideQuote() {
      return this.insideSingleQuote || this.insideDoubleQuote;
    }

    private boolean checkIfSingleLineCommentStarted() {
      if (!insideQuote()) {
        if (this.slashEncountered) {
          this.slashEncountered = false;
          return true;
        }
        this.slashEncountered = true;
      }
      return false;
    }

    private boolean checkIfMultiLinesCommentStarted() {
      if (!insideQuote()) {
        if (this.slashEncountered) {
          this.slashEncountered = false;
          return true;
        }
      }
      return false;
    }

    private boolean checkIfMultiLinesCommentEnded() {
      if (this.asteriskEncountered) {
        this.insideMultiLinesComment = false;
        this.asteriskEncountered = false;
        this.slashEncountered = false;
        return true;
      }
      return false;
    }

    private void dismissAnyPosibility() {
      this.slashEncountered = false;
      this.asteriskEncountered = false;
    }

    private void makeUpForAnyIgnoredSlash() {
      if (this.slashEncountered) {
        storeIfNotInsideComment('/');
      }
    }

    private void storeOnlyIfConfirmed(char currentChar) {
      if (!this.slashEncountered) {
        storeIfNotInsideComment(currentChar);
      }
    }

    private void storeIfNotInsideComment(char currentChar) {
      if (!this.insideMultiLinesComment) {
        this.buffer.append(currentChar);
      }
    }

    private String endLine() {
      makeUpForAnyIgnoredSlash();
      return this.buffer.toString();
    }
  }
}
