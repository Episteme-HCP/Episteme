package org.jscience.arts.culinary;

import java.util.*;

/**
 * Represents a recipe for cooking.
 */
public class Recipe {

    public enum Category {
        APPETIZER, MAIN_COURSE, SIDE_DISH, DESSERT, BEVERAGE,
        SOUP, SALAD, BREAKFAST, SNACK, SAUCE
    }

    public enum Cuisine {
        FRENCH, ITALIAN, CHINESE, JAPANESE, INDIAN, MEXICAN,
        AMERICAN, MEDITERRANEAN, THAI, KOREAN, MIDDLE_EASTERN
    }

    public enum Difficulty {
        EASY, MEDIUM, HARD, EXPERT
    }

    private final String name;
    private Category category;
    private Cuisine cuisine;
    private Difficulty difficulty;
    private int prepTimeMinutes;
    private int cookTimeMinutes;
    private int servings;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<String> steps = new ArrayList<>();

    public Recipe(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.difficulty = Difficulty.MEDIUM;
        this.servings = 4;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getPrepTimeMinutes() {
        return prepTimeMinutes;
    }

    public int getCookTimeMinutes() {
        return cookTimeMinutes;
    }

    public int getServings() {
        return servings;
    }

    public List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public List<String> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    // Setters
    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setPrepTimeMinutes(int time) {
        this.prepTimeMinutes = time;
    }

    public void setCookTimeMinutes(int time) {
        this.cookTimeMinutes = time;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void addIngredient(Ingredient ingredient) {
        if (ingredient != null) {
            ingredients.add(ingredient);
        }
    }

    public void addStep(String step) {
        if (step != null) {
            steps.add(step);
        }
    }

    public int getTotalTime() {
        return prepTimeMinutes + cookTimeMinutes;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) - %d min, serves %d",
                name, cuisine, category, getTotalTime(), servings);
    }
}


