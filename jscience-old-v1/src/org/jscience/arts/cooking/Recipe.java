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

package org.jscience.arts.cooking;

import org.jscience.economics.Community;
import org.jscience.economics.money.Currency;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import org.jscience.arts.ArtsConstants;
import org.jscience.arts.Artwork;

import org.jscience.util.Commented;

import java.util.*;


/**
 * A class representing the needed step to cook a dish.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//categories reprinted from: http://www.epicurious.com/e_eating/e02_recipes/browse_main.html
//MAIN INGREDIENT
//Beans, Beef, Berries, Cheese, Chocolate, Citrus, Dairy, Eggs, Fish, Fruits, Garlic, Ginger, Grains, Greens, Herbs, Lamb, Mushrooms, Mustard, Nuts, Olives, Onions, Pasta, Peppers, Pork, Potatoes, Poultry, Rice, Shellfish, Tomatoes, Vegetables
//CUISINE
//African, American, Asian, Caribbean, Eastern European, French, Greek, Indian, Italian, Jewish, Mediterranean, Mexican, Middle Eastern, Scandinavian, Spanish
//SPECIAL CONSIDERATIONS
//Kid-Friendly, Low Fat, Meatless
//PREPARATION METHOD
//Advance, Bake, Broil, Fry, Grill, Marinade, Microwave, No Cook, Poach, Quick, Roast, Saut�, Slow Cook, Steam, Stir Fry
//SEASON/OCCASION
//Christmas, Easter, Fall, Fourth of July, Hanukkah, New Year's, Picnics, Spring, Summer, Superbowl, Thanksgiving, Valentine's Day Winter
//COURSE/DISH
//Appetizers, Bread, Breakfast, Brunch, Condiments, Cookies, Desserts, Hors d'Oeuvres, Main Dish, Salads, Sandwiches, Sauces, Side Dish, Snacks, Soup, Vegetables
public class Recipe extends Artwork implements Commented {
    /** DOCUMENT ME! */
    private String category; //for example: the main ingredient, the cuisine, special considerations (fat free...), preparation method, season meal, dish

    /** DOCUMENT ME! */
    private int servingSize; //number of people

    /** DOCUMENT ME! */
    private int preparationTime; //in seconds

    /** DOCUMENT ME! */
    private int cookingTime; //time in the oven, where you don't need to be really doing cooking, in seconds

    /** DOCUMENT ME! */
    private Map ingredients; //ingredient (key), amount (and unit) (value)

    /** DOCUMENT ME! */
    private Vector steps; //Vector of String

    /** DOCUMENT ME! */
    private String comments; //for example the wine to drink with

/**
     * Creates a new Recipe object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     * @param identification  DOCUMENT ME!
     * @param authors         DOCUMENT ME!
     * @param category        DOCUMENT ME!
     * @param servingSize     DOCUMENT ME!
     * @param preparationTime DOCUMENT ME!
     * @param cookingTime     DOCUMENT ME!
     * @param ingredients     DOCUMENT ME!
     * @param steps           DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Recipe(String name, String description, Community producer,
        Date productionDate, Identification identification, Set authors,
        String category, int servingSize, int preparationTime, int cookingTime,
        Map ingredients, Vector steps) {
        super(name, description, Amount.ZERO, producer, producer.getPosition(),
            productionDate, identification, Amount.valueOf(0, Currency.USD),
            authors, ArtsConstants.COOKING);

        Iterator iterator;
        boolean valid;

        if ((category != null) && (ingredients != null) &&
                (ingredients.size() > 0) && (steps != null) &&
                (steps.size() > 0)) {
            iterator = steps.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof String;
            }

            if (valid) {
                iterator = ingredients.keySet().iterator();
                valid = true;

                while (iterator.hasNext() && valid) {
                    valid = iterator.next() instanceof Ingredient;
                }

                iterator = ingredients.values().iterator();

                while (iterator.hasNext() && valid) {
                    valid = iterator.next() instanceof String;
                }

                if (valid) {
                    this.category = category;
                    this.servingSize = servingSize;
                    this.preparationTime = preparationTime;
                    this.cookingTime = cookingTime;
                    this.ingredients = ingredients;
                    this.steps = steps;
                } else {
                    throw new IllegalArgumentException(
                        "The Map can consist only of Ingredient/String pairs.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The Vector can consist only of Strings.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Recipe constructor can't have null arguments (and steps and ingredients shouldn't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCategory() {
        return category;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getServingSize() {
        return servingSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPreparationTime() {
        return preparationTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCookingTime() {
        return cookingTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Map getIngredients() {
        return ingredients;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getSteps() {
        return steps;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comments DOCUMENT ME!
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
}
