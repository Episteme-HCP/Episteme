/*-----------------------------------------------------------------------
 * Copyright (C) 2001 Green Light District Team, Utrecht University
 *
 * This program (Green Light District) is free software.
 * You may redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by
 * the Free Software Foundation (version 2 or later).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * See the documentation of Green Light District for further information.
 *------------------------------------------------------------------------*/
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
