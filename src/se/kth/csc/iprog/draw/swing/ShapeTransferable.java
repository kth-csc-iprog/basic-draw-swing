package se.kth.csc.iprog.draw.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import se.kth.csc.iprog.draw.model.Shape;
import se.kth.csc.iprog.draw.model.ShapeContainer;

/**
 * A Transferable representation of shapes. Reads and writes shapes to files, and allows them to be transfered within a
 * Swing application or between applications. This class does not depend on any view (canvas, form...) hence it is
 * placed in this package. Serializing shapes into string, maybe in SVG format, could actually abstracted up to the
 * model.
 * 
 * @author cristi
 */
public class ShapeTransferable implements Transferable {

    /**
     * the shape to be transferred. TODO: more shapes could be transferred at a time
     */
    Shape shape;

    /**
     * The list of files representing the transferred shapes
     */
    List<File> shapeFileList;

    // could be multiple shapes
    public ShapeTransferable(Shape s) {
        this.shape = s;
    }

    /**
     * For now only files are supported. However, we could also support strings, in order to transfer within a swing
     * application without creating a file
     */
    static DataFlavor[] supported = { DataFlavor.javaFileListFlavor };

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return supported;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(DataFlavor.javaFileListFlavor);
    }

    /**
     * write shape to a temporary file and return a list of only one file
     */
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!isDataFlavorSupported(flavor))
            throw new UnsupportedFlavorException(flavor);
        // if we have already made the file list, we just return it
        if (shapeFileList != null)
            return shapeFileList;

        // otherwise we make a file list
        File shapeFile = File.createTempFile("/tmp/" + shape.getType(), ".properties");
        Properties p = new Properties();
        p.put("type", shape.getType());
        p.put("x", "" + shape.getX());
        p.put("y", "" + shape.getY());
        p.put("w", "" + shape.getW());
        p.put("h", "" + shape.getH());
        FileOutputStream out = new FileOutputStream(shapeFile);
        p.store(out, "");
        out.close();
        shapeFileList = new ArrayList<File>();
        shapeFileList.add(shapeFile);
        return shapeFileList;
    }

    /**
     * Read shapes from files and add them to a model. Utility method for entities that receive a list of shape files
     * 
     * @param data
     * @param model
     * @param index
     */
    public static void addToModel(List<File> data, ShapeContainer model, int index) {
        for (File f : data) {
            Properties p = new Properties();
            try {
                FileInputStream in = new FileInputStream(f);
                p.load(in);
                in.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            int type = -1;
            switch (p.getProperty("type")) {
                case "rectangle":
                    type = ShapeContainer.RECTANGLE;
                    break;
                case "ellipse":
                    type = ShapeContainer.ELLIPSE;
                    break;
                case "segment":
                    type = ShapeContainer.SEGMENT;
                    break;
            }
            model.addShape(type, Double.parseDouble(p.getProperty("x")), Double.parseDouble(p.getProperty("y")),
                Double.parseDouble(p.getProperty("w")), Double.parseDouble(p.getProperty("h")), index++);
        }
    }
}
