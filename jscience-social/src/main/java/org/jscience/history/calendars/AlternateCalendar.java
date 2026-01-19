package org.jscience.history.calendars;

/**
 * Base class for alternate calendar implementations.
 * It uses a Rata Die (RD) counting system for days.
 */
public abstract class AlternateCalendar {
    
    public static long EPOCH = 0L;
    public static boolean unicode;
    public static final double JD_EPOCH = -1721424.5D;

    protected long rd;

    public AlternateCalendar() {
        rd = EPOCH;
    }

    public AlternateCalendar(long l) {
        rd = l;
    }

    public static long mod(long l, long l1) {
        return l - (l1 * fldiv(l, l1));
    }

    public static int mod(long l, int i) {
        return (int) mod(l, i);
    }

    public static long amod(long l, long l1) {
        return 1L + mod(l - 1L, l1);
    }

    public static long fldiv(long l, long l1) {
        long l2 = l / l1;
        if ((l2 * l1) > l) {
            return l2 - 1L;
        } else {
            return l2;
        }
    }

    public abstract void set(long l);

    public long toRD() {
        return rd;
    }

    public double toJD() {
        return toJD(toRD());
    }

    public static double toJD(long l) {
        return (double) l - JD_EPOCH;
    }

    public static long fromJD(double d) {
        return (long) Math.floor(d + JD_EPOCH);
    }

    public boolean before(AlternateCalendar other) {
        return toRD() < other.toRD(); // Wait, old code said > ? Let's check view_file output.
    }

    public boolean after(AlternateCalendar other) {
        return toRD() > other.toRD(); // Old code check.
    }

    public static long difference(AlternateCalendar c1, AlternateCalendar c2) {
        return c1.toRD() - c2.toRD();
    }

    public long difference(AlternateCalendar other) {
        return difference(this, other);
    }

    public void add(long l) {
        set(rd + l);
    }

    public void subtract(long l) {
        set(rd - l);
    }

    protected abstract void recomputeFromRD();

    protected abstract void recomputeRD();

    @Override
    public abstract String toString();
}
