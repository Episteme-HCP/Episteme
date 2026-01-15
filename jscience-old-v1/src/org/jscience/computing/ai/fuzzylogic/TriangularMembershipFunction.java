package org.jscience.computing.ai.fuzzylogic;

/**
 * <p/>
 * Triangular fuzzy membership function implementation.
 * </p>
 *
 * @author Levent Bayindir
 * @version 0.0.1
 */
public class TriangularMembershipFunction implements MembershipFunction {
    /**
     * DOCUMENT ME!
     */
    private String mName;

    /**
     * DOCUMENT ME!
     */
    private double mLeftPoint;

    /**
     * DOCUMENT ME!
     */
    private double mMiddlePoint;

    /**
     * DOCUMENT ME!
     */
    private double mRightPoint;

    /**
     * DOCUMENT ME!
     */
    private double mDeFuzzificationInputValue; // Input value for this MF used in defuzzification.

    /**
     * Creates a new TriangularMembershipFunction object.
     *
     * @param name        DOCUMENT ME!
     * @param leftPoint   DOCUMENT ME!
     * @param middlePoint DOCUMENT ME!
     * @param rightPoint  DOCUMENT ME!
     */
    public TriangularMembershipFunction(String name, double leftPoint,
                                        double middlePoint, double rightPoint) {
        mName = name.toLowerCase();
        mLeftPoint = leftPoint;
        mMiddlePoint = middlePoint;
        mRightPoint = rightPoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return mName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getLeftPoint() {
        return mLeftPoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMiddlePoint() {
        return mMiddlePoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRightPoint() {
        return mRightPoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mLeftPoint DOCUMENT ME!
     */
    public void setLeftPoint(double mLeftPoint) {
        this.mLeftPoint = mLeftPoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mMiddlePoint DOCUMENT ME!
     */
    public void setMiddlePoint(double mMiddlePoint) {
        this.mMiddlePoint = mMiddlePoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mRightPoint DOCUMENT ME!
     */
    public void setRightPoint(double mRightPoint) {
        this.mRightPoint = mRightPoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return MembershipFunction.TYPE_TRIANGULAR;
    }

    /**
     * DOCUMENT ME!
     *
     * @param input DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double fuzzify(double input) {
        // Check if input value is in range, if not, return 0.
        if ((input < mLeftPoint) || (input > mRightPoint)) {
            return 0;
        }

        // Determine which of /\ slopes works
        if (input == mMiddlePoint) {
            return 1;
        } else if (input < mMiddlePoint) {
            return ((input - mLeftPoint) / (mMiddlePoint - mLeftPoint));
        } else {
            return ((mRightPoint - input) / (mRightPoint - mMiddlePoint));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTypicalValue() {
        return mMiddlePoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param inputValue DOCUMENT ME!
     */
    public void setDeFuzzificationInputValue(double inputValue) {
        mDeFuzzificationInputValue = inputValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDeFuzzificationInputValue() {
        return mDeFuzzificationInputValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return mName;
    }
}
