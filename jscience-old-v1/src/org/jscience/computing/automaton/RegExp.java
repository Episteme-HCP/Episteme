/*
 * org.jscience.computing.automaton
 * Copyright (C) 2001-2005 Anders Moeller
 * All rights reserved.
 */

package org.jscience.computing.automaton;

import java.util.*;

/**
 * Regular Expression extension to <code>Automaton</code>.
 * <p/>
 * Regular expressions are built from the following abstract syntax:<p>
 * <table border=0>
 * <tr><td><i>regexp</i></td><td>::=</td><td><i>unionexp</i></td><td></td><td></td></tr>
 * <p/>
 * <tr><td><i>unionexp</i></td><td>::=</td><td><i>interexp</i>&nbsp;<tt><b>|</b></tt>&nbsp;<i>unionexp</i></td><td>(union)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><i>interexp</i></td><td></td><td></td></tr>
 * <p/>
 * <tr><td><i>interexp</i></td><td>::=</td><td><i>concatexp</i>&nbsp;<tt><b>&amp;</b></tt>&nbsp;<i>interexp</i></td><td>(intersection)</td><td><small>[OPTIONAL]</small></td></tr>
 * <tr><td></td><td>|</td><td><i>concatexp</i></td><td></td><td></td></tr>
 * <p/>
 * <tr><td><i>concatexp</i></td><td>::=</td><td><i>repeatexp</i>&nbsp;<i>concatexp</i></td><td>(concatenation)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><i>repeatexp</i></td><td></td><td></td></tr>
 * <p/>
 * <tr><td><i>repeatexp</i></td><td>::=</td><td><i>repeatexp</i>&nbsp;<tt><b>?</b></tt></td><td>(zero or one occurrence)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><i>repeatexp</i>&nbsp;<tt><b>*</b></tt></td><td>(zero or more occurrences)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><i>repeatexp</i>&nbsp;<tt><b>+</b></tt></td><td>(one or more occurrences)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><i>repeatexp</i>&nbsp;<tt><b>{</b><i>n</i><b>}</b></tt></td><td>(<tt><i>n</i></tt> occurrences)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><i>repeatexp</i>&nbsp;<tt><b>{</b><i>n</i><b>,}</b></tt></td><td>(<tt><i>n</i></tt> or more occurrences)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><i>repeatexp</i>&nbsp;<tt><b>{</b><i>n</i><b>,</b><i>m</i><b>}</b></tt></td><td>(<tt><i>n</i></tt> to <tt><i>m</i></tt> occurrences, including both)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><i>complexp</i></td><td></td><td></td></tr>
 * <p/>
 * <tr><td><i>complexp</i></td><td>::=</td><td><tt><b>~</b></tt>&nbsp;<i>complexp</i></td><td>(complement)</td><td><small>[OPTIONAL]</small></td></tr>
 * <tr><td></td><td>|</td><td><i>charclassexp</i></td><td></td><td></td></tr>
 * <p/>
 * <tr><td><i>charclassexp</i></td><td>::=</td><td><tt><b>[</b></tt>&nbsp;<i>charclasses</i>&nbsp;<tt><b>]</b></tt></td><td>(character class)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><tt><b>[^</b></tt>&nbsp;<i>charclasses</i>&nbsp;<tt><b>]</b></tt></td><td>(negated character class)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><i>simpleexp</i></td><td></td><td></td></tr>
 * <p/>
 * <tr><td><i>charclasses</i></td><td>::=</td><td><i>charclass</i>&nbsp;<i>charclasses</i></td><td></td><td></td></tr>
 * <tr><td></td><td>|</td><td><i>charclass</i></td><td></td><td></td></tr>
 * <p/>
 * <tr><td><i>charclass</i></td><td>::=</td><td><i>charexp</i>&nbsp;<tt><b>-</b></tt>&nbsp;<i>charexp</i></td><td>(character range, including end-points)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><i>charexp</i></td><td></td><td></td></tr>
 * <p/>
 * <tr><td><i>simpleexp</i></td><td>::=</td><td><i>charexp</i></td><td></td><td></td></tr>
 * <tr><td></td><td>|</td><td><tt><b>.</b></tt></td><td>(any single character)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><tt><b>#</b></tt></td><td>(the empty language)</td><td><small>[OPTIONAL]</small></td></tr>
 * <tr><td></td><td>|</td><td><tt><b>@</b></tt></td><td>(any string)</td><td><small>[OPTIONAL]</small></td></tr>
 * <tr><td></td><td>|</td><td><tt><b>"</b></tt>&nbsp;&lt;Unicode string without double-quotes&gt;&nbsp;<tt><b>"</b></tt></td><td>(a string)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><tt><b>(</b></tt>&nbsp;<tt><b>)</b></tt></td><td>(the empty string)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><tt><b>(</b></tt>&nbsp;<i>unionexp</i>&nbsp;<tt><b>)</b></tt></td><td>(precedence override)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><tt><b>&lt;</b></tt>&nbsp;&lt;identifier&gt;&nbsp;<tt><b>&gt;</b></tt></td><td>(named automaton)</td><td><small>[OPTIONAL]</small></td></tr>
 * <tr><td></td><td>|</td><td><tt><b>&lt;</b><i>n</i>-<i>m</i><b>&gt;</b></tt></td><td>(numerical interval)</td><td><small>[OPTIONAL]</small></td></tr>
 * <p/>
 * <tr><td><i>charexp</i></td><td>::=</td><td>&lt;Unicode character&gt;</td><td>(a single non-reserved character)</td><td></td></tr>
 * <tr><td></td><td>|</td><td><tt><b>\</b></tt>&nbsp;&lt;Unicode character&gt;&nbsp;</td><td>(a single character)</td><td></td></tr>
 * </table>
 * <p/>
 * The productions marked <small>[OPTIONAL]</small> are only allowed
 * if specified by the syntax flags passed to the <code>RegExp</code>
 * constructor.  The reserved characters used in the (enabled) syntax
 * must be escaped with backslash (<tt><b>\</b></tt>) or double-quotes
 * (<tt><b>"..."</b></tt>). (In contrast to other regexp syntaxes,
 * this is required also in character classes.)  Be aware that
 * dash (<tt><b>-</b></tt>) has a special meaning in <i>charclass</i> expressions.
 * An identifier is a string not containing right angle bracket
 * (<tt><b>&gt;</b></tt>) or dash (<tt><b>-</b></tt>).  Numerical intervals are
 * specified by non-negative decimal integers and include both end
 * points, and if <tt><i>n</i></tt> and <tt><i>m</i></tt> have the
 * same number of digits, then the conforming strings must have that
 * length (i.e. prefixed by 0's).
 *
 * @author Anders M&oslash;ller &lt;<a href="mailto:amoeller@brics.dk">amoeller@brics.dk</a>&gt;
 */
public class RegExp {

    enum Kind {
        UNION,
        CONCATENATION,
        INTERSECTION,
        OPTIONAL,
        REPEAT,
        REPEAT_MIN,
        REPEAT_MINMAX,
        COMPLEMENT,
        CHAR,
        CHAR_RANGE,
        ANYCHAR,
        EMPTY,
        STRING,
        ANYSTRING,
        AUTOMATON,
        INTERVAL
    }

    /**
     * Syntax flag, enables intersection (<tt>&amp;</tt>).
     */
    public static final int INTERSECTION = 0x0001;

    /**
     * Syntax flag, enables complement (<tt>~</tt>).
     */
    public static final int COMPLEMENT = 0x0002;

    /**
     * Syntax flag, enables empty language (<tt>#</tt>).
     */
    public static final int EMPTY = 0x0004;

    /**
     * Syntax flag, enables anystring (<tt>@</tt>).
     */
    public static final int ANYSTRING = 0x0008;

    /**
     * Syntax flag, enables named automata (<tt>&lt;</tt>identifier<tt>&gt;</tt>).
     */
    public static final int AUTOMATON = 0x0010;

    /**
     * Syntax flag, enables numerical intervals (<tt>&lt;<i>n</i>-<i>m</i>&gt;</tt>).
     */
    public static final int INTERVAL = 0x0020;

    /**
     * Syntax flag, enables all optional regexp syntax.
     */
    public static final int ALL = 0xffff;

    /**
     * Syntax flag, enables no optional regexp syntax.
     */
    public static final int NONE = 0x0000;

    Kind kind;
    RegExp exp1, exp2;
    String s;
    char c;
    int min, max, digits;
    char from, to;

    StringBuilder b;
    int flags;
    int pos;

    RegExp() {
    }

    /**
     * Constructs new <code>RegExp</code> from a string.
     * Same as <code>RegExp(s, ALL)</code>.
     *
     * @param s regexp string
     * @throws IllegalArgumentException if an error occured while parsing the regular expression
     */
    public RegExp(String s) throws IllegalArgumentException {
        this(s, ALL);
    }

    /**
     * Constructs new <code>RegExp</code> from a string.
     *
     * @param s            regexp string
     * @param syntax_flags boolean 'or' of optional syntax constructs to be enabled
     * @throws IllegalArgumentException if an error occured while parsing the regular expression
     */
    public RegExp(String s, int syntax_flags) throws IllegalArgumentException {
        b = new StringBuilder(s);
        flags = syntax_flags;
        RegExp e = parseUnionExp();
        if (pos < b.length())
            throw new IllegalArgumentException("end-of-string expected at position " + pos);
        kind = e.kind;
        exp1 = e.exp1;
        exp2 = e.exp2;
        this.s = e.s;
        c = e.c;
        min = e.min;
        max = e.max;
        digits = e.digits;
        from = e.from;
        to = e.to;
        b = null;
    }

    /**
     * Constructs new <code>Automaton</code> from this <code>RegExp</code>.
     * Same as <code>toAutomaton(null)</code> (empty automaton map).
     */
    public Automaton toAutomaton() {
        return toAutomaton(null);
    }

    /**
     * Constructs new <code>Automaton</code> from this <code>RegExp</code>.
     * The constructed automaton is minimal and deterministic and has no
     * transitions to dead states.
     *
     * @param automata a map from automaton identifiers to automata
     *                 (of type <code>Automaton</code>).
     * @throws IllegalArgumentException if this regular expression uses
     *                                  a named identifier that does not occur in the automaton map
     */
    public Automaton toAutomaton(Map<String, Automaton> automata) throws IllegalArgumentException {
        List<Automaton> list;
        Automaton a = null;
        switch (kind) {
            case UNION:
                list = new ArrayList<Automaton>();
                findLeaves(exp1, Kind.UNION, list, automata);
                findLeaves(exp2, Kind.UNION, list, automata);
                a = Automaton.union(list);
                a.minimize();
                break;
            case CONCATENATION:
                list = new ArrayList<Automaton>();
                findLeaves(exp1, Kind.CONCATENATION, list, automata);
                findLeaves(exp2, Kind.CONCATENATION, list, automata);
                a = Automaton.concatenate(list);
                a.minimize();
                break;
            case INTERSECTION:
                a = exp1.toAutomaton(automata).intersection(exp2.toAutomaton(automata));
                a.minimize();
                break;
            case OPTIONAL:
                a = exp1.toAutomaton(automata).optional();
                a.minimize();
                break;
            case REPEAT:
                a = exp1.toAutomaton(automata).repeat();
                a.minimize();
                break;
            case REPEAT_MIN:
                a = exp1.toAutomaton(automata).repeat(min);
                a.minimize();
                break;
            case REPEAT_MINMAX:
                a = exp1.toAutomaton(automata).repeat(min, max);
                a.minimize();
                break;
            case COMPLEMENT:
                a = exp1.toAutomaton(automata).complement();
                a.minimize();
                break;
            case CHAR:
                a = Automaton.makeChar(c);
                break;
            case CHAR_RANGE:
                a = Automaton.makeCharRange(from, to);
                break;
            case ANYCHAR:
                a = Automaton.makeAnyChar();
                break;
            case EMPTY:
                a = Automaton.makeEmpty();
                break;
            case STRING:
                a = Automaton.makeString(s);
                break;
            case ANYSTRING:
                a = Automaton.makeAnyString();
                break;
            case AUTOMATON:
                Automaton aa = automata.get(s);
                if (aa == null)
                    throw new IllegalArgumentException(s + " not found");
                a = (Automaton) aa.clone();
                break;
            case INTERVAL:
                a = Automaton.makeInterval(min, max, digits);
                break;
        }
        return a;
    }

    private void findLeaves(RegExp exp, Kind kind, List<Automaton> list, Map<String, Automaton> automata) {
        if (exp.kind == kind) {
            findLeaves(exp.exp1, kind, list, automata);
            findLeaves(exp.exp2, kind, list, automata);
        } else
            list.add(exp.toAutomaton(automata));
    }

    /**
     * Constructs string from parsed regular expression.
     */
    @Override
    public String toString() {
        return toStringBuilder(new StringBuilder()).toString();
    }

    StringBuilder toStringBuilder(StringBuilder b) {
        switch (kind) {
            case UNION:
                b.append("(");
                exp1.toStringBuilder(b);
                b.append("|");
                exp2.toStringBuilder(b);
                b.append(")");
                break;
            case CONCATENATION:
                exp1.toStringBuilder(b);
                exp2.toStringBuilder(b);
                break;
            case INTERSECTION:
                b.append("(");
                exp1.toStringBuilder(b);
                b.append("&");
                exp2.toStringBuilder(b);
                b.append(")");
                break;
            case OPTIONAL:
                b.append("(");
                exp1.toStringBuilder(b);
                b.append(")?");
                break;
            case REPEAT:
                b.append("(");
                exp1.toStringBuilder(b);
                b.append(")*");
                break;
            case REPEAT_MIN:
                b.append("(");
                exp1.toStringBuilder(b);
                b.append("){").append(min).append(",}");
                break;
            case REPEAT_MINMAX:
                b.append("(");
                exp1.toStringBuilder(b);
                b.append("){").append(min).append(",").append(max).append("}");
                break;
            case COMPLEMENT:
                b.append("~(");
                exp1.toStringBuilder(b);
                b.append(")");
                break;
            case CHAR:
                b.append("\\").append(c);
                break;
            case CHAR_RANGE:
                b.append("[\\").append(from).append("-\\").append(to).append("]");
                break;
            case ANYCHAR:
                b.append(".");
                break;
            case EMPTY:
                b.append("#");
                break;
            case STRING:
                b.append("\"").append(s).append("\"");
                break;
            case ANYSTRING:
                b.append("@");
                break;
            case AUTOMATON:
                b.append("<").append(s).append(">");
                break;
            case INTERVAL:
                String s1 = (new Integer(min)).toString();
                String s2 = (new Integer(max)).toString();
                b.append("<");
                if (digits > 0)
                    for (int i = s1.length(); i < digits; i++)
                        b.append('0');
                b.append(s1).append("-");
                if (digits > 0)
                    for (int i = s2.length(); i < digits; i++)
                        b.append('0');
                b.append(s2).append(">");
                break;
        }
        return b;
    }

    /**
     * Returns set of automaton identifiers that occur in this regular expression.
     */
    public Set<String> getIdentifiers() {
        HashSet<String> set = new HashSet<String>();
        getIdentifiers(set);
        return set;
    }

    void getIdentifiers(Set<String> set) {
        switch (kind) {
            case UNION:
            case CONCATENATION:
            case INTERSECTION:
                exp1.getIdentifiers(set);
                exp2.getIdentifiers(set);
                break;
            case OPTIONAL:
            case REPEAT:
            case REPEAT_MIN:
            case REPEAT_MINMAX:
            case COMPLEMENT:
                exp1.getIdentifiers(set);
                break;
            case AUTOMATON:
                set.add(s);
                break;
            default:
        }
    }

    static RegExp makeUnion(RegExp exp1, RegExp exp2) {
        RegExp r = new RegExp();
        r.kind = Kind.UNION;
        r.exp1 = exp1;
        r.exp2 = exp2;
        return r;
    }

    static RegExp makeConcatenation(RegExp exp1, RegExp exp2) {
        if ((exp1.kind == Kind.CHAR || exp1.kind == Kind.STRING) &&
                (exp2.kind == Kind.CHAR || exp2.kind == Kind.STRING))
            return makeString(exp1, exp2);
        RegExp r = new RegExp();
        r.kind = Kind.CONCATENATION;
        if (exp1.kind == Kind.CONCATENATION &&
                (exp1.exp2.kind == Kind.CHAR || exp1.exp2.kind == Kind.STRING) &&
                (exp2.kind == Kind.CHAR || exp2.kind == Kind.STRING)) {
            r.exp1 = exp1.exp1;
            r.exp2 = makeString(exp1.exp2, exp2);
        } else if ((exp1.kind == Kind.CHAR || exp1.kind == Kind.STRING) &&
                exp2.kind == Kind.CONCATENATION &&
                (exp2.exp1.kind == Kind.CHAR || exp2.exp1.kind == Kind.STRING)) {
            r.exp1 = makeString(exp1, exp2.exp1);
            r.exp2 = exp2.exp2;
        } else {
            r.exp1 = exp1;
            r.exp2 = exp2;
        }
        return r;
    }

    static private RegExp makeString(RegExp exp1, RegExp exp2) {
        StringBuilder b = new StringBuilder();
        if (exp1.kind == Kind.STRING)
            b.append(exp1.s);
        else
            b.append(exp1.c);
        if (exp2.kind == Kind.STRING)
            b.append(exp2.s);
        else
            b.append(exp2.c);
        return makeString(b.toString());
    }

    static RegExp makeIntersection(RegExp exp1, RegExp exp2) {
        RegExp r = new RegExp();
        r.kind = Kind.INTERSECTION;
        r.exp1 = exp1;
        r.exp2 = exp2;
        return r;
    }

    static RegExp makeOptional(RegExp exp) {
        RegExp r = new RegExp();
        r.kind = Kind.OPTIONAL;
        r.exp1 = exp;
        return r;
    }

    static RegExp makeRepeat(RegExp exp) {
        RegExp r = new RegExp();
        r.kind = Kind.REPEAT;
        r.exp1 = exp;
        return r;
    }

    static RegExp makeRepeat(RegExp exp, int min) {
        RegExp r = new RegExp();
        r.kind = Kind.REPEAT_MIN;
        r.exp1 = exp;
        r.min = min;
        return r;
    }

    static RegExp makeRepeat(RegExp exp, int min, int max) {
        RegExp r = new RegExp();
        r.kind = Kind.REPEAT_MINMAX;
        r.exp1 = exp;
        r.min = min;
        r.max = max;
        return r;
    }

    static RegExp makeComplement(RegExp exp) {
        RegExp r = new RegExp();
        r.kind = Kind.COMPLEMENT;
        r.exp1 = exp;
        return r;
    }

    static RegExp makeChar(char c) {
        RegExp r = new RegExp();
        r.kind = Kind.CHAR;
        r.c = c;
        return r;
    }

    static RegExp makeCharRange(char from, char to) {
        RegExp r = new RegExp();
        r.kind = Kind.CHAR_RANGE;
        r.from = from;
        r.to = to;
        return r;
    }

    static RegExp makeAnyChar() {
        RegExp r = new RegExp();
        r.kind = Kind.ANYCHAR;
        return r;
    }

    static RegExp makeEmpty() {
        RegExp r = new RegExp();
        r.kind = Kind.EMPTY;
        return r;
    }

    static RegExp makeString(String s) {
        RegExp r = new RegExp();
        r.kind = Kind.STRING;
        r.s = s;
        return r;
    }

    static RegExp makeAnyString() {
        RegExp r = new RegExp();
        r.kind = Kind.ANYSTRING;
        return r;
    }

    static RegExp makeAutomaton(String s) {
        RegExp r = new RegExp();
        r.kind = Kind.AUTOMATON;
        r.s = s;
        return r;
    }

    static RegExp makeInterval(int min, int max, int digits) {
        RegExp r = new RegExp();
        r.kind = Kind.INTERVAL;
        r.min = min;
        r.max = max;
        r.digits = digits;
        return r;
    }

    private boolean peek(String s) {
        return more() && s.indexOf(b.charAt(pos)) != -1;
    }

    private boolean match(char c) {
        if (pos >= b.length())
            return false;
        if (b.charAt(pos) == c) {
            pos++;
            return true;
        }
        return false;
    }

    private boolean more() {
        return pos < b.length();
    }

    private char next() throws IllegalArgumentException {
        if (!more())
            throw new IllegalArgumentException("unexpected end-of-string");
        return b.charAt(pos++);
    }

    private boolean check(int flag) {
        return (flags & flag) != 0;
    }

    RegExp parseUnionExp() throws IllegalArgumentException {
        RegExp e = parseInterExp();
        if (match('|'))
            e = makeUnion(e, parseUnionExp());
        return e;
    }

    RegExp parseInterExp() throws IllegalArgumentException {
        RegExp e = parseConcatExp();
        if (check(INTERSECTION) && match('&'))
            e = makeIntersection(e, parseInterExp());
        return e;
    }

    RegExp parseConcatExp() throws IllegalArgumentException {
        RegExp e = parseRepeatExp();
        if (more() && !peek(")&|"))
            e = makeConcatenation(e, parseConcatExp());
        return e;
    }

    RegExp parseRepeatExp() throws IllegalArgumentException {
        RegExp e = parseComplExp();
        while (peek("?*+{")) {
            if (match('?'))
                e = makeOptional(e);
            else if (match('*'))
                e = makeRepeat(e);
            else if (match('+'))
                e = makeRepeat(e, 1);
            else if (match('{')) {
                int start = pos;
                while (peek("0123456789"))
                    next();
                if (start == pos)
                    throw new IllegalArgumentException("integer expected at position " + pos);
                int n = Integer.parseInt(b.substring(start, pos));
                int m = -1;
                if (match(',')) {
                    start = pos;
                    while (peek("0123456789"))
                        next();
                    if (start != pos)
                        m = Integer.parseInt(b.substring(start, pos));
                } else
                    m = n;
                if (!match('}'))
                    throw new IllegalArgumentException("expected '}' at position " + pos);
                if (m == -1)
                    return makeRepeat(e, n);
                else
                    return makeRepeat(e, n, m);
            }
        }
        return e;
    }

    RegExp parseComplExp() throws IllegalArgumentException {
        if (check(COMPLEMENT) && match('~'))
            return makeComplement(parseComplExp());
        else
            return parseCharClassExp();
    }

    RegExp parseCharClassExp() throws IllegalArgumentException {
        if (match('[')) {
            boolean negate = false;
            if (match('^'))
                negate = true;
            RegExp e = parseCharClasses();
            if (negate)
                e = makeIntersection(makeAnyChar(), makeComplement(e));
            if (!match(']'))
                throw new IllegalArgumentException("expected ']' at position " + pos);
            return e;
        } else
            return parseSimpleExp();
    }

    RegExp parseCharClasses() throws IllegalArgumentException {
        RegExp e = parseCharClass();
        while (more() && !peek("]"))
            e = makeUnion(e, parseCharClass());
        return e;
    }

    RegExp parseCharClass() throws IllegalArgumentException {
        char c = parseCharExp();
        if (match('-'))
            return makeCharRange(c, parseCharExp());
        else
            return makeChar(c);
    }

    RegExp parseSimpleExp() throws IllegalArgumentException {
        if (match('.'))
            return makeAnyChar();
        else if (check(EMPTY) && match('#'))
            return makeEmpty();
        else if (check(ANYSTRING) && match('@'))
            return makeAnyString();
        else if (match('"')) {
            int start = pos;
            while (more() && !peek("\""))
                next();
            if (!match('"'))
                throw new IllegalArgumentException("expected '\"' at position " + pos);
            return makeString(b.substring(start, pos - 1));
        } else if (match('(')) {
            if (match(')'))
                return makeString("");
            RegExp e = parseUnionExp();
            if (!match(')'))
                throw new IllegalArgumentException("expected ')' at position " + pos);
            return e;
        } else if ((check(AUTOMATON) || check(INTERVAL)) && match('<')) {
            int start = pos;
            while (more() && !peek(">"))
                next();
            if (!match('>'))
                throw new IllegalArgumentException("expected '>' at position " + pos);
            String s = b.substring(start, pos - 1);
            int i = s.indexOf('-');
            if (i == -1) {
                if (!check(AUTOMATON))
                    throw new IllegalArgumentException("interval syntax error at position " + (pos - 1));
                return makeAutomaton(s);
            } else {
                if (!check(INTERVAL))
                    throw new IllegalArgumentException("illegal identifier at position " + (pos - 1));
                try {
                    if (i == 0 || i == s.length() - 1 || i != s.lastIndexOf('-'))
                        throw new NumberFormatException();
                    String smin = s.substring(0, i);
                    String smax = s.substring(i + 1, s.length());
                    int imin = Integer.parseInt(smin);
                    int imax = Integer.parseInt(smax);
                    int digits;
                    if (smin.length() == smax.length())
                        digits = smin.length();
                    else
                        digits = 0;
                    if (imin > imax) {
                        int t = imin;
                        imin = imax;
                        imax = t;
                    }
                    return makeInterval(imin, imax, digits);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("interval syntax error at position " + (pos - 1));
                }
            }
        } else
            return makeChar(parseCharExp());
    }

    char parseCharExp() throws IllegalArgumentException {
        match('\\');
        return next();
    }
}
