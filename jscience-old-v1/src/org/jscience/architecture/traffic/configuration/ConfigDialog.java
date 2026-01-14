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

import org.jscience.architecture.traffic.*;
import org.jscience.architecture.traffic.infrastructure.Roaduser;
import org.jscience.architecture.traffic.simulation.SimController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.Observable;
import java.util.Observer;


/**
 * DOCUMENT ME!
 *
 * @author Group GUI
 * @version 1.0
 */
public class ConfigDialog extends Frame implements Observer, WindowListener,
    ActionListener {
    /** DOCUMENT ME! */
    protected static final int WIDTH = 400;

    /** DOCUMENT ME! */
    protected static final int HEIGHT = 350;

    /** DOCUMENT ME! */
    protected static final int TITLE_XPOS = 5;

    /** DOCUMENT ME! */
    protected static final int TITLE_YPOS = 25;

    /** DOCUMENT ME! */
    protected static final int TITLE_WIDTH = WIDTH - (TITLE_XPOS * 2);

    /** DOCUMENT ME! */
    protected static final int TITLE_HEIGHT = 25;

    /** DOCUMENT ME! */
    protected static final int CLOSE_WIDTH = 50;

    /** DOCUMENT ME! */
    protected static final int CLOSE_HEIGHT = 24;

    /** DOCUMENT ME! */
    protected static final int CLOSE_XPOS = (int) ((WIDTH / 2) -
        (CLOSE_WIDTH / 2)) - 1;

    /** DOCUMENT ME! */
    protected static final int CLOSE_YPOS = HEIGHT - CLOSE_HEIGHT - 10;

    /** DOCUMENT ME! */
    protected static final int PANEL_XPOS = 10;

    /** DOCUMENT ME! */
    protected static final int PANEL_YPOS = TITLE_YPOS + TITLE_HEIGHT + 10;

    /** DOCUMENT ME! */
    protected static final int PANEL_WIDTH = WIDTH - (PANEL_XPOS * 2);

    /** DOCUMENT ME! */
    protected static final int PANEL_HEIGHT = CLOSE_YPOS - PANEL_YPOS - 10;

    /** DOCUMENT ME! */
    public static boolean AlwaysOnTop = true;

    /** DOCUMENT ME! */
    Controller controller;

    /** DOCUMENT ME! */
    SubPanel subPanel;

    /** DOCUMENT ME! */
    Button close;

    /** DOCUMENT ME! */
    PanelFactory cpf;

    /** DOCUMENT ME! */
    Label title;

/**
     * Creates a new ConfigDialog object.
     *
     * @param con DOCUMENT ME!
     */
    public ConfigDialog(Controller con) {
        super("Configuration dialog");

        controller = con;
        con.getCurrentSelection().addObserver(this);
        con.getModel().addObserver(this);

        cpf = new PanelFactory(this,
                (con instanceof SimController) ? PanelFactory.TYPE_SIM
                                               : PanelFactory.TYPE_EDIT);

        setBounds(100, 100, WIDTH, HEIGHT);
        setLayout(null);
        addWindowListener(this);
        setResizable(false);
        setBackground(SystemColor.control);

        title = new Label("", Label.CENTER);
        title.setFont(new Font(null, 0, 16));
        title.setBounds(TITLE_XPOS, TITLE_YPOS, TITLE_WIDTH, TITLE_HEIGHT);
        add(title);

        subPanel = new SubPanel();
        subPanel.setBounds(PANEL_XPOS, PANEL_YPOS, PANEL_WIDTH, PANEL_HEIGHT);
        add(subPanel);

        close = new Button("OK");
        close.setBounds(CLOSE_XPOS, CLOSE_YPOS, CLOSE_WIDTH, CLOSE_HEIGHT);
        close.addActionListener(this);
        add(close);

        subPanel.setConfigPanel(new GeneralPanel(this));
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     * @param arg DOCUMENT ME!
     */
    public void update(Observable o, Object arg) {
        if (o instanceof Model) {
            ConfigPanel cp = subPanel.getConfigPanel();

            if (cp != null) {
                cp.reset();
            }

            return;
        }

        Selection s = (Selection) o;

        try {
            ConfigPanel cp = subPanel.getConfigPanel();
            cp.ok();
            subPanel.setConfigPanel(cpf.createPanel(s));

            if (AlwaysOnTop) {
                toFront();
            }
        } catch (ConfigException e) {
            Controller.reportError(e);
        }
    }

    /**
     * Returns the controller that created this configuration dialog
     *
     * @return DOCUMENT ME!
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Sets the controller that created this configuration dialog
     *
     * @param con DOCUMENT ME!
     */
    public void setController(Controller con) {
        controller = con;
    }

    /**
     * Returns the current configuration panel
     *
     * @return DOCUMENT ME!
     */
    public ConfigPanel getConfigPanel() {
        return subPanel.getConfigPanel();
    }

    /**
     * Sets the current configuration panel
     *
     * @param cp DOCUMENT ME!
     */
    public void setConfigPanel(ConfigPanel cp) {
        subPanel.setConfigPanel(cp);
    }

    /**
     * Returns the title of this dialog
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return title.getText();
    }

    /**
     * Sets the title of this dialog
     *
     * @param newtitle DOCUMENT ME!
     */
    public void setTitle(String newtitle) {
        title.setText(newtitle);
        super.setTitle(newtitle);
    }

    /**
     * Shows a message dialog with an OK button
     *
     * @param msg DOCUMENT ME!
     */
    public void showError(String msg) {
        new ErrorDialog(this, msg);
    }

    /**
     * Changes the current selection to select the given object
     *
     * @param s DOCUMENT ME!
     */
    public void selectObject(Selectable s) {
        controller.getCurrentSelection().newSelection(s);
    }

    /**
     * Show the general panel.
     */
    public void showGeneralPanel() {
        ConfigPanel cp = subPanel.getConfigPanel();
        cp.ok();
        subPanel.setConfigPanel(new GeneralPanel(this));
    }

    /**
     * Shows the road user panel.
     *
     * @param ru DOCUMENT ME!
     */
    public void showRoaduser(Roaduser ru) {
        ConfigPanel cp = subPanel.getConfigPanel();
        cp.ok();
        subPanel.setConfigPanel(new RoaduserPanel(this, ru));
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setVisible(boolean b) {
        if (b == false) {
            subPanel.getConfigPanel().ok();
        }

        super.setVisible(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowClosing(WindowEvent e) {
        hide();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowActivated(WindowEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowClosed(WindowEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowDeactivated(WindowEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowIconified(WindowEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowDeiconified(WindowEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowOpened(WindowEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class SubPanel extends Panel {
        /** DOCUMENT ME! */
        ConfigPanel current;

/**
         * Creates a new SubPanel object.
         */
        public SubPanel() {
            setLayout(null);
        }

/**
         * Creates a new SubPanel object.
         *
         * @param cp DOCUMENT ME!
         */
        public SubPanel(ConfigPanel cp) {
            this();
            setConfigPanel(cp);
        }

        /**
         * DOCUMENT ME!
         *
         * @param cp DOCUMENT ME!
         */
        public void setConfigPanel(ConfigPanel cp) {
            if ((cp != current) && (cp != null)) {
                if (current != null) {
                    remove(current);
                }

                add(cp);
                cp.setBounds(0, 0, getWidth(), getHeight());
                current = cp;
                doLayout();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ConfigPanel getConfigPanel() {
            return current;
        }
    }
}
