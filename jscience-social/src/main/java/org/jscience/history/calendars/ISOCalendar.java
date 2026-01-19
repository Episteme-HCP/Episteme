package org.jscience.history.calendars;

/**
 * ISO 8601 Week Date Calendar.
 */
public class ISOCalendar extends GregorianCalendar {

    private int week;
    private int day;
    private int year;

    public ISOCalendar() {
        this(GregorianCalendar.EPOCH);
    }

    public ISOCalendar(long l) {
        set(l);
    }

    public ISOCalendar(int week, int day, int year) {
        set(week, day, year);
    }

    public ISOCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    public synchronized void set(int week, int day, int year) {
        this.week = week;
        this.day = day;
        this.year = year;
        recomputeRD();
    }
    
    @Override
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    @Override
    public int getYear() {
        return year;
    }

    public int getWeek() {
        return week;
    }

    public int getDay() {
        return day;
    }

    @Override
    protected synchronized void recomputeRD() {
        GregorianCalendar gregorian = new GregorianCalendar(12, 28, year - 1);
        gregorian.nthKDay(week, 0); // "0th Sunday"? 
        // Sunday is 0. 
        // nthKDay(n, k) -> adds n weeks, moves to k day?
        // Let's verify SevenDaysWeek.nthKDay logic: "kDayBefore" if n < 0, else "kDayAfter", then add n*7.
        // Gregorian 12/28/[year-1] is the base.
        // We move to the week-th Sunday?
        gregorian.add(day);
        super.rd = gregorian.toRD();
    }

    @Override
    protected synchronized void recomputeFromRD() {
        int i = (new GregorianCalendar(super.rd - 3L)).getYear();
        ISOCalendar iso = new ISOCalendar(1, 1, i + 1);

        if (super.rd < iso.toRD()) {
            iso.set(1, 1, i);
        }

        year = iso.getYear();
        week = (int) AlternateCalendar.fldiv(super.rd - iso.toRD(), 7L) + 1;
        day = (int) AlternateCalendar.amod(super.rd, 7L);
    }

    @Override
    public String toString() {
        try {
            return SevenDaysWeek.DAYNAMES[AlternateCalendar.mod(day, 7)] +
            " of week " + week + ", " + year;
        } catch (Exception e) {
            return day + " " + week + " " + year;
        }
    }
    
    @Override
    public int getMonth() {
        return 0; // Not month based
    }
}
