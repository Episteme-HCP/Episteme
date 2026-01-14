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

import org.jscience.architecture.traffic.ErrorDialog;
import org.jscience.architecture.traffic.simulation.SimController;
import org.jscience.architecture.traffic.simulation.SimModel;

import java.awt.*;
import java.awt.event.*;

import java.io.IOException;


/**
 * The controller for the tracking window, it controls a TrackingView.
 *
 * @author Group GUI
 * @version 1.0
 */
public class TrackingController extends Frame {
    /** DOCUMENT ME! */
    private SimModel model;

    /** DOCUMENT ME! */
    private SimController controller;

    /** DOCUMENT ME! */
    private TrackingView view;

/**
     * Creates a <code>TrackingController</code>.
     *
     * @param _model      The <code>SimModel</code> statistics should be read from.
     * @param _controller The parent <code>SimController</code>.
     * @param _view       The <code>TrackingView</code> to be shown.
     */
    public TrackingController(SimModel _model, SimController _controller,
        TrackingView _view) {
        model = _model;
        controller = _controller;
        view = _view;

        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    closeWindow();
                }
            });
        setBounds(200, 200, 400, 300);
        setBackground(Color.lightGray);
        setTitle("Tracking - " + view.getDescription());
        setVisible(true);

        add(view);
        model.addObserver(view);
        setViewEnabled(true);

        setMenuBar(makeMenuBar());
    }

    /**
     * Closes the <code>TrackingController</code>.
     */
    public void closeWindow() {
        setVisible(false);
        model.deleteObserver(view);
        dispose();
    }

    /**
     * Enables or disables the view.
     *
     * @param enable DOCUMENT ME!
     */
    public void setViewEnabled(boolean enable) {
        view.setVisible(enable);
        view.redraw();
    }

    /**
     * Returns true if the view is enabled.
     *
     * @return DOCUMENT ME!
     */
    public boolean isViewEnabled() {
        return view.isVisible();
    }

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

        MenuListener ml = new MenuListener(this);

        item = new MenuItem("Export...", new MenuShortcut(KeyEvent.VK_S));
        menu.add(item);
        item.addActionListener(ml);

        menu.add(new MenuItem("-"));

        item = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
        menu.add(item);
        item.addActionListener(ml);

        /*  Options */
        menu = new Menu("Options");
        bar.add(menu);

        CheckboxMenuItem citem = new CheckboxMenuItem("Toggle view", true);
        menu.add(citem);
        citem.addItemListener(ml);

        addToOptionsMenu(menu);

        return bar;
    }

    /**
     * DOCUMENT ME!
     *
     * @param menu DOCUMENT ME!
     */
    protected void addToOptionsMenu(Menu menu) {
    }

    /**
     * Shows an error dialog.
     *
     * @param msg The message to be shown.
     */
    public void showError(String msg) {
        new ErrorDialog(this, msg);
    }

    /**
     * Resets the view.
     */
    public void reset() {
        view.reset();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TrackingView getTrackingView() {
        return view;
    }

    /**
     * Listens to the menus.
     */
    protected class MenuListener implements ActionListener, ItemListener {
        /** DOCUMENT ME! */
        TrackingController controller;

/**
         * Creates a new MenuListener object.
         *
         * @param _controller DOCUMENT ME!
         */
        public MenuListener(TrackingController _controller) {
            controller = _controller;
        }

        /**
         * Handles the <code>ActionEvent</code> action.
         *
         * @param e The ActionEvent that has occured.
         */
        public void actionPerformed(ActionEvent e) {
            String sel = ((MenuItem) e.getSource()).getLabel();

            if (sel.equals("Close")) {
                closeWindow();
            } else if (sel.equals("Export...")) {
                FileDialog diag = new FileDialog(controller, "Export...",
                        FileDialog.SAVE);
                diag.setFile("export - " + view.getDescription() + ".dat");
                diag.show();

                String filename;

                if ((filename = diag.getFile()) == null) {
                    return;
                }

                filename = diag.getDirectory() + filename;

                try {
                    view.saveData(filename, model);
                } catch (IOException exc) {
                    controller.showError("Couldn't export data to \"" +
                        filename + "\"!");
                }
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void itemStateChanged(ItemEvent e) {
            CheckboxMenuItem item = (CheckboxMenuItem) e.getItemSelectable();
            setViewEnabled(item.getState());
        }
    }
}
