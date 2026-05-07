/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.economics;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.util.UniversalDataModel;
import org.episteme.social.economics.money.Money;
import org.episteme.social.economics.money.Currency;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

import org.episteme.core.util.Named;
import java.io.Serializable;
import java.util.*;

/**
 * Data model for the FinancialPortfolioViewer.
 * Represents a collection of assets with price history and holdings.
 * Uses the Money and Quantity system for high-precision financial analysis.
 */
@Persistent
public final class PortfolioData implements UniversalDataModel, ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final java.util.Map<String, Object> traits = new java.util.HashMap<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Holding> holdings = new ArrayList<>();

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final List<Asset> watchlist = new ArrayList<>();

    public PortfolioData() {
        this(new org.episteme.core.util.identity.UUIDIdentification(UUID.randomUUID()));
    }

    public PortfolioData(Identification id) {
        this.id = Objects.requireNonNull(id);
    }

    @Override
    public String getModelType() { return "FINANCIAL_PORTFOLIO"; }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public java.util.Map<String, Object> getTraits() {
        return traits;
    }

    @Persistent
    public static class Asset implements Serializable, Named {
        private static final long serialVersionUID = 1L;
        @Attribute
        private String symbol;
        @Attribute
        private String name;
        @Attribute
        private Money currentPrice;
        @Attribute
        private Money dailyChange;
        @Attribute
        private Real dailyChangePercent;
        @Attribute
        private long volume;
        @Attribute
        private List<Money> priceHistory;

        public Asset() {}

        public Asset(String symbol, String name, Money currentPrice, Money dailyChange,
                     Real dailyChangePercent, long volume, List<Money> priceHistory) {
            this.symbol = symbol;
            this.name = name;
            this.currentPrice = currentPrice;
            this.dailyChange = dailyChange;
            this.dailyChangePercent = dailyChangePercent;
            this.volume = volume;
            this.priceHistory = priceHistory;
        }

        public String getSymbol() { return symbol; }
        public String getName() { return name; }
        public Money getCurrentPrice() { return currentPrice; }
        public Money getDailyChange() { return dailyChange; }
        public Real getDailyChangePercent() { return dailyChangePercent; }
        public long getVolume() { return volume; }
        public List<Money> getPriceHistory() { return priceHistory; }
    }

    @Persistent
    public static class Holding implements Serializable {
        private static final long serialVersionUID = 1L;
        @Relation(type = Relation.Type.MANY_TO_ONE)
        private Asset asset;
        @Attribute
        private Real quantity;
        @Attribute
        private Money averageCost;

        public Holding() {}

        public Holding(Asset asset, Real quantity, Money averageCost) {
            this.asset = asset;
            this.quantity = quantity;
            this.averageCost = averageCost;
        }

        public Asset getAsset() { return asset; }
        public Real getQuantity() { return quantity; }
        public Money getAverageCost() { return averageCost; }

        public Money totalValue() {
            return asset.getCurrentPrice().multiply(quantity);
        }
        public Money profitLoss() {
            return totalValue().subtract(averageCost.multiply(quantity));
        }
    }

    public void addHolding(Asset asset, Real quantity, Money averageCost) {
        holdings.add(new Holding(asset, quantity, averageCost));
    }

    public void addToWatchlist(Asset asset) {
        watchlist.add(asset);
    }

    public List<Holding> getHoldings() { return Collections.unmodifiableList(holdings); }
    public List<Asset> getWatchlist() { return Collections.unmodifiableList(watchlist); }

    public Money getTotalPortfolioValue(Currency targetCurrency) {
        Money total = new Money(Real.ZERO, targetCurrency);
        for (Holding h : holdings) {
            total = total.add(h.totalValue());
        }
        return total;
    }

    @Override
    public java.util.Map<String, org.episteme.core.measure.Quantity<?>> getQuantities() {
        java.util.Map<String, org.episteme.core.measure.Quantity<?>> quantities = new java.util.HashMap<>();
        quantities.put("total_value_usd", getTotalPortfolioValue(Currency.USD));
        return quantities;
    }
}


