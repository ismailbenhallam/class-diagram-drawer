package org.mql.application.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.mql.application.business.ReflectionUtilities;
import org.mql.application.models.DrawingClass;
import org.mql.application.swing.components.DrawingPane;
import org.mql.application.swing.components.Menu;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private DrawingPane drawingPane;
	private JScrollPane scrollPane;
	private JPanel contentPane;
	private Menu menuBar;
	private Map<Integer, Set<DrawingClass>> models;
	private String packageName;
	private static final ImageIcon ICON = new ImageIcon("resources/icons/package.png");

	public MainFrame() {
		super("Diagramme de classe");

		setIconImage(ICON.getImage());
		Icon image = new ImageIcon("resources/icons/package.png");
		SortedSet<String> packages = new TreeSet<>();
		packages.addAll(ReflectionUtilities.getInternalPackages());

		packageName = (String) JOptionPane.showInputDialog(this, "Veuillez saisir le package",
				"diagramme de classes",
				JOptionPane.PLAIN_MESSAGE, image,
				packages.toArray(), packages.toArray()[packages.size() - 1]);
		if (packageName == null) {
			System.exit(0);
		}

		initDrawingClasses();

		contentPane = new JPanel(new BorderLayout());
		drawingPane = new DrawingPane(models);
		scrollPane = new JScrollPane(drawingPane);

		contentPane.add(new JLabel("package : " + packageName, SwingConstants.CENTER), BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		setContentPane(contentPane);

		initMenuBar();
		config();
	}

	private void initDrawingClasses() {
		Map<Integer, Set<Class<?>>> classesTable = ReflectionUtilities.getClassesByInheritanceLevel(packageName);
		models = new Hashtable<>();
		Set<Entry<Integer, Set<Class<?>>>> entrySet = classesTable.entrySet();
		for (Entry<Integer, Set<Class<?>>> entry : entrySet) {
			Set<DrawingClass> set = new HashSet<>();
			for (Class<?> clazz : entry.getValue()) {
				set.add(new DrawingClass(clazz));
			}
			models.put(entry.getKey(), set);
		}
	}

	private void initMenuBar() {
		menuBar = new Menu(new String[] { "Edit", "Initial positions" });
		menuBar.addActionListener("Initial positions", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				drawingPane.returnToInitialPositions();
			}
		});
		setJMenuBar(menuBar);
	}

	private void config() {
		loadColor();
		setBounds(new Rectangle(800, 30, 500, 500));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);
	}

	private void loadColor() {
		Color backgroundColor = Colors.getBackgroundColor();
		drawingPane.setBackground(backgroundColor);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		new MainFrame();
	}
}