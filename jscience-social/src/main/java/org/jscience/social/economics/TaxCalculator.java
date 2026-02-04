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

package org.jscience.social.economics;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.social.economics.money.Money;
import java.util.*;

/**
 * Tax system modeling and calculation.
 */
public final class TaxCalculator {

    private TaxCalculator() {}

    public record TaxBracket(Money lowerBound, Money upperBound, Real rate) {}

    public record TaxResult(
        Money grossIncome,
        Money taxableIncome,
        Money taxOwed,
        Real effectiveRate,
        Real marginalRate,
        Money netIncome
    ) {}

    /**
     * Calculates tax under a progressive bracket system.
     */
    public static TaxResult calculateProgressiveTax(Money grossIncome, List<TaxBracket> brackets,
            Money standardDeduction) {
        
        Money taxableIncome = grossIncome.subtract(standardDeduction);
        if (taxableIncome.getValue().compareTo(Real.ZERO) < 0) {
            taxableIncome = new Money(Real.ZERO, grossIncome.getCurrency());
        }
        
        Money taxOwed = new Money(Real.ZERO, grossIncome.getCurrency());
        Real marginalRate = Real.ZERO;
        Money remaining = taxableIncome;
        
        for (TaxBracket bracket : brackets) {
            // Ensure brackets match currency
            Money lower = bracket.lowerBound();
            if (!lower.getCurrency().equals(grossIncome.getCurrency())) {
                 // ideally convert, but for now skip or error. Assuming same currency.
            }
            Money bracketSize = bracket.upperBound().subtract(bracket.lowerBound());
            
            if (remaining.getValue().compareTo(Real.ZERO) <= 0) break;
            
            if (taxableIncome.compareTo(bracket.lowerBound()) > 0) {
                // If remaining < bracketSize
                Money taxableInBracket = remaining.compareTo(bracketSize) < 0 ? remaining : bracketSize;
                taxOwed = taxOwed.add(taxableInBracket.multiply(bracket.rate()));
                remaining = remaining.subtract(taxableInBracket);
                marginalRate = bracket.rate();
            }
        }
        
        Real effectiveRate = grossIncome.getValue().compareTo(Real.ZERO) > 0 
            ? taxOwed.divide(grossIncome).getValue()
            : Real.ZERO;
        
        return new TaxResult(
            grossIncome, taxableIncome, taxOwed, effectiveRate, marginalRate,
            grossIncome.subtract(taxOwed)
        );
    }

    /**
     * Flat tax calculation.
     */
    public static TaxResult calculateFlatTax(Money grossIncome, Real taxRate, Money exemption) {
        Money taxableIncome = grossIncome.subtract(exemption);
        if (taxableIncome.getValue().compareTo(Real.ZERO) < 0) {
            taxableIncome = new Money(Real.ZERO, grossIncome.getCurrency());
        }
        
        Money taxOwed = taxableIncome.multiply(taxRate);
        Real effectiveRate = grossIncome.getValue().compareTo(Real.ZERO) > 0 
            ? taxOwed.divide(grossIncome).getValue()
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
            new TaxBracket(Money.usd(0), Money.usd(11000), Real.of(0.10)),
            new TaxBracket(Money.usd(11000), Money.usd(44725), Real.of(0.12)),
            new TaxBracket(Money.usd(44725), Money.usd(95375), Real.of(0.22)),
            new TaxBracket(Money.usd(95375), Money.usd(182100), Real.of(0.24)),
            new TaxBracket(Money.usd(182100), Money.usd(231250), Real.of(0.32)),
            new TaxBracket(Money.usd(231250), Money.usd(578125), Real.of(0.35)),
            new TaxBracket(Money.usd(578125), Money.usd(Double.MAX_VALUE), Real.of(0.37))
        );
    }

    /**
     * French progressive tax brackets (2023 approximate).
     */
    public static List<TaxBracket> frenchBrackets2023() {
        return List.of(
            new TaxBracket(Money.eur(0), Money.eur(10777), Real.of(0.0)),
            new TaxBracket(Money.eur(10777), Money.eur(27478), Real.of(0.11)),
            new TaxBracket(Money.eur(27478), Money.eur(78570), Real.of(0.30)),
            new TaxBracket(Money.eur(78570), Money.eur(168994), Real.of(0.41)),
            new TaxBracket(Money.eur(168994), Money.eur(Double.MAX_VALUE), Real.of(0.45))
        );
    }

    /**
     * Calculates VAT/sales tax.
     */
    public static Money calculateVAT(Money priceExcludingTax, Real vatRate) {
        return priceExcludingTax.multiply(vatRate);
    }

    /**
     * Extracts VAT from inclusive price.
     */
    public static Money extractVAT(Money priceIncludingTax, Real vatRate) {
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
    public static Map<Money, TaxResult> analyzeTaxBurden(List<TaxBracket> brackets,
            Money standardDeduction, Money... incomes) {
        
        Map<Money, TaxResult> analysis = new LinkedHashMap<>();
        for (Money income : incomes) {
            analysis.put(income, calculateProgressiveTax(income, brackets, standardDeduction));
        }
        return analysis;
    }
}

