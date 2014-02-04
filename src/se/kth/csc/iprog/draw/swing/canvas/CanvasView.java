package se.kth.csc.iprog.draw.swing.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import se.kth.csc.iprog.draw.model.Ellipse;
import se.kth.csc.iprog.draw.model.Rectangle;
import se.kth.csc.iprog.draw.model.Segment;
import se.kth.csc.iprog.draw.model.Shape;
import se.kth.csc.iprog.draw.model.ShapeContainer;

/**
 * Canvas view, draws all the shapes in a ShapeContainer model. It is a JPanel
 * that overrides paintComponent to draw the shapes. Simply repaints everything
 * on notification from the model.
 * 
 * @author cristi
 */
public class CanvasView extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

	ShapeContainer model;

	public CanvasView(ShapeContainer m) {
		model = m;
		model.addObserver(this);
	}

	/**
	 * This method is needed so the observer doesn't remain hanged when e.g. the
	 * canvas is not visible any longer.
	 */
	public void close() {
		model.deleteObserver(this);
	}

	@Override
	protected void finalize() {
		close();
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		for (Shape s : model.getAllShapes()) {
			if (s instanceof Segment) {
				g.drawLine((int) s.getX(), (int) s.getY(), (int) s.getX()
						+ (int) s.getW(), (int) s.getY() + (int) s.getH());
			} else if (s instanceof Rectangle) {
				g.drawRect((int) s.getX(), (int) s.getY(), (int) s.getW(),
						(int) s.getH());
			} else if (s instanceof Ellipse) {
				g.drawOval((int) s.getX(), (int) s.getY(), (int) s.getW(),
						(int) s.getH());
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

	static Dimension pref = new Dimension(300, 300);

	@Override
	public Dimension getPreferredSize() {
		return pref;

	}
}
