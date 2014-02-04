package se.kth.csc.iprog.draw.swing;

import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * example of creating a view without extending a Swing class
 * 
 * @author cristi
 */
public class MainPanelCreator {

    /*
     * factory method to produce a layout
     */
    public JComponent createPanel(String[] buttonNames, ActionListener listener) {
        Box bx = Box.createHorizontalBox();
        for (String name : buttonNames) {
            JButton button = new JButton(name);
            button.setName(name);
            button.addActionListener(listener);
            bx.add(button);
        }
        return bx;
    }

}
