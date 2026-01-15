/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.io;

import org.jscience.util.*;
import org.jscience.util.logging.Level;
import org.jscience.util.logging.Logger;

import java.io.*;

import java.net.URI;

import java.util.*;
import java.util.zip.*;


/**
 * A specialized File class that provides some additional functionality.
 * ExtendedFile adds no members to class File, only methods.
 *
 * @author Holger Antelmann
 *
 * @since 3/23/2002
 */
public class ExtendedFile extends File {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 1237779913761118481L;

    /**
     * DOCUMENT ME!
     */
    static String pathSeparator = System.getProperty("file.separator", "/");

    /**
     * Creates a new ExtendedFile object.
     *
     * @param file DOCUMENT ME!
     */
    public ExtendedFile(File file) {
        super(file.getPath());
    }

    /**
     * Creates a new ExtendedFile object.
     *
     * @param pathname DOCUMENT ME!
     */
    public ExtendedFile(String pathname) {
        super(pathname);
    }

    /**
     * Creates a new ExtendedFile object.
     *
     * @param parent DOCUMENT ME!
     * @param child DOCUMENT ME!
     */
    public ExtendedFile(String parent, String child) {
        super(parent, child);
    }

    /**
     * Creates a new ExtendedFile object.
     *
     * @param parent DOCUMENT ME!
     * @param child DOCUMENT ME!
     */
    public ExtendedFile(File parent, String child) {
        super(parent, child);
    }

    /**
     * Creates a new ExtendedFile object.
     *
     * @param uri DOCUMENT ME!
     */
    public ExtendedFile(URI uri) {
        super(uri);
    }

    /**
     * returns <code>setLastModified(System.currentTimeMillis())</code>
     * after possibly creating the file if it didn't exist before.
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public boolean touch() throws IOException {
        createNewFile();

        return setLastModified(System.currentTimeMillis());
    }

    /**
     * deletes the content of the file
     *
     * @throws IOException DOCUMENT ME!
     */
    public void clear() throws IOException {
        writeText("", false);
    }

    /**
     * in case of a directory, the combined size of all files within
     * the tree is returned; otherwise the file size.
     *
     * @return DOCUMENT ME!
     */
    public long length() {
        if (isDirectory()) {
            ExtendedFile[] list = listFilesInTree();
            long sum = 0;

            for (int i = 0; i < list.length; i++) {
                sum += list[i].length();
            }

            return sum;
        } else {
            return super.length();
        }
    }

    /**
     * returns the file type denoted by its file extension. The
     * extension is the String of those characters that follow the last 'dot'
     * (".") in the file name in lowercase. If no extension is present, null
     * is returned.
     *
     * @return DOCUMENT ME!
     */
    public String getFileExtension() {
        String fname = getName();
        int i = fname.lastIndexOf('.');

        if ((i > 0) && (i < (fname.length() - 1))) {
            return fname.substring(i + 1).toLowerCase();
        }

        return null;
    }

    /**
     * returns true only if this ExtendedFile is in the given dir (or
     * sub-dir)
     *
     * @param dir DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasParent(File dir) {
        File parent = getAbsoluteFile().getParentFile();

        if (parent == null) {
            return false;
        }

        if (parent.equals(dir.getAbsoluteFile())) {
            return true;
        }

        return new ExtendedFile(parent).hasParent(dir);
    }

    /**
     * returns the file as a relative reference from the given
     * directory. The given directory should be a parent of the file at hand;
     * if not, the absolute file path is returned.
     *
     * @param dir DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ExtendedFile getFileRelativeTo(File dir) {
        if (!hasParent(dir)) {
            return new ExtendedFile(getAbsolutePath());
        }

        String dirName = dir.getAbsolutePath();

        if (dirName.endsWith(pathSeparator)) {
            // making sure it also works with the root directory
            dirName = dirName.substring(0, dirName.length() - 1);
        }

        return new ExtendedFile(getAbsolutePath().substring(dirName.length() +
                1));
    }

    /**
     * assuming the current ExtendedFile is a directory, all
     * sub-directories are returned.
     *
     * @return DOCUMENT ME!
     */
    public File[] listSubDirs() {
        FileFilter filter = new FileFilter() {
                public boolean accept(File pathname) {
                    return (pathname.isDirectory()) ? true : false;
                }
            };

        return listFiles(filter);
    }

    /**
     * returns all files including those in subdirectories recursively
     *
     * @return DOCUMENT ME!
     */
    public ExtendedFile[] listFilesInTree() {
        ArrayList<ExtendedFile> list = new ArrayList<ExtendedFile>();
        File[] files = listFiles();

        if (files == null) {
            return null;
        }

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                list.addAll(Arrays.asList(
                        new ExtendedFile(files[i]).listFilesInTree()));
            }

            list.add(new ExtendedFile(files[i]));
        }

        return list.toArray(new ExtendedFile[list.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ExtendedFile[] listFilesInTree(FileFilter filter) {
        ExtendedFile[] all = listFilesInTree();

        if (filter == null) {
            return all;
        }

        if (all == null) {
            return null;
        }

        ArrayList<ExtendedFile> list = new ArrayList<ExtendedFile>();

        for (int i = 0; i < all.length; i++) {
            if (filter.accept(all[i])) {
                list.add(all[i]);
            }
        }

        return list.toArray(new ExtendedFile[list.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ExtendedFile[] listFilesInTree(FilenameFilter filter) {
        ExtendedFile[] all = listFilesInTree();
        ArrayList<ExtendedFile> list = new ArrayList<ExtendedFile>();

        for (int i = 0; i < all.length; i++) {
            if (filter.accept(this, all[i].getName())) {
                list.add(all[i]);
            }
        }

        return list.toArray(new ExtendedFile[list.size()]);
    }

    /**
     * copies this file to the given destination file. The last
     * modified time of the copied file will be set equal to the original
     * file. The read-only attribute is NOT applied to the copied file even if
     * the original file was read-only.
     *
     * @param destinationFile DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void copyTo(File destinationFile) throws IOException {
        FileInputStream in = null;
        ;

        FileOutputStream out = null;
        IOException exception = null;

        try {
            in = new FileInputStream(this);
            out = new FileOutputStream(destinationFile);
            IOUtils.transfer(in, out);
            destinationFile.setLastModified(lastModified());
        } catch (IOException ex) {
            exception = ex;
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
                if (exception != null) {
                    ex.initCause(exception);
                }

                exception = ex;
            } catch (NullPointerException ignore) {
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * copies this file to the given destination file while
     * searchPattern is replaced with replacePattern
     *
     * @param destinationFile DOCUMENT ME!
     * @param searchPattern DOCUMENT ME!
     * @param replacePattern DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void copyTo(File destinationFile, byte[] searchPattern,
        byte[] replacePattern) throws IOException {
        ReplaceInputStream in = null;
        ;

        FileOutputStream out = null;
        IOException exception = null;

        try {
            in = new ReplaceInputStream(new FileInputStream(this),
                    searchPattern, replacePattern);
            out = new FileOutputStream(destinationFile);
            IOUtils.transfer(in, out);
            destinationFile.setLastModified(lastModified());
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ignore) {
                if (exception != null) {
                    ignore.initCause(exception);
                }

                exception = ignore;
            } catch (NullPointerException ignore) {
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * returns true only if both files have the exact same content
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public boolean compareContent(File file) throws IOException {
        return compareContent(0, file, 0, 0);
    }

    /**
     * returns true only if both files have the exact same content
     * beginning at beginIndex and up to maxBytes number of bytes.
     *
     * @param beginIndexThisFile DOCUMENT ME!
     * @param otherFile DOCUMENT ME!
     * @param beginIndexOtherFile DOCUMENT ME!
     * @param maxBytes if 0, the entire content is compared to EOF
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public boolean compareContent(long beginIndexThisFile, File otherFile,
        long beginIndexOtherFile, long maxBytes) throws IOException {
        FileInputStream in1 = new FileInputStream(this);
        FileInputStream in2 = new FileInputStream(otherFile);
        in1.skip(beginIndexThisFile);
        in2.skip(beginIndexOtherFile);

        boolean eq = IOUtils.equals(in1, in2, maxBytes);
        in1.close();
        in2.close();

        return eq;
    }

    /**
     * returns a text describing as to how this file compares to the
     * given file
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public String fileCompareText(File file) throws IOException {
        return fileCompareText(0, file, 0, 0);
    }

    /**
     * returns a text describing as to how this file compares to the
     * given file
     *
     * @param beginIndexThisFile DOCUMENT ME!
     * @param otherFile DOCUMENT ME!
     * @param beginIndexOtherFile DOCUMENT ME!
     * @param maxBytes DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public String fileCompareText(long beginIndexThisFile, File otherFile,
        long beginIndexOtherFile, long maxBytes) throws IOException {
        String s = "comparing " + this + " to " + otherFile + ":" +
            StringUtils.lb;
        boolean compare = compareContent(beginIndexThisFile, otherFile,
                beginIndexOtherFile, maxBytes);
        s += "The contents are ";

        if (!compare) {
            s += "not ";
        }

        s += ("equal." + StringUtils.lb);

        if (otherFile.lastModified() == lastModified()) {
            s += ("Both files are equally old (" + new Date(lastModified()) +
            ")" + StringUtils.lb);
        } else {
            s += ("File " +
            ((otherFile.lastModified() > lastModified()) ? otherFile : this) +
            " is more recent (by " +
            Stopwatch.timeAsString(Math.abs(otherFile.lastModified() -
                    lastModified())) + ")." + StringUtils.lb);
        }

        if (!compare) {
            long diff = otherFile.length() - length();

            if (diff == 0) {
                s += ("Both files have the same size (" + length() + " bytes)");
            } else {
                s += ("File " + ((diff > 0) ? otherFile : this) +
                " is larger by " + Math.abs(diff) + " bytes (" +
                MiscellaneousUtils.asPercent(Math.abs(
                        (double) diff / (double) ((diff > 0) ? length()
                                                             : otherFile.length()))) +
                ").");
            }

            s += StringUtils.lb;

            // check first few different characters
            BufferedInputStream stream1 = new BufferedInputStream(new FileInputStream(
                        this));
            BufferedInputStream stream2 = new BufferedInputStream(new FileInputStream(
                        otherFile));
            long length = IOUtils.countEqualBytes(stream1, stream2);
            stream2.reset();
            stream2.reset();

            if ((length >= otherFile.length()) || (length >= length())) {
                s += ("Files are only different in size." + StringUtils.lb);
            } else {
                s += ("Difference starts at position " + length + "." +
                StringUtils.lb);
                stream1.reset();
                stream2.reset();

                BufferedReader reader1 = new BufferedReader(new InputStreamReader(
                            stream1));
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(
                            stream2));
                String line1 = reader1.readLine();
                String line2 = reader2.readLine();
                s += StringUtils.lb;
                s += ("first different trimmed line from " + this + ":" +
                StringUtils.lb);
                s += (clean(line1) + StringUtils.lb);
                s += ("first different trimmed line from " + otherFile + ":" +
                StringUtils.lb);
                s += (clean(line2) + StringUtils.lb);
            }
        }

        return s;
    }

    /**
     * used above in fileCompareText(File)
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static String clean(String o) {
        String s = "";

        for (int i = 0; (i < o.length()) && (s.length() < 80); i++) {
            char c = o.charAt(i);

            if (!Character.isISOControl(c)) {
                s += c;
            }
        }

        return s;
    }

    /**
     * compares this directory with the given one and prints results to
     * the given stream
     *
     * @param dir DOCUMENT ME!
     * @param ps DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void directoryCompare(File dir, PrintStream ps)
        throws IOException {
        directoryCompare(dir, ps, new Monitor());
    }

    /**
     * DOCUMENT ME!
     *
     * @param dir DOCUMENT ME!
     * @param ps DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void directoryCompare(File dir, PrintStream ps, Monitor monitor)
        throws IOException {
        if (!isDirectory()) {
            throw new IllegalArgumentException("not a directory: " + this);
        }

        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("not a directory: " + dir);
        }

        ExtendedFile dir1 = this;
        ExtendedFile dir2 = new ExtendedFile(dir);
        ExtendedFile[] files1 = dir1.listFilesInTree();

        for (int i = 0; i < files1.length; i++) {
            files1[i] = files1[i].getFileRelativeTo(dir1);
        }

        ps.println("number of files in " + dir1 + ": " + files1.length);

        ExtendedFile[] files2 = dir2.listFilesInTree();

        for (int i = 0; i < files2.length; i++) {
            files2[i] = files2[i].getFileRelativeTo(dir2);
        }

        ps.println("number of files in " + dir2 + ": " + files2.length);

        if (monitor.disabled()) {
            ps.println("action aborted by monitor");

            return;
        }

        ArrayList<ExtendedFile> list = null;

        if (files1.length == files2.length) {
            ps.println("files in each directory tree: " + files1.length);
            list = new ArrayList<ExtendedFile>(files1.length);
        } else {
            if (files1.length > files2.length) {
                ps.println(dir1 + " contains more files");
                list = new ArrayList<ExtendedFile>(files1.length);
            } else {
                ps.println(dir2 + " contains more files");
                list = new ArrayList<ExtendedFile>(files2.length);
            }
        }

        ps.println("\nfiles that only exist in " + dir1 + ":");

        if (monitor.disabled()) {
            ps.println("action aborted by monitor");

            return;
        }

        for (int i = 0; i < files1.length; i++) {
            if (!inArray(files1[i], files2)) {
                ps.println(files1[i]);
            }
        }

        ps.println("\nfiles that only exist in " + dir2 + ":");

        for (int i = 0; i < files2.length; i++) {
            if (!inArray(files2[i], files1)) {
                ps.println(files2[i]);
            }
        }

        if (monitor.disabled()) {
            ps.println("action aborted by monitor");

            return;
        }

        ps.println("\nfiles that are different:");

        for (int i = 0; i < files1.length; i++) {
            for (int j = 0; j < files2.length; j++) {
                if (monitor.disabled()) {
                    ps.println("action aborted by monitor");

                    return;
                }

                if (files1[i].equals(files2[j])) {
                    if (!new ExtendedFile(dir1, files1[i].getPath()).compareContent(
                                new ExtendedFile(dir2, files2[j].getPath()))) {
                        ps.println(files1[i]);
                    } else {
                        list.add(files1[i]);
                    }

                    continue;
                }
            }
        }

        ps.println("\nfiles that are the same:");

        for (int i = 0; i < list.size(); i++) {
            ps.println(list.get(i));
        }
    }

    /**
     * used by the method above
     *
     * @param obj DOCUMENT ME!
     * @param array DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean inArray(Object obj, Object[] array) {
        for (int i = 0; i < array.length; i++) {
            if (obj.equals(array[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Assuming the current ExtendedFile is a directory, this method
     * synchronizes it with the destination directory by writing all files
     * into the destination directory that exist in the origin directory.<p>The
     * procedure follows the follwing rules:
     *  <ul>
     *      <li>All files that only exist in the first directory,
     *      will be copied over</li>
     *      <li>If a file already exists at the destination, it
     *      will only be overwritten if it is more recent or if the length is
     *      different</li>
     *      <li>Files at the destination that do not exist at the
     *      origin will be deleted</li>
     *      <li>The above will be performed recursively for each
     *      subdirectory</li>
     *  </ul>
     *  Note that file attributes are ignored, i.e. if a file is
     * copied, the default attributes are kept and may be different from those
     * of the originating file. If a directory is to be removed in the
     * destination dir, the method makes a best effort to do so.</p>
     *
     * @param destinationDir DOCUMENT ME!
     *
     * @return array of files that could not be copied or deleted; if no errors
     *         occurred, this array would be empty
     *
     * @throws IOException if either the current ExtendedFile or the given
     *         destination are not directories
     */
    public ExtendedFile[] synchronizeDir(File destinationDir)
        throws IOException {
        return synchronizeDir(destinationDir, null, null, null);
    }

    /**
     * synchronizes only those files that are accepted by the filter
     *
     * @param destinationDir DOCUMENT ME!
     * @param filter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public ExtendedFile[] synchronizeDir(File destinationDir, FileFilter filter)
        throws IOException {
        return synchronizeDir(destinationDir, filter, null, null);
    }

    /**
     * This method allows for intermediate feedback through the logger;
     * otherwise it's the same as the other synchronizeDir() method.<p>This
     * method allows e.g. GUIs to display status messages by implementing a
     * special LogWriter. <br>
     * The given logger may be null, otherwise its usage is as follows:
     *  <ul>
     *      <li>Every processed directory produces a level
     *      Level.FINE message naming the given directory</li>
     *      <li>Every copied/deleted file produces a level
     *      Level.FINER message naming the file</li>
     *      <li>Every unsuccessfull copy/delete operation produces
     *      a level Level.WARNING message naming the file/directory and
     *      containing the IOException as a parameter</li>
     *  </ul>
     *  </p>
     *
     * @param destinationDir DOCUMENT ME!
     * @param logger DOCUMENT ME!
     *
     * @return array of files that could not be copied or deleted; if no errors
     *         occurred, this array would be empty
     *
     * @throws IOException DOCUMENT ME!
     *
     * @see #synchronizeDir(File)
     * @see org.jscience.util.Backup
     */
    public ExtendedFile[] synchronizeDir(File destinationDir, Logger logger)
        throws IOException {
        return synchronizeDir(destinationDir, null, logger, null);
    }

    /**
     * This method allows for intermediate feedback and interactive
     * stopping; otherwise it's the same as the other synchronizeDir() method.<p>This
     * method allows monitoring threads to abort the synchronization by
     * disabling the given monitor during processing. The monitor/logger may
     * be null; monitor checks occur for each directory and after each copy
     * operation - right after the logger was called to log the operation.</p>
     *
     * @param destinationDir DOCUMENT ME!
     * @param logger DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @return array of files that could not be copied or deleted; if no errors
     *         occurred, this array would be empty
     *
     * @throws IOException DOCUMENT ME!
     *
     * @see #synchronizeDir(File,Logger)
     * @see org.jscience.util.Backup
     */
    public ExtendedFile[] synchronizeDir(File destinationDir, Logger logger,
        Monitor monitor) throws IOException {
        return synchronizeDir(destinationDir, null, logger, monitor, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param destinationDir DOCUMENT ME!
     * @param filter DOCUMENT ME!
     * @param logger DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ExtendedFile[] synchronizeDir(File destinationDir,
        FileFilter filter, Logger logger, Monitor monitor)
        throws IOException {
        if (!isDirectory() || !destinationDir.isDirectory()) {
            String s = "both, this ExtendedFile and the given parameters must be directories; not a directory: ";

            if (!isDirectory()) {
                s += this.toString();
            } else {
                s += destinationDir.toString();
            }

            throw new IllegalArgumentException(s);
        }

        ArrayList<ExtendedFile> errors = new ArrayList<ExtendedFile>();

        return synchronizeDir(destinationDir, filter, logger, monitor, errors);
    }

    /**
     * DOCUMENT ME!
     *
     * @param destinationDir DOCUMENT ME!
     * @param filter DOCUMENT ME!
     * @param logger DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     * @param errors DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private synchronized ExtendedFile[] synchronizeDir(File destinationDir,
        FileFilter filter, Logger logger, Monitor monitor,
        ArrayList<ExtendedFile> errors) throws IOException {
        File[] d;

        if (filter == null) {
            d = destinationDir.listFiles();
        } else {
            d = destinationDir.listFiles(filter);
        }

        HashMap<String, File> dmap = new HashMap<String, File>();

        for (int i = 0; i < d.length; i++) {
            dmap.put(d[i].getName(), d[i]);
        }

        ArrayList<ExtendedFile> newDirs = new ArrayList<ExtendedFile>();
        File[] o;

        if (filter == null) {
            o = listFiles();
        } else {
            o = listFiles(filter);
        }

        if (logger != null) {
            logger.log(this, Level.FINE,
                "processing " + this + " (contains " + o.length + " files)");
        }

        if ((monitor != null) && monitor.disabled()) {
            if (logger != null) {
                logger.log(this, Level.INFO, "operation aborted by monitor");
            }

            return (ExtendedFile[]) errors.toArray(new ExtendedFile[errors.size()]);
        }

        for (int i = 0; i < o.length; i++) {
            if (o[i].isDirectory()) {
                newDirs.add(new ExtendedFile(o[i]));

                String name = o[i].getName();
                dmap.remove(name);

                File tmp = new File(destinationDir, name);

                if (tmp.exists() && !tmp.isDirectory()) {
                    if (tmp.delete()) {
                        if (logger != null) {
                            logger.log(this, Level.FINER, "deleted file: " +
                                tmp);
                        }
                    } else {
                        errors.add(new ExtendedFile(tmp));

                        if (logger != null) {
                            logger.log(this, Level.WARNING,
                                "could not delete file: " + tmp);
                        }
                    }
                }
            } else {
                String name = o[i].getName();

                if (dmap.containsKey(name)) {
                    File f = (File) dmap.get(name);

                    if ((f.lastModified() >= o[i].lastModified()) &&
                            (f.length() == o[i].length())) {
                        dmap.remove(name);

                        continue;
                    }
                }

                try {
                    new ExtendedFile(o[i]).copyTo(new File(destinationDir, name));

                    if (logger != null) {
                        logger.log(this, Level.FINER, "copied file: " + o[i]);
                    }
                } catch (IOException e) {
                    errors.add(new ExtendedFile(o[i]));

                    if (logger != null) {
                        logger.log(this, Level.WARNING,
                            "could not copy file: " + o[i], new Object[] { e });
                    }
                }

                dmap.remove(name);
            }

            if ((monitor != null) && monitor.disabled()) {
                if (logger != null) {
                    logger.log(this, Level.INFO, "operation aborted by monitor");
                }

                return (ExtendedFile[]) errors.toArray(new ExtendedFile[errors.size()]);
            }
        }

        Iterator i = dmap.keySet().iterator();

        while (i.hasNext()) {
            ExtendedFile tbd = new ExtendedFile((File) dmap.get(i.next()));

            if (tbd.isDirectory()) {
                if (tbd.removeTree(true)) {
                    if (logger != null) {
                        logger.log(this, Level.FINER,
                            "deleted directory: " + tbd);
                    }
                } else {
                    errors.add(tbd);

                    if (logger != null) {
                        logger.log(this, Level.WARNING,
                            "could not delete directory: " + tbd);
                    }
                }
            } else {
                if (tbd.delete()) {
                    if (logger != null) {
                        logger.log(this, Level.FINER, "deleted file: " + tbd);
                    }
                } else {
                    errors.add(tbd);

                    if (logger != null) {
                        logger.log(this, Level.WARNING,
                            "could not delete file: " + tbd);
                    }
                }
            }
        }

        i = newDirs.iterator();

        while (i.hasNext()) {
            ExtendedFile nextdir = (ExtendedFile) i.next();
            ExtendedFile ddir = new ExtendedFile(destinationDir,
                    nextdir.getName());

            if (!ddir.exists()) {
                if (ddir.mkdir()) {
                    if (logger != null) {
                        logger.log(this, Level.FINER, "created dir: " + ddir);
                    }
                } else {
                    errors.add(ddir);

                    if (logger != null) {
                        logger.log(this, Level.WARNING,
                            "could not create dir: " + ddir);
                    }
                }
            }

            nextdir.synchronizeDir(ddir, filter, logger, monitor, errors);
        }

        return (ExtendedFile[]) errors.toArray(new ExtendedFile[errors.size()]);
    }

    /**
     * If the ExtendedFile at hand is a directory, this method attempts
     * to delete the entire subtree; therefore use it with caution!<p>If
     * the ExtendedFile is not a directory, false is returned immediately
     * (nothing gets deleted). If it is a directory but it is not empty, it
     * will attempt to first delete all content recursively and then again the
     * top directory. The method may return false as soon as any subdirectory
     * could not be removed, i.e. not necessarily everything that could be
     * deleted within the tree may be deleted if the method returns false in
     * the case of a directory; still: all files handled before will be
     * deleted.</p>
     *
     * @param includingThisRoot if true, this directory is deleted along with
     *        the sub-tree
     *
     * @return true only if ExtendedFile is a directory and the entire subtree
     *         was successfully removed
     */
    public boolean removeTree(boolean includingThisRoot) {
        if (!isDirectory()) {
            return false;
        }

        File[] f = listFiles();

        for (int i = 0; i < f.length; i++) {
            if (f[i].isDirectory()) {
                if (!new ExtendedFile(f[i]).removeTree(true)) {
                    return false;
                }
            } else {
                if (!f[i].delete()) {
                    return false;
                }
            }
        }

        return includingThisRoot ? delete() : true;
    }

    /**
     * returns the index within the content of this file of the first
     * occurrence of the specified pattern; -1 is returned if pattern is not
     * found. This method is to be used with smaller/medium sized text files;
     * the entire file must fit into memory.
     *
     * @param pattern DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws OutOfMemoryError if the file doesn't fit into memory
     */
    public int indexOf(String pattern) throws IOException, OutOfMemoryError {
        return getContentAsString().indexOf(pattern);
    }

    /**
     * returns the index of the beginning of the given pattern within
     * the file (beginning the search at beginIndex) or -1
     *
     * @param pattern DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public long indexOf(byte[] pattern) throws IOException {
        return indexOf(pattern, 0);
    }

    /**
     * returns the index of the beginning of the given pattern within
     * the file (beginning the search at beginIndex) or -1
     *
     * @param pattern DOCUMENT ME!
     * @param beginIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public long indexOf(byte[] pattern, int beginIndex)
        throws IOException {
        /**
         * DOCUMENT ME!
         *
         * @author $author$
         * @version $Revision: 1.3 $
          */
        class Check {
            /**
             * DOCUMENT ME!
             */
            boolean test = false;
        }
        ;

        final Check found = new Check();
        InterceptInputStream.Handler handler = new InterceptInputStream.Handler() {
                public void patternFound(InterceptInputStream stream,
                    byte[] pattern) {
                    found.test = true;
                }
            };

        InterceptInputStream stream = new InterceptInputStream(new FileInputStream(
                    this), pattern);
        stream.addHandler(handler);
        stream.skip(beginIndex);

        int n = stream.read();

        while (!found.test && (n >= 0))
            n = stream.read();

        return found.test ? (stream.readSoFar() - 1) : (-1);
    }

    /**
     * returns how many times the given byte pattern occurs within the
     * file
     *
     * @param pattern DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public int count(byte[] pattern) throws IOException {
        return IOUtils.count(new FileInputStream(this), pattern);
    }

    /**
     * searches for the first pattern and replaces all occurrences with
     * the second pattern (no regular expressions used). This method is to be
     * used with smaller/medium sized text files; the entire file must fit
     * into memory (twice, actually).
     *
     * @param searchPattern DOCUMENT ME!
     * @param replacePattern DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws OutOfMemoryError if the file doesn't fit into memory
     */
    public void replace(String searchPattern, String replacePattern)
        throws IOException, OutOfMemoryError {
        String s = getContentAsString();
        s = StringUtils.replaceAll(s, searchPattern, replacePattern);

        byte[] bytes = s.getBytes();
        IOException exception = null;
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(this);
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                out.close();
            } catch (IOException ignore) {
                if (exception != null) {
                    ignore.initCause(exception);
                }

                exception = ignore;
            } catch (NullPointerException ignore) {
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * searches for the first pattern and replaces all occurrences with
     * the second pattern. Uses a temp file for the replacement; data is then
     * copied back to the original file
     *
     * @param searchPattern DOCUMENT ME!
     * @param replacePattern DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void replace(byte[] searchPattern, byte[] replacePattern)
        throws IOException {
        if (!canWrite()) {
            throw new IOException("cannot write to file: " + this.toString());
        }

        File tmpFile = createTempFile("myfilecopy", "tmp");

        //tmpFile.deleteOnExit();
        ReplaceInputStream in = new ReplaceInputStream(new FileInputStream(this),
                searchPattern, replacePattern);
        IOUtils.transfer(in, new FileOutputStream(tmpFile));
        new ExtendedFile(tmpFile).copyTo(this);
        tmpFile.delete();
    }

    /**
     * returns the entire file content as a String (using default
     * encoding). This method is better to be used with smaller/medium sized
     * text files; the entire file must fit into memory.
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws OutOfMemoryError if the file doesn't fit into memory
     *
     * @see #getContentAsBytes()
     */
    public String getContentAsString() throws IOException, OutOfMemoryError {
        return new String(getContentAsBytes());
    }

    /**
     * returns the entire file content as a String (using default
     * encoding). This method is better to be used with smaller/medium sized
     * text files; the entire file must fit into memory.
     *
     * @param charsetName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws OutOfMemoryError if the file doesn't fit into memory
     *
     * @see #getContentAsBytes()
     */
    public String getContentAsString(String charsetName)
        throws IOException, OutOfMemoryError {
        return new String(getContentAsBytes(), charsetName);
    }

    /**
     * returns the entire file content as a byte array. This method is
     * better to be used with smaller/medium sized binary files; the entire
     * file must fit into memory.
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws OutOfMemoryError if the file doesn't fit into memory
     *
     * @see #getContentAsString()
     */
    public byte[] getContentAsBytes() throws IOException, OutOfMemoryError {
        IOException exception = null;
        FileInputStream in = null;

        try {
            in = new FileInputStream(this);

            byte[] bytes = new byte[(int) length()];
            in.read(bytes);
            in.close();

            return bytes;
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
                if (exception != null) {
                    ignore.initCause(exception);
                }

                exception = ignore;
            } catch (NullPointerException ignore) {
            }
        }

        throw exception;
    }

    /**
     * appends the content of the given file to this file
     *
     * @param file DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void append(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileOutputStream out = new FileOutputStream(this, true);
        IOUtils.transfer(in, out);
    }

    /**
     * writes the given text to the file and flushes; if append is
     * false, all previous content will be overwritten. Suitable for text
     * files only
     *
     * @see #writeObject(Object,boolean)
     */
    public void writeText(String text, boolean append)
        throws IOException {
        IOException exception = null;
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(this, append);
            fileWriter.write(text);
            fileWriter.flush();
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ignore) {
                if (exception != null) {
                    ignore.initCause(exception);
                }

                exception = ignore;
            } catch (NullPointerException ignore) {
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * appends a platform specific linebreak into a text file
     *
     * @throws IOException DOCUMENT ME!
     */
    public void writeln() throws IOException {
        writeText(StringUtils.lb, true);
    }

    /**
     * writes the given bytes directly to the file and flushes; if
     * append is false, all previous content will be overwritten. Suitable for
     * binary files.
     *
     * @see #writeObject(Object,boolean)
     */
    public void writeBytes(byte[] bytes, boolean append)
        throws IOException {
        IOException exception = null;
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(this, append);
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                out.close();
            } catch (IOException ignore) {
                if (exception != null) {
                    ignore.initCause(exception);
                }

                exception = ignore;
            } catch (NullPointerException ignore) {
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * serializes the given object, writes it to the file and flushes.
     * Use with binary files only. Note: appending different objects to an
     * existing file may result into ClassCastException in ObjectInputStream
     * when reading back from the file; one solution is to simply write byte
     * arrays as objects that contain the already serialized object.
     *
     * @param obj DOCUMENT ME!
     * @param append DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     *
     * @see #writeText(String,boolean)
     * @see #writeBytes(byte[],boolean)
     * @see IOUtils#serialize(java.lang.Object)
     */
    public void writeObject(Object obj, boolean append)
        throws IOException {
        IOException exception = null;
        ObjectOutputStream out = null;

        try {
            if (append && exists() && (length() > 0)) {
                out = new ObjectOutputStream(new FileOutputStream(this, true)) {
                            protected void writeStreamHeader()
                                throws IOException {
                                // nothin', as the header had been written before
                            }
                        };
            } else {
                out = new ObjectOutputStream(new FileOutputStream(this));
            }

            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                out.close();
            } catch (IOException ignore) {
                if (exception != null) {
                    ignore.initCause(exception);
                }

                exception = ignore;
            } catch (NullPointerException ignore) {
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * prints the stack trace of the given Throwable
     *
     * @param t DOCUMENT ME!
     * @param append DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void writeStackTrace(Throwable t, boolean append)
        throws IOException {
        writeStackTrace(null, t, append);
    }

    /**
     * prints the stack trace of the given Throwable to a PrintWriter.
     *
     * @param t DOCUMENT ME!
     * @param e DOCUMENT ME!
     * @param append DOCUMENT ME!
     *
     * @return true only if the stack was successfully written.
     *
     * @throws FileNotFoundException DOCUMENT ME!
     */
    public boolean writeStackTrace(Thread t, Throwable e, boolean append)
        throws FileNotFoundException {
        long time = System.currentTimeMillis();
        PrintWriter writer = new PrintWriter(new FileOutputStream(this, append));
        //FileWriter writer = new FileWriter(this, append);
        writer.println();
        writer.print(e.getClass().getName());
        writer.print(" thrown at " + new Date(time));
        writer.println(" in " + t + ":");
        e.printStackTrace(writer);
        writer.flush();
        writer.close();

        return !writer.checkError();
    }

    /**
     * writes a gzip compressed file that can be uncompressed to its
     * original form using <code>uncompressTo(File)</code>.
     *
     * @see #uncompressTo(File)
     */
    public void compressTo(File file) throws IOException {
        GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(file,
                    false));
        FileInputStream in = new FileInputStream(this);
        IOUtils.transfer(in, out);
    }

    /**
     * restores a gzip compressed file written with
     * <code>compressTo(File)</code>.
     *
     * @see #compressTo(File)
     */
    public void uncompressTo(File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file, false);
        GZIPInputStream in = new GZIPInputStream(new FileInputStream(this));
        IOUtils.transfer(in, out);
    }

    /**
     * compresses the file using gzip to a file named by adding the
     * type gz to the original name.
     *
     * @param deleteOriginal if true, the uncompressed original file will be
     *        deleted
     *
     * @return the compressed file
     *
     * @throws IOException DOCUMENT ME!
     */
    public ExtendedFile compress(boolean deleteOriginal)
        throws IOException {
        ExtendedFile file = new ExtendedFile(getPath() + ".gz");
        compressTo(file);

        if (deleteOriginal) {
            delete();
        }

        return file;
    }

    /**
     * calls <code>loadObject(false)</code>
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public Object loadObject() throws IOException, ClassNotFoundException {
        return loadObject(false);
    }

    /**
     * loads a single object from the file into memory and returns it.
     *
     * @param useDeserialization if true, this method attepts deserialization
     *        on byte arrays
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public Object loadObject(boolean useDeserialization)
        throws IOException, ClassNotFoundException {
        Exception exception = null;
        ObjectInputStream in = null;

        try {
            in = new ObjectInputStream(new FileInputStream(this));

            Object obj = in.readObject();

            if ((useDeserialization) && (obj instanceof byte[])) {
                try {
                    obj = IOUtils.deserialize((byte[]) obj);
                } catch (ClassNotFoundException ignore) {
                } catch (IOException ignore) {
                }
            }

            in.close();

            return obj;
        } catch (IOException e) {
            exception = e;
        } catch (ClassNotFoundException e) {
            exception = e;
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
                if (exception != null) {
                    ignore.initCause(exception);
                }

                exception = ignore;
            } catch (NullPointerException ignore) {
            }
        }

        if (exception instanceof IOException) {
            throw (IOException) exception;
        } else {
            throw (ClassNotFoundException) exception;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public Properties loadProperties() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(this));

        return props;
    }

    /**
     * DOCUMENT ME!
     *
     * @param props DOCUMENT ME!
     * @param comment DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void storeProperties(Properties props, String comment)
        throws IOException {
        FileOutputStream out = new FileOutputStream(this);
        props.store(out, comment);
    }

    /**
     * the file must fit into memory
     *
     * @param key DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws OutOfMemoryError DOCUMENT ME!
     */
    public void encrypt(SynchronousKey key)
        throws IOException, OutOfMemoryError {
        writeBytes(key.encode(getContentAsBytes()), false);
    }

    /**
     * the file must fit into memory
     *
     * @param key DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws OutOfMemoryError DOCUMENT ME!
     */
    public void decrypt(SynchronousKey key)
        throws IOException, OutOfMemoryError {
        writeBytes(key.decode(getContentAsBytes()), false);
    }

    /**
     * returns an Enumeration over the objects contained in this binary
     * file with serialized objects written with an ObjectOutputStream
     *
     * @return DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     *
     * @see #writeObject(Object,boolean)
     * @see ObjectEnumerator
     */
    public Enumeration objectEnumerator() throws FileNotFoundException {
        return new ObjectEnumerator(this, false);
    }

    /**
     * returns an Enumeration over the objects contained in this binary
     * file with serialized objects written with an ObjectOutputStream.
     *
     * @param useDeserialization specifies whether - in case the object to be
     *        returned is a byte[] - automatic deserialization should be used.
     *        If the deserialization fails, a byte[] is always returned if
     *        present.
     *
     * @return DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     *
     * @see #writeObject(Object,boolean)
     * @see IOUtils#deserialize(byte[])
     * @see ObjectEnumerator
     */
    public Enumeration objectEnumerator(boolean useDeserialization)
        throws FileNotFoundException {
        return new ObjectEnumerator(this, useDeserialization);
    }

    /**
     * convenience method
     *
     * @see ObjectEnumerator
     */
    public Enumeration objectEnumerator(boolean useDeserialization,
        Logger exceptionLogger) throws FileNotFoundException {
        return new ObjectEnumerator(this, useDeserialization, exceptionLogger);
    }

    /**
     * zips either this file this directory three to the given file
     *
     * @param file DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void zipTo(File file) throws IOException {
        zipTo(file, null, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @param comment DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void zipTo(File file, String comment, Monitor monitor)
        throws IOException {
        zipTo(file, null, comment, monitor);
    }

    /**
     * only the file parameter must not be null
     *
     * @param file DOCUMENT ME!
     * @param filter DOCUMENT ME!
     * @param comment DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void zipTo(File file, FileFilter filter, String comment,
        Monitor monitor) throws IOException {
        if (!exists()) {
            throw new IllegalArgumentException(
                "this file/directory doesn't exist and therefore can't be zipped");
        }

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));

        if (comment != null) {
            out.setComment(comment);
        }

        out.setLevel(9);

        if (isFile()) {
            if (monitor != null) {
                monitor.setMin(0);
                monitor.setMax(0);
                monitor.setMessage("zipping: " + getName());
            }

            out.putNextEntry(new ZipEntry(getName()));
            IOUtils.transfer(new FileInputStream(this), out, false);
            out.closeEntry();
        } else {
            ExtendedFile[] list = listFilesInTree();

            if (monitor != null) {
                monitor.setMin(0);
                monitor.setMax(list.length);
            }

            for (ExtendedFile f : list) {
                File r = f.getFileRelativeTo(this);

                if ((filter == null) || (filter.accept(f))) {
                    if (monitor != null) {
                        monitor.setMessage("zipping: " + r);
                        monitor.increment();

                        if (monitor.disabled()) {
                            break;
                        }
                    }

                    out.putNextEntry(new ZipEntry(r.toString()));
                    IOUtils.transfer(new FileInputStream(f), out, false);
                    out.closeEntry();
                } else {
                    if (monitor != null) {
                        monitor.setMessage("skipping: " + r);
                        monitor.increment();

                        if (monitor.disabled()) {
                            break;
                        }
                    }
                }
            }
        }

        out.finish();
        out.close();
    }

    /**
     * calls <code>unzipTo(directory, true)</code>
     *
     * @param directory DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void unzipTo(File directory) throws IOException {
        unzipTo(directory, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param directory DOCUMENT ME!
     * @param overwriteExistingFiles DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void unzipTo(File directory, boolean overwriteExistingFiles)
        throws IOException {
        unzipTo(directory, true, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param directory DOCUMENT ME!
     * @param overwriteExistingFiles DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void unzipTo(File directory, final boolean overwriteExistingFiles,
        Monitor monitor) throws IOException {
        FileFilter filter = new FileFilter() {
                public boolean accept(File file) {
                    if (overwriteExistingFiles) {
                        return true;
                    }

                    return !file.exists();
                }
            };

        unzipTo(directory, filter, monitor);
    }

    /**
     * allows to react individually per file on whether to unzip it or
     * nor
     *
     * @param directory DOCUMENT ME!
     * @param filter DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void unzipTo(File directory, FileFilter filter, Monitor monitor)
        throws IOException {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("not a directory: " + directory);
        }

        ZipFile zipFile = new ZipFile(this);
        List<?extends ZipEntry> list = AbstractIterator.list(zipFile.entries());

        if (monitor != null) {
            monitor.setMin(0);
            monitor.setMax(list.size());
        }

        for (ZipEntry entry : list) {
            if (monitor != null) {
                monitor.increment();
                monitor.setMessage("unzipping " + entry.getName());

                if (monitor.disabled()) {
                    break;
                }
            }

            File target = new File(directory, entry.getName());

            if (entry.isDirectory()) {
                target.mkdirs();
            } else {
                if (!filter.accept(target)) {
                    continue;
                }

                target.getParentFile().mkdirs();

                InputStream in = zipFile.getInputStream(entry);
                FileOutputStream out = new FileOutputStream(target);
                IOUtils.transfer(in, out);
            }
        }

        zipFile.close();
    }

    /**
     * returns the different file extensions that occur in the given
     * array
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String[] getFileTypes(File[] file) {
        HashSet<String> set = new HashSet<String>();

        for (int i = 0; i < file.length; i++) {
            set.add(new ExtendedFile(file[i]).getFileExtension());
        }

        return set.toArray(new String[set.size()]);
    }
}
