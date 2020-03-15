package org.mql.application.swing.listeners;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import org.mql.application.swing.components.DrawingPane;

public class MoveComponentListener extends MouseInputAdapter {
	private Point mouseInitialPoint;
	private Point componentInitialPoint;

	@Override
	public void mousePressed(MouseEvent e) {
		mouseInitialPoint = e.getLocationOnScreen();
		componentInitialPoint = e.getComponent().getLocation();
	}

	// Pour adapter JScrollPane selon le besoin
	@Override
	public void mouseReleased(MouseEvent e) {
		Component c = e.getComponent();
		if (c.getParent() instanceof DrawingPane) {
			((DrawingPane) c.getParent()).reCalculateUsedArea();
		}
	}

	@Override

	public void mouseDragged(MouseEvent e) {
		Component c = e.getComponent();

		int x = componentInitialPoint.x + e.getXOnScreen() - mouseInitialPoint.x;
		int y = componentInitialPoint.y + e.getYOnScreen() - mouseInitialPoint.y;
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		c.setLocation(x, y);

		// Pour redessiner les relations
		c.getParent().repaint();
	}

}
