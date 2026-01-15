/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.net;

import java.awt.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import javax.swing.*;


/**
 * Downloads a URL or URLCache object into a file in a thread using a
 * ProgressMonitorInputStream, so that the user can abort if desired
 *
 * @author Holger Antelmann
 *
 * @see Downloader
 * @see JWebBrowser
 * @see javax.swing.ProgressMonitorInputStream
 */
public class JDownloader extends Thread {
    /** DOCUMENT ME! */
    static final int BUFFER_SIZE = 16384;

    /** DOCUMENT ME! */
    Component parent;

    /** DOCUMENT ME! */
    URLCache uc;

    /** DOCUMENT ME! */
    File file;

    /** DOCUMENT ME! */
    int read = -3;

    /** DOCUMENT ME! */
    int length = -2;

    /** DOCUMENT ME! */
    boolean showDialog = true;

    /** DOCUMENT ME! */
    IOException exception = null;

/**
     * Creates a new JDownloader object.
     *
     * @param parent DOCUMENT ME!
     * @param url    DOCUMENT ME!
     * @param file   DOCUMENT ME!
     */
    public JDownloader(Component parent, URL url, File file) {
        this(parent, new URLCache(url), file);
    }

/**
     * Creates a new JDownloader object.
     *
     * @param parent DOCUMENT ME!
     * @param uc     DOCUMENT ME!
     * @param file   DOCUMENT ME!
     */
    public JDownloader(Component parent, URLCache uc, File file) {
        super("JDownloader thread for " + uc.getURL());
        this.parent = parent;
        this.uc = uc;
        this.file = file;
    }

    /**
     * returns true if the end-dialog is shown after download (true by
     * default)
     *
     * @return DOCUMENT ME!
     */
    public boolean getShowEndDialog() {
        return showDialog;
    }

    /**
     * DOCUMENT ME!
     *
     * @param on DOCUMENT ME!
     */
    public void setShowEndDialog(boolean on) {
        showDialog = on;
    }

    /**
     * returns the bytes that have been read by the thread; returns -3
     * if the thread hasn't been started, yet
     *
     * @return DOCUMENT ME!
     */
    public int getReadSoFar() {
        return read;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public URL getURL() {
        return uc.getURL();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public File getFile() {
        return file;
    }

    /**
     * returns the content length of the URL once download started;
     * returns -2 if the thread hasn't been started, yet, or -1 if the length
     * is unknown.
     *
     * @return DOCUMENT ME!
     */
    public int getContentLength() {
        return length;
    }

    /**
     * if an IOException occurred during download, it can be later
     * accessed through this method.
     *
     * @return DOCUMENT ME!
     */
    public IOException getException() {
        return exception;
    }

    /**
     * downloads the URL to the file and starts the ProgressMonitor
     */
    public void run() {
        FileOutputStream out = null;
        ProgressMonitorInputStream in = null;
        InputStream stream = null;

        try {
            if (uc.isCached()) {
                length = uc.getRealContentLength();
                stream = uc.getInputStream();
            } else {
                // don't use the cache stuff as it's harder to monitor
                // and hardly possible if the file is very large
                uc.stopCurrentRefresh();
                stream = uc.getURL().openStream();
                length = uc.peekContentLength();
            }

            out = new FileOutputStream(file);

            String note = "saving " + file.getName() + " (bytes: " +
                ((length > 0) ? (length + ")") : "unknown)");
            in = new ProgressMonitorInputStream(parent, note, stream);

            ProgressMonitor mon = in.getProgressMonitor();
            mon.setMaximum((length < 1) ? Integer.MAX_VALUE : length);
            mon.setMillisToDecideToPopup(0);
            mon.setMillisToPopup(0);
            read = 0;

            byte[] buffer = new byte[BUFFER_SIZE];
            int n = 0;
            int read = 0;

            while (n > -1) {
                //if (mon.isCanceled()) throw new IOException("canceled by user");
                n = in.read(buffer);
                read += n;

                if (n < 0) {
                    break;
                }

                out.write(buffer, 0, n);
            }

            out.flush();

            if (showDialog) {
                JOptionPane.showMessageDialog(parent,
                    "download complete (" + file.getName() + ")");
            }
        } catch (IOException e) {
            exception = e;

            if (showDialog) {
                JOptionPane.showMessageDialog(parent, e.getMessage(),
                    "download failed", JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            try {
                out.close();
                in.close();
                stream.close();
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }
        }
    }
}
