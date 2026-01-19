package org.jscience.history.calendars;

/**
 * True French Republican Calendar.
 * Based on the astronomical Autumnal Equinox in Paris.
 */
public class FrenchCalendar extends ModifiedFrenchCalendar {
    
    // Paris Mean Time offset in minutes (approx 9m 21s)
    protected static final double TIMEZONE = 9.3499999999999996D; 
    protected static final double TROPYEAR = 365.24219900000003D;

    public FrenchCalendar() {
        this(ModifiedFrenchCalendar.EPOCH);
    }

    public FrenchCalendar(long l) {
        set(l); // Calls set in ModifiedFrenchCalendar which calls recomputeFromRD (overridden below)
    }

    public FrenchCalendar(int i, int j, int k) {
        super(i, j, k);
    }

    public FrenchCalendar(AlternateCalendar altcalendar) {
        super(altcalendar);
    }

    private static long autumnEquinoxOnOrBefore(long l) {
        // Calculate approx solar longitude
        // l is Rata Die.
        double d = Moment.solarLongitude(Moment.universalFromLocal((double) (l + 1L) - 1721424.5D, TIMEZONE));
        
        double d1;
        if ((d > 150D) && (d < 180D)) {
            d1 = l - 370L;
        } else {
            // Rough estimate back
            d1 = (double) l - ((((double) (l - 260L) % TROPYEAR) + TROPYEAR) % TROPYEAR);
        }

        double d2 = Moment.universalFromLocal(Moment.jdFromMoment(d1), TIMEZONE);
        d2 = Moment.dateNextSolarLongitude(d2, 180); // 180 = Autumnal Equinox
        d2 = Moment.localFromUniversal(d2, TIMEZONE);
        d2 = Moment.apparentFromLocal(d2);

        return (long) Math.floor(d2 + -1721424.5D);
    }

    @Override
    public synchronized void recomputeRD() {
        long l = autumnEquinoxOnOrBefore((long) Math.floor((double) ModifiedFrenchCalendar.EPOCH +
                    (TROPYEAR * (double) (super.year - 1))));
        super.rd = (l - 1L) + (long) (30 * (super.month - 1)) +
            (long) super.day;
    }

    @Override
    public synchronized void recomputeFromRD() {
        long l = autumnEquinoxOnOrBefore(super.rd);
        super.year = (int) Math.round(((double) l -
                (double) ModifiedFrenchCalendar.EPOCH) / TROPYEAR) + 1;
        
        long daysSinceNewYear = super.rd - l;
        super.month = (int) (daysSinceNewYear / 30) + 1;
        super.day = (int) (daysSinceNewYear % 30) + 1;
    }

    public static void main(String[] args) {
        int i, j, k;
        try {
            i = Integer.parseInt(args[0]);
            j = Integer.parseInt(args[1]);
            k = Integer.parseInt(args[2]);
        } catch (Exception e) {
            i = 1; j = 1; k = 1;
        }

        GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
        FrenchCalendar french = new FrenchCalendar(gregorian);
        System.out.println(gregorian + ": " + french);
    }
}
