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

package org.jscience.biology.lsystems.growing.gui;

import org.jscience.biology.lsystems.common.Log;
import org.jscience.biology.lsystems.growing.Plant;

import java.awt.*;
import java.awt.event.MouseEvent;

import java.util.Enumeration;

import javax.swing.*;
import javax.swing.event.MouseInputListener;


/**
 * This class is the map of the scene on the GUI. It handles all drag and
 * drop for the placement of the plants in the scene.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class MapPanel extends JPanel implements MouseInputListener {
    /** The settings refenrence */
    Settings set;

/**
     * Constructor
     */
    MapPanel() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Stores the passed settings in this instance
     *
     * @param set The settings reference to store.
     */
    public void setSettings(Settings set) {
        this.set = set;
    }

    /**
     * Overrides the paintComponent of JPanel. Draws the lines and dots
     * representing the position of the plants.
     *
     * @param g Will be passed by the caller which is the system.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //paint background

        g.setColor(Color.white);
        g.drawRect(5, 5, getWidth() - 10, getHeight() - 10);
        g.drawRect(30, 40, 45, 30);
        g.drawString("House", 35, 60);
        g.drawLine(5, 70, getWidth() - 5, 90);
        g.drawLine(5, 90, getWidth() - 5, 110);
        g.drawString("Path", getWidth() / 2, 95);

        if ((set != null) && (set.getPlants() != null)) {
            Enumeration enumeration = set.getPlants().elements();

            while (enumeration.hasMoreElements()) {
                Plant plant = (Plant) enumeration.nextElement();

                if (plant.isSelected()) {
                    g.setColor(Color.red);
                } else {
                    g.setColor(Color.white);
                }

                g.fillOval(plant.getX(), plant.getY(), 15, 15);
            }
        }
    }

    /**
     * Overrrides the method in the MouseInputListener interface
     *
     * @param e DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent e) {
        Log.debug("mouse button clicked");
        repaint();
    }

    /**
     * Overrrides the method in the MouseInputListener interface
     *
     * @param e DOCUMENT ME!
     */
    public void mousePressed(MouseEvent e) {
        Log.debug("mouse button pressed");

        //wenn eine pflanze angeklickt ist -> verschieben
        if (set != null) {
            Plant plant;
            Enumeration enumeration = set.getPlants().elements();

            while (enumeration.hasMoreElements()) {
                plant = (Plant) enumeration.nextElement();

                if ((plant.getX() < e.getX()) &&
                        ((plant.getX() + 15) > e.getX()) &&
                        (plant.getY() < e.getY()) &&
                        ((plant.getY() + 15) > e.getY())) {
                    plant.setSelected();
                }

                set.getList().setSelectedValue(Plant.getSelected(), true);
                repaint();
            }
        }
    }

    /**
     * Overrrides the method in the MouseInputListener interface
     *
     * @param e DOCUMENT ME!
     */
    public void mouseReleased(MouseEvent e) {
        Log.debug("mouse released");
    }

    /**
     * Overrrides the method in the MouseInputListener interface
     *
     * @param e DOCUMENT ME!
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Overrrides the method in the MouseInputListener interface
     *
     * @param e DOCUMENT ME!
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Overrrides the method in the MouseInputListener interface. Sets
     * the selected item to the new position of the mouse cursor when its
     * dragged in the MapPanel.
     *
     * @param e DOCUMENT ME!
     */
    public void mouseDragged(MouseEvent e) {
        Plant.getSelected().setX(e.getX() - 8);
        Plant.getSelected().setY(e.getY() - 8);
        repaint();
        set.getList().repaint();
    }

    /**
     * Overrrides the method in the MouseInputListener interface
     *
     * @param e DOCUMENT ME!
     */
    public void mouseMoved(MouseEvent e) {
    }
}
