/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
