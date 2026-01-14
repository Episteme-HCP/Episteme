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

package org.jscience.law;

import org.jscience.biology.BiologyConstants;
import org.jscience.biology.human.Human;

import org.jscience.sociology.Person;

import java.awt.*;

import java.util.Iterator;
import java.util.Vector;


/**
 * A class representing elements that allow identification of the
 * considered person
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you should extend this class to provide:
//ethnic specific parameters (caucasian...), religion...
//fingerprints
//see also http://www.oasis-open.org/committees/documents.php?wg_abbrev=humanmarkup for Human Markup Language
//these fields are mostly independant from age but think that even sex can change as the result of surgery
//for DNA samples (use cells)
//also see VCard
public class Biometrics extends Object {
    /** DOCUMENT ME! */
    private int sex; //see BiologyConstants, also use comments if anything spectacular

    /** DOCUMENT ME! */
    private float size; //in meters

    /** DOCUMENT ME! */
    private float weight; //nude weight in kilos

    /** DOCUMENT ME! */
    private Color skinColor; //this is the main color also some disease may be to consider, use comments

    /** DOCUMENT ME! */
    private Color hairColor; //this is the visible color, may be different from natural color

    /** DOCUMENT ME! */
    private Color eyesColor; //this is the color of both eyes, should they be different, use comments

    /** DOCUMENT ME! */
    private Vector disabilities; //a vector of String

    /** DOCUMENT ME! */
    private Image picture; //of the face

    /** DOCUMENT ME! */
    private String ethnicity; //see http://en.wikipedia.org/wiki/List_of_ethnic_groups

    /** DOCUMENT ME! */
    private Vector comments; //a vector of String

    /** DOCUMENT ME! */
    private Human human; //the person callback

/**
     * Creates a new Biometrics object.
     *
     * @param person DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Biometrics(Person person) {
        if (person != null) {
            sex = BiologyConstants.UNKNOWN;
            size = 0;
            weight = 0;
            skinColor = Color.GRAY;
            hairColor = Color.GRAY;
            eyesColor = Color.GRAY;
            disabilities = new Vector();
            picture = null;
            ethnicity = new String();
            comments = new Vector();
            this.human = human;
        } else {
            throw new IllegalArgumentException(
                "The Biometrics constructor doesn't allow null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSex() {
        return sex;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sex DOCUMENT ME!
     */
    public void setSex(int sex) {
        this.sex = sex;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getSize() {
        return size;
    }

    /**
     * DOCUMENT ME!
     *
     * @param size DOCUMENT ME!
     */
    public void setSize(float size) {
        this.size = size;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getWeight() {
        return weight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param weight DOCUMENT ME!
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getSkinColor() {
        return skinColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param skinColor DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setSkinColor(Color skinColor) {
        if (skinColor != null) {
            this.skinColor = skinColor;
        } else {
            throw new IllegalArgumentException("You can't set a null Color.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getHairColor() {
        return hairColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hairColor DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setHairColor(Color hairColor) {
        if (hairColor != null) {
            this.hairColor = hairColor;
        } else {
            throw new IllegalArgumentException("You can't set a null Color.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getEyesColor() {
        return eyesColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param eyeColor DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setEyesColor(Color eyeColor) {
        if (eyeColor != null) {
            this.eyesColor = eyesColor;
        } else {
            throw new IllegalArgumentException("You can't set a null Color.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getDisabilities() {
        return disabilities;
    }

    /**
     * DOCUMENT ME!
     *
     * @param disability DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addDisability(String disability) {
        if (disability != null) {
            disabilities.add(disability);
        } else {
            throw new IllegalArgumentException(
                "You can't add a null disability.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param disability DOCUMENT ME!
     */
    public void removeDisability(String disability) {
        disabilities.remove(disability);
    }

    //will do nothing it there is no disability
    /**
     * DOCUMENT ME!
     */
    public void removeLastDisability() {
        if (disabilities.size() > 0) {
            disabilities.remove(disabilities.size() - 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param disabilities DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setDisabilities(Vector disabilities) {
        Iterator iterator;
        boolean valid;

        if (disabilities != null) {
            iterator = disabilities.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof String;
            }

            if (valid) {
                this.disabilities = disabilities;
            } else {
                throw new IllegalArgumentException(
                    "The disabilities Set must contain only Strings.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set null disabilities.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //may return null
    public Image getPicture() {
        return picture;
    }

    /**
     * DOCUMENT ME!
     *
     * @param picture DOCUMENT ME!
     */
    public void setPicture(Image picture) {
        //if (picture != null) {
        this.picture = picture;

        //} else {
        //    throw new IllegalArgumentException("You can't set a null picture.");
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEthnicity() {
        return ethnicity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ethnicity DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setEthnicity(String ethnicity) {
        if (ethnicity != null) {
            this.ethnicity = ethnicity;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null ethnicity.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param comment DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addComment(String comment) {
        if (comment != null) {
            comments.add(comment);
        } else {
            throw new IllegalArgumentException("You can't add a null comment.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param comment DOCUMENT ME!
     */
    public void removeComment(String comment) {
        comments.remove(comment);
    }

    //will do nothing it there is no comment
    /**
     * DOCUMENT ME!
     */
    public void removeLastComment() {
        if (comments.size() > 0) {
            comments.remove(comments.size() - 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param comments DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setComments(Vector comments) {
        Iterator iterator;
        boolean valid;

        if (comments != null) {
            iterator = comments.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof String;
            }

            if (valid) {
                this.comments = comments;
            } else {
                throw new IllegalArgumentException(
                    "The comments Set must contain only Strings.");
            }
        } else {
            throw new IllegalArgumentException("You can't set null comments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Human getHuman() {
        return human;
    }
}
