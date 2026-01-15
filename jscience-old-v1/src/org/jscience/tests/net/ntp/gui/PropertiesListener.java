package org.jscience.tests.net.ntp.gui;

import java.util.Properties;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
interface PropertiesListener {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Properties getProperties();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Properties getDefaultProperties();

    /**
     * DOCUMENT ME!
     */
    void applyProperties();
}
