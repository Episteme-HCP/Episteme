/*
 * @(#)ContrastTheme.java   1.3 99/07/22
 *
 * Copyright (c) 1997-1999 by Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
package org.jscience.swing;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.DefaultMetalTheme;


/**
 * This class describes a higher-contrast Metal Theme.
 *
 * @author Michael C. Albers
 * @version 1.3 07/22/99
 */
class ContrastTheme extends DefaultMetalTheme {
    /** DOCUMENT ME! */
    private final ColorUIResource primary1 = new ColorUIResource(0, 0, 0);

    /** DOCUMENT ME! */
    private final ColorUIResource primary2 = new ColorUIResource(204, 204, 204);

    /** DOCUMENT ME! */
    private final ColorUIResource primary3 = new ColorUIResource(255, 255, 255);

    /** DOCUMENT ME! */
    private final ColorUIResource primaryHighlight = new ColorUIResource(102,
            102, 102);

    /** DOCUMENT ME! */
    private final ColorUIResource secondary2 = new ColorUIResource(204, 204, 204);

    /** DOCUMENT ME! */
    private final ColorUIResource secondary3 = new ColorUIResource(255, 255, 255);

    /** DOCUMENT ME! */
    private final ColorUIResource controlHighlight = new ColorUIResource(102,
            102, 102);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "Contrast";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ColorUIResource getPrimary2() {
        return primary2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ColorUIResource getPrimary3() {
        return primary3;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ColorUIResource getPrimaryControlHighlight() {
        return primaryHighlight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ColorUIResource getSecondary2() {
        return secondary2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ColorUIResource getSecondary3() {
        return secondary3;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ColorUIResource getControlHighlight() {
        return super.getSecondary3();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ColorUIResource getFocusColor() {
        return getBlack();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ColorUIResource getTextHighlightColor() {
        return getBlack();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ColorUIResource getHighlightedTextColor() {
        return getWhite();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ColorUIResource getMenuSelectedBackground() {
        return getBlack();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ColorUIResource getMenuSelectedForeground() {
        return getWhite();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ColorUIResource getAcceleratorForeground() {
        return getBlack();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ColorUIResource getAcceleratorSelectedForeground() {
        return getWhite();
    }

    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     */
    public void addCustomEntriesToTable(UIDefaults table) {
        Border blackLineBorder = new BorderUIResource(new LineBorder(getBlack()));
        Border whiteLineBorder = new BorderUIResource(new LineBorder(getWhite()));

        Object textBorder = new BorderUIResource(new CompoundBorder(
                    blackLineBorder, new BasicBorders.MarginBorder()));

        table.put("ToolTip.border", blackLineBorder);
        table.put("TitledBorder.border", blackLineBorder);
        table.put("Table.focusCellHighlightBorder", whiteLineBorder);
        table.put("Table.focusCellForeground", getWhite());

        table.put("TextField.border", textBorder);
        table.put("PasswordField.border", textBorder);
        table.put("TextArea.border", textBorder);
        table.put("TextPane.font", textBorder);
    }
}
