package org.jscience.chemistry.gui.extended.molecule;

import java.util.Properties;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public abstract interface PropAttributesInter {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Properties getAttributes();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropAttributes getPropAttributes();

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setAttributes(Properties p);

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setPropAttributes(PropAttributes p);
}
