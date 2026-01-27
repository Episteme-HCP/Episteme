package org.jscience.geography;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.earth.Place;
import org.jscience.earth.PlaceType;
import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Area;
import org.jscience.economics.money.Money;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * A region is an area, typically part of a country or the world, having definable characteristics but not always fixed boundaries.
 * Examples: "Middle East", "Silicon Valley", "Provence".
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Region extends Place {

    private static final long serialVersionUID = 1L;

    public enum SubType {
        CONTINENT, COUNTRY, STATE, PROVINCE, COUNTY, CITY, DISTRICT, OTHER
    }

    @Attribute
    private final SubType subType;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Region parent;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Region> subRegions = new HashSet<>();

    @Attribute
    private Quantity<Area> area;

    @Attribute
    private long population;

    @Attribute
    private String capital; // Name of the capital city/place

    @Attribute
    private String isoCode; // ISO 3166 code if applicable

    @Attribute
    private Quantity<Money> gdp;

    public Region(String name, SubType subType) {
        super(name, PlaceType.REGION);
        this.subType = Objects.requireNonNull(subType);
        this.area = Quantities.create(0.0, Units.SQUARE_METER);
    }

    public SubType getSubType() {
        return subType;
    }

    public Region getParent() {
        return parent;
    }

    public void setParent(Region parent) {
        this.parent = parent;
        if (parent != null) {
            parent.subRegions.add(this);
        }
    }

    public Set<Region> getSubRegions() {
        return Collections.unmodifiableSet(subRegions);
    }

    public Quantity<Area> getArea() {
        return area;
    }

    public void setArea(Quantity<Area> area) {
        this.area = area;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public Quantity<Money> getGdp() {
        return gdp;
    }

    public void setGdp(Quantity<Money> gdp) {
        this.gdp = gdp;
    }

    @Override
    public String toString() {
        return getName() + " (" + subType + ")";
    }
}
