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

package org.jscience.architecture.traffic.tools;

import org.jscience.architecture.traffic.Controller;
import org.jscience.architecture.traffic.TrafficException;
import org.jscience.architecture.traffic.View;
import org.jscience.architecture.traffic.infrastructure.EdgeNode;
import org.jscience.architecture.traffic.infrastructure.Node;
import org.jscience.architecture.traffic.infrastructure.RoaduserFactory;
import org.jscience.architecture.traffic.infrastructure.SpawnFrequency;
import org.jscience.architecture.traffic.simulation.SimController;
import org.jscience.architecture.traffic.simulation.SimModel;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * Tool to set spawning and destination frequencies. A little HOW-TO: -
 * Open infrastructure in GLDsim. - Select 'Edge configuration' tool. - Click
 * on an EdgeNode to select it. - Right-click anywhere on the view to
 * deselect. - Select a roaduser type from the dropdown box in the toolbar if
 * you like. - To change spawning frequency (for currently selected roaduser
 * type): - Left-click on currently selected EdgeNode (with the magenta
 * rectangle around it). - Hold the mousebutton and drag left to decrease,
 * right to increase the spawning frequency. - To change destination
 * frequencies (for currently selected roaduser type): - When an EdgeNode is
 * selected, you can change the chance for each other EdgeNode that     a
 * newly spawned Roaduser will go there. - Left-click on the EdgeNode you want
 * to change the frequency for. - Hold the mousebutton and drag left to
 * decrease, right to increase the destination frequency. // This really works
 * a lot easier than any dialog I could think of. :) //
 *
 * @author Group GUI
 * @version 1.0
 */
public class EdgeNodeTool implements Tool {
    /** DOCUMENT ME! */
    RoaduserPanel roaduserPanel;

    /** DOCUMENT ME! */
    Choice roaduserChoice;

    /** DOCUMENT ME! */
    EdgeNode selected = null;

    /** DOCUMENT ME! */
    EdgeNode modify = null;

    /** DOCUMENT ME! */
    Point lastPoint;

    /** DOCUMENT ME! */
    EdgeNode[] edges;

    /** DOCUMENT ME! */
    Controller controller;

    /** DOCUMENT ME! */
    View view;

    /** DOCUMENT ME! */
    int curRuType;

/**
     * Creates a new EdgeNodeTool.
     *
     * @param c The SimController this tool is used in.
     */
    public EdgeNodeTool(Controller c) {
        controller = c;
        view = controller.getView();
        edges = controller.getModel().getInfrastructure().getEdgeNodes_();
        roaduserPanel = new RoaduserPanel();
    }

    /**
     * Sets the current set of EdgeNodes.
     *
     * @param _edges DOCUMENT ME!
     */
    public void setEdgeNodes(EdgeNode[] _edges) {
        edges = _edges;
        selected = null;
        modify = null;
    }

    /**
     * Sets the current roaduser type.
     *
     * @param sel Description of the roaduser type to set as current.
     */
    protected void setRuType(String sel) {
        curRuType = RoaduserFactory.getTypeByDesc(sel);
        selected = null;
        modify = null;
        view.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param mask DOCUMENT ME!
     */
    public void mousePressed(View view, Point p, Tool.Mask mask) {
        if (selected != null) { // EdgeNode selected

            if (mask.isRight()) { // right-click
                selected = null; // deselect
                modify = null;
            } else if (mask.isLeft()) // left-click
             {
                lastPoint = p;
                modify = findEdgeNode(p); // modify frequency for 'modify'
            }

            view.repaint();

            return;
        }

        if (mask.isRight()) {
            Node node = findNode(p);

            if (node != null) {
                ((SimController) controller).getSimModel().getTLController()
                 .trackNode(node.getId());
            }
        }

        if (!mask.isLeft()) {
            return;
        }

        selected = findEdgeNode(p); // select a new EdgeNode
        view.repaint();
    }

    /**
     * Returns the EdgeNode at given Point.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected EdgeNode findEdgeNode(Point p) {
        EdgeNode best = null;
        int dist = Integer.MAX_VALUE;

        // Find edgenode that was clicked.
        for (int i = 0; i < edges.length; i++) {
            if (edges[i].getBounds().contains(p) &&
                    (edges[i].getDistance(p) < dist)) {
                best = edges[i];
                dist = edges[i].getDistance(p);
            }
        }

        return best;
    }

    /**
     * Returns the EdgeNode at given Point.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Node findNode(Point p) {
        Node best = null;
        int dist = Integer.MAX_VALUE;

        Node[] allNodes = controller.getModel().getInfrastructure().getAllNodes();
        int num_nodes = allNodes.length;

        // Find edgenode that was clicked.
        for (int i = 0; i < num_nodes; i++) {
            if (allNodes[i].getBounds().contains(p) &&
                    (allNodes[i].getDistance(p) < dist)) {
                best = allNodes[i];
                dist = allNodes[i].getDistance(p);
            }
        }

        return best;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param mask DOCUMENT ME!
     */
    public void mouseReleased(View view, Point p, Tool.Mask mask) {
        if ((modify != null) && (modify == selected)) // currently dragging
         {
            SimModel sm = (SimModel) controller.getModel();
            float freqChange = (p.x - lastPoint.x) / (float) 250.0;
            float spawn = modify.getSpawnFrequency(curRuType);
            spawn += freqChange;

            if (spawn > 1) {
                spawn = (float) 1.0;
            }

            if (spawn < 0) {
                spawn = (float) 0.0;
            }

            sm.setSpawnFrequency(modify, curRuType, spawn);
        }

        modify = null; // no longer modifying a frequency

        return;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param mask DOCUMENT ME!
     */
    public void mouseMoved(View view, Point p, Tool.Mask mask) {
        if (modify != null) // currently dragging
         {
            SimModel sm = (SimModel) controller.getModel();
            float freqChange = (p.x - lastPoint.x) / (float) 250.0;

            if (modify == selected) // modifying spawn frequency
             {
                float spawn = modify.getSpawnFrequency(curRuType);
                spawn += freqChange;

                if (spawn > 1) {
                    spawn = (float) 1.0;
                }

                if (spawn < 0) {
                    spawn = (float) 0.0;
                }

                modify.setSpawnFrequency(curRuType, spawn);
            } else // modifying destination frequency
             {
                float dest = selected.getDestFrequency(modify.getId(), curRuType);
                dest += freqChange;

                if (dest > 1) {
                    dest = (float) 1.0;
                }

                if (dest < 0) {
                    dest = (float) 0.0;
                }

                selected.setDestFrequency(modify.getId(), curRuType, dest);
            }

            lastPoint = p;
            view.repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int overlayType() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     *
     * @throws TrafficException DOCUMENT ME!
     */
    public void paint(Graphics g) throws TrafficException {
        if (selected == null) {
            return;
        }

        Rectangle r = selected.getBounds();
        r.grow(3, 3);
        g.setPaintMode();
        g.setColor(Color.magenta);
        g.drawRect(r.x, r.y, r.width, r.height);

        EdgeNode curEdge;

        for (int i = 0; i < edges.length; i++) {
            curEdge = edges[i];
            r = curEdge.getBounds();

            if (curEdge != selected) {
                String dest = "" +
                    selected.getDestFrequency(curEdge.getId(), curRuType);

                if (dest.length() > 5) {
                    dest = dest.substring(0, 5);
                }

                g.drawString(dest, r.x, r.y - 8);
            } else {
                String spawn = "" + selected.getSpawnFrequency(curRuType);

                if (spawn.length() > 5) {
                    spawn = spawn.substring(0, 5);
                }

                g.drawString(spawn, r.x, r.y - 8);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Panel getPanel() {
        return roaduserPanel;
    }

    /**
     * Panel containing a dropdown box for all concrete Roaduser types.
     */
    protected class RoaduserPanel extends Panel {
/**
         * Creates a new RoaduserPanel object.
         */
        public RoaduserPanel() {
            setLayout(null);

            roaduserChoice = new Choice();

            String[] descs;

            if (selected != null) {
                SpawnFrequency[] freqs = modify.getSpawnFrequencies();
                descs = new String[freqs.length];

                for (int i = 0; i < descs.length; i++)
                    descs[i] = RoaduserFactory.getDescByType(freqs[i].ruType);
            } else {
                descs = RoaduserFactory.getConcreteTypeDescs();
            }

            for (int i = 0; i < descs.length; i++)
                roaduserChoice.add(descs[i]);

            roaduserChoice.addItemListener(new MyListener());
            this.add(roaduserChoice);
            setRuType(descs[0]);

            roaduserChoice.setBounds(0, 0, 100, 24);
            setSize(100, 24);
        }

        /**
         * DOCUMENT ME!
         *
         * @author $author$
         * @version $Revision: 1.3 $
         */
        protected class MyListener implements ItemListener {
            /**
             * DOCUMENT ME!
             *
             * @param e DOCUMENT ME!
             */
            public void itemStateChanged(ItemEvent e) {
                String sel = ((Choice) e.getSource()).getSelectedItem();
                setRuType(sel);
            }
        }
    }
}
