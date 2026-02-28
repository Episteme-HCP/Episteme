/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.politics;


import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;

/**
 * An extensible enumeration for voting methods and election algorithms.
 * Supporting standard social choice methods and dynamic custom algorithms.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class VotingMethod extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<VotingMethod> REGISTRY = EnumRegistry.getRegistry(VotingMethod.class);

    // ===== PLURALITY/MAJORITY METHODS =====
    public static final VotingMethod FIRST_PAST_THE_POST = new VotingMethod("FIRST_PAST_THE_POST", true);
    public static final VotingMethod TWO_ROUND = new VotingMethod("TWO_ROUND", true);
    public static final VotingMethod ANTI_PLURALITY = new VotingMethod("ANTI_PLURALITY", true);

    // ===== RANKED CHOICE METHODS =====
    public static final VotingMethod INSTANT_RUNOFF = new VotingMethod("INSTANT_RUNOFF", true);
    public static final VotingMethod CONDORCET = new VotingMethod("CONDORCET", true);
    public static final VotingMethod BORDA = new VotingMethod("BORDA", true);
    public static final VotingMethod SCHULZE = new VotingMethod("SCHULZE", true);
    public static final VotingMethod COPELAND = new VotingMethod("COPELAND", true);
    public static final VotingMethod RANKED_PAIRS = new VotingMethod("RANKED_PAIRS", true);
    public static final VotingMethod MINIMAX = new VotingMethod("MINIMAX", true);
    public static final VotingMethod KEMENY_YOUNG = new VotingMethod("KEMENY_YOUNG", true);
    public static final VotingMethod DODGSON = new VotingMethod("DODGSON", true);
    public static final VotingMethod BUCKLIN = new VotingMethod("BUCKLIN", true);
    public static final VotingMethod COOMBS = new VotingMethod("COOMBS", true);
    public static final VotingMethod STV = new VotingMethod("STV", true);

    // ===== CARDINAL/RATING METHODS =====
    public static final VotingMethod APPROVAL = new VotingMethod("APPROVAL", true);
    public static final VotingMethod RANGE = new VotingMethod("RANGE", true);
    public static final VotingMethod STAR = new VotingMethod("STAR", true);
    public static final VotingMethod MAJORITY_JUDGMENT = new VotingMethod("MAJORITY_JUDGMENT", true);

    // ===== PROPORTIONAL REPRESENTATION =====
    public static final VotingMethod PROPORTIONAL = new VotingMethod("PROPORTIONAL", true);
    public static final VotingMethod DHONDT = new VotingMethod("DHONDT", true);
    public static final VotingMethod SAINTE_LAGUE = new VotingMethod("SAINTE_LAGUE", true);
    public static final VotingMethod MODIFIED_SAINTE_LAGUE = new VotingMethod("MODIFIED_SAINTE_LAGUE", true);
    public static final VotingMethod HUNTINGTON_HILL = new VotingMethod("HUNTINGTON_HILL", true);
    public static final VotingMethod LARGEST_REMAINDER_HARE = new VotingMethod("LARGEST_REMAINDER_HARE", true);
    public static final VotingMethod LARGEST_REMAINDER_DROOP = new VotingMethod("LARGEST_REMAINDER_DROOP", true);

    // ===== MULTI-WINNER/OTHER =====
    public static final VotingMethod SNTV = new VotingMethod("SNTV", true);
    public static final VotingMethod CUMULATIVE = new VotingMethod("CUMULATIVE", true);
    
    public static final VotingMethod OTHER = new VotingMethod("OTHER", true);
    public static final VotingMethod UNKNOWN = new VotingMethod("UNKNOWN", true);

    private final boolean builtIn;

    private VotingMethod(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    public static VotingMethod valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static VotingMethod valueOf(int ordinal) {
        return REGISTRY.valueOf(ordinal);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
}

