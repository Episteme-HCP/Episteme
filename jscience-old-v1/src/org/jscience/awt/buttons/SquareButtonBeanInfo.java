// SquareButtonBeanInfo Class
package org.jscience.awt.buttons;

import java.awt.*;

import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class SquareButtonBeanInfo extends SimpleBeanInfo {
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
            Image img = loadImage("SquareButtonIcon16.gif");

            return img;
        }

        if (iconKind == BeanInfo.ICON_COLOR_32x32) {
            Image img = loadImage("SquareButtonIcon32.gif");

            return img;
        }

        return null;
    }
}
