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
package org.jscience.util;

import java.util.*;

/**
 * A minimal JSON parser to avoid external dependencies for simple resource
 * loading.
 * Supports objects {}, arrays [], strings "", and numbers. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class SimpleJson {

    public static Object parse(String json) {
        return new Parser(json).parseValue();
    }

    private static class Parser {
        private final String src;
        private int pos;

        public Parser(String src) {
            this.src = src;
            this.pos = 0;
            skipWhitespace();
        }

        private void skipWhitespace() {
            while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) {
                pos++;
            }
        }

        public Object parseValue() {
            skipWhitespace();
            if (pos >= src.length())
                return null;
            char c = src.charAt(pos);
            if (c == '{')
                return parseObject();
            if (c == '[')
                return parseArray();
            if (c == '"')
                return parseString();
            if (Character.isDigit(c) || c == '-')
                return parseNumber();
            if (src.startsWith("true", pos)) {
                pos += 4;
                return true;
            }
            if (src.startsWith("false", pos)) {
                pos += 5;
                return false;
            }
            if (src.startsWith("null", pos)) {
                pos += 4;
                return null;
            }
            throw new RuntimeException("Unexpected character at " + pos + ": " + c);
        }

        private Map<String, Object> parseObject() {
            Map<String, Object> map = new HashMap<>();
            pos++; // skip '{'
            skipWhitespace();
            while (pos < src.length() && src.charAt(pos) != '}') {
                String key = parseString();
                skipWhitespace();
                if (src.charAt(pos) != ':')
                    throw new RuntimeException("Expected ':' at " + pos);
                pos++; // skip ':'
                Object value = parseValue();
                map.put(key, value);
                skipWhitespace();
                if (src.charAt(pos) == ',') {
                    pos++;
                    skipWhitespace();
                }
            }
            pos++; // skip '}'
            return map;
        }

        private List<Object> parseArray() {
            List<Object> list = new ArrayList<>();
            pos++; // skip '['
            skipWhitespace();
            while (pos < src.length() && src.charAt(pos) != ']') {
                list.add(parseValue());
                skipWhitespace();
                if (src.charAt(pos) == ',') {
                    pos++;
                    skipWhitespace();
                }
            }
            pos++; // skip ']'
            return list;
        }

        private String parseString() {
            StringBuilder sb = new StringBuilder();
            pos++; // skip start quote
            while (pos < src.length()) {
                char c = src.charAt(pos++);
                if (c == '"') {
                    return sb.toString();
                }
                if (c == '\\') {
                    // Simple escape handling
                    if (pos < src.length()) {
                        sb.append(src.charAt(pos++));
                    }
                } else {
                    sb.append(c);
                }
            }
            throw new RuntimeException("Unterminated string");
        }

        private Number parseNumber() {
            int start = pos;
            if (src.charAt(pos) == '-')
                pos++;
            while (pos < src.length() && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.')) {
                pos++;
            }
            String num = src.substring(start, pos);
            if (num.contains("."))
                return Double.parseDouble(num);
            return Integer.parseInt(num);
        }
    }
}