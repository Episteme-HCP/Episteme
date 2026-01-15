package org.jscience.politics;

import org.jscience.economics.money.Currency;

import org.jscience.geography.Place;

import org.jscience.util.Named;

import java.awt.*;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing the basic facts about a country (the modern tribe)
 * or also kingdoms, empires...
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you should understand country in the broadest possible sense here
//some countries also account for a religious hierarchy
//we could add many fields here
//for a list of candidates see http://www.cia.gov/cia/publications/factbook/docs/profileguide.html
//may be we should see a country as one nation along with many (not empowered tribes)
//this means that you won't get a country's population using getNation() or that getNation gives the common ground philosophy for that country
//perhaps we should therefore define a country as composed of many nations...
public class Country extends Place implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Nation nation;

    /** DOCUMENT ME! */
    private Administration army;

    /** DOCUMENT ME! */
    private Administration police;

    /** DOCUMENT ME! */
    private Administration justice;

    /** DOCUMENT ME! */
    private Currency currency;

    /** DOCUMENT ME! */
    private double gDP;

    /** DOCUMENT ME! */
    private double gNP;

    /** DOCUMENT ME! */
    private Image flag;

    /** DOCUMENT ME! */
    private City capital;

    /** DOCUMENT ME! */
    private Set cities;

    /** DOCUMENT ME! */
    private Set regions; //if any

/**
     * Creates a new Country object.
     *
     * @param name    DOCUMENT ME!
     * @param nation  DOCUMENT ME!
     * @param capital DOCUMENT ME!
     */
    public Country(String name, Nation nation, City capital) {
        super(name, nation.getFormalTerritory().getBoundary());

        if ((name != null) && (name.length() > 0) && (nation != null) &&
                (capital != null)) {
            this.name = name;
            this.nation = nation;
            this.capital = capital;
            this.army = null;
            this.police = null;
            this.justice = null;
            this.currency = null;
            this.gDP = -1;
            this.gNP = -1;
            this.flag = null;
            this.cities = Collections.EMPTY_SET;
            this.regions = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Country constructor doesn't allow null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Nation getNation() {
        return nation;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Administration getArmy() {
        return army;
    }

    /**
     * DOCUMENT ME!
     *
     * @param army DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setArmy(Administration army) {
        if (army != null) {
            if (army.getPosition() == this) {
                this.army = army;
            } else {
                throw new IllegalArgumentException(
                    "You can't set an army whose country is not this.");
            }
        } else {
            this.army = null;
        }
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Administration getPolice() {
        return police;
    }

    /**
     * DOCUMENT ME!
     *
     * @param police DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setPolice(Administration police) {
        if (police != null) {
            if (police.getPosition() == this) {
                this.police = police;
            } else {
                throw new IllegalArgumentException(
                    "You can't set a police whose country is not this.");
            }
        } else {
            this.police = null;
        }
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Administration getJustice() {
        return justice;
    }

    /**
     * DOCUMENT ME!
     *
     * @param justice DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setJustice(Administration justice) {
        if (justice != null) {
            if (justice.getPosition() == this) {
                this.justice = justice;
            } else {
                throw new IllegalArgumentException(
                    "You can't set a justice whose country is not this.");
            }
        } else {
            this.police = null;
        }
    }

    //schools could also be considered as stateforces
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * DOCUMENT ME!
     *
     * @param currency DOCUMENT ME!
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    //may return-1
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getGDP() {
        return gDP;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gdp DOCUMENT ME!
     */
    public void setGDP(double gdp) {
        this.gDP = gdp;
    }

    //may return -1
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getGNP() {
        return gNP;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gnp DOCUMENT ME!
     */
    public void setGNP(double gnp) {
        this.gNP = gnp;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Image getFlag() {
        return flag;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     */
    public void setFlag(Image flag) {
        this.flag = flag;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public City getCapital() {
        return capital;
    }

    /**
     * DOCUMENT ME!
     *
     * @param capital DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //you should only set only a city which is in the cities Set
    public void setCapital(City capital) {
        if ((capital != null)) {
            if (cities.contains(capital)) {
                this.capital = capital;
            } else {
                throw new IllegalArgumentException(
                    "The capital must be in the Set of cities.");
            }
        } else {
            throw new IllegalArgumentException("The capital must be non null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param city DOCUMENT ME!
     */
    public void addCity(City city) {
        cities.add(city);
    }

    /**
     * DOCUMENT ME!
     *
     * @param city DOCUMENT ME!
     */
    public void removeCity(City city) {
        cities.remove(city);
        city.setCountry(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getCities() {
        return cities;
    }

    /**
     * DOCUMENT ME!
     *
     * @param city DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean containsCity(City city) {
        return cities.contains(city);
    }

    //some countries also have regions
    /**
     * DOCUMENT ME!
     *
     * @param region DOCUMENT ME!
     */
    protected void addRegion(Region region) {
        regions.add(region);
    }

    /**
     * DOCUMENT ME!
     *
     * @param region DOCUMENT ME!
     */
    public void removeRegion(Region region) {
        regions.remove(region);
        region.setCountry(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getRegions() {
        return regions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param region DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean containsRegion(Region region) {
        return regions.contains(region);
    }
}
