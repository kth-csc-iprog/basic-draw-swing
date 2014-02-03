package se.kth.csc.iprog.draw.swing.canvas;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import se.kth.csc.iprog.draw.model.Ellipse;
import se.kth.csc.iprog.draw.model.Rectangle;
import se.kth.csc.iprog.draw.model.Segment;
import se.kth.csc.iprog.draw.model.Shape;
import se.kth.csc.iprog.draw.model.ShapeContainer;

public class SelectionController extends CanvasController.InteractionStrategy {
    Point dragStart;

    Shape selected;

    double originalX, originalY;

    public SelectionController(ShapeContainer model, CanvasView view) {
        super(model, view);
    }

    static int DEV = 10;

    @Override
    public void mousePressed(MouseEvent e) {
        dragStart = e.getPoint();
        for (Shape s : model.getAllShapes()) {
            // we do some math to calculate whether we are close to the shape

            if (s instanceof Segment) {
                // TODO: address the case when s.getW() is 0
                // TODO: not sure 1 is the best slope deviation but it seems to work in practice
                if (Math.abs(((dragStart.x - s.getX()) / (dragStart.y - s.getY())) - s.getH() / s.getW()) < 1
                        && dragStart.x < s.getX() + s.getW() + DEV && dragStart.y < s.getY() + s.getW() + DEV)
                    selected = s;
            } else if (s instanceof Rectangle) {
                if (dragStart.x < s.getX() + s.getW() + DEV
                        && dragStart.y < s.getY() + s.getH() + DEV
                        && dragStart.x > s.getX() - DEV
                        && dragStart.y > s.getY() - DEV
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
            originalX = selected.getX();
            originalY = selected.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
        if (selected == null)
            // drag was cancelled
            return;

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
        if (selected == null)
            return;
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
