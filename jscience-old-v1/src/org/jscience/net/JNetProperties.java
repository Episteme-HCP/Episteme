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

package org.jscience.net;

import org.jscience.swing.Menus;

import java.util.Properties;

import javax.swing.*;


/**
 * provides a form to edit Java network properties
 *
 * @author Holger Antelmann
 */
public class JNetProperties extends JComponent {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 2622426095727603294L;

    /** DOCUMENT ME! */
    final static String[] keys = new String[] {
            "http.proxyHost", "http.proxyPort", "http.nonProxyHosts",
            "ftp.proxyHost", "ftp.proxyPort", "ftp.nonProxyHosts", "http.agent",
            "http.auth.digest.validateServer", "http.auth.digest.validateProxy",
            "http.auth.digest.cnonceRepeat", "http.auth.ntlm.domain",
            "http.keepAlive", "http.maxConnections", "socksProxyHost",
            "socksProxyPort", "java.net.preferIPv4Stack",
            "java.net.preferIPv6Addresses", "networkaddress.cache.ttl",
            "networkaddress.cache.negative.ttl", "sun.net.inetaddr.ttl",
            "sun.net.inetaddr.negative.ttl",
            "sun.net.client.defaultConnectTimeout",
            "sun.net.client.defaultReadTimeout"
        };

    /** DOCUMENT ME! */
    final static String[] defaults = new String[] {
            "false", "false", "-1", "10", "<none>",
            "80 if http.proxyHost specified", "<none>", "<none>",
            "80 if ftp.proxyHost specified", "<none>", "Java1.4.0", "false",
            "false", "5", "", "true", "5", "<none>", "1080", "", "", "-1", "-1"
        };

    /** DOCUMENT ME! */
    Properties props;

    /** DOCUMENT ME! */
    JTextField[] field = new JTextField[keys.length];

/**
     * calls <code>this(new Properties())</code>
     */
    public JNetProperties() {
        this(new Properties());
    }

/**
     * Creates a new JNetProperties object.
     *
     * @param props DOCUMENT ME!
     */
    public JNetProperties(Properties props) {
        setLayout(new SpringLayout());

        for (int i = 0; i < keys.length; i++) {
            field[i] = new JTextField(20);

            JLabel label = new JLabel(keys[i] + ":", JLabel.TRAILING);
            label.setToolTipText("defaults to: " + defaults[i]);
            add(label);
            add(field[i]);
        }

        setProperties(props);
        Menus.makeCompactSpringGrid(this, keys.length, 2, 3, 3, 2, 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param props DOCUMENT ME!
     */
    public void setProperties(Properties props) {
        this.props = props;

        for (int i = 0; i < keys.length; i++) {
            field[i].setText(props.getProperty(keys[i]));
        }
    }

    /**
     * merely returns the embedded properties without updating values
     * from the GUI
     *
     * @return DOCUMENT ME!
     */
    public Properties getProperties() {
        return props;
    }

    /**
     * updates the embedded properties with the entered values and
     * returns them
     *
     * @return DOCUMENT ME!
     */
    public Properties updateProperties() {
        for (int i = 0; i < keys.length; i++) {
            String value = field[i].getText();

            if ((value != null) && (value.length() < 1)) {
                value = null;
            }

            if (value == null) {
                props.remove(keys[i]);
            } else {
                props.setProperty(keys[i], value);
            }
        }

        return props;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     */
    public void setEnabled(boolean flag) {
        super.setEnabled(flag);

        for (int i = 0; i < field.length; i++) {
            field[i].setEnabled(flag);
        }
    }
}
