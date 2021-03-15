package org.mql.application.swing.utilities;

import java.awt.Color;

import org.mql.application.business.ConfigReader;

public class Colors {
    private static final Color CONCRETE_CLASS_BACKGROUND_COLOR = getColorFromConfig("ClassPanel.concreteClassBackgroundColor");
    private static final Color ABSTRACT_CLASS_BACKGROUND_COLOR = getColorFromConfig("ClassPanel.abstractClassBackgroundColor");
    private static final Color INTERFACE_BACKGROUND_COLOR = getColorFromConfig("ClassPanel.interfaceBackgroundColor");
    private static final Color ENUM_BACKGROUND_COLOR = getColorFromConfig("ClassPanel.enumBackgroundColor");
    private static final Color ANNOTATION_BACKGROUND_COLOR = getColorFromConfig("ClassPanel.annotationBackgroundColor");
    private static final Color CLASS_FOREGROUND_COLOR = getColorFromConfig("ClassPanel.classForegroundColor");
    private static final Color BACKGROUND_COLOR = getColorFromConfig("MainFrame.backgroundColor");
    private static final Color INHERITANCE_LINE_COLOR = getColorFromConfig("DrawingPane.inheritanceLineColor");
    private static final Color IMPLEMENTATION_LINE_COLOR = getColorFromConfig("DrawingPane.implementationLineColor");
    private static final Color AGGREGATION_LINE_COLOR = getColorFromConfig("DrawingPane.aggregationLineColor");
    private static final Color COMPOSITION_LINE_COLOR = getColorFromConfig("DrawingPane.compositionLineColor");

    private Colors() {
    }

    public static Color getCompositionLineColor() {
        return COMPOSITION_LINE_COLOR;
    }

    public static Color getAggregationLineColor() {
        return AGGREGATION_LINE_COLOR;
    }

    public static Color getImplementationLineColor() {
        return IMPLEMENTATION_LINE_COLOR;
    }

    public static Color getInheritanceLineColor() {
        return INHERITANCE_LINE_COLOR;
    }

    public static Color getBackgroundColor() {
        return BACKGROUND_COLOR;
    }

    public static Color getConcreteClassBackgroundColor() {
        return CONCRETE_CLASS_BACKGROUND_COLOR;
    }

    public static Color getAbstractClassBackgroundColor() {
        return ABSTRACT_CLASS_BACKGROUND_COLOR;
    }

    public static Color getInterfaceBackgroundColor() {
        return INTERFACE_BACKGROUND_COLOR;
    }

    public static Color getEnumBackgroundColor() {
        return ENUM_BACKGROUND_COLOR;
    }

    public static Color getAnnotationBackgroundColor() {
        return ANNOTATION_BACKGROUND_COLOR;
    }

    public static Color getClassForegroundColor() {
        return CLASS_FOREGROUND_COLOR;
    }

    private static Color getColorFromConfig(String name) {
        String colorString = ConfigReader.getProperty(name);
        try {
            return (Color) Color.class
                .getDeclaredField(colorString)
                .get(null);
        } catch (Exception ignored) {
            try {
                return colorFromHex(colorString);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return Color.WHITE;
            }
        }
    }

    public static Color colorFromHex(String hex) {
        if (!hex.startsWith("#")) {
            hex = "#" + hex;
        }
        return new Color(
            Integer.valueOf(hex.substring(1, 3), 16),
            Integer.valueOf(hex.substring(3, 5), 16),
            Integer.valueOf(hex.substring(5, 7), 16)
        );
    }
}
