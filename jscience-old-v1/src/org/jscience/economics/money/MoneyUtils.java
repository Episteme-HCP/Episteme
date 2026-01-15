package org.jscience.economics.money;

/**
 * A class prividing useful methods to compute interest rates...
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class MoneyUtils extends Object {
    // the complex case where n=1
    /**
     * DOCUMENT ME!
     *
     * @param initialDeposit DOCUMENT ME!
     * @param interestRate DOCUMENT ME!
     * @param numberOfPeriods DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSimpleInterestValueAtt(double initialDeposit,
        double interestRate, int numberOfPeriods) {
        return initialDeposit * Math.pow(1 + numberOfPeriods, numberOfPeriods);
    }

    /**
     * DOCUMENT ME!
     *
     * @param resultDeposit DOCUMENT ME!
     * @param interestRate DOCUMENT ME!
     * @param numberOfPeriods DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSimpleInvestmentValueAtt(double resultDeposit,
        double interestRate, int numberOfPeriods) {
        return resultDeposit * Math.pow(1 + numberOfPeriods, -numberOfPeriods);
    }

    /**
     * DOCUMENT ME!
     *
     * @param initialDeposit DOCUMENT ME!
     * @param resultDeposit DOCUMENT ME!
     * @param numberOfPeriods DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSimpleInterestRateAtt(double initialDeposit,
        double resultDeposit, int numberOfPeriods) {
        return Math.pow(resultDeposit / initialDeposit, 1 / numberOfPeriods) -
        1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param initialDeposit DOCUMENT ME!
     * @param resultDeposit DOCUMENT ME!
     * @param interestRate DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSimpleInterestPeriods(double initialDeposit,
        double resultDeposit, double interestRate) {
        return Math.log(resultDeposit / initialDeposit) / Math.log(1 +
            interestRate);
    }

    //http://www.math.com/tables/general/interest.htm
    /**
     * DOCUMENT ME!
     *
     * @param initialDeposit DOCUMENT ME!
     * @param interestRate DOCUMENT ME!
     * @param compoundsPerYear DOCUMENT ME!
     * @param numberOfYears DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getComplexInterestValueAtt(double initialDeposit,
        double interestRate, int compoundsPerYear, int numberOfYears) {
        return initialDeposit * Math.pow(1 + (interestRate / compoundsPerYear),
            compoundsPerYear * numberOfYears);
    }

    /**
     * DOCUMENT ME!
     *
     * @param initialDeposit DOCUMENT ME!
     * @param interestRate DOCUMENT ME!
     * @param numberOfYears DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getContinuousInterestValueAtt(double initialDeposit,
        double interestRate, int numberOfYears) {
        return initialDeposit * Math.exp(interestRate * numberOfYears);
    }

    //http://math.nmsu.edu/morandi/math210gs99/InterestRateFormulas.html
    /**
     * DOCUMENT ME!
     *
     * @param paymentPerPeriod DOCUMENT ME!
     * @param interestRate DOCUMENT ME!
     * @param numberOfPeriods DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSimpleLoanBorrowedValueAtt(double paymentPerPeriod,
        double interestRate, int numberOfPeriods) {
        return (paymentPerPeriod * (1 -
        Math.pow(1 + interestRate, -numberOfPeriods))) / interestRate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param borrowedAmount DOCUMENT ME!
     * @param interestRate DOCUMENT ME!
     * @param numberOfPeriods DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSimpleLoanPaymentValueAtt(double borrowedAmount,
        double interestRate, int numberOfPeriods) {
        return (borrowedAmount * interestRate) / (1 -
        Math.pow(1 + interestRate, -numberOfPeriods));
    }

    //Loan Balance
    /**
     * DOCUMENT ME!
     *
     * @param initialAmountBorrowed DOCUMENT ME!
     * @param annualPercentageRate DOCUMENT ME!
     * @param paymentsPerYear DOCUMENT ME!
     * @param numberOfYears DOCUMENT ME!
     * @param amountPaidPerPayment DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getComplexLoanBalance(double initialAmountBorrowed,
        double annualPercentageRate, int paymentsPerYear, int numberOfYears,
        double amountPaidPerPayment) {
        return (initialAmountBorrowed * Math.pow(1 +
            (annualPercentageRate / paymentsPerYear),
            paymentsPerYear * numberOfYears)) -
        (amountPaidPerPayment * ((Math.pow(1 +
            (annualPercentageRate / paymentsPerYear),
            paymentsPerYear * numberOfYears) - 1) / ((1 +
        (annualPercentageRate / paymentsPerYear)) - 1)));
    }
}
