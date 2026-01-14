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

package org.jscience.linguistics.kif;

import java.io.FileReader;
import java.io.IOException;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A class designed to adhere strictly to the SUO-KIF definition at
 * http://suo.ieee.org/suo-kif.html
 *
 * @author Adam Pease
 */
public class KIFplus {
    /**
     * DOCUMENT ME!
     */
    String filename;

    /**
     * DOCUMENT ME!
     */
    String[] spcl = {
            "!", "$", "%", "&", "*", "+", "-", ".", "/", "<", "=", ">", "?", "@",
            "_", "~"
        };

    /**
     * DOCUMENT ME!
     */
    String[] wht = { " ", "\t", "\n" };

    /**
     * DOCUMENT ME!
     */
    ArrayList special = new ArrayList(Arrays.asList(spcl));

    /*
    upper ::= A | B | C | D | E | F | G | H | I | J | K | L | M |
              N | O | P | Q | R | S | T | U | V | W | X | Y | Z
    
    lower ::= a | b | c | d | e | f | g | h | i | j | k | l | m |
              n | o | p | q | r | s | t | u | v | w | x | y | z
    
    digit ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
    
    white ::= space | tab | return | linefeed | page
    
    initialchar ::= upper | lower
    
    wordchar ::= upper | lower | digit | - | _ | special
    
    character ::= upper | lower | digit | special | white
    
    word ::= initialchar wordchar*
    
    string ::= "character*"
    
    variable ::= ?word | @word
    
    number ::= [-] digit+ [. digit+] [exponent]
    
    exponent ::= e [-] digit+
    
    term ::= variable | word | string | funterm | number | sentence
    
    relword ::= initialchar wordchar*
    
    funword ::= initialchar wordchar*
    
    funterm ::= (funword term+)
    
    sentence ::= word | equation | inequality |
                 relsent | logsent | quantsent
    
    equation ::= (= term term)
    
    relsent ::= (relword term+)
    
    logsent ::= (not sentence) |
                (and sentence+) |
                (or sentence+) |
                (=> sentence sentence) |
                (<=> sentence sentence)
    
    quantsent ::= (forall (variable+) sentence) |
                  (exists (variable+) sentence)
    */
    /**
     * 
     *
     * @param fr DOCUMENT ME!
     *
     * @throws ParseException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    private void readLogsent(FileReader fr) throws ParseException, IOException {
    }

    /**
     * 
     *
     * @param fr DOCUMENT ME!
     *
     * @throws ParseException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    private void start(FileReader fr) throws ParseException, IOException {
        try {
            int ch = 0;
            StringBuffer predicate = new StringBuffer();

            while (fr.ready() && (ch != ' ')) {
                ch = fr.read();
                predicate = predicate.append(ch);
            }

            if (predicate.toString().equalsIgnoreCase("forall")) {
                readLogsent(fr);
            }
        } catch (ParseException pe) {
            throw new ParseException("Error in KIF.readFile(): " +
                pe.getMessage(), pe.getErrorOffset());
        } catch (java.io.IOException e) {
            throw new IOException(
                "Error in KIF.readFile(): IO exception parsing file " +
                filename);
        }
    }

    /**
     * 
     *
     * @param args DOCUMENT ME!
     */
    public void main(String[] args) {
        KIFplus k = new KIFplus();
        String fname = args[0];

        try {
            FileReader fr = new FileReader(fname);
            int linenumber = 0;
            k.filename = fname;

            while (fr.ready()) {
                int ch = fr.read();

                if (ch == '(') {
                    k.start(fr);
                }
            }
        } catch (ParseException pe) {
            System.out.print("Error in KIF.readFile(): " + pe.getMessage());
            System.out.println(pe.getErrorOffset());
        } catch (java.io.IOException e) {
            System.out.println(
                "Error in KIF.readFile(): IO exception parsing file " +
                filename);
        }
    }
}
