package org.jscience.economics.money;

import org.jscience.measure.Amount;

import java.util.Date;


//import java.util.Currency
//should replace our currency class from org.jscience.economics.money.Currency
/**
 * A class representing a Quote on a market.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//inspired from http://www.neuro-tech.net/javadocs/jfl/index.html
public final class Quote extends Object {
    /** DOCUMENT ME! */
    private String symbol;

    /** DOCUMENT ME! */
    private String company;

    /** DOCUMENT ME! */
    private long volume;

    /** DOCUMENT ME! */
    private Amount<Money> value;

    /** DOCUMENT ME! */
    private String market; //you can use Market.getName() although markets as we define them in market are very theorical contrary to the Quote class

    /** DOCUMENT ME! */
    private Amount<Money> openPrice;

    /** DOCUMENT ME! */
    private Date quoteTime;

/**
     * Create a quote with the minimum amount of required information. You have
     * to fill actual values at a later time.
     *
     * @param symbol  DOCUMENT ME!
     * @param company DOCUMENT ME!
     * @param market  DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Quote(String symbol, String company, String market) {
        if ((symbol != null) && (symbol.length() > 0) && (company != null) &&
                (company.length() > 0) && (market != null) &&
                (market.length() > 0)) {
            this.symbol = symbol;
            this.company = company;
            this.volume = 0;
            this.value = Amount.valueOf(0, Currency.USD);
            this.market = market;
            this.openPrice = Amount.valueOf(0, Currency.USD);
            this.quoteTime = new Date();
        } else {
            throw new IllegalArgumentException(
                "The Quote constructor doesn't accept null or empty arguments.");
        }
    }

/**
     * Creates a new Quote object.
     *
     * @param symbol    DOCUMENT ME!
     * @param company   DOCUMENT ME!
     * @param volume    DOCUMENT ME!
     * @param value     DOCUMENT ME!
     * @param market    DOCUMENT ME!
     * @param openPrice DOCUMENT ME!
     * @param quoteTime DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Quote(String symbol, String company, long volume,
        Amount<Money> value, String market, Amount<Money> openPrice,
        Date quoteTime) {
        if ((symbol != null) && (symbol.length() > 0) && (company != null) &&
                (company.length() > 0) && (market != null) &&
                (market.length() > 0) && (quoteTime != null)) {
            this.symbol = symbol;
            this.company = company;
            this.volume = volume;
            this.value = value;
            this.market = market;
            this.openPrice = openPrice;
            this.quoteTime = quoteTime;
        } else {
            throw new IllegalArgumentException(
                "The Quote constructor doesn't accept null or empty erguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final String getSymbol() {
        return symbol;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final String getCompany() {
        return company;
    }

    /**
     * DOCUMENT ME!
     *
     * @param company DOCUMENT ME!
     */
    public final void setCompany(String company) {
        this.company = company;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final long getVolume() {
        return volume;
    }

    /**
     * DOCUMENT ME!
     *
     * @param volume DOCUMENT ME!
     */
    public final void setVolume(long volume) {
        this.volume = volume;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Amount<Money> getValue() {
        return value;
    }

    //think about setting the volume and the quote time at the same time
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public final void setValue(Amount<Money> value) {
        this.value = value;
    }

    //sets different parameters at once
    /**
     * DOCUMENT ME!
     *
     * @param volume DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param quoteTime DOCUMENT ME!
     */
    public final void setQuotation(long volume, Amount<Money> value,
        Date quoteTime) {
        setVolume(volume);
        setValue(value);
        setQuoteTime(quoteTime);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final String getMarket() {
        return market;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Amount<Money> getOpenPrice() {
        return openPrice;
    }

    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     */
    public final void setOpenPrice(Amount<Money> amount) {
        openPrice = amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Date getQuoteTime() {
        return quoteTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param quoteTime DOCUMENT ME!
     */
    public final void setQuoteTime(Date quoteTime) {
        quoteTime = quoteTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final String toString() {
        return "Symbol: " + symbol + ", Company: " + company + ", Value: " +
        value + ", Time: " + quoteTime;
    }
}
