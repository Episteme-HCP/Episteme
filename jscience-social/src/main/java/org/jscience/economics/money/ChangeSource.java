package org.jscience.economics.money;

import org.jscience.util.UnavailableDataException;

//import java.util.Currency
//should replace our currency class from org.jscience.economics.money.Currency

/**
 * A class representing a currency conveter.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//each bank or dealer will have its own rates
//rates change very often
//Foreign Exchange Rate Currency Sources:
//http://www.bankofcanada.ca/en/financial_markets/csv/exchange_eng.csv
//http://www.oanda.com/convert/fxdaily
//http://www.bmo.com/economic/regular/fxrates.html
//http://pacific.commerce.ubc.ca/xr/today.html
//http://www.ny.frb.org/pihome/statistics/forex12.shtml
//http://www.customhouse.com/cgi-bin/World.cgi
//http://moneycentral.msn.com/investor/market/rates.asp
//http://www.xe.com/cus
//or see for complete java solutions:
//http://www.mindprod.com/zips/java/currcon.html
//http://www.neuro-tech.net/archives/000032.html (recommended)
//this is the total price including every commission, tax...
public interface ChangeSource {
    /**
     * public Money convert(Money source, Currency target) {
     * <p/>
     * return new Money(getConverted(source.getAmount(), source.getCurrency(), target), target);
     * <p/>
     * }
     */

    //returns the converted amount
    public float getConverted(float amount, Currency source, Currency target)
            throws UnavailableDataException;
}
