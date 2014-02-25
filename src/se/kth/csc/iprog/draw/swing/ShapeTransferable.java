package se.kth.csc.iprog.draw.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
    static DataFlavor[] supported = { DataFlavor.javaFileListFlavor, DataFlavor.plainTextFlavor };

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return supported;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return Arrays.asList(supported).contains(flavor);
    }

    /**
     * write shape to a temporary file and return a list of only one file
     */
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!isDataFlavorSupported(flavor))
            throw new UnsupportedFlavorException(flavor);
        // if we have already made the file list, we just return it
        if (flavor.equals(DataFlavor.plainTextFlavor)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            shape.writeTo(bos);
            bos.close();
            return new ByteArrayInputStream(bos.toByteArray());
        } else {
            if (shapeFileList != null)
                return shapeFileList;

            // otherwise we make a file list
            File shapeFile = File.createTempFile("/tmp/" + shape.getType(), ".properties");
            FileOutputStream out = new FileOutputStream(shapeFile);
            shape.writeTo(out);
            out.close();
            return Arrays.asList(shapeFile);
        }
    }

    /**
     * Read shapes from files and add them to a model. Utility method for entities that receive a list of shape files
     * 
     * @param transferable
     * @param model
     * @param index
     */
    public static boolean addToModel(Transferable transferable, ShapeContainer model, int index) {
        // get the transferred data
        try {
            @SuppressWarnings("unchecked")
            List<File> transferData = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

            for (File f : transferData) {
                FileInputStream in = new FileInputStream(f);
                model.addShape(Shape.readFrom(in), index++);
                in.close();

            }
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
}
