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

package org.jscience.social.psychology;

import org.jscience.core.util.Commented;
import org.jscience.core.util.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a structured activity composed of nested sub-activities or atomic behaviors.
 * Activities help organize behaviors into goal-oriented sequences for individuals or roles.
 * * @version 1.7
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Activity implements Named, Commented, Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private String comments;
    private String goal;
    private List<Activity> subActivities;
    private Set<Behavior> behaviors;
    private final java.util.Map<String, Object> traits = new java.util.HashMap<>();

    /**
     * Creates a new Activity with the specified name.
     *
     * @param name the name of the activity
     * @throws IllegalArgumentException if name is null or empty
     */
    public Activity(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            this.comments = "";
            this.goal = "";
            this.subActivities = new ArrayList<>();
            this.behaviors = null;
        } else {
            throw new IllegalArgumentException(
                "The Activity constructor can't have null or empty arguments.");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getComments() {
        return comments;
    }

    @Override
    public void setComments(String comments) {
        this.comments = Objects.requireNonNull(comments, "You can't set a null comment.");
    }

    @Override
    public java.util.Map<String, Object> getTraits() {
        return traits;
    }

    /**
     * Returns the goal of the activity.
     * @return the goal description
     */
    public String getGoal() {
        return goal;
    }

    /**
     * Sets the goal description for this activity.
     * @param goal the new goal
     * @throws NullPointerException if goal is null
     */
    public void setGoal(String goal) {
        this.goal = Objects.requireNonNull(goal, "You can't set a null goal.");
    }

    /**
     * Returns the list of sub-activities.
     * @return the sub-activities list
     */
    public List<Activity> getSubActivities() {
        return subActivities;
    }

    /**
     * Adds a sub-activity to this activity.
     * @param activity the activity to add
     * @throws IllegalArgumentException if this activity already has atomic behaviors
     * @throws NullPointerException if activity is null
     */
    public void addSubActivity(Activity activity) {
        if (behaviors == null) {
            subActivities.add(Objects.requireNonNull(activity, "You can't add a null Activity."));
        } else {
            throw new IllegalArgumentException(
                "You can only set sub activities when there is no behavior.");
        }
    }

    /**
     * Removes a sub-activity from this activity.
     * @param activity the activity to remove
     * @throws NullPointerException if activity is null
     */
    public void removeSubActivity(Activity activity) {
        subActivities.remove(Objects.requireNonNull(activity, "You can't remove null Activity."));
    }

    /**
     * Sets the sub-activities list.
     * @param activities the list of activities
     * @throws IllegalArgumentException if this activity already has atomic behaviors
     * @throws NullPointerException if activities is null
     */
    public void setSubActivities(List<Activity> activities) {
        if (behaviors == null) {
            this.subActivities = Objects.requireNonNull(activities, "You can't set a null activities list.");
        } else {
            throw new IllegalArgumentException(
                "You can only set sub activities when there is no behavior.");
        }
    }

    /**
     * Returns the set of atomic behaviors associated with this activity.
     * @return the behaviors set
     */
    public Set<Behavior> getBehaviors() {
        return behaviors;
    }

    /**
     * Sets the atomic behaviors for this activity.
     * @param behaviors the set of behaviors
     * @throws IllegalArgumentException if this activity already has sub-activities
     */
    public void setBehaviors(Set<Behavior> behaviors) {
        if (behaviors != null && !behaviors.isEmpty()) {
            if (subActivities.isEmpty()) {
                this.behaviors = behaviors;
            } else {
                throw new IllegalArgumentException(
                    "You can only set behaviors when there are no sub activities.");
            }
        } else {
            this.behaviors = null;
        }
    }
}

