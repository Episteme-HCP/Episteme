package org.jscience.history.calendars;

/**
 * Julian Calendar.
 * Note: Year 0 is skipped (1 BC -> 1 AD).
 * Implementation uses astronomical year numbering internally (..., -1, 0, 1, ...) 
 * or historical numbering (..., -1, 1, ...) depending on legacy code.
 * Old code: "if (i < 0) i++;" in recomputeRD, implies internal year 0 is skipped or mapped.
 * "if (super.year <= 0) super.year--;" in recomputeFromRD.
 * This suggests the 'year' field stores 1, -1, etc. but not 0.
 */
public class JulianCalendar extends GregorianCalendar {
    
    // Julian Epoch: December 30, year 0?? No, standard RD epoch is Jan 1, 1 (Gregorian) = 1.
    // Julian Calendar aligns differently. 
    // Gregorian: 1/1/1 = RD 1.
    // Julian 1/1/1:
    // Julian 1/1/1 is RD -1 (Dec 30, 2 BC Gregorian?). 
    // Old code says: EPOCH = (new GregorianCalendar(12, 30, 0)).toRD(); 
    // Note: GregorianCalendar logic for year 0 might be astronomical (0 exists).
    
    public static long EPOCH = (new GregorianCalendar(12, 30, 0)).toRD(); 

    public JulianCalendar() {
        this(EPOCH);
    }

    public JulianCalendar(long l) {
        set(l);
    }

    public JulianCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    public JulianCalendar(int month, int day, int year) {
        super(1, 1, 1); // dummy init
        set(month, day, year);
    }

    @Override
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }
    
    public synchronized void set(int month, int day, int year) {
        super.month = month;
        super.day = day;
        super.year = year;
        recomputeRD();
    }

    @Override
    protected synchronized void recomputeRD() {
        int i = super.year;
        
        // Handle year 0 skip if using historical years
        if (i < 0) {
            i++; // Map -1 (1 BC) to 0, -2 to -1, etc.
        }
        
        // Formula:
        // EPOCH - 1 + 365*(y-1) + floor((y-1)/4) + floor((367*m - 362)/12) + day
        super.rd = (EPOCH - 1L) + (long) (365 * (i - 1)) +
            AlternateCalendar.fldiv(i - 1, 4L) +
            AlternateCalendar.fldiv((367 * super.month) - 362, 12L);

        if (super.month > 2) {
            if (isLeapYear(super.year)) {
                super.rd--;
            } else {
                super.rd -= 2L;
            }
        }
        super.rd += super.day;
    }

    @Override
    protected synchronized void recomputeFromRD() {
        // Approximate year
        super.year = (int) AlternateCalendar.fldiv((4L * (toRD() - EPOCH)) + 1464L, 1461L);

        if (super.year <= 0) {
            super.year--; // Skip 0
        }

        JulianCalendar julian = new JulianCalendar(1, 1, super.year);
        int i = (int) (toRD() - julian.toRD());
        
        // Check leap of *this* year
        julian.set(3, 1, super.year); // March 1st

        if (toRD() >= julian.toRD()) {
            if (isLeapYear(super.year)) {
                i++;
            } else {
                i += 2;
            }
        }

        super.month = (int) AlternateCalendar.fldiv((12 * i) + 373, 367L);
        
        JulianCalendar startOfMonth = new JulianCalendar(super.month, 1, super.year);
        super.day = (int) (toRD() - startOfMonth.toRD()) + 1;
    }

    public static boolean isLeapYear(int i) {
        if (i > 0) {
            return AlternateCalendar.mod(i, 4) == 0;
        }
        return AlternateCalendar.mod(i, 4) == 3;
    }

    @Override
    protected String getSuffix() {
        if (super.year > 0) {
            return " C.E.";
        } else {
            return " B.C.E.";
        }
    }

    @Override
    public String toString() {
        try {
            return super.day + " " + monthName() + " " +
            ((super.year < 0) ? (-super.year) : super.year) + getSuffix();
        } catch (Exception e) {
            return "Invalid date: " + super.month + " " + super.day + " " + super.year;
        }
    }
}
