/**
 * Sep 05, 2006
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
import java.util.Iterator;
import java.util.Map;

import net.sf.uitags.build.TldUtils.Tag;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.w3c.dom.Document;

/**
 * @goal detectMissingBeanInfo
 * @description Detects missing bean info classes.
 * @author hgani
 * @version $Id$
 */
public final class MissingBeanInfoDetector extends AbstractMojo {
  //////////////////////////////////////
  ////////// Maven parameters //////////
  //////////////////////////////////////

  /**
   * The name of the TLD file containing the tags that require bean info.
   * @parameter
   * @required
   */
  private String tldFile;
  /**
   * The directory containing the tag bean info source files.
   * @parameter
   * @required
   */
  private String sourceDir;


  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  // keyed on tag names
  private Map tags;


  ////////////////////////////////////////////
  ////////// Initialization Methods //////////
  ////////////////////////////////////////////

  /**
   * Processes Maven parameters and save the results to instance variables.
   */
  private void init() {
    Document tldDocument = TldUtils.getDocument(this.tldFile);
    this.tags = TldUtils.extractTags(tldDocument);
  }


  ////////////////////////////////////
  ////////// Action Methods //////////
  ////////////////////////////////////

  public void execute() throws MojoFailureException {
    init();

    boolean beanInfoMissing = false;
    for (Iterator iter = this.tags.entrySet().iterator(); iter.hasNext(); ) {
      Map.Entry entry = (Map.Entry) iter.next();
      if (!assertBeanInfoExists((Tag) entry.getValue())) {
        beanInfoMissing = true;
      }
    }

    if (beanInfoMissing) {
      throw new MojoFailureException(
          "At least one bean info class is missing");
    }
  }

  private boolean assertBeanInfoExists(Tag tag) {
    String tagHandler = tag.getHandlerPath();

    File handlerFile = new File(
        this.sourceDir + tagHandler + "BeanInfo.java");
    if (!handlerFile.exists()) {
      getLog().error("Missing bean info for: " +
          tag.getFullyQualifiedHandlerName());
      return false;
    }
    return true;
  }

  ////////////////////////////////////
  ////////// Static Methods //////////
  ////////////////////////////////////

  public static void main(String args[]) throws MojoFailureException {
    if (args.length != 2) {
      printUsage();
    }

    MissingBeanInfoDetector generator = new MissingBeanInfoDetector();
    generator.tldFile = args[0];
    generator.sourceDir = args[1];
    generator.execute();
  }

  private static void printUsage() {
    System.err.println("Usage: PROGRAM TLD_FILE JAVA_SRC_DIR");
    System.exit(1);
  }
}
