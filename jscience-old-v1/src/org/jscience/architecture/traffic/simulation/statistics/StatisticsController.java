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

package org.jscience.architecture.traffic.simulation.statistics;

import org.jscience.architecture.traffic.infrastructure.Node;
import org.jscience.architecture.traffic.simulation.SimController;
import org.jscience.architecture.traffic.simulation.SimModel;
import org.jscience.architecture.traffic.util.CheckMenu;

import java.awt.*;
import java.awt.event.*;

import java.io.IOException;


/**
 * The controller for the statistics viewer, it controlls the
 * StatisticsView.
 *
 * @author Group GUI
 * @version 1.0
 */
public class StatisticsController extends Frame {
    /** Currently available views. */
    protected final static String[] viewDescs = { "Summary", "Table" }; //, "Graphical" };

    /** DOCUMENT ME! */
    protected SimController parent;

    /** DOCUMENT ME! */
    protected StatisticsView view = null;

    /** DOCUMENT ME! */
    protected StatisticsModel stats;

    /** DOCUMENT ME! */
    protected CheckMenu viewCM;

    /** DOCUMENT ME! */
    protected CheckMenu modeCM;

    /** DOCUMENT ME! */
    protected Scrollbar sbHorizontal;

    /** DOCUMENT ME! */
    protected Scrollbar sbVertical;

/**
     * Creates a <code>StatisticsController</code>.
     *
     * @param model   The<code>SimModel</code> statistics should be read from.
     * @param _parent The parent<code>SimController</code>.
     */
    public StatisticsController(SimModel model, SimController _parent) {
        stats = new StatisticsModel(model);
        parent = _parent;
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    closeWindow();
                }
            });
        setBounds(200, 200, 400, 300);
        setBackground(Color.lightGray);

        add(sbVertical = new Scrollbar(Scrollbar.VERTICAL, 0, 1, 0, 1),
            BorderLayout.EAST);
        sbVertical.addAdjustmentListener(new ScrollListener());
        add(sbHorizontal = new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, 1),
            BorderLayout.SOUTH);
        sbHorizontal.addAdjustmentListener(new ScrollListener());

        setMenuBar(makeMenuBar());
        setView("Summary");
        refresh();
        setVisible(true);
        view.requestFocus();
    }

    /**
     * DOCUMENT ME!
     *
     * @param hor DOCUMENT ME!
     * @param ver DOCUMENT ME!
     */
    public void setScrollMax(int hor, int ver) {
        sbHorizontal.setMaximum(hor);
        sbVertical.setMaximum(ver);
    }

    /*============================================*/
    /* Invoked by listeners                       */
    /*============================================*/
    /**
     * Closes the <code>StatisticsController</code>.
     */
    private void closeWindow() {
        setVisible(false);
        dispose();
    }

    /**
     * Refreshes the statistical data shown.
     */
    private void refresh() {
        setTitle("Statistics (at cycle " + parent.getSimModel().getCurCycle() +
            ")");
        stats.refresh();
    }

    /**
     * Sets the current view mode.
     *
     * @param desc One of the constants in<code>viewDescs[]</code>.
     */
    protected void setView(String desc) {
        modeCM.setEnabled(true);

        if (view != null) {
            remove(view);
            stats.deleteObserver(view);
        }

        if (desc.equals("Summary")) {
            view = new StatsSummaryView(this, stats);
            modeCM.setEnabled(false);
        } else if (desc.equals("Table")) {
            view = new StatsTableView(this, stats);
        } else if (desc.equals("Graphical")) {
            view = new StatsBarView(this, stats);
        }

        add(view);
        stats.addObserver(view);
        doLayout();
        sbHorizontal.setValue(0);
        sbVertical.setValue(0);
        view.requestFocus();
    }

    /**
     * Exports data to a CSV file.
     */
    protected void exportData() {
        FileDialog diag = new FileDialog(parent, "Export...", FileDialog.SAVE);
        diag.setFile("export - statistics " + stats.getSimName() + ".dat");
        diag.show();

        String filename;

        if ((filename = diag.getFile()) == null) {
            return;
        }

        filename = diag.getDirectory() + filename;

        try {
            stats.saveData(filename);
        } catch (IOException exc) {
            parent.showError("Couldn't export data to \"" + filename + "\"!");
        }
    }

    /*============================================*/
    /* Menubar                                    */
    /*============================================*/
    /**
     * Creates the <code>MenuBar</code> to be used.
     *
     * @return DOCUMENT ME!
     */
    public MenuBar makeMenuBar() {
        MenuBar bar = new MenuBar();
        Menu menu;
        MenuItem item;

        menu = new Menu("File");
        bar.add(menu);

        MenuListener ml = new MenuListener();

        item = new MenuItem("Export...");
        menu.add(item);
        item.addActionListener(ml);

        menu.add(new MenuItem("-"));

        item = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
        menu.add(item);
        item.addActionListener(ml);

        menu = new Menu("Options");
        bar.add(menu);

        item = new MenuItem("Refresh", new MenuShortcut(KeyEvent.VK_R));
        menu.add(item);
        item.addActionListener(ml);

        menu.add(new MenuItem("-"));

        viewCM = new CheckMenu("View", viewDescs, false);
        viewCM.addItemListener(ml);
        viewCM.select(0);
        menu.add(viewCM);

        String[] modes = {
                "all roadusers", "last " + Node.STAT_NUM_DATA + " roadusers"
            };
        modeCM = new CheckMenu("Show average of", modes, false);
        modeCM.addItemListener(ml);
        modeCM.select(0);
        menu.add(modeCM);

        return bar;
    }

    /*============================================*/
    /* Listeners                                  */
    /*============================================*/
    /**
     * Listens to the menus.
     */
    public class MenuListener implements ActionListener, ItemListener {
        /**
         * Handles the <code>ActionEvent</code> action.
         *
         * @param e The ActionEvent that has occured.
         */
        public void actionPerformed(ActionEvent e) {
            String sel = ((MenuItem) e.getSource()).getLabel();

            if (sel.equals("Export...")) {
                exportData();
            } else if (sel.equals("Close")) {
                closeWindow();
            } else if (sel.equals("Refresh")) {
                refresh();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void itemStateChanged(ItemEvent e) {
            CheckMenu menu = (CheckMenu) e.getItemSelectable();

            if (menu == modeCM) {
                stats.setAllTimeAvg(menu.getSelectedIndex() == 0);
            } else {
                setView(menu.getSelectedItem().getLabel());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected class ScrollListener implements AdjustmentListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void adjustmentValueChanged(AdjustmentEvent e) {
            if (e.getSource() == sbHorizontal) {
                view.setHorScroll(e.getValue());
            } else {
                view.setVerScroll(e.getValue());
            }
        }
    }
}
