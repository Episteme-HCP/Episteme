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

package org.jscience.chemistry.gui.extended.graphics3d;

import org.jscience.chemistry.gui.extended.molecule.Molecule;

import java.awt.*;

import javax.media.j3d.Canvas3D;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Panel3D extends Panel {
    /** DOCUMENT ME! */
    Canvas3D moleculeCanvas;

    /** DOCUMENT ME! */
    MolecularScene mScene;

/**
     * Creates a new Panel3D object.
     */
    public Panel3D() {
        GraphicsDevice dev = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                                .getDefaultScreenDevice();

        setLayout(new BorderLayout());
        moleculeCanvas = new Canvas3D(dev.getDefaultConfiguration());
        mScene = new MolecularScene(moleculeCanvas);
        add("Center", moleculeCanvas);
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void addMolecule(Molecule m) {
        //moleculeCanvas.stopRenderer();
        mScene.addMolecule(m);

        //moleculeCanvas.startRenderer();
    }

    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     */
    public void setRenderStyle(int style) {
        mScene.setRenderStyle(style);
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        mScene.clear();
    }
}
