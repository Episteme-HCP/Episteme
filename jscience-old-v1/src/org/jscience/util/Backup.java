/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;

import org.jscience.io.ExtendedFile;
import org.jscience.util.license.Licensed;
import org.jscience.util.logging.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.TimerTask;


/**
 * allows to easily backup directories. Copied are only those files that do
 * not exist or have a modification date after the one that exists or have a
 * different file length. Other existing files in the target directory remain
 * there.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.io.ExtendedFile#synchronizeDir(File)
 * @see org.jscience.swing.JBackup
 */
public class Backup implements Runnable, Licensed {
    /**
     * DOCUMENT ME!
     */
    FileFilter filter;

    /**
     * DOCUMENT ME!
     */
    ExtendedFile originDir;

    /**
     * DOCUMENT ME!
     */
    ExtendedFile destinationDir;

    /**
     * DOCUMENT ME!
     */
    Logger logger;

    /**
     * DOCUMENT ME!
     */
    Monitor monitor;

    /**
     * DOCUMENT ME!
     */
    boolean running = false;

    /**
     * DOCUMENT ME!
     */
    boolean deleteAtTarget;

    /**
     * Creates a new Backup object.
     *
     * @param originDir DOCUMENT ME!
     * @param destinationDir DOCUMENT ME!
     */
    public Backup(File originDir, File destinationDir) {
        this(originDir, destinationDir, null);
    }

    /**
     * Creates a new Backup object.
     *
     * @param originDir DOCUMENT ME!
     * @param destinationDir DOCUMENT ME!
     * @param filter DOCUMENT ME!
     */
    public Backup(File originDir, File destinationDir, FileFilter filter) {
        this(originDir, destinationDir, filter, false, new Monitor(),
            new Logger());
    }

    /**
     * Creates a new Backup object.
     *
     * @param originDir DOCUMENT ME!
     * @param destinationDir DOCUMENT ME!
     * @param filter DOCUMENT ME!
     * @param deleteAtTarget DOCUMENT ME!
     */
    public Backup(File originDir, File destinationDir, FileFilter filter,
        boolean deleteAtTarget) {
        this(originDir, destinationDir, filter, deleteAtTarget, new Monitor(),
            new Logger());
    }

    /**
     * Creates a new Backup object.
     *
     * @param originDir DOCUMENT ME!
     * @param destinationDir DOCUMENT ME!
     * @param filter DOCUMENT ME!
     * @param deleteAtTarget DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     * @param logger DOCUMENT ME!
     */
    public Backup(File originDir, File destinationDir, FileFilter filter,
        boolean deleteAtTarget, Monitor monitor, Logger logger) {
        Settings.checkLicense(this);

        if ((!originDir.isDirectory() || (!destinationDir.isDirectory()))) {
            throw new IllegalArgumentException(
                "both, origin and destination must be directories");
        }

        this.logger = logger;
        setMonitor(monitor);
        this.originDir = new ExtendedFile(originDir);
        this.destinationDir = new ExtendedFile(destinationDir);
        this.filter = filter;
        this.deleteAtTarget = deleteAtTarget;
    }

    /**
     * replaces any previous filter
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setNoSubDirsFilter() throws IllegalStateException {
        FileFilter f = new FileFilter() {
                public boolean accept(File file) {
                    return new ExtendedFile(file).getFileRelativeTo(originDir)
                                                 .getParent() == null;
                }
            };

        setFilter(f);
    }

    /**
     * replaces any previous filter; the path matching is
     * case-insensitive
     *
     * @param excludeList DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setExcludeFiles(final File[] excludeList)
        throws IllegalArgumentException {
        FileFilter f = new FileFilter() {
                public boolean accept(File file) {
                    for (File exclude : excludeList) {
                        if (file.getPath().toLowerCase()
                                    .indexOf(exclude.getPath().toLowerCase()) >= 0) {
                            return false;
                        }
                    }

                    return true;
                }
            };

        setFilter(f);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TimerTask createTimerTask() {
        return new TimerTask() {
                public void run() {
                    run();
                }
            };
    }

    /**
     * performs the backup operation
     */
    public void run() {
        Settings.checkLicense(this);

        synchronized (this) {
            if (running) {
                if (logger != null) {
                    logger.log(this, Level.WARNING,
                        "attempting to start backup while backup is already running");
                }

                return;
            }

            running = true;
        }

        monitor.setMin(0);
        monitor.setMax(1);
        monitor.setNumber(0);
        doBackup();
        running = false;
    }

    /**
     * DOCUMENT ME!
     */
    private void doBackup() {
        if (logger != null) {
            logger.log(this, Level.BEGIN, "begin backup", originDir,
                destinationDir);
        }

        ExtendedFile[] list = originDir.listFilesInTree(filter);

        if (list == null) {
            monitor.setNumber(1);
            monitor.done();

            if (logger != null) {
                logger.log(this, Level.EXCEPTION, "files not retrievable",
                    originDir);
            }

            return;
        }

        monitor.setMax(list.length);

        HashSet<ExtendedFile> deleteList = new HashSet<ExtendedFile>();

        if (deleteAtTarget) {
            for (ExtendedFile f : destinationDir.listFilesInTree(filter)) {
                deleteList.add(f);
            }
        }

        for (ExtendedFile file : list) {
            if (monitor.disabled()) {
                break;
            }

            try {
                monitor.setMessage(file.toString());
                monitor.increment();

                ExtendedFile dest = new ExtendedFile(destinationDir,
                        file.getFileRelativeTo(originDir).getPath());

                if (deleteAtTarget) {
                    deleteList.remove(dest);
                }

                if (file.isDirectory()) {
                    if (!dest.exists()) {
                        dest.mkdirs();

                        if (logger != null) {
                            logger.log(this, Level.FINE, "dir created",
                                file.getFileRelativeTo(originDir));
                        }
                    }
                } else {
                    if (dest.exists() &&
                            (dest.lastModified() >= file.lastModified()) &&
                            (dest.length() == file.length())) {
                        if (logger != null) {
                            logger.log(this, Level.FINER,
                                "file exists; not copied",
                                file.getFileRelativeTo(originDir));
                        }
                    } else {
                        dest.getParentFile().mkdirs();
                        file.copyTo(dest);

                        if (logger != null) {
                            logger.log(this, Level.FINE, "file copied",
                                file.getFileRelativeTo(originDir));
                        }
                    }
                }
            } catch (IOException ex) {
                if (logger != null) {
                    logger.log(this, ex);
                }
            }
        }

        if (deleteAtTarget) {
            for (ExtendedFile f : deleteList) {
                if (monitor.disabled()) {
                    break;
                }

                boolean flag = f.delete();

                if (!flag && f.isDirectory()) {
                    flag = f.removeTree(true);
                }

                if (logger != null) {
                    if (flag) {
                        logger.log(this, Level.FINE, "file deleted", f);
                    } else {
                        logger.log(this, Level.WARNING,
                            "could not delete file", f);
                    }
                }
            }
        }

        if (monitor.disabled() && (logger != null)) {
            logger.log(this, Level.INFO, "backup aborted by monitor");
        }

        monitor.done();

        if (logger != null) {
            logger.log(this, Level.END, "end backup", originDir, destinationDir);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void cancel() {
        monitor.disable();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getDeleteAtTarget() {
        return deleteAtTarget;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Monitor getMonitor() {
        return monitor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FileFilter getFilter() {
        return filter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     */
    public synchronized void setDeleteAtTarget(boolean flag) {
        if (running) {
            throw new IllegalStateException("backup is running");
        }

        deleteAtTarget = flag;
    }

    /**
     * DOCUMENT ME!
     *
     * @param logger DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public synchronized void setLogger(Logger logger)
        throws IllegalStateException {
        if (running) {
            throw new IllegalStateException("backup is running");
        }

        this.logger = logger;
    }

    /**
     * DOCUMENT ME!
     *
     * @param monitor DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     * @throws NullPointerException DOCUMENT ME!
     */
    public synchronized void setMonitor(Monitor monitor)
        throws IllegalStateException {
        if (running) {
            throw new IllegalStateException("backup is running");
        }

        if (monitor == null) {
            throw new NullPointerException();
        }

        this.monitor = monitor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public synchronized void setFilter(FileFilter filter)
        throws IllegalStateException {
        if (running) {
            throw new IllegalStateException("backup is running");
        }

        this.filter = filter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param originDir DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public synchronized void setOriginDir(File originDir)
        throws IllegalStateException {
        if (running) {
            throw new IllegalStateException("backup is running");
        }

        if (!originDir.isDirectory()) {
            throw new IllegalArgumentException("not a directory: " + originDir);
        }

        this.originDir = new ExtendedFile(originDir);
    }

    /**
     * DOCUMENT ME!
     *
     * @param destinationDir DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public synchronized void setDestinationDir(File destinationDir)
        throws IllegalStateException {
        if (running) {
            throw new IllegalStateException("backup is running");
        }

        if (!destinationDir.isDirectory()) {
            throw new IllegalArgumentException("not a directory: " +
                destinationDir);
        }

        this.destinationDir = new ExtendedFile(destinationDir);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public File getOriginDir() {
        return originDir;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public File getDestinationDir() {
        return destinationDir;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println(
                "usage \"java org.jscience.util.Backup [-s] <originDir> <destinationDir> [logFile]\"");

            return;
        }

        int n = 0;
        Backup b = null;

        if (args[0].equals("-s")) {
            if (args.length < 3) {
                System.out.println(
                    "usage \"java org.jscience.util.Backup [-s] <originDir> <destinationDir> [logFile]\"");

                return;
            }

            n++;
            b = new Backup(new File(args[n]), new File(args[n + 1]));
            b.setFilter(null);
        } else {
            b = new Backup(new File(args[n]), new File(args[n + 1]));
            b.setNoSubDirsFilter();
        }

        b.setLogger(new Logger(new ConsoleLog()));

        if (args.length > (n + 2)) {
            b.getLogger().addWriter(new LogFile(args[n + 2]));
        }

        b.getLogger().setFilter(new Filter<LogEntry>() {
                public boolean accept(LogEntry entry) {
                    return (entry.getLevel().compareTo(Level.FINE) >= 0);
                }
            });
        b.run();
    }
}
