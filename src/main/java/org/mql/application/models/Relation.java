package org.mql.application.models;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import org.mql.application.swing.utilities.Colors;

public class Relation {
	private static final Set<Class<?>> CLASSES_WITH_SELF_AGGREGATION = new HashSet<>();

	public enum RelationType {
		INHERITANCE, IMPLEMENTATION, AGGREGATION
	}

	private static final Color inheritanceLineColor = Colors.getInheritanceLineColor();
	private static final Color implementationLineColor = Colors.getImplementationLineColor();
	private static final Color aggregationLineColor = Colors.getAggregationLineColor();
	private final DrawingClass from;
	private final DrawingClass to;
	private final RelationType type;
	private Point startPoint;
	private Point endPoint;

	public Relation(DrawingClass from, DrawingClass to, RelationType type) {
		super();
		this.from = from;
		this.to = to;
		this.type = type;

		prepareLine();
	}

	private void prepareLine() {
		startPoint = new Point(from.getArea().getUpperMidpoint().x, from.getArea().getUpperMidpoint().y);
		endPoint = new Point(to.getArea().getLowerMidpoint().x, to.getArea().getLowerMidpoint().y);

		// Pour determiner les points les plus proches pour dessiner la relation
		double d = startPoint.distance(endPoint);
		for (Point p1 : from.getArea().getPoints()) {
			for (Point p2 : to.getArea().getPoints()) {
				if (p1.distance(p2) < d) {
					d = p1.distance(p2);
					startPoint = p1;
					endPoint = p2;
				}
			}
		}
	}

	public DrawingClass getFrom() {
		return from;
	}

	public DrawingClass getTo() {
		return to;
	}

	public RelationType getType() {
		return type;
	}

	public void updateLine() {
		prepareLine();
	}

	public void draw(Graphics g) {
		Color c = g.getColor();

		if (type == RelationType.INHERITANCE) {
			g.setColor(inheritanceLineColor);
			new DrawingRelation(startPoint, endPoint, to.getArea().getPointLocation(endPoint), type).drawRelation(g);
		} else if (type == RelationType.IMPLEMENTATION) {
			g.setColor(implementationLineColor);
			new DrawingRelation(startPoint, endPoint, to.getArea().getPointLocation(endPoint), type).drawRelation(g);
		} else if (type == RelationType.AGGREGATION) {
			g.setColor(aggregationLineColor);
			if (from.getCls().equals(to.getCls())) {
				if (!CLASSES_WITH_SELF_AGGREGATION.contains(from.getCls())) {
					new DrawingRelation(from.getArea())
							.drawRelation(g);
					CLASSES_WITH_SELF_AGGREGATION.add(from.getCls());
				}
			} else {
				new DrawingRelation(startPoint, endPoint, to.getArea().getPointLocation(endPoint), type)
						.drawRelation(g);
			}
		}
		g.setColor(c);
	}

	public static void clearClassesWithSelfAggregation() {
		CLASSES_WITH_SELF_AGGREGATION.clear();
	}

}