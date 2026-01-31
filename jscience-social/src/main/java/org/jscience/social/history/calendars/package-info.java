/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

/**
 * Historical and alternate calendar systems for chronological calculations.
 * 
 * <p>This package provides implementations of various historical and cultural calendar systems,
 * allowing conversion between different chronological frameworks. Each calendar extends
 * {@link AlternateCalendar} which provides common operations like addition, subtraction,
 * and conversion to/from Julian Day and Rata Die representations.</p>
 * 
 * <h2>Included Calendar Systems:</h2>
 * <ul>
 *   <li><b>Gregorian</b> - The internationally accepted civil calendar</li>
 *   <li><b>Julian</b> - The predecessor to the Gregorian calendar</li>
 *   <li><b>Islamic (Hijri)</b> - The lunar calendar used in Muslim countries</li>
 *   <li><b>Hebrew (Jewish)</b> - The lunisolar calendar for Jewish observances</li>
 *   <li><b>Chinese</b> - The traditional lunisolar Chinese calendar</li>
 *   <li><b>Persian (Solar Hijri)</b> - The solar calendar used in Iran and Afghanistan</li>
 *   <li><b>Coptic</b> - Used by the Coptic Orthodox Church</li>
 *   <li><b>Ethiopian</b> - The official calendar of Ethiopia</li>
 *   <li><b>French Republican</b> - The decimal calendar of the French Revolution</li>
 *   <li><b>BahÃ¡'Ã­</b> - The calendar of the BahÃ¡'Ã­ Faith</li>
 *   <li><b>Mayan</b> - The ancient Mesoamerican calendar system</li>
 *   <li><b>ISO</b> - ISO 8601 week-date calendar</li>
 * </ul>
 * 
 * <p>Original implementations based on code by Mark E. Shoulson.</p>
 * 
 * @author Mark E. Shoulson (original implementations)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
package org.jscience.social.history.calendars;

