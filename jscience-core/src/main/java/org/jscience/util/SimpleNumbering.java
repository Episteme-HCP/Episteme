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
 */
package org.jscience.util;

/**
 * A simpler Numbering implementation for legacy support.
 */
public class SimpleNumbering extends Numbering {
    private static final long serialVersionUID = 1L;

    public SimpleNumbering(String s) {
        super(parse(s).getValues());
    }

    public SimpleNumbering(int... values) {
        super(values);
    }
}
