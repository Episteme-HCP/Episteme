/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.net;

import java.io.*;

import java.net.URL;

import java.util.ArrayList;


/**
 * Downloads a URL or URLCache object into a file. You can either download
 * the URL synchronously using the provided static method or asynchronously in
 * a separate thread. When doing the latter, you can first instanciate this
 * Runnable, register a listener (in case you want to be notified upon
 * completion) and then start the thread. If in a separate thread, you can
 * also monitor the progress of the download using getReadSoFar() or cancel
 * the download.
 *
 * @author Holger Antelmann
 *
 * @see JDownloader
 */
public class Downloader implements Runnable {
    /**
     * DOCUMENT ME!
     */
    public static final int UNKNOWN_LENGTH = -1;

    /**
     * DOCUMENT ME!
     */
    public static final int LENGTH_NOT_OBTAINED_YET = -2;

    /**
     * DOCUMENT ME!
     */
    public static final int THREAD_NOT_STARTED_YET = -3;

    /**
     * DOCUMENT ME!
     */
    int bufferSize = 16384;

    /**
     * DOCUMENT ME!
     */
    URLCache uc;

    /**
     * DOCUMENT ME!
     */
    int read = THREAD_NOT_STARTED_YET;

    /**
     * DOCUMENT ME!
     */
    int length = LENGTH_NOT_OBTAINED_YET;

    /**
     * DOCUMENT ME!
     */
    ArrayList<Listener> listeners;

    /**
     * DOCUMENT ME!
     */
    boolean enabled;

    /**
     * DOCUMENT ME!
     */
    InputStream in = null;

    /**
     * DOCUMENT ME!
     */
    OutputStream out;

    /**
     * DOCUMENT ME!
     */
    boolean closeOnCompletion;

    /**
     * DOCUMENT ME!
     */
    volatile boolean started = false;

    /**
     * Creates a new Downloader object.
     *
     * @param url DOCUMENT ME!
     * @param file DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     */
    public Downloader(URL url, File file) throws FileNotFoundException {
        this(new URLCache(url), file);
    }

    /**
     * Creates a new Downloader object.
     *
     * @param uc DOCUMENT ME!
     * @param file DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     */
    public Downloader(URLCache uc, File file) throws FileNotFoundException {
        this(uc, new FileOutputStream(file));
    }

/**
     * closes the stream on completion
     */
    public Downloader(URL url, OutputStream out) {
        this(new URLCache(url), out, true);
    }

/**
     * closes the stream on completion
     */
    public Downloader(URLCache uc, OutputStream out) {
        this(uc, out, true);
    }

    /**
     * Creates a new Downloader object.
     *
     * @param uc DOCUMENT ME!
     * @param out DOCUMENT ME!
     * @param closeOnCompletion DOCUMENT ME!
     */
    public Downloader(URLCache uc, OutputStream out, boolean closeOnCompletion) {
        this.uc = uc;
        this.out = out;
        this.closeOnCompletion = closeOnCompletion;
        listeners = new ArrayList<Listener>(1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setCloseOnCompletion(boolean flag) throws IllegalStateException {
        if (started) {
            throw new IllegalStateException("download already started");
        }

        closeOnCompletion = flag;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getCloseOnCompletion() {
        return closeOnCompletion;
    }

    /**
     * synchronously downloads the given url to the given file
     *
     * @param url DOCUMENT ME!
     * @param file DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void download(URL url, File file) throws IOException {
        download(new URLCache(url), file);
    }

    /**
     * synchronously downloads the given uc to the given file
     *
     * @param uc DOCUMENT ME!
     * @param file DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void download(URLCache uc, File file)
        throws IOException {
        download(uc, new FileOutputStream(file));
    }

    /**
     * synchronously downloads the given url to the given output stream
     *
     * @param url DOCUMENT ME!
     * @param out DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void download(URL url, OutputStream out)
        throws IOException {
        download(new URLCache(url), out);
    }

    /**
     * synchronously downloads the given url to the given output stream
     *
     * @param uc DOCUMENT ME!
     * @param out DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void download(URLCache uc, OutputStream out)
        throws IOException {
        Downloader d = new Downloader(uc, out);
        d.download();
    }

    /**
     * cancels the current download; the listeners will be called with
     * an IOException containing the message that canced() was called or that
     * the input stream was closed
     */
    public void cancel() {
        enabled = false;

        try {
            in.close();
        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param size DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setBufferSize(int size) throws IllegalStateException {
        if (enabled) {
            throw new IllegalStateException("download is running");
        }

        bufferSize = size;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void download() throws IOException {
        started = true;

        IOException exception = null;

        try {
            if (uc.isCached()) {
                length = uc.getRealContentLength();
                in = uc.getInputStream();
            } else {
                // don't use the cache stuff as it's harder to monitor
                // and hardly possible if the file is very large
                uc.stopCurrentRefresh();
                in = uc.getURL().openStream();
                length = uc.peekContentLength();
            }

            read = 0;

            byte[] buffer = new byte[bufferSize];
            int n = 0;

            while (enabled && (n > -1)) {
                n = in.read(buffer);
                read += n;

                if (n < 0) {
                    break;
                }

                out.write(buffer, 0, n);
            }

            out.flush();
        } catch (IOException e) {
            exception = e;

            if (!enabled) {
                e = new IOException("download interrupted by calling cancel()");
            }
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                in.close();
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * downloads the URL to the file and calls any listeners upon
     * completion
     *
     * @throws IllegalStateException DOCUMENT ME!
     *
     * @see Downloader.Listener
     * @see #addListener(Downloader.Listener)
     */
    public synchronized void run() {
        if (started) {
            throw new IllegalStateException("download has already been started");
        }

        enabled = true;

        try {
            download();
            notifyListener(null);
        } catch (IOException e) {
            notifyListener(e);
        }

        enabled = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public OutputStream getOutputStream() {
        return out;
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
     * returns the bytes that have been read by the thread; returns
     * <code>THREAD_NOT_STARTED_YET</code> if the thread hasn't been started,
     * yet
     *
     * @return DOCUMENT ME!
     */
    public int getReadSoFar() {
        return read;
    }

    /**
     * returns the content length or
     * <code>LENGTH_NOT_OBTAINED_YET</code> if the download hasn't been
     * started, yet; <code>UNKNOWN_LENGTH</code> is returned if the length is
     * unknown
     *
     * @return DOCUMENT ME!
     */
    public int getContentLength() {
        return length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    synchronized void notifyListener(IOException e) {
        for (Listener l : listeners)
            l.downloaded(this, e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    public interface Listener {
        /**
         * is called when a download from a Downloader finished.
         *
         * @param dwnl DOCUMENT ME!
         * @param errorIfAny if null, the download completed normally,
         *        otherwise it contains the error that caused the failure
         */
        void downloaded(Downloader dwnl, IOException errorIfAny);
    }
}
