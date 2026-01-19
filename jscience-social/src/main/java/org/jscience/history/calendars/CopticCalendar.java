package org.jscience.history.calendars;


public class CopticCalendar extends JulianCalendar {
    
    // Coptic Epoch: August 29, 284 AD (Julian)
    public static long EPOCH = (new JulianCalendar(8, 29, 284)).toRD();

    private static final String[] MONTHS = {
            "Tut", "Babah", "Hatur", "Kiyahk", "Tubah", "Amshir", "Baramhat",
            "Baramundah", "Bashans", "Ba'unah", "Abib", "Misra", "al-Nasi"
    };

    public CopticCalendar() {
        this(EPOCH);
    }

    public CopticCalendar(long l) {
        set(l);
    }

    public CopticCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    public CopticCalendar(int month, int day, int year) {
        super(1, 1, 1);
        set(month, day, year);
    }

    @Override
    public synchronized void set(int month, int day, int year) {
        super.month = month;
        super.day = day;
        super.year = year;
        recomputeRD();
    }

    public static boolean isLeapYear(int i) {
        return AlternateCalendar.mod(i, 4) == 3;
    }

    @Override
    protected synchronized void recomputeRD() {
        super.rd = (EPOCH - 1L) + (long) (365 * (super.year - 1)) +
            AlternateCalendar.fldiv(super.year, 4L) +
            (long) (30 * (super.month - 1)) + (long) super.day;
    }

    @Override
    protected synchronized void recomputeFromRD() {
        super.year = (int) AlternateCalendar.fldiv((4L * (super.rd - EPOCH)) + 1463L, 1461L);
        
        long startOfYear = (new CopticCalendar(1, 1, super.year)).toRD();
        super.month = (int) AlternateCalendar.fldiv(super.rd - startOfYear, 30L) + 1;
        
        long startOfMonth = (new CopticCalendar(super.month, 1, super.year)).toRD();
        super.day = (int) ((super.rd + 1L) - startOfMonth);
    }

    @Override
    protected String monthName() {
        return MONTHS[Math.max(0, Math.min(super.month - 1, 12))];
    }

    @Override
    protected String getSuffix() {
        return "";
    }

    @Override
    public java.util.List<String> getMonths() {
        return java.util.Arrays.asList(MONTHS);
    }

    @Override
    public String toString() {
        try {
            return super.day + " " + monthName() + " " + super.year + getSuffix();
        } catch (Exception e) {
            return "Invalid date: " + super.month + " " + super.day + " " + super.year;
        }
    }
}
