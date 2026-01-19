package org.jscience.arts;

import org.jscience.history.time.UncertainDate;
import java.util.Objects;

/**
 * A generic Analysis or Scientific Examination of an Artwork.
 * E.g., Carbon-14 dating, X-Ray fluorescence, Pigment analysis.
 */
public class Analysis {

    private final String methodName;
    private final String performer; // Lab or Organization Name
    private final UncertainDate date;
    private final String result;
    private String comments;

    public Analysis(String methodName, String performer, UncertainDate date, String result) {
        this.methodName = Objects.requireNonNull(methodName, "Method name cannot be null");
        this.performer = Objects.requireNonNull(performer, "Performer cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.result = Objects.requireNonNull(result, "Result cannot be null");
    }

    public String getMethodName() {
        return methodName;
    }

    public String getPerformer() {
        return performer;
    }

    public UncertainDate getDate() {
        return date;
    }

    public String getResult() {
        return result;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    @Override
    public String toString() {
        return String.format("Analysis[%s by %s on %s: %s]", methodName, performer, date, result);
    }
}
