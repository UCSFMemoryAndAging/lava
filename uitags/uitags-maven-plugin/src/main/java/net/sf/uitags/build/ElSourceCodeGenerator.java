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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import net.sf.uitags.build.TldUtils.Tag;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.w3c.dom.Document;

/**
 * Generates EL source code for custom tags specified in the TLD.
 *
 * @goal generateElSourceCode
 * @description Generates EL source code for custom tags specified in the TLD.
 * @author hgani
 * @author jonni
 * @version $Id$
 */
public final class ElSourceCodeGenerator extends AbstractMojo {
  //////////////////////////////////////
  ////////// Maven parameters //////////
  //////////////////////////////////////

  /**
   * The name of the TLD file to read for generating non-EL tag handlers.
   * @parameter
   * @required
   */
  private String tldFile;
  /**
   * The name of the TLD file to read for generating EL tag handlers.
   * @parameter
   * @required
   */
  private String elTldFile;
  /**
   * The directory containing the generated source code.
   * @parameter
   * @required
   */
  private String outputDir;


  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  // keyed on tag names
  private Map tagMap;
  private String serialVersionUid;


  ////////////////////////////////////////////
  ////////// Initialization Methods //////////
  ////////////////////////////////////////////

  /**
   * Processes Maven parameters and save the results to instance variables.
   */
  private void init() {
    Document tldDocument = TldUtils.getDocument(this.tldFile);
    Document elTldDocument = TldUtils.getDocument(this.elTldFile);
    this.tagMap = TldUtils.extractTags(tldDocument, elTldDocument);

    this.serialVersionUid = tldDocument.getElementsByTagName(
        "tlib-version").item(0).getFirstChild().getNodeValue();
    this.serialVersionUid = this.serialVersionUid.replaceAll("[^0-9]", "");
    this.serialVersionUid = this.serialVersionUid.replaceAll("^0*", "");
    this.serialVersionUid = this.serialVersionUid + "L";
  }


  ////////////////////////////////////
  ////////// Action Methods //////////
  ////////////////////////////////////

  private void ensureDirectoryExists(File directory) {
    if (!directory.exists()) {
      directory.mkdir();
    }
    else if (!directory.isDirectory()) {
      throw new IllegalArgumentException(
          directory.getName() + " is not a directory.");
    }
  }

  private void generateCodeFor(Tag tag) {
    try {
      String outputPath = this.outputDir + tag.getElHandlerPath() + ".java";
      ensureDirectoryExists(new File(outputPath).getParentFile());

      Properties properties = new Properties();
      properties.setProperty("resource.loader", "classpath");
      properties.setProperty("classpath.resource.loader.class",
          "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
      Velocity.init(properties);

      Map params = null;

      params = new HashMap();
      params.put("serialVersionUid", this.serialVersionUid);
      params.put("packageName", tag.getPackageNameFraction());
      params.put("className", tag.getHandlerNameFraction());
      params.put("elPackageName", tag.getElPackageNameFraction());
      params.put("elClassName", tag.getElHandlerNameFraction());
      params.put("attributes", tag.getAttributes());
      printToFile(outputPath, "net/sf/uitags/build/el-tagHandler.vm", params);

      params = new HashMap();
      params.put("packageName", tag.getPackageNameFraction());
      params.put("elPackageName", tag.getElPackageNameFraction());
      params.put("elClassName", tag.getElHandlerNameFraction());
      printToFile(this.outputDir + tag.getElHandlerPath() + "BeanInfo.java",
          "net/sf/uitags/build/el-tagBeanInfo.vm", params);
    }
    // Convert checked exceptions into unchecked.
    catch (FileNotFoundException fnfe) {
      throw new RuntimeException(fnfe);
    }
    catch (ResourceNotFoundException rnfe) {
      throw new RuntimeException(rnfe);
    }
    catch (MethodInvocationException mie) {
      throw new RuntimeException(mie);
    }
    catch (ParseErrorException pee) {
      throw new RuntimeException(pee);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void printToFile(
      String outputPath, String velocityTemplatePath, Map velocityParams)
      throws FileNotFoundException, ResourceNotFoundException,
          MethodInvocationException, ParseErrorException, Exception {
    PrintStream out = new PrintStream(new FileOutputStream(outputPath));
    StringWriter result = new StringWriter();
    Velocity.mergeTemplate(velocityTemplatePath, "ISO8859-1",
        new VelocityContext(velocityParams), result);
    out.println(result.toString());
    out.close();
  }

  public void execute() {
    init();

    getLog().info("Generating EL source code for:");
    for (Iterator iter = this.tagMap.entrySet().iterator();
        iter.hasNext(); ) {
      Map.Entry entry = (Map.Entry) iter.next();
      Tag tag = (Tag) entry.getValue();
      getLog().info(tag.getName());
      generateCodeFor(tag);
    }
    getLog().info("Done!");
  }

  ////////////////////////////////////
  ////////// Static Methods //////////
  ////////////////////////////////////

  public static void main(String args[]) {
    if (args.length != 3) {
      printUsage();
    }

    ElSourceCodeGenerator generator = new ElSourceCodeGenerator();
    generator.tldFile = args[0];
    generator.elTldFile = args[1];
    generator.outputDir = args[2];
    generator.execute();
  }

  private static void printUsage() {
    System.err.println("Usage: PROGRAM TLD_FILE EL_TLD_FILE OUTPUT_FOLDER");
    System.exit(1);
  }
}
