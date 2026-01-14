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

package org.jscience.architecture.traffic.util;

import java.awt.*;
import java.awt.event.ActionListener;


/**
 * Basic ToolBar class. Designed on Windows, so it may not look as good on
 * other platforms...
 *
 * @author Group Joep Moritz (No, I'm really not schizo.)
 * @version 1.0
 */
public class ToolBar extends Panel {
    //	Vector tools;
    /** DOCUMENT ME! */
    protected int separatorWidth = 8;

    /** DOCUMENT ME! */
    protected int buttonWidth = 24;

    /** DOCUMENT ME! */
    protected int buttonHeight = 24;

    /** DOCUMENT ME! */
    protected int totalWidth = 0;

/**
     * Creates a new ToolBar object.
     */
    public ToolBar() {
        super();

        //		tools = new Vector(3);
        setLayout(null);
        setBackground(Color.lightGray);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSeparatorWidth() {
        return separatorWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     */
    public void setSeparatorWidth(int w) {
        separatorWidth = w;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getButtonWidth() {
        return buttonWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     */
    public void setButtonWidth(int w) {
        buttonWidth = w;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getButtonHeight() {
        return buttonHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param h DOCUMENT ME!
     */
    public void setButtonHeight(int h) {
        buttonHeight = h;
    }

    /**
     * Adds an IconButton to this toolbar.
     *
     * @param imgurl The url of the image for the new IconButton
     * @param al The ActionListener to add to the new IconButton
     * @param id The id for the new button
     */
    public void addButton(String imgurl, ActionListener al, int id) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        IconButton b = new IconButton(tk.getImage(imgurl)
                                        .getScaledInstance(buttonWidth - 6,
                    buttonHeight - 6, Image.SCALE_SMOOTH), id);
        b.setBounds(totalWidth, 0, buttonWidth, buttonHeight);
        b.addActionListener(al);
        add(b);

        totalWidth += buttonWidth;
    }

    /**
     * Adds a separator to this toolbar
     */
    public void addSeparator() {
        Panel p = new Panel(null);
        p.setSize(separatorWidth, buttonHeight);
        add(p);

        totalWidth += separatorWidth;
    }

    /**
     * Adds a component to this toolbar
     *
     * @param c DOCUMENT ME!
     */
    public void addComponent(Component c) {
        add(c);
        c.setLocation(totalWidth, 0);
        totalWidth += c.getWidth();
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void remComponent(Component c) {
        remove(c);

        totalWidth -= c.getWidth();
    }
}
