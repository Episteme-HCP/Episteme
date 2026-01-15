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
