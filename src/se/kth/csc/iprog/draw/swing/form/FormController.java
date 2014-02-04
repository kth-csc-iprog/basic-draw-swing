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

public class FormController implements ListSelectionListener, ActionListener {

    ShapeContainer model;

    Shape currentSelection;
    
    FormView view;
	
	public FormController(final ShapeContainer model) {
        this.model = model;

        // controller creates the view
        this.view = new FormView(model);

        // controller displays the view
        JFrame f = new JFrame("form");
        f.add(view);

        f.pack();

        // close the view (so it will unregister from the model) when the window closes
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                view.close();
            }
        });
        
        view.list.addListSelectionListener(this);
        view.xTextField.addActionListener(this);
        view.yTextField.addActionListener(this);
        view.heightTextField.addActionListener(this);
        view.widthTextField.addActionListener(this);
        
        f.setLocation((int) (300 * Math.random()), (int) (500 * Math.random()));
        f.setVisible(true);
        
        
    }

	@Override
	public void valueChanged(ListSelectionEvent e) {
	    if (e.getValueIsAdjusting() == false) {
        	currentSelection = view.list.getSelectedValue();
        	view.updateSelected(currentSelection);
	    }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			model.modifyShape(currentSelection, 
					Double.parseDouble(view.xTextField.getText()), 
					Double.parseDouble(view.yTextField.getText()), 
					Double.parseDouble(view.widthTextField.getText()), 
					Double.parseDouble(view.heightTextField.getText()));
		} catch (NumberFormatException nfe) {
			view.showError("Please provide valid number for shape properties");
		}
		
	}
	
	
}
