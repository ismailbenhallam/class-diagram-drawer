package org.mql.application.swing.components;

import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private static final String PATH = "resources/icons/";
	private static final String EXTENSION = ".gif";
	private final Hashtable<String, JMenuItem> items;

	public Menu(String[]... labels) {
		items = new Hashtable<>();
		for (String[] label : labels) {
			addMenu(label);
		}
	}

	private void addMenu(String[] t) {
		JMenu m = new JMenu(t[0]);
		add(m);
		for (int i = 1; i < t.length; i++) {
			if (t[i].equals("-")) {
				m.addSeparator();
			} else {
				JMenuItem item = new JMenuItem(t[i], new ImageIcon(PATH + t[i] + EXTENSION));
				m.add(item);
				items.put(t[i], item);
			}
		}
	}

	public void addActionListener(String item, ActionListener listener) {
		items.get(item).addActionListener(listener);
	}

}