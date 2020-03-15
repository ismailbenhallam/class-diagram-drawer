package org.mql.application.swing.components;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

public class Area extends Rectangle {
	private static final long serialVersionUID = 1L;
	private Point upperMidpoint;
	private Point rightMidpoint;
	private Point lowerMidpoint;
	private Point leftMidpoint;
	private Point centerPoint;
	private Point[] points;
	private Map<Point, PointLocation> pointsLocations;

	public enum PointLocation {
		UP, LEFT, RIGHT, DOWN;
	}

	public Area(Rectangle rectangle) {
		super(rectangle);
		centerPoint = new Point(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
		upperMidpoint = new Point(rectangle.x + rectangle.width / 2, rectangle.y);
		lowerMidpoint = new Point(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height);
		leftMidpoint = new Point(rectangle.x, rectangle.y + rectangle.height / 2);
		rightMidpoint = new Point(rectangle.x + rectangle.width, rectangle.y + rectangle.height / 2);

		points = new Point[] { upperMidpoint, lowerMidpoint, leftMidpoint, rightMidpoint };

		pointsLocations = new HashMap<>();
		pointsLocations.put(upperMidpoint, PointLocation.UP);
		pointsLocations.put(lowerMidpoint, PointLocation.DOWN);
		pointsLocations.put(rightMidpoint, PointLocation.RIGHT);
		pointsLocations.put(leftMidpoint, PointLocation.LEFT);
	}

	public PointLocation getPointLocation(Point p) {
		return pointsLocations.get(p);
	}

	public void setPointsLocations(Map<Point, PointLocation> pointsLocations) {
		this.pointsLocations = pointsLocations;
	}

	public Area(int x, int y, int width, int height) {
		this(new Rectangle(x, y, width, height));
	}

	public Point getUpperMidpoint() {
		return upperMidpoint;
	}

	public Point getRightMidpoint() {
		return rightMidpoint;
	}

	public Point getLowerMidpoint() {
		return lowerMidpoint;
	}

	public Point getLeftMidpoint() {
		return leftMidpoint;
	}

	public Point getCenterPoint() {
		return centerPoint;
	}

	public Point[] getPoints() {
		return points;
	}

}
