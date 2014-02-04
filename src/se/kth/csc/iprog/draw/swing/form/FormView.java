package se.kth.csc.iprog.draw.swing.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import se.kth.csc.iprog.draw.model.Shape;
import se.kth.csc.iprog.draw.model.ShapeContainer;

/**
 * The form view. Builds the whole view. Reads the shapes from the model, fills
 * them in a list and show the properties of the selected one.
 * 
 */
public class FormView extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

	// The model reference
	ShapeContainer model;

	// The model representation for the JList
	DefaultListModel<Shape> listModel;

	// View components
	JList<Shape> list;
	JTextField xTextField;
	JTextField yTextField;
	JTextField heightTextField;
	JTextField widthTextField;
	JLabel surfaceValueLabel;
	JLabel errorLabel;

	/**
	 * The constructor that adds the view to the model list of observers and
	 * layouts the view components (list and text fields).
	 */
	public FormView(ShapeContainer m) {
		super(new BorderLayout());
		model = m;
		model.addObserver(this);

		listModel = new DefaultListModel<Shape>();

		list = new JList<Shape>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(230, 200));
		add(listScroller, "Center");

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JLabel xLabel = new JLabel("X:");
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		formPanel.add(xLabel, c);
		xTextField = new JTextField();
		c.gridx = 1;
		c.insets = new Insets(1, 0, 1, 5);
		xTextField.setColumns(10);
		formPanel.add(xTextField, c);

		JLabel yLabel = new JLabel("Y:");
		c.gridx = 0;
		c.gridy = 1;
		formPanel.add(yLabel, c);
		yTextField = new JTextField();
		c.gridx = 1;
		yTextField.setColumns(10);
		formPanel.add(yTextField, c);

		JLabel heightLabel = new JLabel("Height:");
		c.gridx = 0;
		c.gridy = 2;
		formPanel.add(heightLabel, c);
		heightTextField = new JTextField();
		c.gridx = 1;
		heightTextField.setColumns(10);
		formPanel.add(heightTextField, c);

		JLabel widthLabel = new JLabel("Width:");
		c.gridx = 0;
		c.gridy = 3;
		formPanel.add(widthLabel, c);
		widthTextField = new JTextField();
		c.gridx = 1;
		widthTextField.setColumns(10);
		formPanel.add(widthTextField, c);

		JLabel surfaceLabel = new JLabel("Surface:");
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(1, 10, 1, 2);
		formPanel.add(surfaceLabel, c);
		surfaceValueLabel = new JLabel();
		c.gridx = 1;
		c.anchor = GridBagConstraints.WEST;
		formPanel.add(surfaceValueLabel, c);

		errorLabel = new JLabel("abc");
		errorLabel.setForeground(Color.red);
		add(errorLabel, "South");

		add(formPanel, "East");

		// After the view is built, load the current shapes from the model
		loadShapes();

	}

	/**
	 * This method is needed so the observer doesn't remain hanged when e.g. the
	 * canvas is not visible any longer.
	 */
	public void close() {
		model.deleteObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		loadShapes();
	}

	/**
	 * Loads the shapes from the model.
	 */
	private void loadShapes() {
		// We store the currently selected shape, so we can reselect it after
		// the shapes have been reloaded
		Shape current = list.getSelectedValue();

		// We remove all elements in the list and the error message
		listModel.removeAllElements();
		errorLabel.setText(" ");

		// Read the shapes from the model and add them to the list
		for (Shape shape : model.getAllShapes()) {
			listModel.addElement(shape);
		}

		// Reselect the previously selected shape
		list.setSelectedValue(current, true);
	}

	/**
	 * Updates the form input controls to represent the values of currently
	 * selected shape. If no shape is selected, the form is disabled.
	 */
	public void updateSelected(Shape shape) {
		if (shape != null) {
			xTextField.setEnabled(true);
			yTextField.setEnabled(true);
			heightTextField.setEnabled(true);
			widthTextField.setEnabled(true);
			xTextField.setText(shape.getX() + "");
			yTextField.setText(shape.getY() + "");
			heightTextField.setText(shape.getH() + "");
			widthTextField.setText(shape.getW() + "");
			surfaceValueLabel.setText(shape.getSurface() + "");
		} else {
			xTextField.setEnabled(false);
			yTextField.setEnabled(false);
			heightTextField.setEnabled(false);
			widthTextField.setEnabled(false);
			xTextField.setText("");
			yTextField.setText("");
			heightTextField.setText("");
			widthTextField.setText("");
			surfaceValueLabel.setText("");
		}

	}

	/**
	 * Show the error message.
	 */
	public void showError(String text) {
		errorLabel.setText(text);
	}

}
