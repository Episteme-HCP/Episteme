package org.jscience.history.calendars;


public class HebrewCalendar extends MonthDayYear {
    
    // Hebrew Epoch: October 7, 3761 BC (Julian)
    protected static final long EPOCH = (new JulianCalendar(10, 7, -3761)).toRD();

    private static final String[] MONTHS = {
            "Nisan", "Iyyar", "Sivan", "Tammuz", "Av", "Elul", "Tishri",
            "Heshvan", "Kislev", "Tevet", "Shvat", "Adar", "Adar II"
    };

    public HebrewCalendar() {
        this(EPOCH);
    }

    public HebrewCalendar(long l) {
        set(l);
    }

    public HebrewCalendar(int month, int day, int year) {
        set(month, day, year);
    }

    public HebrewCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    public static boolean isLeapYear(int i) {
        // 19 year cycle
        return AlternateCalendar.mod((7 * i) + 1, 19) < 7;
    }

    public static int lastMonth(int i) {
        return (!isLeapYear(i)) ? 12 : 13;
    }

    protected static long elapsedDays(int i) {
        long l = AlternateCalendar.fldiv((235 * i) - 234, 19L);
        long l1 = 12084L + (13753L * l);
        long l2 = (29L * l) + AlternateCalendar.fldiv(l1, 25920L);

        if (AlternateCalendar.mod(3L * (l2 + 1L), 7) < 3) {
            l2++;
        }
        return l2;
    }

    protected static int delay(int i) {
        long l = elapsedDays(i - 1);
        long l1 = elapsedDays(i);
        long l2 = elapsedDays(i + 1);

        if ((l2 - l1) == 356L) {
            return 2;
        }
        return ((l1 - l) != 382L) ? 0 : 1;
    }

    protected static int lastDay(int i, int j) {
        if ((i == 2) || (i == 4) || (i == 6) || (i == 10) || (i == 13)) return 29;
        if (i == 12 && !isLeapYear(j)) return 29;
        if (i == 8 && !longHeshvan(j)) return 29;
        if (i == 9 && shortKislev(j)) return 29;
        return 30;
    }

    protected static boolean longHeshvan(int i) {
        return AlternateCalendar.mod(daysInYear(i), 10) == 5;
    }

    protected static boolean shortKislev(int i) {
        return AlternateCalendar.mod(daysInYear(i), 10) == 3;
    }

    protected static int daysInYear(int i) {
        return (int) ((new HebrewCalendar(7, 1, i + 1)).toRD() -
        (new HebrewCalendar(7, 1, i)).toRD());
    }

    @Override
    protected synchronized void recomputeRD() {
        super.rd = (EPOCH + elapsedDays(super.year) + (long) delay(super.year) +
            (long) super.day) - 1L;

        // Tishri (7) is start of year for year-count purposes, but months counted 1..12/13 starting Nisan
        // Logic handles this relative sum
        if (super.month < 7) {
            int i = lastMonth(super.year);
            for (int k = 7; k <= i; k++)
                super.rd += lastDay(k, super.year);

            for (int l = 1; l < super.month; l++)
                super.rd += lastDay(l, super.year);
            return;
        }

        for (int j = 7; j < super.month; j++)
            super.rd += lastDay(j, super.year);
    }

    @Override
    protected synchronized void recomputeFromRD() {
        int i = (int) Math.floor((double) (super.rd - EPOCH) / 365.25D);
        super.year = i - 1;

        int j = i;
        HebrewCalendar hebrew;
        
        // Find correct year
        for (hebrew = new HebrewCalendar(7, 1, j); super.rd >= hebrew.toRD(); hebrew.set(7, 1, j)) {
            super.year++;
            j++;
        }

        hebrew.set(1, 1, super.year);

        if (super.rd < hebrew.toRD()) {
            super.month = 7;
        } else {
            super.month = 1;
        }

        int k = super.month;
        hebrew.set(k, lastDay(k, super.year), super.year);

        while (super.rd > hebrew.toRD()) {
            super.month++;
            k++;
            hebrew.set(k, lastDay(k, super.year), super.year);
        }

        hebrew.set(super.month, 1, super.year);
        super.day = (int) ((super.rd - hebrew.toRD()) + 1L);
    }

    @Override
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    @Override
    protected String monthName() {
         if ((super.month == 12) && isLeapYear(super.year)) {
            // Adar in leap year is Adar I? 
            // The array has "Adar", "Adar II". 
            // In standard: Adar I is leap month? Or Adar II? 
            // Month 12 is Adar. Month 13 is Adar II.
            // If leap year, Month 12 is often called Adar I. 
            // Code says: return MONTHS[11] + " I" -> "Adar I".
            return MONTHS[super.month - 1] + " I";
        } else {
            return MONTHS[Math.max(0, Math.min(super.month - 1, 12))];
        }
    }

    @Override
    protected String getSuffix() {
        return " A.M.";
    }

    @Override
    public java.util.List<String> getMonths() {
        return java.util.Arrays.asList(MONTHS);
    }
}
