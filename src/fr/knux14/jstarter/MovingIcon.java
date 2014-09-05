package fr.knux14.jstarter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class MovingIcon extends JDialog implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	long lastClick = -1;
	boolean lastRightClick = false;
	int posX = 0, posY = 0;
	
	public MovingIcon() {
		setSize(45, 45);
		setAlwaysOnTop(true);
		setUndecorated(true);
		setLocationRelativeTo(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(new Color(0, 0, 0, 0));
		add(new PanelPicture());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		long currClick = System.currentTimeMillis();
		if ((currClick - 500) < lastClick && lastClick > 0) {
			if (lastRightClick && e.getButton() == MouseEvent.BUTTON3) {
				Main.askExit();
			} else {
				Point loc = getLocation();
				Main.instance.setLocation(loc.x - Main.instance.getWidth() + 50, loc.y);
				setVisible(false);
				Main.instance.setVisible(true);
			}
		}
		lastClick = System.currentTimeMillis();
		if (e.getButton() == MouseEvent.BUTTON3) lastRightClick = true;
		else lastRightClick = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		posX = e.getX();
		posY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseDragged(MouseEvent arg0) {
		setLocation(arg0.getXOnScreen() - posX, arg0.getYOnScreen() - posY);		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {	}
	
}

class PanelPicture extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	PanelPicture() {
		setSize(50, 50);
		setBackground(new Color(0, 0, 0, 0));
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(Main.icon, 0, 0, Main.icon.getWidth(), Main.icon.getHeight(), null);
	}
	
}