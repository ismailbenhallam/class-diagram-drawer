package org.mql.application.swing.utilities;

import java.awt.Font;
import java.awt.FontMetrics;

public class SwingUtilities {

	private SwingUtilities() {
	}

	public static Font italicFont(Font f) {
		return new Font(f.getFamily(), f.getStyle() | Font.ITALIC, f.getSize());
	}

	// Juste pour calculer la hauteur d'une ligne, selon un FontMetrics
	public static int lineHeight(FontMetrics metrics) {
		return metrics.getMaxAscent() + metrics.getMaxDescent();
	}

}
