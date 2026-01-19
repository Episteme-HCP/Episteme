package org.jscience.history.calendars;



public abstract class MonthDayYear extends SevenDaysWeek {
    protected int month;
    protected int day;
    protected int year;

    public MonthDayYear() {
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public synchronized void set(int i, int j, int k) {
        month = i;
        day = j;
        year = k;
        recomputeRD();
    }

    @Override
    public String toString() {
        try {
            return day + " " + monthName() + " " + year + getSuffix();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Invalid date: " + month + " " + day + " " + year;
        }
    }

    protected abstract String monthName();

    protected abstract String getSuffix();

    public abstract java.util.List<String> getMonths();

}
