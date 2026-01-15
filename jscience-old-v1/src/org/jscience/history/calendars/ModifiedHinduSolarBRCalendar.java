//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
package org.jscience.history.calendars;

import org.jscience.mathematics.algebraic.numbers.ExactRational;


// Referenced classes of package calendars:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ModifiedHinduSolarBRCalendar extends OldHinduSolarCalendar {
    /** DOCUMENT ME! */
    protected static final int SOLARERA = 3179;

    /** DOCUMENT ME! */
    protected static ModifiedHinduBRCalendar mh;

/**
     * Creates a new ModifiedHinduSolarBRCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public ModifiedHinduSolarBRCalendar(long l) {
        super(l);
    }

/**
     * Creates a new ModifiedHinduSolarBRCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public ModifiedHinduSolarBRCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new ModifiedHinduSolarBRCalendar object.
     */
    public ModifiedHinduSolarBRCalendar() {
    }

/**
     * Creates a new ModifiedHinduSolarBRCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public ModifiedHinduSolarBRCalendar(int i, int j, int k) {
        set(i, j, k);
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void recomputeFromRD() {
        ExactRational bigrational = new ExactRational(dayCount());
        ExactRational bigrational1 = ModifiedHinduBRCalendar.sunrise(bigrational);
        super.month = ModifiedHinduBRCalendar.zodiac(bigrational1);
        super.year = ModifiedHinduBRCalendar.calYear(bigrational1) - 3179;

        ExactRational bigrational2;

        for (bigrational2 = bigrational.subtract(new ExactRational(3L))
                                       .subtract(new ExactRational(
                        ModifiedHinduBRCalendar.solarLongitude(bigrational1)
                                               .mod(new ExactRational(1800L))
                                               .divide(new ExactRational(60L))
                                               .floor()));
                ModifiedHinduBRCalendar.zodiac(ModifiedHinduBRCalendar.sunrise(
                        bigrational2)) != super.month;
                bigrational2 = bigrational2.add(new ExactRational(1L)))
            ;

        super.day = bigrational.subtract(bigrational2).intValue() + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modhindusolarbr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean precedes(ModifiedHinduSolarBRCalendar modhindusolarbr) {
        return (super.year < ((MonthDayYear) (modhindusolarbr)).year) ||
        ((super.year == ((MonthDayYear) (modhindusolarbr)).year) &&
        ((super.month < ((MonthDayYear) (modhindusolarbr)).month) ||
        ((super.month == ((MonthDayYear) (modhindusolarbr)).month) &&
        (super.day < ((MonthDayYear) (modhindusolarbr)).day))));
    }

    /**
     * DOCUMENT ME!
     *
     * @throws InconsistentDateException DOCUMENT ME!
     */
    public synchronized void recomputeRD() {
        ExactRational bigrational = new ExactRational(super.month - 1, 12L);
        bigrational = bigrational.add(new ExactRational(super.year + 3179))
                                 .multiply(ModifiedHinduBRCalendar.SIDEREALYEAR);

        long l = bigrational.floor().longValue();
        l += ((OldHinduSolarCalendar.EPOCH + (long) super.day) - 9L);

        ModifiedHinduSolarBRCalendar modhindusolarbr = new ModifiedHinduSolarBRCalendar(l);
        System.err.println("tmp: " + modhindusolarbr);

        if ((((MonthDayYear) (modhindusolarbr)).month == super.month) &&
                (((MonthDayYear) (modhindusolarbr)).year == super.year)) {
            l += (super.day - ((MonthDayYear) (modhindusolarbr)).day - 1);
        }

        for (; modhindusolarbr.precedes(this);
                System.err.println("tmp: " + modhindusolarbr + " => (" +
                    modhindusolarbr.toRD() + ")"))
            modhindusolarbr = new ModifiedHinduSolarBRCalendar(++l);

        super.rd = l;

        if ((((MonthDayYear) (modhindusolarbr)).day != super.day) ||
                (((MonthDayYear) (modhindusolarbr)).month != super.month) ||
                (((MonthDayYear) (modhindusolarbr)).year != super.year)) {
            throw new InconsistentDateException("Illegal Hindu Solar Date");
        } else {
            return;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getSuffix() {
        return " S.E.";
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

        ModifiedHinduSolarBRCalendar modhindusolarbr = new ModifiedHinduSolarBRCalendar(gregorian);
        System.out.println(gregorian + "(" + modhindusolarbr.toRD() + "): " +
            modhindusolarbr);
        System.out.println("===============");
        modhindusolarbr = new ModifiedHinduSolarBRCalendar(i, j, k);
        System.out.println("HinduCalendar " + i + " " + j + " " + k + ": " +
            modhindusolarbr.toRD());
    }
}
