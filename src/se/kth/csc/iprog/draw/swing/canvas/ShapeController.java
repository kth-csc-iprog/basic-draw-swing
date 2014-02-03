package se.kth.csc.iprog.draw.swing.canvas;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import se.kth.csc.iprog.draw.model.Shape;
import se.kth.csc.iprog.draw.model.ShapeContainer;

public class ShapeController extends CanvasController.InteractionStrategy {

    int type;

    Point dragStart;

    Shape current;

    public ShapeController(int type, ShapeContainer model, CanvasView view) {
        super(model, view);
        this.type = type;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        current = null;
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
        if (dragStart == null)
            // drag was cancelled
            return;
        ShapeCoordinates sc = new ShapeCoordinates(type, dragStart, e.getPoint());
        if (current == null)
            if (sc.w > 0 || sc.h > 0)
                current = model.addShape(type, sc.x, sc.y, sc.w, sc.h);
            else
                ;
        else
            model.modifyShape(current, sc.x, sc.y, sc.w, sc.h);
    }

    static class ShapeCoordinates {
        public int x, y, w, h;

        public ShapeCoordinates(int type, Point start, Point end) {
            x = start.x;
            y = start.y;
            w = end.x - start.x;
            h = end.y - start.y;

            // for segments we allow negative height or width
            if (type == ShapeContainer.SEGMENT)
                return;

            // normalize the coordinates so height and width are positive
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

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (dragStart == null || current == null)
            return;
        if ("Escape".equals(KeyEvent.getKeyText(e.getKeyCode()))) {
            model.removeShape(current);
            current = null;
            dragStart = null;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
