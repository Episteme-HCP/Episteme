package org.jscience.history.time;

import java.io.Serializable;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Time;
import org.jscience.util.persistence.Persistent;

/**
 * An abstract base class for different time representation systems.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public abstract class Time implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Returns the total elapsed time as a quantity.
     *
     * @return the amount of time
     */
    public abstract Quantity<Time> getTime();

    public abstract int getMilliseconds();

    public abstract int getSeconds();

    public abstract int getMinutes();

    public abstract int getHours();

    public abstract int getDays();

    public abstract void nextMillisecond();

    public abstract void nextSecond();

    public abstract void nextMinute();

    public abstract void nextHour();

    public abstract void nextDay();

    @Override
    public abstract String toString();
}
