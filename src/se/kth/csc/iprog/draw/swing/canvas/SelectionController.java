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
 * Shape selection interaction, allows moving of already created shapes.
 */
public class SelectionController extends CanvasInteractionController {
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
    static int PIXEL_DEV = 10;

    // the pixel segment may have a slightly different slope from the math segment
    static double SLOPE_DEV = 0.3;

    @Override
    public void mousePressed(MouseEvent e) {
        dragStart = e.getPoint();
        for (Shape s : model.getAllShapes()) {
            // we do some math to calculate whether we are close to the shape
            // FIXME: this code is experimental, to prove the point. It will not be fixed for rigorous math

            if (s instanceof Segment) {

                double height = s.getH();
                // avoid division by zero
                if (height == 0)
                    height = 0.000001;
                // avoid division by zero
                double pixelHeight = dragStart.y - s.getY();
                if (pixelHeight == 0)
                    pixelHeight = 0.000001;

                // check whether the slope in relation to the segment origin is the same as the segment slope, and
                // whether we are within the segment bounds

                if (inBoundsOf(dragStart, s)
                        && Math.abs((dragStart.x - s.getX()) / pixelHeight - s.getW() / height) < SLOPE_DEV)
                    selected = s;
            } else if (s instanceof Rectangle) {
                // check that we are within the rectangle bounds
                if (inBoundsOf(dragStart, s)
                // check that we are close enough to one of the rectangle edges
                        && (Math.abs(dragStart.x - s.getX()) < PIXEL_DEV
                                || Math.abs(dragStart.x - s.getX() - s.getW()) < PIXEL_DEV
                                || Math.abs(dragStart.y - s.getY()) < PIXEL_DEV || Math.abs(dragStart.y - s.getY()
                                - s.getH()) < PIXEL_DEV))
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

    /**
     * check whether the point is within the bounds of the given shape
     */
    private static boolean inBoundsOf(Point p, Shape s) {
        // FIXME: segments with negative height or width are not covered
        return p.x < s.getX() + s.getW() + PIXEL_DEV && p.y < s.getY() + s.getH() + PIXEL_DEV
                && p.x > s.getX() - PIXEL_DEV && p.y > s.getY() - PIXEL_DEV;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // end of dragging, shape was moved already by the mouseDragged event
        selected = null;
        dragStart = null;
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
    public void keyPressed(KeyEvent e) {
        // if nothing is selected or dragging was cancelled, we ignore the keyboard
        if (selected == null)
            return;

        // dragging is going on. if escape is pressed, we stop the dragging
        if (isEscapeKey(e)) {
            model.modifyShape(selected, originalX, originalY, selected.getW(), selected.getH());
            selected = null;
            dragStart = null;
        }

    }

}
