package se.kth.csc.iprog.draw.swing.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

public class FormView extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

    ShapeContainer model;
    
    DefaultListModel<Shape> listModel;
    JList<Shape> list;
    JTextField xTextField;
    JTextField yTextField;
    JTextField heightTextField;
    JTextField widthTextField;
    JLabel errorLabel;
    
    
    public FormView(ShapeContainer m) {
        model = m;
        model.addObserver(this);
        
        listModel = new DefaultListModel<Shape>();
        
        list = new JList<Shape>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(80, 200));
        add(listScroller);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        JLabel xLabel = new JLabel("X:");
        c.gridx = 0;
        c.gridy = 0;
        formPanel.add(xLabel,c);
        xTextField = new JTextField();
        c.gridx = 1;
        xTextField.setColumns(10);
        formPanel.add(xTextField,c);

        JLabel yLabel = new JLabel("Y:");
        c.gridx = 0;
        c.gridy = 1;
        formPanel.add(yLabel,c);
        yTextField = new JTextField();
        c.gridx = 1;
        yTextField.setColumns(10);
        formPanel.add(yTextField,c);
        
        JLabel heightLabel = new JLabel("Height:");
        c.gridx = 0;
        c.gridy = 2;
        formPanel.add(heightLabel,c);
        heightTextField = new JTextField();
        c.gridx = 1;
        heightTextField.setColumns(10);
        formPanel.add(heightTextField,c);
        
        JLabel widthLabel = new JLabel("Width:");
        c.gridx = 0;
        c.gridy = 3;
        formPanel.add(widthLabel,c);
        widthTextField = new JTextField();
        c.gridx = 1;
        widthTextField.setColumns(10);
        formPanel.add(widthTextField,c);
        
        errorLabel = new JLabel();
        c.gridx = 0;
        c.gridy = 4;
        errorLabel.setForeground(Color.red);
        formPanel.add(errorLabel,c);
        
        add(formPanel);
        
    }
    
    /**
     * This method is needed so the observer doesn't remain hanged when e.g. the canvas is not visible any longer.
     */
    public void close() {
        model.deleteObserver(this);
    }

	@Override
	public void update(Observable o, Object arg) {
		
		Shape current = list.getSelectedValue();
		listModel.removeAllElements();
		errorLabel.setText("");
		
		for (Shape shape : model.getAllShapes()) {
			listModel.addElement(shape);
		}
		
		list.setSelectedValue(current, true);
		
	}
	
	public void updateSelected(Shape shape) {
		if (shape!=null) {
			xTextField.setEnabled(true);
			yTextField.setEnabled(true);
			heightTextField.setEnabled(true);
			widthTextField.setEnabled(true);
			xTextField.setText(shape.getX() + "");
			yTextField.setText(shape.getY() + "");
			heightTextField.setText(shape.getH() + "");
			widthTextField.setText(shape.getW() + "");
		} else {
			xTextField.setEnabled(false);
			yTextField.setEnabled(false);
			heightTextField.setEnabled(false);
			widthTextField.setEnabled(false);
			xTextField.setText("");
			yTextField.setText("");
			heightTextField.setText("");
			widthTextField.setText("");
		}
			
	}
	
	public void showError(String text) {
		errorLabel.setText(text);
	}

}