// IntLEDDisplayBeanInfo.java
package org.jscience.awt.displays;


// Imports
import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class IntLEDDisplayBeanInfo extends SimpleBeanInfo {
    // Get the appropriate icon
    /**
     * DOCUMENT ME!
     *
     * @param iconKind DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.awt.Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_COLOR_16x16) {
            java.awt.Image img = loadImage("LEDDisplayIcon16.gif");

            return img;
        }

        if (iconKind == BeanInfo.ICON_COLOR_32x32) {
            java.awt.Image img = loadImage("LEDDisplayIcon32.gif");

            return img;
        }

        return null;
    }
}
