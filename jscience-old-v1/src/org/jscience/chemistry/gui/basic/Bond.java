package org.jscience.chemistry.gui.basic;

/**
 * A class representing Bonds in 2D.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this code is adapted from Eric Harlow
//http://www.netbrain.com/~brain/molecule/
//bigbrain@netbrain.com
public class Bond {
    /** DOCUMENT ME! */
    int m_nIndex1;

    /** DOCUMENT ME! */
    int m_nIndex2;

    /** DOCUMENT ME! */
    boolean m_painted;

/**
     * Creates a new Bond object.
     *
     * @param nIndex1 DOCUMENT ME!
     * @param nIndex2 DOCUMENT ME!
     */
    Bond(int nIndex1, int nIndex2) {
        m_nIndex1 = nIndex1;
        m_nIndex2 = nIndex2;
        m_painted = false;
    }
}
