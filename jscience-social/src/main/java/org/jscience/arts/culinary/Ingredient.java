package org.jscience.arts.culinary;

import java.util.Objects;

/**
 * Represents an edible ingredient with nutritional information.
 */
public class Ingredient {

    private final String name;
    private final String description;
    private double water;        // percent
    private double protein;      // percent
    private double fat;          // percent
    private double carbohydrate; // percent
    private double fiber;        // percent
    private double ash;          // percent
    private double calories;     // per 100g

    public Ingredient(String name, String description) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = description != null ? description : "";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Setters for nutritional info
    public void setNutritionalInfo(double water, double protein, double fat, 
                                   double carbohydrate, double fiber, double ash, double calories) {
        this.water = water;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        this.fiber = fiber;
        this.ash = ash;
        this.calories = calories;
    }

    public double getWater() { return water; }
    public double getProtein() { return protein; }
    public double getFat() { return fat; }
    public double getCarbohydrate() { return carbohydrate; }
    public double getFiber() { return fiber; }
    public double getAsh() { return ash; }
    public double getCalories() { return calories; }

    @Override
    public String toString() {
        return String.format("%s (%.1f kcal/100g)", name, calories);
    }
}
