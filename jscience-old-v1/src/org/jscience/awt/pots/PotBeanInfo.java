// PotBeanInfo Class
package org.jscience.awt.pots;

import java.awt.*;

import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class PotBeanInfo extends SimpleBeanInfo {
    // Get the appropriate icon
    /**
     * DOCUMENT ME!
     *
     * @param iconKind DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_COLOR_16x16) {
            Image image = loadImage("PotIcon16.gif");

            return image;
        }

        if (iconKind == BeanInfo.ICON_COLOR_32x32) {
            Image image = loadImage("PotIcon32.gif");

            return image;
        }

        return null;
    }
}
