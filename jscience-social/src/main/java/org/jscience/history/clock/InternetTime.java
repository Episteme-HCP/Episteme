package org.jscience.history.clock;

import org.jscience.measure.Quantity;

import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Time;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Represents Swatch Internet Time, dividing the day into 1000 ".beats".
 * It is based on Biel Mean Time (BMT), which is UTC+1.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class InternetTime extends org.jscience.history.time.Time {

    private static final long serialVersionUID = 2L;

    public static final int BEATS_PER_DAY = 1000;
    private static final long MILLIS_PER_DAY = 86400000L;

    @Attribute
    private int millibeats;
    @Attribute
    private int beats;
    @Attribute
    private int days;

    public InternetTime(double days, double beats, double millibeats) {
        if (days < 0 || beats < 0 || millibeats < 0) {
            throw new IllegalArgumentException("Time components cannot be negative");
        }
        initFromComponents(days, beats, millibeats);
    }

    public InternetTime(double siMillis) {
        if (siMillis < 0) {
            throw new IllegalArgumentException("Milliseconds cannot be negative");
        }
        initFromSiMillis(siMillis);
    }

    public InternetTime() {
        this((double) System.currentTimeMillis());
    }

    private void initFromComponents(double days, double beats, double millibeats) {
        this.millibeats = (int) (millibeats % 1000);
        beats += millibeats / 1000;
        this.beats = (int) (beats % BEATS_PER_DAY);
        this.days = (int) (days + beats / BEATS_PER_DAY);
    }

    private void initFromSiMillis(double siMillis) {
        double bmtMillis = (siMillis + 3600000.0); // UTC+1
        this.days = (int) (bmtMillis / MILLIS_PER_DAY);
        double dayMillis = bmtMillis % MILLIS_PER_DAY;
        double totalBeats = dayMillis / 86400.0;
        this.beats = (int) totalBeats;
        this.millibeats = (int) ((totalBeats - beats) * 1000.0);
    }

    @Override
    public Quantity<Time> getTime() {
        long totalMillibeats = (long)days * BEATS_PER_DAY * 1000L + (long)beats * 1000L + millibeats;
        // 1 beat = 86.4 s. 1 millibeat = 0.0864 s.
        return Quantities.create(org.jscience.mathematics.numbers.real.Real.of(totalMillibeats * 0.0864), Units.SECOND);
    }

    @Override
    public int getMilliseconds() { return millibeats; }
    @Override
    public int getSeconds() { return beats; }
    @Override
    public int getMinutes() { throw new UnsupportedOperationException("Internet Time does not use minutes"); }
    @Override
    public int getHours() { throw new UnsupportedOperationException("Internet Time does not use hours"); }
    @Override
    public int getDays() { return days; }

    @Override
    public void nextMillisecond() {
        millibeats++;
        if (millibeats >= 1000) {
            millibeats = 0;
            nextSecond();
        }
    }

    @Override
    public void nextSecond() {
        beats++;
        if (beats >= BEATS_PER_DAY) {
            beats = 0;
            days++;
        }
    }

    @Override
    public void nextMinute() { throw new UnsupportedOperationException("Internet Time does not use minutes"); }
    @Override
    public void nextHour() { throw new UnsupportedOperationException("Internet Time does not use hours"); }
    @Override
    public void nextDay() { days++; }

    @Override
    public String toString() {
        return String.format("%d d @%03d.%03d", days, beats, millibeats);
    }
}
