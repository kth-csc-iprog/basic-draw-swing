package se.kth.csc.iprog.draw.swing.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import se.kth.csc.iprog.draw.model.Ellipse;
import se.kth.csc.iprog.draw.model.Rectangle;
import se.kth.csc.iprog.draw.model.Segment;
import se.kth.csc.iprog.draw.model.Shape;
import se.kth.csc.iprog.draw.model.ShapeContainer;

/**
 * Canvas view, draws all the shapes in a ShapeContainer model. It is a JPanel that overrides paintComponent to draw the
 * shapes. Simply repaints everything on notification from the model.
 * 
 * @author cristi
 */
public class CanvasView extends JComponent implements Observer {
    private static final long serialVersionUID = 1L;

    ShapeContainer model;

    /** Register as an observer to the model. Called by the controller */
    public CanvasView(ShapeContainer m) {
        model = m;
        model.addObserver(this);
    }

    /**
     * This method is needed so the observer doesn't remain hanged when e.g. the canvas is not visible any longer.
     * Called by the controller when it detects e.g. a window closing.
     */
    public void close() {
        model.deleteObserver(this);
    }

    /**
     * This method is called by Swing when painting is needed. We read the model and paint our shapes.
     */
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        for (Shape s : model.getAllShapes()) {
            if (s instanceof Segment) {
                g.drawLine((int) s.getX(), (int) s.getY(), (int) s.getX() + (int) s.getW(),
                    (int) s.getY() + (int) s.getH());
            } else if (s instanceof Rectangle) {
                g.drawRect((int) s.getX(), (int) s.getY(), (int) s.getW(), (int) s.getH());
            } else if (s instanceof Ellipse) {
                g.drawOval((int) s.getX(), (int) s.getY(), (int) s.getW(), (int) s.getH());
            }
        }
    }

    /**
     * Called by the model when something changes. We ask for repaint, which will lead to a Swing call to
     * paintComponent()
     */
    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    static Dimension pref = new Dimension(300, 300);

    /**
     * Called by Swing. We indicate a preferred size.
     */
    @Override
    public Dimension getPreferredSize() {
        return pref;
    }

    /**
     * Just in case somebody forgets to call close(). Called by the Java Virtual Machine when the object is no longer
     * referenced.
     */
    @Override
    protected void finalize() {
        close();
    }

}
