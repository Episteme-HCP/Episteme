package org.jscience.chemistry.gui.extended.beans;

import org.jscience.chemistry.gui.extended.graphics3d.Panel3D;
import org.jscience.chemistry.gui.extended.molecule.Molecule;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class CentralDisplayAdapter extends MolViewerEventAdapter {
    /** DOCUMENT ME! */
    Panel3D myPanel;

/**
     * Creates a new CentralDisplayAdapter object.
     *
     * @param panel DOCUMENT ME!
     */
    public CentralDisplayAdapter(Panel3D panel) {
        myPanel = panel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void centralDisplayChange(MolViewerEvent event) {
        if (event.getType() == MolViewerEvent.REPLACE_MOLECULE) {
            myPanel.clear();
            myPanel.addMolecule((Molecule) event.getParam());
        }
    }
}
