package edu.ucsf.lava.core.view.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class CssBox extends ContentBox {


	protected String selectorStr;
	protected String classStr;
	protected String positionAttribute;
	protected String floatAttribute;
	protected String clearAttribute;
	protected String divId;
	protected String title;
	protected String url;
	protected Integer zIndex;
	protected List<CssBox> nestedCssBoxes;

	public CssBox() {
		super();
		this.content="";
		nestedCssBoxes = new ArrayList<CssBox>();
	}

	public String getSelectorStr() {
		return selectorStr;
	}

	public void setSelectorStr(String selectorStr) {
		this.selectorStr = selectorStr;
	}

	public String getClassStr() {
		return classStr;
	}

	public void setClassStr(String classStr) {
		this.classStr = classStr;
	}

	public String getPositionAttribute() {
		return positionAttribute;
	}

	public void setPositionAttribute(String positionAttribute) {
		this.positionAttribute = positionAttribute;
	}

	public String getFloatAttribute() {
		return floatAttribute;
	}

	public void setFloatAttribute(String floatAttribute) {
		this.floatAttribute = floatAttribute;
	}

	public String getClearAttribute() {
		return clearAttribute;
	}

	public void setClearAttribute(String clearAttribute) {
		this.clearAttribute = clearAttribute;
	}

	public String getDivId() {
		return divId;
	}

	public void setDivId(String divId) {
		this.divId = divId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Integer getzIndex() {
		return zIndex;
	}

	public void setzIndex(Integer zIndex) {
		this.zIndex = zIndex;
	}

	public void addNestedCssBox(CssBox nestedCssBox){
		this.nestedCssBoxes.add(nestedCssBox);
	}

	// render methods
	// generate code for a div tag
	@Override
	public String generateRenderCode() throws Exception {

		StringBuffer renderCode = new StringBuffer();

		// open div tag
		renderCode.append("<div ");

		// div id
		if (divId!=null){
			renderCode.append("id=\"").append(divId).append("\" ");
		}
		//class listing
		if (classStr!=null){
			renderCode.append("class=\"").append(classStr).append("\" ");
		}
		// title
		if (title!=null){
			renderCode.append("title=\"").append(title).append("\" ");
		}
		// url link
		if (url!=null){
			renderCode.append("onclick=\"window.location.href ='").append(url).append("'\" ");
		}
		// style attribute includes any non-null properties
		renderCode.append(" style=\"");
		if (this.positionAttribute!=null){
			renderCode.append("position: ").append(positionAttribute).append("; ");
		} else if (this.x!=null || this.y!=null){
			renderCode.append("position: absolute; ");
		}
		if (this.floatAttribute!=null){
			renderCode.append("float: ").append(floatAttribute).append("; ");
		}
		if (this.clearAttribute!=null){
			renderCode.append("clear: ").append(clearAttribute).append("; ");
		}
		if (this.x!=null){
			renderCode.append("left: ").append(x).append("px; ");
		}
		if (this.y!=null){
			renderCode.append("top: ").append(y).append("px; ");
		}
		if (this.padding!=null){
			renderCode.append("padding: ").append(padding).append("px; ");
		}
		if (this.margin!=null){
			renderCode.append("margin: ").append(margin).append("px; ");
		}
		if (this.width!=null){
			renderCode.append("width: ").append(width).append("px; ");
		}
		if (this.height!=null){
			renderCode.append("height: ").append(height).append("px; ");
		}
		if (this.backgroundColor!=null){
			renderCode.append("background-color: #").append(Integer.toHexString(backgroundColor.getRGB() & 0x00ffffff)).append("; ");
		}
		if (this.borderWidth!=null){
			renderCode.append("border-width: ").append(borderWidth).append("px; ");
		}
		if (this.borderStyle!=null){
			renderCode.append("border-style: ").append(borderStyle.toString().toLowerCase()).append("; ");	
		}
		if (this.borderColor!=null){
			renderCode.append("border-color: #").append(Integer.toHexString(borderColor.getRGB() & 0x00ffffff)).append("; ");
		}
		if (this.contentOverflow!=null){
			renderCode.append("overflow: ").append(contentOverflow).append("; ");
		}
		if (url!=null){
			renderCode.append("cursor: pointer;");
		}
		if (zIndex!=null){
			renderCode.append("z-index: ").append(zIndex).append("; ");
		}
		// close style attribute 
		renderCode.append("\">");

		// nested boxes
		for (CssBox cssBox : nestedCssBoxes){
			renderCode.append(cssBox.generateRenderCode());
		}
		
		// box content string
		renderCode.append(this.content);

		// close div tag
		renderCode.append("</div>");
		
		return renderCode.toString();
	}


}
