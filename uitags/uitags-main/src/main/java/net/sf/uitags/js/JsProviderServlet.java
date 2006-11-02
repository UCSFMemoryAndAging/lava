package net.sf.uitags.js;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Reads JS source files from either uitags JAR or file system, and serves
 * them to the browser.
 *
 * @author jonni
 */
public final class JsProviderServlet extends HttpServlet {

  private static final long serialVersionUID = 100L;

  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  /// Init param names ///

  static final String DIR = "directory";
  static final String SUITES = "suites";
  static final String CUSTOM_FILES = "customFiles";
  static final String DEBUG = "debug";



  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  private JsFiles jsFiles;



  ////////////////////////////////////
  ////////// Initialization //////////
  ////////////////////////////////////

  public void init(ServletConfig config) throws ServletException {
    Suites suites = Suites.getInstance(config.getInitParameter(SUITES));
    FileFinder fileFinder = FileFinder.getInstance(
        config.getServletContext(), config.getInitParameter(DIR));

    this.jsFiles = new JsFiles(suites, fileFinder);
    this.jsFiles.setCustomFileNames(config.getInitParameter(CUSTOM_FILES));
    this.jsFiles.setInDebugMode(
        Boolean.valueOf(config.getInitParameter(DEBUG)).booleanValue());
  }



  /////////////////////////////////////
  ////////// Service methods //////////
  /////////////////////////////////////

  /**
   * Probably noone would request a JS using POST but implement this anyway.
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    super.doPost(request, response);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.getOutputStream().println(this.jsFiles.getContents());
  }

  protected long getLastModified(HttpServletRequest arg0) {
    long lastModified = this.jsFiles.getLastModified();
    return (lastModified == 0)? -1 : lastModified;
  }
}
