package se.kth.csc.iprog.draw.swing;

import se.kth.csc.iprog.draw.model.ShapeContainer;
import se.kth.csc.iprog.draw.swing.canvas.CanvasController;
import se.kth.csc.iprog.draw.swing.form.FormController;

/**
 * A central controller to instantiate all the different kinds of view-controller pairs (canvas, form...)
 * 
 * @author cristi
 */
public class MainController {
    public static void main(String[] argv) {

        // a common model for all views
        ShapeContainer model = new ShapeContainer();

        // this will be a button, creates a canvas every time it's pressed
        // controllers instantiate their views
        new CanvasController(model);
        new CanvasController(model);
        
        new FormController(model);
        
        // some test shapes
        model.addShape(ShapeContainer.SEGMENT, 10, 20, 30, 40);
        model.addShape(ShapeContainer.RECTANGLE, 40, 30, 30, 40);

    }

}
