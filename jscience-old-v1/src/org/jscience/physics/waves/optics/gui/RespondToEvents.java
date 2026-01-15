/**
 * Title:        NewProj
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright:    Copyright (c) imt
 * </p>
 *
 * <p>
 * Company:      imt
 * </p>
 *
 * <p></p>
 */
package org.jscience.physics.waves.optics.gui;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public interface RespondToEvents {
    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void scrollbar(String name, int value);

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void checkbox(String name, boolean value);

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void button(String name);
}
