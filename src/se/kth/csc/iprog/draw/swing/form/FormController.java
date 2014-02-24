package se.kth.csc.iprog.draw.swing.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import se.kth.csc.iprog.draw.model.Shape;
import se.kth.csc.iprog.draw.model.ShapeContainer;

/**
 * The form controller. Display the form view. Reacts to selection event in the list of shapes and action (edit) event
 * on the form controls.
 */
public class FormController implements ListSelectionListener, ActionListener {

    // Reference to the model
    ShapeContainer model;

    // Currently selected shape
    Shape currentSelection;

    // Reference to the view
    FormView view;

    public FormController(final ShapeContainer model) {
        this.model = model;

        // controller creates the view
        this.view = new FormView(model);

        // attach the listeners to the list and text fields
        // note the access to the view fields (so-called Java "package" access)
        view.list.addListSelectionListener(this);

        new ShapeTransferListController(view.list, model);

        view.xTextField.addActionListener(this);
        view.yTextField.addActionListener(this);
        view.heightTextField.addActionListener(this);
        view.widthTextField.addActionListener(this);

        // controller displays the view
        JFrame f = new JFrame("form");
        f.add(view);

        f.pack();

        // close the view (so it will unregister from the model) when the window
        // closes
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                view.close();
            }
        });

        // show the view
        f.setLocation((int) (300 * Math.random()), (int) (500 * Math.random()));
        f.setVisible(true);

    }

    /**
     * a selection has changed in the list, update the view
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            currentSelection = view.list.getSelectedValue();
            view.updateSelected(currentSelection);
        }
    }

    /**
     * a value has changed in one of the text fields, try to update the model and display the error message if it fails
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            model.modifyShape(currentSelection, Double.parseDouble(view.xTextField.getText()),
                Double.parseDouble(view.yTextField.getText()), Double.parseDouble(view.widthTextField.getText()),
                Double.parseDouble(view.heightTextField.getText()));
        } catch (NumberFormatException nfe) {
            view.showError("Please provide valid number for shape properties");
        } catch (IllegalArgumentException iae) {
            view.showError(iae.getMessage());
        }

    }

}
