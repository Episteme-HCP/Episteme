package org.jscience.economics;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for financial mathematics.
 * Includes Net Present Value (NPV), Future Value (FV), and Amortization
 * calculations.
 */
public class FinancialMath {

    private FinancialMath() {
        // Utility class
    }

    /**
     * Calculates the Future Value (FV) of an investment.
     * FV = PV * (1 + r)^n
     *
     * @param presentValue The current value (PV).
     * @param rate         The interest rate per period (r).
     * @param periods      The number of periods (n).
     * @return The future value.
     */
    public static Money calculateFutureValue(Money presentValue, double rate, int periods) {
        BigDecimal pv = BigDecimal.valueOf(presentValue.getAmount().doubleValue());
        BigDecimal multiplier = BigDecimal.valueOf(1 + rate).pow(periods);
        BigDecimal fv = pv.multiply(multiplier);
        return new Money(fv.doubleValue(), presentValue.getCurrency());
    }

    /**
     * Calculates the Net Present Value (NPV) of a series of cash flows.
     * NPV = Sum(CF_t / (1 + r)^t)
     *
     * @param rate      The discount rate (r).
     * @param cashFlows The series of cash flows (Money objects).
     * @return The Net Present Value.
     */
    public static Money calculateNPV(double rate, Money... cashFlows) {
        if (cashFlows.length == 0) {
            return new Money(0.0, Currency.USD); // Default
        }

        Currency currency = cashFlows[0].getCurrency();
        BigDecimal npv = BigDecimal.ZERO;

        for (int t = 0; t < cashFlows.length; t++) {
            BigDecimal cf = BigDecimal.valueOf(cashFlows[t].getAmount().doubleValue());
            BigDecimal denominator = BigDecimal.valueOf(1 + rate).pow(t); // t=0 is initial investment usually
            npv = npv.add(cf.divide(denominator, 4, RoundingMode.HALF_UP));
        }

        return new Money(npv.doubleValue(), currency);
    }

    /**
     * Calculates monthly mortgage payment (Amortization).
     * M = P [ i(1 + i)^n ] / [ (1 + i)^n – 1]
     *
     * @param principal  The loan amount (P).
     * @param annualRate The annual interest rate.
     * @param years      The loan term in years.
     * @return The monthly payment.
     */
    public static Money calculateMonthlyPayment(Money principal, double annualRate, int years) {
        double monthlyRate = annualRate / 12.0;
        int months = years * 12;

        BigDecimal p = BigDecimal.valueOf(principal.getAmount().doubleValue());
        BigDecimal r = BigDecimal.valueOf(monthlyRate);
        BigDecimal onePlusR = r.add(BigDecimal.ONE).pow(months);

        BigDecimal numerator = p.multiply(r).multiply(onePlusR);
        BigDecimal denominator = onePlusR.subtract(BigDecimal.ONE);

        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal flat = p.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
            return new Money(flat.doubleValue(), principal.getCurrency());
        }

        BigDecimal monthlyPayment = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        return new Money(monthlyPayment.doubleValue(), principal.getCurrency());
    }
}
