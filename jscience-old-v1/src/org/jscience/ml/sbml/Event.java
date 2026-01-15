package org.jscience.ml.sbml;

import java.util.ArrayList;
import java.util.List;

/**
 * An instantaneous discontinuous change in the state of the model when a criterion is met.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */

public final class Event extends SBaseId {
    private final List eventAssignment;
    private SBase assignmentsElement;
    private String delay;
    private String timeUnits;
    private String trigger;

    /**
     * Creates a new instance of Event
     */

    public Event() {
        super();
        assignmentsElement = new SBase();
        eventAssignment = new ArrayList();
        setDelay(null);
    }

    /**
     * Getter for property assignmentsElement.
     *
     * @return Value of property assignmentsElement.
     */

    public SBase getAssignmentsElement() {
        return assignmentsElement;
    }

    /**
     * Getter for property delay.
     *
     * @return Value of property delay.
     */

    public String getDelay() {
        return delay;
    }

    /**
     * Getter for property eventAssignment.
     *
     * @return Value of property eventAssignment.
     */

    public List getEventAssignment() {
        return eventAssignment;
    }

    /**
     * Getter for property timeUnits.
     *
     * @return Value of property timeUnits.
     */

    public String getTimeUnits() {
        return timeUnits;
    }

    /**
     * Getter for property trigger.
     *
     * @return Value of property trigger.
     */

    public String getTrigger() {
        return trigger;
    }

    /**
     * Setter for property assignmentsElement.
     *
     * @param assignmentsElement New value of property assignmentsElement.
     */

    public void setAssignmentsElement(SBase assignmentsElement) {
        this.assignmentsElement = assignmentsElement;
    }

    /**
     * Sets the length of time after the event has fired that the event is executed.
     *
     * @param delay New value of property delay.
     */

    public void setDelay(String delay) {
        if (delay == null) {
            this.delay = "<math:math><math:cn>0</math:cn></math:math>";
            return;
        }
        assert delay.startsWith("<math:math>");
        this.delay = delay;
    }

    /**
     * Sets the units of time that apply to the delay field.
     *
     * @param timeUnits New value of property timeUnits.
     */

    public void setTimeUnits(String timeUnits) {
        this.timeUnits = timeUnits;
    }

    /**
     * Sets the MathML boolean expression that defines when an event is fired on the transition from false to true.
     *
     * @param trigger New value of property trigger.
     */

    public void setTrigger(String trigger) {
        assert trigger == null || trigger.startsWith("<math:math>");
        this.trigger = trigger;
    }

    /**
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */

    public String toString() {
        StringBuffer s = new StringBuffer("<event");
        if (id != null)
            s.append(" id=\"" + id + "\"");
        if (name != null)
            s.append(" name=\"" + name + "\"");
        if (timeUnits != null)
            s.append(" timeUnits=\"" + timeUnits + "\"");
        s.append(">\n");
        if (trigger != null)
            s.append("<trigger>" + trigger + "</trigger>\n");
        if (delay != null)
            s.append("<delay>" + delay + "</delay>\n");
        printList(s, eventAssignment, "<listOfEventAssignments>", "</listOfEventAssignments>");
        s.append(super.toString());
        s.append("</event>\n");
        return s.toString();
    }
}
