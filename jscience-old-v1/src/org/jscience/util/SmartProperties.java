package org.jscience.util;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;
import java.util.StringTokenizer;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;


//stolen some bits from DaliWorld
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SmartProperties extends Properties {
/**
     * Creates a new SmartProperties object.
     */
    public SmartProperties() {
    }

/**
     * Creates a new SmartProperties object.
     *
     * @param properties DOCUMENT ME!
     */
    public SmartProperties(Properties properties) {
        super(properties);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void load(String s) throws IOException {
        FileInputStream fileinputstream = new FileInputStream(s);
        load(fileinputstream);
        fileinputstream.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getString(String s) {
        return getProperty(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boolean getBoolean(String s) {
        Boolean boolean1 = null;
        String s1 = getProperty(s);

        if (s1 != null) {
            try {
                boolean1 = new Boolean(s1);
            } catch (NumberFormatException numberformatexception) {
                boolean1 = null;
            }
        }

        return boolean1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Integer getInteger(String s) {
        Integer integer = null;
        String s1 = getProperty(s);

        if (s1 != null) {
            try {
                integer = new Integer(s1);
            } catch (NumberFormatException numberformatexception) {
                integer = null;
            }
        }

        return integer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Float getFloat(String s) {
        Float float1 = null;
        String s1 = getProperty(s);

        if (s1 != null) {
            try {
                float1 = new Float(s1);
            } catch (NumberFormatException numberformatexception) {
                float1 = null;
            }
        }

        return float1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Long getLong(String s) {
        Long long1 = null;
        String s1 = getProperty(s);

        if (s1 != null) {
            try {
                long1 = new Long(s1);
            } catch (NumberFormatException numberformatexception) {
                long1 = null;
            }
        }

        return long1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Double getDouble(String s) {
        Double double1 = null;
        String s1 = getProperty(s);

        if (s1 != null) {
            try {
                double1 = new Double(s1);
            } catch (NumberFormatException numberformatexception) {
                double1 = null;
            }
        }

        return double1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point2f getPoint2f(String s) {
        Point2f point2f = null;
        String s1 = getProperty(s);

        if (s1 != null) {
            try {
                StringTokenizer stringtokenizer = new StringTokenizer(s1,
                        ", \t\n\r\f");
                float f = Float.parseFloat(stringtokenizer.nextToken());
                float f1 = Float.parseFloat(stringtokenizer.nextToken());

                if (stringtokenizer.hasMoreTokens()) {
                    throw new Exception();
                }

                point2f = new Point2f(f, f1);
            } catch (Exception exception) {
                point2f = null;
            }
        }

        return point2f;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point3f getPoint3f(String s) {
        Point3f point3f = null;
        String s1 = getProperty(s);

        if (s1 != null) {
            try {
                StringTokenizer stringtokenizer = new StringTokenizer(s1,
                        ", \t\n\r\f");
                float f = Float.parseFloat(stringtokenizer.nextToken());
                float f1 = Float.parseFloat(stringtokenizer.nextToken());
                float f2 = Float.parseFloat(stringtokenizer.nextToken());

                if (stringtokenizer.hasMoreTokens()) {
                    throw new Exception();
                }

                point3f = new Point3f(f, f1, f2);
            } catch (Exception exception) {
                point3f = null;
            }
        }

        return point3f;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.awt.Dimension getDimension(String key) {
        int[] i = getInts(key);

        return (new java.awt.Dimension(i[0], i[1]));
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.awt.Point getPoint(String key) {
        int[] i = getInts(key);

        return (new java.awt.Point(i[0], i[1]));
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getStrings(String key) {
        String src = getString(key);
        String s;
        java.util.StringTokenizer st;
        int i = 0;
        st = new java.util.StringTokenizer(src, "\n");

        while (st.hasMoreTokens()) {
            i++;
            st.nextToken();
        }

        String[] ret = new String[i];
        i = 0;
        st = new java.util.StringTokenizer(src, "\n");

        while (st.hasMoreTokens())
            ret[i++] = st.nextToken();

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getInts(String key) {
        String src = getString(key);
        String s;
        java.util.StringTokenizer st;
        int i = 0;
        st = new java.util.StringTokenizer(src, " ");

        while (st.hasMoreTokens()) {
            i++;
            st.nextToken();
        }

        int[] ret = new int[i];
        i = 0;
        st = new java.util.StringTokenizer(src, " ");

        while (st.hasMoreTokens())
            ret[i++] = Integer.valueOf(st.nextToken()).intValue();

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float[] getFloats(String key) {
        String src = getString(key);
        String s;
        java.util.StringTokenizer st;
        int i = 0;
        st = new java.util.StringTokenizer(src, " ");

        while (st.hasMoreTokens()) {
            i++;
            st.nextToken();
        }

        float[] ret = new float[i];
        i = 0;
        st = new java.util.StringTokenizer(src, " ");

        while (st.hasMoreTokens())
            ret[i++] = Float.valueOf(st.nextToken()).floatValue();

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getDoubles(String key) {
        String src = getString(key);
        String s;
        java.util.StringTokenizer st;
        int i = 0;
        st = new java.util.StringTokenizer(src, " ");

        while (st.hasMoreTokens()) {
            i++;
            st.nextToken();
        }

        double[] ret = new double[i];
        i = 0;
        st = new java.util.StringTokenizer(src, " ");

        while (st.hasMoreTokens())
            ret[i++] = Double.valueOf(st.nextToken()).doubleValue();

        return ret;
    }
}
