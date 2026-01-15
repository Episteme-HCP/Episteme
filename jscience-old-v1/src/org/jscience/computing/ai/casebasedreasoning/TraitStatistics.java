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
