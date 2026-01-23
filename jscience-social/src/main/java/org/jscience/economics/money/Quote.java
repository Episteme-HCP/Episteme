package org.jscience.economics.money;



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
    private String symbol;
    private String company;
    private long volume;
    private Money value;
    private String market;
    private Money openPrice;
    private Money highPrice;
    private Money lowPrice;
    private Money change;
    private Date quoteTime;

    public Quote(String symbol, String company, String market) {
        if ((symbol != null) && (symbol.length() > 0) && (company != null) &&
                (company.length() > 0) && (market != null) &&
                (market.length() > 0)) {
            this.symbol = symbol;
            this.company = company;
            this.volume = 0;
            this.value = Money.usd(0);
            this.market = market;
            this.openPrice = Money.usd(0);
            this.quoteTime = new Date();
        } else {
            throw new IllegalArgumentException(
                "The Quote constructor doesn't accept null or empty arguments.");
        }
    }

    public Quote(String symbol, String company, long volume,
        Money value, String market, Money openPrice,
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

    public final String getSymbol() {
        return symbol;
    }

    public final String getCompany() {
        return company;
    }

    public final void setCompany(String company) {
        this.company = company;
    }

    public final long getVolume() {
        return volume;
    }

    public final void setVolume(long volume) {
        this.volume = volume;
    }

    public final Money getValue() {
        return value;
    }

    public final void setValue(Money value) {
        this.value = value;
    }

    public final void setQuotation(long volume, Money value,
        Date quoteTime) {
        setVolume(volume);
        setValue(value);
        setQuoteTime(quoteTime);
    }

    public final String getMarket() {
        return market;
    }

    public final Money getOpenPrice() {
        return openPrice;
    }

    public final void setOpenPrice(Money amount) {
        openPrice = amount;
    }

    public final Date getQuoteTime() {
        return quoteTime;
    }

    public final void setQuoteTime(Date quoteTime) {
        this.quoteTime = quoteTime;
    }

    public final Money getHighPrice() {
        return highPrice;
    }

    public final void setHighPrice(Money highPrice) {
        this.highPrice = highPrice;
    }

    public final Money getLowPrice() {
        return lowPrice;
    }

    public final void setLowPrice(Money lowPrice) {
        this.lowPrice = lowPrice;
    }

    public final Money getChange() {
        return change;
    }

    public final void setChange(Money change) {
        this.change = change;
    }

    public final String toString() {
        return "Symbol: " + symbol + ", Company: " + company + ", Value: " +
        value + ", Time: " + quoteTime;
    }
}
