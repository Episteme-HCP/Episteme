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

package org.jscience.architecture.traffic.algorithms.tlc;

import org.jscience.architecture.traffic.Controller;
import org.jscience.architecture.traffic.infrastructure.*;
import org.jscience.architecture.traffic.util.ArrayUtils;
import org.jscience.architecture.traffic.xml.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;

import java.util.Dictionary;


/**
 * This is the abstract class for Traffic light algorithms. It is informed
 * about every movement  made by road users. In this way not every road user
 * has to be iterated. By using this information it provides a table
 * containing Q-values(reward values)  for each trafficlight in it's 'Green'
 * setting.
 *
 * @author Group Algorithms
 * @version 1.0
 */
public abstract class TLController implements XMLSerializable, TwoStageLoader {
    /** DOCUMENT ME! */
    protected Infrastructure infra;

    /** DOCUMENT ME! */
    protected TLDecision[][] tld;

    /** DOCUMENT ME! */
    public int trackNode = -1;

    /** DOCUMENT ME! */
    protected int num_tls = 0;

/**
     * The constructor for TL controllers
     */
    TLController() {
    }

/**
     * Creates a new TLController object.
     *
     * @param i DOCUMENT ME!
     */
    TLController(Infrastructure i) {
        setInfrastructure(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Infrastructure getInfrastructure() {
        return infra;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void setInfrastructure(Infrastructure i) {
        tld = createDecisionArray(i);
    }

    /**
     * Calculates how every traffic light should be switched
     *
     * @return DOCUMENT ME!
     *
     * @see gld.algo.tlc.TLDecision
     */
    public abstract TLDecision[][] decideTLs();

    /**
     * Creates a TLDecision[][] for the given infrastructure. All Q
     * values are set to 0
     *
     * @param infra DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TLDecision[][] createDecisionArray(Infrastructure infra) {
        Node[] nodes = infra.getAllNodes();
        int num_nodes = nodes.length;

        Sign[] signs = null;
        int num_signs = 0;
        int counter;

        TLDecision[][] tld = new TLDecision[num_nodes][];
        TLDecision[] dec = null;
        Node node = null;

        for (int i = 0; i < num_nodes; i++) {
            node = nodes[i];
            counter = 0;

            if (node.getType() == Node.JUNCTION) {
                signs = ((Junction) node).getSigns();
            } else {
                signs = new Sign[0];
            }

            num_signs = signs.length;
            dec = new TLDecision[num_signs];

            for (int j = 0; j < num_signs; j++)
                if (signs[j].getType() == Sign.TRAFFICLIGHT) {
                    dec[counter] = new TLDecision((TrafficLight) signs[j], 0);
                    counter++;
                    num_tls++;
                }

            if (counter < num_signs) {
                dec = (TLDecision[]) ArrayUtils.cropArray(dec, counter);
            }

            tld[i] = dec;
        }

        return tld;
    }

    /**
     * Extracts the Gain-values of a decision array for load/save
     *
     * @param array DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected float[][] getGainValuesFromDecisionArray(TLDecision[][] array) {
        float[][] result = new float[array.length][array[0].length];

        for (int t = 0; t < array.length; t++) {
            result[t] = new float[array[t].length];

            for (int u = 0; u < array[t].length; u++)
                result[t][u] = array[t][u].getGain();
        }

        return result;
    }

    /**
     * Apply an array of Gain-values to an array of TLDecisions
     * Assumes that the dimensions of the two arrays are equal.
     *
     * @param array DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    protected void applyGainValues(TLDecision[][] array, float[][] value) {
        for (int t = 0; t < array.length; t++) {
            for (int u = 0; u < array[t].length; u++)
                array[t][u].setGain(value[t][u]);
        }
    }

    /**
     * Resets the Algorithm
     */
    public void reset() {
    }

    /**
     * Sets the Node that can be tracked during excecution of a TLC
     *
     * @param i DOCUMENT ME!
     */
    public void trackNode(int i) {
        trackNode = i;
    }

    /**
     * Returns the number of TrafficLights in this Infrastructure
     *
     * @return DOCUMENT ME!
     */
    public int getNumTLs() {
        return num_tls;
    }

    /**
     * DOCUMENT ME!
     *
     * @param _ru DOCUMENT ME!
     * @param _prevlane DOCUMENT ME!
     * @param _prevsign DOCUMENT ME!
     * @param _prevpos DOCUMENT ME!
     * @param _dlanenow DOCUMENT ME!
     * @param _signnow DOCUMENT ME!
     * @param _posnow DOCUMENT ME!
     * @param posMovs DOCUMENT ME!
     * @param _desiredLane DOCUMENT ME!
     */
    public abstract void updateRoaduserMove(Roaduser _ru, Drivelane _prevlane,
        Sign _prevsign, int _prevpos, Drivelane _dlanenow, Sign _signnow,
        int _posnow, PosMov[] posMovs, Drivelane _desiredLane);

    // XMLSerializable implementation
    /**
     * DOCUMENT ME!
     *
     * @param myElement DOCUMENT ME!
     * @param loader DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLInvalidInputException DOCUMENT ME!
     */
    public void load(XMLElement myElement, XMLLoader loader)
        throws XMLTreeException, IOException, XMLInvalidInputException {
        trackNode = myElement.getAttribute("track-node").getIntValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = new XMLElement("tlc");
        result.addAttribute(new XMLAttribute("track-node", trackNode));

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param saver DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public void saveChilds(XMLSaver saver)
        throws XMLTreeException, IOException, XMLCannotSaveException { // A TLController has no child objects
    }

    /**
     * DOCUMENT ME!
     *
     * @param parentName DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     */
    public void setParentName(String parentName) throws XMLTreeException {
        throw new XMLTreeException(
            "Attempt to change fixed parentName of a TLC class.");
    }

    // Empty TwoStageLoader (standard)
    /**
     * DOCUMENT ME!
     *
     * @param dictionaries DOCUMENT ME!
     *
     * @throws XMLInvalidInputException DOCUMENT ME!
     * @throws XMLTreeException DOCUMENT ME!
     */
    public void loadSecondStage(Dictionary dictionaries)
        throws XMLInvalidInputException, XMLTreeException {
    }

    //////////// TLC settings ///////////
    /**
     * To be overridden by subclasses if TLC settings are to be
     * modified.
     *
     * @param c DOCUMENT ME!
     */
    public void showSettings(Controller c) {
        return;
    }

    /**
     * Shows the TLC settings dialog for the given TLCSettings.
     *
     * @param c DOCUMENT ME!
     * @param settings DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected TLCSettings doSettingsDialog(Controller c, TLCSettings settings) {
        TLCDialog tlcDialog;
        tlcDialog = new TLCDialog(c, settings);
        tlcDialog.show();

        return tlcDialog.getSettings();
    }

    /**
     * Class used in combination with TLCDialog to modify TLC-specific
     * settings.
     *
     * @author Group GUI
     * @version 1.0
     */
    protected class TLCSettings {
        /** DOCUMENT ME! */
        public String[] descriptions;

        /** DOCUMENT ME! */
        public int[] ints;

        /** DOCUMENT ME! */
        public float[] floats;

/**
         * Creates a new TLCSettings object.
         *
         * @param _descriptions DOCUMENT ME!
         * @param _ints         DOCUMENT ME!
         * @param _floats       DOCUMENT ME!
         */
        public TLCSettings(String[] _descriptions, int[] _ints, float[] _floats) {
            descriptions = _descriptions;
            ints = _ints;
            floats = _floats;
        }
    }

    /**
     * The dialog used to set <code>TLController</code> properties.
     *
     * @author Group GUI
     * @version 1.0
     */
    protected class TLCDialog extends Dialog {
        /** DOCUMENT ME! */
        TextField[] texts;

        /** DOCUMENT ME! */
        TLCSettings settings;

/**
         * Creates a <code>TLCDialog</code>.
         *
         * @param c         DOCUMENT ME!
         * @param _settings DOCUMENT ME!
         */
        public TLCDialog(Controller c, TLCSettings _settings) {
            super(c, "TLC properties...", true);
            settings = _settings;

            setResizable(false);
            setSize(500, 250);
            addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        hide();
                    }
                });
            setLayout(new BorderLayout());

            ActionListener al = new TLCActionListener();
            this.add(new TLCPropPanel(), BorderLayout.CENTER);
            this.add(new OkCancelPanel(al), BorderLayout.SOUTH);
        }

        /*============================================*/
        /* GET                                        */
        /*============================================*/
        public TLCSettings getSettings() {
            return settings;
        }

        /*============================================*/
        /* Listeners                                  */
        /*============================================*/
        /**
         * Listens to the buttons of the dialog.
         */
        public class TLCActionListener implements ActionListener {
            /**
             * DOCUMENT ME!
             *
             * @param e DOCUMENT ME!
             */
            public void actionPerformed(ActionEvent e) {
                String sel = ((Button) e.getSource()).getLabel();

                if (sel.equals("Ok")) {
                    int tc = 0;

                    try {
                        if (settings.ints != null) {
                            for (int i = 0; i < settings.ints.length;
                                    i++, tc++)
                                settings.ints[i] = Integer.parseInt(texts[tc].getText());
                        }

                        if (settings.floats != null) {
                            for (int i = 0; i < settings.floats.length;
                                    i++, tc++)
                                settings.floats[i] = Float.valueOf(texts[tc].getText())
                                                          .floatValue();
                        }
                    } catch (NumberFormatException nfe) {
                        String s = (settings.ints == null) ? "float"
                                                           : ((tc < settings.ints.length)
                            ? "int" : "float");
                        texts[tc].setText("Enter a valid " + s +
                            "! (Or press cancel)");

                        return;
                    }
                }

                hide();
            }
        }

        /*============================================*/
        /* Panels                                     */
        /*============================================*/
        /**
         * Panel containing the necessary components to set the TLC
         * properties.
         */
        public class TLCPropPanel extends Panel {
/**
             * Creates a new TLCPropPanel object.
             */
            public TLCPropPanel() {
                GridBagLayout gridbag = new GridBagLayout();
                this.setLayout(gridbag);

                texts = new TextField[settings.descriptions.length];

                int tc = 0;

                if (settings.ints != null) {
                    for (int i = 0; i < settings.ints.length; i++, tc++)
                        texts[tc] = makeRow(gridbag, settings.descriptions[tc],
                                texts[tc], settings.ints[i] + "");
                }

                if (settings.floats != null) {
                    for (int i = 0; i < settings.floats.length; i++, tc++)
                        texts[tc] = makeRow(gridbag, settings.descriptions[tc],
                                texts[tc], settings.floats[i] + "");
                }
            }

            /**
             * DOCUMENT ME!
             *
             * @param gridbag DOCUMENT ME!
             * @param label DOCUMENT ME!
             * @param textField DOCUMENT ME!
             * @param text DOCUMENT ME!
             *
             * @return DOCUMENT ME!
             */
            private TextField makeRow(GridBagLayout gridbag, String label,
                TextField textField, String text) {
                GridBagConstraints c = new GridBagConstraints();
                Label lbl;

                c.fill = GridBagConstraints.BOTH;
                c.weightx = 1.0;
                lbl = new Label(label);
                gridbag.setConstraints(lbl, c);
                this.add(lbl);
                c.gridwidth = GridBagConstraints.REMAINDER;
                c.weightx = 1.0;
                textField = new TextField(text, 10);
                gridbag.setConstraints(textField, c);
                this.add(textField);

                return textField;
            }
        }

        /**
         * Panel containing buttons "Ok" and "Cancel".
         */
        public class OkCancelPanel extends Panel {
/**
             * Creates a new OkCancelPanel object.
             *
             * @param action DOCUMENT ME!
             */
            public OkCancelPanel(ActionListener action) {
                this.setLayout(new FlowLayout(FlowLayout.CENTER));

                String[] labels = { "Ok", "Cancel" };
                Button b;

                for (int i = 0; i < labels.length; i++) {
                    b = new Button(labels[i]);
                    b.addActionListener(action);
                    this.add(b);
                }
            }
        }
    }
}
