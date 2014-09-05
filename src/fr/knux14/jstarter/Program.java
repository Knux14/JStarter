package fr.knux14.jstarter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Program extends JPanel {

	private static final long serialVersionUID = 1L;

	Color bg = new Color(0, 0, 0, 0);
	String name;
	String path;
	String iconPath;
	String arch;

	File file;
	BufferedImage icon;

	public Program(String arch, String name, String path, String icon) {
		this.arch = arch;
		this.name = name;
		this.path = path;
		this.iconPath = icon;
		setSize(200, 74);
		try {
			this.icon = ImageIO.read(Main.class.getResource("res/defIcon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean loadConfig() {
		file = new File(path);
		File imageFile = new File(iconPath);
		if (!file.exists()) {
			return false;
		}
		try {
			icon = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 74);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(bg);
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2.drawImage(icon, 5, 5, 64, 64, null);

		g2.setColor(Color.black);
		g2.setFont(g2.getFont().deriveFont(14f));
		g2.drawString(name, 74, 25);

		g2.setColor(Color.lightGray);
		g2.setFont(g2.getFont().deriveFont(13f));
		g2.drawString(path, 74, 40);
	}
}
