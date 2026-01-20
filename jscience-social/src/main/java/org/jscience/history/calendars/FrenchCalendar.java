//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
package org.jscience.history.calendars;


// Referenced classes of package calendars:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class FrenchCalendar extends ModifiedFrenchCalendar {
    /** DOCUMENT ME! */
    protected static final double TIMEZONE = 9.3499999999999996D;

    /** DOCUMENT ME! */
    protected static final double TROPYEAR = 365.24219900000003D;

/**
     * Creates a new FrenchCalendar object.
     */
    public FrenchCalendar() {
        this(ModifiedFrenchCalendar.EPOCH);
    }

/**
     * Creates a new FrenchCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public FrenchCalendar(long l) {
    }

/**
     * Creates a new FrenchCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public FrenchCalendar(int i, int j, int k) {
        super(i, j, k);
    }

/**
     * Creates a new FrenchCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public FrenchCalendar(AlternateCalendar altcalendar) {
        super(altcalendar);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static long autumnEquinoxOnOrBefore(long l) {
        double d = Moment.solarLongitude(Moment.universalFromLocal((double) (l +
                    1L) - -1721424.5D, 9.3499999999999996D));
        double d1;

        if ((d > 150D) && (d < 180D)) {
            d1 = l - 370L;
        } else {
            d1 = (double) l -
                ((((double) (l - 260L) % 365.24219900000003D) +
                365.24219900000003D) % 365.24219900000003D);
        }

        double d2 = Moment.universalFromLocal(Moment.jdFromMoment(d1),
                9.3499999999999996D);
        d2 = Moment.dateNextSolarLongitude(d2, 90);
        d2 = Moment.localFromUniversal(d2, 9.3499999999999996D);
        d2 = Moment.apparentFromLocal(d2);

        return (long) Math.floor(d2 + -1721424.5D);
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void recomputeRD() {
        long l = autumnEquinoxOnOrBefore((long) Math.floor((double) ModifiedFrenchCalendar.EPOCH +
                    (365.24219900000003D * (double) (super.year - 1))));
        super.rd = (l - 1L) + (long) (30 * (super.month - 1)) +
            (long) super.day;
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void recomputeFromRD() {
        long l = autumnEquinoxOnOrBefore(super.rd);
        super.year = (int) Math.round(((double) l -
                (double) ModifiedFrenchCalendar.EPOCH) / 365.24219900000003D) +
            1;
        super.month = ((int) (super.rd - l) / 30) + 1;
        super.day = AlternateCalendar.mod(super.rd - l, 30) + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        int i;
        int j;
        int k;

        try {
            i = Integer.parseInt(args[0]);
            j = Integer.parseInt(args[1]);
            k = Integer.parseInt(args[2]);
        } catch (Exception _ex) {
            i = k = j = 1;
        }

        GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
        System.out.println(gregorian.toRD());
        System.out.println(gregorian + "\n");

        FrenchCalendar french = new FrenchCalendar(gregorian);
        System.out.println(gregorian + ": " + french);
        french.set(i, j, k);
        System.out.println("FrenchCalendar(" + i + "," + j + "," + k + "): " +
            french);
        System.out.println(french.toRD());
    }
}
