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
public class ModifiedHinduLunarCalendar extends OldHinduLunarCalendar {
    /** DOCUMENT ME! */
    protected static final int LUNARERA = 3044;

    /** DOCUMENT ME! */
    protected static ModifiedHinduCalendar mh;

    /** DOCUMENT ME! */
    protected boolean leapday;

/**
     * Creates a new ModifiedHinduLunarCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public ModifiedHinduLunarCalendar(long l) {
        super(l);
    }

/**
     * Creates a new ModifiedHinduLunarCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public ModifiedHinduLunarCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new ModifiedHinduLunarCalendar object.
     */
    public ModifiedHinduLunarCalendar() {
    }

/**
     * Creates a new ModifiedHinduLunarCalendar object.
     *
     * @param i     DOCUMENT ME!
     * @param flag  DOCUMENT ME!
     * @param j     DOCUMENT ME!
     * @param flag1 DOCUMENT ME!
     * @param k     DOCUMENT ME!
     */
    public ModifiedHinduLunarCalendar(int i, boolean flag, int j,
        boolean flag1, int k) {
        set(i, flag, j, flag1, k);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param flag DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param flag1 DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public synchronized void set(int i, boolean flag, int j, boolean flag1,
        int k) {
        super.month = i;
        super.leap = flag;
        super.day = j;
        leapday = flag1;
        super.year = k;
        recomputeRD();
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void recomputeFromRD() {
        double d = super.rd - OldHinduSolarCalendar.EPOCH;
        double d1 = ModifiedHinduCalendar.sunrise(d);
        super.day = ModifiedHinduCalendar.lunarDay(d1);
        leapday = super.day == ModifiedHinduCalendar.lunarDay(ModifiedHinduCalendar.sunrise(d -
                    1.0D));

        double d2 = ModifiedHinduCalendar.newMoon(d1);
        double d3 = ModifiedHinduCalendar.newMoon(Math.floor(d2) + 35D);
        int i = ModifiedHinduCalendar.zodiac(d2);
        super.leap = i == ModifiedHinduCalendar.zodiac(d3);
        super.month = (int) AlternateCalendar.amod(i + 1, 12L);
        super.year = ModifiedHinduCalendar.calYear(d3) - 3044 -
            ((!super.leap || (super.month != 1)) ? 0 : (-1));
    }

    /**
     * DOCUMENT ME!
     *
     * @param modhindulunar DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean precedes(ModifiedHinduLunarCalendar modhindulunar) {
        return (super.year < ((MonthDayYear) (modhindulunar)).year) ||
        ((super.year == ((MonthDayYear) (modhindulunar)).year) &&
        ((super.month < ((MonthDayYear) (modhindulunar)).month) ||
        ((super.month == ((MonthDayYear) (modhindulunar)).month) &&
        ((super.leap && !((OldHinduLunarCalendar) (modhindulunar)).leap) ||
        ((super.leap == ((OldHinduLunarCalendar) (modhindulunar)).leap) &&
        ((super.day < ((MonthDayYear) (modhindulunar)).day) ||
        ((super.day == ((MonthDayYear) (modhindulunar)).day) && !leapday &&
        modhindulunar.leapday)))))));
    }

    /**
     * DOCUMENT ME!
     *
     * @throws InconsistentDateException DOCUMENT ME!
     */
    public synchronized void recomputeRD() {
        int i = super.year + 3044;
        OldHinduLunarCalendar oldhindulunar = new OldHinduLunarCalendar(1L);

        try {
            oldhindulunar.set(super.month, super.leap, super.day, i);
        } catch (InconsistentDateException _ex) {
        }

        long l = oldhindulunar.toRD();
        long l1;

        if ((new ModifiedHinduLunarCalendar(l + 15L)).precedes(this)) {
            l1 = l + 29L;
        } else if (precedes(new ModifiedHinduLunarCalendar(l - 15L))) {
            l1 = l - 29L - 1L;
        } else {
            l1 = l;
        }

        long l2 = l1 - 4L;
        long l3 = l1 + 4L;
        long l4 = (l2 + l3) / 2L;

        do {
            l4 = (l2 + l3) / 2L;

            if ((new ModifiedHinduLunarCalendar(l4)).precedes(this)) {
                l2 = l4;
            } else {
                l3 = l4;
            }
        } while ((l3 - l2) >= 2L);

        ModifiedHinduLunarCalendar modhindulunar = new ModifiedHinduLunarCalendar(l4);

        if ((super.day == ((MonthDayYear) (modhindulunar)).day) &&
                (leapday == modhindulunar.leapday) &&
                (super.month == ((MonthDayYear) (modhindulunar)).month) &&
                (super.leap == ((OldHinduLunarCalendar) (modhindulunar)).leap) &&
                (super.year == ((MonthDayYear) (modhindulunar)).year)) {
            super.rd = l4;

            return;
        }

        modhindulunar = new ModifiedHinduLunarCalendar(l4 - 1L);

        if ((super.day == ((MonthDayYear) (modhindulunar)).day) &&
                (leapday == modhindulunar.leapday) &&
                (super.month == ((MonthDayYear) (modhindulunar)).month) &&
                (super.leap == ((OldHinduLunarCalendar) (modhindulunar)).leap) &&
                (super.year == ((MonthDayYear) (modhindulunar)).year)) {
            super.rd = l4 - 1L;

            return;
        }

        modhindulunar = new ModifiedHinduLunarCalendar(l4 + 1L);

        if ((super.day == ((MonthDayYear) (modhindulunar)).day) &&
                (leapday == modhindulunar.leapday) &&
                (super.month == ((MonthDayYear) (modhindulunar)).month) &&
                (super.leap == ((OldHinduLunarCalendar) (modhindulunar)).leap) &&
                (super.year == ((MonthDayYear) (modhindulunar)).year)) {
            super.rd = l4 + 1L;

            return;
        } else {
            super.rd = l4;
            throw new InconsistentDateException(
                "Invalid Hindu Lunar Date (RD: " + super.rd + "?)");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getLeapDay() {
        return leapday;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return super.day + (leapday ? "(leap) " : " ") + monthName() + " " +
        super.year + getSuffix();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSuffix() {
        return " V.E.";
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
        boolean flag;
        boolean flag1;

        try {
            i = Integer.parseInt(args[0]);
            j = Integer.parseInt(args[1]);
            k = Integer.parseInt(args[2]);
            flag = args[3].equals("l");
            flag1 = args[4].equals("ld");
        } catch (Exception _ex) {
            i = k = j = 1;
            flag = flag1 = false;
        }

        GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
        System.out.println(gregorian.toRD());
        System.out.println(gregorian + "\n");

        ModifiedHinduLunarCalendar modhindulunar = new ModifiedHinduLunarCalendar(gregorian.toRD());
        System.out.println(gregorian + "(" + modhindulunar.toRD() + "): " +
            modhindulunar);
        System.out.println("===============");
        modhindulunar.set(i, flag, j, flag1, k);
        System.out.println(modhindulunar + " => " + modhindulunar.toRD());
    }
}
