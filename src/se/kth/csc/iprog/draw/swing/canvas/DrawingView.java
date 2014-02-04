package se.kth.csc.iprog.draw.swing.canvas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;

/**
 * The layout of the drawing view. Makes the drawing toolbar buttons and places them in relation to the drawing canvas
 * and the status bar. This is not a fully-fledged MVC view as it does not observe the model. If we would have a small
 * intermediate model containing the current drawing mode, currrent shape, current operation, this view could observe
 * that and e.g. update the status bar. It is not really needed to extend JPanel as we don't override any JPanel method,
 * we could simply have a method that returns JPanel. See MainPanelCreator for an example.
 * 
 * @author cristi
 */
public class DrawingView extends JPanel {
    private static final long serialVersionUID = 1L;

    /**
     * layout the given canvas and status bar, with a toolbar of buttons, select the first button.
     */
    public DrawingView(CanvasView view, JLabel status, ActionListener ctrl, List<String> buttonNames) {
        super(new BorderLayout());
        Box buttons = Box.createVerticalBox();
        ButtonGroup group = new ButtonGroup();

        // first button is initially selected by convention
        boolean selected = true;

        // create the buttons with the given names
        for (String s : buttonNames) {
            ImageIcon icon = new ImageIcon("images/" + s + ".png");
            JToggleButton button = new JToggleButton(icon);
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
        add(status, "South");
    }

}
