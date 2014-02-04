package se.kth.csc.iprog.draw.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

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
        final ShapeContainer model = new ShapeContainer();

        // some test shapes
        model.addShape(ShapeContainer.SEGMENT, 10, 20, 30, 40);
        model.addShape(ShapeContainer.RECTANGLE, 40, 30, 30, 40);

        // an action listener that distinguishes between buttons based on their name, and creates the respective type of
        // view (created by its controller)
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (((AbstractButton) e.getSource()).getName().equals("New canvas")) {
                    new CanvasController(model);
                } else if (((AbstractButton) e.getSource()).getName().equals("New form")) {
                    new FormController(model);
                }
            }
        };

        // create a panel with buttons, and attach the buttonListener to them
        JComponent panel = new MainPanelCreator().createPanel(Arrays.asList("New canvas", "New form"), buttonListener);

        // display the button panel in a window
        JFrame window = new JFrame("shapes");
        window.add(panel);
        window.pack();

        // quit the program when the window is closed
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        // show the window
        window.setVisible(true);
    }
}
