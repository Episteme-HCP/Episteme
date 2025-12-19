package org.jscience.medicine.epidemiology;

/**
 * Epidemiological statistics calculations.
 */
public class EpidemiologyStats {

    private EpidemiologyStats() {
    }

    /**
     * Incidence rate.
     * Incidence = new cases / population at risk during time period
     */
    public static double incidenceRate(int newCases, int populationAtRisk) {
        return (double) newCases / populationAtRisk;
    }

    /**
     * Prevalence.
     * Prevalence = total cases / total population
     */
    public static double prevalence(int totalCases, int totalPopulation) {
        return (double) totalCases / totalPopulation;
    }

    /**
     * Odds ratio from 2x2 contingency table.
     * 
     * | Disease+ | Disease- |
     * Exposed+ | a | b |
     * Exposed- | c | d |
     * 
     * OR = (a*d) / (b*c)
     */
    public static double oddsRatio(int a, int b, int c, int d) {
        return (double) (a * d) / (b * c);
    }

    /**
     * Relative risk.
     * RR = (a/(a+b)) / (c/(c+d))
     */
    public static double relativeRisk(int a, int b, int c, int d) {
        double riskExposed = (double) a / (a + b);
        double riskUnexposed = (double) c / (c + d);
        return riskExposed / riskUnexposed;
    }

    /**
     * Sensitivity.
     * True positive rate = TP / (TP + FN)
     */
    public static double sensitivity(int truePositive, int falseNegative) {
        return (double) truePositive / (truePositive + falseNegative);
    }

    /**
     * Specificity.
     * True negative rate = TN / (TN + FP)
     */
    public static double specificity(int trueNegative, int falsePositive) {
        return (double) trueNegative / (trueNegative + falsePositive);
    }

    /**
     * Positive Predictive Value.
     * PPV = TP / (TP + FP)
     */
    public static double positivePredictiveValue(int truePositive, int falsePositive) {
        return (double) truePositive / (truePositive + falsePositive);
    }

    /**
     * Negative Predictive Value.
     * NPV = TN / (TN + FN)
     */
    public static double negativePredictiveValue(int trueNegative, int falseNegative) {
        return (double) trueNegative / (trueNegative + falseNegative);
    }

    /**
     * Number Needed to Treat.
     * NNT = 1 / ARR (Absolute Risk Reduction)
     */
    public static double numberNeededToTreat(double controlEventRate, double treatmentEventRate) {
        double arr = controlEventRate - treatmentEventRate;
        return 1.0 / arr;
    }
}
