/**
 * June 7, 2006
 * Charlie Toohey
 * RemoveOptionTag
 *
 * Copyright 2004 - 2005 uitags
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
package net.sf.uitags.tag.formGuide;

import java.util.Map.Entry;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.uitags.util.UiString;

/**
 * Removes the option(s) selected in the observe tags from the specified
 * remove tag. Assumes that the observe tag(s) and remove tag have the
 * same options. 
 */
public class RemoveOptionTag extends BaseChildTag {
  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 52L;

  /**
   * Javascript callbacks for elements.
   */
  static final String CLONE_OPTION_CALLBACK_METHOD = "cloneOptions";
  static final String REMOVE_OPTION_CALLBACK_METHOD = "removeOption";



  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////


  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Default constructor.
   */
  public RemoveOptionTag() {
    super();
  }



  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////



  ///////////////////////////////
  ////////// Tag logic //////////
  ///////////////////////////////

  /**
   * Communicates with the parent tag ({@link FormGuideTag}).
   *
   * @return <code>EVAL_PAGE</code>
   * @throws JspException to communicate error
   */
  public int doEndTag() throws JspException {
	validateAttributes();
    FormGuideTag formGuideTag = (FormGuideTag) findParent(FormGuideTag.class);
    
    String[] elementArray = null;
    if (this.elementIds != null) {
    	elementArray = this.elementIds.split(",");
    }
    else if (this.elementNames != null) {
    	elementArray = this.elementNames.split(",");
    }
    for (String elementIdOrName : elementArray) {
   		if (this.elementIds != null && this.component != null) {
   	   		elementIdOrName = component + "_" + elementIdOrName.trim(); 
   		}
   		String idOrNameParam = getJsElementParam(elementIdOrName.trim());
    						    
   		// removeOption child tag specifies the selectbox whose option(s) get removed where 
   		// the removed options are those selected by the observed selectbox(es). assumes that 
   		// the specified removeOption selectbox contains the same options that the observed 
   		// selectbox(es) contain (minus the option(s) currently selected in the observed 
                // selectbox(es) which the action of this tag removes)
   		// e.g. the instrument quality issues select box widgets.
  						    
   		// widgetGroups is a Map of distinct observed widgets where any given widget only appears 
                // once (if a widget has more than one observation it appears multiple times in observedWidgets, 
                // which is why not using observedWidgets here). either the widget id or name is the key, 
   		// depending upon whether the observe child tag used an elementId or elementName. the value
   		// is an ObservedWidget object so that the id and name can be passed as arguments to the
   		// cloneOption task, which is part of removing an option.
    						    
   		// iterate thru the observed selectboxes, adding a task to remove the observed selectboxes selected
   		// value from the selectbox specified in the removeOption tag as elementId/elementName 
   		for (Entry<String,FormGuideTag.ObservedWidget> entry: formGuideTag.getObservedWidgetGroups().entrySet()) {
   			String observeIdParam = getJsElementParam(entry.getValue().getId());
   			String observeNameParam = getJsElementParam(entry.getValue().getName());
   			if (!formGuideTag.isFirstObserveCloned()) {
   				formGuideTag.setFirstObserveCloned(true);
   				// task which clears all removeOption widget options and copies the widget options from
   				// the (first) observe widget, so the removeOption widget starts with a clean slate before
   				// its option(s) removed
	    	    formGuideTag.addJavascriptCallback(
	    	    		UiString.simpleConstruct(
	    	            "{0}(domEvent, {1}, {2}, {3}, {4});",
	    	            new String[] { CLONE_OPTION_CALLBACK_METHOD, observeIdParam, observeNameParam, (this.elementIds != null ? idOrNameParam : null), (this.elementNames != null ? idOrNameParam : null)}),
	    	            null);
   			}
    					
   			// note: there is no undo task for a removeOption child tag, because the do task is invoked for 
   			//       every onchange event (unless changing to the blank option), so it handles everything
    	    formGuideTag.addJavascriptCallback(
    				UiString.simpleConstruct(
   						"{0}(domEvent, {1}, {2}, {3}, {4});",
   						new String[] { REMOVE_OPTION_CALLBACK_METHOD, observeIdParam, observeNameParam, (this.elementIds != null ? idOrNameParam : null), (this.elementNames != null ? idOrNameParam : null)}),
            		null);
   		}		
    }
    
    return EVAL_PAGE;
  }
}
