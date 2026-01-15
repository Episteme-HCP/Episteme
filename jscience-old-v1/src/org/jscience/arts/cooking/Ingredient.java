package org.jscience.arts.cooking;

import org.jscience.economics.PotentialResource;

import org.jscience.measure.Amount;


/**
 * A class representing something that can be eaten (by humans).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//see for example http://www.fao.org/docrep/x5557e/x5557e00.htm or http://www.nal.usda.gov/fnic/foodcomp/Data/HG72/hg72_2002.pdf
//for a list of food see http://www.fao.org/infoods/nomenclature_en.stm or http://www.nal.usda.gov/fnic/foodcomp/index.html for foods, nutrients
public class Ingredient extends PotentialResource {
    /** DOCUMENT ME! */
    private float water; //percent

    /** DOCUMENT ME! */
    private float protein; //percent

    /** DOCUMENT ME! */
    private float fat; //percent

    /** DOCUMENT ME! */
    private float carbohydrate; //percent

    /** DOCUMENT ME! */
    private float fiber; //percent (specific carbohydrates)

    /** DOCUMENT ME! */
    private float ash; //percent

    /** DOCUMENT ME! */
    private float calories; //per 100grammes

/**
     * Creates a new Ingredient object.
     *
     * @param name        DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param amount      DOCUMENT ME!
     */
    public Ingredient(String name, String description, Amount amount) {
        super(name, description, amount);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getWater() {
        return water;
    }

    /**
     * DOCUMENT ME!
     *
     * @param water DOCUMENT ME!
     */
    public void setWater(float water) {
        this.water = water;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getProteins() {
        return protein;
    }

    /**
     * DOCUMENT ME!
     *
     * @param protein DOCUMENT ME!
     */
    public void setProteins(float protein) {
        this.protein = protein;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getFat() {
        return fat;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fat DOCUMENT ME!
     */
    public void setFat(float fat) {
        this.fat = fat;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getCarbohydrates() {
        return carbohydrate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param carbohydrate DOCUMENT ME!
     */
    public void setCarbohydrates(float carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getFibers() {
        return fiber;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fiber DOCUMENT ME!
     */
    public void setFibers(float fiber) {
        this.fiber = fiber;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAsh() {
        return ash;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ash DOCUMENT ME!
     */
    public void setAsh(float ash) {
        this.ash = ash;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getCalories() {
        return calories;
    }

    /**
     * DOCUMENT ME!
     *
     * @param calories DOCUMENT ME!
     */
    public void setCalories(float calories) {
        this.calories = calories;
    }
}
