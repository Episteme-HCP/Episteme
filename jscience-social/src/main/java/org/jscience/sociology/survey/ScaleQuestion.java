package org.jscience.sociology.survey;

/**
 * A question asking for a rating on a linear scale.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class ScaleQuestion extends Question {

    private int min;
    private int max;
    private String minLabel;
    private String maxLabel;

    public ScaleQuestion(String text, int min, int max) {
        super(text, Type.LINEAR_SCALE);
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getMinLabel() {
        return minLabel;
    }

    public void setMinLabel(String minLabel) {
        this.minLabel = minLabel;
    }

    public String getMaxLabel() {
        return maxLabel;
    }

    public void setMaxLabel(String maxLabel) {
        this.maxLabel = maxLabel;
    }
}
