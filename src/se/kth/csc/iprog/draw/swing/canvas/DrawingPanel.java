package se.kth.csc.iprog.draw.swing.canvas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;

/**
 * The layout of the drawing view. Makes the tool buttons and places them in relation to the drawing canvas
 * 
 * @author cristi
 */
public class DrawingPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    /**
     * layout the given canvas with a toolbar of buttons, select the first button.
     */
    public DrawingPanel(CanvasView view, ActionListener ctrl, List<String> buttonNames) {
        super(new BorderLayout());
        Box buttons = Box.createVerticalBox();
        ButtonGroup group = new ButtonGroup();

        // first button is selected
        boolean selected = true;
        for (String s : buttonNames) {
            JRadioButton button = new JRadioButton(s);
            button.setName(s);
            button.setSelected(selected);
            selected = false;

            group.add(button);
            button.addActionListener(ctrl);
            buttons.add(button);
        }
        view.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        add(buttons, "West");
        add(view, "Center");
    }
}
