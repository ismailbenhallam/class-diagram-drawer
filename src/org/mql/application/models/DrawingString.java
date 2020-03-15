package org.mql.application.models;

import java.awt.Font;
import java.awt.Graphics;

public class DrawingString {
	private String string;
	private int x;
	private int y;
	private Font font;

	public DrawingString(String string, int x, int y) {
		this.string = string;
		this.x = x;
		this.y = y;
	}

	public DrawingString(String string, int x, int y, Font font) {
		this(string, x, y);
		this.font = font;
	}

	public DrawingString() {
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void draw(Graphics g) {
		if (font != null) {
			Font tmp = g.getFont();
			g.setFont(font);
			g.drawString(string, x, y);
			g.setFont(tmp);
		} else {
			g.drawString(string, x, y);
		}
	}

}