package org.mql.application.swing;

import java.awt.Color;

import org.mql.application.business.ConfigReader;

public class Colors {
	private static final String DEFAULT_BACKGROUND_CLASS_COLOR_NAME = "LIGHT_GRAY";
	private static final String DEFAULT_FOREGROUND_COLOR_NAME = "BLACK";
	private static final String DEFAULT_BACKGROUND_COLOR_NAME = "WHITE";

	private static final Color DEFAULT_BACKGROUND_CLASS_COLOR = Color.LIGHT_GRAY;
	private static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
	private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;

	private static Color concreteClassBackgroundColor;
	private static Color abstractClassBackgroundColor;
	private static Color interfaceBackgroundColor;
	private static Color enumBackgroundColor;
	private static Color annotationBackgroundColor;
	private static Color classForegroundColor;
	private static Color backgroundColor;
	private static Color inheritanceLineColor;
	private static Color implementationLineColor;
	private static Color aggregationLineColor;
	private static Color compositionLineColor;

	private Colors() {
	}

	public static Color getCompositionLineColor() {
		if (compositionLineColor == null) {
			try {
				compositionLineColor = (Color) Color.class
						.getDeclaredField(
								ConfigReader.getProperty("DrawingPane.compositionLineColor",
										String.valueOf(DEFAULT_FOREGROUND_COLOR_NAME)))
						.get(null);
			} catch (Exception e) {
				compositionLineColor = DEFAULT_FOREGROUND_COLOR;
			}
		}
		return compositionLineColor;
	}

	public static Color getAggregationLineColor() {
		if (aggregationLineColor == null) {
			try {
				aggregationLineColor = (Color) Color.class
						.getDeclaredField(
								ConfigReader.getProperty("DrawingPane.aggregationLineColor",
										String.valueOf(DEFAULT_FOREGROUND_COLOR_NAME)))
						.get(null);
			} catch (Exception e) {
				aggregationLineColor = DEFAULT_FOREGROUND_COLOR;
			}
		}
		return aggregationLineColor;
	}

	public static Color getImplementationLineColor() {
		if (implementationLineColor == null) {
			try {
				implementationLineColor = (Color) Color.class
						.getDeclaredField(
								ConfigReader.getProperty("DrawingPane.implementationLineColor",
										String.valueOf(DEFAULT_FOREGROUND_COLOR_NAME)))
						.get(null);
			} catch (Exception e) {
				implementationLineColor = DEFAULT_FOREGROUND_COLOR;
			}
		}
		return implementationLineColor;
	}

	public static Color getInheritanceLineColor() {
		if (inheritanceLineColor == null) {
			try {
				inheritanceLineColor = (Color) Color.class
						.getDeclaredField(
								ConfigReader.getProperty("DrawingPane.inheritanceLineColor",
										String.valueOf(DEFAULT_FOREGROUND_COLOR_NAME)))
						.get(null);
			} catch (Exception e) {
				inheritanceLineColor = DEFAULT_FOREGROUND_COLOR;
			}
		}
		return inheritanceLineColor;
	}

	public static Color getBackgroundColor() {
		if (backgroundColor == null) {
			try {
				backgroundColor = (Color) Color.class
						.getDeclaredField(
								ConfigReader.getProperty("MainFrame.backgroundColor",
										String.valueOf(DEFAULT_BACKGROUND_COLOR_NAME)))
						.get(null);
			} catch (Exception e) {
				backgroundColor = DEFAULT_BACKGROUND_COLOR;
			}
		}
		return backgroundColor;
	}

	public static Color getConcreteClassBackgroundColor() {
		if (concreteClassBackgroundColor == null) {
			try {
				concreteClassBackgroundColor = (Color) Color.class
						.getDeclaredField(
								ConfigReader.getProperty("ClassPanel.concreteClassBackgroundColor",
										String.valueOf(DEFAULT_BACKGROUND_CLASS_COLOR_NAME)))
						.get(null);
			} catch (Exception e) {
				concreteClassBackgroundColor = DEFAULT_BACKGROUND_CLASS_COLOR;
			}
		}
		return concreteClassBackgroundColor;
	}

	public static Color getAbstractClassBackgroundColor() {
		if (abstractClassBackgroundColor == null) {
			try {
				abstractClassBackgroundColor = (Color) Color.class
						.getDeclaredField(
								ConfigReader.getProperty("ClassPanel.abstractClassBackgroundColor",
										String.valueOf(DEFAULT_BACKGROUND_CLASS_COLOR_NAME)))
						.get(null);
			} catch (Exception e) {
				abstractClassBackgroundColor = DEFAULT_BACKGROUND_CLASS_COLOR;
			}
		}
		return abstractClassBackgroundColor;
	}

	public static Color getInterfaceBackgroundColor() {
		if (interfaceBackgroundColor == null) {
			try {
				interfaceBackgroundColor = (Color) Color.class
						.getDeclaredField(
								ConfigReader.getProperty("ClassPanel.interfaceBackgroundColor",
										String.valueOf(DEFAULT_BACKGROUND_CLASS_COLOR_NAME)))
						.get(null);
			} catch (Exception e) {
				interfaceBackgroundColor = DEFAULT_BACKGROUND_CLASS_COLOR;
			}
		}
		return interfaceBackgroundColor;
	}

	public static Color getEnumBackgroundColor() {
		if (enumBackgroundColor == null) {
			try {
				enumBackgroundColor = (Color) Color.class
						.getDeclaredField(
								ConfigReader.getProperty("ClassPanel.enumBackgroundColor",
										String.valueOf(DEFAULT_BACKGROUND_CLASS_COLOR_NAME)))
						.get(null);
			} catch (Exception e) {
				enumBackgroundColor = DEFAULT_BACKGROUND_CLASS_COLOR;
			}
		}
		return enumBackgroundColor;
	}

	public static Color getAnnotationBackgroundColor() {
		if (annotationBackgroundColor == null) {
			try {
				annotationBackgroundColor = (Color) Color.class
						.getDeclaredField(
								ConfigReader.getProperty("ClassPanel.annotationBackgroundColor",
										String.valueOf(DEFAULT_BACKGROUND_CLASS_COLOR_NAME)))
						.get(null);
			} catch (Exception e) {
				annotationBackgroundColor = DEFAULT_BACKGROUND_CLASS_COLOR;
			}
		}
		return annotationBackgroundColor;
	}

	public static Color getClassForegroundColor() {
		if (classForegroundColor == null) {
			try {
				classForegroundColor = (Color) Color.class
						.getDeclaredField(
								ConfigReader.getProperty("ClassPanel.classForegroundColor",
										String.valueOf(DEFAULT_FOREGROUND_COLOR_NAME)))
						.get(null);
			} catch (Exception e) {
				classForegroundColor = DEFAULT_FOREGROUND_COLOR;
			}
		}
		return classForegroundColor;
	}

}
