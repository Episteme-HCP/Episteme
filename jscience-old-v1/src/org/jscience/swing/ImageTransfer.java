/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
