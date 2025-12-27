/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.sports;

/**
 * Represents a sport or athletic activity.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Sport {

    public enum Category {
        TEAM, INDIVIDUAL, COMBAT, RACQUET, WATER, WINTER, ATHLETICS, MOTORSPORT
    }

    public enum Surface {
        GRASS, CLAY, HARD_COURT, ICE, SNOW, WATER, TRACK, ROAD, INDOOR
    }

    private final String name;
    private final Category category;
    private final int teamSize;
    private final Surface surface;
    private final boolean olympicSport;
    private final String governingBody;

    public Sport(String name, Category category, int teamSize, Surface surface,
            boolean olympicSport, String governingBody) {
        this.name = name;
        this.category = category;
        this.teamSize = teamSize;
        this.surface = surface;
        this.olympicSport = olympicSport;
        this.governingBody = governingBody;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public Surface getSurface() {
        return surface;
    }

    public boolean isOlympicSport() {
        return olympicSport;
    }

    public String getGoverningBody() {
        return governingBody;
    }

    public boolean isTeamSport() {
        return teamSize > 1;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) - %s", name, category,
                isTeamSport() ? teamSize + " players" : "individual",
                olympicSport ? "Olympic" : "Non-Olympic");
    }

    // Common sports
    public static final Sport SOCCER = new Sport("Association Football", Category.TEAM, 11,
            Surface.GRASS, true, "FIFA");
    public static final Sport BASKETBALL = new Sport("Basketball", Category.TEAM, 5,
            Surface.HARD_COURT, true, "FIBA");
    public static final Sport TENNIS = new Sport("Tennis", Category.RACQUET, 1,
            Surface.HARD_COURT, true, "ITF");
    public static final Sport SWIMMING = new Sport("Swimming", Category.WATER, 1,
            Surface.WATER, true, "FINA");
    public static final Sport ATHLETICS = new Sport("Athletics", Category.ATHLETICS, 1,
            Surface.TRACK, true, "World Athletics");
}
