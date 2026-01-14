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
import org.jscience.architecture.traffic.infrastructure.RoaduserFactory;
import org.jscience.architecture.traffic.simulation.SimController;
import org.jscience.architecture.traffic.simulation.SimModel;
import org.jscience.architecture.traffic.util.ArrayUtils;
import org.jscience.architecture.traffic.util.CheckMenu;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * The extended controller for the tracking window, it controls a
 * TrackingView. Offers functionality to choose between allTime average and
 * last 1000 roadusers and lets you choose which roaduser types the data are
 * drawn of.
 *
 * @author Group GUI
 * @version 1.0
 */
public class ExtendedTrackingController extends TrackingController {
    /** DOCUMENT ME! */
    ExtendedTrackingView extView;

    /** DOCUMENT ME! */
    CheckMenu modeCM;

    /** DOCUMENT ME! */
    CheckMenu ruTypeCM;

/**
     * Creates a <code>ExtendedTrackingController</code>.
     *
     * @param _model      The <code>SimModel</code> statistics should be read from.
     * @param _controller The parent <code>SimController</code>.
     * @param _extView    The <code>ExtendedTrackingView</code> to be shown.
     */
    public ExtendedTrackingController(SimModel _model,
        SimController _controller, ExtendedTrackingView _extView) {
        super(_model, _controller, _extView);
        extView = _extView;

        if (!extView.useModes()) {
            modeCM.setEnabled(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param menu DOCUMENT ME!
     */
    protected void addToOptionsMenu(Menu menu) {
        Menu submenu;
        MenuItem item;

        menu.add(new MenuItem("-"));

        ETCMenuListener ml = new ETCMenuListener();

        String[] modes = {
                "all roadusers", "last " + Node.STAT_NUM_DATA + " roadusers"
            };
        modeCM = new CheckMenu("Track average of", modes, false);
        modeCM.addItemListener(ml);
        modeCM.select(0);
        menu.add(modeCM);

        String[] ruTypes = { "All roadusers" };
        ruTypes = (String[]) ArrayUtils.addArray(ruTypes,
                RoaduserFactory.getConcreteTypeDescs());
        ruTypeCM = new CheckMenu("Roaduser type", ruTypes, true);
        ruTypeCM.addItemListener(ml);
        ruTypeCM.select(0);
        menu.add(ruTypeCM);
    }

    /**
     * Listens to the extra menu items.
     */
    public class ETCMenuListener implements ItemListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void itemStateChanged(ItemEvent e) {
            CheckMenu menu = (CheckMenu) e.getItemSelectable();

            if (menu == modeCM) {
                extView.setAllTime(modeCM.getSelectedIndex() == 0);
            } else {
                CheckboxMenuItem[] citems = ruTypeCM.getItems();

                for (int i = 0; i < citems.length; i++) {
                    int statIndex = RoaduserFactory.getStatIndexByDesc(citems[i].getLabel());
                    extView.showGraph(statIndex, citems[i].getState());
                }
            }
        }
    }
}
