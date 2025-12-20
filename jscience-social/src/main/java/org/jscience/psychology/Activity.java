/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.psychology;

import java.util.*;

/**
 * Represents a human activity with goals and sub-activities.
 * <p>
 * Modernized from v1 with hierarchical activity modeling.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Activity {

    public enum Category {
        WORK, LEISURE, SOCIAL, PHYSICAL, MENTAL, CREATIVE, DOMESTIC, SPIRITUAL
    }

    private final String name;
    private String description;
    private String goal;
    private Category category;
    private final List<Activity> subActivities = new ArrayList<>();
    private final List<Behavior> behaviors = new ArrayList<>();
    private int durationMinutes;
    private double energyCost; // 0.0 to 1.0

    public Activity(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public Activity(String name, Category category) {
        this(name);
        this.category = category;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGoal() {
        return goal;
    }

    public Category getCategory() {
        return category;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public double getEnergyCost() {
        return energyCost;
    }

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDurationMinutes(int duration) {
        this.durationMinutes = duration;
    }

    public void setEnergyCost(double cost) {
        this.energyCost = Math.max(0, Math.min(1, cost));
    }

    // Sub-activities
    public void addSubActivity(Activity activity) {
        subActivities.add(activity);
    }

    public List<Activity> getSubActivities() {
        return Collections.unmodifiableList(subActivities);
    }

    public boolean hasSubActivities() {
        return !subActivities.isEmpty();
    }

    // Behaviors
    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
    }

    public List<Behavior> getBehaviors() {
        return Collections.unmodifiableList(behaviors);
    }

    /**
     * Returns total duration including sub-activities.
     */
    public int getTotalDuration() {
        int total = durationMinutes;
        for (Activity sub : subActivities) {
            total += sub.getTotalDuration();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Activity '%s' (%s) - %d min", name, category, durationMinutes);
    }

    // Common activities
    public static Activity work() {
        Activity a = new Activity("Work", Category.WORK);
        a.setDurationMinutes(480);
        a.setEnergyCost(0.6);
        return a;
    }

    public static Activity sleep() {
        Activity a = new Activity("Sleep", Category.PHYSICAL);
        a.setDurationMinutes(480);
        a.setEnergyCost(-1.0); // Restores energy
        return a;
    }

    public static Activity exercise() {
        Activity a = new Activity("Exercise", Category.PHYSICAL);
        a.setDurationMinutes(60);
        a.setEnergyCost(0.4);
        return a;
    }
}
