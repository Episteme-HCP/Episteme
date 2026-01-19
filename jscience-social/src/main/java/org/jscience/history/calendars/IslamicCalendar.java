package org.jscience.history.calendars;


public class IslamicCalendar extends MonthDayYear {
    
    // Islamic Epoch: July 16, 622 AD (Julian)
    public static long EPOCH = (new JulianCalendar(7, 16, 622)).toRD();

    private static final String[] MONTHS = {
            "Muharram", "Safar", "Rabi` I", "Rabi` II", "Jumada I", "Jumada II",
            "Rajab", "Sha`ban", "Ramadan", "Shawwal", "Dhu al-Qa`da",
            "Dhu al-Hijja"
    };

    public IslamicCalendar() {
        this(EPOCH);
    }

    public IslamicCalendar(long l) {
        set(l);
    }
    
    public IslamicCalendar(int month, int day, int year) {
        set(month, day, year);
    }

    public IslamicCalendar(AlternateCalendar altcalendar) {
        set(altcalendar.toRD());
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
    protected synchronized void recomputeRD() {
        super.rd = ((long) super.day +
            (long) Math.ceil(29.5D * (double) (super.month - 1)) +
            (long) ((super.year - 1) * 354) +
            AlternateCalendar.fldiv(3 + (11 * super.year), 30L) + EPOCH) - 1L;
    }

    @Override
    protected synchronized void recomputeFromRD() {
        super.year = (int) AlternateCalendar.fldiv((30L * (super.rd - EPOCH)) + 10646L, 10631L);

        int i = (int) Math.ceil((double) (super.rd - 29L -
                (new IslamicCalendar(1, 1, super.year)).toRD()) / 29.5D) + 1;
        super.month = (i >= 12) ? 12 : i;
        super.day = (int) (super.rd -
            (new IslamicCalendar(super.month, 1, super.year)).toRD()) + 1;
    }

    @Override
    protected String monthName() {
        return MONTHS[Math.max(0, Math.min(super.month - 1, 11))];
    }

    @Override
    protected String getSuffix() {
        return " A.H.";
    }

    @Override
    public java.util.List<String> getMonths() {
        return java.util.Arrays.asList(MONTHS);
    }
}
