// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.gui;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class ImportDialogLayout implements LayoutManager {
    /**
     * Creates a new ImportDialogLayout object.
     */
    public ImportDialogLayout() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void addLayoutComponent(String name, Component c) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void removeLayoutComponent(Component c) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension preferredLayoutSize(Container target) {
        return new Dimension(500, 500);
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension minimumLayoutSize(Container target) {
        return new Dimension(100, 100);
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     */
    public void layoutContainer(Container target) {
        Insets insets = target.insets();
        int targetw = target.size().width - insets.left - insets.right;
        int targeth = target.size().height - (insets.top + insets.bottom);
        int i;
        int pw = 300;

        if (target.getComponentCount() == 0) {
            return;
        }

        Component cl = target.getComponent(target.getComponentCount() - 1);
        Dimension dl = cl.getPreferredSize();
        target.getComponent(0).move(insets.left, insets.top);

        int cw = target.size().width - insets.left - insets.right;
        int ch = target.size().height - insets.top - insets.bottom - dl.height;
        target.getComponent(0).resize(cw, ch);

        int h = ch + insets.top;
        int x = 0;

        for (i = 1; i < target.getComponentCount(); i++) {
            Component m = target.getComponent(i);

            if (m.isVisible()) {
                Dimension d = m.getPreferredSize();
                m.move(insets.left + x, h);
                m.resize(d.width, d.height);
                x += d.width;
            }
        }
    }
}
