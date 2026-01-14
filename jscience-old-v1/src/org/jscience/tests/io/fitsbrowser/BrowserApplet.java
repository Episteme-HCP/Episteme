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

package org.jscience.tests.io.fitsbrowser;

import eap.applet.DetachableApplet;
import eap.filter.*;
import org.jscience.io.fits.FitsException;
import org.jscience.io.fits.FitsFile;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;


/**
 * This is the applet version of the Java FITS Broswer. It accepts the
 * following parameters:
 * <p/>
 * <ul>
 * <li>
 * FILE - the name of the FITS file to be loaded relative to the directory in
 * which the applet's HTML document resides. If this parameter is omitted, the
 * browser will display a minimal empty FITS file.
 * </li>
 * <li>
 * PASSWORD - the encryption key to be used if the file is encrypted. This is
 * useful if the password is stored in a cookie.
 * </li>
 * <li>
 * DATASET - Specifies a unique name for the dataset to which this file
 * belongs. The Applet remembers encryption keys entered by the user, and uses
 * the dataset name to later recall the key. So a "dataset" is defined as a
 * set of files with the same encryption key. This parameter has no effect on
 * files which are not encrypted. If this parameter is omitted, the key will
 * not be remembered.
 * </li>
 * </ul>
 * <p/>
 * Note encrypted files can only be read if you have the optional eap.crypto
 * package.
 */
public class BrowserApplet extends DetachableApplet {
    /**
     * DOCUMENT ME!
     */
    FITSFileDisplay display;

    /**
     * DOCUMENT ME!
     */
    private String new_password;

    /**
     * DOCUMENT ME!
     */
    private AppletPasswordProvider password;

    /**
     * This method runs after the browser loads the page. It sets up the
     * applet, and calls a separate thread to read the file data.
     */
    public void init() {
        new_password = null;

        display = new FITSFileDisplay();
        password = new AppletPasswordProvider(this);
        display.setPasswordProvider(password);
        getContentPane().add(display);

        // setVisible(true);
        String file = getParameter("FILE");
        new BackgroundLoader(file).start();
    } // end of init method

    /**
     * Reports an error in loading the file. The FITS file display is replaced
     * with a JTextArea which displays a description of the error.
     *
     * @param file        The name of the file being read
     * @param explanation A human readable explaination of why the error
     *                    occured.
     * @param e           The exception being reported.
     */
    private void reportError(String file, String explanation, Throwable e) {
        JTextArea message = new JTextArea();

        message.setEditable(false);
        message.setLineWrap(true);

        Container applet = getContentPane();
        applet.add(message, BorderLayout.NORTH);

        /**
         * file name
         */
        message.append("Could not read " + file + "\n");

        if (!explanation.equals("")) {
            message.append("\n");
        }

        /**
         * exmplation
         */
        message.append(explanation);

        if (!explanation.equals("") && !explanation.endsWith("\n")) {
            message.append("\n");
        }

        /**
         * exception
         */
        message.append("\n" + e.toString() + "\n");

        try {
            /**
             * dumped any chained exceptions - this is wrapped in a try block
             * for backward compatibility i.e. versions of Java before
             * "getCause()".
             */
            for (Throwable cause = e.getCause(); cause != null;
                 cause = cause.getCause()) {
                message.append(cause.toString() + "\n");
            }
        } catch (Throwable ex) {
        }
    } // end of reportError method

    /**
     * Erases the password for this applet which may have been stored from a
     * previous user entry. This is useful if you want to reset the pasxsword
     * using JavaScript.
     */
    public void forgetPassword() {
        password.forgetPassword();
    }

    /**
     * inner class for loading the FITS file in a background thread
     */
    private class BackgroundLoader extends Thread {
        /**
         * DOCUMENT ME!
         */
        String file;

        /**
         * create a new class which will load the given file
         *
         * @param file The file to be loaded.
         */
        public BackgroundLoader(String file) {
            this.file = file;
        }

        /**
         * This method is invoked when the thread is started.
         */
        public void run() {
            try {
                /**
                 * try reading the file from the URL
                 */
                if (file != null) {
                    display.load(new URL(getDocumentBase(), file));
                } else {
                    display.load(FitsFile.createEmpty());
                }

                /**
                 * If we prompted for a password, save it now that we have
                 * sucessfully read the file with it
                 */
                password.savePassword();
            } catch (FitsException e) {
                /**
                 * corrupted file
                 */
                reportError(file,
                        "The data do not conform to the FITS standard.\n" +
                                "If this is an encrypted file, then you may have " +
                                "entered the wrong encryption key\n" +
                                "Otherwise, the data may be corrupted or incorrectly formatted\n",
                        e);
            } catch (OutOfMemoryError e) {
                /**
                 * file too big
                 */
                reportError(file,
                        "The FITS file is too large to view with this applet", e);
            } catch (FilterException e) {
                reportError(file, "Error applying " + e.getFilter(), e);
            } catch (java.security.AccessControlException e) {
                /** Security error */
                String host = getCodeBase().getHost();
                String server = ".";

                if ((host != null) && !host.equals("")) {
                    server = " which in this case is " + host;
                }

                reportError(file,
                        "Unsigned applets are usually restricted to reading " +
                                "data from the same server as the one on which the applet resides" +
                                server, e);
            } catch (IOException e) {
                /**
                 * IOException
                 */
                reportError(file,
                        "There was an error transferring the file to your browser. " +
                                "This could be due to network trouble, or to a problem on the web server. " +
                                "You can try again later, but if this problem persists, " +
                                "report it to the administrator of these web pages", e);
            } catch (Throwable e) {
                /**
                 * Random trouble
                 */
                reportError(file, "", e);
            }
        } // end of run method
    } // end of BackgroundLoader class
} // end of BrowserApplet class
