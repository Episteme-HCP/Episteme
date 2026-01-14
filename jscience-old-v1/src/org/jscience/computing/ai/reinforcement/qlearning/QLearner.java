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

package org.jscience.computing.ai.reinforcement.qlearning;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Random;

/**
 * Q-learning algorithm implementation. You should override this class
 * for your requirements.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public abstract class QLearner {
    /**
     * "Epsinon Greedy" selection method for selecting actions
     */
    public static final int EpsinonGreedy = 0;
    private QTable qTable;
    private double learningRate;
    private double discountFactor;
    private double epsinon; // used if this is an epsinon-greedy q-learner
    private double defaultValue;
    private int selectionPolicy;
    private Random random = null;

    /**
     * Initializes the learner with default parameters. Default parameters
     * are listed below:
     * <p/>
     * learningRate = 0.2;
     * discountFactor = 0.9;
     * epsinon = 0.75;
     * defaultValue = 0.0;
     * selectionPolicy = QLearner.EpsinonGreedy;
     */
    public void initialize() {
        this.learningRate = 0.2;
        this.discountFactor = 0.9;
        this.epsinon = 0.75;
        this.defaultValue = 0.0;
        this.selectionPolicy = QLearner.EpsinonGreedy;

        qTable = new QTable();
        random = new Random();
    }

    /**
     * Initializes the learner with given parameters.
     *
     * @param learningRate    learning rate
     * @param discountFactor  discount factor
     * @param selectionPolicy selection method
     */
    public void initialize(double learningRate, double discountFactor,
                           int selectionPolicy) {
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
        this.epsinon = 0.75;
        this.selectionPolicy = selectionPolicy;

        qTable = new QTable();
        random = new Random();
    }

    // Setter Methods

    /**
     * Changes the learning rate
     *
     * @param learningRate new learning rate
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Changes the discount factor
     *
     * @param dicountFactor new discount factor
     */
    public void setDiscountFactor(double dicountFactor) {
        this.discountFactor = discountFactor;
    }

    /**
     * Changes the selection method
     *
     * @param selectionPolicy new selection method
     */
    public void setSelectionPolicy(int selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    /**
     * Changes default valee for q-table entries.
     *
     * @param defaultValue new default value
     */
    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    // Getter Methods

    /**
     * Returns learning rate
     *
     * @return learning rate
     */
    public double getLearningRate() {
        return learningRate;
    }

    /**
     * Returns discount factor
     *
     * @return discount factor
     */
    public double getDiscountFactor() {
        return discountFactor;
    }

    /**
     * Returns the number of entries in q-table
     *
     * @return size of the q-table
     */
    public int getQTableSize() {
        return qTable.getSize();
    }

    /**
     * Returns the number of different states in the q-table
     *
     * @return number of different states in the q-table
     */
    public int getNumberOfStates() {
        return qTable.getNumberOfStates();
    }

    /**
     * Returns different states in the q-table.
     *
     * @return different states in the q-table
     */
    public Enumeration getStates() {
        return qTable.getStates();
    }

    /**
     * Returns all of the best actions for each states tried so far
     *
     * @return all of the best actions
     */
    public Collection getBestActions() {
        return qTable.getBestActions();
    }

    /**
     * Returns best reinforcement value for given state
     *
     * @param state state to look for best reinforcement value
     * @return best reinforcement value for given state
     * @throws QTableEntryNotFoundException if the state is not found in q-table
     */
    public double getBestValue(Object state)
            throws QTableEntryNotFoundException {
        return qTable.getBestValue(state);
    }

    // Other Methods

    /**
     * Updates q-table with given state, action, new state and reward information
     *
     * @param state    state before the action is done
     * @param action   action performed for given reward
     * @param newState new state observed after the action done
     * @param reward   reinforcement taken after the action is done
     */
    public void updateQTable(Object state, Object action, Object newState,
                             double reward) {
        double newQValue;

        try {
            if (qTable.hasStateActionPair(state, action) &&
                    qTable.hasState(newState)) {
                newQValue = ((1 - learningRate) * qTable.getValue(state, action)) +
                        (learningRate * (reward +
                                (discountFactor * qTable.getBestValue(newState))));
                qTable.updateValue(state, action, newQValue);
            } else if (!qTable.hasStateActionPair(state, action) &&
                    qTable.hasState(newState)) {
                newQValue = ((1 - learningRate) * defaultValue) +
                        (learningRate * (reward +
                                (discountFactor * qTable.getBestValue(newState))));
                qTable.updateValue(state, action, newQValue);
            } else if (qTable.hasStateActionPair(state, action) &&
                    !qTable.hasState(newState)) {
                newQValue = ((1 - learningRate) * qTable.getValue(state, action)) +
                        (learningRate * (reward + (discountFactor * defaultValue)));
                qTable.updateValue(state, action, newQValue);
            } else {
                newQValue = ((1 - learningRate) * defaultValue) +
                        (learningRate * (reward + (discountFactor * defaultValue)));
                qTable.updateValue(state, action, newQValue);
            }
        } catch (QTableEntryNotFoundException e1) {
            System.out.println(e1);
        }

        /*
       try {

         newQValue = ( (1 - learningRate) * qTable.getValue(key, action)) +
             (learningRate *
              (reward + (discountFactor * qTable.getBestValue(newState))));
         qTable.setValue(key, newQValue);

       }
       catch (QTableEntryNotFoundException e) {
         newQValue = ( (1 - learningRate) * qTable.getValue(key)) +
             (learningRate * (reward + (discountFactor * defaultValue)));
         qTable.updateValue(state, action, newQValue);
       }
       catch (NullPointerException e) {

         try {
           newQValue = ( (1 - learningRate) * defaultValue) +
               (learningRate *
                (reward + (discountFactor * qTable.getBestValue(newState))));
           qTable.setValue(key, newQValue);
         }
         catch (QTableEntryNotFoundException e1) {
           newQValue = ( (1 - learningRate) * defaultValue) +
               (learningRate * (reward + (discountFactor * defaultValue)));
           qTable.setValue(key, newQValue);
         }

       }
        */
    }

    /**
     * Selects an action based on given state
     *
     * @param state state to select action for
     * @return selected action
     */
    public Object selectAction(Object state) {
        switch (selectionPolicy) {
            case QLearner.EpsinonGreedy:
                return selectActionWithEpsinonGreedy(state);
        }

        return null;
    }

    private Object selectActionWithEpsinonGreedy(Object state) {
        if (epsinon < random.nextDouble()) {
            // Explore the Search Space (select a random action)
            return selectRandomAction(state);
        }

        // Else Go Along the Best Path
        try {
            return qTable.getBestAction(state);
        } catch (QTableEntryNotFoundException e) {
            return selectRandomAction(state);
        }
    }

    /*
    *        This method has to return action based on the state given.
    */

    /**
     * Selects an action randomly. This method should be implemented in subclasses,
     * since possible actions are not known by QLearner class.
     *
     * @param state state to select an action randomly
     * @return selected action
     */
    protected abstract Object selectRandomAction(Object state);
}
