/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.io;

import java.io.File;
import java.io.IOException;

import java.util.HashSet;
import java.util.Iterator;

import javax.swing.filechooser.FileFilter;


/**
 * ExtensionFileFilter is a flexible multi-purpose FileFilter.
 * ExtensionFileFilter implements java.io.FileFilter as well as extending
 * javax.swing.filechooser.FileFilter, so it can be used for GUIs as well as
 * for other more general purposes.<p>ExtensionFileFilter keeps a default
 * type, which can be used when saving a file when no file type was given (in
 * the case of using ExtensionFileFilter as a
 * javax.swing.filechooser.FileFilter). If a ExtensionFileFilter is
 * constructed with the argument null for the default type, all file types
 * will be accepted regardless of any additional added types; pattern matching
 * will still take place.</p>
 *  <p>As for the pattern matching capabilities: <br>
 * By default, no pattern matching takes place until you explicitly add
 * include or exclude patterns. If any patterns are present (i.e.
 * getIncludePattern() or getExcludePattern() return an array with more than
 * zero elements), the file is loaded as a String into memory and is checked
 * against the given patterns. Note, therefore, that include/exclude patterns
 * should only be used for small/medium sized files; if a file is too big to
 * fit into memory to perform the pattern search, <code>accept()</code> will
 * return <code>false</code>.<br>
 * If include and/or exclude patterns are set, a file is accepted by this
 * MyFilter only if all include patterns are found AND all exclude patterns
 * are not found in the file. If any patterns are set and - during pattern
 * matching - an IOException is caught, the file will not be accepted.<br>
 * Note that include and/or exclude patterns should only be used if all files
 * that may run through the accept() method fit into memory.</p>
 *
 * @author Holger Antelmann
 */

//also look at http://jakarta.apache.org/commons/io/ for more filters
public class ExtensionFileFilter extends FileFilter implements java.io.FileFilter //, Serializable
 {
    //static final long serialVersionUID = -8589706484160727539L;
    /**
     * DOCUMENT ME!
     */
    String defaultType;

    /**
     * DOCUMENT ME!
     */
    String description;

    /**
     * DOCUMENT ME!
     */
    HashSet<String> includePattern = new HashSet<String>(3);

    /**
     * DOCUMENT ME!
     */
    HashSet<String> excludePattern = new HashSet<String>(3);

    /**
     * DOCUMENT ME!
     */
    HashSet<String> moreTypes = new HashSet<String>(3);

/**
     * provides a default filter for text files (*.txt)
     */
    public ExtensionFileFilter() {
        this("txt", "Text files (*.txt)");
    }

    /**
     * Creates a new ExtensionFileFilter object.
     *
     * @param defaultExtensionType DOCUMENT ME!
     */
    public ExtensionFileFilter(String defaultExtensionType) {
        this(defaultExtensionType, defaultExtensionType + " files");
    }

/**
     * if the defaultType is null, all files are initially accepted
     *
     * @param defaultType a string specifying the default type (example: "txt");
     *                    the string will be converted to lowercase
     * @param description a string with a description (example: "Text files (*.txt)")
     */
    public ExtensionFileFilter(String defaultType, String description) {
        this.defaultType = (defaultType == null) ? null
                                                 : defaultType.toLowerCase();
        this.description = description;
    }

    /**
     * allows to add additional file types to be accepted by this
     * filter. Note that if the default type is null, added types have no
     * effect as all file types are then accepted (unless restricted by
     * patterns).
     *
     * @see #getDefaultType()
     */
    public synchronized boolean addType(String type) {
        //if (type == null) throw new IllegalArgumentException("null type not allowed");
        return moreTypes.add(type);
    }

    /**
     * removes previously added type. This doesn't ever affect the
     * default type. Note that if the default type is null, added types have
     * no effect as all file types are then accepted (unless restricted by
     * patterns).
     *
     * @see #getDefaultType()
     */
    public synchronized boolean removeType(String type) {
        return moreTypes.remove(type);
    }

    /**
     * returns file types other than the default type if they were
     * added through <code>addType(String)</code>. Note that if the default
     * type is null, added types have no effect as all file types are then
     * accepted (unless restricted by patterns).
     *
     * @see #getDefaultType()
     */
    public synchronized String[] getAddedTypes() {
        if (moreTypes == null) {
            return null;
        }

        return moreTypes.toArray(new String[moreTypes.size()]);
    }

    /**
     * adds a pattern to be included in a file to be accepted
     *
     * @param pattern DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean addIncludePattern(String pattern) {
        return includePattern.add(pattern);
    }

    /**
     * adds a pattern to be excluded in a file to be accepted
     *
     * @param pattern DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean addExcludePattern(String pattern) {
        return excludePattern.add(pattern);
    }

    /**
     * removes a pattern to be included in a file to be accepted
     *
     * @param pattern DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean removeIncludePattern(String pattern) {
        return includePattern.remove(pattern);
    }

    /**
     * removes a pattern to be included in a file to be accepted
     *
     * @param pattern DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean removeExcludePattern(String pattern) {
        return excludePattern.remove(pattern);
    }

    /**
     * returns all include patterns to be checked (empty array returned
     * if none)
     *
     * @return DOCUMENT ME!
     */
    public synchronized String[] getIncludePattern() {
        return includePattern.toArray(new String[includePattern.size()]);
    }

    /**
     * returns all exclude patterns to be checked (empty array returned
     * if none)
     *
     * @return DOCUMENT ME!
     */
    public synchronized String[] getExcludePattern() {
        return excludePattern.toArray(new String[excludePattern.size()]);
    }

    /**
     * removes all include and exclude patterns and ensures therefore
     * that the accept() method doesn't have to load the content of a file
     */
    public synchronized void removeAllPattern() {
        includePattern.clear();
        excludePattern.clear();
    }

    /**
     * checks file type and include/exclude pattern if applicable.
     * Directories are always accepted.
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        if (defaultType == null) {
            return checkPattern(f);
        } else {
            String ext = getFileType(f);

            if (ext != null) {
                if (ext.equals(defaultType)) {
                    return checkPattern(f);
                }

                Iterator it = moreTypes.iterator();

                while (it.hasNext()) {
                    if (ext.equals(it.next())) {
                        return checkPattern(f);
                    }
                }
            }

            return false;
        }
    }

    /**
     * called by accept() this reduntant implementation seems necessary
     * to avoid stupid InterruptedExceptions in the file loader thread from
     * javax.swing, when calling <code>new
     * ExtendedFile(f).getFileType()</code> from accept(), which would be the
     * preferred way #see ExtendedFile#getFileExtension()
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String getFileType(File f) {
        String fname = f.getName();
        int i = fname.lastIndexOf('.');

        if ((i > 0) && (i < (fname.length() - 1))) {
            return fname.substring(i + 1).toLowerCase();
        }

        return null;
    }

    /**
     * called by accept()
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean checkPattern(File f) {
        try {
            if (includePattern.isEmpty()) {
                return checkExclude(f, null);
            } else {
                String content = new ExtendedFile(f).getContentAsString();
                Iterator i = includePattern.iterator();

                while (i.hasNext()) {
                    if (content.indexOf((String) i.next()) < 0) {
                        return false;
                    }
                }

                return checkExclude(f, content);
            }
        } catch (IOException e) {
            // don't accept files where the patterns could not be determined
            return false;
        } catch (OutOfMemoryError e) {
            // don't accept files that are too big to check for the patterns
            return false;
        }
    }

    /**
     * called by checkPattern()
     *
     * @param f DOCUMENT ME!
     * @param content DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    boolean checkExclude(File f, String content) throws IOException {
        if (excludePattern.isEmpty()) {
            return true;
        } else {
            if (content == null) {
                content = new ExtendedFile(f).getContentAsString();
            }

            Iterator i = excludePattern.iterator();

            while (i.hasNext()) {
                if (content.indexOf((String) i.next()) > -1) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * returns the default type for this filter
     *
     * @return DOCUMENT ME!
     */
    public String getDefaultType() {
        return defaultType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = super.toString();
        s += (" (description: " + description);
        s += ("; default type: " + defaultType);
        s += "; other types: ";

        if (moreTypes.isEmpty()) {
            s += " none";
        } else {
            Iterator i = moreTypes.iterator();

            while (i.hasNext()) {
                s += (i.next() + ", ");
            }

            s = s.substring(0, s.length() - 2);
        }

        s += "; include pattern: ";

        if (includePattern.isEmpty()) {
            s += " none";
        } else {
            Iterator i = includePattern.iterator();

            while (i.hasNext()) {
                s += (i.next() + ", ");
            }

            s = s.substring(0, s.length() - 2);
        }

        s += "; exclude pattern: ";

        if (excludePattern.isEmpty()) {
            s += " none";
        } else {
            Iterator i = excludePattern.iterator();

            while (i.hasNext()) {
                s += (i.next() + ", ");
            }

            s = s.substring(0, s.length() - 2);
        }

        s += ")";

        return s;
    }
}
