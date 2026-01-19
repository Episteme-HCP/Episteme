package org.jscience.history.calendars;


public class EthiopicCalendar extends CopticCalendar {
    
    // Ethiopic Epoch: August 29, 8 AD (Julian)
    public static long EPOCH = (new JulianCalendar(8, 29, 8)).toRD();

    private static final String[] MONTHS = {
            "Maskaram", "Teqemt", "Khedar", "Takhsas", "Ter", "Yakatit",
            "Magabit", "Miyazya", "Genbot", "Sane", "Hamle", "Nahase",
            "Paguemen"
    };

    public EthiopicCalendar() {
        this(EPOCH);
    }

    public EthiopicCalendar(long l) {
        set(l);
    }

    public EthiopicCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    public EthiopicCalendar(int month, int day, int year) {
        super(month, day, year);
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
        
        long startOfYear = (new EthiopicCalendar(1, 1, super.year)).toRD();
        super.month = (int) AlternateCalendar.fldiv(super.rd - startOfYear, 30L) + 1;
        
        long startOfMonth = (new EthiopicCalendar(super.month, 1, super.year)).toRD();
        super.day = (int) ((super.rd + 1L) - startOfMonth);
    }

    @Override
    protected String monthName() {
        return MONTHS[Math.max(0, Math.min(super.month - 1, 12))];
    }

    @Override
    public java.util.List<String> getMonths() {
        return java.util.Arrays.asList(MONTHS);
    }
}
