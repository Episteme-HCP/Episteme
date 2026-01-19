package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Macroeconomic indicators calculation and analysis.
 */
public final class MacroIndicators {

    private MacroIndicators() {}

    /**
     * GDP calculation using expenditure approach.
     * GDP = C + I + G + (X - M)
     */
    public static Real calculateGDP(Real consumption, Real investment, 
            Real governmentSpending, Real exports, Real imports) {
        return consumption.add(investment).add(governmentSpending).add(exports).subtract(imports);
    }

    /**
     * Real GDP adjusted for inflation.
     */
    public static Real realGDP(Real nominalGDP, Real deflator) {
        return nominalGDP.divide(deflator).multiply(Real.of(100));
    }

    /**
     * GDP growth rate between two periods.
     */
    public static Real gdpGrowthRate(Real gdpCurrent, Real gdpPrevious) {
        return gdpCurrent.subtract(gdpPrevious).divide(gdpPrevious).multiply(Real.of(100));
    }

    /**
     * Inflation rate using CPI.
     */
    public static Real inflationRate(Real cpiCurrent, Real cpiPrevious) {
        return cpiCurrent.subtract(cpiPrevious).divide(cpiPrevious).multiply(Real.of(100));
    }

    /**
     * Unemployment rate calculation.
     */
    public static Real unemploymentRate(long unemployed, long laborForce) {
        return Real.of((double) unemployed / laborForce * 100);
    }

    /**
     * Labor force participation rate.
     */
    public static Real laborForceParticipation(long laborForce, long workingAgePopulation) {
        return Real.of((double) laborForce / workingAgePopulation * 100);
    }

    /**
     * Trade balance.
     */
    public static Real tradeBalance(Real exports, Real imports) {
        return exports.subtract(imports);
    }

    /**
     * Current account balance.
     */
    public static Real currentAccountBalance(Real tradeBalance, Real netIncome, Real transfers) {
        return tradeBalance.add(netIncome).add(transfers);
    }

    /**
     * Debt-to-GDP ratio.
     */
    public static Real debtToGDP(Real totalDebt, Real gdp) {
        return totalDebt.divide(gdp).multiply(Real.of(100));
    }

    /**
     * Budget deficit/surplus as percentage of GDP.
     */
    public static Real budgetBalance(Real governmentRevenue, Real governmentExpenditure, Real gdp) {
        return governmentRevenue.subtract(governmentExpenditure).divide(gdp).multiply(Real.of(100));
    }

    /**
     * Money supply multiplier.
     */
    public static Real moneyMultiplier(Real reserveRatio) {
        return Real.of(1.0).divide(reserveRatio);
    }

    /**
     * Velocity of money (MV = PY).
     */
    public static Real velocityOfMoney(Real nominalGDP, Real moneySupply) {
        return nominalGDP.divide(moneySupply);
    }

    /**
     * Fisher equation for real interest rate.
     * r = i - π
     */
    public static Real realInterestRate(Real nominalRate, Real inflationRate) {
        return nominalRate.subtract(inflationRate);
    }

    /**
     * Taylor Rule for recommended interest rate.
     * i = r* + π + 0.5(π - π*) + 0.5(y - y*)
     */
    public static Real taylorRule(Real neutralRate, Real currentInflation, Real targetInflation,
            Real outputGap) {
        Real inflationGap = currentInflation.subtract(targetInflation);
        return neutralRate.add(currentInflation)
            .add(inflationGap.multiply(Real.of(0.5)))
            .add(outputGap.multiply(Real.of(0.5)));
    }

    /**
     * Okun's Law - relationship between unemployment and GDP gap.
     * Each 1% unemployment above natural rate ≈ 2% GDP gap.
     */
    public static Real okunnGDPGap(Real actualUnemployment, Real naturalUnemployment) {
        return actualUnemployment.subtract(naturalUnemployment).multiply(Real.of(-2));
    }

    /**
     * Phillips Curve - inflation-unemployment tradeoff.
     */
    public static Real phillipsCurveInflation(Real expectedInflation, Real unemploymentGap, 
            Real supplyShock) {
        // π = πe - β(u - u*) + ε
        return expectedInflation.subtract(unemploymentGap.multiply(Real.of(0.5))).add(supplyShock);
    }

    /**
     * Gini coefficient from income distribution.
     */
    public static Real giniCoefficient(List<Real> incomes) {
        if (incomes == null || incomes.size() < 2) return Real.ZERO;
        
        List<Real> sorted = new ArrayList<>(incomes);
        sorted.sort(Real::compareTo);
        
        int n = sorted.size();
        double sum = sorted.stream().mapToDouble(Real::doubleValue).sum();
        
        double numerator = 0;
        for (int i = 0; i < n; i++) {
            numerator += (2 * (i + 1) - n - 1) * sorted.get(i).doubleValue();
        }
        
        double gini = numerator / (n * sum);
        return Real.of(gini);
    }

    /**
     * Human Development Index (simplified).
     */
    public static Real hdi(Real lifeExpectancy, Real meanSchooling, Real gniPerCapita) {
        // Normalize each dimension
        double healthIndex = (lifeExpectancy.doubleValue() - 20) / (85 - 20);
        double educationIndex = meanSchooling.doubleValue() / 15;
        double incomeIndex = (Math.log(gniPerCapita.doubleValue()) - Math.log(100)) / 
            (Math.log(75000) - Math.log(100));
        
        // Geometric mean
        double hdi = Math.pow(healthIndex * educationIndex * incomeIndex, 1.0/3.0);
        return Real.of(Math.max(0, Math.min(1, hdi)));
    }
}
