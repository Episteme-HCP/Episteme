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

import org.jscience.architecture.traffic.FileMenu;
import org.jscience.architecture.traffic.HelpMenu;
import org.jscience.architecture.traffic.algorithms.dp.DPFactory;
import org.jscience.architecture.traffic.algorithms.tlc.TLCFactory;
import org.jscience.architecture.traffic.simulation.statistics.TrackerFactory;
import org.jscience.architecture.traffic.util.CheckMenu;

import java.awt.*;
import java.awt.event.*;


/**
 * The MenuBar for the editor
 *
 * @author Group GUI
 * @version 1.0
 */
public class SimMenuBar extends MenuBar {
    /** DOCUMENT ME! */
    SimController controller;

    /** DOCUMENT ME! */
    SpeedMenu speedMenu;

    /** DOCUMENT ME! */
    TLCMenu tlcMenu;

    /** DOCUMENT ME! */
    DPMenu dpMenu;

    /** DOCUMENT ME! */
    CheckboxMenuItem viewEnabled;

    /** DOCUMENT ME! */
    CheckboxMenuItem cycleCounterEnabled;

/**
     * Creates a new SimMenuBar object.
     *
     * @param sc         DOCUMENT ME!
     * @param speedTexts DOCUMENT ME!
     */
    public SimMenuBar(SimController sc, String[] speedTexts) {
        String[] trackers = {
                "Total waiting queue length", "Total roadusers arrived",
                "Average trip waiting time", "Average junction waiting time"
            };

        String[] dps = DPFactory.getDescriptions();

        controller = sc;

        Menu menu;
        MenuItem item;

        add(new FileMenu(controller, false));

        /*  Simulation */
        menu = new Menu("Simulation");
        add(menu);

        SimListener simListener = new SimListener();

        item = new MenuItem("Do one step", new MenuShortcut(KeyEvent.VK_D));
        menu.add(item);
        item.addActionListener(simListener);

        item = new MenuItem("Run", new MenuShortcut(KeyEvent.VK_R));
        menu.add(item);
        item.addActionListener(simListener);

        item = new MenuItem("Pause", new MenuShortcut(KeyEvent.VK_U));
        menu.add(item);
        item.addActionListener(simListener);

        item = new MenuItem("Stop", new MenuShortcut(KeyEvent.VK_P));
        menu.add(item);
        item.addActionListener(simListener);

        item = new MenuItem("Run Series", new MenuShortcut(KeyEvent.VK_S));
        menu.add(item);
        item.addActionListener(simListener);

        /* Speed */
        speedMenu = new SpeedMenu(speedTexts);
        menu.add(speedMenu);

        /* Statistics */
        menu = new Menu("Statistics");
        add(menu);

        StatsListener statsListener = new StatsListener();

        item = new MenuItem("Show statistics", new MenuShortcut(KeyEvent.VK_T));
        menu.add(item);
        item.addActionListener(statsListener);

        Menu submenu = new Menu("Track");

        for (int i = 0; i < trackers.length; i++) {
            item = new MenuItem(trackers[i]);
            submenu.add(item);
            item.addActionListener(statsListener);
        }

        menu.add(submenu);

        menu.add(new MenuItem("-"));

        CheckboxMenuItem citem = new CheckboxMenuItem("Toggle in-view statistics",
                false);
        menu.add(citem);
        citem.addItemListener(statsListener);

        /* Options */
        menu = new Menu("Options");
        add(menu);

        OptionMenuListener ol = new OptionMenuListener();

        viewEnabled = new CheckboxMenuItem("Toggle view", true);
        menu.add(viewEnabled);
        viewEnabled.setName("view");
        viewEnabled.addItemListener(ol);

        cycleCounterEnabled = new CheckboxMenuItem("Toggle cycle counter", true);
        menu.add(cycleCounterEnabled);
        cycleCounterEnabled.setName("cyclecounter");
        cycleCounterEnabled.addItemListener(ol);

        menu.add(new MenuItem("-"));

        tlcMenu = new TLCMenu();
        menu.add(tlcMenu);

        dpMenu = new DPMenu(dps);
        menu.add(dpMenu);

        menu.add(new MenuItem("-"));

        item = new MenuItem("Open editor", new MenuShortcut(KeyEvent.VK_E));
        menu.add(item);
        item.addActionListener(ol);

        item = new MenuItem("Settings...");
        menu.add(item);
        item.addActionListener(ol);

        add(new HelpMenu(controller));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SpeedMenu getSpeedMenu() {
        return speedMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TLCMenu getTLCMenu() {
        return tlcMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DPMenu getDPMenu() {
        return dpMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    protected void setViewEnabled(boolean b) {
        viewEnabled.setState(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    protected void setCycleCounterEnabled(boolean b) {
        cycleCounterEnabled.setState(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public class SpeedMenu extends CheckMenu implements ItemListener {
/**
         * Creates a new SpeedMenu object.
         *
         * @param texts DOCUMENT ME!
         */
        public SpeedMenu(String[] texts) {
            super("Speed", texts);
            addItemListener(this);
            select(1);
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void itemStateChanged(ItemEvent e) {
            controller.setSpeed(getSelectedIndex());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public class TLCMenu extends Menu implements ItemListener {
        /** DOCUMENT ME! */
        CheckMenu[] submenus;

        /** DOCUMENT ME! */
        int selectedMenu = -1;

/**
         * Creates a new TLCMenu object.
         */
        public TLCMenu() {
            super("Traffic light controller");

            String[] tlcCats = TLCFactory.getCategoryDescs();
            String[] tlcDescs = TLCFactory.getTLCDescriptions();
            int[][] allTLCs = TLCFactory.getCategoryTLCs();

            submenus = new CheckMenu[tlcCats.length];

            CheckMenu subsubmenu;
            String[] texts;

            for (int i = 0; i < tlcCats.length; i++) {
                texts = new String[allTLCs[i].length];

                for (int j = 0; j < texts.length; j++)
                    texts[j] = tlcDescs[allTLCs[i][j]];

                subsubmenu = new CheckMenu(tlcCats[i], texts);
                add(subsubmenu);
                subsubmenu.addItemListener(this);

                submenus[i] = subsubmenu;
            }

            setTLC(0, 0);
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void itemStateChanged(ItemEvent e) {
            CheckMenu cm = (CheckMenu) e.getItemSelectable();

            for (int i = 0; i < submenus.length; i++) {
                if (submenus[i] == cm) {
                    selectedMenu = i;
                    controller.setTLC(i, cm.getSelectedIndex());
                } else {
                    submenus[i].deselectAll();
                }
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param cat DOCUMENT ME!
         * @param tlc DOCUMENT ME!
         */
        public void setTLC(int cat, int tlc) {
            for (int i = 0; i < submenus.length; i++)
                submenus[i].deselectAll();

            submenus[cat].select(tlc);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getCategory() {
            return selectedMenu;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getTLC() {
            return submenus[selectedMenu].getSelectedIndex();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public class DPMenu extends CheckMenu implements ItemListener {
/**
         * Creates a new DPMenu object.
         *
         * @param dps DOCUMENT ME!
         */
        public DPMenu(String[] dps) {
            super("Driving policy", dps);
            addItemListener(this);
            select(0);
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void itemStateChanged(ItemEvent e) {
            controller.setDrivingPolicy(getSelectedIndex());
        }
    }

    /*============================================*/
    /* Listeners                                  */
    /*============================================*/
    /**
     * Listens to the "Statistics" menu
     */
    public class StatsListener implements ActionListener, ItemListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String sel = e.getActionCommand();

            if (controller.getSimModel().getInfrastructure().getNumNodes() == 0) {
                controller.showError(
                    "Please load an infrastructure or simulation before opening any statistics windows.");

                return;
            }

            if (sel.equals("Show statistics")) {
                controller.showStatistics();
            } else if (sel.equals("Total waiting queue length")) {
                controller.showTracker(TrackerFactory.TOTAL_QUEUE);
            } else if (sel.equals("Total roadusers arrived")) {
                controller.showTracker(TrackerFactory.TOTAL_ROADUSERS);
            } else if (sel.equals("Average trip waiting time")) {
                controller.showTracker(TrackerFactory.TOTAL_WAIT);
            } else if (sel.equals("Average junction waiting time")) {
                controller.showTracker(TrackerFactory.TOTAL_JUNCTION);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void itemStateChanged(ItemEvent e) {
            CheckboxMenuItem item = (CheckboxMenuItem) e.getItemSelectable();

            if (item.getState()) {
                controller.enableOverlay();
            } else {
                controller.disableOverlay();
            }
        }
    }

    /**
     * Listens to the "Options" menu
     */
    public class OptionMenuListener implements ActionListener, ItemListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String ac = e.getActionCommand();

            if (ac == "Open editor") {
                controller.openEditor();
            } else if (ac == "Settings...") {
                controller.showSettings();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void itemStateChanged(ItemEvent e) {
            CheckboxMenuItem item = (CheckboxMenuItem) e.getItemSelectable();
            boolean enable = item.getState();

            if (item.getName().equals("view")) {
                controller.setViewEnabled(enable);
            } else {
                controller.setCycleCounterEnabled(enable);
            }
        }
    }

    /**
     * Listens to the "Simulation" menu
     */
    public class SimListener implements ActionListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String sel = e.getActionCommand();

            if (sel.equals("Do one step")) {
                controller.doStep();
            } else if (sel.equals("Run")) {
                controller.unpause();
            } else if (sel.equals("Pause")) {
                controller.pause();
            } else if (sel.equals("Stop")) {
                controller.stop();
            } else if (sel.equals("Run Series")) {
                controller.runSeries();
            }
        }
    }
}
