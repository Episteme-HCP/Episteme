/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.io;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.NoSuchElementException;


/**
 * ArgumentParser is a helper class that parses arguments in results
 * according to CDDB Protocol level 2. It works similarly to a
 * StringTokenizer, but it properly handles arguments enclosed with quotes,
 * i.e. delimiters are not recognized if the occurence is within a quoted
 * section.
 *
 * @author Holger Antelmann
 */
public class ArgumentParser implements Serializable {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 8411263038547003882L;

    /**
     * DOCUMENT ME!
     */
    String line;

    /** protected to allow custom delimiters for subclasses */
    protected String delimiter = " \t\n\r\f";

    /**
     * Creates a new ArgumentParser object.
     *
     * @param line DOCUMENT ME!
     */
    public ArgumentParser(String line) {
        this(line, 0);
    }

    /**
     * Creates a new ArgumentParser object.
     *
     * @param line DOCUMENT ME!
     * @param delimiter DOCUMENT ME!
     */
    public ArgumentParser(String line, String delimiter) {
        this.line = line;
        this.delimiter = delimiter;
    }

    /**
     * Creates a new ArgumentParser object.
     *
     * @param line DOCUMENT ME!
     * @param startingPosition DOCUMENT ME!
     */
    public ArgumentParser(String line, int startingPosition) {
        this.line = line.substring(startingPosition);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String remainder() {
        while (hasMoreArguments() && isDelimiter(line.charAt(0)))
            line = line.substring(1);

        String rest = line;
        line = "";

        return rest;
    }

    /**
     * DOCUMENT ME!
     *
     * @param line DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String[] getAll(String line) {
        ArgumentParser ap = new ArgumentParser(line);
        ArrayList<String> v = new ArrayList<String>();

        while (ap.hasMoreArguments()) {
            v.add(ap.nextArgument());
        }

        return (String[]) v.toArray(new String[v.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param line DOCUMENT ME!
     * @param delimiter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String[] getAll(String line, String delimiter) {
        ArgumentParser ap = new ArgumentParser(line, delimiter);
        ArrayList<String> v = new ArrayList<String>();

        while (ap.hasMoreArguments()) {
            v.add(ap.nextArgument());
        }

        return (String[]) v.toArray(new String[v.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasMoreArguments() {
        return (line.length() > 0) ? true : false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public String nextArgument() throws NoSuchElementException {
        if (!hasMoreArguments()) {
            throw new NoSuchElementException();
        }

        // strip slack on the beginning
        while (hasMoreArguments() && isDelimiter(line.charAt(0)))
            line = line.substring(1);

        if (line.length() < 1) {
            return "";
        }

        int pos = -1;
        String arg = null;

        if (line.charAt(0) == '\"') {
            // handle quoted argument
            pos = findNextQuote(1);
            arg = line.substring(1, pos);
        } else {
            // handle unquoted argument
            pos = findNextDelimiter(1);
            arg = line.substring(0, pos);
        }

        if (pos >= line.length()) {
            line = "";
        } else {
            line = line.substring(pos + 1);
        }

        return arg;
    }

    /**
     * DOCUMENT ME!
     *
     * @param from DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int findNextQuote(int from) {
        int i = from;

        for (; (i < line.length()) && !(line.charAt(i) == '\"'); i++)
            ;

        return i;
    }

    /**
     * DOCUMENT ME!
     *
     * @param from DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int findNextDelimiter(int from) {
        int i = from;

        for (; (i < line.length()) && !isDelimiter(line.charAt(i)); i++)
            ;

        return i;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean isDelimiter(char c) {
        for (int i = 0; i < delimiter.length(); i++) {
            if (c == delimiter.charAt(i)) {
                return true;
            }
        }

        return false;
    }
}
