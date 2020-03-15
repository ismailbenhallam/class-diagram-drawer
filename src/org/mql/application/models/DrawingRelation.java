package org.mql.application.models;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;

import org.mql.application.business.ConfigReader;
import org.mql.application.models.Relation.RelationType;
import org.mql.application.swing.components.Area;
import org.mql.application.swing.components.Area.PointLocation;

public class DrawingRelation {
	private static int rib;
	private static final int DEFAULT_TRIANGLE_RIB = 15;
	private Point startPoint;
	private Point endPoint;
	private Point newEndPoint;

	private Polygon lozenge;
	private Polygon triangle;

	private PointLocation pointLocation;
	private RelationType relationType;
	private Area area;

	static {
		try {
			rib = Integer.parseInt(
					ConfigReader.getProperty("DrawingRelation.SUB_LINE_RIB", String.valueOf(DEFAULT_TRIANGLE_RIB)));
		} catch (NumberFormatException e) {
			rib = DEFAULT_TRIANGLE_RIB;
		}
	}

	public DrawingRelation(Point startPoint, Point endPoint, PointLocation pointLocation, RelationType relationType) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.relationType = relationType;
		this.pointLocation = pointLocation;
	}

	public DrawingRelation(Area area) {
		this.area = area;
		this.relationType = RelationType.AGGREGATION;
	}

	public void drawRelation(Graphics g) {
		switch (relationType) {
			case INHERITANCE:
				drawInheritance(g);
				break;
			case AGGREGATION:
				if (area != null) {
					drawSelfAggregation(g);
				} else {
					drawAggregation(g);
				}
				break;
			case IMPLEMENTATION:
				drawImplementation(g);
				break;
			default:
				break;
		}
	}

	private void drawSelfAggregation(Graphics g) {
		Point p1 = area.getRightMidpoint();
		int x = p1.x;
		int y = p1.y;
		lozenge = new Polygon(new int[] { x, x + rib, x + rib * 2, x + rib },
				new int[] { y, y + rib / 2, y, y - rib / 2 }, 4);
		g.drawPolygon(lozenge);

		/***************/
		Point p2 = area.getRightMidpoint();
		p2.x += rib * 2;

		p1 = (Point) p2.clone();
		p1.y -= area.getHeight() / 2 + rib;
		DrawingLine line = new DrawingLine(p1, p2);
		line.draw(g);

		p2 = (Point) p1.clone();
		p2.x = area.getUpperMidpoint().x;
		line = new DrawingLine(p1, p2);
		line.draw(g);

		p1 = area.getUpperMidpoint();
		line = new DrawingLine(p1, p2);
		line.draw(g);
	}

	private void drawAggregation(Graphics g) {
		initLozenge();
		g.drawLine(startPoint.x, startPoint.y, newEndPoint.x, newEndPoint.y);
		if (lozenge == null) {
			return;
		}
		g.drawPolygon(lozenge);
	}

	private void initLozenge() {
		newEndPoint = (Point) endPoint.clone();
		int x = endPoint.x;
		int y = endPoint.y;
		if (pointLocation == null) {
			return;
		}
		switch (pointLocation) {
			case UP:
				lozenge = new Polygon(new int[] { x, x + rib / 2, x, x - rib / 2 },
						new int[] { y, y - rib, y - 2 * rib, y - rib }, 4);
				newEndPoint.y -= 2 * rib;
				break;
			case DOWN:
				lozenge = new Polygon(new int[] { x, x + rib / 2, x, x - rib / 2 },
						new int[] { y, y + rib, y + 2 * rib, y + rib }, 4);
				newEndPoint.y += 2 * rib;
				break;
			case RIGHT:
				lozenge = new Polygon(new int[] { x, x + rib, x + rib * 2, x + rib },
						new int[] { y, y + rib / 2, y, y - rib / 2 }, 4);
				newEndPoint.x += 2 * rib;
				break;
			case LEFT:
				lozenge = new Polygon(new int[] { x, x - rib, x - rib * 2, x - rib },
						new int[] { y, y - rib / 2, y, y + rib / 2 }, 4);
				newEndPoint.x -= 2 * rib;
				break;
		}
	}

	// Dessiner le trait pointillé + flèche qui exprime l'héritage dans le langage
	private void drawImplementation(Graphics g) {
		newEndPoint = (Point) endPoint.clone();
		initTriangle();

		drawDashedLine(g, startPoint, newEndPoint);
		g.drawPolygon(triangle);
	}

	// Dessiner le trait + flèche qui exprime l'héritage dans le langage UML
	private void drawInheritance(Graphics g) {
		newEndPoint = (Point) endPoint.clone();
		initTriangle();

		g.drawLine(startPoint.x, startPoint.y, newEndPoint.x, newEndPoint.y);
		g.drawPolygon(triangle);
	}

	// Initialize les points du triangle à dessiner selon le PointLocation
	private void initTriangle() {
		newEndPoint = (Point) endPoint.clone();
		int x = endPoint.x;
		int y = endPoint.y;
		switch (pointLocation) {
			case UP:
				triangle = new Polygon(new int[] { x, x + rib / 2, x - rib / 2 },
						new int[] { y, y - rib, y - rib }, 3);
				newEndPoint.y -= rib;
				break;
			case DOWN:
				triangle = new Polygon(new int[] { x, x - rib / 2, x + rib / 2 },
						new int[] { y, y + rib, y + rib }, 3);
				newEndPoint.y += rib;
				break;
			case RIGHT:
				triangle = new Polygon(new int[] { x, x + rib, x + rib },
						new int[] { y, y + rib / 2, y - rib / 2 }, 3);
				newEndPoint.x += rib;
				break;
			case LEFT:
				triangle = new Polygon(new int[] { x, x - rib, x - rib },
						new int[] { y, y - rib / 2, y + rib / 2 }, 3);
				newEndPoint.x -= rib;
				break;
		}
	}

	/***************************/
	private static void drawDashedLine(Graphics g, Point startPoint, Point endPoint) {
		Graphics2D g2d = (Graphics2D) g.create();
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
		g2d.setStroke(dashed);
		g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
		g2d.dispose();
	}
}