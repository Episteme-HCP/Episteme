/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;


/**
 * Level classifies the logging of LogEntry objects. The implementation is
 * inspired by java.util.logging, but simpler.
 *
 * @author Holger Antelmann
 *
 * @see LogEntry
 * @see Logger
 */
public class Level implements Serializable, Comparable<Level> {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -6255426885431391139L;

    /** used when no other level was given */
    public static final Level DEFAULT = new Level("DEFAULT", 0);

    /**
     * DOCUMENT ME!
     */
    public static final Level NORMAL = new Level("NORMAL", 0);

    /**
     * DOCUMENT ME!
     */
    public static final Level CONFIG = new Level("CONFIG", -10);

    /**
     * DOCUMENT ME!
     */
    public static final Level BEGIN = new Level("BEGIN", -10);

    /**
     * DOCUMENT ME!
     */
    public static final Level END = new Level("END", -10);

    /**
     * DOCUMENT ME!
     */
    public static final Level SUCCESS = new Level("SUCCESS", 10);

    /**
     * DOCUMENT ME!
     */
    public static final Level FAILURE = new Level("FAILURE", 10);

    /**
     * DOCUMENT ME!
     */
    public static final Level DEBUG = new Level("DEBUG", -500);

    /**
     * DOCUMENT ME!
     */
    public static final Level EXCEPTION = new Level("EXCEPTION", 50);

    /**
     * DOCUMENT ME!
     */
    public static final Level FINE = new Level("FINE", -100);

    /**
     * DOCUMENT ME!
     */
    public static final Level FINER = new Level("FINER", -200);

    /**
     * DOCUMENT ME!
     */
    public static final Level FINEST = new Level("FINEST", -300);

    /**
     * DOCUMENT ME!
     */
    public static final Level INFO = new Level("INFO", 10);

    /**
     * DOCUMENT ME!
     */
    public static final Level ERROR = new Level("ERROR", 1000);

    /**
     * DOCUMENT ME!
     */
    public static final Level SEVERE = new Level("SEVERE", 1000);

    /**
     * DOCUMENT ME!
     */
    public static final Level WARNING = new Level("WARNING", 10);

    /**
     * DOCUMENT ME!
     */
    public static final Level IMPORTANT = new Level("IMPORTANT", 100);

    /**
     * DOCUMENT ME!
     */
    public static final Level COMMENT = new Level("COMMENT", -500);

    /**
     * DOCUMENT ME!
     */
    String text;

    /**
     * DOCUMENT ME!
     */
    int compareValue;

/**
   * @param text must not be null
   */
    protected Level(String text, int compareValue) {
        if (text == null) {
            throw new NullPointerException("text parameter for Level is null");
        }

        this.text = text;
        this.compareValue = compareValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Level[] getKnownLevels() {
        try {
            ArrayList<Level> list = new ArrayList<Level>();

            for (Field field : Level.class.getFields()) {
                int mod = field.getModifiers();

                if (!Modifier.isPublic(mod)) {
                    continue;
                }

                if (!Modifier.isStatic(mod)) {
                    continue;
                }

                if (!Level.class.isAssignableFrom(field.getType())) {
                    continue;
                }

                list.add((Level) field.get(null));
            }

            return list.toArray(new Level[list.size()]);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * returns the level corresponding to the given name. If the level
     * is not known, a level named 'Special Level (name)' is returned.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public static Level forName(String name) {
        try {
            Field field = Level.class.getField(name);

            if (!Level.class.isAssignableFrom(field.getType())) {
                throw new NoSuchFieldException();
            }

            if (!Modifier.isStatic(field.getModifiers())) {
                throw new NoSuchFieldException();
            }

            return (Level) field.get(null);
        } catch (NoSuchFieldException ex) {
            return new Level("Special Level (" + name + ")", 0);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * returns the value the natural order is based on
     *
     * @return DOCUMENT ME!
     */
    public int getImportance() {
        return compareValue;
    }

    /**
     * provides a simple way to order Levels by their assumed
     * importance.
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareTo(Level l) {
        if (compareValue == l.compareValue) {
            return text.compareTo(l.text);
        }

        return compareValue - l.compareValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Level)) {
            return false;
        }

        return ((Level) obj).text.equals(text);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return text;
    }
}
