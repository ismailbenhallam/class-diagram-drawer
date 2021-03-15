package org.mql.application.swing.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JPanel;

import org.mql.application.business.ConfigReader;
import org.mql.application.business.ReflectionUtilities;
import org.mql.application.models.DrawingClass;
import org.mql.application.models.Relation;
import org.mql.application.models.Relation.RelationType;

public class DrawingPane extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Set<Relation> relations;
	private final Set<DrawingClass> classesAlreadyDrawn;
	private final int spaceBetweenClasses;
	private Dimension usedArea;
	private Map<Integer, Set<ClassPanel>> panelsTable;

	public DrawingPane(Map<Integer, Set<DrawingClass>> models) {
		setLayout(null);
		relations = new HashSet<>();
		classesAlreadyDrawn = new HashSet<>();
		usedArea = new Dimension();

		spaceBetweenClasses = Integer
			.parseInt(ConfigReader.getProperty("DrawingPane.spaceBetweenClasses", "50"));

		initClassPanels(models);
		prepareRelations();
	}

	/*
	 * Cr�er les ClassPanel et definir leurs locations selons les niveaux
	 * (h�ritage), et les ajouter au Panel principal
	 */
	private void initClassPanels(Map<Integer, Set<DrawingClass>> models) {
		panelsTable = new Hashtable<>();
		Set<ClassPanel> set;

		// To order the Map
		Map<Integer, Set<DrawingClass>> tmp = new TreeMap<>((o1, o2) -> o1 - o2);
		tmp.putAll(models);
		models = tmp;

		Point p = new Point(spaceBetweenClasses, spaceBetweenClasses);
		usedArea = getSize();

		int heightRow = 0;
		for (Entry<Integer, Set<DrawingClass>> levelDrawingModel : models.entrySet()) {
			set = new HashSet<>();
			for (DrawingClass drawingClass : levelDrawingModel.getValue()) {
				ClassPanel panel = new ClassPanel(drawingClass, p);

				set.add(panel);
				add(panel);

				classesAlreadyDrawn.add(drawingClass);

				usedArea.width = Math.max(usedArea.width, panel.getSize().width + panel.getLocation().x);
				usedArea.height = Math.max(usedArea.height, panel.getSize().height + panel.getLocation().y);

				p.x += panel.getWidth() + spaceBetweenClasses;
				heightRow = Math.max(heightRow, panel.getSize().height + spaceBetweenClasses);
			}
			panelsTable.put(levelDrawingModel.getKey(), set);

			p.x = spaceBetweenClasses;
			p.y += heightRow;
			heightRow = 0;
		}

		usedArea.width += spaceBetweenClasses;
		usedArea.height += spaceBetweenClasses;
		setPreferredSize(usedArea);
	}

	/*
	 * Initialiser le Set des Relations une seule fois, et les dessiner, � chaque
	 * repaint
	 */
	private void prepareRelations() {
		relations.clear();
		Set<Class<?>> interfaces = null;
		boolean isImplementing;
		for (DrawingClass model : classesAlreadyDrawn) {

			// Aggregation
			Field[] fields = model.getCls().getDeclaredFields();
			if (fields.length > 0) {
				for (Field f : fields) {
					for (DrawingClass composant : classesAlreadyDrawn) {
						if (f.getType().isPrimitive()) {
							continue;
						}

						// Decorator
						if (f.getType().equals(composant.getCls())) {
							relations.add(new Relation(composant, model, RelationType.AGGREGATION));

							// Composite : List
						} else if (f.getGenericType() instanceof ParameterizedType) {
							ParameterizedType parameterizedType = (ParameterizedType) f.getGenericType();
							// Si c'est une collection et qu'elle est param�tr�e
							if (Collection.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())
								&& parameterizedType.getActualTypeArguments().length > 0
								&& parameterizedType.getActualTypeArguments()[0] instanceof Class<?>
								&& parameterizedType.getActualTypeArguments()[0]
								.equals(composant.getCls())) {
								relations.add(new Relation(composant, model, RelationType.AGGREGATION));
							}
							// Composite : []
						} else if (f.getType().isArray()) {
							Class<?> c = ReflectionUtilities.getClassFromArrayClass(f.getType());
							if (c != null && c.equals(composant.getCls())) {
								relations.add(new Relation(composant, model, RelationType.AGGREGATION));
							}
						}

					}
				}
			}

			/*
			 * flag pour indiquer que cette class/interface h�rite/implemente une/des
			 * interfaces(s)
			 */
			isImplementing = false;
			if (model.getCls().getInterfaces().length > 0) {
				interfaces = new HashSet<>(Arrays.asList(model.getCls().getInterfaces()));
				isImplementing = true;
			}

			for (DrawingClass supModel : classesAlreadyDrawn) {
				// Si on a trouv� la superclass de cette classe
				if (model.getCls().getSuperclass() != null
					&& model.getCls().getSuperclass().equals(supModel.getCls())) {
					relations.add(new Relation(model, supModel, RelationType.INHERITANCE));
					// Si cette class/interface h�rite/implemente une/des interfaces(s)
				} else if (isImplementing && interfaces.contains(supModel.getCls())) {
					// Si c'est une interface qui h�rite d'une autre, le type de relation est :
					// h�ritage
					if (model.getCls().isInterface()) {
						relations.add(new Relation(model, supModel, RelationType.INHERITANCE));
					}
					// Si c'est une class qui h�rite d'une interface, le type de relation est :
					// implementation
					else {
						relations.add(new Relation(model, supModel, RelationType.IMPLEMENTATION));
					}
				}
			}
		}
	}

	/* Mettre � jour les relations pour pouvoir dessiner les lines convenablement */

	private void updateRelations() {
		for (Relation r : relations) {
			r.updateLine();
		}
	}

	/* Recalculer la zone utilis�, pour pouvoir l'adapter avec JScrollPane */

	public void reCalculateUsedArea() {
		Dimension d = new Dimension();
		Rectangle tmp;
		for (DrawingClass drawingClass : classesAlreadyDrawn) {
			tmp = drawingClass.getArea();
			d.width = Math.max(tmp.width + tmp.x, d.width);
			d.height = Math.max(tmp.height + tmp.y, d.height);
		}
		usedArea = d;
		usedArea.width += spaceBetweenClasses;
		usedArea.height += spaceBetweenClasses;
		setPreferredSize(usedArea);
		revalidate();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Relation r : relations) {
			r.draw(g);
		}
	}

	@Override
	public void repaint() {
		Relation.clearClassesWithSelfAggregation();
		if (relations != null && !relations.isEmpty()) {
			updateRelations();
		}
		super.repaint();
	}

	public void returnToInitialPositions() {
		for (Entry<Integer, Set<ClassPanel>> panelsSet : panelsTable.entrySet()) {
			for (ClassPanel p : panelsSet.getValue()) {
				p.setLocation(p.getInitialArea().getLocation());
			}
		}
		repaint();
	}

}