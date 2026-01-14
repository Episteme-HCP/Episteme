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

package org.jscience.tests.net.ntp.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Properties;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class ConfigureDialog extends JDialog implements ActionListener {
    /** DOCUMENT ME! */
    private Properties ntpProperties;

    /** DOCUMENT ME! */
    private JButton ok = new JButton("   Ok   ");

    /** DOCUMENT ME! */
    private JButton cancel = new JButton("Cancel");

    /** DOCUMENT ME! */
    private JButton apply = new JButton(" Apply ");

    /** DOCUMENT ME! */
    private JButton _default = new JButton("Default");

    /** DOCUMENT ME! */
    private ServerChooser serverChooser;

    /** DOCUMENT ME! */
    private AlarmChooser alarmChooser;

    /** DOCUMENT ME! */
    private FontChooser dateChooser;

    /** DOCUMENT ME! */
    private FontChooser timeChooser;

    /** DOCUMENT ME! */
    private PropertiesListener propertiesListener;

/**
     * Creates a new ConfigureDialog object.
     *
     * @param propertiesListener DOCUMENT ME!
     */
    protected ConfigureDialog(PropertiesListener propertiesListener) {
        this.propertiesListener = propertiesListener;
        ntpProperties = propertiesListener.getProperties();
        serverChooser = new ServerChooser(propertiesListener);
        alarmChooser = new AlarmChooser();
        dateChooser = new FontChooser("Date Font");
        timeChooser = new FontChooser("Time Font");

        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(dateChooser);
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPane.add(timeChooser);
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPane.add(alarmChooser);
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPane.add(serverChooser);
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.X_AXIS));
        b.add(_default);
        b.add(Box.createGlue());
        b.add(ok);
        b.add(cancel);
        b.add(apply);
        contentPane.add(b);
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pack();
        center();
        ok.addActionListener(this);
        cancel.addActionListener(this);
        apply.addActionListener(this);
        _default.addActionListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param visible DOCUMENT ME!
     */
    public void setVisible(boolean visible) {
        if (visible) {
            propertiesToWidgets(ntpProperties);
        }

        super.setVisible(visible);
    }

    /**
     * DOCUMENT ME!
     *
     * @param properties DOCUMENT ME!
     */
    private void propertiesToWidgets(Properties properties) {
        serverChooser.setServer(properties.getProperty("server"));
        dateChooser.setFontName(properties.getProperty("datefont"));
        dateChooser.setFontSize(Integer.parseInt(properties.getProperty(
                    "datefontsize")));
        timeChooser.setFontName(properties.getProperty("timefont"));
        timeChooser.setFontSize(Integer.parseInt(properties.getProperty(
                    "timefontsize")));
        alarmChooser.setAlarmTimeHour(Integer.parseInt(properties.getProperty(
                    "alarmtimehour")));
        alarmChooser.setAlarmTimeMin(Integer.parseInt(properties.getProperty(
                    "alarmtimemin")));
        alarmChooser.setAlarmTimeSec(Integer.parseInt(properties.getProperty(
                    "alarmtimesec")));
        alarmChooser.setAlarmMessage(properties.getProperty("alarmmessage"));

        if (properties.getProperty("alarmon").equals("true")) {
            alarmChooser.setAlarmOn(true);
        } else {
            alarmChooser.setAlarmOn(false);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void widgetsToProperties() {
        ntpProperties.put("server", serverChooser.getServer());
        ntpProperties.put("server", serverChooser.getServer());
        ntpProperties.put("datefont", dateChooser.getFontName());
        ntpProperties.put("timefont", timeChooser.getFontName());
        ntpProperties.put("datefontsize", "" + dateChooser.getFontSize());
        ntpProperties.put("timefontsize", "" + timeChooser.getFontSize());
        ntpProperties.put("alarmtimehour", "" +
            alarmChooser.getAlarmTimeHour());
        ntpProperties.put("alarmtimemin", "" + alarmChooser.getAlarmTimeMin());
        ntpProperties.put("alarmtimesec", "" + alarmChooser.getAlarmTimeSec());

        if (alarmChooser.isAlarmOn()) {
            ntpProperties.put("alarmon", "true");
        } else {
            ntpProperties.put("alarmon", "false");
        }

        ntpProperties.put("alarmmessage", "" + alarmChooser.getAlarmMessage());
    }

    /**
     * DOCUMENT ME!
     */
    protected void center() {
        Dimension d = getSize();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dd = toolkit.getScreenSize();
        setLocation((dd.width - d.width) / 2, (dd.height - d.height) / 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ae DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == ok) {
            setVisible(false);
            widgetsToProperties();
            propertiesListener.applyProperties();
        }

        if (source == apply) {
            widgetsToProperties();
            propertiesListener.applyProperties();
        }

        if (source == cancel) {
            setVisible(false);
        }

        if (source == _default) {
            propertiesToWidgets(propertiesListener.getDefaultProperties());
        }
    }
}
