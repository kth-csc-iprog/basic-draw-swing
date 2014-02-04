package se.kth.csc.iprog.draw.swing.canvas;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import se.kth.csc.iprog.draw.model.ShapeContainer;

/**
 * A controller that takes care of mouse events, especially drag and drop actions on the canvas, and the Escape key that
 * cancells the drag. The same events (drag, drop) can have different meanings in different drawing modes. Each such
 * drawing mode is treated by a subclass of this class. Switching between such modes is done by the main
 * CanvasController by calling deactivate() and activate(). The class also defines some empty listener methods, to keep
 * the subclasses simpler.
 */
abstract class CanvasInteractionController implements MouseListener, MouseMotionListener, KeyListener {

    ShapeContainer model;

    CanvasView view;

    CanvasInteractionController(ShapeContainer model, CanvasView view) {
        this.model = model;
        this.view = view;
    }

    /** Activate this controller, register listeners */
    void activate() {
        view.setFocusable(true);
        view.requestFocusInWindow();
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
        view.addKeyListener(this);
    }

    /** Another controller is being activated so we unregister listeners */
    void deactivate() {
        view.removeMouseListener(this);
        view.removeMouseMotionListener(this);
        view.removeKeyListener(this);
    }

    /**
     * utility method to detect the escape key
     */
    protected boolean isEscapeKey(KeyEvent e) {
        return "Escape".equals(KeyEvent.getKeyText(e.getKeyCode()));
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
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}