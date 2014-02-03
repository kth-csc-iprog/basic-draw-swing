package se.kth.csc.iprog.draw.swing.canvas;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import se.kth.csc.iprog.draw.model.Ellipse;
import se.kth.csc.iprog.draw.model.Rectangle;
import se.kth.csc.iprog.draw.model.Segment;
import se.kth.csc.iprog.draw.model.Shape;
import se.kth.csc.iprog.draw.model.ShapeContainer;

/**
 * Shape selection interaction, allows moving of already created shapes. Since InteractionStrategy implements many
 * listeners but is abstract, the listener methods must be defined here. MouseListener and MouseMotionListener are used
 * to detect drag start, drag end and dragging. A key listener is needed to detect drag cancellation.
 */
public class SelectionController extends CanvasController.InteractionStrategy {
    /*
     * starting point for the dragging gesture
     */
    Point dragStart;

    /*
     * selected shape, if any
     */
    Shape selected;

    /*
     * original position of the selected shape
     */
    double originalX, originalY;

    public SelectionController(ShapeContainer model, CanvasView view) {
        super(model, view);
    }

    // it is hard to e.g. click precisely on the rectangle so we allow a pixel deviation
    static int DEV = 10;

    @Override
    public void mousePressed(MouseEvent e) {
        dragStart = e.getPoint();
        for (Shape s : model.getAllShapes()) {
            // we do some math to calculate whether we are close to the shape
            // FIXME: this code is experimental, to prove the point. It will not be fixed for rigorous math

            if (s instanceof Segment) {
                // calculate whether the slope in relation to the segment origin is the same as the segment slope, and
                // whether we are within the segment bounds
                // TODO: address the case when s.getW() is 0
                // TODO: not sure 1 is the best slope deviation but it seems to work in practice
                if (Math.abs(((dragStart.x - s.getX()) / (dragStart.y - s.getY())) - s.getH() / s.getW()) < 1
                        && dragStart.x < s.getX() + s.getW() + DEV && dragStart.y < s.getY() + s.getW() + DEV)
                    selected = s;
            } else if (s instanceof Rectangle) {
                // check that we are within the rectangle bounds
                if (dragStart.x < s.getX() + s.getW() + DEV && dragStart.y < s.getY() + s.getH() + DEV
                        && dragStart.x > s.getX() - DEV
                        && dragStart.y > s.getY() - DEV
                        // check that we are close enough to one of the rectangle segments
                        && (Math.abs(dragStart.x - s.getX()) < DEV || Math.abs(dragStart.x - s.getX() - s.getW()) < DEV
                                || Math.abs(dragStart.y - s.getY()) < DEV || Math.abs(dragStart.y - s.getY() - s.getH()) < DEV))
                    selected = s;

            } else if (s instanceof Ellipse) {

                // TODO

            }
        }
        if (selected == null)
            dragStart = null;
        else {
            // save the original position of the shape
            originalX = selected.getX();
            originalY = selected.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // end of dragging, shape was moved already by the mouseDragged event
        selected = null;
        dragStart = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // the drag didn't start on a shape, or was cancelled with Escape
        if (selected == null)
            return;

        // move the shape in relation to the original position
        model.modifyShape(selected, originalX + e.getPoint().x - dragStart.x, originalY + e.getPoint().y - dragStart.y,
            selected.getW(), selected.getH());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // if nothing is selected or dragging was cancelled, we ignore the keyboard
        if (selected == null)
            return;

        // dragging is going on. if escape is pressed, we stop the dragging
        if ("Escape".equals(KeyEvent.getKeyText(e.getKeyCode()))) {
            model.modifyShape(selected, originalX, originalY, selected.getW(), selected.getH());
            selected = null;
            dragStart = null;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
