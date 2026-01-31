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

package org.jscience.social.economics.money;

import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.social.history.time.TimePoint;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a market quote for a financial instrument.
 * <p>
 * A quote contains pricing information for a security including
 * current price, volume, and price range data.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public final class Quote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute
    private final String symbol;

    @Attribute
    private String company;

    @Attribute
    private long volume;

    @Attribute
    private Money value;

    @Attribute
    private final String market;

    @Attribute
    private Money openPrice;

    @Attribute
    private Money highPrice;

    @Attribute
    private Money lowPrice;

    @Attribute
    private Money previousClose;

    @Attribute
    private Money change;

    @Attribute
    private TimeCoordinate quoteTime;

    /**
     * Creates a new Quote with basic information.
     *
     * @param symbol  the ticker symbol
     * @param company the company name
     * @param market  the exchange market
     * @throws NullPointerException     if any argument is null
     * @throws IllegalArgumentException if any string is empty
     */
    public Quote(String symbol, String company, String market) {
        this.symbol = requireNonEmpty(symbol, "Symbol");
        this.company = requireNonEmpty(company, "Company");
        this.market = requireNonEmpty(market, "Market");
        this.volume = 0;
        this.value = Money.usd(0);
        this.openPrice = Money.usd(0);
        this.quoteTime = TimePoint.now();
    }

    /**
     * Creates a new Quote with full information.
     *
     * @param symbol    the ticker symbol
     * @param company   the company name
     * @param volume    the trading volume
     * @param value     the current price
     * @param market    the exchange market
     * @param openPrice the opening price
     * @param quoteTime the time of the quote
     */
    public Quote(String symbol, String company, long volume, Money value,
                 String market, Money openPrice, TimeCoordinate quoteTime) {
        this.symbol = requireNonEmpty(symbol, "Symbol");
        this.company = requireNonEmpty(company, "Company");
        this.market = requireNonEmpty(market, "Market");
        this.volume = volume;
        this.value = Objects.requireNonNull(value, "Value cannot be null");
        this.openPrice = openPrice;
        this.quoteTime = Objects.requireNonNull(quoteTime, "Quote time cannot be null");
    }

    private static String requireNonEmpty(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
        return value;
    }

    // Getters

    public String getSymbol() {
        return symbol;
    }

    public String getCompany() {
        return company;
    }

    public long getVolume() {
        return volume;
    }

    public Money getValue() {
        return value;
    }

    public String getMarket() {
        return market;
    }

    public Money getOpenPrice() {
        return openPrice;
    }

    public Money getHighPrice() {
        return highPrice;
    }

    public Money getLowPrice() {
        return lowPrice;
    }

    public Money getPreviousClose() {
        return previousClose;
    }

    public Money getChange() {
        return change;
    }

    public TimeCoordinate getQuoteTime() {
        return quoteTime;
    }

    /**
     * Returns the price change as a percentage.
     * @return the change percentage
     */
    public org.jscience.core.mathematics.numbers.real.Real getChangePercent() {
        if (previousClose == null || previousClose.getValue().isZero()) {
            return org.jscience.core.mathematics.numbers.real.Real.ZERO;
        }
        return change.getValue().divide(previousClose.getValue()).multiply(org.jscience.core.mathematics.numbers.real.Real.of(100));
    }

    // Setters

    public void setCompany(String company) {
        this.company = company;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    public void setOpenPrice(Money openPrice) {
        this.openPrice = openPrice;
    }

    public void setHighPrice(Money highPrice) {
        this.highPrice = highPrice;
    }

    public void setLowPrice(Money lowPrice) {
        this.lowPrice = lowPrice;
    }

    public void setPreviousClose(Money previousClose) {
        this.previousClose = previousClose;
    }

    public void setChange(Money change) {
        this.change = change;
    }

    public void setQuoteTime(TimeCoordinate quoteTime) {
        this.quoteTime = quoteTime;
    }

    /**
     * Updates the quote with new trading data.
     *
     * @param volume    the new volume
     * @param value     the new price
     * @param quoteTime the time of update
     */
    public void update(long volume, Money value, TimeCoordinate quoteTime) {
        setVolume(volume);
        setValue(value);
        setQuoteTime(quoteTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quote)) return false;
        Quote other = (Quote) obj;
        return Objects.equals(symbol, other.symbol) &&
               Objects.equals(market, other.market);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, market);
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %s, Vol: %d, Time: %s",
            symbol, company, value, volume, quoteTime);
    }
}

