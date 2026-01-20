package org.jscience.psychology;

import org.jscience.util.Commented;
import org.jscience.util.Named;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


//human and other species organize themselves around activities and a set of behaviors are associated to support the activity

//this class should be plugged in Individual and/or Role

//perhaps we should have a stronger system with "tasks (or activities) that are sequential or parallel and each can be sub divided into other activities or terminal behaviors" including preconditions to trigger the behaviors, a scheduler at Individual level and a blackboard of current activities (as well as needs and resources for the individuals ; a genetically encoded action selection system) (a repertoire of behaviors at Species level). Current system provides no ordering at all. (moreover current model allows for activities to be dependent upon themselves, ie cycles with subactivities)... probably all this should rathr be part of JRobotics after all.

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class Activity extends Object implements Named, Commented {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String comments;

    /** DOCUMENT ME! */
    private String goal;

    /** DOCUMENT ME! */
    private Vector subActivities;

    /** DOCUMENT ME! */
    private Set behaviors;

/**
     * Creates a new Activity object.
     *
     * @param name DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Activity(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            this.comments = new String();
            this.goal = new String();
            this.subActivities = new Vector();
            this.behaviors = null;
        } else {
            throw new IllegalArgumentException(
                "The Activity constructor can't have null or empty arguments.");
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
    public String getComments() {
        return comments;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comments DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setComments(String comments) {
        if (comments != null) {
            this.comments = comments;
        } else {
            throw new IllegalArgumentException("You can't set a null comment.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getGoal() {
        return goal;
    }

    /**
     * DOCUMENT ME!
     *
     * @param goal DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setGoal(String goal) {
        if (goal != null) {
            this.goal = goal;
        } else {
            throw new IllegalArgumentException("You can't set a null goal.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getSubActivities() {
        return subActivities;
    }

    /**
     * DOCUMENT ME!
     *
     * @param activity DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addSubActivity(Activity activity) {
        if (behaviors == null) {
            if (activity != null) {
                subActivities.add(activity);
            } else {
                throw new IllegalArgumentException(
                    "You can't add a null Activity.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can only set sub activities when there is no behavior.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param activity DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeSubActivity(Activity activity) {
        if (activity != null) {
            subActivities.remove(activity);
        } else {
            throw new IllegalArgumentException(
                "You can't remove null Activity.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param activities DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setSubActivities(Vector activities) {
        Iterator iterator;
        boolean valid;

        if (behaviors == null) {
            if (activities != null) {
                iterator = activities.iterator();
                valid = true;

                while (iterator.hasNext() && valid) {
                    valid = iterator.next() instanceof Activity;
                }

                if (valid) {
                    this.subActivities = activities;
                } else {
                    throw new IllegalArgumentException(
                        "The activities Vector must contain only Activities.");
                }
            } else {
                throw new IllegalArgumentException(
                    "You can't set a null activities Vector.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can only set sub activities when there is no behavior.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //may return null
    public Set getBehaviors() {
        return behaviors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param behaviors DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setBehaviors(Set behaviors) {
        Iterator iterator;
        boolean valid;

        if ((behaviors != null) && (behaviors.size() > 0)) {
            if (subActivities.size() == 0) {
                iterator = behaviors.iterator();
                valid = true;

                while (iterator.hasNext() && valid) {
                    valid = iterator.next() instanceof Behavior;
                }

                if (valid) {
                    this.behaviors = behaviors;
                } else {
                    throw new IllegalArgumentException(
                        "The behaviors Set must contain only Behaviors.");
                }
            } else {
                throw new IllegalArgumentException(
                    "You can only set Behaviors when there is no sub activity.");
            }
        }
    }
}
