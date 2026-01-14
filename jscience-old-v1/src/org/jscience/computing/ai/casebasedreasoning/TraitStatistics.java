/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.computing.ai.casebasedreasoning;

/**
 * The TraitStatistics class contains statistical info
 * <p/>
 * about the various values for a single, specific trait.
 * <p/>
 * That includes the max value, min value and range
 * <p/>
 * <p/>
 * <p/>
 * This class has two primary purposes.
 * <p/>
 * First, we need this info to handle queries that use the variables
 * <p/>
 * [MAX_VALUE] and [MIN_VALUE]
 * <p/>
 * Second, we need the min, max and range info to do the
 * <p/>
 * nearest neighbor/similarity calculation
 * <p/>
 * <p/>
 * <p/>
 * Although this should be obvious, this class was only designed
 * <p/>
 * to work with numbers - i don't want to compute degrees of
 * <p/>
 * similarity on strings or booleans
 * <p/>
 * When building stats for strings and booleans, min=0 max=1
 * <p/>
 * <p/>
 * <p/>
 * It is believed that DataSetStatistics is the only class
 * <p/>
 * that will instantiate this one. The class was designed
 * <p/>
 * to be contained by DataSetStatistics
 *
 * @author <small>baylor</small>
 */
public class TraitStatistics {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    private String traitName;
    private float minimumValue;
    private float maximumValue;
    private int numberOfExamples = 0;

    //-----------------------------------------------------------------------------
    // Constructors
    //-----------------------------------------------------------------------------
    public TraitStatistics(String traitName) {
        setTraitName(traitName);
    }

    //-----------------------------------------------------------------------------
    // Methods
    //-----------------------------------------------------------------------------
    public void addExample(float value) {
        if (numberOfExamples == 0) {
            this.setMinimumValue(value);

            this.setMaximumValue(value);
        } else {
            if (value < getMinimumValue()) {
                this.setMinimumValue(value);
            }

            if (value > getMaximumValue()) {
                this.setMaximumValue(value);
            }
        }

        numberOfExamples++;
    } //--- addExample

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getMaximumValue() {
        return maximumValue;
    } //--- getMaximumValue

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getMinimumValue() {
        return minimumValue;
    } //--- getMinimumValue

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTraitName() {
        return traitName;
    } //--- getTraitName

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getRange() {
        return (maximumValue - minimumValue);
    } //--- getMinimumValue

    protected void setMaximumValue(float value) {
        maximumValue = value;
    } //--- setMaximumValue

    protected void setMinimumValue(float value) {
        minimumValue = value;
    } //--- setMinimumValue

    protected void setTraitName(String name) {
        traitName = name;
    } //--- setTraitName
} //--- TraitStatistics
