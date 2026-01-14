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

package org.jscience.architecture.traffic.simulation;

import org.jscience.architecture.traffic.PopupException;
import org.jscience.architecture.traffic.Selectable;
import org.jscience.architecture.traffic.TrafficException;
import org.jscience.architecture.traffic.infrastructure.*;
import org.jscience.architecture.traffic.simulation.statistics.TrackerFactory;

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
public class SimPopupMenuFactory {
    /** DOCUMENT ME! */
    protected SimController controller;

/**
     * Creates a new SimPopupMenuFactory object.
     *
     * @param con DOCUMENT ME!
     */
    public SimPopupMenuFactory(SimController con) {
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

    /* Node popup menu's & listeners */
    protected PopupMenu getNodeMenu(Node n) throws PopupException {
        PopupMenuListener pml = null;

        if (n instanceof EdgeNode) {
            return getEdgeNodeMenu((EdgeNode) n);
        }

        if (n instanceof Junction) {
            return getJunctionMenu((Junction) n);
        }

        if (n instanceof NetTunnel) {
            return getNetTunnelMenu((NetTunnel) n);
        }

        throw new PopupException("Unknown Node type");
    }

    // EdgeNode popup menu
    /**
     * DOCUMENT ME!
     *
     * @param edge DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected PopupMenu getEdgeNodeMenu(EdgeNode edge) {
        PopupMenu menu = new PopupMenu();
        PopupMenuListener pml = new EdgeNodePopupListener(edge, controller);

        String[] items = {
                "Track waiting queue length", "Track roadusers arrived",
                "Track trip waiting time"
            };
        MenuItem item;

        for (int i = 0; i < items.length; i++) {
            item = new MenuItem(items[i]);
            item.addActionListener(pml);
            menu.add(item);
        }

        menu.add(new MenuItem("-"));

        item = new MenuItem("Properties...", new MenuShortcut(KeyEvent.VK_ENTER));
        item.addActionListener(pml);
        menu.add(item);

        return menu;
    }

    // NetTunnel popup menu
    /**
     * DOCUMENT ME!
     *
     * @param tunnel DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected PopupMenu getNetTunnelMenu(NetTunnel tunnel) {
        PopupMenu menu = new PopupMenu();
        PopupMenuListener pml = new NetTunnelPopupListener(tunnel, controller);

        String[] items = {
                "Track roadusers arrived", "Track trip waiting time",
                "Track waiting (receive) queue", "Track send queue"
            };
        MenuItem item;

        for (int i = 0; i < items.length; i++) {
            item = new MenuItem(items[i]);
            item.addActionListener(pml);
            menu.add(item);
        }

        menu.add(new MenuItem("-"));

        item = new MenuItem("Properties...", new MenuShortcut(KeyEvent.VK_ENTER));
        item.addActionListener(pml);
        menu.add(item);

        return menu;
    }

    // Junction popup menu
    /**
     * DOCUMENT ME!
     *
     * @param junction DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected PopupMenu getJunctionMenu(Junction junction) {
        PopupMenu menu = new PopupMenu();
        PopupMenuListener pml = new JunctionPopupListener(junction, controller);

        String[] items = {
                "Track roadusers that crossed", "Track junction waiting time"
            };
        MenuItem item;

        for (int i = 0; i < items.length; i++) {
            item = new MenuItem(items[i]);
            item.addActionListener(pml);
            menu.add(item);
        }

        menu.add(new MenuItem("-"));

        item = new MenuItem("Properties...", new MenuShortcut(KeyEvent.VK_ENTER));
        item.addActionListener(pml);
        menu.add(item);

        return menu;
    }

    /* Road popup menu & listeners */
    protected PopupMenu getRoadMenu(Road r) {
        PopupMenu menu = new PopupMenu();
        PopupMenuListener pml = new RoadPopupListener(r, controller);

        MenuItem item = new MenuItem("Properties...",
                new MenuShortcut(KeyEvent.VK_ENTER));
        item.addActionListener(pml);
        menu.add(item);

        return menu;
    }

    /* Drivelane popup menu & listeners */
    protected PopupMenu getDrivelaneMenu(Drivelane l) {
        PopupMenu menu = new PopupMenu();
        PopupMenuListener pml = new LanePopupListener(l, controller);

        MenuItem item = new MenuItem("Properties...",
                new MenuShortcut(KeyEvent.VK_ENTER));
        item.addActionListener(pml);
        menu.add(item);

        return menu;
    }

    /* Popup menu listener interface */
    protected static interface PopupMenuListener extends ActionListener {
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected static class EdgeNodePopupListener implements PopupMenuListener {
        /** DOCUMENT ME! */
        SimController controller;

        /** DOCUMENT ME! */
        EdgeNode node;

/**
         * Creates a new EdgeNodePopupListener object.
         *
         * @param n   DOCUMENT ME!
         * @param con DOCUMENT ME!
         */
        public EdgeNodePopupListener(EdgeNode n, SimController con) {
            controller = con;
            node = n;
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String sel = e.getActionCommand();

            try {
                if (sel.equals("Properties...")) {
                    controller.showConfigDialog();
                } else if (sel.equals("Track waiting queue length")) {
                    TrackerFactory.showTracker(controller.getSimModel(),
                        controller, node, TrackerFactory.SPECIAL_QUEUE);
                } else if (sel.equals("Track trip waiting time")) {
                    TrackerFactory.showTracker(controller.getSimModel(),
                        controller, node, TrackerFactory.SPECIAL_WAIT);
                } else if (sel.equals("Track roadusers arrived")) {
                    TrackerFactory.showTracker(controller.getSimModel(),
                        controller, node, TrackerFactory.SPECIAL_ROADUSERS);
                }
            } catch (TrafficException exc) {
                controller.showError(exc.toString());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected static class NetTunnelPopupListener implements PopupMenuListener {
        /** DOCUMENT ME! */
        SimController controller;

        /** DOCUMENT ME! */
        NetTunnel node;

/**
         * Creates a new NetTunnelPopupListener object.
         *
         * @param n   DOCUMENT ME!
         * @param con DOCUMENT ME!
         */
        public NetTunnelPopupListener(NetTunnel n, SimController con) {
            controller = con;
            node = n;
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String sel = e.getActionCommand();

            try {
                if (sel.equals("Properties...")) {
                    controller.showConfigDialog();
                } else if (sel.equals("Track trip waiting time")) {
                    TrackerFactory.showTracker(controller.getSimModel(),
                        controller, node, TrackerFactory.SPECIAL_WAIT);
                } else if (sel.equals("Track roadusers arrived")) {
                    TrackerFactory.showTracker(controller.getSimModel(),
                        controller, node, TrackerFactory.SPECIAL_ROADUSERS);
                } else if (sel.equals("Track waiting (receive) queue")) {
                    TrackerFactory.showTracker(controller.getSimModel(),
                        controller, node, TrackerFactory.SPECIAL_QUEUE);
                } else if (sel.equals("Track send queue")) {
                    TrackerFactory.showTracker(controller.getSimModel(),
                        controller, node, TrackerFactory.NETTUNNEL_SEND);
                }
            } catch (TrafficException exc) {
                controller.showError(exc.toString());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected static class JunctionPopupListener implements PopupMenuListener {
        /** DOCUMENT ME! */
        SimController controller;

        /** DOCUMENT ME! */
        Junction node;

/**
         * Creates a new JunctionPopupListener object.
         *
         * @param n   DOCUMENT ME!
         * @param con DOCUMENT ME!
         */
        public JunctionPopupListener(Junction n, SimController con) {
            controller = con;
            node = n;
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String sel = e.getActionCommand();

            if (sel.equals("Properties...")) {
                controller.showConfigDialog();
            } else {
                try {
                    if (sel.equals("Track roadusers that crossed")) {
                        TrackerFactory.showTracker(controller.getSimModel(),
                            controller, node, TrackerFactory.JUNCTION_ROADUSERS);
                    } else if (sel.equals("Track junction waiting time")) {
                        TrackerFactory.showTracker(controller.getSimModel(),
                            controller, node, TrackerFactory.JUNCTION_WAIT);
                    }
                } catch (TrafficException exc) {
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected static class RoadPopupListener implements PopupMenuListener {
        /** DOCUMENT ME! */
        SimController controller;

        /** DOCUMENT ME! */
        Road road;

/**
         * Creates a new RoadPopupListener object.
         *
         * @param r   DOCUMENT ME!
         * @param con DOCUMENT ME!
         */
        public RoadPopupListener(Road r, SimController con) {
            controller = con;
            road = r;
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Properties...")) {
                controller.showConfigDialog();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected static class LanePopupListener implements PopupMenuListener {
        /** DOCUMENT ME! */
        SimController controller;

        /** DOCUMENT ME! */
        Drivelane lane;

/**
         * Creates a new LanePopupListener object.
         *
         * @param l   DOCUMENT ME!
         * @param con DOCUMENT ME!
         */
        public LanePopupListener(Drivelane l, SimController con) {
            controller = con;
            lane = l;
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Properties...")) {
                controller.showConfigDialog();
            }
        }
    }
}
