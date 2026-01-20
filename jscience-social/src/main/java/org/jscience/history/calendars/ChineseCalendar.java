//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
package org.jscience.history.calendars;

import java.util.Enumeration;


// Referenced classes of package calendars:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ChineseCalendar extends MonthDayYear {
    /** DOCUMENT ME! */
    public static final String[] MONTHS = {
            "Yushui", "Chufen", "Guyu", "Xiaoman", "Xiazhi", "Dashu", "Chushu",
            "Qiufen", "Shuangjiang", "Xiaoxue", "Dongzhi", "Dahan"
        };

    /** DOCUMENT ME! */
    public static final long EPOCH = (new GregorianCalendar(2, 15, -2636)).toRD();

    /** DOCUMENT ME! */
    public static final int EPOCHYEAR = -2636;

    /** DOCUMENT ME! */
    private int cycle;

    /** DOCUMENT ME! */
    private boolean leap;

/**
     * Creates a new ChineseCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public ChineseCalendar(long l) {
        super.rd = l;
        recomputeFromRD();
    }

/**
     * Creates a new ChineseCalendar object.
     *
     * @param i    DOCUMENT ME!
     * @param j    DOCUMENT ME!
     * @param k    DOCUMENT ME!
     * @param flag DOCUMENT ME!
     * @param l    DOCUMENT ME!
     */
    public ChineseCalendar(int i, int j, int k, boolean flag, int l) {
        set(i, j, k, flag, l);
    }

/**
     * Creates a new ChineseCalendar object.
     */
    public ChineseCalendar() {
        this(EPOCH);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int majorSolarTerm(long l) {
        double d = Moment.solarLongitude(Moment.universalFromLocal(
                    Moment.jdFromMoment(l), timeZone(l)));

        return (int) AlternateCalendar.amod(2 + (int) Math.floor(d / 30D), 12L);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double timeZone(long l) {
        return ((new GregorianCalendar(l)).getYear() >= 1929) ? 480D
                                                              : 465.66666666666669D;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double timeZone() {
        return timeZone(super.rd);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static long dateNextSolarLongitude(double d, int i) {
        double d1 = timeZone((long) d);

        return (long) Math.floor(Moment.momentFromJD(Moment.localFromUniversal(
                    Moment.dateNextSolarLongitude(Moment.universalFromLocal(
                            Moment.jdFromMoment(d), d1), i), d1)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static long majorSolarTermOnOrAfter(double d) {
        return dateNextSolarLongitude(d, 30);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static long newMoonOnOrAfter(long l) {
        double d = timeZone(l);

        return (long) Math.floor(Moment.momentFromJD(Moment.localFromUniversal(
                    Moment.newMoonAtOrAfter(Moment.universalFromLocal(
                            Moment.jdFromMoment(l), d)), d)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static long newMoonBefore(long l) {
        double d = timeZone(l);

        return (long) Math.floor(Moment.momentFromJD(Moment.localFromUniversal(
                    Moment.newMoonBefore(Moment.universalFromLocal(
                            Moment.jdFromMoment(l), d)), d)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean noMajorSolarTerm(long l) {
        return majorSolarTerm(l) == majorSolarTerm(newMoonOnOrAfter(l + 1L));
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean priorLeapMonth(long l, long l1) {
        return (l1 >= l) &&
        (noMajorSolarTerm(l1) || priorLeapMonth(l, newMoonBefore(l1)));
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void recomputeFromRD() {
        int i = (new GregorianCalendar(super.rd)).getYear();
        long l = majorSolarTermOnOrAfter((new GregorianCalendar(12, 15, i - 1)).toRD());
        long l1 = majorSolarTermOnOrAfter((new GregorianCalendar(12, 15, i)).toRD());
        long l2;
        long l3;

        if ((l <= super.rd) && (super.rd < l1)) {
            l2 = newMoonOnOrAfter(l + 1L);
            l3 = newMoonBefore(l1 + 1L);
        } else {
            l2 = newMoonOnOrAfter(l1 + 1L);
            l3 = newMoonBefore(majorSolarTermOnOrAfter(
                        (new GregorianCalendar(12, 15, i + 1)).toRD()) + 1L);
        }

        long l4 = newMoonBefore(super.rd + 1L);
        boolean flag = Math.round((double) (l3 - l2) / 29.530588853000001D) == 12L;
        super.month = (int) Math.round((double) (l4 - l2) / 29.530588853000001D);

        if (flag && priorLeapMonth(l2, l4)) {
            super.month--;
        }

        super.month = (int) AlternateCalendar.amod(super.month, 12L);
        leap = flag && noMajorSolarTerm(l4) &&
            !priorLeapMonth(l2, newMoonBefore(l4));

        int j = i - -2636;

        if ((super.month < 11) ||
                (super.rd > (new GregorianCalendar(7, 1, i)).toRD())) {
            j++;
        }

        cycle = (int) Math.floor((double) (j - 1) / 60D) + 1;
        super.year = (int) AlternateCalendar.amod(j, 60L);
        super.day = (int) ((super.rd - l4) + 1L);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static long newYear(int i) {
        long l = majorSolarTermOnOrAfter((new GregorianCalendar(12, 15, i - 1)).toRD());
        long l1 = majorSolarTermOnOrAfter((new GregorianCalendar(12, 15, i)).toRD());
        long l2 = newMoonOnOrAfter(l + 1L);
        long l3 = newMoonOnOrAfter(l2 + 1L);
        long l4 = newMoonBefore(l1 + 1L);

        if ((Math.round((double) (l4 - l2) / 29.530588853000001D) == 12L) &&
                (noMajorSolarTerm(l2) || noMajorSolarTerm(l3))) {
            return newMoonOnOrAfter(l3 + 1L);
        } else {
            return l3;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void recomputeRD() {
        int i = ((((cycle - 1) * 60) + super.year) - 1) + -2636;
        long l = newYear(i);
        long l1 = newMoonOnOrAfter(l + (long) ((super.month - 1) * 29));
        ChineseCalendar chinese = new ChineseCalendar(l1);
        long l2;

        if ((super.month == ((MonthDayYear) (chinese)).month) &&
                (leap == chinese.leap)) {
            l2 = l1;
        } else {
            l2 = newMoonOnOrAfter(l1 + 1L);
        }

        super.rd = (l2 + (long) super.day) - 1L;
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public synchronized void set(int i, int j, int k) {
        set((int) Math.floor((double) (k - 1) / 60D) + 1,
            (int) AlternateCalendar.amod(k, 60L), i, false, j);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     * @param flag DOCUMENT ME!
     * @param l DOCUMENT ME!
     */
    public synchronized void set(int i, int j, int k, boolean flag, int l) {
        super.month = k;
        super.day = l;
        cycle = i;
        super.year = j;
        leap = flag;
        recomputeRD();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getLeap() {
        return leap;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSuffix() {
        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String monthName() {
        return MONTHS[super.month - 1] + (leap ? "(leap)" : "");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Enumeration getMonths() {
        return new ArrayEnumeration(MONTHS);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String yearName() {
        String[] as = {
                "", "Jia", "Yi", "Bing", "Ding", "Wu", "Ji", "Geng", "Xin",
                "Ren", "Gui"
            };
        String[] as1 = {
                "", "Zi (Rat)", "Chou (Ox)", "Yin (Tiger)", "Mao (Hare)",
                "Chen (Dragon)", "Si (Snake)", "Wu (Horse)", "Wei (Sheep)",
                "Shen (Monkey)", "You (Fowl)", "Xu (Dog)", "Hai (Pig)"
            };

        return as[(int) AlternateCalendar.amod(super.year, 10L)] + "-" +
        as1[(int) AlternateCalendar.amod(super.year, 12L)];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return super.day + " " + monthName() + ", Year " + super.year + ": " +
        yearName() + ", cycle " + cycle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        int i = 1;
        int j = 1;
        int k = 1;
        int l = 1;

        try {
            i = Integer.parseInt(args[0]);
            j = Integer.parseInt(args[1]);
            k = Integer.parseInt(args[2]);
            l = Integer.parseInt(args[3]);
        } catch (Exception _ex) {
        }

        GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
        ChineseCalendar chinese = new ChineseCalendar(gregorian.toRD());
        System.out.println(chinese);
        chinese.set(l, k, i, false, j);
        System.out.println(chinese + ": " + chinese.toRD());

        try {
            chinese.set(Long.parseLong(args[4]));
        } catch (Exception _ex) {
        }

        System.out.println(chinese.toRD() + ": " + chinese);
    }
}
