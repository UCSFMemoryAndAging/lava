package edu.ucsf.lava.core.view.model;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class BaseRenderObject implements Cloneable, Serializable{
	
	public abstract String generateRenderCode() throws Exception;

	protected Integer x;
	protected Integer y;
	protected Integer width;
	protected Integer height;
	protected Color backgroundColor;
	
	public BaseRenderObject(){
	}
	
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public BaseRenderObject getCopy(){
		// return a complete object copy as implemented in EntityBase class deepClone() method
		// this ensures that any collections present in subclasses are duplicated correctly
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try{	
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			oos = new ObjectOutputStream(bos);
			oos.writeObject(this);   
			oos.flush();             
			ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bin);  
			return (BaseRenderObject)ois.readObject();
		}catch(Exception e){
			throw new InternalError(e.toString());
		}finally{
			try{
				oos.close();
				ois.close();
			}catch(IOException e){}
		}

	}
	
}
