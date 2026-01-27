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

package org.jscience.economics.loaders;

import org.jscience.economics.PortfolioData;
import org.jscience.economics.money.Money;
import org.jscience.economics.money.Currency;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;
import org.jscience.io.BasicMiniCatalog;
import java.util.*;

/**
 * Resource reader for financial portfolio scenarios.
 * Provides real-time mock data if no external source is defined.
 */
public final class EconomicScenarioReader extends AbstractResourceReader<PortfolioData> {

    @Override public String getCategory() { return "Economics"; }
    @Override public String getName() { return "Economic Scenario Reader"; }
    @Override public String getDescription() { return "Loads financial portfolio data and market simulations."; }
    @Override public String getLongDescription() { return "Support for JSON and CSV portfolio structures with built-in sample data."; }
    @Override public String getResourcePath() { return "data/economics"; }
    @Override public Class<PortfolioData> getResourceType() { return PortfolioData.class; }
    @Override public String[] getSupportedVersions() { return new String[] {"1.0"}; }

    @Override
    protected PortfolioData loadFromSource(String id) throws Exception {
        return null; 
    }

    @Override
    protected MiniCatalog<PortfolioData> getMiniCatalog() {
        BasicMiniCatalog<PortfolioData> catalog = new BasicMiniCatalog<>();
        catalog.register("GlobalMarkets", createGlobalMarkets());
        return catalog;
    }

    private PortfolioData createGlobalMarkets() {
        PortfolioData pd = new PortfolioData();
        Random rand = new Random();
        
        String[] symbols = {"AAPL", "MSFT", "GOOGL", "TSLA", "BTC", "GOLD", "OIL"};
        for (String s : symbols) {
            List<Money> history = new ArrayList<>();
            Currency cur = Currency.USD;
            Money price = new Money(Real.of(100 + rand.nextDouble() * 500), cur);
            for (int i = 0; i < 50; i++) {
                history.add(price);
                price = price.multiply(Real.of(0.98).add(Real.of(rand.nextDouble()).multiply(Real.of(0.04))));
            }
            PortfolioData.Asset asset = new PortfolioData.Asset(s, s + " Corp", price, 
                                    new Money(Real.of(rand.nextDouble()), cur), 
                                    Real.of(rand.nextDouble()).multiply(Real.of(2)), (long)(rand.nextDouble() * 1000000), history);
            pd.addHolding(asset, Real.of(rand.nextInt(100) + 1), price.multiply(Real.of(0.9)));
            pd.addToWatchlist(asset);
        }
        return pd;
    }
}
