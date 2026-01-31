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

package org.jscience.social.law;


import org.jscience.social.sociology.Person;
import org.jscience.social.sociology.Human;

import org.jscience.natural.biology.BiologicalSex;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing biometric data and physical characteristics used 
 * for the identification of a person.
 * 
 * <p>This includes basic biological traits like sex, height, and weight, 
 * as well as identifying features like hair color, eye color, and disabilities.</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Biometrics {
    
    private BiologicalSex sex;
    private float height; // in meters
    private float weight; // nude weight in kilograms
    private Color skinColor;
    private Color hairColor;
    private Color eyesColor;
    private List<String> disabilities;
    private Image picture; // face picture
    private String ethnicity;
    private List<String> comments;
    private Human human; // callback to the human biological entity

    /**
     * Creates a new Biometrics object for a given person.
     *
     * @param person the person to associate these biometrics with
     * @throws IllegalArgumentException if person is null
     */
    public Biometrics(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null.");
        }
        this.sex = BiologicalSex.UNKNOWN;
        this.height = 0.0f;
        this.weight = 0.0f;
        this.skinColor = Color.GRAY;
        this.hairColor = Color.GRAY;
        this.eyesColor = Color.GRAY;
        this.disabilities = new ArrayList<>();
        this.picture = null;
        this.ethnicity = "";
        this.comments = new ArrayList<>();
        
        // Note: Assumes person has a way to relate to human, 
        // using the person as the base for initialization.
    }

    /**
     * Returns the sex of the individual.
     * @return the sex
     */
    public BiologicalSex getSex() {
        return sex;
    }

    /**
     * Sets the sex of the individual.
     * @param sex the sex
     */
    public void setSex(BiologicalSex sex) {
        this.sex = sex;
    }

    /**
     * Returns the height of the individual.
     * @return height in meters
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the height of the individual.
     * @param height height in meters
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Returns the weight of the individual.
     * @return weight in kilograms
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Sets the weight of the individual.
     * @param weight weight in kilograms
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Returns the primary skin color.
     * @return the skin color
     */
    public Color getSkinColor() {
        return skinColor;
    }

    /**
     * Sets the primary skin color.
     * @param skinColor the color to set
     * @throws IllegalArgumentException if skinColor is null
     */
    public void setSkinColor(Color skinColor) {
        if (skinColor == null) {
            throw new IllegalArgumentException("Skin color cannot be null.");
        }
        this.skinColor = skinColor;
    }

    /**
     * Returns the current hair color.
     * @return the hair color
     */
    public Color getHairColor() {
        return hairColor;
    }

    /**
     * Sets the current hair color.
     * @param hairColor the color to set
     * @throws IllegalArgumentException if hairColor is null
     */
    public void setHairColor(Color hairColor) {
        if (hairColor == null) {
            throw new IllegalArgumentException("Hair color cannot be null.");
        }
        this.hairColor = hairColor;
    }

    /**
     * Returns the eye color.
     * @return the eye color
     */
    public Color getEyesColor() {
        return eyesColor;
    }

    /**
     * Sets the eye color.
     * @param eyesColor the color to set
     * @throws IllegalArgumentException if eyesColor is null
     */
    public void setEyesColor(Color eyesColor) {
        if (eyesColor == null) {
            throw new IllegalArgumentException("Eye color cannot be null.");
        }
        this.eyesColor = eyesColor;
    }

    /**
     * Returns the list of reported disabilities.
     * @return the list of disabilities
     */
    public List<String> getDisabilities() {
        return disabilities;
    }

    /**
     * Adds a specific disability description.
     * @param disability the disability to add
     * @throws IllegalArgumentException if disability is null
     */
    public void addDisability(String disability) {
        if (disability == null) {
            throw new IllegalArgumentException("Disability description cannot be null.");
        }
        disabilities.add(disability);
    }

    /**
     * Removes a specific disability description.
     * @param disability the disability to remove
     */
    public void removeDisability(String disability) {
        disabilities.remove(disability);
    }

    /**
     * Removes the most recently added disability.
     */
    public void removeLastDisability() {
        if (!disabilities.isEmpty()) {
            disabilities.remove(disabilities.size() - 1);
        }
    }

    /**
     * Sets the complete list of disabilities.
     * @param disabilities the list of disabilities
     * @throws IllegalArgumentException if list is null or contains non-String elements
     */
    public void setDisabilities(List<String> disabilities) {
        if (disabilities == null) {
            throw new IllegalArgumentException("The list of disabilities cannot be null.");
        }
        this.disabilities = new ArrayList<>(disabilities);
    }

    /**
     * Returns the identification picture of the person's face.
     * @return the image, or null if not available
     */
    public Image getPicture() {
        return picture;
    }

    /**
     * Sets the identification picture.
     * @param picture the image to set
     */
    public void setPicture(Image picture) {
        this.picture = picture;
    }

    /**
     * Returns the ethnic group or ethnicity.
     * @return the ethnicity string
     */
    public String getEthnicity() {
        return ethnicity;
    }

    /**
     * Sets the ethnic group or ethnicity.
     * @param ethnicity the ethnicity string
     * @throws IllegalArgumentException if ethnicity is null
     */
    public void setEthnicity(String ethnicity) {
        if (ethnicity == null) {
            throw new IllegalArgumentException("Ethnicity cannot be null.");
        }
        this.ethnicity = ethnicity;
    }

    /**
     * Adds an identification-related comment.
     * @param comment the comment to add
     * @throws IllegalArgumentException if comment is null
     */
    public void addComment(String comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null.");
        }
        comments.add(comment);
    }

    /**
     * Removes a specific comment.
     * @param comment the comment to remove
     */
    public void removeComment(String comment) {
        comments.remove(comment);
    }

    /**
     * Removes the most recently added comment.
     */
    public void removeLastComment() {
        if (!comments.isEmpty()) {
            comments.remove(comments.size() - 1);
        }
    }

    /**
     * Sets the complete list of comments.
     * @param comments the list of comments
     * @throws IllegalArgumentException if list is null
     */
    public void setComments(List<String> comments) {
        if (comments == null) {
            throw new IllegalArgumentException("The list of comments cannot be null.");
        }
        this.comments = new ArrayList<>(comments);
    }

    /**
     * Returns the human entity associated with these biometrics.
     * @return the human object
     */
    public Human getHuman() {
        return human;
    }
}

