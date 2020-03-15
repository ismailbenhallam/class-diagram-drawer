package org.mql.application.swing.components;

import static org.mql.application.swing.SwingUtilities.italicFont;
import static org.mql.application.swing.SwingUtilities.lineHeight;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;

import org.mql.application.business.ReflectionUtilities;
import org.mql.application.models.DrawingClass;
import org.mql.application.models.DrawingLine;
import org.mql.application.models.DrawingString;
import org.mql.application.swing.Colors;
import org.mql.application.swing.listeners.MoveComponentListener;

public class ClassPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String INTERFACE_LABEL = "<<interface>>";
	public static final String ENUMERATION_LABEL = "<<enumeration>>";
	public static final String ANNOTATION_LABEL = "<<annotation>>";
	private DrawingClass model;
	private Color classForegroundColor;
	private Font font;

	private int totalWidth;
	private int totalHeight;
	private FontMetrics metrics;
	private List<DrawingString> drawingStrings;
	private List<DrawingLine> drawingLines;
	private int caracterWidth;
	private int spaceWidth;
	private int classTypeLabelWidth;
	private int classTypeLabelHeight;
	private Area initialArea;

	public ClassPanel(DrawingClass model, Point position) {
		setLayout(null);
		this.model = model;
		setLocation(position);

		drawingStrings = new ArrayList<>();
		drawingLines = new ArrayList<>();
		spaceWidth = 1;
		font = getFont();
		metrics = getFontMetrics(font);

		setColors();
		MouseInputListener listener = new MoveComponentListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		init();
	}

	private void setColors() {
		setBackground(Colors.getConcreteClassBackgroundColor());
		classForegroundColor = Colors.getClassForegroundColor();
		setBorder(new LineBorder(classForegroundColor));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(classForegroundColor);

		for (DrawingString drawingString : drawingStrings) {
			drawingString.draw(g);
		}

		for (DrawingLine drawingLine : drawingLines) {
			drawingLine.draw(g);
		}

	}

	private void init() {
		totalHeight = lineHeight(metrics);
		totalWidth = 0;
		caracterWidth = metrics.stringWidth("w");
		Class<?> clazz = model.getCls();

		int classNameHeight = metrics.getMaxAscent();
		String className = ReflectionUtilities.getClassName(clazz);
		int classNameWidth = metrics.stringWidth(className);

		// If it's not an an annotation, an interface or an enum
		boolean isNormalClass = true;
		String classTypelabel = "";
		if (clazz.isAnnotation()) {
			setBackground(Colors.getAnnotationBackgroundColor());
			classTypeLabelWidth = metrics.stringWidth(ANNOTATION_LABEL);
			classTypeLabelHeight = classNameHeight;
			isNormalClass = false;
			classTypelabel = ANNOTATION_LABEL;
		}
		// Si c'est une interface
		else if (clazz.isInterface()) {
			setBackground(Colors.getInterfaceBackgroundColor());
			classTypeLabelWidth = metrics.stringWidth(INTERFACE_LABEL);
			classTypeLabelHeight = classNameHeight;
			isNormalClass = false;
			classTypelabel = INTERFACE_LABEL;

			// Si c'est une enumertaion
		} else if (clazz.isEnum()) {
			setBackground(Colors.getEnumBackgroundColor());
			classTypeLabelWidth = metrics.stringWidth(ENUMERATION_LABEL);
			classTypeLabelHeight = classNameHeight;
			isNormalClass = false;
			classTypelabel = ENUMERATION_LABEL;
		}

		if (!isNormalClass) {
			classNameHeight += lineHeight(metrics);
			totalHeight += lineHeight(metrics);
			totalWidth = classTypeLabelWidth;
		}

		// Si la classe est abstraite, on écrit son nom en Italique
		if (ReflectionUtilities.isAbstract(clazz)) {
			if (!clazz.isInterface()) {
				setBackground(Colors.getAbstractClassBackgroundColor());
			}
			classNameWidth = metrics.stringWidth(className);
		}

		totalWidth = Math.max(classNameWidth, totalWidth);

		// Fields
		initFields();

		totalHeight += metrics.getMaxDescent() * 2;
		int fieldsHeight = totalHeight;

		// Methods
		initMethods();

		totalWidth += caracterWidth;
		totalHeight += metrics.getMaxDescent() * 2;

		// Draw : <<enumeration>> or <<interface>> or <<interface>>
		if (!isNormalClass) {
			drawingStrings.add(new DrawingString(classTypelabel, (totalWidth - classTypeLabelWidth) / 2,
					classTypeLabelHeight));
		}

		// Draw class name in italic if the class is abstract
		if (ReflectionUtilities.isAbstract(model.getCls())) {
			drawingStrings.add(new DrawingString(className, (totalWidth - classNameWidth) / 2,
					classNameHeight, italicFont(font)));
		} else {
			drawingStrings.add(new DrawingString(className, (totalWidth - classNameWidth) / 2,
					classNameHeight));
		}

		// Line After className and type..
		Point startPoint;
		Point endPoint;
		if (isNormalClass) {
			startPoint = new Point(0, lineHeight(metrics));
			endPoint = new Point(totalWidth, lineHeight(metrics));
		} else {
			startPoint = new Point(0, lineHeight(metrics) * 2);
			endPoint = new Point(totalWidth, lineHeight(metrics) * 2);
		}
		drawingLines.add(new DrawingLine(startPoint, endPoint));

		// Draw Line after Fields declaration
		drawingLines.add(new DrawingLine(0, fieldsHeight, totalWidth, fieldsHeight));

		initialArea = new Area(new Rectangle(getLocation(), new Dimension(totalWidth, totalHeight)));
		setSize(initialArea.getSize());
		model.setArea(initialArea);
	}

	private void initMethods() {
		for (Method m : model.getCls().getDeclaredMethods()) {
			totalHeight += lineHeight(metrics);

			String methodInfo = ReflectionUtilities.getMethodInfo(m);

			int methodWidth;

			if (ReflectionUtilities.isAbstract(m)) {
				methodWidth = metrics.stringWidth(methodInfo);
				drawingStrings.add(new DrawingString(methodInfo, caracterWidth / 2, totalHeight, italicFont(font)));
			} else {
				methodWidth = metrics.stringWidth(methodInfo);
				drawingStrings.add(new DrawingString(methodInfo, caracterWidth / 2, totalHeight));
			}
			totalWidth = Math.max(totalWidth, methodWidth);

			// Draw a line if it's a static Method
			if (ReflectionUtilities.isStatic(m)) {
				Point startPoint = new Point(caracterWidth / 2,
						totalHeight + metrics.getDescent() - spaceWidth);
				Point endPoint = new Point(caracterWidth / 2 + metrics.stringWidth(methodInfo),
						totalHeight + metrics.getDescent() - spaceWidth);

				drawingLines.add(new DrawingLine(startPoint, endPoint));

			}
		}
	}

	private void initFields() {
		for (Field f : model.getCls().getDeclaredFields()) {
			totalHeight += lineHeight(metrics);
			String fieldInfo = ReflectionUtilities.getFieldInfo(f);
			int fieldWidth = metrics.stringWidth(fieldInfo);
			totalWidth = Math.max(totalWidth, fieldWidth);

			drawingStrings.add(new DrawingString(fieldInfo, caracterWidth / 2, totalHeight));

			// Draw a line if it's a static Field
			if (ReflectionUtilities.isStatic(f)) {
				Point startPoint = new Point(caracterWidth / 2, totalHeight + metrics.getDescent() - spaceWidth);
				Point endPoint = new Point(caracterWidth / 2 + metrics.stringWidth(fieldInfo),
						totalHeight + metrics.getDescent() - spaceWidth);

				drawingLines.add(new DrawingLine(startPoint, endPoint));
			}
		}
	}

	public DrawingClass getModel() {
		return model;
	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		Rectangle area = model.getArea();
		area.setLocation(getLocation());
		model.setArea(area);
	}

	@Override
	public void setLocation(Point p) {
		this.setLocation(p.x, p.y);
		Rectangle area = model.getArea();
		area.setLocation(getLocation());
		model.setArea(area);
	}

	@Override
	public void setBounds(Rectangle r) {
		super.setBounds(r);
		model.setArea(r);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		model.setArea(new Rectangle(x, y, width, height));
	}

	public Area getInitialArea() {
		return initialArea;
	}

}