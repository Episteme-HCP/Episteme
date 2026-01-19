package org.jscience.history.calendars;



public class GregorianCalendar extends MonthDayYear {
    
    protected static final String[] MONTHS = {
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"
    };

    protected static long EPOCH = 1L;
    


    public GregorianCalendar(long l) {
        set(l);
    }

    public GregorianCalendar(int month, int day, int year) {
        set(month, day, year);
    }

    public GregorianCalendar() {
        this(EPOCH);
    }

    public GregorianCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    public static boolean isLeapYear(int i) {
        if (AlternateCalendar.mod(i, 4) != 0) {
            return false;
        }
        int j = (int) AlternateCalendar.mod(i, 400); // Cast to int as mod returns long/int depending on overload. Helper returns long for (long, long) or int for (long, int).
        
        return (j != 100) && (j != 200) && (j != 300);
    }

    @Override
    protected synchronized void recomputeRD() {
        // year, month, day are in super class
        int i = super.year - 1;
        super.rd = (((EPOCH - 1L) + (long) (365 * i) +
            AlternateCalendar.fldiv(i, 4L)) - AlternateCalendar.fldiv(i, 100L)) +
            AlternateCalendar.fldiv(i, 400L) +
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
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    @Override
    protected synchronized void recomputeFromRD() {
        int[] ai = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        long l = super.rd - EPOCH;
        int i = (int) AlternateCalendar.fldiv(l, 146097L);
        int j = (int) AlternateCalendar.mod(l, 146097); // 0x23ab1
        int k = (int) AlternateCalendar.fldiv(j, 36524L);
        int i1 = (int) AlternateCalendar.mod(j, 36524);
        int j1 = (int) AlternateCalendar.fldiv(i1, 1461L);
        int k1 = (int) AlternateCalendar.mod(i1, 1461);
        int l1 = (int) AlternateCalendar.fldiv(k1, 365L);
        
        super.year = (400 * i) + (100 * k) + (4 * j1) + l1;

        int i2 = (int) AlternateCalendar.mod(k1, 365) + 1;
        super.month = 1;
        super.day = i2;

        if ((k != 4) && (l1 != 4)) {
            super.year++;
        }

        for (int j2 = 0; j2 <= 11; j2++) { // Fixed loop bound 0 to 11 (size 12). Old code <= 12 was likely wrong or using 1-based indexing logic on array? ArrayEnumeration uses 0-based. 
            // array `ai` has 12 elements. indices 0..11.
            // Old code: for (int j2 = 0; j2 <= 12; j2++)
            // Wait, if j2=12, ai[j2] throws OutOfBounds.
            // Old code might have had a bug or different array size. 
            // checking old file content: `int[] ai = { 31, 28, ... }` (12 items).
            // loop `j2 <= 12`. If it reached 12, it WOULD crash if it accessed ai[12].
            // However, inside loop: `if (super.day <= ai[j2]) break;`
            // If day is large, it subtracts ai[j2].
            // I should use `j2 < 12`.
            
            if (j2 >= 12) break; // Safety
            
            if (super.day <= ai[j2]) {
                break;
            }

            if ((j2 == 1) && isLeapYear(super.year)) {
                if (super.day <= 29) {
                    break;
                }
                super.day--;
            }

            super.day -= ai[j2];
            super.month++;
        }
    }

    public int dayNumber() {
        return (int) difference(new GregorianCalendar(12, 31, getYear() - 1));
    }

    public int daysLeft() {
        return (int) AlternateCalendar.difference(new GregorianCalendar(12, 31, getYear()), this);
    }

    @Override
    protected String getSuffix() {
        return "";
    }

    @Override
    protected String monthName() {
        return MONTHS[super.month - 1];
    }

    @Override
    public java.util.List<String> getMonths() {
        return java.util.Arrays.asList(MONTHS);
    }


    public static void main(String[] args) {
        int i, j, k;
        try {
            i = Integer.parseInt(args[0]);
            j = Integer.parseInt(args[1]);
            k = Integer.parseInt(args[2]);
        } catch (Exception e) {
            i = 1; k = 1; j = 1;
        }

        GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
        System.out.println(gregorian.toRD());
        System.out.println(gregorian + "\n");
    }
}
