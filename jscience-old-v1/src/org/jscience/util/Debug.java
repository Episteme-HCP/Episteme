/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;

import org.jscience.io.DoublePrintStream;
import org.jscience.io.ExtendedFile;
import org.jscience.swing.JMainFrame;
import org.jscience.swing.JMemoryGauge;
import org.jscience.swing.Menus;
import org.jscience.util.logging.Logger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Date;
import java.util.TreeMap;


/**
 * provides globally accessible methods and variables for convenient
 * debugging
 *
 * @author Holger Antelmann
 * @see Monitor
 */
public final class Debug {
    /**
     * can be used to display a dialog for uncaught exceptions.
     *
     * @see java.lang.Thread#setDefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler)
     * @see java.lang.Thread#setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler)
     */
    public static final Thread.UncaughtExceptionHandler dialogExceptionHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread t, Throwable e) {
            //SoundPlayer.beep();
            Menus.showExceptionDialog(null, e, t);
        }
    };

    /**
     * DOCUMENT ME!
     */
    final static boolean licensingEnabled = "false".equals(Settings.getProperty(
            "application.licensing.enabled", "false"));

    /**
     * monitor is initialized with new Monitor(10)
     */
    public static final Monitor monitor = new Monitor(10);

    /**
     * false by default
     */
    public static volatile boolean enabled = false;

    /**
     * the logger is initialized as a plain empty logger
     */
    public static final Logger logger = new Logger();

    /**
     * initialized as non-running
     */
    public static final Stopwatch stopwatch = new Stopwatch(false);

    /**
     * a ExtendedFile object that is initially null
     */
    public static ExtendedFile file = null;

    /**
     * Creates a new Debug object.
     */
    private Debug() {
    }

    /**
     * pops up a JMemoryGauge in a simple JMainFrame
     *
     * @see org.jscience.swing.JMemoryGauge
     */
    public static void showMemoryGauge() {
        final JMemoryGauge gauge = new JMemoryGauge();
        JMainFrame frame = new JMainFrame(gauge, false, true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                gauge.deactivate();
            }
        });
        frame.setTitle("MemoryGauge");
        frame.updateStatusText("max memory: " +
                (Runtime.getRuntime().maxMemory() / 1024) + " kb");
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static String stackTraceAsString(Throwable t) {
        return stackTraceAsString(t, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tw DOCUMENT ME!
     * @param td DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static String stackTraceAsString(Throwable tw, Thread td) {
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);

        if (td == null) {
            writer.println("stack trace for " + Thread.currentThread() +
                    "(*) at " + new Date() + ":");
        } else {
            writer.println("stack trace for " + td + " at " + new Date() + ":");
        }

        tw.printStackTrace(writer);
        writer.flush();
        writer.close();

        return sw.getBuffer().toString();
    }

    /**
     * returns the name of the temp file the Throwable was written to
     * or null if the write attempt was unsuccessful.
     *
     * @param t DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static File printToTempFile(Throwable t) {
        try {
            ExtendedFile file = new ExtendedFile(File.createTempFile(
                    t.getClass().getName(), null));
            file.writeStackTrace(t, false);
            System.out.println("stack trace written to: " + file);

            return file;
        } catch (IOException e) {
            System.err.println("could not write stack trace:");
            e.printStackTrace();
            System.err.println("original exception:");
            t.printStackTrace();

            return null;
        }
    }

    /**
     * returns true only if operation was successfull
     *
     * @param t      DOCUMENT ME!
     * @param file   DOCUMENT ME!
     * @param append DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static boolean printToFile(Throwable t, File file, boolean append) {
        try {
            ExtendedFile f = new ExtendedFile(file);
            f.writeStackTrace(t, append);
            System.out.println("stack trace written to: " + file);

            return true;
        } catch (IOException e) {
            System.err.println("could not write stack trace:");
            e.printStackTrace();
            System.err.println("original exception:");
            t.printStackTrace();

            return false;
        }
    }

    /**
     * prints all settings (including all system properties and
     * environment variables) to the console. The output is sorted by property
     * name. System variables may be overwritten by system properties may be
     * overwritten by application settings in this map.
     */
    public static void dumpSettings() {
        TreeMap<Object, Object> p = new TreeMap<Object, Object>();
        p.putAll(System.getenv());
        p.putAll(System.getProperties());
        p.putAll(Settings.getProperties());

        String s = StringUtils.mapAsString(p, " = ", StringUtils.lb);
        System.out.println(s);
    }

    /**
     * excludes the call to this method and beyond in the stack trace
     *
     * @return DOCUMENT ME!
     */
    public static String getStackTraceAsString() {
        Thread t = Thread.currentThread();
        StackTraceElement[] stack = t.getStackTrace();
        StringBuilder buf = new StringBuilder("stack trace for " + t + " at " +
                new Date() + ":" + StringUtils.lb);

        for (int i = 3; i < stack.length; i++) {
            buf.append(stack[i].toString() + StringUtils.lb);
        }

        return buf.toString();
    }

    /**
     * DOCUMENT ME!
     */
    public static void dumpStack() {
        System.out.println(
                "------------------------------------------------------------");
        System.out.println(getStackTraceAsString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param file   DOCUMENT ME!
     * @param append DOCUMENT ME!
     * @throws FileNotFoundException DOCUMENT ME!
     */
    public static void addSystemErrFile(File file, boolean append)
            throws FileNotFoundException {
        System.setErr(new DoublePrintStream(System.err,
                new PrintStream(new FileOutputStream(file, append))));
    }

    /**
     * DOCUMENT ME!
     *
     * @param file   DOCUMENT ME!
     * @param append DOCUMENT ME!
     * @throws FileNotFoundException DOCUMENT ME!
     */
    public static void addSystemOutFile(File file, boolean append)
            throws FileNotFoundException {
        System.setOut(new DoublePrintStream(System.out,
                new PrintStream(new FileOutputStream(file, append))));
    }

    /**
     * DOCUMENT ME!
     *
     * @param file   DOCUMENT ME!
     * @param append DOCUMENT ME!
     * @throws FileNotFoundException DOCUMENT ME!
     */
    public static void setSystemErr(File file, boolean append)
            throws FileNotFoundException {
        System.setErr(new PrintStream(new FileOutputStream(file, append)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param file   DOCUMENT ME!
     * @param append DOCUMENT ME!
     * @throws FileNotFoundException DOCUMENT ME!
     */
    public static void setSystemOut(File file, boolean append)
            throws FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream(file, append)));
    }

    /**
     * can be used to conveniently write all exceptions to a file.
     *
     * @see java.lang.Thread#setDefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler)
     * @see java.lang.Thread#setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler)
     */
    public static class FileExceptionHandler implements Thread.UncaughtExceptionHandler {
        /**
         * DOCUMENT ME!
         */
        ExtendedFile file;

        /**
         * DOCUMENT ME!
         */
        boolean showDialog;

        /**
         * Creates a new FileExceptionHandler object.
         *
         * @param file DOCUMENT ME!
         */
        public FileExceptionHandler(File file) {
            this(file, false);
        }

        /**
         * Creates a new FileExceptionHandler object.
         *
         * @param file       DOCUMENT ME!
         * @param showDialog DOCUMENT ME!
         */
        public FileExceptionHandler(File file, boolean showDialog) {
            this.file = new ExtendedFile(file);
            this.showDialog = showDialog;
        }

        /**
         * DOCUMENT ME!
         *
         * @param t DOCUMENT ME!
         * @param e DOCUMENT ME!
         */
        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();

            try {
                if (file.writeStackTrace(t, e, true)) {
                    System.err.println("(exception written to: " + file + ")");

                    if (showDialog) {
                        Menus.showExceptionDialog(null, e, t);
                    } else {
                        //SoundPlayer.beep();
                    }

                    return;
                }
            } catch (FileNotFoundException ex) {
                System.err.println(ex.toString());
            }

            System.err.println("warning: stack trace could not be written to " +
                    file);

            if (showDialog) {
                Menus.showExceptionDialog(null, e, t);
            } else {
                //SoundPlayer.beep();
            }
        }
    }
}
