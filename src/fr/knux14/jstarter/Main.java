package fr.knux14.jstarter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	public static String _VERSION = "1.0";
	public static Main instance; // LOL I NO DIS IZ NO OOP
	public static MovingIcon miInstance; // LOL I NO DIS IZ NO TOO BUT I DNT
											// CARE
	public static ArrayList<Program> progList = new ArrayList<>();
	public static BufferedImage icon;

	private JMenuBar menuBar;
	private JMenu file;
	private JMenuItem addApp, remApp, about, exit;
	private JList<Program> appList;

	public Main() {
		setSize(300, 400);
		setTitle("JStarter");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(0);
		setIconImage(icon);

		menuBar = new JMenuBar();
		file = new JMenu("Fichier");
		addApp = new JMenuItem("Ajouter une appli");
		remApp = new JMenuItem("Supprimer cette appli");
		about = new JMenuItem("A propos");
		exit = new JMenuItem("Quitter");

		addApp.setEnabled(false);
		remApp.setEnabled(false);

		file.add(addApp);
		file.add(remApp);
		file.add(about);
		file.addSeparator();
		file.add(exit);

		menuBar.add(file);

		setJMenuBar(menuBar);

		appList = new JList<>();
		appList.setCellRenderer(new CellRenderer());

		add(new JScrollPane(appList));

		appList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Runtime r = Runtime.getRuntime();
					try {
						String path = ProgramLoader.getRoot().getAbsolutePath() +"\\" + appList.getSelectedValue().path;
						r.exec(path);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		addApp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"JStarter - Créé par Knux14\nhttp://knux.eu/");
			}
		});

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.askExit();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Point loc = getLocation();
				miInstance.setLocation(loc.x + getWidth() - 50, loc.y);
				setVisible(false);
				miInstance.setVisible(true);
			}
		});
	}

	public static void main(String args[]) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException, IOException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		icon = ImageIO.read(MovingIcon.class.getResource("res/ico.png"));
		progList = ProgramLoader.load();
		miInstance = new MovingIcon();
		instance = new Main();
		updateList(getSystemApps(Main.progList));
		instance.setVisible(true);
	}

	public static ArrayList<Program> getSystemApps(ArrayList<Program> progList) {
		ArrayList<Program> returnList = new ArrayList<>();
		String arch = System.getProperty("os.name").toLowerCase();
		for (Program p : progList) {
			if (arch.contains(p.arch)) {
				p.loadConfig();
				returnList.add(p);
			} else if (arch.contains("nix") || arch.contains("nux")
					|| arch.contains("aix")) {
				if (p.arch.equals("linux")) {
					p.loadConfig();
					returnList.add(p);
				}
			}
		}
		return returnList;
	}

	public static void updateList(ArrayList<Program> progList) {
		DefaultListModel<Program> model = new DefaultListModel<>();
		for (Program p : progList) {
			model.addElement(p);
		}
		instance.appList.setModel(model);
	}

	public static void askExit() {
		String ObjButtons[] = { "Oui", "Non" };
		int PromptResult = JOptionPane.showOptionDialog(null,
				"Êtes vous sur de vouloir quitter ?", "JStarter",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				ObjButtons, ObjButtons[1]);
		if (PromptResult == 0) {
			System.exit(0);
		}
	}

}

class CellRenderer implements ListCellRenderer<Program> {

	Color selected;
	Color unselected;

	@SuppressWarnings("rawtypes")
	public Component getListCellRendererComponent(JList list,
			final Program value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {

		if (selected == null)
			selected = list.getSelectionBackground();
		if (unselected == null)
			unselected = new Color(0, 0, 0, 0);

		value.bg = isSelected ? selected : unselected;

		return value;
	}
}
