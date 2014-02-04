package se.kth.csc.iprog.draw.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import se.kth.csc.iprog.draw.model.ShapeContainer;
import se.kth.csc.iprog.draw.swing.canvas.CanvasController;
import se.kth.csc.iprog.draw.swing.form.FormController;

/**
 * A central controller to instantiate all the different kinds of
 * view-controller pairs (canvas, form...)
 * 
 * @author cristi
 */
public class MainController {
	public static void main(String[] argv) {

		// a common model for all views
		final ShapeContainer model = new ShapeContainer();

		// create a row of buttons with the same action listener that
		// distinguishes between them based on the name
		JComponent panel = new MainPanelCreator().createPanel(new String[] {
				"New canvas", "New form" }, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (((AbstractButton) e.getSource()).getName().contains(
						"canvas")) {
					new CanvasController(model);
				}

				if (((AbstractButton) e.getSource()).getName().contains("form")) {
					new FormController(model);
				}
			}
		});

		// display the buttons in a window
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
		window.setVisible(true);

		// some test shapes
		model.addShape(ShapeContainer.SEGMENT, 10, 20, 30, 40);
		model.addShape(ShapeContainer.RECTANGLE, 40, 30, 30, 40);

	}
}
