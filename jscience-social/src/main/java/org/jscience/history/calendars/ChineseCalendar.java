package org.jscience.history.calendars;


/**
 * Chinese Lunisolar Calendar.
 * Uses astronomical calculations for solar terms and new moons.
 */
public class ChineseCalendar extends MonthDayYear {
    
    public static final String[] MONTHS = {
            "Yushui", "Chufen", "Guyu", "Xiaoman", "Xiazhi", "Dashu", "Chushu",
            "Qiufen", "Shuangjiang", "Xiaoxue", "Dongzhi", "Dahan"
    };

    public static final long EPOCH = (new GregorianCalendar(2, 15, -2636)).toRD();
    public static final int EPOCHYEAR = -2636;

    private int cycle;
    private boolean leap;

    public ChineseCalendar() {
        this(EPOCH);
    }

    public ChineseCalendar(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    public ChineseCalendar(int cycle, int year, int month, boolean leap, int day) {
        set(cycle, year, month, leap, day);
    }

    public static double timeZone(long l) {
        // Before 1929, Beijing local time was used? 
        // 1929+, standard time +8h (480 min).
        // Before, 116.416666... E -> 7h 45m 40s -> 465.666 min? 
        return ((new GregorianCalendar(l)).getYear() >= 1929) ? 480D : 465.66666666666669D;
    }

    public static int majorSolarTerm(long l) {
        double d = Moment.solarLongitude(Moment.universalFromLocal(
                    Moment.jdFromMoment(l), timeZone(l)));
        return (int) AlternateCalendar.amod(2 + (int) Math.floor(d / 30D), 12L);
    }

    public static long dateNextSolarLongitude(double d, int i) {
        double d1 = timeZone((long) d);
        return (long) Math.floor(Moment.momentFromJD(Moment.localFromUniversal(
                    Moment.dateNextSolarLongitude(Moment.universalFromLocal(
                            Moment.jdFromMoment(d), d1), i), d1)));
    }

    public static long majorSolarTermOnOrAfter(double d) {
        return dateNextSolarLongitude(d, 30);
    }

    public static long newMoonOnOrAfter(long l) {
        double d = timeZone(l);
        return (long) Math.floor(Moment.momentFromJD(Moment.localFromUniversal(
                    Moment.newMoonAtOrAfter(Moment.universalFromLocal(
                            Moment.jdFromMoment(l), d)), d)));
    }

    public static long newMoonBefore(long l) {
        double d = timeZone(l);
        return (long) Math.floor(Moment.momentFromJD(Moment.localFromUniversal(
                    Moment.newMoonBefore(Moment.universalFromLocal(
                            Moment.jdFromMoment(l), d)), d)));
    }

    public static boolean noMajorSolarTerm(long l) {
        return majorSolarTerm(l) == majorSolarTerm(newMoonOnOrAfter(l + 1L));
    }

    public static boolean priorLeapMonth(long l, long l1) {
        return (l1 >= l) && (noMajorSolarTerm(l1) || priorLeapMonth(l, newMoonBefore(l1)));
    }

    @Override
    protected synchronized void recomputeFromRD() {
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
        leap = flag && noMajorSolarTerm(l4) && !priorLeapMonth(l2, newMoonBefore(l4));

        int j = i - EPOCHYEAR;
        if ((super.month < 11) || (super.rd > (new GregorianCalendar(7, 1, i)).toRD())) {
            j++;
        }

        cycle = (int) Math.floor((double) (j - 1) / 60D) + 1;
        super.year = (int) AlternateCalendar.amod(j, 60L);
        super.day = (int) ((super.rd - l4) + 1L);
    }

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

    public synchronized void set(int cycle, int year, int month, boolean leap, int day) {
        this.cycle = cycle;
        super.year = year;
        super.month = month;
        this.leap = leap;
        super.day = day;
        recomputeRD();
    }

    @Override
    protected synchronized void recomputeRD() {
        int i = ((((cycle - 1) * 60) + super.year) - 1) + EPOCHYEAR;
        long l = newYear(i);
        long l1 = newMoonOnOrAfter(l + (long) ((super.month - 1) * 29));
        ChineseCalendar chinese = new ChineseCalendar(l1);
        
        long l2;
        if ((super.month == chinese.month) && (leap == chinese.leap)) {
            l2 = l1;
        } else {
            l2 = newMoonOnOrAfter(l1 + 1L);
        }

        super.rd = (l2 + (long) super.day) - 1L;
    }

    @Override
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    public boolean isLeap() {
        return leap;
    }

    @Override
    public java.util.List<String> getMonths() {
        return java.util.Arrays.asList(MONTHS);
    }

    @Override
    protected String monthName() {
        return MONTHS[Math.max(0, Math.min(super.month - 1, 11))] + (leap ? "(leap)" : "");
    }

    private String yearName() {
        String[] stems = {
                "", "Jia", "Yi", "Bing", "Ding", "Wu", "Ji", "Geng", "Xin", "Ren", "Gui"
        };
        String[] branches = {
                "", "Zi (Rat)", "Chou (Ox)", "Yin (Tiger)", "Mao (Hare)",
                "Chen (Dragon)", "Si (Snake)", "Wu (Horse)", "Wei (Sheep)",
                "Shen (Monkey)", "You (Fowl)", "Xu (Dog)", "Hai (Pig)"
        };
        return stems[(int) AlternateCalendar.amod(super.year, 10L)] + "-" +
               branches[(int) AlternateCalendar.amod(super.year, 12L)];
    }

    @Override
    public String toString() {
        return super.day + " " + monthName() + ", Year " + super.year + ": " +
               yearName() + ", cycle " + cycle;
    }

    @Override
    protected String getSuffix() {
        return "";
    }
}
