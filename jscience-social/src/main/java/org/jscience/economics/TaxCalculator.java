package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Tax system modeling and calculation.
 */
public final class TaxCalculator {

    private TaxCalculator() {}

    public record TaxBracket(Real lowerBound, Real upperBound, Real rate) {}

    public record TaxResult(
        Real grossIncome,
        Real taxableIncome,
        Real taxOwed,
        Real effectiveRate,
        Real marginalRate,
        Real netIncome
    ) {}

    /**
     * Calculates tax under a progressive bracket system.
     */
    public static TaxResult calculateProgressiveTax(Real grossIncome, List<TaxBracket> brackets,
            Real standardDeduction) {
        
        Real taxableIncome = grossIncome.subtract(standardDeduction);
        if (taxableIncome.compareTo(Real.ZERO) < 0) {
            taxableIncome = Real.ZERO;
        }
        
        Real taxOwed = Real.ZERO;
        Real marginalRate = Real.ZERO;
        Real remaining = taxableIncome;
        
        for (TaxBracket bracket : brackets) {
            Real bracketSize = bracket.upperBound().subtract(bracket.lowerBound());
            
            if (remaining.compareTo(Real.ZERO) <= 0) break;
            
            if (taxableIncome.compareTo(bracket.lowerBound()) > 0) {
                Real taxableInBracket = remaining.compareTo(bracketSize) < 0 ? remaining : bracketSize;
                taxOwed = taxOwed.add(taxableInBracket.multiply(bracket.rate()));
                remaining = remaining.subtract(taxableInBracket);
                marginalRate = bracket.rate();
            }
        }
        
        Real effectiveRate = grossIncome.compareTo(Real.ZERO) > 0 
            ? taxOwed.divide(grossIncome) 
            : Real.ZERO;
        
        return new TaxResult(
            grossIncome, taxableIncome, taxOwed, effectiveRate, marginalRate,
            grossIncome.subtract(taxOwed)
        );
    }

    /**
     * Flat tax calculation.
     */
    public static TaxResult calculateFlatTax(Real grossIncome, Real taxRate, Real exemption) {
        Real taxableIncome = grossIncome.subtract(exemption);
        if (taxableIncome.compareTo(Real.ZERO) < 0) {
            taxableIncome = Real.ZERO;
        }
        
        Real taxOwed = taxableIncome.multiply(taxRate);
        Real effectiveRate = grossIncome.compareTo(Real.ZERO) > 0 
            ? taxOwed.divide(grossIncome) 
            : Real.ZERO;
        
        return new TaxResult(
            grossIncome, taxableIncome, taxOwed, effectiveRate, taxRate,
            grossIncome.subtract(taxOwed)
        );
    }

    /**
     * US-style progressive tax brackets (2023 approximate).
     */
    public static List<TaxBracket> usFederalBrackets2023() {
        return List.of(
            new TaxBracket(Real.ZERO, Real.of(11000), Real.of(0.10)),
            new TaxBracket(Real.of(11000), Real.of(44725), Real.of(0.12)),
            new TaxBracket(Real.of(44725), Real.of(95375), Real.of(0.22)),
            new TaxBracket(Real.of(95375), Real.of(182100), Real.of(0.24)),
            new TaxBracket(Real.of(182100), Real.of(231250), Real.of(0.32)),
            new TaxBracket(Real.of(231250), Real.of(578125), Real.of(0.35)),
            new TaxBracket(Real.of(578125), Real.of(Double.MAX_VALUE), Real.of(0.37))
        );
    }

    /**
     * French progressive tax brackets (2023 approximate).
     */
    public static List<TaxBracket> frenchBrackets2023() {
        return List.of(
            new TaxBracket(Real.ZERO, Real.of(10777), Real.of(0.0)),
            new TaxBracket(Real.of(10777), Real.of(27478), Real.of(0.11)),
            new TaxBracket(Real.of(27478), Real.of(78570), Real.of(0.30)),
            new TaxBracket(Real.of(78570), Real.of(168994), Real.of(0.41)),
            new TaxBracket(Real.of(168994), Real.of(Double.MAX_VALUE), Real.of(0.45))
        );
    }

    /**
     * Calculates VAT/sales tax.
     */
    public static Real calculateVAT(Real priceExcludingTax, Real vatRate) {
        return priceExcludingTax.multiply(vatRate);
    }

    /**
     * Extracts VAT from inclusive price.
     */
    public static Real extractVAT(Real priceIncludingTax, Real vatRate) {
        return priceIncludingTax.multiply(vatRate).divide(Real.of(1).add(vatRate));
    }

    /**
     * Calculates capital gains tax.
     */
    public static Real capitalGainsTax(Real purchasePrice, Real salePrice, Real taxRate,
            int yearsHeld, Real annualExemption) {
        
        Real gain = salePrice.subtract(purchasePrice);
        if (gain.compareTo(Real.ZERO) <= 0) return Real.ZERO;
        
        // Apply exemption
        Real taxableGain = gain.subtract(annualExemption);
        if (taxableGain.compareTo(Real.ZERO) <= 0) return Real.ZERO;
        
        // Some systems reduce tax for long-term holdings
        Real effectiveRate = yearsHeld > 1 ? taxRate.multiply(Real.of(0.8)) : taxRate;
        
        return taxableGain.multiply(effectiveRate);
    }

    /**
     * Calculates Laffer curve point (theoretical maximum revenue rate).
     */
    public static Real lafferOptimalRate(Real elasticity) {
        // Simplified: optimal rate = 1 / (1 + elasticity)
        return Real.of(1).divide(Real.of(1).add(elasticity));
    }

    /**
     * Compares tax burdens across income levels.
     */
    public static Map<Real, TaxResult> analyzeTaxBurden(List<TaxBracket> brackets,
            Real standardDeduction, Real... incomes) {
        
        Map<Real, TaxResult> analysis = new LinkedHashMap<>();
        for (Real income : incomes) {
            analysis.put(income, calculateProgressiveTax(income, brackets, standardDeduction));
        }
        return analysis;
    }
}
