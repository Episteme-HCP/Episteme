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

package org.jscience.architecture.traffic.configuration;

import org.jscience.architecture.traffic.infrastructure.Roaduser;
import org.jscience.architecture.traffic.util.Hyperlink;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * DOCUMENT ME!
 *
 * @author Group GUI
 * @version 1.0
 */
public class RoaduserPanel extends ConfigPanel implements ActionListener {
    /** DOCUMENT ME! */
    Roaduser ru;

    /** DOCUMENT ME! */
    Hyperlink start;

    /** DOCUMENT ME! */
    Hyperlink dest;

    /** DOCUMENT ME! */
    Label vehicle;

    /** DOCUMENT ME! */
    Label driver;

    /** DOCUMENT ME! */
    Label delay;

    /** DOCUMENT ME! */
    Label length;

    /** DOCUMENT ME! */
    Label speed;

    /** DOCUMENT ME! */
    TextArea description;

    /** DOCUMENT ME! */
    Image picture;

/**
     * Creates a new RoaduserPanel object.
     *
     * @param cd DOCUMENT ME!
     * @param r  DOCUMENT ME!
     */
    public RoaduserPanel(ConfigDialog cd, Roaduser r) {
        super(cd);

        setLayout(null);

        Label lab;

        lab = new Label("Source:");
        lab.setBounds(0, 0, 80, 20);
        add(lab);
        start = new Hyperlink();
        start.addActionListener(this);
        start.setBounds(80, 0, 100, 20);
        add(start);

        lab = new Label("Destination:");
        lab.setBounds(0, 20, 80, 20);
        add(lab);
        dest = new Hyperlink();
        dest.addActionListener(this);
        dest.setBounds(80, 20, 100, 20);
        add(dest);

        lab = new Label("Delay:");
        lab.setBounds(0, 40, 80, 20);
        add(lab);
        delay = new Label();
        delay.setBounds(80, 40, 100, 20);
        add(delay);

        lab = new Label("Length:");
        lab.setBounds(0, 60, 80, 20);
        add(lab);
        length = new Label();
        length.setBounds(80, 60, 100, 20);
        add(length);

        lab = new Label("Speed:");
        lab.setBounds(0, 80, 80, 20);
        add(lab);
        speed = new Label();
        speed.setBounds(80, 80, 100, 20);
        add(speed);

        lab = new Label("Vehicle:");
        lab.setBounds(0, 100, 80, 20);
        add(lab);
        vehicle = new Label();
        vehicle.setBounds(80, 100, 200, 20);
        add(vehicle);

        lab = new Label("Driver:");
        lab.setBounds(0, 120, 80, 20);
        add(lab);
        driver = new Label();
        driver.setBounds(80, 120, 200, 20);
        add(driver);

        lab = new Label("Description:");
        lab.setBounds(0, 140, 80, 20);
        add(lab);

        description = new TextArea("", 1, 1, TextArea.SCROLLBARS_NONE);
        description.setEditable(false);
        description.setBounds(0, 160, 180, 80);
        add(description);

        setRoaduser(r);
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        delay.setText("" + ru.getDelay());
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void setRoaduser(Roaduser r) {
        ru = r;
        confd.setTitle(ru.getName());
        reset();

        start.setText(ru.getStartNode().getName());
        dest.setText(ru.getDestNode().getName());

        length.setText("" + ru.getLength());
        speed.setText("" + ru.getSpeed());

        vehicle.setText(ru.getVehicleName());
        driver.setText(ru.getDriverName());
        description.setText(ru.getDescription());

        String s = ru.getPicture();

        if (s != null) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            picture = tk.getImage(s);
        } else {
            picture = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        super.paint(g);

        if (picture != null) {
            g.drawImage(picture, 200, 0, this);
        } else {
            g.drawString("No picture available", 200, 20);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            confd.selectObject(ru.getStartNode());
        } else {
            confd.selectObject(ru.getDestNode());
        }
    }
}
