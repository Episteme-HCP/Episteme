/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import java.awt.*;
import java.awt.datatransfer.*;

import java.io.IOException;


/**
 * allows to transfer images via clipboard
 *
 * @author Holger Antelmann
 */
class ImageTransfer implements Transferable, ClipboardOwner {
    /** DOCUMENT ME! */
    Image image;

/**
     * Creates a new ImageTransfer object.
     *
     * @param image DOCUMENT ME!
     */
    public ImageTransfer(Image image) {
        this.image = image;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flavor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnsupportedFlavorException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public synchronized Object getTransferData(DataFlavor flavor)
        throws UnsupportedFlavorException, IOException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }

        return image;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { DataFlavor.imageFlavor };
    }

    /**
     * DOCUMENT ME!
     *
     * @param flavor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.imageFlavor.equals(flavor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cp DOCUMENT ME!
     * @param contents DOCUMENT ME!
     */
    public void lostOwnership(Clipboard cp, Transferable contents) {
        // empty implementation
    }
}
