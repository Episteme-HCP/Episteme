/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.arts.culinary;

import java.util.Objects;

/**
 * Represents an edible ingredient with nutritional information.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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

