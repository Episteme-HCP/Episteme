package org.jscience.chemistry.gui.extended.geometry;

/**
 * A class that provides mathematical definition of geometric angle
 *
 * @author Zhidong Xie (zxie@tripos.com)
 *         Original Version
 * @date 8/14/97
 */
public class Angle {
    /**
     * value of the angle
     */
    protected double value = 0.0;

    /**
     * error/warning message, or other comment
     */
    protected String comment = null;

    /**
     * default constructor: value is 0.0;
     */
    public Angle() {
    }

    /**
     * full constructor
     *
     * @param angle
     * @param inDegree boolean flag indicating value in degree unit
     */
    public Angle(double value, boolean inDegree) {
        this.value = (inDegree) ? (value / 180.0 * Math.PI) : value;
    }

    /**
     * constructor
     *
     * @param angle value in rad unit
     */
    public Angle(double value) {
        this(value, false);
    }

    /**
     * copy constructor
     *
     * @param a the angle to be copied
     */
    public Angle(Angle angle) {
        value = angle.value;
    }

    /**
     * Return angle value in rad unit
     */
    public double radValue() {
        return value;
    }

    /**
     * Return angle value in degree unit
     */
    public double degreeValue() {
        return ((180.0 * value) / Math.PI);
    }

    /**
     * Return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Return string representation of angle
     */
    public String toString() {
        return Float.toString((float) degreeValue());
    }

    /**
     * Set value of the angle
     *
     * @param value    angle's value to be set
     * @param inDegree true if value is in degree unit, false if in rad unit
     */
    public void setValue(double value, boolean inDegree) {
        this.value = (inDegree) ? (value / 180.0 * Math.PI) : value;
    }

    /**
     * Set value of the angle
     *
     * @param value angle's value to be set, in rad unit
     */
    public void setValue(double value) {
        this.setValue(value, false);
    }

    /**
     * Set comment
     *
     * @param comment
     * @see getComment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
} // end of Angle class
