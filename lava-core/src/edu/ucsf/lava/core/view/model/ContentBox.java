package edu.ucsf.lava.core.view.model;

import java.awt.Color;

public abstract class ContentBox extends BaseRenderObject {

	public abstract String generateRenderCode() throws Exception;
	
	protected Integer margin;
	protected Integer padding;
	protected Integer borderWidth;
	protected String content;
	protected LavaBorderStyle borderStyle;
	protected Color borderColor;
	protected String contentOverflow;
	

	public ContentBox(){
		super();
	}

	public Integer getMargin() {
		return margin;
	}

	public void setMargin(Integer margin) {
		this.margin = margin;
	}

	public Integer getPadding() {
		return padding;
	}

	public void setPadding(Integer padding) {
		this.padding = padding;
	}

	public Integer getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LavaBorderStyle getBorderStyle() {
		return borderStyle;
	}

	public void setBorderStyle(LavaBorderStyle borderStyle) {
		this.borderStyle = borderStyle;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public String getContentOverflow() {
		return contentOverflow;
	}

	public void setContentOverflow(String contentOverflow) {
		this.contentOverflow = contentOverflow;
	}
	
	// convenience method to get total width of frame including margin, border and padding
	public Integer getFrameWidth(){
		Integer b, m, p;
		b = borderWidth==null ? 0 : borderWidth;
		m = margin==null ? 0 : margin;
		p = padding==null ? 0 : padding;
		return b + m + p;
	}
	
	// convenience methods to access total width/height of content box including frame
	public Integer getBoxWidth(){
		if (width==null){return null;}
		return width + this.getFrameWidth()*2;
	}
	public void setBoxWidth(Integer boxWidth){
		Integer w = boxWidth - this.getFrameWidth()*2;
		this.width = w>0 ? w : 0;
	}
	public Integer getBoxHeight(){
		if (height==null){return null;}
		return height + this.getFrameWidth()*2;
	}
	public void setBoxHeight(Integer boxHeight){
		Integer h = boxHeight - this.getFrameWidth()*2;
		this.height = h>0 ? h : 0;
	}
	
}
