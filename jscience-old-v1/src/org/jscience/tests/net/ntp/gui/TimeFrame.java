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

import org.jscience.net.ntp.LocalTimeManager;
import org.jscience.net.ntp.NtpConnection;
import org.jscience.net.ntp.TimeManager;

import java.awt.*;
import java.awt.event.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.InetAddress;

import java.util.Enumeration;
import java.util.Properties;

import javax.swing.*;
import javax.swing.SwingWorker;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class TimeFrame extends JWindow implements ActionListener,
    PropertiesListener {
    /** DOCUMENT ME! */
    private static Properties ntpDefaultProperties = new Properties();

    static {
        initDefaultProperties();
    }

    /** DOCUMENT ME! */
    private NtpConnection ntpConnection;

    /** DOCUMENT ME! */
    private LocalTimeManager localTimeManager;

    /** DOCUMENT ME! */
    private JPopupMenu properties;

    /** DOCUMENT ME! */
    private TimeField timeField;

    /** DOCUMENT ME! */
    private JMenuItem setByNtp;

    /** DOCUMENT ME! */
    private JMenuItem setSystemDate;

    /** DOCUMENT ME! */
    private JMenuItem configure;

    /** DOCUMENT ME! */
    private JMenuItem exit;

    /** DOCUMENT ME! */
    private Properties ntpProperties = new Properties();

    /** DOCUMENT ME! */
    private ConfigureDialog configureDialog;

    /** DOCUMENT ME! */
    private String propertiesFile = System.getProperty("user.home") +
        System.getProperty("file.separator") + "Ntp.properties";

    /** DOCUMENT ME! */
    private String ntpServer;

    /** DOCUMENT ME! */
    private TimeManager timeManager;

    /** DOCUMENT ME! */
    private TimeFrame thisReference = this;

    /** DOCUMENT ME! */
    private boolean setByNtpIsRunning = false;

    /** DOCUMENT ME! */
    private boolean setSystemDateIsRunning = false;

/**
     * Creates a new TimeFrame object.
     *
     * @throws Exception DOCUMENT ME!
     */
    public TimeFrame() throws Exception {
        loadProperties();
        timeField = new TimeField();
        configureDialog = new ConfigureDialog(this);

        try {
            timeManager = TimeManager.getInstance();
        } catch (Exception e) {
            //      System.out.println(e.getMessage());
            //      e.printStackTrace();
            timeManager = null;
        }

        applyProperties();

        Container contentPane = getContentPane();
        contentPane.add(timeField);

        WindowListener l = new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    dispose();
                    System.exit(0);
                }
            };

        addWindowListener(l);
        properties = new JPopupMenu("Properties");
        setByNtp = new JMenuItem("Set by Ntp");
        setByNtp.addActionListener(this);
        setSystemDate = new JMenuItem("Set system date");
        setSystemDate.addActionListener(this);

        if (timeManager == null) {
            setSystemDate.setEnabled(false);
        }

        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        configure = new JMenuItem("Configure");
        configure.addActionListener(this);
        properties.add(setByNtp);
        properties.add(setSystemDate);
        properties.add(new JPopupMenu.Separator());
        properties.add(configure);
        properties.add(new JPopupMenu.Separator());
        properties.add(exit);

        MouseListener m = new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    maybePopup(e);
                }

                public void mouseReleased(MouseEvent e) {
                    maybePopup(e);
                }
            };

        addMouseListener(m);
        pack();
        upperRight();
    }

    /**
     * DOCUMENT ME!
     */
    private static void initDefaultProperties() {
        ntpDefaultProperties.put("offset", "0");
        ntpDefaultProperties.put("server", "ntp.skynet.be");
        ntpDefaultProperties.put("datefont", "Dialog");
        ntpDefaultProperties.put("datefontsize", "11");
        ntpDefaultProperties.put("timefont", "Monospaced");
        ntpDefaultProperties.put("timefontsize", "24");
        ntpDefaultProperties.put("alarmtimehour", "12");
        ntpDefaultProperties.put("alarmtimemin", "0");
        ntpDefaultProperties.put("alarmtimesec", "0");
        ntpDefaultProperties.put("alarmon", "false");
        ntpDefaultProperties.put("alarmmessage", "This is the alarm!");
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    private void maybePopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            int x = e.getX();
            int y = e.getY();
            Component c = e.getComponent();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension propertiesSize = properties.getSize();
            Point componentLocation = c.getLocation();

            if (propertiesSize.width == 0) {
                propertiesSize = properties.getPreferredSize();
            }

            if ((componentLocation.x + x + propertiesSize.width) > screenSize.width) {
                x = screenSize.width - propertiesSize.width -
                    componentLocation.x;
            }

            properties.show(e.getComponent(), x, y);
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void loadProperties() {
        try {
            FileInputStream f = new FileInputStream(propertiesFile);
            ntpProperties.load(f);
            f.close();
        } catch (Exception e) {
            //      System.out.println(e.getMessage());
            //      e.printStackTrace();
        }

        Enumeration e = ntpDefaultProperties.keys();

        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();

            if (ntpProperties.getProperty(s) == null) {
                ntpProperties.put(s, ntpDefaultProperties.getProperty(s));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    protected void saveProperties() throws IOException {
        FileOutputStream f = new FileOutputStream(propertiesFile);
        ntpProperties.save(f, "Properties file");
        f.close();
    }

    /**
     * DOCUMENT ME!
     */
    public void applyProperties() {
        try {
            saveProperties();

            long offset;
            offset = Long.parseLong(ntpProperties.getProperty("offset"));
            timeField.setOffset(offset);

            int alarmTimeHour = Integer.parseInt(ntpProperties.getProperty(
                        "alarmtimehour"));
            int alarmTimeMin = Integer.parseInt(ntpProperties.getProperty(
                        "alarmtimemin"));
            int alarmTimeSec = Integer.parseInt(ntpProperties.getProperty(
                        "alarmtimesec"));
            timeField.setAlarmTime(new AlarmTime(alarmTimeHour, alarmTimeMin,
                    alarmTimeSec));

            String alarmMessage = ntpProperties.getProperty("alarmmessage");
            timeField.setAlarmMessage(alarmMessage);

            String alarmOn = ntpProperties.getProperty("alarmon");

            if (alarmOn.equals("true")) {
                timeField.setAlarmOn(true);
            } else {
                timeField.setAlarmOn(false);
            }

            String dateFontName = ntpProperties.getProperty("datefont");
            int dateFontSize = Integer.parseInt(ntpProperties.getProperty(
                        "datefontsize"));
            String timeFontName = ntpProperties.getProperty("timefont");
            int timeFontSize = Integer.parseInt(ntpProperties.getProperty(
                        "timefontsize"));
            timeField.setDateFont(new Font(dateFontName, Font.PLAIN,
                    dateFontSize));
            timeField.setTimeFont(new Font(timeFontName, Font.BOLD, timeFontSize));
            timeField.invalidate();
            pack();
            upperRight();
            System.out.println("Offset : " + offset);
            ntpServer = ntpProperties.getProperty("server");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Properties getProperties() {
        return ntpProperties;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Properties getDefaultProperties() {
        return ntpDefaultProperties;
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     */
    public void setOffset(long offset) {
        timeField.setOffset(offset);
        ntpProperties.put("offset", "" + offset);
        System.out.println("Offset : " + offset);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ae DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == setByNtp) {
            new SetByNtpWorker();
        }

        if (source == setSystemDate) {
            new SetSystemDateWorker();
        }

        if (source == configure) {
            configureDialog.setVisible(true);
        }

        if (source == exit) {
            try {
                saveProperties();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

            dispose();
            System.exit(0);
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void upperRight() {
        Dimension d = getSize();

        if (d.width == 0) {
            d = getPreferredSize();
        }

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dd = toolkit.getScreenSize();
        setLocation(dd.width - d.width, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.4 $
     */
    class SetByNtpWorker extends SwingWorker {
        /** DOCUMENT ME! */
        private boolean errorOccurred;

        /** DOCUMENT ME! */
        private long offset;

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object construct() {
            boolean doSomething;

            synchronized (thisReference) {
                if (setByNtpIsRunning) {
                    doSomething = false;
                } else {
                    doSomething = true;
                    setByNtpIsRunning = true;
                }
            }

            errorOccurred = false;

            if (doSomething) {
                try {
                    ntpConnection = new NtpConnection(InetAddress.getByName(
                                ntpServer));
                    offset = ntpConnection.getInfo().offset;
                    ntpConnection.close();
                    errorOccurred = false;
                } catch (Exception e) {
                    errorOccurred = true;
                }

                setByNtpIsRunning = false;
            }

            return null;
        }

        /**
         * DOCUMENT ME!
         */
        public void finished() {
            if (errorOccurred) {
                JOptionPane.showMessageDialog(thisReference,
                    "There is a server problem", "", JOptionPane.ERROR_MESSAGE);
            } else {
                setOffset(offset);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.4 $
     */
    class SetSystemDateWorker extends SwingWorker {
        /** DOCUMENT ME! */
        private boolean errorOccurred;

        /** DOCUMENT ME! */
        private long offset;

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object construct() {
            boolean doSomething;

            synchronized (thisReference) {
                if (setSystemDateIsRunning) {
                    doSomething = false;
                } else {
                    doSomething = true;
                    setSystemDateIsRunning = true;
                }
            }

            errorOccurred = false;

            if (doSomething) {
                try {
                    ntpConnection = new NtpConnection(InetAddress.getByName(
                                ntpServer));
                    offset = ntpConnection.getInfo().offset;
                    ntpConnection.close();
                    errorOccurred = false;
                } catch (Exception e) {
                    errorOccurred = true;
                }

                setSystemDateIsRunning = false;
            }

            return null;
        }

        /**
         * DOCUMENT ME!
         */
        public void finished() {
            if (errorOccurred) {
                JOptionPane.showMessageDialog(thisReference,
                    "Setting Date Unsuccesful", "", JOptionPane.ERROR_MESSAGE);
            } else {
                timeManager.setTime(offset);
                timeField.timerReset();
                new SetByNtpWorker();
            }
        }
    }
}
