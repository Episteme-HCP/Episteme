// Toggle Switch Button Bean Info Class
package org.jscience.awt.buttons;

import java.awt.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ToggleSwitchButtonBeanInfo extends SimpleBeanInfo {
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
            Image img = loadImage("ToggleSwitchButtonIcon16.gif");

            return img;
        }

        if (iconKind == BeanInfo.ICON_COLOR_32x32) {
            Image img = loadImage("ToggleSwitchButtonIcon32.gif");

            return img;
        }

        return null;
    }

    // Explicit declare the properties
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor // Descriptor for each properties
            fontStyle = new PropertyDescriptor("fontStyle",
                    ToggleSwitchButton.class);
            PropertyDescriptor bottomCaption = new PropertyDescriptor("bottomCaption",
                    ToggleSwitchButton.class);
            PropertyDescriptor width = new PropertyDescriptor("width",
                    ToggleSwitchButton.class);
            PropertyDescriptor height = new PropertyDescriptor("height",
                    ToggleSwitchButton.class);
            PropertyDescriptor fontName = new PropertyDescriptor("fontName",
                    ToggleSwitchButton.class);
            PropertyDescriptor sticky = new PropertyDescriptor("sticky",
                    ToggleSwitchButton.class);
            PropertyDescriptor foreground = new PropertyDescriptor("foreground",
                    ToggleSwitchButton.class);
            PropertyDescriptor highlight = new PropertyDescriptor("highlight",
                    ToggleSwitchButton.class);
            PropertyDescriptor state = new PropertyDescriptor("state",
                    ToggleSwitchButton.class);
            PropertyDescriptor captionAtBottom = new PropertyDescriptor("captionAtBottom",
                    ToggleSwitchButton.class);
            PropertyDescriptor caption = new PropertyDescriptor("caption",
                    ToggleSwitchButton.class);
            PropertyDescriptor background = new PropertyDescriptor("background",
                    ToggleSwitchButton.class);
            PropertyDescriptor font = new PropertyDescriptor("font",
                    ToggleSwitchButton.class);
            PropertyDescriptor fontSize = new PropertyDescriptor("fontSize",
                    ToggleSwitchButton.class);
            PropertyDescriptor buttonColor = new PropertyDescriptor("buttonColor",
                    ToggleSwitchButton.class);
            PropertyDescriptor panelColor = new PropertyDescriptor("panelColor",
                    ToggleSwitchButton.class);
            PropertyDescriptor name = new PropertyDescriptor("name",
                    ToggleSwitchButton.class);
            PropertyDescriptor textColor = new PropertyDescriptor("textColor",
                    ToggleSwitchButton.class);
            PropertyDescriptor topCaption = new PropertyDescriptor("topCaption",
                    ToggleSwitchButton.class);

            // Hide the un-need properties
            height.setHidden(true);
            foreground.setHidden(true);
            highlight.setHidden(true);
            captionAtBottom.setHidden(true);
            caption.setHidden(true);
            background.setHidden(true);
            name.setHidden(true);

            // Prepare array of property descriptors for return
            PropertyDescriptor[] pd = {
                    foreground, highlight, captionAtBottom, caption, background,
                    name,
                    
                    width, height, sticky, state, topCaption, bottomCaption,
                    buttonColor, textColor, panelColor, font, fontName,
                    fontStyle, fontSize
                };

            return pd;
        } catch (IntrospectionException e) {
            throw new Error(e.toString());
        }
    }
}
