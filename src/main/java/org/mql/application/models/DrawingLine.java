package org.mql.application.models;

import java.awt.Graphics;
import java.awt.Point;

public class DrawingLine {
	private final Point startPoint;
	private final Point endPoint;

	public DrawingLine(Point startPoint, Point endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	public DrawingLine(int startPointX, int startPointY, int endPointX, int endPointY) {
		this.startPoint = new Point(startPointX, startPointY);
		this.endPoint = new Point(endPointX, endPointY);
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public int getStartX() {
		return startPoint.x;
	}

	public int getStartY() {
		return startPoint.y;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public int getEndX() {
		return endPoint.x;
	}

	public int getEndY() {
		return endPoint.y;
	}

	public void draw(Graphics g) {
		g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
	}

}