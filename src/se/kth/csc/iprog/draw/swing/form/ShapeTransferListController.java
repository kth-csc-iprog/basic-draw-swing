package se.kth.csc.iprog.draw.swing.form;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import se.kth.csc.iprog.draw.model.Shape;
import se.kth.csc.iprog.draw.model.ShapeContainer;
import se.kth.csc.iprog.draw.swing.ShapeTransferable;

/**
 * Data transfer from/to a JList of shapes
 * 
 * @author cristi
 */
final class ShapeTransferListController extends TransferHandler {
    private final JList<Shape> list;

    private final ShapeContainer model;

    /**
     * Enable drag and drop / copy-paste on the list
     * 
     * @param list
     * @param model
     */
    ShapeTransferListController(JList<Shape> list, ShapeContainer model) {
        this.list = list;
        this.model = model;
        list.setDragEnabled(true);
        list.setDropMode(DropMode.INSERT);
        list.setTransferHandler(this);
    }

    // ---------------- SOURCE methods
    /**
     * data transfer semantic
     */
    @Override
    public int getSourceActions(JComponent source) {
        System.out.println("** getSourceActions call");
        return TransferHandler.COPY_OR_MOVE;
    }

    Shape dragged;

    /**
     * put the shape into a tranferrable form
     */
    @Override
    public Transferable createTransferable(JComponent source) {
        System.out.println("** createTransferable call");
        // TODO: there may be more shapes selected
        dragged = list.getSelectedValue();
        return new ShapeTransferable(dragged);
    }

    /**
     * remove the dragged element once it was transferred somewhere else
     */
    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        System.out.println("** exportDone call");

        if (source != list)
            return;

        if (action == TransferHandler.MOVE) {
            model.removeShape(dragged);
        }

    }

    // ---------------- TARGET methods
    /**
     * called during drag over, to see whether we can accept a drop
     */
    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        System.out.println("canImport call");

        // we simply check whether the data transferred is a file list
        // we could actually obtain the file list, and check to see whether it contains shape files
        // like this: support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor)
        return support.getDataFlavors()[0].equals(DataFlavor.javaFileListFlavor);
    }

    /**
     * Drop or paste took place
     */
    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        // we cannot assume that canImport was called, e.g. it is not called for copy-paste
        if (!canImport(support))
            return false;

        System.out.println("** importData call");
        try {
            // where will the new shape land? The default is "above the currently selected"
            int addIndex = list.getSelectedIndex();

            // drag and drop provides a precise location
            if (support.isDrop())
                addIndex = ((JList.DropLocation) support.getDropLocation()).getIndex();

            // get the transferred data
            @SuppressWarnings("unchecked")
            List<File> transferData = (List<File>) support.getTransferable().getTransferData(
                DataFlavor.javaFileListFlavor);

            // add the transferred data to the model
            ShapeTransferable.addToModel(transferData, model, addIndex);
            return true;

        } catch (UnsupportedFlavorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}