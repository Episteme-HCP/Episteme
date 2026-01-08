package org.jscience.ui.viewers.chemistry.backend;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import javafx.scene.paint.Color;


/**
 * Abstraction for molecular rendering engines.
 * implementations can use JavaFX (Native), Jmol, PyMOL, etc.
 */
public interface MolecularRenderer {

    /**
     * Clears the current view.
     */
    void clear();

    /**
     * Set global rendering style.
     * 
     * @param style The style to apply.
     */
    void setStyle(RenderStyle style);

    /**
     * Draw a single atom.
     * 
     * @param atom The atom data model.
     */
    void drawAtom(Atom atom);

    /**
     * Draw a bond between two atoms.
     * 
     * @param bond The bond data model.
     */
    void drawBond(Bond bond);

    /**
     * Set background color of the viewer.
     * 
     * @param color JavaFX Color (Implementations may convert this).
     */
    void setBackgroundColor(Color color);

    /**
     * Retrieves the native component (e.g. SubScene for JavaFX, JPanel for
     * Swing/Jmol).
     * 
     * @return The UI component.
     */
    Object getViewComponent();

    /**
     * Returns the backend type of this renderer.
     * 
     * @return The backend enum.
     */
    MolecularBackend getBackend();
}
