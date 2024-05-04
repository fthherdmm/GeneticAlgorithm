package main;

public class Rect {
	private int xPos, yPos;
	private int width, height;
	
	public Rect(int width, int height) {
		this.xPos = 0;
		this.yPos = 0;
		this.width = width;
		this.height = height;
	}
	
	public Rect(int x, int y, int width, int height) {
		this.xPos = x;
		this.yPos = y;
		this.width = width;
		this.height = height;
	}
	
	public int getArea() {
		return width * height;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public void setX(int newX) {
		xPos = newX;
	}
	
	public void setY(int newY) {
		yPos = newY;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setWidth(int newWidth) {
		width = newWidth;
	}
	
	public void setHeight(int newHeight) {
		height = newHeight;
	}
}
