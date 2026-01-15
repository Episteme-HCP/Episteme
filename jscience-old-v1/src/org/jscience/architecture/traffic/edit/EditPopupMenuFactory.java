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
package org.jscience.architecture.traffic.edit;

import org.jscience.architecture.traffic.PopupException;
import org.jscience.architecture.traffic.Selectable;
import org.jscience.architecture.traffic.infrastructure.Drivelane;
import org.jscience.architecture.traffic.infrastructure.Node;
import org.jscience.architecture.traffic.infrastructure.Road;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


/**
 * Factory for creating popup menus for editor
 *
 * @author Group GUI
 * @version 1.0
 */
public class EditPopupMenuFactory {
    /** DOCUMENT ME! */
    protected EditController controller;

/**
     * Creates a new EditPopupMenuFactory object.
     *
     * @param con DOCUMENT ME!
     */
    public EditPopupMenuFactory(EditController con) {
        controller = con;
    }

    /**
     * Creates a right-click PopupMenu for the given object. A listener
     * is added to the menu as well.
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws PopupException DOCUMENT ME!
     */
    public PopupMenu getPopupMenuFor(Selectable obj) throws PopupException {
        if (obj instanceof Node) {
            return getNodeMenu((Node) obj);
        }

        if (obj instanceof Road) {
            return getRoadMenu((Road) obj);
        }

        if (obj instanceof Drivelane) {
            return getDrivelaneMenu((Drivelane) obj);
        }

        throw new PopupException("Unknown object type");
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws PopupException DOCUMENT ME!
     */
    protected PopupMenu getNodeMenu(Node n) throws PopupException {
        return getGenericMenu(new DefaultGUIObjectListener());
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws PopupException DOCUMENT ME!
     */
    protected PopupMenu getRoadMenu(Road r) throws PopupException {
        return getGenericMenu(new DefaultGUIObjectListener());
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws PopupException DOCUMENT ME!
     */
    protected PopupMenu getDrivelaneMenu(Drivelane l) throws PopupException {
        return getGenericMenu(new DefaultGUIObjectListener());
    }

    /**
     * DOCUMENT ME!
     *
     * @param pml DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected PopupMenu getGenericMenu(PopupMenuListener pml) {
        PopupMenu menu = new PopupMenu();
        MenuItem item = new MenuItem("Delete",
                new MenuShortcut(KeyEvent.VK_DELETE));
        item.addActionListener(pml);
        menu.add(item);

        menu.add(new MenuItem("-"));

        item = new MenuItem("Properties...", new MenuShortcut(KeyEvent.VK_ENTER));
        item.addActionListener(pml);
        menu.add(item);

        return menu;
    }

/**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected static interface PopupMenuListener extends ActionListener {
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected class DefaultGUIObjectListener implements PopupMenuListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String s = e.getActionCommand();

            if (s.equals("Delete")) {
                controller.deleteSelection();
            } else if (s.equals("Properties...")) {
                controller.showConfigDialog();
            }
        }
    }
}
