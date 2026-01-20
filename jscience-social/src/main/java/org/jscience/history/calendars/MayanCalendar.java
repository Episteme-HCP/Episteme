//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
package org.jscience.history.calendars;


// Referenced classes of package calendars:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class MayanCalendar extends AlternateCalendar {
    /** DOCUMENT ME! */
    static final long EPOCH = AlternateCalendar.fromJD(584284.5D);

    /** DOCUMENT ME! */
    static final String[] HAAB = {
            "Pop", "Uo", "Zip", "Zotz", "Tzec", "Xul", "Yaxkin", "Mol", "Chen",
            "Yax", "Zac", "Ceh", "Mac", "Kankin", "Muan", "Pax", "Kayab",
            "Cumku", "Uayeb"
        };

    /** DOCUMENT ME! */
    static final String[] TZOLKIN = {
            "Imix", "Ik", "Akbal", "Kan", "Chicchan", "Cimi", "Manik", "Lamat",
            "Muluc", "Oc", "Chuen", "Eb", "Ben", "Ix", "Men", "Cib", "Caban",
            "Etznab", "Cauac", "Ahau"
        };

    /** DOCUMENT ME! */
    static final MayanCalendar EPOCH_OBJ;

    /** DOCUMENT ME! */
    static final Haab HAAB_EPOCH;

    /** DOCUMENT ME! */
    static final Tzolkin TZOLKIN_EPOCH;

    static {
        EPOCH_OBJ = new MayanCalendar(8, 18, 4, 20);
        HAAB_EPOCH = EPOCH_OBJ.haab;
        TZOLKIN_EPOCH = EPOCH_OBJ.tzolkin;
    }

    /** DOCUMENT ME! */
    long longcount;

    /** DOCUMENT ME! */
    Haab haab;

    /** DOCUMENT ME! */
    Tzolkin tzolkin;

/**
     * Creates a new MayanCalendar object.
     */
    public MayanCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new MayanCalendar object.
     *
     * @param i  DOCUMENT ME!
     * @param j  DOCUMENT ME!
     * @param k  DOCUMENT ME!
     * @param l  DOCUMENT ME!
     * @param i1 DOCUMENT ME!
     */
    public MayanCalendar(int i, int j, int k, int l, int i1) {
        haab = new Haab();
        tzolkin = new Tzolkin();
        set(i, j, k, l, i1);
    }

/**
     * Creates a new MayanCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public MayanCalendar(long l) {
        haab = new Haab();
        tzolkin = new Tzolkin();
        set(l);
    }

/**
     * Creates a new MayanCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public MayanCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new MayanCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     * @param l DOCUMENT ME!
     */
    protected MayanCalendar(int i, int j, int k, int l) {
        longcount = 1L;
        super.rd = EPOCH;
        haab = new Haab(i, j);
        tzolkin = new Tzolkin(k, l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     * @param l DOCUMENT ME!
     * @param i1 DOCUMENT ME!
     */
    public synchronized void set(int i, int j, int k, int l, int i1) {
        longcount = (i * 0x23280) + (j * 7200) + (k * 360) + (l * 20) + i1;
        super.rd = longcount + EPOCH;
        recomputeFromRD();
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeFromRD() {
        longcount = super.rd - EPOCH;
        haab.recompute();
        tzolkin.recompute();
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeRD() {
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
     * @return DOCUMENT ME!
     */
    public String longCountString() {
        long l = AlternateCalendar.mod(longcount, 0x23280);
        long l1 = AlternateCalendar.mod(l, 7200);
        long l2 = AlternateCalendar.mod(l1, 360);

        return AlternateCalendar.fldiv(longcount, 0x23280L) + "." +
        AlternateCalendar.fldiv(l, 7200L) + "." +
        AlternateCalendar.fldiv(l1, 360L) + "." +
        AlternateCalendar.fldiv(l2, 20L) + "." + AlternateCalendar.mod(l2, 20);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return longCountString() + ": " + tzolkin + "; " + haab;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        int i;
        int j;
        int k;

        try {
            i = Integer.parseInt(args[0]);
            j = Integer.parseInt(args[1]);
            k = Integer.parseInt(args[2]);
        } catch (Exception _ex) {
            i = k = j = 1;
        }

        GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
        System.out.println(gregorian.toRD());
        System.out.println(gregorian + "\n");

        MayanCalendar mayan = new MayanCalendar(gregorian);
        System.out.println(gregorian + ": " + mayan);
        System.out.println(mayan.toRD());
        System.out.println("\nHaab Epoch: " + HAAB_EPOCH + "\nTzolkin Epoch: " +
            TZOLKIN_EPOCH);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class Haab {
        /** DOCUMENT ME! */
        private int month;

        /** DOCUMENT ME! */
        private int day;

/**
         * Creates a new Haab object.
         *
         * @param i DOCUMENT ME!
         * @param j DOCUMENT ME!
         */
        public Haab(int i, int j) {
            month = j;
            day = i;
        }

/**
         * Creates a new Haab object.
         *
         * @param haab1 DOCUMENT ME!
         */
        public Haab(Haab haab1) {
            month = haab1.month;
            day = haab1.day;
        }

/**
         * Creates a new Haab object.
         */
        public Haab() {
            this(MayanCalendar.HAAB_EPOCH);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getMonth() {
            return month;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getDay() {
            return day;
        }

        /**
         * DOCUMENT ME!
         */
        public synchronized void recompute() {
            int i = AlternateCalendar.mod(longcount +
                    (long) MayanCalendar.HAAB_EPOCH.getDay() +
                    (long) (20 * (MayanCalendar.HAAB_EPOCH.getMonth() - 1)), 365);
            day = i % 20;
            month = (i / 20) + 1;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            try {
                return day + " " + MayanCalendar.HAAB[month - 1];
            } catch (ArrayIndexOutOfBoundsException _ex) {
                return "Invalid Date: " + day + " " + month;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class Tzolkin {
        /** DOCUMENT ME! */
        private int number;

        /** DOCUMENT ME! */
        private int name;

/**
         * Creates a new Tzolkin object.
         */
        public Tzolkin() {
            this(MayanCalendar.TZOLKIN_EPOCH);
        }

/**
         * Creates a new Tzolkin object.
         *
         * @param i DOCUMENT ME!
         * @param j DOCUMENT ME!
         */
        public Tzolkin(int i, int j) {
            number = i;
            name = j;
        }

/**
         * Creates a new Tzolkin object.
         *
         * @param tzolkin1 DOCUMENT ME!
         */
        public Tzolkin(Tzolkin tzolkin1) {
            number = tzolkin1.getNumber();
            name = tzolkin1.getName();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getNumber() {
            return number;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getName() {
            return name;
        }

        /**
         * DOCUMENT ME!
         */
        public synchronized void recompute() {
            number = (int) AlternateCalendar.amod(longcount +
                    (long) MayanCalendar.TZOLKIN_EPOCH.getNumber(), 13L);
            name = (int) AlternateCalendar.amod(longcount +
                    (long) MayanCalendar.TZOLKIN_EPOCH.getName(), 20L);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            try {
                return number + " " + MayanCalendar.TZOLKIN[name - 1];
            } catch (ArrayIndexOutOfBoundsException _ex) {
                return "Invalid Date: " + number + " " + name;
            }
        }
    }
}
