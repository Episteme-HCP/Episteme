package org.jscience.history.calendars;


public class PersianCalendar extends MonthDayYear {
    
    // Persian Epoch: March 19, 622 AD (Julian)
    public static long EPOCH = (new JulianCalendar(3, 19, 622)).toRD();

    // Constant for internal calculation
    private static final long FOUR75 = (new PersianCalendar(1, 1, 475)).toRD();

    private static final String[] MONTHS = {
            "Farvardin", "Ordibehesht", "Khordad", "Tir", "Mordad", "Shahrivar",
            "Mehr", "Aban", "Azar", "Dey", "Bahman", "Esfand"
    };

    public PersianCalendar() {
        this(EPOCH);
    }

    public PersianCalendar(long l) {
        set(l);
    }
    
    public PersianCalendar(int month, int day, int year) {
        set(month, day, year);
    }

    public PersianCalendar(AlternateCalendar altcalendar) {
        set(altcalendar.toRD());
    }

    private static int yconv(int i) {
        if (i > 0) {
            return i - 474;
        } else {
            return i - 473;
        }
    }

    public static boolean isLeapYear(int i) {
        int j = yconv(i);
        int k = AlternateCalendar.mod(j, 2820) + 474;
        return AlternateCalendar.mod((k + 38) * 682, 2816) < 682;
    }

    @Override
    protected synchronized void recomputeRD() {
        int i = yconv(super.year);
        int j = AlternateCalendar.mod(i, 2820) + 474;
        super.rd = (EPOCH - 1L) +
            (0xfb75fL * AlternateCalendar.fldiv(i, 2820L)) +
            (long) (365 * (j - 1)) +
            AlternateCalendar.fldiv((682 * j) - 110, 2816L) +
            (long) ((super.month > 7) ? ((30 * (super.month - 1)) + 6)
                                      : (31 * (super.month - 1))) +
            (long) super.day;
    }

    @Override
    protected synchronized void recomputeFromRD() {
        long l = super.rd - FOUR75;
        int i = (int) AlternateCalendar.fldiv(l, 0xfb75fL);
        int j = AlternateCalendar.mod(l, 0xfb75f);
        int k;

        if (j == 0xfb75e) {
            k = 2820;
        } else {
            k = (int) AlternateCalendar.fldiv((2816L * (long) j) + 0xfbca9L, 0xfb1aaL);
        }

        super.year = 474 + (2820 * i) + k;

        if (super.year <= 0) {
            super.year--;
        }

        int i1 = (int) ((super.rd -
            (new PersianCalendar(1, 1, super.year)).toRD()) + 1L);

        if (i1 <= 186) {
            super.month = (int) Math.ceil((double) i1 / 31D);
        } else {
            super.month = (int) Math.ceil((double) (i1 - 6) / 30D);
        }

        super.day = (int) ((super.rd -
            (new PersianCalendar(super.month, 1, super.year)).toRD()) + 1L);
    }

    @Override
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }
    
    @Override
    public synchronized void set(int month, int day, int year) {
        super.month = month;
        super.day = day;
        super.year = year;
        recomputeRD();
    }

    @Override
    protected String monthName() {
        return MONTHS[Math.max(0, Math.min(super.month - 1, 11))];
    }

    @Override
    protected String getSuffix() {
        return " A.P.";
    }

    @Override
    public java.util.List<String> getMonths() {
        return java.util.Arrays.asList(MONTHS);
    }
}
