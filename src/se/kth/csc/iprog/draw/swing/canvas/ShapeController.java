package se.kth.csc.iprog.draw.swing.canvas;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import se.kth.csc.iprog.draw.model.Shape;
import se.kth.csc.iprog.draw.model.ShapeContainer;

/**
 * Shape drawing interaction.
 */
public class ShapeController extends CanvasInteractionController {

    /**
     * type of shape drawn
     */
    int type;

    /**
     * the point where dragging began
     */
    Point dragStart;

    /**
     * the currently created shape. It is created when dragging begins, then continuously modified as long as dragging
     * lasts.
     */
    Shape current;

    public ShapeController(int type, ShapeContainer model, CanvasView view, JLabel status) {
        super(model, view, status);
        this.type = type;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // dragging ended
        current = null;
        dragStart = null;
        status.setText(" ");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragStart == null)
            // drag was cancelled
            return;
        // we use a class to normalize the coordinates
        // ShapeCoordinates is defined locally in this file
        ShapeCoordinates sc = new ShapeCoordinates(type, dragStart, e.getPoint());

        // if dragging just began
        if (current == null)
            // new shape, only if there is some height or width
            if (sc.w > 0 || sc.h > 0)
                current = model.addShape(type, sc.x, sc.y, sc.w, sc.h);
            else
                ;
        else
            // if dragging has been going on for a while, modify current shape
            model.modifyShape(current, sc.x, sc.y, sc.w, sc.h);
        if (current != null)
            status.setText("drawing " + current);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // if we are not in a d&d or we didn't create a shape yet, or the shape creation was cancelled, we ignore the
        // event
        if (dragStart == null || current == null)
            return;

        // escape pressed during d&d, we remove the currently created shape
        if (isEscapeKey(e)) {
            model.removeShape(current);
            current = null;
            dragStart = null;
            status.setText("");
        }

    }
}

/**
 * Calculate valid shape coordinates and size given two end-points
 * 
 * @author cristi
 */
class ShapeCoordinates {
    int x, y, w, h;

    public ShapeCoordinates(int type, Point start, Point end) {
        // copy the coordinates and size first
        x = start.x;
        y = start.y;
        w = end.x - start.x;
        h = end.y - start.y;

        // for segments we allow negative height or width
        if (type == ShapeContainer.SEGMENT)
            return;

        // for rectangles and ellipses normalize the coordinates so height and width are positive
        if (w < 0) {
            x = end.x;
            w = -w;
        }
        if (h < 0) {
            y = end.y;
            h = -h;
        }
    }

}
