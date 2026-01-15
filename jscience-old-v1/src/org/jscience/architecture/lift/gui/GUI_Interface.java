package org.jscience.architecture.lift.gui;

/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:53 $
 */
public interface GUI_Interface {
    /**
     * DOCUMENT ME!
     */
    public void setup();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean render();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isShown();

    /**
     * DOCUMENT ME!
     */
    public void showGUI();

    /**
     * DOCUMENT ME!
     */
    public void hideGUI();
}
