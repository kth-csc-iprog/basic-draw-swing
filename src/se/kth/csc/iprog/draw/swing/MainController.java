package se.kth.csc.iprog.draw.swing;

import se.kth.csc.iprog.draw.model.ShapeContainer;
import se.kth.csc.iprog.draw.swing.canvas.CanvasController;

public class MainController {
    public static void main(String[] argv) {
        ShapeContainer model = new ShapeContainer();

        // this will be a button, creates a canvas every time it's pressed
        new CanvasController(model);
        new CanvasController(model);

        // some test shapes
        model.addShape(ShapeContainer.SEGMENT, 10, 20, 30, 40);
        model.addShape(ShapeContainer.RECTANGLE, 40, 30, 30, 40);
    }

}
