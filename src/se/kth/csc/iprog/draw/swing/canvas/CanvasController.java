package se.kth.csc.iprog.draw.swing.canvas;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import se.kth.csc.iprog.draw.model.ShapeContainer;

/**
 * The canvas controller. Delegates to one sub-controller for each interaction mode (segment drawing, rectangle drawing,
 * ellipse drawing, selection). Displays its canvas.
 * 
 * @author cristi
 */
public class CanvasController {
    ShapeContainer model;

    CanvasView view;

    Point dragStart;

    Point dragStop;

    /** keep track of all interaction controllers in a mapping */
    Map<String, CanvasInteractionController> modes;

    String mode = "segment";

    public CanvasController(final ShapeContainer model) {
        this.model = model;

        // controller creates the view
        this.view = new CanvasView(model);

        // prepare interaction controllers
        modes = new HashMap<String, CanvasInteractionController>();
        modes.put("segment", new ShapeController(ShapeContainer.SEGMENT, model, view));
        modes.put("rectangle", new ShapeController(ShapeContainer.RECTANGLE, model, view));
        modes.put("ellipse", new ShapeController(ShapeContainer.ELLIPSE, model, view));
        modes.put("select", new SelectionController(model, view));

        modes.get("select").activate();

        // controller displays the view
        JFrame f = new JFrame("canvas");
        f.add(view);

        f.pack();

        // close the view (so it will unregister from the model) when the window closes
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                view.close();
            }
        });
        f.setLocation((int) (300 * Math.random()), (int) (500 * Math.random()));
        f.setVisible(true);
    }
}
