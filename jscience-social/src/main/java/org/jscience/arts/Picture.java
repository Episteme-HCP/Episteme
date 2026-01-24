package org.jscience.arts;

import org.jscience.history.temporal.TemporalCoordinate;
import org.jscience.geography.Place;
import java.util.Objects;

/**
 * Represents a visual artwork such as a painting, drawing, or photograph.
 */
public class Picture extends Artwork {

    public enum Medium {
        OIL, WATERCOLOR, CHARCOAL, PENCIL, ACRYLIC, DIGITAL, PHOTOGRAPH, OTHER
    }

    public enum Support {
        CANVAS, WOOD, PAPER, FRESCO, WALL, COPPER, GLASS, DIGITAL
    }

    private final Medium medium;
    private final Support support;
    private double widthCm;
    private double heightCm;

    public Picture(String name, String description, TemporalCoordinate productionDate, 
                   Place productionPlace, ArtForm category, Medium medium, Support support) {
        super(name, description, productionDate, productionPlace, category);
        this.medium = Objects.requireNonNull(medium, "Medium cannot be null");
        this.support = Objects.requireNonNull(support, "Support cannot be null");
    }

    public Picture(String name, TemporalCoordinate productionDate, Medium medium) {
        this(name, "", productionDate, null, ArtForm.PAINTING, medium, Support.CANVAS);
    }

    public Medium getMedium() {
        return medium;
    }

    public Support getSupport() {
        return support;
    }

    public double getWidthCm() {
        return widthCm;
    }

    public void setWidthCm(double widthCm) {
        this.widthCm = widthCm;
    }

    public double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(double heightCm) {
        this.heightCm = heightCm;
    }

    @Override
    public String toString() {
        return String.format("%s (%s on %s)", getName(), medium, support);
    }
}
