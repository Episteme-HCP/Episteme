package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.data.UniversalDataModel;
import java.util.*;

/**
 * Data model for the FinancialPortfolioViewer.
 * Represents a collection of assets with price history and holdings.
 * Uses the Money and Quantity system for high-precision financial analysis.
 */
public final class PortfolioData implements UniversalDataModel {

    @Override
    public String getModelType() { return "FINANCIAL_PORTFOLIO"; }

    public record Asset(
        String symbol,
        String name,
        Money currentPrice,
        Money dailyChange,
        Real dailyChangePercent,
        long volume,
        List<Money> priceHistory
    ) {}

    public record Holding(
        Asset asset,
        Real quantity,
        Money averageCost
    ) {
        public Money totalValue() {
            return asset.currentPrice().multiply(quantity);
        }
        public Money profitLoss() {
            return totalValue().subtract(averageCost.multiply(quantity));
        }
    }

    private final List<Holding> holdings = new ArrayList<>();
    private final List<Asset> watchlist = new ArrayList<>();

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
}
