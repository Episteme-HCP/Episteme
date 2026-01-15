package org.jscience.devices.telescopes.LX200;

import java.awt.*;
import java.awt.event.*;

import java.io.IOException;

import java.rmi.Naming;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public final class LX200ControlPanel extends Panel implements Runnable {
    /** DOCUMENT ME! */
    private LX200Remote lx200;

    /** DOCUMENT ME! */
    private Thread statusThr = null;

    /** DOCUMENT ME! */
    private TextField raField = new TextField(8);

    /** DOCUMENT ME! */
    private TextField decField = new TextField(8);

    /** DOCUMENT ME! */
    private Button gotoButton = new Button("Goto");

    /** DOCUMENT ME! */
    private Button syncButton = new Button("Sync");

    /** DOCUMENT ME! */
    private Label raLabel = new Label();

    /** DOCUMENT ME! */
    private Label decLabel = new Label();

    /** DOCUMENT ME! */
    private Button inButton = new Button("+");

    /** DOCUMENT ME! */
    private Button outButton = new Button("-");

    /** DOCUMENT ME! */
    private Choice focusRateCombo = new Choice();

    /** DOCUMENT ME! */
    private Button northButton = new Button("N");

    /** DOCUMENT ME! */
    private Button eastButton = new Button("E");

    /** DOCUMENT ME! */
    private Button southButton = new Button("S");

    /** DOCUMENT ME! */
    private Button westButton = new Button("W");

    /** DOCUMENT ME! */
    private Choice slewRateCombo = new Choice();

/**
     * Creates a new LX200ControlPanel object.
     *
     * @param url DOCUMENT ME!
     */
    public LX200ControlPanel(String url) {
        try {
            lx200 = (LX200Remote) Naming.lookup(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gotoButton.addActionListener(new GotoActionListener());
        syncButton.addActionListener(new SyncActionListener());
        inButton.addActionListener(new FocusActionListener(LX200.FOCUS_IN));
        outButton.addActionListener(new FocusActionListener(LX200.FOCUS_OUT));
        focusRateCombo.add("Fast");
        focusRateCombo.add("Slow");
        focusRateCombo.addItemListener(new FocusRateItemListener());
        northButton.addActionListener(new SlewActionListener(LX200.SLEW_NORTH));
        eastButton.addActionListener(new SlewActionListener(LX200.SLEW_EAST));
        southButton.addActionListener(new SlewActionListener(LX200.SLEW_SOUTH));
        westButton.addActionListener(new SlewActionListener(LX200.SLEW_WEST));
        slewRateCombo.add("Slew");
        slewRateCombo.add("Find");
        slewRateCombo.add("Center");
        slewRateCombo.add("Guide");
        slewRateCombo.addItemListener(new SlewRateItemListener());

        Panel radecPanel = new Panel();
        radecPanel.add(raLabel);
        radecPanel.add(decLabel);
        radecPanel.add(raField);
        radecPanel.add(decField);
        radecPanel.add(gotoButton);
        radecPanel.add(syncButton);

        Panel focusPanel = new Panel();
        focusPanel.setLayout(new GridLayout(3, 1));
        focusPanel.add(inButton);
        focusPanel.add(outButton);
        focusPanel.add(focusRateCombo);

        Panel slewPanel = new Panel();
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout gridbag = new GridBagLayout();
        slewPanel.setLayout(gridbag);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gridbag.setConstraints(northButton, gbc);
        slewPanel.add(northButton);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gridbag.setConstraints(westButton, gbc);
        slewPanel.add(westButton);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gridbag.setConstraints(eastButton, gbc);
        slewPanel.add(eastButton);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gridbag.setConstraints(southButton, gbc);
        slewPanel.add(southButton);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(slewRateCombo, gbc);
        slewPanel.add(slewRateCombo);
        add(radecPanel);
        add(focusPanel);
        add(slewPanel);
        statusThr = new Thread(this);
        statusThr.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        if (arg.length != 1) {
            System.out.println(
                "Usage: LX200ControlPanel rmi://<host>[:port]/<remote name>");

            return;
        }

        Frame app = new Frame("LX200 Control Panel");
        app.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    System.exit(0);
                }
            });
        app.add(new LX200ControlPanel(arg[0]));
        app.setSize(100, 400);
        app.setVisible(true);
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        while (statusThr == Thread.currentThread()) {
            try {
                raLabel.setText("RA: " + lx200.getRA());
                decLabel.setText("Dec: " + lx200.getDec());
            } catch (IOException e) {
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class GotoActionListener implements ActionListener {
        /**
         * DOCUMENT ME!
         *
         * @param evt DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent evt) {
            try {
                lx200.setObjectCoords(raField.getText(), decField.getText());
                lx200.slewToObject();
            } catch (IOException e) {
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class SyncActionListener implements ActionListener {
        /**
         * DOCUMENT ME!
         *
         * @param evt DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent evt) {
            try {
                lx200.setObjectCoords(raField.getText(), decField.getText());
                lx200.syncCoords();
            } catch (IOException e) {
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class FocusActionListener implements ActionListener {
        /** DOCUMENT ME! */
        private final int direction;

        /** DOCUMENT ME! */
        private boolean focusing = false;

/**
         * Creates a new FocusActionListener object.
         *
         * @param dir DOCUMENT ME!
         */
        public FocusActionListener(int dir) {
            direction = dir;
        }

        /**
         * DOCUMENT ME!
         *
         * @param evt DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent evt) {
            if (focusing) {
                try {
                    lx200.stopFocus();
                } catch (IOException e) {
                }

                focusing = false;
            } else {
                try {
                    lx200.startFocus(direction);
                } catch (IOException e) {
                }

                focusing = true;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class FocusRateItemListener implements ItemListener {
        /**
         * DOCUMENT ME!
         *
         * @param evt DOCUMENT ME!
         */
        public void itemStateChanged(ItemEvent evt) {
            try {
                lx200.setFocusRate(focusRateCombo.getSelectedIndex() + 1);
            } catch (IOException e) {
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class SlewActionListener implements ActionListener {
        /** DOCUMENT ME! */
        private final int direction;

        /** DOCUMENT ME! */
        private boolean slewing = false;

/**
         * Creates a new SlewActionListener object.
         *
         * @param dir DOCUMENT ME!
         */
        public SlewActionListener(int dir) {
            direction = dir;
        }

        /**
         * DOCUMENT ME!
         *
         * @param evt DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent evt) {
            if (slewing) {
                try {
                    lx200.stopSlew(direction);
                } catch (IOException e) {
                }

                slewing = false;
            } else {
                try {
                    lx200.startSlew(direction);
                } catch (IOException e) {
                }

                slewing = true;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class SlewRateItemListener implements ItemListener {
        /**
         * DOCUMENT ME!
         *
         * @param evt DOCUMENT ME!
         */
        public void itemStateChanged(ItemEvent evt) {
            try {
                lx200.setSlewRate(slewRateCombo.getSelectedIndex() + 1);
            } catch (IOException e) {
            }
        }
    }
}
