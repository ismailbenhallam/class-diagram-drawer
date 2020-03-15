package org.mql.application.models;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Objects;

import org.mql.application.swing.components.Area;

public class DrawingClass {
	private Class<?> cls;
	private Area area;

	public DrawingClass() {
		super();
	}

	public DrawingClass(Class<?> cls) {
		super();
		this.cls = cls;
	}

	public DrawingClass(Class<?> classToDraw, Rectangle rectangle) {
		super();
		this.cls = classToDraw;
		this.area = new Area(rectangle);
	}

	public Class<?> getCls() {
		return cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}

	public void setArea(Rectangle area) {
		this.area = new Area(area);
	}

	public Area getArea() {
		return area;
	}

	public Point getUpperMidpoint() {
		return area.getUpperMidpoint();
	}

	public Point getRightMidpoint() {
		return area.getRightMidpoint();
	}

	public Point getLowerMidpoint() {
		return area.getLowerMidpoint();
	}

	public Point getLeftMidpoint() {
		return area.getLeftMidpoint();
	}

	public Point getCenterPoint() {
		return area.getCenterPoint();
	}

	@Override
	public int hashCode() {
		return Objects.hash(area, cls);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DrawingClass)) {
			return false;
		}
		DrawingClass other = (DrawingClass) obj;
		return Objects.equals(cls, other.cls) && Objects.equals(area, other.area);
	}

}