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

package org.jscience.util;

import org.jscience.swing.Menus;
import org.jscience.util.license.License;
import org.jscience.util.license.LicenseHandler;
import org.jscience.util.license.LicenseManager;
import org.jscience.util.license.LicensingException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;


/**
 * can be used to aquire licenses from a known location on demand. The
 * LicenseLoader can be initialized to first check a list of URLs to be loaded
 * to aquire the needed license. Only if that fails (or if no URLs were
 * configured to begin with), an upcoming dialog will enable the user to
 * provide a location from where a final attempt is made to load the required
 * license. If that last resort fails, too, a LicensingException to be thrown
 * may be inevidable.
 *
 * @author Holger Antelmann
 * @see Settings#setLicenseHandler(LicenseHandler)
 * @see Settings#checkLicense(Object)
 * @see org.jscience.util.license.LicensingException
 */
public class LicenseLoader implements LicenseHandler {
    /**
     * DOCUMENT ME!
     */
    Vector<URL> urls = new Vector<URL>();

    /**
     * DOCUMENT ME!
     */
    Component parentComponent = null;

    /**
     * Creates a new LicenseLoader object.
     */
    public LicenseLoader() {
    }

    /**
     * Creates a new LicenseLoader object.
     *
     * @param url DOCUMENT ME!
     */
    public LicenseLoader(URL url) {
        urls.add(url);
    }

    /**
     * Creates a new LicenseLoader object.
     *
     * @param col DOCUMENT ME!
     */
    public LicenseLoader(Collection<URL> col) {
        urls.addAll(col);
    }

    /**
     * Creates a new LicenseLoader object.
     *
     * @param list DOCUMENT ME!
     */
    public LicenseLoader(URL[] list) {
        for (URL url : list)
            urls.add(url);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getParentComponent() {
        return parentComponent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void setParentComponent(Component c) {
        parentComponent = c;
    }

    /**
     * these URLs will be used to aquire licenses on demand before user
     * interaction may be required.
     *
     * @return DOCUMENT ME!
     */
    public Vector<URL> getLocations() {
        return urls;
    }

    /**
     * first checks the already known locations and tries to aquire a
     * license from there; if that fails, a user dialog appears - allowing to
     * choose another location.
     *
     * @param licensee DOCUMENT ME!
     */
    public synchronized void aquireLicense(Object licensee) {
        LicenseManager lm = LicenseManager.getLicenseManager();
        License l = lm.getLicense(licensee);

        try {
            Iterator<URL> i = urls.iterator();

            while ((i.hasNext()) && ((l == null) || (!l.isValid()))) {
                URL url = i.next();
                i.remove();

                try {
                    lm.loadFrom(url);
                } catch (IOException ignore) {
                }

                l = lm.getLicense(licensee);
            }

            if ((l != null) && (l.isValid())) {
                return;
            }

            //SoundPlayer.beep();
            String[] options = new String[]{"from file", "from URL", "cancel"};
            int option = JOptionPane.showOptionDialog(parentComponent,
                    "license required for " + licensee.getClass().getName(),
                    "LicenseLoader", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, options, null);

            switch (option) {
                case 0:

                    JFileChooser fc = new JFileChooser();
                    fc.setDialogTitle("aquire license");

                    int a = fc.showOpenDialog(null);

                    if (a != JFileChooser.APPROVE_OPTION) {
                        return;
                    }

                    lm.loadFrom(fc.getSelectedFile());

                    break;

                case 1:

                    String location = JOptionPane.showInputDialog(null,
                            "enter URL for license");

                    if (location == null) {
                        return;
                    }

                    lm.loadFrom(new URL(location));

                    break;

                case JOptionPane.CLOSED_OPTION:
                    return;

                default:
                    assert false : "unexpected option encountered";

                    return;
            }
        } catch (IOException ex) {
            Menus.showExceptionDialog(ex);
        } catch (LicensingException ex) {
            Menus.showExceptionDialog(ex);
        }
    }
}
