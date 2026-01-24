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

package org.jscience.economics.money;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Utility class for financial calculations.
 * <p>
 * Provides methods for computing interest rates, loan payments,
 * and other financial formulas.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="http://www.math.com/tables/general/interest.htm">Interest Formulas</a>
 */
public final class MoneyUtils {

    private MoneyUtils() {
        // Utility class - no instantiation
    }

    // ===== Simple Interest =====

    /**
     * Calculates the future value with simple interest.
     *
     * @param principal       initial deposit
     * @param interestRate    annual interest rate (as decimal, e.g., 0.05 for 5%)
     * @param numberOfPeriods number of periods
     * @return the future value
     */
    public static double simpleInterestFutureValue(double principal, 
            double interestRate, int numberOfPeriods) {
        return principal * (1 + interestRate * numberOfPeriods);
    }

    /**
     * Calculates the present value for simple interest.
     *
     * @param futureValue     the target future value
     * @param interestRate    annual interest rate
     * @param numberOfPeriods number of periods
     * @return the present value (initial deposit needed)
     */
    public static double simpleInterestPresentValue(double futureValue, 
            double interestRate, int numberOfPeriods) {
        return futureValue / (1 + interestRate * numberOfPeriods);
    }

    /**
     * Calculates the interest rate from initial and final values.
     *
     * @param principal       initial deposit
     * @param futureValue     final value
     * @param numberOfPeriods number of periods
     * @return the annual interest rate
     */
    public static double simpleInterestRate(double principal, double futureValue, 
            int numberOfPeriods) {
        return (futureValue / principal - 1) / numberOfPeriods;
    }

    /**
     * Calculates the number of periods needed to reach a target.
     *
     * @param principal    initial deposit
     * @param futureValue  target value
     * @param interestRate annual interest rate
     * @return the number of periods
     */
    public static double simpleInterestPeriods(double principal, double futureValue, 
            double interestRate) {
        return (futureValue / principal - 1) / interestRate;
    }

    // ===== Compound Interest =====

    /**
     * Calculates the future value with compound interest.
     *
     * @param principal        initial deposit
     * @param interestRate     annual interest rate
     * @param compoundsPerYear number of compounding periods per year
     * @param numberOfYears    number of years
     * @return the future value
     */
    public static double compoundInterestFutureValue(double principal, 
            double interestRate, int compoundsPerYear, int numberOfYears) {
        double rate = interestRate / compoundsPerYear;
        int periods = compoundsPerYear * numberOfYears;
        return principal * Math.pow(1 + rate, periods);
    }

    /**
     * Calculates future value with continuous compounding.
     *
     * @param principal     initial deposit
     * @param interestRate  annual interest rate
     * @param numberOfYears number of years
     * @return the future value
     */
    public static double continuousInterestFutureValue(double principal, 
            double interestRate, int numberOfYears) {
        return principal * Math.exp(interestRate * numberOfYears);
    }

    /**
     * Calculates the effective annual rate (EAR).
     *
     * @param nominalRate      the nominal annual rate
     * @param compoundsPerYear number of compounding periods
     * @return the effective annual rate
     */
    public static double effectiveAnnualRate(double nominalRate, int compoundsPerYear) {
        return Math.pow(1 + nominalRate / compoundsPerYear, compoundsPerYear) - 1;
    }

    // ===== Loan Calculations =====

    /**
     * Calculates the maximum loan amount for given payment.
     *
     * @param paymentPerPeriod the periodic payment
     * @param interestRate     periodic interest rate
     * @param numberOfPeriods  total number of payments
     * @return the maximum loan amount
     */
    public static double loanPrincipal(double paymentPerPeriod, 
            double interestRate, int numberOfPeriods) {
        if (interestRate == 0) {
            return paymentPerPeriod * numberOfPeriods;
        }
        double factor = 1 - Math.pow(1 + interestRate, -numberOfPeriods);
        return paymentPerPeriod * factor / interestRate;
    }

    /**
     * Calculates the periodic payment for a loan.
     *
     * @param principal       loan amount
     * @param interestRate    periodic interest rate
     * @param numberOfPeriods total number of payments
     * @return the periodic payment
     */
    public static double loanPayment(double principal, double interestRate, 
            int numberOfPeriods) {
        if (interestRate == 0) {
            return principal / numberOfPeriods;
        }
        double factor = 1 - Math.pow(1 + interestRate, -numberOfPeriods);
        return principal * interestRate / factor;
    }

    /**
     * Calculates the remaining balance on a loan.
     *
     * @param principal           original loan amount
     * @param annualRate          annual percentage rate
     * @param paymentsPerYear     payments per year
     * @param yearsPaid           years of payments made
     * @param paymentAmount       amount of each payment
     * @return the remaining balance
     */
    public static double loanBalance(double principal, double annualRate, 
            int paymentsPerYear, int yearsPaid, double paymentAmount) {
        double periodRate = annualRate / paymentsPerYear;
        int periodsPaid = paymentsPerYear * yearsPaid;
        
        double compoundFactor = Math.pow(1 + periodRate, periodsPaid);
        double paymentsFactor = (compoundFactor - 1) / periodRate;
        
        return principal * compoundFactor - paymentAmount * paymentsFactor;
    }

    /**
     * Calculates total interest paid over the life of a loan.
     *
     * @param principal       loan amount
     * @param payment         periodic payment
     * @param numberOfPeriods total number of payments
     * @return the total interest paid
     */
    public static double totalInterestPaid(double principal, double payment, 
            int numberOfPeriods) {
        return payment * numberOfPeriods - principal;
    }

    // ===== Money-based methods =====

    /**
     * Applies compound interest to a money amount.
     *
     * @param principal        the initial amount
     * @param interestRate     annual interest rate (as decimal)
     * @param compoundsPerYear compounding frequency
     * @param years            number of years
     * @return the future value
     */
    public static Money compoundInterest(Money principal, double interestRate, 
            int compoundsPerYear, int years) {
        double futureValue = compoundInterestFutureValue(
            principal.getValue().doubleValue(), interestRate, compoundsPerYear, years);
        return Money.valueOf(Real.of(futureValue), principal.getCurrency());
    }

    /**
     * Calculates monthly mortgage payment.
     *
     * @param principal   loan amount
     * @param annualRate  annual interest rate
     * @param years       loan term in years
     * @return the monthly payment
     */
    public static Money monthlyMortgagePayment(Money principal, double annualRate, int years) {
        double monthlyRate = annualRate / 12;
        int totalPayments = years * 12;
        double payment = loanPayment(principal.getValue().doubleValue(), monthlyRate, totalPayments);
        return Money.valueOf(Real.of(payment), principal.getCurrency());
    }
}
