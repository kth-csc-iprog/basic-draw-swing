package se.kth.csc.iprog.draw.swing;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * Example of creating a view without extending a Swing class. It produces a simple graphical "view" which doesn't
 * observe a model. For more typical views, see FormView and CanvasView. Even if this is small, it is still good to
 * separate from the controller as the concerns differ (layout vs interaction) : we could later on decide to have a
 * different appearance without changing the interaction. As long as the button names are the same, that would work.
 * 
 * @see se.kth.csc.iprog.draw.swing.form.FormView
 * @see se.kth.csc.iprog.draw.swing.canvas.CanvasView
 * @author cristi
 */
public class MainPanelCreator {

    /**
     * factory method to produce a layout
     */
    public JComponent createPanel(List<String> list, ActionListener listener) {
        Box bx = Box.createHorizontalBox();
        for (String name : list) {
            JButton button = new JButton(name);
            button.setName(name);
            button.addActionListener(listener);
            bx.add(button);
        }
        return bx;
    }

}
