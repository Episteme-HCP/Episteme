/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.util;

import org.junit.jupiter.api.Test;
import org.jscience.economics.*;
import org.jscience.sociology.*;
import org.jscience.geography.*;
import org.jscience.politics.*;
import org.jscience.arts.*;
import org.jscience.arts.music.*;
import org.jscience.arts.culinary.*;

public class SocialPojoTests {

    @Test
    public void testEconomicsPojos() {
        PojoTester.testPojo(Money.class);
        PojoTester.testPojo(Currency.class);
        PojoTester.testPojo(Bank.class);
        PojoTester.testPojo(Worker.class);
        PojoTester.testPojo(Factory.class);
        PojoTester.testPojo(Market.class);
        // PojoTester.testPojo(Product.class); // Does not exist
        PojoTester.testPojo(Resource.class);
        PojoTester.testPojo(MaterialResource.class);
    }

    @Test
    public void testSociologyPojos() {
        PojoTester.testPojo(Person.class);
        PojoTester.testPojo(Group.class);
        PojoTester.testPojo(Family.class);
        PojoTester.testPojo(School.class);
        PojoTester.testPojo(org.jscience.sociology.Organization.class);
        PojoTester.testPojo(Religion.class);
        PojoTester.testPojo(Culture.class);
    }

    @Test
    public void testGeographyPojos() {
        PojoTester.testPojo(Place.class);
        PojoTester.testPojo(Region.class);
        PojoTester.testPojo(Address.class);
        PojoTester.testPojo(Boundary.class);
        PojoTester.testPojo(ClimateZone.class);
    }

    @Test
    public void testPoliticsPojos() {
        PojoTester.testPojo(Party.class);
        PojoTester.testPojo(Election.class);
        PojoTester.testPojo(Constituency.class);
        PojoTester.testPojo(Government.class);
        PojoTester.testPojo(Country.class);
    }

    @Test
    public void testArtsPojos() {
        PojoTester.testPojo(Artist.class);
        PojoTester.testPojo(Artwork.class);
        PojoTester.testPojo(Book.class);
        PojoTester.testPojo(Film.class);
        PojoTester.testPojo(Recipe.class);
        PojoTester.testPojo(Instrument.class);
        PojoTester.testPojo(Note.class);
    }
}
